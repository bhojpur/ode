package integration.chmod;

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

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import ode.cmd.Delete2;
import ode.cmd.graphs.ChildOption;
import ode.gateway.util.Requests;
import ode.model.Annotation;
import ode.model.CommentAnnotation;
import ode.model.CommentAnnotationI;
import ode.model.Dataset;
import ode.model.DatasetAnnotationLink;
import ode.model.DatasetAnnotationLinkI;
import ode.model.DatasetI;
import ode.model.DatasetImageLink;
import ode.model.DatasetImageLinkI;
import ode.model.IObject;
import ode.model.Image;
import ode.model.Permissions;
import ode.sys.EventContext;
import ode.sys.ParametersI;
import ode.gateway.model.DatasetData;
import integration.AbstractServerTest;

/**
 * Tests the can edit, can annotate.
 */
public class RolesTest extends AbstractServerTest {

    private static final ChildOption KEEP_ANN = Requests.option().excludeType("Annotation").build();

    /**
     * Since we are creating a new client on each invocation, we should also
     * clean it up. Note: {@link #newUserAndGroup(String)} also closes, but not
     * the very last invocation.
     * @throws Exception unexpected
     */
    @AfterMethod
    public void close() throws Exception {
        clean();
    }

    // Group RW---- i.e. private
    /**
     * Test the interaction with an object in a RW member
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testInteractionByMemberRW() throws Exception {
        EventContext ec = newUserAndGroup("rw----");
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        Permissions perms = d.getDetails().getPermissions();
        long id = d.getId().getValue();
        // make sure data owner can do everything
        Assert.assertTrue(perms.canEdit());
        Assert.assertTrue(perms.canAnnotate());
        Assert.assertTrue(perms.canDelete());
        Assert.assertTrue(perms.canLink());

        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());
        // Create link
        DatasetImageLink l = new DatasetImageLinkI();
        l.link(new DatasetI(d.getId().getValue(), false), img);
        l = (DatasetImageLink) iUpdate.saveAndReturnObject(l);

        // Create annotation
        CommentAnnotation annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("comment"));
        Annotation ann = (Annotation) iUpdate.saveAndReturnObject(annotation);
        DatasetAnnotationLink dl = new DatasetAnnotationLinkI();
        dl.link(new DatasetI(d.getId().getValue(), false), ann);
        dl = (DatasetAnnotationLink) iUpdate.saveAndReturnObject(dl);

        disconnect();
        // Now a new member to the group.
        newUserInGroup(ec);
        String sql = "select i from Dataset as i ";
        sql += "where i.id = :id";
        ParametersI param = new ParametersI();
        param.addId(id);
        List<IObject> datasets = iQuery.findAllByQuery(sql, param);
        // Cannot view the data.
        Assert.assertEquals(datasets.size(), 0);

        // Create a link canLink
        // Try to delete the link i.e. canDelete
        try {
            Delete2 dc = Requests.delete().target(l).build();
            callback(false, client, dc);
        } catch (Exception e) {

            Assert.fail("Member should not be allowed to delete "
                    + "an image/dataset link.");
        }

        try {
            Delete2 dc = Requests.delete().target(dl).option(KEEP_ANN).build();
            callback(false, client, dc);
        } catch (Exception e) {
            Assert.fail("Member should not be allowed to delete "
                    + "an image/dataset link.");
        }

        // Try to delete the annotation i.e. canDelete
        try {
            Delete2 dc = Requests.delete().target(ann).build();
            callback(false, client, dc);
        } catch (Exception e) {
            Assert.fail("Member should not be allowed to delete the annotation.");
        }


        // Try to link an image i.e. canLink
        try {
            img = (Image) iUpdate.saveAndReturnObject(mmFactory.createImage());
            l = new DatasetImageLinkI();
            l.link(new DatasetI(id, false), img);
            iUpdate.saveAndReturnObject(l);
            Assert.fail("Member should not be allowed to create an image/dataset link.");
        } catch (Exception e) {
        }

        // Try to create the annotation i.e. canAnnotate
        try {
            annotation = new CommentAnnotationI();
            annotation.setTextValue(ode.rtypes.rstring("comment"));
            ann = (Annotation) iUpdate.saveAndReturnObject(annotation);
            dl = new DatasetAnnotationLinkI();
            dl.link(new DatasetI(d.getId().getValue(), false), ann);
            dl = (DatasetAnnotationLink) iUpdate.saveAndReturnObject(dl);
            Assert.fail("Member should not be allowed to annotate a dataset.");
        } catch (Exception e) {
        }

        // Try to edit i.e. canEdit
        try {
            d.setName(ode.rtypes.rstring("newName"));
            iUpdate.saveAndReturnObject(d);
            Assert.fail("Member should not be allowed to edit a dataset.");
        } catch (Exception e) {
        }
    }

    /**
     * Test the interaction with an object in a RW group by the owner
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testInteractionByGroupOwnerRW() throws Exception {
        EventContext ec = newUserAndGroup("rw----");
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        Permissions perms = d.getDetails().getPermissions();
        long id = d.getId().getValue();
        Assert.assertTrue(perms.canEdit());
        Assert.assertTrue(perms.canAnnotate());
        Assert.assertTrue(perms.canDelete());
        Assert.assertTrue(perms.canLink());

        // create image link

        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());
        DatasetImageLink l = new DatasetImageLinkI();
        l.link(new DatasetI(d.getId().getValue(), false), img);
        l = (DatasetImageLink) iUpdate.saveAndReturnObject(l);

        // Create annotation
        CommentAnnotation annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("comment"));
        Annotation ann = (Annotation) iUpdate.saveAndReturnObject(annotation);
        DatasetAnnotationLink dl = new DatasetAnnotationLinkI();
        dl.link(new DatasetI(id, false), ann);
        dl = (DatasetAnnotationLink) iUpdate.saveAndReturnObject(dl);

        disconnect();
        // Now a new member to the group.
        newUserInGroup(ec);
        makeGroupOwner();
        String sql = "select i from Dataset as i ";
        sql += "where i.id = :id";
        ParametersI param = new ParametersI();
        param.addId(id);
        List<IObject> datasets = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(datasets.size(), 1);
        d = (Dataset) datasets.get(0);
        perms = d.getDetails().getPermissions();

        Assert.assertTrue(perms.canEdit());
        Assert.assertFalse(perms.canAnnotate());
        Assert.assertTrue(perms.canDelete());
        Assert.assertFalse(perms.canLink());

        // Create a link canLink
        // Try to delete the link i.e. canDelete
        Delete2 dc = Requests.delete().target(l).build();
        callback(true, client, dc);

        // Try to delete the annotation link i.e. canDelete
        dc = Requests.delete().target(dl).option(KEEP_ANN).build();
        callback(true, client, dc);

        // Try to delete the annotation i.e. canDelete
        dc = Requests.delete().target(ann).build();
        callback(true, client, dc);

        // Try to link an image i.e. canLink
        try {
            img = (Image) iUpdate.saveAndReturnObject(mmFactory.createImage());
            l = new DatasetImageLinkI();
            l.link(new DatasetI(d.getId().getValue(), false), img);
            iUpdate.saveAndReturnObject(l);
            Assert.fail("Group owner should not be allowed to create "
                    + "an image/dataset link.");
        } catch (Exception e) {
        }

        // Try to create the annotation i.e. canAnnotate
        try {
            annotation = new CommentAnnotationI();
            annotation.setTextValue(ode.rtypes.rstring("comment"));
            ann = (Annotation) iUpdate.saveAndReturnObject(annotation);
            dl = new DatasetAnnotationLinkI();
            dl.link(new DatasetI(d.getId().getValue(), false), ann);
            dl = (DatasetAnnotationLink) iUpdate.saveAndReturnObject(dl);
            Assert.fail("Group owner should not be allowed to annotate "
                    + "a dataset.");
        } catch (Exception e) {
        }

        // Try to edit i.e. canEdit
        d.setName(ode.rtypes.rstring("newName"));
        iUpdate.saveAndReturnObject(d);
    }

    /**
     * Test the interaction with an object in a RW group by the owner
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testInteractionByAdminRW() throws Exception {
        EventContext ec = newUserAndGroup("rw----");
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        Permissions perms = d.getDetails().getPermissions();
        long id = d.getId().getValue();

        Assert.assertTrue(perms.canEdit());
        Assert.assertTrue(perms.canAnnotate());
        Assert.assertTrue(perms.canDelete());
        Assert.assertTrue(perms.canLink());

        // create image link

        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());
        DatasetImageLink l = new DatasetImageLinkI();
        l.link(new DatasetI(id, false), img);
        l = (DatasetImageLink) iUpdate.saveAndReturnObject(l);

        // Create annotation
        CommentAnnotation annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("comment"));
        Annotation ann = (Annotation) iUpdate.saveAndReturnObject(annotation);
        DatasetAnnotationLink dl = new DatasetAnnotationLinkI();
        dl.link(new DatasetI(id, false), ann);
        dl = (DatasetAnnotationLink) iUpdate.saveAndReturnObject(dl);

        disconnect();
        logRootIntoGroup(ec);

        String sql = "select i from Dataset as i ";
        sql += "where i.id = :id";
        ParametersI param = new ParametersI();
        param.addId(id);
        List<IObject> datasets = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(datasets.size(), 1);
        d = (Dataset) datasets.get(0);
        perms = d.getDetails().getPermissions();

        Assert.assertFalse(perms.canAnnotate());
        Assert.assertTrue(perms.canDelete());
        Assert.assertTrue(perms.canEdit());
        Assert.assertFalse(perms.canLink());

        // Create a link canLink
        // Try to delete the link i.e. canDelete
        Delete2 dc = Requests.delete().target(l).build();
        callback(true, client, dc);

        // Try to delete the annotation link i.e. canDelete
        dc = Requests.delete().target(dl).option(KEEP_ANN).build();
        callback(true, client, dc);

        // Try to delete the annotation i.e. canDelete
        dc = Requests.delete().target(ann).build();
        callback(true, client, dc);

        // Try to link an image i.e. canLink
        try {
            img = (Image) iUpdate.saveAndReturnObject(mmFactory.createImage());
            l = new DatasetImageLinkI();
            l.link(new DatasetI(id, false), img);
            iUpdate.saveAndReturnObject(l);
            Assert.fail("Admin should not be allowed to create an image/dataset link.");
        } catch (Exception e) {
        }

        // Try to create the annotation i.e. canAnnotate
        try {
            annotation = new CommentAnnotationI();
            annotation.setTextValue(ode.rtypes.rstring("comment"));
            ann = (Annotation) iUpdate.saveAndReturnObject(annotation);
            dl = new DatasetAnnotationLinkI();
            dl.link(new DatasetI(id, false), ann);
            dl = (DatasetAnnotationLink) iUpdate.saveAndReturnObject(dl);
            Assert.fail("Admin should not be allowed to annotate a dataset.");
        } catch (Exception e) {
        }

        // Try to edit i.e. canEdit
        d.setName(ode.rtypes.rstring("newName"));
        iUpdate.saveAndReturnObject(d);
    }

    // Group RWR---
    /**
     * Test the interaction with an object in a RWR group by a member
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testInteractionByMemberRWR() throws Exception {
        EventContext ec = newUserAndGroup("rwr---");
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        long id = d.getId().getValue();

        Permissions perms = d.getDetails().getPermissions();
        // make sure data owner can do everything
        Assert.assertTrue(perms.canEdit());
        Assert.assertTrue(perms.canAnnotate());
        Assert.assertTrue(perms.canDelete());
        Assert.assertTrue(perms.canLink());

        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());
        // Create link
        DatasetImageLink l = new DatasetImageLinkI();
        l.link(new DatasetI(id, false), img);
        l = (DatasetImageLink) iUpdate.saveAndReturnObject(l);

        // Create annotation
        CommentAnnotation annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("comment"));
        Annotation ann = (Annotation) iUpdate.saveAndReturnObject(annotation);
        DatasetAnnotationLink dl = new DatasetAnnotationLinkI();
        dl.link(new DatasetI(id, false), ann);
        dl = (DatasetAnnotationLink) iUpdate.saveAndReturnObject(dl);

        disconnect();

        // Now a new member to the group.
        newUserInGroup(ec);
        String sql = "select i from Dataset as i ";
        sql += "where i.id = :id";
        ParametersI param = new ParametersI();
        param.addId(id);
        List<IObject> datasets = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(datasets.size(), 1);

        // Just a member should be able to neither (for the moment)
        // Reload the perms (from the object that the member loaded)
        // and check status.
        d = (Dataset) datasets.get(0);

        perms = d.getDetails().getPermissions();

        Assert.assertFalse(perms.canEdit());
        Assert.assertFalse(perms.canAnnotate());
        Assert.assertFalse(perms.canDelete());
        Assert.assertFalse(perms.canLink());

        DatasetData data = new DatasetData(d);
        Assert.assertFalse(data.canEdit());
        Assert.assertFalse(data.canAnnotate());
        Assert.assertFalse(data.canDelete());
        Assert.assertFalse(data.canLink());
        // Create a link canLink
        // Try to delete the link i.e. canDelete
        try {
            Delete2 dc = Requests.delete().target(l).build();
            callback(false, client, dc);
        } catch (Exception e) {

            Assert.fail("Member should not be allowed to delete "
                    + "an image/dataset link.");
        }

        try {
            Delete2 dc = Requests.delete().target(dl).option(KEEP_ANN).build();
            callback(false, client, dc);
        } catch (Exception e) {
            Assert.fail("Member should not be allowed to delete "
                    + "an image/dataset link.");
        }

        // Try to delete the annotation i.e. canDelete
        try {
            Delete2 dc = Requests.delete().target(ann).build();
            callback(false, client, dc);
        } catch (Exception e) {
            Assert.fail("Member should not be allowed to delete " + "the annotation.");
        }

        // Try to link an image i.e. canLink
        try {
            img = (Image) iUpdate.saveAndReturnObject(mmFactory.createImage());
            l = new DatasetImageLinkI();
            l.link(new DatasetI(id, false), img);
            iUpdate.saveAndReturnObject(l);
            Assert.fail("Member should not be allowed to create an image/dataset link.");
        } catch (Exception e) {
        }

        // Try to create the annotation i.e. canAnnotate
        try {
            annotation = new CommentAnnotationI();
            annotation.setTextValue(ode.rtypes.rstring("comment"));
            ann = (Annotation) iUpdate.saveAndReturnObject(annotation);
            dl = new DatasetAnnotationLinkI();
            dl.link(new DatasetI(id, false), ann);
            dl = (DatasetAnnotationLink) iUpdate.saveAndReturnObject(dl);
            Assert.fail("Member should not be allowed to annotate a dataset.");
        } catch (Exception e) {
        }

        // Try to edit i.e. canEdit
        try {
            d.setName(ode.rtypes.rstring("newName"));
            iUpdate.saveAndReturnObject(d);
            Assert.fail("Member should not be allowed to edit a dataset.");
        } catch (Exception e) {
        }
    }

    /**
     * Test the interaction with an object in a RWR group by the owner
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testInteractionByGroupOwnerRWR() throws Exception {
        EventContext ec = newUserAndGroup("rwr---");
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        long id = d.getId().getValue();
        Permissions perms = d.getDetails().getPermissions();
        // make sure owner can do everything
        Assert.assertTrue(perms.canEdit());
        Assert.assertTrue(perms.canAnnotate());
        Assert.assertTrue(perms.canDelete());
        Assert.assertTrue(perms.canLink());

        // create image link

        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());
        DatasetImageLink l = new DatasetImageLinkI();
        l.link(new DatasetI(id, false), img);
        l = (DatasetImageLink) iUpdate.saveAndReturnObject(l);

        // Create annotation
        CommentAnnotation annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("comment"));
        Annotation ann = (Annotation) iUpdate.saveAndReturnObject(annotation);
        DatasetAnnotationLink dl = new DatasetAnnotationLinkI();
        dl.link(new DatasetI(id, false), ann);
        dl = (DatasetAnnotationLink) iUpdate.saveAndReturnObject(dl);

        disconnect();
        // Now a new member to the group.
        newUserInGroup(ec);
        makeGroupOwner();
        String sql = "select i from Dataset as i ";
        sql += "where i.id = :id";
        ParametersI param = new ParametersI();
        param.addId(id);
        List<IObject> datasets = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(datasets.size(), 1);
        d = (Dataset) datasets.get(0);

        perms = d.getDetails().getPermissions();

        // Check what the group owner can do
        Assert.assertTrue(perms.canEdit());
        Assert.assertTrue(perms.canAnnotate());
        Assert.assertTrue(perms.canDelete());
        Assert.assertTrue(perms.canLink());

        // Try to delete the link i.e. canLink
        Delete2 dc = Requests.delete().target(l).build();
        callback(true, client, dc);

        // Try to delete the annotation link i.e. canDelete
        dc = Requests.delete().target(dl).option(KEEP_ANN).build();
        callback(true, client, dc);

        // Try to delete the annotation i.e. canDelete
        dc = Requests.delete().target(ann).build();
        callback(true, client, dc);

        // Try to link an image i.e. canLink
        img = (Image) iUpdate.saveAndReturnObject(mmFactory.createImage());
        l = new DatasetImageLinkI();
        l.link(new DatasetI(d.getId().getValue(), false), img);
        iUpdate.saveAndReturnObject(l);

        // Try to create the annotation i.e. canAnnotate
        annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("comment"));
        ann = (Annotation) iUpdate.saveAndReturnObject(annotation);
        dl = new DatasetAnnotationLinkI();
        dl.link(new DatasetI(id, false), ann);
        dl = (DatasetAnnotationLink) iUpdate.saveAndReturnObject(dl);

        // Try to edit i.e. canEdit
        d.setName(ode.rtypes.rstring("newName"));
        iUpdate.saveAndReturnObject(d);
    }

    /**
     * Test the interaction with an object in a RWR group by the admin
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testInteractionByAdminRWR() throws Exception {
        EventContext ec = newUserAndGroup("rwr---");
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        Permissions perms = d.getDetails().getPermissions();
        long id = d.getId().getValue();

        Assert.assertTrue(perms.canEdit());
        Assert.assertTrue(perms.canAnnotate());
        Assert.assertTrue(perms.canDelete());
        Assert.assertTrue(perms.canLink());

        // create image link
        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());
        DatasetImageLink l = new DatasetImageLinkI();
        l.link(new DatasetI(id, false), img);
        l = (DatasetImageLink) iUpdate.saveAndReturnObject(l);

        // Create annotation
        CommentAnnotation annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("comment"));
        Annotation ann = (Annotation) iUpdate.saveAndReturnObject(annotation);
        DatasetAnnotationLink dl = new DatasetAnnotationLinkI();
        dl.link(new DatasetI(id, false), ann);
        dl = (DatasetAnnotationLink) iUpdate.saveAndReturnObject(dl);

        disconnect();
        // Now a new member to the group.
        logRootIntoGroup(ec);
        String sql = "select i from Dataset as i ";
        sql += "where i.id = :id";
        ParametersI param = new ParametersI();
        param.addId(id);
        List<IObject> datasets = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(datasets.size(), 1);
        d = (Dataset) datasets.get(0);
        id = d.getId().getValue();

        perms = d.getDetails().getPermissions();

        // Check what the group owner can do
        Assert.assertTrue(perms.canEdit());
        Assert.assertTrue(perms.canAnnotate());
        Assert.assertTrue(perms.canDelete());
        Assert.assertTrue(perms.canLink());

        // Try to delete the link i.e. canLink
        Delete2 dc = Requests.delete().target(l).build();
        callback(true, client, dc);

        // Try to delete the annotation link i.e. canDelete
        dc = Requests.delete().target(dl).option(KEEP_ANN).build();
        callback(true, client, dc);

        // Try to delete the annotation i.e. canDelete
        dc = Requests.delete().target(ann).build();
        callback(true, client, dc);

        // Try to link an image i.e. canLink
        img = (Image) iUpdate.saveAndReturnObject(mmFactory.createImage());
        l = new DatasetImageLinkI();
        l.link(new DatasetI(id, false), img);
        iUpdate.saveAndReturnObject(l);

        // Try to create the annotation i.e. canAnnotate
        annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("comment"));
        ann = (Annotation) iUpdate.saveAndReturnObject(annotation);
        dl = new DatasetAnnotationLinkI();
        dl.link(new DatasetI(id, false), ann);
        dl = (DatasetAnnotationLink) iUpdate.saveAndReturnObject(dl);

        // Try to edit i.e. canEdit
        d.setName(ode.rtypes.rstring("newName"));
        iUpdate.saveAndReturnObject(d);
    }

    // Group RWRA--
    /**
     * Test the interaction with an object in a RWRA group by a member
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testInteractionByMemberRWRA() throws Exception {
        EventContext ec = newUserAndGroup("rwra--");
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        Permissions perms = d.getDetails().getPermissions();
        long id = d.getId().getValue();

        Assert.assertTrue(perms.canAnnotate());
        Assert.assertTrue(perms.canEdit());
        Assert.assertTrue(perms.canLink());
        Assert.assertTrue(perms.canAnnotate());

        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());
        // Create link
        DatasetImageLink l = new DatasetImageLinkI();
        l.link(new DatasetI(id, false), img);
        l = (DatasetImageLink) iUpdate.saveAndReturnObject(l);

        // Create annotation
        CommentAnnotation annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("comment"));
        Annotation ann = (Annotation) iUpdate.saveAndReturnObject(annotation);
        DatasetAnnotationLink dl = new DatasetAnnotationLinkI();
        dl.link(new DatasetI(id, false), ann);
        dl = (DatasetAnnotationLink) iUpdate.saveAndReturnObject(dl);

        disconnect();

        // Now a new member to the group.
        newUserInGroup(ec);
        String sql = "select i from Dataset as i ";
        sql += "where i.id = :id";
        ParametersI param = new ParametersI();
        param.addId(id);
        List<IObject> datasets = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(datasets.size(), 1);

        // Just a member should be able to neither (for the moment)
        // Reload the perms (from the object that the member loaded)
        // and check status.
        d = (Dataset) datasets.get(0);

        perms = d.getDetails().getPermissions();

        Assert.assertFalse(perms.canEdit());
        Assert.assertTrue(perms.canAnnotate());
        Assert.assertFalse(perms.canDelete());
        Assert.assertFalse(perms.canLink());

        // Create a link canLink
        // Try to delete the link i.e. canDelete
        try {
            Delete2 dc = Requests.delete().target(l).build();
            callback(false, client, dc);
        } catch (Exception e) {

            Assert.fail("Member should not be allowed to delete "
                    + "an image/dataset link.");
        }

        try {
            Delete2 dc = Requests.delete().target(dl).option(KEEP_ANN).build();
            callback(false, client, dc);
        } catch (Exception e) {
            Assert.fail("Member should not be allowed to delete "
                    + "an image/dataset link.");
        }

        // Try to delete the annotation i.e. canDelete
        try {
            Delete2 dc = Requests.delete().target(ann).build();
            callback(false, client, dc);
        } catch (Exception e) {
            Assert.fail("Member should not be allowed to delete " + "the annotation.");
        }

        // Try to link an image i.e. canLink
        try {
            img = (Image) iUpdate.saveAndReturnObject(mmFactory.createImage());
            l = new DatasetImageLinkI();
            l.link(new DatasetI(d.getId().getValue(), false), img);
            iUpdate.saveAndReturnObject(l);
            Assert.fail("Member should not be allowed to create an image/dataset link.");
        } catch (Exception e) {
        }

        // Try to create the annotation i.e. canAnnotate
        annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("comment"));
        ann = (Annotation) iUpdate.saveAndReturnObject(annotation);
        dl = new DatasetAnnotationLinkI();
        dl.link(new DatasetI(id, false), ann);
        dl = (DatasetAnnotationLink) iUpdate.saveAndReturnObject(dl);

        // Try to edit i.e. canEdit
        try {
            d.setName(ode.rtypes.rstring("newName"));
            iUpdate.saveAndReturnObject(d);
            Assert.fail("Member should not be allowed to edit a dataset.");
        } catch (Exception e) {
        }
    }

    /**
     * Test the interaction with an object in a RWRA group by the owner
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testInteractionByGroupOwnerRWRA() throws Exception {
        EventContext ec = newUserAndGroup("rwra--");
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        Permissions perms = d.getDetails().getPermissions();
        // make sure owner can do everything
        Assert.assertTrue(perms.canEdit());
        Assert.assertTrue(perms.canAnnotate());
        Assert.assertTrue(perms.canDelete());
        Assert.assertTrue(perms.canLink());

        long id = d.getId().getValue();

        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());
        // Create link
        DatasetImageLink l = new DatasetImageLinkI();
        l.link(new DatasetI(id, false), img);
        l = (DatasetImageLink) iUpdate.saveAndReturnObject(l);

        // Create annotation
        CommentAnnotation annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("comment"));
        Annotation ann = (Annotation) iUpdate.saveAndReturnObject(annotation);
        DatasetAnnotationLink dl = new DatasetAnnotationLinkI();
        dl.link(new DatasetI(id, false), ann);
        dl = (DatasetAnnotationLink) iUpdate.saveAndReturnObject(dl);

        disconnect();
        // Now a new member to the group.
        newUserInGroup(ec);
        makeGroupOwner();
        String sql = "select i from Dataset as i ";
        sql += "where i.id = :id";
        ParametersI param = new ParametersI();
        param.addId(id);
        List<IObject> datasets = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(datasets.size(), 1);
        d = (Dataset) datasets.get(0);

        perms = d.getDetails().getPermissions();

        // Check what the group owner can do
        Assert.assertTrue(perms.canEdit());
        Assert.assertTrue(perms.canAnnotate());
        Assert.assertTrue(perms.canDelete());
        Assert.assertTrue(perms.canLink());

        // Try to delete the link i.e. canLink
        Delete2 dc = Requests.delete().target(l).build();
        callback(true, client, dc);

        // Try to delete the annotation link i.e. canDelete
        dc = Requests.delete().target(dl).option(KEEP_ANN).build();
        callback(true, client, dc);

        // Try to delete the annotation i.e. canDelete
        dc = Requests.delete().target(ann).build();
        callback(true, client, dc);

        // Try to link an image i.e. canLink
        img = (Image) iUpdate.saveAndReturnObject(mmFactory.createImage());
        l = new DatasetImageLinkI();
        l.link(new DatasetI(d.getId().getValue(), false), img);
        iUpdate.saveAndReturnObject(l);

        // Try to create the annotation i.e. canAnnotate
        annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("comment"));
        ann = (Annotation) iUpdate.saveAndReturnObject(annotation);
        dl = new DatasetAnnotationLinkI();
        dl.link(new DatasetI(id, false), ann);
        dl = (DatasetAnnotationLink) iUpdate.saveAndReturnObject(dl);

        // Try to edit i.e. canEdit
        d.setName(ode.rtypes.rstring("newName"));
        iUpdate.saveAndReturnObject(d);
    }

    /**
     * Test the interaction with an object in a RWRA group by the admin
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testInteractionByAdminRWRA() throws Exception {
        EventContext ec = newUserAndGroup("rwr---");
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());

        long id = d.getId().getValue();

        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());
        // Create link
        DatasetImageLink l = new DatasetImageLinkI();
        l.link(new DatasetI(d.getId().getValue(), false), img);
        l = (DatasetImageLink) iUpdate.saveAndReturnObject(l);

        // Create annotation
        CommentAnnotation annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("comment"));
        Annotation ann = (Annotation) iUpdate.saveAndReturnObject(annotation);
        DatasetAnnotationLink dl = new DatasetAnnotationLinkI();
        dl.link(new DatasetI(id, false), ann);
        dl = (DatasetAnnotationLink) iUpdate.saveAndReturnObject(dl);

        Permissions perms = d.getDetails().getPermissions();
        Assert.assertTrue(perms.canEdit());
        Assert.assertTrue(perms.canAnnotate());
        Assert.assertTrue(perms.canDelete());
        Assert.assertTrue(perms.canLink());

        disconnect();
        // Now a new member to the group.
        logRootIntoGroup(ec);
        String sql = "select i from Dataset as i ";
        sql += "where i.id = :id";
        ParametersI param = new ParametersI();
        param.addId(id);
        List<IObject> datasets = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(datasets.size(), 1);

        d = (Dataset) datasets.get(0);

        perms = d.getDetails().getPermissions();
        // Check what the group owner can do
        Assert.assertTrue(perms.canEdit());
        Assert.assertTrue(perms.canAnnotate());
        Assert.assertTrue(perms.canDelete());
        Assert.assertTrue(perms.canLink());

        // Try to delete the link i.e. canLink
        Delete2 dc = Requests.delete().target(l).build();
        callback(true, client, dc);

        // Try to delete the annotation link i.e. canDelete
        dc = Requests.delete().target(dl).option(KEEP_ANN).build();
        callback(true, client, dc);

        // Try to delete the annotation i.e. canDelete
        dc = Requests.delete().target(ann).build();
        callback(true, client, dc);

        // Try to link an image i.e. canLink
        img = (Image) iUpdate.saveAndReturnObject(mmFactory.createImage());
        l = new DatasetImageLinkI();
        l.link(new DatasetI(id, false), img);
        iUpdate.saveAndReturnObject(l);

        // Try to create the annotation i.e. canAnnotate
        annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("comment"));
        ann = (Annotation) iUpdate.saveAndReturnObject(annotation);
        dl = new DatasetAnnotationLinkI();
        dl.link(new DatasetI(id, false), ann);
        dl = (DatasetAnnotationLink) iUpdate.saveAndReturnObject(dl);

        // Try to edit i.e. canEdit
        d.setName(ode.rtypes.rstring("newName"));
        iUpdate.saveAndReturnObject(d);
    }

    // Group RWRW--
    /**
     * Test the interaction with an object in a RWRW group by a member
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testInteractionByMemberRWRW() throws Exception {
        EventContext ec = newUserAndGroup("rwrw--");
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        Permissions perms = d.getDetails().getPermissions();
        long id = d.getId().getValue();
        Assert.assertTrue(perms.canEdit());
        Assert.assertTrue(perms.canAnnotate());
        Assert.assertTrue(perms.canDelete());
        Assert.assertTrue(perms.canLink());

        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());
        // Create link
        DatasetImageLink l = new DatasetImageLinkI();
        l.link(new DatasetI(id, false), img);
        l = (DatasetImageLink) iUpdate.saveAndReturnObject(l);

        // Create annotation
        CommentAnnotation annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("comment"));
        Annotation ann = (Annotation) iUpdate.saveAndReturnObject(annotation);
        DatasetAnnotationLink dl = new DatasetAnnotationLinkI();
        dl.link(new DatasetI(id, false), ann);
        dl = (DatasetAnnotationLink) iUpdate.saveAndReturnObject(dl);

        disconnect();
        // Now a new member to the group.
        newUserInGroup(ec);
        String sql = "select i from Dataset as i ";
        sql += "where i.id = :id";
        ParametersI param = new ParametersI();
        param.addId(id);
        List<IObject> datasets = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(datasets.size(), 1);
        d = (Dataset) datasets.get(0);

        perms = d.getDetails().getPermissions();

        // Check what the group owner can do
        Assert.assertTrue(perms.canEdit());
        Assert.assertTrue(perms.canAnnotate());
        Assert.assertTrue(perms.canDelete());
        Assert.assertTrue(perms.canLink());

        // Try to delete the link i.e. canLink
        Delete2 dc = Requests.delete().target(l).build();
        callback(true, client, dc);

        // Try to delete the annotation link i.e. canDelete
        dc = Requests.delete().target(dl).option(KEEP_ANN).build();
        callback(true, client, dc);

        // Try to delete the annotation i.e. canDelete
        dc = Requests.delete().target(ann).build();
        callback(true, client, dc);
        // Try to delete the dataset i.e. canDelete

        // Try to link an image i.e. canLink
        img = (Image) iUpdate.saveAndReturnObject(mmFactory.createImage());
        l = new DatasetImageLinkI();
        l.link(new DatasetI(id, false), img);
        iUpdate.saveAndReturnObject(l); // The dataset's been deleted??

        // Try to create the annotation i.e. canAnnotate
        annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("comment"));
        ann = (Annotation) iUpdate.saveAndReturnObject(annotation);
        dl = new DatasetAnnotationLinkI();
        dl.link(new DatasetI(id, false), ann);
        dl = (DatasetAnnotationLink) iUpdate.saveAndReturnObject(dl);

        // Try to edit i.e. canEdit
        d.setName(ode.rtypes.rstring("newName"));
        iUpdate.saveAndReturnObject(d);
        dc = Requests.delete().target(d).build();
        callback(true, client, dc);
    }

    /**
     * Test the interaction with an object in a RW group by the owner
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testInteractionByGroupOwnerRWRW() throws Exception {
        EventContext ec = newUserAndGroup("rwrw--");
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        Permissions perms = d.getDetails().getPermissions();
        long id = d.getId().getValue();

        // make sure data owner can do everything
        Assert.assertTrue(perms.canEdit());
        Assert.assertTrue(perms.canAnnotate());
        Assert.assertTrue(perms.canDelete());
        Assert.assertTrue(perms.canLink());

        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());
        // Create link
        DatasetImageLink l = new DatasetImageLinkI();
        l.link(new DatasetI(id, false), img);
        l = (DatasetImageLink) iUpdate.saveAndReturnObject(l);

        // Create annotation
        CommentAnnotation annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("comment"));
        Annotation ann = (Annotation) iUpdate.saveAndReturnObject(annotation);
        DatasetAnnotationLink dl = new DatasetAnnotationLinkI();
        dl.link(new DatasetI(id, false), ann);
        dl = (DatasetAnnotationLink) iUpdate.saveAndReturnObject(dl);

        disconnect();
        // Now a new member to the group.
        newUserInGroup(ec);
        makeGroupOwner();
        String sql = "select i from Dataset as i ";
        sql += "where i.id = :id";
        ParametersI param = new ParametersI();
        param.addId(id);
        List<IObject> datasets = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(datasets.size(), 1);
        d = (Dataset) datasets.get(0);

        perms = d.getDetails().getPermissions();

        // Check what the group owner can do
        Assert.assertTrue(perms.canEdit());
        Assert.assertTrue(perms.canAnnotate());
        Assert.assertTrue(perms.canDelete());
        Assert.assertTrue(perms.canLink());

        // Try to delete the link i.e. canLink
        Delete2 dc = Requests.delete().target(l).build();
        callback(true, client, dc);

        // Try to delete the annotation link i.e. canDelete
        dc = Requests.delete().target(dl).option(KEEP_ANN).build();
        callback(true, client, dc);

        // Try to delete the annotation i.e. canDelete
        dc = Requests.delete().target(ann).build();
        callback(true, client, dc);

        // Try to link an image i.e. canLink
        img = (Image) iUpdate.saveAndReturnObject(mmFactory.createImage());
        l = new DatasetImageLinkI();
        l.link(new DatasetI(id, false), img);
        iUpdate.saveAndReturnObject(l);

        // Try to create the annotation i.e. canAnnotate
        annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("comment"));
        ann = (Annotation) iUpdate.saveAndReturnObject(annotation);
        dl = new DatasetAnnotationLinkI();
        dl.link(new DatasetI(id, false), ann);
        dl = (DatasetAnnotationLink) iUpdate.saveAndReturnObject(dl);

        // Try to edit i.e. canEdit
        d.setName(ode.rtypes.rstring("newName"));
        iUpdate.saveAndReturnObject(d);
    }

    /**
     * Test the interaction with an object in a RWRW group by the admin
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testInteractionByAdminRWRW() throws Exception {
        EventContext ec = newUserAndGroup("rwrw--");
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        Permissions perms = d.getDetails().getPermissions();
        long id = d.getId().getValue();

        // Check what the group owner can do
        Assert.assertTrue(perms.canEdit());
        Assert.assertTrue(perms.canAnnotate());
        Assert.assertTrue(perms.canDelete());
        Assert.assertTrue(perms.canLink());

        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());
        // Create link
        DatasetImageLink l = new DatasetImageLinkI();
        l.link(new DatasetI(d.getId().getValue(), false), img);
        l = (DatasetImageLink) iUpdate.saveAndReturnObject(l);

        // Create annotation
        CommentAnnotation annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("comment"));
        Annotation ann = (Annotation) iUpdate.saveAndReturnObject(annotation);
        DatasetAnnotationLink dl = new DatasetAnnotationLinkI();
        dl.link(new DatasetI(id, false), ann);
        dl = (DatasetAnnotationLink) iUpdate.saveAndReturnObject(dl);

        disconnect();
        // Now a new member to the group.
        logRootIntoGroup(ec);

        String sql = "select i from Dataset as i ";
        sql += "where i.id = :id";
        ParametersI param = new ParametersI();
        param.addId(id);
        List<IObject> datasets = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(datasets.size(), 1);
        d = (Dataset) datasets.get(0);

        // Check what the group owner can do
        Assert.assertTrue(perms.canEdit());
        Assert.assertTrue(perms.canAnnotate());
        Assert.assertTrue(perms.canDelete());
        Assert.assertTrue(perms.canLink());

        // Try to delete the link i.e. canLink
        Delete2 dc = Requests.delete().target(l).build();
        callback(true, client, dc);

        // Try to delete the annotation link i.e. canDelete
        dc = Requests.delete().target(dl).option(KEEP_ANN).build();
        callback(true, client, dc);

        // Try to delete the annotation i.e. canDelete
        dc = Requests.delete().target(ann).build();
        callback(true, client, dc);

        // Try to link an image i.e. canLink
        img = (Image) iUpdate.saveAndReturnObject(mmFactory.createImage());
        l = new DatasetImageLinkI();
        l.link(new DatasetI(id, false), img);
        iUpdate.saveAndReturnObject(l);

        // Try to create the annotation i.e. canAnnotate
        annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("comment"));
        ann = (Annotation) iUpdate.saveAndReturnObject(annotation);
        dl = new DatasetAnnotationLinkI();
        dl.link(new DatasetI(id, false), ann);
        dl = (DatasetAnnotationLink) iUpdate.saveAndReturnObject(dl);

        // Try to edit i.e. canEdit
        d.setName(ode.rtypes.rstring("newName"));
        iUpdate.saveAndReturnObject(d);
    }

}
