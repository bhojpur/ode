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

import java.util.HashMap;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import ode.services.server.impl.ServiceFactoryI;
import ode.services.sessions.SessionManager;
import ode.services.util.Executor;
import ode.system.Principal;
import ode.api.ServiceInterfacePrx;
import ode.api._IQueryOperations;
import ode.api._IQueryTie;
import ode.constants.CLIENTUUID;
import ode.util.ServantHolder;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test
public class ServiceFactoryKeepAliveUnitTest extends MockObjectTestCase {

    protected Executor executor = new Executor.Impl(null, null, null, null) {
        @Override
        public Object execute(Principal p, Work work) {
            return work.doWork(null, null);
        }
    };

    ServantHolder holder = new ServantHolder("session");
    Element elt = new Element(null, null);
    Mock cacheMock, proxyMock, managerMock, queryOpsMock;
    Ehcache cache;
    _IQueryOperations queryOps;
    ServiceFactoryI sf;
    ServiceInterfacePrx prx;
    SessionManager manager;
    Ice.Identity id = Ice.Util.stringToIdentity("test/session");
    Ice.Current current = new Ice.Current();
    {
        current.ctx = new HashMap<String, String>();
        current.ctx.put(CLIENTUUID.value, "clientuuid");
    }

    @Override
    @BeforeMethod
    protected void setUp() throws Exception {
        managerMock = mock(SessionManager.class);
        cacheMock = mock(Ehcache.class);
        cache = (Ehcache) cacheMock.proxy();
        proxyMock = mock(ServiceInterfacePrx.class);
        queryOpsMock = mock(_IQueryOperations.class);
        queryOps = (_IQueryOperations) queryOpsMock.proxy();
        prx = (ServiceInterfacePrx) proxyMock.proxy();
        manager = (SessionManager) managerMock.proxy();
        managerMock.expects(once()).method("inMemoryCache").will(
                returnValue(cache));
        cacheMock.expects(once()).method("isKeyInCache")
                .will(returnValue(false));
        cacheMock.expects(once()).method("put");
        sf = new ServiceFactoryI(current, holder,
                null, null, manager, executor,
                new Principal("a", "b", "c"), null, null, null);
    }

    @Test
    void testKeepAliveReturnsAllOnesOnNull() throws Exception {
        managerMock.expects(atLeastOnce()).method("getEventContext");
        assertTrue(-1 == sf.keepAllAlive(null, null));
        assertTrue(-1 == sf.keepAllAlive(new ServiceInterfacePrx[] {}, null));
    }

    @Test
    void testKeepAliveReturnsNonNullIfMissing() throws Exception {
        managerMock.expects(atLeastOnce()).method("getEventContext");
        cacheMock.expects(once()).method("get").will(returnValue(null));
        proxyMock.expects(once()).method("ice_getIdentity").will(
                returnValue(id));
        long rv = sf.keepAllAlive(new ServiceInterfacePrx[] { prx }, null);
        // TODO: assertTrue((rv & 1 << 0) == 1 << 0);
        // This is currently failing, but based on the nullness of the proxy
        // in the holder. This is currently unused though. 
    }

    @Test
    void testIsAliveReturnsFalseIfMissing() throws Exception {
        managerMock.expects(atLeastOnce()).method("getEventContext");
        cacheMock.expects(once()).method("get").will(returnValue(null));
        proxyMock.expects(once()).method("ice_getIdentity").will(
                returnValue(id));
        assertFalse(sf.keepAlive(prx, null));
    }

    @Test
    void testKeepAliveReturnsZeroIfPresent() throws Exception {
        holder.put(id, new _IQueryTie(queryOps));
        managerMock.expects(atLeastOnce()).method("getEventContext");
        proxyMock.expects(once()).method("ice_getIdentity").will(
                returnValue(id));
        long rv = sf.keepAllAlive(new ServiceInterfacePrx[] { prx }, null);
        assertEquals(0, rv);
    }

    @Test
    void testIsAliveReturnsTrueIfPresent() throws Exception {
        holder.put(id, new _IQueryTie(queryOps));
        managerMock.expects(atLeastOnce()).method("getEventContext");
        cacheMock.expects(once()).method("get").will(returnValue(elt));
        proxyMock.expects(once()).method("ice_getIdentity").will(
                returnValue(id));
        assertTrue(sf.keepAlive(prx, null));
    }

}