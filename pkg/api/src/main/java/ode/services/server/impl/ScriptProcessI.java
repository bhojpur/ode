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

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Ice.Current;

import ode.services.procs.scripts.ScriptProcess;

import ode.RInt;
import ode.RType;
import ode.ServerError;
import ode.api.JobHandlePrx;
import ode.constants.categories.PROCESSCALLBACK;
import ode.grid.InteractiveProcessorPrx;
import ode.grid.ProcessCallbackI;
import ode.grid.ProcessCallbackPrx;
import ode.grid.ProcessPrx;
import ode.grid.ScriptProcessPrx;
import ode.grid.ScriptProcessPrxHelper;
import ode.grid._InteractiveProcessorOperations;
import ode.grid._ScriptProcessOperations;
import ode.grid._ScriptProcessTie;
import ode.model.ScriptJob;

/**
 *
 */
public class ScriptProcessI extends AbstractAmdServant
    implements _ScriptProcessOperations {

    private static Logger log = LoggerFactory.getLogger(ScriptProcess.class);

    private final InteractiveProcessorPrx processorPrx;

    private final _InteractiveProcessorOperations processor;

    private final ProcessPrx process;

    private final ProcessCallbackI cb;

    private final ServiceFactoryI sf;

    private final ScriptProcessPrx self;

    private final Ice.Identity id;

    private final long jobId;

    public ScriptProcessI(ServiceFactoryI sf, Ice.Current current,
            InteractiveProcessorPrx processorPrx,
            _InteractiveProcessorOperations processor, ProcessPrx process)
            throws ServerError {
        super(null, null);
        this.jobId = processor.getJob(current).getId().getValue();
        this.processorPrx = processorPrx;
        this.processor = processor;
        this.process = process;
        this.sf = sf;
        this.cb = new ProcessCallbackI(sf.getAdapter(), "ProcessCallback", process);
        this.id = new Ice.Identity(UUID.randomUUID().toString(), PROCESSCALLBACK.value);
        this.self = ScriptProcessPrxHelper.uncheckedCast(
                sf.registerServant(this.id, new _ScriptProcessTie(this)));
        sf.allow(this.self);
    }

    public ScriptProcessPrx getProxy() {
        return self;
    }

    // Processor delegates
    // =========================================================================

    public void close(boolean detach, Current __current) throws ServerError {
        processor.setDetach(detach, __current);
        processor.stop(__current);
        sf.unregisterServant(processorPrx.ice_getIdentity());
        sf.unregisterServant(self.ice_getIdentity());
        this.cb.close();
    }

    public ScriptJob getJob(Current __current) throws ServerError {
        return (ScriptJob) processor.getJob(__current);
    }

    public Map<String, RType> getResults(int waitSecs, Current __current)
            throws ServerError {

        if (waitSecs > 5 || 0 > waitSecs) {
            throw new ode.ApiUsageException(null, null,
                    "Refusing to wait more than 5 seconds: " + waitSecs);
        }

        try {
            cb.block(waitSecs * 1000);
        } catch (InterruptedException e) {
            // ok
        }
        return processor.getResults(process, __current).getValue();
    }

    public String setMessage(String message, Current __current)
            throws ServerError {

        JobHandlePrx jh = sf.createJobHandle(null);
        try {
            jh.attach(jobId);
            return jh.setMessage(message);
        } finally {
            jh.close();
        }
    }

    // Process delegates
    // =========================================================================

    public int _wait(Current __current) throws ServerError {
        return process._wait();
    }

    public boolean cancel(Current __current) throws ServerError {
        return process.cancel();
    }

    public boolean kill(Current __current) {
        return process.kill();
    }

    public RInt poll(Current __current) throws ServerError {
        return process.poll();
    }

    public void registerCallback(ProcessCallbackPrx cb, Current __current)
            throws ServerError {
        process.registerCallback(cb);
    }

    public void shutdown(Current __current) {
        process.shutdown();
    }

    public void unregisterCallback(ProcessCallbackPrx cb, Current __current)
            throws ServerError {
        process.unregisterCallback(cb);
    }

}