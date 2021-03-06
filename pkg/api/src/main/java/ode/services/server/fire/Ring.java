package ode.services.server.fire;

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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ode.security.NodeProvider;
import ode.security.basic.NodeProviderInMemory;
import ode.services.server.fire.Registry;
import ode.services.server.redirect.NullRedirector;
import ode.services.server.redirect.Redirector;
import ode.services.server.util.ServerConfiguration;
import ode.services.scripts.ScriptRepoHelper;
import ode.services.sessions.SessionManager;
import ode.services.util.Executor;
import ode.system.Principal;
import ode.util.SqlAction;
import ode.grid.ClusterNodePrx;
import ode.grid.ClusterNodePrxHelper;
import ode.grid._ClusterNodeDisp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import Glacier2.CannotCreateSessionException;
import Glacier2.SessionPrx;
import Ice.Current;

/**
 * Distributed ring of {@link ServerConfiguration} objects which manages lookups
 * of sessions and other resources from all the Bhojpur ODEs which take part in the
 * cluster. Membership in the {@link Ring} is based on a single token --
 * "ode.instance" -- retrieved from the current context, or if missing, a
 * calculated value which will prevent this instance from taking part in
 * clustering.
 * 
 * The {@link Ring} also listens for
 */
public class Ring extends _ClusterNodeDisp implements Redirector.Context {

    private final static Logger log = LoggerFactory.getLogger(Ring.class);

    /**
     * UUID for this cluster node. Used to uniquely identify the session manager
     * in this server instance. Most likely used in common with internal server
     * components. <em>Must</em> specify a valid session id.
     */
    public final String uuid;

    public final Principal principal;

    private final Executor executor;

    private final Redirector redirector;

    private final ScriptRepoHelper scriptRepoHelper;

    private final NodeProvider nodeProvider;

    private/* final */Ice.Communicator communicator;

    private/* final */ Registry registry;

    /**
     * Standard server adapter which is used for the callback.
     */
    private/* final */Ice.ObjectAdapter adapter;

    /**
     * Direct proxy value to the {@link SessionManager} in this server instance.
     */
    private/* final */String directProxy;

    public Ring(String uuid, Executor executor) {
        this(uuid, executor, new NullRedirector(), null, new NodeProviderInMemory(uuid));
    }

    public Ring(String uuid, Executor executor, Redirector redirector, ScriptRepoHelper scriptRepoHelper,
            NodeProvider nodeProvider) {
        this.uuid = uuid;
        this.executor = executor;
        this.redirector = redirector;
        this.scriptRepoHelper = scriptRepoHelper;
        this.principal = new Principal(uuid, "system", "Internal");
        this.nodeProvider = nodeProvider;
    }

    /**
     * Sets the {@link Registry} for this instance. This is currently done in
     * {@link ServerConfiguration}
     */
    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    // Redirector.Context API
    // =========================================================================

    public String uuid() {
        return this.uuid;
    }

    public Principal principal() {
        return this.principal;
    }

    public Set<String> getManagerList(final boolean onlyActive) {
        return nodeProvider.getManagerList(onlyActive);
    }

    /**
     * Returns the proxy information for the local {@link SessionManager}.
     * 
     * @return See above.
     */
    public String getDirectProxy() {
        return this.directProxy;
    }

    public Ice.Communicator getCommunicator() {
        return this.communicator;
    }

    // Configuration and cluster usage
    // =========================================================================

    /**
     * Typically called from within {@link ServerConfiguration} after the
     * communicator and adapter have been properly setup.
     */
    public void init(Ice.ObjectAdapter adapter, String directProxy) {
        this.adapter = adapter;
        this.communicator = adapter.getCommunicator();
        this.directProxy = directProxy;

        // Before we add our self we check the validity of the cluster.
        Set<String> nodeUuids = checkCluster();
        if (nodeUuids == null) {
            log.warn("No clusters found. Aborting ring initialization");
            return; // EARLY EXIT!
        }
        
        try {
            // Now our checking is done, add ourselves.
            Ice.Identity clusterNode = this.communicator
                    .stringToIdentity("ClusterNode/" + uuid);
            this.adapter.add(this, clusterNode); // OK ADAPTER USAGE
            nodeProvider.addManager(uuid, directProxy);
            registry.addObject(this.adapter.createDirectProxy(clusterNode));
            nodeUuids.add(uuid);
            redirector.chooseNextRedirect(this, nodeUuids);
        } catch (Exception e) {
            throw new RuntimeException("Cannot register self as node: ", e);
        }
        if (scriptRepoHelper != null) {
            /* requires self to be registered as node */
            scriptRepoHelper.checkForScriptUpdates();
        }
    }

