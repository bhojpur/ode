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

import ode.services.server.Entry;

import org.jmock.MockObjectTestCase;
import org.testng.annotations.Test;

/**
 * Creates sessions and tests the various ways they can be destroyed. Initially
 * this was used to manually inspect in a profiler whether or not
 * all related instances were being properly cleaned up.
 */
public class ServerEntryTest extends MockObjectTestCase {

    @Test(groups = "integration")
    public void testCreation() throws Exception {
        final Entry e = new Entry("ODE.server.test");
        class Work extends Thread {
            @Override
            public void run() {
                e.start();
            }
        }
        Work work = new Work();
        work.start();

        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() < start + 5000L) {
            // try
        }

        // Shutdown & test
        e.shutdown(false);

        assertEquals(0, e.status());
    }

}