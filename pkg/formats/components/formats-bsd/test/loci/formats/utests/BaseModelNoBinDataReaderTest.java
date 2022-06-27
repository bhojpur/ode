/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.utests;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;

import loci.formats.ChannelFiller;
import loci.formats.ChannelSeparator;
import loci.formats.IFormatReader;
import loci.formats.ImageReader;
import loci.formats.MinMaxCalculator;
import loci.formats.meta.IMetadata;
import loci.formats.ode.ODEXMLMetadataImpl;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class BaseModelNoBinDataReaderTest {

  private BaseModelMock mock;
  
  private File temporaryFile;

  private IFormatReader reader;

  private IMetadata metadata;

  @BeforeClass
  public void setUp() throws Exception {
    mock = new BaseModelMock();
    temporaryFile = File.createTempFile(this.getClass().getName(), ".ode");
    SPWModelReaderTest.writeMockToFile(mock, temporaryFile, false);
  }

  @AfterClass
  public void tearDown() throws Exception {
    temporaryFile.delete();
  }

  @Test
  public void testSetId() throws Exception {
    reader = new MinMaxCalculator(new ChannelSeparator(
        new ChannelFiller(new ImageReader())));
    metadata = new ODEXMLMetadataImpl();
    reader.setMetadataStore(metadata);
    reader.setId(temporaryFile.getAbsolutePath());
  }

  @Test(dependsOnMethods={"testSetId"})
  public void testSeriesCount() {
    assertEquals(1, reader.getSeriesCount());
  }

}
