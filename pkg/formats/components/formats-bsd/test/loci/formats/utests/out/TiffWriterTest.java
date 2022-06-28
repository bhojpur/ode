package loci.formats.utests.out;

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
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.junit.Assert;
import loci.common.ByteArrayHandle;
import loci.common.Location;
import loci.common.services.ServiceFactory;
import loci.formats.FormatException;
import loci.formats.in.TiffReader;
import loci.formats.meta.IMetadata;
import loci.formats.out.TiffWriter;
import loci.formats.services.ODEXMLService;
import loci.formats.tiff.IFD;
import loci.formats.utests.tiff.TiffWriterMock;
import ode.xml.model.enums.DimensionOrder;
import ode.xml.model.enums.PixelType;
import ode.xml.model.primitives.PositiveInteger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests the functionality of TiffWriter
 */
public class TiffWriterTest {

  private IFD ifd;
  private TiffWriter writer;
  private IMetadata metadata;

  final long BIG_TIFF_CUTOFF = (long) 1024 * 1024 * 3990;
  private static final byte[] buf = new byte[1024*1024];
  private static final int TILE_GRANULARITY = 16;

  /* Percentage of tiling tests to be executed */
  private static int percentageOfTilingTests = 0;
  private static int percentageOfSaveBytesTests = 0;

  @DataProvider(name = "bigTiffSuffixes")
  public Object[][] createSuffixes() {
    return new Object[][] {{"tf2"}, {"tf8"}, {"btf"}, {"tif"}, {"tiff"}};
  }

  @DataProvider(name = "codecs")
  public Object[][] createCodecs() {
    return WriterUtilities.getCodecs();
  }
  
  @DataProvider(name = "tiling")
  public Object[][] createTiling() {
    if (percentageOfTilingTests == 0) {
      return new Object[][] {{0, false, false, 0, 0, 0, null, 0, false}};
    }

    int[] tileSizes = {1, 32, 43, 64, WriterUtilities.PLANE_WIDTH};
    int[] channelCounts = {1, 3};
    int[] seriesCounts = {1, 5};
    int[] timeCounts = {1};
    String[] compressions = {WriterUtilities.COMPRESSION_UNCOMPRESSED, WriterUtilities.COMPRESSION_LZW, WriterUtilities.COMPRESSION_J2K, 
        WriterUtilities.COMPRESSION_J2K_LOSSY, WriterUtilities.COMPRESSION_JPEG};
    return WriterUtilities.getData(tileSizes, channelCounts, seriesCounts, timeCounts, compressions, percentageOfTilingTests);
  }
  
  @DataProvider(name = "nonTiling")
  public Object[][] createNonTiling() {
    if (percentageOfSaveBytesTests == 0) {
      return new Object[][] {{0, false, false, 0, 0, 0, null, 0, false}};
    }
    int[] tileSizes = {0};
    int[] channelCounts = {1, 3};
    int[] seriesCounts = {1};
    int[] timeCounts = {1, 5};
    String[] compressions = {WriterUtilities.COMPRESSION_UNCOMPRESSED, WriterUtilities.COMPRESSION_LZW, WriterUtilities.COMPRESSION_J2K, 
        WriterUtilities.COMPRESSION_J2K_LOSSY, WriterUtilities.COMPRESSION_JPEG};
    return WriterUtilities.getData(tileSizes, channelCounts, seriesCounts, timeCounts, compressions, percentageOfSaveBytesTests);
  }

  @BeforeClass
  public void readProperty() throws Exception {
    percentageOfTilingTests = WriterUtilities.getPropValue("testng.runWriterTilingTests");
    percentageOfSaveBytesTests = WriterUtilities.getPropValue("testng.runWriterSaveBytesTests");
  }

  @BeforeMethod
  public void setUp() throws Exception {
    ifd = new IFD();
    writer = new TiffWriterMock();
    metadata = WriterUtilities.createMetadata();
    for (int i = 0; i < 1024 *1024; i++) {
      buf[i] = (byte)(i%255);
    }
  }

