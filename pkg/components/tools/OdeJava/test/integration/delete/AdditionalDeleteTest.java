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

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import ode.testing.ObjectFactory;
import ode.ApiUsageException;
import ode.RObject;
import ode.RType;
import ode.cmd.Delete2;
import ode.cmd.SkipHead;
import ode.cmd.graphs.ChildOption;
import ode.gateway.util.Requests;
import ode.model.AffineTransform;
import ode.model.AffineTransformI;
import ode.model.AnnotationAnnotationLink;
import ode.model.AnnotationAnnotationLinkI;
import ode.model.Dataset;
import ode.model.DatasetI;
import ode.model.ExternalInfo;
import ode.model.ExternalInfoI;
import ode.model.FileAnnotation;
import ode.model.FileAnnotationI;
import ode.model.IObject;
import ode.model.ImageAnnotationLink;
import ode.model.ImageAnnotationLinkI;
import ode.model.ImageI;
import ode.model.OriginalFile;
import ode.model.OriginalFileI;
import ode.model.Pixels;
import ode.model.Plate;
import ode.model.PlateI;
import ode.model.Point;
import ode.model.PointI;
import ode.model.Project;
import ode.model.ProjectI;
import ode.model.Roi;
import ode.model.RoiI;
import ode.model.Screen;
import ode.model.ScreenAnnotationLink;
import ode.model.ScreenAnnotationLinkI;
import ode.model.ScreenI;
import ode.model.TagAnnotation;
import ode.model.TagAnnotationI;
import ode.model.TermAnnotation;
import ode.model.TermAnnotationI;
import ode.model.Well;
import ode.model.WellI;
import ode.model.WellSample;
import ode.model.WellSampleI;
import ode.sys.ParametersI;
import ode.util.IceMapper;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests deletion of various elements of the graph.
 * These tests are resurrected from the previous DeleteITest class, now deleted.
 */
@Test(groups = { "integration", "delete" })
public class AdditionalDeleteTest extends AbstractServerTest {

    /**
     * Uses the /Image/Pixels/Channel delete specification to remove the
     * channels added during {@link #importImage()} and tests that the channels
     * are gone afterwards.
     */
    public void testDeleteChannels() throws Throwable {

        // Create test data
        final long imageId = importImage();
        List<?> ids = iQuery.projection(
                "select ch.id from Channel ch where ch.pixels.image.id = "
                        + imageId, null);
        Assert.assertFalse(ids.isEmpty());

        // Perform delete
        final SkipHead dc = Requests.skipHead().target("Image").id(imageId).startFrom("Channel")
                .request(Delete2.class).build();
        callback(true, client, dc);

        // Check that data is gone
        ids = iQuery.projection(
                "select ch.id from Channel ch where ch.pixels.image.id = "
                        + imageId, null);
        Assert.assertTrue(ids.isEmpty());

        // Check that the image remains
        ids = iQuery.projection(
                "select id from Image where id = "
                        + imageId, null);
        Assert.assertFalse(ids.isEmpty());
    }

    /**
     * Uses the /Image/Pixels/RenderingDef delete specification to remove the
     * channels added during {@link #importImage()} and tests that the settings
     * are gone afterwards.
     */
    public void testDeleteRenderingDef() throws Throwable {

        // Create test data
        final long imageId = importImage();
        String check = "select rdef.id from RenderingDef rdef where rdef.pixels.image.id = "
                + imageId;
        List<?> ids = iQuery.projection(check, null);
        Assert.assertFalse(ids.isEmpty());

        // Perform delete
        final SkipHead dc = Requests.skipHead().target("Image").id(imageId).startFrom("RenderingDef")
                .request(Delete2.class).build();
        callback(true, client, dc);

        // Check that data is gone
        ids = iQuery.projection(check, null);
        Assert.assertTrue(ids.isEmpty());

        // Check that the channels remain
        ids = iQuery.projection(
                "select ch.id from Channel ch where ch.pixels.image.id = "
                        + imageId, null);
        Assert.assertFalse(ids.isEmpty());

        // Check that the image remains
        ids = iQuery.projection(
                "select id from Image where id = "
                        + imageId, null);
        Assert.assertFalse(ids.isEmpty());
    }

