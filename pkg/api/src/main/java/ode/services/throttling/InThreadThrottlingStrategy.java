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

import java.util.concurrent.Callable;

import ode.api.ServiceInterface;
import ode.security.basic.CurrentDetails;
import ode.services.server.util.IceMethodInvoker;
import ode.util.IceMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Ice.Current;

/**
 * Throttling implementation which uses the calling server {@link Thread} for
 * execution. This mimics the behavior of the pre-AMD server
 */
public class InThreadThrottlingStrategy extends AbstractThrottlingStrategy {

    private final static Logger log = LoggerFactory
            .getLogger(InThreadThrottlingStrategy.class);

    private final CurrentDetails cd;

    public InThreadThrottlingStrategy(CurrentDetails cd) {
        this.cd = cd;
    }

    void setup(Ice.Current current) {
        if (current != null) {
            cd.setContext(current.ctx);
        }
    }

    void teardown() {
        cd.setContext(null);
    }

    public void callInvokerOnRawArgs(ServiceInterface service,
            IceMethodInvoker invoker, Object __cb, Ice.Current __current,
            Object... args) {

        setup(__current);
        try {
            IceMapper mapper = new IceMapper();
            Callback cb = new Callback(service, invoker, mapper, __cb,
                    __current, args);
            cb.run(ctx);
        } finally {
            teardown();
        }
    }

    public void callInvokerWithMappedArgs(ServiceInterface service,
            IceMethodInvoker invoker, IceMapper mapper, Object __cb,
            Current __current, Object... args) {

        setup(__current);
        try {
            Callback cb = new Callback(service, invoker, mapper, __cb,
                    __current, args);
            cb.run(ctx);
        } finally {
            teardown();
        }
    }

    public <R> void safeRunnableCall(Current __current, Object __cb, boolean isVoid, Callable<R> callable) {
        setup(__current);
        try {
            Callback2<R> cb = new Callback2<R>(__current, __cb, isVoid, callable);
            cb.run(ctx);
        } finally {
            teardown();
        }
    }

    public void runnableCall(Current __current, Task runnable) {
        setup(__current);
        try {
            runnable.run(ctx);
        } catch (Exception e) {
            log.error("Exception during runnableCall", e);
        } finally {
            teardown();
        }
    }

}