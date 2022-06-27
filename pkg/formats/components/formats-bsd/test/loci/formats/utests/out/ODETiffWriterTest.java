/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.utests.out;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import loci.formats.FormatException;
import loci.formats.in.TiffReader;
import loci.formats.in.ODETiffReader;
import loci.formats.in.DynamicMetadataOptions;
import loci.formats.meta.IMetadata;
import loci.formats.out.ODETiffWriter;
import loci.formats.tiff.IFD;
import ode.xml.model.enums.PixelType;
import ode.xml.model.primitives.PositiveInteger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests the functionality of ODETiffWriter
 */
public class ODETiffWriterTest {
  private ODETiffWriter writer;
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

    int[] tileSizes = {1, 32, 43, 64};
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
    int[] tileSizes = {WriterUtilities.PLANE_WIDTH};
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
    writer = new ODETiffWriter();
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
  public void testGetPlaneCount() throws IOException, FormatException {
    writer.setMetadataRetrieve(metadata);
    writer.setSeries(0);
    assertEquals(WriterUtilities.SIZE_T * WriterUtilities.SIZE_Z * WriterUtilities.SIZE_C, writer.getPlaneCount());
    metadata.setPixelsSizeC(new PositiveInteger(4), 0);
    metadata.setPixelsType(PixelType.INT16, 0);
    writer.setMetadataRetrieve(metadata);
    assertEquals(WriterUtilities.SIZE_T * WriterUtilities.SIZE_Z * 4, writer.getPlaneCount());
  }

  @Test(dataProvider = "tiling")
  public void testSaveBytesTiling(int tileSize, boolean littleEndian, boolean interleaved, int rgbChannels, 
      int seriesCount, int sizeT, String compression, int pixelType, boolean bigTiff) throws Exception {
    if (percentageOfTilingTests == 0) return;

    File tmp = File.createTempFile("ODETiffWriterTest_Tiling", ".ode.tiff");
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

    File tmp = File.createTempFile("ODETiffWriterTest", ".ode.tiff");
    tmp.deleteOnExit();
    Plane originalPlane = WriterUtilities.writeImage(tmp, tileSize, littleEndian, interleaved, rgbChannels, seriesCount, sizeT, compression, pixelType, bigTiff);

    TiffReader reader = new TiffReader();
    reader.setId(tmp.getAbsolutePath());

    WriterUtilities.checkImage(reader, originalPlane, interleaved, rgbChannels, seriesCount, sizeT, compression);

    tmp.delete();
    reader.close();
  }

  @Test
  public void testCompanion() throws Exception {
    Path wd = Files.createTempDirectory(this.getClass().getName());
    File outFile = wd.resolve("test.ode.tif").toFile();
    File cFile = wd.resolve("test.companion.ode").toFile();
    String companion = cFile.getAbsolutePath();
    DynamicMetadataOptions options = new DynamicMetadataOptions();
    options.set(ODETiffWriter.COMPANION_KEY, companion);
    int planeCount =
      WriterUtilities.SIZE_Z * WriterUtilities.SIZE_C * WriterUtilities.SIZE_T;

    ODETiffWriter cwriter = new ODETiffWriter();
    cwriter.setMetadataOptions(options);
    cwriter.setMetadataRetrieve(metadata);
    cwriter.setId(outFile.getAbsolutePath());
    cwriter.setSeries(0);
    byte[] img = new byte[WriterUtilities.SIZE_X * WriterUtilities.SIZE_Y];
    for (int i = 0; i < planeCount; i++) {
      cwriter.saveBytes(i, img);
    }
    cwriter.close();

    assertTrue(cFile.exists());
    ODETiffReader reader = new ODETiffReader();
    reader.setId(companion);
    assertEquals(reader.getSizeX(), WriterUtilities.SIZE_X);
    assertEquals(reader.getSizeY(), WriterUtilities.SIZE_Y);
    assertEquals(reader.getSizeZ(), WriterUtilities.SIZE_Z);
    assertEquals(reader.getSizeC(), WriterUtilities.SIZE_C);
    assertEquals(reader.getSizeT(), WriterUtilities.SIZE_T);
    reader.close();
    outFile.deleteOnExit();
    cFile.deleteOnExit();
    wd.toFile().deleteOnExit();
  }
}
