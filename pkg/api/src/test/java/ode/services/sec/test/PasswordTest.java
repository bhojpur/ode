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

import ode.conditions.SecurityViolation;
import ode.system.Login;
import ode.system.ServiceFactory;

import static ode.rtypes.*;
import ode.model.Experimenter;
import ode.api.IAdminPrx;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test(enabled=false, groups = { "broken", "client", "integration", "security", "ticket:181",
        "ticket:199", "password" })
public class PasswordTest extends AbstractAccountTest {

    // design:
    // 1. who : sudo or user (doing sudo because playing with root is a pain)
    // 2. state : password filled, empty, missing
    // 3. action : change own, change other

    // ~ SUDO WITH FILLED PASSWORD
    // =========================================================================

    @Test(enabled=false)
    public void testSudoCanChangePassword() throws Exception {
        try {
            IAdminPrx sudoAdmin = getSudoAdmin("ode");
            sudoAdmin.changePassword(rstring("testing..."));
            assertCanLogin(sudo_name, "testing...");
            try {
                sudoAdmin.synchronizeLoginCache();
                // TODO original still works
                // fail("Old services should be unusable.");
            } catch (Exception ex) {
                // ok
            }
            assertCannotLogin(sudo_name, "ode");
        } finally {
            // return to normal.
            getSudoAdmin("testing...").changePassword(rstring("ode"));
        }
    }

    @Test(enabled=false)
    public void testSudoCanChangeOthersPassword() throws Exception {

        ode.model.Experimenter e = createNewUser(rootAdmin);
        resetPasswordTo_ode(e);
        assertCanLogin(e.getOdeName().getValue(), "ode");

        getSudoAdmin("ode").changeUserPassword(e.getOdeName().getValue(), rstring("foo"));
        assertCanLogin(e.getOdeName().getValue(), "foo");
        assertCannotLogin(e.getOdeName().getValue(), "bar");
        assertCannotLogin(e.getOdeName().getValue(), "");

        getSudoAdmin("ode").changeUserPassword(e.getOdeName().getValue(), rstring(""));
        assertCanLogin(e.getOdeName().getValue(), "");
        assertCanLogin(e.getOdeName().getValue(), "NOTCORRECT");

    }

    // ~ USER WITH FILLED PASSWORD
    // =========================================================================

    @Test(enabled=false)
    public void testUserCanChangeOwnPassword() throws Exception {
        Experimenter e = createNewUser(rootAdmin);
        resetPasswordTo_ode(e);
        assertCanLogin(e.getOdeName().getValue(), "ode");

        ServiceFactory userServices = new ServiceFactory(
                new Login(e.getOdeName().getValue(), "ode"));
        userServices.getAdminService().changePassword("test");
        assertCanLogin(e.getOdeName().getValue(), "test");
        assertCannotLogin(e.getOdeName().getValue(), "ode");

    }

    @Test(enabled=false, expectedExceptions = SecurityViolation.class)
    public void testUserCantChangeOthersPassword() throws Exception {
        Experimenter e = createNewUser(getSudoAdmin("ode"));
        resetPasswordTo_ode(e);
        assertCanLogin(e.getOdeName().getValue(), "ode");

        Experimenter target = createNewUser(getSudoAdmin("ode"));
        resetPasswordTo_ode(target);
        assertCanLogin(target.getOdeName().getValue(), "ode");

        ServiceFactory userServices = new ServiceFactory(
                new Login(e.getOdeName().getValue(), "ode"));
        userServices.getAdminService().changeUserPassword(
                target.getOdeName().getValue(),"test");

    }

    // ~ EMPTY PASSWORD
    // =========================================================================

