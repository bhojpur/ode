/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.utests.xml;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.io.IOException;

import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.ode.ODEXMLMetadata;
import loci.formats.services.ODEXMLService;

import ode.xml.model.ODE;
import ode.xml.model.Plate;
import ode.xml.model.Well;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BackReferenceTest {

  private ODEXMLService service;
  private ODEXMLMetadata metadata;

  @BeforeMethod
  public void setUp() throws DependencyException, ServiceException, IOException
  {
    ServiceFactory sf = new ServiceFactory();
    service = sf.getInstance(ODEXMLService.class);
    metadata = service.createODEXMLMetadata();
  }

  @Test
  public void testPlateWellReferences() {
    metadata.setPlateID("Plate:0", 0);
    metadata.setWellID("Well:0:0", 0, 0);

    ODE root = (ODE) metadata.getRoot();

    Plate plate = root.getPlate(0);
    Well well = plate.getWell(0);

    assertNotNull(plate);
    assertNotNull(well);
    assertEquals(plate, well.getPlate());
  }

}
