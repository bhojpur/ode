package ode.model.internal;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit tests for the the {@link BooleanArrayCache}.
 * Setup and teardown provide each test method with a fresh cache.
 */
public class BooleanArrayCacheTest {

    private BooleanArrayCache cache = null;

    /**
     * Set up a cache for each test method.
     */
    @BeforeMethod
    public void setupNewCache() {
        cache = new BooleanArrayCache();
    }

    /**
     * Retire the cache after each test method.
     */
    @AfterClass
    public void teardownCache() {
        cache = null;
    }

    /**
     * Generate a Boolean array from a string. The <em>last</em> character of the string corresponds to the array's zero index.
     * If passed {@code null} then also returns {@code null}.
     * @param bits a bit string
     * @return the corresponding Boolean array
     */
    private static boolean[] fromString(String bits) {
        if (bits == null) {
            return null;
        }
        bits = new StringBuilder(bits).reverse().toString();
        final boolean[] rv = new boolean[bits.length()];
        for (int index = 0; index < rv.length; index++) {
            rv[index] = bits.charAt(index) != '0';
        }
        return rv;
    }

    /**
     * Test for cache hits with bit-strings that differ only in how much {@code false}-padding they have at higher indices.
     * @param x one array
     * @param y another array
     */
    @Test(dataProvider = "similar arrays")
    public void testSimilar(String x, String y) {
        final boolean[] cachedX = cache.getArrayFor(fromString(x));
        final boolean[] cachedY = cache.getArrayFor(fromString(y));
        Assert.assertTrue(cachedX == cachedY);
    }

    /**
     * Test for cache misses with bit-strings that differ in ways beyond how much {@code false}-padding they have at higher indices.
     * @param x one array
     * @param y another array
     */
    @Test(dataProvider = "differing arrays")
    public void testDifferent(String x, String y) {
        final boolean[] cachedX = cache.getArrayFor(fromString(x));
        final boolean[] cachedY = cache.getArrayFor(fromString(y));
        Assert.assertFalse(cachedX == cachedY);
    }

    /**
     * Turn sets of bit-strings into ordered pairs of different bit-strings.
     * @param sets of bit-strings
     * @return ordered pairs of bit-strings
     */
    private static Object[][] providePairs(Object[][] sets) {
        final List<Object[]> pairs = new ArrayList<Object[]>();
        for (final Object[] set : sets) {
            for (int index1 = 0; index1 < set.length; index1++) {
                for (int index2 = 0; index2 < set.length; index2++) {
                    if (index1 != index2) {
                        pairs.add(new Object[] {set[index1], set[index2]});
                    }
                }
            }
        }
        return pairs.toArray(new Object[pairs.size()][]);
    }

    /**
     * @return test cases for {@link #testSimilar(String, String)}
     */
    @DataProvider(name = "similar arrays")
    public Object[][] provideSimilar() {
        final String[][] sets = new String[][] {
                {null, "", "0", "00"},
                {"1", "01", "001"},
                {"10", "010"},
                {"101", "0101"},
                {"110", "0110"}};
        return providePairs(sets);
    }

    /**
     * @return test cases for {@link #testDifferent(String, String)}
     */
    @DataProvider(name = "differing arrays")
    public Object[][] provideDifferent() {
        final String[][] sets = new String[][] {
                {"0", "1", "10", "100"},
                {"01", "10", "11"},
                {"101", "010"},
                {"10", "01", "1010", "0101"}};
        return providePairs(sets);
    }

    /**
     * Test that array mutation is detected by the cache.
     */
    @Test(expectedExceptions = IllegalStateException.class)
    public void testCacheViolation() {
        boolean[] array = new boolean[] {false, true, false, true};
        cache.getArrayFor(array);
        array[0] = true;
        array = new boolean[] {false, true, false, true};
        cache.getArrayFor(array);
    }

    /**
     * Test that an array can be cached even when it uses all the bits of a {@code long}.
     */
    @Test
    public void testMaximumBooleans() {
        final boolean[] initialArray = new boolean[64];
        Arrays.fill(initialArray, true);
        final boolean[] cachedArray = cache.getArrayFor(initialArray);
        Assert.assertEquals(cachedArray.length, initialArray.length);
    }

    /**
     * Test that an array cannot be cached when it uses more bits than a {@code long} affords.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTooManyBooleans() {
        final boolean[] array = new boolean[65];
        Arrays.fill(array, true);
        cache.getArrayFor(array);
    }
}