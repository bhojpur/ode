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

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ode.model.enums.AdminPrivilege;
import ode.model.internal.NamedValue;
import ode.model.meta.Experimenter;
import ode.model.meta.Session;
import ode.system.Roles;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * Report the light administrator privileges associated with a given session.
 * Caches recent results.
 */
public class LightAdminPrivileges {

    private static final Logger LOGGER = LoggerFactory.getLogger(LightAdminPrivileges.class);

    private static final String USER_CONFIG_NAME_PREFIX = AdminPrivilege.class.getSimpleName() + ':';

    // NOTE THE SET OF LIGHT ADMINISTRATOR PRIVILEGES AND THEIR ASSOCIATED VALUE STRINGS

    private static final ImmutableSet<AdminPrivilege> ADMIN_PRIVILEGES;
    private static final ImmutableMap<String, AdminPrivilege> ADMIN_PRIVILEGES_BY_VALUE;

    static {
        final ImmutableSet<String> privilegeValues = ImmutableSet.of(
                AdminPrivilege.VALUE_CHGRP,
                AdminPrivilege.VALUE_CHOWN,
                AdminPrivilege.VALUE_DELETE_FILE,
                AdminPrivilege.VALUE_DELETE_MANAGED_REPO,
                AdminPrivilege.VALUE_DELETE_OWNED,
                AdminPrivilege.VALUE_DELETE_SCRIPT_REPO,
                AdminPrivilege.VALUE_MODIFY_GROUP,
                AdminPrivilege.VALUE_MODIFY_GROUP_MEMBERSHIP,
                AdminPrivilege.VALUE_MODIFY_USER,
                AdminPrivilege.VALUE_READ_SESSION,
                AdminPrivilege.VALUE_SUDO,
                AdminPrivilege.VALUE_WRITE_FILE,
                AdminPrivilege.VALUE_WRITE_MANAGED_REPO,
                AdminPrivilege.VALUE_WRITE_OWNED,
                AdminPrivilege.VALUE_WRITE_SCRIPT_REPO);

        final ImmutableMap.Builder<String, AdminPrivilege> builder = ImmutableMap.builder();
        for (final String privilegeValue : privilegeValues) {
            builder.put(privilegeValue, new AdminPrivilege(privilegeValue));
        }
        ADMIN_PRIVILEGES_BY_VALUE = builder.build();
        ADMIN_PRIVILEGES = ImmutableSet.copyOf(ADMIN_PRIVILEGES_BY_VALUE.values());
    }

    // PUBLIC QUERY METHODS

    /**
     * @return all the light administrator privileges
     */
    public static ImmutableSet<AdminPrivilege> getAllPrivileges() {
        return ADMIN_PRIVILEGES;
    }

    /**
     * @param value the string value of a light administrator privilege as recorded in {@code Experimenter.config.name}
     * @return the corresponding privilege, or {@code null} if there is no privilege with that string value
     */
    public AdminPrivilege getPrivilegeForConfigName(String value) {
        if (value.startsWith(USER_CONFIG_NAME_PREFIX)) {
            return getPrivilege(value.substring(USER_CONFIG_NAME_PREFIX.length()));
        } else {
            return null;
        }
    }

    /**
     * @param privilege a light administrator privilege
     * @return the string value of the given privilege as recorded in {@code Experimenter.config.name}
     */
    public String getConfigNameForPrivilege(AdminPrivilege privilege) {
        return USER_CONFIG_NAME_PREFIX + privilege.getValue();
    }

