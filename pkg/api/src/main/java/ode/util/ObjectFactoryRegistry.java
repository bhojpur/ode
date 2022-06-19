package ode.util;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SPI type picked up from the Spring configuration and given a chance to
 * register all its {@link Ice.ObjectFactory} instances with the
 * {@link Ice.Communicator}.
 */
public abstract class ObjectFactoryRegistry {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public static abstract class ObjectFactory implements Ice.ObjectFactory {

        private final String id;

        public ObjectFactory(String id) {
            this.id = id;
        }

        public void register(Logger log, Ice.Communicator ic, boolean strict) {
            if (strict) {
                ic.addObjectFactory(this, id);
            } else {
                final Ice.ObjectFactory of = ic.findObjectFactory(id);
                if (null == of) {
                    ic.addObjectFactory(this, id);
                } else {
                    log.debug(String.format(
                            "ObjectFactory already exists: %s=%s", id, of));
                }
            }
        }

        public abstract Ice.Object create(String name);

        public void destroy() {
            // noop
        }

    }

    public abstract Map<String, ObjectFactory> createFactories(Ice.Communicator ic);

    public void setIceCommunicator(Ice.Communicator ic) {
        Map<String, ObjectFactory> factories = createFactories(ic);
        for (ObjectFactory of : factories.values()) {
            of.register(log, ic, false);
        }
    }

}