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

import net.sf.ehcache.Cache;
import ode.model.meta.Session;
import ode.services.server.fire.Ring;

import org.jmock.MockObjectTestCase;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 */
public class ClusteredServerTest extends MockObjectTestCase {

    MockFixture fixture1, fixture2;

    @BeforeClass(groups = "integration")
    public void setup() throws Exception {
        fixture1 = new MockFixture(this, "a");
        fixture2 = new MockFixture(this, "b");
    }

    @Test(groups = "integration")
    public void testSimple() throws Exception {
        Session s = fixture1.session();
        Cache c = fixture1.cache();
        fixture1.prepareServiceFactory(s, c);
        fixture1.createServiceFactory("username", "client1");
        Thread.sleep(1000L);
        fixture1.prepareServiceFactory(s, c);
        fixture2.createServiceFactory("my-session-uuid", "client2");

        Ring ring = fixture1.ring();

        // Tests


    }

}