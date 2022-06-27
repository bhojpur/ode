/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.utests.tiff;

import static org.testng.AssertJUnit.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import loci.formats.FormatException;
import loci.formats.IFormatReader;
import loci.formats.ImageReader;
import loci.formats.MetadataTools;
import loci.formats.meta.DummyMetadata;
import loci.formats.meta.IMetadata;
import loci.formats.meta.IPyramidStore;
import loci.formats.out.PyramidODETiffWriter;
import loci.formats.tiff.IFD;

import ode.xml.model.enums.DimensionOrder;
import ode.xml.model.enums.PixelType;
import ode.xml.model.primitives.PositiveInteger;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Reads a set of pyramid resolutions (one per file) and converts to a single
 * pyramid ODE-TIFF.
 */
public class PyramidTest {

  private static final int RESOLUTION_COUNT = 4;
  private static final int EXTRA_WIDTH = 9;
  private static final int EXTRA_HEIGHT = 3;
  private static final int TILE_SIZE = 1;
  private static final int SCALE = 2;

  private File[] files = new File[9];

  @BeforeClass
  public void setUp() throws Exception {
    for (int i=0; i<files.length; i++) {
      files[i] = File.createTempFile("PyramidTest", ".ode.tiff");
    }
  }

  @AfterClass
  public void tearDown() throws Exception {
    for (File f : files) {
      f.delete();
    }
  }

  @Test
  public void testSinglePyramid() throws FormatException, IOException {
    writePyramid(files[0].getAbsolutePath(), new int[] {8}, new int[] {8}, 1, 0, false, false);
    IFormatReader reader = getReader(0);
    try {
      assertEquals(reader.getSeriesCount(), 1);
      assertEquals(reader.getResolutionCount(), RESOLUTION_COUNT);
      assertTrue(checkPixels(reader));
    }
    finally {
      reader.close();
    }
  }

  @Test
  public void testSinglePyramidBigTiff() throws FormatException, IOException {
    writePyramid(files[1].getAbsolutePath(), new int[] {8}, new int[] {8}, 1, 0, false, true);
    IFormatReader reader = getReader(1);
    try {
      assertEquals(reader.getSeriesCount(), 1);
      assertEquals(reader.getResolutionCount(), RESOLUTION_COUNT);
      assertTrue(checkPixels(reader));
    }
    finally {
      reader.close();
    }
  }

  @Test
  public void testSinglePyramidBigEndian() throws FormatException, IOException {
    writePyramid(files[2].getAbsolutePath(), new int[] {8}, new int[] {8}, 1, 0, true, false);
    IFormatReader reader = getReader(2);
    try {
      assertEquals(reader.getSeriesCount(), 1);
      assertEquals(reader.getResolutionCount(), RESOLUTION_COUNT);
      assertTrue(checkPixels(reader));
    }
    finally {
      reader.close();
    }
  }

  @Test
  public void testSinglePyramidBigEndianBigTiff() throws FormatException, IOException {
    writePyramid(files[3].getAbsolutePath(), new int[] {8}, new int[] {8}, 1, 0, true, true);
    IFormatReader reader = getReader(3);
    try {
      assertEquals(reader.getSeriesCount(), 1);
      assertEquals(reader.getResolutionCount(), RESOLUTION_COUNT);
      assertTrue(checkPixels(reader));
    }
    finally {
      reader.close();
    }
  }

  @Test
  public void testSinglePyramidWithExtra() throws FormatException, IOException {
    writePyramid(files[4].getAbsolutePath(), new int[] {16}, new int[] {16}, 1, 2, false, true);
    IFormatReader reader = getReader(4);
    try {
      assertEquals(reader.getSeriesCount(), 3);
      assertEquals(reader.getResolutionCount(), RESOLUTION_COUNT);
      assertEquals(reader.getSizeX(), 16);
      assertEquals(reader.getSizeY(), 16);
      reader.setSeries(1);
      assertEquals(reader.getResolutionCount(), 1);
      assertEquals(reader.getSizeX(), EXTRA_WIDTH);
      assertEquals(reader.getSizeY(), EXTRA_HEIGHT);
      reader.setSeries(2);
      assertEquals(reader.getResolutionCount(), 1);
      assertEquals(reader.getSizeX(), EXTRA_WIDTH);
      assertEquals(reader.getSizeY(), EXTRA_HEIGHT);
      assertTrue(checkPixels(reader));
    }
    finally {
      reader.close();
    }
  }

