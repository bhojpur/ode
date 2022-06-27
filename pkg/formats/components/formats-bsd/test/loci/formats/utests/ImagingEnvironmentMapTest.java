/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.utests;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import ode.xml.model.Image;
import ode.xml.model.ImagingEnvironment;
import ode.xml.model.MapPair;
import ode.xml.model.ODE;
import ode.xml.model.ODEModel;
import ode.xml.model.ODEModelImpl;
import ode.xml.model.Pixels;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Test case for ImagingEnvironment Map values
 */
public class ImagingEnvironmentMapTest {

  private ODE ode = new ODE();

  @BeforeClass
  public void setUp() throws Exception {
    // Add an Image/Pixels
    Image image = new Image();
    image.setID("Image:0");
    Pixels pixels = new Pixels();
    pixels.setID("Pixels:0");
    image.setPixels(pixels);

    // Add an ImagingEnvironment with an Map
    ImagingEnvironment imagingEnvironment = new ImagingEnvironment();

    List<MapPair> map = new ArrayList<MapPair>();
    map.add(new MapPair("a", "1"));
    map.add(new MapPair("d", "2"));
    map.add(new MapPair("c", "3"));
    map.add(new MapPair("b", "4"));
    map.add(new MapPair("e", "5"));
    map.add(new MapPair("c", "6"));
    assertEquals(6, map.size());
    imagingEnvironment.setMap(map);

    image.setImagingEnvironment(imagingEnvironment);

    ode.addImage(image);
  }

  @Test
  public void testGenericExcitationSourceValid() throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder parser = factory.newDocumentBuilder();
    Document document = parser.newDocument();
    // Produce a valid ODE DOM element hierarchy
    Element root = ode.asXMLElement(document);
    SPWModelMock.postProcess(root, document, false);
    ODEModel model = new ODEModelImpl();
    ode = new ODE(document.getDocumentElement(), model);
    model.resolveReferences();
  }

  @Test
  public void testGenericExcitationSourceMapContent() throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder parser = factory.newDocumentBuilder();
    Document document = parser.newDocument();
    // Produce a valid Bhojpur ODE DOM element hierarchy
    Element root = ode.asXMLElement(document);
    SPWModelMock.postProcess(root, document, false);
    ODEModel model = new ODEModelImpl();
    ode = new ODE(document.getDocumentElement(), model);
    model.resolveReferences();

    assertNotNull(ode); 
    assertEquals(ode.getImage(0).getPixels().getID(), "Pixels:0"); 
    assertNotNull(ode.getImage(0).getImagingEnvironment()); 

    ImagingEnvironment imagingEnvironment = ode.getImage(0).getImagingEnvironment(); 
    List<MapPair> dataMap = imagingEnvironment.getMap();

    assertEquals(6, dataMap.size());
    assertPair(dataMap, 0, "a", "1");
    assertPair(dataMap, 1, "d", "2");
    assertPair(dataMap, 2, "c", "3");
    assertPair(dataMap, 3, "b", "4");
    assertPair(dataMap, 4, "e", "5");
    assertPair(dataMap, 5, "c", "6");
  }

  void assertPair(List<MapPair> dataMap, int idx, String name, String value) {
    assertEquals(name, dataMap.get(idx).getName());
    assertEquals(value, dataMap.get(idx).getValue());
  }
}
