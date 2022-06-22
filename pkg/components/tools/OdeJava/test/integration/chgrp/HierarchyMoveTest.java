package integration.chgrp;

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
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import ode.cmd.Chgrp2;
import ode.cmd.Chgrp2Response;
import ode.gateway.util.Requests;
import ode.gateway.util.Requests.Chgrp2Builder;
import ode.grid.Column;
import ode.grid.LongColumn;
import ode.grid.TablePrx;
import ode.model.AffineTransform;
import ode.model.AffineTransformI;
import ode.model.Arc;
import ode.model.Channel;
import ode.model.Dataset;
import ode.model.DatasetI;
import ode.model.DatasetImageLink;
import ode.model.DatasetImageLinkI;
import ode.model.ExperimenterGroup;
import ode.model.FileAnnotation;
import ode.model.FileAnnotationI;
import ode.model.IObject;
import ode.model.Image;
import ode.model.Instrument;
import ode.model.LightSettings;
import ode.model.LightSource;
import ode.model.LogicalChannel;
import ode.model.OriginalFile;
import ode.model.Pixels;
import ode.model.Plate;
import ode.model.PlateAcquisition;
import ode.model.PlateAnnotationLink;
import ode.model.PlateAnnotationLinkI;
import ode.model.PlateI;
import ode.model.Point;
import ode.model.PointI;
import ode.model.Project;
import ode.model.ProjectDatasetLink;
import ode.model.ProjectDatasetLinkI;
import ode.model.Reagent;
import ode.model.Rectangle;
import ode.model.RectangleI;
import ode.model.Roi;
import ode.model.RoiAnnotationLink;
import ode.model.RoiAnnotationLinkI;
import ode.model.RoiI;
import ode.model.Screen;
import ode.model.ScreenPlateLink;
import ode.model.ScreenPlateLinkI;
import ode.model.Shape;
import ode.model.StatsInfo;
import ode.model.Well;
import ode.model.WellSample;
import ode.sys.EventContext;
import ode.sys.ParametersI;

import org.apache.commons.collections.CollectionUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;

import ode.gateway.model.FileAnnotationData;

/**
 * Collection of test to move hierarchies between groups.
 */
public class HierarchyMoveTest extends AbstractServerTest {

