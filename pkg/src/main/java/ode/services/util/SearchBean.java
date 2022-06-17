package ode.services;

// Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ode.annotations.RolesAllowed;
import ode.api.Search;
import ode.api.ServiceInterface;
import ode.conditions.ApiUsageException;
import ode.conditions.InternalException;
import ode.conditions.OverUsageException;
import ode.logic.QueryImpl;
import ode.model.IObject;
import ode.model.annotations.Annotation;
import ode.model.internal.Details;
import ode.parameters.Parameters;
import ode.services.search.AnnotatedWith;
import ode.services.search.Complement;
import ode.services.search.FullText;
import ode.services.search.HqlQuery;
import ode.services.search.Intersection;
import ode.services.search.SearchAction;
import ode.services.search.SearchValues;
import ode.services.search.SimilarTerms;
import ode.services.search.SomeMustNone;
import ode.services.search.TagsAndGroups;
import ode.services.search.Union;
import ode.services.util.Executor;
import ode.services.util.TimeoutSetter;
import ode.system.SelfConfigurableService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.lucene.analysis.Analyzer;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implements the {@link Search} interface.
 */
@Transactional(readOnly = true)
public class SearchBean extends AbstractStatefulBean implements Search {

    private final static long serialVersionUID = 59809384038000069L;

    /** The logger for this class. */
    private final static Logger log = LoggerFactory.getLogger(SearchBean.class);

    private final ActionList actions = new ActionList();

    private final SearchValues values = new SearchValues();

    private final List<List<IObject>> results = new ArrayList<List<IObject>>();

    private/* final */transient Executor executor;

    private transient TimeoutSetter timeoutSetter;

    private/* final */transient Class<? extends Analyzer> analyzer;

    public SearchBean(Executor executor, Class<? extends Analyzer> analyzer) {
        this.executor = executor;
        this.analyzer = analyzer;
    }

    public Class<? extends ServiceInterface> getServiceInterface() {
        return Search.class;
    }

    /**
     * Empty constructor required by EJB and
     * {@link SelfConfigurableService self configuration}.
     */
    public SearchBean() {

    }

    /**
     * Injector used by Spring, currently, since
     * {@link SelfConfigurableService#selfConfigure()} requires it.
     */
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public void setTimeoutSetter(TimeoutSetter timeoutSetter) {
        getBeanHelper().throwIfAlreadySet(this.timeoutSetter, timeoutSetter);
        this.timeoutSetter = timeoutSetter;
    }

    /**
     * Injector used by Spring.
     */
    public void setAnalyzer(Class<? extends Analyzer> analyzer) {
        this.analyzer = analyzer;
    }

    /**
     * Injector used by Spring.
     */
    @Deprecated
    public void setMaxClauseCount(Integer maxClauseCount) {
        /* ignored */
    }

    // Lifecycle methods
    // ===================================================

    // See documentation on JobBean#passivate
    @RolesAllowed("user")
    @Transactional(readOnly = true)    
    public void passivate() {
	// All state is passivatable.
    }

    // See documentation on JobBean#activate
    @RolesAllowed("user")
    @Transactional(readOnly = true)    
    public void activate() {
	// State needs to be read back with synchronization.
    }

    @RolesAllowed("user")
    @Transactional(readOnly = true)    
    public void close() {
	// Could null state.
    }

    // Interface methods ~
    // ============================================

    //
    // CREATE METHODS
    //

    @Transactional
    @RolesAllowed("user")
    public void byAnnotatedWith(Annotation... examples) {
        SearchAction byAnnotatedWith;
        synchronized (values) {
            byAnnotatedWith = new AnnotatedWith(values, examples, false, false);
        }
        actions.add(byAnnotatedWith);
    }

    @Transactional
    @RolesAllowed("user")
    public void byFullText(String query) {
        SearchAction byFullText;
        synchronized (values) {
            byFullText = new FullText(values, query, analyzer);
        }
        actions.add(byFullText);

    }
    
    @Transactional
    @RolesAllowed("user")
    public void byLuceneQueryBuilder(String fields, String from,
            String to, String dateType, String query) {
        SearchAction byFullText;
        synchronized (values) {
            byFullText = new FullText(values, fields, from,
                    to, dateType, query, analyzer);
        }
        actions.add(byFullText);
    }
    

    @Transactional
    @RolesAllowed("user")
    public void byHqlQuery(String query, Parameters p) {
        SearchAction byHqlQuery;
        synchronized (values) {
            byHqlQuery = new HqlQuery(values, query, p);
        }
        actions.add(byHqlQuery);
    }

