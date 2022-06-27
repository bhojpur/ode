/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.utests.tiff;

import java.io.IOException;

import loci.formats.FormatException;

public class BitsPerSampleSamplesPerPixelMismatchMock extends RGBTiffMock {

  public BitsPerSampleSamplesPerPixelMismatchMock()
    throws FormatException, IOException
  {
    super();
  }

  @Override
  public int[] getBitsPerSample() {
    return new int[] { 8, 8 };
  }

}
