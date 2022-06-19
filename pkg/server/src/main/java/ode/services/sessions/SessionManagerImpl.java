package ode.services.sessions;

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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import ode.model.IObject;
import ode.system.EventContext;
import ode.api.local.LocalAdmin;
import ode.conditions.ApiUsageException;
import ode.conditions.AuthenticationException;
import ode.conditions.InternalException;
import ode.conditions.RemovedSessionException;
import ode.conditions.SecurityViolation;
import ode.conditions.SessionException;
import ode.conditions.SessionTimeoutException;
import ode.model.annotations.Annotation;
import ode.model.annotations.CommentAnnotation;
import ode.model.annotations.TextAnnotation;
import ode.model.enums.AdminPrivilege;
import ode.model.enums.EventType;
import ode.model.internal.Details;
import ode.model.internal.NamedValue;
import ode.model.internal.Permissions;
import ode.model.meta.Experimenter;
import ode.model.meta.ExperimenterGroup;
import ode.model.meta.Session;
import ode.model.meta.Share;
import ode.parameters.Parameters;
import ode.security.basic.LightAdminPrivileges;
import ode.security.basic.PrincipalHolder;
import ode.services.messages.CreateSessionMessage;
import ode.services.messages.DestroySessionMessage;
import ode.services.sessions.events.ChangeSecurityContextEvent;
import ode.services.sessions.events.UserGroupUpdateEvent;
import ode.services.sessions.state.SessionCache;
import ode.services.sessions.stats.CounterFactory;
import ode.services.sessions.stats.SessionStats;
import ode.services.util.Executor;
import ode.services.util.Executor.Priority;
import ode.services.util.ReadOnlyStatus;
import ode.system.OdeContext;
import ode.system.Principal;
import ode.system.Roles;
import ode.system.ServiceFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Joiner;
import com.google.common.collect.MapMaker;

/**
 * Is for ISession a cache and will be kept there in sync? OR Factors out the
 * logic from ISession and SessionManagerI
 *
 * Therefore either called directly, or via synchronous messages.
 *
 * Uses the name of a Principal as the key to the session. We may need to limit
 * user names to prevent this. (Strictly alphanumeric)
 *
 * Receives notifications as an {@link ApplicationListener}, which should be
 * used to keep the {@link Session} instances up-to-date.
 */
