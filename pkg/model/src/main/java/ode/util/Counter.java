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

import org.apache.commons.lang.ArrayUtils;

/**
 * A simple integral counter that can be incremented. Its properties include,
 * <ul>
 * <li><em>unbounded</em></li>
 * <li>{@link #increment()} typically does <em>not</em> require a heap allocation</li>
 * <li><em>not</em> thread-safe.</li>
 * </ul>
 */
public class Counter {

    private byte[] count;

    /**
     * Create a new counter starting at zero.
     */
    public Counter() {
        reset();
    }

    /**
     * Increment the value of this counter by one.
     */
    public void increment() {
        for (int index = count.length; index > 0;) {
            if (++count[--index] != 0) {
                return;
            }
        }
        count = new byte[count.length + 1];
        count[0] = 1;
    }

    /**
     * Reset this counter to zero.
     */
    public void reset() {
        count = ArrayUtils.EMPTY_BYTE_ARRAY;
    }

    /**
     * @return the integer value of this counter
     */
    public BigInteger asBigInteger() {
        return new BigInteger(1, count);
    }
}