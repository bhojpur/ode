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

import java.lang.reflect.Method;

import ode.system.OdeContext;
import ode.InternalException;
import ode.ServerError;
import ode.util.IceMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple base task which contains logic for routing calls reflectively to
 * ice_response and ice_exception of any AMD callback.
 */
public abstract class Task {

    private final static Logger log = LoggerFactory.getLogger(Task.class);

    protected final Object cb;

    protected final boolean isVoid;

    protected final Ice.Current current;

    protected final Method response;

    protected final Method exception;

    public Task(Object callback, Ice.Current current, boolean isVoid) {
        this.current = current;
        this.isVoid = isVoid;
        this.cb = callback;
        if (callback != null) {
            response = getMethod(callback, "ice_response");
            exception = getMethod(callback, "ice_exception");
        } else {
            response = null;
            exception = null;
        }
    }

    public abstract void run(OdeContext ctx);

    /**
     * Calls the response method
     */
    protected void response(Object rv, OdeContext ctx) {
        try {
            if (isVoid) {
                response.invoke(cb);
            } else {
                response.invoke(cb, postProcess(rv));
            }
        } catch (Exception e) {
            InternalException ie = new InternalException();
            IceMapper.fillServerError(ie, e);
            ie.message = "Failed to invoke: " + this.toString();
            log.error(ie.message, e);
            exception(ie, ctx);
        }
    }

    /**
     * Can be overridden to transform the return value from the async method.
     * This implementation leaves the return value unchanged.
     * @param rv a return value
     * @return the return value transformed
     * @throws ServerError if the transformation failed
     */
    protected Object postProcess(Object rv) throws ServerError {
        return rv;
    }

    protected void exception(Throwable ex, OdeContext ctx) {
        try {
            if (!(ex instanceof Exception)) {
                log.error("Throwable thrown!", ex);
            }
            IceMapper mapper = new IceMapper();
            ex = mapper.handleException(ex, ctx);
            exception.invoke(cb, ex);
        } catch (Exception e2) {
            String msg = "Failed to invoke exception()";
            log.error(msg, e2);
            throw new RuntimeException("Failed to invoke exception()", e2);
        }
    }

    // Helpers
    // =========================================================================

    Method getMethod(Object o, String methodName) {
        Class c = getPublicInterface(o.getClass());
        Method[] methods = c.getMethods();
        Method rv = null;
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            if (methodName.equals(m.getName())) {
                if (rv != null) {
                    throw new RuntimeException(methodName + " exists twice!");
                } else {
                    rv = m;
                }
            }
        }
        return rv;
    }

    /**
     * The Ice AMD-implementations are package-private and so cannot be executed
     * on. Instead, we have to find the public interface and use its methods.
     */
    private Class getPublicInterface(Class c) {
        if (!c.getName().startsWith("AMD_")) {
            while (!c.equals(Object.class)) {
                Class[] ifaces = c.getInterfaces();
                for (Class c2 : ifaces) {
                    if (c2.getSimpleName().startsWith("AMD_")) {
                        return c2;
                    }
                }
                // Ok. We didn't find anything so recurse into the superclass
                c = c.getSuperclass();
            }
            throw new RuntimeException("No public AMD_ interface found.");
        } else {
            return c;
        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" (");
        sb.append(cb);
        sb.append(" )");
        return sb.toString();
    }

}