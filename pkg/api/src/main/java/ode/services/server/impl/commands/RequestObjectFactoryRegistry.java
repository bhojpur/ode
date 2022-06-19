package ode.services.server.impl.commands;

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

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import ode.system.OdeContext;

/**
 * Types which rely on the ode.model and ode.api objects rather than just
 * the ode.cmd types. These are typically intended as replacements for the
 * API method calls provided by ode.services.server.impl servants.
 */
public class RequestObjectFactoryRegistry extends
        ode.util.ObjectFactoryRegistry implements ApplicationContextAware {

    private/* final */OdeContext ctx;

    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException {
        this.ctx = (OdeContext) ctx;
    }

    public Map<String, ObjectFactory> createFactories(Ice.Communicator ic) {
        Map<String, ObjectFactory> factories = new HashMap<String, ObjectFactory>();
        factories.put(SaveI.ice_staticId(), new ObjectFactory(
                SaveI.ice_staticId()) {
            @Override
            public Ice.Object create(String name) {
                return new SaveI();
            }

        });
        return factories;
    }

}