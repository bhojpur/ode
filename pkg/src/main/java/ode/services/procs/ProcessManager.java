package ode.services.procs;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ode.api.JobHandle;
import ode.model.IObject;
import ode.model.jobs.Job;
import ode.model.jobs.JobStatus;
import ode.parameters.Parameters;
import ode.security.SecureAction;
import ode.security.SecuritySystem;
import ode.services.sessions.SessionManager;
import ode.services.util.ExecutionThread;
import ode.services.util.Executor;
import ode.system.Principal;
import ode.system.ServiceFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Session;

public class ProcessManager extends ExecutionThread implements IProcessManager {

    /**
     * Task performed by the {@link ProcessManager} on each invocation of
     * {@link ProcessManager#run()}.
     */
    public static class Work implements Executor.Work {

        /**
         * processors available for processing. This array will never be null.
         */
        final protected List<Processor> processors;

        /**
         * {@link SecuritySystem} in order to perform a secure save on the
         * {@link Job} instance.
         */
        final protected SecuritySystem sec;

        /**
         * Map of all active {@link Process processes}.
         */
        protected Map<Long, Process> procMap = Collections
                .synchronizedMap(new HashMap<Long, Process>());

        public Work(SecuritySystem sec, Processor... procs) {
            this.sec = sec;
            if (procs == null) {
                this.processors = new ArrayList<Processor>();
            } else {
                this.processors = Arrays.asList(procs);
            }
        }

        public String description() {
            return "ProcessManager";
        }
        
        public List<Job> doWork(Session session, ServiceFactory sf) {
            final List<Job> jobs = sf.getQueryService().findAllByQuery(
                    "select j from Job j where status.id = :id",
                    new Parameters().addId(getSubmittedStatus(sf).getId()));

            for (Job job : jobs) {
                startProcess(sf, job.getId());
            }

            return null;
        }

        /**
         * 
         */
        public void startProcess(final ServiceFactory sf, final long jobId) {
            Process p = null;

            for (Processor proc : processors) {
                p = proc.process(jobId);
                // Take first processor
                if (p != null) {
                    break;
                }
            }

            if (p == null) {
                if (log.isWarnEnabled()) {
                    log.warn("No processor found for job:" + jobId);
                }

                Job job = job(sf, jobId);
                job.setStatus(getWaitingStatus(sf));
                job.setMessage("No processor found for job.");
                sec.doAction(new SecureAction() {
                    public <T extends IObject> T updateObject(T... objs) {
                        return sf.getUpdateService().saveAndReturnObject(
                                objs[0]);
                    }

                }, job);
            } else {
                procMap.put(jobId, p);
            }

        }

        // Helpers ~
        // =========================================================================

        protected Job job(ServiceFactory sf, long id) {
            Job job = sf.getQueryService().find(Job.class, id);
            return job;
        }

        private JobStatus getStatus(ServiceFactory sf, String status) {
            JobStatus statusObj = sf.getTypesService().getEnumeration(
                    JobStatus.class, status);
            return statusObj;
        }

        private JobStatus getSubmittedStatus(ServiceFactory sf) {
            return getStatus(sf, JobHandle.SUBMITTED);
        }

        private JobStatus getWaitingStatus(ServiceFactory sf) {
            return getStatus(sf, JobHandle.WAITING);
        }
    } // End Work

    //
    // ProcessManager
    //

    private static Logger log = LoggerFactory.getLogger(ProcessManager.class);

    private static Principal PRINCIPAL = new Principal("root", "user",
            "Processing");

    /**
     * main constructor which takes a non-null array of {@link Processor}
     * instances as its only argument. This array is copied, so modifications
     * will not be noticed.
     *
     * @param manager
     *              Reference to the session manager.
     * @param sec
     *          Reference to the security manager
     * @param executor
     *              The executor.
     * @param procs
     *            Array of Processors. Not null.
     */
    public ProcessManager(SessionManager manager, SecuritySystem sec,
            Executor executor, Processor... procs) {
        super(manager, executor, new Work(sec, procs), PRINCIPAL);
    }

    // Main methods ~
    // =========================================================================

    @Override
    @SuppressWarnings("unchecked")
    public void doRun() {

        if (log.isDebugEnabled()) {
            log.debug("Starting processing...");
        }

        try {

            this.executor.execute(getPrincipal(), this.work);

        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Error while processing", e);
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Finished processing...");
        }

    }

    public Process runningProcess(long jobId) {
        Process p = ((Work) work).procMap.get(jobId);
        return p;
    }

}
