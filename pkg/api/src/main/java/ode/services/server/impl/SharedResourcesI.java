package ode.services.server.impl;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ode.api.JobHandle;
import ode.model.IObject;
import ode.services.server.fire.Registry;
import ode.services.server.fire.TopicManager;
import ode.services.server.util.ServerExecutor;
import ode.services.server.util.ServerOnly;
import ode.services.server.util.ParamsCache;
import ode.services.server.util.ResultHolder;
import ode.services.server.util.ServiceFactoryAware;
import ode.services.scripts.ScriptRepoHelper;
import ode.services.util.Executor;
import ode.system.ServiceFactory;
import ode.util.Filterable;
import ode.ApiUsageException;
import ode.InternalException;
import ode.RTime;
import ode.ServerError;
import ode.ValidationException;
import ode.constants.categories.PROCESSCALLBACK;
import ode.constants.categories.PROCESSORCALLBACK;
import ode.constants.topics.PROCESSORACCEPTS;
import ode.grid.InteractiveProcessorI;
import ode.grid.InteractiveProcessorPrx;
import ode.grid.InteractiveProcessorPrxHelper;
import ode.grid.InternalRepositoryPrx;
import ode.grid.ParamsHelper;
import ode.grid.ProcessorPrx;
import ode.grid.RepositoryMap;
import ode.grid.RepositoryPrx;
import ode.grid.TablePrx;
import ode.grid.TablePrxHelper;
import ode.grid.TablesPrx;
import ode.grid.TablesPrxHelper;
import ode.grid._InteractiveProcessorTie;
import ode.grid._SharedResourcesOperations;
import ode.model.Job;
import ode.model.JobStatus;
import ode.model.JobStatusI;
import ode.model.OriginalFile;
import ode.model.OriginalFileI;
import ode.util.IceMapper;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import Ice.Current;

/**
 * Implementation of the SharedResources interface.
 * @see ode.grid.SharedResources
 */
