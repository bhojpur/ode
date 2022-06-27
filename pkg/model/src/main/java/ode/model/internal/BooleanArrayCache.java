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

import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ode.util.Counter;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Memory-sensitive cache of Boolean arrays. They are expected to be <em>immutable</em>.
 * Shorter arrays are considered identical to longer arrays with the higher indices set to {@code false}.
 * This class is <em>not</em> thread-safe.
 */
class BooleanArrayCache {

    private static Logger logger = LoggerFactory.getLogger(BooleanArrayCache.class);

    /* can be resurrected by getArrayFor */
    private SoftReference<? extends Map<Long, boolean[]>> cacheRef = new SoftReference<>(new HashMap<Long, boolean[]>());

    private final Counter hits = new Counter();  // may grow very large
    private long misses = 0;
    private long resets = 0;

    private final DelayTracker logWhen = new DelayTracker(50 * 60);  // fifty minutes

    /**
     * Calculate a {@code long} that corresponds to the bit pattern of the array.
     * <code>numberForArray(new boolean[] {false, true, false, false}) == 2</code>
     * @param array the array for which to calculate a number
     * @return the number for the array's bit pattern
     */
    private static long numberForArray(boolean[] array) {
        if (array.length > Long.SIZE) {
            throw new IllegalArgumentException("array may have no more elements than " + Long.SIZE);
        }
        long number = 0;
        long nextBit = 1;
        for (final boolean bit : array) {
            if (bit) {
                number |= nextBit;
            }
            nextBit <<= 1;
        }
        return number;
    }

    /**
     * Return an array with the same {@code true} indices as the given array.
     * If passed {@code null} then returns an empty array.
     * @param array an array
     * @return a corresponding cached array, may be the same as given
     */
    boolean[] getArrayFor(boolean[] array) {
        if (logWhen.isNow()) {
            logger.debug("cache stats: hits={}, misses={}, resets={}", hits.asBigInteger(), misses, resets);
        }
        if (array == null) {
            array = ArrayUtils.EMPTY_BOOLEAN_ARRAY;
        }
        final long number = numberForArray(array);
        Map<Long, boolean[]> cache = cacheRef.get();
        if (cache == null) {
            /* if the old cache grew too large then the garbage collector must have needed the space */
            logger.debug("resetting cache");
            cache = new HashMap<Long, boolean[]>();
            cacheRef = new SoftReference<>(cache);
            hits.reset();
            misses = 0;
            resets++;
        }
        boolean[] cached = cache.get(number);
        if (cached == null) {
            cache.put(number, array);
            misses++;
            return array;
        } else {
            if (numberForArray(cached) != number) {
                throw new IllegalStateException("cache violation");
            }
            hits.increment();
            return cached;
        }
    }

    /**
     * Provides a method for transforming a Boolean array.
     */
    interface Transformer {
        boolean[] transform(boolean[] array);
    }

    /**
     * Transforms the given array using the given transformer.
     * If passed {@code null} then passes the transformer an empty array.
     * The transformed array is passed through {@link #getArrayFor(boolean[])} before being returned.
     * @param transformer an array transform
     * @param array an array to transform
     * @return the transformed array
     */
    public boolean[] transform(Transformer transformer, boolean[] array) {
        if (array == null) {
            array = ArrayUtils.EMPTY_BOOLEAN_ARRAY;
        } else if (array.length > 0) {
            array = Arrays.copyOf(array, array.length);
        }
        return getArrayFor(transformer.transform(array));
    }

    /**
     * Helper class for delaying sufficiently between subsequent repeated actions.
     */
    private static class DelayTracker {
        /* given Java 8 could consider LocalDateTime */
        private final long intervalMs;
        private long delayUntil;

        /**
         * Enforce delay intervals of at least the given duration.
         * @param delaySeconds how many seconds to delay at minimum
         */
        DelayTracker(int delaySeconds) {
            intervalMs = delaySeconds * 1000L;
            setNextDelay();
        }

        /**
         * Set the delay to be the current time plus the configured interval.
         */
        private void setNextDelay() {
            delayUntil = System.currentTimeMillis() + intervalMs;
        }

        /**
         * Check if it is at least the configured delay since this method last returned {@code true}.
         * @return if it is now time to perform the act, because the delay is past
         */
        boolean isNow() {
            final boolean isNow = delayUntil <= System.currentTimeMillis();
            if (isNow) {
                setNextDelay();
            }
            return isNow;
        }
    }
}