/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.utests;

import static org.testng.AssertJUnit.assertNotNull;
import loci.common.services.DependencyException;
import loci.common.services.ServiceFactory;
import loci.formats.services.NetCDFService;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MissingNetCDFServiceTest {

  private ServiceFactory sf;

  @BeforeMethod
  public void setUp() throws DependencyException {
    sf = new ServiceFactory();
  }

  @Test(expectedExceptions={DependencyException.class})
  public void testInstantiate() throws DependencyException {
    NetCDFService service = sf.getInstance(NetCDFService.class);
    assertNotNull(service);
  }

}
