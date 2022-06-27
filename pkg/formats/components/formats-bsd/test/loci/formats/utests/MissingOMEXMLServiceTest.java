/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.utests;

import static org.testng.AssertJUnit.assertNotNull;
import loci.common.services.DependencyException;
import loci.common.services.ServiceFactory;
import loci.formats.services.ODEXMLService;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MissingODEXMLServiceTest {

  private ServiceFactory sf;

  @BeforeMethod
  public void setUp() throws DependencyException {
    sf = new ServiceFactory();
  }

  @Test(expectedExceptions={DependencyException.class})
  public void testInstantiate() throws DependencyException {
    ODEXMLService service = sf.getInstance(ODEXMLService.class);
    assertNotNull(service);
  }

}