    /**
     * Test to move an image w/o pixels between 2 private groups.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testMoveBasicImage() throws Exception {
        String perms = "rw----";
        EventContext ctx = newUserAndGroup(perms);
        ExperimenterGroup g = newGroupAddUser(perms, ctx.userId);
        iAdmin.getEventContext(); // Refresh
        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());
        long id = img.getId().getValue();
        final Chgrp2 dc = Requests.chgrp().target(img).toGroup(g).build();
        callback(true, client, dc);
        // Now check that the image is no longer in group
        ParametersI param = new ParametersI();
        param.addId(id);

        Assert.assertNotEquals(g.getId().getValue(), ctx.groupId);
        StringBuilder sb = new StringBuilder();
        sb.append("select i from Image i ");
        sb.append("where i.id = :id");
        Assert.assertNull(iQuery.findByQuery(sb.toString(), param));

        EventContext ec = loginUser(g);
        Assert.assertEquals(g.getId().getValue(), ec.groupId);
        Assert.assertNotNull(iQuery.findByQuery(sb.toString(), param));
    }

    /**
     * Test to move an image w/o pixels between 2 private groups.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testMoveImage() throws Exception {
        String perms = "rw----";
        EventContext ctx = newUserAndGroup(perms);
        ExperimenterGroup g = newGroupAddUser(perms, ctx.userId);
        iAdmin.getEventContext(); // Refresh
        Image img = mmFactory.createImage();
        img = (Image) iUpdate.saveAndReturnObject(img);
        Pixels pixels = img.getPrimaryPixels();
        long pixId = pixels.getId().getValue();
        // method already tested in PixelsServiceTest
        // make sure objects are loaded.
        pixels = factory.getPixelsService().retrievePixDescription(pixId);
        // channels.
        long id = img.getId().getValue();

        List<Long> channels = new ArrayList<Long>();
        List<Long> logicalChannels = new ArrayList<Long>();
        List<Long> infos = new ArrayList<Long>();
        Channel channel;
        LogicalChannel lc;
        StatsInfo info;
        for (int i = 0; i < pixels.getSizeC().getValue(); i++) {
            channel = pixels.getChannel(i);
            Assert.assertNotNull(channel);
            channels.add(channel.getId().getValue());
            lc = channel.getLogicalChannel();
            Assert.assertNotNull(lc);
            logicalChannels.add(lc.getId().getValue());
            info = channel.getStatsInfo();
            Assert.assertNotNull(info);
            infos.add(info.getId().getValue());
        }

        // Move the image
        final Chgrp2 dc = Requests.chgrp().target(img).toGroup(g).build();
        callback(true, client, dc);
        ParametersI param = new ParametersI();
        param.addId(id);

        StringBuilder sb = new StringBuilder();
        sb.append("select i from Image i ");
        sb.append("where i.id = :id");
        Assert.assertNull(iQuery.findByQuery(sb.toString(), param));
        sb = new StringBuilder();
        param = new ParametersI();
        param.addId(pixId);
        sb.append("select i from Pixels i ");
        sb.append("where i.id = :id");
        Assert.assertNull(iQuery.findByQuery(sb.toString(), param));
        Iterator<Long> i = channels.iterator();
        while (i.hasNext()) {
            id = i.next();
            param = new ParametersI();
            param.addId(id);
            sb = new StringBuilder();
            sb.append("select i from Channel i ");
            sb.append("where i.id = :id");
            Assert.assertNull(iQuery.findByQuery(sb.toString(), param));
        }
        i = infos.iterator();
        while (i.hasNext()) {
            id = i.next();
            param = new ParametersI();
            param.addId(id);
            sb = new StringBuilder();
            sb.append("select i from StatsInfo i ");
            sb.append("where i.id = :id");
            Assert.assertNull(iQuery.findByQuery(sb.toString(), param));
        }
        i = logicalChannels.iterator();
        while (i.hasNext()) {
            id = i.next();
            param = new ParametersI();
            param.addId(id);
            sb = new StringBuilder();
            sb.append("select i from LogicalChannel i ");
            sb.append("where i.id = :id");
            Assert.assertNull(iQuery.findByQuery(sb.toString(), param));
        }
        // Check that the data moved
        loginUser(g);

        id = img.getId().getValue();
        param = new ParametersI();
        param.addId(id);

        sb = new StringBuilder();
        sb.append("select i from Image i ");
        sb.append("where i.id = :id");
        Assert.assertNotNull(iQuery.findByQuery(sb.toString(), param));
        sb = new StringBuilder();
        param = new ParametersI();
        param.addId(pixId);
        sb.append("select i from Pixels i ");
        sb.append("where i.id = :id");
        Assert.assertNotNull(iQuery.findByQuery(sb.toString(), param));
        i = channels.iterator();
        while (i.hasNext()) {
            id = i.next();
            param = new ParametersI();
            param.addId(id);
            sb = new StringBuilder();
            sb.append("select i from Channel i ");
            sb.append("where i.id = :id");
            Assert.assertNotNull(iQuery.findByQuery(sb.toString(), param));
        }
        i = infos.iterator();
        while (i.hasNext()) {
            id = i.next();
            param = new ParametersI();
            param.addId(id);
            sb = new StringBuilder();
            sb.append("select i from StatsInfo i ");
            sb.append("where i.id = :id");
            Assert.assertNotNull(iQuery.findByQuery(sb.toString(), param));
        }
        i = logicalChannels.iterator();
        while (i.hasNext()) {
            id = i.next();
            param = new ParametersI();
            param.addId(id);
            sb = new StringBuilder();
            sb.append("select i from LogicalChannel i ");
            sb.append("where i.id = :id");
            Assert.assertNotNull(iQuery.findByQuery(sb.toString(), param));
        }
    }

    /**
     * Test to move an image with ROis.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testMoveImageWithROIs() throws Exception {
        String perms = "rw----";
        EventContext ctx = newUserAndGroup(perms);
        ExperimenterGroup g = newGroupAddUser(perms, ctx.userId);
        iAdmin.getEventContext(); // Refresh

        Image image = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());
        Roi roi = new RoiI();
        roi.setImage(image);
        Rectangle rect;
        Roi serverROI = (Roi) iUpdate.saveAndReturnObject(roi);
        for (int i = 0; i < 3; i++) {
            rect = new RectangleI();
            rect.setX(ode.rtypes.rdouble(10));
            rect.setY(ode.rtypes.rdouble(10));
            rect.setWidth(ode.rtypes.rdouble(10));
            rect.setHeight(ode.rtypes.rdouble(10));
            rect.setTheZ(ode.rtypes.rint(i));
            rect.setTheT(ode.rtypes.rint(0));
            serverROI.addShape(rect);
        }
        serverROI = (RoiI) iUpdate.saveAndReturnObject(serverROI);
        List<Long> shapeIds = new ArrayList<Long>();
        Shape shape;
        for (int i = 0; i < serverROI.sizeOfShapes(); i++) {
            shape = serverROI.getShape(i);
            shapeIds.add(shape.getId().getValue());
        }
        // Move the image.
        final Chgrp2 dc = Requests.chgrp().target(image).toGroup(g).build();
        callback(true, client, dc);

        // check if the objects have been delete.

        ParametersI param = new ParametersI();
        param.addId(serverROI.getId().getValue());
        String sql = "select d from Roi as d where d.id = :id";
        Assert.assertNull(iQuery.findByQuery(sql, param));

        // shapes
        param = new ParametersI();
        param.addIds(shapeIds);
        sql = "select d from Shape as d where d.id in (:ids)";
        List<IObject> results = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(results.size(), 0);

        // Check that the data moved
        loginUser(g);
        param = new ParametersI();
        param.addId(serverROI.getId().getValue());
        sql = "select d from Roi as d where d.id = :id";
        Assert.assertNotNull(iQuery.findByQuery(sql, param));

        // shapes
        param = new ParametersI();
        param.addIds(shapeIds);
        sql = "select d from Shape as d where d.id in (:ids)";
        results = iQuery.findAllByQuery(sql, param);
        Assert.assertTrue(CollectionUtils.isNotEmpty(results));
    }

    /**
     * Test to move a populated plate. Plate with plate acquisition.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testMovePlate() throws Exception {
        String perms = "rw----";
        EventContext ctx = newUserAndGroup(perms);
        ExperimenterGroup g = newGroupAddUser(perms, ctx.userId);
        iAdmin.getEventContext(); // Refresh

        Plate p;
        List<IObject> results;
        PlateAcquisition pa = null;
        StringBuilder sb;
        Well well;
        WellSample field;
        Iterator<IObject> j;
        ParametersI param;
        List<Long> wellSampleIds;
        List<Long> imageIds;
        p = (Plate) iUpdate.saveAndReturnObject(mmFactory.createPlate(1, 1, 1,
                1, false));
        param = new ParametersI();
        param.addLong("plateID", p.getId().getValue());
        sb = new StringBuilder();
        sb.append("select well from Well as well ");
        sb.append("left outer join fetch well.plate as pt ");
        sb.append("left outer join fetch well.wellSamples as ws ");
        sb.append("left outer join fetch ws.image as img ");
        sb.append("where pt.id = :plateID");
        results = iQuery.findAllByQuery(sb.toString(), param);

        sb = new StringBuilder();
        sb.append("select pa from PlateAcquisition as pa "
                + "where pa.plate.id = :plateID");
        pa = (PlateAcquisition) iQuery.findByQuery(sb.toString(), param);

        j = results.iterator();
        wellSampleIds = new ArrayList<Long>();
        imageIds = new ArrayList<Long>();
        while (j.hasNext()) {
            well = (Well) j.next();
            for (int k = 0; k < well.sizeOfWellSamples(); k++) {
                field = well.getWellSample(k);
                wellSampleIds.add(field.getId().getValue());
                Assert.assertNotNull(field.getImage());
                imageIds.add(field.getImage().getId().getValue());
            }
        }
        // Move the plate.
        final Chgrp2 dc = Requests.chgrp().target(p).toGroup(g).build();
        callback(true, client, dc);

        // check the well
        param = new ParametersI();
        param.addLong("plateID", p.getId().getValue());
        sb = new StringBuilder();
        sb.append("select well from Well as well ");
        sb.append("left outer join fetch well.plate as pt ");
        sb.append("where pt.id = :plateID");
        results = iQuery.findAllByQuery(sb.toString(), param);
        Assert.assertEquals(results.size(), 0);

        // check the well samples.
        sb = new StringBuilder();
        param = new ParametersI();
        param.addIds(wellSampleIds);
        sb.append("select p from WellSample as p where p.id in (:ids)");
        results = iQuery.findAllByQuery(sb.toString(), param);
        Assert.assertEquals(results.size(), 0);

        // check the image.
        sb = new StringBuilder();
        param = new ParametersI();
        param.addIds(imageIds);
        sb.append("select p from Image as p where p.id in (:ids)");
        results = iQuery.findAllByQuery(sb.toString(), param);
        Assert.assertEquals(results.size(), 0);
        if (pa != null) {
            param = new ParametersI();
            param.addId(pa.getId().getValue());
            sb = new StringBuilder();
            // check the plate
            sb.append("select p from PlateAcquisition as p "
                    + "where p.id = :id");
            Assert.assertNull(iQuery.findByQuery(sb.toString(), param));
        }
        loginUser(g);
        // check the well
        param = new ParametersI();
        param.addLong("plateID", p.getId().getValue());
        sb = new StringBuilder();
        sb.append("select well from Well as well ");
        sb.append("left outer join fetch well.plate as pt ");
        sb.append("where pt.id = :plateID");
        results = iQuery.findAllByQuery(sb.toString(), param);
        Assert.assertTrue(CollectionUtils.isNotEmpty(results));

        // check the well samples.
        sb = new StringBuilder();
        param = new ParametersI();
        param.addIds(wellSampleIds);
        sb.append("select p from WellSample as p where p.id in (:ids)");
        results = iQuery.findAllByQuery(sb.toString(), param);
        Assert.assertTrue(CollectionUtils.isNotEmpty(results));

        // check the image.
        sb = new StringBuilder();
        param = new ParametersI();
        param.addIds(imageIds);
        sb.append("select p from Image as p where p.id in (:ids)");
        results = iQuery.findAllByQuery(sb.toString(), param);
        Assert.assertTrue(CollectionUtils.isNotEmpty(results));
        if (pa != null) {
            param = new ParametersI();
            param.addId(pa.getId().getValue());
            sb = new StringBuilder();
            // check the plate
            sb.append("select p from PlateAcquisition as p "
                    + "where p.id = :id");
            Assert.assertNotNull(iQuery.findByQuery(sb.toString(), param));
        }
    }

    /**
     * Test to move a populated plate. Plate with no plate acquisition.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testMovePlateNoPlateAcquisition() throws Exception {
        String perms = "rw----";
        EventContext ctx = newUserAndGroup(perms);
        ExperimenterGroup g = newGroupAddUser(perms, ctx.userId);
        iAdmin.getEventContext(); // Refresh

        Plate p;
        List<IObject> results;
        StringBuilder sb;
        Well well;
        WellSample field;
        Iterator<IObject> j;
        ParametersI param;
        List<Long> wellSampleIds;
        List<Long> imageIds;
        p = (Plate) iUpdate.saveAndReturnObject(mmFactory.createPlate(1, 1, 1,
                0, false));
        param = new ParametersI();
        param.addLong("plateID", p.getId().getValue());
        sb = new StringBuilder();
        sb.append("select well from Well as well ");
        sb.append("left outer join fetch well.plate as pt ");
        sb.append("left outer join fetch well.wellSamples as ws ");
        sb.append("left outer join fetch ws.image as img ");
        sb.append("where pt.id = :plateID");
        results = iQuery.findAllByQuery(sb.toString(), param);

        j = results.iterator();
        wellSampleIds = new ArrayList<Long>();
        imageIds = new ArrayList<Long>();
        while (j.hasNext()) {
            well = (Well) j.next();
            for (int k = 0; k < well.sizeOfWellSamples(); k++) {
                field = well.getWellSample(k);
                wellSampleIds.add(field.getId().getValue());
                Assert.assertNotNull(field.getImage());
                imageIds.add(field.getImage().getId().getValue());
            }
        }

        // Move the plate.
        final Chgrp2 dc = Requests.chgrp().target(p).toGroup(g).build();
        callback(true, client, dc);

        // check the well
        param = new ParametersI();
        param.addLong("plateID", p.getId().getValue());
        sb = new StringBuilder();
        sb.append("select well from Well as well ");
        sb.append("left outer join fetch well.plate as pt ");
        sb.append("where pt.id = :plateID");
        results = iQuery.findAllByQuery(sb.toString(), param);
        Assert.assertEquals(results.size(), 0);

        // check the well samples.
        sb = new StringBuilder();
        param = new ParametersI();
        param.addIds(wellSampleIds);
        sb.append("select p from WellSample as p where p.id in (:ids)");
        results = iQuery.findAllByQuery(sb.toString(), param);
        Assert.assertEquals(results.size(), 0);

        // check the image.
        sb = new StringBuilder();
        param = new ParametersI();
        param.addIds(imageIds);
        sb.append("select p from Image as p where p.id in (:ids)");
        results = iQuery.findAllByQuery(sb.toString(), param);
        Assert.assertEquals(results.size(), 0);

        loginUser(g);
        // check the well
        param = new ParametersI();
        param.addLong("plateID", p.getId().getValue());
        sb = new StringBuilder();
        sb.append("select well from Well as well ");
        sb.append("left outer join fetch well.plate as pt ");
        sb.append("where pt.id = :plateID");
        results = iQuery.findAllByQuery(sb.toString(), param);
        Assert.assertTrue(CollectionUtils.isNotEmpty(results));

        // check the well samples.
        sb = new StringBuilder();
        param = new ParametersI();
        param.addIds(wellSampleIds);
        sb.append("select p from WellSample as p where p.id in (:ids)");
        results = iQuery.findAllByQuery(sb.toString(), param);
        Assert.assertTrue(CollectionUtils.isNotEmpty(results));

        // check the image.
        sb = new StringBuilder();
        param = new ParametersI();
        param.addIds(imageIds);
        sb.append("select p from Image as p where p.id in (:ids)");
        results = iQuery.findAllByQuery(sb.toString(), param);
        Assert.assertTrue(CollectionUtils.isNotEmpty(results));
    }

    /**
     * Tests to move a screen containing 2 plates, one w/o plate acquisition and
     * one with plate acquisition.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testMoveScreen() throws Exception {
        String perms = "rw----";
        EventContext ctx = newUserAndGroup(perms);
        ExperimenterGroup g = newGroupAddUser(perms, ctx.userId);
        iAdmin.getEventContext(); // Refresh

        Screen screen = (Screen) iUpdate.saveAndReturnObject(mmFactory
                .simpleScreenData().asIObject());
        // Plate w/o plate acquisition
        Plate p1 = (Plate) iUpdate.saveAndReturnObject(mmFactory.createPlate(1,
                1, 1, 0, false));
        // Plate with plate acquisition
        Plate p2 = (Plate) iUpdate.saveAndReturnObject(mmFactory.createPlate(1,
                1, 1, 1, false));
        List<IObject> links = new ArrayList<IObject>();
        ScreenPlateLink link = new ScreenPlateLinkI();
        link.setChild(p1);
        link.setParent(screen);
        links.add(link);
        link = new ScreenPlateLinkI();
        link.setChild(p2);
        link.setParent(screen);
        links.add(link);
        iUpdate.saveAndReturnArray(links);

        final Chgrp2 dc = Requests.chgrp().target(screen).toGroup(g).build();
        callback(true, client, dc);

        List<Long> ids = new ArrayList<Long>();
        ids.add(p1.getId().getValue());
        ids.add(p2.getId().getValue());

        // Check if the plates exist.
        ParametersI param = new ParametersI();
        param.addIds(ids);
        String sql = "select i from Plate as i where i.id in (:ids)";
        List<IObject> results = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(results.size(), 0);

        param = new ParametersI();
        param.addId(screen.getId().getValue());
        sql = "select i from Screen as i where i.id = :id";
        Assert.assertNull(iQuery.findByQuery(sql, param));

        // Check that the data moved
        loginUser(g);
        param = new ParametersI();
        param.addIds(ids);
        sql = "select i from Plate as i where i.id in (:ids)";
        results = iQuery.findAllByQuery(sql, param);
        Assert.assertTrue(CollectionUtils.isNotEmpty(results));

        param = new ParametersI();
        param.addId(screen.getId().getValue());
        sql = "select i from Screen as i where i.id = :id";
        Assert.assertNotNull(iQuery.findByQuery(sql, param));
    }

    /**
     * Tests to move screen with a plate and a reagent.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testMoveScreenWithReagent() throws Exception {
        String perms = "rw----";
        EventContext ctx = newUserAndGroup(perms);
        ExperimenterGroup g = newGroupAddUser(perms, ctx.userId);
        iAdmin.getEventContext(); // Refresh

        Screen s = mmFactory.simpleScreenData().asScreen();
        Reagent r = mmFactory.createReagent();
        s.addReagent(r);
        Plate p = mmFactory.createPlateWithReagent(1, 1, 1, r);
        s.linkPlate(p);
        s = (Screen) iUpdate.saveAndReturnObject(s);
        long screenId = s.getId().getValue();
        // reagent first
        String sql = "select r from Reagent as r ";
        sql += "join fetch r.screen as s ";
        sql += "where s.id = :id";
        ParametersI param = new ParametersI();
        param.addId(screenId);
        r = (Reagent) iQuery.findByQuery(sql, param);
        long reagentID = r.getId().getValue();
        //
        sql = "select s from ScreenPlateLink as s ";
        sql += "join fetch s.child as c ";
        sql += "join fetch s.parent as p ";
        sql += "where p.id = :id";
        param = new ParametersI();
        param.addId(screenId);
        ScreenPlateLink link = (ScreenPlateLink) iQuery.findByQuery(sql, param);
        p = link.getChild();
        long plateID = p.getId().getValue();

        final Chgrp2 dc = Requests.chgrp().target(s).toGroup(g).build();
        callback(true, client, dc);

        sql = "select r from Screen as r ";
        sql += "where r.id = :id";
        param = new ParametersI();
        param.addId(screenId);
        Assert.assertNull(iQuery.findByQuery(sql, param));

        sql = "select r from Reagent as r ";
        sql += "where r.id = :id";
        param = new ParametersI();
        param.addId(reagentID);
        Assert.assertNull(iQuery.findByQuery(sql, param));

        sql = "select r from Plate as r ";
        sql += "where r.id = :id";
        param = new ParametersI();
        param.addId(plateID);
        Assert.assertNull(iQuery.findByQuery(sql, param));

        // Check data moved
        loginUser(g);
        sql = "select r from Screen as r ";
        sql += "where r.id = :id";
        param = new ParametersI();
        param.addId(screenId);
        Assert.assertNotNull(iQuery.findByQuery(sql, param));

        sql = "select r from Reagent as r ";
        sql += "where r.id = :id";
        param = new ParametersI();
        param.addId(reagentID);
        Assert.assertNotNull(iQuery.findByQuery(sql, param));

        sql = "select r from Plate as r ";
        sql += "where r.id = :id";
        param = new ParametersI();
        param.addId(plateID);
        Assert.assertNotNull(iQuery.findByQuery(sql, param));
    }

    /**
     * Tests to move a plate with a reagent. The test now passes with or w/o the
     * FORCE option. Similar to delete
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testMovePlateWithReagent() throws Exception {
        String perms = "rw----";
        EventContext ctx = newUserAndGroup(perms);
        ExperimenterGroup g = newGroupAddUser(perms, ctx.userId);
        iAdmin.getEventContext(); // Refresh

        Screen s = mmFactory.simpleScreenData().asScreen();
        Reagent r = mmFactory.createReagent();
        s.addReagent(r);
        Plate p = mmFactory.createPlateWithReagent(1, 1, 1, r);
        s.linkPlate(p);
        s = (Screen) iUpdate.saveAndReturnObject(s);
        long screenId = s.getId().getValue();
        // reagent first
        String sql = "select r from Reagent as r ";
        sql += "join fetch r.screen as s ";
        sql += "where s.id = :id";
        ParametersI param = new ParametersI();
        param.addId(screenId);
        r = (Reagent) iQuery.findByQuery(sql, param);
        long reagentID = r.getId().getValue();
        //
        sql = "select s from ScreenPlateLink as s ";
        sql += "join fetch s.child as c ";
        sql += "join fetch s.parent as p ";
        sql += "where p.id = :id";
        param = new ParametersI();
        param.addId(screenId);
        ScreenPlateLink link = (ScreenPlateLink) iQuery.findByQuery(sql, param);
        p = link.getChild();
        long plateID = p.getId().getValue();
        final Chgrp2 dc = Requests.chgrp().target(p).toGroup(g).build();
        callback(true, client, dc);
        sql = "select r from Screen as r ";
        sql += "where r.id = :id";
        param = new ParametersI();
        param.addId(screenId);
        Assert.assertNotNull(iQuery.findByQuery(sql, param));

        sql = "select r from Reagent as r ";
        sql += "where r.id = :id";
        param = new ParametersI();
        param.addId(reagentID);
        Assert.assertNotNull(iQuery.findByQuery(sql, param));

        sql = "select r from Plate as r ";
        sql += "where r.id = :id";
        param = new ParametersI();
        param.addId(plateID);
        Assert.assertNull(iQuery.findByQuery(sql, param));

        // Check move
        loginUser(g);
        sql = "select r from Screen as r ";
        sql += "where r.id = :id";
        param = new ParametersI();
        param.addId(screenId);
        Assert.assertNull(iQuery.findByQuery(sql, param));

        sql = "select r from Reagent as r ";
        sql += "where r.id = :id";
        param = new ParametersI();
        param.addId(reagentID);
        Assert.assertNull(iQuery.findByQuery(sql, param));

        sql = "select r from Plate as r ";
        sql += "where r.id = :id";
        param = new ParametersI();
        param.addId(plateID);
        Assert.assertNotNull(iQuery.findByQuery(sql, param));
    }

    /**
     * Test to move a plate with ROI on images. The ROI will have measurements.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testDeletePlateWithROIMeasurements() throws Exception {
        String perms = "rw----";
        EventContext ctx = newUserAndGroup(perms);
        ExperimenterGroup g = newGroupAddUser(perms, ctx.userId);
        iAdmin.getEventContext(); // Refresh

        Plate p = (Plate) iUpdate.saveAndReturnObject(mmFactory.createPlate(1,
                1, 1, 0, false));
        List<Well> results = loadWells(p.getId().getValue(), true);
        Well well = (Well) results.get(0);
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
        long id = fa.getId().getValue();
        // link fa to ROI
        List<IObject> links = new ArrayList<IObject>();
        RoiAnnotationLink rl = new RoiAnnotationLinkI();
        rl.setChild(new FileAnnotationI(id, false));
        rl.setParent(new RoiI(roi.getId().getValue(), false));
        links.add(rl);
        PlateAnnotationLink il = new PlateAnnotationLinkI();
        il.setChild(new FileAnnotationI(id, false));
        il.setParent(new PlateI(p.getId().getValue(), false));
        links.add(il);
        iUpdate.saveAndReturnArray(links);

        final Chgrp2 dc = Requests.chgrp().target(p).toGroup(g).build();
        callback(true, client, dc);
 
        // Shouldn't have measurements
        ParametersI param = new ParametersI();
        param.addId(id);
        StringBuilder sb = new StringBuilder();
        sb.append("select a from Annotation as a ");
        sb.append("where a.id = :id");
        Assert.assertEquals(iQuery.findAllByQuery(sb.toString(), param).size(), 0);

        loginUser(g);
        List<IObject> l = iQuery.findAllByQuery(sb.toString(), param);
        Assert.assertTrue(CollectionUtils.isNotEmpty(l));
    }

    /**
     * Tests to move a project containing a dataset with images.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testMoveProject() throws Exception {
        String perms = "rw----";
        EventContext ctx = newUserAndGroup(perms);
        ExperimenterGroup g = newGroupAddUser(perms, ctx.userId);
        iAdmin.getEventContext(); // Refresh

        Project p = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject()).proxy();
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject()).proxy();
        Image image1 = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage()).proxy();
        Image image2 = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage()).proxy();
        List<IObject> links = new ArrayList<IObject>();
        links.add(linkParentToChild(d, image1));
        links.add(linkParentToChild(d, image2));

        links.add(linkParentToChild(p, d));
        iUpdate.saveAndReturnArray(links);

        List<Long> ids = new ArrayList<Long>();
        ids.add(image1.getId().getValue());
        ids.add(image2.getId().getValue());

        final Chgrp2 dc = Requests.chgrp().target(p).toGroup(g).build();
        callback(true, client, dc);

        // Check if objects have been deleted
        ParametersI param = new ParametersI();
        param.addIds(ids);
        String sql = "select i from Image as i where i.id in (:ids)";
        List<IObject> results = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(results.size(), 0);

        param = new ParametersI();
        param.addId(d.getId().getValue());
        sql = "select i from Dataset as i where i.id = :id";
        Assert.assertNull(iQuery.findByQuery(sql, param));

        param = new ParametersI();
        param.addId(p.getId().getValue());
        sql = "select i from Project as i where i.id = :id";
        Assert.assertNull(iQuery.findByQuery(sql, param));

        // Logger in to other group
        loginUser(g);

        param = new ParametersI();
        param.addIds(ids);
        sql = "select i from Image as i where i.id in (:ids)";
        results = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(results.size(), ids.size());

        param = new ParametersI();
        param.addId(d.getId().getValue());
        sql = "select i from Dataset as i where i.id = :id";
        Assert.assertNotNull(iQuery.findByQuery(sql, param));

        param = new ParametersI();
        param.addId(p.getId().getValue());
        sql = "select i from Project as i where i.id = :id";
        Assert.assertNotNull(iQuery.findByQuery(sql, param));
    }

    /**
     * Tests to move a screen containing a plate also contained in another
     * screen. The screen should be moved but not the plate.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testMoveScreenWithSharedPlate() throws Exception {
        String perms = "rw----";
        EventContext ctx = newUserAndGroup(perms);
        ExperimenterGroup g = newGroupAddUser(perms, ctx.userId);
        iAdmin.getEventContext(); // Refresh

        Screen s1 = (Screen) iUpdate.saveAndReturnObject(mmFactory
                .simpleScreenData().asIObject());

        Screen s2 = (Screen) iUpdate.saveAndReturnObject(mmFactory
                .simpleScreenData().asIObject());

        // Plate w/o plate acquisition
        Plate p1 = (Plate) iUpdate.saveAndReturnObject(mmFactory.createPlate(1,
                1, 1, 0, false));
        // Plate with plate acquisition
        List<IObject> links = new ArrayList<IObject>();
        ScreenPlateLink link = new ScreenPlateLinkI();
        link.setChild((Plate) p1.proxy());
        link.setParent(s1);
        links.add(link);
        link = new ScreenPlateLinkI();
        link.setChild((Plate) p1.proxy());
        link.setParent(s2);
        links.add(link);
        iUpdate.saveAndReturnArray(links);

        final Chgrp2 dc = Requests.chgrp().target(s1).toGroup(g).build();
        callback(true, client, dc);

        List<Long> ids = new ArrayList<Long>();
        ids.add(p1.getId().getValue());

        // Check if the plates exist.
        ParametersI param = new ParametersI();
        param.addIds(ids);
        String sql = "select i from Plate as i where i.id in (:ids)";
        List<IObject> results = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(results.size(), ids.size());

        param = new ParametersI();
        param.addId(s1.getId().getValue());
        sql = "select i from Screen as i where i.id = :id";
        Assert.assertNull(iQuery.findByQuery(sql, param));

        param = new ParametersI();
        param.addId(s2.getId().getValue());
        Assert.assertNotNull(iQuery.findByQuery(sql, param));

        loginUser(g);
        param = new ParametersI();
        param.addIds(ids);
        // plate should not have moved.
        sql = "select i from Plate as i where i.id in (:ids)";
        results = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(results.size(), 0);

        // screen should move
        param = new ParametersI();
        param.addId(s1.getId().getValue());
        sql = "select i from Screen as i where i.id = :id";
        Assert.assertNotNull(iQuery.findByQuery(sql, param));
    }

    /**
     * Test to move a dataset containing an image whose projection is not in the dataset.
     * The image should be left behind but others should still move.
     * @throws Exception unexpected
     */
    @Test
    public void testMoveDatasetWithProjectedImage() throws Exception {
        /* prepare a pair of groups for the user */
        newUserAndGroup("rwr---");
        final EventContext ctx = iAdmin.getEventContext();
        final ExperimenterGroup source = iAdmin.getGroup(ctx.groupId);
        final ExperimenterGroup destination = newGroupAddUser("rwr---", ctx.userId);

        /* start in the source group */
        loginUser(source);

        /* create a dataset */
        Dataset dataset = new DatasetI();
        dataset.setName(ode.rtypes.rstring("dataset"));
        dataset = (Dataset) iUpdate.saveAndReturnObject(dataset).proxy();

        /* create an instrument */
        Instrument instrument = mmFactory.createInstrument();
        instrument = (Instrument) iUpdate.saveAndReturnObject(instrument).proxy();

        /* the original and its projection are related and share an instrument */
        Image original = mmFactory.createImage();
        original.setInstrument(instrument);
        original = (Image) iUpdate.saveAndReturnObject(original);
        Image projection = mmFactory.createImage();
        projection.setInstrument(instrument);
        projection.getPrimaryPixels().setRelatedTo((Pixels) original.getPrimaryPixels().proxy());
        projection = (Image) iUpdate.saveAndReturnObject(projection);

        original = (Image) original.proxy();
        projection = (Image) projection.proxy();

        /* create another image */
        Image other = mmFactory.createImage();
        other = (Image) iUpdate.saveAndReturnObject(other).proxy();

        /* only the original and the other are in the dataset; the projection is not */
        for (final Image image : new Image[] {original, other}) {
            linkParentToChild(dataset, image);
        }

        /* move the dataset */
        final Chgrp2 chgrp = Requests.chgrp().target(dataset).toGroup(destination).build();
        callback(true, client, chgrp);

        /* check what remains in the source group */
        Assert.assertNull(iQuery.findByQuery("FROM Dataset WHERE id = :id", new ParametersI().addId(dataset.getId())));
        Assert.assertNotNull(iQuery.findByQuery("FROM Image WHERE id = :id", new ParametersI().addId(original.getId())));
        Assert.assertNotNull(iQuery.findByQuery("FROM Image WHERE id = :id", new ParametersI().addId(projection.getId())));
        Assert.assertNull(iQuery.findByQuery("FROM Image WHERE id = :id", new ParametersI().addId(other.getId())));

        /* switch to the destination group */
        loginUser(destination);

        /* check what was moved to the destination group */
        Assert.assertNotNull(iQuery.findByQuery("FROM Dataset WHERE id = :id", new ParametersI().addId(dataset.getId())));
        Assert.assertNull(iQuery.findByQuery("FROM Image WHERE id = :id", new ParametersI().addId(original.getId())));
        Assert.assertNull(iQuery.findByQuery("FROM Image WHERE id = :id", new ParametersI().addId(projection.getId())));
        Assert.assertNotNull(iQuery.findByQuery("FROM Image WHERE id = :id", new ParametersI().addId(other.getId())));
    }