public class SessionManagerImpl implements SessionManager, SessionCache.StaleCacheListener,
        ApplicationContextAware, ApplicationListener<ApplicationEvent> {

    public final static String GROUP_SUDO_NS = "bhojpur.net/security/group-sudo";

    private final static Logger log = LoggerFactory.getLogger(SessionManagerImpl.class);

    /**
     * The id of this session manager, used to identify its own actions. This
     * value may be overwritten by an injector with a value which is used
     * throughout this server instance.
     */
    private String internal_uuid = UUID.randomUUID().toString();

    // Injected
    protected OdeContext context;
    protected Roles roles;
    protected LightAdminPrivileges adminPrivileges;
    protected SessionCache cache;
    protected Executor executor;
    protected long defaultTimeToIdle;
    protected long maxUserTimeToIdle;
    protected long defaultTimeToLive;
    protected long maxUserTimeToLive;
    protected PrincipalHolder principalHolder;
    protected CounterFactory factory;
    protected boolean readOnly = false;
    protected SessionProvider sessionProvider;

    // Local state

    /**
     * A private session for use only by this instance for running methods via
     * {@link Executor}. The name of this {@link Principal} will not be removed
     * by calls to {@link #closeAll()}.
     */
    protected Principal asroot;

    /**
     * Internal {@link SessionContext} created during {@link #init()} which is
     * used for all method calls internal to the session manager (see execute*
     * methods)
     */
    protected SessionContext internalSession;

    // ~ Injectors
    // =========================================================================

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.context = (OdeContext) applicationContext;
    }

    public void setUuid(String uuid) {
        this.internal_uuid = uuid;
    }

    public void setSessionCache(SessionCache sessionCache) {
        cache = sessionCache;
        this.cache.setStaleCacheListener(this);
    }

    public void setRoles(Roles securityRoles) {
        roles = securityRoles;
    }

    public void setAdminPrivileges(LightAdminPrivileges adminPrivileges) {
        this.adminPrivileges = adminPrivileges;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public void setDefaultTimeToIdle(long defaultTimeToIdle) {
        this.defaultTimeToIdle = defaultTimeToIdle;
    }

    public void setMaxUserTimeToIdle(long maxUserTimeToIdle) {
        this.maxUserTimeToIdle = maxUserTimeToIdle;
    }

    public void setDefaultTimeToLive(long defaultTimeToLive) {
        this.defaultTimeToLive = defaultTimeToLive;
    }

    public void setMaxUserTimeToLive(long maxUserTimeToLive) {
        this.maxUserTimeToLive = maxUserTimeToLive ;
    }

    public void setPrincipalHolder(PrincipalHolder principal) {
        this.principalHolder = principal;
    }

    public void setCounterFactory(CounterFactory factory) {
        this.factory = factory;
    }

    public void setReadOnly(ReadOnlyStatus readOnly) {
        this.readOnly = readOnly.isReadOnlyDb();
    }

    public void setSessionProvider(SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    /**
     * Initialization method called by the Spring run-time to acquire an initial
     * {@link Session}.
     */
    public void init() {
        try {
            asroot = new Principal(internal_uuid, "system", "Sessions");

            // Create a basic session
            Session session = new Session();
            define(session, internal_uuid, "Session Manager internal",
                    System.currentTimeMillis(), Long.MAX_VALUE, 0L,
                    "Sessions", "Internal", null);

            session = sessionProvider.executeInternalSession(internal_uuid, session);
            internalSession = new InternalSessionContext(session, LightAdminPrivileges.getAllPrivileges(), roles);
            cache.putSession(internal_uuid, internalSession);
        } catch (UncategorizedSQLException uncat) {
            log.warn("Assuming that this is read-only");
        } catch (DataAccessException dataAccess) {
            throw new RuntimeException(
                    "          "
                            + "=====================================================\n"
                            + "Data access exception: Did you create your database? \n"
                            + "=====================================================\n",
                    dataAccess);
        }
    }

    // ~ Session definition
    // =========================================================================

    protected void define(Session s, String uuid, String message, long started,
            CreationRequest req) {
        Long idle = req.timeToIdle == null ? defaultTimeToIdle : req.timeToIdle;
        Long live = req.timeToLive == null ? defaultTimeToLive : req.timeToLive;
        if (req.groupsLed != null) {
            CommentAnnotation ca = new CommentAnnotation();
            ca.setNs(GROUP_SUDO_NS);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < req.groupsLed.size(); i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(req.groupsLed.get(i));
            }
            ca.setTextValue(sb.toString());
            s.linkAnnotation(ca);
        }
        define(s, uuid, message, started, idle, live,
                req.principal.getEventType(), req.agent, req.ip);
    }

    protected void define(Session s, String uuid, String message, long started,
            long idle, long live, String eventType, String agent, String ip) {
        s.getDetails().setPermissions(Permissions.PRIVATE);
        s.setUuid(uuid);
        s.setMessage(message);
        s.setStarted(new Timestamp(started));
        s.setTimeToIdle(idle);
        s.setTimeToLive(live);
        s.setDefaultEventType(eventType);
        s.setUserAgent(agent);
        s.setUserIP(ip);
    }

    // ~ Session management
    // =========================================================================

    @Override
    public Session createFromRequest(CreationRequest request) {

        // If credentials exist as session, then return that
        if (request.credentials != null) {
            try {
                SessionContext context = cache
                        .getSessionContext(request.credentials);
                if (context != null) {
                    context.count().increment();
                    return context.getSession(); // EARLY EXIT!
                }
            } catch (SessionException se) {
                // oh well.
            }

            // Though trusted values, if we receive a null principal, not ok;
            boolean ok = request.principal == null ? false : executeCheckPassword(
                request.principal, request.credentials);

            if (!ok) {
                log.warn("Failed to authenticate: " + request.principal);
                throw new AuthenticationException("Authentication exception.");
            }
        }

        // authentication checked. Now delegating to the admin method (no pass)
        Session session = new Session();
        define(session, UUID.randomUUID().toString(), "Initial message.",
                System.currentTimeMillis(), request);
        return createSession(request, session);
    }

    /*k
     * Is given trustable values by the {@link SessionBean}
     */
    @Override
    public Session createWithAgent(Principal _principal, final String credentials, String agent, String ip) {
        final CreationRequest req = new CreationRequest();
        req.principal = _principal;
        req.credentials = credentials;
        req.agent = agent;
        req.ip = ip;
        return createFromRequest(req);
    }

    @Override
    public Session createWithAgent(Principal principal, String agent, String ip) {
        final CreationRequest req = new CreationRequest();
        req.principal = principal;
        req.agent = agent;
        req.ip = ip;
        return createFromRequest(req);
    }

    @Override
    public Share createShare(Principal principal, boolean enabled,
                             long timeToLive, String eventType, String description,
                             long groupId) {
        Share share = newShare();
        define(share, UUID.randomUUID().toString(), description, System
                .currentTimeMillis(), defaultTimeToIdle, timeToLive, eventType,
                "Share", null);
        share.setGroup(new ExperimenterGroup(groupId, false));
        share.setActive(enabled);
        share.setData(new byte[] {});
        share.setItemCount(0L);
        CreationRequest req = new CreationRequest();
        req.principal = principal;
        return (Share) createSession(req, share);
    }

    @SuppressWarnings("unchecked")
    private Session createSession(final CreationRequest req,
            final Session oldsession) {

        final Principal principal = req.principal;

        if (internal_uuid != null && internal_uuid.equals(principal.getName())) {
            /* 2018-SV2 */
            throw new AuthenticationException("to create a session one may not use the internal UUID as the principal's user name");
        }

        // If username exists as session, then return that
        try {
            SessionContext context = cache.getSessionContext(principal.getName());
            if (context != null) {
                context.count().increment();
                return context.getSession(); // EARLY EXIT!
            }
        } catch (SessionException se) {
            // oh well
        }

        List<Object> rv;
        Map<String, String> sysContext = new HashMap<String, String>();
        sysContext.put("ode.group", Long.toString(roles.getSystemGroupId()));
        // No reason to perform this in any group other than system.
        if (readOnly) {
            rv = (List<Object>) executor.execute(sysContext, this.asroot,
                    new Executor.SimpleWork(
                    this, "read-only createSession") {
                @Transactional(readOnly = true)
                public Object doWork(org.hibernate.Session __s,
                        ServiceFactory sf) {
                    Principal p = validateSessionInputs(sf, req);
                    oldsession.setDefaultEventType(p.getEventType());
                    final long userId = executeLookupUser(sf, p);
                    // Here, we hope that the implementation has been updated
                    // to match read-only status. Note: this code block matches
                    // the one below, but the annotation is a compile-time rather
                    // than run-time concern.
                    final Session s = sessionProvider.executeUpdate(sf, oldsession, internal_uuid, userId, req.sudoer);
                    return executeSessionContextLookup(sf, p, s);
                }
            });
        } else {
            rv = (List<Object>) executor.execute(sysContext, this.asroot,
                    new Executor.SimpleWork(
                    this, "createSession") {
                @Transactional(readOnly = false)
                public Object doWork(org.hibernate.Session __s,
                        ServiceFactory sf) {
                    Principal p = validateSessionInputs(sf, req);
                    oldsession.setDefaultEventType(p.getEventType());
                    long userId = executeLookupUser(sf, p);
                    final Session s = sessionProvider.executeUpdate(sf, oldsession, internal_uuid, userId, req.sudoer);
                    return executeSessionContextLookup(sf, p, s);
                }

            });
        }

        if (rv == null) {
            throw new RemovedSessionException("No info in database for "
                    + principal);
        }
        SessionContext newctx = createSessionContext(rv, null);

        // This the publishEvent returns successfully, then we will have to
        // handle rolling back this addition ourselves
        String uuid = newctx.getCurrentSessionUuid();
        cache.putSession(uuid, newctx);
        try {
            context.publishEvent(new CreateSessionMessage(this, uuid));
        } catch (RuntimeException re) {
            log.warn("Session creation cancelled by event listener", re);
            cache.removeSession(uuid);
            throw re;
        }

        // All successful, increment and return.
        newctx.count().increment();
        return newctx.getSession();
    }

    @Override
    public Session update(Session session) {
        return update(session, false);
    }

    @Override
    public Session update(final Session session, final boolean trusted) {

        if (session == null || !session.isLoaded() || session.getUuid() == null) {
            throw new RemovedSessionException("Cannot update; No uuid.");
        }

        final String uuid = session.getUuid();
        final Details details = session.getDetails();
        final SessionContext ctx = cache.getSessionContext(uuid);
        if (ctx == null) {
            throw new RemovedSessionException(
                    "Can't update; No session with uuid:" + uuid);
        }

        final Session orig = ctx.getSession();

        // TODO // FIXME
        // =====================================================
        // This needs to get smarter

        @SuppressWarnings("unchecked")
        List<Object> list = (List<Object>) executor.execute(asroot, new Executor.SimpleWork(
                this, "load_for_update") {
            @Transactional(readOnly = false)
            public Object doWork(org.hibernate.Session __s, ServiceFactory sf) {

                // Allow user to change default group
                String defaultGroup = null;
                if (details != null) {
                    ExperimenterGroup group = details.getGroup();
                    if (group != null) {
                        try {
                            Long groupId = group.getId();
                            if (groupId != null) {
                                group = ((LocalAdmin) sf.getAdminService())
                                        .groupProxy(groupId);
                                if (group != null) {
                                    defaultGroup = group.getName();
                                }
                            }
                        } catch (Exception e) {
                            throw new ApiUsageException(
                                    "Cannot change default group to " + group
                                            + "\n" + e.getMessage());
                        }
                    }
                }

                // If still null, take the current
                if (defaultGroup == null) {
                    defaultGroup = ctx.getCurrentGroupName();
                }

                Principal principal = new Principal(ctx.getCurrentUserName(),
                        defaultGroup, ctx.getCurrentEventType());
                CreationRequest req = new CreationRequest();
                req.principal = principal;
                principal = validateSessionInputs(sf, req);

                // Unconditionally settable; these are open to the user for
                // change
                parseAndSetDefaultType(session.getDefaultEventType(), orig);
                parseAndSetUserAgent(session.getUserAgent(), orig);

                // Conditionally settable
                parseAndSetTimeouts(session.getTimeToLive(), session
                        .getTimeToIdle(), orig, trusted);

                // TODO Need to handle notifications
                return executeSessionContextLookup(sf, principal, orig);

            }
        });

        if (list == null) {
            log.info("removeSession on update: " + uuid);
            cache.removeSession(uuid);
            throw new RemovedSessionException("Database contains no info for "
                    + uuid);
        }
        ;

        final SessionContext newctx = createSessionContext(list, ctx);
        final Session copy = copy(orig);
        executor.execute(asroot, new Executor.SimpleWork<Session>(this, "update") {
            @Transactional(readOnly = false)
            public Session doWork(org.hibernate.Session __s, ServiceFactory sf) {
                final Long sudoerId;
                if (orig.getSudoer() == null) {
                    sudoerId = null;
                } else {
                    sudoerId = orig.getSudoer().getId();
                }
                return sessionProvider.executeUpdate(sf, copy, internal_uuid, newctx.getCurrentUserId(), sudoerId);
            }
        });
        cache.putSession(uuid, newctx);
        return copy(orig);

    }

    /**
     * Takes a snapshot as from
     * {@link #executeSessionContextLookup(ServiceFactory, Principal, Session)}
     * and turns it into a {@link SessionContext} instance.
     * List argument should never be null. Abort if
     * {@link #executeSessionContextLookup(ServiceFactory, Principal, Session)}
     * returns null.
     */
    @SuppressWarnings("unchecked")
    protected SessionContext createSessionContext(List<?> list, SessionContext previous) {

        final Experimenter exp = (Experimenter) list.get(0);
        final ExperimenterGroup grp = (ExperimenterGroup) list.get(1);
        final Set<AdminPrivilege> adminPrivileges = (Set<AdminPrivilege>) list.get(2);
        final List<Long> memberOfGroupsIds = (List<Long>) list.get(3);
        final List<Long> leaderOfGroupsIds = (List<Long>) list.get(4);
        final List<String> userRoles = (List<String>) list.get(5);
        final Principal principal = (Principal) list.get(6);
        final Session session = (Session) list.get(7);

        parseAndSetDefaultType(principal.getEventType(), session);

        session.getDetails().setOwner(exp);
        session.getDetails().setGroup(grp);

        SessionContext sessionContext = new SessionContextImpl(session, adminPrivileges,
                leaderOfGroupsIds, memberOfGroupsIds, userRoles, factory
                        .createStats(), roles, previous);
        return sessionContext;
    }

    @Override
    public Session find(String uuid) {
        SessionContext sessionContext = cache.getSessionContext(uuid);
        checkIfShare(sessionContext);
        return (sessionContext == null) ? null : sessionContext.getSession();
    }

    @Override
    public Session findQuietly(String uuid) {
        SessionContext sessionContext = cache.getSessionContext(uuid, true);
        checkIfShare(sessionContext);
        return (sessionContext == null) ? null : sessionContext.getSession();
    }

    private void checkIfShare(SessionContext sessionContext) {
        if (sessionContext.getSession() instanceof Share) {
            final Long id = sessionContext.getSession().getId();
            final String uuid = sessionContext.getSession().getUuid();
            final String prefix = String.format("Share:%s (%s)", id, uuid);

            List<Object[]> rv = executeProjection(
                    "select s.active, s.timeToLive, s.started from Share s where s.id = :id",
                    new Parameters().addId(sessionContext.getSession().getId()));

            if (rv.size() != 1) {
                throw new RuntimeException(prefix + " could not be found!");
            }

            Object[] items = rv.get(0);
            Boolean active = (Boolean) items[0];
            Long timeToLive = (Long) items[1];
            Timestamp started = (Timestamp) items[2];

            if (Boolean.FALSE.equals(active)) {
               throw new SecurityViolation(prefix + " is inactive");
            } else if ((System.currentTimeMillis() - started.getTime()) > timeToLive) {
                String msg = String.format("%s has expired: %s, timeToLive=%s",
                        prefix, started, timeToLive);
                throw new SecurityViolation(msg);
            }
        }
    }

    private List<Session> findByQuery(String query, Parameters p) {
        List<Object[]> ids_uuids = executeProjection(query, p);
        List<Session> rv = new ArrayList<Session>();
        for (Object[] arr : ids_uuids) {
            String uuid = (String) arr[1];
            try {
                SessionContext sc = cache.getSessionContext(uuid);
                rv.add(sc.getSession());
            } catch (Exception e) {
                // skip
            }
        }
        return rv;
    }

    @Override
    public List<Session> findSameUser(String uuid, String... agents) {
        /* determine the light administrator privileges associated with the given session */
        final Session session = find(uuid);
        final String membershipQuery = "SELECT id FROM GroupExperimenterMap WHERE parent.id = :group AND child.id = :user";
        boolean hasAdminPrivileges = CollectionUtils.isNotEmpty(executeProjection(membershipQuery,
                new Parameters().addLong("group", roles.getSystemGroupId()).addLong("user", session.getOwner().getId())));
        if (session.getSudoer() != null) {
            hasAdminPrivileges = hasAdminPrivileges && CollectionUtils.isNotEmpty(executeProjection(membershipQuery,
                    new Parameters().addLong("group", roles.getSystemGroupId()).addLong("user", session.getSudoer().getId())));
        }
        final Set<AdminPrivilege> privileges;
        if (hasAdminPrivileges) {
            privileges = adminPrivileges.getSessionPrivileges(session);
        } else {
            privileges = Collections.emptySet();
        }
        /* determine which agent values should filter results */
        final Set<String> agentSet = new HashSet<>();
        boolean nullAgent = false;
        for (final String agent : agents) {
            if (agent == null) {
                nullAgent = true;
            } else {
                agentSet.add(agent);
            }
        }
        /* construct and perform the query */
        final StringBuilder sessionQuery = new StringBuilder();
        final Parameters params = new Parameters();
        sessionQuery.append("SELECT id, uuid FROM Session WHERE closed IS NULL");
        sessionQuery.append(" AND owner.id = :owner");
        params.addLong("owner", session.getOwner().getId());
        if (!privileges.contains(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_READ_SESSION))) {
            /* user is not privileged so is limited to where sudoer is the same as their current session */
            if (session.getSudoer() == null) {
                sessionQuery.append(" AND sudoer IS NULL");
            } else {
                sessionQuery.append(" AND sudoer.id = :sudoer");
                params.addLong("sudoer", session.getSudoer().getId());
            }
        }
        final List<String> agentClauses = new ArrayList<String>();
        if (!agentSet.isEmpty()) {
            agentClauses.add("userAgent IN (:agents)");
            params.addSet("agents", agentSet);
        }
        if (nullAgent) {
            agentClauses.add("userAgent IS NULL");
        }
        if (!agentClauses.isEmpty()) {
            sessionQuery.append(" AND (" + Joiner.on(" OR ").join(agentClauses) + ")");
        }
        sessionQuery.append(" ORDER BY started DESC");
        return findByQuery(sessionQuery.toString(), params);
    }

    @Override
    public int getReferenceCount(String uuid) {
        SessionContext ctx = cache.getSessionContext(uuid);
        return ctx.count().get();
    }

    @Override
    public int detach(String uuid) {
        SessionContext ctx = cache.getSessionContext(uuid);
        return ctx.count().decrement();
    }

    @Override
    public SessionStats getSessionStats(String uuid) {
        SessionContext ctx = cache.getSessionContext(uuid);
        return ctx.stats();
    }

    /*
     */
    @Override
    public int close(String uuid) {
        SessionContext ctx;
        try {
            ctx = cache.getSessionContext(uuid);
        } catch (SessionException se) {
            log.info("closeSession called but doesn't exist: " + uuid);
            return -1; // EARLY EXIT!
        }

        int refCount = ctx.count().decrement();
        if (refCount < 1) {
            log.info("closeSession called and no more references: " + uuid);
            cache.removeSession(uuid);
            return -2;
        } else {
            log.info("closeSession called but " + refCount
                    + " more references: " + uuid);
            return refCount;
        }
    }

    @Override
    public Map<String, Map<String, Object>> getSessionData() {
        final Collection<String> ids = cache.getIds();
        final Map<String, Map<String, Object>> rv
            = new HashMap<String, Map<String, Object>>();

        for (String id : ids) {
            if (asroot.getName().equals(id)) {
                continue; // DON'T INCLUDE ROOT SESSION
            }
            try {
                rv.put(id,  cache.getSessionData(id, true));
            } catch (RemovedSessionException rse) {
                // Ok. Done for us
            } catch (SessionTimeoutException ste) {
                // Also ok
            } catch (Exception e) {
                log.warn(String.format("Exception thrown on getAll: %s:%s", e
                        .getClass().getName(), e.getMessage()));
            }
        }
        return rv;
    }

    @Override
    public int closeAll() {
        Collection<String> ids = cache.getIds();
        for (String id : ids) {
            if (asroot.getName().equals(id)) {
                continue; // DON'T KILL OUR ROOT SESSION
            }
            try {
                log.info("closeAll called for " + id);
                cache.removeSession(id);
            } catch (RemovedSessionException rse) {
                // Ok. Done for us
            } catch (SessionTimeoutException ste) {
                // Also ok
            } catch (Exception e) {
                log.warn(String.format("Exception thrown on closeAll: %s:%s", e
                        .getClass().getName(), e.getMessage()));
            }
        }
        return ids.size();
    }

    @Override
    public List<String> getUserRoles(String uuid) {
        SessionContext ctx = cache.getSessionContext(uuid);
        if (ctx == null) {
            throw new RemovedSessionException("No session with uuid: " + uuid);
        }
        return ctx.getUserRoles();
    }

    // ~ State attached to session
    // =========================================================================

    @Override
    public Ehcache inMemoryCache(String uuid) {
        return cache.inMemoryCache(uuid);
    }

    @Override
    public Ehcache onDiskCache(String uuid) {
        return cache.onDiskCache(uuid);
    }

    static String INPUT_ENVIRONMENT = "InputEnvironment";
    static String OUTPUT_ENVIRONMENT = "OutputEnvironment";

    @Override
    public Object getInput(String session, String key)
            throws RemovedSessionException {
        return getEnvironmentVariable(session, key, INPUT_ENVIRONMENT);
    }

    @Override
    public Object getOutput(String session, String key)
            throws RemovedSessionException {
        return getEnvironmentVariable(session, key, OUTPUT_ENVIRONMENT);
    }

    @Override
    public Map<String, Object> inputEnvironment(String session) {
        return environment(session, INPUT_ENVIRONMENT);
    }

    @Override
    public Map<String, Object> outputEnvironment(String session) {
        return environment(session, OUTPUT_ENVIRONMENT);
    }

    protected Map<String, Object> environment(String session, String env) {

        // Bump the last accessed time.
        // May throw a session exception
        getReferenceCount(session);

        Map<String, Object> rv = new HashMap<String, Object>();
        Element elt = inMemoryCache(session).get(env);
        if (elt == null) {
            return rv;
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> cv = (Map<String, Object>) elt.getObjectValue();
        if (cv == null) {
            return rv;
        }

        rv.putAll(cv);
        return rv;
    }

    @Override
    public void setInput(String session, String key, Object object)
            throws RemovedSessionException {
        setEnvironmentVariable(session, key, object, INPUT_ENVIRONMENT);
    }

    @Override
    public void setOutput(String session, String key, Object object)
            throws RemovedSessionException {
        setEnvironmentVariable(session, key, object, OUTPUT_ENVIRONMENT);
    }

    private Object getEnvironmentVariable(String session, String key, String env) {
        Ehcache cache = inMemoryCache(session);
        Element elt = cache.get(env);
        if (elt == null) {
            return null;
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) elt.getObjectValue();
        if (map == null) {
            return null;
        } else {
            return map.get(key);
        }
    }

    @SuppressWarnings("unchecked")
    private void setEnvironmentVariable(String session, String key,
            Object object, String env) {
        Ehcache cache = inMemoryCache(session);
        Element elt = cache.get(env);
        Map<String, Object> map;
        if (elt == null) {
            map = new MapMaker().makeMap();
            elt = new Element(env, map);
            cache.put(elt);
        } else {
            map = (Map<String, Object>) elt.getObjectValue();
        }
        if (object == null) {
            map.remove(key);
        } else {
            map.put(key, object);
        }
    }

    // ~ Security methods
    // =========================================================================

    @Override
    public EventContext getEventContext(Principal principal) {
        final SessionContext ctx = cache.getSessionContext(principal.getName());
        if (ctx == null) {
            throw new RemovedSessionException("No session with uuid:"
                    + principal.getName());
        }
        return ctx;
    }

    @Override
    public EventContext reload(final String uuid) {
        final SessionContext ctx = cache.getSessionContext(uuid);
        if (ctx == null) {
            throw new RemovedSessionException("No session with uuid:"
                    + uuid);
        }
        Future<Object> future = executor.submit(Priority.SYSTEM,
                new Callable<Object>(){
                    public Object call() throws Exception {
                        cache.reload(uuid);
                        return null;
                    }});

        // A freshly loaded session should now have been saved
        // as if it had been reloaded during synchronization.
        executor.get(future);
        return cache.getSessionContext(uuid);
    }

    // ~ Notifications
    // =========================================================================

    public String[] notifications(String sessionId) {
        return null;
    }

    /**
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof UserGroupUpdateEvent) {
            cache.updateEvent((UserGroupUpdateEvent) event);
        } else if (event instanceof DestroySessionMessage) {
            sessionProvider.executeCloseSession(((DestroySessionMessage) event).getSessionId());
        }
    }

    // ~ Callbacks (Registering session-based components)
    // =========================================================================

    public void addCallback(String sessionId, SessionCallback cb) {

    }

    public Object getCallbackObject(String sessionId, String name) {
        return null;
    }

    // ~ Misc
    // =========================================================================

    /**
     * Checks the validity of the given {@link Principal}, and in the case of an
     * error attempts to correct the problem by returning a new Principal.
     */
    private Principal validateSessionInputs(
            final ServiceFactory sf,
            final CreationRequest req) {

        final Principal p = req.principal;
        if (p == null || p.getName() == null) {
            throw new ApiUsageException("Null principal name.");
        }

        String type = p.getEventType();
        if (StringUtils.isEmpty(type)) {
            type = "User";
        }

        // Null or bad event type values as well as umasks are handled
        // within the SessionManager and EventHandler.
        String group = p.getGroup();
        if (StringUtils.isEmpty(group)) {
            group = "user";
        }

        // preventing users from logging into "user" group
        if (roles.getUserGroupName().equals(group)) {
            // Throws an exception if no properly defined default group
            ExperimenterGroup g = _getDefaultGroup(sf, p.getName());
            if (g == null) {
                throw new ApiUsageException("Can't find default group for "
                        + p.getName());
            }
            group = g.getName();
        }

        // Now we have a valid, non-"user" group, we can attempt to check
        // if the current context (e.g. group-sudo) is permitted to create
        // such a session.
        if (req.groupsLed != null) {
            long gid = sf.getAdminService().lookupGroup(group).getId();
            if (!req.groupsLed.contains(gid)) {
                throw new SecurityViolation(String.format(
                        "Group sudo is not permitted for group %s (gid=%s)",
                        group, gid));
            }
        }

        // Also checking event type. Throws if missing (and at least a NPE)
        type = sf.getTypesService().getEnumeration(EventType.class, type)
                .getValue();

        return new Principal(p.getName(), group, type);
    }

    private void parseAndSetDefaultType(String type, Session session) {
        String _type = (type == null) ? "User" : type;
        session.setDefaultEventType(_type);
    }

    /**
     * For the moment, user agent is nullable meaning that the only way to unset
     * a set value is by passing in null, so this is allowed here. This implies
     * that the best way to keep userAgent from being set to null is to always
     * return to ISession.updateSession() a session value which was originally
     * retrieved.
     */
    private void parseAndSetUserAgent(String userAgent, Session session) {
        session.setUserAgent(userAgent);
    }

    private void parseAndSetTimeouts(Long timeToLive, Long timeToIdle,
            Session session, boolean trusted) {

        if (timeToLive != null) {
            if (trusted) {
                session.setTimeToLive(timeToLive);
            } else {
                // Let users set a value within limit but if the value is 0,
                // then the maximum must also be 0
                if (maxUserTimeToLive != 0
                    && (timeToLive > maxUserTimeToLive || timeToLive == 0)) {
                    throw new SecurityViolation(
                            "Cannot modify timeToLive beyond maximum: "
                            + maxUserTimeToLive);
                }
                session.setTimeToLive(timeToLive);
            }
        }

        // As above
        if (timeToIdle != null) {
            if (trusted) {
                session.setTimeToIdle(timeToIdle);
            } else {
                // Let users set a value within limit but if the value is 0,
                // then the maximum must also be 0
                if (maxUserTimeToLive != 0
                    && (timeToIdle > maxUserTimeToIdle || timeToIdle == 0)) {
                    throw new SecurityViolation(
                            "Cannot modify timeToIdle beyond maximum: "
                            + maxUserTimeToIdle);
                }
                session.setTimeToIdle(timeToIdle);
            }
        }
    }

    public Session copy(Session source) {
        if (source == null) {
            throw new ApiUsageException("Source may not be null.");
        }

        Session target;
        if (source instanceof Share) {
            target = newShare();
        } else {
            target = new Session();
        }

        target.setId(source.getId());
        target.setClosed(source.getClosed());
        target.setDefaultEventType(source.getDefaultEventType());
        target.getDetails().shallowCopy(source.getDetails());
        target.setMessage(source.getMessage());
        target.setNode(source.getNode());
        target.setStarted(source.getStarted());
        target.setTimeToIdle(source.getTimeToIdle());
        target.setTimeToLive(source.getTimeToLive());
        target.setUserAgent(source.getUserAgent());
        target.setUuid(source.getUuid());

        if (target instanceof Share) {
            Share to = (Share) target;
            Share from = (Share) source;
            to.setItemCount(from.getItemCount());
            to.setActive(from.getActive());
            to.setGroup(from.getGroup());
            to.setData(from.getData());
        }

        return target;
    }

    // StaleCacheListener
    // =========================================================================

    /**
     */
    public void prepareReload() {
        // Noop
    }

    /**
     * Will be called in a synchronized block by {@link SessionCache} in order
     * to allow for an update.
     */
    @SuppressWarnings({"rawtypes" })
    @Override
    public SessionContext reload(final SessionContext ctx) {
        List list = executor.execute(asroot, new Executor.SimpleWork<List<Object>>(
                this, "reload", ctx.getSession().getUuid()) {
            @Transactional(readOnly = true)
            public List<Object> doWork(org.hibernate.Session session,
                    ServiceFactory sf) {
                /* user and group names may change while the session is open */
                final LocalAdmin admin = (LocalAdmin) sf.getAdminService();
                final Experimenter exp = admin.userProxy(ctx.getCurrentUserId());
                final ExperimenterGroup grp = admin.groupProxy(ctx.getCurrentGroupId());
                final Principal p = new Principal(exp.getOdeName(), grp.getName(), ctx.getCurrentEventType());
                return executeSessionContextLookup(sf, p, exp, grp, ctx.getSession());
            }
        });
        if (list == null) {
            return null;
        }
        return createSessionContext(list, ctx);
    }

    // Executor methods
    // =========================================================================

    @SuppressWarnings("unchecked")
    private List<Object[]> executeProjection(final String projection, final Parameters parameters) {
        return executor.execute(asroot,
                new Executor.SimpleWork<List<Object[]>>(this, "executeProjection", projection) {
                    @Transactional(readOnly = true)
                    public List<Object[]> doWork(org.hibernate.Session session, ServiceFactory sf) {
                        return sf.getQueryService().projection(projection, parameters);
                    }
        });
    }

    @Override
    public boolean executePasswordCheck(final String name,
            final String credentials) {

        if (cache.getIds().contains(credentials)) {
            return true;
        }
        return executeCheckPassword(new Principal(name), credentials);
    }

    private boolean executeCheckPassword(final Principal _principal,
            final String credentials) {

        Boolean ok = executeCheckPasswordRO(_principal, credentials);
        if (ok == null) {
            ok = executeCheckPasswordRW(_principal, credentials);
        }
        return ok;
    }

    private Boolean executeCheckPasswordRO(final Principal _principal,
            final String credentials) {
        return executor.execute(asroot, new Executor.SimpleWork<Boolean>(this,
                "executeCheckPasswordRO", _principal) {
            @Transactional(readOnly = true)
            public Boolean doWork(org.hibernate.Session session,
                    ServiceFactory sf) {
                try {
                    return ((LocalAdmin) sf.getAdminService()).checkPassword(
                            _principal.getName(), credentials, true);
                } catch (Exception e) {
                    // thrown if ldap is trying to create a user;
                    // primarily a performance optimization to prevent
                    // creating an event, etc. for all the password
                    // checks which will *not* try to create a user.
                    return null;
                }
            }
        });
    }

    private Boolean executeCheckPasswordRW(final Principal _principal,
            final String credentials) {
        return executor.execute(asroot, new Executor.SimpleWork<Boolean>(this,
                "executeCheckPasswordRW", _principal) {
            @Transactional(readOnly = false)
            public Boolean doWork(org.hibernate.Session session,
                    ServiceFactory sf) {
                return ((LocalAdmin) sf.getAdminService()).checkPassword(
                        _principal.getName(), credentials, false);
            }
        });
    }

    @Override
    public ode.model.IObject setSecurityContext(Principal principal, IObject obj) {
        final Long id = obj == null ? null : obj.getId();
        if (id == null) {
            throw new ApiUsageException("Security context must be managed!");
        }

        final SessionContext sc = cache.getSessionContext(principal.getName());

        TextAnnotation ta = null;
        for (Annotation a : sc.getSession().linkedAnnotationList()) {
            if (a instanceof TextAnnotation) {
                if (roles.isRootUser(a.getDetails().getOwner())) {
                    if (GROUP_SUDO_NS.equals(a.getNs())) {
                        ta = (TextAnnotation) a;
                    }
                }
            }
            if (ta != null) {
                String[] groupIds = ta.getTextValue().split(",");
                throw new SecurityViolation("Group-sudo session cannot change context!");
            }
        }

        final long activeMethods = sc.stats().methodCount();

        if (activeMethods != 0) {
            throw new SecurityViolation(activeMethods + " methods active. Aborting!");
        }

        final Long shareId = sc.getCurrentShareId();
        final Long groupId = sc.getCurrentGroupId();
        ode.model.IObject prevCtx = null;

        if (shareId != null) {
            prevCtx = new Share(shareId, false);
        } else {
            prevCtx = new ExperimenterGroup(groupId, false);
        }

        ChangeSecurityContextEvent csce = new ChangeSecurityContextEvent(
                    this, principal.getName(), prevCtx, obj);

        try {
            this.context.publishMessage(csce);
            csce.throwIfCancelled();
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                InternalException ie =
                    new InternalException("Failed to set call publishMessage");
                ie.initCause(e);
                throw ie;
            }
        }


        if (obj instanceof ExperimenterGroup) {
            setGroupSecurityContext(principal, id);
        } else if (obj instanceof Share) {
            setShareSecurityContext(principal, id);
        } else {
            throw new ApiUsageException("Unknown security context:" + obj);
        }

        return prevCtx;
    }

    private void setGroupSecurityContext(final Principal principal, final Long id) {
        final EventContext ec = getEventContext(principal);
        final ExperimenterGroup[] group = new ExperimenterGroup[1];

        final Session s = executor.execute(principal,
                new Executor.SimpleWork<Session>(this, "setGroupSecurityContext", id) {
                    @Transactional(readOnly = true)
                    public Session doWork(org.hibernate.Session session, ServiceFactory sf) {

                        if (ec.getCurrentShareId() != null) {
                            sf.getShareService().deactivate();
                        }

                        SessionContext sc =
                            cache.getSessionContext(principal.getName());
                        Session s = sc.getSession();

                        // Store old value for rollback
                        if (!sc.isCurrentUserAdmin() &&
                                id >= 0 &&
                                !sc.getMemberOfGroupsList().contains(id)) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("User ");
                            sb.append(sc.getCurrentUserId());
                            sb.append(" is not a member of group ");
                            sb.append(id);
                            throw new SecurityViolation(sb.toString());
                        }
                        group[0] = s.getDetails().getGroup();
                        s.getDetails().setGroup(sf.getAdminService().getGroup(id));
                        return s;
                    }
        });

        // This could also be achieved by filtering out the "check group"
        // logic from BasicSecuritySystem.
        executor.execute(principal, new Executor.SimpleWork<EventContext>(this, "checkGroupSecurityContext", id) {
            @Transactional(readOnly = true)
            public EventContext doWork(org.hibernate.Session session,
                                       ServiceFactory sf) {
                // pre-emptive check
                try {
                    sf.getAdminService().getEventContext();
                } catch (RuntimeException re) {
                    s.getDetails().setGroup(group[0]);
                    throw re;
                }
                return null;
            }

        });



    }

    private void setShareSecurityContext(final Principal principal, final Long id) {
        executor.execute(principal,
                new Executor.SimpleWork<Void>(this, "setShareSecurityContext", id) {
                    @Transactional(readOnly = true)
                    public Void doWork(org.hibernate.Session session, ServiceFactory sf) {
                        // ShareBean does the pre-emptive check
                        sf.getShareService().activate(id);
                        return null;
                    }
        });
    }

    // ~ Non-executor helpers
    // =========================================================================

    /**
     * To prevent having the transaction rolled back, this method returns null
     * rather than throw an exception.
     */
    private ExperimenterGroup _getDefaultGroup(ServiceFactory sf, String name) {
        LocalAdmin admin = (LocalAdmin) sf.getAdminService();
        try {
            Experimenter exp = admin.userProxy(name);
            ExperimenterGroup grp = admin.getDefaultGroup(exp.getId());
            return grp;
        } catch (Exception e) {
            log.warn("Exception while running " + "executeDefaultGroup", e);
            return null;
        }
    }

    /**
     * Looks up a user id by principal. If the name of the principal is actually
     * a removed user session, then a {@link RemovedSessionException} is thrown.
     */
    private long executeLookupUser(ServiceFactory sf, Principal p) {
        List<Object[]> rv = sf.getQueryService().projection("select e.id from Experimenter e where e.odeName = :name",
                new Parameters().addString("name", p.getName()));
        if (rv.size() == 0) {
            throw new RemovedSessionException("Cannot find a user with name "
                    + p.getName());
        }
        return (Long) rv.get(0)[0];
    }

    /**
     * Check if the given user configuration indicates that any of the named administrator privileges are restricted for that user.
     * @param userConfig a user's configuration, see {@link Experimenter#getConfig()}, may be {@code null}
     * @param privilegeNames the names of some administrator privileges, see {@link AdminPrivilege#getValue()}
     * @return if any of the named privileges are restricted
     */
    private boolean isAnyPrivilegeRestricted(List<NamedValue> userConfig, String... privilegeNames) {
        if (userConfig == null) {
            return false;
        }
        final Set<String> configNamesRestricted = new HashSet<>();
        for (final NamedValue configProperty : userConfig) {
            final String configName = configProperty.getName();
            final String configValue = configProperty.getValue();
            if (!Boolean.parseBoolean(configValue)) {
                configNamesRestricted.add(configName);
            }
        }
        for (final String privilegeName : privilegeNames) {
            final AdminPrivilege privilege = adminPrivileges.getPrivilege(privilegeName);
            final String privilegeConfigName = adminPrivileges.getConfigNameForPrivilege(privilege);
            if (configNamesRestricted.contains(privilegeConfigName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a List of state for creating a new {@link SessionContext}. If an
     * exception is thrown, return nulls since throwing an exception within the
     * Work will set our transaction to rollback only.
     */
    private List<Object> executeSessionContextLookup(ServiceFactory sf,
            Principal principal, Session session) {
        final LocalAdmin admin = (LocalAdmin) sf.getAdminService();
        final Experimenter exp = admin.userProxy(principal.getName());
        final ExperimenterGroup grp = admin.groupProxy(principal.getGroup());
        return executeSessionContextLookup(sf, principal, exp, grp, session);
    }

    /**
     * Returns a List of state for creating a new {@link SessionContext}. If an
     * exception is thrown, return nulls since throwing an exception within the
     * Work will set our transaction to rollback only.
     */
    private List<Object> executeSessionContextLookup(ServiceFactory sf,
            Principal principal, Experimenter exp, ExperimenterGroup grp, Session session) {
        try {
            List<Object> list = new ArrayList<Object>();
            LocalAdmin admin = (LocalAdmin) sf.getAdminService();
            final List<Long> memberOfGroupsIds = admin.getMemberOfGroupIds(exp);
            final List<Long> leaderOfGroupsIds = admin.getLeaderOfGroupIds(exp);
            final List<String> userRoles = admin.getUserRoles(exp);
            final Session reloaded = sessionProvider.findSessionById(session.getId(), sf);
            final Experimenter sudoer = reloaded.getSudoer();
            boolean hasAdminPrivileges = memberOfGroupsIds.contains(roles.getSystemGroupId());
            if (sudoer != null) {
                final List<Long> leaderOfGroupsIdsSudoer = admin.getLeaderOfGroupIds(sudoer);
                final List<Long> memberOfGroupsIdsSudoer = admin.getMemberOfGroupIds(sudoer);
                final boolean hasSudoPrivilegeSudoer;
                if (memberOfGroupsIdsSudoer.contains(roles.getSystemGroupId())) {
                    hasSudoPrivilegeSudoer = !isAnyPrivilegeRestricted(sudoer.getConfig(), AdminPrivilege.VALUE_SUDO);
                } else {
                    hasSudoPrivilegeSudoer = false;
                    hasAdminPrivileges = false;
                }
                if (!hasSudoPrivilegeSudoer) {
                    /* Reduce group permissions to reflect a group-owner sudo. */
                    leaderOfGroupsIds.retainAll(leaderOfGroupsIdsSudoer);
                    memberOfGroupsIds.retainAll(memberOfGroupsIdsSudoer);
                }
            }
            list.add(exp);
            list.add(grp);
            list.add(hasAdminPrivileges ? adminPrivileges.getSessionPrivileges(reloaded) : Collections.emptySet());
            list.add(memberOfGroupsIds);
            list.add(leaderOfGroupsIds);
            list.add(userRoles);
            list.add(principal);
            list.add(reloaded);
            return list;
        } catch (Exception e) {
            log.info("No info for " + principal.getName(), e);
            return null;
        }
    }

    private Share newShare() {
        Share share = new Share();
        share.putAt("#2733", "ALLOW");
        return share;
    }
}
