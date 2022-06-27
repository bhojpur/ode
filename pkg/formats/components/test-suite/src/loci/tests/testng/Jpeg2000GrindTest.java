package loci.tests.testng;

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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import loci.tests.testng.TestTools.TileLoopIteration;
import ode.xml.model.enums.DimensionOrder;
import ode.xml.model.primitives.PositiveInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import loci.common.services.ServiceFactory;
import loci.formats.FormatTools;
import loci.formats.in.TiffReader;
import loci.formats.meta.IMetadata;
import loci.formats.out.TiffWriter;
import loci.formats.services.ODEXMLService;
import loci.formats.tiff.IFD;
import loci.formats.tiff.TiffCompression;

/**
 * Test grinding in a multi-threaded environment a JPEG-2000 encoded TIFF.
 */
public class Jpeg2000GrindTest {

  private static final Logger LOGGER =
    LoggerFactory.getLogger(Jpeg2000GrindTest.class);

  public static final String PIXEL_TYPE = "uint16";

  public static final int SIZE_X = 5000;

  public static final int SIZE_Y = 4000;

  public static final int SIZE_Z = 1;

  public static final int SIZE_C = 3;

  public static final int SIZE_T = 1;

  public static final int TILE_WIDTH = 256;

  public static final int TILE_HEIGHT = 256;

  public static final int THREAD_POOL_SIZE = 2;

  private int bytesPerPixel;

  private Map<Integer, String> hashDigests = new HashMap<Integer, String>();

  private TiffWriter writer;

  private File id;

  /** All the IFDs we have used for each "plane". */
  private Map<Integer, IFD> ifds = new HashMap<Integer, IFD>();

  /** Last IFD we used during a tile write operation. */
  private int lastIFD;

  /** Thread pool executor service. */
  private ExecutorService pool;

  /**
   * Initializes the writer.
   * @param output The file where to write the compressed data.
   * @param compression The compression to use.
   * @param bigTiff Pass <code>true</code> to set the <code>bigTiff</code>
   * flag, <code>false</code> otherwise.
   * @throws Exception Thrown if an error occurred.
   */
  private void initializeWriter(String output, String compression,
                                boolean bigTiff)
      throws Exception
  {
    ServiceFactory sf = new ServiceFactory();
    ODEXMLService service = sf.getInstance(ODEXMLService.class);
    IMetadata metadata = service.createODEXMLMetadata();
    metadata.setImageID("Image:0", 0);
    metadata.setPixelsID("Pixels:0", 0);
    metadata.setPixelsBinDataBigEndian(true, 0, 0);
    metadata.setPixelsDimensionOrder(DimensionOrder.XYZCT, 0);
    metadata.setPixelsType(
        ode.xml.model.enums.PixelType.fromString(PIXEL_TYPE), 0);
    metadata.setPixelsSizeX(new PositiveInteger(SIZE_X), 0);
    metadata.setPixelsSizeY(new PositiveInteger(SIZE_Y), 0);
    metadata.setPixelsSizeZ(new PositiveInteger(1), 0);
    metadata.setPixelsSizeC(new PositiveInteger(1), 0);
    metadata.setPixelsSizeT(new PositiveInteger(SIZE_Z * SIZE_C * SIZE_T), 0);
    metadata.setChannelID("Channel:0", 0, 0);
    metadata.setChannelSamplesPerPixel(new PositiveInteger(1), 0, 0);
    writer = new TiffWriter();
    writer.setMetadataRetrieve(metadata);
    writer.setCompression(compression);
    writer.setWriteSequentially(false);
    writer.setInterleaved(true);
    writer.setBigTiff(bigTiff);
    writer.setId(output);
    bytesPerPixel = FormatTools.getBytesPerPixel(PIXEL_TYPE);
  }

  @BeforeClass
  public void setup() throws Exception {
    id = File.createTempFile(Jpeg2000GrindTest.class.getName(), ".tif");
    initializeWriter(id.getAbsolutePath(),
                     TiffCompression.JPEG_2000.getCodecName(),
                     false);
  }

  @AfterClass
  public void tearDown() throws Exception {
    writer.close();
    id.delete();
  }

  @Test(enabled=true)
  public void testPyramidWriteTiles() throws Exception {
    pool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    short tileCount = (short) TestTools.forEachTile(new TileLoopIteration() {
      @Override
      public void run(int z, int c, int t, int x, int y, int tileWidth,
          int tileHeight, int tileCount) {
        int planeNumber = FormatTools.getIndex(
            "XYZCT", SIZE_Z, SIZE_C, SIZE_T, SIZE_Z * SIZE_C * SIZE_T, z, c, t);
        if (planeNumber != lastIFD) {
          pool.shutdown();
          try {
            while (!pool.awaitTermination(30, TimeUnit.SECONDS)) {
              LOGGER.warn("Waiting for runnables to complete...");
            }
          } catch (InterruptedException e) {
            LOGGER.error("Caught interuption while waiting for termination.");
          }
          pool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
          lastIFD = planeNumber;
        }
        pool.submit(new TileRunnable(
            writer, z, c, t, x, y, tileWidth, tileHeight, tileCount));
      }
    }, SIZE_X, SIZE_Y, SIZE_Z, SIZE_C, SIZE_T, TILE_WIDTH, TILE_HEIGHT);
    pool.shutdown();
    while (!pool.awaitTermination(30, TimeUnit.SECONDS)) {
      LOGGER.warn("Waiting for runnables to complete...");
    }
    assertEquals(tileCount, 960);
    writer.close();
  }