    /**
     * Test to move an image whose light source settings are not referenced by a logical channel.
     * @throws Exception unexpected
     */
    @Test(groups = "ticket:13128")
    public void testMoveLightSourceSettings() throws Exception {
        /* prepare a pair of groups for the user */
        newUserAndGroup("rwr---");
        final EventContext ctx = iAdmin.getEventContext();
        final ExperimenterGroup source = iAdmin.getGroup(ctx.groupId);
        final ExperimenterGroup destination = newGroupAddUser("rwr---", ctx.userId);

        /* start in the source group */
        loginUser(source);

        /* create an image with a light source */
        Image image = mmFactory.createImage();
        image.setInstrument(mmFactory.createInstrument(Arc.class.getName()));
        image = (Image) iUpdate.saveAndReturnObject(image).proxy();

        /* add settings to the image's light source */
        LightSource lightSource = (LightSource) iQuery.findByQuery(
                "SELECT image.instrument.lightSource FROM Image image WHERE image.id = :id",
                new ParametersI().addId(image.getId())).proxy();
        LightSettings lightSettings = mmFactory.createLightSettings(lightSource);
        lightSettings = (LightSettings) iUpdate.saveAndReturnObject(lightSettings).proxy();

        /* move the image */
        final Chgrp2Response rsp = (Chgrp2Response) doChange(Requests.chgrp().target(image).toGroup(destination).build());

        /* check that the move reported that light source settings moved and nothing was deleted */
        Assert.assertTrue(rsp.includedObjects.containsKey(ome.model.acquisition.Arc.class.getName()));
        Assert.assertTrue(rsp.deletedObjects.isEmpty());

        /* check what remains in the source group */
        Assert.assertNull(iQuery.findByQuery("FROM Image WHERE id = :id", new ParametersI().addId(image.getId())));
        Assert.assertNull(iQuery.findByQuery("FROM LightSettings WHERE id = :id", new ParametersI().addId(lightSettings.getId())));

        /* switch to the destination group */
        loginUser(destination);

        /* check what was moved to the destination group */
        Assert.assertNotNull(iQuery.findByQuery("FROM Image WHERE id = :id", new ParametersI().addId(image.getId())));
        Assert.assertNotNull(iQuery.findByQuery("FROM LightSettings WHERE id = :id", new ParametersI().addId(lightSettings.getId())));
    }

