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

import static ode.model.internal.Permissions.Role.GROUP;
import static ode.model.internal.Permissions.Role.USER;
import static ode.model.internal.Permissions.Role.WORLD;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ode.conditions.ApiUsageException;
import ode.conditions.GroupSecurityViolation;
import ode.conditions.InternalException;
import ode.conditions.SecurityViolation;
import ode.model.IObject;
import ode.model.core.OriginalFile;
import ode.model.enums.AdminPrivilege;
import ode.model.internal.Details;
import ode.model.internal.Permissions;
import ode.model.internal.Permissions.Right;
import ode.model.internal.Token;
import ode.model.meta.Experimenter;
import ode.model.meta.ExperimenterGroup;
import ode.model.meta.GroupExperimenterMap;
import ode.security.ACLVoter;
import ode.security.SecurityFilter;
import ode.security.SecuritySystem;
import ode.security.SystemTypes;
import ode.security.policy.DefaultPolicyService;
import ode.security.policy.PolicyService;
import ode.services.sessions.SessionProvider;
import ode.services.util.ReadOnlyStatus;
import ode.system.EventContext;
import ode.system.Roles;
import ode.tools.hibernate.HibernateUtils;
import ode.util.PermDetails;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * @see Token
 * @see SecuritySystem
 * @see Details
 * @see Permissions
 */
public class BasicACLVoter implements ACLVoter {

    /**
     * Simple enum to represent the interpretation of "WRITE" permissions.
     */
    private static enum Scope {
        ANNOTATE(Right.ANNOTATE),
        DELETE(Right.WRITE),
        EDIT(Right.WRITE),
        LINK(Right.WRITE),
        CHGRP(Right.WRITE),
        CHOWN(Right.WRITE);

        final Right right;
        Scope(Right right) {
            this.right = right;
        }
    }

    private final static Logger log = LoggerFactory.getLogger(BasicACLVoter.class);

    protected final CurrentDetails currentUser;

    protected final SystemTypes sysTypes;

    protected final TokenHolder tokenHolder;

    protected final SecurityFilter securityFilter;

    protected final PolicyService policyService;

    protected final Roles roles;

    /* ignored if empty set */
    private Set<Class<? extends IObject>> chgrpPermittedClasses = Collections.emptySet();

    /* ignored if empty set */
    private Set<Class<? extends IObject>> chownPermittedClasses = Collections.emptySet();

    private final LightAdminPrivileges adminPrivileges;

    private final SessionProvider sessionProvider;

    private final boolean isReadOnlyDb;

    /* thread-safe */
    private final Set<String> managedRepoUuids, scriptRepoUuids;

    private final String fileRepoSecretKey;

    public BasicACLVoter(CurrentDetails cd, SystemTypes sysTypes,
            TokenHolder tokenHolder, SecurityFilter securityFilter) {
            this(cd, sysTypes, tokenHolder, securityFilter,
                    new DefaultPolicyService(), new Roles());
        }

    public BasicACLVoter(CurrentDetails cd, SystemTypes sysTypes,
            TokenHolder tokenHolder, SecurityFilter securityFilter, SessionProvider sessionProvider, ReadOnlyStatus readOnly) {
            this(cd, sysTypes, tokenHolder, securityFilter,
                    new DefaultPolicyService(), new Roles(), sessionProvider, readOnly);
        }

    @Deprecated
    public BasicACLVoter(CurrentDetails cd, SystemTypes sysTypes,
        TokenHolder tokenHolder, SecurityFilter securityFilter,
        PolicyService policyService) {
        this(cd, sysTypes, tokenHolder, securityFilter, policyService,
                new Roles());
    }

    public BasicACLVoter(CurrentDetails cd, SystemTypes sysTypes,
            TokenHolder tokenHolder, SecurityFilter securityFilter,
            PolicyService policyService,
            Roles roles) {
        this(cd, sysTypes, tokenHolder, securityFilter, policyService,
                roles, new LightAdminPrivileges(roles), null, new ReadOnlyStatus(false, false),
                new HashSet<String>(), new HashSet<String>(), UUID.randomUUID().toString());
        log.info("assuming read-write repository");
    }

