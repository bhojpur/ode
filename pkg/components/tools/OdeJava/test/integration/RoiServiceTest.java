package integration;

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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ode.ApiUsageException;
import ode.ServerError;
import ode.api.IRoiPrx;
import ode.api.RawPixelsStorePrx;
import ode.api.RoiOptions;
import ode.api.RoiResult;
import ode.api.ShapeStats;
import ode.grid.Column;
import ode.grid.LongColumn;
import ode.grid.TablePrx;
import ode.model.AffineTransform;
import ode.model.AffineTransformI;
import ode.model.Annotation;
import ode.model.Ellipse;
import ode.model.EllipseI;
import ode.model.FileAnnotation;
import ode.model.FileAnnotationI;
import ode.model.IObject;
import ode.model.Image;
import ode.model.Line;
import ode.model.LineI;
import ode.model.Mask;
import ode.model.MaskI;
import ode.model.OriginalFile;
import ode.model.Plate;
import ode.model.PlateAnnotationLink;
import ode.model.PlateAnnotationLinkI;
import ode.model.PlateI;
import ode.model.Point;
import ode.model.PointI;
import ode.model.Polygon;
import ode.model.PolygonI;
import ode.model.Polyline;
import ode.model.PolylineI;
import ode.model.Rectangle;
import ode.model.RectangleI;
import ode.model.Roi;
import ode.model.RoiAnnotationLink;
import ode.model.RoiAnnotationLinkI;
import ode.model.RoiI;
import ode.model.Shape;
import ode.model.Well;

import org.testng.Assert;
import org.testng.annotations.Test;

import ode.gateway.model.FileAnnotationData;
import ode.gateway.model.ROIData;
import ode.gateway.model.ShapeData;

/**
 * Collections of tests for the handling ROIs.
 */
public class RoiServiceTest extends AbstractServerTest {

