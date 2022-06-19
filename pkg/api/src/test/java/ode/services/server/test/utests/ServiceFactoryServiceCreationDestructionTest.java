package ode.services.server.test.utests;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import ode.logic.HardWiredInterceptor;
import ode.services.server.impl.ServiceFactoryI;
import ode.services.sessions.SessionManager;
import ode.services.util.Executor;
import ode.system.OdeContext;
import ode.system.Principal;
import ode.constants.CLIENTUUID;

import org.aopalliance.intercept.MethodInvocation;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ServiceFactoryServiceCreationDestructionTest extends
        MockObjectTestCase {

    protected Executor executor = new Executor.Impl(null, null, null, null) {
        @Override
        public Object execute(Principal p, Work work) {
            return work.doWork(null, null);
        }
    };

    Ice.Current curr;
    Mock mockCache, mockAdapter, mockManager;
    Ehcache cache;
    Ice.ObjectAdapter adapter;
    SessionManager manager;
    ServiceFactoryI sf;
    Map<String, Ice.Object> map;
    Ice.Current current = new Ice.Current();
    {
        current.ctx = new HashMap<String, String>();
        current.ctx.put(CLIENTUUID.value, "clientuuid");
    }

    // FIXME OdeContext should be an interface!
    // OdeContext context = new OdeContext(new String[]{
    // "classpath:ode.services.server.test.utests/ServiceFactoryTest.xml",
    // "classpath:ode/services/server-servantDefinitions.xml"});
    //    
    OdeContext context;

    String adminServiceId = "sessionuuid/ode.api.IAdmin";
    String reServiceId = "sessionuuid/uuid-ode.api.RenderingEngine";

    @Override
    @AfterMethod
    protected void tearDown() throws Exception {
        sf = null;
    }

    @Override
    @BeforeMethod
    protected void setUp() throws Exception {

        context = OdeContext.getInstance("ODE.server.test");
        context.refresh(); // Repairing from other methods

        map = new HashMap<String, Ice.Object>();

        mockCache = mock(Ehcache.class);
        cache = (Ehcache) mockCache.proxy();

        mockAdapter = mock(Ice.ObjectAdapter.class);
        adapter = (Ice.ObjectAdapter) mockAdapter.proxy();

        mockManager = mock(SessionManager.class);
        manager = (SessionManager) mockManager.proxy();
        mockManager.expects(once()).method("inMemoryCache").will(
                returnValue(cache));
        mockCache.expects(once()).method("isKeyInCache")
                .will(returnValue(true));
        mockCache.expects(once()).method("get").will(
                returnValue(new Element("activeServants", map)));

        curr = new Ice.Current();
        curr.adapter = adapter;
        curr.id = Ice.Util.stringToIdentity("username/sessionuuid");

        List<HardWiredInterceptor> hwi = new ArrayList<HardWiredInterceptor>();
        hwi.add(new HardWiredInterceptor() {
            public Object invoke(MethodInvocation arg0) throws Throwable {
                return arg0.proceed();
            }
        });

        Principal p = new Principal("session", "group", "type");
        sf = new ServiceFactoryI(current,
                new ode.util.ServantHolder("session"),
                null, context, manager, executor, p, hwi, null, null);
    };

    @Test
    public void testDoStatelessAddsServantToServantListCacheAndAdapter()
            throws Exception {

        callsActiveServices(Collections.singletonList(adminServiceId));
        mockAdapter.expects(once()).method("add").will(returnValue(null));
        mockAdapter.expects(once()).method("find").will(returnValue(null));
        mockAdapter.expects(once()).method("createDirectProxy").will(
                returnValue(null));
        sf.getAdminService(curr);
        List<String> ids = sf.activeServices(curr);
        assertTrue(ids.toString(), ids.size() == 1);
        assertTrue(ids.toString(), ids.get(0).endsWith("Admin"));
    }

    @Test
    public void testDoStatefulAddsServantToServantListCacheAndAdapter()
            throws Exception {

        callsActiveServices(Collections.singletonList(reServiceId));
        mockAdapter.expects(once()).method("add").will(returnValue(null));
        mockAdapter.expects(once()).method("createDirectProxy").will(
                returnValue(null));
        mockAdapter.expects(once()).method("find").will(returnValue(null));
        mockAdapter.expects(once()).method("find").will(returnValue(null));
        sf.createRenderingEngine(curr);
        List<String> ids = sf.activeServices(curr);
        assertTrue(ids.toString(), ids.size() == 1);
        assertTrue(ids.toString(), ids.get(0).endsWith("RenderingEngine"));
    }

    @Test
    void testUnregisterEventCallsClose() throws Exception {
        testDoStatefulAddsServantToServantListCacheAndAdapter();
        Mock closeMock = mock(ode.api.RenderingEngine.class);
        ode.api.RenderingEngine close = (ode.api.RenderingEngine) closeMock
                .proxy();
        mockAdapter.expects(once()).method("remove").will(returnValue(close));
        callsActiveServices(Collections.singletonList(reServiceId));
        String id = sf.activeServices(curr).get(0).toString();
        // Events now called by SessionManagerI
        sf.unregisterServant(Ice.Util.stringToIdentity(id));
    }

    private void callsActiveServices(List<String> idList) {

    }

}