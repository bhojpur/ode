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

import static ode.model.internal.Permissions.Right.READ;
import static ode.model.internal.Permissions.Role.GROUP;
import static ode.model.internal.Permissions.Role.USER;
import static ode.model.internal.Permissions.Role.WORLD;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ode.system.EventContext;
import ode.model.internal.Details;
import ode.model.internal.Permissions;
import ode.model.internal.Permissions.Right;
import ode.model.internal.Permissions.Role;
import ode.model.meta.ExperimenterGroup;
import ode.security.SecurityFilter;
import ode.system.Roles;
import ode.util.SqlAction;

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
public class AllGroupsSecurityFilter extends AbstractSecurityFilter {

    static public final String is_admin = "is_admin";

    static public final String member_of_groups = "member_of_groups";

    static public final String leader_of_groups = "leader_of_groups";

    static public final String filterName = "securityFilter";

    final SqlAction sql;

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
     *
     * @param sql an SQL action instance
     */
    public AllGroupsSecurityFilter(SqlAction sql) {
        this(sql, new Roles());
    }

    public AllGroupsSecurityFilter(SqlAction sql, Roles roles) {
        super(roles);
        this.sql = sql;
    }

    protected String myFilterCondition() {
        return String.format(
                "\n( "
                + "\n  1 = :is_share OR "
                + "\n  1 = :is_admin OR "
                + "\n  (group_id in (:leader_of_groups)) OR "
                + "\n  (owner_id = :current_user AND %s) OR " // 1st arg U
                + "\n  (group_id in (:member_of_groups) AND %s) OR " // 2nd arg G
                + "\n  (%s) " // 3rd arg W
                + "\n)"
                + "\n", isGranted(USER, READ), isGranted(GROUP, READ),
                isGranted(WORLD, READ));
    }

    public String getDefaultCondition() {
        return myFilterCondition();
    }

    public Map<String, String> getParameterTypes() {
        Map<String, String> parameterTypes = new HashMap<String, String>();
        parameterTypes.put(is_share, "int");
        parameterTypes.put(is_admin, "int");
        parameterTypes.put(current_user, "long");
        parameterTypes.put(member_of_groups, "long");
        parameterTypes.put(leader_of_groups, "long");
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

        final Long currentUserId = c.getCurrentUserId();
        final boolean admin = c.isCurrentUserAdmin();
        final boolean share = isShare(c);
        final List<Long> memberOfGroups = c.getMemberOfGroupsList();
        final List<Long> leaderOfGroups = c.getLeaderOfGroupsList();

        final Long o = d.getOwner().getId();
        final Long g = d.getGroup().getId();

        // load permissions for group of object regardless.
        final ExperimenterGroup group = (ExperimenterGroup) session.get(ExperimenterGroup.class, g);
        Permissions p = group.getDetails().getPermissions();

        if (p == null) {
            // Don't know why this is happening, but must do something to
            // force reloading.
            p = ode.util.Utils.toPermissions(sql.getGroupPermissions(g));
            group.getDetails().setPermissions(p);
            log.warn(String.format(
                "Forced to reload permissions for group %s: %s", g, p));
        }

        if (share || admin) {
            return true;
        }

        // most likely and fastest first
        if (p.isGranted(WORLD, READ)) {
            return true;
        }

        if (currentUserId.equals(o) && p.isGranted(USER, READ)) {
            return true;
        }

        if (memberOfGroups.contains(g)
                && p.isGranted(GROUP, READ)) {
            return true;
        }

        if (leaderOfGroups.contains(g)) {
            return true;
        }

        return false;
    }

    /***
     * Since we assume that the group is "-1" for this method, we have to pass
     * in lists of all groups as we did before group permissions (~4.2).
     */
    public void enable(Session sess, EventContext ec) {
        final Filter filter = sess.enableFilter(getName());

        final int share01 = isShare(ec) ? 1 : 0;
        final int admin01 = ec.isCurrentUserAdmin() ? 1 : 0;

        filter.setParameter(is_admin, admin01);
        filter.setParameter(SecurityFilter.is_share, share01); // not checking -1 here.
        filter.setParameter(SecurityFilter.current_user, ec.getCurrentUserId());
        filter.setParameterList(member_of_groups,
                configGroup(ec, ec.getMemberOfGroupsList()));
        filter.setParameterList(leader_of_groups,
                configGroup(ec, ec.getLeaderOfGroupsList()));
        enableBaseFilters(sess, admin01, ec.getCurrentUserId());
    }

    // ~ Helpers
    // =========================================================================

    protected Collection<Long> configGroup(EventContext ec, List<Long> list) {
        Collection<Long> rv = null;

        if (ec.isCurrentUserAdmin()) {
            // Admin is considered to be in every group
            // which is handled by other clauses of the
            // filter
            rv = Collections.singletonList(-1L);
        } else {
            // Non-admin are only in their groups.
            rv = list;
            if (rv == null || rv.size() == 0) {
                // If this list is empty, we have to fake something
                // to prevent Hibernate from complaining.
                rv = Collections.singletonList(Long.MIN_VALUE);
            }
        }

        return rv;
    }

    /*
     * @see ode.model.internal.Permissions#bit(Role, Right)
     */
    protected static String isGranted(Role role, Right right) {
        String bit = "" + Permissions.bit(role, right);
        String isGranted = String
                .format(
                        "(select (__g.permissions & %s) = %s from " +
                        "experimentergroup __g where __g.id = group_id)",
                        bit, bit);
        return isGranted;
    }

}