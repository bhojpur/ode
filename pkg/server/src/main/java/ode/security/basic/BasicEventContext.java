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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ode.system.EventContext;
import ode.system.Principal;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ode.api.local.LocalAdmin;
import ode.conditions.SecurityViolation;
import ode.model.IObject;
import ode.model.enums.AdminPrivilege;
import ode.model.internal.Permissions;
import ode.model.meta.Event;
import ode.model.meta.EventLog;
import ode.model.meta.Experimenter;
import ode.model.meta.ExperimenterGroup;
import ode.services.messages.RegisterServiceCleanupMessage;
import ode.services.sessions.stats.SessionStats;
import ode.services.sharing.ShareStore;
import ode.services.sharing.data.ShareData;
import ode.system.SimpleEventContext;

/**
 * {@link EventContext} implementation for use within the security system. Holds
 * various other information needed for proper functioning of a {@link Thread}.
 * 
 * Not-thread-safe. Intended to be held by a {@link ThreadLocal}
 */
public class BasicEventContext extends SimpleEventContext {

    private final static Logger log = LoggerFactory.getLogger(BasicEventContext.class);

    // Additions beyond simple event context
    // =========================================================================

    /**
     * Principal should be set only once (on
     * {@link PrincipalHolder#login(Principal)}.
     */
    private final Principal p;
    
    private final SessionStats stats;

    private Set<String> disabledSubsystems;

    private Set<RegisterServiceCleanupMessage> serviceCleanups;

    private Set<IObject> lockCandidates;

    private List<EventLog> logs;

    private Event event;

    private Experimenter owner;

    private Experimenter sudoer;

    private ExperimenterGroup group;

    private Map<String, String> callContext;

    private Map<Long, Permissions> groupPermissionsMap;

    public BasicEventContext(Principal p, SessionStats stats) {
        if (p == null || stats == null) {
            throw new RuntimeException("Principal and stats canot be null.");
        }
        this.p = p;
        this.stats = stats;
    }

    /**
     * Copy-constructor to not have to allow the mutator {@link #copy(EventContext)}
     * or {@code copyContext(EventContext)} out of the {@link EventContext}
     * hierarchy.
     */
    public BasicEventContext(Principal p, SessionStats stats, EventContext ec) {
        this(p, stats);
        copyContext(ec);
    }

    void invalidate() {
        owner = null;
        sudoer = null;
        group = null;
        event = null;
    }

    /**
     * Making {@link SimpleEventContext#copy(EventContext)} available to
     * package-private classes.
     */
    void copyContext(EventContext ec) {
        super.copy(ec);
    }

    void checkAndInitialize(EventContext ec, LocalAdmin admin, ShareStore store) {
        this.copyContext(ec);

        // Now re-apply values.
        List<String> toPrint = null;

        final Long sid = parseId(callContext, "ode.share");
        if (sid != null) {
            if (!isAdmin) {
                // If the user is not an admin then we need to verify that
                // s/he is a valid member of the share.
                ShareData data = store.getShareIfAccessible(sid, isAdmin, this.cuId);
                if (data == null) {
                    throw new SecurityViolation(String.format(
                            "User %s cannot access share %s", this.cuId, sid));
                }
            }
            setShareId(sid);
            if (toPrint == null) {
                toPrint = new ArrayList<String>();
            }
            toPrint.add("share="+sid);
            return; // IGNORE all other settings for share (#8608)
        }

        final Long uid = parseId(callContext, "ode.user");
        if (uid != null) {
            // Here we trust the setting of the admin flag if we also have
            // a user setting. In other words, if this has been initialized
            // by the session context, then it's safe to say that we're just
            // overwriting values.
            if (cuId != null && !isAdmin && !cuId.equals(uid)) {
                throw new SecurityViolation(String.format(
                        "User %s is not an admin and so cannot set uid to %s",
                        cuId, uid));
            }
            setOwner(admin.userProxy(uid));
            if (toPrint == null) {
                toPrint = new ArrayList<String>();
            }
            toPrint.add("owner="+uid);
        }

        final Long gid = parseId(callContext, "ode.group");
        if (gid != null) {
            if (gid < 0) {
                setGroup(new ExperimenterGroup(gid, false), Permissions.DUMMY);
            } else {
                ExperimenterGroup g = admin.groupProxy(gid);
                setGroup(g, g.getDetails().getPermissions());
            }
            if (toPrint == null) {
                toPrint = new ArrayList<String>();
            }
            toPrint.add("group="+gid);
        }

        if (toPrint != null && toPrint.size() > 0) {
            log.info(" cctx:\t" + StringUtils.join(toPrint, ","));
        }
    }

    // Call Context
    // =========================================================================

    static Long parseId(Map<String, String> ctx, String key) {
        Long rv = null;
        if (ctx != null && ctx.containsKey(key)) {
            String s = ctx.get(key);
            try {
                rv = Long.valueOf(ctx.get(key));
                log.debug("Using call requested group: " + key + "=" + s);
            } catch (Exception e) {
                log.warn("Ignoring invalid requested group: " + key + "=" + s);
            }
        }
        return rv;
    }

    public Map<String, String> getCallContext() {
        return callContext;
    }

    public Map<String, String> setCallContext(Map<String, String> ctx) {
        final Map<String, String> rv = callContext;
        callContext = ctx;
        return rv;
    }