    /**
     * Tests the creation of ROIs with rectangular shapes and removes one shape.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:1679")
    public void testRemoveShape() throws Exception {
        Image image = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());
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
        List<Shape> shapes = roi.copyShapes();
        Shape shape = roi.getShape(0);
        roi.removeShape(shape);
        int n = shapes.size();
        roi = (RoiI) iUpdate.saveAndReturnObject(roi);
        shapes = roi.copyShapes();
        Assert.assertEquals(shapes.size(), (n - 1));
    }

    /**
     * Tests the retrieval of an ROI. This test uses the
     * <code>findByImage</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testFindByImage() throws Exception {
        IRoiPrx svc = factory.getRoiService();
        // create the roi.
        Image image = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());
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
        RoiResult r = svc.findByImage(image.getId().getValue(),
                new RoiOptions());
        Assert.assertNotNull(r);
        List<Roi> rois = r.rois;
        Assert.assertEquals(rois.size(), 1);
        List<Shape> shapes;
        Iterator<Roi> i = rois.iterator();
        while (i.hasNext()) {
            roi = i.next();
            shapes = roi.copyShapes();
            Assert.assertEquals(shapes.size(), 3);
        }
    }

    /**
     * Tests the retrieval of ROI measurements. This test uses the
     * <code>getRoiMeasurements</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testRoisMeasurementRetrieval() throws Exception {
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
        rl.setParent(new RoiI(roi.getId().getValue(), false));
        links.add(rl);
        PlateAnnotationLink il = new PlateAnnotationLinkI();
        il.setChild(new FileAnnotationI(fa.getId().getValue(), false));
        il.setParent(new PlateI(p.getId().getValue(), false));
        links.add(il);
        iUpdate.saveAndReturnArray(links);

        l = svc.getRoiMeasurements(image.getId().getValue(), options);
        Assert.assertEquals(l.size(), 1);
        Assert.assertTrue(l.get(0) instanceof FileAnnotation);
        // Now create another file annotation linked to the ROI

        links.clear();
        of = (OriginalFile) iUpdate.saveAndReturnObject(mmFactory
                .createOriginalFile());
        fa = new FileAnnotationI();
        fa.setFile(of);
        fa = (FileAnnotation) iUpdate.saveAndReturnObject(fa);
        rl = new RoiAnnotationLinkI();
        rl.setChild(new FileAnnotationI(fa.getId().getValue(), false));
        rl.setParent(new RoiI(roi.getId().getValue(), false));
        links.add(rl);
        il = new PlateAnnotationLinkI();
        il.setChild(new FileAnnotationI(fa.getId().getValue(), false));
        il.setParent(new PlateI(p.getId().getValue(), false));
        links.add(il);
        iUpdate.saveAndReturnArray(links);
        // we should still have one
        l = svc.getRoiMeasurements(image.getId().getValue(), options);
        Assert.assertEquals(l.size(), 1);
    }

    /**
     * Tests the retrieval of an ROI with measurement. This test uses the
     * <code>getMeasuredRoisMap</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testMeasuredRoisMap() throws Exception {
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
        // no measurements
        RoiOptions options = new RoiOptions();
        options.userId = ode.rtypes.rlong(iAdmin.getEventContext().userId);

        IRoiPrx svc = factory.getRoiService();
        // create measurements.
        // First create a table
        String uuid;
        TablePrx table;
        Column[] columns = new Column[1];
        columns[0] = new LongColumn("Uid", "", new long[1]);
        OriginalFile of;
        FileAnnotation fa;
        RoiAnnotationLink rl;
        PlateAnnotationLink il;
        // link fa to ROI
        List<IObject> links = new ArrayList<IObject>();
        int n = 1;
        for (int i = 0; i < n; i++) {
            uuid = "Measurement_" + UUID.randomUUID().toString();
            table = factory.sharedResources().newTable(1, uuid);
            table.initialize(columns);
            of = table.getOriginalFile();
            fa = new FileAnnotationI();
            fa.setNs(ode.rtypes.rstring(FileAnnotationData.MEASUREMENT_NS));
            fa.setFile(of);
            fa = (FileAnnotation) iUpdate.saveAndReturnObject(fa);
            rl = new RoiAnnotationLinkI();
            rl.setChild(new FileAnnotationI(fa.getId().getValue(), false));
            rl.setParent(new RoiI(roi.getId().getValue(), false));
            links.add(rl);
            il = new PlateAnnotationLinkI();
            il.setChild(new FileAnnotationI(fa.getId().getValue(), false));
            il.setParent(new PlateI(p.getId().getValue(), false));
            links.add(il);
            iUpdate.saveAndReturnArray(links);
        }

        List<Annotation> l = svc.getRoiMeasurements(image.getId().getValue(),
                options);
        Assert.assertEquals(l.size(), n);
        FileAnnotation f = (FileAnnotation) l.get(0);

        List<Long> ids = new ArrayList<Long>();
        ids.add(f.getId().getValue());
        Map<Long, RoiResult> values = svc.getMeasuredRoisMap(image.getId()
                .getValue(), ids, options);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 1);
        Assert.assertNotNull(values.get(f.getId().getValue()));
    }

    /**
     * Tests the retrieval of a table with ROI measurements. This test uses the
     * <code>getTable</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testTableResult() throws Exception {
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
        // no measurements
        RoiOptions options = new RoiOptions();
        options.userId = ode.rtypes.rlong(iAdmin.getEventContext().userId);

        IRoiPrx svc = factory.getRoiService();
        // create measurements.
        // First create a table
        String uuid = "Measurement_" + UUID.randomUUID().toString();
        TablePrx table = factory.sharedResources().newTable(1, uuid);
        Column[] columns = new Column[1];
        columns[0] = new LongColumn("Uid", "", new long[1]);
        table.initialize(columns);
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
        rl.setParent(new RoiI(roi.getId().getValue(), false));
        links.add(rl);
        PlateAnnotationLink il = new PlateAnnotationLinkI();
        il.setChild(new FileAnnotationI(fa.getId().getValue(), false));
        il.setParent(new PlateI(p.getId().getValue(), false));
        links.add(il);
        iUpdate.saveAndReturnArray(links);
        List<Annotation> l = svc.getRoiMeasurements(image.getId().getValue(),
                options);
        FileAnnotation f = (FileAnnotation) l.get(0);

        table = svc.getTable(f.getId().getValue());
        Assert.assertNotNull(table);
        Column[] cols = table.getHeaders();
        Assert.assertEquals(cols.length, columns.length);
    }

    /**
     * Compare findByImage, findByROI and findByPlane results (similar to
     * test/integration/test_rois.py TestRois )
     */
    @Test
    public void testFindByImageRoiPlane() throws Exception {
        IRoiPrx svc = factory.getRoiService();
        // create the roi.
        Image image = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());

