package integration;

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

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import ode.ServerError;
import ode.cmd.CmdCallbackI;
import ode.cmd.DoAll;
import ode.cmd.ERR;
import ode.cmd.HandlePrx;
import ode.cmd.Request;
import ode.cmd.Response;
import ode.cmd.Status;
import ode.cmd.Timing;
import ode.sys.EventContext;

import org.testng.Assert;
import org.testng.annotations.Test;

import Ice.Current;

/**
 * Various uses of the {@link ode.cmd.CmdCallbackI} object.
 */
@SuppressWarnings("serial")
public class CmdCallbackTest extends AbstractServerTest {

    class TestCB extends CmdCallbackI {

        final CountDownLatch finished = new CountDownLatch(1);

        final AtomicInteger steps = new AtomicInteger();

        public TestCB(ode.client client, HandlePrx handle) throws ServerError {
            super(client, handle);
        }

        @Override
        public void step(int complete, int total, Current __current) {
            steps.incrementAndGet();
        }

        @Override
        public void onFinished(Response rsp, Status s, Current __current) {
            finished.countDown();
        }

        public void assertSteps(int expected) {
            Assert.assertEquals(steps.get(), expected);
        }

        public void assertFinished() {
            Assert.assertEquals(finished.getCount(), 0);
            Assert.assertFalse(isCancelled());
            Assert.assertFalse(isFailure());
            Response rsp = getResponse();
            if (rsp == null) {
                Assert.fail("null response");
            } else if (rsp instanceof ERR) {
                ERR err = (ERR) rsp;
                String msg = String.format("%s\ncat:%s\nname:%s\nparams:%s\n",
                        err, err.category, err.name, err.parameters);
                Assert.fail(msg);
            }
        }

        public void assertFinished(int expectedSteps) {
            assertFinished();
            assertSteps(expectedSteps);
        }

        public void assertCancelled() {
            Assert.assertEquals(finished.getCount(), 0);
            Assert.assertTrue(isCancelled());
        }
    }

    TestCB run(Request req) throws Exception {
        EventContext ec = newUserAndGroup("rw----");
        loginUser(ec);
        HandlePrx handle = client.getSession().submit(req);
        return new TestCB(client, handle);
    }

    // Timing
    // =========================================================================

    TestCB timing(int millis, int steps) throws Exception {
        Timing t = new Timing();
        t.millisPerStep = millis;
        t.steps = steps;
        return run(t);
    }

    @Test
    public void testTimingFinishesOnLatch() throws Exception {
        TestCB cb = timing(25, 4 * 10); // Runs 1 second
        cb.finished.await(1500, TimeUnit.MILLISECONDS);
        Assert.assertEquals(cb.finished.getCount(), 0);
        cb.assertFinished(10); // Modulus-10
    }

    @Test
    public void testTimingFinishesOnBlock() throws Exception {
        TestCB cb = timing(25, 4 * 10); // Runs 1 second
        cb.block(1500);
        cb.assertFinished(10); // Modulus-10
    }

    @Test
    public void testTimingFinishesOnLoop() throws Exception {
        TestCB cb = timing(25, 4 * 10); // Runs 1 second
        cb.loop(3, scalingFactor);
        cb.assertFinished(10); // Modulus-10
    }

    // DoAll
    // =========================================================================

    TestCB doAllOfNothing() throws Exception {
        return run(new DoAll());
    }

    TestCB doAllTiming(int count) throws Exception {
        Timing[] timings = new Timing[count];
        for (int i = 0; i < count; i++) {
            timings[i] = new Timing(3, 2); // 6 ms
        }
        return run(new DoAll(Arrays.<Request> asList(timings), null));
    }

    @Test
    public void testDoNothingFinishesOnLatch() throws Exception {
        TestCB cb = doAllOfNothing();
        cb.finished.await(5, TimeUnit.SECONDS);
        cb.assertCancelled();
    }

    @Test
    public void testDoNothingFinishesOnLoop() throws Exception {
        TestCB cb = doAllOfNothing();
        cb.loop(5, scalingFactor);
        cb.assertCancelled();
    }

    @Test
    public void testDoAllTimingFinishesOnLoop() throws Exception {
        TestCB cb = doAllTiming(3);
        cb.loop(3, scalingFactor);
        cb.assertFinished();
        // For some reason the number of steps is varying between 10 and 15
    }

}
