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

import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.fail;

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.ZipException;

import loci.formats.FormatException;
import loci.formats.codec.CodecOptions;
import loci.formats.codec.NikonCodecOptions;
import loci.formats.tiff.TiffCompression;

import org.testng.annotations.Test;

/**
 * Tests the various TIFF decompression schemes.
 */
public class TiffCompressionDecompressTest {

  private static final byte[] DATA = new byte[64];
  
  private static final CodecOptions OPTIONS = new CodecOptions();

  static {
    OPTIONS.width = 8;
    OPTIONS.height = 8;
    OPTIONS.bitsPerSample = 8;
    OPTIONS.interleaved = true;
    OPTIONS.littleEndian = true;
    OPTIONS.maxBytes = DATA.length;
  }

  @Test
  public void testUNCOMPRESSED() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.UNCOMPRESSED;
    assertNotNull(compression.decompress(DATA, OPTIONS));
  }

  @Test(expectedExceptions={ FormatException.class })
  public void testCCITT_1D() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.CCITT_1D;
    assertNotNull(compression.decompress(DATA, OPTIONS));
  }

  @Test(expectedExceptions={ FormatException.class })
  public void testGROUP_3_FAX() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.GROUP_3_FAX;
    assertNotNull(compression.decompress(DATA, OPTIONS));
  }

  @Test(expectedExceptions={ FormatException.class })
  public void testGROUP_4_FAX() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.GROUP_3_FAX;
    assertNotNull(compression.decompress(DATA, OPTIONS));
  }

  @Test
  public void testLZW() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.LZW;
    try {
      assertNotNull(compression.decompress(DATA, OPTIONS));
    }
    catch (FormatException e) {
      if (!(e.getCause() instanceof EOFException)) {
        fail("Unexpected exception: " + e);
      }
    }
  }

  // Needs to have a "properly" encoded JPEG, throws an NPE otherwise
  @Test(expectedExceptions={ NullPointerException.class })
  public void testJPEG() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.JPEG;
    assertNotNull(compression.decompress(DATA, OPTIONS));
  }

  @Test
  public void testPACK_BITS() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.PACK_BITS;
    assertNotNull(compression.decompress(DATA, OPTIONS));
  }

  @Test
  public void testPROPRIETARY_DEFLATE() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.PROPRIETARY_DEFLATE;
    try {
      assertNotNull(compression.decompress(DATA, OPTIONS));
    }
    catch (FormatException e) {
      if (!(e.getCause() instanceof ZipException)) {
        fail("Unexpected exception: " + e);
      }
    }
  }

  @Test
  public void testDEFLATE() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.DEFLATE;
    try {
      assertNotNull(compression.decompress(DATA, OPTIONS));
    }
    catch (FormatException e) {
      if (!(e.getCause() instanceof ZipException)) {
        fail("Unexpected exception: " + e);
      }
    }
  }

  @Test(expectedExceptions={ FormatException.class })
  public void testTHUNDERSCAN() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.THUNDERSCAN;
    assertNotNull(compression.decompress(DATA, OPTIONS));
  }

  // Needs to have "properly" encoded JP2, throws a runtime exception otherwise
  @Test(expectedExceptions={ RuntimeException.class })
  public void testJPEG_2000() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.JPEG_2000;
    assertNotNull(compression.decompress(DATA, OPTIONS));
  }

  // Needs to have "properly" encoded JP2, throws a runtime exception otherwise
  @Test(expectedExceptions={ RuntimeException.class })
  public void testJPEG_2000_LOSSY() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.JPEG_2000_LOSSY;
    assertNotNull(compression.decompress(DATA, OPTIONS));
  }

  // Needs to have a "properly" encoded JPEG, throws an NPE otherwise
  @Test(expectedExceptions={ NullPointerException.class })
  public void testALT_JPEG() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.ALT_JPEG;
    assertNotNull(compression.decompress(DATA, OPTIONS));
  }

  @Test
  public void testNIKON() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.NIKON;
    NikonCodecOptions options = new NikonCodecOptions();
    options.interleaved = OPTIONS.interleaved;
    options.littleEndian = OPTIONS.littleEndian;
    options.maxBytes = OPTIONS.maxBytes;
    options.curve = new int[] { 1 };
    options.vPredictor = new int[] { 1 };
    options.lossless = true;
    options.split = 1;
    assertNotNull(compression.decompress(DATA, options));
  }

  @Test(expectedExceptions={ FormatException.class })
  public void testLURAWAVE() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.LURAWAVE;
    assertNotNull(compression.decompress(DATA, OPTIONS));
  }
}
