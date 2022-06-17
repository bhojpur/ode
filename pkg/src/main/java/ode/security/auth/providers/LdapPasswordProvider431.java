package ode.security.auth.providers;

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

import java.security.Permissions;

import ode.conditions.ApiUsageException;
import ode.logic.LdapImpl;
import ode.model.meta.Experimenter;
import ode.security.SecuritySystem;
import ode.security.auth.ConfigurablePasswordProvider;
import ode.security.auth.PasswordProvider;
import ode.security.auth.PasswordUtil;

import org.springframework.util.Assert;

/**
 * LDAP {@link PasswordProvider} which can create users on
 * {@link #checkPassword(String, String, boolean) request} to synchronize with an LDAP
 * directory. Assuming that a user exists in the configured LDAP store but not
 * in the database, then a new user will be created. Authentication, however,
 * always takes place against LDAP, and changing passwords is not allowed.
 *
 * Note: deleted LDAP users will not be removed from Bhojpur ODE, but will not be able
 * to login.
 *
 * Note: unlike {@link ode.security.auth.LdapPasswordProvider}, this implementation
 * (the default LDAP password provider up until 4.3.2) does <em>not</em> check
 * the user_filter on every login, but only when a user does not exist. This means
 * that when using this implementation it is not possible to remove a user's login
 * simply by modifying a part of the user_filter.
 */

public class LdapPasswordProvider431 extends ConfigurablePasswordProvider {

    final protected LdapImpl ldapUtil;

    public LdapPasswordProvider431(PasswordUtil util, LdapImpl ldap) {
        super(util);
        Assert.notNull(ldap);
        this.ldapUtil = ldap;
    }

    public LdapPasswordProvider431(PasswordUtil util,
            LdapImpl ldap,
            boolean ignoreUnknown) {
        super(util, ignoreUnknown);
        Assert.notNull(ldap);
        this.ldapUtil = ldap;
    }

    /**
     * Only returns if the user is already in the database and has a DN value in
     * the password table. Note: after a call to
     * {@link #checkPassword(String, String,boolean)} with this same user value, this
     * method might begin to return {@code true} due to a call to
     * {@link LdapImpl#createUser(String, String)}.
     */
    @Override
    public boolean hasPassword(String user) {
        if (ldapUtil.getSetting()) {
            Long id = util.userId(user);
            if (id != null) {
                String dn = ldapUtil.lookupLdapAuthExperimenter(id);
                if (dn != null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Boolean checkPassword(String user, String password, boolean readOnly) {

        if (!ldapUtil.getSetting()) {
            return null; // EARLY EXIT!
        }

        Long id = util.userId(user);

        // Unknown user. First try to create.
        if (null == id) {
            try {
                if (readOnly == true) {
                    throw new IllegalStateException("Cannot create user!");
                }
                Experimenter experimenter = ldapUtil.createUser(user, password);
                // Use default logic if the user creation did not exist,
                // because there may be another non-database login mechanism
                // which should also be given a chance.
                if (experimenter != null) {
                    loginAttempt(user, true);
                    return true;
                }
            } catch (ApiUsageException e) {
                log.info(String.format(
                        "Default choice on create user: %s (%s)", user, e));
            }
        }

        // Known user
        else {
            try {
                String dn = ldapUtil.lookupLdapAuthExperimenter(id);
                if (dn != null) {
                    return loginAttempt(user,
                            ldapUtil.validatePassword(dn, password));
                }
            } catch (ApiUsageException e) {
                log.warn("Default choice on check ldap password: " + user, e);
            }
        }

        // If anything goes wrong, use the default (configurable) logic.
        return super.checkPassword(user, password, readOnly);
    }

}