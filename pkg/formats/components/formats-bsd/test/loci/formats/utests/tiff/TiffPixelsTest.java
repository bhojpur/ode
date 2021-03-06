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

import java.io.IOException;

import loci.common.ByteArrayHandle;
import loci.common.RandomAccessInputStream;
import loci.common.RandomAccessOutputStream;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.tiff.IFD;
import loci.formats.tiff.TiffCompression;
import loci.formats.tiff.TiffParser;
import loci.formats.tiff.TiffSaver;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests saving and reading TIFF pixel data that has been compressed using
 * various schemes.
 */
public class TiffPixelsTest {

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
    ifd.put(IFD.ROWS_PER_STRIP, new long[] {IMAGE_LENGTH});
    ifd.put(IFD.STRIP_OFFSETS, new long[] {0});
    data = new byte[IMAGE_WIDTH * IMAGE_LENGTH * (BITS_PER_PIXEL / 8)];
    for (int i=0; i<data.length; i++) {
      data[i] = (byte) i;
    }
    ifd.put(IFD.STRIP_BYTE_COUNTS, new long[] {data.length});
  }

  @Test
  public void testUNCOMPRESSED() throws FormatException, IOException {
    ifd.put(IFD.COMPRESSION, TiffCompression.UNCOMPRESSED.getCode());
    byte[] plane = readSavedPlane();
    for (int i=0; i<plane.length; i++) {
      assertEquals(plane[i], data[i]);
    }
  }

  @Test(expectedExceptions={ FormatException.class })
  public void testCCITT_1D() throws FormatException, IOException {
    ifd.put(IFD.COMPRESSION, TiffCompression.CCITT_1D.getCode());
    byte[] plane = readSavedPlane();
    for (int i=0; i<plane.length; i++) {
      assertEquals(plane[i], data[i]);
    }
  }

  @Test(expectedExceptions={ FormatException.class })
  public void testGROUP_3_FAX() throws FormatException, IOException {
    ifd.put(IFD.COMPRESSION, TiffCompression.GROUP_3_FAX.getCode());
    byte[] plane = readSavedPlane();
    for (int i=0; i<plane.length; i++) {
      assertEquals(plane[i], data[i]);
    }
  }

  @Test(expectedExceptions={ FormatException.class })
  public void testGROUP_4_FAX() throws FormatException, IOException {
    ifd.put(IFD.COMPRESSION, TiffCompression.GROUP_4_FAX.getCode());
    byte[] plane = readSavedPlane();
    for (int i=0; i<plane.length; i++) {
      assertEquals(plane[i], data[i]);
    }
  }

  @Test
  public void testLZW() throws FormatException, IOException {
    ifd.put(IFD.COMPRESSION, TiffCompression.LZW.getCode());
    byte[] plane = readSavedPlane();
    for (int i=0; i<plane.length; i++) {
      assertEquals(plane[i], data[i]);
    }
  }

  @Test(expectedExceptions={ FormatException.class })
  public void testPACK_BITS() throws FormatException, IOException {
    ifd.put(IFD.COMPRESSION, TiffCompression.PACK_BITS.getCode());
    byte[] plane = readSavedPlane();
    for (int i=0; i<plane.length; i++) {
      assertEquals(plane[i], data[i]);
    }
  }

  @Test
  public void testPROPRIETARY_DEFLATE() throws FormatException, IOException {
    ifd.put(IFD.COMPRESSION, TiffCompression.PROPRIETARY_DEFLATE.getCode());
    byte[] plane = readSavedPlane();
    for (int i=0; i<plane.length; i++) {
      assertEquals(plane[i], data[i]);
    }
  }

  @Test
  public void testDEFLATE() throws FormatException, IOException {
    ifd.put(IFD.COMPRESSION, TiffCompression.DEFLATE.getCode());
    byte[] plane = readSavedPlane();
    for (int i=0; i<plane.length; i++) {
      assertEquals(plane[i], data[i]);
    }
  }

  // -- Helper method --

  private byte[] readSavedPlane() throws FormatException, IOException {
    ByteArrayHandle savedData = new ByteArrayHandle();
    byte[] plane = null;
    try (RandomAccessOutputStream out = new RandomAccessOutputStream(savedData);
          RandomAccessInputStream in = new RandomAccessInputStream(savedData)) {
        TiffSaver saver = new TiffSaver(out, savedData);
        //saver.setInputStream(in);
        TiffParser parser = new TiffParser(in);
        saver.writeImage(data, ifd, 0, FormatTools.UINT16, false);
        plane = new byte[data.length];
        parser.getSamples(ifd, plane);
    }
    return plane;
  }

}
