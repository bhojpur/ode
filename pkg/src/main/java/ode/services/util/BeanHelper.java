package ode.services.util;

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

import ode.conditions.InternalException;
import ode.system.OdeContext;
import ode.system.SelfConfigurableService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextAware;

/**
 * Helper for all bean implementations. This allows us to (largely) not subclass
 * a single abstract bean.
 */
public class BeanHelper {

    private transient Class beanClass;
    
    private transient Logger logger;

    private transient OdeContext applicationContext;

    public BeanHelper(Class implementationClass) {
        beanClass = implementationClass;
        logger = LoggerFactory.getLogger(beanClass);
    }
    
    // ~ Self-configuration
    // =========================================================================

    /**
     * Lazy loads the application context, which means that if we're not in
     * the application server, then the {@link OdeContext#MANAGED_CONTEXT} 
     * should never get loaded.
     */
    public final void acquireContext() {
        if (this.applicationContext == null) {
            this.applicationContext = OdeContext.getManagedServerContext();
        }
    }

    public final void configure(SelfConfigurableService bean) {
        this.acquireContext();
        // This will, in turn, call throwIfAlreadySet
        this.applicationContext.applyBeanPropertyValues(bean,
                bean.getServiceInterface());
        // FIXME setApplicationContext should be called properly (I think?)
        // However, we're going to do it here anyway.
        if (bean instanceof ApplicationContextAware) {
            ApplicationContextAware aca = (ApplicationContextAware) bean;
            aca.setApplicationContext(applicationContext);
        }
    }

    // ~ Helpers
    // =========================================================================

    public void throwIfAlreadySet(Object current, Object injected) {
        if (current != null) {
            throw new InternalException(String.format("%s already configured "
                    + "with %s cannot set inject %s.", this.getClass()
                    .getName(), current, injected));
        }
    }

    public void passivationNotAllowed() {
        throw new InternalException(
                String
                        .format(
                                "Passivation should have been disabled for this Stateful Session Beans (%s).\n"
                                        + "Please contact the Bhojpur ODE development team for how to ensure that passivation\n"
                                        + "is disabled on your application server.",
                                this.getClass().getName()));
    }

    public Exception translateException(Throwable t) {
        if (Exception.class.isAssignableFrom(t.getClass())) {
            return (Exception) t;
        } else {
            InternalException ie = new InternalException(t.getMessage());
            ie.setStackTrace(t.getStackTrace());
            return ie;
        }
    }
    
    public Logger getLogger() {
        return this.logger;
    }

    public String getLogString(SelfConfigurableService bean) {
        StringBuilder sb = new StringBuilder();
        sb.append("Bean ");
        sb.append(bean);
        sb.append("\n with Context ");
        sb.append(applicationContext);
        return sb.toString();
    }

}
