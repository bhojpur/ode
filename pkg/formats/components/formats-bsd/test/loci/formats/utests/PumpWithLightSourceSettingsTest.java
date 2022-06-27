/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.utests;

import static org.testng.AssertJUnit.assertEquals;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import ode.xml.model.Arc;
import ode.xml.model.Channel;
import ode.xml.model.Image;
import ode.xml.model.Instrument;
import ode.xml.model.Laser;
import ode.xml.model.LightSourceSettings;
import ode.xml.model.ODE;
import ode.xml.model.ODEModel;
import ode.xml.model.ODEModelImpl;
import ode.xml.model.Pixels;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Test case which outlines the problems.
 */
public class PumpWithLightSourceSettingsTest {

  private ODE ode = new ODE();

  @BeforeClass
  public void setUp() throws Exception {
    Instrument instrument = new Instrument();
    instrument.setID("Instrument:0");
    // Add a Laser with an Arc pump
    Laser laser = new Laser();
    laser.setID("Laser:0");
    Arc pump = new Arc();
    pump.setID("Arc:0");
    laser.linkPump(pump);
    instrument.addLightSource(laser);
    instrument.addLightSource(pump);
    ode.addInstrument(instrument);
    // Add an Image/Pixels with a LightSourceSettings reference to the Pump
    // on one of its channels.
    Image image = new Image();
    image.setID("Image:0");
    Pixels pixels = new Pixels();
    pixels.setID("Pixels:0");
    Channel channel = new Channel();
    channel.setID("Channel:0");
    LightSourceSettings settings = new LightSourceSettings();
    settings.setID("Arc:0");
    channel.setLightSourceSettings(settings);
    pixels.addChannel(channel);
    image.setPixels(pixels);
    ode.addImage(image);
  }

  @Test
  public void testLightSourceType() throws Exception {
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

}