  @AfterMethod
  public void tearDown() throws Exception {
    writer.close();
  }

  @Test
  public void testSetBigTiffFileTooLarge() throws IOException, FormatException {
    // Test that no exception is thrown while below the big tiff limit (2147483648L)
    // Exception is thrown when size is out.length() + 2 * (width * height * c * bytesPerPixel)
    writer.setMetadataRetrieve(metadata);
    ((TiffWriterMock)writer).createOutputBuffer(true);
    long length = 4294967296L - (buf.length * 4);
    ((TiffWriterMock)writer).setBufferLength(length);
    writer.setId("test.tiff");
    writer.saveBytes(0, buf, ifd);

    //Test format exception is thrown after the big tiff limit (2147483648L)
    boolean thrown = false;
    try {
      writer.saveBytes(1, buf, ifd);
    }
    catch(FormatException e) {
      if (e.getMessage().contains("File is too large; call setBigTiff(true)")) {
        thrown = true;
      }
    }
    assert(thrown);
  }
  
  @Test
  public void testSetBigTiff() throws IOException, FormatException {
    //Test that no exception is thrown after setting big tiff
    writer.setMetadataRetrieve(metadata);
    ((TiffWriterMock)writer).createOutputBuffer(true);
    long length = 4294967296L - (buf.length * 2);
    ((TiffWriterMock)writer).setBufferLength(length);
    writer.setBigTiff(true);
    writer.setId("test.tiff");
    writer.saveBytes(0, buf, ifd);
  }

  @Test
  public void testSetBigTiffAutomatic() throws IOException, FormatException {
    //Test that no exception is thrown when bigTiff is set automatically due to size
    metadata.setPixelsSizeT(new PositiveInteger(1000), 0);
    writer.setMetadataRetrieve(metadata);
    ((TiffWriterMock)writer).createOutputBuffer(true);
    long length = 4294967296L;
    ((TiffWriterMock)writer).setBufferLength(length);
    writer.setId("test.tiff");
    writer.saveBytes(0, buf, ifd);
  }

  @Test(dataProvider = "bigTiffSuffixes")
  public void testSetBigTiffSuffixes(String suffix) throws IOException, FormatException {
    //Test that no exception is thrown when bigTiff is set automatically due to size
    writer.setMetadataRetrieve(metadata);
    ((TiffWriterMock)writer).createOutputBuffer(true);
    long length = 4294967296L;
    ((TiffWriterMock)writer).setBufferLength(length);
    writer.setId("test." + suffix);
    boolean thrown = false;
    try {
      writer.saveBytes(0, buf, ifd);
    }
    catch(FormatException e) {
      thrown = true;
    }
    if (suffix.contains("tif")) {
      assertEquals(true,thrown);
    }
    else {
      assertEquals(false,thrown);
    }
  }

  @Test(dataProvider = "codecs")
  public void testgetPixelTypes(String codec, int[] pixelTypes) {
    Assert.assertArrayEquals(pixelTypes, writer.getPixelTypes(codec));
  }

  @Test
  public void testGetPlaneCount() throws IOException, FormatException {
    writer.setMetadataRetrieve(metadata);
    writer.setSeries(0);
    assertEquals(WriterUtilities.SIZE_T * WriterUtilities.SIZE_Z * WriterUtilities.SIZE_C, writer.getPlaneCount());
    metadata.setPixelsSizeC(new PositiveInteger(4), 0);
    metadata.setPixelsType(PixelType.INT16, 0);
    writer.setMetadataRetrieve(metadata);
    assertEquals(WriterUtilities.SIZE_T * WriterUtilities.SIZE_Z * 4, writer.getPlaneCount());
  }

  @Test
  public void testGetTileSizeX() throws IOException, FormatException {
    writer.setMetadataRetrieve(metadata);
    assertEquals(0, writer.getTileSizeX());
    writer.close();
    writer = new TiffWriter();
    metadata.setPixelsSizeX(new PositiveInteger(100), 0);
    writer.setMetadataRetrieve(metadata);
    assertEquals(0, writer.getTileSizeX());
  }

