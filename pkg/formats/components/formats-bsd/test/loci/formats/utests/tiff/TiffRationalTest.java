/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.utests.tiff;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;
import loci.formats.tiff.TiffRational;

import org.testng.annotations.Test;

/**
 * Unit tests for TIFF rationals.
 */
public class TiffRationalTest {

  @Test
  public void testEqualTiffRational() {
    TiffRational a = new TiffRational(1, 4);
    TiffRational b = new TiffRational(1, 4);
    assertTrue(a.equals(b));
    assertTrue(a.equals((Object) b));
    assertEquals(0, a.compareTo(b));
  }
  
  @Test
  public void testEqualObject() {
    TiffRational a = new TiffRational(1, 4);
    Object b = new Object();
    assertTrue(!(a.equals(b)));
  }

  @Test
  public void testNotEqual() {
    TiffRational a = new TiffRational(1, 4);
    TiffRational b = new TiffRational(1, 5);
    assertTrue(!(a.equals(b)));
  }

  @Test
  public void testGreaterThan() {
    TiffRational a = new TiffRational(1, 4);
    TiffRational b = new TiffRational(1, 5);
    assertEquals(1, a.compareTo(b));
  }

  @Test
  public void testLessThan() {
    TiffRational a = new TiffRational(1, 5);
    TiffRational b = new TiffRational(1, 4);
    assertEquals(-1, a.compareTo(b));
  }
}
