package ode.services.server.test.mock;

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

import ode.security.SecuritySystem;
import ode.services.server.fire.Ring;
import ode.services.server.util.ServerConfiguration;
import ode.services.sessions.SessionManager;
import ode.services.sessions.SessionProvider;
import ode.services.util.Executor;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ServerConfigurationTest extends MockObjectTestCase {

    Ring ring;
    ServerConfiguration config;
    SessionManager sm;
    SessionProvider sp;
    SecuritySystem ss;
    Executor ex;
    Mock m_sm, m_ss, m_ex;
    
    @BeforeClass(groups = "integration")
    public void setup() throws Exception {
        ring = new Ring("uuid", null);
    }
    
    @Test(groups = "integration")
    public void testCreation() throws Exception {
        Ice.InitializationData id = new Ice.InitializationData();
        id.properties = Ice.Util.createProperties();
        id.properties.setProperty("ServerAdapter.Endpoints", "default -h 127.0.0.1");
        config = new ServerConfiguration(id, ring, sm, sp, ss, ex, 10000);
    }
    
    @Test(groups = "integration")
    public void testCreationAndDestruction() throws Exception {
        Ice.InitializationData id = new Ice.InitializationData();
        id.properties = Ice.Util.createProperties();
        id.properties.setProperty("ServerAdapter.Endpoints", "default -h 127.0.0.1");
        config = new ServerConfiguration(id, ring, sm, sp, ss, ex, 10000);
        config.destroy();
    }

}