    /**
     * Method called during initialization to get all the active uuids within
     * the cluster, and remove any dead nodes. May return null if lookup fails.
     */
    public Set<String> checkCluster() {
        
        log.info("Checking cluster");
        ClusterNodePrx[] nodes = registry.lookupClusterNodes();
        if (nodes == null) {
            log.error("Could not lookup nodes. Skipping initialization...");
            return null; // EARLY EXIT
        }

        // Contact each of the cluster. During init this, instance has not been
        // added, so this will not cause a callback. On clusterCheckTrigger,
        // however, it might.
        Set<String> nodeUuids = new HashSet<String>();
        for (int i = 0; i < nodes.length; i++) {
            ClusterNodePrx prx = nodes[i];
            if (prx == null) {
                log.warn("Null proxy found");
                continue;
            } else {
                try {
                    nodeUuids.add(nodes[i].getNodeUuid());
                } catch (Exception e) {
                    log.warn("Error getting uuid from node " + nodes[i]
                            + " -- removing.");
                    registry.removeObjectSafely(prx.ice_getIdentity());
                }
            }
        }
        log.info("Got " + nodeUuids.size() + " cluster uuids : " + nodeUuids);

        // Now any stale nodes (ones not found in the registry) are forcibly
        // removed, since it is assumed they didn't shut down cleanly.
        assertNodes(nodeUuids);

        return nodeUuids;
    }

    public void destroy() {
        try {
            Ice.Identity id = this.communicator.stringToIdentity("ClusterNode/"
                    + uuid);
            registry.removeObjectSafely(id);
            redirector.handleRingShutdown(this, this.uuid);
            final int count = nodeProvider.closeSessionsForManager(uuid);
            log.info("Removed " + count + " entries for " + uuid);
            log.info("Disconnected from Bhojpur ODE.cluster");
        } catch (Exception e) {
            log.error("Error stopping ring " + this, e);
        } finally {
            ClusterNodePrx[] nodes = null;
            try {
                // TODO this would be better served with a storm message!
                nodes = registry.lookupClusterNodes();
                if (nodes != null) {
                    for (ClusterNodePrx clusterNodePrx : nodes) {
                        try {
                            clusterNodePrx = ClusterNodePrxHelper
                                    .uncheckedCast(clusterNodePrx.ice_oneway());
                            clusterNodePrx.down(this.uuid);
                        } catch (Exception e) {
                            String msg = "Error signaling down to "
                                    + clusterNodePrx;
                            log.warn(msg, e);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Error signaling down to: "
                        + Arrays.deepToString(nodes), e);
            }
        }
    }

    // Cluster Node API
    // =========================================================================

    public String getNodeUuid(Current __current) {
        return this.uuid;
    }

    /**
     * Called when any node goes down. First we try to remove any redirect for
     * that instance. Then we try to install ourselves.
     */
    public void down(String downUuid, Current __current) {
        redirector.handleRingShutdown(this, downUuid);
    }

    // Local usage
    // =========================================================================

    /**
     * Currently only returns false since if the regular password check
     * performed by {@link ode.services.sessions.SessionManager} cannot find the
     * session, then the cluster has no extra information.
     */
    public boolean checkPassword(final String userId) {
        return (Boolean) executor.executeSql(new Executor.SimpleSqlWork(this,
                        "checkPassword") {
                    @Transactional(readOnly = true)
                    public Object doWork(SqlAction sql) {
                        return sql.activeSession(userId);
                    }
                });
    }

    /**
     * Delegates to the {@link #redirector} strategy configured for this
     * instance.
     */
    public SessionPrx getProxyOrNull(String userId,
            Glacier2.SessionControlPrx control, Ice.Current current)
            throws CannotCreateSessionException {
        return redirector.getProxyOrNull(this, userId, control, current);
    }

    public Set<String> knownManagers() {
        return getManagerList(true);
    }

    public void assertNodes(Set<String> nodeUuids) {
        Set<String> managers = knownManagers();

        for (String manager : managers) {
            if (!nodeUuids.contains(manager)) {
                // Also verify this is not ourself, since
                // possibly we haven't finished registration
                // yet
                if (!uuid.equals(manager)) {
                    // And also don't try to purge the original manager.
                    if (!"000000000000000000000000000000000000".equals(manager)) {
                        purgeNode(manager);
                    }
                }
            }
        }
    }

    protected void purgeNode(String manager) {
        log.info("Purging node: " + manager);
        try {
            Ice.Identity id = this.communicator.stringToIdentity("ClusterNode/"
                    + manager);
            registry.removeObjectSafely(id);
            final int count = nodeProvider.closeSessionsForManager(manager);
            log.info("Removed " + count + " entries with value " + manager);
            nodeProvider.setManagerDown(manager);
            log.info("Removed manager: " + manager);
            redirector.handleRingShutdown(this, manager);
            log.info("handleRingShutdown: " + manager);
        } catch (Exception e) {
            log.error("Failed to purge node " + manager, e);
        }
    }
}