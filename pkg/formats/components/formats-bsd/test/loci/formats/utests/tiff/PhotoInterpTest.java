/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.utests.tiff;

import static org.testng.AssertJUnit.assertEquals;
import loci.common.enumeration.EnumException;
import loci.formats.tiff.PhotoInterp;

import org.testng.annotations.Test;

public class PhotoInterpTest {

  @Test
  public void testLookupBlackIsZero() {
    PhotoInterp pi = PhotoInterp.get(1);
    assertEquals(PhotoInterp.BLACK_IS_ZERO, pi);
  }
  
  @Test(expectedExceptions={ EnumException.class })
  public void testUnknownCode() {
    PhotoInterp.get(-1);
  }
}
