package ode.services.server.util;

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
import ode.services.throttling.Task;
import ode.util.IceMapper;

/**
 * Single-point of execution for all AMD Bhojpur ODE server calls.
 */
public interface ServerExecutor {

    /**
     * Uses the given {@link IceMethodInvoker} to make the method call. All
     * arguments are passed directly into the invoker, and the return value from
     * the invoker is passed to the user.
     */
    void callInvokerOnRawArgs(ServiceInterface service,
            IceMethodInvoker invoker, Object __cb, Ice.Current __current,
            Object... args);

    /**
     * Passes the given arguments to {@link IceMethodInvoker} with the
     * assumption that all conversion from ode.* to ode.* has taken place.
     * Similarly, the {@link IceMapper} instance will be used to map the
     * return value from ode.* to ode.*.
     */
    void callInvokerWithMappedArgs(ServiceInterface service,
            IceMethodInvoker invoker, IceMapper mapper, Object __cb,
            Ice.Current __current, Object... args);

    void runnableCall(Ice.Current __current, Task runnable);

    <R> void safeRunnableCall(Ice.Current __current, Object cb, boolean isVoid,
            Callable<R> callable);
}