    /**
     * Deletes the whole image.
     */
    public void testImage() throws Exception {
        final long imageId = iUpdate.saveAndReturnObject(mmFactory.createImage()).getId().getValue();
        final Delete2 dc = Requests.delete().target("Image").id(imageId).build();
        callback(true, client, dc);

        // Check that data is gone
        List<?> l = iQuery.projection("select i.id from Image i where i.id = "
                + imageId, null);
        Assert.assertTrue(l.isEmpty());
    }

    /**
     * Deletes the whole image using the subclass name {@link ImageI}.
     */
    public void testImageI() throws Exception {
        final long imageId = iUpdate.saveAndReturnObject(mmFactory.createImage()).getId().getValue();
        final Delete2 dc = Requests.delete().target("ImageI").id(imageId).build();
        callback(true, client, dc);

        // Check that data is gone
        List<?> l = iQuery.projection("select i.id from Image i where i.id = "
                + imageId, null);
        Assert.assertTrue(l.isEmpty());
    }

    /**
     * Uses the /Image delete specification to remove an Image and its
     * annotations simply linked annotation. This is the most basic case.
     */
    @Test(groups = "ticket:2769")
    public void testImageWithAnnotations() throws Exception {

        // Create test data
        final long imageId = iUpdate.saveAndReturnObject(mmFactory.createImage()).getId().getValue();
        ImageAnnotationLink link = new ImageAnnotationLinkI();
        link.link(new ImageI(imageId, false), new TagAnnotationI());
        link = (ImageAnnotationLink) iUpdate.saveAndReturnObject(link);
        final long annId = link.getChild().getId().getValue();

        // Perform delete
        final Delete2 dc = Requests.delete().target("Image").id(imageId).build();
        callback(true, client, dc);

        // Check that the annotation is gone
        List<?> ids = iQuery.projection(
                "select ann.id from Annotation ann where ann.id = :id",
                new ParametersI().addId(annId));
        Assert.assertTrue(ids.isEmpty());
    }

    /**
     * Uses the /Image delete specification to remove an Image and attempts to
     * remove its annotations. If those annotations are multiply linked,
     * however, the attempted delete is rolled back (via a savepoint)
     *
     * As of 4.4.2, only a warning is returned for the annotationlink_child_annotation fk.
     */
    @Test(groups = {"ticket:2769", "ticket:2780"})
    public void testImageWithSharedAnnotations() throws Exception {

        // Create test data
        final long imageId1 = iUpdate.saveAndReturnObject(mmFactory.createImage()).getId().getValue();
        final long imageId2 = iUpdate.saveAndReturnObject(mmFactory.createImage()).getId().getValue();

        TagAnnotation tag = new TagAnnotationI();
        tag = (TagAnnotation) iUpdate.saveAndReturnObject(tag);

        ImageAnnotationLink link1 = new ImageAnnotationLinkI();
        link1.link(new ImageI(imageId1, false), tag);
        link1 = (ImageAnnotationLink) iUpdate.saveAndReturnObject(link1);

        ImageAnnotationLink link2 = new ImageAnnotationLinkI();
        link2.link(new ImageI(imageId2, false), tag);
        link2 = (ImageAnnotationLink) iUpdate.saveAndReturnObject(link2);

        // Perform delete
        final Delete2 dc = Requests.delete().target("Image").id(imageId1).build();
        callback(true, client, dc);

        // Check that data is gone
        List<?> ids = iQuery.projection(
                "select img.id from Image img where img.id = :id",
                new ParametersI().addId(imageId1));

        Assert.assertTrue(ids.isEmpty());

        // Check that the annotation remains
        ids = iQuery.projection(
                "select ann.id from Annotation ann where ann.id = :id",
                new ParametersI().addId(tag.getId().getValue()));

        Assert.assertFalse(ids.isEmpty());
    }

