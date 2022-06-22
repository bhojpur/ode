package ode.system;

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

import java.io.Serializable;

import ode.model.meta.Experimenter;
import ode.model.meta.ExperimenterGroup;

/**
 * encapsulates the naming scheme for critical system groups and accounts.
 * 
 * These values are also used during install to initialize the database.
 */
public final class Roles implements Serializable {

    private static final long serialVersionUID = -2488864989534638213L;

    private final long rId;

    private final String rName;

    private final long sgId;

    private final String sgName;

    private final long ugId;

    private final String ugName;

    private final long guestId;

    private final String guestName;

    private final long ggId;

    private final String ggName;

    /** default constructor which assigns hard-coded values to all roles */
    public Roles() {
        long nextUserId = -1;
        long nextGroupId = -1;
        /* these must be defined in the same order as in psql-footer.vm */
        this.rId = ++nextUserId;
        this.rName = "root";
        this.sgId = ++nextGroupId;
        this.sgName = "system";
        this.ugId = ++nextGroupId;
        this.ugName = "user";
        this.guestId = ++nextUserId;
        this.guestName = "guest";
        this.ggId = ++nextGroupId;
        this.ggName = "guest";
    }

    /** constructor which allows full specification of all roles */
    public Roles(long rootUserId, String rootUserName,
            long systemGroupId, String systemGroupName, long userGroupId, String userGroupName,
            long guestUserId, String guestUserName, long guestGroupId, String guestGroupName) {
        this.rId = rootUserId;
        this.rName = rootUserName;
        this.sgId = systemGroupId;
        this.sgName = systemGroupName;
        this.ugId = userGroupId;
        this.ugName = userGroupName;
        this.guestId = guestUserId;
        this.guestName = guestUserName;
        this.ggId = guestGroupId;
        this.ggName = guestGroupName;
    }

    // ~ Checks
    // =========================================================================

    public boolean isRootUser(Experimenter user) {
        return user != null && user.getId() != null && user.getId().equals(getRootId());
    }

    public boolean isUserGroup(ExperimenterGroup group) {
        return group != null && group.getId() != null && group.getId().equals(getUserGroupId());
    }

    public boolean isSystemGroup(ExperimenterGroup group) {
        return group != null && group.getId() != null && group.getId().equals(getSystemGroupId());
    }

    // ~ Accessors
    // =========================================================================

    /**
     * @return the id of the root user
     */
    public long getRootId() {
        return rId;
    }

    /**
     * @return the {@link Experimenter#getOdeName()} of the root user
     */
    public String getRootName() {
        return rName;
    }

    /**
     * @return the id of the guest user
     */
    public long getGuestId() {
        return guestId;
    }

    /**
     * @return the {@link Experimenter#getOdeName()} of the guest user
     */
    public String getGuestName() {
        return guestName;
    }

    /**
     * @return the id of the system group
     */
    public long getSystemGroupId() {
        return sgId;
    }

    /**
     * @return the {@link ExperimenterGroup#getName()} of the system group
     */
    public String getSystemGroupName() {
        return sgName;
    }

    /**
     * @return the id of the user group
     */
    public long getUserGroupId() {
        return ugId;
    }

    /**
     * @return the {@link ExperimenterGroup#getName()} of the user group
     */
    public String getUserGroupName() {
        return ugName;
    }

    /**
     * @return the id of the guest group
     */
    public long getGuestGroupId() {
        return ggId;
    }

    /**
     * @return the {@link ExperimenterGroup#getName()} of the guest group
     */
    public String getGuestGroupName() {
        return ggName;
    }
}