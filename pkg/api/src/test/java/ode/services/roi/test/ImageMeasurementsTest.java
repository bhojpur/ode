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

import static ode.rtypes.rlong;
import static ode.rtypes.rstring;
import static ode.rtypes.rtime;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ode.api.RoiResult;
import ode.constants.namespaces.NSMEASUREMENT;
import ode.model.FileAnnotation;
import ode.model.FileAnnotationI;
import ode.model.Image;
import ode.model.ImageAnnotationLink;
import ode.model.ImageAnnotationLinkI;
import ode.model.ImageI;
import ode.model.OriginalFile;
import ode.model.OriginalFileI;
import ode.model.Plate;
import ode.model.PlateI;
import ode.model.Roi;
import ode.model.RoiAnnotationLink;
import ode.model.RoiAnnotationLinkI;
import ode.model.Shape;
import ode.model.Well;
import ode.model.WellI;
import ode.model.WellSample;
import ode.model.WellSampleI;

import org.testng.annotations.Test;

/**
 *
 */
@Test(groups = { "integration", "rois" })
public class ImageMeasurementsTest extends AbstractRoiITest {

    Image i;

    private void setupImage() throws Exception {
        i = new ImageI();
        i.setName(rstring("RoiPerformanceTest"));
        i = assertSaveAndReturn(i);
        i.unload();
    }
    
    @Test
    public void testMeasurements() throws Exception {
        setupImage();
        Roi roi = createRoi(i, "meas", geomTool.random(1).toArray(new Shape[0]));

        FileAnnotation fa = new FileAnnotationI();
        fa.setNs(rstring(NSMEASUREMENT.value));
        OriginalFile file = new OriginalFileI();
        file.setName(rstring("meas"));
        file.setHash(rstring("meas"));
        file.setPath(rstring("meas"));
        file.setAtime(rtime(0));
        file.setCtime(rtime(0));
        file.setMtime(rtime(0));
        file.setSize(rlong(0));
        file.setMimetype(rstring("ODE.tables"));
        fa.setFile(file);
        fa = assertSaveAndReturn(fa);

        Plate plate = new PlateI();
        plate.setName(rstring("meas"));
        plate.linkAnnotation(fa);
        Well well = new WellI();
        WellSample sample = new WellSampleI();
        sample.setImage(i);
        well.addWellSample(sample);
        plate.addWell(well);

        plate = assertSaveAndReturn(plate);
        RoiAnnotationLink rlink = new RoiAnnotationLinkI();
        ImageAnnotationLink ilink = new ImageAnnotationLinkI();
        rlink.link(roi, fa);
        ilink.link(i, fa);
        assertSaveAndReturn(rlink);
        assertSaveAndReturn(ilink);
        
        List<FileAnnotation> fas = assertGetImageMeasurements(i.getId().getValue());
        assertEquals(fas.toString(), 1, fas.size());
        
        long aid = fas.get(0).getId().getValue();
        Map<Long, RoiResult> rv = assertGetMeasuredRoisMap(i.getId().getValue(),
                Arrays.asList(aid));
        assertEquals(1, rv.size());
        RoiResult rr = rv.get(aid);
        assertNotNull(rr);
        assertEquals(1, rr.rois.size());
        
    }

}