  /*
  @Test(enabled=true)
  public void testPyramidWriteTiles() throws Exception {
    short tileCount = (short) TestTools.forEachTile(new TileLoopIteration() {
      public void run(int z, int c, int t, int x, int y, int tileWidth,
          int tileHeight, int tileCount) {
        byte[] tile = new byte[tileWidth * tileHeight * bytesPerPixel];
        ByteBuffer.wrap(tile).asShortBuffer().put(0, (short) tileCount);
        hashDigests.put(tileCount, TestTools.md5(tile));
        int planeNumber = FormatTools.getIndex(
            "XYZCT", SIZE_Z, SIZE_C, SIZE_T, SIZE_Z * SIZE_C * SIZE_T, z, c, t);
        IFD ifd;
        synchronized (ifds) {
          if (!ifds.containsKey(planeNumber)) {
            ifd = new IFD();
            ifd.put(IFD.TILE_WIDTH, TILE_WIDTH);
            ifd.put(IFD.TILE_LENGTH, TILE_HEIGHT);
            ifds.put(planeNumber, ifd);
          }
          ifd = ifds.get(planeNumber);
        }
        try {
          writer.saveBytes(planeNumber, tile, ifd, x, y, tileWidth, tileHeight);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
        tileCount++;
      }
    }, SIZE_X, SIZE_Y, SIZE_Z, SIZE_C, SIZE_T, TILE_WIDTH, TILE_HEIGHT);
    assertEquals(tileCount, 960);
    writer.close();
  }
  */

  @Test(dependsOnMethods={"testPyramidWriteTiles"}, enabled=true)
  public void testPyramidReadTilesMultiThreaded() throws Exception {
    pool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    for (int theC = 0; theC < SIZE_C; theC++) {
      pool.execute(new ChannelRunnable(theC));
    }
    pool.shutdown();
    while (!pool.awaitTermination(30, TimeUnit.SECONDS)) {
      LOGGER.warn("Waiting for channel runnables to complete...");
    }
  }

  class TileRunnable implements Runnable {

    private int tileNumber;
    private TiffWriter writer;
    private int z;
    private int c;
    private int t;
    private int x;
    private int y;
    private int tileWidth;
    private int tileHeight;

    public TileRunnable(TiffWriter writer, int z, int c, int t, int x, int y,
        int tileWidth, int tileHeight, int tileNumber) {
      this.z = z;
      this.c = c;
      this.t = t;
      this.x = x;
      this.y = y;
      this.tileWidth = tileWidth;
      this.tileHeight = tileHeight;
      this.writer = writer;
      this.tileNumber = tileNumber;
    }

    @Override
    public void run() {
      byte[] tile = new byte[tileWidth * tileHeight * bytesPerPixel];
      ByteBuffer.wrap(tile).asShortBuffer().put(0, (short) tileNumber);
      hashDigests.put(tileNumber, TestTools.md5(tile));
      int planeNumber = FormatTools.getIndex(
          "XYZCT", SIZE_Z, SIZE_C, SIZE_T, SIZE_Z * SIZE_C * SIZE_T, z, c, t);
      IFD ifd;
      synchronized (ifds) {
        if (!ifds.containsKey(planeNumber)) {
          ifd = new IFD();
          ifd.put(IFD.TILE_WIDTH, TILE_WIDTH);
          ifd.put(IFD.TILE_LENGTH, TILE_HEIGHT);
          ifds.put(planeNumber, ifd);
        }
        ifd = ifds.get(planeNumber);
      }
      try {
        writer.saveBytes(planeNumber, tile, ifd, x, y, tileWidth, tileHeight);
      } catch (Exception e) {
        LOGGER.error("Exception while writing tile", e);
        throw new RuntimeException(e);
      }
    }
  }

  class ChannelRunnable implements Runnable {

    private int theC;

    public ChannelRunnable(int theC) {
      this.theC = theC;
    }

    @Override
    public void run() {
      final TiffReader reader = new TiffReader();
      try {
        reader.setId(id.getAbsolutePath());
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
      assertEquals(reader.getImageCount(), SIZE_Z * SIZE_C * SIZE_T);
      assertEquals(reader.getSeriesCount(), 6);
      short tileCount = (short) TestTools.forEachTile(new TileLoopIteration() {
        @Override
        public void run(int z, int c, int t, int x, int y, int tileWidth,
            int tileHeight, int tileCount) {
          try {
            tileCount += theC * 320;
            int planeNumber = FormatTools.getIndex(
                "XYZCT", SIZE_Z, SIZE_C, SIZE_T, SIZE_Z * SIZE_C * SIZE_T,
                z, theC, t);
            byte[] tile = null;
            try {
              tile = reader.openBytes(planeNumber, x, y, tileWidth,
                  tileHeight);
            }
            catch (Throwable throwable) {
              fail(String.format("Failure reading tile z:%d c:%d t:%d "
                  + "x:%d y:%d", z, theC, t, x, y), throwable);
            }
            String readDigest = TestTools.md5(tile);
            String writtenDigest = hashDigests.get(tileCount);
            if (!writtenDigest.equals(readDigest)) {
              fail(String.format("Hash digest mismatch z:%d c:%d t:%d "
                  + "x:%d y:%d -- %s != %s", z, theC, t, x, y, writtenDigest,
                  readDigest));
            }
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
      }, SIZE_X, SIZE_Y, SIZE_Z, 1, SIZE_T, TILE_WIDTH, TILE_HEIGHT);
      assertEquals(tileCount, 320);
    }
  }

}
