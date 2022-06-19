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

import ode.api.AMD_IRoi_getPoints;
import ode.api.ShapePoints;
import ode.model.Ellipse;
import ode.model.Image;
import ode.model.Rectangle;
import ode.model.Roi;
import ode.model.Shape;

import org.testng.annotations.Test;

@Test(groups = { "integration", "rois" })
public class ContainedPointsTest extends AbstractRoiITest {

    Image i;

    @Test
    public void testGeometryOfRectangle() throws Exception {
        Rectangle r = geomTool.rect(0, 0, 10, 10);
        Roi roi = createRoi("geoOfRect", r);
        ShapePoints pts = assertPoints(roi.getPrimaryShape());
        assertEquals(100, pts.x.length);
        assertEquals(100, pts.y.length);
    }

    @Test
    public void testGeometryOfCircle() throws Exception {
        Ellipse e = geomTool.ellipse(5, 5, 5, 5);
        Roi roi = createRoi("geoOfCircle - inside 0,0,10,10 rect", e);
        ShapePoints pts = assertPoints(roi.getPrimaryShape());
        assertEquals(16, pts.x.length);
        assertEquals(16, pts.y.length);
    }

    protected ShapePoints assertPoints(final Shape shape) throws Exception {
        final RV rv = new RV();
        user_roisvc.getPoints_async(new AMD_IRoi_getPoints() {

            public void ice_exception(Exception ex) {
                rv.ex = ex;
            }

            public void ice_response(ShapePoints __ret) {
                rv.rv = __ret;
            }
        }, shape.getId().getValue(), null);

        rv.assertPassed();
        ShapePoints geom = (ShapePoints) rv.rv;
        assertNotNull(geom.x);
        assertNotNull(geom.y);
        return geom;
    }

}