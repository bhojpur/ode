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
import ode.system.Server;

public class ServerTest {

    @Test(expectedExceptions = ApiUsageException.class)
    public void test_null_host()  {
        new Server(null);
    }

    @Test(expectedExceptions = ApiUsageException.class)
    public void test_bad_port() {
        new Server("", -100);
    }

    @Test
    public void test_asProperties() {
        Server s = new Server("a");
        Properties p = s.asProperties();
        Assert.assertNotNull(p.getProperty(Server.ODE_HOST));
        Assert.assertNotNull(p.getProperty(Server.ODE_PORT));
        Assert.assertEquals(p.getProperty(Server.ODE_HOST), "a");
        Assert.assertEquals(p.getProperty(Server.ODE_PORT), "1099");
    }

    @Test
    public void test_asProperties_ext() {
        Server l = new Server("a", 999);
        Properties p = l.asProperties();
        Assert.assertNotNull(p.getProperty(Server.ODE_HOST));
        Assert.assertNotNull(p.getProperty(Server.ODE_PORT));

        Assert.assertEquals(p.getProperty(Server.ODE_HOST), "a");
        Assert.assertEquals(p.getProperty(Server.ODE_PORT), "999");
    }

    @Test
    public void test_getters() {
        Server s = new Server("a");

        Assert.assertNotNull(s.getHost());
        Assert.assertEquals(s.getHost(), "a");
        Assert.assertEquals(s.getPort(), 1099);

    }

    @Test
    public void test_getters_ext() {
        Server s = new Server("a", 999);

        Assert.assertNotNull(s.getHost());
        Assert.assertEquals(s.getHost(), "a");
        Assert.assertEquals(s.getPort(), 999);

    }

}