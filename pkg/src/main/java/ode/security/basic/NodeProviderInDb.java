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
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import ode.model.meta.Node;
import ode.parameters.Filter;
import ode.parameters.Parameters;
import ode.security.NodeProvider;
import ode.services.util.Executor;
import ode.services.util.ReadOnlyStatus;
import ode.system.Principal;
import ode.system.ServiceFactory;

/**
 * Provider for {@link Node} objects which is responsible for persisting and
 * populating such entities.
 */
public class NodeProviderInDb implements NodeProvider, ReadOnlyStatus.IsAware {

    /**
     * UUID for this cluster node. Used to uniquely identify the session manager
     * in this server instance. Most likely used in common with internal server
     * components. <em>Must</em> specify a valid session id.
     */
    public final String uuid;

    private final Executor executor;

    private final Principal principal;

    public NodeProviderInDb(String uuid, Executor executor) {
        this.uuid = uuid;
        this.executor = executor;
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
        try {
            return sql.nodeId(managerUuid);
        } catch (EmptyResultDataAccessException erdae) {
            return 0;
        }
    };

    /* (non-Javadoc)
     * @see ode.security.NodeProvider#getManagerByUuid(java.lang.String, ode.system.ServiceFactory)
     */
    public Node getManagerByUuid(final String managerUuid, ServiceFactory sf) {
        Parameters p = new Parameters();
        p.addString("uuid", managerUuid).setFilter(
                new Filter().page(0, 1));
        return (Node) sf.getQueryService().findByQuery(
                "select n from Node n where uuid = :uuid", p);
    };

    /* (non-Javadoc)
     */
    @SuppressWarnings("unchecked")
    public Set<String> getManagerList(final boolean onlyActive) {
        return (Set<String>) executor.execute(principal,
                new Executor.SimpleWork(this, "getManagerList") {
                    @Transactional(readOnly = true)
                    public Object doWork(Session session, ServiceFactory sf) {
                        List<Node> nodes = sf.getQueryService().findAll(
                                Node.class, null);
                        Set<String> nodeIds = new HashSet<String>();
                        for (Node node : nodes) {
                            if (onlyActive && node.getDown() != null) {
                                continue; // Remove none active managers
                            }
                            nodeIds.add(node.getUuid());
                        }
                        return nodeIds;
                    }
                });
    }

    /**
     * Assumes that the given manager is no longer available and so will not
     * attempt to call cache.removeSession() since that requires the session to
     * be in memory. Instead directly modifies the database to set the session
     * to closed.
     */
    public int closeSessionsForManager(final String managerUuid) {
        // First look up the sessions in on transaction
        return (Integer) executor.execute(principal, new Executor.SimpleWork(
                this, "executeUpdate - set closed = now()") {
            @Transactional(readOnly = false)
            public Object doWork(Session session, ServiceFactory sf) {
                return getSqlAction().closeNodeSessions(managerUuid);
            }
        });
    }

    /* (non-Javadoc)
     */
    public void setManagerDown(final String managerUuid) {
        executor.execute(principal, new Executor.SimpleWork(this,
                "setManagerDown") {
            @Transactional(readOnly = false)
            public Object doWork(Session session, ServiceFactory sf) {
                return getSqlAction().closeNode(managerUuid);
            }
        });
    }

    /* (non-Javadoc)
     */
    public Node addManager(String managerUuid, String proxyString) {
        final Node node = new Node();
        node.setConn(proxyString);
        node.setUuid(managerUuid);
        node.setUp(new Timestamp(System.currentTimeMillis()));
        return (Node) executor.execute(principal, new Executor.SimpleWork(this,
                "addManager") {
            @Transactional(readOnly = false)
            public Object doWork(Session session, ServiceFactory sf) {
                return sf.getUpdateService().saveAndReturnObject(node);
            }
        });
    }

    @Override
    public boolean isReadOnly(ReadOnlyStatus readOnly) {
        return readOnly.isReadOnlyDb();
    }
}