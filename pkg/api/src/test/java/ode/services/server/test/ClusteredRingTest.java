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

import java.util.Set;

import ode.services.server.fire.Ring;
import ode.services.server.test.mock.MockFixture;
import ode.services.messages.CreateSessionMessage;

import org.jmock.MockObjectTestCase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups ={"integration","mock"})
public class ClusteredRingTest extends MockObjectTestCase {

    MockFixture fixture1, fixture2;
    Ring ring1, ring2;
    PlatformTransactionManager tm;
    TransactionStatus tx;

    @BeforeMethod
    public void createFixtures() throws Exception {

        // To run everything in a single transaction, uncomment;
        // tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
        // TransactionAttribute ta = new DefaultTransactionAttribute(){
        // @Override
        // public boolean rollbackOn(Throwable ex) {
        // return false;
        // }
        // };
        // tx = tm.getTransaction(ta);

        fixture1 = new MockFixture(this);
        fixture2 = new MockFixture(this);
    }

    @AfterMethod(alwaysRun = true)
    public void teardownFixtures() throws Exception {
        if (fixture1 != null) {
            fixture1.tearDown();
        }
        if (fixture2 != null) {
            fixture2.tearDown();
        }
        // tm.commit(tx);
        Thread.sleep(1000L); // Give everything time to shutdown.
    }

    @Test
    public void testAfterStartupNoNonActiveManagersArePresent()
            throws Exception {
        String key = "manager-foo";
        try {
            /*
            MUST BE PORTED TO SQLACTION!
            fixture1.jdbc.update(
                    "insert into session_ring (key, value) values (?,?)", key,
                    "bar");
            */
        } catch (DataIntegrityViolationException dive) {
            // then this test has failed before. oh well.
        }

        // Creating a new ring should automatically clean up "foo"
        MockFixture fixture3 = new MockFixture(this);
        fixture3.tearDown();

        Set<String> managers = fixture1.server.getRing().knownManagers();
        assertFalse(managers.contains(key));
        /*
        MUST BE PORTED TO SQLACTION!
        assertEquals(0, fixture1.jdbc.queryForInt(
                "select count(key) from session_ring where key = ?", key));
        */
    }

    @Test
    public void testAddedSessionGetsUuidOfManager() throws Exception {
        fixture1.ctx.publishEvent(new CreateSessionMessage(this, "test-for-uuid"));
        assertTrue(fixture1.server.getRing().checkPassword("test-for-uuid"));
        /*
        MUST BE PORTED TO SQLACTION!
        String value = fixture1.jdbc.queryForObject("select value from session_ring where key = ?", String.class, "session-test-for-uuid");
        assertEquals(fixture1.server.getRing().uuid, value);
        */
    }

    @Test
    public void testHandlesMissingServers() throws Exception {
        fail();
    }

    @Test
    public void testRemovesUnreachable() throws Exception {
        fail();
    }

    @Test
    public void testReaddsSelfIfTemporarilyUnreachable() throws Exception {
        fail();
    }

    @Test
    public void testAllSessionRemovedIfDiscoveryFails() throws Exception {
        fail();
    }

    @Test
    public void testAllSessionsReassertedIfSessionComesBackOnline()
            throws Exception {
        fail();
    }

    @Test
    public void testIfRedirectIsDeletedAnotherHostTakesOver() throws Exception {
        fail();
    }
}