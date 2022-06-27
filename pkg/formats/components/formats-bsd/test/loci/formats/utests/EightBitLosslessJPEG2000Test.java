/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.utests;

import static org.testng.AssertJUnit.*;

import java.io.File;
import java.util.ArrayList;

import loci.common.DataTools;
import loci.common.Location;
import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.FormatException;
import loci.formats.IFormatWriter;
import loci.formats.ImageReader;
import loci.formats.MetadataTools;
import loci.formats.meta.IMetadata;
import loci.formats.out.JPEG2000Writer;
import loci.formats.services.ODEXMLService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test case which outlines the problems.
 */
public class EightBitLosslessJPEG2000Test {

  private static final Logger LOGGER =
    LoggerFactory.getLogger(EightBitLosslessJPEG2000Test.class);

  private ArrayList<String> files = new ArrayList<String>();
  private byte[][] pixels = new byte[256][1];

  @BeforeMethod
  public void setUp() throws Exception {
    for (byte v=Byte.MIN_VALUE; v<Byte.MAX_VALUE; v++) {
      int index = v + Byte.MAX_VALUE + 1;
      pixels[index][0] = v;

      String file = index + ".jp2";
      File tempFile = File.createTempFile("test", ".jp2");
      tempFile.deleteOnExit();
      Location.mapId(file, tempFile.getAbsolutePath());
      files.add(file);

      IMetadata metadata;

      try {
        ServiceFactory factory = new ServiceFactory();
        ODEXMLService service = factory.getInstance(ODEXMLService.class);
        metadata = service.createODEXMLMetadata();
      }
      catch (DependencyException exc) {
        throw new FormatException("Could not create ODE-XML store.", exc);
      }
      catch (ServiceException exc) {
        throw new FormatException("Could not create ODE-XML store.", exc);
      }

      MetadataTools.populateMetadata(metadata, 0, "foo", false, "XYCZT",
        "uint8", 1, 1, 1, 1, 1, 1);
      IFormatWriter writer = new JPEG2000Writer();
      writer.setMetadataRetrieve(metadata);
      writer.setId(file);
      writer.saveBytes(0, pixels[index]);
      writer.close();
    }
  }

  @Test
  public void testLosslessPixels() throws Exception {
    int failureCount = 0;
    for (int i=0; i<files.size(); i++) {
      ImageReader reader = new ImageReader();
      reader.setId(files.get(i));
      byte[] plane = reader.openBytes(0);
      if (plane[0] != pixels[i][0]) {
        LOGGER.debug("FAILED on {}", pixels[i][0]);
        failureCount++;
      }
      reader.close();
    }
    assertEquals(failureCount, 0);
  }

}