    /**
     * Deletes a project and all its datasets though no images are created.
     */
    public void testProjectNoImage() throws Exception {

        // Create test data
        Project p = new ProjectI();
        p.setName(ode.rtypes.rstring("name"));
        Dataset d = new DatasetI();
        d.setName(p.getName());

        p.linkDataset(d);
        p = (Project) iUpdate.saveAndReturnObject(p);
        final long id = p.getId().getValue();

        // Do Delete
        final Delete2 dc = Requests.delete().target("Project").id(id).build();
        callback(true, client, dc);

        // Check that data is gone
        List<?> ids;
        ids = iQuery.projection("select p.id from Project p where p.id = " + id,
                null);
        Assert.assertTrue(ids.isEmpty());
        ids = iQuery.projection("select d.id from Dataset d where d.id = " + id,
                null);
        Assert.assertTrue(ids.isEmpty());
    }

    /**
     * Deletes a project and all its datasets which have images.
     */
    public void testProject() throws Exception {

        final long iid = iUpdate.saveAndReturnObject(mmFactory.createImage()).getId().getValue();

        // Create test data
        Project p = new ProjectI();
        p.setName(ode.rtypes.rstring("name"));
        Dataset d = new DatasetI();
        d.setName(p.getName());

        p.linkDataset(d);
        d.linkImage(new ImageI(iid, false));
        p = (Project) iUpdate.saveAndReturnObject(p);
        d = p.linkedDatasetList().get(0);
        final long pid = p.getId().getValue();
        final long did = d.getId().getValue();

        // Do Delete
        final Delete2 dc = Requests.delete().target("Project").id(pid).build();
        callback(true, client, dc);

        // Check that data is gone
        List<?> ids;
        ids = iQuery.projection("select p.id from Project p where p.id = " + pid,
                null);
        Assert.assertTrue(ids.isEmpty());
        ids = iQuery.projection("select d.id from Dataset d where d.id = " + did,
                null);
        Assert.assertTrue(ids.isEmpty());
        ids = iQuery.projection("select i.id from Image i where i.id = " + iid,
                null);
        Assert.assertTrue(ids.isEmpty());
    }

    /**
     * Deletes a very simple plate to ensure that the "/Image+WS" spec is
     * working.
     */
    public void testSimplePlate() throws Exception {

        final long iid = iUpdate.saveAndReturnObject(mmFactory.createImage()).getId().getValue();

        // Create test data
        Plate p = createPlate(iid);
        p = (Plate) iUpdate.saveAndReturnObject(p);

        final long pid = p.getId().getValue();

        Well w = p.copyWells().get(0);
        final long wid = w.getId().getValue();

        WellSample ws = w.getWellSample(0);
        final long wsid = ws.getId().getValue();

        // Do Delete
        final Delete2 dc = Requests.delete().target("Plate").id(pid).build();
        callback(true, client, dc);

        // Check that data is gone
        List<?> ids;
        ids = iQuery.projection("select p.id from Plate p where p.id = " + pid,
                null);
        Assert.assertTrue(ids.isEmpty());
        ids = iQuery.projection("select w.id from Well w where w.id = " + wid,
                null);
        Assert.assertTrue(ids.isEmpty());
        ids = iQuery.projection("select ws.id from WellSample ws where ws.id = "
                + wsid, null);
        Assert.assertTrue(ids.isEmpty());
        ids = iQuery.projection("select i.id from Image i where i.id = " + iid,
                null);
        Assert.assertTrue(ids.isEmpty());
    }

    /**
     * Deletes a very simple image/annotation graph, to guarantee that the
     * basic options are working
     */
    public void testSimpleImageWithAnnotation() throws Exception {

        final long iid = iUpdate.saveAndReturnObject(mmFactory.createImage()).getId().getValue();

        // Create test data
        ImageAnnotationLink link = new ImageAnnotationLinkI();
        link.link(new ImageI(iid, false), new TagAnnotationI());
        link = (ImageAnnotationLink) iUpdate.saveAndReturnObject(link);

        final long lid = link.getId().getValue();
        final long aid = link.getChild().getId().getValue();

        // Do Delete
        final Delete2 dc = Requests.delete().target("Image").id(iid).build();
        callback(true, client, dc);

        // Check that data is gone
        List<?> ids;
        ids = iQuery.projection("select i.id from Image i where i.id = " + iid,
                null);
        Assert.assertTrue(ids.isEmpty());
        ids = iQuery.projection("select l.id from ImageAnnotationLink l where l.id = " + lid,
                null);
        Assert.assertTrue(ids.isEmpty());
        ids = iQuery.projection("select a.id from Annotation a where a.id = "
                + aid, null);
        Assert.assertTrue(ids.isEmpty());
    }