  @Test
  public void testSetTileSizeX() {
    writer.setMetadataRetrieve(metadata);
    try {
      for (int i = 16; i < WriterUtilities.SIZE_X; i+=16) {
        writer.setTileSizeX(i);
        assertEquals(i, writer.getTileSizeX());
      }
      writer.setTileSizeX(WriterUtilities.SIZE_X);
      assertEquals(WriterUtilities.SIZE_X, writer.getTileSizeX());
      for (int i = 1; i < 24; i++) {
        writer.setTileSizeX(i);
        assertEquals(16, writer.getTileSizeX());
      }
      for (int i = 24; i < 40; i++) {
        writer.setTileSizeX(i);
        assertEquals(32, writer.getTileSizeX());
      }
    }
    catch(FormatException fe) {
      assert(false);
    }
  }

  @Test
  public void testGetTileSizeY() throws IOException, FormatException {
    writer.setMetadataRetrieve(metadata);
    assertEquals(0, writer.getTileSizeY());
    writer.close();
    writer = new TiffWriter();
    metadata.setPixelsSizeY(new PositiveInteger(100), 0);
    writer.setMetadataRetrieve(metadata);
    assertEquals(0, writer.getTileSizeY());
  }

  @Test
  public void testSetTileSizeY() {
    writer.setMetadataRetrieve(metadata);
    try {
      for (int i = 16; i < WriterUtilities.SIZE_Y; i+=16) {
        writer.setTileSizeY(i);
        assertEquals(i, writer.getTileSizeY());
      }
      writer.setTileSizeY(WriterUtilities.SIZE_Y);
      assertEquals(WriterUtilities.SIZE_Y, writer.getTileSizeY());
      for (int i = 1; i < 24; i++) {
        writer.setTileSizeY(i);
        assertEquals(16, writer.getTileSizeY());
      }
      for (int i = 24; i < 40; i++) {
        writer.setTileSizeY(i);
        assertEquals(32, writer.getTileSizeY());
      }
    }
    catch(FormatException fe) {
      assert(false);
    }
  }

  @Test
  public void testExplicitlyDisableTiling() {
    try {
      writer.setMetadataRetrieve(metadata);
      writer.setTileSizeX(0);
      assertEquals(0, writer.getTileSizeX());
      writer.setTileSizeY(0);
      assertEquals(0, writer.getTileSizeY());
    }
    catch (FormatException e) {
      assert(false);
    }
  }

  @Test
  public void testTileFormatExceptions() {
    boolean thrown = false;
    int tile_size = 16;
    try {
      writer.setTileSizeY(tile_size);
    }
    catch(FormatException e) {
      if (e.getMessage().contains("Size Y must not be null")) {
        thrown = true;
      }
    }
    assert(thrown);
    thrown = false;
    try {
      writer.setTileSizeX(tile_size);
    }
    catch(FormatException e) {
      if (e.getMessage().contains("Size X must not be null")) {
        thrown = true;
      }
    }
    assert(thrown);
    thrown = false;
    try {
      writer.getTileSizeX();
    }
    catch(FormatException e) {
      thrown = true;
    }
    assert(!thrown);
    thrown = false;
    try {
      writer.getTileSizeY();
    }
    catch(FormatException e) {
      thrown = true;
    }
    assert(!thrown);
    writer.setMetadataRetrieve(metadata);
    thrown = false;
    try {
      writer.setTileSizeX(0);
    }
    catch(FormatException e) {
      thrown = true;
    }
    assert(!thrown);
    thrown = false;
    try {
      writer.setTileSizeY(0);
    }
    catch(FormatException e) {
      thrown = true;
    }
    assert(!thrown);
    thrown = false;
    try {
      writer.setTileSizeX(WriterUtilities.SIZE_X);
    }
    catch(FormatException e) {
        thrown = true;
    }
    assert(!thrown);
    thrown = false;
    try {
      writer.setTileSizeY(WriterUtilities.SIZE_Y);
    }
    catch(FormatException e) {
        thrown = true;
    }
    assert(!thrown);
    thrown = false;
    try {
      writer.setTileSizeX(WriterUtilities.SIZE_X + 16);
    }
    catch(FormatException e) {
      thrown = true;
    }
    assert(!thrown);
    thrown = false;
    try {
      writer.setTileSizeY(WriterUtilities.SIZE_Y + 16);
    }
    catch(FormatException e) {
      thrown = true;
    }
    assert(!thrown);
  }

