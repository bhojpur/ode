package ode.grid;

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

import static ode.rtypes.rmap;
import static ode.rtypes.robject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import ode.system.EventContext;
import ode.api.JobHandle;
import ode.api.RawFileStore;
import ode.api.local.LocalAdmin;
import ode.model.core.OriginalFile;
import ode.model.meta.Session;
import ode.parameters.Parameters;
import ode.services.server.util.ParamsCache;
import ode.services.procs.Processor;
import ode.services.scripts.ScriptRepoHelper;
import ode.services.sessions.SessionManager;
import ode.services.util.Executor;
import ode.system.Principal;
import ode.system.ServiceFactory;
import ode.tools.hibernate.QueryBuilder;
import ode.ApiUsageException;
import ode.RMap;
import ode.RType;
import ode.ServerError;
import ode.ValidationException;
import ode.model.Job;
import ode.model.OriginalFileI;
import ode.model.ParseJob;
import ode.util.CloseableServant;
import ode.util.IceMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import Glacier2.IdentitySetPrx;
import Glacier2.SessionControlPrx;
import Ice.Current;

/**
 * {@link Processor} implementation which delegates to an ode.grid.Processor
 * servant. Functions as a state machine. A single {@link ProcessPrx} can be
 * active at any given time. Once it's complete, then the {@link RMap results}
 * can be obtained, then a new {@link Job} can be submitted, until {@link #stop}
 * is set. Any other use throws an {@link ApiUsageException}.
 */
