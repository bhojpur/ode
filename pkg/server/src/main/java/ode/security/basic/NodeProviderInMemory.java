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

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import ode.model.meta.Node;
import ode.model.meta.Session;
import ode.security.NodeProvider;
import ode.services.util.ReadOnlyStatus;
import ode.system.Principal;
import ode.system.ServiceFactory;

/**
 * Provider for {@link Node} objects which is responsible for persisting and
 * populating such entities.
 */
public class NodeProviderInMemory implements NodeProvider, ReadOnlyStatus.IsAware {

    /**
     * UUID for this cluster node. Used to uniquely identify the session manager
     * in this server instance. Most likely used in common with internal server
     * components. <em>Must</em> specify a valid session id.
     */
    public final String uuid;

    private final Principal principal;

    private final Map<String, Node> currentNodes = new ConcurrentHashMap<>();

    private final AtomicLong currentNodeId = new AtomicLong(-1L);

    public NodeProviderInMemory(String uuid) {
        this.uuid = uuid;
        this.principal = new Principal(uuid, "system", "Internal");
    }

    /* (non-Javadoc)
     */
    public Principal principal() {
        return this.principal;
    }

    // Database interactions
    // =========================================================================

    /* (non-Javadoc)
     * @see ode.security.NodeProvider#getManagerIdByUuid(java.lang.String, ode.util.SqlAction)
     */
    public long getManagerIdByUuid(String managerUuid, ode.util.SqlAction sql) {
        final Node manager = getManagerByUuid(managerUuid, null);
        return manager == null ? 0 : manager.getId();
    };

    /* (non-Javadoc)
     * @see ode.security.NodeProvider#getManagerByUuid(java.lang.String, ode.system.ServiceFactory)
     */
    public Node getManagerByUuid(final String managerUuid, ServiceFactory sf) {
        return currentNodes.get(managerUuid);
    };

    /* (non-Javadoc)
     */
    public Set<String> getManagerList(final boolean onlyActive) {
        Set<String> nodeIds = new HashSet<String>();
        for (final Node node : currentNodes.values()) {
            if (onlyActive && node.getDown() != null) {
                continue; // Remove inactive managers
            }
            nodeIds.add(node.getUuid());
        }
        return nodeIds;
    }

    /**
     * Assumes that the given manager is no longer available and will clean up
     * all in-memory sessions.
     */
    public int closeSessionsForManager(final String managerUuid) {
        // Implementation of the following SQL query in memory:
        //
        // update session set closed = now()
        //     where closed is null and node in
        //         (select id from Node where uuid = ?)
        final Node node = currentNodes.get(managerUuid);
        int modificationCount = 0;
        if (node != null) {
            Iterator<Session> i = node.iterateSessions();
            while (i.hasNext()) {
                Session session = i.next();
                if (session.getClosed() == null) {
                    session.setClosed(
                            new Timestamp(System.currentTimeMillis()));
                    modificationCount++;
                }
            }
        }
        return modificationCount;
    }

    /* (non-Javadoc)
     */
    public void setManagerDown(final String managerUuid) {
        // Implement of the following SQL query in memory:
        //
        // update Node set down = now() where uuid = ?
        final Node node = currentNodes.get(managerUuid);
        if (node != null) {
            node.setDown(new Timestamp(System.currentTimeMillis()));
        }
    }

    /* (non-Javadoc)
     */
    public Node addManager(String managerUuid, String proxyString) {
        final Node node = new Node();
        node.setId(currentNodeId.getAndDecrement());
        node.setConn(proxyString);
        node.setUuid(managerUuid);
        node.setUp(new Timestamp(System.currentTimeMillis()));
        currentNodes.put(managerUuid, node);
        return node;
    }

    @Override
    public boolean isReadOnly(ReadOnlyStatus readOnly) {
        return false;
    }
}