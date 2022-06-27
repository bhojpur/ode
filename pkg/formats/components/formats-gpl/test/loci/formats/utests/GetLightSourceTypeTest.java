/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.utests;

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
