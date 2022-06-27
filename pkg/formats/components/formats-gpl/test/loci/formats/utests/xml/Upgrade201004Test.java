/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.utests.xml;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.io.InputStream;

import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.ode.ODEXMLMetadata;
import loci.formats.services.ODEXMLService;

import ode.xml.model.CommentAnnotation;
import ode.xml.model.ODE;
import ode.xml.model.StructuredAnnotations;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Upgrade201004Test {

  private static final String XML_FILE = "2010-04.ode";

  private ODEXMLService service;
  private String xml;
  private ODEXMLMetadata metadata;
  private ODE ode;

  @BeforeMethod
  public void setUp() throws Exception {
    ServiceFactory sf = new ServiceFactory();
    service = sf.getInstance(ODEXMLService.class);

    InputStream s = Upgrade201004Test.class.getResourceAsStream(XML_FILE);
    byte[] b = new byte[s.available()];
    s.read(b);
    s.close();

    xml = new String(b);
    metadata = service.createODEXMLMetadata(xml);
    ode = (ODE) metadata.getRoot();
  }

  @Test
  public void getODEXMLVersion() throws ServiceException {
    assertEquals("2016-06", service.getODEXMLVersion(metadata));
  }

  @Test
  public void validateUpgrade() throws ServiceException {
    assertEquals(1, ode.sizeOfImageList());
    // StringAnnotation --> CommentAnnotation
    StructuredAnnotations structuredAnnotations =
      ode.getStructuredAnnotations();
    assertNotNull(structuredAnnotations);
    assertEquals(1, structuredAnnotations.sizeOfCommentAnnotationList());
    CommentAnnotation commentAnnotation =
      structuredAnnotations.getCommentAnnotation(0);
    assertEquals("StringAnnotation:0", commentAnnotation.getID());
    assertEquals("Transform", commentAnnotation.getNamespace());
    assertEquals("Foobar", commentAnnotation.getValue());
  }

}