  @Test
  public void testSinglePyramidMultiplePlanes() throws FormatException, IOException {
    writePyramid(files[5].getAbsolutePath(), new int[] {16}, new int[] {16}, 3, 0, false, true);
    IFormatReader reader = getReader(5);
    try {
      assertEquals(reader.getSeriesCount(), 1);
      assertEquals(reader.getResolutionCount(), RESOLUTION_COUNT);
      assertEquals(reader.getSizeX(), 16);
      assertEquals(reader.getSizeY(), 16);
      for (int i=0; i<RESOLUTION_COUNT; i++) {
        reader.setResolution(i);
        assertEquals(reader.getSizeZ(), 3);
      }
      assertTrue(checkPixels(reader));
    }
    finally {
      reader.close();
    }
  }

  @Test
  public void testMultiplePyramids() throws FormatException, IOException {
    int[] dims = new int[] {16, 10};
    writePyramid(files[6].getAbsolutePath(), dims, dims, 1, 0, false, true);
    IFormatReader reader = getReader(6);
    try {
      assertEquals(reader.getSeriesCount(), 2);
      for (int s=0; s<reader.getSeriesCount(); s++) {
        reader.setSeries(s);
        assertEquals(reader.getResolutionCount(), RESOLUTION_COUNT);
        assertEquals(reader.getSizeX(), dims[s]);
        assertEquals(reader.getSizeY(), dims[s]);
        assertEquals(reader.getSizeZ(), 1);
      }
      assertTrue(checkPixels(reader));
    }
    finally {
      reader.close();
    }
  }

  @Test
  public void testMultiplePyramidsExtra() throws FormatException, IOException {
    int[] dims = new int[] {8, 10};
    writePyramid(files[7].getAbsolutePath(), dims, dims, 1, 2, false, true);
    IFormatReader reader = getReader(7);
    try {
      assertEquals(reader.getSeriesCount(), 4);
      for (int s=0; s<reader.getSeriesCount(); s++) {
        reader.setSeries(s);
        if (s < dims.length) {
          assertEquals(reader.getResolutionCount(), RESOLUTION_COUNT);
          assertEquals(reader.getSizeX(), dims[s]);
          assertEquals(reader.getSizeY(), dims[s]);
        }
        else {
          assertEquals(reader.getResolutionCount(), 1);
          assertEquals(reader.getSizeX(), EXTRA_WIDTH);
          assertEquals(reader.getSizeY(), EXTRA_HEIGHT);
        }
        assertEquals(reader.getSizeZ(), 1);
      }
      assertTrue(checkPixels(reader));
    }
    finally {
      reader.close();
    }
  }

  @Test
  public void testMultiplePyramidsMultiplePlanes() throws FormatException, IOException {
    int[] dims = new int[] {8, 10};
    writePyramid(files[8].getAbsolutePath(), dims, dims, 2, 0, false, true);
    IFormatReader reader = getReader(8);
    try {
      assertEquals(reader.getSeriesCount(), 2);
      for (int s=0; s<reader.getSeriesCount(); s++) {
        reader.setSeries(s);
        assertEquals(reader.getResolutionCount(), RESOLUTION_COUNT);
        assertEquals(reader.getSizeX(), dims[s]);
        assertEquals(reader.getSizeY(), dims[s]);
        assertEquals(reader.getSizeZ(), 2);
      }
      assertTrue(checkPixels(reader));
    }
    finally {
      reader.close();
    }
  }

  private IFormatReader getReader(int index) throws FormatException, IOException {
    ImageReader reader = new ImageReader();
    reader.setFlattenedResolutions(false);
    reader.setId(files[index].getAbsolutePath());
    return reader;
  }

