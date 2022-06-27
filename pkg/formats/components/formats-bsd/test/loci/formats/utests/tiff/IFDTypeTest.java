/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.utests.tiff;

import static org.testng.AssertJUnit.assertEquals;
import loci.common.enumeration.EnumException;
import loci.formats.tiff.IFDType;

import org.testng.annotations.Test;

public class IFDTypeTest {

  @Test
  public void testLookupByte() {
    IFDType pi = IFDType.get(1);
    assertEquals(IFDType.BYTE, pi);
  }

  @Test(expectedExceptions={ EnumException.class })
  public void testUnknownCode() {
    IFDType.get(-1);
  }
}
