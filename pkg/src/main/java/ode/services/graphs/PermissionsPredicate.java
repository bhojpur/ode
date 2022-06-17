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

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;

import ode.model.IObject;
import ode.model.internal.Permissions;
import ode.model.meta.ExperimenterGroup;
import ode.services.graphs.GraphPolicy.Details;

/**
 * A predicate that allows {@code perms=rwr---} and similar to be used in graph policy rule matches.
 */
public class PermissionsPredicate implements GraphPolicyRulePredicate {

    private Map<Long, String> groupPermissions = new HashMap<Long, String>();

    @Override
    public String getName() {
        return "perms";
    }

    @Override
    public void noteDetails(Session session, IObject object, String realClass, long id) {
        if (!(object instanceof ExperimenterGroup)) {
            /* This is a small object and should typically hit the ORM cache but if still too slow then we could exploit that
             * GraphTraversal.planning.detailsNoted already has the details object cached. */
            final String hql = "SELECT details FROM " + realClass + " WHERE id = " + id;
            final ode.model.internal.Details details = (ode.model.internal.Details) session.createQuery(hql).uniqueResult();
            if (details == null) {
                return;
            }
            object = details.getGroup();
            if (object == null) {
                return;
            }
        }
        final Long groupId = object.getId();
        if (!groupPermissions.containsKey(groupId)) {
            final ExperimenterGroup group = (ExperimenterGroup) session.get(ExperimenterGroup.class, groupId);
            final Permissions permissions = group.getDetails().getPermissions();
            groupPermissions.put(groupId, permissions.toString());
        }
    }

    @Override
    public boolean isMatch(Details object, String parameter) throws GraphException {
        if (object.groupId == null) {
            return false;
        }
        final String permissions = groupPermissions.get(object.groupId);
        if (permissions == null) {
            throw new GraphException("no group permissions for " + object);
        }
        if (parameter.length() != permissions.length()) {
            throw new GraphException(
                    "parameter " + parameter + " has different length from permissions " + permissions + " on " + object);
        }
        int index = permissions.length();
        while (--index >= 0) {
            final char parameterChar = parameter.charAt(index);
            if (parameterChar != '?' && parameterChar != permissions.charAt(index)) {
                return false;
            }
        }
        return true;
    }
}
