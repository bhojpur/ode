/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.utests.tiff;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import java.io.IOException;

import loci.common.ByteArrayHandle;
import loci.common.RandomAccessInputStream;
import loci.common.RandomAccessOutputStream;
import loci.formats.FormatException;
import loci.formats.tiff.IFD;
import loci.formats.tiff.TiffParser;
import loci.formats.tiff.TiffSaver;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TiffSaverTest {

  private RandomAccessOutputStream out;

  private RandomAccessInputStream in;

  private TiffSaver tiffSaver;

  private TiffParser tiffParser;

  private IFD ifd;

  private static final int INITIAL_CAPACITY = 1024 * 1024;  // 1MB

  @BeforeMethod
  public void setUp() throws IOException {
    ByteArrayHandle handle = new ByteArrayHandle(INITIAL_CAPACITY);
    out = new RandomAccessOutputStream(handle);
    in = new RandomAccessInputStream(handle);
    tiffSaver = new TiffSaver(out, handle);
    tiffParser = new TiffParser(in);

    ifd = new IFD();
    ifd.putIFDValue(IFD.IMAGE_WIDTH, 512);
    ifd.putIFDValue(IFD.IMAGE_DESCRIPTION, "comment");
  }

  @AfterMethod
  public void tearDown() throws IOException {
      in.close();
      out.close();
  }

  @Test(expectedExceptions={ IllegalArgumentException.class })
  public void testNullOutputStream() throws IOException {
    RandomAccessOutputStream a = null;
    String b = null;
    tiffSaver = new TiffSaver(a, b);
    tiffSaver.writeHeader();
  }

  @Test(expectedExceptions={ IllegalArgumentException.class })
  public void testNullFilename() throws IOException {
    RandomAccessOutputStream a = 
      new RandomAccessOutputStream(new ByteArrayHandle());
    String b = null;
    tiffSaver = new TiffSaver(a, b);
    tiffSaver.writeHeader();
  }

  @Test(expectedExceptions={ IllegalArgumentException.class })
  public void testNullBytes() throws IOException {
    RandomAccessOutputStream a = 
      new RandomAccessOutputStream(new ByteArrayHandle());
    ByteArrayHandle b = null;
    tiffSaver = new TiffSaver(a, b);
    tiffSaver.writeHeader();
  }

  @Test
  public void testWriteHeaderBigEndianRegularTiff() throws IOException {
    tiffSaver.writeHeader();
    assertTrue(tiffParser.isValidHeader());
    assertFalse(tiffParser.checkHeader());
    assertFalse(tiffParser.isBigTiff());
  }

  @Test
  public void testWriteHeaderLittleEndianRegularTiff() throws IOException {
    tiffSaver.setLittleEndian(true);
    tiffSaver.writeHeader();
    assertTrue(tiffParser.isValidHeader());
    assertTrue(tiffParser.checkHeader());
    assertFalse(tiffParser.isBigTiff());
  }

  @Test
  public void testWriteHeaderBigEndianBigTiff() throws IOException {
    tiffSaver.setLittleEndian(false);
    tiffSaver.setBigTiff(true);
    tiffSaver.writeHeader();
    assertTrue(tiffParser.isValidHeader());
    assertFalse(tiffParser.checkHeader());
    assertTrue(tiffParser.isBigTiff());
  }

  @Test
  public void testWriteHeaderLittleEndianBigTiff() throws IOException {
    tiffSaver.setLittleEndian(true);
    tiffSaver.setBigTiff(true);
    tiffSaver.writeHeader();
    assertTrue(tiffParser.isValidHeader());
    assertTrue(tiffParser.checkHeader());
    assertTrue(tiffParser.isBigTiff());
  }

  @Test
  public void testOverwriteIFDValue() throws FormatException, IOException {
    out.seek(0);
    tiffSaver.setBigTiff(false);
    tiffSaver.writeHeader();
    tiffSaver.writeIFD(ifd, 0);

    tiffSaver.overwriteIFDValue(in, 0, IFD.IMAGE_WIDTH, 1024);
    assertEquals(1024, tiffParser.getFirstIFD().getImageWidth());
  }

  @Test
  public void testOverwriteComment() throws FormatException, IOException {
    out.seek(0);
    tiffSaver.writeHeader();
    tiffSaver.writeIFD(ifd, 0);
    tiffSaver.overwriteComment(in, "new comment");
    assertTrue("new comment".equals(tiffParser.getComment()));
  }

  @Test
  public void testOverwriteCommentEqualLength() throws FormatException, IOException {
    out.seek(0);
    tiffSaver.writeHeader();
    tiffSaver.writeIFD(ifd, 46);
    tiffSaver.writeIFD(ifd, 0);
    tiffSaver.overwriteComment(in, "COMMENT");
    assertEquals("COMMENT", tiffParser.getComment());
    assertEquals("comment",
      tiffParser.getIFDs().get(1).getIFDTextValue(IFD.IMAGE_DESCRIPTION));
  }

}
