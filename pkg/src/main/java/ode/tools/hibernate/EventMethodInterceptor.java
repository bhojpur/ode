package ode.tools.hibernate;

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

// Java imports
import java.lang.reflect.Method;

// Third-party imports
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.event.AbstractEvent;

// Application-internal dependencies
import ode.conditions.InternalException;

/**
 * filter which can be added to any event type in order to catch execution.
 */
public class EventMethodInterceptor implements MethodInterceptor {

    public static class Action {
        public Object call(MethodInvocation mi) {
            if (mi.getMethod().getReturnType().equals(boolean.class)) {
                return Boolean.FALSE;
            }
            return null;
        }
    }

    public static class DisableAction extends Action {
        @Override
        public Object call(MethodInvocation mi) {
            if (disabled(mi)) {
                throw createException(mi);
            }
            return super.call(mi);
        }

        protected boolean disabled(MethodInvocation mi) {
            return true;
        }

        protected InternalException createException(MethodInvocation mi) {
            return new InternalException(String.format(
                    "\nHibernate %s events have been disabled.", getType(mi)));
        }

        protected String getType(MethodInvocation mi) {
            Object event = mi.getArguments()[0];
            String type = "(unknown)";
            if (AbstractEvent.class.isAssignableFrom(event.getClass())) {
                type = event.getClass().getName();
            }
            return type;
        }

    }

    static volatile String last = null;

    static volatile int count = 1;

    private static Logger log = LoggerFactory.getLogger(EventMethodInterceptor.class);

    protected boolean verbose = false;

    protected Action action;

    public EventMethodInterceptor() {
        this.action = new Action();
    }

    public EventMethodInterceptor(Action action) {
        this.action = action;
    }

    // ~ Injectors
    // =========================================================================

    public void setDebug(boolean debug) {
        this.verbose = debug;
    }

    public Object invoke(MethodInvocation arg0) throws Throwable {
        Object[] args = arg0.getArguments();
        Method method = arg0.getMethod();

        if (verbose && method.getName().startsWith("on")) {
            log(String.format("%s.%s called.", method.getDeclaringClass()
                    .getName(), method.getName()));
        }

        return action.call(arg0);
    }

    // ~ Helpers
    // =========================================================================

    protected void log(String msg) {
        if (msg.equals(last)) {
            count++;
        }

        else if (log.isInfoEnabled()) {
            String times = " ( " + count + " times )";
            log.info(msg + times);
            last = msg;
            count = 1;
        }
    }

}