  @Test(dataProvider = "tiling")
  public void testSaveBytesTiling(int tileSize, boolean littleEndian, boolean interleaved, int rgbChannels, 
      int seriesCount, int sizeT, String compression, int pixelType, boolean bigTiff) throws Exception {
    if (percentageOfTilingTests == 0) return;

    File tmp = File.createTempFile("tiffWriterTest_Tiling", ".tiff");
    tmp.deleteOnExit();
    Plane originalPlane = WriterUtilities.writeImage(tmp, tileSize, littleEndian, interleaved, rgbChannels, seriesCount, sizeT, compression, pixelType, bigTiff);

    TiffReader reader = new TiffReader();
    reader.setId(tmp.getAbsolutePath());

    int expectedTileSize = tileSize;
    if (tileSize < TILE_GRANULARITY) {
      expectedTileSize = TILE_GRANULARITY;
    }
    else {
      expectedTileSize = Math.round((float)tileSize/TILE_GRANULARITY) * TILE_GRANULARITY;
    }

    IFD tileIFd = reader.getIFDs().get(0);
    assertEquals(tileIFd.getIFDIntValue(IFD.TILE_LENGTH), expectedTileSize);
    assertEquals(tileIFd.getIFDIntValue(IFD.TILE_WIDTH), expectedTileSize);

    WriterUtilities.checkImage(reader, originalPlane, interleaved, rgbChannels, seriesCount, sizeT, compression);

    tmp.delete();
    reader.close();
  }

  @Test(dataProvider = "nonTiling")
  public void testSaveBytes(int tileSize, boolean littleEndian, boolean interleaved, int rgbChannels, 
      int seriesCount, int sizeT, String compression, int pixelType, boolean bigTiff) throws Exception {
    if (percentageOfSaveBytesTests == 0) return;

    File tmp = File.createTempFile("tiffWriterTest", ".tiff");
    tmp.deleteOnExit();
    Plane originalPlane = WriterUtilities.writeImage(tmp, tileSize, littleEndian, interleaved, rgbChannels, seriesCount, sizeT, compression, pixelType, bigTiff);

    TiffReader reader = new TiffReader();
    reader.setId(tmp.getAbsolutePath());

    WriterUtilities.checkImage(reader, originalPlane, interleaved, rgbChannels, seriesCount, sizeT, compression);

    tmp.delete();
    reader.close();
  }

  @Test(dataProvider = "nonTiling")
  public void testSaveBytesInMemory(int tileSize, boolean littleEndian, boolean interleaved, int rgbChannels,
    int seriesCount, int sizeT, String compression, int pixelType, boolean bigTiff) throws Exception
  {
    if (percentageOfSaveBytesTests == 0) return;

    ByteArrayHandle handle = new ByteArrayHandle();
    String id = Math.random() + "-" + System.currentTimeMillis() + ".tif";
    Location.mapFile(id, handle);
    Plane originalPlane = WriterUtilities.writeImage(id, tileSize, littleEndian, interleaved, rgbChannels, seriesCount, sizeT, compression, pixelType, bigTiff);

    ByteBuffer bytes = handle.getByteBuffer();
    byte[] file = new byte[(int) handle.length()];
    bytes.position(0);
    bytes.get(file);
    handle = new ByteArrayHandle(file);
    Location.mapFile(id, handle);

    TiffReader reader = new TiffReader();
    reader.setId(id);

    WriterUtilities.checkImage(reader, originalPlane, interleaved, rgbChannels, seriesCount, sizeT, compression);

    reader.close();
    Location.mapFile(id, null);
  }

}
