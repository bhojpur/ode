/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package spec.schema;

import java.io.InputStream;

import javax.xml.transform.Templates;
import javax.xml.transform.stream.StreamSource;

import loci.common.services.ServiceFactory;
import loci.common.xml.XMLTools;
import loci.formats.services.ODEXMLService;

import ode.xml.model.Image;
import ode.xml.model.ODE;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Collections of tests.
 * Checks if the upgrade from 2011-06 schema to 2012-06 schema works.
 */
@Test(groups = { "all" })
public class Schema2011_06_TO_2012_06_Test {
    private static final Templates UPDATE_201106 =
            XMLTools.getStylesheet("/transforms/2011-06-to-2012-06.xsl",
            Schema2011_06_TO_2012_06_Test.class);

    private static final String RESOURCE =
            "/spec/schema/samples/2011-06/6x4y1z1t1c8b-swatch.ode";

    private static final String IMAGE_NAME = "6x6x1x8-swatch.tif";
    private static final String IMAGE_DATE = "2010-02-23T12:51:30";

    private ODE ode;

    @BeforeClass
    public void setUp() throws Exception {
        InputStream source = this.getClass().getResourceAsStream(RESOURCE);
        ServiceFactory sf = new ServiceFactory();
        ODEXMLService service = sf.getInstance(ODEXMLService.class);
        String xml = XMLTools.transformXML(
                new StreamSource(source), UPDATE_201106);
        ode = (ODE) service.createODEXMLRoot(xml);
    }

    @Test
    public void testName() {
        Assert.assertNotNull(ode);
        Assert.assertEquals(1, ode.sizeOfImageList());
        Image image = ode.getImage(0);
        Assert.assertNotNull(image);
        Assert.assertEquals(IMAGE_NAME, image.getName());
    }

    @Test
    public void testDate() {
        Assert.assertNotNull(ode);
        Assert.assertEquals(1, ode.sizeOfImageList());
        Image image = ode.getImage(0);
        Assert.assertNotNull(image);
        Assert.assertEquals(IMAGE_DATE, image.getAcquisitionDate());
    }
}
