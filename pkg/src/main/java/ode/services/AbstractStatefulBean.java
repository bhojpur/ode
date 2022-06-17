package ode.services;

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

import java.io.Serializable;

import ode.annotations.RolesAllowed;
import ode.api.StatefulServiceInterface;
import ode.api.local.LocalQuery;
import ode.api.local.LocalUpdate;
import ode.security.SecuritySystem;
import ode.services.util.BeanHelper;
import ode.system.EventContext;
import ode.system.SelfConfigurableService;
import ode.system.SimpleEventContext;

/**
 * Base bean implementation for stateful services. Particularly useful is the
 * implementation of
 */
public abstract class AbstractStatefulBean implements SelfConfigurableService,
        StatefulServiceInterface, Serializable {

    private transient BeanHelper beanHelper = new BeanHelper(this.getClass());

    protected transient LocalQuery iQuery;

    protected transient LocalUpdate iUpdate;

    protected transient SecuritySystem sec;

    /**
     * True if any write operation took place on this bean.
     * Allows for updating the database representation if needed.
     */
    protected transient boolean modified;

    /**
     * Query service Bean injector.
     * 
     * @param iQuery
     *            an <code>IQuery</code> service.
     */
    public final void setQueryService(LocalQuery iQuery) {
        getBeanHelper().throwIfAlreadySet(this.iQuery, iQuery);
        this.iQuery = iQuery;
    }

    public final void setUpdateService(LocalUpdate update) {
        getBeanHelper().throwIfAlreadySet(this.iUpdate, update);
        this.iUpdate = update;
    }

    public final void setSecuritySystem(SecuritySystem secSys) {
        getBeanHelper().throwIfAlreadySet(this.sec, secSys);
        this.sec = secSys;
    }

    public void selfConfigure() {
        getBeanHelper().configure(this);
    }

    protected BeanHelper getBeanHelper() {
        if (beanHelper == null) {
            beanHelper = new BeanHelper(this.getClass());
        }
        return beanHelper;
    }

    protected boolean isModified() {
        return modified;
    }

    protected void modified() {
        modified = true;
    }

    @RolesAllowed("user")
    public final EventContext getCurrentEventContext() {
        return new SimpleEventContext(sec.getEventContext());
    }

}