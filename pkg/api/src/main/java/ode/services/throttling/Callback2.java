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

import ode.services.server.util.ServerOnly;
import ode.system.OdeContext;

import org.springframework.util.Assert;

/**
 * {@link Callable}-based callback which guarantees that ice_response or
 * ice_exception is called. Similar to {@link Callback}, this instance is useful
 * for servants which are {@link ServerOnly}
 */
public class Callback2<R> extends Task {

    private final Object cb;

    private final Ice.Current current;

    private final Callable<R> callable;

    public Callback2(Ice.Current current, Object cb, boolean isVoid,
            Callable<R> callable) {

        super(cb, current, isVoid);

        Assert.notNull(cb, "Null callback object");
        Assert.notNull(callable, "Null callable object");

        this.cb = cb;
        this.current = current;
        this.callable = callable;
    }

    public void run(OdeContext ctx) {
        try {
            R rv = callable.call();
            response(rv, ctx);
        } catch (Throwable e) {
            exception(e, ctx);
        }
    }

}