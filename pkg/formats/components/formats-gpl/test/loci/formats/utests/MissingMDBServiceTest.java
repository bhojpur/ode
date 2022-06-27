/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.utests;

import static org.testng.AssertJUnit.assertNotNull;
import loci.common.services.DependencyException;
import loci.common.services.ServiceFactory;
import loci.formats.services.MDBService;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MissingMDBServiceTest {

  private ServiceFactory sf;

  @BeforeMethod
  public void setUp() throws DependencyException {
    sf = new ServiceFactory();
  }

  @Test(expectedExceptions={DependencyException.class})
  public void testInstantiate() throws DependencyException {
    MDBService service = sf.getInstance(MDBService.class);
    assertNotNull(service);
  }

}
