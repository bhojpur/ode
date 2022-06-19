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

import java.io.File;

import ode.system.Login;
import ode.system.OdeContext;
import ode.api.IUpdatePrx;
import ode.api.IUpdatePrxHelper;
import ode.constants.UPDATESERVICE;
import ode.model.ImageI;

import org.springframework.util.ResourceUtils;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Ice.RouterPrx;

@Test(groups = { "integration", "server" })
public abstract class IceTest {

    protected OdeContext context;
    protected Login rootLogin;
    protected ode.client ice = null, root = null;
    protected Ice.Communicator ic = null;

    @BeforeMethod
    public void setUp() throws Exception {
        context = OdeContext.getInstance("ode.client.test");
        rootLogin = (Login) context.getBean("rootLogin");
        File f1 = ResourceUtils.getFile("classpath:ice.config");
        File f2 = ResourceUtils.getFile("classpath.local.properties");
        ice = new ode.client(f1, f2);
        ice.createSession(null, null);
        root = new ode.client();
        root.createSession(rootLogin.getName(), rootLogin.getPassword());
        ic = ice.getCommunicator();
    }

    @AfterMethod
    public void tearDown() throws Exception {
        ice.closeSession();
        root.closeSession();
    }

    // ~ Helpers
    // =========================================================================

    protected Glacier2.RouterPrx getRouter(Ice.Communicator ic)
            throws Exception {
        RouterPrx defaultRouter = ic.getDefaultRouter();
        Glacier2.RouterPrx router = Glacier2.RouterPrxHelper
                .checkedCast(defaultRouter);
        return router;
    }

    protected Glacier2.SessionPrx getSession(Ice.Communicator ic)
            throws Exception {
        Glacier2.SessionPrx sessionPrx = getRouter(ic).createSession("josh",
                "test");
        return sessionPrx;
    }

    protected void closeSession(Ice.Communicator ic) throws Exception {
        getRouter(ic).destroySession();
    }

    protected IUpdatePrx checkUpdateService(Ice.Communicator ic)
            throws Exception {
        Ice.ObjectPrx base = ic.stringToProxy(UPDATESERVICE.value
                + ":default -p 10000");
        if (base == null) {
            throw new RuntimeException("Cannot create proxy");
        }

        IUpdatePrx iUpdate = IUpdatePrxHelper.checkedCast(base);
        if (iUpdate == null) {
            throw new RuntimeException("Invalid proxy");
        }

        iUpdate.saveAndReturnObject(new ImageI());
        return iUpdate;
    }
}
