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

import ode.services.roi.GeomTool;
import ode.model.SmartLineI;
import ode.model.SmartShape.Util;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 */
@Test(groups = { "rois" })
public class ShapeUnitTest {

    GeomTool geomTool = new GeomTool(null, null, null);

    @Test
    public void testDiscriminators() throws Exception {
        Assert.assertEquals("text", geomTool.discriminator("Text"));
        Assert.assertEquals("text", geomTool.discriminator("TextI"));
        Assert.assertEquals("text", geomTool.discriminator("ode.model.Text"));
        Assert.assertEquals("text", geomTool.discriminator("ode.model.TextI"));
        Assert.assertEquals("text", geomTool.discriminator("ode::model::Text"));
        Assert.assertEquals("text", geomTool.discriminator("::ode::model::Text"));
        Assert.assertEquals("mask", geomTool.discriminator("Mask"));
        Assert.assertEquals("mask", geomTool.discriminator("MaskI"));
        Assert.assertEquals("mask", geomTool.discriminator("ode.model.Mask"));
        Assert.assertEquals("mask", geomTool.discriminator("ode.model.MaskI"));
        Assert.assertEquals("mask", geomTool.discriminator("ode::model::Mask"));
        Assert.assertEquals("mask", geomTool.discriminator("::ode::model::Mask"));
    }

    
    @Test
    public void testGeometryOfLineGood() throws Exception {
        SmartLineI l = (SmartLineI) geomTool.ln(0, 0, 1, 1);
        Assert.assertTrue(Util.checkNonNull(l.asPoints()));
    }

    @Test(expectedExceptions = AssertionError.class)
    public void testGeometryOfLineBad() throws Exception {
        SmartLineI l = (SmartLineI) geomTool.ln(0, 0, 1, 1);
        l.setY2(null);
        Assert.assertFalse(Util.checkNonNull(l.asPoints()));
    }
}