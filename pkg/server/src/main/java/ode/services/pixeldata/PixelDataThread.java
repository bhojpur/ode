package ode.services.pixeldata;

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

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import ode.conditions.InternalException;
import ode.io.messages.MissingPyramidMessage;
import ode.model.core.Pixels;
import ode.model.enums.EventType;
import ode.model.meta.Event;
import ode.model.meta.EventLog;
import ode.model.meta.Experimenter;
import ode.model.meta.ExperimenterGroup;
import ode.security.basic.CurrentDetails;
import ode.services.sessions.SessionManager;
import ode.services.util.ExecutionThread;
import ode.services.util.Executor;
import ode.services.util.ReadOnlyStatus;
import ode.system.EventContext;
import ode.system.Principal;
import ode.system.ServiceFactory;
import ode.system.metrics.Metrics;
import ode.system.metrics.NullMetrics;
import ode.system.metrics.Timer;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.transaction.annotation.Transactional;

public class PixelDataThread extends ExecutionThread implements ApplicationListener<MissingPyramidMessage> {

    private final static Logger log = LoggerFactory.getLogger(PixelDataThread.class);

    private final static Principal DEFAULT_PRINCIPAL = new Principal("root",
            "system", "Task");

    private final static int DEFAULT_THREADS = 1;

    /** Server session UUID */
    private final String uuid;

    /** Number of threads that should be used for processing **/
    private final int numThreads;

    /**
     * Whether this thread should perform actual processing or simply add a
     * PIXELDATA {@link EventLog}. For the moment, this is determined based
     * on whether "pixelDataTrigger" is defined. If yes, then this is the
     * standalone pixel data processor using the ode/services/pixeldata.xml
     * Spring configuration. Otherwise, it's the main ODE Server process.
     */
    private final boolean performProcessing;

    private final Timer batchTimer;

    private final ReadOnlyStatus readOnly;

    /**
     * Uses default {@link Principal} for processing
     */
    @Deprecated
    public PixelDataThread(SessionManager manager, Executor executor,
            PixelDataHandler handler, String uuid) {
        this(manager, executor, handler, DEFAULT_PRINCIPAL, uuid, DEFAULT_THREADS);
    }

    /**
     * Uses default {@link Principal} for processing and a {@link NullMetrics}
     * instance.
     */
    @Deprecated
    public PixelDataThread(SessionManager manager, Executor executor,
            PixelDataHandler handler, String uuid, int numThreads) {
        this(manager, executor, handler, DEFAULT_PRINCIPAL, uuid, numThreads,
                new NullMetrics());
    }

    /**
     * Calculates {@link #performProcessing} based on the existence of the
     * "pixelDataTrigger" and passes all parameters to
     * {@link #PixelDataThread(boolean, SessionManager, Executor, PixelDataHandler, Principal, String, int) the main ctor}
     * passing a {@link NullMetrics} as necessary.
     */
    @Deprecated
    public PixelDataThread(SessionManager manager, Executor executor,
            PixelDataHandler handler, Principal principal, String uuid,
            int numThreads) {
        this(executor.getContext().containsBean("pixelDataTrigger"),
            manager, executor, handler, principal, uuid, numThreads,
            new NullMetrics());
    }

    /**
     * Calculates {@link #performProcessing} based on the existence of the
     * "pixelDataTrigger" and passes all parameters to
     * {@link #PixelDataThread(boolean, SessionManager, Executor, PixelDataHandler, Principal, String, int) the main ctor}.
     */
    @Deprecated
    public PixelDataThread(
            SessionManager manager, Executor executor,
            PixelDataHandler handler, String uuid,
            int numThreads, Metrics metrics) {
        this(executor.getContext().containsBean("pixelDataTrigger"),
                manager, executor, handler, DEFAULT_PRINCIPAL,
                uuid, numThreads, metrics);
    }

    /**
     * Calculates {@link #performProcessing} based on the existence of the
     * "pixelDataTrigger" and passes all parameters to
     * {@link #PixelDataThread(boolean, SessionManager, Executor, PixelDataHandler, Principal, String, int) the main ctor}.
     */
    public PixelDataThread(
            SessionManager manager, Executor executor,
            PixelDataHandler handler, String uuid,
            int numThreads, Metrics metrics, ReadOnlyStatus readOnly) {
        this(executor.getContext().containsBean("pixelDataTrigger"),
                manager, executor, handler, DEFAULT_PRINCIPAL,
                uuid, numThreads, metrics, readOnly);
    }