    /**
     * Attempts to use the ILink type for deleting all links which point
     * at an annotation.
     */
    public void testDeleteAllAnnotationLinks() throws Exception {

        // Create test data
        AnnotationAnnotationLink link = new AnnotationAnnotationLinkI();
        link.link(new TagAnnotationI(), new TagAnnotationI());
        link = (AnnotationAnnotationLink) iUpdate.saveAndReturnObject(link);

        final long lid = link.getId().getValue();
        final long pid = link.getParent().getId().getValue();
        final long cid = link.getChild().getId().getValue();

        // Do Delete
        final Delete2 dc = Requests.delete().target("Annotation").id(cid).build();
        callback(true, client, dc);

        // Make sure the parent annotation still exists, but both the annotation
        // link and the annotation that was linked to (the child) are gone.
        List<?> ids;
        ids = iQuery.projection("select p.id from Annotation p where p.id = " + pid,
                null);
        Assert.assertFalse(ids.isEmpty());
        ids = iQuery.projection("select l.id from AnnotationAnnotationLink l where l.id = " + lid,
                null);
        Assert.assertTrue(ids.isEmpty());
        ids = iQuery.projection("select c.id from Annotation c where c.id = "
                + cid, null);
        Assert.assertTrue(ids.isEmpty());
    }

    /**
     * Uses {@link SkipHead} to prevent a delete from happening.
     */
    public void testKeepImageAnnotation() throws Exception {

        // Create test data
        final long iid = iUpdate.saveAndReturnObject(mmFactory.createImage()).getId().getValue();
        ImageAnnotationLink link = new ImageAnnotationLinkI();
        link.link(new ImageI(iid, false), new TagAnnotationI());
        link = (ImageAnnotationLink) iUpdate.saveAndReturnObject(link);

        final long lid = link.getId().getValue();
        final long pid = link.getParent().getId().getValue();
        final long cid = link.getChild().getId().getValue();

        // Do Delete
        final ChildOption option = Requests.option().excludeType("TagAnnotation").build();
        final Delete2 dc = Requests.delete().target("Image").id(pid).option(option).build();
        callback(true, client, dc);

        // Make sure the image is deleted but the annotation remains.
        List<?> ids;
        ids = iQuery.projection("select p.id from Image p where p.id = " + pid,
                null);
        Assert.assertTrue(ids.isEmpty());
        ids = iQuery.projection("select l.id from ImageAnnotationLink l where l.id = " + lid,
                null);
        Assert.assertTrue(ids.isEmpty());
        ids = iQuery.projection("select c.id from Annotation c where c.id = "
                + cid, null);
        Assert.assertFalse(ids.isEmpty());
    }

    /**
     * Tests overriding the {@link ChildOption#excludeType} setting by a hard-code
     * value to the graph request factory. These are well-known "unshared" annotations,
     * that should be deleted, regardless of the setting.
     */
    public void testDontKeepImageAnnotationIfUnsharedNS() throws Exception {

        // Create test data
        FileAnnotation file = new FileAnnotationI();
        file.setNs(ode.rtypes.rstring("bhojpur.net/ode/import/companionFile"));

        final long iid = iUpdate.saveAndReturnObject(mmFactory.createImage()).getId().getValue();
        ImageAnnotationLink link = new ImageAnnotationLinkI();
        link.link(new ImageI(iid, false), file);
        link = (ImageAnnotationLink) iUpdate.saveAndReturnObject(link);

        final long lid = link.getId().getValue();
        final long pid = link.getParent().getId().getValue();
        final long cid = link.getChild().getId().getValue();

        // Do Delete
        final ChildOption option = Requests.option().excludeType("FileAnnotation").build();
        final Delete2 dc = Requests.delete().target("Image").id(pid).option(option).build();
        callback(true, client, dc);

        // Make sure the image and annotation are deleted.
        List<?> ids;
        ids = iQuery.projection("select p.id from Image p where p.id = " + pid,
                null);
        Assert.assertTrue(ids.isEmpty());
        ids = iQuery.projection("select l.id from ImageAnnotationLink l where l.id = " + lid,
                null);
        Assert.assertTrue(ids.isEmpty());
        ids = iQuery.projection("select c.id from Annotation c where c.id = "
                + cid, null);
        Assert.assertTrue(ids.isEmpty());
    }

