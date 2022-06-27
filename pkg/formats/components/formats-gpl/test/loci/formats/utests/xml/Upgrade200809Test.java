/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.utests.xml;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.io.InputStream;

import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.ode.ODEXMLMetadata;
import loci.formats.services.ODEXMLService;

import ode.xml.model.Image;
import ode.xml.model.ODE;
import ode.xml.model.Pixels;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Upgrade200809Test {

  private static final String XML_FILE = "2008-09.ode";

  private ODEXMLService service;
  private String xml;
  private ODEXMLMetadata metadata;
  private ODE ode;

  @BeforeMethod
  public void setUp() throws Exception {
    ServiceFactory sf = new ServiceFactory();
    service = sf.getInstance(ODEXMLService.class);

    InputStream s = Upgrade200809Test.class.getResourceAsStream(XML_FILE);
    byte[] b = new byte[s.available()];
    s.read(b);
    s.close();

    xml = new String(b);
    metadata = service.createODEXMLMetadata(xml);
    ode = (ODE) metadata.getRoot();
  }

  @Test
  public void getODEXMLVersion() throws ServiceException {
    assertEquals("2016-06", service.getODEXMLVersion(metadata));
  }

  @Test
  public void validateUpgrade() throws ServiceException {
    assertEquals(1, ode.sizeOfImageList());
    Image image = ode.getImage(0);
    Pixels pixels = image.getPixels();
    assertNotNull(pixels);
  }

  @Test
  public void testChannelColor() {
    // ODE:Channel, Color now use new colour representation and is required
    assertEquals(-1, metadata.getChannelColor(0, 0).getValue().intValue());
  }
  
  @Test
  public void testCustomAttributes() throws ServiceException {
    assertNotNull(ode.getStructuredAnnotations());
    assertEquals(4, metadata.getXMLAnnotationCount());
    String expectedAnnotation = "<OriginalMetadata ID=\"OriginalMetadata:7\" "
        + "Name=\"Custom Metadata 7\" Value=\"7\"/>";
    assertEquals(expectedAnnotation, metadata.getXMLAnnotationValue(2));
  }

}
