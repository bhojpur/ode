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
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.io.IOException;

import loci.formats.FormatException;
import loci.formats.codec.CodecOptions;
import loci.formats.codec.JPEG2000CodecOptions;
import loci.formats.tiff.IFD;
import loci.formats.tiff.TiffCompression;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the various TIFF compression schemes.
 */
public class TiffCompressionCompressTest {

  private static final int IMAGE_WIDTH = 64;

  private static final int IMAGE_LENGTH = 64;

  private static final int BITS_PER_PIXEL = 16;

  private IFD ifd = new IFD();

  private byte[] data;

  @BeforeMethod
  public void setUp() {
    ifd.put(IFD.IMAGE_WIDTH, IMAGE_WIDTH);
    ifd.put(IFD.IMAGE_LENGTH, IMAGE_LENGTH);
    ifd.put(IFD.BITS_PER_SAMPLE, new int[] { BITS_PER_PIXEL });
    ifd.put(IFD.SAMPLES_PER_PIXEL, 1);
    ifd.put(IFD.LITTLE_ENDIAN, Boolean.TRUE);
    data = new byte[IMAGE_WIDTH * IMAGE_LENGTH * (BITS_PER_PIXEL / 8)];
  }

  @Test
  public void testUNCOMPRESSED() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.UNCOMPRESSED;
    CodecOptions options = compression.getCompressionCodecOptions(ifd);
    byte[] compressed = compression.compress(data, options);
    assertNotNull(compressed);
    assertEquals(compressed.length, data.length);
  }

  @Test(expectedExceptions={ FormatException.class })
  public void testCCITT_1D() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.CCITT_1D;
    CodecOptions options = compression.getCompressionCodecOptions(ifd);
    compression.compress(data, options);
  }

  @Test(expectedExceptions={ FormatException.class })
  public void testGROUP_3_FAX() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.GROUP_3_FAX;
    CodecOptions options = compression.getCompressionCodecOptions(ifd);
    compression.compress(data, options);
  }

  @Test(expectedExceptions={ FormatException.class })
  public void testGROUP_4_FAX() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.GROUP_3_FAX;
    CodecOptions options = compression.getCompressionCodecOptions(ifd);
    compression.compress(data, options);
  }

  @Test
  public void testLZW() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.LZW;
    CodecOptions options = compression.getCompressionCodecOptions(ifd);
    byte[] compressed = compression.compress(data, options);
    assertNotNull(compressed);
  }

  @Test
  public void testJPEG() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.JPEG;
    ifd.put(IFD.BITS_PER_SAMPLE, new int[] { 8 });
    CodecOptions options = compression.getCompressionCodecOptions(ifd);
    byte[] compressed = compression.compress(data, options);
    assertNotNull(compressed);
  }

  @Test(expectedExceptions={ FormatException.class })
  public void testPACK_BITS() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.PACK_BITS;
    CodecOptions options = compression.getCompressionCodecOptions(ifd);
    compression.compress(data, options);
  }

  @Test
  public void testPROPRIETARY_DEFLATE() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.PROPRIETARY_DEFLATE;
    CodecOptions options = compression.getCompressionCodecOptions(ifd);
    byte[] compressed = compression.compress(data, options);
    assertNotNull(compressed);
  }

  @Test
  public void testDEFLATE() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.DEFLATE;
    CodecOptions options = compression.getCompressionCodecOptions(ifd);
    byte[] compressed = compression.compress(data, options);
    assertNotNull(compressed);
  }

  @Test(expectedExceptions={ FormatException.class })
  public void testTHUNDERSCAN() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.THUNDERSCAN;
    CodecOptions options = compression.getCompressionCodecOptions(ifd);
    compression.compress(data, options);
  }

  @Test
  public void testJPEG_2000() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.JPEG_2000;
    CodecOptions options = compression.getCompressionCodecOptions(ifd);
    byte[] compressed = compression.compress(data, options);
    assertNotNull(compressed);
  }

  @Test
  public void testJPEG_2000_LOSSY() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.JPEG_2000_LOSSY;
    CodecOptions options = compression.getCompressionCodecOptions(ifd);
    byte[] compressed = compression.compress(data, options);
    assertNotNull(compressed);
  }

  @Test
  public void testALT_JPEG() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.ALT_JPEG;
    ifd.put(IFD.BITS_PER_SAMPLE, new int[] { 8 });
    CodecOptions options = compression.getCompressionCodecOptions(ifd);
    byte[] compressed = compression.compress(data, options);
    assertNotNull(compressed);
  }

  @Test(expectedExceptions={ FormatException.class })
  public void testNIKON() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.NIKON;
    CodecOptions options = compression.getCompressionCodecOptions(ifd);
    compression.compress(data, options);
  }

  @Test(expectedExceptions={ FormatException.class })
  public void testLURAWAVE() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.LURAWAVE;
    CodecOptions options = compression.getCompressionCodecOptions(ifd);
    compression.compress(data, options);
  }

  @Test(enabled=true)
  public void testJPEG_2000_ResetQuality() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.JPEG_2000;
    JPEG2000CodecOptions opt = JPEG2000CodecOptions.getDefaultOptions();
    opt.quality = 1.0f;
    CodecOptions options = compression.getCompressionCodecOptions(ifd, opt);
    assertEquals(options.quality, opt.quality);
    compression = TiffCompression.JPEG_2000_LOSSY;
    options = compression.getCompressionCodecOptions(ifd, opt);
    assertEquals(options.quality, opt.quality);
    compression = TiffCompression.ALT_JPEG2000;
    options = compression.getCompressionCodecOptions(ifd, opt);
    assertEquals(options.quality, opt.quality);
  }

  @Test(enabled=true)
  public void testJPEG_2000_ResetBlockSize() throws FormatException, IOException {
    TiffCompression compression = TiffCompression.JPEG_2000;
    JPEG2000CodecOptions opt = JPEG2000CodecOptions.getDefaultOptions();
    int v = 16;
    opt.codeBlockSize = new int[] {v, v};
    CodecOptions options = compression.getCompressionCodecOptions(ifd, opt);
    assertTrue(options instanceof JPEG2000CodecOptions);
    JPEG2000CodecOptions j2k = (JPEG2000CodecOptions) options;
    assertEquals(j2k.codeBlockSize.length, opt.codeBlockSize.length);
    for (int i = 0; i < j2k.codeBlockSize.length; i++) {
      assertEquals(j2k.codeBlockSize[i], opt.codeBlockSize[i]);
    }
    compression = TiffCompression.JPEG_2000_LOSSY;
    options = compression.getCompressionCodecOptions(ifd, opt);
    assertTrue(options instanceof JPEG2000CodecOptions);
    j2k = (JPEG2000CodecOptions) options;
    assertEquals(j2k.codeBlockSize.length, opt.codeBlockSize.length);
    for (int i = 0; i < j2k.codeBlockSize.length; i++) {
      assertEquals(j2k.codeBlockSize[i], opt.codeBlockSize[i]);
    }
    compression = TiffCompression.ALT_JPEG2000;
    options = compression.getCompressionCodecOptions(ifd, opt);
    j2k = (JPEG2000CodecOptions) options;
    assertEquals(j2k.codeBlockSize.length, opt.codeBlockSize.length);
    for (int i = 0; i < j2k.codeBlockSize.length; i++) {
      assertEquals(j2k.codeBlockSize[i], opt.codeBlockSize[i]);
    }
  }

  @Test(enabled=true)
  public void testJPEG_2000_ResetNumberDecompositionLevel() 
    throws FormatException, IOException {
    TiffCompression compression = TiffCompression.JPEG_2000;
    JPEG2000CodecOptions opt = JPEG2000CodecOptions.getDefaultOptions();
    int v = 16;
    opt.numDecompositionLevels = v;
    CodecOptions options = compression.getCompressionCodecOptions(ifd, opt);
    assertTrue(options instanceof JPEG2000CodecOptions);
    JPEG2000CodecOptions j2k = (JPEG2000CodecOptions) options;
    assertEquals(j2k.numDecompositionLevels, opt.numDecompositionLevels);
    compression = TiffCompression.JPEG_2000_LOSSY;
    options = compression.getCompressionCodecOptions(ifd, opt);
    assertTrue(options instanceof JPEG2000CodecOptions);
    j2k = (JPEG2000CodecOptions) options;
    assertEquals(j2k.numDecompositionLevels, opt.numDecompositionLevels);
    compression = TiffCompression.ALT_JPEG2000;
    options = compression.getCompressionCodecOptions(ifd, opt);
    j2k = (JPEG2000CodecOptions) options;
    assertEquals(j2k.numDecompositionLevels, opt.numDecompositionLevels);
  }

}
