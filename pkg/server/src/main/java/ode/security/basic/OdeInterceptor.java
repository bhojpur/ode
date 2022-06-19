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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.EntityMode;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.collection.PersistentList;
import org.hibernate.engine.CollectionEntry;
import org.hibernate.engine.PersistenceContext;
import org.hibernate.type.ComponentType;
import org.hibernate.type.Type;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import com.google.common.base.Splitter;

import ode.conditions.ApiUsageException;
import ode.conditions.GroupSecurityViolation;
import ode.conditions.InternalException;
import ode.conditions.OptimisticLockException;
import ode.conditions.PermissionMismatchGroupSecurityViolation;
import ode.conditions.ReadOnlyGroupSecurityViolation;
import ode.conditions.SecurityViolation;
import ode.conditions.ValidationException;
import ode.model.IAnnotationLink;
import ode.model.IMutable;
import ode.model.IObject;
import ode.model.core.Image;
import ode.model.core.OriginalFile;
import ode.model.core.Pixels;
import ode.model.display.RenderingDef;
import ode.model.display.Thumbnail;
import ode.model.enums.AdminPrivilege;
import ode.model.internal.Details;
import ode.model.internal.NamedValue;
import ode.model.internal.Permissions;
import ode.model.internal.Permissions.Right;
import ode.model.internal.Permissions.Role;
import ode.model.meta.Experimenter;
import ode.model.meta.ExperimenterGroup;
import ode.model.meta.ExternalInfo;
import ode.model.meta.GroupExperimenterMap;
import ode.model.roi.Roi;
import ode.security.SecuritySystem;
import ode.security.SystemTypes;
import ode.services.sessions.stats.SessionStats;
import ode.system.EventContext;
import ode.system.Roles;
import ode.tools.hibernate.ExtendedMetadata;
import ode.tools.hibernate.HibernateUtils;
import ode.tools.hibernate.SqlQueryTransformer;
import ode.tools.lsid.LsidUtils;
import ode.util.SqlAction;

/**
 * implements {@link org.hibernate.Interceptor} for controlling various aspects
 * of the Hibernate runtime. Where no special requirements exist, methods
 * delegate to {@link EmptyInterceptor}
 *
 * Current responsibilities include the proper (re-)setting of {@link Details}
 */
public class OdeInterceptor extends SqlQueryInterceptor {

    static volatile String last = null;

    static volatile int count = 1;

    private static Logger log = LoggerFactory.getLogger(OdeInterceptor.class);

    /* array indices for OriginalFile "repo", "path" and "name" properties */
    private static final String IDX_FILE_REPO = LsidUtils.parseField(OriginalFile.REPO);
    private static final String IDX_FILE_PATH = LsidUtils.parseField(OriginalFile.PATH);
    private static final String IDX_FILE_NAME = LsidUtils.parseField(OriginalFile.NAME);

    private final SystemTypes sysTypes;

    private final CurrentDetails currentUser;

    private final TokenHolder tokenHolder;

    private final ExtendedMetadata em;

    private final SessionStats stats;

    private final Roles roles;

    private final LightAdminPrivileges adminPrivileges;

    private final SqlAction sqlAction;

    /* thread-safe */
    private final Set<String> managedRepoUuids, scriptRepoUuids;

    public OdeInterceptor(Roles roles, SystemTypes sysTypes, ExtendedMetadata em,
            CurrentDetails cd, TokenHolder tokenHolder, SessionStats stats,
            LightAdminPrivileges adminPrivileges, SqlAction sqlAction, SqlQueryTransformer sqlQueryTransformer,
            Set<String> managedRepoUuids, Set<String> scriptRepoUuids) {
        super(sqlQueryTransformer);
        Assert.notNull(tokenHolder);
        Assert.notNull(sysTypes);
        // Assert.notNull(em); Permitting null for testing
        // Assert.notNull(cd); Permitting null for testing
        Assert.notNull(stats);
        Assert.notNull(roles);
        this.tokenHolder = tokenHolder;
        this.currentUser = cd;
        this.sysTypes = sysTypes;
        this.stats = stats;
        this.roles = roles;
        this.em = em;
        this.adminPrivileges = adminPrivileges;
        this.sqlAction = sqlAction;
        this.managedRepoUuids = managedRepoUuids;
        this.scriptRepoUuids = scriptRepoUuids;
    }

    /**
     * default logic, but we may want to use them eventually for
     * dependency-injection.
     */
    public Object instantiate(String entityName, EntityMode entityMode,
            Serializable id) throws CallbackException {

        debug("Intercepted instantiate.");
        return super.instantiate(entityName, entityMode, id);

    }

    /** default logic. */
    public boolean onLoad(Object entity, Serializable id, Object[] state,
            String[] propertyNames, Type[] types) throws CallbackException {

        debug("Intercepted load.");
        this.stats.loadedObjects(1);
        return super.onLoad(entity, id, state, propertyNames, types);

    }

