/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.utests;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import ode.xml.model.Annotation;
import ode.xml.model.Channel;
import ode.xml.model.Image;
import ode.xml.model.ODE;
import ode.xml.model.ODEModel;
import ode.xml.model.ODEModelImpl;
import ode.xml.model.Pixels;
import ode.xml.model.XMLAnnotation;
import ode.xml.model.enums.EnumerationException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

/**
 * Test case which outlines the problems.
 */
public class XMLAnnotationTest {

  private ODE ode;

  @BeforeClass
  public void setUp() throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder parser = factory.newDocumentBuilder();
    Document document = parser.parse(
        this.getClass().getResourceAsStream("XMLAnnotationTest.ode"));
    ODEModel model = new ODEModelImpl();
    // Read string XML in as a DOM tree and parse into the object hierarchy
    ode = new ODE(document.getDocumentElement(), model);
    model.resolveReferences();
  }

  @Test
  public void testValidXMLAnnotation() throws EnumerationException {
    assertNotNull(ode);
    assertEquals(1, ode.sizeOfImageList());
    Image image = ode.getImage(0);
    Pixels pixels = image.getPixels();
    assertNotNull(pixels);
    assertEquals(3, pixels.sizeOfChannelList());
    Channel channel = pixels.getChannel(0);
    assertEquals(1, channel.sizeOfLinkedAnnotationList());
    Annotation annotation = channel.getLinkedAnnotation(0);
    assertEquals(XMLAnnotation.class, annotation.getClass());
    String annotationValue = ((XMLAnnotation) annotation).getValue();

    // normalize line endings if the test is run on Windows
    annotationValue = annotationValue.replaceAll("\r\n", "\n");
    assertEquals("<TestData>\n                    <key>foo</key>\n\t\t\t\t\t<value>bar</value>\n                </TestData>", annotationValue);
  }

}
