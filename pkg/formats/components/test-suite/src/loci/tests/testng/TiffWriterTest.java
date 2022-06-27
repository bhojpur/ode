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

import static org.testng.AssertJUnit.fail;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import loci.common.services.ServiceFactory;
import loci.formats.IFormatReader;
import loci.formats.ImageReader;
import loci.formats.in.TiffReader;
import loci.formats.meta.IMetadata;
import loci.formats.out.TiffWriter;
import loci.formats.services.ODEXMLService;
import loci.formats.tiff.IFD;
import loci.formats.tiff.TiffCompression;

import ode.xml.meta.MetadataConverter;

/**
 * Tests writing of tiles in a tiff.
 */
public class TiffWriterTest {

  /** Reader */
  private IFormatReader reader;

  /** The metadata store. */
  private IMetadata metadata;

  /** Service to create the metadata store. */
  private ODEXMLService service;

  /** The compression levels to test. */
  private final static String[] COMPRESSION;

  /** The big tiff flags. */
  private final static Boolean[] BIG_TIFF;

  static {
    COMPRESSION = new String[2];
    COMPRESSION[0] = TiffCompression.UNCOMPRESSED.getCodecName();
    COMPRESSION[1] = TiffCompression.JPEG_2000.getCodecName();
    BIG_TIFF = new Boolean[2];
    BIG_TIFF[0] = Boolean.valueOf(false);
    BIG_TIFF[1] = Boolean.valueOf(true);
  }

  /**
   * Initializes the writer.
   * @param output The file where to write the compressed data.
   * @param compression The compression to use.
   * @param bigTiff Pass <code>true</code> to set the <code>bigTiff</code> flag,
   *                <code>false</code> otherwise.
   * @return See above.
   * @throws Exception Thrown if an error occurred.
   */
  private TiffWriter initializeWriter(String output, String compression, boolean
      bigTiff)
  throws Exception {
    IMetadata newMetadata = service.createODEXMLMetadata();
    MetadataConverter.convertMetadata(metadata, newMetadata);
    TiffWriter writer = new TiffWriter();
    writer.setMetadataRetrieve(newMetadata);
    writer.setCompression(compression);
    writer.setWriteSequentially(true);
    writer.setInterleaved(false);
    writer.setBigTiff(bigTiff);
    writer.setId(output);
    return writer;
  }

  /**
   * Tests the writing of the tiles.
   * @param output The output where to write the data.
   * @param compression The compression to use.
   * @param n The value by which to divide the width of the image.
   * @param m The value by which to divide the height of the image.
   * @param bigTiff Pass <code>true</code> to set the <code>bigTiff</code> flag,
   *                <code>false</code> otherwise.
   */
  private void assertTiles(String output, String compression, int n, int m, 
      boolean bigTiff) 
    throws Exception {
    TiffWriter writer = initializeWriter(output, compression, bigTiff);
    int x, y;
    byte[] tile;
    long[] rowPerStrip;
    int w, h;
    IFD ifd;
    int count;
    int series = reader.getSeriesCount();
    String[][][] tileMD5s = new String[series][][]; 
    for (int s = 0; s < series; s++) {
      reader.setSeries(s);
      w = reader.getSizeX()/n;
      h = reader.getSizeY()/m;
      rowPerStrip = new long[1];
      rowPerStrip[0] = h;
      count = reader.getImageCount();
      tileMD5s[s] = new String[count][m * n];
      for (int k = 0; k < count; k++) {
        ifd = new IFD();
        ifd.put(IFD.TILE_WIDTH, w);
        ifd.put(IFD.TILE_LENGTH, h);
        ifd.put(IFD.ROWS_PER_STRIP, rowPerStrip);
        for (int i = 0; i < m; i++) {
          y = h*i;
          for (int j = 0; j < n; j++) {
            x = w*j;
            tile = reader.openBytes(k, x, y, w, h);
            tileMD5s[s][k][(i * n) + j] = TestTools.md5(tile);
            writer.saveBytes(k, tile, ifd, x, y, w, h);
          }
        }
      }
    }
    writer.close();
    //Now going to read the output.
    TiffReader outputReader = new TiffReader();
    outputReader.setId(output);

    //first series.
    String writtenDigest;
    String readDigest;
    for (int s = 0; s < series; s++) {
      outputReader.setSeries(s);
      count = outputReader.getImageCount();
      h = outputReader.getSizeY()/m;
      w = outputReader.getSizeX()/n;
      for (int k = 0; k < count; k++) {
        for (int i = 0; i < m; i++) {
          y = h*i;
          for (int j = 0; j < n; j++) {
            x = w*j;
            tile = outputReader.openBytes(k, x, y, w, h);
            writtenDigest = tileMD5s[s][k][(i * n) + j];
            readDigest = TestTools.md5(tile);
            if (!writtenDigest.equals(readDigest)) {
              fail(String.format(
                  "Compression:%s MD5:%d;%d;%d;%d;%d; %s != %s",
                  compression, k, x, y, w, h, writtenDigest, readDigest));
            }
          }
        }
      }
    }
    outputReader.close();
  }

