package ode.util;

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

import java.math.BigInteger;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test of the behavior of {@link Counter}.
 */
public class CounterTest {

    /**
     * Test that the count stays in step as expected.
     * Given the {@code byte[]}-based implementation, crosses a couple of byte overflow boundaries in testing.
     */
    @Test
    public void testCounting() {
        final Counter actual = new Counter();
        for (int expected = 0; expected < 75000; expected++, actual.increment()) {
            Assert.assertEquals(actual.asBigInteger(), BigInteger.valueOf(expected));
        }
    }

    /**
     * Test the reset behavior of counters.
     */
    @Test
    public void testReset() {
        final Counter actual = new Counter();
        Assert.assertEquals(actual.asBigInteger(), BigInteger.ZERO);
        actual.increment();
        Assert.assertEquals(actual.asBigInteger(), BigInteger.ONE);
        actual.reset();
        Assert.assertEquals(actual.asBigInteger(), BigInteger.ZERO);
        actual.increment();
        Assert.assertEquals(actual.asBigInteger(), BigInteger.ONE);
    }
}