package integration.delete;

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

import integration.AbstractServerTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ode.api.IRoiPrx;
import ode.api.RoiOptions;
import ode.cmd.Delete2;
import ode.gateway.util.Requests;
import ode.grid.Column;
import ode.grid.LongColumn;
import ode.grid.TablePrx;
import ode.model.Annotation;
import ode.model.FileAnnotation;
import ode.model.FileAnnotationI;
import ode.model.IObject;
import ode.model.Image;
import ode.model.OriginalFile;
import ode.model.Plate;
import ode.model.PlateAnnotationLink;
import ode.model.PlateAnnotationLinkI;
import ode.model.PlateI;
import ode.model.Rectangle;
import ode.model.RectangleI;
import ode.model.Roi;
import ode.model.RoiAnnotationLink;
import ode.model.RoiAnnotationLinkI;
import ode.model.RoiI;
import ode.model.Well;
import ode.sys.EventContext;

import org.testng.annotations.Test;
import org.testng.Assert;
import ode.gateway.model.FileAnnotationData;

/**
 * Tests for deleting rois and images which have rois.
 */
@Test(groups = "ticket:2615")
public class RoiDeleteTest extends AbstractServerTest {

    /**
     * Test to delete an image with ROIs owned by another user.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = { "ticket:2962", "ticket:3010" })
    public void testDeleteWithAnotherUsersRoi() throws Exception {

        EventContext owner = newUserAndGroup("rwrw--");
        Image i1 = (Image) iUpdate.saveAndReturnObject(mmFactory.createImage());
        disconnect();

        newUserInGroup(owner);
        Roi roi = new RoiI();
        roi.setImage((Image) i1.proxy());
        roi = (Roi) iUpdate.saveAndReturnObject(roi);
        disconnect();

        loginUser(owner);
        final Delete2 dc = Requests.delete().target(i1).build();
        callback(true, client, dc);

        assertDoesNotExist(i1);
        assertDoesNotExist(roi);
    }

    /**
     * Test to delete ROI with measurement.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testDeleteROIAndResults() throws Exception {
        Plate p = mmFactory.createPlate(1, 1, 1, 0, true);
        p = (Plate) iUpdate.saveAndReturnObject(p);
        List<Well> results = loadWells(p.getId().getValue(), true);
        Well well = results.get(0);
        // create the roi.
        Image image = well.getWellSample(0).getImage();
        Roi roi = new RoiI();
        roi.setImage(image);
        Rectangle rect;
        roi = (Roi) iUpdate.saveAndReturnObject(roi);
        for (int i = 0; i < 3; i++) {
            rect = new RectangleI();
            rect.setX(ode.rtypes.rdouble(10));
            rect.setY(ode.rtypes.rdouble(10));
            rect.setWidth(ode.rtypes.rdouble(10));
            rect.setHeight(ode.rtypes.rdouble(10));
            rect.setTheZ(ode.rtypes.rint(i));
            rect.setTheT(ode.rtypes.rint(0));
            roi.addShape(rect);
        }
        roi = (RoiI) iUpdate.saveAndReturnObject(roi);
        long roiID = roi.getId().getValue();
        // no measurements
        RoiOptions options = new RoiOptions();
        options.userId = ode.rtypes.rlong(iAdmin.getEventContext().userId);

        IRoiPrx svc = factory.getRoiService();
        List<Annotation> l = svc.getRoiMeasurements(image.getId().getValue(),
                options);
        Assert.assertEquals(l.size(), 0);

        // create measurements.
        // First create a table
        String uuid = "Measurement_" + UUID.randomUUID().toString();
        TablePrx table = factory.sharedResources().newTable(1, uuid);
        Column[] columns = new Column[1];
        columns[0] = new LongColumn("Uid", "", new long[1]);
        table.initialize(columns);
        Assert.assertNotNull(table);
        OriginalFile of = table.getOriginalFile();
        Assert.assertTrue(of.getId().getValue() > 0);
        FileAnnotation fa = new FileAnnotationI();
        fa.setNs(ode.rtypes.rstring(FileAnnotationData.MEASUREMENT_NS));
        fa.setFile(of);
        fa = (FileAnnotation) iUpdate.saveAndReturnObject(fa);
        // link fa to ROI
        List<IObject> links = new ArrayList<IObject>();
        RoiAnnotationLink rl = new RoiAnnotationLinkI();
        rl.setChild(new FileAnnotationI(fa.getId().getValue(), false));
        rl.setParent(new RoiI(roiID, false));
        links.add(rl);
        PlateAnnotationLink il = new PlateAnnotationLinkI();
        il.setChild(new FileAnnotationI(fa.getId().getValue(), false));
        il.setParent(new PlateI(p.getId().getValue(), false));
        links.add(il);
        iUpdate.saveAndReturnArray(links);

        // Now delete the rois.
        final Delete2 dc = Requests.delete().target(roi).build();
        callback(true, client, dc);
        assertDoesNotExist(roi);
        l = svc.getRoiMeasurements(image.getId().getValue(), options);
        Assert.assertEquals(l.size(), 0);
    }

    /**
     * Test to delete ROI with measurement.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testDeletePlateWithROIAndResults() throws Exception {
        Plate p = mmFactory.createPlate(1, 1, 1, 0, true);
        p = (Plate) iUpdate.saveAndReturnObject(p);
        List<Well> results = loadWells(p.getId().getValue(), true);
        Well well = results.get(0);
        // create the roi.
        Image image = well.getWellSample(0).getImage();
        Roi roi = new RoiI();
        roi.setImage(image);
        Rectangle rect;
        roi = (Roi) iUpdate.saveAndReturnObject(roi);
        for (int i = 0; i < 3; i++) {
            rect = new RectangleI();
            rect.setX(ode.rtypes.rdouble(10));
            rect.setY(ode.rtypes.rdouble(10));
            rect.setWidth(ode.rtypes.rdouble(10));
            rect.setHeight(ode.rtypes.rdouble(10));
            rect.setTheZ(ode.rtypes.rint(i));
            rect.setTheT(ode.rtypes.rint(0));
            roi.addShape(rect);
        }
        roi = (RoiI) iUpdate.saveAndReturnObject(roi);
        long roiID = roi.getId().getValue();
        // no measurements
        RoiOptions options = new RoiOptions();
        options.userId = ode.rtypes.rlong(iAdmin.getEventContext().userId);

        IRoiPrx svc = factory.getRoiService();
        List<Annotation> l = svc.getRoiMeasurements(image.getId().getValue(),
                options);
        Assert.assertEquals(l.size(), 0);

        // create measurements.
        // First create a table
        String uuid = "Measurement_" + UUID.randomUUID().toString();
        TablePrx table = factory.sharedResources().newTable(1, uuid);
        Column[] columns = new Column[1];
        columns[0] = new LongColumn("Uid", "", new long[1]);
        table.initialize(columns);
        Assert.assertNotNull(table);
        OriginalFile of = table.getOriginalFile();
        Assert.assertTrue(of.getId().getValue() > 0);
        FileAnnotation fa = new FileAnnotationI();
        fa.setNs(ode.rtypes.rstring(FileAnnotationData.MEASUREMENT_NS));
        fa.setFile(of);
        fa = (FileAnnotation) iUpdate.saveAndReturnObject(fa);
        // link fa to ROI
        List<IObject> links = new ArrayList<IObject>();
        RoiAnnotationLink rl = new RoiAnnotationLinkI();
        rl.setChild(new FileAnnotationI(fa.getId().getValue(), false));
        rl.setParent(new RoiI(roiID, false));
        links.add(rl);
        PlateAnnotationLink il = new PlateAnnotationLinkI();
        il.setChild(new FileAnnotationI(fa.getId().getValue(), false));
        il.setParent(new PlateI(p.getId().getValue(), false));
        links.add(il);
        iUpdate.saveAndReturnArray(links);

        // Now delete the plate
        final Delete2 dc = Requests.delete().target(p).build();
        callback(true, client, dc);
        assertDoesNotExist(p);
        assertDoesNotExist(roi);
        l = svc.getRoiMeasurements(image.getId().getValue(), options);
        Assert.assertEquals(l.size(), 0);
    }

}
