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

import java.util.HashMap;
import java.util.Map;

import ode.api.local.LocalQuery;
import ode.conditions.InternalException;
import ode.model.meta.Experimenter;
import ode.model.meta.Node;
import ode.model.meta.Session;
import ode.model.meta.Share;
import ode.parameters.Parameters;
import ode.security.NodeProvider;
import ode.services.util.Executor;
import ode.services.util.ReadOnlyStatus;
import ode.system.Roles;
import ode.system.ServiceFactory;
import ode.util.SqlAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

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
public class SessionProviderInDb implements SessionProvider, ReadOnlyStatus.IsAware {

    private final static Logger log = LoggerFactory.getLogger(SessionProviderInDb.class);

    private final Roles roles;

    private final NodeProvider nodeProvider;

    private final Executor executor;

    private final SqlAction sqlAction;

    public SessionProviderInDb(Roles roles, NodeProvider nodeProvider, Executor executor, SqlAction sqlAction) {
        this.roles = roles;
        this.nodeProvider = nodeProvider;
        this.executor = executor;
        this.sqlAction = sqlAction;
    }

    // Executor methods
    // =========================================================================

    @Override
    public Session executeUpdate(ServiceFactory sf, Session session, String uuid,
            long userId, Long sudoerId) {
        Node node = nodeProvider.getManagerByUuid(uuid, sf);
        if (node == null) {
            node = new Node(0L, false); // Using default node.
        }
        session.setNode(node);
        session.setOwner(new Experimenter(userId, false));
        if (sudoerId == null) {
            session.setSudoer(null);
        } else {
            session.setSudoer(new Experimenter(sudoerId, false));
        }
        final String userIP = session.getUserIP();  // saving "session" unloads it
        Session rv = sf.getUpdateService().saveAndReturnObject(session);
        if (userIP != null) {
            sqlAction.updateSessionUserIP(rv.getId(), userIP);
        }
        rv.putAt("#2733", session.retrieve("#2733"));
        return rv;
    }

    @Override
    public void executeCloseSession(final String uuid) {
        executor.executeSql(new Executor.SimpleSqlWork(this,
                        "executeCloseSession") {
            @Transactional(readOnly = false)
            public Object doWork(SqlAction sql) {
                try {
                    final int count = sql.closeSessions(uuid);
                    if (count == 0) {
                        log.warn("No session updated on closeSession: {}", uuid);
                    } else {
                        log.debug("Session.closed set to now() for {}", uuid);
                    }
                } catch (Exception e) {
                    log.error("FAILED TO CLOSE SESSION IN DATABASE: {}", uuid, e);
                }
                return null;
            }
        });
    }

    @Override
    public Session executeInternalSession(final String uuid, final Session session) {
        final Long sessionId = executeNextSessionId();
        return (Session) executor
                .executeSql(new Executor.SimpleSqlWork(this,
                        "executeInternalSession") {
                    @Transactional(readOnly = false)
                    public Object doWork(SqlAction sql) {

                        // Set the owner and node specially for an internal sess
                        final long nodeId = nodeProvider.getManagerIdByUuid(uuid, sql);

                        // SQL defined in data.vm for creating original session
                        // (id,permissions,timetoidle,timetolive,started,closed,defaulteventtype,uuid,owner,node)
                        // select nextval('seq_session'),-35,
                        // 0,0,now(),now(),'rw----','PREVIOUSITEMS','1111',0,0;
                        Map<String, Object> params = new HashMap<String, Object>();
                        params.put("sid", sessionId);
                        params.put("ttl", session.getTimeToLive());
                        params.put("tti", session.getTimeToIdle());
                        params.put("start", session.getStarted());
                        params.put("type", session.getDefaultEventType());
                        params.put("uuid", session.getUuid());
                        params.put("node", nodeId);
                        params.put("owner", roles.getRootId());
                        params.put("agent", session.getUserAgent());
                        params.put("ip", session.getUserIP());
                        int count = sql.insertSession(params);
                        if (count == 0) {
                            throw new InternalException(
                                    "Failed to insert new session: "
                                            + session.getUuid());
                        }
                        Long id = sql.sessionId(session.getUuid());
                        session.setNode(new Node(nodeId, false));
                        session.setOwner(new Experimenter(roles.getRootId(), false));
                        session.setId(id);
                        return session;
                    }
                });
    }

    @Override
    public long executeNextSessionId() {
        return (Long) executor
                .executeSql(new Executor.SimpleSqlWork(this,
                        "executeNextSessionId") {
                    @Transactional(readOnly = false)
                    public Object doWork(SqlAction sql) {
                        return sql.nextSessionId();
                    }
                });
    }

    @Override
    public Session findSessionById(long id, org.hibernate.Session session) {
        return (Session) session.get(Session.class, id);
    }

    @Override
    public Session findSessionById(long id, ServiceFactory sf) {
        final LocalQuery iQuery = (LocalQuery) sf.getQueryService();
        final String sessionClass = iQuery.find(Share.class, id) == null ? "Session" : "Share";
        return (Session) iQuery.findByQuery(
                        "select s from " + sessionClass + " s "
                        + "left outer join fetch s.sudoer "
                        + "left outer join fetch s.annotationLinks l "
                        + "left outer join fetch l.child a where s.id = :id",
                        new Parameters().addId(id).cache());
    }

    @Override
    public Long findSessionIdByUuid(final String uuid, ServiceFactory sf) {
        final Session session = sf.getQueryService().findByString(Session.class, "uuid", uuid);
        return session == null ? null : session.getId();
    }

    @Override
    public Long findSessionIdByUuid(final String uuid) {
        return (Long) executor.executeSql(new Executor.SimpleSqlWork(this,
                "getSessionId") {
                    @Override
                    @Transactional(readOnly = true)
                    public Object doWork(SqlAction sql) {
                        try {
                            return sql.sessionId(uuid);
                        } catch (EmptyResultDataAccessException erdae) {
                            return null;
                        }
                    }
        });
    }

    @Override
    public boolean isReadOnly(ReadOnlyStatus readOnly) {
        return readOnly.isReadOnlyDb();
    }
}
