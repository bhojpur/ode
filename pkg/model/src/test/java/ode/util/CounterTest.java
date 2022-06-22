package ome.util;

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