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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import ode.annotations.RolesAllowed;
import ode.api.IUpdate;
import ode.api.ServiceInterface;
import ode.api.local.LocalAdmin;
import ode.api.local.LocalQuery;
import ode.api.local.LocalUpdate;
import ode.conditions.ApiUsageException;
import ode.conditions.InternalException;
import ode.conditions.ValidationException;
import ode.model.IObject;
import ode.model.meta.EventLog;
import ode.parameters.Parameters;
import ode.services.eventlogs.EventLogLoader;
import ode.services.fulltext.FullTextBridge;
import ode.services.fulltext.FullTextIndexer;
import ode.services.fulltext.FullTextThread;
import ode.services.search.IndexWatcher;
import ode.services.sessions.SessionManager;
import ode.services.util.Executor;
import ode.system.EventContext;
import ode.tools.hibernate.ReloadFilter;
import ode.tools.hibernate.UpdateFilter;
import ode.util.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Session;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * implementation of the IUpdate service interface
 */
@Transactional(readOnly = false)
public class UpdateImpl extends AbstractLevel1Service implements LocalUpdate {

    private final Logger log = LoggerFactory.getLogger(UpdateImpl.class);

    protected transient LocalAdmin localAdmin;

    protected transient LocalQuery localQuery;

    protected transient Executor executor;

    protected transient SessionManager sessionManager;

    protected transient FullTextBridge fullTextBridge;

    protected transient IndexWatcher indexWatcher;

    public final void setAdminService(LocalAdmin admin) {
        getBeanHelper().throwIfAlreadySet(this.localAdmin, admin);
        this.localAdmin = admin;
    }

    public final void setQueryService(LocalQuery query) {
        getBeanHelper().throwIfAlreadySet(this.localQuery, query);
        this.localQuery = query;
    }

    public void setExecutor(Executor executor) {
        getBeanHelper().throwIfAlreadySet(this.executor, executor);
        this.executor = executor;
    }

    public void setSessionManager(SessionManager sessionManager) {
        getBeanHelper().throwIfAlreadySet(this.sessionManager, sessionManager);
        this.sessionManager = sessionManager;
    }

    public void setFullTextBridge(FullTextBridge fullTextBridge) {
        getBeanHelper().throwIfAlreadySet(this.fullTextBridge, fullTextBridge);
        this.fullTextBridge = fullTextBridge;
    }

    public void setIndexWatcher(IndexWatcher indexWatcher) {
        getBeanHelper().throwIfAlreadySet(this.indexWatcher, indexWatcher);
        this.indexWatcher = indexWatcher;
    }

    public Class<? extends ServiceInterface> getServiceInterface() {
        return IUpdate.class;
    };

    // ~ LOCAL PUBLIC METHODS
    // =========================================================================

    @RolesAllowed("user")
    public void flush() {
        session().flush();
    }

    // ~ INTERFACE METHODS
    // =========================================================================

    @RolesAllowed("user")
    public void saveObject(IObject graph) {
        doAction(graph, new UpdateAction<IObject>() {
            @Override
            public IObject run(IObject value, UpdateFilter filter, Session s) {
                return internalMerge(value, filter, s);
            }
        });
    }

    @RolesAllowed("user")
    public IObject saveAndReturnObject(IObject graph) {
        return doAction(graph, new UpdateAction<IObject>() {
            @Override
            public IObject run(IObject value, UpdateFilter filter, Session s) {
                return internalMerge(value, filter, s);
            }
        });
    }

    @RolesAllowed("user")
    public void saveCollection(Collection graph) {
        doAction(graph, new UpdateAction<Collection>() {
            @Override
            public Collection run(Collection value, UpdateFilter filter,
                    Session s) {
                for (Object o : value) {
                    IObject obj = (IObject) o;
                    obj = internalMerge(obj, filter, s);
                }
                return null;
            }
        });
    }

