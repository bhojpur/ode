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

import java.util.HashSet;

import ode.model.meta.Node;
import ode.services.server.fire.Registry;
import ode.services.server.fire.Ring;
import ode.services.util.Executor;
import ode.system.OdeContext;
import ode.grid.ClusterNodePrx;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RingTest extends MockObjectTestCase {

    Executor ex;
    Registry reg;
    OdeContext ctx;
    JdbcTemplate jdbc;
    Ice.ObjectAdapter oa;
    Ice.Communicator ic;
    Mock mockIc, mockOa, mockEx, mockReg;

    @BeforeMethod
    public void setupMethod() throws Exception {
        mockIc = mock(Ice.Communicator.class);
        ic = (Ice.Communicator) mockIc.proxy();
        mockOa = mock(Ice.ObjectAdapter.class);
        oa = (Ice.ObjectAdapter) mockOa.proxy();
        mockEx = mock(Executor.class);
        ex = (Executor) mockEx.proxy();
        mockReg = mock(Registry.class);
        reg = (Registry) mockReg.proxy();
    }

    @Test
    public void testFirstTakesOver() throws Exception {

        prepareInit("one");
        Ring one = new Ring("one", ex);
        one.setRegistry(reg);
        one.init(oa, "one");

        prepareInit("two");
        Ring two = new Ring("two", ex);
        two.setRegistry(reg);
        two.init(oa, "two");

    }

    private void prepareInit(String uuid) {
        mockOa.expects(once()).method("getCommunicator").will(returnValue(ic));

        mockReg.expects(once()).method("lookupClusterNodes").will(
                returnValue(new ClusterNodePrx[0]));
        mockEx.expects(once()).method("execute").will(
                returnValue(new HashSet<String>())).id(uuid + "lookup");
        mockEx.expects(once()).method("execute").after(uuid + "lookup").will(
                returnValue(new Node())).id(uuid + "createManager");
        mockEx.expects(once()).method("execute").after(uuid + "createManager")
                .will(returnValue(Boolean.TRUE))
                .id(uuid + "initializeRedirect");
        mockEx.expects(once()).method("execute").after(
                uuid + "initializeRedirect").will(returnValue(uuid)).id(
                uuid + "getRedirect");

        mockIc.expects(once()).method("stringToIdentity").will(
                returnValue(new Ice.Identity()));
        mockOa.expects(once()).method("add");
        mockOa.expects(once()).method("createDirectProxy");
        mockReg.expects(once()).method("addObject");
    }

    @Test
    public void testSeveralThreadsStartAndOnlyOneValueIsSet() throws Exception {
        fail();
    }

    @Test
    public void testHandlesMissingServers() throws Exception {
        fail();
    }

    @Test
    public void testRemovesUnreachable() throws Exception {
        fail();
    }

    @Test
    public void testReaddsSelfIfTemporarilyUnreachable() throws Exception {
        fail();
    }

    @Test
    public void testAllSessionRemovedIfDiscoveryFails() throws Exception {
        fail();
    }

    @Test
    public void testAllSessionsReassertedIfSessionComesBackOnline()
            throws Exception {
        fail();
    }
}