    @Transactional
    @RolesAllowed("user")
    public void bySomeMustNone(String[] some, String[] must, String[] none) {
        SearchAction bySomeMustNone;
        synchronized (values) {
            bySomeMustNone = new SomeMustNone(values, some, must, none,
                    analyzer);
        }
        actions.add(bySomeMustNone);
    }


    @Transactional
    @RolesAllowed("user")
    public void bySimilarTerms(String...terms) {
        SearchAction bySimilarTerms;
        synchronized (values) {
            bySimilarTerms = new SimilarTerms(values, terms);
        }
        actions.add(bySimilarTerms);
    }
    
    @Transactional
    @RolesAllowed("user")
    public void byGroupForTags(String group) {
        SearchAction byTags;
        synchronized (values) {
            byTags = new TagsAndGroups(values, group, false);
        }
        actions.add(byTags);
    }

    @Transactional
    @RolesAllowed("user")
    public void byTagForGroups(String tag) {
        SearchAction byTags;
        synchronized (values) {
            byTags = new TagsAndGroups(values, tag, true);
        }
        actions.add(byTags);
    }

    @Transactional
    @RolesAllowed("user")
    public void byUUID(String[] uuids) {
        throw new UnsupportedOperationException();
    }

    // LOGICAL COMBINATIONS

    @Transactional
    @RolesAllowed("user")
    public void or() {
        actions.union();
    }

    @Transactional
    @RolesAllowed("user")
    public void and() {
        actions.intersection();
    }

    @Transactional
    @RolesAllowed("user")
    public void not() {
        actions.complement();
    }

    //
    // FETCH METHODS
    //

    @Transactional
    @RolesAllowed("user")
    public boolean hasNext() {

        while (results.size() > 0) {
            List<IObject> first = results.get(0);
            if (first == null || first.size() < 1) {
                results.remove(0);
            } else {
                return true;
            }
        }

        // There are no current results, we now need to execute an action
        if (actions.size() == 0) {
            return false;
        }
        SearchAction action = actions.popFirst();
        timeoutSetter.setTimeout(action::setTimeout);
        final List<IObject> list;
        try {
            list = (List<IObject>) executor.execute(null, action);
        } catch (DataAccessResourceFailureException e) {
            if (QueryImpl.isProbablyTimeout(e)) {
                throw new OverUsageException("query failed, probable timeout");
            } else {
                throw e;
            }
        }
        results.add(list);
        return hasNext(); // recursive call
    }

    @Transactional
    @RolesAllowed("user")
    public IObject next() throws ApiUsageException {

        if (!hasNext()) {
            throw new ApiUsageException("No element. Please use hasNext().");
        }

        // Now we're guaranteed to have an element
        return pop(results.get(0));
    }

    @Transactional
    @RolesAllowed("user")
    public Map<String, Annotation> currentMetadata() {
        throw new UnsupportedOperationException();
    }

    @Transactional
    @RolesAllowed("user")
    public List<Map<String, Annotation>> currentMetadataList() {
        throw new UnsupportedOperationException();
    }

    @Transactional
    @RolesAllowed("user")
    public <T extends IObject> List<T> results() {

        if (!hasNext()) {
            throw new ApiUsageException("No elements. Please use hasNext().");
        }

        // Now we're guaranteed to have an element
        List<T> rv = new ArrayList<T>();
        while (hasNext() && rv.size() < values.batchSize) {
            List<IObject> current = results.get(0);
            if (current.size() > 0) {
                rv.add((T) pop(current));
            } else {
                // If batches aren't merged, we can exit now.
                if (!values.mergedBatches) {
                    break;
                }
            }
        }
        return rv;
    }

    /**
     * Wrapper method which should be called on all results for the user.
     * Removes the value from the last list, and applies all requirements of
     * {@link #values}.
     */
    protected IObject pop(List<IObject> current) {
        IObject obj = current.remove(0);
        if (values.returnUnloaded) {
            obj.unload();
        }
        return obj;
    }

    @Transactional
    @RolesAllowed("user")
    public void lastresultsAsWorkingGroup() {
        throw new UnsupportedOperationException();
    }

