package ode.logic;

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

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import ode.annotations.RolesAllowed;
import ode.api.IQuery;
import ode.api.ServiceInterface;
import ode.api.local.LocalQuery;
import ode.conditions.ApiUsageException;
import ode.conditions.OverUsageException;
import ode.conditions.ValidationException;
import ode.model.IObject;
import ode.parameters.Filter;
import ode.parameters.Parameters;
import ode.services.SearchBean;
import ode.services.query.Query;
import ode.services.search.FullText;
import ode.services.search.SearchValues;
import ode.services.util.TimeoutSetter;
import ode.tools.hibernate.QueryBuilder;

import org.apache.commons.collections.CollectionUtils;
import org.apache.lucene.analysis.Analyzer;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Provides methods for directly querying object graphs.
 */
@Transactional(readOnly = true)
public class QueryImpl extends AbstractLevel1Service implements LocalQuery {

    protected Class<? extends Analyzer> analyzer;

    protected TimeoutSetter timeoutSetter;

    public void setAnalyzer(Class<? extends Analyzer> analyzer) {
        getBeanHelper().throwIfAlreadySet(this.analyzer, analyzer);
        this.analyzer = analyzer;
    }

    public void setTimeoutSetter(TimeoutSetter timeoutSetter) {
        getBeanHelper().throwIfAlreadySet(this.timeoutSetter, timeoutSetter);
        this.timeoutSetter = timeoutSetter;
    }

    public Class<? extends ServiceInterface> getServiceInterface() {
        return IQuery.class;
    }

    /**
     * Temporary WORKAROUND during the removal of HibernateTemplate to
     * let IQuery continue functioning.
     */
    private HibernateTemplate getHibernateTemplate() {
        return new HibernateTemplate(getSessionFactory(), false);
    }

    // ~ LOCAL PUBLIC METHODS
    // =========================================================================

    @RolesAllowed("user")
    @Transactional(readOnly = false)
    public boolean contains(Object obj) {
        return getHibernateTemplate().contains(obj);
    }

    @RolesAllowed("user")
    @Transactional(readOnly = false)
    public void evict(Object obj) {
        getHibernateTemplate().evict(obj);
    }

    @RolesAllowed("user")
    @Transactional(readOnly = false)
    public void clear() {
        getHibernateTemplate().clear();
    }

    @RolesAllowed("user")
    public void initialize(Object obj) {
        Hibernate.initialize(obj);
    }

    @RolesAllowed("user")
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean checkType(String type) {
        ClassMetadata meta = getHibernateTemplate().getSessionFactory()
                .getClassMetadata(type);
        return meta == null ? false : true;
    }

