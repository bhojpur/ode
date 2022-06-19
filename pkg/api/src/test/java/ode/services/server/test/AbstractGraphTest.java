package ode.services.server.test;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jmock.Mock;
import org.testng.annotations.BeforeClass;

import ode.io.nio.PixelsService;
import ode.io.nio.ThumbnailService;
import ode.security.ACLVoter;
import ode.security.SecuritySystem;
import ode.security.auth.PasswordProvider;
import ode.security.auth.PasswordUtil;
import ode.services.mail.MailUtil;
import ode.services.util.ReadOnlyStatus;
import ode.system.Roles;
import ode.tools.hibernate.ExtendedMetadata;
import ode.RType;
import ode.cmd.ERR;
import ode.cmd.HandleI;
import ode.cmd.IRequest;
import ode.cmd.OK;
import ode.cmd.RequestObjectFactoryRegistry;
import ode.cmd.Response;
import ode.cmd.State;
import ode.cmd.Status;
import ode.cmd._HandleTie;
import ode.cmd.graphs.GraphRequestFactory;
import ode.sys.ParametersI;

public class AbstractGraphTest extends AbstractServantTest {

    Mock adapterMock;

    Ice.Communicator ic;

    @Override
    @BeforeClass
    protected void setUp() throws Exception {
        super.setUp();
        adapterMock = (Mock) user.ctx.getBean("adapterMock");
        adapterMock.setDefaultStub(new FakeAdapter());
        ic = ctx.getBean("Ice.Communicator", Ice.Communicator.class);

        // Register ChgrpI, etc. This happens automatically on the server.
        RequestObjectFactoryRegistry rofr = new RequestObjectFactoryRegistry(
                user.ctx.getBean(ExtendedMetadata.class),
                user.ctx.getBean(ACLVoter.class),
                user.ctx.getBean(Roles.class),
                user.ctx.getBean("/ODE/Pixels", PixelsService.class),
                user.ctx.getBean("/ODE/Thumbs", ThumbnailService.class),
                user.ctx.getBean(MailUtil.class),
                user.ctx.getBean(PasswordUtil.class),
                user.ctx.getBean(SecuritySystem.class),
                user.ctx.getBean(PasswordProvider.class),
                user.ctx.getBean("graphRequestFactory", GraphRequestFactory.class)
                );
        rofr.setApplicationContext(ctx);
        rofr.setIceCommunicator(ic);
    }

    //
    // Helpers
    //

    protected _HandleTie submit(IRequest req) throws Exception {
        return submit(req, null);
    }

    protected _HandleTie submit(IRequest req, long groupID) throws Exception {
        Map<String, String> callContext = new HashMap<String, String>();
        callContext.put("ode.group", ""+groupID);
        return submit(req, callContext);
    }

    protected _HandleTie submit(IRequest req, Map<String, String> callContext) throws Exception {
        Ice.Identity id = new Ice.Identity("handle", req.toString());
        final HandleI handle = new HandleI(new ReadOnlyStatus(false, false), 1000);
        handle.setSession(user.sf);
        handle.initialize(id, req, callContext);
        handle.run();
        // Client side this would need a try/finally { handle.close() }
        return new _HandleTie(handle);
    }

    protected void block(_HandleTie handle, int loops, long pause)
            throws InterruptedException {
        for (int i = 0; i < loops && null == handle.getResponse(); i++) {
            Thread.sleep(pause);
        }
    }

    protected Response assertSuccess(_HandleTie handle) {
        Response rsp = handle.getResponse();
        Status status = handle.getStatus();
        assertSuccess(rsp);
        assertFalse(status.flags.contains(State.FAILURE));
        return rsp;
    }

    protected Response assertSuccess(Response rsp) {
        assertNotNull(rsp);
        if (rsp instanceof ERR) {
            ERR err = (ERR) rsp;
            fail(printErr(err));
        }
        return rsp;
    }

    protected void assertFailure(_HandleTie handle, String...allowedMessages) {
        final List<String> msgs = Arrays.asList(allowedMessages);
        final Response rsp = handle.getResponse();
        assertNotNull(rsp);
        if (rsp instanceof OK) {
            OK ok = (OK) rsp;
            fail(ok.toString());
        } else {
            ERR err = (ERR) rsp;
            if (msgs.size() > 0) {
                assertTrue(String.format("%s not in %s: %s", err.name,
                        msgs, printErr(err)), msgs.contains(err.name));
            }
        }
        assertFlag(handle, State.FAILURE);
    }

    protected void assertFlag(_HandleTie handle, State s) {
        Status status = handle.getStatus();
        assertTrue(String.format("Looking for %s. Found: %s", s, status.flags),
            status.flags.contains(s));
    }

    protected void assertDoesExist(String table, long id) throws Exception {
        List<List<RType>> ids = assertProjection(
                "select x.id from " +table+" x where x.id = :id",
                new ParametersI().addId(id));
        assertEquals(1, ids.size());
    }

    protected void assertDoesNotExist(String table, long id) throws Exception {
        List<List<RType>> ids = assertProjection(
                "select x.id from " +table+" x where x.id = :id",
                new ParametersI().addId(id));
        assertEquals(0, ids.size());
    }

    private String printErr(ERR err) {
        StringBuilder sb = new StringBuilder();
        sb.append(err.toString());
        sb.append("\n");
        sb.append("==========================================\n");
        sb.append("category=");
        sb.append(err.category);
        sb.append("\n");
        sb.append("name=");
        sb.append(err.name);
        sb.append("\n");
        sb.append("params=");
        sb.append(err.parameters);
        sb.append("\n");
        sb.append("==========================================\n");
        return sb.toString();
    }

}