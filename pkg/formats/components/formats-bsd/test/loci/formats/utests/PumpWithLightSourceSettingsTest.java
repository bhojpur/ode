package loci.formats.utests;

// Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

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
