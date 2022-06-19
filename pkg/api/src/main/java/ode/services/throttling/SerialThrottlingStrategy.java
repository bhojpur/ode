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
import ode.services.server.util.IceMethodInvoker;
import ode.system.OdeContext;
import ode.util.IceMapper;
import Ice.Current;

/**
 * Throttling implementation which only allows a single invocation to be run at
 * any given time.
 * 
 */
public class SerialThrottlingStrategy extends AbstractThrottlingStrategy {

    private final Slot slot;

    private final Queue queue;

    public SerialThrottlingStrategy(OdeContext ctx) {
        queue = new Queue(ctx);
        slot = new Slot(queue);
    }

    public void callInvokerOnRawArgs(ServiceInterface service,
            IceMethodInvoker invoker, Object __cb, Ice.Current __current,
            Object... args) {
        IceMapper mapper = new IceMapper();
        Callback cb = new Callback(service, invoker, mapper, __cb, __current,
                args);
        queue.put(cb);
    }

    public void callInvokerWithMappedArgs(ServiceInterface service,
            IceMethodInvoker invoker, IceMapper mapper, Object __cb,
            Current __current, Object... args) {
        Callback cb = new Callback(service, invoker, mapper, __cb, __current,
                args);
        queue.put(cb);
    }

    public void runnableCall(Current __current, Task runnable) {
        throw new UnsupportedOperationException();
    }

    public <R> void safeRunnableCall(Current __current, Object __cb, boolean isVoid, Callable<R> callable) {
        throw new UnsupportedOperationException();
    }

}