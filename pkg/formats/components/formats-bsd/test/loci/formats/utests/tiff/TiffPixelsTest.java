/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.utests.tiff;

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
