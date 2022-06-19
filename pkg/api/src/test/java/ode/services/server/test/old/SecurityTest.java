package ode.services.server.test.old;

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

import java.util.UUID;

import static ode.rtypes.*;
import ode.SecurityViolation;
import ode.ServerError;

import org.testng.annotations.Test;

import Glacier2.CannotCreateSessionException;
import Glacier2.PermissionDeniedException;

public class SecurityTest extends IceTest {

    @Test(groups = "ticket:645", expectedExceptions = SecurityViolation.class)
    public void testSynchronizeLoginCacheShouldBeDisallowed() throws Exception {
        ode.client user = createUser();
        user.getSession().getAdminService().synchronizeLoginCache();
    }

    @Test(groups = "ticket:645")
    public void testGetEventContextShouldBeAllowed() throws Exception {
        ode.client user = createUser();
        user.getSession().getAdminService().getEventContext();
    }

    private ode.client createUser() throws ServerError,
            CannotCreateSessionException, PermissionDeniedException {
        ode.model.Experimenter e = new ode.model.ExperimenterI();
        e.setOmeName(rstring(UUID.randomUUID().toString()));
        e.setFirstName(rstring("ticket"));
        e.setLastName(rstring("645"));
        root.getSession().getAdminService().createUser(e, "default");

        ode.client user = new ode.client();
        user.createSession(e.getOmeName().getValue(), "");
        return user;
    }

}