    @RolesAllowed("user")
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean checkProperty(String type, String property) {
        ClassMetadata meta = getHibernateTemplate().getSessionFactory()
                .getClassMetadata(type);
        String[] names = meta.getPropertyNames();
        for (int i = 0; i < names.length; i++) {
            // TODO: possibly with caching and Arrays.sort/search
            if (names[i].equals(property)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param exception a wrapped query exception
     * @return if the exception probably was caused by a timeout
     */
    public static boolean isProbablyTimeout(DataAccessResourceFailureException exception) {
        if (exception.getCause() instanceof SQLException) {
            final SQLException cause = (SQLException) exception.getCause();
            final String message = cause.getMessage();
            return message != null && message.endsWith(" user request");
        }
        return false;
    }

    /**
     * @see LocalQuery#execute(HibernateCallback)
     */
    @RolesAllowed("user")
    @SuppressWarnings("unchecked")
    public <T> T execute(HibernateCallback callback) {
        try {
            return (T) getHibernateTemplate().execute(callback);
        } catch (DataAccessResourceFailureException e) {
            if (isProbablyTimeout(e)) {
                throw new OverUsageException("query failed, probable timeout");
            } else {
                throw e;
            }
        }
    }

    /**
     * @see ode.api.local.LocalQuery#execute(Query)
     */
    @RolesAllowed("user")
    @SuppressWarnings("unchecked")
    public <T> T execute(ode.services.query.Query<T> query) {
        try {
            return (T) getHibernateTemplate().execute(query);
        } catch (DataAccessResourceFailureException e) {
            if (isProbablyTimeout(e)) {
                throw new OverUsageException("query failed, probable timeout");
            } else {
                throw e;
            }
        }
    }

    // ~ INTERFACE METHODS
    // =========================================================================

    @Override
    @RolesAllowed("user")
    @SuppressWarnings("unchecked")
    // TODO weirdness here; learn more about CGLIB initialization.
    public IObject get(final Class klass, final long id)
            throws ValidationException {
        return (IObject) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException {

                        IObject o = null;
                        try {
                            o = (IObject) session.load(klass, id);
                        } catch (ObjectNotFoundException onfe) {
                            throw new ApiUsageException(
                                    String
                                            .format(
                                                    "The requested object (%s,%s) is not available.\n"
                                                            + "Please use IQuery.find to determine existence.\n",
                                                    klass.getName(), id));
                        }

                        Hibernate.initialize(o);
                        return o;

                    }
                });
    }

    @Override
    @RolesAllowed("user")
    @SuppressWarnings("unchecked")
    // TODO weirdness here; learn more about CGLIB initialization.
    public IObject find(final Class klass, final long id) {
        return (IObject) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException {

                        IObject o = (IObject) session.get(klass, id);
                        Hibernate.initialize(o);
                        return o;

                    }
                });
    }

    @Override
    @RolesAllowed("user")
    @SuppressWarnings("unchecked")
    public <T extends IObject> List<T> findAll(final Class<T> klass,
            final Filter filter) {
        if (filter == null) {
            return getHibernateTemplate().loadAll(klass);
        }

        return (List<T>) execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException, SQLException {
                        Criteria c = session.createCriteria(klass);
                        timeoutSetter.setTimeout(c::setTimeout);
                        c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                        parseFilter(c, filter);
                        return c.list();
                    }
                });
    }

    @Override
    @RolesAllowed("user")
    @SuppressWarnings("unchecked")
    public <T extends IObject> T findByExample(final T example)
            throws ApiUsageException {
        return (T) execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException {

                try {
                    Criteria c = session.createCriteria(example.getClass());
                    timeoutSetter.setTimeout(c::setTimeout);
                    c.add(Example.create(example));
                    return c.uniqueResult();
                } catch (IncorrectResultSizeDataAccessException irsdae) {
                    throwNonUnique("findByExample");
                } catch (NonUniqueResultException nure) {
                    throwNonUnique("findByExample");
                }
                // Never reached!
                return null;

            }
        });
    }

    @Override
    @RolesAllowed("user")
    @SuppressWarnings("unchecked")
    public <T extends IObject> List<T> findAllByExample(final T example,
            final Filter filter) {
        return (List<T>) execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException {

                        Criteria c = session.createCriteria(example.getClass());
                        timeoutSetter.setTimeout(c::setTimeout);
                        c.add(Example.create(example));
                        parseFilter(c, filter);
                        return c.list();

                    }
                });
    }

    @Override
    @RolesAllowed("user")
    @SuppressWarnings("unchecked")
    public <T extends IObject> T findByString(final Class<T> klass,
            final String fieldName, final String value)
            throws ApiUsageException {
        return (T) execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException {

                try {
                    Criteria c = session.createCriteria(klass);
                    timeoutSetter.setTimeout(c::setTimeout);
                    c.add(Restrictions.eq(fieldName, value));
                    return c.uniqueResult();
                } catch (IncorrectResultSizeDataAccessException irsdae) {
                    throwNonUnique("findByString");
                } catch (NonUniqueResultException nure) {
                    throwNonUnique("findByString");
                }
                // Never reached
                return null;

            }
        });
    }

    @Override
    @RolesAllowed("user")
    @SuppressWarnings("unchecked")
    public <T extends IObject> List<T> findAllByString(final Class<T> klass,
            final String fieldName, final String value,
            final boolean caseSensitive, final Filter filter)
            throws ApiUsageException {
        return (List<T>) execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException {

                        Criteria c = session.createCriteria(klass);
                        timeoutSetter.setTimeout(c::setTimeout);
                        parseFilter(c, filter);

                        if (caseSensitive) {
                            c.add(Restrictions.like(fieldName, value,
                                    MatchMode.ANYWHERE));
                        } else {
                            c.add(Restrictions.ilike(fieldName, value,
                                    MatchMode.ANYWHERE));
                        }

                        return c.list();

                    }
                });
    }

    @Override
    @RolesAllowed("user")
    public <T extends IObject> T findByQuery(String queryName, Parameters params)
            throws ValidationException {

        if (params == null) {
            params = new Parameters();
        }

        // specify that we should only return a single value if possible
        params.unique();

        final Query<T> q = getQueryFactory().<T>lookup(queryName, params);
        timeoutSetter.setTimeout(q::setTimeout);
        if (params.isCache()) {
            q.enableQueryCache();
        }
        T result = null;
        try {
            result = execute(q);
        } catch (ClassCastException cce) {
            throw new ApiUsageException(
                    "Query named:\n\t"
                            + queryName
                            + "\n"
                            + "has returned an Object of type "
                            + cce.getMessage()
                            + "\n"
                            + "Queries must return IObjects when using findByQuery. \n"
                            + "Please try findAllByQuery for queries which return Lists. ");
        } catch (IncorrectResultSizeDataAccessException irsdae) {
            throwNonUnique(queryName);
        } catch (NonUniqueResultException nure) {
            throwNonUnique(queryName);
        }
        return result;
    }

    private void throwNonUnique(String queryName) {
        throw new ApiUsageException(
                "Query named:\n\n\t"
                        + queryName
                        + "\n\n"
                        + "has returned more than one Object\n"
                        + "findBy methods must return a single value.\n"
                        + "Please try findAllBy methods for queries which return Lists.");
    }

    @Override
    @RolesAllowed("user")
    public <T extends IObject> List<T> findAllByQuery(String queryName,
            Parameters params) {
        Query<List<T>> q = getQueryFactory().lookup(queryName, params);
        timeoutSetter.setTimeout(q::setTimeout);
        if (params != null && params.isCache()) {
            q.enableQueryCache();
        }
        return execute(q);
    }

    @Override
    @RolesAllowed("user")
    @SuppressWarnings("unchecked")
    public <T extends IObject> List<T> findAllByFullText(final Class<T> type,
            final String query, final Parameters params) {
        if (analyzer == null) {
            throw new ApiUsageException(
                    "IQuery not configured for full text search.\n"
                            + "Please use ode.api.Search instead.");
        }

        final List<IObject> results = (List<IObject>) execute(
                new HibernateCallback<List<IObject>>() {
                    @SuppressWarnings("rawtypes")
                    public List<IObject> doInHibernate(Session session)
                            throws HibernateException, SQLException {
                        SearchValues values = new SearchValues();
                        values.onlyTypes = Arrays.asList((Class) type);
                        values.copy(params);
                        FullText fullText = new FullText(values, query,
                                analyzer);
                        timeoutSetter.setTimeout(fullText::setTimeout);
                        return (List<IObject>) fullText.doWork(session, null);
                    }
                });

        if (CollectionUtils.isEmpty(results)) {
            return Collections.emptyList();
        }

        SearchBean search = new SearchBean();
        search.setTimeoutSetter(timeoutSetter);
        search.addParameters(params);
        search.addResult(results);
        return search.results();
    }

    // ~ Aggregations
    // =========================================================================

    @Override
    @SuppressWarnings("unchecked")
    @RolesAllowed("user")
    public List<Object[]> projection(final String query, Parameters p) {
        final Parameters params = (p == null ? new Parameters() : p);
        final Query<List<Object>> q = getQueryFactory().lookup(query, params);
        timeoutSetter.setTimeout(q::setTimeout);
        if (params.isCache()) {
            q.enableQueryCache();
        }

        @SuppressWarnings("rawtypes")
        final List rv = (List) execute(q);
        final int size = rv.size();
        Object obj = null;
        for (int i = 0; i < size; i++) {
            obj = rv.get(i);
            if (obj != null) {
                if (!Object[].class.isAssignableFrom(obj.getClass())) {
                    rv.set(i, new Object[]{obj});
                }
            }
        }
        for (int i = 0; i < size; i++) {
            Object[] x = (Object[]) rv.get(i);
            if (x != null && x.length == 1 && x[0] instanceof Map) {
                Map<Object, Object> y = (Map<Object, Object>) x[0];
                for (Map.Entry<Object, Object> z : y.entrySet()) {
                    if (z != null && z.getKey().toString().endsWith("_details_permissions")) {
                        z.setValue(new ode.util.PermDetails((IObject) z.getValue()));
                    }
                }
            }

        }
        return rv;
    }

    final static Pattern AGGS  = Pattern.compile("(count|sum|max|min)");
    final static Pattern FIELD = Pattern.compile("\\w?[\\w.\\s\\+\\-\\*]*");

    @RolesAllowed("user")
    public Long aggByQuery(String agg, String field, String query,
            Parameters params) {

        if (!AGGS.matcher(agg).matches()) {
            throw new ValidationException(agg + " does not match " + AGGS);
        }

        if (!FIELD.matcher(field).matches()) {
            throw new ValidationException(field + " does not match " + FIELD);
        }

        final QueryBuilder qb = new QueryBuilder();
        qb.select(agg + "("+field+")").append(query);
        qb.params(params);
        return (Long) execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException, SQLException {
                        final org.hibernate.Query q = qb.query(session);
                        timeoutSetter.setTimeout(q::setTimeout);
                        return q.uniqueResult();
                    }
                });
    }

    @SuppressWarnings("unchecked")
    @RolesAllowed("user")
    public Map<String, Long> aggMapByQuery(String agg, String mapKey,
            String field, String query, Parameters params) {
        if (!AGGS.matcher(agg).matches()) {
            throw new ValidationException(agg + " does not match " + AGGS);
        }

        if (!FIELD.matcher(field).matches()) {
            throw new ValidationException(field + " does not match " + FIELD);
        }

        if (!FIELD.matcher(mapKey).matches()) {
            throw new ValidationException(mapKey + " does not match " + FIELD);
        }

        final QueryBuilder qb = new QueryBuilder();
        qb.select(mapKey, agg + "(" + field + ")").append(query);
        qb.append(" group by " + mapKey);
        qb.params(params);
        final List<Object[]> list = (List<Object[]>) execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException, SQLException {
                        final org.hibernate.Query q = qb.query(session);
                        timeoutSetter.setTimeout(q::setTimeout);
                        return q.list();
                    }
                });

        Map<String, Long> rv = new HashMap<String, Long>();
        for (Object[] objs : list) {
            Object key = objs[0];
            Object value = objs[1];
            Long l = null;
            if (value instanceof Long) {
                l = (Long) value;
            } else if (value instanceof Integer) {
                l = ((Integer) value).longValue();
            } else {
                throw new ValidationException("Value for key " + key + " is " + value);
            }
            rv.put(key.toString(), l);
        }
        return rv;
    }


    // ~ Others
    // =========================================================================

    @Override
    public <T extends IObject> T refresh(T iObject) throws ApiUsageException {
        getHibernateTemplate().refresh(iObject);
        return iObject;
    }

    // ~ HELPERS
    // =========================================================================

    /**
     * Responsible for applying the information provided in a
     * {@link ode.parameters.Filter} to a {@link org.hibernate.Criteria}
     * instance.
     * @param c a criteria instance
     * @param f a filter to limit a query
     */
    protected void parseFilter(Criteria c, Filter f) {
        // Reverting for 4.0
        if (f != null && f.offset != null) {
            c.setFirstResult(f.offset);
        } else {
            c.setFirstResult(0);
        }
        if (f != null && f.limit != null) {
            c.setMaxResults(f.limit);
        } else {
            c.setMaxResults(Integer.MAX_VALUE);
        }
    }

}