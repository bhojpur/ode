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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ode.system.EventContext;
import ode.api.IQuery;
import ode.api.local.LocalAdmin;
import ode.api.local.LocalQuery;
import ode.conditions.ApiUsageException;
import ode.conditions.InternalException;
import ode.conditions.SecurityViolation;
import ode.conditions.SessionTimeoutException;
import ode.model.IObject;
import ode.model.enums.AdminPrivilege;
import ode.model.enums.EventType;
import ode.model.internal.Details;
import ode.model.internal.GraphHolder;
import ode.model.internal.Permissions;
import ode.model.internal.Permissions.Right;
import ode.model.internal.Permissions.Role;
import ode.model.internal.Token;
import ode.model.meta.Event;
import ode.model.meta.EventLog;
import ode.model.meta.Experimenter;
import ode.model.meta.ExperimenterGroup;
import ode.model.meta.GroupExperimenterMap;
import ode.model.meta.Share;
import ode.parameters.Parameters;
import ode.security.ACLVoter;
import ode.security.AdminAction;
import ode.security.EventProvider;
import ode.security.SecureAction;
import ode.security.SecurityFilter;
import ode.security.SecurityFilterHolder;
import ode.security.SecuritySystem;
import ode.security.SystemTypes;
import ode.security.policy.DefaultPolicyService;
import ode.security.policy.PolicyService;
import ode.services.messages.EventLogMessage;
import ode.services.messages.EventLogsMessage;
import ode.services.sessions.SessionContext;
import ode.services.sessions.SessionManager;
import ode.services.sessions.SessionManagerImpl;
import ode.services.sessions.SessionProvider;
import ode.services.sessions.SessionProviderInMemory;
import ode.services.sessions.events.UserGroupUpdateEvent;
import ode.services.sessions.state.SessionCache;
import ode.services.sessions.stats.PerSessionStats;
import ode.services.sharing.ShareStore;
import ode.services.util.ReadOnlyStatus;
import ode.system.OdeContext;
import ode.system.Principal;
import ode.system.Roles;
import ode.system.ServiceFactory;
import ode.tools.hibernate.ExtendedMetadata;
import ode.tools.hibernate.SqlQueryTransformer;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.proxy.HibernateProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.Assert;

import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * simplest implementation of {@link SecuritySystem}. Uses an ctor-injected
 * {@link EventContext} and the {@link ThreadLocal ThreadLocal-}based
 * {@link CurrentDetails} to provide the security infrastructure.
 */
