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

import static ode.rtypes.rstring;

import java.util.UUID;

import ode.conditions.ValidationException;
import ode.system.Login;
import ode.ServerError;
import ode.api.IQueryPrx;
import ode.api.IUpdatePrx;
import ode.api.ServiceFactoryPrx;
import ode.model.Experimenter;
import ode.model.ExperimenterGroup;
import ode.model.ExperimenterGroupI;
import ode.model.ExperimenterI;
import ode.model.Image;
import ode.model.ImageI;
import ode.sys.Roles;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Glacier2.CannotCreateSessionException;
import Glacier2.PermissionDeniedException;

@Test(enabled=false, groups = { "broken", "client", "integration", "security", "ticket:52", "init" })
public class AbstractChangeDetailClientTest extends AbstractSecurityTest {

    private String user_name = "USER:" + UUID.randomUUID().toString();

    private String other_name = "OTHER:" + UUID.randomUUID().toString();

    private String world_name = "WORLD:" + UUID.randomUUID().toString();

    private String pi_name = "PI:" + UUID.randomUUID().toString();

    private String pi_group = "PIGRP:" + UUID.randomUUID().toString();

    private String other_grp = "OTHERGRP:" + UUID.randomUUID().toString();

    protected Login asRoot, asUser, asOther, asWorld, asPI;

    protected Experimenter toRoot, toUser, toOther, toWorld, toPI;

    protected ExperimenterGroup toSystem, toUserGroup, toOtherGroup, toPIGroup;

    // ~ Testng Adapter
    // =========================================================================
    @BeforeClass
    public void createUsersAndImages() throws Exception {
        init();

        ServiceFactoryPrx sf = c.createSession();
        
        Roles roles = sf.getAdminService().getSecurityRoles();
        toRoot = new ExperimenterI(roles.rootId, false);
        toSystem = new ExperimenterGroupI(roles.systemGroupId, false);
        toUserGroup = new ExperimenterGroupI(roles.userGroupId, false);

        toOtherGroup = new ExperimenterGroupI();
        toOtherGroup.setName(rstring(other_grp));
        toOtherGroup = new ExperimenterGroupI(rootAdmin
                .createGroup(toOtherGroup), false);

        toPIGroup = new ExperimenterGroupI();
        toPIGroup.setName(rstring(pi_group));
        toPIGroup = new ExperimenterGroupI(rootAdmin.createGroup(toPIGroup),
                false);

        toUser = new ExperimenterI();
        toUser.setFirstName(rstring("test"));
        toUser.setLastName(rstring("test"));
        toUser.setOmeName(rstring(user_name));
        toUser = new ExperimenterI(rootAdmin.createUser(toUser, pi_group), false);

        toOther = new ExperimenterI();
        toOther.setFirstName(rstring("test"));
        toOther.setLastName(rstring("test"));
        toOther.setOmeName(rstring(other_name));
        toOther = new ExperimenterI(rootAdmin.createUser(toOther, pi_group),
                false);

        toWorld = new ExperimenterI();
        toWorld.setFirstName(rstring("test"));
        toWorld.setLastName(rstring("test"));
        toWorld.setOmeName(rstring(world_name));
        toWorld = new ExperimenterI(rootAdmin.createUser(toWorld, other_grp),
                false);

        toPI = new ExperimenterI();
        toPI.setFirstName(rstring("test"));
        toPI.setLastName(rstring("test"));
        toPI.setOmeName(rstring(pi_name));
        toPI = new ExperimenterI(rootAdmin.createUser(toPI, pi_group), false);

        rootAdmin.setGroupOwner(toPIGroup, toPI);

        asRoot = super.rootLogin;
        asUser = new Login(user_name, "ode", pi_group, "Test");
        asOther = new Login(other_name, "ode", pi_group, "Test");
        asWorld = new Login(world_name, "ode");
        asPI = new Login(pi_name, "ode");

    }

    // ~ Helpers
    // =========================================================================
    protected Long managedImage(Login login) {
        try
        {
            ServiceFactoryPrx services = c.createSession(login.getName(), login.getPassword());
            Image i = new ImageI();
            i.setName(rstring("test"));
            i = (Image) services.getUpdateService().saveAndReturnObject(i);
            // They need to actual belong to the right people
            Assert.assertEquals(rootAdmin.lookupExperimenter(login.getName()).getId(),
                    rootQuery.get("Image", i.getId().getValue()).getDetails().getOwner()
                            .getId());
            return i.getId().getValue();
        } catch (CannotCreateSessionException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("Unimplemented exception.");
        } catch (PermissionDeniedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("Unimplemented exception.");
        } catch (ServerError e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("Unimplemented exception.");
        }
    }

    protected void createAsUserToOwner(Login login, Experimenter owner)
            throws ValidationException {
        try
        {
            ServiceFactoryPrx factory = c.createSession(login.getName(), login.getPassword());
            IUpdatePrx iUpdate = factory.getUpdateService();
            Image i = new ImageI();
            i.setName(rstring("test"));
            i.getDetails().setOwner(owner);
            iUpdate.saveObject(i);
        } catch (CannotCreateSessionException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("Unimplemented exception.");
        } catch (PermissionDeniedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("Unimplemented exception.");
        } catch (ServerError e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("Unimplemented exception.");
        }

    }

    protected void updateAsUserToOwner(Long imageId, Login login,
            Experimenter owner) throws ValidationException {
        try
        {
            ServiceFactoryPrx factory = c.createSession(login.getName(), login.getPassword());
            IQueryPrx iQuery = factory.getQueryService();
            Image i = (Image) iQuery.get("Image", imageId);
            i.getDetails().setOwner(owner);
            IUpdatePrx iUpdate = factory.getUpdateService();
            iUpdate.saveObject(i);
        } catch (CannotCreateSessionException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("Unimplemented exception.");
        } catch (PermissionDeniedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("Unimplemented exception.");
        } catch (ServerError e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("Unimplemented exception.");
        }
    }

    protected void createAsUserToGroup(Login login, ExperimenterGroup group)
            throws ValidationException {

        try {
            ServiceFactoryPrx factory = c.createSession(login.getName(), login.getPassword());
            IUpdatePrx iUpdate = factory.getUpdateService();
            Image i = new ImageI();
            i.setName(rstring("test"));
            i.getDetails().setGroup(group);
            iUpdate.saveObject(i);
        } catch (CannotCreateSessionException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("Unimplemented exception.");
        } catch (PermissionDeniedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("Unimplemented exception.");
        } catch (ServerError e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("Unimplemented exception.");
        }
    }

    protected void updateAsUserToGroup(Long imageId, Login login,
            ExperimenterGroup group) throws ValidationException {

        try {
            ServiceFactoryPrx factory = c.createSession(login.getName(), login.getPassword());
            IQueryPrx iQuery = factory.getQueryService();
            Image i = (Image) iQuery.get("Image", imageId);
            i.getDetails().setGroup(group);
            IUpdatePrx iUpdate = factory.getUpdateService();
            iUpdate.saveObject(i);
        } catch (CannotCreateSessionException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("Unimplemented exception.");
        } catch (PermissionDeniedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("Unimplemented exception.");
        } catch (ServerError e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("Unimplemented exception.");
        }
    }
}