public class InteractiveProcessorI implements _InteractiveProcessorOperations,
    CloseableServant {

    private static Session UNINITIALIZED = new Session();

    private static Logger log = LoggerFactory.getLogger(InteractiveProcessorI.class);

    private final SessionManager mgr;

    private final ProcessorPrx prx;

    /**
     * Used to access params for non-{@link ParseJob}. This may require another
     * call to the processor with a {@link ParseJob}.
     */
    private final ParamsCache paramsCache;

    /**
     * Used to generate and save params for a {@link ParseJob}
     */
    private final ParamsHelper paramsHelper;

    private final ScriptRepoHelper scriptRepoHelper;

    private final Executor ex;

    private final Job job;

    private final long scriptId;

    private final String scriptSha1;

    private final String mimetype;

    private final String launcher;

    private final String process;

    /**
     * Number of milliseconds which the session used by this processor
     * will be allowed to live (timeToLive)
     */
    private final long timeout;

    private final ReadWriteLock rwl = new ReentrantReadWriteLock();

    private final Principal principal;

    private final SessionControlPrx control;

    private boolean detach = false;

    private boolean obtainResults = false;

    private boolean stop = false;

    private ProcessPrx currentProcess = null;

    private Session session;

    private JobParams params;

    /**
     * Milliseconds since the epoch when the session for this processor was
     * created.
     */
    private long started;


    /**
     * 
     * @param p
     * @param mgr
     * @param prx
     * @param job
     *            Unloaded {@link Job} instance, which will be used by
     *            {@link ode.grid.Processor} to reload the {@link Job}
     * @param timeout
     */
    public InteractiveProcessorI(Principal p, SessionManager mgr, Executor ex,
            ProcessorPrx prx, Job job, long timeout, SessionControlPrx control,
            ParamsCache paramsCache, ParamsHelper paramsHelper, ScriptRepoHelper scriptRepoHelper,
            Ice.Current current)
        throws ServerError {
        this.paramsCache = paramsCache;
        this.paramsHelper = paramsHelper;
        this.scriptRepoHelper = scriptRepoHelper;
        this.principal = p;
        this.ex = ex;
        this.mgr = mgr;
        this.prx = prx;
        this.job = job;
        this.timeout = timeout;
        this.control = control;
        this.session = UNINITIALIZED;

        // Loading values.
        OriginalFile f = getScriptId(job, current);
        this.scriptId = f.getId();
        this.scriptSha1 = f.getHash();
        this.mimetype = f.getMimetype();
        this.launcher = scriptRepoHelper.getLauncher(this.mimetype);
        this.process = scriptRepoHelper.getProcess(this.mimetype);

    }

    private void setLauncher(Ice.Current __current) {
        __current.ctx.put("ode.launcher", this.launcher);
        __current.ctx.put("ode.process", this.process);
    }

    public JobParams params(Current __current) throws ServerError {

        rwl.writeLock().lock();

        try {

            if (stop) {
                throw new ApiUsageException(null, null,
                        "This processor is stopped.");
            }

            // Setup new user session
            if (session == UNINITIALIZED) {
                session = newSession(__current);
            }

            if (params == null) {
                try {
                    if (job instanceof ParseJob) {
                        setLauncher(__current);
                        params = prx.parseJob(session.getUuid(), job, __current.ctx);
                        if (params == null) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Can't find params for ");
                            sb.append(scriptId);
                            sb.append("!\n");
                            for (String which : new String[]{"stdout", "stderr"}) {
                                OriginalFile file = loadFileOrNull(which, __current);
                                if (file == null) {
                                    sb.append("No ");
                                    sb.append(which);
                                    sb.append(".\n");
                                } else {
                                    sb.append(which);
                                    sb.append(" is in file " + file.getId());
                                    sb.append(":");
                                    sb.append("\n---------------------------------\n");
                                    appendIfText(file, sb, __current);
                                    sb.append("\n---------------------------------\n");
                                }
                            }
                            throw new ode.ValidationException(null, null,
                                    sb.toString());
                        }

                        paramsHelper.saveScriptParams(params, (ParseJob) job,
                                __current);
                    } else {
                        params = paramsCache.getParams(scriptId,  scriptSha1, __current);
                    }
                } catch (Throwable t) {
                    if (t instanceof ServerError) {
                        log.debug("Error while parsing job", t);
                        throw (ServerError) t;
                    } else {
                        ode.InternalException ie = new ode.InternalException();
                        IceMapper.fillServerError(ie, t);
                        throw ie;
                    }
                }
            }
            return params;

        } finally {
            rwl.writeLock().unlock();
        }

    }

    public ProcessPrx execute(RMap inputs, Current __current)
            throws ServerError {

        rwl.writeLock().lock();

        try {

            if (currentProcess != null) {
                throw new ApiUsageException(null, null,
                        "Process currently running.");
            }

            if (obtainResults) {
                throw new ApiUsageException(null, null,
                        "Please retrieve results.");
            }

            if (stop) {
                throw new ApiUsageException(null, null,
                        "This processor is stopped.");
            }

            // Setup new user session
            if (session == UNINITIALIZED) {
                session = newSession(__current);
            }

            // Setup environment
            if (inputs != null && inputs.getValue() != null) {
                IceMapper mapper = new IceMapper();
                for (String key : inputs.getValue().keySet()) {
                    Object v = mapper.fromRType(inputs.get(key));
                    mgr.setInput(session.getUuid(), key, v);
                }
            }

            // Execute
            try {
                final String uuid = session.getUuid();
                if (params == null) {
                    params = params(__current);
                }

                setLauncher(__current);
                currentProcess = prx.processJob(uuid, params, job, __current.ctx);

                // One of these fields is being returned as null on at least
                // one system. Adding debugging to detect which.
                if (control == null) {
                    log.error("Control null on execute");
                } else {
                    IdentitySetPrx identities = control.identities();
                    if (identities == null) {
                        log.error("Identities null on execute");
                    } else {
                        // Have to add the process to the control, otherwise the
                        // user won't be able to view it: ObjectNotExistException!
                        identities.add(
                            new Ice.Identity[]{currentProcess.ice_getIdentity()});
                    }
                }

            } catch (ode.ValidationException ve) {
                failJob(ve, __current);
                throw ve;
            } catch (ServerError se) {
                log.debug("Error while processing job", se);
                throw se;
            }

            if (currentProcess == null) {
                return null;
            }

            obtainResults = true;
            return currentProcess;

        } finally {
            rwl.writeLock().unlock();
        }

    }

    public RMap getResults(ProcessPrx proc, Current __current)
            throws ServerError {

        rwl.writeLock().lock();
        try {
            finishedOrThrow();

            // Gather output
            ode.RMap output = rmap(new HashMap<String, ode.RType>());
            Map<String, Object> env = mgr.outputEnvironment(session.getUuid());
            IceMapper mapper = new IceMapper();
            for (String key : env.keySet()) {
                RType rt = mapper.toRType(env.get(key));
                output.put(key, rt);
            }
            optionallyLoadFile(output.getValue(), "stdout", __current);
            optionallyLoadFile(output.getValue(), "stderr", __current);
            currentProcess = null;
            obtainResults = false;
            return output;
        } finally {
            rwl.writeLock().unlock();
        }
    }

    public long expires(Current __current) {
        return started + timeout;
    }

    public Job getJob(Current __current) {
        return job;
    }

    public boolean setDetach(boolean detach, Current __current) {
        rwl.writeLock().lock();
        try {
            boolean old = this.detach;
            this.detach = detach;
            return old;
        } finally {
            rwl.writeLock().unlock();
        }
    }
    
    /**
     * Cancels the current process, nulls the value, and returns immediately
     * if detach is false.
     */
    public void stop(Current __current) throws ServerError {
        rwl.writeLock().lock();
        
        if (stop) {
            return; // Already stopped.
        }
        
        try {
            
            if (detach) {
                if (currentProcess != null) {
                    log.info("Detaching from " + currentProcess);
                }
            } else {
                doStop();
            }

        stop = true;
            
        } finally {
            rwl.writeLock().unlock();
        }
    }

    protected void doStop() throws ServerError {
        // Then perform cleanup
        Exception pException = null;
        Exception sException = null;
        
        if (currentProcess != null) {
            try {
                ProcessPrx p = ProcessPrxHelper.uncheckedCast(
                        currentProcess.ice_oneway());
                p.shutdown();
                currentProcess = null;
            } catch (Exception ex) {
                log.warn("Failed to stop process", ex);
                pException = ex;
            }
        }
        
        if (session != null && session != UNINITIALIZED) {
            try {
                while (mgr.close(session.getUuid()) > 0);
                session = null;
            } catch (Exception ex) {
                log.warn("Failed to close session " + session.getUuid(), ex);
                sException = ex;
            }
        }
        
        if (pException != null || sException != null) {
            ode.InternalException ie = new ode.InternalException();
            StringBuilder sb = new StringBuilder();
            if (pException != null) {
                sb.append("Failed to shutdown process: " + pException.getMessage());
            }
            if (sException != null) {
                sb.append("Failed to close session: " + sException.getMessage());
            }
            ie.message = sb.toString();
            throw ie;
        }

    }
    
    // Helpers
    // =========================================================================

    private void finishedOrThrow() throws ServerError {
        if (currentProcess == null) {
            throw new ApiUsageException(null, null, "No current process.");
        } else if (currentProcess.poll() == null) {
            throw new ApiUsageException(null, null, "Process still running.");
        }
    }

    private final static String stdfile_query = "select file from Job job "
            + "join job.originalFileLinks links join links.child file "
            + "where file.name = :name and job.id = :id";

    private OriginalFile loadFileOrNull(final String name, final Ice.Current current) {
        return this.ex.execute(current.ctx, this.principal,
                new Executor.SimpleWork<OriginalFile>(this, "optionallyLoadFile") {
            @Transactional(readOnly=true)
            public OriginalFile doWork(org.hibernate.Session session,
                    ServiceFactory sf) {

                return sf.getQueryService().findByQuery(
                        stdfile_query,
                        new Parameters().addId(job.getId().getValue())
                                .addString("name", name));
            }
        });
    }

    private void optionallyLoadFile(final Map<String, RType> val,
            final String name, final Ice.Current current) {

        OriginalFile file = loadFileOrNull(name, current);
        if (file != null) {
            val.put(name,
            robject(new OriginalFileI(file.getId(), false)));
        }
    }

    private void appendIfText(final OriginalFile file, final StringBuilder sb, final Ice.Current current) {
        if (file.getMimetype() != null && file.getMimetype().contains("text")) {
            this.ex.execute(current.ctx, this.principal, new Executor.SimpleWork<Void>(this, "appendIfText", file) {
                @Transactional(readOnly=true)
                public Void doWork(org.hibernate.Session session,
                        ServiceFactory sf) {
                    RawFileStore rfs = sf.createRawFileStore();
                    try {
                        rfs.setFileId(file.getId());
                        sb.append(new String(rfs.read(0, file.getSize().intValue())));
                    } finally {
                        rfs.close();
                    }
                    return null;
                }
            });
        }
    }

    private void failJob(final ValidationException ve, final Ice.Current current) {
        this.ex.execute(current.ctx, this.principal, new Executor.SimpleWork<Void>(this, "failJob", job.getId().getValue()) {
            @Transactional(readOnly=false)
            public Void doWork(org.hibernate.Session session,
                    ServiceFactory sf) {
                JobHandle jh = sf.createJobHandle();
                try {
                    jh.attach(job.getId().getValue());
                    jh.setStatusAndMessage("Error", // Just make it SQL "text"?
                            (ve.message == null ? null :
                                ve.message.substring(0,
                                        Math.min(255, ve.message.length()))));
                } finally {
                    jh.close();
                }
                return null;
            }
        });
    }

    private EventContext getEventContext(final Ice.Current current) {
        return this.ex.execute(current.ctx, this.principal, new Executor.SimpleWork<EventContext>(this, "getEventContext") {
            @Transactional(readOnly=true)
            public EventContext doWork(org.hibernate.Session session,
                                       ServiceFactory sf) {
                    return ((LocalAdmin) sf.getAdminService()).getEventContextQuiet();
            }
        });
    }

    private Session newSession(Current __current) {
        EventContext ec = getEventContext(__current);
        Session newSession = mgr.createWithAgent(
                new Principal(ec.getCurrentUserName(),
                ec.getCurrentGroupName(), "Processing"), "ODE.scripts", null);
        newSession.setTimeToIdle(0L);
        newSession.setTimeToLive(timeout);
        newSession = mgr.update(newSession, true);
        started = System.currentTimeMillis();

        return newSession;
    }

    private OriginalFile getScriptId(final Job job, final Ice.Current current) throws ode.ValidationException {
        final QueryBuilder qb = new QueryBuilder();
        qb.select("o").from("Job", "j");
        qb.join("j.originalFileLinks", "links", false, false);
        qb.join("links.child", "o", false, false);
        qb.where();
        qb.and("j.id = :id").param("id", job.getId().getValue());
        scriptRepoHelper.buildQuery(qb);

        final Map<String, String> ctx = new HashMap<String, String>();
        ctx.putAll(current.ctx);
        ctx.put("ode.group", "-1");

        final OriginalFile f = (OriginalFile) this.ex.execute(ctx, this.principal,
                new Executor.SimpleWork(this, "getScriptId") {
                    @Transactional(readOnly = true)
                    public Object doWork(org.hibernate.Session session,
                            ServiceFactory sf) {
                        return qb.query(session).uniqueResult();
                    }
                });
        if (f == null) {
            throw new ode.ValidationException(null, null,
                    "No script for job :" + job.getId().getValue());
        }
        return f;
    }

    public void close(Current current) throws Exception {
        stop(current);
    }

}