package loci.formats.utests;

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

import loci.formats.ImageReader;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests for checking the speed and accuracy of isThisType(String, boolean) in
 * IFormatReader.
 */
public class IsThisTypeTests {

  // The test file is 'C1979091.h5' from:
  // http://www.hdfgroup.org/training/other-ex5/sample-programs/convert/Conversion.html
  private static final String TEST_FILE = "test.h5";
  private static final long TIMEOUT = 2000;

  private ImageReader openReader;
  private ImageReader noOpenReader;

  @BeforeMethod
  public void setUp() {
    openReader = new ImageReader();
    noOpenReader = new ImageReader();
    noOpenReader.setAllowOpenFiles(false);
  }

  @Test
  public void testAccuracy() {
    boolean openReaderIsValid = openReader.isThisType(TEST_FILE);
    boolean noOpenReaderIsValid = noOpenReader.isThisType(TEST_FILE);

    assertEquals(openReaderIsValid, noOpenReaderIsValid);
    assertEquals(openReaderIsValid, false);
  }

  @Test
  public void testTypeCheckingSpeed() {
    long t0 = System.currentTimeMillis();
    openReader.isThisType(TEST_FILE);
    long t1 = System.currentTimeMillis();
    noOpenReader.isThisType(TEST_FILE);
    long t2 = System.currentTimeMillis();

    assertTrue((t1 - t0) < TIMEOUT);
    assertTrue((t2 - t1) < TIMEOUT);
  }

}
