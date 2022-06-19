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

import ode.services.query.QueryFactory;
import ode.services.util.BeanHelper;
import ode.system.OdeContext;
import ode.system.SelfConfigurableService;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * service level 1
 */
public abstract class AbstractLevel1Service implements SelfConfigurableService {

    protected transient QueryFactory queryFactory;

    protected transient SessionFactory sessionFactory;
    /**
     * Performs the necessary {@link OdeContext} lookup and calls
     * {@link OdeContext#applyBeanPropertyValues(Object, Class)} when
     * necessary.
     */
    private transient BeanHelper beanHelper = new BeanHelper(this.getClass());

    public final void setQueryFactory(QueryFactory factory) {
        getBeanHelper().throwIfAlreadySet(this.queryFactory, factory);
        this.queryFactory = factory;
    }

    public QueryFactory getQueryFactory() {
        return this.queryFactory;
    }

    /**
     * This method was previously called by the EJB container, but is no longer
     * needed. Instead, all configuration happens within Spring.
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

    /**
     * delegates to {@link HibernateDaoSupport}. Used during initialization to
     * create a {@link HibernateTemplate}
     * 
     * @see HibernateDaoSupport#setSessionFactory(SessionFactory)
     */
    public final void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * delegates to {@link HibernateDaoSupport} to get the current
     * {@link SessionFactory}
     * 
     * @see HibernateDaoSupport#getSessionFactory()
     */
    public final SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}