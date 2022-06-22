package integration;

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

import java.util.List;
import java.util.UUID;

import ode.RString;
import ode.client;
import ode.api.IAdminPrx;
import ode.api.ServiceFactoryPrx;
import ode.api.StatefulServiceInterfacePrx;
import ode.model.Experimenter;
import ode.model.ExperimenterGroup;
import ode.model.ExperimenterGroupI;
import ode.model.ExperimenterI;
import ode.model.PermissionsI;
import ode.sys.EventContext;

import org.testng.Assert;
import org.testng.annotations.Test;

import Ice.UserException;

/**
 * Various uses of the {@link ode.client} object. All configuration comes from
 * the ICE_CONFIG environment variable.
 */
@Test
public class ClientUsageTest extends AbstractServerTest {

    /**
     * Closes automatically the session.
     *
     * @throws Exception
     *             If an error occurred.
     */
    public void testClientClosedAutomatically() throws Exception {
        IAdminPrx svc = root.getSession().getAdminService();
        String uuid = UUID.randomUUID().toString();
        Experimenter e = new ExperimenterI();
        e.setOdeName(ode.rtypes.rstring(uuid));
        e.setFirstName(ode.rtypes.rstring("integration"));
        e.setLastName(ode.rtypes.rstring("tester"));
        e.setLdap(ode.rtypes.rbool(false));
        ExperimenterGroup g = new ExperimenterGroupI();
        g.setName(ode.rtypes.rstring(uuid));
        g.setLdap(ode.rtypes.rbool(false));
        g.getDetails().setPermissions(new PermissionsI("rw----"));
        g = svc.getGroup(svc.createGroup(g));
        long uid = newUserInGroupWithPassword(e, g, uuid);
        svc.setDefaultGroup(svc.getExperimenter(uid), g);
        client = new ode.client();
        client.createSession(uuid, uuid);
        client.closeSession();
    }

    /**
     * Tests the usage of memory.
     *
     * @throws Exception
     *             If an error occurred.
     */
    public void testUseSharedMemory() throws Exception {
        IAdminPrx svc = root.getSession().getAdminService();
        String uuid = UUID.randomUUID().toString();
        Experimenter e = new ExperimenterI();
        e.setOdeName(ode.rtypes.rstring(uuid));
        e.setFirstName(ode.rtypes.rstring("integration"));
        e.setLastName(ode.rtypes.rstring("tester"));
        e.setLdap(ode.rtypes.rbool(false));
        ExperimenterGroup g = new ExperimenterGroupI();
        g.setName(ode.rtypes.rstring(uuid));
        g.setLdap(ode.rtypes.rbool(false));
        g.getDetails().setPermissions(new PermissionsI("rw----"));
        g = svc.getGroup(svc.createGroup(g));
        long uid = newUserInGroupWithPassword(e, g, uuid);
        svc.setDefaultGroup(svc.getExperimenter(uid), g);
        client = new ode.client();
        client.createSession(uuid, uuid);

        Assert.assertEquals(0, client.getInputKeys().size());
        client.setInput("a", ode.rtypes.rstring("b"));
        Assert.assertEquals(1, client.getInputKeys().size());
        Assert.assertTrue(client.getInputKeys().contains("a"));
        Assert.assertEquals("b", ((RString) client.getInput("a")).getValue());

        client.closeSession();
    }

    /**
     * Test the creation of an insecure client.
     *
     * @throws Exception
     *             If an error occurred.
     */
    public void testCreateInsecureClientTicket2099() throws Exception {
        IAdminPrx svc = root.getSession().getAdminService();
        String uuid = UUID.randomUUID().toString();
        Experimenter e = new ExperimenterI();
        e.setOdeName(ode.rtypes.rstring(uuid));
        e.setFirstName(ode.rtypes.rstring("integration"));
        e.setLastName(ode.rtypes.rstring("tester"));
        e.setLdap(ode.rtypes.rbool(false));
        ExperimenterGroup g = new ExperimenterGroupI();
        g.setName(ode.rtypes.rstring(uuid));
        g.setLdap(ode.rtypes.rbool(false));
        g.getDetails().setPermissions(new PermissionsI("rw----"));
        g = svc.getGroup(svc.createGroup(g));
        long uid = newUserInGroupWithPassword(e, g, uuid);
        svc.setDefaultGroup(svc.getExperimenter(uid), g);
        client secure = new ode.client();
        ServiceFactoryPrx factory = secure.createSession(uuid, uuid);
        Assert.assertTrue(secure.isSecure());
        try {
            factory.getAdminService().getEventContext();
            ode.client insecure = secure.createClient(false);
            try {
                insecure.getSession().getAdminService().getEventContext();
                Assert.assertFalse(insecure.isSecure());
            } finally {
                insecure.closeSession();
            }
        } finally {
            secure.closeSession();
        }
    }

    /**
     * Test the {@link ode.client#getStatefulServices()} method. All stateful
     * services should be returned. Calling close on them should remove them
     * from future calls, which will allow
     * {@link ServiceFactoryPrx#setSecurityContext} to be called.
     *
     * @throws Exception
     *             If an error occurred.
     */
    public void testGetStatefulServices() throws Exception {
        ServiceFactoryPrx sf = root.getSession();
        sf.setSecurityContext(new ode.model.ExperimenterGroupI(0L, false));
        sf.createRenderingEngine();
        List<StatefulServiceInterfacePrx> srvs = root.getStatefulServices();
        Assert.assertEquals(1, srvs.size());
        try {
            sf.setSecurityContext(new ode.model.ExperimenterGroupI(1L, false));
            Assert.fail("Should not be allowed");
        } catch (ode.SecurityViolation sv) {
            // good
        }
        srvs.get(0).close();
        srvs = root.getStatefulServices();
        Assert.assertEquals(0, srvs.size());
        sf.setSecurityContext(new ode.model.ExperimenterGroupI(1L, false));
    }

    /**
     * Test that {@link client#joinSession(String)} fails after the client is disconnected.
     * @throws Exception unexpected
     */
    public void testJoinSession() throws Exception {
        //create a new user.
        EventContext ec = newUserAndGroup("rw----", true);
        String session = ec.sessionUuid;
        //delete the active client
        disconnect();
        
        try {
            // wait a bit before trying to join the session
            Thread.sleep(2000);
        } catch (Exception e1) {
        }
        
        client c = new client();
        try {
            c.joinSession(session);
            Assert.fail("The session should have been deleted");
        } catch (UserException e) {
            /* expected because the client is disconnected */
        }
    }
}
