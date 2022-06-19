package ode.services.throttling;

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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import ode.system.OdeContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** * Manages AMD-based method dispatches from server. * */
public class Queue {

    private final static Logger log = LoggerFactory.getLogger(Queue.class);

    static class CancelledException extends Exception {
    }

    private final OdeContext ctx;
    private final BlockingQueue<Callback> q = new LinkedBlockingQueue<Callback>();
    private final AtomicBoolean done = new AtomicBoolean();

    public Queue(OdeContext ctx) {
        done.set(false);
        this.ctx = ctx;
    }

    public void put(Callback callback) {
        boolean cont = !done.get();
        if (cont) {
            while (true) {
                try {
                    q.put(callback);
                    break;
                } catch (InterruptedException e) {
                    log.warn("Queue interrupted during put");
                }
            }
        } else {
            callback.exception(new CancelledException(), ctx);
        }
    }

    public Callback take() {
        Callback cb = null;
        while (true) {
            try {
                cb = q.take();
            } catch (InterruptedException e) {
                log.warn("Queue interrupted during take");
            }
            break;
        }
        return cb;
    }

    public void destroy() {
        boolean wasDone = done.getAndSet(true);
        if (!wasDone) {
            for (Callback cb : q) {
                cb.exception(new CancelledException(), ctx);
            }
        }
    }
}