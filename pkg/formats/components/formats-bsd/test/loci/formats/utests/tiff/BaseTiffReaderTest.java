/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.utests.tiff;

import static org.testng.AssertJUnit.assertEquals;
import loci.formats.tiff.IFD;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests the functionality of BaseTiffReader
 */
public class BaseTiffReaderTest {

  private IFD ifd;
  private BaseTiffReaderMock reader;

  @BeforeClass
  public void setUp() throws Exception {
    ifd = new IFD();
    reader = new BaseTiffReaderMock();
  }
  
  @AfterClass
  public void tearDown() throws Exception {
    ifd.clear();
    reader.clearIFDs();
  }
  
  @Test
  public void testCreationDateString() {
    ifd.put(IFD.DATE_TIME, "CreationDate");
    reader.addIFD(ifd);
    assertEquals("CreationDate", reader.getCreationDate());
  }
  
  @Test
  public void testCreationDateNull() {
    ifd.put(IFD.DATE_TIME, null);
    reader.addIFD(ifd);
    assertEquals(null, reader.getCreationDate());
  }
  
  @Test
  public void testCreationDateEmptyString() {
    ifd.put(IFD.DATE_TIME, "");
    reader.addIFD(ifd);
    assertEquals("", reader.getCreationDate());
  }
  
  @Test
  public void testCreationDateStringArray() {
    String [] creationDates = new String[2];
    creationDates[0] = "CreationDate1";
    creationDates[1] = "CreationDate2";
    ifd.put(IFD.DATE_TIME, creationDates);
    reader.addIFD(ifd);
    assertEquals("CreationDate1", reader.getCreationDate());
  }
  
  @Test
  public void testCreationDateEmptyStringArray() {
    String [] creationDates = new String[1];
    creationDates[0] = "";
    ifd.put(IFD.DATE_TIME, creationDates);
    reader.addIFD(ifd);
    assertEquals("", reader.getCreationDate());
  }
  
  @Test
  public void testCreationDateNullStringArray() {
    String [] creationDates = new String[1];
    creationDates[0] = null;
    ifd.put(IFD.DATE_TIME, creationDates);
    reader.addIFD(ifd);
    assertEquals(null, reader.getCreationDate());
  }
  
  @Test
  public void testCreationDateZeroLengthArray() {
    String [] creationDates = new String[0];
    ifd.put(IFD.DATE_TIME, creationDates);
    reader.addIFD(ifd);
    assertEquals(null, reader.getCreationDate());
  }
  
  @Test
  public void testCreationDateMultipleIFDs() {
    ifd.put(IFD.DATE_TIME, "CreationDate1");
    reader.addIFD(ifd);
    IFD ifd2 = new IFD();
    ifd2.put(IFD.DATE_TIME, "CreationDate2");
    reader.addIFD(ifd2);
    assertEquals("CreationDate1", reader.getCreationDate());
  }

  // TODO: Test remaining Metadata initialisation and helper methods
}