    public BasicACLVoter(CurrentDetails cd, SystemTypes sysTypes,
            TokenHolder tokenHolder, SecurityFilter securityFilter,
            PolicyService policyService,
            Roles roles, SessionProvider sessionProvider, ReadOnlyStatus readOnly) {
        this(cd, sysTypes, tokenHolder, securityFilter, policyService,
                roles, new LightAdminPrivileges(roles), sessionProvider, readOnly,
                new HashSet<String>(), new HashSet<String>(), UUID.randomUUID().toString());
    }

    public BasicACLVoter(CurrentDetails cd, SystemTypes sysTypes,
        TokenHolder tokenHolder, SecurityFilter securityFilter,
        PolicyService policyService,
        Roles roles, LightAdminPrivileges adminPrivileges, SessionProvider sessionProvider, ReadOnlyStatus readOnly,
        Set<String> managedRepoUuids, Set<String> scriptRepoUuids, String fileRepoSecretKey) {
        this.currentUser = cd;
        this.sysTypes = sysTypes;
        this.securityFilter = securityFilter;
        this.tokenHolder = tokenHolder;
        this.roles = roles;
        this.policyService = policyService;
        this.adminPrivileges = adminPrivileges;
        this.sessionProvider = sessionProvider;
        this.isReadOnlyDb = readOnly.isReadOnlyDb();
        this.managedRepoUuids = managedRepoUuids;
        this.scriptRepoUuids = scriptRepoUuids;
        this.fileRepoSecretKey = fileRepoSecretKey;
    }

    // ~ Interface methods
    // =========================================================================

