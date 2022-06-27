/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.utests;

import org.testng.annotations.Factory;

/**
 * Long-running test case which outlines the problems
 */
public class SixteenBitLosslessJPEG2000LongRunningTest {

  @Factory
  public Object[] factoryMethod() {
    return new Object[] {new SixteenBitLosslessJPEG2000Test(1)};
  }

}
