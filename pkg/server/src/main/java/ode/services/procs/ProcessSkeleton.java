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

import java.util.HashSet;
import java.util.Set;

import ode.conditions.ApiUsageException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessSkeleton implements Process {

    private final Logger log;

    private final Set<ProcessCallback> cbs = new HashSet<ProcessCallback>();

    private final Processor processor;

    private boolean cancelled, finished;

    public ProcessSkeleton(Processor p) {
        this.processor = p;
        this.log = LoggerFactory.getLogger(this.getClass());
    }

    public Processor processor() {
        return this.processor;
    }

    public void registerCallback(ProcessCallback cb) {
        synchronized (cbs) {
            checkState();
            boolean added = cbs.add(cb);
            debug("Added", cb, added);
        }
    }

    public void unregisterCallback(ProcessCallback cb) {
        synchronized (cbs) {
            checkState();
            boolean removed = cbs.remove(cb);
            debug("removed", cb, removed);
        }
    }

    public void cancel() {
        synchronized (cbs) {
            checkState();
            for (ProcessCallback cb : cbs) {
                try {
                    cb.processCancelled(this);
                } catch (Exception e) {
                    log
                            .warn(String.format("Exception thrown by %s while "
                                    + "cancelling %s. Removing callback.", cb,
                                    this), e);
                    cbs.remove(cb);
                }
            }
            cbs.clear();
            cancelled = true;
            if (log.isDebugEnabled()) {
                log.debug("Process cancelled: " + this);
            }
        }
    }

    public void finish() {
        synchronized (cbs) {
            checkState();
            for (ProcessCallback cb : cbs) {
                try {
                    cb.processFinished(this);
                } catch (Exception e) {
                    log.warn(String.format("Exception throw by %s while "
                            + "finished %s. Removing callback.", cb, this), e);
                    cbs.remove(cb);
                }
            }
            cbs.clear();
            finished = true;
            if (log.isDebugEnabled()) {
                log.debug("Process finished: " + this);
            }

        }
    }

    public boolean isActive() {
        return !cancelled && !finished;
    }

    protected void checkState() {
        if (!isActive()) {
            String state = cancelled ? "cancelled" : "finished";
            throw new ApiUsageException(String.format("Process already %s: %s",
                    state, this));
        }
    }

    private void debug(String action, ProcessCallback cb, boolean added) {
        if (log.isDebugEnabled()) {
            if (added) {
                log.debug(String.format("%s %s to %s", cb, action, this));
            } else {
                log.debug(String
                        .format("%s already %s to %s", cb, action, this));
            }
        }
    }
}
