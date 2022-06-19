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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import ode.api.ServiceInterface;
import ode.logic.HardWiredInterceptor;
import ode.services.server.fire.AopContextInitializer;
import ode.services.server.util.ServerExecutor;
import ode.services.server.util.IceMethodInvoker;
import ode.services.throttling.Task;
import ode.services.throttling.ThrottlingStrategy;
import ode.services.util.Executor;
import ode.system.OdeContext;
import ode.ServerError;
import ode.api.AMD_StatefulServiceInterface_activate;
import ode.api.AMD_StatefulServiceInterface_close;
import ode.api.AMD_StatefulServiceInterface_getCurrentEventContext;
import ode.api.AMD_StatefulServiceInterface_passivate;
import ode.api._ServiceInterfaceOperations;
import ode.util.IceMapper;
import ode.util.ServantHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import Ice.Current;

/**
 * {@link ThrottlingStrategy throttled} implementation base class which can be
 * used by {@link _ServiceInterfaceOperations} implementors and injected into a
 * tie servant.
 */
public abstract class AbstractAmdServant implements ApplicationContextAware {

    final protected Logger log = LoggerFactory.getLogger(getClass());

    final protected ServerExecutor be;

    /**
     * If there is no undering ode.* service, then this value can be null.
     */
    protected ServiceInterface service;

    /**
     * If a service is provided, then an invoker will be created to cache all of
     * its methods.
     */
    protected IceMethodInvoker invoker;

    protected OdeContext ctx;

    protected ServantHolder holder;

    public AbstractAmdServant(ServiceInterface service, ServerExecutor be) {
        this.be = be;
        this.service = service;
    }

    /**
     * Sets the {@link ServantHolder} for the current session so that on
     * {@link AbstractCloseableAmdServant#close_async(AMD_StatefulServiceInterface_close, Current)}
     * it will be possible to cleanup the resources.
     * @param holder
     */
    public void setHolder(ServantHolder holder) {
        this.holder = holder;
    }

    /**
     * Creates an {@link IceMethodInvoker} for this instance if {@link #service}
     * is non-null. Otherwise gives subclasses a chance to use the {@link OdeContext}
     * via {@link #onSetOdeContext(OdeContext)}
     */
    public final void setApplicationContext(ApplicationContext ctx)
            throws BeansException {
        this.ctx = (OdeContext) ctx;
        if (service != null) {
            this.invoker = new IceMethodInvoker(service, this.ctx);
        }
        try {
            onSetOdeContext(this.ctx);
        } catch (Exception e) {
            throw new FatalBeanException("Error on setOdeContext", e);
        }
    }
    
    /**
     * To be overridden by subclasses.
     */
    public void onSetOdeContext(OdeContext context) throws Exception {
        //no-op
    }

    /**
     * Applies the hard-wired intercepting to this instance. It is not possible
     * to configure hard-wired interceptors in Spring, instead they must be
     * passed in at runtime from a properly compiled class.
     */
    public final void applyHardWiredInterceptors(
            List<HardWiredInterceptor> cptors, AopContextInitializer initializer) {

        if (service != null) {
            ProxyFactory wiredService = new ProxyFactory();
            wiredService.setInterfaces(service.getClass().getInterfaces());
            wiredService.setTarget(service);

            List<HardWiredInterceptor> reversed = new ArrayList<HardWiredInterceptor>(
                    cptors);
            Collections.reverse(reversed);
            for (HardWiredInterceptor hwi : reversed) {
                wiredService.addAdvice(0, hwi);
            }
            wiredService.addAdvice(0, initializer);
            service = (ServiceInterface) wiredService.getProxy();
        }
    }

    public final void callInvokerOnRawArgs(Object __cb, Ice.Current __current,
            Object... args) {
        if (service == null) {
            throw new ode.conditions.InternalException(
                    "Null service; cannot use callInvoker()");
        }
        this.be.callInvokerOnRawArgs(service, invoker, __cb, __current, args);
    }

    public final void callInvokerOnMappedArgs(IceMapper mapper, Object __cb,
            Ice.Current __current, Object... args) {
        if (service == null) {
            throw new ode.conditions.InternalException(
                    "Null service; cannot use callInvoker()");
        }
        this.be.callInvokerWithMappedArgs(service, invoker, mapper, __cb,
                __current, args);
    }

    public final void runnableCall(Ice.Current __current, Task r) {
        this.be.runnableCall(__current, r);
    }

    public final <R> void safeRunnableCall(Ice.Current __current, Object cb,
            boolean isVoid, Callable<R> callable) {
        this.be.safeRunnableCall(__current, cb, isVoid, callable);
    }

    public final void executorWorkCall(Executor.Work work) {
        throw new UnsupportedOperationException();
    }

    //
    // StatefulServiceInterface
    //

    public final void activate_async(AMD_StatefulServiceInterface_activate __cb,
            Current __current) {
        // Do nothing for the moment
    }

    public final void passivate_async(AMD_StatefulServiceInterface_passivate __cb,
            Current __current) {
        // Do nothing for the moment
    }

    public final void getCurrentEventContext_async(
            AMD_StatefulServiceInterface_getCurrentEventContext __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

}