public class BasicSecuritySystem implements SecuritySystem,
        ApplicationContextAware, ApplicationListener<EventLogMessage> {

    private final static Logger log = LoggerFactory.getLogger(BasicSecuritySystem.class);

    protected final OdeInterceptor interceptor;

    protected final SystemTypes sysTypes;

    protected final CurrentDetails cd;

    protected final TokenHolder tokenHolder;

    protected final Roles roles;

    protected final SessionManager sessionManager;

    protected final SessionProvider sessionProvider;

    protected final EventProvider eventProvider;

    protected final ServiceFactory sf;

    protected final List<SecurityFilter> filters;

    protected final PolicyService policyService;

    protected/* final */OdeContext ctx;

    protected/* final */ShareStore store;

    protected final ACLVoter aclVoter;

    /**
     * Simplified factory method which generates all the security primitives
     * internally. Primarily useful for generated testing instances.
     * @param sm the session manager
     * @param sf the session factory
     * @param cache the session cache
     * @return a configured security system
     */
    public static BasicSecuritySystem selfConfigure(SessionManager sm,
            ServiceFactory sf, SessionCache cache) {
        CurrentDetails cd = new CurrentDetails(cache);
        SystemTypes st = new SystemTypes();
        TokenHolder th = new TokenHolder();
        Roles roles = new Roles();
        final SessionProvider sessionProvider = new SessionProviderInMemory(roles, new NodeProviderInMemory(""), null);
        final OdeInterceptor oi = new OdeInterceptor(roles,
                st, new ExtendedMetadata.Impl(),
                cd, th, new PerSessionStats(cd),
                new LightAdminPrivileges(roles), null, new SqlQueryTransformer(), new HashSet<>(), new HashSet<>());
        SecurityFilterHolder holder = new SecurityFilterHolder(
                cd, new OneGroupSecurityFilter(roles),
                new AllGroupsSecurityFilter(null, roles),
                new SharingSecurityFilter(roles, null));
        BasicSecuritySystem sec = new BasicSecuritySystem(oi, st, cd, sm, sessionProvider, new EventProviderInMemory(),
                roles, sf, new TokenHolder(), Collections.<SecurityFilter>singletonList(holder), new DefaultPolicyService(),
                new BasicACLVoter(cd, st, th, holder, sessionProvider, new ReadOnlyStatus(false, false)));
        return sec;
    }

    /**
     * Main public constructor for this {@link SecuritySystem} implementation.
     * @param interceptor the Bhojpur ODE interceptor for Hibernate
     * @param sysTypes the system types
     * @param cd the current details
     * @param sessionManager the session manager
     * @param sessionProvider a session provider
     * @param eventProvider an event provider
     * @param roles the Bhojpur ODE roles
     * @param sf the session factory
     * @param tokenHolder the token holder
     * @param filters the security filters
     * @param policyService the policy service
     * @param aclVoter the ACL voter, may be {@code null}
     */
    public BasicSecuritySystem(OdeInterceptor interceptor,
            SystemTypes sysTypes, CurrentDetails cd,
            SessionManager sessionManager, SessionProvider sessionProvider, EventProvider eventProvider,
            Roles roles, ServiceFactory sf,
            TokenHolder tokenHolder, List<SecurityFilter> filters,
            PolicyService policyService, ACLVoter aclVoter) {
        this.sessionManager = sessionManager;
        this.sessionProvider = sessionProvider;
        this.eventProvider = eventProvider;
        this.policyService = policyService;
        this.tokenHolder = tokenHolder;
        this.interceptor = interceptor;
        this.sysTypes = sysTypes;
        this.filters = filters;
        this.roles = roles;
        this.cd = cd;
        this.sf = sf;
        this.aclVoter = aclVoter;
    }

    public void setApplicationContext(ApplicationContext arg0)
            throws BeansException {
        this.ctx = (OdeContext) arg0;
        this.store = this.ctx.getBean("shareStore", ShareStore.class);
    }

    // ~ Login/logout
    // =========================================================================

    public void login(Principal principal) {
        cd.login(principal);
    }

    public int logout() {
        return cd.logout();
    }

    // ~ Checks
    // =========================================================================
    /**
     * implements {@link SecuritySystem#isReady()}. Simply checks for null
     * values in all the relevant fields of {@link CurrentDetails}
     */
    public boolean isReady() {
        return cd.isReady();
    }

    /**
     * classes which cannot be created by regular users.
     */
    public boolean isSystemType(Class<? extends IObject> klass) {
        return sysTypes.isSystemType(klass);
    }

    /**
     * tests whether or not the current user is either the owner of this entity,
     * or the supervisor of this entity, for example as root or as group owner.
     * 
     * @param iObject
     *            Non-null managed entity.
     * @return true if the current user is owner or supervisor of this entity
     */
    public boolean isOwnerOrSupervisor(IObject iObject) {
        return cd.isOwnerOrSupervisor(iObject);
    }

    // ~ Read security
    // =========================================================================
    /**
     * enables the read filter such that graph queries will have non-visible
     * entities silently removed from the return value. This filter does <em>
     * not</em>
     * apply to single value loads from the database. See
     * {@link ode.security.ACLVoter#allowLoad(Session, Class, Details, long)} for more.
     * 
     * Note: this filter must be disabled on logout, otherwise the necessary
     * parameters (current user, current group, etc.) for building the filters
     * will not be available. Similarly, while enabling this filter, no calls
     * should be made on the given session object.
     * 
     * @param session
     *            a generic session object which can be used to enable this
     *            filter. Each {@link SecuritySystem} implementation will
     *            require a specific session type.
     * @see EventHandler#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public void enableReadFilter(Object session) {
        if (session == null || !(session instanceof Session)) {
            throw new ApiUsageException(
                    "The Object argument to enableReadFilter"
                            + " in the BasicSystemSecurity implementation must be a "
                            + " non-null org.hibernate.Session.");
        }

        checkReady("enableReadFilter");
        // beware
        // http://opensource.atlassian.com/projects/hibernate/browse/HHH-1932
        final EventContext ec = getEventContext();
        final Session sess = (Session) session;
        for (final SecurityFilter filter : filters) {
            filter.enable(sess, ec);
        }
    }

    public void  updateReadFilter(Session session) {
        disableReadFilter(session);
        enableReadFilter(session);
    }

    /**
     * disable this filer. All future queries will have no security context
     * associated with them and all items will be visible.
     * 
     * @param session
     *            a generic session object which can be used to disable this
     *            filter. Each {@link SecuritySystem} implementation will
     *            require a specifc session type.
     * @see EventHandler#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public void disableReadFilter(Object session) {
        // Session system doesn't seem to provide this
        // i.e. isReady() is false here. Disabling but need to review
        // checkReady("disableReadFilter");

        Session sess = (Session) session;
        for (final SecurityFilter filter : filters) {
            filter.disable(sess);
        }
    }

    // ~ Subsystem disabling
    // =========================================================================

    public void disable(String... ids) {
        if (ids == null || ids.length == 0) {
            throw new ApiUsageException("Ids should not be empty.");
        }
        cd.addAllDisabled(ids);
    }

    public void enable(String... ids) {
        if (ids == null || ids.length == 0) {
            cd.clearDisabled();
        }
        cd.removeAllDisabled(ids);
    }

    public boolean isDisabled(String id) {
        if (id == null) {
            throw new ApiUsageException("Id should not be null.");
        }
        return cd.isDisabled(id);
    }

    // OdeInterceptor delegation
    // =========================================================================

    public Details newTransientDetails(IObject object)
            throws ApiUsageException, SecurityViolation {
        checkReady("transientDetails");
        return interceptor.newTransientDetails(object);
    }

    public Details checkManagedDetails(IObject object, Details trustedDetails)
            throws ApiUsageException, SecurityViolation {
        checkReady("managedDetails");
        return interceptor.checkManagedDetails(object, trustedDetails);
    }

    // ~ CurrentDetails delegation (ensures proper settings of Tokens)
    // =========================================================================


    public boolean isGraphCritical(Details details) {
        checkReady("isGraphCritical");
        return cd.isGraphCritical(details);
    }

    /**
     * Check the given group context.
     * @param sessionId a session ID
     * @param groupId a group ID
     * @return if the group context is permitted for the given session
     */
    protected boolean isGroupContextPermitted(long sessionId, long groupId) {
        final LocalQuery query = (LocalQuery) sf.getQueryService();
        return query.execute(new HibernateCallback<Boolean>() {
            public Boolean doInHibernate(Session session) {
                final SQLQuery query = session.createSQLQuery(
                        "SELECT a.textValue FROM sessionannotationlink l, annotation a " +
                        "WHERE l.parent = ? AND l.child = a.id AND a.owner_id = ? AND a.ns = ? AND a.discriminator = ?");
                query.setLong(0, sessionId);
                query.setLong(1, roles.getRootId());
                query.setString(2, SessionManagerImpl.GROUP_SUDO_NS);
                query.setString(3, "/basic/text/comment/");
                for (final Object permittedGroupIds : query.list()) {
                    if (!isGroupContextPermitted(groupId, (String) permittedGroupIds)) {
                        return false;
                    }
                }
                return true;
            }
        });
    }

    /**
     * @param groupId a group ID
     * @param permittedGroupIds a comma-separated string of group IDs
     * @return if the string of IDs contains the given group
     */
    protected boolean isGroupContextPermitted(long groupId, String permittedGroupIds) {
        final String requiredGroupId = Long.toString(groupId);
        for (final String permittedGroupId : Splitter.on(',').split(permittedGroupIds)) {
            if (requiredGroupId.equals(permittedGroupId)) {
                return true;
            }
        }
        return false;
    }

    public void loadEventContext(boolean isReadOnly) {
        loadEventContext(isReadOnly, false);
    }

    public void loadEventContext(boolean isReadOnly, boolean isClose) {

        final LocalAdmin admin = (LocalAdmin) sf.getAdminService();

        // Call to session manager throws an exception on failure
        final Principal p = clearAndCheckPrincipal();

        // Rather than catch the RemoveSessionException
        // we are going to check the type of the context and if it
        // matches, then we know we should do no more loading.
        EventContext ec = cd.getCurrentEventContext();
        if (ec instanceof BasicSecurityWiring.CloseOnNoSessionContext) {
            throw new SessionTimeoutException("closing", ec);
        }

        // Catching SessionTimeoutException in order to permit
        // the close of a stateful service.
        try {
            ec = sessionManager.getEventContext(p);
        } catch (SessionTimeoutException ste) {
            if (!isClose) {
                throw ste;
            }
            ec = (EventContext) ste.sessionContext;
        }

        // Ensure that sudoer has name loaded for SimpleEventContext.copy
        final boolean isShare;
        if (ec instanceof SessionContext) {
            final ode.model.meta.Session session = ((SessionContext) ec).getSession();
            Experimenter sudoer = session.getSudoer();
            if (!(sudoer == null || sudoer.isLoaded())) {
                final Long sudoerId = sudoer.getId();
                final IQuery iQuery = sf.getQueryService();
                final String hql = "SELECT odeName FROM Experimenter WHERE id = :id";
                final Parameters params = new Parameters().addId(sudoerId);
                final List<Object[]> result = iQuery.projection(hql, params);
                final String sudoerName = (String) result.get(0)[0];
                sudoer = new Experimenter(sudoerId, true);
                sudoer.setOdeName(sudoerName);
                session.setSudoer(sudoer);
            }
            isShare = session instanceof Share;
        } else {
            isShare = false;
        }

        // Refill current details
        cd.checkAndInitialize(ec, admin, store);
        ec = cd.getCurrentEventContext(); // Replace with callContext
        final long groupId = ec.getCurrentGroupId();
        final long sessionId = ec.getCurrentSessionId();

        // Check that group context is consistent with any group sudo.
        if (!isGroupContextPermitted(sessionId, groupId)) {
            throw new SecurityViolation("Group-sudo session cannot change context!");
        }

        // Experimenter
        Experimenter exp;
        if (isReadOnly) {
            exp = new Experimenter(ec.getCurrentUserId(), false);
        } else {
            exp = admin.userProxy(ec.getCurrentUserId());
        }
        tokenHolder.setToken(exp.getGraphHolder());

        // Sudoer
        final Experimenter sudoer;
        final Long sudoerId = ec.getCurrentSudoerId();
        if (sudoerId == null) {
            sudoer = null;
        } else if (isReadOnly) {
            sudoer = new Experimenter(sudoerId, false);
        } else {
            sudoer = admin.userProxy(sudoerId);
        }

        // isAdmin
        boolean isAdmin = false;
        for (long gid : ec.getMemberOfGroupsList()) {
            if (roles.getSystemGroupId() == gid) {
                isAdmin = true;
                break;
            }
        }

        // admin privileges
        final Set<AdminPrivilege> adminPrivileges = ec.getCurrentAdminPrivileges();

        // Active group - starting with #3529, the current group and the current
        // share values should be definitive as setting the context on
        // BasicEventContext will automatically update the global values. For
        // security reasons, we need only guarantee that non-admins are
        // actually members of the noted groups.
        //
        // Joined with public group block
        Long shareId = ec.getCurrentShareId();
        ExperimenterGroup callGroup = null;
        ExperimenterGroup eventGroup = null;
        long eventGroupId;
        Permissions callPerms;

        // Code copied in SessionManagerImpl
        if (groupId >= 0) { // negative groupId means all member groups
            eventGroupId = groupId;
            callGroup = admin.groupProxy(groupId);
            eventGroup = callGroup;
            callPerms = callGroup.getDetails().getPermissions();

            if (!isAdmin && !ec.getMemberOfGroupsList().contains(groupId)) {
                if (!callPerms.isGranted(Role.WORLD, Right.READ)) {
                    throw new SecurityViolation(String.format(
                        "User %s is not a member of group %s and cannot login",
                                ec.getCurrentUserId(), groupId));
                }
            }

        } else {
            List<Long> memList = ec.getMemberOfGroupsList();
            eventGroupId = memList.get(0);
            if (eventGroupId == roles.getUserGroupId() && memList.size() > 1) {
                eventGroupId = memList.get(1);
            }
            log.debug("Choice for event group: " + eventGroupId);

            eventGroup = admin.getGroup(eventGroupId);
            callGroup = new ExperimenterGroup(groupId, false);
            callPerms = Permissions.DUMMY;

        }

        final ode.model.meta.Session session;
        if (isShare) {
            /* Cannot read annotations on session. */
            session = new ode.model.meta.Session(sessionId, false);
        } else {
            session = sessionProvider.findSessionById(sessionId, sf);
        }

        tokenHolder.setToken(callGroup.getGraphHolder());

        // In order to less frequently access the ThreadLocal in CurrentDetails
        // All properties are now set in one shot, except for Event.
        cd.setValues(exp, sudoer, callGroup, callPerms, isAdmin, adminPrivileges, isReadOnly, shareId);

        // Event
        String t = p.getEventType();
        if (t == null) {
            t = ec.getCurrentEventType();
        }
        EventType type = new EventType(t);
        tokenHolder.setToken(type.getGraphHolder());
        final Event event = cd.newEvent(session, type, tokenHolder);
        tokenHolder.setToken(event.getGraphHolder());

        // If this event is not read only, then let's save this event to prevent
        // flushing issues later.
        if (!isReadOnly) {
            if (event.getExperimenterGroup().getId() < 0) {
                event.setExperimenterGroup(eventGroup);
            }
            cd.updateEvent(eventProvider.updateEvent(event)); // TODO use merge
        }
    }

    private Principal clearAndCheckPrincipal() {

        // clear even if this fails. (make SecuritySystem unusable)
        invalidateEventContext();

        if (cd.size() == 0) {
            throw new SecurityViolation(
                    "Principal is null. Not logged in to SecuritySystem.");
        }

        final Principal p = cd.getLast();

        if (p.getName() == null) {
            throw new InternalException(
                    "Principal.name is null. Security system failure.");
        }

        return p;
    }

    public void addLog(String action, Class klass, Long id) {
        cd.addLog(action, klass, id);
    }

    public List<EventLog> getLogs() {
        return cd.getLogs();
    }

    public void clearLogs() {
        if (log.isDebugEnabled()) {
            log.debug("Clearing EventLogs.");
        }

        List<EventLog> logs = getLogs();
        if (!logs.isEmpty()) {

            boolean foundAdminType = false;
            final Multimap<String, EventLog> map = ArrayListMultimap.create();

            for (EventLog el : getLogs()) {
                String t = el.getEntityType();
                if (Experimenter.class.getName().equals(t)
                        || ExperimenterGroup.class.getName().equals(t)
                        || GroupExperimenterMap.class.getName().equals(t)) {
                    foundAdminType = true;
                }
                map.put(t, el);
            }

            if (ctx == null) {
                log.error("No context found for publishing");
            } else {
                // publish message if administrative type is modified
                if (foundAdminType) {
                    this.ctx.publishEvent(new UserGroupUpdateEvent(this));
                }
                this.ctx.publishEvent(new EventLogsMessage(this, map));
            }
        }
        
        cd.clearLogs();
    }

    public void invalidateEventContext() {
        if (log.isDebugEnabled()) {
            log.debug("Invalidating current EventContext.");
        }
        cd.invalidateCurrentEventContext();
    }

    // ~ Tokens & Actions
    // =========================================================================

    /**
     * 
     * It would be better to catch the
     * {@link SecureAction#updateObject(IObject...)} method in a try/finally block,
     * but since flush can be so poorly controlled that's not possible. instead,
     * we use the one time token which is removed this Object is checked for
     * {@link #hasPrivilegedToken(IObject) privileges}.
     * 
     * @param objs
     *            A managed (non-detached) entity. Not null.
     * @param action
     *            A code-block that will be given the entity argument with a
     *            {@link #hasPrivilegedToken(IObject)} privileged token}.
     */
    public <T extends IObject> T doAction(SecureAction action, T... objs) {
        Assert.notNull(objs);
        Assert.notEmpty(objs);
        Assert.notNull(action);

        final LocalQuery query = (LocalQuery) sf.getQueryService();
        final List<GraphHolder> ghs = new ArrayList<GraphHolder>();

        for (T obj : objs) {

            // TODO inject
            if (obj.getId() != null && !query.contains(obj)) {
                throw new SecurityViolation("Services are not allowed to call "
                        + "doAction() on non-Session-managed entities.");
            }

            // use of IQuery.get along with doAction() creates
            // two objects (outer proxy and inner target) and only the outer
            // proxy has its graph holder modified without this block, leading
            // to security violations on flush since no token is present.
            if (obj instanceof HibernateProxy) {
                HibernateProxy hp = (HibernateProxy) obj;
                IObject obj2 = (IObject) hp.getHibernateLazyInitializer().getImplementation();
                ghs.add(obj2.getGraphHolder());
            }

            // FIXME
            // Token oneTimeToken = new Token();
            // oneTimeTokens.put(oneTimeToken);
            ghs.add(obj.getGraphHolder());

        }

        // Holding onto the graph holders since they protect the access
        // to their tokens
        for (GraphHolder graphHolder : ghs) {
            tokenHolder.setToken(graphHolder); // oneTimeToken
        }

        T retVal;
        try {
            retVal = action.updateObject(objs);
        } finally {
            for (GraphHolder graphHolder : ghs) {
                tokenHolder.clearToken(graphHolder);
            }
        }
        return retVal;
    }

    /**
     * Calls {@link #runAsAdmin(AdminAction)} with a null-group id.
     */
    public void runAsAdmin(final AdminAction action) {
        runAsAdmin(null, action);
    }

    /**
     * merge event is disabled for {@link #runAsAdmin(AdminAction)} because
     * passing detached (client-side) entities to this method is particularly
     * dangerous.
     */
    public void runAsAdmin(final ExperimenterGroup group, final AdminAction action) {

        Assert.notNull(action);

        // Need to check here so that no exception is thrown
        // during the try block below
        checkReady("runAsAdmin");

        final LocalQuery query = (LocalQuery) sf.getQueryService();
        query.execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {

                BasicEventContext c = cd.current();
                boolean wasAdmin = c.isCurrentUserAdmin();
                final Set<AdminPrivilege> oldAdminPrivileges = c.getAdminPrivileges();
                ExperimenterGroup oldGroup = c.getGroup();

                try {
                    c.setAdmin(true);
                    c.setAdminPrivileges(LightAdminPrivileges.getAllPrivileges());
                    if (group != null) {
                        c.setGroup(group, group.getDetails().getPermissions());
                    }
                    disable(MergeEventListener.MERGE_EVENT);
                    enableReadFilter(session);
                    action.runAsAdmin();
                } finally {
                    c.setAdmin(wasAdmin);
                    c.setAdminPrivileges(oldAdminPrivileges);
                    if (group != null) {
                        c.setGroup(oldGroup, oldGroup.getDetails().getPermissions());
                    }
                    enable(MergeEventListener.MERGE_EVENT);
                    enableReadFilter(session); // Now as non-admin
                }
                return null;
            }
        });
    }

    /**
     * @see TokenHolder#copyToken(IObject, IObject)
     */
    public void copyToken(IObject source, IObject copy) {
        tokenHolder.copyToken(source, copy);
    }

    /**
     * @see TokenHolder#hasPrivilegedToken(IObject)
     */
    public boolean hasPrivilegedToken(IObject obj) {
        return tokenHolder.hasPrivilegedToken(obj);
    }

    public void checkRestriction(String name, IObject obj) {
        policyService.checkRestriction(name, obj);
    }

    // ~ Configured Elements
    // =========================================================================

    public Roles getSecurityRoles() {
        return roles;
    }

    public EventContext getEventContext(boolean refresh) {
        EventContext ec = cd.getCurrentEventContext();
        if (refresh) {
            String uuid = ec.getCurrentSessionUuid();
            ec = sessionManager.reload(uuid);
        }
        return ec;
    }

    public EventContext getEventContext() {
        return getEventContext(false);
    }

    /**
     * Returns the Id of the currently logged in user.
     * Returns owner of the share while in share
     * @return See above.
     */
    public Long getEffectiveUID() {
        final EventContext ec = getEventContext();
        final Long shareId = ec.getCurrentShareId();
        if (shareId != null) {
            if (shareId < 0) {
                return null;
            }
            ode.model.meta.Session s = sf.getQueryService().get(
                    ode.model.meta.Session.class, shareId);
            return s.getOwner().getId();
        }
        return ec.getCurrentUserId();
    }

    // ~ Helpers
    // =========================================================================

    /**
     * calls {@link #isReady()} and if not throws an {@link ApiUsageException}.
     * The {@link SecuritySystem} must be in a valid state to perform several
     * functions.
     */
    protected void checkReady(String method) {
        if (!isReady()) {
            throw new ApiUsageException("The security system is not ready.\n"
                    + "Cannot execute: " + method);
        }

    }

    public void onApplicationEvent(EventLogMessage elm) {
        if (elm != null) {
            for (Long id : elm.entityIds) {
                addLog(elm.action, elm.entityType, id);
            }
        }
    }

}