    /**
     * Tests overriding the {@link ChildOption#excludeType} setting by setting
     * a namespace which should always be deleted (an "unshared" annotation).
     */
    public void testDontKeepImageAnnotationIfRequestedNS() throws Exception {

        // Create test data
        FileAnnotation file = new FileAnnotationI();
        file.setNs(ode.rtypes.rstring("keepme"));

        final long iid = iUpdate.saveAndReturnObject(mmFactory.createImage()).getId().getValue();
        ImageAnnotationLink link = new ImageAnnotationLinkI();
        link.link(new ImageI(iid, false), file);
        link = (ImageAnnotationLink) iUpdate.saveAndReturnObject(link);

        long lid = link.getId().getValue();
        long pid = link.getParent().getId().getValue();
        long cid = link.getChild().getId().getValue();

        // Do Delete
        final ChildOption option = Requests.option().excludeType("FileAnnotation").excludeNs("keepme").build();
        final Delete2 dc = Requests.delete().target("Image").id(pid).option(option).build();
        callback(true, client, dc);

        // Make sure the image and annotation are deleted.
        List<?> ids;
        ids = iQuery.projection("select p.id from Image p where p.id = " + pid,
                null);
        Assert.assertTrue(ids.isEmpty());
        ids = iQuery.projection("select l.id from ImageAnnotationLink l where l.id = " + lid,
                null);
        Assert.assertTrue(ids.isEmpty());
        ids = iQuery.projection("select c.id from Annotation c where c.id = "
                + cid, null);
        Assert.assertTrue(ids.isEmpty());
    }

    /**
     * This method is copied from DeleteServiceTest to reproduce an issue
     * in which KEEP;excludes= was not being taken into account.
     */
    @Test
    public void testDeleteObjectWithAnnotationWithoutNS()
        throws Exception
    {
        Screen obj = new ScreenI();
        obj.setName(ode.rtypes.rstring("testDelete"));
        obj = (Screen) iUpdate.saveAndReturnObject(obj);
        String type = "Screen";
        final long id = obj.getId().getValue();

        List<Long> annotationIds = createNonSharableAnnotation(obj, null);
        List<Long> annotationIdsNS = createNonSharableAnnotation(obj, "TEST");

        final ChildOption option = Requests.option().excludeType("Annotation").excludeNs("TEST").build();
        final Delete2 dc = Requests.delete().target(type).id(id).option(option).build();
        callback(true, client, dc);

        ParametersI param = new ParametersI();
        param.addId(obj.getId().getValue());
        String hql = "select s from Screen s where id = :id";
        Assert.assertTrue(iQuery.projection(hql, param).isEmpty());
        param = new ParametersI();
        param.addIds(annotationIds);
        Assert.assertFalse(annotationIds.isEmpty());

        hql = "select id from Annotation where id in (:ids)";
        List<?> ids = iQuery.projection(hql, param);
        Assert.assertEquals(annotationIds.size(), ids.size());
        param = new ParametersI();
        param.addIds(annotationIdsNS);
        Assert.assertFalse(annotationIdsNS.isEmpty());
        ids = iQuery.projection(hql, param);
        Assert.assertTrue(ids.isEmpty());
    }

    private List<Long> createNonSharableAnnotation(Screen obj, String ns) throws Exception {
        TermAnnotation ta = new TermAnnotationI();
        if (ns != null) {
            ta.setNs(ode.rtypes.rstring(ns));
        }
        ScreenAnnotationLink link = new ScreenAnnotationLinkI();
        link.link((Screen) obj.proxy(), ta);
        link = (ScreenAnnotationLink) iUpdate.saveAndReturnObject(link);
        return Arrays.asList(link.getChild().getId().getValue());
    }

    // original files
    //

    @Test(groups = "ticket:7314")
    public void testOriginalFileAnnotation() throws Exception {
        final FileAnnotationI ann = mockAnnotation();
        final OriginalFile file = ann.getFile();

        // Do Delete
        final Delete2 dc = Requests.delete().target(file).build();
        callback(true, client, dc);

        assertGone(file);
        assertGone(ann);
    }