  private boolean checkPixels(IFormatReader reader) throws FormatException, IOException {
    int index = 1;
    for (int s=0; s<reader.getSeriesCount(); s++) {
      reader.setSeries(s);
      for (int r=0; r<reader.getResolutionCount(); r++) {
        reader.setResolution(r);

        for (int p=0; p<reader.getImageCount(); p++) {
          byte[] plane = reader.openBytes(p);
          for (byte pixel : plane) {
            if ((pixel & 0xff) != index) {
              return false;
            }
          }
          index++;
        }
      }
    }
    return true;
  }

  private void writePyramid(String file, int[] widths, int[] heights, int planes,
    int extra, boolean bigEndian, boolean bigTiff)
    throws FormatException, IOException
   {
    // read each input file's metadata to build an IPyramidStore
    // that represents the full image pyramid
    IMetadata meta = MetadataTools.createODEXMLMetadata();

    if (!(meta instanceof IPyramidStore)) {
      throw new FormatException("MetadataStore is not an IPyramidStore; " +
        "cannot write pyramid");
    }

    for (int p=0; p<widths.length; p++) {
      populateImage(meta, p, widths[p], heights[p], planes, bigEndian);

      for (int r=1; r<RESOLUTION_COUNT; r++) {
        int scale = (int) Math.pow(SCALE, r);
        ((IPyramidStore) meta).setResolutionSizeX(
          new PositiveInteger(widths[p] / scale), p, r);
        ((IPyramidStore) meta).setResolutionSizeY(
          new PositiveInteger(heights[p] / scale), p, r);
      }
    }
    for (int p=widths.length; p<widths.length+extra; p++) {
      populateImage(meta, p, EXTRA_WIDTH, EXTRA_HEIGHT, planes, bigEndian);
    }
  
    PyramidODETiffWriter writer = new PyramidODETiffWriter();
    writer.setBigTiff(bigTiff);
    writer.setWriteSequentially(true);
    writer.setMetadataRetrieve(meta);
    writer.setId(file);

    int index = 1;
    for (int p=0; p<widths.length; p++) {
      writer.setSeries(p);
      for (int r=0; r<RESOLUTION_COUNT; r++) {
        writer.setResolution(r);

        int scale = (int) Math.pow(SCALE, r);
        int width = widths[p] / scale;
        int height = heights[p] / scale;
        for (int plane=0; plane<planes; plane++) {
          byte[] tile = new byte[] {(byte) index++};
          IFD ifd = new IFD();
          ifd.put(IFD.TILE_WIDTH, TILE_SIZE);
          ifd.put(IFD.TILE_LENGTH, TILE_SIZE);

          for (int yy=0; yy<height; yy++) {
            for (int xx=0; xx<width; xx++) {
              writer.saveBytes(plane, tile, ifd, xx, yy, TILE_SIZE, TILE_SIZE);
            }
          }
        }
      }
    }
    for (int e=0; e<extra; e++) {
      writer.setSeries(widths.length + e);
      for (int plane=0; plane<planes; plane++) {
        byte[] extraPlane = new byte[EXTRA_WIDTH * EXTRA_HEIGHT];
        Arrays.fill(extraPlane, (byte) index++);
        writer.saveBytes(plane, extraPlane);
      }
    }
    writer.close();
  }

  /**
   * Set metadata for writing a single Image/series.  Does not set subresolution data.
   */
  private void populateImage(IMetadata meta, int p, int width, int height, int planes, boolean bigEndian) {
    meta.setImageID("Image:" + p, p);
    meta.setPixelsID("Pixels:" + p, p);
    meta.setPixelsDimensionOrder(DimensionOrder.XYZCT, p);
    meta.setPixelsSizeX(new PositiveInteger(width), p);
    meta.setPixelsSizeY(new PositiveInteger(height), p);
    meta.setPixelsSizeZ(new PositiveInteger(planes), p);
    meta.setPixelsSizeC(new PositiveInteger(1), p);
    meta.setPixelsSizeT(new PositiveInteger(1), p);
    meta.setPixelsType(PixelType.UINT8, p);
    meta.setPixelsBigEndian(bigEndian, p);
    meta.setChannelID("Channel:" + p + ":0", p, 0);
    meta.setChannelSamplesPerPixel(new PositiveInteger(1), p, 0);
  }

}
