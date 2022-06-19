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

import ode.services.server.util.ServerExecutor;
import ode.services.util.Executor;
import ode.system.OdeContext;
import ode.system.Principal;
import ode.util.IceMapper;

import org.springframework.util.Assert;

/**
 * Simple adapter which takes a {@link ode.services.util.Executor.Work} instance
 * and executes it as a {@link ServerExecutor} task. All exceptions are caught and
 * routed to via
 * the {@link #exception(Throwable, OdeContext)} method to the provided callback.
 */
public class Adapter extends Task {

    private final Executor ex;
    private final Executor.Work work;
    private final Principal p;
    private final IceMapper mapper;

    public Adapter(Object callback, Ice.Current current, IceMapper mapper,
            Executor ex, Principal p, Executor.Work work) {
        super(callback, current, mapper.isVoid());
        this.p = p;
        this.ex = ex;
        this.work = work;
        this.mapper = mapper;
        Assert.notNull(callback, "Callback null");
        Assert.notNull(work, "Work null");
        Assert.notNull(ex, "Executor null");
        Assert.notNull(p, "Principal null");
    }

    public void run(OdeContext ctx) {
        try {
            Object retVal = null;

            // If the work throw an exception, we have to handle it as
            // IceMethodInvoker would.
            try {
                retVal = this.ex.execute(this.p, this.work);
            } catch (Throwable t) {
                Ice.UserException ue = mapper.handleException(t, ex
                        .getContext());
                exception(ue, ex.getContext());
                return; // EARLY EXIT
            }

            // Any exception thrown now will be thrown as is.
            if (mapper != null && mapper.canMapReturnValue()) {
                retVal = mapper.mapReturnValue(retVal);
            }
            response(retVal, ctx);

        } catch (Exception e) {
            exception(e, ctx);
        }

    }

}