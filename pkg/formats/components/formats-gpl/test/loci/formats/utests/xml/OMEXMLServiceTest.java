/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.utests.xml;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.services.ODEXMLService;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ODEXMLServiceTest {

  private static final String XML_FILE = "2008-09.ode";

  private ODEXMLService service;
  private String xml;

  @BeforeMethod
  public void setUp() throws DependencyException, IOException {
    ServiceFactory sf = new ServiceFactory();
    service = sf.getInstance(ODEXMLService.class);

    InputStream s = ODEXMLServiceTest.class.getResourceAsStream(XML_FILE);
    byte[] b = new byte[s.available()];
    s.read(b);
    s.close();

    xml = new String(b);
  }

  @Test
  public void testGetLatestVersion() {
    assertEquals("2016-06", service.getLatestVersion());
  }

  @Test
  public void testCreateEmptyODEXMLMetadata() throws ServiceException {
    assertNotNull(service.createODEXMLMetadata());
  }

  @Test
  public void testCreateODEXMLMetadata() throws ServiceException {
    assertNotNull(service.createODEXMLMetadata(xml));
  }

  @Test
  public void testCreateODEXMLRoot() throws ServiceException {
    assertNotNull(service.createODEXMLRoot(xml));
  }

  @Test
  public void isODEXMLMetadata() throws ServiceException {
    assertEquals(true,
      service.isODEXMLMetadata(service.createODEXMLMetadata()));
  }

  @Test
  public void getODEXMLVersion() throws ServiceException {
    assertEquals("2008-09", service.getODEXMLVersion(xml));
    assertEquals("2016-06",
      service.getODEXMLVersion(service.createODEXMLMetadata(xml)));
  }

  @Test
  public void getODEXML() throws ServiceException {
    assertNotNull(service.getODEXML(service.createODEXMLMetadata(xml)));
  }

  @Test
  public void transformToLatestVersion() throws ServiceException {
    String updated = service.transformToLatestVersion(xml);
    assertEquals("2016-06", service.getODEXMLVersion(updated));
  }

  @Test(expectedExceptions={ServiceException.class})
  public void transformToLatestVersionBad() throws ServiceException {
    service.transformToLatestVersion("");
  }

}
