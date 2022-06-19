package ode.security.basic;

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

import ode.conditions.ApiUsageException;
import ode.conditions.InternalException;
import ode.conditions.SessionTimeoutException;
import ode.logic.HardWiredInterceptor;
import ode.security.MethodSecurity;
import ode.services.sessions.stats.DelegatingStats;
import ode.system.Principal;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Responsible for logging users in and out via the {@link Principal} before and
 * after the actual invocation of ODE methods.
 * 
 * This class is the only {@link HardWiredInterceptor} which is hard-wired by
 * default into ODE classes. This permits simple start-up without the need for
 * the ant build, which may replace the hard-wired value with a more extensive
 * list of {@link HardWiredInterceptor} instances.
 * 
 * Note: any internal "client" will have to handle logging in and out with an
 * appropriate {@link Principal}.
 */
public final class BasicSecurityWiring extends HardWiredInterceptor {

    private final static Logger log = LoggerFactory.getLogger(BasicSecurityWiring.class);

    protected PrincipalHolder principalHolder;

    protected MethodSecurity methodSecurity;

    /**
     * Lookup name.
     */
    // TODO This should be replaced by a components concept
    @Override
    public String getName() {
        return "securityWiring";
    }

    /**
     * Setter injection.
     */
    public void setPrincipalHolder(PrincipalHolder principalHolder) {
        this.principalHolder = principalHolder;
    }

    /**
     * Setter injection.
     */
    public void setMethodSecurity(MethodSecurity security) {
        this.methodSecurity = security;
    }

    /**
     * Wraps all Bhojpur ODE invocations with login/logout semantics.
     */
    public Object invoke(MethodInvocation mi) throws Throwable {

        Principal p = getPrincipal(mi);
        boolean hp = hasPassword(mi);
        boolean close = "close".equals(mi.getMethod().getName());

        if (!close  && methodSecurity.isActive()) {
            methodSecurity.checkMethod(mi.getThis(), mi.getMethod(), p, hp);
        }

        // First try a login
        try {
            login(mi, p);
        } catch (SessionTimeoutException ste) {
            if (!close) {
                throw ste;
            }
            log.warn("SessionTimeoutException on close:" + p);
            principalHolder.login(new CloseOnNoSessionContext());
        }

        // Assuming the above block didn't throw an
        // exception, then continue and cleanup.
        try {
            return mi.proceed();
        } finally {
            logout();
        }
    }

    private void login(MethodInvocation mi, Principal p) {

        if (p == null) {
            throw new ApiUsageException(
                    "ode.system.Principal instance must be provided on login.");
        }

        int size = principalHolder.size();
        if (size > 0) {
            throw new InternalException(
                    "SecuritySystem is still active on login. " + size
                            + " logins remaining in thread.");
        }
        principalHolder.login(p);
        if (log.isDebugEnabled()) {
            log.debug("Running with user: " + p.getName());
        }

    }

    private void logout() {
        int size = principalHolder.logout();
        if (size > 0) {
            log.error("SecuritySystem is still active on logout. " + size
                    + " logins remaining in thread.");
        }
    }

    public static class CloseOnNoSessionContext extends BasicEventContext {

        public CloseOnNoSessionContext()
        {
            super(new Principal(""), new DelegatingStats());
        }

    }
}