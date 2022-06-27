/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.utests.tiff;

import static org.testng.AssertJUnit.assertEquals;
import loci.common.enumeration.EnumException;
import loci.formats.tiff.TiffCompression;

import org.testng.annotations.Test;

public class TiffCompressionTest {

  @Test
  public void testLookupUncompressed() {
    TiffCompression pi = TiffCompression.get(1);
    assertEquals(TiffCompression.UNCOMPRESSED, pi);
  }
  
  @Test(expectedExceptions={ EnumException.class })
  public void testUnknownCode() {
    TiffCompression.get(-1);
  }
}