    /** default logic */
    public int[] findDirty(Object entity, Serializable id,
            Object[] currentState, Object[] previousState,
            String[] propertyNames, Type[] types) {
        debug("Intercepted dirty check.");
        return super.findDirty(entity, id, currentState, previousState,
                propertyNames, types);
    }

    /**
     * calls back to {@link BasicSecuritySystem#newTransientDetails(IObject)} for
     * properly setting {@link IObject#getDetails() Details}
     */
    public boolean onSave(Object entity, Serializable id, Object[] state,
            String[] propertyNames, Type[] types) {
        debug("Intercepted save.");
        this.stats.updatedObjects(1);
        if (entity instanceof IObject) {
            IObject iobj = (IObject) entity;
            int idx = HibernateUtils.detailsIndex(propertyNames);

            Details d = evaluateLinkages(iobj);

            // Get a new details based on the current context
            d = newTransientDetails(iobj, d);
            state[idx] = d;
        }

        return true; // transferDetails ALWAYS edits the new entity.
    }

    /**
     * Do not allow <q>.</q> and <q>..</q> components in file paths.
     * @param filepath a file path
     * @return if the file path contains any prohibited components
     */
    private static boolean isProblemFilepath(String filepath) {
        for (final String component : Splitter.on('/').split(filepath)) {
            switch (component) {
            case ".":
            case "..":
                return true;
            }
        }
        return false;
    }

    /**
     * calls back to
     * {@link BasicSecuritySystem#checkManagedDetails(IObject, Details)} for
     * properly setting {@link IObject#getDetails() Details}.
     */
    public boolean onFlushDirty(Object entity, Serializable id,
            Object[] currentState, Object[] previousState,
            String[] propertyNames, Type[] types) {
        debug("Intercepted update.");
        this.stats.updatedObjects(1);
        boolean altered = false;
        if (entity instanceof IObject) {
            IObject iobj = (IObject) entity;
            int idx = HibernateUtils.detailsIndex(propertyNames);

            Details newDetails = evaluateLinkages(iobj);

            if (previousState != null && iobj instanceof OriginalFile && !currentUser.current().isCurrentUserAdmin()) {
                final int pathIndex = HibernateUtils.index(IDX_FILE_PATH, propertyNames);
                final int nameIndex = HibernateUtils.index(IDX_FILE_NAME, propertyNames);
                final String currentPath = (String) currentState[pathIndex];
                final String currentName = (String) currentState[nameIndex];
                if (currentPath != null && currentName != null &&
                        !(currentPath.equals(previousState[pathIndex]) && currentName.equals(previousState[nameIndex])) &&
                        isProblemFilepath(currentPath + currentName)) {
                    throw new SecurityViolation("only administrators may introduce non-canonical OriginalFile path or name");
                }
            }

            altered |= resetDetails(iobj, currentState, previousState, idx,
                    newDetails);

        }
        /* Cannot yet change OriginalFile.repo except via SQL.
         * TODO: Need to first work through implications before permitting this. */
        if (entity instanceof OriginalFile) {
            final int repoIndex = HibernateUtils.index(IDX_FILE_REPO, propertyNames);
            if (previousState != null && !ObjectUtils.equals(previousState[repoIndex], currentState[repoIndex])) {
                log.warn("reverting change to OriginalFile.repo");
                currentState[repoIndex] = previousState[repoIndex];
            }
        }
        return altered;
    }

    /** default logic */
    public void onDelete(Object entity, Serializable id, Object[] state,
            String[] propertyNames, Type[] types) throws CallbackException {
        debug("Intercepted delete.");
        super.onDelete(entity, id, state, propertyNames, types);
    }

    // ~ Collections (of interest)
    // =========================================================================
    public void onCollectionRecreate(Object collection, Serializable key)
            throws CallbackException {
        debug("Intercepted collection recreate.");
    }

    public void onCollectionRemove(Object collection, Serializable key)
            throws CallbackException {
        debug("Intercepted collection remove.");
    }