public class SharedResourcesI extends AbstractCloseableAmdServant implements
        _SharedResourcesOperations, ServerOnly, ServiceFactoryAware,
        ParamsHelper.Acquirer { // FIXME

    /**
     * If no value is passed to this instance, this is the time (1 hour)
     * which will be used for a script session.
     */
    public final static long DEFAULT_TIMEOUT = 60 * 60 * 1000L;

    private final static Logger log = LoggerFactory.getLogger(SharedResourcesI.class);

    private final Set<String> tableIds = new HashSet<String>();

    private final Set<String> processorIds = new HashSet<String>();

    private final TopicManager topicManager;

    private final Registry registry;

    private final ScriptRepoHelper helper;

    private final ParamsCache paramsCache;

    /**
     * How long to wait for shared resources (e.g. TablesPrx) to respond to
     * requests.
     */
    private final long waitMillis;

    /**
     * Length of time (ms) to give a script session to live. Once the session
     * times out, the process will be killed.
     */
    private final long timeout;

    private ServiceFactoryI sf;

    public SharedResourcesI(ServerExecutor be, TopicManager topicManager,
            Registry registry, ScriptRepoHelper helper, ParamsCache cache) {
        this(be, topicManager, registry, helper, cache, 5000);
    }

    public SharedResourcesI(ServerExecutor be, TopicManager topicManager,
                Registry registry, ScriptRepoHelper helper, ParamsCache cache,
                long waitMillis) {
        this( be, topicManager, registry, helper, cache, waitMillis, DEFAULT_TIMEOUT);
    }

    public SharedResourcesI(ServerExecutor be, TopicManager topicManager,
                Registry registry, ScriptRepoHelper helper, ParamsCache cache,
                long waitMillis, long timeout) {
        super(null, be);
        this.waitMillis = waitMillis;
        this.topicManager = topicManager;
        this.registry = registry;
        this.helper = helper;
        this.paramsCache = cache;
        this.timeout = timeout;
        // In order to prevent overflow of currentTimeMillis+timeout and more
        // generally to prevent nonsensical timeouts, we limit to 1 year.
        if (timeout > DEFAULT_TIMEOUT * 24 * 365) {
            throw new ode.conditions.InternalException(
                    "Timeout too large: " + timeout);
        }
    }

    public void setServiceFactory(ServiceFactoryI sf) throws ServerError {
        this.sf = sf;
    }

    @Override
    protected void preClose(Ice.Current current) {
        synchronized (tableIds) {
            for (String id : tableIds) {
                TablePrx table =
                    TablePrxHelper.uncheckedCast(
                            sf.adapter.getCommunicator().stringToProxy(id));
                try {
                    table.close();
                } catch (Ice.NotRegisteredException e) {
                    log.debug("Table already gone: " + id);
                } catch (Exception e) {
                    log.error("Exception while closing table " + id, e);
                }

            }
            tableIds.clear();
        }
    }

    @Override
    protected void postClose(Current current) {
        // no-op
    }

    // Acquisition framework
    // =========================================================================

    private void register(TablePrx prx) {
        if (prx != null) {
            synchronized(tableIds) {
                tableIds.add(
                    Ice.Util.identityToString(prx.ice_getIdentity()));
            }
        }
    }

    private void checkAcquisitionWait(int seconds) throws ApiUsageException {
        if (seconds > (3 * 60)) {
            ApiUsageException aue = new ApiUsageException();
            aue.message = "Delay is too long. Maximum = 3 minutes.";
            throw aue;
        }
    }

    /**
     * A task that gets applied to various proxies to test their validity.
     * Usually defined inline as anonymous classes.
     *
     * @see {@link ProcessorCheck}
     */
    private interface RepeatTask<U extends Ice.ObjectPrx> {
        void requestService(Ice.ObjectPrx server, ResultHolder<U> holder)
                throws ServerError;
    }

    private <U extends Ice.ObjectPrx> U lookup(long millis, List<Ice.ObjectPrx> objectPrxs,
            RepeatTask<U> task) throws ServerError {

        ResultHolder<U> holder = new ResultHolder<U>(millis);
        for (Ice.ObjectPrx prx : objectPrxs) {
            if (prx != null) {
                task.requestService(prx, holder);
            }
        }
        return holder.get();
    }

    // Public interface
    // =========================================================================

    static String QUERY = "select o from OriginalFile o where o.mimetype = 'Repository'";

    public RepositoryPrx getScriptRepository(Current __current)
            throws ServerError {
        InternalRepositoryPrx[] repos = registry.lookupRepositories();
        InternalRepositoryPrx prx = null;
        if (repos != null) {
            for (int i = 0; i < repos.length; i++) {
                if (repos[i] != null) {
                    if (repos[i].toString().contains(helper.getUuid())) {
                        prx = repos[i];
                    }
                }
            }
        }
        return prx == null ? null : prx.getProxy();
    }

    @SuppressWarnings("unchecked")
    public RepositoryMap repositories(Current current) throws ServerError {

        // TODO
        // Possibly need to throttle the numbers of acquisitions per time.
        // Need to keep up with closing
        // might need to cache the found repositories.

        IceMapper mapper = new IceMapper();
        List<OriginalFile> objs = (List<OriginalFile>) mapper
                .map((List<Filterable>) sf.executor.execute(current.ctx, sf.principal,
                        new Executor.SimpleWork(this, "acquireRepositories") {
                            @Transactional(readOnly = true)
                            public Object doWork(Session session,
                                    ServiceFactory sf) {
                                return sf.getQueryService().findAllByQuery(
                                        QUERY, null);
                            }
                        }));

        InternalRepositoryPrx[] repos = registry.lookupRepositories();

        RepositoryMap map = new RepositoryMap();
        map.descriptions = new ArrayList<OriginalFile>();
        map.proxies = new ArrayList<RepositoryPrx>();

        List<Long> found = new ArrayList<Long>();
        for (InternalRepositoryPrx i : repos) {
            if (i == null) {
                continue;
            }
            try {
                OriginalFile desc = i.getDescription();
                if (desc == null || desc.getId() == null) {
                    log.warn("Description is null for " + i);
                    continue;
                }
                RepositoryPrx proxy = i.getProxy();
                map.descriptions.add(desc);
                map.proxies.add(proxy);
                found.add(desc.getId().getValue());
                sf.allow(proxy);
            } catch (Ice.LocalException e) {
                // Ok.
            }
        }

        for (OriginalFile r : objs) {
            if (!found.contains(r.getId().getValue())) {
                map.descriptions.add(r);
                map.proxies.add(null);
            }
        }

        return map;
    }

    public boolean areTablesEnabled(Current __current) throws ServerError {
        TablesPrx[] tables = registry.lookupTables();
        return null != lookup(waitMillis, Arrays.<Ice.ObjectPrx> asList(tables),
                new RepeatTask<TablesPrx>() {
                    public void requestService(Ice.ObjectPrx prx,
                            final ResultHolder<TablesPrx> holder) {
                        final TablesPrx server = TablesPrxHelper
                                .checkedCast(prx);
                        try {
                            if (server != null && server.getRepository() != null) {
                                holder.set(server);
                            }
                        } catch (Exception e) {
                            log.debug("Exception on getRepository: " + e);
                            holder.set(null);
                        }
                    }
                });
    }

    public TablePrx newTable(final long repo, String path, Current __current)
            throws ServerError {

        // Overriding repository logic for creation. As long as the
        // security system is still in charge, we need to have the files
        // being created for the proper user.
        final OriginalFile file = new OriginalFileI();
        RTime time = ode.rtypes.rtime(System.currentTimeMillis());
        file.setAtime(time);
        file.setMtime(time);
        file.setCtime(time);
        file.setMimetype(ode.rtypes.rstring("ODE.tables"));
        file.setPath(ode.rtypes.rstring(path));
        file.setName(ode.rtypes.rstring(path));

        IObject obj = (IObject) sf.executor.execute(__current.ctx,
                sf.principal, new Executor.SimpleWork(this, "newTable", repo, path) {
            @Transactional(readOnly = false)
            public Object doWork(Session session, ServiceFactory sf) {
                try {
                    IObject obj = (IObject) new IceMapper().reverse(file);
                    return sf.getUpdateService().saveAndReturnObject(obj);
                } catch (Exception e) {
                    log.error(e.toString()); // slf4j migration: toString()
                    return null;
                }
            }

        });

        OriginalFile saved = (OriginalFile) new IceMapper().map(obj);
        if (saved == null) {
            throw new InternalException(null, null, "Failed to save file");
        }
        return openTable(saved, __current);

    }

    @SuppressWarnings("unchecked")
    public TablePrx openTable(final OriginalFile file, final Current __current)
            throws ServerError {

        // Now make sure the current user has permissions to do this
        if (file == null || file.getId() == null) {

            throw new ValidationException(null, null,
                    "file must be a managed instance.");

        }

        try {
            sf.executor.execute(__current.ctx, sf.principal,
                new Executor.SimpleWork(this,
                    "checkOriginalFilePermissions", file.getId().getValue()) {
                    @Transactional(readOnly = true)
                    public Object doWork(Session session, ServiceFactory sf) {
                        return sf.getQueryService().get(
                            ode.model.core.OriginalFile.class,
                                file.getId().getValue());
                    }
                });
        } catch (Exception e) {
            IceMapper mapper = new IceMapper();
            ServerError ue = mapper.handleServerError(e, this.ctx);
            throw ue;
        }

        // Okay. All's valid.
        TablesPrx[] tables = registry.lookupTables();
        TablePrx tablePrx = (TablePrx) lookup(waitMillis,
                Arrays.<Ice.ObjectPrx> asList(tables),
                new RepeatTask<TablePrx>() {
                    public void requestService(Ice.ObjectPrx prx,
                            final ResultHolder holder) throws ServerError {
                       final TablesPrx server = TablesPrxHelper
                          .uncheckedCast(prx);

                       Map<String, String> ctx = new HashMap<String, String>();
                       ctx.put("ode.group", "-1");
                       server.begin_getTable(file, sf.proxy(), ctx,
                           new Ice.Callback() {
                               public void completed(Ice.AsyncResult r) {
                                   try {
                                       holder.set(server.end_getTable(r));
                                   } catch (Exception e) {
                                       holder.set(null);
                                   }
                               }});
                       }
                    });

        sf.allow(tablePrx);
        register(tablePrx);
        TableCloserI closer = new TableCloserI(sf, tablePrx, sessionedID("Table"));
        closer.setApplicationContext(this.ctx);
        closer.setHolder(holder);
        return closer.getProxy();
    }

    // Check job
    // Lookup processor
    // Create wrapper (InteractiveProcessor)
    // Create session (with session)
    // Setup environment
    // Send off to processor
    public InteractiveProcessorPrx acquireProcessor(final Job submittedJob,
            int seconds, final Current current) throws ServerError {

        checkAcquisitionWait(seconds);

        // Check job
        final IceMapper mapper = new IceMapper();
        final ode.model.jobs.Job savedJob = saveJob(submittedJob, mapper, current);
        if (savedJob == null) {
            throw new ApiUsageException(null, null, "Could not submit job. ");
        }

        // Okay. All's valid.
        final Job job = (Job) mapper.map(savedJob);
        ResultHolder<ProcessorPrx> holder = new ResultHolder<ProcessorPrx>(seconds*1000);
        ProcessorCallbackI callback = new ProcessorCallbackI(sf, holder, job);
        ProcessorPrx server = callback.activateAndWait(current);

        // Nothing left to try
        if (server == null) {
            final int count = callback.getResponses();
            final String msg = String.format(
                    "No processor available! [%d response(s)]", count);
            updateJob(job.getId().getValue(), "Error", msg, current);
            throw new ode.NoProcessorAvailable(null, null, msg, count);
        }

        InteractiveProcessorI ip = new InteractiveProcessorI(sf.principal,
                sf.sessionManager, sf.executor, server, job, timeout,
                sf.control, paramsCache,
                new ParamsHelper(this, sf.getExecutor(), sf.getPrincipal()),
                helper, current);
        Ice.Identity procId = sessionedID("InteractiveProcessor");
        Ice.ObjectPrx rv = sf.registerServant(procId, new _InteractiveProcessorTie(ip));
        sf.allow(rv);
        return InteractiveProcessorPrxHelper.uncheckedCast(rv);
    }

    public void addProcessor(ProcessorPrx proc, Current __current)
            throws ServerError {
        topicManager.register(PROCESSORACCEPTS.value, proc, false);
        processorIds.add(Ice.Util.identityToString(proc.ice_getIdentity()));
        if (sf.control != null) {
            sf.control.categories().add(
                    new String[]{PROCESSORCALLBACK.value, PROCESSCALLBACK.value});
        }
    }

    public void removeProcessor(ProcessorPrx proc, Current __current)
            throws ServerError {
        topicManager.unregister(PROCESSORACCEPTS.value, proc);
        processorIds.remove(Ice.Util.identityToString(proc.ice_getIdentity()));
    }

    //
    // HELPERS
    //
    // =========================================================================


    private Ice.Identity sessionedID(String type) {
        String key = type + "-" + UUID.randomUUID();
        return sf.getIdentity(key);
    }

    private ode.model.jobs.Job saveJob(final Job submittedJob,
            final IceMapper mapper, final Ice.Current current) {
        // First create the job with a status of WAITING.
        // The InteractiveProcessor will be responsible for its
        // further lifetime.
        final ode.model.jobs.Job savedJob = (ode.model.jobs.Job) sf.executor
                .execute(current.ctx, sf.principal, new Executor.SimpleWork(this,
                        "submitJob") {
                    @Transactional(readOnly = false)
                    public ode.model.jobs.Job doWork(Session session,
                            ServiceFactory sf) {

                        final JobHandle handle = sf.createJobHandle();
                        try {
                            JobStatus status = new JobStatusI();
                            status.setValue(ode.rtypes
                                    .rstring(JobHandle.WAITING));
                            submittedJob.setStatus(status);
                            handle.submit((ode.model.jobs.Job) mapper
                                    .reverse(submittedJob));
                            return handle.getJob();
                        } catch (ApiUsageException e) {
                            return null;
                        } catch (ObjectNotFoundException onfe) {
                            return null;
                        } finally {
                            if (handle != null) {
                                handle.close();
                            }
                        }
                    }
                });
        return savedJob;
    }

    private void updateJob(final long id, final String status, final String message, final Ice.Current current) {
        sf.executor.execute(current.ctx, sf.principal, new Executor.SimpleWork(this, "updateJob") {
            @Transactional(readOnly = false)
            public Object doWork(Session session,
                    ServiceFactory sf) {

                final JobHandle handle = sf.createJobHandle();
                try {
                    handle.attach(id);
                    handle.setStatusAndMessage(status, message);
                    return null;
                } finally {
                    handle.close();
                }
            }
        });
    }

}