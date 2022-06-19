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

import ode.api.IConfig;
import ode.api.ISession;
import ode.conditions.RemovedSessionException;
import ode.model.meta.Experimenter;
import ode.model.meta.Session;
import ode.system.Login;
import ode.system.OdeContext;
import ode.system.Principal;
import ode.system.ServiceFactory;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test(enabled=false, groups = { "broken" })
public class SessionTest {

    Login rootLogin = null; // (Login) OdeContext.getInstance("ode.client.test") .getBean("rootLogin");
    Principal rootPrincipal = null; // new Principal(rootLogin.getName(), "system", "Test");

    @Test(enabled=false)
    public void testServiceFactoryWithNormalUsageAcquiresSession() {
        ServiceFactory sf = new ServiceFactory(rootLogin);
        sf.getQueryService().get(Experimenter.class, 0L);
    }

    @Test(enabled=false)
    public void AndIfSessionIsLostReacquires() {
        ServiceFactory sf = new ServiceFactory();
        IConfig c1 = sf.getConfigService(), c2;
        sf.closeSession();
        try {
            c1.getServerTime();
            Assert.fail("should fail since session closed");
        } catch (Exception e) {
            // ok
        }
        // A new proxy should work
        c2 = sf.getConfigService();
        c2.getServerTime();

        // Just calling close session
        sf.getSessionService().closeSession(sf.getSession());
        try {
            sf.getQueryService().get(Experimenter.class, 0L);
            Assert.fail("Shouldn't be logged in");
        } catch (Exception e) {
            // ok
        }

    }

    @Test(enabled=false)
    public void testSimpleCreate() throws Exception {
        ServiceFactory sf = new ServiceFactory();
        ISession service = sf.getServiceByClass(ISession.class);

        Session s = service.createSession(rootPrincipal, rootLogin
                .getPassword());
        sf.setSession(s);
        Session s2 = sf.getSession();
        Assert.assertEquals(s, s2);
        service.closeSession(s);
    }

    @Test(enabled=false)
    public void testCreationByRoot() throws Exception {
        ServiceFactory sf = new ServiceFactory("ode.client.test");
        String name = sf.getAdminService().getEventContext()
                .getCurrentUserName();
        ServiceFactory root = new ServiceFactory(rootLogin);
        ISession sessions = root.getServiceByClass(ISession.class);
        Principal p = new Principal(name, "user", "Test");
        Session s = sessions.createSessionWithTimeout(p, 10 * 1000L);
        ServiceFactory sessionedSf = new ServiceFactory();
        sessionedSf.setSession(s);
        sessionedSf.getConfigService().getServerTime();
    }

    @Test(enabled=false, expectedExceptions = RemovedSessionException.class)
    public void testOthersCanKillASession() {
        ServiceFactory sf1 = new ServiceFactory(rootLogin), sf2 = new ServiceFactory(
                rootLogin);

        Session session = sf1.getSession();
        sf2.getSessionService().closeSession(session);
        sf1.getConfigService().getServerTime();
    }

}