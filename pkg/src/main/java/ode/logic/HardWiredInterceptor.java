package ode.logic;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import ode.system.OdeContext;
import ode.system.Principal;
import ode.system.ServiceFactory;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ReflectiveMethodInvocation;

/**
 * Extension point for interceptors which should be compiled in from
 * third-party code. Subclasses can be added to the build system via the
 * ode.hard-wired.interceptors property value. All subclasses must
 * have a no-arg constructor, but can assess various environment
 * variables via the getters defined on this class.
 */
public abstract class HardWiredInterceptor implements MethodInterceptor {

    /** Unique string for the current ServiceFactory instance */
    private final static String SF = "ode.hard-wired.service-factory";

    /** Unique string for the current {@link Principal} instance */
    private final static String PR = "ode.hard-wired.principal";
    
    /** Unique string for the current password-state */
    private final static String RS = "ode.hard-wired.reusedSession";

    public static void configure(List<HardWiredInterceptor> hwi, OdeContext ctx) {
        for (HardWiredInterceptor interceptor : hwi) {
            interceptor.selfConfigure(ctx);
        }
    }
    
    /**
     * Can be implemented by all subclasses, so that they can configure themselves
     * in {@link #selfConfigure(OdeContext)}. If the method returns null,
     * {@link #selfConfigure(OdeContext)} will not run.
     */
    public String getName() {
        return null;
    }
    
    /**
     * Calls {@link OdeContext#applyBeanPropertyValues(Object, String)} to 
     * have properties injected.
     */
    public void selfConfigure(OdeContext context) {
        String name = getName();
        if (name != null) {
            context.applyBeanPropertyValues(this, getName());
        }
    }
    
    /** 
     * Produces a {@link List} of instantiated interceptors from
     * a list of {@link HardWiredInterceptor} subclass names.
     */
    public static List<HardWiredInterceptor> parse(String[] classNames) {

        List<HardWiredInterceptor> cptors = new ArrayList<HardWiredInterceptor>();

        for (int i = 0; i < classNames.length; i++) {
            try {
                Class klass = Class.forName(classNames[i]);
                cptors.add((HardWiredInterceptor) klass.newInstance());
            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate hard-wired "
                        + "interceptors:" + classNames[i], e);
            }
        }
        return Collections.unmodifiableList(cptors);
    }

    /** 
     * Adds the given environment context objects to the 
     * {@link ReflectiveMethodInvocation#getUserAttributes()} 
     * {@link java.util.Map} for lookup in subclasses
     */
    public static void initializeUserAttributes(MethodInvocation mi,
            ServiceFactory sf, Principal pr, AtomicBoolean reusedSession) {
        ReflectiveMethodInvocation rmi = (ReflectiveMethodInvocation) mi;
        Map<String, Object> attrs = rmi.getUserAttributes();
        attrs.put(SF, sf);
        attrs.put(PR, pr);
        attrs.put(RS, reusedSession);
    }

    protected ServiceFactory getServiceFactory(MethodInvocation mi) {
        ReflectiveMethodInvocation rmi = (ReflectiveMethodInvocation) mi;
        return (ServiceFactory) rmi.getUserAttribute(SF);
    }

    protected Principal getPrincipal(MethodInvocation mi) {
        ReflectiveMethodInvocation rmi = (ReflectiveMethodInvocation) mi;
        return (Principal) rmi.getUserAttribute(PR);
    }

    protected boolean hasPassword(MethodInvocation mi) {
        ReflectiveMethodInvocation rmi = (ReflectiveMethodInvocation) mi;
        AtomicBoolean reusedSession = (AtomicBoolean) rmi.getUserAttribute(RS);
        return reusedSession == null ? true : !reusedSession.get();
    }

}