    public boolean allowChmod(IObject iObject) {
        if (iObject == null) {
            throw new ApiUsageException("Object can't be null");
        }
        if (isReadOnlyDb) {
            return false;
        }
        final Long ownerId = HibernateUtils.nullSafeOwnerId(iObject);
        final Long groupId; // see 2874 and chmod
        if (iObject instanceof ExperimenterGroup) {
            groupId = iObject.getId();
        } else {
            groupId = HibernateUtils.nullSafeGroupId(iObject);
        }
        final EventContext ec = currentUser.getCurrentEventContext();
        if (ec.getCurrentUserId().equals(ownerId) || ec.getLeaderOfGroupsList().contains(groupId)) {
            return true;   // object owner or group owner
        } else if (!ec.isCurrentUserAdmin()) {
            return false;  // not an admin
        }
        final Set<AdminPrivilege> privileges = ec.getCurrentAdminPrivileges();
        if (sysTypes.isSystemType(iObject.getClass())) {
            if (iObject instanceof Experimenter) {
                return privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_MODIFY_USER));
            } else if (iObject instanceof ExperimenterGroup) {
                return privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_MODIFY_GROUP));
            } else if (iObject instanceof GroupExperimenterMap) {
                return privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_MODIFY_GROUP_MEMBERSHIP));
            } else {
                return true;
            }
        } else {
            if (iObject instanceof OriginalFile) {
                final String repo = ((OriginalFile) iObject).getRepo();
                if (repo != null) {
                    if (managedRepoUuids.contains(repo)) {
                        return privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_WRITE_MANAGED_REPO));
                    } else if (scriptRepoUuids.contains(repo)) {
                        return privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_WRITE_SCRIPT_REPO));
                    }
                }
                return privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_WRITE_FILE));
            } else {
                return privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_WRITE_OWNED));
            }
        }
    }

    /**
     * delegates to SecurityFilter because that is where the logic is defined
     * for the {@link BasicSecuritySystem#enableReadFilter(Object) read filter}
     * 
     * Ignores the id for the moment.
     * 
     * Though we pass in whether or not a share is active for completeness, a
     * different {@link ACLVoter} implementation will almost certainly be active
     * for share use.
     */
    public boolean allowLoad(Session session, Class<? extends IObject> klass, Details d, long id) {
        Assert.notNull(klass);

        final BasicEventContext ec = currentUser.current();

        /* regarding sessions see LightAdminPrivilegesSecurityFilter */
        if (klass == ode.model.meta.Session.class) {
            /* determine "real" owner of current and queried session, i.e. taking sudo into account */
            final ode.model.meta.Session currentSession = sessionProvider.findSessionById(ec.getCurrentSessionId(), session);
            Experimenter sessionOwnerCurrent = currentSession.getSudoer();
            if (sessionOwnerCurrent == null) {
                sessionOwnerCurrent = currentSession.getOwner();
            }
            final ode.model.meta.Session queriedSession = sessionProvider.findSessionById(id, session);
            Experimenter sessionOwnerQueried = queriedSession.getSudoer();
            if (sessionOwnerQueried == null) {
                sessionOwnerQueried = queriedSession.getOwner();
            }
            /* can read sessions for which "real" owner matches */
            if (sessionOwnerCurrent.getId().equals(sessionOwnerQueried.getId())) {
                return true;
            }
            /* only a full administrator may read all sessions */
            return ec.getCurrentAdminPrivileges().contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_READ_SESSION));
        }

        if (d == null || sysTypes.isSystemType(klass)) {
            // Here we're returning true because there
            // will be no group value that we can use
            // to store any permissions and don't want
            // WARNS in the log.

            return true; // EARLY EXIT!
        }

        boolean rv = false;
        if (sysTypes.isInSystemGroup(d) ||
                sysTypes.isInUserGroup(d)) {
            rv = true;
        }
        else {
            rv = securityFilter.passesFilter(session, d, ec);
        }

        // Misusing this location to store the loaded objects perms for later.
        if (ec.getCurrentGroupId() < 0) {
            // For every object that gets loaded when ode.group = -1, we
            // cache its permissions in the session context so that when the
            // session is over we can re-apply all the permissions.
            ExperimenterGroup g = d.getGroup();
            if (g == null) {
                log.warn(String.format("Group null while loading %s:%s",
                        klass.getName(), id));
            }
            if (g != null) { // Null for system types
                Long gid = g.getId();
                Permissions p = g.getDetails().getPermissions();
                if (p == null) {
                    log.warn(String.format("Permissions null for group %s " +
                            "while loading %s:%s", gid, klass.getName(), id));
                } else {
                    ec.setPermissionsForGroup(gid, p);
                }
            }
        }

        return rv;
    }

    public void throwLoadViolation(IObject iObject) throws SecurityViolation {
        Assert.notNull(iObject);
        throw new SecurityViolation("Cannot read " + iObject);
    }

    public boolean allowCreation(IObject iObject) {
        Assert.notNull(iObject);

        if (isReadOnlyDb) {
            return false;
        }

        Class<?> cls = iObject.getClass();

        boolean sysType = sysTypes.isSystemType(cls);

        // Note: removed restriction from #1769 that admins can only
        // create objects belonging to the current user. Instead,
        // OdeInterceptor checks whether or not objects are only
        // LINKED to one's own objects which is the actual intent.

        final EventContext ec = currentUser.getCurrentEventContext();

        if (tokenHolder.hasPrivilegedToken(iObject)) {
            return true;
        } else if (!sysType) {
            if (ec.getCurrentUserId() == roles.getGuestId()) {
                return false;
            } else if (iObject instanceof OriginalFile) {
                final OriginalFile file = (OriginalFile) iObject;
                if (file.getRepo() != null && !file.getName().startsWith(fileRepoSecretKey)) {
                    /* Cannot yet set OriginalFile.repo except via secret key stored in database.
                     * TODO: Need to first work through implications before permitting this. */
                    return false;
                }
            }
            /* also checked by OdeInterceptor.newTransientDetails */
            return true;
        } else if (ec.isCurrentUserAdmin()) {
            final Set<AdminPrivilege> privileges = ec.getCurrentAdminPrivileges();
            if (iObject instanceof Experimenter) {
                return privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_MODIFY_USER));
            } else if (iObject instanceof ExperimenterGroup) {
                return privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_MODIFY_GROUP));
            } else if (iObject instanceof GroupExperimenterMap) {
                return privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_MODIFY_GROUP_MEMBERSHIP));
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public void throwCreationViolation(IObject iObject)
            throws SecurityViolation {
        Assert.notNull(iObject);

        boolean sysType = sysTypes.isSystemType(iObject.getClass());

        if (sysType) {
            throw new SecurityViolation(iObject + " is a System-type, and may be created only through privileged APIs.");
        } else if (iObject instanceof OriginalFile && ((OriginalFile) iObject).getRepo() != null) {
            /* Cannot yet set OriginalFile.repo except via secret key stored in database. */
            throw new SecurityViolation("cannot set repo property of " + iObject + " via ORM");
        } else if (currentUser.isGraphCritical(iObject.getDetails())) { //
            throw new GroupSecurityViolation(iObject + "-insertion violates " +
                    "group-security.");
        } else {
            throw new SecurityViolation("not permitted to create " + iObject);
        }
    }

    public boolean allowAnnotate(IObject iObject, Details trustedDetails) {
        BasicEventContext c = currentUser.current();
        return 1 == allowUpdateOrDelete(c, iObject, trustedDetails, Scope.ANNOTATE);
    }

    public boolean allowUpdate(IObject iObject, Details trustedDetails) {
        BasicEventContext c = currentUser.current();
        return 1 == allowUpdateOrDelete(c, iObject, trustedDetails, Scope.EDIT);
    }

    public void throwUpdateViolation(IObject iObject) throws SecurityViolation {
        Assert.notNull(iObject);

        boolean sysType = sysTypes.isSystemType(iObject.getClass());

        if (!sysType && currentUser.isGraphCritical(iObject.getDetails())) { //
            throw new GroupSecurityViolation(iObject +"-modification violates " +
                    "group-security.");
        }

        throw new SecurityViolation("Updating " + iObject + " not allowed.");
    }

    public boolean allowDelete(IObject iObject, Details trustedDetails) {
        BasicEventContext c = currentUser.current();
        return 1 == allowUpdateOrDelete(c, iObject, trustedDetails, Scope.DELETE);
    }

    public void throwDeleteViolation(IObject iObject) throws SecurityViolation {
        Assert.notNull(iObject);
        throw new SecurityViolation("Deleting " + iObject + " not allowed.");
    }

    boolean owner(Long o, EventContext c) {
        return (o != null && o.equals(c.getCurrentUserId()));
    }

    boolean owner(Details d, EventContext c) {
        Long o = d.getOwner() == null ? null : d.getOwner().getId();
        return (o != null && o.equals(c.getCurrentUserId()));
    }

    boolean member(Long g, EventContext c) {
        return (g != null && c.getMemberOfGroupsList().contains(g));
    }

    boolean member(Details d, EventContext c) {
        Long g = d.getGroup() == null ? null : d.getGroup().getId();
        return member(g, c);
    }

    boolean leader(Long g, EventContext c) {
        return (g != null && c.getLeaderOfGroupsList().contains(g));
    }

    boolean leader(Details d, EventContext c) {
        Long g = d.getGroup() == null ? null : d.getGroup().getId();
        return leader(g, c);
    }

    /**
     * Determines whether or not the {@link Right} is available on this object
     * based on the ownership, group-membership, and group-permissions.
     *
     * Note: group leaders are automatically granted all rights.
     *
     * @param iObject
     * @param trustedDetails
     * @param update
     * @param right
     * @return an int with the bit turned on for each {@link Scope} element
     *     which should be allowed.
     */
    private int allowUpdateOrDelete(BasicEventContext c, IObject iObject,
        Details trustedDetails, Scope...scopes) {

        int rv = 0;

        if (iObject == null) {
            throw new IllegalArgumentException("null object");
        }

        if (isReadOnlyDb) {
            return rv;
        }

        // Do not take the details directly from iObject
        // as it is in a critical state. Values such as
        // Permissions, owner, and group may have been changed.
        final Details d = trustedDetails;

        // this can now only happen if a table doesn't have permissions
        // and there aren't any of those. so let it be updated.
        if (d == null) {
            throw new InternalException("trustedDetails are null!");
        }

        final boolean sysType = sysTypes.isSystemType(iObject.getClass());
        final boolean sysTypeOrUsrGroup = sysType ||
            sysTypes.isInUserGroup(d);

        // needs no details info
        if (tokenHolder.hasPrivilegedToken(iObject)) {
            return 1; // allow move to "user
        } else if (!sysTypeOrUsrGroup && currentUser.isGraphCritical(d)) { //
            Boolean belongs = null;
            final Long uid = c.getCurrentUserId();
            for (int i = 0; i < scopes.length; i++) {
                if (scopes[i].equals(Scope.LINK) || scopes[i].equals(Scope.ANNOTATE)) {
                    if (belongs == null) {
                        belongs = objectBelongsToUser(iObject, uid);
                    }
                    // Cancel processing of this scope. rv is already 0
                    if (!belongs) {
                        scopes[i] = null;
                    }
                }
            }
            // Don't return. Need further processing for delete.
        }

        final Set<AdminPrivilege> privileges = c.getCurrentAdminPrivileges();
        if (LightAdminPrivileges.getAllPrivileges().equals(privileges)) {
            for (int i = 0; i < scopes.length; i++) {
                if (scopes[i] != null) {
                    rv |= (1<<i);
                }
            }
            return rv; // EARLY EXIT!
        }

        final boolean isDir = iObject instanceof OriginalFile &&
                "Directory".equals(((OriginalFile) iObject).getMimetype());

        Permissions grpPermissions = null;
        if (d.getGroup() != null) {
            /* got a group set so review its permissions */
            final Long gid = d.getGroup().getId();
            if (!isDir && roles.getUserGroupId() == gid) {
                /* special handling for user group permissions */
                grpPermissions = new Permissions(Permissions.PRIVATE);
            } else {
                /* use group's permissions */
                grpPermissions = c.getPermissionsForGroup(gid);
            }
        }
        if (grpPermissions == null && roles.getUserGroupId() != c.getCurrentGroupId()) {
            /* fall back to current group permissions if not user group */
            grpPermissions = c.getCurrentGroupPermissions();
        }
        if (grpPermissions == null || grpPermissions == Permissions.DUMMY) {
            /* failing the above, fall back to no permissions */
            grpPermissions = new Permissions(Permissions.EMPTY);
        }

        final boolean owner = owner(d, c);
        final boolean leader = leader(d, c);
        final boolean member = member(d, c);

        for (int i = 0; i < scopes.length; i++) {
            Scope scope = scopes[i];
            if (scope == null) continue;

            boolean hasLightAdminPrivilege = false;
            if (!sysType) {
                if (c.getCurrentUserId() == roles.getGuestId()) {
                    return 0;
                } else if (iObject instanceof OriginalFile) {
                    final String repo = ((OriginalFile) iObject).getRepo();
                    if (repo != null) {
                        if (managedRepoUuids.contains(repo)) {
                            if (privileges.contains(adminPrivileges.getPrivilege(scope == Scope.DELETE ?
                                    AdminPrivilege.VALUE_DELETE_MANAGED_REPO : AdminPrivilege.VALUE_WRITE_MANAGED_REPO))) {
                                hasLightAdminPrivilege = true;
                            }
                        } else if (scriptRepoUuids.contains(repo)) {
                            if (privileges.contains(adminPrivileges.getPrivilege(scope == Scope.DELETE ?
                                    AdminPrivilege.VALUE_DELETE_SCRIPT_REPO : AdminPrivilege.VALUE_WRITE_SCRIPT_REPO))) {
                                hasLightAdminPrivilege = true;
                            }
                        } else {
                            /* other repository */
                            if (privileges.contains(adminPrivileges.getPrivilege(scope == Scope.DELETE ?
                                    AdminPrivilege.VALUE_DELETE_FILE : AdminPrivilege.VALUE_WRITE_FILE))) {
                                hasLightAdminPrivilege = true;
                            }
                        }
                    } else {
                        /* not in repository */
                        if (privileges.contains(adminPrivileges.getPrivilege(scope == Scope.DELETE ?
                                AdminPrivilege.VALUE_DELETE_FILE : AdminPrivilege.VALUE_WRITE_FILE))) {
                            hasLightAdminPrivilege = true;
                        }
                    }
                } else {
                    if (privileges.contains(adminPrivileges.getPrivilege(scope == Scope.DELETE ?
                            AdminPrivilege.VALUE_DELETE_OWNED : AdminPrivilege.VALUE_WRITE_OWNED))) {
                        hasLightAdminPrivilege = true;
                    }
                }
            } else if (iObject instanceof Experimenter) {
                if (privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_MODIFY_USER))) {
                    hasLightAdminPrivilege = true;
                }
            } else if (iObject instanceof ExperimenterGroup) {
                if (privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_MODIFY_GROUP))) {
                    hasLightAdminPrivilege = true;
                }
            } else if (iObject instanceof GroupExperimenterMap) {
                if (privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_MODIFY_GROUP_MEMBERSHIP))) {
                    hasLightAdminPrivilege = true;
                }
            } else if (c.isCurrentUserAdmin()) {
                hasLightAdminPrivilege = true;
            }

            final boolean isLinkageScope = scopes[i].equals(Scope.LINK) || scopes[i].equals(Scope.ANNOTATE);

            if (hasLightAdminPrivilege) {
                rv |= (1<<i);
            } else if (sysType) {
                /* system types allow only linking */
                if (isLinkageScope) {
                    rv |= (1<<i);
                }
            } else if (leader) {
                rv |= (1<<i);
            } else if (grpPermissions == null) {
                throw new InternalException(
                    "Permissions are null! Security system "
                            + "failure -- refusing to continue. The Permissions should "
                            + "be set to a default value.");
            }

            // standard
            else if (grpPermissions.isGranted(WORLD, scope.right)) {
                rv |= (1<<i);
            }

            else if (owner && grpPermissions.isGranted(USER, scope.right)) {
                // Using cuId rather than getOwner since postProcess is also
                // post-login!
                rv |= (1<<i);
            }
            // This is handled by the separation of ANNOTATE and WRITE
            else if (member && grpPermissions.isGranted(GROUP, scope.right) ) {
                rv |= (1<<i);
            }
            else if (isLinkageScope && (sysTypes.isInSystemGroup(d) || sysTypes.isInUserGroup(d) && !isDir)) {
                // Can always link to objects in system or user group except for user-group directories.
                rv |= (1<<i);
            }
        }
        return rv; // default was off, i.e. false

    }

    @Override
    public Set<String> restrictions(IObject object) {
        return policyService.listActiveRestrictions(object);
    }

    @Override
    public void setPermittedClasses(Map<Integer, Set<Class<? extends IObject>>> objectClassesPermitted) {
        final Set<Class<? extends IObject>> chgrpPermittedClasses = objectClassesPermitted.get(Permissions.CHGRPRESTRICTION);
        final Set<Class<? extends IObject>> chownPermittedClasses = objectClassesPermitted.get(Permissions.CHOWNRESTRICTION);
        if (CollectionUtils.isNotEmpty(chgrpPermittedClasses)) {
            this.chgrpPermittedClasses = chgrpPermittedClasses;
        }
        if (CollectionUtils.isNotEmpty(chownPermittedClasses)) {
            this.chownPermittedClasses = chownPermittedClasses;
        }
    }

    /**
     * On the given permissions integer sets the {@code CHGRP}, {@code CHOWN} restriction bits if the current user may
     * move or give an object of the given class and with the given details.
     * @param objectClass a model object's class
     * @param details a model object's details
     * @param allow a permissions integer
     * @return the permissions integer, possibly adjusted
     */
    private int addChgrpChownRestrictionBits(Class<? extends IObject> objectClass, Details details, int allow) {
        if (isReadOnlyDb) {
            return allow;
        }
        final EventContext ec = currentUser.getCurrentEventContext();
        final Set<AdminPrivilege> privileges = ec.getCurrentAdminPrivileges();
        final boolean isChgrpPrivilege = privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_CHGRP));
        final boolean isChownPrivilege = privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_CHOWN));
        final int chgrpBit = 1 << Permissions.CHGRPRESTRICTION;
        final int chownBit = 1 << Permissions.CHOWNRESTRICTION;
        if (isChgrpPrivilege || details.getOwner() != null && ec.getCurrentUserId().equals(details.getOwner().getId())) {
            allow |= chgrpBit;
        }
        if (isChownPrivilege || details.getGroup() != null && ec.getLeaderOfGroupsList().contains(details.getGroup().getId())) {
            allow |= chownBit;
        }
        if ((allow & chgrpBit) > 0 && !chgrpPermittedClasses.isEmpty()) {
            boolean isPermitted = false;
            for (final Class<? extends IObject> permittedClass : chgrpPermittedClasses) {
                if (permittedClass.isAssignableFrom(objectClass)) {
                    isPermitted = true;
                    break;
                }
            }
            if (!isPermitted) {
                allow &= ~chgrpBit;
            }
        }
        if ((allow & chownBit) > 0 && !chownPermittedClasses.isEmpty()) {
            boolean isPermitted = false;
            for (final Class<? extends IObject> permittedClass : chownPermittedClasses) {
                if (permittedClass.isAssignableFrom(objectClass)) {
                    isPermitted = true;
                    break;
                }
            }
            if (!isPermitted) {
                allow &= ~chownBit;
            }
        }
        return allow;
    }

    public void postProcess(IObject object) {
        if (object.isLoaded()) {
            if (object instanceof PermDetails) {
                object = ((PermDetails) object).getInternalContext();
                if (!object.isLoaded()) {
                    return; // EARLY EXIT
                }
            }
            Details details = object.getDetails();
            // Sets context values.
            this.currentUser.applyContext(details,
                    !(object instanceof ExperimenterGroup));

            final BasicEventContext c = currentUser.current();
            final Permissions p = details.getPermissions();
            int allow = allowUpdateOrDelete(c, object, details,
                // This order must match the ordered of restrictions[]
                // expected by p.copyRestrictions
                Scope.LINK, Scope.EDIT, Scope.DELETE, Scope.ANNOTATE);
            allow = addChgrpChownRestrictionBits(object.getClass(), details, allow);

            // #9635 - This is not the most efficient solution
            // But since it's unclear why Permission objects
            // are currently being shared, the safest solution
            // is to always produce a copy.
            Permissions copy = new Permissions(p);
            copy.copyRestrictions(allow, restrictions(object));
            details.setPermissions(copy); // #9635
        }
    }

    /**
     * Check if the given object is owned by the given user.
     * @param iObject a model object
     * @param uid the ID of a user
     * @return if the object is owned by the user, or is not yet persisted
     */
    // TODO this is less problematic than linking
    private boolean objectBelongsToUser(IObject iObject, Long uid) {
        final Experimenter e = iObject.getDetails().getOwner();
        if (e == null) {
            if (iObject.getId() == null) {
                // if this object does not yet have an ID
                // then we'll assume it's a newly created instance
                // which will eventually be saved with owner==uid
                return true;
            }

            throw new NullPointerException("Null owner for " + iObject);
        }
        Long oid = e.getId();
        return uid.equals(oid); // Only allow own objects!
    }

}