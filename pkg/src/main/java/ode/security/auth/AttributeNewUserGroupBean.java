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
import java.util.Set;

import ode.conditions.ValidationException;
import ode.security.SecuritySystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapOperations;

/**
 * Handles the "*_attribute" specifiers from ode-server.properties.
 *
 * The values of the attribute equal to the string following ":*_attribute:" are
 * taken to be the names and/or DNs of {@link ode.model.meta.ExperimenterGroup}
 * instances and created if necessary. If the constructor's {@code filtered}
 * argument is set to {@code true}, then the names/DNs found must pass the
 * assigned group filter.
 */
public class AttributeNewUserGroupBean implements NewUserGroupBean {

    private final static Logger log = LoggerFactory.getLogger(AttributeNewUserGroupBean.class);

    /**
     * The value following ":*attribute:" in the configuration, where "*"
     * can be one of: "", "filtered_", "dn_", and "filtered_dn_".
     */
    private final String grpAttribute;

    /**
     * Whether or not the group filter should be applied to found groups.
     */
    private final boolean filtered;

    /**
     * Whether the value of the attribute should be interpreted as a DN
     * or as the group name.
     */
    private final boolean dn;

    public AttributeNewUserGroupBean(String grpAttribute, boolean filtered, boolean dn) {
        this.grpAttribute = grpAttribute;
        this.filtered = filtered;
        this.dn = dn;
    }

    @SuppressWarnings("unchecked")
    public List<Long> groups(String username, LdapConfig config,
            LdapOperations ldap, RoleProvider provider, AttributeSet attrSet) {

        Set<String> groupNames = attrSet.getAll(grpAttribute);
        if (groupNames == null) {
            throw new ValidationException(username + " has no attributes "
                    + grpAttribute);
        }

        final GroupAttributeMapper mapper = new GroupAttributeMapper(config);

            // If filtered is activated, then load all group names as mapped
        // via the name field.
        //
        // TODO: this should likely be done via either paged queries
        // or once for each target.
        List<String> filteredNames = null;
        if (filtered) {
            String filter = config.getGroupFilter().encode();
            filteredNames = (List<String>) ldap.search("", filter, mapper);
        }

        List<Long> groups = new ArrayList<Long>();
        for (String grpName : groupNames) {
            // If DN is true, then we need to map from the attribute value
            // to the actual group name before comparing.
            if (dn) {
                DistinguishedName relative = config.relativeDN(grpName);
                String nameAttr = config.getGroupAttribute("name");
                grpName = relative.getValue(nameAttr);
            }

            // Apply filter if necessary.
            if (filtered && !filteredNames.contains(grpName)) {
                log.debug("Group not found by filter: " + grpName);
                continue;
            }

            // Finally, add the grou
            groups.add(provider.createGroup(grpName, null, false, true));

        }
        return groups;

    }

}