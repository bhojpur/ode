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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ode.model.enums.AdminPrivilege;
import ode.model.internal.Permissions;

/**
 * simple, non-thread-safe, serializable {@link EventContext}
 */
public class SimpleEventContext implements EventContext, Serializable {

    private static final long serialVersionUID = -391820349350359539L;

    protected Long shareId;

    protected Long csId;

    protected Long cgId;

    protected Long cuId;

    protected Long csuId;

    protected Long ceId;

    protected String csName;

    protected String cgName;

    protected String cuName;

    protected String csuName;

    protected String ceType;

    protected boolean isAdmin;

    protected Set<AdminPrivilege> adminPrivileges;

    protected boolean isReadOnly;

    protected List<Long> memberOfGroups;

    protected List<Long> leaderOfGroups;

    protected Permissions umask;

    private Permissions groupPermissions;

    /** Constructor for subclasses */
    protected SimpleEventContext() {
    }

    /** copy constructor. Makes defensive copies where necessary */
    public SimpleEventContext(EventContext ec) {
        if (ec == null) {
            throw new IllegalArgumentException("Argument cannot be null.");
        }
        copy(ec);
    }

    /**
     * Copies all values directly from the given instance into this instance. If
     * any of {@link #getCurrentEventId()}, {@link #isCurrentUserAdmin()},
     * {@link #isReadOnly()}, or {@link #getCurrentUmask()} throws an
     * exception, those fields will remain null assuming that the
     * {@code ode.security.SecuritySystem} will reload them later.
     */
    protected void copy(EventContext ec) {
        this.shareId = ec.getCurrentShareId();
        this.csId = ec.getCurrentSessionId();
        this.cgId = ec.getCurrentGroupId();
        this.cuId = ec.getCurrentUserId();
        this.csuId = ec.getCurrentSudoerId();
        this.csName = ec.getCurrentSessionUuid();
        this.cgName = ec.getCurrentGroupName();
        this.cuName = ec.getCurrentUserName();
        this.csuName = ec.getCurrentSudoerName();
        this.ceType = ec.getCurrentEventType();
        this.memberOfGroups = new ArrayList<Long>(ec.getMemberOfGroupsList());
        this.leaderOfGroups = new ArrayList<Long>(ec.getLeaderOfGroupsList());
        setGroupPermissions(ec.getCurrentGroupPermissions());
        try {
            this.isAdmin = ec.isCurrentUserAdmin();
            this.adminPrivileges = ec.getCurrentAdminPrivileges();
            this.isReadOnly = ec.isReadOnly();
            this.ceId = ec.getCurrentEventId();
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    public Long getCurrentShareId() {
        return shareId;
    }

    public Long getCurrentSessionId() {
        return csId;
    }

    public String getCurrentSessionUuid() {
        return csName;
    }

    public Long getCurrentGroupId() {
        return cgId;
    }

    public String getCurrentGroupName() {
        return cgName;
    }

    public Long getCurrentUserId() {
        return cuId;
    }

    public String getCurrentUserName() {
        return cuName;
    }

    @Override
    public Long getCurrentSudoerId() {
        return csuId;
    }

    @Override
    public String getCurrentSudoerName() {
        return csuName;
    }

    public boolean isCurrentUserAdmin() {
        return isAdmin;
    }

    @Override
    public Set<AdminPrivilege> getCurrentAdminPrivileges() {
        return adminPrivileges;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public List<Long> getMemberOfGroupsList() {
        return memberOfGroups;
    }

    public List<Long> getLeaderOfGroupsList() {
        return leaderOfGroups;
    }

    public Long getCurrentEventId() {
        return ceId;
    }

    public String getCurrentEventType() {
        return ceType;
    }

    public Permissions getCurrentUmask() {
        return umask;
    }

    public Permissions getCurrentGroupPermissions() {
        return groupPermissions;
    }

    protected void setGroupPermissions(Permissions p) {
        this.groupPermissions = p;
        if (this.groupPermissions == null) {
            throw new ode.conditions.InternalException("null permissions");
        }
    }

}