    public void onCollectionUpdate(Object collection, Serializable key)
            throws CallbackException {
        debug("Intercepted collection update.");
        if (collection instanceof PersistentList) {
            PersistentList list = (PersistentList) collection;
            PersistenceContext context = list.getSession().getPersistenceContext();
            CollectionEntry entry = context.getCollectionEntry(list);

            if (!(entry.getCurrentPersister().getElementType()
                    instanceof ComponentType)) {
                // We assume that any modification of any
                // CollectionOfElements like NamedValue-lists
                // should be subject to the security of the
                // parent. If this *isn't* such a collection,
                // then exit.
                return;
            }

            List snapshot = (List) entry.getSnapshot();
            Object owner = list.getOwner();

            if (list.size() == 0 && snapshot.size() == 0) {
                // Nothing here, so we don't care
                return;
            }

            boolean equals = true;
            if (list.size() == snapshot.size()) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) == null) {
                        if (snapshot.get(i) == null) {
                            continue;
                        }
                    } else { // first element is not null
                        Object lhs = list.get(i);
                        if (lhs instanceof NamedValue) {
                            if (((NamedValue) lhs).equals(snapshot.get(i))) {
                                continue;
                            }
                        }
                    }
                    // If we reach this point, there's a non-match and the
                    // bumping of the version number should proceed.
                    equals = false;
                    break;
                }
                // The two lists were found to be equal, do not bump the
                // version number;
                if (equals) {
                    return;
                }
            }

            // https://hibernate.atlassian.net/browse/HHH-4897 workaround:
            // ----------------------------------------------------------
            // Assuming we get here, we bump the version number for the
            // object which will hopefully cause the regular security
            // checks to fail.
            try {
                IObject iobj = (IObject) owner;
                Method getter = iobj.getClass().getMethod("getVersion");
                Integer oldVersion = (Integer) getter.invoke(iobj);
                Integer newVersion = oldVersion == null ? 1 : oldVersion + 1;
                Method setter = iobj.getClass().getMethod("setVersion", Integer.class);
                setter.invoke(iobj, newVersion);
                log.info("Updating version for collections from {} to {}",
                        oldVersion, newVersion);
            } catch (Exception e) {
                InternalException ie = new InternalException("Failed to set version");
                ie.initCause(e);
                throw ie;
            }
        }
    }

    // ~ Flush (currently unclear semantics)
    // =========================================================================
    public void preFlush(Iterator entities) throws CallbackException {
        debug("Intercepted preFlush.");
        super.preFlush(entities);
    }

    public void postFlush(Iterator entities) throws CallbackException {
        debug("Intercepted postFlush.");

        if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
            debug("detected read-only transaction");
        } else if (sqlAction != null) {
            /* read-write transactions may trigger checks */
            debug("updating current light administrator privileges");
            final Set<AdminPrivilege> privileges = currentUser.current().getCurrentAdminPrivileges();
            sqlAction.deleteCurrentAdminPrivileges();
            if (CollectionUtils.isNotEmpty(privileges)) {
                sqlAction.insertCurrentAdminPrivileges(privileges);
            }
        }
    }

    // ~ Serialization
    // =========================================================================

    private static final long serialVersionUID = 7616611615023614920L;

    private void readObject(ObjectInputStream s) throws IOException,
            ClassNotFoundException {
        s.defaultReadObject();
    }

    // ~ Unused interface methods
    // =========================================================================

    public void afterTransactionBegin(Transaction tx) {
    }

    public void afterTransactionCompletion(Transaction tx) {
    }

    public void beforeTransactionCompletion(Transaction tx) {
    }

    public Object getEntity(String entityName, Serializable id)
            throws CallbackException {
        return super.getEntity(entityName, id);
    }

    public String getEntityName(Object object) throws CallbackException {
        return super.getEntityName(object);
    }

    public Boolean isTransient(Object entity) {
        return super.isTransient(entity);
    }

    public String onPrepareStatement(String sql) {
        // start
        sql = super.onPrepareStatement(sql);
        if (!log.isDebugEnabled()) {
            return sql;
        }

        // from
        StringBuilder sb = new StringBuilder();
        String[] first = sql.split("\\sfrom\\s");
        sb.append(first[0]);

        for (int i = 1; i < first.length; i++) {
            sb.append("\n from ");
            sb.append(first[i]);
        }

        // where
        String[] second = sb.toString().split("\\swhere\\s");
        sb = new StringBuilder();
        sb.append(second[0]);

        for (int j = 1; j < second.length; j++) {
            sb.append("\n where ");
            sb.append(second[j]);
        }

        return sb.toString();

    }

    // ~ Helpers
    // =========================================================================

    /**
     * asks {@link BasicSecuritySystem} to create a new managed {@link Details}
     * based on the previous state of this entity. If the previous state is null
     * then throw an exception.
     *
     * @param entity
     *            IObject to be updated
     * @param currentState
     *            the possibly changed field data for this entity
     * @param previousState
     *            the field data as seen in the db
     * @param idx
     *            the index of Details in the state arrays.
     */
    protected boolean resetDetails(IObject entity, Object[] currentState,
            Object[] previousState, int idx, Details newDetails) {

        if (previousState == null) {
            log.warn(String.format("Null previousState for %s(loaded=%s). Details=%s",
                entity, entity.isLoaded(), currentState[idx]));
            throw new InternalException("Previous state is null. Possibly caused by evict");
        }

        final Details previous = (Details) previousState[idx];
        final Details result = checkManagedDetails(entity, previous, newDetails);

        if (previous != result) {
            currentState[idx] = result;
            return true;
        }

        return false;
    }

    protected void log(String msg) {
        if (msg.equals(last)) {
            count++;
        }

        else if (log.isDebugEnabled()) {
            String times = " ( " + count + " times )";
            log.debug(msg + times);
            last = msg;
            count = 1;
        }
    }

    private void debug(String msg) {
        if (log.isDebugEnabled()) {
            log(msg);
        }
    }

    // Methods moved from BasicSecuritySystem
    // =========================================================================

    /**
     * Checks the details of the objects which the given object links to in
     * order to guarantee that linkages are valid. In the case of a non-specific
     * UID or GID, then the Details object returned by this method can be used
     * as the basis for unknown user/group.
     *
     * This method is called during
     * {@link OdeInterceptor#onSave(Object, java.io.Serializable, Object[], String[], org.hibernate.type.Type[])
     * save} and
     * {@link OdeInterceptor#onFlushDirty(Object, java.io.Serializable, Object[], Object[], String[], org.hibernate.type.Type[])
     * update} since this is the only time that new entity references can be
     * created.
     *
     * @param changedObject
     *            new or updated entity which may reference other entities which
     *            then require locking. Nulls are tolerated but do nothing.
     */
    public Details evaluateLinkages(IObject changedObject) {

        if (changedObject == null) {
            return null;
        }

        final Class<?> changedClass = changedObject.getClass();
        final Details rv = changedObject.getDetails().newInstance();

        // Valid to link to any system type or object in system group
        if (sysTypes.isSystemType(changedObject.getClass()) ||
                sysTypes.isInSystemGroup(changedObject.getDetails())) {
            return rv;
        }

        final boolean isDir = changedObject instanceof OriginalFile &&
                "Directory".equals(((OriginalFile) changedObject).getMimetype());

        final Long currentGroupId = currentUser.getGroup().getId();
        final boolean currentGroupNegative = currentGroupId < 0;
        final IObject[] candidates = em.getLockCandidates(changedObject);
        for (IObject linkedObject : candidates) {

            // If the linked object is a system type or object in the system
            // group, or further in the shared user group, then we permit
            // the linkage.
            if (sysTypes.isSystemType(linkedObject.getClass()) ||
                    sysTypes.isInSystemGroup(linkedObject.getDetails()) ||
                    sysTypes.isInUserGroup(linkedObject.getDetails()) && !isDir) {
                continue;
            }

            final Class<?> linkedClass = linkedObject.getClass();
            final Details linkedDetails = linkedObject.getDetails();
            if (linkedDetails == null) {
                // Previously, the details of the candidates
                // were never null. the addition of the reagent linkages
                // *somehow* led to NPEs here. for the moment, we're assuming
                // if null, then the object can't be mis-linked. (i.e. it's
                // probably new)
                continue;
            }

            // If this is -1 situation, then we pass back out the
            // group for the linked object. In the case of new transient
            // objects, this will be set as the new group.
            if (currentGroupNegative) {
                if (rv.getGroup() == null) {
                    // If this is the first linked object then we assume
                    // that this object should use this value.
                    rv.setGroup(linkedDetails.getGroup());
                } else {
                    throwIfGroupsDontMatch(rv.getGroup(), changedObject,
                            linkedDetails.getGroup(), linkedObject);
                }
            } else {
                throwIfGroupsDontMatch(currentUser.getGroup(),
                        changedObject, linkedDetails.getGroup(),
                        linkedObject);
            }

            // Rather than as in <=4.1 in which objects were scheduled
            // for locking which prevented later actions, now we check
            // whether or not we're graph critical and if so, and if
            // the objects do not belong to the current user, then we abort.

            final Experimenter linkedOwner = linkedObject.getDetails().getOwner();
            final ExperimenterGroup linkedGroup = linkedObject.getDetails().getGroup();
            if (linkedOwner == null || linkedGroup == null) {
                continue; // Only for system types which should be filtered
            }

            final Long linkedUid = linkedOwner.getId();
            final Long linkedGid = linkedGroup.getId();
            if (linkedUid == null || linkedGid == null) {
                continue; // Highly unlikely.
            }

            final EventContext ec = currentUser.getCurrentEventContext();
            final boolean isOwner = ec.getCurrentUserId().equals(linkedUid);
            final boolean isOwnerOrSupervisor = currentUser.isOwnerOrSupervisor(linkedObject);
            final boolean isSupervisor = (!isOwner) && isOwnerOrSupervisor;
            final boolean isMember = ec.getMemberOfGroupsList().contains(linkedGid);
            final Permissions p = currentUser.getCurrentEventContext()
                .getCurrentGroupPermissions();

            if (!isOwner && currentUser.isGraphCritical(rv)) {
                String gname = currentUser.getGroup().getName();
                String oname = currentUser.getOwner().getOdeName();

                Long changedUid = null;
                if (changedObject.getDetails().getOwner() != null) {
                    changedUid = changedObject.getDetails().getOwner().getId();
                }

                // allow admin users to specify the owner
                // for such a graph critical situation since if the new
                // object also belongs to the user of the linked obj, then
                // visibility will be guaranteed.
                if (changedUid == null || !changedUid.equals(linkedUid)) {
                    throw new ReadOnlyGroupSecurityViolation(String.format(
                    "Cannot link to %s\n" +
                    "Current user (%s) is an admin or the owner of\n" +
                    "the private group (%s=%s). It is not allowed to\n" +
                    "link to users' data.", linkedObject, oname, gname, p));
                }
            }

            final Right neededRight = neededRight(changedClass, linkedClass);
            final Role neededRole = neededRole(isOwner, isMember);

            if (!isSupervisor) {
                throwIfNotGranted(p, neededRole, neededRight, linkedObject);
            }
        }
        return rv;
    }

    private Role neededRole(boolean isOwner, boolean isMember) {
        if (isOwner) {
            return Role.USER;
        } else if (isMember) {
            return Role.GROUP;
        } else {
            return Role.WORLD;
        }
    }

    /**
     * The default right need for a linkage is {@link Right#WRITE}.
     * If however, this is only an annotation or only a viewing,
     * then less permission is needed.
     * @param changedClass the changed class
     * @param linkedClass the linked class
     * @return the right that is needed
     */
    protected Right neededRight(final Class<?> changedClass,
            final Class<?> linkedClass) {

        Right neededRight = Right.WRITE;
        if (RenderingDef.class.isAssignableFrom(linkedClass) ||
            RenderingDef.class.isAssignableFrom(changedClass) ||
            (Pixels.class.isAssignableFrom(linkedClass) &&
                 Thumbnail.class.isAssignableFrom(changedClass))) {
            neededRight = Right.READ;
        } else if (IAnnotationLink.class.isAssignableFrom(changedClass) ||
                (Roi.class.isAssignableFrom(changedClass) &&
                        Image.class.isAssignableFrom(linkedClass))) {
            neededRight = Right.ANNOTATE;
        }
        return neededRight;
    }

    // TODO is this natural? perhaps permissions don't belong in details
    // details are the only thing that users can change the rest is
    // read only...
    /**
     * @see SecuritySystem#newTransientDetails(IObject)
     */
    public Details newTransientDetails(IObject obj) {

        if (obj == null) {
            throw new ApiUsageException("Argument cannot be null.");
        }
        final Details newDetails = obj.getDetails().newInstance();
        return newTransientDetails(obj, newDetails);
    }

    /**
     * Like {@link #newTransientDetails(IObject)} but allows passing in a
     * newDetails object with possibly preset values.
     * @see #evaluateLinkages(IObject)
     */
    protected Details newTransientDetails(final IObject obj,
            final Details newDetails) {

        if (tokenHolder.hasPrivilegedToken(obj)) {
            return obj.getDetails(); // EARLY EXIT
        }

        final Details source = obj.getDetails();
        final BasicEventContext bec = currentUser.current();

        // Allow values to be passed in.
        newDetails.copyWhereUnset(null, currentUser.createDetails());

        // Light administrator privileges
        final boolean isPrivilegedCreator;
        final boolean sysType = sysTypes.isSystemType(obj.getClass());
        final Set<AdminPrivilege> privileges = bec.getCurrentAdminPrivileges();

        if (!bec.isCurrentUserAdmin()) {
            isPrivilegedCreator = false;
        } else if (sysType) {
            isPrivilegedCreator = true;
        } else if (obj instanceof Experimenter) {
            isPrivilegedCreator = privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_MODIFY_USER));
        } else if (obj instanceof ExperimenterGroup) {
            isPrivilegedCreator = privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_MODIFY_GROUP));
        } else if (obj instanceof GroupExperimenterMap) {
            isPrivilegedCreator = privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_MODIFY_GROUP_MEMBERSHIP));
        } else if (obj instanceof OriginalFile) {
            final String repo = ((OriginalFile) obj).getRepo();
            if (repo != null) {
                if (managedRepoUuids.contains(repo)) {
                    isPrivilegedCreator =
                            privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_WRITE_MANAGED_REPO));
                } else if (scriptRepoUuids.contains(repo)) {
                    isPrivilegedCreator =
                            privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_WRITE_SCRIPT_REPO));
                } else {
                    /* other repository */
                    isPrivilegedCreator = privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_WRITE_FILE));
                }
            } else {
                /* not in repository */
                isPrivilegedCreator = privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_WRITE_FILE));
            }
        } else {
            isPrivilegedCreator = privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_WRITE_OWNED));
        }

        // OWNER
        // users *aren't* allowed to set the owner of an item.
        if (source.getOwner() != null
                && !newDetails.getOwner().getId().equals(
                        source.getOwner().getId())) {
            if (isPrivilegedCreator) {
                // but this is an administrator
                newDetails.setOwner(source.getOwner());
            } else {
                throw new SecurityViolation(String.format(
                        "You are not authorized to set the Experimenter"
                                + " for %s to %s", obj, source.getOwner()));
            }

        }

        // GROUP
        // users are only allowed to set to the current group
        // if, however, the current group is -1 (all groups)
        // and the user is a member of that group or an admin,
        // then permit the setting with the assumption that the
        // later link check will catch any inappropriate linking.
        if (source.getGroup() != null && source.getGroup().getId() != null) {

            final long sourceGroupId = source.getGroup().getId();

            if (bec.getCurrentGroupId().equals(sourceGroupId)) {
                newDetails.setGroup(source.getGroup());
            }

            else if (isPrivilegedCreator &&
                    Long.valueOf(roles.getUserGroupId())
                    .equals(source.getGroup().getId())) {
                newDetails.setGroup(source.getGroup());
            }

            else if ((bec.getCurrentGroupId() < 0) &&
                    (isPrivilegedCreator || bec.getMemberOfGroupsList()
                        .contains(sourceGroupId))) {
                newDetails.setGroup(source.getGroup());
            }

            // oops. boom!
            else {
                throw new SecurityViolation(String.format(
                        "You are not authorized to set the ExperimenterGroup"
                                + " for %s to %s", obj, source.getGroup()));
            }
        } else if (isPrivilegedCreator || bec.getMemberOfGroupsList().contains(newDetails.getGroup().getId())) {
            // admin or group member so okay
        } else if (bec.getCurrentGroupPermissions().isGranted(Role.WORLD,
                obj instanceof IAnnotationLink ? Right.ANNOTATE : Right.WRITE)) {
            // group allows non-members to write
        } else if ("ode.model.display".equals(obj.getClass().getPackage().getName()) &&
                bec.getCurrentGroupPermissions().isGranted(Role.WORLD, Right.READ)) {
            // group allows non-members to read so allow creation of "display" objects
        } else {
            /* TODO: may need to loosen further for rwrwra groups */
            throw new SecurityViolation(String.format("You are not authorized to create %s", obj));
        }

        // before 4.2, users were allowed to manually set the permissions
        // on an object, and even set a umask to be applied. for the initial
        // 4.2 version, however, we are disallowing manually setting
        // permissions so that all objects will match group permissions.
        // Doing this after the setting of newDetails.group in case the
        // user is logged into user or system.
        if (source.getPermissions() != null) {

            Permissions groupPerms = currentUser.getCurrentEventContext()
                .getCurrentGroupPermissions();

            boolean isInUsrGrp = sysTypes.isInUserGroup(newDetails);
            if (groupPerms.identical(source.getPermissions())) {
                // ok. weird that they're set. probably an instance
                // of a managed object being passed in as with
            } else if (!sysTypes.isSystemType(obj.getClass())) {
                if (isInUsrGrp) {
                    // similarly, allow whatever in user group for the moment.
                } else {
                    throw new PermissionMismatchGroupSecurityViolation(
                    "Manually setting permissions currently disallowed");
                }
            }
            // Above didn't throw, so set permissions.
            newDetails.setPermissions(source.getPermissions());
        }

        // EXTERNALINFO
        // users _are_ allowed to set the external info on a new object.
        // subsequent operations, however, will not be able to edit this
        // value.
        newDetails.setExternalInfo(source.getExternalInfo());

        // CREATION/UPDATEVENT : currently ignore what users do

        return newDetails;

    }

    /**
     * @see SecuritySystem#checkManagedDetails(IObject, Details)
     */
    public Details checkManagedDetails(final IObject iobj,
            final Details previousDetails) {

        if (iobj == null) {
            throw new ApiUsageException("Argument cannot be null.");
        }

        return checkManagedDetails(iobj, previousDetails,
                iobj.getDetails().newInstance());
    }

    /**
     * Like {@link #checkManagedDetails(IObject, Details, Details)} but allows
     * passing in a specific {@link Details} instance.
     * @see SecuritySystem#checkManagedDetails(IObject, Details)
     * @see #evaluateLinkages(IObject)
     */
    protected Details checkManagedDetails(final IObject iobj,
            final Details previousDetails, /* not final */Details newDetails) {

        if (iobj == null) {
            throw new ApiUsageException("Argument cannot be null.");
        }

        if (iobj.getId() == null) {
            throw new ValidationException(
                    "Id required on all detached instances.");
        }

        // Note: privileged check moved into the if statement below.

        // done first as validation.
        if (iobj instanceof IMutable) {
            Integer version = ((IMutable) iobj).getVersion();
            if (version == null || version.intValue() < 0) {
                ;
                // throw new ValidationException(
                // "Version must properly be set on managed objects :\n"+
                // obj.toString()
                // );
                // TODO
            }
        }

        // check if the newDetails variable has been reset or if the instance
        // has been changed.
        boolean altered = false;

        final Details currentDetails = iobj.getDetails();
        newDetails.copyWhereUnset(previousDetails, currentUser.createDetails());

        // This happens if all fields of details are null (which can't happen)
        // And is so uninteresting for all of our checks. The object can't be
        // locked and nothing can be edited. Just return null.
        if (previousDetails == null) {
            newDetails = null;
            altered = true;
            if (log.isDebugEnabled()) {
                log.debug("Setting details on " + iobj
                        + " to null like original");
            }
        }

        // Also uninteresting. If the users say nothing, then the originals.
        // Probably common since users don't worry about this information.
        else if (currentDetails == null) {
            newDetails = previousDetails.copy();
            altered = true;
            if (log.isDebugEnabled()) {
                log.debug("Setting details on " + iobj
                        + " to copy of original details.");
            }

            // Now we have to make sure certain things do not happen. The
            // following
            // take into account whether or not the entity is privileged (has a
            // token),
            // is locked in the database, and who the current user and group
            // are.
        } else {

            boolean privileged = false;

            if (tokenHolder.hasPrivilegedToken(iobj)) {
                privileged = true;
            }

            // Acquiring the context here to prevent multiple
            // accesses to the threadlocal
            final BasicEventContext bec = currentUser.current();

            // NOTE: here we are NOT including a check
            // for sysTypes.isInSystemGroup(), since that implies that
            // the object doesn't have owner/group
            final boolean sysType = sysTypes.isSystemType(iobj.getClass());

            // As of Bhojpur ODE, we are no longer being restrictive about external
            // info. It is now a user-concern and can be changed like other
            // fields.

            // implies that owner doesn't matter
            if (!sysType) {
                altered |= managedOwner(privileged, iobj,
                        previousDetails, currentDetails, newDetails, bec);
            }

            // implies that group doesn't matter
            if (!sysType) {
                altered |= managedGroup(privileged, iobj,
                        previousDetails, currentDetails, newDetails, bec);
            }

            // the event check needs to be last, because we need to test
            // whether or not it is necessary to change the updateEvent
            // (i.e. last modification)
            // implies that event doesn't matter
            if (!sysType) {
                altered |= managedEvent(privileged, iobj,
                        previousDetails, currentDetails, newDetails);
            }


        }

        // all permissions are ignored. We simply don't trust
        // any coming in from outside. In the case of chgrp, the value
        // will be modified in the DB directly. They've been marked as
        // immutable in the model.

        return altered ? newDetails : previousDetails;

    }

    /**
     * responsible for guaranteeing that external info is not modified by any
     * users, including root. This does not apply to the "client concern" fields
     * which can be modified after the fact.
     *
     * @param privileged if the user is privileged
     * @param obj the model object
     * @param previousDetails
     *            details representing the known DB state
     * @param currentDetails
     *            details representing the user request (UNTRUSTED)
     * @param newDetails
     *            details from the current context. Holder for the merged
     *            {@link Permissions}
     * @return true if the {@link Permissions} of newDetails are changed.
     */
    @Deprecated
    protected boolean managedExternalInfo(boolean privileged,
            IObject obj, Details previousDetails, Details currentDetails,
            Details newDetails) {

        boolean altered = false;

        ExternalInfo previous = previousDetails == null ? null
                : previousDetails.getExternalInfo();

        ExternalInfo current = currentDetails == null ? null : currentDetails
                .getExternalInfo();

        if (previous == null) {
            if (current != null) {
              newDetails.setExternalInfo(current);
              altered = true;
            }
        }

        // The ExternalInfo was previously set. We do not allow it to be
        // changed,
        // similar to not allowing the Event for an entity to be changed.
        else {
            if (!HibernateUtils.idEqual(previous, current)) {
                throw new SecurityViolation(String.format(
                        "Cannot update ExternalInfo for %s from %s to %s", obj,
                        previous, current));
            }
        }

        return altered;
    }

    protected boolean managedOwner(boolean privileged,
            IObject obj, Details previousDetails, Details currentDetails,
            Details newDetails, final BasicEventContext bec) {

        final Set<AdminPrivilege> privileges = bec.getCurrentAdminPrivileges();

        if (!HibernateUtils.idEqual(previousDetails.getOwner(), currentDetails
                .getOwner())) {

            // !idEquals implies that they aren't both null; if current_owner is
            // null, then it was *probably* not intended, so just fix it and
            // move on. this goes for root and admins as well.
            if (currentDetails.getOwner() == null) {
                newDetails.setOwner(previousDetails.getOwner());
                return true;
            }

            // if the current user is an admin or if the entity has been
            // marked privileged, then use the current owner.
            else if (bec.isCurrentUserAdmin() && privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_CHOWN))
                    || privileged) {
                // ok
            }

            // everyone else can't change them at all.
            else {
                throw new SecurityViolation(String.format(
                        "You are not authorized to change "
                                + "the owner for %s from %s to %s", obj,
                        previousDetails.getOwner(), currentDetails.getOwner()));
            }
        }

        else {

            // values are the same. ensure they are the same for
            // newDetails as well
            newDetails.setOwner(previousDetails.getOwner());
        }
        return false;
    }

    protected boolean managedGroup(boolean privileged,
            IObject obj, Details previousDetails, Details currentDetails,
            Details newDetails, final BasicEventContext bec) {

        if (null != previousDetails.getGroup()) {
            long objGroupId = previousDetails.getGroup().getId();
            long sessGroupId = currentUser.getGroup().getId();
            long userGroupId = roles.getUserGroupId();
            if (sessGroupId != objGroupId && objGroupId != userGroupId) { //
                throw new SecurityViolation(String.format(
                        "Currently logged into group %s. Cannot alter object in group %s",
                        sessGroupId, objGroupId));
            }
        }

        final Set<AdminPrivilege> privileges = bec.getCurrentAdminPrivileges();

        // previous and current have different ids. either change it and return
        // true if permitted, or throw an exception.
        if (!HibernateUtils.idEqual(previousDetails.getGroup(), currentDetails
                .getGroup())) {

            // !idEquals implies that they aren't both null; if current_group is
            // null, then it was *probably* not intended, so just fix it and
            // move on. this goes for root and admins as well.
            if (currentDetails.getGroup() == null) {
                newDetails.setGroup(previousDetails.getGroup());
                return true;
            }

            // if user is a member of the group or the current user is an admin
            // or if the entity has been marked as privileged, then use the
            // current group.
            // TODO refactor
            else if ((!currentDetails.getGroup().getId().equals(
                         roles.getUserGroupId()) &&
                       bec.getMemberOfGroupsList().contains(
                         currentDetails.getGroup().getId())) //
                    || bec.isCurrentUserAdmin() && privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_CHGRP))
                    || privileged) {
                newDetails.setGroup(currentDetails.getGroup());
                return true;
            }

            // everyone else can't change them at all.
            else {
                throw new SecurityViolation(String.format(
                        "You are not authorized to change "
                                + "the group for %s from %s to %s", obj,
                        previousDetails.getGroup(), currentDetails.getGroup()));
            }

        }

        // previous and current are the same, but we need to set
        // that value on newDetails.
        else {

            // This doesn't need to return true, because it'll only
            // be used if something else was changed.
            newDetails.setGroup(previousDetails.getGroup());

        }
        return false;
    }

    protected boolean managedEvent(boolean privileged,
            IObject obj, Details previousDetails, Details currentDetails,
            Details newDetails) {

        // TODO no longer need to keep track of alteration boolean. like
        // transient with update event, managedDetails will now ALWAYS return
        // an updated details.
        // -------------------

        boolean altered = false;

        // creation event~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        if (!HibernateUtils.idEqual(previousDetails.getCreationEvent(),
                currentDetails.getCreationEvent())) {

            // !idEquals implies that they aren't both null; if current_event is
            // null, then it was *probably* not intended, so just fix it and
            // move on. this goes for root and admins as well.
            if (currentDetails.getCreationEvent() == null) {
                newDetails.setCreationEvent(previousDetails.getCreationEvent());
                altered = true;
            }
            // otherwise throw an exception, because,
            // it can lead to confusion otherwise. See:
            else {

                // no one change them.
                throw new SecurityViolation(String.format(
                        "You are not authorized to change "
                                + "the creation event for %s from %s to %s",
                        obj, previousDetails.getCreationEvent(), currentDetails
                                .getCreationEvent()));
            }

        }

        // they are equal meaning no change was intended but in case other
        // changes took place, we have to make sure newDetails has the correct
        // value
        else {
            newDetails.setCreationEvent(previousDetails.getCreationEvent());
        }

        // update event ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        if (!HibernateUtils.idEqual(previousDetails.getUpdateEvent(),
                currentDetails.getUpdateEvent())) {

            // !idEquals implies that they aren't both null; if current_event is
            // null, then it was *probably* not intended, so just fix it and
            // move on. this goes for root and admins as well.
            if (currentDetails.getUpdateEvent() == null) {
                newDetails.setUpdateEvent(previousDetails.getUpdateEvent());
                altered = true;
            }
            // otherwise throw an exception, because,
            // it can lead to confusion otherwise. See:
            else {

                // no one change them, but this is less likely intentional
                // and more likely an optimistic lock issue.
                throw new OptimisticLockException(String.format(
                        "You are not authorized to change "
                                + "the update event for %s from %s to %s\n"
                                + "You may need to reload the object before continuing.", obj,
                        previousDetails.getUpdateEvent(), currentDetails
                                .getUpdateEvent()));
            }

        }

        // they are equal meaning no change was intended but in case other
        // changes took place, we have to make sure newDetails has the correct
        // value
        else {
            newDetails.setUpdateEvent(previousDetails.getUpdateEvent());
        }

        // QUATSCH
        // update event : newDetails keeps its update event, which is by
        // necessity different then what was in currentDetails and there-
        // fore we now return true;

        return altered;
    }

    // ~ Details checks. Used by to examine transient and managed Details.
    // =========================================================================
    // Also copied from BasicSecuritySystem

    /**
     * everyone is allowed to set the umask if desired. if the user does not set
     * a permissions, then the DEFAULT value as defined in the Permissions class
     * is used. if there's a umask for this session then that will be AND'd
     * against the given permissions.
     */
    boolean copyNonNullPermissions(Details target, Permissions p) {
        if (p != null) {
            target.setPermissions(p);
            return true;
        }
        return false;
    }

    void throwIfGroupsDontMatch(
            ExperimenterGroup changedObjectGroup, IObject changedObject,
            ExperimenterGroup linkedGroup, IObject linkedObject) {

        if (linkedGroup != null &&
            !HibernateUtils.idEqual(linkedGroup, changedObjectGroup)) {

            throw new GroupSecurityViolation(String.format(
                "MIXED GROUP: " +
                "%s(group=%s) and %s(group=%s) cannot be linked.",
                changedObject, changedObjectGroup,
                linkedObject, linkedGroup));
        }

    }

    void throwIfNotGranted(Permissions p, Role role, Right right,
            IObject linkedObject) {

        if (!p.isGranted(role, right)) {

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Group is %s. ", p));
            sb.append("Cannot link to object: ");
            sb.append(linkedObject);

            throw new SecurityViolation(sb.toString());
        }
    }
}