    /**
     * Calculates {@link #performProcessing} based on the existence of the
     * "pixelDataTrigger" and passes all parameters to
     * {@link #PixelDataThread(boolean, SessionManager, Executor, PixelDataHandler, Principal, String, int) the main ctor}.
     */
    @Deprecated
    public PixelDataThread(
            SessionManager manager, Executor executor,
            PixelDataHandler handler, Principal principal, String uuid,
            int numThreads, Metrics metrics) {
        this(executor.getContext().containsBean("pixelDataTrigger"),
                manager, executor, handler, principal,
                uuid, numThreads, metrics);
    }

    /**
     * Calculates {@link #performProcessing} based on the existence of the
     * "pixelDataTrigger" and passes all parameters to
     * {@link #PixelDataThread(boolean, SessionManager, Executor, PixelDataHandler, Principal, String, int) the main ctor}.
     */
    @Deprecated
    public PixelDataThread(
            SessionManager manager, Executor executor,
            PixelDataHandler handler, Principal principal, String uuid,
            int numThreads, Metrics metrics, ReadOnlyStatus readOnly) {
        this(executor.getContext().containsBean("pixelDataTrigger"),
                manager, executor, handler, principal,
                uuid, numThreads, metrics, readOnly);
    }

    /**
     * Calls main constructor with {@link NullMetrics}.
     */
    public PixelDataThread(boolean performProcessing,
            SessionManager manager, Executor executor,
            PixelDataHandler handler, Principal principal, String uuid,
            int numThreads) {
        this(performProcessing, manager, executor, handler, principal,
                uuid, numThreads, new NullMetrics());
    }

    /**
     * Calls main constructor with read-only status being all read-write.
     */
    public PixelDataThread(boolean performProcessing,
            SessionManager manager, Executor executor,
            PixelDataHandler handler, Principal principal, String uuid,
            int numThreads, Metrics metrics) {
        this(performProcessing, manager, executor, handler, principal,
                uuid, numThreads, metrics, new ReadOnlyStatus(false, false));
    }

    /**
     * Main constructor. No arguments can be null.
     */
    public PixelDataThread(boolean performProcessing,
            SessionManager manager, Executor executor,
            PixelDataHandler handler, Principal principal, String uuid,
            int numThreads, Metrics metrics, ReadOnlyStatus readOnly) {
        super(manager, executor, handler, principal);
        this.performProcessing = performProcessing;
        this.uuid = uuid;
        this.numThreads = numThreads;
        this.batchTimer = metrics.timer(this, "batch");
        this.readOnly = readOnly;
    }

    /**
     * Called by Spring on creation. Currently a no-op.
     */
    public void start() {
        StringBuilder sb = new StringBuilder();
        sb.append("Initializing PixelDataThread");
        if (performProcessing) {
            sb.append(String.format(" (threads=%s)", numThreads));
        } else {
            sb.append(" (create events only)");
        }
        log.info(sb.toString());
    }

    /**
     * Loads event logs from the {@link PixelDataHandler} processing them
     * all then in a background thread via a {@link ExecutorCompletionService}.
     *
     * {@link #numThreads} variable is also used there, so the value returned
     * <em>should</em> match. In case it isn't, we additionally use an
     * {@link ArrayBlockingQueue} to hold the results.
     */
    @Override
    public void doRun() {
        if (performProcessing) {

            final ExecutorCompletionService<Object> ecs =
                new ExecutorCompletionService<Object>(executor.getService(),
                        new ArrayBlockingQueue<Future<Object>>(numThreads));

            @SuppressWarnings("unchecked")
            List<EventLog> eventLogs = (List<EventLog>)
                    executor.execute(getPrincipal(), work);

            for (final EventLog log : eventLogs) {
                ecs.submit(new Callable<Object>(){
                    @Override
                    public Object call()
                        throws Exception
                    {
                        return go(log);
                    }
                });
            }

            int count = eventLogs.size();
            while (count > 0) {
                try {
                    Future<Object> future = ecs.poll(500, TimeUnit.MILLISECONDS);
                    if (future != null && future.get() != null) {
                        count--;
                    }
                } catch (ExecutionException ee) {
                    onExecutionException(ee);
                } catch (InterruptedException ie) {
                    log.debug("Interrupted; looping", ie);
                }
            }
        }
    }

