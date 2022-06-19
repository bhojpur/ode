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

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import ode.ServerError;
import Ice.Current;

public class ProcessCallbackI extends _ProcessCallbackDisp {

    public enum Action {
        FINISHED,
        CANCELLED,
        KILLED;
    }

    private final Ice.ObjectAdapter adapter;

    private final Ice.Identity id;

    private final BlockingQueue<Action> q = new LinkedBlockingQueue<Action>();

    private final boolean poll;

    /**
     * Proxy passed to this instance on creation. Can be used by subclasses
     * freely. The object will not be nulled, but may be closed server-side.
     */
    protected final ProcessPrx process;

    public ProcessCallbackI(ProcessCallbackI pcb) throws ServerError {
        this(pcb.adapter, pcb.id.category, pcb.process);
    }
    
    public ProcessCallbackI(ode.client client, ProcessPrx process)
    throws ServerError {
        this(client, process, true);
    }

    public ProcessCallbackI(ode.client client, ProcessPrx process, boolean poll)
    throws ServerError {
        this(client.getAdapter(), client.getCategory(), process, poll);
    }

    public ProcessCallbackI(Ice.ObjectAdapter adapter, String category,
            ProcessPrx process) throws ServerError {
        this(adapter, category, process, true);
    }

    public ProcessCallbackI(Ice.ObjectAdapter adapter, String category,
            ProcessPrx process, boolean poll)
        throws ServerError {

        this.adapter = adapter;
        this.poll = poll;
        this.process = process;
        this.id = new Ice.Identity();
        this.id.name = UUID.randomUUID().toString();
        this.id.category = category;
        Ice.ObjectPrx prx = adapter.add(this, id);
        ProcessCallbackPrx cb = ProcessCallbackPrxHelper.uncheckedCast(prx);
        process.registerCallback(cb);
    }

    /**
     *
     * Should only be used if the default logic of the process methods is kept
     * in place. If "q.put" does not get called, this method will always
     * block for the given milliseconds.
     */
    public Action block(long ms) throws InterruptedException {
        if (poll) {
            try {
                ode.RInt rc = process.poll();
                if (rc != null) {
                    processFinished(rc.getValue(), null);
                }
            } catch (Exception e) {
                System.err.println("Error calling poll:" + e);
            }
        }
        return q.poll(ms, TimeUnit.MILLISECONDS);
    }

    public void processCancelled(boolean success, Current __current) {
        try {
            q.put(Action.CANCELLED);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void processFinished(int returncode, Current __current) {
        try {
            q.put(Action.FINISHED);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void processKilled(boolean success, Current __current) {
        try {
            q.put(Action.KILLED);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void close() {
         adapter.remove(id); // OK ADAPTER USAGE
     }
}