    /**
     * @param value the string value of a light administrator privilege
     * @return the corresponding privilege, or {@code null} if there is no privilege with that string value
     */
    public AdminPrivilege getPrivilege(String value) {
        final AdminPrivilege privilege = ADMIN_PRIVILEGES_BY_VALUE.get(value);
        if (privilege == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("checked for unknown privilege " + value);
            }
            return null;
        }
        return privilege;
    }

    /**
     * Determine the light administrator privileges associated with a session.
     * If the session originates via <q>sudo</q>, takes that into account.
     * Does <em>not</em> take account of if the relevant user is a member of <tt>system</tt>:
     * calculates assuming that the user is an administrator.
     * Caches newly fetched privileges for future lookups.
     * @param session a Bhojpur ODE session
     * @return the light administrator privileges associated with the session
     */
    public ImmutableSet<AdminPrivilege> getSessionPrivileges(Session session) {
        return getSessionPrivileges(session, true);
    }

    /**
     * Determine the light administrator privileges associated with a session.
     * If the session originates via <q>sudo</q>, takes that into account.
     * Does <em>not</em> take account of if the relevant user is a member of <tt>system</tt>:
     * calculates assuming that the user is an administrator.
     * @param session a Bhojpur ODE session
     * @param isCache if newly fetched privileges should be cached for future lookups
     * @return the light administrator privileges associated with the session
     */
    private ImmutableSet<AdminPrivilege> getSessionPrivileges(Session session, boolean isCache) {
        final SessionEqualById wrappedSession = new SessionEqualById(session);
        try {
            if (isCache) {
                return PRIVILEGE_CACHE.get(wrappedSession);
            } else {
                final ImmutableSet<AdminPrivilege> privileges = PRIVILEGE_CACHE.getIfPresent(wrappedSession);
                if (privileges != null) {
                    return privileges;
                } else {
                    return getPrivileges(session);
                }
            }
        } catch (ExecutionException ee) {
            LOGGER.warn("failed to check privileges for session " + session.getId(), ee.getCause());
            return ImmutableSet.of();
        }
    }

    // DETERMINE AND CACHE PRIVILEGES FOR SESSIONS

    private final long rootId;

    /**
     * @param roles the Bhojpur ODE roles
     */
    public LightAdminPrivileges(Roles roles) {
        rootId = roles.getRootId();
    }

    private final LoadingCache<SessionEqualById, ImmutableSet<AdminPrivilege>> PRIVILEGE_CACHE =
            CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build(
                    new CacheLoader<SessionEqualById, ImmutableSet<AdminPrivilege>>() {
                        @Override
                        public ImmutableSet<AdminPrivilege> load(SessionEqualById wrappedSession) {
                            try {
                                return getPrivileges(wrappedSession.session);
                            } catch (Throwable t) {
                                /* Guava's loading caches do not report exception messages well */
                                LOGGER.error("failed to check permissions for session #" + wrappedSession.sessionId, t);
                                throw t;
                            }
                        }
                    });

    /**
     * Determine the light administrator privileges associated with a session.
     * If the session originates via <q>sudo</q>, takes that into account.
     * Does <em>not</em> take account of if the relevant user is a member of <tt>system</tt>:
     * calculates assuming that the user is an administrator.
     * Assumes that <tt>root</tt> has all light administrator privileges.
     * @param session a Bhojpur ODE session
     * @return the light administrator privileges associated with the session
     */
    private ImmutableSet<AdminPrivilege> getPrivileges(Session session) {
        final Set<AdminPrivilege> privileges = new HashSet<>(getAllPrivileges());
        removeUserPrivileges(session.getSudoer(), privileges);
        removeUserPrivileges(session.getOwner(), privileges);
        return ImmutableSet.copyOf(privileges);
    }

    /**
     * Remove from the given light administrator privileges those not shared by the given user.
     * Does <em>not</em> take account of if the user is a member of <tt>system</tt>:
     * calculates assuming that the user is an administrator.
     * Assumes that <tt>root</tt> has all light administrator privileges.
     * @param user a user, may be {@code null}
     * @param privileges a set of light administrator privileges
     */
    private void removeUserPrivileges(Experimenter user, Set<AdminPrivilege> privileges) {
        if (user == null || user.getId() == rootId) {
            return;
        }
        final List<NamedValue> config = user.getConfig();
        if (CollectionUtils.isNotEmpty(config)) {
            for (final NamedValue configProperty : config) {
                if (!Boolean.parseBoolean(configProperty.getValue())) {
                    final String configPropertyName = configProperty.getName();
                    if (configPropertyName.startsWith(USER_CONFIG_NAME_PREFIX)) {
                        final String adminPrivilegeName = configPropertyName.substring(USER_CONFIG_NAME_PREFIX.length());
                        privileges.remove(ADMIN_PRIVILEGES_BY_VALUE.get(adminPrivilegeName));
                    }
                }
            }
        }
    }

    /**
     * A wrapper for {@link Session} instances that relies on only their ID property for checking equality.
     */
    private static final class SessionEqualById {

        private final Session session;
        private final Long sessionId;

        /**
         * Wrap a {@link Session} instance.
         * @param session the session to wrap
         */
        private SessionEqualById(Session session) {
            this.session = session;
            this.sessionId = session.getId();
        }

        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            } else if (object instanceof SessionEqualById) {
                return this.sessionId.equals(((SessionEqualById) object).sessionId);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(getClass(), sessionId);
        }
    }
}