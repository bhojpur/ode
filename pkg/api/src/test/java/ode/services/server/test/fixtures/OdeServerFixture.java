package ode.services.server.test.fixtures;

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

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sf.ehcache.Ehcache;
import ode.api.IAdmin;
import ode.api.IQuery;
import ode.api.IUpdate;
import ode.api.RawPixelsStore;
import ode.model.meta.Experimenter;
import ode.model.meta.Node;
import ode.model.meta.Session;
import ode.services.server.Entry;
import ode.services.server.Router;
import ode.services.sessions.SessionContext;
import ode.services.sessions.SessionContextImpl;
import ode.services.sessions.state.CacheFactory;
import ode.services.sessions.stats.NullSessionStats;
import ode.system.OdeContext;
import ode.system.Roles;
import ode.tools.spring.ManagedServiceFactory;
import odeis.providers.re.RenderingEngine;
import ode.api.ServiceFactoryPrx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.springframework.util.ResourceUtils;

/**
 * Note: Using the {@link Router} wrapper class can cause processes to be
 * orphaned on the OS.
 */
public class OdeServerFixture extends MockObjectTestCase {

    private static final Logger log = LoggerFactory.getLogger(ServerServerFixture.class);

    private final Map<String, Object> STUBS = new HashMap<String, Object>();

    private final Map<String, Mock> MOCKS = new HashMap<String, Mock>();

    protected Session session;
    protected SessionContext sc;

    protected Ehcache cache;

    // Name used to look up the test context.
    protected static String DEFAULT = "ODE.server.test";
    protected String name;
    protected Thread t;
    protected Entry m;
    protected Router r;
    protected ode.client ice;
    protected OdeContext ctx;

    int sessionTimeout;
    int serviceTimeout;

    // Keys for the mocks that are known to be needed
    final static String adm = "internal-ode.api.IAdmin",
            qu = "internal-ode.api.IQuery", up = "internal-ode.api.IUpdate",
            ss = "securitySystem",
            re = "managed-odeis.providers.re.RenderingEngine",
            px = "internal-ode.api.RawPixelsStore", ms = "methodSecurity",
            sm = "sessionManager";

    public ServerServerFixture() {
        this(DEFAULT, 30, 10);
    }

    public ServerServerFixture(int sessionTimeout, int serviceTimeout) {
        this(DEFAULT, sessionTimeout, serviceTimeout);

    }

    public ServerServerFixture(String contextName) {
        this(contextName, 30, 10);
    }

