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

import ode.api.local.LocalQuery;
import ode.api.local.LocalUpdate;
import ode.security.SecuritySystem;
import ode.services.query.QueryFactory;
import ode.services.util.BeanHelper;
import ode.services.util.ReadOnlyStatus;
import ode.system.SelfConfigurableService;
import ode.tools.hibernate.ExtendedMetadata;

/**
 * service level 2
 */
public abstract class AbstractLevel2Service implements SelfConfigurableService {

    private transient BeanHelper beanHelper = new BeanHelper(this.getClass());

    protected transient LocalUpdate iUpdate;

    protected transient LocalQuery iQuery;

    protected transient QueryFactory queryFactory;

    protected transient SecuritySystem sec;

    protected transient ExtendedMetadata metadata;

    protected transient ReadOnlyStatus readOnlyStatus;

    // ~ Selfconfiguration (injection) for Non-JavaEE
    // =========================================================================

    /**
     * This method was previously called by the EJB container,
     * but is no longer needed. Instead, all configuration happens
     * within Spring.
     */
    public void selfConfigure() {
        getBeanHelper().configure(this);
    }

    protected BeanHelper getBeanHelper() {
        if (beanHelper == null) {
            beanHelper = new BeanHelper(this.getClass());
        }
        return beanHelper;
    }

    public final void setUpdateService(LocalUpdate update) {
        getBeanHelper().throwIfAlreadySet(this.iUpdate, update);
        this.iUpdate = update;
    }

    public final void setQueryFactory(QueryFactory qFactory) {
        getBeanHelper().throwIfAlreadySet(this.queryFactory, qFactory);
        this.queryFactory = qFactory;
    }

    public final void setQueryService(LocalQuery query) {
        getBeanHelper().throwIfAlreadySet(this.iQuery, query);
        this.iQuery = query;
    }

    public final void setSecuritySystem(SecuritySystem secSys) {
        getBeanHelper().throwIfAlreadySet(this.sec, secSys);
        this.sec = secSys;
    }

    public final void setExtendedMetadata(ExtendedMetadata em) {
        getBeanHelper().throwIfAlreadySet(this.metadata, em);
        this.metadata = em;
    }

    public final void setReadOnlyStatus(ReadOnlyStatus readOnlyStatus) {
        getBeanHelper().throwIfAlreadySet(this.readOnlyStatus, readOnlyStatus);
        this.readOnlyStatus = readOnlyStatus;
    }

    public final QueryFactory getQueryFactory() {
        return this.queryFactory;
    }

    public final SecuritySystem getSecuritySystem() {
        return this.sec;
    }

    public final ExtendedMetadata getExtendedMetadata() {
        return this.metadata;
    }
}