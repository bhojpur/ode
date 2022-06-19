package ode.services.server.util;

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

import ode.services.server.impl.ServiceFactoryI;
import ode.util.messages.InternalMessage;

/**
 * {@link InternalMessage} raised when a servant needs to find
 * the {@link ServiceFactoryI} instance that the current user
 * is working with.
 */
public class FindServiceFactoryMessage extends InternalMessage {

    private static final long serialVersionUID = 345845093802L;

    protected final transient Ice.Current curr;

    protected transient Ice.Identity id;

    protected transient ServiceFactoryI sf;

    public FindServiceFactoryMessage(Object source, Ice.Identity id) {
        super(source);
        this.id = id;
        this.curr = null;
    }

    public FindServiceFactoryMessage(Object source, Ice.Current current) {
        super(source);
        this.curr = current;
    }

    public Ice.Identity getIdentity() {
        return this.id;
    }

    public Ice.Current getCurrent() {
        if (this.curr == null) {
            throw new RuntimeException(
                    "This instance was initialized with an Identity");
        }
        return this.curr;
    }

    /**
     * Store the information. Subclasses may try to make remote calls.
     *
     * @throws ode.ServerError
     *      so that subclasses can make use of remote methods.
     */
    public void setServiceFactory(Ice.Identity id, ServiceFactoryI sf)
        throws ode.ServerError {
        this.sf = sf;
        this.id = id;
    }

    public ServiceFactoryI getServiceFactory() {
        return sf;
    }

}