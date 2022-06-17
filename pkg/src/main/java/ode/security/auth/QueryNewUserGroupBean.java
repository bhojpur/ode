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
import java.util.Collections;
import java.util.List;

import ode.conditions.ValidationException;
import ode.security.SecuritySystem;

import org.apache.commons.lang.StringUtils;
import org.springframework.ldap.core.LdapOperations;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.HardcodedFilter;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the ":query:" specifier from ode-server.properties.
 *
 * The string following ":query:" is interpreted as an LDAP query to be run in
 * combination with the "ode.ldap.group_filter" value. Properties of the form
 * "${}" will be replaced with found user properties.
 */
public class QueryNewUserGroupBean implements NewUserGroupBean, NewUserGroupOwnerBean {

    private final static Logger log = LoggerFactory.getLogger(QueryNewUserGroupBean.class);

    private final String grpQuery;

    public QueryNewUserGroupBean(String grpQuery) {
        this.grpQuery = grpQuery;
    }

    private String parseQuery(final AttributeSet attrSet, final String query) {
        PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("@{",
                "}", null, false);
        return helper.replacePlaceholders(query,
                new PlaceholderResolver() {
                    public String resolvePlaceholder(String arg0) {
                        if (attrSet.size(arg0) > 1) {
                            throw new ValidationException(
                                    "Multivalued property used in @{} format:"
                                            + query + "="
                                            + attrSet.getAll(arg0).toString());
                        }
                        return attrSet.getFirst(arg0);
                    }
                });
    }

    @SuppressWarnings("unchecked")
    private List<Long> _groups(boolean owner, String username, LdapConfig config,
            LdapOperations ldap, RoleProvider provider, final AttributeSet attrSet) {

        String query = parseQuery(attrSet, grpQuery);
        String ownerQuery = null;
        if (owner) {
            ownerQuery = config.getNewUserGroupOwner();
            if (StringUtils.isBlank(ownerQuery)) {
                log.debug("Owner query disabled");
                return Collections.emptyList(); // EARLY EXIT
            }
            ownerQuery = parseQuery(attrSet, ownerQuery);
        }

        AndFilter and = new AndFilter();
        and.and(config.getGroupFilter());
        and.and(new HardcodedFilter(query));
        if (owner) {
            and.and(new HardcodedFilter(ownerQuery));
        }

        log.debug("Running query: {}", and.encode());
        List<String> groupNames = (List<String>) ldap.search("", and.encode(),
            new GroupAttributeMapper(config));

        List<Long> groups = new ArrayList<Long>(groupNames.size());
        for (String groupName : groupNames) {
            groups.add(provider.createGroup(groupName, null, false, true));
        }
        return groups;

    }

    @Override
    public List<Long> groups(String username, LdapConfig config,
            LdapOperations ldap, RoleProvider provider,
            AttributeSet attrSet) {
        return _groups(false, username, config, ldap, provider, attrSet);
    }

    @Override
    public List<Long> ownerOfGroups(String username, LdapConfig config,
            LdapOperations ldap, RoleProvider provider, AttributeSet attrSet) {
        return _groups(true, username, config, ldap, provider, attrSet);
    }
}