    @Transactional
    @RolesAllowed("user")
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "Cannot remove via ode.api.Search");
    }

    //
    // QUERY MANAGEMENT
    // 

    @Transactional
    @RolesAllowed("user")
    public int activeQueries() {
        return actions.size();
    }

    @Transactional
    @RolesAllowed("user")
    public void clearQueries() {
        actions.clear();
    }

    //
    // TEMPLATE STATE
    //

    @Transactional
    @RolesAllowed("user")
    public void resetDefaults() {
        synchronized (values) {
            values.copy(new SearchValues());
        }
    }

    @Transactional
    @RolesAllowed("user")
    public void addOrderByAsc(String path) {
        synchronized (values) {
            values.orderBy.add("A" + path);
        }
    }

    @Transactional
    @RolesAllowed("user")
    public void addOrderByDesc(String path) {
        synchronized (values) {
            values.orderBy.add("D" + path);
        }
    }

    @Transactional
    @RolesAllowed("user")
    public void unordered() {
        synchronized (values) {
            values.orderBy.clear();
        }
    }

    @Transactional
    @RolesAllowed("user")
    public <T extends IObject> void fetchAlso(Map<T, String> fetches) {
        synchronized (values) {
            throw new UnsupportedOperationException();
        }
    }

    @Transactional
    @RolesAllowed("user")
    public void fetchAnnotations(Class... classes) {
        synchronized (values) {
            values.fetchAnnotations = new ArrayList();
            for (Class k : classes) {
                values.fetchAnnotations.add(k);
            }
        }
    }

    @Transactional
    @RolesAllowed("user")
    public int getBatchSize() {
        synchronized (values) {
            return values.batchSize;
        }
    }

    @Transactional
    @RolesAllowed("user")
    public boolean isCaseSensitive() {
        synchronized (values) {
            return values.caseSensitive;
        }
    }

    @Transactional
    @RolesAllowed("user")
    public boolean isMergedBatches() {
        synchronized (values) {
            return values.mergedBatches;
        }

    }

    @Transactional
    @RolesAllowed("user")
    public void onlyAnnotatedBetween(Timestamp start, Timestamp stop) {
        synchronized (values) {
            values.annotatedStart = SearchValues.copyTimestamp(start);
            values.annotatedStop = SearchValues.copyTimestamp(stop);
        }
    }

    @Transactional
    @RolesAllowed("user")
    public void onlyAnnotatedBy(Details d) {
        synchronized (values) {
            values.annotatedBy = SearchValues.copyDetails(d);
        }
    }

    @Transactional
    @RolesAllowed("user")
    public void notAnnotatedBy(Details d) {
        synchronized (values) {
            values.notAnnotatedBy = SearchValues.copyDetails(d);
        }
    }

    @Transactional
    @RolesAllowed("user")
    public void onlyAnnotatedWith(Class... classes) {
        synchronized (values) {
            if (classes == null) {
                values.onlyAnnotatedWith = null;
            } else {
                List<Class> list = Arrays.<Class> asList(classes);
                values.onlyAnnotatedWith = SearchValues.copyList(list);
            }
        }
    }

    @Transactional
    @RolesAllowed("user")
    public void onlyCreatedBetween(Timestamp start, Timestamp stop) {
        synchronized (values) {
            values.createdStart = SearchValues.copyTimestamp(start);
            values.createdStop = SearchValues.copyTimestamp(stop);
            if (start != null && stop != null) {
                if (stop.getTime() < start.getTime()) {
                    log.warn("FullText search created with "
                            + "creation stop before start");
                }
            }
        }
    }

    @Transactional
    @RolesAllowed("user")
    public void onlyOwnedBy(Details d) {
        synchronized (values) {
            values.ownedBy = SearchValues.copyDetails(d);
        }
    }

    @Transactional
    @RolesAllowed("user")
    public void onlyIds(Long... ids) {
        synchronized (values) {
            if (ids == null) {
                values.onlyIds = null;
            } else {
                values.onlyIds = Arrays.asList(ids);
            }
        }
    }

    @Transactional
    @RolesAllowed("user")
    public void notOwnedBy(Details d) {
        synchronized (values) {
            values.notOwnedBy = SearchValues.copyDetails(d);
        }
    }

    @Transactional
    @RolesAllowed("user")
    public void allTypes() {
        throw new UnsupportedOperationException();
    }

    @Transactional
    @RolesAllowed("user")
    @SuppressWarnings("all")
    public <T extends IObject> void onlyType(Class<T> klass) {
        onlyTypes(klass);
    }

    @Transactional
    @RolesAllowed("user")
    @SuppressWarnings("unchecked")
    public <T extends IObject> void onlyTypes(Class<T>... classes) {
        synchronized (values) {
            values.onlyTypes = new ArrayList();
            for (Class<T> k : classes) {
                values.onlyTypes.add(k);
            }
        }
    }

    @Transactional
    @RolesAllowed("user")
    @SuppressWarnings("unchecked")
    public void setAllowLeadingWildcard(boolean allowLeadingWildcard) {
        synchronized (values) {
            values.leadingWildcard = allowLeadingWildcard;
        }
    }

    @Transactional
    @RolesAllowed("user")
    public void setBatchSize(int size) {
        synchronized (values) {
            values.batchSize = size;
        }
    }

    @Transactional
    @RolesAllowed("user")
    public void setIdOnly() {
        synchronized (values) {
            values.idOnly = true;
        }
    }

    @Transactional
    @RolesAllowed("user")
    public void setMergedBatches(boolean merge) {
        synchronized (values) {
            values.mergedBatches = merge;
        }
    }

    @Transactional
    @RolesAllowed("user")
    public void fetchAlso(String... fetches) {
        synchronized (values) {
            values.fetches = Arrays.asList(fetches);
        }
    }

    @Transactional
    @RolesAllowed("user")
    public boolean isAllowLeadingWildcard() {
        synchronized (values) {
            return values.leadingWildcard;
        }
    }

    @Transactional
    @RolesAllowed("user")
    public boolean isReturnUnloaded() {
        synchronized (values) {
            return values.returnUnloaded;
        }
    }

    @Transactional
    @RolesAllowed("user")
    public boolean isUseProjections() {
        synchronized (values) {
            return values.useProjections;
        }
    }

    @Transactional
    @RolesAllowed("user")
    public void onlyModifiedBetween(Timestamp start, Timestamp stop) {
        synchronized (values) {
            values.modifiedStart = SearchValues.copyTimestamp(start);
            values.modifiedStop = SearchValues.copyTimestamp(stop);
            if (start != null && stop != null) {
                if (stop.getTime() < start.getTime()) {
                    log.warn("FullText search created "
                            + "with modification stop before start");
                }
            }
        }
    }

    @Transactional
    @RolesAllowed("user")
    public void setCaseSentivice(boolean caseSensitive) {
        setCaseSensitive(caseSensitive);
    }

    @Transactional
    @RolesAllowed("user")
    public void setCaseSensitive(boolean caseSensitive) {
        synchronized (values) {
            values.caseSensitive = caseSensitive;
        }
    }

    @Transactional
    @RolesAllowed("user")
    public void setReturnUnloaded(boolean returnUnloaded) {
        synchronized (values) {
            values.returnUnloaded = returnUnloaded;
        }
    }

    @Transactional
    @RolesAllowed("user")
    public void setUseProjections(boolean useProjections) {
        throw new UnsupportedOperationException();
        // Before activating, please test heavily.
        // In fact, this may need to be removed,
        // since much of the security in Lucene
        // is based on the db.
        // synchronized (values) {
        // values.useProjections = useProjections;
        // }
    }

    //
    // LOCAL API (mostly for testing)
    //

    public void addAction(SearchAction action) {
        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null");
        }
        synchronized (actions) {
            actions.add(action);
        }
    }

    public void addResult(List<IObject> result) {
        synchronized (results) {
            results.add(result); // Can be null as flag?
        }
    }

    public void addParameters(Parameters params) {
        synchronized (values) {
            values.copy(params);
        }
    }

    /**
     * Synchronized helper collection for maintaining {@link SearchAction}
     * instances. Also knows how to do logical joins (union, etc.)
     */
    private static class ActionList implements Serializable {

        private static final long serialVersionUID = 1L;

        enum State {
            normal, union, intersection, complement;
        }

        private State state = State.normal;

        final private List<SearchAction> actions = new ArrayList<SearchAction>();

        synchronized void union() {
            state = State.union;
        }

        synchronized void intersection() {
            state = State.intersection;
        }

        synchronized void complement() {
            state = State.complement;
        }

        synchronized void add(SearchAction b) {

            // Any call to "add" reset the state of the ActionList
            State previousState = state;
            this.state = State.normal;

            SearchAction a;
            switch (previousState) {
            case normal:
                actions.add(b);
                break;
            case union:
                a = popLast();
                actions.add(new Union(b.copyOfValues(), a, b));
                break;
            case intersection:
                a = popLast();
                actions.add(new Intersection(b.copyOfValues(), a, b));
                break;
            case complement:
                a = popLast();
                actions.add(new Complement(b.copyOfValues(), a, b));
                break;
            default:
                throw new InternalException("Unknown state:" + state);
            }
        }

        synchronized int size() {
            return actions.size();
        }

        synchronized void clear() {
            actions.clear();
        }

        synchronized SearchAction popFirst() {
            assertNonZero();
            return actions.remove(0);
        }

        synchronized SearchAction popLast() {
            assertNonZero();
            return actions.remove(actions.size() - 1);
        }

        synchronized void assertNonZero() {
            if (actions.size() == 0) {
                throw new ApiUsageException("There must be at least 1"
                        + " active query for this operation.");
            }
        }
    }
}
