/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.utests.tiff;

import java.io.IOException;

import loci.formats.FormatException;

public class NonUniformRowsPerStripMock extends BaseTiffMock {

  public NonUniformRowsPerStripMock() throws FormatException, IOException {
    super();
  }

  @Override
  public int[] getRowsPerStrip() {
    return new int[] { 1, 2, 3 };
  }

}
