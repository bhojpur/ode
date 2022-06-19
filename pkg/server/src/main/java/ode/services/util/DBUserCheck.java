package ode.services.util;

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
import java.util.HashSet;
import java.util.Map;

import ode.system.PreferenceContext;
import ode.system.Roles;
import ode.util.SqlAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hook run by the context. This hook examines the database for the well known
 * user info on creation and provides them to the Spring context via
 * {@link #getRoles()}.
 */
public class DBUserCheck {

    public final static Logger log = LoggerFactory.getLogger(DBUserCheck.class);

    final private SqlAction sql;
    final private PreferenceContext prefs;
    final private Roles roles;

    public DBUserCheck(SqlAction sql, PreferenceContext prefs, ReadOnlyStatus readOnly) throws Exception {
        this.sql = sql;
        this.prefs = prefs;
        this.roles = load();
        if (!readOnly.isReadOnlyDb()) {
            sql.setRoles(roles.getRootId(), roles.getGuestId(),
                    roles.getSystemGroupId(), roles.getUserGroupId(), roles.getGuestGroupId());
        }
    }

    private String getRoleName(String which, String defaultValue) {
        String rv;
        try {
            rv = prefs.getProperty("ode.roles." + which);
        } catch (Exception e) {
            rv = null;
        }

        if (rv == null) {
            return defaultValue;
        }
        return rv;
    }

    public Roles getRoles() {
        return roles;
    }

    public Roles load() throws Exception {
        String userGroup = getRoleName("group.user", "user");
        String sysGroup = getRoleName("group.system", "system");
        String guestGroup = getRoleName("group.guest", "guest");
        String guestUser = getRoleName("user.guest", "guest");
        String rootUser = getRoleName("user.root", "root");

        Long rootUserID = -1L;
        try {
            rootUserID = sql.getUserId(rootUser);
        } catch (Exception e) {
            log.debug("No root user found", e);
        }

        Long guestUserID = -1L;
        try {
            guestUserID = sql.getUserId(guestUser);
        } catch (Exception e) {
            log.debug("No guest user found", e);
        }

        Map<String, Long> groupIDs =
                sql.getGroupIds(new HashSet<String>(
                        Arrays.asList(userGroup, sysGroup, guestGroup)));

        Long userGroupID = groupIDs.get(userGroup);
        if (userGroupID == null) {
            userGroupID = -1L;
        }

        Long sysGroupID = groupIDs.get(sysGroup);
        if (sysGroupID == null) {
            sysGroupID = -1L;
        }

        Long guestGroupID = groupIDs.get(guestGroup);
        if (guestGroupID == null) {
            guestGroupID = -1L;
        }

        log.info("User {}.id = {}", rootUser, rootUserID);
        log.info("User {}.id = {}", guestUser, guestUserID);
        log.info("Group {}.id = {}", sysGroup, sysGroupID);
        log.info("Group {}.id = {}", userGroup, userGroupID);
        log.info("Group {}.id = {}", guestGroup, guestGroupID);
        return new Roles(rootUserID, rootUser,
                sysGroupID, sysGroup,
                userGroupID, userGroup,
                guestUserID, guestUser,
                guestGroupID, guestGroup);
    }

}
