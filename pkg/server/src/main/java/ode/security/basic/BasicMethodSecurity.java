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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import ode.annotations.PermitAll;
import ode.annotations.RolesAllowed;
import ode.conditions.SecurityViolation;
import ode.security.MethodSecurity;
import ode.security.SecuritySystem;
import ode.services.sessions.SessionManager;
import ode.system.Principal;

import org.springframework.aop.framework.Advised;

/**
 * Implementation of {@link MethodSecurity} which checks method security based
 * on the {@link RolesAllowed} annotations of our implementation methods. To do
 * this, it is necessary to "unwrap" proxies via the {@link Advised} interface.
 */
public class BasicMethodSecurity implements MethodSecurity {

    private final boolean active;

    class Info {
        RolesAllowed rolesAllowed;
        boolean permitAll;
    }

    private SessionManager sessionManager;

    public BasicMethodSecurity() {
        this(true);
    }

    public BasicMethodSecurity(boolean active) {
        this.active = active;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * See {@link MethodSecurity#isActive()}
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @see MethodSecurity#checkMethod(Object, Method, Principal, boolean)
     */
    public void checkMethod(Object o, Method m, Principal p, boolean hasPassword) {
        String[] allowedRoles = null;
        Annotation[] anns;
        //
        // Map<Method, Info> map = info.get(o.getClass());
        // if (map == null) {
        // map = new MapMaker().makeMap();
        // map.put(o.getClass(), map);
        // }
        // Info i = map.get(m);
        // if (i == null) {
        // i.
        // }
        //        
        try {
            Class<?> c = o.getClass(); // Getting runtime class
            while (Advised.class.isAssignableFrom(c)) {
                Advised advised = (Advised) o;
                o = advised.getTargetSource().getTarget();
                c = o.getClass();
            }
            Method mthd = c.getMethod(m.getName(), m.getParameterTypes());
            anns = mthd.getDeclaredAnnotations();
        } catch (Exception e) {
            throw new SecurityViolation("Invalid method accessed.");
        }

        for (Annotation annotation : anns) {
            if (annotation instanceof RolesAllowed) {
                RolesAllowed ra = (RolesAllowed) annotation;
                allowedRoles = ra.value();
                break; // Can only be one annotation of a type
            } else if (annotation instanceof PermitAll) {
                return; // EARLY EXIT
            }
        }

        // TODO add exception subclass
        if (allowedRoles == null) {
            throw new SecurityViolation("This method allows no remote access.");
        }

        boolean allow = false;
        boolean block = false;

        List<String> actualRoles = sessionManager.getUserRoles(p.getName());
        for (String allowed : allowedRoles) {
            if (actualRoles.contains(allowed)) {
                allow = true;
            }
            if ("HasPassword".equals(allowed)) {
                block = !hasPassword;
            }
        }

        if (block) {
            throw new SecurityViolation(
                    "Bad authentication credentials for this action.\n" +
                    "See setSecurityPassword for more information");

        }

        if (!allow) {
            throw new SecurityViolation(String.format(
                    "No matching roles found in %s for session %s (allowed: %s)",
                    actualRoles, p, Arrays.asList(allowedRoles)));
        }
    }

}