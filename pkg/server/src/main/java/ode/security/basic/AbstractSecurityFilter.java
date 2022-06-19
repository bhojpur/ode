package ode.security.basic;

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

import ode.system.EventContext;
import ode.model.internal.Details;
import ode.model.internal.Permissions;
import ode.model.internal.Permissions.Right;
import ode.model.internal.Permissions.Role;
import ode.security.SecurityFilter;
import ode.system.Roles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.orm.hibernate3.FilterDefinitionFactoryBean;

/**
 * overrides {@link FilterDefinitionFactoryBean} in order to construct our
 * security filter in code and not in XML. This allows us to make use of the
 * knowledge within {@link Permissions}
 *
 * With the addition of shares in 4.0, it is necessary to remove the security
 * filter if a share is active and allow loading to throw the necessary
 * exceptions.
 */
public abstract class AbstractSecurityFilter extends FilterDefinitionFactoryBean
    implements SecurityFilter {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected final Roles roles;

    /**
     * Default constructor which calls all the necessary setters for this
     * {@link FactoryBean}. Also calls {@link #setDefaultFilterCondition(String)}.
     * This query clause must be kept in sync with
     * {@link #passesFilter(Session, Details, EventContext)}.
     *
     * @see #passesFilter(Session, Details, EventContext)
     * @see FilterDefinitionFactoryBean#setFilterName(String)
     * @see FilterDefinitionFactoryBean#setParameterTypes(java.util.Map)
     * @see FilterDefinitionFactoryBean#setDefaultFilterCondition(String)
     */
    public AbstractSecurityFilter() {
        this(new Roles());
    }

    public AbstractSecurityFilter(Roles roles) {
        this.roles = roles;
        this.setFilterName(getName());
        this.setParameterTypes(getParameterTypes());
        this.setDefaultFilterCondition(getDefaultCondition());
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public void disable(Session sess) {
	    sess.disableFilter(getName());
	    disableBaseFilters(sess);
    }

    public boolean isNonPrivate(EventContext c) {
        return c.getCurrentGroupPermissions().isGranted(Role.GROUP, Right.READ)
            || c.getCurrentGroupPermissions().isGranted(Role.WORLD, Right.READ);
    }

    public boolean isAdminOrPi(EventContext c) {
        return c.isCurrentUserAdmin() ||
            c.getLeaderOfGroupsList().contains(c.getCurrentGroupId());

    }

    public boolean isShare(EventContext c) {
        return c.getCurrentShareId() != null;
    }

    protected void enableBaseFilters(Session sess, int admin01, Long currentUserId) {
        final Filter sessionFilter = sess.enableFilter("owner_or_admin");
        sessionFilter.setParameter("is_admin", admin01);
        sessionFilter.setParameter("current_user", currentUserId);
	}

    protected void disableBaseFilters(Session sess) {
        sess.disableFilter("owner_or_admin");
    }
}