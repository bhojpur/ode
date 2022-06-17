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
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;

import ode.api.IQuery;
import ode.model.meta.Experimenter;
import ode.model.meta.Node;
import ode.model.meta.Session;
import ode.security.NodeProvider;
import ode.services.util.Executor;
import ode.services.util.ReadOnlyStatus;
import ode.system.Roles;
import ode.system.ServiceFactory;
import ode.util.SqlAction;

/**
 * In-memory implementation of {@link SessionProviderInDb}.
 */
public class SessionProviderInMemory implements SessionProvider, ReadOnlyStatus.IsAware {

    private final static Logger log =
            LoggerFactory.getLogger(SessionProviderInMemory.class);

    private final Roles roles;

    private final NodeProvider nodeProvider;

    private final Executor executor;

    private final AtomicLong currentSessionId = new AtomicLong(-1L);

    private final Map<String, Session> openSessions = new ConcurrentHashMap<>();
    private final Map<String, Session> closedSessions = CacheBuilder.newBuilder().maximumSize(512).<String, Session>build().asMap();

    public SessionProviderInMemory(Roles roles, NodeProvider nodeProvider, Executor executor) {
        this.roles = roles;
        this.nodeProvider = nodeProvider;
        this.executor = executor;
    }

    @Override
    public Session executeUpdate(ServiceFactory sf, Session session, String uuid,
            long userId, Long sudoerId) {
        final IQuery iQuery = sf.getQueryService();
        Node node = nodeProvider.getManagerByUuid(uuid, sf);
        if (node == null) {
            node = new Node(0L, false); // Using default node.
        }
        if (session.getId() == null) {
            session.setId(executeNextSessionId());
        }
        session.setNode(node);
        session.setOwner(iQuery.get(Experimenter.class, userId));
        if (sudoerId == null) {
            session.setSudoer(null);
        } else {
            session.setSudoer(iQuery.get(Experimenter.class, sudoerId));
        }
        /* put before remove so that the session is never missing altogether */
        if (session.getClosed() == null) {
            openSessions.put(session.getUuid(), session);
            closedSessions.remove(session.getUuid());
        } else {
            closedSessions.put(session.getUuid(), session);
            openSessions.remove(session.getUuid());
        }
        log.debug("Registered Session:{} ({})", session.getId(), session.getUuid());
        /* Match how Hibernate nulls transient properties in persisting the instance. */
        session.setUserIP(null);
        return session;
    }

    @Override
    public long executeNextSessionId() {
        return currentSessionId.getAndDecrement();
    }

    @Override
    public Session executeInternalSession(final String uuid, Session session) {
        Node node = (Node) executor.executeSql(new Executor.SimpleSqlWork(this,
                "executeInternalSession") {
            @Transactional(readOnly = true)
            public Object doWork(SqlAction sql) {
                final Long nodeId = nodeProvider.getManagerIdByUuid(uuid, sql);
                return nodeId == null ? null : new Node(nodeId, false);
            }
        });
        session.setId(executeNextSessionId());
        log.debug("Created session: {}", session);
        log.debug("Setting node: {}", node);
        session.setNode(node);
        session.setOwner(new Experimenter(roles.getRootId(), false));
        openSessions.put(session.getUuid(), session);
        return session;
    }

    @Override
    public void executeCloseSession(String uuid) {
        Session session = openSessions.get(uuid);
        if (session == null) {
            if (closedSessions.containsKey(uuid)) {
                log.debug("attempt to close session {} but is already closed", uuid);
            } else {
                log.warn("attempt to close session {} but is no longer cached", uuid);
            }
        } else {
            session.setClosed(new Timestamp(System.currentTimeMillis()));
            closedSessions.put(session.getUuid(), session);
            openSessions.remove(session.getUuid());
            log.debug("closed session {}", uuid);
        }
    }

    @Override
    public Session findSessionById(long id, org.hibernate.Session session) {
        return findSessionById(id, (ServiceFactory) null);
    }

    @Override
    public Session findSessionById(long id, ServiceFactory sf) {
        final SortedSet<Long> tries = new TreeSet<Long>();
        final Stream<Session> sessionStream = Stream.concat(openSessions.values().stream(), closedSessions.values().stream());
        for (final Session session : (Iterable<Session>) sessionStream::iterator) {
            if (session.getId().equals(id)) {
                return session;
            } else {
                tries.add(session.getId());
            }
        }
        log.info("Requested unknown session ID {}. Found {} other sessions.", id, tries.size());
        log.debug("Found sessions: {}", tries);
        return null;
    }

    @Override
    public Long findSessionIdByUuid(String uuid, ServiceFactory sf) {
        return findSessionIdByUuid(uuid);
    }

    @Override
    public Long findSessionIdByUuid(String uuid) {
        for (final Map<String, Session> sessions : ImmutableList.of(openSessions, closedSessions)) {
            final Session session = sessions.get(uuid);
            if (session != null) {
                return session.getId();
            }
        }
        return null;
    }

    @Override
    public boolean isReadOnly(ReadOnlyStatus readOnly) {
        return false;
    }
}
