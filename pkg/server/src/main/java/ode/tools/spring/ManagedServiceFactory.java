package ode.tools.spring;

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

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import ode.system.OdeContext;
import ode.system.ServiceFactory;

/**
 * subclass of ode.system.ServiceFactory which retrieves managed
 * {@link ode.api.ServiceInterface service-}instances. These have all the
 * necessary layers of AOP interceptors for proper functioning. In fact, the
 * returned services behave almost exactly as if they were in an application
 * server ("container").
 */
public class ManagedServiceFactory extends ServiceFactory implements
        ApplicationContextAware {

    @Override
    protected String getPrefix() {
        return "managed-";
    }

    /**
     * returns null to prevent the lookup of any context, but rather wait on
     * injection as a {@link ApplicationContextAware}
     */
    @Override
    protected String getDefaultContext() {
        return null;
    }

    /**
     * simple injector for the {@link ApplicationContext}
     */
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.ctx = (OdeContext) applicationContext;
    }

}