    @RolesAllowed("user")
    public IObject[] saveAndReturnArray(IObject[] graph) {
        return doAction(graph, new UpdateAction<IObject[]>() {
            @Override
            public IObject[] run(IObject[] value, UpdateFilter filter, Session s) {
                IObject[] copy = new IObject[value.length];
                for (int i = 0; i < value.length; i++) {
                    copy[i] = internalMerge(value[i], filter, s);
                }
                return copy;
            }
        });
    }

    @RolesAllowed("user")
    public List<Long> saveAndReturnIds(IObject[] graph) {

        if (graph == null || graph.length == 0) {
            return Collections.emptyList(); // EARLY EXIT!
        }

        final List<Long> ids = new ArrayList<Long>(graph.length);
        final ReloadFilter filter = new ReloadFilter(session());
        doAction(graph, filter, new UpdateAction<IObject[]>() {
            @Override
            public IObject[] run(IObject[] value, UpdateFilter filter, Session s) {
                for (int i = 0; i < value.length; i++) {
                    ids.add(i, internalSave(value[i], (ReloadFilter) filter, s));
                }
                return null;
            }
        });
        return ids;
    }

    @RolesAllowed("user")
    public void saveArray(IObject[] graph) {
        doAction(graph, new UpdateAction<IObject[]>() {
            @Override
            public IObject[] run(IObject[] value, UpdateFilter filter, Session s) {
                IObject[] copy = new IObject[value.length];
                for (int i = 0; i < value.length; i++) {
                    copy[i] = internalMerge(value[i], filter, s);
                }
                return copy;
            }
        });
    }

    @RolesAllowed("user")
    public void deleteObject(IObject row) {
        if (row == null) {
            return;
        }
        if (row.getId() == null) {
            throw new ApiUsageException(
                    "Non-managed IObject entity cannot be deleted. Must have an id.");
        }
        try {
            doAction(row, new UpdateAction<IObject>() {
                @Override
                public IObject run(IObject value, UpdateFilter filter, Session s) {
                    internalDelete(value, filter, s);
                    return null;
                }
            });
        } catch (InvalidDataAccessApiUsageException idaaue) {
            throw new ApiUsageException("Cannot delete " + row + "\n" +
			"Original message: " + idaaue.getMessage() + "\n" +
			"Consider using IDelete instead.");
        }
    }

    @RolesAllowed("system")
    public void indexObject(IObject row) {
        if (row == null || row.getId() == null) {
            throw new ValidationException(
                    "Non-managed object cannot be indexed.");
        }

        /*
        CreationLogLoader logs = new CreationLogLoader(localQuery, row);
        FullTextIndexer fti = new FullTextIndexer(logs);
        fti.setApplicationContext(this.executor.getContext());

        final FullTextThread ftt = new FullTextThread(sessionManager, executor,
                fti, this.fullTextBridge, true);
        Future<Object> future = executor.submit(Executors.callable(ftt));
        executor.get(future);
        */

        /* Gather information for new event for REINDEX log entry. */
        final EventContext ec = localAdmin.getEventContextQuiet();
        final long userId = ec.getCurrentUserId();
        long groupId = ec.getCurrentGroupId();
        final long sessionId = ec.getCurrentSessionId();
        if (groupId == -1) {
            /* Must provide a real group. */
            groupId = localAdmin.getDefaultGroup(userId).getId();
        }
        /* Write REINDEX to event log and wait for processing. */
        log.debug("Awaiting indexing of {}.", row);
        try {
            if (!indexWatcher.indexObject(row, userId, groupId, sessionId).tryAcquire(1, TimeUnit.MINUTES)) {
                log.info("Timed out while awaiting indexing of {}.", row);
                throw new InternalException("indexing did not occur within a reasonable time");
            }
        } catch (InterruptedException ie) {
            log.warn("unexpectedly awoken", ie);
        }
    }

