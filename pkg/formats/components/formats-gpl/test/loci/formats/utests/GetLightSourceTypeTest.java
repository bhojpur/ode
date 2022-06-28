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

import loci.formats.meta.IMetadata;
import loci.formats.ode.ODEXMLMetadataImpl;

import ode.xml.meta.ODEXMLMetadataRoot;
import ode.xml.model.Arc;
import ode.xml.model.Filament;
import ode.xml.model.Instrument;
import ode.xml.model.Laser;
import ode.xml.model.LightEmittingDiode;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class GetLightSourceTypeTest {

  private IMetadata metadata;

  @BeforeClass
  public void setUp() throws Exception {
    metadata = new ODEXMLMetadataImpl();
    ODEXMLMetadataRoot ode = new ODEXMLMetadataRoot();
    Instrument instrument = new Instrument();
    instrument.addLightSource(new Arc());
    instrument.addLightSource(new Filament());
    instrument.addLightSource(new Laser());
    instrument.addLightSource(new LightEmittingDiode());
    ode.addInstrument(instrument);
    metadata.setRoot(ode);
  }

  @Test
  public void testLightSourceType() throws Exception {
    assertEquals(1, metadata.getInstrumentCount());
    assertEquals(4, metadata.getLightSourceCount(0));
    assertEquals("Arc", metadata.getLightSourceType(0, 0));
    assertEquals("Filament", metadata.getLightSourceType(0, 1));
    assertEquals("Laser", metadata.getLightSourceType(0, 2));
    assertEquals("LightEmittingDiode", metadata.getLightSourceType(0, 3));
  }

}
