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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import ode.model.enums.AdminPrivilege;
import ode.system.EventContext;
import ode.system.Roles;
import ode.util.PrivilegedStringTypeDescriptor;

/**
 * Initialize the filters for the subclasses of {@link ode.util.PrivilegedStringType}.
 */
public class PropertyFilterInitializer {

    private final CurrentDetails currentDetails;
    private final DataSource dataSource;
    private final AdminPrivilege privilegeReadSession;
    private final Roles roles;

    /**
     * The filter for {@link PrivilegedStringTypeDescriptor.Filter#FULL_ADMIN}.
     * Requires either the session owner or a user with {AdminPrivilege#VALUE_READ_SESSION}.
     * @param sessionOwnerId the ID of the {@link ode.model.meta.Session} whose properties are being filtered
     * @return if property values may be read
     */
    private boolean isFullAdmin(long sessionOwnerId) {
        if (currentDetails.size() == 0) {
            /* This cannot be an external user request so permit it. */
            return true;
        }
        /* Determine the currently effective user ID. */
        final EventContext ec = currentDetails.getCurrentEventContext();
        final Long currentUserId = ec.getCurrentUserId();
        if (currentUserId == null) {
            /* This cannot be an external user request so permit it. */
            return true;
        }
        /* Check filter criteria. */
        if (ec.getCurrentAdminPrivileges().contains(privilegeReadSession)) {
            /* User is a full administrator. */
            return true;
        }
        if (sessionOwnerId == currentUserId) {
            /* User is reading their own session. */
            return true;
        }
        /* No requirement is satisfied. */
        return false;
    }

    /**
     * The filter for {@link PrivilegedStringTypeDescriptor.Filter#RELATED_USER}.
     * Requires the experimenter themself, an administrator, a group owner or a fellow group member of a non-private group.
     * @param experimenterId the ID of the {@link ode.model.meta.Experimenter} whose properties are being filtered
     * @return if property values may be read
     */
    private boolean isRelatedUser(long experimenterId) {
        if (currentDetails.size() == 0) {
            /* This cannot be an external user request so permit it. */
            return true;
        }
        /* Never filter system users. */
        if (experimenterId == roles.getRootId() || experimenterId == roles.getGuestId()) {
            return true;
        }
        /* Determine the currently effective user ID. */
        final EventContext ec = currentDetails.getCurrentEventContext();
        final Long currentUserId = ec.getCurrentUserId();
        if (currentUserId == null) {
            /* This cannot be an external user request so permit it. */
            return true;
        }
        /* Check filter criteria. */
        if (ec.isCurrentUserAdmin() || !ec.getLeaderOfGroupsList().isEmpty()) {
            /* User is an administrator or a group owner. */
            return true;
        }
        if (experimenterId == currentUserId) {
            /* User is reading their own user metadata. */
            return true;
        }
        /* The only remaining option is for the user to be a fellow group member of a non-private group. */
        boolean isFellowMember = false;
        try (final Connection connection = dataSource.getConnection()) {
            // note: The "64" in the SQL corresponds to: Permissions.Right.READ.mask() << Permissions.Role.GROUP.shift().
            final PreparedStatement statement = connection.prepareStatement(
                    "SELECT 1 FROM GroupExperimenterMap m1 WHERE m1.child = ? AND m1.parent <> ? AND EXISTS " +
                    "(SELECT 1 FROM GroupExperimenterMap m2 WHERE m2.child = ? AND m1.parent = m2.parent) AND EXISTS " +
                    "(SELECT 1 FROM ExperimenterGroup g WHERE m1.parent = g.id AND g.permissions & 64 = 64)");
            statement.setLong(1, experimenterId);
            statement.setLong(2, roles.getUserGroupId());
            statement.setLong(3, currentUserId);
            statement.setMaxRows(1);
            isFellowMember = statement.executeQuery().next();
            statement.close();
        } catch (SQLException sqle) {
            /* Assume no results. */
        }
        if (isFellowMember) {
            /* User is a fellow group member. */
            return true;
        }
        /* No requirement is satisfied. */
        return false;
    }

    /**
     * Provide the property filter implementations for {@link ode.util.PrivilegedStringType}.
     * @param adminPrivileges the light administrator privileges helper
     * @param currentDetails the details of the current thread's security context
     * @param dataSource the data source to be used for JDBC access to the database
     * @param roles the users and groups that are special to Bhojpur ODE
     */
    public PropertyFilterInitializer(LightAdminPrivileges adminPrivileges, CurrentDetails currentDetails, DataSource dataSource,
            Roles roles) {
        this.currentDetails = currentDetails;
        this.dataSource = dataSource;
        this.privilegeReadSession = adminPrivileges.getPrivilege(AdminPrivilege.VALUE_READ_SESSION);
        this.roles = roles;

        PrivilegedStringTypeDescriptor.setFilter(PrivilegedStringTypeDescriptor.Filter.FULL_ADMIN, this::isFullAdmin);
        PrivilegedStringTypeDescriptor.setFilter(PrivilegedStringTypeDescriptor.Filter.RELATED_USER, this::isRelatedUser);
    }
}