    /** It is important to have the timeouts set before context creation */
    public ServerServerFixture(String contextName, int sessionTimeout,
            int serviceTimeout) {

        this.name = contextName;
        this.serviceTimeout = serviceTimeout;
        this.sessionTimeout = sessionTimeout;

        // Set property before the OdeContext is created
        try {
            File ice_config = ResourceUtils.getFile("classpath:manual.config");
            System.setProperty("ICE_CONFIG", ice_config.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.setProperty("ode.server.cache.timeToIdle", "" + serviceTimeout);

        ctx = OdeContext.getInstance(name);
        ctx.close();
        startServer();

        ManagedServiceFactory stubs = new ManagedServiceFactory();
        stubs.setApplicationContext(ctx);
        MockServiceFactory mocks = new MockServiceFactory();
        mocks.setApplicationContext(ctx);

        Mock mock;
        Object stub;

        mock = mocks.getMockByClass(IAdmin.class);
        stub = stubs.getAdminService();
        addMock(adm, mock, stub);

        mock = mocks.getMockByClass(IQuery.class);
        stub = stubs.getQueryService();
        addMock(qu, mock, stub);

        mock = mocks.getMockByClass(IUpdate.class);
        stub = stubs.getUpdateService();
        addMock(up, mock, stub);

        mock = mocks.getMockByClass(RenderingEngine.class);
        stub = stubs.createRenderingEngine();
        addMock(re, mock, stub);

        mock = mocks.getMockByClass(RawPixelsStore.class);
        stub = stubs.createRawPixelsStore();
        addMock(px, mock, stub);

        mock = (Mock) ctx.getBean("methodMock");
        stub = ctx.getBean("methodSecurity");
        addMock(ms, mock, stub);

        mock = (Mock) ctx.getBean("securityMock");
        stub = ctx.getBean("securitySystem");
        addMock(ss, mock, stub);

        mock = (Mock) ctx.getBean("sessionsMock");
        stub = ctx.getBean("sessionManager");
        addMock(sm, mock, stub);

    }

    private void startServer() {
        m = new Entry(name);
        throw new RuntimeException("All classes relying on this fixture\n"
                +"need to be converted to using the in-memory glacier!");
        /*
        t = new Thread(m);
        r = new Router();
        r.setTimeout(sessionTimeout);
        r.allowAdministration();
        m.setRouter(r);
        t.start();
        assertTrue("Startup must succeed", m.waitForStartup());

        log
                .warn("\n"
                        + "============================================================\n"
                        + "Use ONLY Ctrl-C or 'q' in the console to cancel this process\n"
                        + "============================================================");
         */
    }

    public void prepareLogin() {
        session = new Session(new Node(0L, false), "uuid", new Experimenter(0L,
                false), new Long(0), new Long(0), null, "Test");
        sc = new SessionContextImpl(session, Collections.singletonList(1L),
                Collections.singletonList(1L), Collections
                        .singletonList("user"), new NullSessionStats(), null);
        CacheFactory factory = new CacheFactory();
        factory.setBeanName("server.fixture");
        factory.setOverflowToDisk(false);
        cache = factory.createCache();
        getMock(sm).expects(once()).method("executePasswordCheck").will(
                returnValue(true));
        getMock(sm).expects(once()).method("create").will(returnValue(session));
        getMock(sm).expects(once()).method("inMemoryCache").will(
                returnValue(cache));
        getMock(adm).expects(once()).method("checkPassword").will(
                returnValue(true));
        getMock(ss).expects(once()).method("getSecurityRoles").will(
                returnValue(new Roles()));
    }

    public ServiceFactoryPrx createSession() throws Exception {
        prepareLogin();
        Properties p = new Properties();
        p.setProperty("ode.client.Endpoints", "tcp -p 10000");
        p.setProperty("ode.user", "user");
        p.setProperty("ode.pass", "pass");
        p.setProperty("Ice.Default.Router",
                "ODE.Glacier2/router:tcp -p 4064 -h 127.0.0.1");
        p.setProperty("Ice.ImplicitContext", "Shared");
        ice = new ode.client(p);
        ServiceFactoryPrx session = ice.createSession(null, null);
        return session;
    }

    public void methodCall() throws Exception {
        getMock(sm).expects(once()).method("getEventContext").will(
                returnValue(sc));
        getMock(ms).expects(once()).method("isActive").will(returnValue(true));
        getMock(ms).expects(once()).method("checkMethod");
        getMock(ss).expects(once()).method("login");
        getMock(ss).expects(once()).method("logout").will(returnValue(0));
        getMock(re).expects(once()).method("close");
    }

    public void destroySession() throws Exception {
        ice.closeSession();
    }

    @Override
    public void tearDown() throws Exception {
        try {
            super.tearDown();
        } finally {
            /*
            m.setStop();
            try {
                Thread.sleep(2L);
            } catch (InterruptedException ie) {
                // ok
            }
            m.shutdown();
            */
        }
    }

    public OdeContext getContext() {
        return ctx;
    }

    // MOCK MANAGEMENT

    private void addMock(String name, Mock mock, Object proxy) {
        if (MOCKS.containsKey(name)) {
            throw new RuntimeException(name + " already exists.");
        }
        MOCKS.put(name, mock);
        STUBS.put(name, proxy);
    }

    public Mock getMock(String name) {
        return MOCKS.get(name);
    }

    public Object getStub(String name) {
        return STUBS.get(name);
    }

    public Mock getAdmin() {
        return MOCKS.get(adm);
    }

    public Mock getQuery() {
        return MOCKS.get(qu);
    }

    public Mock getUpdate() {
        return MOCKS.get(up);
    }

    public Mock getSecSystem() {
        return MOCKS.get(ss);
    }

    public Mock getRndEngine() {
        return MOCKS.get(re);
    }

    public Mock getPixelsStore() {
        return MOCKS.get(px);
    }

    public Mock getMethodSecurity() {
        return MOCKS.get(ms);
    }

    public Mock getSessionManager() {
        return MOCKS.get(sm);
    }
}

class MockServiceFactory extends ManagedServiceFactory {

    @Override
    protected String getPrefix() {
        return "mock:";
    }

    public Mock getMockByClass(Class klass) {
        return (Mock) ctx.getBean(getPrefix() + klass.getName());
    }
}