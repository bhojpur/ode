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

import org.testng.Assert;
import org.testng.annotations.*;

import ode.conditions.SecurityViolation;
import ode.conditions.ValidationException;
import ode.model.core.Image;
import ode.model.internal.Permissions;
import ode.system.Login;
import ode.system.ServiceFactory;

@Test(enabled=false, groups = { "broken", "client", "integration", "security", "ticket:365", "chmod" })
public class ChmodClientTest extends AbstractChangeDetailClientTest {

    // TODO : This series of tests (AbstractChangeDetailClientTest)
    // should be unified with AbstractPermissionsTest (both setup
    // UserOtherWorldPiRoot etc.)

    // design parameters:
    // 1. various permissions (belonging to root, user, or other)
    // 2. as user or root
    // 3. changing to various permissions

    @Test(enabled=false)
    public void test_user_RWRW_user_PUBLIC() throws Exception {
        newUserImagePermissionsAsUserToPermissions(true, asUser,
                Permissions.GROUP_PRIVATE, asUser, Permissions.PUBLIC);
    }

    @Test(enabled=false)
    public void test_user_RWRW_other_PUBLIC() throws Exception {
        newUserImagePermissionsAsUserToPermissions(false, asUser,
                Permissions.GROUP_PRIVATE, asOther, Permissions.PUBLIC);
    }

    @Test(enabled=false)
    public void test_user_RWRW_world_PUBLIC() throws Exception {
        newUserImagePermissionsAsUserToPermissions(false, asUser,
                Permissions.GROUP_PRIVATE, asWorld, Permissions.PUBLIC);
    }

    @Test(enabled=false)
    public void test_user_RWRW_pi_PUBLIC() throws Exception {
        newUserImagePermissionsAsUserToPermissions(true, asUser,
                Permissions.GROUP_PRIVATE, asPI, Permissions.PUBLIC);
    }

    @Test(enabled=false)
    public void test_user_RWRW_root_PUBLIC() throws Exception {
        newUserImagePermissionsAsUserToPermissions(true, asUser,
                Permissions.GROUP_PRIVATE, asRoot, Permissions.PUBLIC);
    }

    @Test(enabled=false, groups = { "ticket:397", "broken" })
    public void testCheckInitialParameters() throws Exception {
        Assert.fail("USER CAN CURRENTLY JUST PASS IN WHATEVER OWNER THEY WANT.");
        // UNTAINT
    }

    // ~ Helpers
    // =========================================================================

    protected void newUserImagePermissionsAsUserToPermissions(boolean ok,
            Login owner, Permissions orig, Login changer, Permissions target)
            throws ValidationException {
        ServiceFactory factory = new ServiceFactory(owner);
        ServiceFactory factory2 = new ServiceFactory(changer);

        Image i;

        // via IAdmin
        try {
            i = new Image();
            i.getDetails().setPermissions(orig);
            i.setName("test");
            i = factory.getUpdateService().saveAndReturnObject(i);

            factory2.getAdminService().changePermissions(i, target);
            if (!ok) {
                Assert.fail("secvio!");
            }
        } catch (SecurityViolation sv) {
            if (ok) {
                throw sv;
            }
        }

        // via Details
        try {
            i = new Image();
            i.getDetails().setPermissions(orig);
            i.setName("test");
            i = factory.getUpdateService().saveAndReturnObject(i);

            i.getDetails().setPermissions(target);
            factory2.getUpdateService().saveObject(i);
            if (!ok) {
                Assert.fail("secvio!");
            }
        } catch (SecurityViolation sv) {
            if (ok) {
                throw sv;
            }
        }
    }

}