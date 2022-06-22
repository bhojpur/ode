package ode.system.utests;

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
import java.util.Properties;
import ode.conditions.ApiUsageException;
import ode.system.Login;

public class LoginTest {

    @Test(expectedExceptions = ApiUsageException.class)
    public void test_null_user() throws Exception {
        new Login(null, "");
    }

    @Test(expectedExceptions = ApiUsageException.class)
    public void test_null_password() throws Exception {
        new Login("", null);
    }

    @Test(expectedExceptions = ApiUsageException.class)
    public void test_null_user_ext() throws Exception {
        new Login(null, "", null, null);
    }

    @Test(expectedExceptions = ApiUsageException.class)
    public void test_null_password_ext() throws Exception {
        new Login("", null, null, null);
    }

    @Test
    public void test_asProperties() throws Exception {
        Login l = new Login("a", "b");
        Properties p = l.asProperties();
        Assert.assertNotNull(p.getProperty(Login.ODE_USER));
        Assert.assertNotNull(p.getProperty(Login.ODE_PASS));
        Assert.assertNull(p.getProperty(Login.ODE_GROUP));
        Assert.assertNull(p.getProperty(Login.ODE_EVENT));

        Assert.assertEquals(p.getProperty(Login.ODE_USER), "a");
        Assert.assertEquals(p.getProperty(Login.ODE_PASS), "b");
    }

    @Test
    public void test_asProperties_extNulls() throws Exception {
        Login l = new Login("a", "b", null, null);
        Properties p = l.asProperties();
        Assert.assertNotNull(p.getProperty(Login.ODE_USER));
        Assert.assertNotNull(p.getProperty(Login.ODE_PASS));
        Assert.assertNull(p.getProperty(Login.ODE_GROUP));
        Assert.assertNull(p.getProperty(Login.ODE_EVENT));

        Assert.assertEquals(p.getProperty(Login.ODE_USER), "a");
        Assert.assertEquals(p.getProperty(Login.ODE_PASS), "b");
    }

    @Test
    public void test_asProperties_ext() throws Exception {
        Login l = new Login("a", "b", "c", "d");
        Properties p = l.asProperties();
        Assert.assertNotNull(p.getProperty(Login.ODE_USER));
        Assert.assertNotNull(p.getProperty(Login.ODE_PASS));
        Assert.assertNotNull(p.getProperty(Login.ODE_GROUP));
        Assert.assertNotNull(p.getProperty(Login.ODE_EVENT));

        Assert.assertEquals(p.getProperty(Login.ODE_USER), "a");
        Assert.assertEquals(p.getProperty(Login.ODE_PASS), "b");
        Assert.assertEquals(p.getProperty(Login.ODE_GROUP), "c");
        Assert.assertEquals(p.getProperty(Login.ODE_EVENT), "d");
    }

    @Test
    public void test_getters() throws Exception {
        Login l = new Login("a", "b");

        Assert.assertNotNull(l.getName());
        Assert.assertEquals(l.getName(), "a");

        Assert.assertNotNull(l.getPassword());
        Assert.assertEquals(l.getPassword(), "b");

        Assert.assertNull(l.getGroup());
        Assert.assertNull(l.getEvent());

    }

    @Test
    public void test_getters_extNulls() throws Exception {
        Login l = new Login("a", "b", null, null);

        Assert.assertNotNull(l.getName());
        Assert.assertEquals(l.getName(), "a");

        Assert.assertNotNull(l.getPassword());
        Assert.assertEquals(l.getPassword(), "b");

        Assert.assertNull(l.getGroup());
        Assert.assertNull(l.getEvent());
    }

    @Test
    public void test_getters_ext() throws Exception {
        Login l = new Login("a", "b", "c", "d");

        Assert.assertNotNull(l.getName());
        Assert.assertEquals(l.getName(), "a");

        Assert.assertNotNull(l.getPassword());
        Assert.assertEquals(l.getPassword(), "b");

        Assert.assertNotNull(l.getGroup());
        Assert.assertEquals(l.getGroup(), "c");

        Assert.assertNotNull(l.getEvent());
        Assert.assertEquals(l.getEvent(), "d");

    }

}