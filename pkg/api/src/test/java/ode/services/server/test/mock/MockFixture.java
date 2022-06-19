package ode.services.server.test.mock;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sf.ehcache.Cache;

import ode.model.meta.Session;
import ode.security.SecuritySystem;
import ode.security.basic.NodeProviderInMemory;
import ode.services.server.fire.Ring;
import ode.services.server.fire.SessionManagerI;
import ode.services.server.fire.TopicManager;
import ode.services.server.test.utests.TestCache;
import ode.services.server.util.ServerConfiguration;
import ode.services.roi.RoiTypes;
import ode.services.scheduler.SchedulerFactoryBean;
import ode.services.sessions.SessionManager;
import ode.services.sessions.SessionProvider;
import ode.services.sessions.SessionProviderInMemory;
import ode.services.util.Executor;
import ode.system.OdeContext;
import ode.system.Roles;
import ode.util.SqlAction;
import ode.rtypes;
import ode.api.ServiceFactoryPrx;
import ode.api.ServiceFactoryPrxHelper;
import ode.constants.CLIENTUUID;
import ode.util.ModelObjectFactoryRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

import Glacier2.AMD_Router_createSession;
import Glacier2.AMD_Router_createSessionFromSecureConnection;
import Glacier2.CannotCreateSessionException;
import Glacier2.PermissionDeniedException;
import Glacier2.SessionNotExistException;
import Glacier2.SessionPrx;
import Ice.Current;
import Ice.InitializationData;
import Ice.ObjectPrx;

public class MockFixture {

    public final MockObjectTestCase test;

    public final SchedulerFactoryBean scheduler;
    public final ServerConfiguration server;
    public final SessionManagerI sm;
    public final SessionManager mgr;
    public final SecuritySystem ss;
    public final OdeContext ctx;
    public final Executor ex;
    public final Ring ring;
    public final String router;
    public final SqlAction sql;

    public static OdeContext basicContext() {
        return new OdeContext(new String[] {
                "classpath:ode/test.xml",
                "classpath:ode/config.xml",
                "classpath:ode/services/datalayer.xml",
                "classpath:ode/services/server-servantDefinitions.xml",
                "classpath:ode/services/server-graph-rules.xml",
                "classpath:ode/services/throttling/throttling.xml",
                "classpath:ode/services/messaging.xml",
                // Following 2 required by GeomTool deps.
                "classpath:ode/services/service-ode.io.nio.PixelsService.xml",
                "classpath:ode/services/service-ode.io.nio.OriginalFilesService.xml"});
    }

    public MockFixture(MockObjectTestCase test) throws Exception {
        this(test, basicContext());
    }

    /**
     * The string arguments sets the {@link SessionManagerI#self} field, which
     * is used in clustering.
     */
    public MockFixture(MockObjectTestCase test, String name) throws Exception {
        this(test, basicContext());
    }