  /**
   * Tests the writing of the tiles.
   * @param output The output where to write the data.
   * @param compression The compression to use.
   * @param blockWidth The width of block to write.
   * @param blockHeight The height of block to write.
   * @param bigTiff Pass <code>true</code> to set the <code>bigTiff</code> flag,
   *                <code>false</code> otherwise.
   */
  private void assertUnevenTiles(String output, String compression, 
      int blockWidth, int blockHeight, boolean bigTiff) 
    throws Exception {
    TiffWriter writer = initializeWriter(output, compression, bigTiff);
    int x, y;
    byte[] tile;
    long[] rowPerStrip;
    int w, h;
    IFD ifd;
    int count;
    int sizeX, sizeY;
    int n, m;
    int diffWidth, diffHeight;
    int series = reader.getSeriesCount();
    String[][][] tileMD5s = new String[series][][]; 
    for (int s = 0; s < series; s++) {
      reader.setSeries(s);
      sizeX = reader.getSizeX();
      sizeY = reader.getSizeY();
      if (blockWidth <= 0) blockWidth = sizeX;
      if (blockHeight <= 0) blockHeight = sizeY;
      n = sizeX/blockWidth;
      m = sizeY/blockHeight;
      if (n == 0) {
        blockWidth = sizeX;
        n = 1;
      }
      if (m == 0) {
        blockHeight = sizeY;
        m = 1;
      }
      diffWidth = sizeX-n*blockWidth;
      diffHeight = sizeY-m*blockHeight;
      if (diffWidth > 0) n++;
      if (diffHeight > 0) m++;
      rowPerStrip = new long[1];
      rowPerStrip[0] = blockHeight;
      count = reader.getImageCount();
      tileMD5s[s] = new String[count][m * n];
      for (int k = 0; k < count; k++) {
        x = 0;
        y = 0;
        ifd = new IFD();
        ifd.put(IFD.TILE_WIDTH, blockWidth);
        ifd.put(IFD.TILE_LENGTH, blockHeight);
        ifd.put(IFD.ROWS_PER_STRIP, rowPerStrip);
        for (int i = 0; i < m; i++) {
          if (diffHeight > 0 && i == (m-1)) {
            y = sizeY-diffHeight;
            h = diffHeight;
          } else {
            y = blockHeight*i;
            h = blockHeight;
          }
          for (int j = 0; j < n; j++) {
            if (diffWidth > 0 && j == (n-1)) {
              x = sizeX-diffWidth;
              w = diffWidth;
            } else {
              x = blockWidth*j;
              w = blockWidth;
            }
            tile = reader.openBytes(k, x, y, w, h);
            tileMD5s[s][k][(i * n) + j] = TestTools.md5(tile);
            writer.saveBytes(0, tile, ifd, x, y, w, h);
          }
        }
      }
    }
    writer.close();
    //Now going to read the output.
    TiffReader outputReader = new TiffReader();
    outputReader.setId(output);

    //first series.
    String writtenDigest;
    String readDigest;
    for (int s = 0; s < series; s++) {
      outputReader.setSeries(s);
      count = outputReader.getImageCount();
      for (int k = 0; k < count; k++) {
        sizeX = outputReader.getSizeX();
        sizeY = outputReader.getSizeY();
        n = sizeX/blockWidth;
        m = sizeY/blockHeight;
        diffWidth = sizeX-n*blockWidth;
        diffHeight = sizeY-m*blockHeight;
        if (diffWidth > 0) n++;
        if (diffHeight > 0) m++;
        for (int i = 0; i < m; i++) {
          if (diffHeight > 0 && i == (m-1)) {
            y = sizeY-diffHeight;
            h = diffHeight;
          } else {
            y = blockHeight*i;
            h = blockHeight;
          }
          for (int j = 0; j < n; j++) {
            if (diffWidth > 0 && j == (n-1)) {
              x = sizeX-diffWidth;
              w = diffWidth;
            } else {
              x = blockWidth*j;
              w = blockWidth;
            }
            tile = outputReader.openBytes(k, x, y, w, h);
            writtenDigest = tileMD5s[s][k][(i * n) + j];
            readDigest = TestTools.md5(tile);
            if (!writtenDigest.equals(readDigest)) {
                fail(String.format(
                        "Compression:%s MD5:%d;%d;%d;%d;%d; %s != %s",
                        compression, k, x, y, w, h, writtenDigest, readDigest));
            }
          }
        }
      }
    }
    outputReader.close();
  }

