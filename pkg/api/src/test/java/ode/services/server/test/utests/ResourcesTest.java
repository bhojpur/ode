package ode.services.server.test.utests;

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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ode.util.Resources;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ResourcesTest {

    int MAX_WAIT = 3;

    ScheduledExecutorService s;
    Resources r;
    TestEntry e;

    @BeforeMethod
    void startup() {
        s = Executors.newSingleThreadScheduledExecutor();
        r = new Resources(1, s);
    }

    @AfterMethod
    void shutdown() {
        if (r != null) {
            r.cleanup();
        }
    }

    /**
     * Can be used to pause the execution of the
     * {@link ScheduledExecutorService} by inserting a blocking task into the
     * single thread. Once {@link CountDownLatch#countDown()} is called on the
     * return value, then execution can resume.
     */
    CountDownLatch pause() throws Exception {
        final CountDownLatch entered = new CountDownLatch(1);
        final CountDownLatch exit = new CountDownLatch(1);
        s.execute(new Runnable() {
            public void run() {
                entered.countDown();
                try {
                    exit.await();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });
        Assert.assertTrue(entered.await(10, TimeUnit.SECONDS));
        return exit;
    }

    class TestEntry implements Resources.Entry {

        volatile CountDownLatch checkLatch = new CountDownLatch(2);
        volatile CountDownLatch cleanLatch = new CountDownLatch(1);

        volatile boolean checkValue = true;
        volatile boolean throwOnCheck = false;
        volatile boolean throwOnCleanup = false;

        public boolean checkWait() throws InterruptedException {
            return checkLatch.await(MAX_WAIT, TimeUnit.SECONDS);
        }

        public boolean cleanWait() throws InterruptedException {
            return cleanLatch.await(MAX_WAIT, TimeUnit.SECONDS);
        }

        public boolean check() {
            checkLatch.countDown();
            if (throwOnCheck) {
                throw new RuntimeException("They made me do it");
            }
            return checkValue;
        }

        public void cleanup() {
            cleanLatch.countDown();
            if (throwOnCleanup) {
                throw new RuntimeException("They made me do it again.");
            }
        }
    }

    @Test
    public void testSimple() throws Exception {
        e = new TestEntry();
        r.add(e);
        Assert.assertTrue(e.checkWait());
    }

    @Test
    public void testShouldBeRemoved() throws Exception {
        e = new TestEntry();
        r.add(e);
        Assert.assertEquals(1, r.size());
        Assert.assertTrue(e.checkWait());
        CountDownLatch resume = pause();
        e.checkValue = false;
        resume.countDown();
        Assert.assertTrue(e.cleanWait());
        Assert.assertEquals(0, r.size());
    }

    @Test
    public void testCheckFalseLeadsToRemove() throws Exception {
        e = new TestEntry();
        r.add(e);
        Assert.assertEquals(1, r.size());
        Assert.assertTrue(e.checkWait());
        CountDownLatch resume = pause();
        e.checkValue = false;
        resume.countDown();
        Assert.assertTrue(e.cleanWait());
        Assert.assertEquals(0, r.size());
    }

    @Test
    public void testCheckThrowsLeadsToRemove() throws Exception {
        e = new TestEntry();
        r.add(e);
        Assert.assertEquals(1, r.size());
        Assert.assertTrue(e.checkWait());
        CountDownLatch resume = pause();
        e.throwOnCheck = true;
        resume.countDown();
        Assert.assertTrue(e.cleanWait());
        Assert.assertEquals(0, r.size());
    }

    @Test
    public void testCleanupThrowsIsCaught() throws Exception {
        e = new TestEntry();
        r.add(e);
        Assert.assertEquals(1, r.size());
        Assert.assertTrue(e.checkWait());
        CountDownLatch resume = pause();
        e.checkValue = false; // Force cleanup.
        e.throwOnCleanup = true;
        resume.countDown();
        Assert.assertTrue(e.cleanWait());
        Assert.assertEquals(0, r.size());
    }

}