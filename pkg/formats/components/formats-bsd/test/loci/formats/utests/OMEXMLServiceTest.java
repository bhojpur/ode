/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.utests;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Hashtable;

import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.meta.OriginalMetadataAnnotation;
import loci.formats.ode.ODEXMLMetadata;
import loci.formats.services.ODEXMLService;

import ode.units.UNITS;
import ode.units.quantity.Length;
import ode.xml.model.ODE;
import ode.xml.model.StructuredAnnotations;
import ode.xml.model.XMLAnnotation;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 */
public class ODEXMLServiceTest {

  private ODEXMLService service;

  @BeforeMethod
  public void setUp() throws DependencyException {
    ServiceFactory sf = new ServiceFactory();
    service = sf.getInstance(ODEXMLService.class);
  }

  @Test
  public void testOriginalMetadata() throws ServiceException {
    ODEXMLMetadata metadata = service.createODEXMLMetadata();
    service.populateOriginalMetadata(metadata, "testKey", "testValue");

    Hashtable metadataTable = service.getOriginalMetadata(metadata);
    assertEquals(metadataTable.size(), 1);
    assertTrue("testValue".equals(metadataTable.get("testKey")));

    ODE root = (ODE) metadata.getRoot();
    StructuredAnnotations annotations = root.getStructuredAnnotations();
    assertEquals(annotations.sizeOfXMLAnnotationList(), 1);
    XMLAnnotation xmlAnn = annotations.getXMLAnnotation(0);
    String txt = "<OriginalMetadata><Key>testKey</Key><Value>testValue</Value></OriginalMetadata>";
    assertEquals(txt, xmlAnn.getValue());
    OriginalMetadataAnnotation omAnn = (OriginalMetadataAnnotation) xmlAnn;
    assertEquals("testValue", omAnn.getValueForKey());
  }

  /**
   * Test that the XML serialization of floating point unit properties
   * includes a decimal point. In the schema a shape's stroke width is
   * {@code type="xsd:float"} which in Bhojpur ODE is mapped to a {@code double}.
   * @throws ServiceException unexpected
   */
  @Test
  public void testFloatingPointUnitProperty() throws ServiceException {
    final Length propertyValue = new Length(3.0d, UNITS.PIXEL);

    final StringBuffer expectedText = new StringBuffer();
    expectedText.append(" StrokeWidth=");
    expectedText.append('"');
    expectedText.append(propertyValue.value().doubleValue());
    expectedText.append('"');

    final ODEXMLMetadata metadata = service.createODEXMLMetadata();
    metadata.setROIID("test ROI", 0);
    metadata.setPointID("test point", 0, 0);
    metadata.setPointX(0.0, 0, 0);
    metadata.setPointY(0.0, 0, 0);
    metadata.setPointStrokeWidth(propertyValue, 0, 0);
    final String xml = service.getODEXML(metadata);

    assertTrue(xml.contains(expectedText));
  }


  /**
   * Test that the XML serialization of integer unit properties does not
   * include a decimal point. In the schema a shape's font size is
   * {@code type="ODE:NonNegativeInt"} which in Bhojpur ODE is mapped to a
   * {@code double}.
   * @throws ServiceException unexpected
   */
  @Test
  public void testIntegerUnitProperty() throws ServiceException {
    final Length propertyValue = new Length(12.0d, UNITS.POINT);

    final StringBuffer expectedText = new StringBuffer();
    expectedText.append(" FontSize=");
    expectedText.append('"');
    expectedText.append(propertyValue.value().longValue());
    expectedText.append('"');

    final ODEXMLMetadata metadata = service.createODEXMLMetadata();
    metadata.setROIID("test ROI", 0);
    metadata.setPointID("test point", 0, 0);
    metadata.setPointX(0.0, 0, 0);
    metadata.setPointY(0.0, 0, 0);
    metadata.setPointFontSize(propertyValue, 0, 0);
    final String xml = service.getODEXML(metadata);

    assertTrue(xml.contains(expectedText));
  }
}
