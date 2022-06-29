/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.utests.xml;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.io.InputStream;

import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.ode.ODEXMLMetadata;
import loci.formats.services.ODEXMLService;

import ode.xml.model.Image;
import ode.xml.model.Label;
import ode.xml.model.ODE;
import ode.xml.model.ROI;
import ode.xml.model.Shape;
import ode.xml.model.Union;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Upgrade201106Test {

  private static final String XML_FILE = "2011-06.ode";

  private ODEXMLService service;
  private String xml;
  private ODEXMLMetadata metadata;
  private ODE ode;

  @BeforeMethod
  public void setUp() throws Exception {
    ServiceFactory sf = new ServiceFactory();
    service = sf.getInstance(ODEXMLService.class);

    InputStream s = Upgrade201106Test.class.getResourceAsStream(XML_FILE);
    byte[] b = new byte[s.available()];
    s.read(b);
    s.close();

    xml = new String(b);
    metadata = service.createODEXMLMetadata(xml);
    ode = (ODE) metadata.getRoot();
  }

  @Test
  public void getODEXMLVersion() throws ServiceException {
    assertEquals("2018-03", service.getODEXMLVersion(metadata));
  }

  @Test
  public void validateUpgrade() throws ServiceException {
    assertEquals(1, ode.sizeOfImageList());
    Image image = ode.getImage(0);
    assertNotNull(image.getAcquisitionDate());

    assertEquals(1, ode.sizeOfROIList());
    ROI roi = ode.getROI(0);
    Union union = roi.getUnion();
    assertEquals(1, union.sizeOfShapeList());
    Shape shape = union.getShape(0);
    assertTrue(shape instanceof Label);
    assertNotNull(shape.getText());
  }

}
