package ode.services.server.test;

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

import static ode.rtypes.rstring;

import ode.services.server.impl.ExporterI;
import ode.services.server.impl.OdeMetadata;
import ode.services.db.DatabaseIdentity;
import ode.api.AMD_Exporter_addImage;
import ode.api.AMD_Exporter_generateTiff;
import ode.api.AMD_Exporter_generateXml;
import ode.api.AMD_Exporter_read;
import ode.api.AMD_IConfig_getDatabaseUuid;
import ode.model.CommentAnnotationI;
import ode.model.Image;
import ode.model.ImageI;
import ode.model.TagAnnotationI;
import ode.model.TermAnnotationI;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups = "integration")
public class ExporterITest extends AbstractServantTest {

    DatabaseIdentity db = new DatabaseIdentity("test", "test");
    ExporterI user_e, root_e;

    @Override
    @BeforeMethod
    protected void setUp() throws Exception {
        super.setUp();

        user_e = new ExporterI(user.be, db, null);
        user_e.setServiceFactory(user.sf);

        root_e = new ExporterI(root.be, db, null);
        root_e.setServiceFactory(root.sf);
    }

    //
    // XML Generation
    //

    //
    // export
    //

    @Test
    public void testForDatabaseUuid() throws Exception {
        final RV rv = new RV();
        user.config.getDatabaseUuid_async(new AMD_IConfig_getDatabaseUuid(){

            public void ice_exception(Exception ex) {
                rv.ex = ex;
            }

            public void ice_response(String __ret) {
                rv.rv = __ret;
            }}, current("getDatabaseUuid"));
        rv.assertPassed();
        assertNotNull(rv.rv);
    }

    @Test
    public void testForExistingExternalInfo() throws Exception {
        fail("NYI ~ all queries will need to load them.");
    }

    @Test
    public void testBasicExport() throws Exception {
        Image i = assertNewImage();
        assertAddImage(i.getId().getValue());
        assertGenerateXml();
        byte[] buf = assertRead(0, 1024 * 1024);
        assertNotNull(buf);
        assertTrue(buf.length > 1);

        // Now let's compare the XML
        String xml1 = new String(buf);
        OdeMetadata retrieve = new OdeMetadata(db);
        retrieve.addImage(new ImageI(i.getId(), false));
        ExporterI exporter = new ExporterI(null, null, null);
        String xml2 = exporter.generateXml(retrieve);
        System.out.println(xml1);
        assertEquals(xml1, xml2);

        // After reading, nothing should be returned
        buf = assertRead(0, 1024 * 1024);
        assertEquals(0, buf.length);
    }

//    Used for manually testing
//    @Test(enabled = false)
//    public void testBasicExportDicom() throws Exception {
//        assertAddImage(root_e, 701);
//        assertGenerateTiff(root_e);
//    }

    @Test
    public void testTiffGeneration() throws Exception {
        Image i = assertNewImage();
        assertAddImage(i.getId().getValue());
        long size = assertGenerateTiff();
        byte[] buf = assertRead(0, (int)size);
        assertTrue(size > 0);
        assertEquals(size, buf.length);
        
    }
    // Helpers
    // =========================================================================

    private Image assertNewImage() throws Exception {
        long id = this.makePixels();
        Image i = (Image) assertFindByQuery(
                "select i from Image i " +
                "join fetch i.pixels p " +
                "left outer join fetch i.annotationLinks " +
                "where p.id = " + id, null).get(0);
        CommentAnnotationI ca = new CommentAnnotationI();
        ca.setNs(rstring("a_namespace"));
        ca.setDescription(rstring("a_description"));
        ca.setTextValue(rstring("a_textValue"));
        TagAnnotationI ta = new TagAnnotationI();
        ta.setNs(rstring("a_tag_namespace"));
        ta.setDescription(rstring("a_tag_description"));
        ta.setTextValue(rstring("a_tag_textValue"));
        TermAnnotationI terma = new TermAnnotationI();
        terma.setNs(rstring("a_term_namespace"));
        terma.setDescription(rstring("a_term_description"));
        terma.setTermValue(rstring("a_term_textValue"));
        i.linkAnnotation(ca);
        i.linkAnnotation(ta);
        i.linkAnnotation(terma);
        i = assertSaveAndReturn(i);
        // Image i = new ImageI();
        // i.setName(rstring("basic export"));
        // i.addPixels(new PixelsI(id, false));
        // i = assertSaveAndReturn(i);
        return i;
    }
    
    private long assertGenerateXml() throws Exception {

        final RV rv = new RV();
        user_e.generateXml_async(new AMD_Exporter_generateXml() {

            public void ice_exception(Exception ex) {
                rv.ex = ex;
            }

            public void ice_response(long val) {
                rv.rv = val;
            }
        }, null);
        rv.assertPassed();
        return ((Long)rv.rv).longValue();
    }

    private long assertGenerateTiff() throws Exception {
        return assertGenerateTiff(user_e);
    }

    private long assertGenerateTiff(ExporterI e) throws Exception {

        final RV rv = new RV();
        e.generateTiff_async(new AMD_Exporter_generateTiff() {

            public void ice_exception(Exception ex) {
                rv.ex = ex;
            }

            public void ice_response(long val) {
                rv.rv = val;
            }
        }, null);
        rv.assertPassed();
        return ((Long)rv.rv).longValue();
    }

    private void assertAddImage(long id) throws Exception {
        assertAddImage(user_e, id);
    }
    
    private void assertAddImage(ExporterI e, long id) throws Exception {

        final RV rv = new RV();
        e.addImage_async(new AMD_Exporter_addImage() {

            public void ice_exception(Exception ex) {
                rv.ex = ex;
            }

            public void ice_response() {
                rv.rv = null;
            }
        }, id, null);
        rv.assertPassed();
    }

    private byte[] assertRead(long pos, int size) throws Exception {

        final RV rv = new RV();
        user_e.read_async(new AMD_Exporter_read() {

            public void ice_exception(Exception ex) {
                rv.ex = ex;
            }

            public void ice_response(byte[] buf) {
                rv.rv = buf;
            }
        }, pos, size, null);
        rv.assertPassed();
        return (byte[]) rv.rv;
    }

}