        Roi r1 = createRoi(image, 0, 0);
        Roi r2 = createRoi(image, 0, 1);
        Roi r3 = createRoi(image, 1, 0);
        Roi r4 = createRoi(image, 1, 1);

        RoiResult r = svc.findByImage(image.getId().getValue(),
                new RoiOptions());
        Assert.assertNotNull(r);
        List<Roi> rois = r.rois;
        Assert.assertEquals(rois.size(), 4);
        List<Shape> shapes;
        Roi roi;
        Iterator<Roi> i = rois.iterator();
        while (i.hasNext()) {
            roi = i.next();
            shapes = roi.copyShapes();
            Assert.assertEquals(shapes.size(), 3);
        }

        r = svc.findByRoi(r1.getId().getValue(), new RoiOptions());
        rois = r.rois;
        Assert.assertEquals(rois.size(), 1);
        i = rois.iterator();
        while (i.hasNext()) {
            roi = i.next();
            Assert.assertEquals(roi.getId().getValue(), r1.getId().getValue());
            shapes = roi.copyShapes();
            Assert.assertEquals(shapes.size(),  3);
        }

        r = svc.findByRoi(r2.getId().getValue(), new RoiOptions());
        rois = r.rois;
        Assert.assertEquals(rois.size(),  1);
        i = rois.iterator();
        while (i.hasNext()) {
            roi = i.next();
            Assert.assertEquals(roi.getId().getValue(), r2.getId().getValue());
            shapes = roi.copyShapes();
            Assert.assertEquals(shapes.size(), 3);
        }

        r = svc.findByPlane(image.getId().getValue(), 1, 0, new RoiOptions());
        rois = r.rois;
        Assert.assertEquals(rois.size(), 1);
        i = rois.iterator();
        while (i.hasNext()) {
            roi = i.next();
            Assert.assertEquals(roi.getId().getValue(),  r3.getId().getValue());
            shapes = roi.copyShapes();
            Assert.assertEquals(shapes.size(), 3);
        }

