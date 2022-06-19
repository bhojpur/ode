package ode.services.roi.test;

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
import static ode.rtypes.rtime;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ode.services.server.impl.RoiI;
import ode.services.server.test.AbstractServantTest;
import ode.services.roi.GeomTool;
import ode.util.SqlAction;
import ode.api.AMD_IRoi_getRoiMeasurements;
import ode.api.AMD_IRoi_getMeasuredRoisMap;
import ode.api.RoiOptions;
import ode.api.RoiResult;
import ode.model.Annotation;
import ode.model.FileAnnotation;
import ode.model.Image;
import ode.model.ImageI;
import ode.model.Roi;
import ode.model.Shape;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *<pre>
 * // REMAINING TESTS:
 * // * CROSS-PRODUCT (and MULTI)
 * // * INDEXES
 * // * BOUNDARIES
 * // * LONG TABLES, BIG OBJECTS
 * </pre>
 */
@Test(groups = "integration")
public class AbstractRoiITest extends AbstractServantTest {

    protected RoiI user_roisvc, root_roisvc;
    protected GeomTool geomTool;
    protected Shape test;
    protected SqlAction sql;

    @Override
    @BeforeClass
    protected void setUp() throws Exception {
        super.setUp();

        geomTool = (GeomTool) ctx.getBean("geomTool");
        sql = (SqlAction) ctx.getBean("simpleSqlAction");
        user_roisvc = new RoiI(user.be, geomTool, sql);
        user_roisvc.setServiceFactory(user.sf);

        root_roisvc = new RoiI(root.be, geomTool, sql);
        root_roisvc.setServiceFactory(root.sf);
    }

    //
    // assertions
    //

    protected List<FileAnnotation> assertGetImageMeasurements(long imageId)
    throws Exception {
        final RV rv = new RV();
        user_roisvc.getRoiMeasurements_async(new AMD_IRoi_getRoiMeasurements(){
            public void ice_exception(Exception ex) {
                rv.ex = ex;
            }
            public void ice_response(List<Annotation> __ret) {
                rv.rv = __ret;
            }}, imageId, new RoiOptions(), current("getRoiMeasurements"));
     
        rv.assertPassed();
        return (List<FileAnnotation>) rv.rv;
    }
    
    protected Map<Long, RoiResult> assertGetMeasuredRoisMap(long imageId, List<Long> annotationIds)
    throws Exception {
        final RV rv = new RV();
        user_roisvc.getMeasuredRoisMap_async(new AMD_IRoi_getMeasuredRoisMap(){
            public void ice_exception(Exception ex) {
                rv.ex = ex;
            }
            public void ice_response(Map<Long, RoiResult> __ret) {
                rv.rv = __ret;
            }}, imageId, annotationIds, new RoiOptions(), current("getMeasuredRoisMap"));
     
        rv.assertPassed();
        return (Map<Long, RoiResult>) rv.rv;
    }

    //
    // helpers
    //

    protected Roi createRoi(String name, Shape... shapes) throws Exception {
        Image i = new ImageI();
        i.setName(rstring(name));
        return createRoi(i, name, shapes);
    }

    protected Roi createRoi(Image i, String name, Shape... shapes)
            throws Exception {
        Roi roi = new ode.model.RoiI();
        roi.setImage(i);
        roi.addAllShapeSet(Arrays.asList(shapes));
        roi = assertSaveAndReturn(roi);
        roi = (Roi) assertFindByQuery(
                "select roi from Roi roi "
                        + "join fetch roi.shapes shapes join fetch shapes.roi "
                        + "join fetch roi.image image "
                        + "left outer join fetch image.pixels " // OUTER
                        + "where roi.id = " + roi.getId().getValue(), null)
                .get(0);
        return roi;
    }

}