package ode.cmd;

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

import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import ode.security.basic.CurrentDetails;
import ode.system.OdeContext;
import ode.SecurityViolation;

/**
 * Interceptor which takes any context provided by the
 * client and calls setContext on CurrentDetails. This
 * allows users to dynamically change, for example, the
 * call group without modifying the whole session.
 */
public class CallContext implements MethodInterceptor {

    public static final String FILENAME_KEY = "ode.logfilename";

    public static final String TOKEN_KEY = "ode.logfilename.token";

    private static Logger log = LoggerFactory.getLogger(CallContext.class);

    private final CurrentDetails cd;

    private final String token;

    private final Ice.Current current;

    public CallContext(OdeContext ctx, String token, Ice.Current current) {
        this.cd = ctx.getBean(CurrentDetails.class);
        this.token = token;
        this.current = current;
    }

    public CallContext(OdeContext ctx, String token) {
        this(ctx, token, null);
    }

    public CallContext(CurrentDetails cd, String token) {
        this.cd = cd;
        this.token = token;
        this.current = null;
    }

    /**
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(final MethodInvocation arg0) throws Throwable {

        if (arg0 != null) {
            final Object[] args = arg0.getArguments();
            if (args != null && args.length > 0) {
                final Object last = args[args.length-1];
                if (Ice.Current.class.isAssignableFrom(last.getClass())) {
                    final Ice.Current current = (Ice.Current) last;
                    final Map<String, String> ctx = current.ctx;
                    if (ctx != null && ctx.size() > 0) {
                        cd.setContext(ctx);
                        // Don't trust user-passed values
                        if (!checkLogFile(ctx, true)) {
                            if (this.current != null) {
                                // fall back to the service-wide values
                                checkLogFile(this.current.ctx, true);
                            }
                        }
                    }
                }
            }
        }

        try {
            return arg0.proceed();
        } finally {
            cd.setContext(null);
            MDC.clear();
        }
    }

    /**
     * If "ode.logfilename" is in the passed {@link Map}, then set it in the
     * {@link MDC} IFF requireToken is false or the token is present and matches
     * the original token set on this instance.
     */
    private boolean checkLogFile(Map<String, String> ctx, boolean requireToken)
        throws SecurityViolation {

        if (ctx == null) {
            return false;
        }

        String filename = ctx.get(FILENAME_KEY);
        if (filename == null) {
            return false;
        }

        if (requireToken) {
            String token = ctx.get(TOKEN_KEY);
            if (!this.token.equals(token)) {
                log.error("Found bad token: user={} != server={}",
                        token, this.token);
                throw new SecurityViolation(null, null,
                        String.format("Setting the %s value is"
                        + " not permitted without a secure"
                        + " server token!", FILENAME_KEY));
            }
        }

        MDC.put("fileset", filename);
        return true;
    }
}