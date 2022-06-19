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

import ode.api.ServiceInterface;
import ode.services.server.util.IceMethodInvoker;
import ode.system.OdeContext;
import ode.util.IceMapper;

import org.springframework.util.Assert;

/**
 * Manages AMD-based method dispatches from server
 */
public class Callback extends Task {

    private final Boolean io;
    private final Boolean db;
    private final IceMethodInvoker invoker;
    private final ServiceInterface service;
    private final Object[] args;
    private final IceMapper mapper;

    public Callback(Boolean io, Boolean db, ServiceInterface service,
            IceMethodInvoker invoker, Object cb, IceMapper mapper,
            Ice.Current current, Object... args) {
        
        super(cb, current, invoker.isVoid(current));

        Assert.notNull(invoker, "Null invoker");
        Assert.notNull(service, "Null service");
        Assert.notNull(args, "Null argument array");

        this.io = io;
        this.db = db;
        this.service = service;
        this.invoker = invoker;
        this.args = args;
        this.mapper = mapper;
    }

    public Callback(ServiceInterface service, IceMethodInvoker invoker,
            IceMapper mapper, Object cb, Ice.Current current, Object... args) {
        this(null, null, service, invoker, cb, mapper, current, args);
    }

    public void run(OdeContext ctx) {
        try {
            Object retVal = invoker.invoke(service, current, mapper, args);
            response(retVal, ctx);
        } catch (Throwable e) {
            exception(e, ctx);
        }
    }

    /**
     * Callback can be either IO-intensive ({@link Boolean#TRUE}),
     * IO-non-intensive ({@link Boolean#FALSE}), or it can be unknown ({@link <code>null</code>}).
     */
    Boolean ioIntensive() {
        return io;
    }

    /**
     * Callback can be either database-intensive ({@link Boolean#TRUE}),
     * database-non-intensive ({@link Boolean#FALSE}), or it can be unknown ({@link <code>null</code>}).
     */
    Boolean dbIntensive() {
        return db;
    }

}