        r = svc.findByPlane(image.getId().getValue(), 1, 1, new RoiOptions());
        rois = r.rois;
        Assert.assertEquals(rois.size(), 1);
        i = rois.iterator();
        while (i.hasNext()) {
            roi = i.next();
            Assert.assertEquals(roi.getId().getValue(), r4.getId().getValue());
            shapes = roi.copyShapes();
            Assert.assertEquals(shapes.size(), 3);
        }
    }

    /**
     * Tests that shape fill and stroke color is stored as RGBA integer.
     *
     * @throws Exception
     */
    @Test
    public void testShapeColor() throws Exception {
        Image image = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());

        Roi roi = createRoi(image, 0, 0);
        ROIData roiData = new ROIData(roi);

        final int a = 10;
        final int r = 20;
        final int g = 30;
        final int b = 40;

        final Color c = new Color(r, g, b, a);
        int rgba = 0;
        rgba |= r << 24;
        rgba |= g << 16;
        rgba |= b << 8;
        rgba |= (a & 255);

        ShapeData shape = roiData.getShapes(0, 0).iterator().next();
        shape.getShapeSettings().setStroke(c);
        shape.getShapeSettings().setFill(c);

        roi = (RoiI) iUpdate.saveAndReturnObject(roi);
        roiData = new ROIData(roi);
        shape = roiData.getShapes(0, 0).iterator().next();
        Shape iShape = (Shape) shape.asIObject();

        Assert.assertEquals(iShape.getStrokeColor().getValue(), rgba);
        Assert.assertEquals(iShape.getFillColor().getValue(), rgba);

        Assert.assertEquals(shape.getShapeSettings().getStroke(), c);
        Assert.assertEquals(shape.getShapeSettings().getFill(), c);
    }

    /**
     * Creates an ROI with 3 rectangluar shapes on the specified plane
     *
     * @param img
     *            The Image the ROI will be linked to
     * @param z
     *            The Z plane
     * @param t
     *            The T plane
     * @return See above.
     * @throws ServerError
     *             If ROI couldn't be created
     */
    private Roi createRoi(Image img, int z, int t) throws ServerError {
        Roi roi = new RoiI();
        roi.setImage(img);
        Rectangle rect;
        roi = (Roi) iUpdate.saveAndReturnObject(roi);
        for (int i = 0; i < 3; i++) {
            rect = new RectangleI();
            rect.setX(ode.rtypes.rdouble(10));
            rect.setY(ode.rtypes.rdouble(10));
            rect.setWidth(ode.rtypes.rdouble(10));
            rect.setHeight(ode.rtypes.rdouble(10));
            rect.setTheZ(ode.rtypes.rint(z));
            rect.setTheT(ode.rtypes.rint(t));
            roi.addShape(rect);
        }
        roi = (RoiI) iUpdate.saveAndReturnObject(roi);
        return roi;
    }

    private Map<Long, ShapeStats> setUpForStatsTest() throws Exception {
        // create image
        Image img = mmFactory.createImage(
            10, 10, 1, 1, 1, ModelMockFactory.UINT8);
        img = (Image) iUpdate.saveAndReturnObject(img);

        // set binary data to more easily check results
        // we create a checkerboard pattern of 5x5 pixels
        // left at 0,0 the quadrants alternate between all 0 or 0xFF
        // 00 FF
        // FF 00
        RawPixelsStorePrx rawPixStore = factory.createRawPixelsStore();
        rawPixStore.setPixelsId(img.getPrimaryPixels().getId().getValue(), true);
        byte [] bytes = new byte[10*10];
        for (int y=0;y<10;y++)
            for (int x=0;x<10;x++) {
                if ((y<5 && x>4) || (y>4 && x<5))
                    bytes[y*10 + x] = Byte.MAX_VALUE;
            }
        rawPixStore.setPlane(bytes, 0, 0, 0);

        // create roi with shapes
        Roi roi = new RoiI();
        roi.setImage(img);

        // a rectangle fully in the FF quandrant
        final Rectangle rect = new RectangleI();
        rect.setX(ode.rtypes.rdouble(5));
        rect.setY(ode.rtypes.rdouble(0));
        rect.setWidth(ode.rtypes.rdouble(4));
        rect.setHeight(ode.rtypes.rdouble(4));
        rect.setTheC(ode.rtypes.rint(0));
        rect.setTheZ(ode.rtypes.rint(0));
        rect.setTheT(ode.rtypes.rint(0));
        roi.addShape(rect);

        // a line rectangle crossing 00/FF horizontally
        final Line line = new LineI();
        line.setX1(ode.rtypes.rdouble(0));
        line.setY1(ode.rtypes.rdouble(1));
        line.setX2(ode.rtypes.rdouble(9));
        line.setY2(ode.rtypes.rdouble(1));
        line.setTheC(ode.rtypes.rint(0));
        line.setTheZ(ode.rtypes.rint(0));
        line.setTheT(ode.rtypes.rint(0));
        roi.addShape(line);

        // an ellipse across the upper 2 quadrants
        final Ellipse ellipse = new EllipseI();
        ellipse.setX(ode.rtypes.rdouble(4.5));
        ellipse.setY(ode.rtypes.rdouble(2.5));
        ellipse.setRadiusX(ode.rtypes.rdouble(5));
        ellipse.setRadiusY(ode.rtypes.rdouble(2.5));
        ellipse.setTheC(ode.rtypes.rint(0));
        ellipse.setTheZ(ode.rtypes.rint(0));
        ellipse.setTheT(ode.rtypes.rint(0));
        roi.addShape(ellipse);

        // a point at the origin translated
        // into the FF quandrant
        final Point point = new PointI();
        point.setX(ode.rtypes.rdouble(0.5));
        point.setY(ode.rtypes.rdouble(0.5));
        point.setTheC(ode.rtypes.rint(0));
        point.setTheZ(ode.rtypes.rint(0));
        point.setTheT(ode.rtypes.rint(0));
        final AffineTransform t =  new AffineTransformI();
        t.setA00(ode.rtypes.rdouble(1));
        t.setA01(ode.rtypes.rdouble(0));
        t.setA02(ode.rtypes.rdouble(2.5));
        t.setA10(ode.rtypes.rdouble(0));
        t.setA11(ode.rtypes.rdouble(1));
        t.setA12(ode.rtypes.rdouble(7.5));
        point.setTransform(t);
        roi.addShape(point);

        // a polyline (forming triangle)
        final Polyline polyline = new PolylineI();
        polyline.setPoints(
            ode.rtypes.rstring("0,0 9,9 0,9 0,0"));
        polyline.setTheC(ode.rtypes.rint(0));
        polyline.setTheZ(ode.rtypes.rint(0));
        polyline.setTheT(ode.rtypes.rint(0));
        roi.addShape(polyline);

        // a polygon the shape of a rectangle
        // transformed onto the entire image
        final Polygon polygon = new PolygonI();
        polygon.setPoints(
            ode.rtypes.rstring("-2.5,2.5 0,2.5 0,0 -2.5,0 -2.5,2.5"));
        polygon.setTheC(ode.rtypes.rint(0));
        polygon.setTheZ(ode.rtypes.rint(0));
        polygon.setTheT(ode.rtypes.rint(0));
        final AffineTransform t2 =  new AffineTransformI();
        t2.setA00(ode.rtypes.rdouble(0));
        t2.setA01(ode.rtypes.rdouble(4));
        t2.setA02(ode.rtypes.rdouble(0));
        t2.setA10(ode.rtypes.rdouble(-4));
        t2.setA11(ode.rtypes.rdouble(0));
        t2.setA12(ode.rtypes.rdouble(0));
        polygon.setTransform(t2);
        roi.addShape(polygon);

        // a mask fully in the FF quandrant
        final Mask mask = new MaskI();
        mask.setX(ode.rtypes.rdouble(5));
        mask.setY(ode.rtypes.rdouble(0));
        mask.setWidth(ode.rtypes.rdouble(4));
        mask.setHeight(ode.rtypes.rdouble(4));
        mask.setTheC(ode.rtypes.rint(0));
        mask.setTheZ(ode.rtypes.rint(0));
        mask.setTheT(ode.rtypes.rint(0));
        roi.addShape(mask);
        roi = (RoiI) iUpdate.saveAndReturnObject(roi);

        // add all shape ids to the list to be queried
        // and their expected stats
        final Map<Long, ShapeStats> shapeList =
            new HashMap<Long, ShapeStats>();
        final ShapeStats [] expShapeStats = new ShapeStats[] {
            new ShapeStats( // rectangle in FF
                0, new long[] {0}, new long[] {16},
                new double[] {Byte.MAX_VALUE},
                new double[] {Byte.MAX_VALUE},
                new double[] {16*Byte.MAX_VALUE},
                new double[] {(double)Byte.MAX_VALUE},
                new double[1]),
            new ShapeStats( // horizontal line across
                0, new long[] {0}, new long[] {10},
                new double[] {0},
                new double[] {Byte.MAX_VALUE},
                new double[] {5*Byte.MAX_VALUE},
                new double[] {((double)5*Byte.MAX_VALUE)/10},
                new double[1]),
            new ShapeStats( // ellipse center upper half
                0, new long[] {0}, new long[] {36},
                new double[] {0},
                new double[] {Byte.MAX_VALUE},
                new double[] {15*Byte.MAX_VALUE},
                new double[] {((double)15*Byte.MAX_VALUE)/36},
                new double[1]),
            new ShapeStats( // point translated FF
                0, new long[] {0}, new long[] {1},
                new double[] {Byte.MAX_VALUE},
                new double[] {Byte.MAX_VALUE},
                new double[] {Byte.MAX_VALUE},
                new double[] {(double)Byte.MAX_VALUE},
                new double[1]),
            new ShapeStats( // a polyline (forming a triangle)
                0, new long[] {0}, new long[] {3*10},
                new double[] {0},
                new double[] {Byte.MAX_VALUE},
                new double[] {10*Byte.MAX_VALUE},
                new double[] {((double)10*Byte.MAX_VALUE)/30},
                new double[1]),
            new ShapeStats( // a polygon transformed onto image)
                0, new long[] {0}, new long[] {10*10},
                new double[] {0},
                new double[] {Byte.MAX_VALUE},
                new double[] {50*Byte.MAX_VALUE},
                new double[] {((double) 50*Byte.MAX_VALUE)/100},
                new double[1]),
            new ShapeStats( // mask in FF
                    0, new long[] {0}, new long[] {16},
                    new double[] {Byte.MAX_VALUE},
                    new double[] {Byte.MAX_VALUE},
                    new double[] {16*Byte.MAX_VALUE},
                    new double[] {(double)Byte.MAX_VALUE},
                    new double[1])
        };
        for (int i=0; i<roi.sizeOfShapes();i++) {
            shapeList.put(
                roi.getShape(i).getId().getValue(),
                expShapeStats[i]);
        }

        return shapeList;
    }

    @Test
    public void testStatsBasicUsageAndInputChecks() throws Exception {
        IRoiPrx svc = factory.getRoiService();

        final Map<Long,ShapeStats> testAndAssertData = setUpForStatsTest();
        final List<Long> ids = new ArrayList<Long>(testAndAssertData.keySet());

        // some basic inputs checks
        final List<Object[]> inputs = new ArrayList<Object[]>();
        inputs.add(new Object[] {
            null, new Integer(0), new Integer(0), new int[] {0}, "null ids"});
        inputs.add(new Object[] {
            new ArrayList<Long>(), new Integer(0), new Integer(0), new int[] {0},
            "empty ids" });
        inputs.add(new Object[] {
            Arrays.asList(new Long[] {new Long (-1)}), new Integer(0), new Integer(0),
            new int[] {0}, "erroneous id" });
        inputs.add(new Object[] { ids, new Integer(10), new Integer(10), new int[] {0},
            "wrong fallback z" });
        inputs.add(new Object[] { ids, new Integer(0), new Integer(-10), new int[] {0},
            "wrong fallback t" });
        inputs.add(new Object[] { ids, new Integer(0), new Integer(0), new int[] {200},
            "wrong channels" });
        inputs.add(new Object[] { ids, new Integer(0), new Integer(0), null,
            "missing channels" });
        inputs.add(new Object[] { ids, new Integer(0), new Integer(0), new int[] {},
            "empty channels" });

        // all these inputs should trigger an ApiUsageException
        for (Object [] in : inputs) {
            boolean succeeded = false;
            try {
                svc.getShapeStatsRestricted(
                    (List<Long>) in[0], (Integer) in[1], (Integer)in[2], (int []) in[3]);
            } catch(ApiUsageException any) {
                succeeded = true;
            } catch (Exception anythingElse) {}
            assertTrue(succeeded, "testStatsBasicUsageAndInputChecks: " + in[4]);
        }

        // call again with valid params and assert results
        final ShapeStats [] stats = svc.getShapeStatsRestricted(ids, 0, 0, new int[] {0});
        for (final ShapeStats calcStats : stats) {
            final ShapeStats expStats = testAndAssertData.get(calcStats.shapeId);
            assertEquals(calcStats.pointsCount[0], expStats.pointsCount[0]);
            assertEquals(calcStats.min[0], expStats.min[0]);
            assertEquals(calcStats.max[0], expStats.max[0]);
            assertEquals(calcStats.sum[0], expStats.sum[0]);
            assertEquals(calcStats.mean[0], expStats.mean[0]);
        }
    }
}
