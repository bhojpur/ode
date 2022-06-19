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

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import net.sf.ehcache.Cache;
import ode.model.meta.Session;
import ode.services.messages.DestroySessionMessage;

import org.jmock.MockObjectTestCase;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the use of the client callback on session destruction
 */
public class ClientCallbackTest extends MockObjectTestCase {

    Cache cache;
    Session session;
    ode.client client;
    MockFixture fixture;

    @BeforeMethod(groups = "integration")
    public void setup() throws Exception {
        fixture = new MockFixture(this);
        client = fixture.newClient();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() throws Exception {
        if (client != null) {
            // doesn't throw
            client.closeSession();
        }
        if (fixture != null) {
            fixture.tearDown();
        }
    }

    @Test(groups = "integration")
    public void testBasic() throws Exception {
        session = fixture.session("uuid-basic");
        cache = fixture.cache();
        fixture.prepareServiceFactory(session, cache);
        client.createSession("a", "b");


        fixture.prepareClose(0);
        client.closeSession();
    }

    @Test(groups = "integration")
    public void testServerCloses() throws Exception {
        session = fixture.session("uuid-serverCloses");
        cache = fixture.cache();
        fixture.prepareServiceFactory(session, cache);
        client.createSession("a", "b");

        fixture.prepareClose(0);
        fixture.ctx.publishEvent(new DestroySessionMessage(this,
                "uuid-serverCloses"));
    }

    @Test(groups = "integration")
    public void testClientReceivesHeartRequest() throws Exception {
        session = fixture.session("uuid-clientReceivesHeartRequest");
        cache = fixture.cache();
        fixture.prepareServiceFactory(session, cache);
        client.createSession("a", "b");

        fixture.prepareClose(0);
        final CyclicBarrier barrier = new CyclicBarrier(2);
        Runnable r = new Runnable(){
            public void run() {
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }};
            client.onHeartbeat(r);
            barrier.await(10, TimeUnit.SECONDS);
            assertEquals(0, barrier.getNumberWaiting());
            assertFalse(barrier.isBroken());
    }
}