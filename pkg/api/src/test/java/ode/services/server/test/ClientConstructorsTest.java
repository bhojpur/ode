package ode.services.server.test;

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

import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "integration", "server", "client" })
public class ClientConstructorsTest {

    public void testHostConstructor() throws Exception {
        ode.client c = new ode.client("localhost");
        c.createSession("root", "ode");
        c.closeSession();
        c.createSession("root", "ode");
        c.closeSession();
    }

    public void testInitializationDataConstructor() throws Exception {
        Ice.InitializationData id = new Ice.InitializationData();
        id.properties = Ice.Util.createProperties();
        id.properties.setProperty("ode.host", "localhost");
        id.properties.setProperty("ode.user", "root");
        id.properties.setProperty("ode.pass", "ode");
        ode.client c = new ode.client(id);
        c.createSession();
        c.closeSession();
        c.createSession();
        c.closeSession();
    }

    public void testMainArgsConstructor() throws Exception {
        String[] args = new String[] {"--ode.host=localhost","--ode.user=root", "--ode.pass=ode"};
        ode.client c = new ode.client(args);
        c.createSession();
        c.closeSession();
        c.createSession();
        c.closeSession();
    }
    
    public void testMapConstructor() throws Exception {
        Properties p = new Properties();
        p.put("ode.host","localhost");
        p.put("ode.user","root");
        p.put("ode.pass","ode");
        ode.client c = new ode.client(p);
        c.createSession();
        c.closeSession();
        c.createSession();
        c.closeSession();
    }
    
    public void testMainArgsGetsIcePrefix() throws Exception {
        String[] args = new String[] {"--Ice.MessageSizeMax=10","--ode.host=localhost","--ode.user=root", "--ode.pass=ode"};
        ode.client c = new ode.client(args);
        c.createSession();
        Assert.assertEquals("10", c.getProperty("Ice.MessageSizeMax"));
        c.closeSession();
    }

    
}