/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.utests.tiff;

import java.io.IOException;

import loci.formats.FormatException;

public class RGBTiffMock extends BaseTiffMock {

  public RGBTiffMock() throws FormatException, IOException {
    super();
  }

  @Override
  public int[] getBitsPerSample() {
    return new int[] { 8, 8, 8 };
  }

  @Override
  public int getSamplesPerPixel() {
    return 3;
  }

}