    // ~ Internals
    // =========================================================
    private void beforeUpdate(Object argument, UpdateFilter filter) {

        if (argument == null) {
            throw new IllegalArgumentException(
                    "Argument to save cannot be null.");
        }

        if (getBeanHelper().getLogger().isDebugEnabled()) {
            getBeanHelper().getLogger().debug(" Saving event before merge. ");
        }

    }

    /**
     * Note if we use anything other than merge here, functionality from
     * {@link ode.security.basic.MergeEventListener} needs to be moved to
     * {@link UpdateFilter} or to another event listener.
     */
    protected Long internalSave(IObject obj, ReloadFilter filter,
            Session session) {
        if (getBeanHelper().getLogger().isDebugEnabled()) {
            getBeanHelper().getLogger().debug(" Internal save. ");
        }

        IObject result = (IObject) filter.filter(null, obj);
        Long id = (Long) session.save(result);
        return id;
    }

    /**
     * Note if we use anything other than merge here, functionality from
     * {@link ode.security.basic.MergeEventListener} needs to be moved to
     * {@link UpdateFilter} or to another event listener.
     */
    protected IObject internalMerge(IObject obj, UpdateFilter filter,
            Session session) {
        if (getBeanHelper().getLogger().isDebugEnabled()) {
            getBeanHelper().getLogger().debug(" Internal merge. ");
        }
        final Long previousId = obj.getId();
        IObject result = (IObject) filter.filter(null, obj);
        result = (IObject) session.merge(result);
        final Long currentId = result.getId();
        if (previousId != null && previousId != currentId) {
            /* HHH-1661: merge may insert deleted entities with new ID */
            if (getBeanHelper().getLogger().isDebugEnabled()) {
                getBeanHelper().getLogger().debug("attempt to save deleted object: " + obj);
            }
            throw new ValidationException("object no longer exists in database");
        }
        return result;
    }

    protected void internalDelete(IObject obj, UpdateFilter filter,
            Session session) {
        if (getBeanHelper().getLogger().isDebugEnabled()) {
            getBeanHelper().getLogger().debug(" Internal delete. ");
        }

        session.delete(session.load(Utils.trueClass(obj.getClass()), obj
                .getId()));
    }

    private void afterUpdate(UpdateFilter filter, Session session) {

        if (getBeanHelper().getLogger().isDebugEnabled()) {
            getBeanHelper().getLogger().debug(" Post-save cleanup. ");
        }

        // Clean up
        session.flush();
        filter.unloadReplacedObjects();

    }

    private <T> T doAction(final T graph, final UpdateAction<T> action) {
        final UpdateFilter filter = new UpdateFilter();
        return doAction(graph, filter, action);
    }

    private <T> T doAction(final T graph, final UpdateFilter filter,
            final UpdateAction<T> action) {
        final Session session = session();
        T retVal;
        beforeUpdate(graph, filter);
        retVal = action.run(graph, filter, session);
        afterUpdate(filter, session);
        return retVal;
    }

    private abstract class UpdateAction<T> {
        public abstract T run(T value, UpdateFilter filter, Session s);
    }

    private Session session() {
        return SessionFactoryUtils.getSession(getSessionFactory(), false);
    }
}

/**
 * {@link EventLogLoader} which loads a single instance.
 */
class CreationLogLoader extends EventLogLoader {

    final private LocalQuery query;

    private IObject obj;

    public CreationLogLoader(LocalQuery query, IObject obj) {
        this.obj = obj;
        this.query = query;
        setQueryService(query);
    }

    @Override
    public EventLog query() {
        if (obj == null) {
            return null;
        } else {
            EventLog el = query.findByQuery("select el from EventLog el "
                    + "where el.action = 'INSERT' and "
                    + "el.entityType = :type and " + "el.entityId = :id",
                    new Parameters()
                            .addString("type", obj.getClass().getName()).addId(
                                    obj.getId()));
            obj = null;
            return el;
        }
    }

    @Override
    public long more() {
        return 0;
    }

}