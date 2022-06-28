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

import com.google.common.hash.Hashing;

import static org.testng.AssertJUnit.fail;
import org.testng.annotations.Test;

import loci.formats.codec.CodecOptions;
import loci.formats.tiff.IFD;
import loci.formats.tiff.TiffCompression;

/**
 * Tests the various codec used to compress and decompress data.
 * Not yet supported:
 * <code>Nikon</code>
 * <code>PackBits</code>
 * <code>LuraWave</code>
 */
public class CompressDecompressTest {

  /**
   * Tests the writing of the tiles.
   * @param compression The compression to use.
   * @param lossy whether or not this is a lossy compression type
   */
  private void assertCompression(TiffCompression compression, boolean lossy)
    throws Exception
  {
    IFD ifd = new IFD();
    int w = 64;
    int h = 64;
    int bpp = 8;
    ifd.put(IFD.IMAGE_WIDTH, w);
    ifd.put(IFD.IMAGE_LENGTH, h);
    ifd.put(IFD.BITS_PER_SAMPLE, new int[] { bpp });
    ifd.put(IFD.SAMPLES_PER_PIXEL, 1);
    ifd.put(IFD.LITTLE_ENDIAN, Boolean.TRUE);


    byte[] plane = new byte[w * h * (bpp / 8)];
    for (int i=0; i<plane.length; i++) {
      plane[i] = (byte) i;
    }

    String beforeCompression, afterCompression, afterDecompression;
    CodecOptions options = compression.getCompressionCodecOptions(ifd);
    byte[] compressed;
    beforeCompression = Hashing.md5().hashBytes(plane).toString();
    compressed = compression.compress(plane, options);
    afterCompression = Hashing.md5().hashBytes(compressed).toString();
    if (compression.equals(TiffCompression.UNCOMPRESSED)) {
      if (!beforeCompression.equals(afterCompression)) {
        fail("Compression: "+compression.getCodecName()+" "+
            String.format("Compression MD5 %s != %s",
            beforeCompression, afterCompression));
      }
      afterDecompression = Hashing.md5().hashBytes(
        compression.decompress(compressed, options)).toString();
      if (!beforeCompression.equals(afterDecompression)) {
        fail("Compression: "+compression.getCodecName()+" "+
            String.format("Decompression MD5 %s != %s",
            beforeCompression, afterDecompression));
      }
    } else {
      if (beforeCompression.equals(afterCompression)) {
        fail("Compression: "+compression.getCodecName()+" "+
            String.format("Compression MD5 %s != %s",
            beforeCompression, afterCompression));
      }
      afterDecompression = Hashing.md5().hashBytes(
        compression.decompress(compressed, options)).toString();
      if (!lossy && !beforeCompression.equals(afterDecompression)) {
        fail("Compression: "+compression.getCodecName()+" "+
            String.format("Decompression MD5 %s != %s",
            beforeCompression, afterDecompression));
      }
    }
  }

  /**
   * Tests the compression and decompression using <code>JPEG2000</code>.
   * @throws Exception Throw if an error occurred while writing.
   */
  @Test
  public void testCompressDecompressedJ2KLossless() throws Exception {
    assertCompression( TiffCompression.JPEG_2000, false);
  }

  /**
   * Tests the compression and decompression using <code>JPEG2000-lossy</code>.
   * @throws Exception Throw if an error occurred while writing.
   */
  @Test
  public void testCompressDecompressedJ2KLossy() throws Exception {
    assertCompression( TiffCompression.JPEG_2000_LOSSY, true);
  }

  /**
   * Tests the compression and decompression using <code>JPEG</code>.
   * @throws Exception Throw if an error occurred while writing.
   */
  @Test
  public void testCompressDecompressedJPEG() throws Exception {
    assertCompression( TiffCompression.JPEG, true);
  }

  /**
   * Tests the compression and decompression using <code>Deflate</code>.
   * @throws Exception Throw if an error occurred while writing.
   */
  @Test
  public void testCompressDecompressedDeflate() throws Exception {
    assertCompression( TiffCompression.DEFLATE, false);
  }

  /**
   * Tests the compression and decompression using <code>Uncompressed</code>.
   * @throws Exception Throw if an error occurred while writing.
   */
  @Test
  public void testCompressDecompressedUncompressed() throws Exception {
    assertCompression( TiffCompression.UNCOMPRESSED, false);
  }

}
