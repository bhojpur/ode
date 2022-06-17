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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ode.system.EventContext;
import ode.model.enums.AdminPrivilege;
import ode.model.internal.Details;
import ode.system.Roles;

import org.hibernate.Filter;
import org.hibernate.Session;

import com.google.common.collect.ImmutableMap;

/**
 * Filter database queries to respect light administrator privileges.
 * Specifically, provide means to be sudo-aware in filtering according
 * to the current {@link ode.model.meta.Session}'s ownership and the
 * light administrator's privileges.
 */
@Deprecated
public class LightAdminPrivilegesSecurityFilter extends AbstractSecurityFilter {

    private static final ImmutableMap<String, String> PARAMETER_TYPES =
            ImmutableMap.of("real_owner", "long",
                            "privileges", "string");

    /**
     * Construct a new light administrator filter.
     * @param roles the users and groups that are special to Bhojpur ODE
     */
    public LightAdminPrivilegesSecurityFilter(Roles roles) {
        super(roles);
    }

    @Override
    public Map<String, String> getParameterTypes() {
        return PARAMETER_TYPES;
    }

    @Override
    public String getDefaultCondition() {
        /* provided instead by annotations */
        return null;
    }

    @Override
    public boolean passesFilter(Session session, Details details, EventContext ec) {
        /* this method will not be called with system types */
        return false;
    }

    @Override
    public void enable(Session session, EventContext ec) {
        final List<String> privilegeValues = new ArrayList<String>();
        for (final AdminPrivilege adminPrivilege : ec.getCurrentAdminPrivileges()) {
            privilegeValues.add(adminPrivilege.getValue());
        }
        Long realOwner = ec.getCurrentSudoerId();
        if (realOwner == null) {
            realOwner = ec.getCurrentUserId();
        }

        final int isAdmin01 = ec.isCurrentUserAdmin() ? 1 : 0;

        final Filter filter = session.enableFilter(getName());
        filter.setParameter("real_owner", realOwner);
        if (privilegeValues.isEmpty()) {
            filter.setParameterList("privileges", Collections.singletonList("none"));
        } else {
            filter.setParameterList("privileges", privilegeValues);
        }
        enableBaseFilters(session, isAdmin01, ec.getCurrentUserId());
    }
}