    public MockFixture(MockObjectTestCase test, OdeContext ctx) {

        this.test = test;
        this.ctx = ctx;
        this.ring = (Ring) ctx.getBean("ring");
        this.ex = (Executor) ctx.getBean("executor");
        this.ss = (SecuritySystem) ctx.getBean("securitySystem");
        this.mgr = (SessionManager) ctx.getBean("sessionManager");
        this.sql = (SqlAction) ctx.getBean("simpleSqlAction");

        // --------------------------------------------

        InitializationData id = new InitializationData();
        id.properties = Ice.Util.createProperties();

        //
        // The follow properties are necessary for Gateway
        //

        // Collocation isn't working (but should)
        id.properties.setProperty("Ice.Default.CollocationOptimized", "0");
        // Gateway calls back on the SF and so needs another thread or
        // blocks.
        id.properties.setProperty("Ice.ThreadPool.Client.Size", "2");
        id.properties.setProperty("Ice.ThreadPool.Client.SizeMax", "50");
        id.properties.setProperty("Ice.ThreadPool.Server.Size", "10");
        id.properties.setProperty("Ice.ThreadPool.Server.SizeMax", "100");
        // For testing large calls
        id.properties.setProperty("Ice.MessageSizeMax", "4096");
        // Basic configuration
        id.properties.setProperty("ServerAdapter.Endpoints",
                "default -h 127.0.0.1");
        // Cluster configuration from etc/internal.cfg
        id.properties.setProperty("Cluster.Endpoints",
                "udp -h 224.0.0.5 -p 10000");
        id.properties.setProperty("ClusterProxy",
                "Cluster:udp -h 224.0.0.5 -p 10000");

        /*
        Node node = new Node();
        this.mock("executorMock").expects(test.once()).method("execute").will(test.returnValue(node));
        this.mock("executorMock").expects(test.once()).method("execute").will(test.returnValue(true));
        this.mock("executorMock").expects(test.once()).method("execute").will(test.returnValue(Collections.EMPTY_LIST));
        */
        final SessionProvider sessionProvider = new SessionProviderInMemory(new Roles(), new NodeProviderInMemory(""), ex);
        server = new ServerConfiguration(id, ring, mgr, sessionProvider, ss, ex, 10000);
        this.sm = (SessionManagerI) server.getServerManager();
        this.sm.setApplicationContext(ctx);
        this.ctx.addApplicationListener(this.sm);

        // Reproducing the logic of initializing the ObjectFactories
        // which typically happens within Spring
        new rtypes.RTypeObjectFactoryRegistry().setIceCommunicator(server.getCommunicator());
        new RoiTypes.RoiTypesObjectFactoryRegistry().setIceCommunicator(server.getCommunicator());
        new ModelObjectFactoryRegistry().setIceCommunicator(server.getCommunicator());

        /* UNUSED
        // The following is a bit of spring magic so that we can configure
        // the adapter in code. If this can be pushed to ServerConfiguration
        // for example then we might not need it here anymore.
        HotSwappableTargetSource ts = (HotSwappableTargetSource) ctx
                .getBean("swappableAdapterSource");
        ts.swap(server.getServerAdapter());
        */

        // Add our topic manager
        TopicManager tm = new TopicManager.Impl(server.getCommunicator());
        this.ctx.addApplicationListener(tm);

        // Setup mock router which allows us to use ode.client
        // rather than solely ServiceFactoryProxies, though it is
        // still necessary to call the proper mock methods.
        Ice.ObjectPrx prx = server.getServerAdapter().add(
                new MockRouter(this.sm),
                Ice.Util.stringToIdentity("ODE.Glacier2/router"));
        router = "ODE.Glacier2/router:"
                + prx.ice_getEndpoints()[0]._toString();

        // Finally, starting a scheduler to act like a real
        // server
        try {
            MethodInvokingJobDetailFactoryBean runBeats = new MethodInvokingJobDetailFactoryBean();
            runBeats.setBeanName("runBeats");
            runBeats.setTargetMethod("requestHeartBeats");
            runBeats.setTargetObject(server.getServerManager());
            runBeats.afterPropertiesSet();
            CronTriggerFactoryBean triggerBeats = new CronTriggerFactoryBean();
            triggerBeats.setBeanName("triggerBeats");
            triggerBeats.setJobDetail((JobDetail) runBeats.getObject());
            triggerBeats.setCronExpression("0-59/5 * * * * ?");
            triggerBeats.afterPropertiesSet();
            scheduler = new SchedulerFactoryBean();
            scheduler.setApplicationContext(ctx);
            scheduler.setTriggers(new Trigger[] { triggerBeats.getObject() });
            scheduler.afterPropertiesSet();
            scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ode.client newClient() {
        Properties p = new Properties();
        p.setProperty("Ice.Default.Router", router);
        ode.client client = new ode.client(p);
        return client;
    }

    public void tearDown() {
        mock("executorMock").expects(test.once()).method("execute")
            .will(test.returnValue(0));
        this.server.destroy();
        scheduler.stop();
        // this.ctx.closeAll();
    }

    public OdeContext getContext() {
        return this.ctx;
    }

    public SessionManagerI getSessionManager() {
        return this.sm;
    }

    public ServiceFactoryPrx createServiceFactory() throws Exception {
        Session s = session();
        Cache cache = cache();
        return createServiceFactory(s, cache, "single-client");
    }

    /**
     * Makes a direct call to SessionManager.create. A call should be made on
     * <em>some</em> fixture to {@link #prepareServiceFactory(Session, Cache)}
     * since the call may be re-routed to a clustered instance.
     */
    public ServiceFactoryPrx createServiceFactory(String name, String client)
            throws Exception {
        return ServiceFactoryPrxHelper.uncheckedCast(sm.create(name, null, this
                .current("create", client)));
    }

    public ServiceFactoryPrx createServiceFactory(Session s) throws Exception {
        Cache cache = cache();
        return createServiceFactory(s, cache, "single-client");
    }

    public ServiceFactoryPrx createServiceFactory(Session s, Cache cache,
            String clientId) throws Exception {
        prepareServiceFactory(s, cache);
        return ServiceFactoryPrxHelper.uncheckedCast(sm.create("name", null,
                this.current("create", clientId)));
    }

    public void prepareServiceFactory(Session s, Cache cache) {
        mock("sessionsMock").expects(test.once()).method("find")
                .will(test.returnValue(s));
        mock("securityMock").expects(test.once()).method("getSecurityRoles")
                .will(test.returnValue(new Roles()));
        mock("sessionsMock").expects(test.once()).method("create").will(
                test.returnValue(s));
        mock("sessionsMock").expects(test.once()).method("inMemoryCache").will(
                test.returnValue(cache));
        mock("methodMock").expects(test.atLeastOnce()).method("isActive").will(
                test.returnValue(false));
    }

    public void prepareClose(int referenceCount) {
        mock("sessionsMock").expects(test.once()).method("detach").will(
                test.returnValue(referenceCount));
        if (referenceCount < 1) {
            mock("sessionsMock").expects(test.once()).method("close").will(
                    test.returnValue(-2));
        }
    }

    Ring ring() {
        return server.getRing();
    }

    public Mock mock(String name) {
        return (Mock) ctx.getBean(name);
    }

    public Cache cache() {
        return new TestCache();
    }

    public Ice.Current current(String method) {
        return current(method, "my-client-uuid");
    }

    public Ice.Current current(String method, String clientId) {
        Ice.Current current = new Ice.Current();
        current.operation = method;
        current.adapter = server.getServerAdapter();
        current.ctx = new HashMap<String, String>();
        current.ctx.put(CLIENTUUID.value, clientId);
        return current;
    }

    public Session session() {
        return session("my-session-uuid");
    }

    public Session session(String uuid) {
        Session session = new Session();
        session.setUuid(uuid);
        session.setStarted(new Timestamp(System.currentTimeMillis()));
        session.setTimeToIdle(0L);
        session.setTimeToLive(0L);
        return session;
    }

    public Mock serverMock(Class serviceClass) {
        String name = serviceClass.getName();
        name = name.replaceFirst("ode", "ode").replace("PrxHelper", "");
        // WORKAROUND
        if (name.equals("ode.api.RenderingEngine")) {
            name = "odeis.providers.re.RenderingEngine";
        }
        Mock mock = mock("mock-" + name);
        if (mock == null) {
            throw new RuntimeException("No mock for serviceClass");
        }
        return mock;
    }

    public static class MockRouter extends Glacier2._RouterDisp {

        private final static Logger log = LoggerFactory.getLogger(MockRouter.class);

        private final SessionManagerI sm;

        private final Map<Ice.Connection, SessionPrx> sessionByConnection = new HashMap<Ice.Connection, SessionPrx>();

        public MockRouter(SessionManagerI sm) {
            this.sm = sm;
        }

        public void createSessionFromSecureConnection_async(
                AMD_Router_createSessionFromSecureConnection arg0, Current arg1)
                throws CannotCreateSessionException, PermissionDeniedException {
            arg0.ice_exception(new UnsupportedOperationException());
        }

        public void createSession_async(AMD_Router_createSession arg0,
                String arg1, String arg2, Current arg3)
                throws CannotCreateSessionException, PermissionDeniedException {
            try {
                SessionPrx prx = sm.create(arg1, null, arg3);
                sessionByConnection.put(arg3.con, prx);
                log.info(String.format("Storing %s under %s", prx, arg3.con));
                arg0.ice_response(prx);
            } catch (Exception e) {
                arg0.ice_exception(e);
            }

        }

        public void destroySession(Current arg0)
                throws SessionNotExistException {
            SessionPrx prx = sessionByConnection.get(arg0.con);
            if (prx != null) {
                log.info("Destroying " + prx);
                prx.destroy();
            }
        }

        public String getCategoryForClient(Current arg0) {
            return sessionByConnection.get(arg0.con).ice_id();
        }

        public int getACMTimeout(Ice.Current arg0) {
            throw new UnsupportedOperationException();
        }

        public long getSessionTimeout(Current arg0) {
            throw new UnsupportedOperationException();
        }

        public ObjectPrx[] addProxies(ObjectPrx[] arg0, Current arg1) {
            log.warn("addProxies called with " + Arrays.deepToString(arg0));
            return null;
        }

        @SuppressWarnings("deprecation")
        public void addProxy(ObjectPrx arg0, Current arg1) {
            log.warn("addProxy called with " + arg0);
        }

        public ObjectPrx getClientProxy(Current arg0) {
            return null;
        }

        public ObjectPrx getServerProxy(Current arg0) {
            return sessionByConnection.get(arg0.con);
        }

        public void refreshSession(Ice.Current current) throws Glacier2.SessionNotExistException {
            throw new UnsupportedOperationException();
        }

        public void refreshSession_async(Glacier2.AMD_Router_refreshSession arg0, Ice.Current arg1)
                        throws Glacier2.SessionNotExistException {
            throw new UnsupportedOperationException();
        }

    }

}