    /**
     * Check that a shared ROI transform cannot be split by moving.
     * @throws Exception unexpected
     */
    @Test
    public void testMoveTransformedRoi() throws Exception {
        /* prepare a pair of groups for the user */
        newUserAndGroup("rwr---");
        final EventContext ctx = iAdmin.getEventContext();
        final ExperimenterGroup source = iAdmin.getGroup(ctx.groupId);
        final ExperimenterGroup destination = newGroupAddUser("rwr---", ctx.userId);

        /* start in the source group */
        loginUser(source);

        /* create ROIs whose shapes share a transform */

        AffineTransform transformation = new AffineTransformI();
        transformation.setA00(ode.rtypes.rdouble(0));
        transformation.setA10(ode.rtypes.rdouble(1));
        transformation.setA01(ode.rtypes.rdouble(1));
        transformation.setA11(ode.rtypes.rdouble(0));
        transformation.setA02(ode.rtypes.rdouble(0));
        transformation.setA12(ode.rtypes.rdouble(0));
        transformation = (AffineTransform) iUpdate.saveAndReturnObject(transformation).proxy();

        Point point1 = new PointI();
        point1.setX(ode.rtypes.rdouble(2));
        point1.setY(ode.rtypes.rdouble(3));
        point1.setTransform(transformation);
        Roi roi1 = new RoiI();
        roi1.addShape(point1);
        roi1 = (Roi) iUpdate.saveAndReturnObject(roi1);
        point1 = (Point) roi1.getShape(0);

        Point point2 = new PointI();
        point2.setX(ode.rtypes.rdouble(4));
        point2.setY(ode.rtypes.rdouble(5));
        point2.setTransform(transformation);
        Roi roi2 = new RoiI();
        roi2.addShape(point2);
        roi2 = (Roi) iUpdate.saveAndReturnObject(roi2);
        point2 = (Point) roi2.getShape(0);

        /* note IDs of created objects */
        final List<Long> roiIds = ImmutableList.of(roi1.getId().getValue(), roi2.getId().getValue());
        final List<Long> pointIds = ImmutableList.of(point1.getId().getValue(), point2.getId().getValue());
        final Long transformationId = transformation.getId().getValue();

        /* move of only one ROI fails */
        final Chgrp2Builder move = Requests.chgrp().toGroup(destination).target(roi1);
        doChange(client, factory, move.build(), false);

        /* move of both ROIs succeeds */
        move.target(roi2);
        doChange(move.build());

        /* check what remains in the source group */
        Assert.assertNull(iQuery.findByQuery("FROM Roi WHERE id IN (:ids)", new ParametersI().addIds(roiIds)));
        Assert.assertNull(iQuery.findByQuery("FROM Point WHERE id IN (:ids)", new ParametersI().addIds(pointIds)));
        Assert.assertNull(iQuery.findByQuery("FROM AffineTransform WHERE id = :id", new ParametersI().addId(transformationId)));

        /* switch to the destination group */
        loginUser(destination);

        /* check what was moved to the destination group */
        assertExists(roi1);
        assertExists(roi2);
        assertExists(point1);
        assertExists(point2);
        assertExists(transformation);
    }
}