  @Parameters({"id"})
  @BeforeClass
  public void parse(String id) throws Exception {
    ServiceFactory factory = new ServiceFactory();
    service = factory.getInstance(ODEXMLService.class);
    metadata = service.createODEXMLMetadata();
    reader = new ImageReader();
    reader.setMetadataStore(metadata);
    reader.setId(id);
  }

  @AfterClass
  public void tearDown() throws Exception {
    reader.close();
  }

  /**
   * Tests the writing of the full size image as JPEG200 stream.
   * @throws Exception Throw if an error occurred while writing.
   */
  @Test(enabled=true)
  public void testWriteFullImage() throws Exception {
    File f;
    for (int i = 0; i < COMPRESSION.length; i++) {
      for (int j = 0; j < BIG_TIFF.length; j++) {
        f =  File.createTempFile("testWriteFullImage_"+j+"_"+
            COMPRESSION[i], ".tiff");
        f.deleteOnExit();
        assertTiles(f.getAbsolutePath(), COMPRESSION[i], 1, 1, BIG_TIFF[j]);
      }
    }
  }

  /**
   * Tests the writing of the image divided in 4 blocks.
   * @throws Exception Throw if an error occurred while writing.
   */
  @Test(enabled=true)
  public void testWriteImageFourTiles() throws Exception {
    File f;
    for (int i = 0; i < COMPRESSION.length; i++) {
      for (int j=0; j<BIG_TIFF.length; j++) {
        f =  File.createTempFile("testWriteImageFourTiles_" + j + "_" +
          COMPRESSION[i], ".tiff");
        f.deleteOnExit();
        assertTiles(f.getAbsolutePath(), COMPRESSION[i], 2, 2, BIG_TIFF[j]);
      }
    }
  }

  /**
   * Tests the writing of the image with 2 tiles with full width.
   * @throws Exception Throw if an error occurred while writing.
   */
  @Test(enabled=true)
  public void testWriteImageSplitHorizontal() throws Exception {
    File f;
    for (int i = 0; i < COMPRESSION.length; i++) {
      for (int j=0; j<BIG_TIFF.length; j++) {
        f =  File.createTempFile("testWriteImageSplitHorizontal_" + j + "_" +
          COMPRESSION[i], ".tiff");
        f.deleteOnExit();
        assertTiles(f.getAbsolutePath(), COMPRESSION[i], 1, 2, BIG_TIFF[j]);
      }
    }
  }

  /**
   * Tests the writing of the image with 2 tiles with full height.
   * @throws Exception Throw if an error occurred while writing.
   */
  @Test(enabled=true)
  public void testWriteImageSplitVertical() throws Exception {
    File f;
    for (int i = 0; i < COMPRESSION.length; i++) {
      for (int j=0; j<BIG_TIFF.length; j++) {
        f =  File.createTempFile("testWriteImageSplitVertical_" + j + "_" +
          COMPRESSION[i], ".tiff");
        f.deleteOnExit();
        assertTiles(f.getAbsolutePath(), COMPRESSION[i], 2, 1, BIG_TIFF[j]);
      }
    }
  }

  /**
   * Tests the writing of blocks of 256x256. Tiles should be square and size
   * multiple of 16.
   * @throws Exception Throw if an error occurred while writing.
   */
  @Test(enabled=true)
  public void testWriteUnevenTilesImage128x128Block() throws Exception {
    File f;
    for (int i = 0; i < COMPRESSION.length; i++) {
      for (int j = 0; j < BIG_TIFF.length; j++) {
        f =  File.createTempFile("testWriteUnevenTilesImage128x128Block_"+j+"_"+
            COMPRESSION[i], ".tiff");
        f.deleteOnExit();
        assertUnevenTiles(f.getAbsolutePath(), COMPRESSION[i], 128, 128, 
            BIG_TIFF[j]);
      }
    }
  }

  /**
   * Tests the writing of blocks of 256x256. Tiles should be square and size
   * multiple of 16.
   * @throws Exception Throw if an error occurred while writing.
   */
  @Test(enabled=true)
  public void testWriteUnevenTilesImage256x256Block() throws Exception {
    File f;
    for (int i = 0; i < COMPRESSION.length; i++) {
      for (int j = 0; j < BIG_TIFF.length; j++) {
        f =  File.createTempFile("testWriteUnevenTilesImage256x256Block_"+j+"_"+
            COMPRESSION[i], ".tiff");
        f.deleteOnExit();
        assertUnevenTiles(f.getAbsolutePath(), COMPRESSION[i], 256, 256, 
            BIG_TIFF[j]);
      }
    }
  }

}
