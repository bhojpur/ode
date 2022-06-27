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

import ode.xml.model.Channel;
import ode.xml.model.Image;
import ode.xml.model.Instrument;
import ode.xml.model.LightSourceSettings;
import ode.xml.model.GenericExcitationSource;
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
 * Test case for GenericExcitationSource Map values
 */
public class GenericExcitationMapTest {

  private ODE ode = new ODE();

  @BeforeClass
  public void setUp() throws Exception {
    Instrument instrument = new Instrument();
    instrument.setID("Instrument:0");
    // Add a GenericExcitationSource with an Map
    GenericExcitationSource geSource = new GenericExcitationSource();
    geSource.setID("LightSource:0");
    List<MapPair> dataMap = new ArrayList<MapPair>();
    dataMap.add(new MapPair("a", "1"));
    dataMap.add(new MapPair("d", "2"));
    dataMap.add(new MapPair("c", "3"));
    dataMap.add(new MapPair("b", "4"));
    dataMap.add(new MapPair("e", "5"));
    dataMap.add(new MapPair("c", "6"));
    assertEquals(6, dataMap.size());
    geSource.setMap(dataMap);

    instrument.addLightSource(geSource);
    ode.addInstrument(instrument);
    // Add an Image/Pixels with a LightSourceSettings reference to the
    // GenericExcitationSource on one of its channels.
    Image image = new Image();
    image.setID("Image:0");
    Pixels pixels = new Pixels();
    pixels.setID("Pixels:0");
    Channel channel = new Channel();
    channel.setID("Channel:0");
    LightSourceSettings settings = new LightSourceSettings();
    settings.setID("LightSource:0");
    channel.setLightSourceSettings(settings);
    pixels.addChannel(channel);
    image.setPixels(pixels);
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
    // Produce a valid ODE DOM element hierarchy
    Element root = ode.asXMLElement(document);
    SPWModelMock.postProcess(root, document, false);
    ODEModel model = new ODEModelImpl();
    ode = new ODE(document.getDocumentElement(), model);
    model.resolveReferences();

    assertNotNull(ode); 
    assertEquals(ode.getImage(0).getPixels().getChannel(0).getLightSourceSettings().getID(), "LightSource:0"); 

    assertNotNull(ode.getInstrument(0).getLightSource(0));
    GenericExcitationSource geSource = (GenericExcitationSource) ode.getInstrument(0).getLightSource(0); 
    List<MapPair> dataMap = geSource.getMap();

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
