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
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.fail;

import java.io.IOException;
import java.lang.reflect.Constructor;

import loci.formats.FormatException;
import loci.formats.tiff.IFD;
import loci.formats.tiff.PhotoInterp;
import loci.formats.tiff.TiffParser;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

@Test(groups={ "tiffParserTests" })
public class TiffParserTest {

  private TiffParser tiffParser;
  
  private BaseTiffMock mock;

  @Parameters({ "mockClassName" })
  @BeforeMethod
  public void setUp(String mockClassName) throws Exception {
    Class mockClass = Class.forName(mockClassName);
    Constructor constructor = mockClass.getDeclaredConstructor();
    mock = (BaseTiffMock) constructor.newInstance();
    tiffParser = mock.getTiffParser();
  }
  
  @Test
  public void testHeader() throws IOException {
    assertTrue(tiffParser.isValidHeader());
    assertTrue(tiffParser.checkHeader());
    assertFalse(tiffParser.isBigTiff());
  }

  @Test
  public void testFirstOffset() throws IOException {
    assertEquals(8, tiffParser.getFirstOffset());
  }

  @Test
  public void testGetPhotoInterp() throws IOException, FormatException {
    assertEquals(PhotoInterp.RGB,
                 tiffParser.getFirstIFD().getPhotometricInterpretation());
  }

  @Test
  public void testGetImageLength() throws IOException, FormatException {
    assertEquals(mock.getImageLength(),
                 tiffParser.getFirstIFD().getImageLength());
  }

  @Test
  public void testGetImageWidth() throws IOException, FormatException {
    assertEquals(mock.getImageWidth(), tiffParser.getFirstIFD().getImageWidth());
  }

  @Test
  public void testGetBitsPerSample() throws IOException, FormatException {
    int[] bitsPerSample = tiffParser.getFirstIFD().getBitsPerSample();
    assertNotNull(bitsPerSample);
    assertEquals(mock.getSamplesPerPixel(), bitsPerSample.length);
    int[] BITS_PER_SAMPLE = mock.getBitsPerSample();
    for (int i = 0; i < BITS_PER_SAMPLE.length; i++) {
      int a = BITS_PER_SAMPLE[i];
      long b = bitsPerSample[i];
      if (a != b) {
        fail(String.format(
            "Bits per sample offset %d not equivilent: %d != %d", i, a, b));
      }
    }
  }

  @Test
  public void testGetSamplesPerPixel() throws IOException, FormatException {
    assertEquals(mock.getSamplesPerPixel(),
                 tiffParser.getFirstIFD().getSamplesPerPixel());
  }

  @Test
  public void testGetStripOffsets() throws IOException, FormatException {
    long[] stripOffsets = tiffParser.getFirstIFD().getStripOffsets();
    assertNotNull(stripOffsets);
    int[] STRIP_OFFSETS = mock.getStripOffsets();
    assertEquals(STRIP_OFFSETS.length, stripOffsets.length);
    for (int i = 0; i < STRIP_OFFSETS.length; i++) {
      int a = STRIP_OFFSETS[i];
      long b = stripOffsets[i]; 
      if (a != b) {
        fail(String.format(
            "Strip offset %d not equivilent: %d != %d", i, a, b));
      }
    }
  }

  @Test
  public void testGetRowsPerStrip() throws IOException, FormatException {
    long[] rowsPerStrip = tiffParser.getFirstIFD().getRowsPerStrip();
    assertNotNull(rowsPerStrip);
    int[] ROWS_PER_STRIP = mock.getRowsPerStrip();
    assertEquals(ROWS_PER_STRIP.length, rowsPerStrip.length);
    for (int i = 0; i < ROWS_PER_STRIP.length; i++) {
      int a = ROWS_PER_STRIP[i];
      long b = rowsPerStrip[i]; 
      if (a != b) {
        fail(String.format(
            "Rows per strip %d not equivilent: %d != %d", i, a, b));
      }
    }
  }

  @Test
  public void testGetXResolution() throws IOException, FormatException {
    int tag = IFD.X_RESOLUTION;
    assertTrue(mock.getXResolution().equals(
        tiffParser.getFirstIFD().getIFDRationalValue(tag)));
  }

  @Test
  public void testGetYResolution() throws IOException, FormatException {
    int tag = IFD.Y_RESOLUTION;
    assertTrue(mock.getYResolution().equals(
        tiffParser.getFirstIFD().getIFDRationalValue(tag)));
  }

  @Test
  public void testGetResolutionUnit() throws IOException, FormatException {
    int tag = IFD.RESOLUTION_UNIT;
    assertEquals(mock.getResolutionUnit(),
                 tiffParser.getFirstIFD().getIFDIntValue(tag));
  }

  @Test(expectedExceptions={ FormatException.class })
  public void testNonUniformRowsPerStrip() throws IOException, FormatException {
    mock = new NonUniformRowsPerStripMock();
    tiffParser = mock.getTiffParser();
    assertTrue(tiffParser.checkHeader());
    tiffParser.getFirstIFD().getRowsPerStrip();
    mock.close();
  }

  @Test
  public void testBitsPerSampleMismatch() throws IOException, FormatException {
    mock = new BitsPerSampleSamplesPerPixelMismatchMock();
    tiffParser = mock.getTiffParser();
    assertTrue(tiffParser.checkHeader());
    IFD ifd = tiffParser.getFirstIFD();
    int[] bitsPerSample = ifd.getBitsPerSample();
    int[] mockBitsPerSample = mock.getBitsPerSample();
    assertEquals(bitsPerSample.length, ifd.getSamplesPerPixel());
    for (int i=0; i<mockBitsPerSample.length; i++) {
      assertEquals(bitsPerSample[i], mockBitsPerSample[i]);
    }
    mock.close();
  }

  // TODO: Test wrong type exceptions
}
