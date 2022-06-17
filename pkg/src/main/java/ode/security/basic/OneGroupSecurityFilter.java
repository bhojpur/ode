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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ode.system.EventContext;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.orm.hibernate3.FilterDefinitionFactoryBean;

import ode.conditions.InternalException;
import ode.model.internal.Details;
import ode.model.internal.Permissions;
import ode.model.internal.Permissions.Right;
import ode.model.internal.Permissions.Role;
import ode.security.SecurityFilter;
import ode.system.Roles;

/**
 * overrides {@link FilterDefinitionFactoryBean} in order to construct our
 * security filter in code and not in XML. This allows us to make use of the
 * knowledge within {@link Permissions}
 *
 * With the addition of shares in 4.0, it is necessary to remove the security
 * filter if a share is active and allow loading to throw the necessary
 * exceptions.
 */
public class OneGroupSecurityFilter extends AbstractSecurityFilter {

    static public final String current_group = "current_group";

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
    public OneGroupSecurityFilter() {
        super();
    }

    public OneGroupSecurityFilter(Roles roles) {
        super(roles);
    }

    protected String myFilterCondition() {
        return "(\n"
                // Should handle hidden groups at the top-level
                // Allowing system objects to be read.
                + "\n  ( group_id = :current_group AND "
                + "\n     ( 1 = :is_nonprivate OR "
                + "\n       1 = :is_adminorpi OR "
                + "\n       owner_id = :current_user"
                + "\n     )"
                + "\n  ) OR"
                + "\n  group_id = %s OR " //
                // Will need to add something about world readable here.
                + "\n 1 = :is_share"
                + "\n)\n";
    }

    public String getDefaultCondition() {
        return String.format(myFilterCondition(), roles.getUserGroupId());
    }

    public Map<String, String> getParameterTypes() {
       Map<String, String> parameterTypes = new HashMap<String, String>();
       parameterTypes.put(is_share, "int");
       parameterTypes.put(is_adminorpi, "int");
       parameterTypes.put(is_nonprivate, "int");
       parameterTypes.put(current_group, "long");
       parameterTypes.put(current_user, "long");
       return parameterTypes;
   }

    /**
     * tests that the {@link Details} argument passes the security test that
     * this filter defines. The two must be kept in sync. This will be used
     * mostly by the
     * {@link OdeInterceptor#onLoad(Object, java.io.Serializable, Object[], String[], org.hibernate.type.Type[])}
     * method.
     *
     * @param d
     *            Details instance. If null (or if its {@link Permissions} are
     *            null all {@link Right rights} will be assumed.
     * @return true if the object to which this
     */
    public boolean passesFilter(Session session, Details d, EventContext c) {

        final Long currentGroupId = c.getCurrentGroupId();
        final Long currentUserId = c.getCurrentUserId();
        final boolean nonPrivate = isNonPrivate(c);
        final boolean adminOrPi = isAdminOrPi(c);
        final boolean share = isShare(c);
        final List<Long> memberOfGroups = c.getMemberOfGroupsList();

        if (d == null || d.getPermissions() == null) {
            throw new InternalException("Details/Permissions null! "
                    + "Security system failure -- refusing to continue. "
                    + "The Permissions should be set to a default value.");
        }

        Long o = d.getOwner().getId();
        Long g = d.getGroup().getId();

        if (share) {
            return true;
        }

        // Only loading current objects is permitted.
        // This method will not be called with system types.
        // See BasicACLVoter
        // Also allowing system objects to be read.
        // Also allowing user objects to be read.
        if (Long.valueOf(roles.getSystemGroupId()).equals(g) ||
                Long.valueOf(roles.getUserGroupId()).equals(g)) {
            return true;
        }

        if (currentGroupId < 0) {
            throwNegOne();
        } else if (!currentGroupId.equals(g)) {
            return false;
        }

        if (nonPrivate) {
            return true;
        }

        if (adminOrPi) {
            return true;
        }

        if (currentUserId.equals(o)) {
            return true;
        }

        return false;
    }

    public void enable(Session sess, EventContext ec) {
        final Filter filter = sess.enableFilter(getName());

        final Long groupId = ec.getCurrentGroupId();
        final Long shareId = ec.getCurrentShareId();
        int share01 = shareId != null ? 1 : 0; // non-final; below

        final int adminOrPi01 = (ec.isCurrentUserAdmin() ||
                ec.getLeaderOfGroupsList().contains(ec.getCurrentGroupId()))
                ? 1 : 0;

        final int nonpriv01 = (ec.getCurrentGroupPermissions().isGranted(Role.GROUP, Right.READ)
                || ec.getCurrentGroupPermissions().isGranted(Role.WORLD, Right.READ))
                ? 1 : 0;

        if (groupId < 0) { // Special marker
            throwNegOne();
        }

        filter.setParameter(SecurityFilter.is_share, share01); // not checking -1 here.
        filter.setParameter(SecurityFilter.is_adminorpi, adminOrPi01);
        filter.setParameter(SecurityFilter.is_nonprivate, nonpriv01);
        filter.setParameter(SecurityFilter.current_user, ec.getCurrentUserId());
        filter.setParameter(current_group, groupId);
        enableBaseFilters(sess, ec.isCurrentUserAdmin() ? 1 : 0, ec.getCurrentUserId());
    }

    private void throwNegOne() {
        throw new InternalException("OneGroupSecurityFilter is not " +
                "capable of handling ode.group=-1. This is handled by " +
                "AllGroupsSecurityFilter");
    }
}