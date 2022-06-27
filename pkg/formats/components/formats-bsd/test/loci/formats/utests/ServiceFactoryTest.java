/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.utests;

import loci.common.services.DependencyException;
import loci.common.services.Service;
import loci.common.services.ServiceFactory;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ServiceFactoryTest {

  private ServiceFactory sf;

  @BeforeMethod
  public void setUp() throws DependencyException {
    sf = new ServiceFactory();
  }

  @Test(expectedExceptions={DependencyException.class})
  public void testGetNonExistantService() throws DependencyException {
    sf.getInstance(NonExistantService.class);
  }

  interface NonExistantService extends Service { };
}
