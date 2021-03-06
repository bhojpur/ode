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

import ode.parameters.Parameters;
import ode.security.AdminAction;
import ode.security.SecuritySystem;
import ode.services.util.Executor;
import ode.services.util.IceUtil;
import ode.system.Principal;
import ode.system.ServiceFactory;
import ode.InternalException;
import ode.ServerError;
import ode.model.Job;
import ode.model.OriginalFileI;
import ode.model.ParseJob;
import ode.model.ParseJobI;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import Ice.Current;
import Ice.ReadObjectCallback;
import Ice.UnmarshalOutOfBoundsException;

public class ParamsHelper {

    private final static Logger log = LoggerFactory.getLogger(ParamsHelper.class);

    private final Acquirer acq;

    private final Executor ex;

    private final Principal p;

    private final SecuritySystem secSys;

    public ParamsHelper(Acquirer acq, Executor ex, Principal p) {
        this.acq = acq;
        this.ex = ex;
        this.p = p;
        this.secSys = // FIXME REFACTOR
            (SecuritySystem) ex.getContext().getBean("securitySystem");
    }

    //
    // HELPER
    //

    /**
     * Build a job for the script with id.
     *
     * @param id
     *            script id.
     * @return the job.
     * @throws ServerError if the job could not be built for the script
     */
    public ParseJobI buildParseJob(final long id) throws ServerError {
        final OriginalFileI file = new OriginalFileI(id, false);
        final ParseJobI job = new ParseJobI();
        job.linkOriginalFile(file);
        job.setMessage(ode.rtypes.rstring(String.format("Parsing script %s",
                id)));
        return job;
    }

    /**
     * Get the script params for the file.
     *
     * @param id
     *            the ID of the script
     * @param __current
     *            regarding the method invocation
     * @return jobparams of the script.
     * @throws ServerError if the params could not be retrieved or created
     */
    public JobParams getOrCreateParams(final long id, Current __current)
            throws ServerError {

        JobParams params = getParamsOrNull(id, __current);
        if (params == null) {
            return generateScriptParams(id, __current);
        }
        return params;
    }

    JobParams getParamsOrNull(final long scriptId, Current __current) {
        ode.model.jobs.ParseJob job = getParseJobForScript(scriptId, __current);

        if (job != null) {
            return parse(job.getParams(), __current);
        }
        return null;
    }

    ode.model.jobs.ParseJob getParseJobForScript(final long scriptId, final Ice.Current current) {
        ode.model.jobs.ParseJob job = (ode.model.jobs.ParseJob) ex.execute(current.ctx, p,
                new Executor.SimpleWork(this, "getParseJobForScript", scriptId) {
                    @Transactional(readOnly = true)
                    public Object doWork(Session session, ServiceFactory sf) {
                        Parameters p = new Parameters();
                        p.page(0, 1);
                        p.addLong("id", scriptId);
                        return sf
                                .getQueryService()
                                .findByQuery(
                                        "select job from ParseJob job "
                                                + "join job.originalFileLinks scriptlinks "
                                                + "join scriptlinks.child script "
                                                + "where job.params is not null "
                                                + "and script.id = :id "
                                                + "and script.details.updateEvent.id <= job.details.updateEvent.id "
                                                + "order by job.details.updateEvent.id desc",
                                        p);
                    }

                });
        return job;
    }

    JobParams generateScriptParams(long id, Ice.Current __current)
            throws ServerError {
       return generateScriptParams(id, true, __current);
    }

    /**
     * Acquires an {@link InteractiveProcessorPrx} and runs a {@link ParseJob}.
     * If the "save" argument is true, then the {@link JobParams} instance will
     * be saved to the database; otherwise, the {@link ParseJob} will be deleted.
     */
    public JobParams generateScriptParams(long id, boolean save, Ice.Current __current)
            throws ServerError {

        ParseJob job = buildParseJob(id);
        InteractiveProcessorPrx proc = acq.acquireProcessor(job, 10, __current);
        if (proc == null) {
            throw new InternalException(null, null, "No processor acquired.");
        }

        job = (ParseJob) proc.getJob(__current.ctx);
        final JobParams rv = proc.params(__current.ctx);
        if (save) {
            // Guaranteed non-null for a parse job
            saveScriptParams(rv, job, __current);
        } else {
            deleteScriptParams(job, __current);
        }
        return rv;
    }

    void saveScriptParams(JobParams params,
            final ParseJob job, Ice.Current __current)
            throws ServerError {

        final byte[] data = parse(params, __current);
        ex.execute(__current.ctx, p, new Executor.SimpleWork(this, "saveScriptParams", job.getId().getValue()) {
            @Transactional(readOnly = false)
            public Object doWork(final Session session, final ServiceFactory sf) {
                ode.model.jobs.ParseJob parseJob = sf.getQueryService().get(
                        ode.model.jobs.ParseJob.class, job.getId().getValue());
                parseJob.setParams(data);
                secSys.runAsAdmin(new AdminAction(){
                    public void runAsAdmin() {
                        session.flush();
                    }});
                return null;
            }
        });
    }

    void deleteScriptParams(final ParseJob job, Ice.Current __current)
            throws ServerError {

        ex.execute(__current.ctx, p, new Executor.SimpleWork(this, "deleteScriptParams", job.getId().getValue()) {
            @Transactional(readOnly = false)
            public Object doWork(final Session session, final ServiceFactory sf) {
                final ode.model.jobs.ParseJob parseJob = sf.getQueryService().get(
                        ode.model.jobs.ParseJob.class, job.getId().getValue());
                secSys.runAsAdmin(new AdminAction(){
                    public void runAsAdmin() {
                        session.delete(parseJob);
                        session.flush();
                    }});
                return null;
            }
        });
    }

    byte[] parse(JobParams params, Ice.Current current) {
        Ice.OutputStream os = IceUtil.createSafeOutputStream(
                current.adapter.getCommunicator());
        byte[] bytes = null;
        try {
            os.writeObject(params);
            os.writePendingObjects();
            bytes = os.finished();
        } finally {
            os.destroy();
        }
        return bytes;
    }

    JobParams parse(byte[] data, Ice.Current current) {

        if (data == null) {
            return null; // EARLY EXIT!
        }

        Ice.InputStream is = IceUtil.createSafeInputStream(
                current.adapter.getCommunicator(), data);
        final JobParams[] params = new JobParams[1];
        try {
            is.readObject(new ReadObjectCallback() {
                public void invoke(Ice.Object arg0) {
                    params[0] = (JobParams) arg0;
                }
            });
            is.readPendingObjects();
        } catch (UnmarshalOutOfBoundsException oob) {
            // ok, returning null.
        } catch (Ice.MarshalException me) {
            // less specific than oob; not great, but returning null. #5662
            log.error(String.format("MarshalException: %s (len=%s)",
                    me.reason, data.length));
        } catch (OutOfMemoryError oom) {
            // Not ok, but not much we can do.
            // This is caused by changes to slice files.
            // See:
            log.warn("http://www.zeroc.com/forums/bug-reports/4782-3-3-1-outofmemory-client-when-slice-definition-modified.html");
        } finally {
            is.destroy();
        }
        return params[0];
    }

    /**
     * Interface added in order to allow ParamHelper instances to use methods
     * from SharedResourcesI. The build does not allow for a dependency between
     * the two.
     */
    // TODO refactor
    public interface Acquirer {
        public InteractiveProcessorPrx acquireProcessor(final Job submittedJob,
                int seconds, final Current current) throws ServerError;
    }

}