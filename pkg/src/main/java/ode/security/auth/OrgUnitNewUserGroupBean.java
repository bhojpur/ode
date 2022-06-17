package ode.security.auth;

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
import java.util.List;

import ode.security.SecuritySystem;

import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapOperations;
import org.springframework.ldap.core.LdapRdn;

/**
 * Handles the ":ou:" specifier from ode-server.properties by adding the user
 * to a group named by the final organizational unit. For example, if a user
 * is in the group, "ou=Bhojpur,ou=Research,ou=India", then the user will
 * be added to the "Bhojpur" group.
 */
public class OrgUnitNewUserGroupBean implements NewUserGroupBean {

    private final DistinguishedName dn;

    public OrgUnitNewUserGroupBean(DistinguishedName dn) {
        this.dn = dn;
    }

    @SuppressWarnings("unchecked")
    public List<Long> groups(String username, LdapConfig config,
            LdapOperations ldap, RoleProvider provider,
            final AttributeSet attrSet) {

        final List<Long> groups = new ArrayList<Long>();
        final List<LdapRdn> names = dn.getNames();
        for (int i = names.size(); i > 0; i--) {
            LdapRdn name = names.get(i-1);
            if ("ou".equals(name.getKey())) {
                final String grpName = name.getValue("ou");
                groups.add(provider.createGroup(grpName, null, false, true));
                break;
            }
        }
        return groups;
    }

}