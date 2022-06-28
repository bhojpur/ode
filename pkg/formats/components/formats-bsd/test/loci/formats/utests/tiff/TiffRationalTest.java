package loci.formats.utests.tiff;

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
