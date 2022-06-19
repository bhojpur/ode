package ode.services.server.impl;

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

import java.util.NoSuchElementException;

import ode.api.ServiceInterface;
import ode.api.StatefulServiceInterface;
import ode.services.server.util.ServerExecutor;
import ode.services.server.util.UnregisterServantMessage;
import ode.util.messages.InternalMessage;
import ode.ShutdownInProgress;
import ode.api.AMD_StatefulServiceInterface_close;
import ode.util.CloseableServant;
import ode.util.IceMapper;
import Ice.ObjectAdapterDeactivatedException;

/**
 * Base class for all servants that must guarantee proper clean-up of held
 * resources on close.
 */
public abstract class AbstractCloseableAmdServant extends AbstractAmdServant
    implements CloseableServant {

    public AbstractCloseableAmdServant(ServiceInterface service, ServerExecutor be) {
        super(service, be);
    }

    public final void close(Ice.Current __current) {
        final RuntimeException[] re = new RuntimeException[1];
        AMD_StatefulServiceInterface_close cb =
            new AMD_StatefulServiceInterface_close() {
            public void ice_exception(Exception ex) {
                if (ex instanceof RuntimeException) {
                    re[0] = (RuntimeException) ex;
                } else {
                    re[0] = new RuntimeException(ex);
                }
            }
            public void ice_response() {
                // ok.
            }
        };
        close_async(cb, __current);
        if (re[0] != null) {
            throw re[0];
        }
    }

    /**
     * {@link ode.tools.hibernate.SessionHandler} also
     * specially catches close() calls, but cannot remove the servant
     * from the {@link Ice.ObjectAdapter} and thereby prevent any
     * further communication. Once the invocation is finished, though,
     * it is possible to raise the message and have the servant
     * cleaned up.
     */
    public final void close_async(AMD_StatefulServiceInterface_close __cb,
            Ice.Current __current) {

        Throwable t = null;

        // First we call close on the object
        try {
            preClose(__current);
            if (service instanceof StatefulServiceInterface) {
                StatefulServiceInterface ss = (StatefulServiceInterface) service;
                ss.close();
            }
        } catch (NoSuchElementException nsee) {
            log.info("NoSuchElementException: Login is already gone");
            t = nsee;
        } catch (Throwable t1) {
            log.error("Error on close, stage1", t1);
            t = t1;
        }

        // Then we publish the close event
        try {
            InternalMessage msg = new UnregisterServantMessage(this, __current, holder);
            ctx.publishMessage(msg);
        } catch (ObjectAdapterDeactivatedException oade) {
            log.warn("ObjectAdapter deactivated!");
            ShutdownInProgress sip = new ShutdownInProgress();
            IceMapper.fillServerError(sip, oade);
            t = sip;
        } catch (Throwable t2) {
            log.error("Error on close, stage2", t2);
            t = t2;
        }

        // Now we've finished that, let's return control to the user.
        try {
            if (t == null) {
                __cb.ice_response();
            } else {
                __cb.ice_exception(new IceMapper().handleException(t, ctx));
            }
        } finally {
            postClose(__current);
        }
    }

    protected abstract void preClose(Ice.Current current) throws Throwable;

    /**
     * Should not throw any exceptions which should be detected by clients
     * since it is called in a finally block after the client thread has been
     * released.
     */
    protected abstract void postClose(Ice.Current current);

}