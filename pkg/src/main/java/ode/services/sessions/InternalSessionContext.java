package ode.services.sessions;

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

import java.util.Arrays;

import com.google.common.collect.ImmutableSet;

import ode.model.enums.AdminPrivilege;
import ode.model.internal.Permissions;
import ode.model.meta.Session;
import ode.services.sessions.stats.NullSessionStats;
import ode.system.Roles;

/**
 * Essentially dummy {@link SessionContext} implementation which uses the values
 * in {@link Role} to define a root-based admin instance.
 */
class InternalSessionContext extends SessionContextImpl {

    Roles roles;

    InternalSessionContext(Session s, ImmutableSet<AdminPrivilege> adminPrivileges, Roles roles) {
        super(s, adminPrivileges, Arrays.asList(roles.getSystemGroupId()),
                Arrays.asList(roles.getSystemGroupId()),
                Arrays.asList(roles.getSystemGroupName()),
                new NullSessionStats(), roles, null);
        this.roles = roles;
    }

    @Override
    public String getCurrentEventType() {
        return "Internal"; // TODO This should be in Roles
    }

    @Override
    public Long getCurrentGroupId() {
        return roles.getSystemGroupId();
    }

    @Override
    public String getCurrentGroupName() {
        return roles.getSystemGroupName();
    }

    @Override
    public Long getCurrentUserId() {
        return roles.getRootId();
    }

    @Override
    public String getCurrentUserName() {
        return roles.getRootName();
    }

    @Override
    public boolean isCurrentUserAdmin() {
        return true;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    /**
     * Overrides the base definition so that the call to force an early NPE
     * passes in the case of the internal session.
     */
    @Override
    public Permissions getCurrentGroupPermissions() {
        return Permissions.USER_PRIVATE;
    }
}