    // ~ Getters/Setters for superclass state
    // =========================================================================

    public void setUmask(Permissions umask) {
        this.umask = umask;
    }

    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
    }

    public void setAdminPrivileges(Set<AdminPrivilege> adminPrivileges) {
        this.adminPrivileges = adminPrivileges;
    }

    public Set<AdminPrivilege> getAdminPrivileges() {
        return adminPrivileges;
    }

    public void setReadOnly(boolean readOnly) {
        this.isReadOnly = readOnly;
    }

    public void setShareId(Long id) {
        this.shareId = id;
    }

    // ~ Accessors for other state
    // =========================================================================

    public Principal getPrincipal() {
        return p;
    }
    
    public SessionStats getStats() {
        return stats;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        this.ceId = event.getId();
        if (event.isLoaded()) {
            if (event.getType().isLoaded()) {
                this.ceType = event.getType().getValue();
            }
        }
    }

    public Experimenter getOwner() {
        return owner;
    }

    public void setOwner(Experimenter owner) {
        this.owner = owner;
        this.cuId = owner.getId();
        if (owner.isLoaded()) {
            this.cuName = owner.getOdeName();
        }
    }

    public Experimenter getSudoer() {
        return sudoer;
    }

    public void setSudoer(Experimenter sudoer) {
        this.sudoer = sudoer;
        if (sudoer == null) {
            this.csuId = null;
            this.csuName = null;
        } else {
            this.csuId = sudoer.getId();
            if (sudoer.isLoaded()) {
                this.csuName = sudoer.getOdeName();
            }
        }
    }

    public ExperimenterGroup getGroup() {
        return group;
    }

    public void setGroup(ExperimenterGroup group, Permissions p) {
        this.group = group;
        setGroupPermissions(p);
        if (this.cgId.equals(group.getId())) {
            // Do nothing.
        } else {
            this.cgId = group.getId();
            this.cgName = null;
            // If unloaded or group.id < -1 these will remain null
            if (group.isLoaded()) {
                this.cgName = group.getName();
            }
        }
    }

    public Set<String> getDisabledSubsystems() {
        return disabledSubsystems;
    }

    public void setDisabledSubsystems(Set<String> disabledSubsystems) {
        this.disabledSubsystems = disabledSubsystems;
    }

    public Set<RegisterServiceCleanupMessage> getServiceCleanups() {
        return serviceCleanups;
    }

    public void setServiceCleanups(
            Set<RegisterServiceCleanupMessage> serviceCleanups) {
        this.serviceCleanups = serviceCleanups;
    }

    public Set<IObject> getLockCandidates() {
        return lockCandidates;
    }

    public void setLockCandidates(Set<IObject> lockCandidates) {
        this.lockCandidates = lockCandidates;
    }

    public List<EventLog> getLogs() {
        return logs;
    }

    public void setLogs(List<EventLog> logs) {
        this.logs = logs;
    }

    // ~ Special logic for groups
    // =========================================================================

    @Override
    public List<Long> getMemberOfGroupsList() {
        return memberOfGroups;
    }

    @Override
    public List<Long> getLeaderOfGroupsList() {
        return leaderOfGroups;
    }

    public void setMemberOfGroups(List<Long> groupIds) {
        this.memberOfGroups = groupIds;
    }

    public void setLeaderOfGroups(List<Long> groupIds) {
        this.leaderOfGroups = groupIds;
    }

    // Other
    // =========================================================================

    /**
     * Never returns {@link Permissions#DUMMY}.
     * @param group a group ID, may be {@code null}
     * @return the group's permissions, may be {@code null}
     */
    public Permissions getPermissionsForGroup(Long group) {
        if (group == null || groupPermissionsMap == null) {
            return null;
        }
        return groupPermissionsMap.get(group);
    }

    /**
     * Called during {@link BasicACLVoter#allowLoad(org.hibernate.Session, Class, ode.model.internal.Details, long)}
     * to track groups that will need resolving later.
     * @param group a group ID, not {@code null}
     * @param perms the group's permissions
     * @return the group's previous permissions, or {@code null} if none are noted
     */
    public Permissions setPermissionsForGroup(Long group, Permissions perms) {
        if (perms == Permissions.DUMMY) {
            throw new ode.conditions.InternalException(
                "DUMMY permissions passed to setPermissionsForGroup!");
        }

        if (groupPermissionsMap == null) {
            groupPermissionsMap = new HashMap<Long, Permissions>();
        }
        return groupPermissionsMap.put(group, perms);
    }

    public void loadPermissions(org.hibernate.Session session) {
        if (groupPermissionsMap != null) {
            for (Map.Entry<Long, Permissions> entry :
                groupPermissionsMap.entrySet()) {
                if (entry.getValue() == null) {
                    Long id = entry.getKey();
                    ExperimenterGroup g = (ExperimenterGroup)
                        session.get(ExperimenterGroup.class, id);
                    entry.setValue(g.getDetails().getPermissions());
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("(");
        sb.append("Principal:" + p);
        sb.append(";");
        sb.append(this.owner);
        sb.append(";");
        sb.append(this.sudoer);
        sb.append(';');
        sb.append(this.group);
        sb.append(";");
        sb.append(this.event);
        sb.append(";");
        sb.append("ReadOnly:" + this.isReadOnly);
        sb.append(")");
        return sb.toString();
    }

}