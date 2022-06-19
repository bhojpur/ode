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

import javax.sql.DataSource;

import ode.system.Login;
import ode.system.OdeContext;
import ode.client;
import ode.api.IAdminPrx;
import ode.api.IQueryPrx;
import ode.api.IUpdatePrx;
import ode.api.ServiceFactoryPrx;

import org.springframework.jdbc.core.JdbcTemplate;

import org.testng.annotations.Test;

@Test(enabled = false, groups = { "broken", "client", "integration", "security" })
public class AbstractSecurityTest {

    protected OdeContext context = null;

    protected client c;

    protected ode.system.ServiceFactory tmp = null; // new ode.system.ServiceFactory(context);

    protected DataSource dataSource = null; // (DataSource) tmp.getContext().getBean( "ode.security.test");

    protected JdbcTemplate jdbc = null; // new JdbcTemplate(dataSource);

    protected Login rootLogin = null; // (Login) tmp.getContext().getBean("rootLogin");

    protected ServiceFactoryPrx rootServices;

    protected ServiceFactoryPrx serviceFactory;

    protected IAdminPrx rootAdmin;

    protected IQueryPrx rootQuery;

    protected IUpdatePrx rootUpdate;

    // shouldn't use beforeTestClass here because called by all subclasses
    // in their beforeTestClass i.e. super.setup(); ...
    protected void init() throws Exception {

        // See ticket:10560 for issues with ode.db.poolsize
        context = OdeContext.getInstance("ODE.security.test");

        // TODO: Make work
        c = new client();
        serviceFactory = c.createSession(rootLogin.getName(), rootLogin.getPassword());

        // Server services
        rootUpdate = serviceFactory.getUpdateService();
        rootQuery = serviceFactory.getQueryService();
        rootAdmin = serviceFactory.getAdminService();

        try {
            rootQuery.get("Experimenter", 0l);
        } catch (Throwable t) {
            // TODO no, no, really. This is ok. (And temporary)
        }
    }
}