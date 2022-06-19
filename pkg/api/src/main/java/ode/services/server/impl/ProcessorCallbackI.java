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

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import ode.system.EventContext;
import ode.services.server.fire.TopicManager;
import ode.services.server.util.ResultHolder;

import ode.system.Principal;

import ode.ServerError;
import ode.constants.categories.PROCESSORCALLBACK;
import ode.constants.topics.PROCESSORACCEPTS;
import ode.grid.ProcessorCallbackPrx;
import ode.grid.ProcessorCallbackPrxHelper;
import ode.grid.ProcessorPrx;
import ode.grid.ProcessorPrxHelper;
import ode.grid._ProcessorCallbackOperations;
import ode.grid._ProcessorCallbackTie;
import ode.model.Job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Ice.Current;

/**
 * Callback used to lookup active processors via IceStorm.
 */
public class ProcessorCallbackI extends AbstractAmdServant
    implements _ProcessorCallbackOperations {

    private final static Logger log = LoggerFactory.getLogger(ProcessorCallbackI.class);

    private final Job job;

    private final ServiceFactoryI sf;

    private final ResultHolder<ProcessorPrx> holder;

    private final AtomicInteger responses = new AtomicInteger(0);

    /**
     * Simplified constructor used to see if any usermode processor is active
     * for either the current group or the current user. Currently uses a
     * hard-coded value of 5 seconds wait time. For more control, pass in a
     * {@link ResultHolder} instance.
     */
    public ProcessorCallbackI(ServiceFactoryI sf) {
        this(sf, new ResultHolder<ProcessorPrx>(5 * 1000), null);
    }

    /**
     * Primary constructor. Asks processors if they will accept the given job
     * for the current user and the current group.
     *
     * @param sf
     *            Cannot be null.
     * @param holder
     *            Cannot be null.
     * @param job
     *            Can be null.
     */
    public ProcessorCallbackI(ServiceFactoryI sf, ResultHolder<ProcessorPrx> holder,
            Job job) {
        super(null, null);
        this.sf = sf;
        this.job = job;
        this.holder = holder;
    }

    /**
     * Return the number of times this instance has been called in a thread
     * safe manner.
     * @return See above.
     */
    public int getResponses() {
        return responses.get();
    }

    /**
     * Generates a UUID-based {@link Ice.Identity} with the category of
     * {@link PROCESSORCALLBACK#value} and then calls
     * {@link #activateAndWait(Current, Ice.Identity)}.
     * @return See above.
     */
    public ProcessorPrx activateAndWait(Ice.Current current) throws ServerError {
        Ice.Identity acceptId = new Ice.Identity();
        acceptId.name = UUID.randomUUID().toString();
        acceptId.category = PROCESSORCALLBACK.value;
        return activateAndWait(current, acceptId);
    }

    /**
     * Primary method which adds this instance to IceStorm, waits for a response
     * from any active processor services, and finally unregister itself before
     * returning the first processor instance which responded.

     * @param current
     * @param acceptId
     * @return See above.
     * @throws ServerError
     */
    public ProcessorPrx activateAndWait(Ice.Current current,
            Ice.Identity acceptId) throws ServerError {
        Ice.ObjectPrx prx = sf.registerServant(acceptId,
                new _ProcessorCallbackTie(this));

        try {
            prx = sf.adapter.createDirectProxy(acceptId);
            ProcessorCallbackPrx cbPrx = ProcessorCallbackPrxHelper
                    .uncheckedCast(prx);

            EventContext ec = sf.getEventContext(current);

            TopicManager.TopicMessage msg = new TopicManager.TopicMessage(this,
                    PROCESSORACCEPTS.value, new ProcessorPrxHelper(),
                    "willAccept", new ode.model.ExperimenterI(ec
                            .getCurrentUserId(), false),
                    new ode.model.ExperimenterGroupI(ec
                            .getCurrentGroupId(), false),
                            this.job, cbPrx);
            sf.topicManager.onApplicationEvent(msg);
            return holder.get();
        } finally {
            sf.unregisterServant(acceptId);
        }
    }

    /**
     * Callback method called by the remote processor instance.
     * for reasons this method should not be used.
     */
    @Deprecated
    public void isAccepted(boolean accepted, String sessionUuid,
            String procConn, Current __current) {
        isProxyAccepted(accepted, sessionUuid, ProcessorPrxHelper.checkedCast(
            sf.adapter.getCommunicator().stringToProxy(procConn)), __current);
    }

    /**
     * Callback method called by the remote processor instance.
     */
    public void isProxyAccepted(boolean accepted, String sessionUuid,
            ProcessorPrx procProxy, Current __current) {

        responses.incrementAndGet();
        Exception exc = null;
        String reason = "because false returned";

        if (accepted) {
            String procLog = sf.adapter.getCommunicator().proxyToString(procProxy);
            log.debug(String.format(
                    "Processor with session %s returned %s accepted",
                    sessionUuid, procLog, accepted));
            try {
                EventContext procEc = sf.sessionManager
                        .getEventContext(new Principal(sessionUuid));
                EventContext ec = sf.getEventContext(__current);
                if (procEc.isCurrentUserAdmin()
                        || procEc.getCurrentUserId().equals(
                                ec.getCurrentUserId())) {
                    this.holder.set(ProcessorPrxHelper.checkedCast(procProxy));
                    return;  // EARLY EXIT
                } else {
                    reason = "since disallowed";
                }
            } catch (Ice.ObjectNotExistException onee) {
                exc = onee;
                reason = "due to ObjectNotExistException: " + procLog;
            } catch (Exception e) {
                exc = e;
                reason = "due to exception: " + e.getMessage();
            }
        }

        String msg = String.format("Processor with session %s rejected %s",
                sessionUuid, reason);
        if (exc != null) {
            log.warn(msg, exc);
        } else {
            log.debug(msg);
        }
        this.holder.set(null);

    }

    /**
     * Callback method which should not be called for this instance.
     */
    public void responseRunning(List<Long> jobIds, Current __current) {
        log.error("responseRunning should not have been called");
    }

}