    /**
     * This is not possible without nulling the FileAnnotation.file field.
     * So, the FileAnnotation must be deleted if its file is.
     */
    @Test(groups = "ticket:7314")
    public void testOriginalFileAnnotationWithKeep() throws Exception {
        final FileAnnotationI ann = mockAnnotation();
        final OriginalFile file = ann.getFile();

        // Do Delete
        final ChildOption option = Requests.option().excludeType("Annotation").build();
        final Delete2 dc = Requests.delete().target(file).option(option).build();
        callback(true, client, dc);

        assertGone(ann);
        assertGone(file);

    }

    /**
     * Test that deleting an project also deletes its external information.
     * @throws Exception unexpected
     */
    @Test(groups = "ticket:13176")
    public void testDeleteExternalInfoWithProject() throws Exception {
        ExternalInfo info = new ExternalInfoI();
        info.setEntityType(ode.rtypes.rstring("ExternalEntity"));
        info.setEntityId(ode.rtypes.rlong(0));
        info.setUuid(ode.rtypes.rstring(UUID.randomUUID().toString()));
        info = (ExternalInfo) iUpdate.saveAndReturnObject(info).proxy();

        IObject object = mmFactory.simpleProject();
        object.getDetails().setExternalInfo(info);
        object = iUpdate.saveAndReturnObject(object).proxy();

        doChange(Requests.delete().target(object).build());

        assertGone(object);
        assertGone(info);
    }

    /**
     * Check that a shared ROI transform is not deleted prematurely.
     * @throws Exception unexpected
     */
    @Test
    public void testDeleteTransformedRoi() throws Exception {
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

        /* delete first ROI */
        doChange(Requests.delete().target(roi1).build());

        /* check what remains */
        assertDoesNotExist(roi1);
        assertExists(roi2);
        assertDoesNotExist(point1);
        assertExists(point2);
        assertExists(transformation);  // needed by second ROI

        /* delete second ROI */
        doChange(Requests.delete().target(roi2).build());

        /* check what remains */
        assertDoesNotExist(roi2);
        assertDoesNotExist(point2);
        assertDoesNotExist(transformation);  // no longer needed
    }

    private FileAnnotationI mockAnnotation()
        throws Exception
    {
        OriginalFile file = (OriginalFileI) new IceMapper().map(ObjectFactory.createFile());
        FileAnnotationI ann = new FileAnnotationI();
        ann.setFile(file);
        ann = (FileAnnotationI) iUpdate.saveAndReturnObject(ann);
        return ann;
    }

    private void assertGone(IObject obj) throws Exception {
        final IObject test = assertLoadObject(obj);
        Assert.assertNull(test);
    }

    private IObject assertLoadObject(IObject obj)
        throws ApiUsageException, Exception
    {
        final String kls = IceMapper.odeClass(obj.getClass().getName(), true).getSimpleName();
        final List<List<RType>> objects = iQuery.projection(
            "select x from " + kls + " x where x.id = " +
            obj.getId().getValue(), null);
        if (objects.isEmpty()) {
            return null;
        } else {
            final RObject object = (RObject) objects.get(0).get(0);
            return object.getValue();
        }
    }

    //
    // Helpers
    //

    private long importImage() throws Throwable {
        String name = "testDV&pixelType=int16&sizeX=20&sizeY=20&sizeZ=5&sizeT=6&sizeC=1.fake";
        final File imageFile = new File(System.getProperty("java.io.tmpdir"), name);
        imageFile.deleteOnExit();
        imageFile.createNewFile();
        final Pixels pixels = importFile(imageFile, "fake").get(0);
        return pixels.getImage().getId().getValue();
    }

    private Plate createPlate(long imageId) throws Exception {
        Plate p = new PlateI();
        p.setRows(ode.rtypes.rint(1));
        p.setColumns(ode.rtypes.rint(1));
        p.setName(ode.rtypes.rstring("plate"));
        // now make wells
        Well well = new WellI();
        well.setRow(ode.rtypes.rint(0));
        well.setColumn(ode.rtypes.rint(0));
        WellSample sample = new WellSampleI();
        sample.setImage(new ImageI(imageId, false));
        well.addWellSample(sample);
        p.addWell(well);
        return p;
    }
}