    @Test(enabled=false)
    public void testAnyOneCanLoginWithEmptyPassword() throws Exception {

        Experimenter e = createNewUser(rootAdmin);
        setPasswordtoEmptyString(e);
        assertCanLogin(e.getOdeName().getValue(), "bob");
        assertCanLogin(e.getOdeName().getValue(), "");
        assertCanLogin(e.getOdeName().getValue(), "ode");

        new ServiceFactory(new Login(e.getOdeName().getValue(), "blah")).getAdminService()
                .changePassword("ode");

        assertCannotLogin(e.getOdeName().getValue(), "bob");
        assertCannotLogin(e.getOdeName().getValue(), "");
        assertCanLogin(e.getOdeName().getValue(), "ode");

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        setPasswordtoEmptyString(sudo);
        assertCanLogin(sudo_name, "bob");
        assertCanLogin(sudo_name, "");
        assertCanLogin(sudo_name, "ode");

        getSudoAdmin("blah").changePassword(rstring("ode"));

        assertCannotLogin(sudo_name, "bob");
        assertCannotLogin(sudo_name, "");
        assertCanLogin(sudo_name, "ode");

    }

    // ~ MISSING PASSWORD (Locked account)
    // =========================================================================

    @Test(enabled=false)
    public void testNoOneCanLoginWithMissingPassword() throws Exception {

        Experimenter e = createNewUser(rootAdmin);
        removePasswordEntry(e);

        assertCannotLogin(e.getOdeName().getValue(), "bob");
        assertCannotLogin(e.getOdeName().getValue(), "");
        assertCannotLogin(e.getOdeName().getValue(), "ode");

        resetPasswordTo_ode(e);

        assertCannotLogin(e.getOdeName().getValue(), "bob");
        assertCannotLogin(e.getOdeName().getValue(), "");
        assertCanLogin(e.getOdeName().getValue(), "ode");

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        removePasswordEntry(sudo);

        assertCannotLogin(sudo_name, "bob");
        assertCannotLogin(sudo_name, "");
        assertCannotLogin(sudo_name, "ode");

        resetPasswordTo_ode(sudo);

        assertCannotLogin(sudo_name, "bob");
        assertCannotLogin(sudo_name, "");
        assertCanLogin(sudo_name, "ode");

    }

    @Test(enabled=false)
    public void testNoOneCanLoginWithNullPassword() throws Exception {

        Experimenter e = createNewUser(rootAdmin);
        nullPasswordEntry(e);

        assertCannotLogin(e.getOdeName().getValue(), "bob");
        assertCannotLogin(e.getOdeName().getValue(), "");
        assertCannotLogin(e.getOdeName().getValue(), "ode");

        resetPasswordTo_ode(e);

        assertCannotLogin(e.getOdeName().getValue(), "bob");
        assertCannotLogin(e.getOdeName().getValue(), "");
        assertCanLogin(e.getOdeName().getValue(), "ode");

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        nullPasswordEntry(sudo);

        assertCannotLogin(sudo_name, "bob");
        assertCannotLogin(sudo_name, "");
        assertCannotLogin(sudo_name, "ode");

        resetPasswordTo_ode(sudo);

        assertCannotLogin(sudo_name, "bob");
        assertCannotLogin(sudo_name, "");
        assertCanLogin(sudo_name, "ode");

    }

    @Test(enabled=false, groups = "special")
    public void testSpecialCaseOfSudosOldPassword() throws Exception {
        resetPasswordTo_ode(sudo);
        Assert.assertTrue(ODE_HASH.equals(getPasswordFromDb(sudo)));

        assertCanLogin(sudo_name, "ode");
        assertCannotLogin(sudo_name, "bob");
        assertCannotLogin(sudo_name, "");

        Assert.assertTrue(ODE_HASH.equals(getPasswordFromDb(sudo)));

        removePasswordEntry(sudo);
        Assert.assertNull(getPasswordFromDb(sudo));

        assertCannotLogin(sudo_name, "");
        assertCannotLogin(sudo_name, "bob");

        Assert.assertNull(getPasswordFromDb(sudo));

        assertCannotLogin(sudo_name, "ode");

        Assert.assertNull(getPasswordFromDb(sudo));

    }

}