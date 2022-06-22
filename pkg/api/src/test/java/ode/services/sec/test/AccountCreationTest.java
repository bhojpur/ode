package ode.services.sec.test;

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

import static ode.rtypes.*;

import java.util.UUID;

import ode.ServerError;
import ode.model.Experimenter;
import ode.model.ExperimenterI;
import ode.model.ExperimenterGroup;
import ode.model.ExperimenterGroupI;
import ode.api.ServiceFactoryPrx;

import org.testng.Assert;
import org.testng.annotations.Test;

import Glacier2.CannotCreateSessionException;
import Glacier2.PermissionDeniedException;

@Test(enabled=false, groups = { "broken", "client", "integration", "security", "ticket:181",
        "ticket:199", "password" })
public class AccountCreationTest extends AbstractAccountTest {

    @Test(enabled=false)
    public void testSudoCreatesAccountThroughIUpdate() throws Exception {
        Experimenter e = createNewUser(getSudoUpdate("ode"));

        // passwords are no longer null by default
        removePasswordEntry(e);
        Assert.assertNull(getPasswordFromDb(e));

        assertCannotLogin(e.getOdeName().getValue(), "ode");
        assertCannotLogin(e.getOdeName().getValue(), "");

        doesNotHaveSystemPrivileges(e);

        getSudoAdmin("ode").changeUserPassword(e.getOdeName().getValue(), rstring("test"));
        assertCanLogin(e.getOdeName().getValue(), "test");
    }

    @Test(enabled=false)
    public void testSudoCreatesUserAccountThroughIAdmin() throws Exception {
        ExperimenterGroup g = new ExperimenterGroupI();
        g.setName(rstring(UUID.randomUUID().toString()));
        getSudoAdmin("ode").createGroup(g);
        Experimenter e = new ExperimenterI();
        e.setOdeName(rstring(UUID.randomUUID().toString()));
        e.setFirstName(rstring("ticket:181"));
        e.setLastName(rstring("ticket:199"));
        e = getSudoAdmin("ode").getExperimenter(
                getSudoAdmin("ode").createUser(e, g.getName().getValue()));
        assertCanLogin(e.getOdeName().getValue(), "");
        assertCanLogin(e.getOdeName().getValue(), "ode");
        assertCanLogin(e.getOdeName().getValue(), "bob");

        doesNotHaveSystemPrivileges(e);
    }

    @Test(enabled=false)
    public void testSudoCreatesSystemAccountThroughIAdmin() throws Exception {
        Experimenter e = new ExperimenterI();
        e.setOdeName(rstring(UUID.randomUUID().toString()));
        e.setFirstName(rstring("ticket:181"));
        e.setLastName(rstring("ticket:199"));
        e = getSudoAdmin("ode").getExperimenter(
                getSudoAdmin("ode").createSystemUser(e));
        assertCanLogin(e.getOdeName().getValue(), "");
        assertCanLogin(e.getOdeName().getValue(), "ode");
        assertCanLogin(e.getOdeName().getValue(), "bob");

        hasSystemPrivileges(e);

        getSudoAdmin("ode").changeUserPassword(e.getOdeName().getValue(), rstring("bob"));

        assertCannotLogin(e.getOdeName().getValue(), "");
        assertCannotLogin(e.getOdeName().getValue(), "ode");
        assertCanLogin(e.getOdeName().getValue(), "bob");

    }

    @Test(enabled=false)
    public void testSudoCreatesAccountThroughIAdmin() throws Exception {
        Experimenter e = new ExperimenterI();
        e.setOdeName(rstring(UUID.randomUUID().toString()));
        e.setFirstName(rstring("ticket:181"));
        e.setLastName(rstring("ticket:199"));
        e = getSudoAdmin("ode").getExperimenter(
                getSudoAdmin("ode").createUser(e, "default"));
        assertCanLogin(e.getOdeName().getValue(), "");
        assertCanLogin(e.getOdeName().getValue(), "ode");
        assertCanLogin(e.getOdeName().getValue(), "bob");

        doesNotHaveSystemPrivileges(e);

        getSudoAdmin("ode").changeUserPassword(e.getOdeName().getValue(), rstring("bob"));

        assertCannotLogin(e.getOdeName().getValue(), "");
        assertCannotLogin(e.getOdeName().getValue(), "ode");
        assertCanLogin(e.getOdeName().getValue(), "bob");

    }

    @Test(enabled=false)
    public void testSudoSysCreatesAccountThroughIAdmin() throws Exception {
        Experimenter e = new ExperimenterI();
        e.setOdeName(rstring(UUID.randomUUID().toString()));
        e.setFirstName(rstring("ticket:181"));
        e.setLastName(rstring("ticket:199"));
        e = getSudoAdmin("ode").getExperimenter(
                getSudoAdmin("ode").createSystemUser(e));
        assertCanLogin(e.getOdeName().getValue(), "");
        assertCanLogin(e.getOdeName().getValue(), "ode");
        assertCanLogin(e.getOdeName().getValue(), "bob");

        hasSystemPrivileges(e);

        getSudoAdmin("ode").changeUserPassword(e.getOdeName().getValue(), rstring("bob"));

        assertCannotLogin(e.getOdeName().getValue(), "");
        assertCannotLogin(e.getOdeName().getValue(), "ode");
        assertCanLogin(e.getOdeName().getValue(), "bob");

    }

    // ~ Helpers
    // =========================================================================

    private void hasSystemPrivileges(Experimenter e) {
        try
        {
            ServiceFactoryPrx sf = c.createSession(e.getOdeName().getValue(), "");
            sf.getAdminService().synchronizeLoginCache();
        } catch (ServerError e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            throw new RuntimeException("Unimplemented exception.");
        } catch (CannotCreateSessionException e2)
        {
            // TODO Auto-generated catch block
            e2.printStackTrace();
            throw new RuntimeException("Unimplemented exception.");
        } catch (PermissionDeniedException e3)
        {
            // TODO Auto-generated catch block
            e3.printStackTrace();
            throw new RuntimeException("Unimplemented exception.");
        }
    }

    private void doesNotHaveSystemPrivileges(Experimenter e) {
        try {
            hasSystemPrivileges(e);
            Assert.fail("Should be security violation");
        } catch (Exception ex) {
            // ok.
        }
    }

}