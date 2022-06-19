package ode.cmd.admin;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ode.conditions.ApiUsageException;
import ode.security.SecuritySystem;
import ode.security.auth.PasswordProvider;
import ode.security.auth.PasswordUtil;
import ode.services.server.test.AbstractServantTest;
import ode.services.util.Executor;
import ode.services.mail.MailUtil;
import ode.system.ServiceFactory;
import ode.cmd.ERR;
import ode.cmd.Helper;
import ode.cmd.Request;
import ode.cmd.Response;
import ode.cmd.ResetPasswordResponse;
import ode.cmd.Status;
import ode.cmd.HandleI.Cancel;

import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ResetPasswordRequestTest extends AbstractServantTest {

    @Override
    @BeforeClass
    protected void setUp() throws Exception {
        super.setUp();
    }

    protected ResetPasswordResponse assertRequest(
            final ResetPasswordRequestI req, Map<String, String> ctx) {

        final Status status = new Status();

        @SuppressWarnings("unchecked")
        List<Object> rv = (List<Object>) user.ex.execute(ctx, user
                .getPrincipal(), new Executor.SimpleWork(this, "testRequest") {
            @Transactional(readOnly = true)
            public List<Object> doWork(Session session, ServiceFactory sf) {

                // from HandleI.steps()
                List<Object> rv = new ArrayList<Object>();

                Helper helper = new Helper((Request) req, status,
                        getSqlAction(), session, sf);
                req.init(helper);

                int j = 0;
                while (j < status.steps) {
                    try {
                        status.currentStep = j;
                        rv.add(req.step(j));
                    } catch (Cancel c) {
                        throw c;
                    } catch (Throwable t) {
                        throw helper.cancel(new ERR(), t, "bad-step", "step",
                                "" + j);
                    }
                    j++;
                }

                return rv;
            }
        });

        // Post-process
        for (int step = 0; step < status.steps; step++) {
            Object obj = rv.get(step);
            req.buildResponse(step, obj);
        }

        Response rsp = req.getResponse();
        if (rsp instanceof ERR) {
            fail(rsp.toString());
        }

        return (ResetPasswordResponse) rsp;
    }

    @Test(expectedExceptions = ApiUsageException.class)
    public void testSendEmail() throws Exception {

        ResetPasswordRequestI req = new ResetPasswordRequestI(
                (MailUtil) user.ctx.getBean("mailUtil"),
                (PasswordUtil) user.ctx.getBean("passwordUtil"),
                (SecuritySystem) user.ctx.getBean("securitySystem"),
                (PasswordProvider) user.ctx.getBean("passwordProvider"));
        req.odename = "test";
        req.email = "user@mail";

        ResetPasswordResponse rsp = assertRequest(req, null);
    }

}