package ode.services.graphs;

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

import ode.model.IObject;
import ode.services.graphs.GraphPolicy.Details;
import ode.system.Roles;

import org.hibernate.Session;

import com.google.common.collect.ImmutableMap;

/**
 * A predicate that allows {@code group=system} and similar to be used in graph policy rule matches.
 */
public class GroupPredicate implements GraphPolicyRulePredicate {

    private static enum GroupMarker { SYSTEM, USER, GUEST };

    private static ImmutableMap<String, GroupMarker> groupsByName = ImmutableMap.of(
            "system", GroupMarker.SYSTEM,
            "user",   GroupMarker.USER,
            "guest",  GroupMarker.GUEST);

    private final ImmutableMap<Long, GroupMarker> groupsById;

    /**
     * Construct a new group predicate.
     * @param securityRoles the security roles
     */
    public GroupPredicate(Roles securityRoles) {
        groupsById = ImmutableMap.of(
                securityRoles.getSystemGroupId(), GroupMarker.SYSTEM,
                securityRoles.getUserGroupId(),   GroupMarker.USER,
                securityRoles.getGuestGroupId(),  GroupMarker.GUEST);
    }

    @Override
    public String getName() {
        return "group";
    }

    @Override
    public void noteDetails(Session session, IObject object, String realClass, long id) {
        /* nothing to do */
    }

    @Override
    public boolean isMatch(Details object, String parameter) throws GraphException {
        if (object.groupId == null) {
            return false;
        }
        final boolean isInvert;
        if (parameter.startsWith("!")) {
            parameter = parameter.substring(1);
            isInvert = true;
        } else {
            isInvert = false;
        }
        final GroupMarker sought = groupsByName.get(parameter);
        if (sought == null) {
            throw new GraphException("unknown group: " + parameter);
        }
        final GroupMarker actual = groupsById.get(object.groupId);
        return isInvert != (sought == actual);
    }
}