    /**
     * {@link Executor.Work} implementation for the second phase of PixelData
     * processing. Once the {@link EventLog} instances are available, each
     * should be passed to a new {@link HandleEventLog} instance and then
     * processed in a background thread.
     */
    private static class HandleEventLog extends Executor.SimpleWork {

        private final PixelDataHandler handler;

        private final EventLog log;

        HandleEventLog(EventLog log, PixelDataHandler handler,
                Object self, String description, Object...args) {
            super(self, description, args);
            this.handler = handler;
            this.log = log;
        }

        @Transactional(readOnly=false)
        @Override
        public Object doWork(Session session, ServiceFactory sf) {
            this.handler.handleEventLog(log, session, sf);
            return null;
        }

    }

    private Object go(EventLog log) {
        final Timer.Context timer = batchTimer.time();
        try {
             executor.execute(getPrincipal(),
                     new HandleEventLog(log, (PixelDataHandler) work,
                             this, "handleEventLog"));
             return log;
        } finally {
            timer.stop();
        }
    }

    /**
     * Basic handling just logs at ERROR level. Subclasses (especially for
     * testing) can do more.
     */
    protected void onExecutionException(ExecutionException ee) {
        log.error("ExceptionException!", ee.getCause());
    }

    /**
     * Called by Spring on destruction.
     */
    public void stop() {
        log.info("Shutting down PixelDataThread");
        ((PixelDataHandler) this.work).loader.setStop(true);
    }

    /**
     * Called in the main server (Server-0) in order to create a PIXELDATA
     * {@link EventLog} which will get processed by PixelData-0.
     */
    public void onApplicationEvent(final MissingPyramidMessage mpm) {
        if (readOnly.isReadOnlyDb()) {
            log.debug("Ignored: " + mpm);
            return;
        }
        log.info("Received: " + mpm);
        // #5232. If this is called without an active event, then throw
        // an exception since a call to Executor should wrap whatever the
        // invoker is doing.
        final CurrentDetails cd = executor.getContext().getBean(CurrentDetails.class);
        if (cd.size() <= 0) {
            throw new InternalException("Not logged in.");
        }
        final EventContext ec = cd.getCurrentEventContext();
        if (null == ec.getCurrentUserId()) {
            throw new InternalException("No user! Must be wrapped by call to Executor?");
        }

        Future<EventLog> future = this.executor.submit(cd.getContext(),
                new Callable<EventLog>(){
            public EventLog call() throws Exception {
                return makeEvent(ec, mpm);
            }});
        this.executor.get(future);
    }

    private EventLog makeEvent(final EventContext ec,
                               final MissingPyramidMessage mpm) {

        final Principal p = new Principal(uuid);
        final Map<String, String> callContext = new HashMap<String, String>();

        // First call is with -1 in order to find the pixels group.
        // TODO: this could equally be done with sqlAction.
        callContext.put("ode.group", "-1");
        final Long groupID = (Long) this.executor.execute(callContext, p,
                new Executor.SimpleWork(this, "getGroupId") {
            @Transactional(readOnly = true)
            public Object doWork(Session session, ServiceFactory sf) {
                final ExperimenterGroup group = sf.getQueryService().findByQuery(
                        "select p.details.group from Pixels p where p.id = :id",
                        new ode.parameters.Parameters().addId(mpm.pixelsID));
                return group.getId();
            }
        });

        // Reset to prevent "Not intended for copying" errors
        callContext.put("ode.group", groupID.toString());
        return (EventLog) this.executor.execute(callContext, p,
                new Executor.SimpleWork(this, "createEvent") {
        @Transactional(readOnly = false)
        public Object doWork(Session session, ServiceFactory sf) {
            log.info("Creating PIXELDATA event for pixels id:"
                    + mpm.pixelsID);

                // Load objects
                final EventType type = sf.getTypesService().getEnumeration(
                        EventType.class, ec.getCurrentEventType());
                final EventLog el = new EventLog();
                final Event e = new Event();
                e.setExperimenter(
                        new Experimenter(ec.getCurrentUserId(), false));
                e.setExperimenterGroup(new ExperimenterGroup(groupID, false));
                e.setSession(new ode.model.meta.Session(
                        ec.getCurrentSessionId(), false));
                e.setTime(new Timestamp(new Date().getTime()));
                e.setType(type);
                el.setAction("PIXELDATA");
                el.setEntityId(mpm.pixelsID);
                el.setEntityType(Pixels.class.getName());
                el.setEvent(e);
                return sf.getUpdateService().saveAndReturnObject(el);
            }
        });
    }
}
