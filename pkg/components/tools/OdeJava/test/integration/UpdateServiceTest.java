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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ode.services.scripts.ScriptRepoHelper;
import ode.ServerError;
import ode.ValidationException;
import ode.api.IQueryPrx;
import ode.api.IUpdatePrx;
import ode.cmd.Delete2;
import ode.cmd.Request;
import ode.gateway.util.Requests;
import ode.model.Annotation;
import ode.model.AnnotationAnnotationLinkI;
import ode.model.BooleanAnnotation;
import ode.model.BooleanAnnotationI;
import ode.model.Channel;
import ode.model.CommentAnnotation;
import ode.model.CommentAnnotationI;
import ode.model.Dataset;
import ode.model.DatasetAnnotationLink;
import ode.model.DatasetAnnotationLinkI;
import ode.model.DatasetI;
import ode.model.DatasetImageLink;
import ode.model.DatasetImageLinkI;
import ode.model.Detector;
import ode.model.Dichroic;
import ode.model.EllipseI;
import ode.model.ExternalInfo;
import ode.model.ExternalInfoI;
import ode.model.FileAnnotation;
import ode.model.FileAnnotationI;
import ode.model.Filter;
import ode.model.Folder;
import ode.model.IObject;
import ode.model.Image;
import ode.model.ImageAnnotationLink;
import ode.model.ImageAnnotationLinkI;
import ode.model.ImageI;
import ode.model.Instrument;
import ode.model.Laser;
import ode.model.Line;
import ode.model.LineI;
import ode.model.LongAnnotation;
import ode.model.LongAnnotationI;
import ode.model.Mask;
import ode.model.MaskI;
import ode.model.Objective;
import ode.model.OriginalFile;
import ode.model.Pixels;
import ode.model.PlaneInfo;
import ode.model.Plate;
import ode.model.PlateAcquisition;
import ode.model.PlateAcquisitionAnnotationLink;
import ode.model.PlateAcquisitionAnnotationLinkI;
import ode.model.PlateAnnotationLink;
import ode.model.PlateAnnotationLinkI;
import ode.model.PlateI;
import ode.model.Point;
import ode.model.PointI;
import ode.model.Polygon;
import ode.model.PolygonI;
import ode.model.Polyline;
import ode.model.PolylineI;
import ode.model.Project;
import ode.model.ProjectAnnotationLink;
import ode.model.ProjectAnnotationLinkI;
import ode.model.ProjectDatasetLink;
import ode.model.ProjectDatasetLinkI;
import ode.model.ProjectI;
import ode.model.Reagent;
import ode.model.Rectangle;
import ode.model.RectangleI;
import ode.model.Roi;
import ode.model.RoiAnnotationLink;
import ode.model.RoiAnnotationLinkI;
import ode.model.RoiI;
import ode.model.Screen;
import ode.model.ScreenAnnotationLink;
import ode.model.ScreenAnnotationLinkI;
import ode.model.ScreenI;
import ode.model.ScreenPlateLink;
import ode.model.Session;
import ode.model.Shape;
import ode.model.ShapeAnnotationLink;
import ode.model.ShapeAnnotationLinkI;
import ode.model.TagAnnotation;
import ode.model.TagAnnotationI;
import ode.model.TermAnnotation;
import ode.model.TermAnnotationI;
import ode.model.XmlAnnotation;
import ode.model.XmlAnnotationI;
import ode.sys.Parameters;
import ode.sys.ParametersI;

import org.apache.commons.collections.CollectionUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import ode.gateway.model.BooleanAnnotationData;
import ode.gateway.model.DatasetData;
import ode.gateway.model.EllipseData;
import ode.gateway.model.ImageData;
import ode.gateway.model.LineData;
import ode.gateway.model.LongAnnotationData;
import ode.gateway.model.MaskData;
import ode.gateway.model.PlateData;
import ode.gateway.model.PointData;
import ode.gateway.model.PolygonData;
import ode.gateway.model.PolylineData;
import ode.gateway.model.ProjectData;
import ode.gateway.model.ROIData;
import ode.gateway.model.RectangleData;
import ode.gateway.model.ScreenData;
import ode.gateway.model.ShapeData;
import ode.gateway.model.TagAnnotationData;
import ode.gateway.model.TermAnnotationData;
import ode.gateway.model.TextualAnnotationData;
import ode.gateway.model.XMLAnnotationData;
import ode.grid.ManagedRepositoryPrx;
import ode.grid.RepositoryMap;
import ode.grid.RepositoryPrx;

/**
 * Collections of tests for the <code>IUpdate</code> service.
 */
public class UpdateServiceTest extends AbstractServerTest {

    /**
     * Test to create an image and make sure the version is correct.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testVersionHandling() throws Exception {
        Image img = mmFactory.simpleImage();
        img.setName(ode.rtypes.rstring("version handling"));
        Image sent = (Image) iUpdate.saveAndReturnObject(img);
        long version = sent.getDetails().getUpdateEvent().getId().getValue();

        sent.setDescription(ode.rtypes.rstring("version handling update"));
        // Update event should be created
        Image sent2 = (Image) iUpdate.saveAndReturnObject(sent);
        long version2 = sent2.getDetails().getUpdateEvent().getId().getValue();
        Assert.assertNotEquals(version, version2);
    }

    /**
     * Test to make sure that an update event is created for an object after
     * updating an annotation linked to the image.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:118")
    public void testVersionNotIncreasingAfterUpdate() throws Exception {
        CommentAnnotation ann = new CommentAnnotationI();
        Image img = mmFactory.simpleImage();
        img.setName(ode.rtypes.rstring("version_test"));
        img = (Image) iUpdate.saveAndReturnObject(img);

        ann.setTextValue(ode.rtypes.rstring("version_test"));
        img.linkAnnotation(ann);

        img = (Image) iUpdate.saveAndReturnObject(img);
        ann = (CommentAnnotation) img.linkedAnnotationList().get(0);
        Assert.assertNotNull(img.getId());
        Assert.assertNotNull(ann.getId());
        long oldId = img.getDetails().getUpdateEvent().getId().getValue();
        ann.setTextValue(ode.rtypes.rstring("updated version_test"));
        ann = (CommentAnnotation) iUpdate.saveAndReturnObject(ann);
        img = (Image) iQuery.get(Image.class.getName(), img.getId().getValue());

        long newId = img.getDetails().getUpdateEvent().getId().getValue();
        Assert.assertEquals(newId, oldId);
    }

    /**
     * Test to make sure that an update event is not created when when invoking
     * the <code>SaveAndReturnObject</code> on an unmodified Object.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:118")
    public void testVersionNotIncreasingOnUnmodifiedObject() throws Exception {
        Image img = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());
        Assert.assertNotNull(img.getDetails().getUpdateEvent());
        long id = img.getDetails().getUpdateEvent().getId().getValue();
        Image test = (Image) iUpdate.saveAndReturnObject(img);
        Assert.assertNotNull(test.getDetails().getUpdateEvent());
        Assert.assertEquals(id, test.getDetails().getUpdateEvent().getId().getValue());
    }

    /**
     * Tests the creation of a project without datasets.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:1106")
    public void testEmptyProject() throws Exception {
        Project p = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());
        Assert.assertNotNull(p);
        ProjectData pd = new ProjectData(p);
        Assert.assertTrue(p.getId().getValue() > 0);
        Assert.assertEquals(p.getId().getValue(), pd.getId());
        Assert.assertEquals(p.getName().getValue(), pd.getName());
        Assert.assertEquals(p.getDescription().getValue(), pd.getDescription());
    }

    /**
     * Tests the creation of a dataset.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:1106")
    public void testEmptyDataset() throws Exception {
        Dataset p = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        Assert.assertNotNull(p);
        DatasetData d = new DatasetData(p);
        Assert.assertTrue(p.getId().getValue() > 0);
        Assert.assertEquals(p.getId().getValue(), d.getId());
        Assert.assertEquals(p.getName().getValue(), d.getName());
        Assert.assertEquals(p.getDescription().getValue(), d.getDescription());
    }

    /**
     * Tests the creation of a dataset.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:1106")
    public void testEmptyImage() throws Exception {
        Image p = (Image) iUpdate.saveAndReturnObject(mmFactory.simpleImage());
        ImageData img = new ImageData(p);
        Assert.assertNotNull(p);
        Assert.assertTrue(p.getId().getValue() > 0);
        Assert.assertEquals(p.getId().getValue(), img.getId());
        Assert.assertEquals(p.getName().getValue(), img.getName());
        Assert.assertEquals(p.getDescription().getValue(), img.getDescription());
    }

    /**
     * Tests the creation of an image with a set of pixels.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateImageWithPixels() throws Exception {
        Image img = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());
        Assert.assertNotNull(img);
        Pixels pixels = mmFactory.createPixels();
        img.addPixels(pixels);
        img = (Image) iUpdate.saveAndReturnObject(img);

        ParametersI param = new ParametersI();
        param.addId(img.getId().getValue());

        StringBuilder sb = new StringBuilder();
        sb.append("select i from Image i ");
        sb.append("left outer join fetch i.pixels as pix ");
        sb.append("left outer join fetch pix.pixelsType as pt ");
        sb.append("where i.id = :id");
        img = (Image) iQuery.findByQuery(sb.toString(), param);
        Assert.assertNotNull(img);
        // Make sure we have a pixels set.
        pixels = img.getPixels(0);
        Assert.assertNotNull(pixels);
    }

    /**
     * Tests the creation of a screen.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:1106")
    public void testEmptyScreen() throws Exception {
        Screen p = (Screen) factory.getUpdateService().saveAndReturnObject(
                mmFactory.simpleScreenData().asIObject());
        ScreenData data = new ScreenData(p);
        Assert.assertNotNull(p);
        Assert.assertTrue(p.getId().getValue() > 0);
        Assert.assertEquals(p.getId().getValue(), data.getId());
        Assert.assertEquals(p.getName().getValue(), data.getName());
        Assert.assertEquals(p.getDescription().getValue(), data.getDescription());
    }

    /**
     * Tests the creation of a screen.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testEmptyPlate() throws Exception {
        Plate p = (Plate) factory.getUpdateService().saveAndReturnObject(
                mmFactory.simplePlateData().asIObject());
        PlateData data = new PlateData(p);
        Assert.assertNotNull(p);
        Assert.assertTrue(p.getId().getValue() > 0);
        Assert.assertEquals(p.getId().getValue(), data.getId());
        Assert.assertEquals(p.getName().getValue(), data.getName());
        Assert.assertEquals(p.getDescription().getValue(), data.getDescription());
    }

    /**
     * Tests the creation of a plate with wells, wells sample and plate
     * acquisition.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testPopulatedPlate() throws Exception {
        Plate p = mmFactory.createPlate(1, 1, 1, 1, false);
        p = (Plate) iUpdate.saveAndReturnObject(p);
        Assert.assertNotNull(p);
        Assert.assertNotNull(p.getName().getValue());
        Assert.assertNotNull(p.getStatus().getValue());
        Assert.assertNotNull(p.getDescription().getValue());
        Assert.assertNotNull(p.getExternalIdentifier().getValue());
        String sql = "select l from PlateAcquisition as l ";
        sql += "join fetch l.plate as p ";
        sql += "where p.id = :id";
        ParametersI param = new ParametersI();
        param.addId(p.getId());
        Assert.assertNotNull(iQuery.findByQuery(sql, param));

        p = mmFactory.createPlate(1, 1, 1, 0, false);
        p = (Plate) iUpdate.saveAndReturnObject(p);
        Assert.assertNotNull(p);
        p = mmFactory.createPlate(1, 1, 1, 1, true);
        p = (Plate) iUpdate.saveAndReturnObject(p);
        Assert.assertNotNull(p);
    }

    /**
     * Test to create a project and link datasets to it.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateProjectAndLinkDatasets() throws Exception {
        String name = " 2&1 " + System.currentTimeMillis();
        Project p = new ProjectI();
        p.setName(ode.rtypes.rstring(name));

        p = (Project) iUpdate.saveAndReturnObject(p);

        Dataset d1 = new DatasetI();
        d1.setName(ode.rtypes.rstring(name));
        d1 = (Dataset) iUpdate.saveAndReturnObject(d1);

        Dataset d2 = new DatasetI();
        d2.setName(ode.rtypes.rstring(name));
        d2 = (Dataset) iUpdate.saveAndReturnObject(d2);

        List<IObject> links = new ArrayList<IObject>();
        ProjectDatasetLink link = new ProjectDatasetLinkI();
        link.setParent(p);
        link.setChild(d1);
        links.add(link);
        link = new ProjectDatasetLinkI();
        link.setParent(p);
        link.setChild(d2);
        links.add(link);
        // links dataset and project.
        iUpdate.saveAndReturnArray(links);

        // load the project
        ParametersI param = new ParametersI();
        param.addId(p.getId());

        StringBuilder sb = new StringBuilder();
        sb.append("select p from Project p ");
        sb.append("left outer join fetch p.datasetLinks pdl ");
        sb.append("left outer join fetch pdl.child ds ");
        sb.append("where p.id = :id");
        p = (Project) iQuery.findByQuery(sb.toString(), param);

        // Check the conversion of Project to ProjectData
        ProjectData pData = new ProjectData(p);
        Set<DatasetData> datasets = pData.getDatasets();
        // We should have 2 datasets
        Assert.assertEquals(datasets.size(), 2);
        int count = 0;
        Iterator<DatasetData> i = datasets.iterator();
        DatasetData dataset;
        while (i.hasNext()) {
            dataset = i.next();
            if (dataset.getId() == d1.getId().getValue()
                    || dataset.getId() == d2.getId().getValue())
                count++;
        }
        Assert.assertEquals(count, 2);
    }

    /**
     * Test to create a dataset and link images to it.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateDatasetAndLinkImages() throws Exception {
        String name = " 2&1 " + System.currentTimeMillis();
        Dataset p = new DatasetI();
        p.setName(ode.rtypes.rstring(name));

        p = (Dataset) iUpdate.saveAndReturnObject(p);

        Image d1 = new ImageI();
        d1.setName(ode.rtypes.rstring(name));
        d1 = (Image) iUpdate.saveAndReturnObject(d1);

        Image d2 = new ImageI();
        d2.setName(ode.rtypes.rstring(name));
        d2 = (Image) iUpdate.saveAndReturnObject(d2);

        List<IObject> links = new ArrayList<IObject>();
        DatasetImageLink link = new DatasetImageLinkI();
        link.setParent(p);
        link.setChild(d1);
        links.add(link);
        link = new DatasetImageLinkI();
        link.setParent(p);
        link.setChild(d2);
        links.add(link);
        // links dataset and project.
        iUpdate.saveAndReturnArray(links);

        // load the project
        ParametersI param = new ParametersI();
        param.addId(p.getId());

        StringBuilder sb = new StringBuilder();
        sb.append("select p from Dataset p ");
        sb.append("left outer join fetch p.imageLinks pdl ");
        sb.append("left outer join fetch pdl.child ds ");
        sb.append("where p.id = :id");
        p = (Dataset) iQuery.findByQuery(sb.toString(), param);

        // Check the conversion of Project to ProjectData
        DatasetData pData = new DatasetData(p);
        Set<ImageData> images = pData.getImages();
        // We should have 2 datasets
        Assert.assertEquals(images.size(), 2);
        int count = 0;
        Iterator<ImageData> i = images.iterator();
        ImageData image;
        while (i.hasNext()) {
            image = i.next();
            if (image.getId() == d1.getId().getValue()
                    || image.getId() == d2.getId().getValue())
                count++;
        }
        Assert.assertEquals(count, 2);
    }

    /**
     * Test that folder hierarchies must be a strict tree.
     * @throws ServerError unexpected
     */
    @Test
    public void testCreateCyclicFolders() throws ServerError {
        Folder α = saveAndReturnFolder(mmFactory.simpleFolder());
        Folder β = saveAndReturnFolder(mmFactory.simpleFolder());
        Folder γ = saveAndReturnFolder(mmFactory.simpleFolder());
        Folder δ = saveAndReturnFolder(mmFactory.simpleFolder());

        γ.setParentFolder(β);
        γ = saveAndReturnFolder(γ);

        δ.setParentFolder(γ);
        δ = saveAndReturnFolder(δ);

        /* at this point δ ← γ ← β */

        /* only α may be β's parent */

        for (Folder parent : Arrays.asList(δ, γ, β, α)) {
            β = returnFolder(β);
            β.setParentFolder(returnFolder(parent));
            try {
                iUpdate.saveObject(β);
                Assert.assertEquals(parent, α);
            } catch (ServerError e) {
                Assert.assertNotEquals(parent, α);
            }
        }

        /* at this point δ ← γ ← β ← α */

        α = returnFolder(α);
        β = returnFolder(β);
        γ = returnFolder(γ);
        δ = returnFolder(δ);

        Assert.assertNull(α.getParentFolder());
        Assert.assertEquals(β.getParentFolder().getId().getValue(), α.getId().getValue());
        Assert.assertEquals(γ.getParentFolder().getId().getValue(), β.getId().getValue());
        Assert.assertEquals(δ.getParentFolder().getId().getValue(), γ.getId().getValue());
    }

    /**
     * Test that parentage of a folder can be changed by {@link Folder#setParentFolder(Folder)}.
     * @throws ServerError unexpected
     */
    @Test
    public void testChangeFolderParentBySetParent() throws ServerError {
        Folder child = saveAndReturnFolder(mmFactory.simpleFolder());
        Folder oldParent = saveAndReturnFolder(mmFactory.simpleFolder());
        Folder newParent = saveAndReturnFolder(mmFactory.simpleFolder());
        List<Folder> oldParentChildren, newParentChildren;

        /* check that all is well with old parent */

        child.setParentFolder(oldParent);

        child = saveAndReturnFolder(child);
        oldParent = returnFolder(oldParent);
        newParent = returnFolder(newParent);
        oldParentChildren = oldParent.copyChildFolders();
        newParentChildren = newParent.copyChildFolders();

        Assert.assertEquals(1, oldParentChildren.size());
        Assert.assertEquals(0, newParentChildren.size());
        Assert.assertEquals(child.getParentFolder().getId().getValue(), oldParent.getId().getValue());
        Assert.assertEquals(oldParentChildren.get(0).getId().getValue(), child.getId().getValue());

        /* change parentage */

        child.setParentFolder(newParent);

        /* check that all is well with new parent */

        child = saveAndReturnFolder(child);
        oldParent = returnFolder(oldParent);
        newParent = returnFolder(newParent);
        oldParentChildren = oldParent.copyChildFolders();
        newParentChildren = newParent.copyChildFolders();

        Assert.assertEquals(0, oldParentChildren.size());
        Assert.assertEquals(1, newParentChildren.size());
        Assert.assertEquals(child.getParentFolder().getId().getValue(), newParent.getId().getValue());
        Assert.assertEquals(newParentChildren.get(0).getId().getValue(), child.getId().getValue());
    }

    /**
     * Test that parentage of a folder can be changed by {@link Folder#addChildFolders(Folder)}.
     * @throws ServerError unexpected
     */
    @Test
    public void testChangeFolderParentByAddChildFolders() throws ServerError {
        Folder child = saveAndReturnFolder(mmFactory.simpleFolder());
        Folder oldParent = saveAndReturnFolder(mmFactory.simpleFolder());
        Folder newParent = saveAndReturnFolder(mmFactory.simpleFolder());
        List<Folder> oldParentChildren, newParentChildren;

        /* check that all is well with old parent */

        child.setParentFolder(oldParent);

        child = saveAndReturnFolder(child);
        oldParent = returnFolder(oldParent);
        newParent = returnFolder(newParent);
        oldParentChildren = oldParent.copyChildFolders();
        newParentChildren = newParent.copyChildFolders();

        Assert.assertEquals(1, oldParentChildren.size());
        Assert.assertEquals(0, newParentChildren.size());
        Assert.assertEquals(child.getParentFolder().getId().getValue(), oldParent.getId().getValue());
        Assert.assertEquals(oldParentChildren.get(0).getId().getValue(), child.getId().getValue());

        /* change parentage */

        newParent.addChildFolders(child);

        /* check that all is well with new parent */

        child = saveAndReturnFolder(child);
        oldParent = returnFolder(oldParent);
        newParent = returnFolder(newParent);
        oldParentChildren = oldParent.copyChildFolders();
        newParentChildren = newParent.copyChildFolders();

        Assert.assertEquals(0, oldParentChildren.size());
        Assert.assertEquals(1, newParentChildren.size());
        Assert.assertEquals(child.getParentFolder().getId().getValue(), newParent.getId().getValue());
        Assert.assertEquals(newParentChildren.get(0).getId().getValue(), child.getId().getValue());
    }

    // Annotation section

    /**
     * Links the passed annotation and test if correctly linked.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    private void linkAnnotationAndObjects(Annotation data) throws Exception {
        // Image
        Image i = (Image) iUpdate.saveAndReturnObject(mmFactory.simpleImage());
        ImageAnnotationLink l = new ImageAnnotationLinkI();
        l.setParent((Image) i.proxy());
        l.setChild((Annotation) data.proxy());
        IObject o1 = iUpdate.saveAndReturnObject(l);
        Assert.assertNotNull(o1);
        l = (ImageAnnotationLink) o1;
        Assert.assertEquals(l.getChild().getId().getValue(), data.getId().getValue());
        Assert.assertEquals(l.getParent().getId().getValue(), i.getId().getValue());

        // Project
        Project p = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());
        ProjectAnnotationLink pl = new ProjectAnnotationLinkI();
        pl.setParent((Project) p.proxy());
        pl.setChild((Annotation) data.proxy());
        o1 = iUpdate.saveAndReturnObject(pl);
        Assert.assertNotNull(o1);
        pl = (ProjectAnnotationLink) o1;
        Assert.assertEquals(pl.getChild().getId().getValue(), data.getId().getValue());
        Assert.assertEquals(pl.getParent().getId().getValue(), p.getId().getValue());

        // Dataset
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        DatasetAnnotationLink dl = new DatasetAnnotationLinkI();
        dl.setParent((Dataset) d.proxy());
        dl.setChild((Annotation) data.proxy());
        o1 = iUpdate.saveAndReturnObject(dl);
        Assert.assertNotNull(o1);
        dl = (DatasetAnnotationLink) o1;
        Assert.assertEquals(dl.getChild().getId().getValue(), data.getId().getValue());
        Assert.assertEquals(dl.getParent().getId().getValue(), d.getId().getValue());

        // Screen
        Screen s = (Screen) iUpdate.saveAndReturnObject(mmFactory
                .simpleScreenData().asIObject());
        ScreenAnnotationLink sl = new ScreenAnnotationLinkI();
        sl.setParent((Screen) s.proxy());
        sl.setChild((Annotation) data.proxy());
        o1 = iUpdate.saveAndReturnObject(sl);
        Assert.assertNotNull(o1);
        sl = (ScreenAnnotationLink) o1;
        Assert.assertEquals(sl.getChild().getId().getValue(), data.getId().getValue());
        Assert.assertEquals(sl.getParent().getId().getValue(), s.getId().getValue());

        // Plate
        Plate pp = (Plate) iUpdate.saveAndReturnObject(mmFactory
                .simplePlateData().asIObject());
        PlateAnnotationLink ppl = new PlateAnnotationLinkI();
        ppl.setParent((Plate) pp.proxy());
        ppl.setChild((Annotation) data.proxy());
        o1 = iUpdate.saveAndReturnObject(ppl);
        Assert.assertNotNull(o1);
        ppl = (PlateAnnotationLink) o1;
        Assert.assertEquals(ppl.getChild().getId().getValue(), data.getId().getValue());
        Assert.assertEquals(ppl.getParent().getId().getValue(), pp.getId().getValue());

        // Plate acquisition
        pp = (Plate) iUpdate.saveAndReturnObject(
                mmFactory.createPlate(1, 1, 1, 1, false));
        long self = factory.getAdminService().getEventContext().userId;
        ParametersI param = new ParametersI();
        param.exp(ode.rtypes.rlong(self));
        //method tested in PojosServiceTest
        List<IObject> results = factory.getContainerService().loadContainerHierarchy(
                Plate.class.getName(),
                Arrays.asList(pp.getId().getValue()), param);
        pp = (Plate) results.get(0);
        List<PlateAcquisition> list = pp.copyPlateAcquisitions();
        Assert.assertEquals(1, list.size());
        PlateAcquisition pa = list.get(0);
        PlateAcquisitionAnnotationLink pal = new PlateAcquisitionAnnotationLinkI();
        pal.setParent((PlateAcquisition) pa.proxy());
        pal.setChild((Annotation) data.proxy());
        o1 = iUpdate.saveAndReturnObject(pal);
        Assert.assertNotNull(o1);
        pal = (PlateAcquisitionAnnotationLink) o1;
        Assert.assertEquals(pal.getChild().getId().getValue(), data.getId().getValue());
        Assert.assertEquals(pal.getParent().getId().getValue(), pa.getId().getValue());

        //Create a roi
        int n = 0;
        ROIData roiData = new ROIData();
        roiData.setImage((Image) i.proxy());
        //Add rectangle
        ShapeData r = new RectangleData(0, 0, 1, 1);
        roiData.addShapeData(r);
        n++;
        //Add ellipse
        r = new EllipseData(2, 2, 1, 1);
        roiData.addShapeData(r);
        n++;
        //Add point
        r = new PointData(1, 1);
        roiData.addShapeData(r);
        n++;
        //Add line
        r = new LineData(0, 1, 1, 2);
        roiData.addShapeData(r);
        n++;
        //Add polygon
        String points = "points[10,10] points1[10,10] points2[10,10]";
        Polygon rect = new PolygonI();
        rect.setPoints(ode.rtypes.rstring(points));
        r = new PolygonData(rect);
        roiData.addShapeData(r);
        n++;
        //Add polyline
        Polyline polyline = new PolylineI();
        polyline.setPoints(ode.rtypes.rstring(points));
        r = new PolylineData(polyline);
        roiData.addShapeData(r);
        n++;
        Roi roi = (Roi) iUpdate.saveAndReturnObject(roiData.asIObject());
        //annotate both roi and the shape.
        RoiAnnotationLink ral = new RoiAnnotationLinkI();
        ral.setParent((Roi) roi.proxy());
        ral.setChild((Annotation) data.proxy());
        o1 = iUpdate.saveAndReturnObject(ral);
        Assert.assertNotNull(o1);
        ral = (RoiAnnotationLink) o1;
        Assert.assertEquals(ral.getChild().getId().getValue(), data.getId().getValue());
        Assert.assertEquals(ral.getParent().getId().getValue(), roi.getId().getValue());
        List<Shape> shapes = roi.copyShapes();
        Assert.assertEquals(n, shapes.size());
        Iterator<Shape> k = shapes.iterator();
        while (k.hasNext()) {
            Shape shape = k.next();
            ShapeAnnotationLink sal = new ShapeAnnotationLinkI();
            sal.setParent((Shape) shape.proxy());
            sal.setChild((Annotation) data.proxy());
            o1 = iUpdate.saveAndReturnObject(sal);
            Assert.assertNotNull(o1);
            sal = (ShapeAnnotationLink) o1;
            Assert.assertEquals(sal.getChild().getId().getValue(), data.getId().getValue());
            Assert.assertEquals(sal.getParent().getId().getValue(), shape.getId().getValue());
        }
    }

    /**
     * Tests to create a comment annotation and link it to various objects.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateCommentAnnotation() throws Exception {
        CommentAnnotation annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("comment"));
        annotation = (CommentAnnotation) iUpdate
                .saveAndReturnObject(annotation);
        Assert.assertNotNull(annotation);
        linkAnnotationAndObjects(annotation);
        TextualAnnotationData data = new TextualAnnotationData(annotation);
        Assert.assertNotNull(data);
        Assert.assertEquals(data.getText(), annotation.getTextValue().getValue());
        Assert.assertNull(data.getNameSpace());
    }

    /**
     * Tests to create a tag annotation and link it to various objects.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateTagAnnotation() throws Exception {
        TagAnnotation annotation = new TagAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("tag"));
        annotation = (TagAnnotation) iUpdate.saveAndReturnObject(annotation);
        Assert.assertNotNull(annotation);
        linkAnnotationAndObjects(annotation);
        TagAnnotationData data = new TagAnnotationData(annotation);
        Assert.assertNotNull(data);
        Assert.assertNull(data.getNameSpace());
        Assert.assertEquals(data.getTagValue(), annotation.getTextValue().getValue());
    }

    /**
     * Tests to create a boolean annotation and link it to various objects.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateBooleanAnnotation() throws Exception {
        BooleanAnnotation annotation = new BooleanAnnotationI();
        annotation.setBoolValue(ode.rtypes.rbool(true));
        annotation = (BooleanAnnotation) iUpdate
                .saveAndReturnObject(annotation);
        Assert.assertNotNull(annotation);
        linkAnnotationAndObjects(annotation);
        BooleanAnnotationData data = new BooleanAnnotationData(annotation);
        Assert.assertNotNull(data);
        Assert.assertNull(data.getNameSpace());
    }

    /**
     * Tests to create a long annotation and link it to various objects.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateLongAnnotation() throws Exception {
        LongAnnotation annotation = new LongAnnotationI();
        annotation.setLongValue(ode.rtypes.rlong(1L));
        annotation = (LongAnnotation) iUpdate.saveAndReturnObject(annotation);
        Assert.assertNotNull(annotation);
        linkAnnotationAndObjects(annotation);
        LongAnnotationData data = new LongAnnotationData(annotation);
        Assert.assertNotNull(data);
        Assert.assertNull(data.getNameSpace());
        Assert.assertEquals(data.getDataValue(), annotation.getLongValue().getValue());
    }

    /**
     * Tests to create a file annotation and link it to various objects.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateFileAnnotation() throws Exception {
        OriginalFile of = (OriginalFile) iUpdate.saveAndReturnObject(mmFactory
                .createOriginalFile());
        Assert.assertNotNull(of);
        FileAnnotation fa = new FileAnnotationI();
        fa.setFile(of);
        FileAnnotation data = (FileAnnotation) iUpdate.saveAndReturnObject(fa);
        Assert.assertNotNull(data);
        linkAnnotationAndObjects(data);
    }

    /**
     * Tests to create a term and link it to various objects.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateTermAnnotation() throws Exception {
        TermAnnotation term = new TermAnnotationI();
        term.setTermValue(ode.rtypes.rstring("term"));
        term = (TermAnnotation) iUpdate.saveAndReturnObject(term);
        Assert.assertNotNull(term);
        linkAnnotationAndObjects(term);
        TermAnnotationData data = new TermAnnotationData(term);
        Assert.assertNotNull(data);
        Assert.assertEquals(data.getTerm(), term.getTermValue().getValue());
        Assert.assertNull(data.getNameSpace());
    }

    /**
     * Tests to unlink of an annotation. Creates only one type of annotation.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testRemoveAnnotation() throws Exception {
        LongAnnotationI annotation = new LongAnnotationI();
        annotation.setLongValue(ode.rtypes.rlong(1L));
        LongAnnotation data = (LongAnnotation) iUpdate
                .saveAndReturnObject(annotation);
        Assert.assertNotNull(data);
        // Image
        Image i = (Image) iUpdate.saveAndReturnObject(mmFactory.simpleImage());
        ImageAnnotationLink l = new ImageAnnotationLinkI();
        l.setParent((Image) i.proxy());
        l.setChild((Annotation) data.proxy());
        l = (ImageAnnotationLink) iUpdate.saveAndReturnObject(l);
        Assert.assertNotNull(l);
        long id = l.getId().getValue();
        // annotation and image are linked. Remove the link.
        final Delete2 dc = Requests.delete().target(l).build();
        callback(true, client, dc);
        // now check that the image is no longer linked to the annotation
        String sql = "select link from ImageAnnotationLink as link";
        sql += " where link.id = :id";
        ParametersI p = new ParametersI();
        p.addId(id);
        IObject object = iQuery.findByQuery(sql, p);
        Assert.assertNull(object);
    }

    /**
     * Tests to update an annotation.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testUpdateAnnotation() throws Exception {
        CommentAnnotationI annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("comment"));
        CommentAnnotation data = (CommentAnnotation) iUpdate
                .saveAndReturnObject(annotation);
        Assert.assertNotNull(data);
        // modified the text
        String newText = "commentModified";
        data.setTextValue(ode.rtypes.rstring(newText));
        CommentAnnotation update = (CommentAnnotation) iUpdate
                .saveAndReturnObject(data);
        Assert.assertNotNull(update);

        Assert.assertEquals(data.getId().getValue(), update.getId().getValue());

        Assert.assertEquals(newText, update.getTextValue().getValue());
    }

    /**
     * Tests the creation of tag annotation, linked it to an image by a user and
     * link it to the same image by a different user.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testUpdateSameTagAnnotationUsedByTwoUsers() throws Exception {
        String groupName = newUserAndGroup("rwrw--").groupName;

        // create an image.
        Image image = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());

        // create the tag.
        TagAnnotationI tag = new TagAnnotationI();
        tag.setTextValue(ode.rtypes.rstring("tag1"));

        Annotation data = (Annotation) iUpdate.saveAndReturnObject(tag);
        // link the image and the tag
        ImageAnnotationLink l = new ImageAnnotationLinkI();
        l.setParent((Image) image.proxy());
        l.setChild((Annotation) data.proxy());

        IObject o1 = iUpdate.saveAndReturnObject(l);
        Assert.assertNotNull(o1);
        CreatePojosFixture2 fixture = CreatePojosFixture2.withNewUser(root,
                groupName);

        l = new ImageAnnotationLinkI();
        l.setParent((Image) image.proxy());
        l.setChild((Annotation) data.proxy());
        // l.getDetails().setOwner(fixture.e);
        IObject o2 = fixture.iUpdate.saveAndReturnObject(l);
        Assert.assertNotNull(o2);

        long self = factory.getAdminService().getEventContext().userId;

        Assert.assertNotEquals(o1.getId().getValue(), o2.getId().getValue());
        Assert.assertEquals(o1.getDetails().getOwner().getId().getValue(), self);
        Assert.assertEquals(o2.getDetails().getOwner().getId().getValue(), fixture.e
                .getId().getValue());
    }

    /**
     * Tests the creation of tag annotation, linked it to an image by a user and
     * link it to the same image by a different user.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testTagSetTagCreation() throws Exception {
        // Create a tag set.
        TagAnnotationI tagSet = new TagAnnotationI();
        tagSet.setTextValue(ode.rtypes.rstring("tagSet"));
        tagSet.setNs(ode.rtypes.rstring(TagAnnotationData.INSIGHT_TAGSET_NS));
        TagAnnotation tagSetReturned = (TagAnnotation) iUpdate
                .saveAndReturnObject(tagSet);
        // create a tag and link it to the tag set
        Assert.assertNotNull(tagSetReturned);
        TagAnnotationI tag = new TagAnnotationI();
        tag.setTextValue(ode.rtypes.rstring("tag"));
        TagAnnotation tagReturned = (TagAnnotation) iUpdate
                .saveAndReturnObject(tag);
        Assert.assertNotNull(tagReturned);
        AnnotationAnnotationLinkI link = new AnnotationAnnotationLinkI();
        link.setChild(tagReturned);
        link.setParent(tagSetReturned);
        IObject l = iUpdate.saveAndReturnObject(link); // save the link.
        Assert.assertNotNull(l);

        ParametersI param = new ParametersI();
        param.addId(l.getId());

        StringBuilder sb = new StringBuilder();
        sb.append("select l from AnnotationAnnotationLink l ");
        sb.append("left outer join fetch l.child c ");
        sb.append("left outer join fetch l.parent p ");
        sb.append("where l.id = :id");
        AnnotationAnnotationLinkI lReturned = (AnnotationAnnotationLinkI) iQuery
                .findByQuery(sb.toString(), param);
        Assert.assertNotNull(lReturned.getChild());
        Assert.assertNotNull(lReturned.getParent());
        Assert.assertEquals(lReturned.getChild().getId().getValue(), tagReturned
                .getId().getValue());
        Assert.assertEquals(lReturned.getParent().getId().getValue(), tagSetReturned
                .getId().getValue());
    }

    //
    // The following are duplicated in ode.server.itests.update.UpdateTest
    //

    /**
     * Test the creation and handling of channels.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:2547")
    public void testChannelMoveWithFullArrayGoesToEnd() throws Exception {
        Image i = mmFactory.createImage(ModelMockFactory.SIZE_X,
                ModelMockFactory.SIZE_Y, ModelMockFactory.SIZE_Z,
                ModelMockFactory.SIZE_T,
                ModelMockFactory.DEFAULT_CHANNELS_NUMBER, ModelMockFactory.UINT16);
        i = (Image) iUpdate.saveAndReturnObject(i);
        Pixels p = i.getPrimaryPixels();

        Set<Long> ids = new HashSet<Long>();
        Assert.assertEquals(ModelMockFactory.DEFAULT_CHANNELS_NUMBER,
                p.sizeOfChannels());
        for (Channel ch : p.copyChannels()) {
            Assert.assertNotNull(ch);
            ids.add(ch.getId().getValue());
        }

        // Now add another channel
        Channel extra = mmFactory.createChannel(0); // Copies dimension orders,
                                                    // etc.
        p.addChannel(extra);

        i = (Image) iUpdate.saveAndReturnObject(i);
        p = i.getPrimaryPixels();

        Assert.assertEquals(ModelMockFactory.DEFAULT_CHANNELS_NUMBER + 1,
                p.sizeOfChannels());
        Assert.assertFalse(ids.contains(p
                .getChannel(ModelMockFactory.DEFAULT_CHANNELS_NUMBER).getId()
                .getValue()));
    }

    /**
     * Test the creation and handling of channels.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:2547")
    public void testChannelMoveWithSpaceFillsSpace() throws Exception {
        Image i = mmFactory.createImage(ModelMockFactory.SIZE_X,
                ModelMockFactory.SIZE_Y, ModelMockFactory.SIZE_Z,
                ModelMockFactory.SIZE_T,
                ModelMockFactory.DEFAULT_CHANNELS_NUMBER, ModelMockFactory.UINT16);
        i = (Image) iUpdate.saveAndReturnObject(i);

        Pixels p = i.getPrimaryPixels();
        p.setChannel(1, null);
        p = (Pixels) iUpdate.saveAndReturnObject(p);

        Set<Long> ids = new HashSet<Long>();
        Channel old = p.getChannel(0);
        Assert.assertEquals(ModelMockFactory.DEFAULT_CHANNELS_NUMBER,
                p.sizeOfChannels());
        Assert.assertNotNull(old);
        ids.add(p.getChannel(0).getId().getValue());

        // Middle should be empty
        Assert.assertNull(p.getChannel(1));

        Assert.assertNotNull(p.getChannel(2));
        ids.add(p.getChannel(2).getId().getValue());

        // Now add a channel to the front

        // extra = (Channel) iUpdate.saveAndReturnObject(extra);
        // p.setChannel(0, extra);
        p.setChannel(1, old);

        p = (Pixels) iUpdate.saveAndReturnObject(p);
        Channel extra = mmFactory.createChannel(0);
        p.setChannel(0, extra);

        p = (Pixels) iUpdate.saveAndReturnObject(p);

        Assert.assertEquals(ModelMockFactory.DEFAULT_CHANNELS_NUMBER,
                p.sizeOfChannels());
        Assert.assertFalse(ids.contains(p.getChannel(0).getId().getValue()));
    }

    /**
     * Test the creation and handling of channels.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:2547")
    public void testChannelToSpaceChangesNothing() throws Exception {
        Image i = mmFactory.createImage(ModelMockFactory.SIZE_X,
                ModelMockFactory.SIZE_Y, ModelMockFactory.SIZE_Z,
                ModelMockFactory.SIZE_T,
                ModelMockFactory.DEFAULT_CHANNELS_NUMBER, ModelMockFactory.UINT16);
        i = (Image) iUpdate.saveAndReturnObject(i);

        Pixels p = i.getPrimaryPixels();
        p.setChannel(1, null);
        p = (Pixels) iUpdate.saveAndReturnObject(p);

        Set<Long> ids = new HashSet<Long>();
        Assert.assertEquals(ModelMockFactory.DEFAULT_CHANNELS_NUMBER,
                p.sizeOfChannels());
        Assert.assertNotNull(p.getChannel(0));
        ids.add(p.getChannel(0).getId().getValue());

        // Middle should be empty
        Assert.assertNull(p.getChannel(1));

        Assert.assertNotNull(p.getChannel(2));
        ids.add(p.getChannel(2).getId().getValue());

        // Now add a channel to the space
        Channel extra = mmFactory.createChannel(0);
        p.setChannel(1, extra);

        p = (Pixels) iUpdate.saveAndReturnObject(p);

        Assert.assertEquals(ModelMockFactory.DEFAULT_CHANNELS_NUMBER,
                p.sizeOfChannels());
        Assert.assertFalse(ids.contains(p.getChannel(1).getId().getValue()));
    }

    /**
     * Tests the creation of plane information objects.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = { "ticket:168", "ticket:767" })
    public void testPlaneInfoSetPixelsSavePlaneInfo() throws Exception {
        Image image = (Image) iUpdate.saveAndReturnObject(mmFactory
                .createImage());
        Pixels pixels = image.getPrimaryPixels();
        pixels.clearPlaneInfo();
        PlaneInfo planeInfo = mmFactory.createPlaneInfo();
        planeInfo.setPixels(pixels);
        planeInfo = (PlaneInfo) iUpdate.saveAndReturnObject(planeInfo);
        ParametersI param = new ParametersI();
        param.addId(planeInfo.getId());
        Pixels test = (Pixels) iQuery.findByQuery(
                "select pi.pixels from PlaneInfo pi where pi.id = :id", param);
        Assert.assertNotNull(test);
    }

    /**
     * Tests the creation of plane information objects. This time the plane info
     * object is directly added to the pixels set. The plane info should be
     * saved.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:168")
    public void testPixelsAddToPlaneInfoSavePixels() throws Exception {
        Image image = mmFactory.createImage();
        image = (Image) iUpdate.saveAndReturnObject(image);
        Pixels pixels = image.getPrimaryPixels();
        pixels.clearPlaneInfo();
        PlaneInfo planeInfo = mmFactory.createPlaneInfo();
        pixels.addPlaneInfo(planeInfo);
        pixels = (Pixels) iUpdate.saveAndReturnObject(pixels);
        ParametersI param = new ParametersI();
        param.addId(pixels.getId());
        List<IObject> test = (List<IObject>) iQuery.findAllByQuery(
                "select pi from PlaneInfo pi where pi.pixels.id = :id", param);
        Assert.assertTrue(CollectionUtils.isNotEmpty(test));
    }

    /**
     * Tests the creation of ROIs whose shapes are Ellipses and converts them
     * into the corresponding <code>POJO</code> objects.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateROIWithEllipse() throws Exception {
        ImageI image = (ImageI) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());
        RoiI roi = new RoiI();
        roi.setImage(image);
        RoiI serverROI = (RoiI) iUpdate.saveAndReturnObject(roi);
        Assert.assertNotNull(serverROI);
        double v = 10;
        int z = 0;
        int t = 0;
        int c = 0;
        EllipseI rect = new EllipseI();
        rect.setX(ode.rtypes.rdouble(v));
        rect.setY(ode.rtypes.rdouble(v));
        rect.setRadiusX(ode.rtypes.rdouble(v));
        rect.setRadiusY(ode.rtypes.rdouble(v));
        rect.setTheZ(ode.rtypes.rint(z));
        rect.setTheT(ode.rtypes.rint(t));
        rect.setTheC(ode.rtypes.rint(c));
        serverROI.addShape(rect);

        serverROI = (RoiI) iUpdate.saveAndReturnObject(serverROI);

        ROIData data = new ROIData(serverROI);
        Assert.assertEquals(data.getId(), serverROI.getId().getValue());
        Assert.assertEquals(data.getShapeCount(), 1);

        List<ShapeData> shapes = data.getShapes(z, t);
        Assert.assertNotNull(shapes);
        Assert.assertEquals(shapes.size(), 1);
        EllipseData shape;
        Iterator<ShapeData> i = shapes.iterator();
        while (i.hasNext()) {
            shape = (EllipseData) i.next();
            Assert.assertEquals(shape.getT(), t);
            Assert.assertEquals(shape.getZ(), z);
            Assert.assertEquals(shape.getC(), c);
            Assert.assertEquals(shape.getX(), v);
            Assert.assertEquals(shape.getY(), v);
            Assert.assertEquals(shape.getRadiusX(), v);
            Assert.assertEquals(shape.getRadiusY(), v);
        }
    }

    /**
     * Tests the creation of ROIs whose shapes are Points and converts them into
     * the corresponding <code>POJO</code> objects.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateROIWithPoint() throws Exception {
        Image image = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());
        Roi roi = new RoiI();
        roi.setImage(image);
        Roi serverROI = (Roi) iUpdate.saveAndReturnObject(roi);
        Assert.assertNotNull(serverROI);
        double v = 10;
        int z = 0;
        int t = 0;
        int c = 0;
        Point rect = new PointI();
        rect.setX(ode.rtypes.rdouble(v));
        rect.setY(ode.rtypes.rdouble(v));
        rect.setTheZ(ode.rtypes.rint(z));
        rect.setTheT(ode.rtypes.rint(t));
        rect.setTheC(ode.rtypes.rint(c));
        serverROI.addShape(rect);

        serverROI = (RoiI) iUpdate.saveAndReturnObject(serverROI);

        ROIData data = new ROIData(serverROI);
        Assert.assertEquals(data.getId(), serverROI.getId().getValue());
        Assert.assertEquals(data.getShapeCount(), 1);

        List<ShapeData> shapes = data.getShapes(z, t);
        Assert.assertNotNull(shapes);
        Assert.assertEquals(shapes.size(), 1);
        PointData shape;
        Iterator<ShapeData> i = shapes.iterator();
        while (i.hasNext()) {
            shape = (PointData) i.next();
            Assert.assertEquals(shape.getT(), t);
            Assert.assertEquals(shape.getZ(), z);
            Assert.assertEquals(shape.getC(), c);
            Assert.assertEquals(shape.getX(), v);
            Assert.assertEquals(shape.getY(), v);
        }
    }

    /**
     * Tests the creation of ROIs whose shapes are Points and converts them into
     * the corresponding <code>POJO</code> objects.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateROIWithRectangle() throws Exception {
        Image image = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());
        Roi roi = new RoiI();
        roi.setImage(image);
        Roi serverROI = (Roi) iUpdate.saveAndReturnObject(roi);
        Assert.assertNotNull(serverROI);
        double v = 10;
        int z = 0;
        int t = 0;
        int c = 0;
        Rectangle rect = new RectangleI();
        rect.setX(ode.rtypes.rdouble(v));
        rect.setY(ode.rtypes.rdouble(v));
        rect.setWidth(ode.rtypes.rdouble(v));
        rect.setHeight(ode.rtypes.rdouble(v));
        rect.setTheZ(ode.rtypes.rint(z));
        rect.setTheT(ode.rtypes.rint(t));
        rect.setTheC(ode.rtypes.rint(c));
        serverROI.addShape(rect);

        serverROI = (RoiI) iUpdate.saveAndReturnObject(serverROI);

        ROIData data = new ROIData(serverROI);
        Assert.assertEquals(data.getId(), serverROI.getId().getValue());
        Assert.assertEquals(data.getShapeCount(), 1);

        List<ShapeData> shapes = data.getShapes(z, t);
        Assert.assertNotNull(shapes);
        Assert.assertEquals(shapes.size(), 1);
        RectangleData shape;
        Iterator<ShapeData> i = shapes.iterator();
        while (i.hasNext()) {
            shape = (RectangleData) i.next();
            Assert.assertEquals(shape.getT(), t);
            Assert.assertEquals(shape.getZ(), z);
            Assert.assertEquals(shape.getC(), c);
            Assert.assertEquals(shape.getX(), v);
            Assert.assertEquals(shape.getY(), v);
            Assert.assertEquals(shape.getWidth(), v);
            Assert.assertEquals(shape.getHeight(), v);
        }
    }

    /**
     * Tests the creation of an ROI not linked to an image.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateROIWithoutImage() throws Exception {
        Roi roi = new RoiI();
        roi.setDescription(ode.rtypes.rstring("roi w/o image"));
        Roi serverROI = (Roi) iUpdate.saveAndReturnObject(roi);
    }

    /**
     * Tests the creation of ROIs whose shapes are Polygons and converts them
     * into the corresponding <code>POJO</code> objects.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateROIWithPolygon() throws Exception {
        Image image = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());
        Roi roi = new RoiI();
        roi.setImage(image);
        Roi serverROI = (Roi) iUpdate.saveAndReturnObject(roi);
        Assert.assertNotNull(serverROI);
        double v = 10;
        double w = 11;
        int z = 0;
        int t = 0;
        int c = 0;
        String points = "points[10,10] points1[10,10] points2[10,10]";
        Polygon rect = new PolygonI();
        rect.setPoints(ode.rtypes.rstring(points));
        rect.setTheZ(ode.rtypes.rint(z));
        rect.setTheT(ode.rtypes.rint(t));
        rect.setTheC(ode.rtypes.rint(c));
        serverROI.addShape(rect);

        serverROI = (RoiI) iUpdate.saveAndReturnObject(serverROI);

        ROIData data = new ROIData(serverROI);
        Assert.assertEquals(data.getId(), serverROI.getId().getValue());
        Assert.assertEquals(data.getShapeCount(), 1);

        List<ShapeData> shapes = data.getShapes(z, t);
        Assert.assertNotNull(shapes);
        Assert.assertEquals(shapes.size(), 1);
        PolygonData shape;
        Iterator<ShapeData> i = shapes.iterator();
        while (i.hasNext()) {
            shape = (PolygonData) i.next();
            Assert.assertEquals(shape.getT(), t);
            Assert.assertEquals(shape.getZ(), z);
            Assert.assertEquals(shape.getC(), c);
            Assert.assertEquals(shape.getPoints().size(), 1);
        }
    }

    /**
     * Tests the creation of ROIs whose shapes are Polylines and converts them
     * into the corresponding <code>POJO</code> objects.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateROIWithPolyline() throws Exception {
        Image image = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());
        Roi roi = new RoiI();
        roi.setImage(image);
        Roi serverROI = (Roi) iUpdate.saveAndReturnObject(roi);
        Assert.assertNotNull(serverROI);
        double v = 10;
        String points = "points[10,10] points1[10,10] points2[10,10]";
        int z = 0;
        int t = 0;
        int c = 0;
        Polyline rect = new PolylineI();
        rect.setPoints(ode.rtypes.rstring(points));
        rect.setTheZ(ode.rtypes.rint(z));
        rect.setTheT(ode.rtypes.rint(t));
        rect.setTheC(ode.rtypes.rint(c));
        serverROI.addShape(rect);

        serverROI = (RoiI) iUpdate.saveAndReturnObject(serverROI);

        ROIData data = new ROIData(serverROI);
        Assert.assertEquals(data.getId(), serverROI.getId().getValue());
        Assert.assertEquals(data.getShapeCount(), 1);

        List<ShapeData> shapes = data.getShapes(z, t);
        Assert.assertNotNull(shapes);
        Assert.assertEquals(shapes.size(), 1);
        PolylineData shape;
        Iterator<ShapeData> i = shapes.iterator();
        while (i.hasNext()) {
            shape = (PolylineData) i.next();
            Assert.assertEquals(shape.getT(), t);
            Assert.assertEquals(shape.getZ(), z);
            Assert.assertEquals(shape.getC(), c);
            Assert.assertEquals(shape.getPoints().size(), 1);
        }
    }

    /**
     * Tests the creation of ROIs whose shapes are Lines and converts them into
     * the corresponding <code>POJO</code> objects.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateROIWithLine() throws Exception {
        Image image = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());
        Roi roi = new RoiI();
        roi.setImage(image);
        Roi serverROI = (Roi) iUpdate.saveAndReturnObject(roi);
        Assert.assertNotNull(serverROI);
        double v = 10;
        double w = 11;
        int z = 0;
        int t = 0;
        int c = 0;
        Line rect = new LineI();
        rect.setX1(ode.rtypes.rdouble(v));
        rect.setY1(ode.rtypes.rdouble(v));
        rect.setX2(ode.rtypes.rdouble(w));
        rect.setY2(ode.rtypes.rdouble(w));
        rect.setTheZ(ode.rtypes.rint(z));
        rect.setTheT(ode.rtypes.rint(t));
        rect.setTheC(ode.rtypes.rint(c));
        serverROI.addShape(rect);

        serverROI = (RoiI) iUpdate.saveAndReturnObject(serverROI);

        ROIData data = new ROIData(serverROI);
        Assert.assertEquals(data.getId(), serverROI.getId().getValue());
        Assert.assertEquals(data.getShapeCount(), 1);

        List<ShapeData> shapes = data.getShapes(z, t);
        Assert.assertNotNull(shapes);
        Assert.assertEquals(shapes.size(), 1);
        LineData shape;
        Iterator<ShapeData> i = shapes.iterator();
        while (i.hasNext()) {
            shape = (LineData) i.next();
            Assert.assertEquals(shape.getT(), t);
            Assert.assertEquals(shape.getZ(), z);
            Assert.assertEquals(shape.getC(), c);
            Assert.assertEquals(shape.getX1(), v);
            Assert.assertEquals(shape.getY1(), v);
            Assert.assertEquals(shape.getX2(), w);
            Assert.assertEquals(shape.getY2(), w);
        }
    }

    /**
     * Tests the creation of ROIs whose shapes are Masks and converts them into
     * the corresponding <code>POJO</code> objects.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateROIWithMask() throws Exception {
        Image image = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());
        Roi roi = new RoiI();
        roi.setImage(image);
        Roi serverROI = (Roi) iUpdate.saveAndReturnObject(roi);
        Assert.assertNotNull(serverROI);
        double v = 10;
        int z = 0;
        int t = 0;
        int c = 0;
        Mask rect = new MaskI();
        rect.setX(ode.rtypes.rdouble(v));
        rect.setY(ode.rtypes.rdouble(v));
        rect.setWidth(ode.rtypes.rdouble(v));
        rect.setHeight(ode.rtypes.rdouble(v));
        rect.setTheZ(ode.rtypes.rint(z));
        rect.setTheT(ode.rtypes.rint(t));
        rect.setTheC(ode.rtypes.rint(c));
        serverROI.addShape(rect);

        serverROI = (RoiI) iUpdate.saveAndReturnObject(serverROI);

        ROIData data = new ROIData(serverROI);
        Assert.assertEquals(data.getId(), serverROI.getId().getValue());
        Assert.assertEquals(data.getShapeCount(), 1);

        List<ShapeData> shapes = data.getShapes(z, t);
        Assert.assertNotNull(shapes);
        Assert.assertEquals(shapes.size(), 1);
        MaskData shape;
        Iterator<ShapeData> i = shapes.iterator();
        while (i.hasNext()) {
            shape = (MaskData) i.next();
            Assert.assertEquals(shape.getT(), t);
            Assert.assertEquals(shape.getZ(), z);
            Assert.assertEquals(shape.getC(), c);
            Assert.assertEquals(shape.getX(), v);
            Assert.assertEquals(shape.getY(), v);
            Assert.assertEquals(shape.getWidth(), v);
            Assert.assertEquals(shape.getHeight(), v);
        }
    }

    /**
     * Tests the creation of an instrument using the <code>Add</code> methods
     * associated to an instrument.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateInstrumentUsingAdd() throws Exception {
        Instrument instrument;
        ParametersI param;
        String sql;
        IObject test;
        String value;
        for (int i = 0; i < ModelMockFactory.LIGHT_SOURCES.length; i++) {
            value = ModelMockFactory.LIGHT_SOURCES[i];
            instrument = mmFactory.createInstrument(value);
            instrument = (Instrument) iUpdate.saveAndReturnObject(instrument);
            Assert.assertNotNull(instrument);
            param = new ParametersI();
            param.addLong("iid", instrument.getId().getValue());
            sql = "select d from Detector as d where d.instrument.id = :iid";
            test = iQuery.findByQuery(sql, param);
            Assert.assertNotNull(test);
            sql = "select d from Dichroic as d where d.instrument.id = :iid";
            test = iQuery.findByQuery(sql, param);
            Assert.assertNotNull(test);
            sql = "select d from Filter as d where d.instrument.id = :iid";
            test = iQuery.findByQuery(sql, param);
            Assert.assertNotNull(test);
            sql = "select d from Objective as d where d.instrument.id = :iid";
            test = iQuery.findByQuery(sql, param);
            Assert.assertNotNull(test);
            sql = "select d from LightSource as d where d.instrument.id = :iid";
            test = iQuery.findByQuery(sql, param);
            Assert.assertNotNull(test);
            param = new ParametersI();
            param.addLong("iid", test.getId().getValue());
            if (ModelMockFactory.LASER.equals(value)) {
                sql = "select d from Laser as d where d.id = :iid";
                test = iQuery.findByQuery(sql, param);
                Assert.assertNotNull(test);
            } else if (ModelMockFactory.FILAMENT.equals(value)) {
                sql = "select d from Filament as d where d.id = :iid";
                test = iQuery.findByQuery(sql, param);
                Assert.assertNotNull(test);
            } else if (ModelMockFactory.ARC.equals(value)) {
                sql = "select d from Arc as d where d.id = :iid";
                test = iQuery.findByQuery(sql, param);
                Assert.assertNotNull(test);
            } else if (ModelMockFactory.LIGHT_EMITTING_DIODE.equals(value)) {
                sql = "select d from LightEmittingDiode as d where d.id = :iid";
                test = iQuery.findByQuery(sql, param);
                Assert.assertNotNull(test);
            }
        }
    }

    /**
     * Tests the creation of an instrument using the <code>setInstrument</code>
     * method on the entities composing the instrument.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateInstrumentUsingSet() throws Exception {
        Instrument instrument = (Instrument) iUpdate
                .saveAndReturnObject(mmFactory.createInstrument());
        Assert.assertNotNull(instrument);

        Detector d = mmFactory.createDetector();
        d.setInstrument((Instrument) instrument.proxy());
        d = (Detector) iUpdate.saveAndReturnObject(d);
        Assert.assertNotNull(d);

        Filter f = mmFactory.createFilter(500, 560);
        f.setInstrument((Instrument) instrument.proxy());
        f = (Filter) iUpdate.saveAndReturnObject(f);
        Assert.assertNotNull(f);

        Dichroic di = mmFactory.createDichroic();
        di.setInstrument((Instrument) instrument.proxy());
        di = (Dichroic) iUpdate.saveAndReturnObject(di);
        Assert.assertNotNull(di);

        Objective o = mmFactory.createObjective();
        o.setInstrument((Instrument) instrument.proxy());
        o = (Objective) iUpdate.saveAndReturnObject(o);
        Assert.assertNotNull(o);

        Laser l = mmFactory.createLaser();
        l.setInstrument((Instrument) instrument.proxy());
        l = (Laser) iUpdate.saveAndReturnObject(l);
        Assert.assertNotNull(l);

        ParametersI param = new ParametersI();
        param.addLong("iid", instrument.getId().getValue());
        // Now check that we have a detector.
        String sql = "select d from Detector as d where d.instrument.id = :iid";
        IObject test = iQuery.findByQuery(sql, param);
        Assert.assertNotNull(test);
        Assert.assertNotNull(test.getId().getValue() == d.getId().getValue());
        sql = "select d from Dichroic as d where d.instrument.id = :iid";
        test = iQuery.findByQuery(sql, param);
        Assert.assertNotNull(test);
        Assert.assertNotNull(test.getId().getValue() == di.getId().getValue());
        sql = "select d from Filter as d where d.instrument.id = :iid";
        test = iQuery.findByQuery(sql, param);
        Assert.assertNotNull(test);
        Assert.assertNotNull(test.getId().getValue() == f.getId().getValue());
        sql = "select d from Objective as d where d.instrument.id = :iid";
        test = iQuery.findByQuery(sql, param);
        Assert.assertNotNull(test);
        Assert.assertNotNull(test.getId().getValue() == o.getId().getValue());
        sql = "select d from LightSource as d where d.instrument.id = :iid";
        test = iQuery.findByQuery(sql, param);
        Assert.assertNotNull(test);
    }


    /**
     * Tests the creation of a plate and reagent
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testPlateAndReagent() throws Exception {
        Screen s = mmFactory.simpleScreenData().asScreen();
        Reagent r = mmFactory.createReagent();
        s.addReagent(r);
        Plate p = mmFactory.createPlateWithReagent(1, 1, 1, r);
        s.linkPlate(p);
        s = (Screen) iUpdate.saveAndReturnObject(s);
        Assert.assertNotNull(s);
        Assert.assertNotNull(s.getName().getValue());
        Assert.assertNotNull(s.getDescription().getValue());
        Assert.assertNotNull(s.getProtocolDescription().getValue());
        Assert.assertNotNull(s.getProtocolIdentifier().getValue());
        Assert.assertNotNull(s.getReagentSetDescription().getValue());
        Assert.assertNotNull(s.getReagentSetIdentifier().getValue());

        // reagent first
        String sql = "select r from Reagent as r ";
        sql += "join fetch r.screen as s ";
        sql += "where s.id = :id";
        ParametersI param = new ParametersI();
        param.addId(s.getId().getValue());
        r = (Reagent) iQuery.findByQuery(sql, param);
        Assert.assertNotNull(r);
        Assert.assertNotNull(r.getName().getValue());
        Assert.assertNotNull(r.getDescription().getValue());
        Assert.assertNotNull(r.getReagentIdentifier().getValue());

        //
        sql = "select s from ScreenPlateLink as s ";
        sql += "join fetch s.child as c ";
        sql += "join fetch s.parent as p ";
        sql += "where p.id = :id";
        param = new ParametersI();
        param.addId(s.getId().getValue());
        ScreenPlateLink link = (ScreenPlateLink) iQuery.findByQuery(sql, param);
        Assert.assertNotNull(link);
        // check the reagent.
        sql = "select s from WellReagentLink as s ";
        sql += "join fetch s.child as c ";
        sql += "join fetch s.parent as p ";
        sql += "where c.id = :id";
        param = new ParametersI();
        param.addId(r.getId().getValue());
        Assert.assertNotNull(iQuery.findByQuery(sql, param));
    }

    /**
     * Tests to create a file annotation and link it to several images using the
     * saveAndReturnArray.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = { "ticket:5370" })
    public void testAttachFileAnnotationToSeveralImages() throws Exception {
        OriginalFile of = (OriginalFile) iUpdate.saveAndReturnObject(mmFactory
                .createOriginalFile());
        Assert.assertNotNull(of);
        FileAnnotation fa = new FileAnnotationI();
        fa.setFile(of);
        FileAnnotation data = (FileAnnotation) iUpdate.saveAndReturnObject(fa);
        Assert.assertNotNull(data);
        // Image
        Image i1 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Image i2 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        List<IObject> links = new ArrayList<IObject>();
        ImageAnnotationLink l = new ImageAnnotationLinkI();
        l.setParent((Image) i1.proxy());
        l.setChild((Annotation) data.proxy());
        links.add(l);
        l = new ImageAnnotationLinkI();
        l.setParent((Image) i2.proxy());
        l.setChild((Annotation) data.proxy());
        links.add(l);
        links = iUpdate.saveAndReturnArray(links);
        Assert.assertNotNull(links);
        Assert.assertEquals(links.size(), 2);
        Iterator<IObject> i = links.iterator();
        long id;
        List<Long> ids = new ArrayList<Long>();
        ids.add(i1.getId().getValue());
        ids.add(i2.getId().getValue());
        int n = 0;
        while (i.hasNext()) {
            l = (ImageAnnotationLink) i.next();
            Assert.assertEquals(l.getChild().getId().getValue(), data.getId()
                    .getValue());
            id = l.getParent().getId().getValue();
            if (ids.contains(id))
                n++;
        }
        Assert.assertEquals(ids.size(), n);
    }

    /**
     * Tests to create a file annotation and link it to several images using the
     * saveAndReturnArray.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = { "ticket:5370" })
    public void testAttachFileAnnotationToSeveralImagesII() throws Exception {
        OriginalFile of = (OriginalFile) iUpdate.saveAndReturnObject(mmFactory
                .createOriginalFile());
        Assert.assertNotNull(of);
        FileAnnotation fa = new FileAnnotationI();
        fa.setFile(of);
        FileAnnotation data = (FileAnnotation) iUpdate.saveAndReturnObject(fa);
        Assert.assertNotNull(data);
        // Image
        Image i1 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Image i2 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        ImageAnnotationLink l = new ImageAnnotationLinkI();
        l.setParent((Image) i1.proxy());
        l.setChild((Annotation) data.proxy());

        l = (ImageAnnotationLink) iUpdate.saveAndReturnObject(l);
        Assert.assertEquals(l.getChild().getId().getValue(), data.getId().getValue());
        Assert.assertEquals(l.getParent().getId().getValue(), i1.getId().getValue());
        l = new ImageAnnotationLinkI();
        l.setParent((Image) i2.proxy());
        l.setChild((Annotation) data.proxy());
        l = (ImageAnnotationLink) iUpdate.saveAndReturnObject(l);

        Assert.assertEquals(l.getChild().getId().getValue(), data.getId().getValue());
        Assert.assertEquals(l.getParent().getId().getValue(), i2.getId().getValue());
    }

    /**
     * Tests the creation of ROIs whose shapes are Polylines and converts them
     * into the corresponding <code>POJO</code> objects. The list of points
     * follows the specification.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateROIWithPolylineUsingSchema() throws Exception {
        Image image = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());
        Roi roi = new RoiI();
        roi.setImage(image);
        Roi serverROI = (Roi) iUpdate.saveAndReturnObject(roi);
        Assert.assertNotNull(serverROI);
        double v = 10;
        String points = "10,10 11,11";
        int z = 0;
        int t = 0;
        int c = 0;
        Polyline rect = new PolylineI();
        rect.setPoints(ode.rtypes.rstring(points));
        rect.setTheZ(ode.rtypes.rint(z));
        rect.setTheT(ode.rtypes.rint(t));
        rect.setTheC(ode.rtypes.rint(c));
        serverROI.addShape(rect);

        serverROI = (RoiI) iUpdate.saveAndReturnObject(serverROI);

        ROIData data = new ROIData(serverROI);
        Assert.assertEquals(data.getId(), serverROI.getId().getValue());
        Assert.assertEquals(data.getShapeCount(), 1);

        List<ShapeData> shapes = data.getShapes(z, t);
        Assert.assertNotNull(shapes);
        Assert.assertEquals(shapes.size(), 1);
        PolylineData shape;
        Iterator<ShapeData> i = shapes.iterator();
        while (i.hasNext()) {
            shape = (PolylineData) i.next();
            Assert.assertEquals(shape.getT(), t);
            Assert.assertEquals(shape.getZ(), z);
            Assert.assertEquals(shape.getC(), c);
            Assert.assertEquals(shape.getPoints().size(), 2);
        }
    }

    /**
     * Tests the creation of ROIs whose shapes are Polygons and converts them
     * into the corresponding <code>POJO</code> objects.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateROIWithPolygonUsingSchema() throws Exception {
        Image image = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());
        Roi roi = new RoiI();
        roi.setImage(image);
        Roi serverROI = (Roi) iUpdate.saveAndReturnObject(roi);
        Assert.assertNotNull(serverROI);
        double v = 10;
        double w = 11;
        int z = 0;
        int t = 0;
        int c = 0;
        String points = "10,10 11,11";
        Polygon rect = new PolygonI();
        rect.setPoints(ode.rtypes.rstring(points));
        rect.setTheZ(ode.rtypes.rint(z));
        rect.setTheT(ode.rtypes.rint(t));
        rect.setTheC(ode.rtypes.rint(c));
        serverROI.addShape(rect);

        serverROI = (RoiI) iUpdate.saveAndReturnObject(serverROI);

        ROIData data = new ROIData(serverROI);
        Assert.assertEquals(data.getId(), serverROI.getId().getValue());
        Assert.assertEquals(data.getShapeCount(), 1);

        List<ShapeData> shapes = data.getShapes(z, t);
        Assert.assertNotNull(shapes);
        Assert.assertEquals(shapes.size(), 1);
        PolygonData shape;
        Iterator<ShapeData> i = shapes.iterator();
        while (i.hasNext()) {
            shape = (PolygonData) i.next();
            Assert.assertEquals(shape.getT(), t);
            Assert.assertEquals(shape.getZ(), z);
            Assert.assertEquals(shape.getC(), c);
            Assert.assertEquals(shape.getPoints().size(), 2);
        }
    }

    /**
     * Tests to create a XML annotation and link it to various objects.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateXmlAnnotation() throws Exception {
        XmlAnnotation term = new XmlAnnotationI();
        term.setTextValue(ode.rtypes.rstring("xml"));
        term = (XmlAnnotation) iUpdate.saveAndReturnObject(term);
        Assert.assertNotNull(term);
        linkAnnotationAndObjects(term);
        XMLAnnotationData data = new XMLAnnotationData(term);
        Assert.assertNotNull(data);
        Assert.assertNull(data.getNameSpace());
        Assert.assertEquals(data.getText(), term.getTextValue().getValue());
    }

    /**
     * Tests to create a tag set annotation i.e. a tag with a name space.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateTagSetAnnotation() throws Exception {
        TagAnnotation annotation = new TagAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("tag set"));
        annotation = (TagAnnotation) iUpdate.saveAndReturnObject(annotation);
        Assert.assertNotNull(annotation);
        linkAnnotationAndObjects(annotation);
        TagAnnotationData data = new TagAnnotationData(annotation);
        data.setNameSpace(TagAnnotationData.INSIGHT_TAGSET_NS);
        annotation = (TagAnnotation) iUpdate.saveAndReturnObject(data
                .asIObject());
        data = new TagAnnotationData(annotation);
        Assert.assertNotNull(data);
        Assert.assertEquals(data.getTagValue(), annotation.getTextValue().getValue());
        Assert.assertEquals(data.getNameSpace(), TagAnnotationData.INSIGHT_TAGSET_NS);
    }

    /**
     * Tests to create a tag annotation i.e. a tag with a name space. using the
     * pojo class.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateTagAnnotationUsingPojo() throws Exception {
        String name = "tag";
        TagAnnotationData data = new TagAnnotationData(name);
        TagAnnotation annotation = (TagAnnotation) iUpdate
                .saveAndReturnObject(data.asIObject());
        data = new TagAnnotationData(annotation);
        Assert.assertNotNull(data);
        Assert.assertEquals(data.getTagValue(), name);
        Assert.assertNull(data.getNameSpace());
    }

    /**
     * Tests to create a tag set annotation i.e. a tag with a name space. using
     * the pojo class.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCreateTagSetAnnotationUsingPojo() throws Exception {
        String name = "tag set";
        TagAnnotationData data = new TagAnnotationData(name, true);
        TagAnnotation annotation = (TagAnnotation) iUpdate
                .saveAndReturnObject(data.asIObject());
        data = new TagAnnotationData(annotation);
        Assert.assertNotNull(data);
        Assert.assertEquals(data.getTagValue(), name);
        Assert.assertNotNull(data.getNameSpace());
        Assert.assertEquals(data.getNameSpace(), TagAnnotationData.INSIGHT_TAGSET_NS);
    }

    /**
     * Test that users cannot adjust the {@link ExternalInfo} of their own session.
     * @throws Exception unexpected
     */
    @Test
    public void testEditSessionExternalInfo() throws Exception {
        newUserAndGroup("rw----");
        /* prepare to act as root */
        final IQueryPrx iQueryRoot = root.getSession().getQueryService();
        final IUpdatePrx iUpdateRoot = root.getSession().getUpdateService();
        /* as root, add external info with a specific UUID to the user's current session */
        Session session = (Session) iQueryRoot.findByString("Session", "uuid", client.getSessionId());
        final long sessionId = session.getId().getValue();
        final ExternalInfo infoOld = new ExternalInfoI();
        infoOld.setEntityType(ode.rtypes.rstring(Session.class.getName()));
        infoOld.setEntityId(ode.rtypes.rlong(sessionId));
        infoOld.setUuid(ode.rtypes.rstring(UUID.randomUUID().toString()));
        session.getDetails().setExternalInfo(infoOld);
        iUpdateRoot.saveObject(session);
        /* prepare for querying session with external info */
        final String hql = "FROM Session s JOIN FETCH s.details.externalInfo WHERE s.id = :id";
        final Parameters params = new ParametersI().addId(sessionId);
        /* as the normal user, check that the session external info UUID is as set by root */
        session = (Session) iQuery.findByQuery(hql, params);
        ExternalInfo info = session.getDetails().getExternalInfo();
        Assert.assertEquals(info.getUuid().getValue(), infoOld.getUuid().getValue());
        /* as the normal user, attempt to set the session external info to something else */
        final ExternalInfo infoNew = new ExternalInfoI();
        infoNew.setEntityType(ode.rtypes.rstring(Session.class.getName()));
        infoNew.setEntityId(ode.rtypes.rlong(sessionId));
        infoNew.setUuid(ode.rtypes.rstring(UUID.randomUUID().toString()));
        Assert.assertNotEquals(infoOld.getUuid().getValue(), infoNew.getUuid().getValue());
        session.getDetails().setExternalInfo(infoNew);
        try {
            iUpdate.saveObject(session);
            Assert.fail("the user cannot change the external info of a session");
        } catch (ValidationException ve) {
            /* expected */
        }
        /* as the normal user, check that the session external info UUID remains as set by root */
        session = (Session) iQuery.findByQuery(hql, params);
        info = session.getDetails().getExternalInfo();
        Assert.assertEquals(info.getUuid().getValue(), infoOld.getUuid().getValue());
        /* as the normal user, attempt to set the session external info UUID to something else */
        info.setUuid(infoNew.getUuid());
        iUpdate.saveObject(info);
        /* as the normal user, check that the session external info UUID remains as set by root */
        session = (Session) iQuery.findByQuery(hql, params);
        info = session.getDetails().getExternalInfo();
        Assert.assertEquals(info.getUuid().getValue(), infoOld.getUuid().getValue());
        /* as root, check that there is external info with UUID as set by root */
        Assert.assertNotNull(iQueryRoot.findByString("ExternalInfo", "uuid", infoOld.getUuid().getValue()));
        /* as root, check that there is not external info with UUID as set by the user */
        Assert.assertNull(iQueryRoot.findByString("ExternalInfo", "uuid", infoNew.getUuid().getValue()));
        /* as root, try setting the external info UUID just as the user first did */
        session = (Session) iQueryRoot.findByQuery(hql, params);
        session.getDetails().setExternalInfo(infoNew);
        iUpdateRoot.saveObject(session);
        /* as the normal user, check that the session external info UUID is now changed */
        session = (Session) iQuery.findByQuery(hql, params);
        info = session.getDetails().getExternalInfo();
        Assert.assertEquals(info.getUuid().getValue(), infoNew.getUuid().getValue());
        /* clean up after test */
        final Request delete = Requests.delete().target(info).build();
        doChange(root, root.getSession(), delete, true);
    }

    /**
     * Test that the update service cannot be used to create a file that is in a specific repository.
     * @throws Exception expects {@link ServerError} to be thrown in the file creation attempt
     */
    @Test(expectedExceptions = ServerError.class)
    public void testFileRepoPropertyNewInstance() throws Exception {
        newUserAndGroup("rwr---");
        OriginalFile file = mmFactory.createOriginalFile();
        file.setRepo(ode.rtypes.rstring("test repo"));
        iUpdate.saveObject(file);
    }

    /**
     * Test that the update service cannot be used to move a file into a specific repository.
     * @throws Exception expects {@link ServerError} to be thrown in the file adjustment attempt
     */
    @Test(expectedExceptions = ServerError.class)
    public void testFileRepoPropertyExistingInstance() throws Exception {
        newUserAndGroup("rwr---");
        OriginalFile file = mmFactory.createOriginalFile();
        file = (OriginalFile) iUpdate.saveAndReturnObject(file);
        file.setRepo(ode.rtypes.rstring("test repo"));
        iUpdate.saveObject(file);
    }

    /**
     * Test that after creating a directory in a repository then the directory's {@code repo} property correctly reflects that
     * repository.
     * @throws Exception unexpected
     */
    @Test
    public void testFileRepoPropertyCorrect() throws Exception {
        newUserAndGroup("rwr---");
        final RepositoryMap repositories = factory.sharedResources().repositories();
        int repoIndex;
        for (repoIndex = 0; !ScriptRepoHelper.SCRIPT_REPO.equals(repositories.descriptions.get(repoIndex).getHash().getValue());
                repoIndex++);
        final RepositoryPrx repo =  repositories.proxies.get(repoIndex);
        final String userDirectory = "Test_" + getClass().getName() + '_' + UUID.randomUUID();
        repo.makeDir("/" + userDirectory, false);
        final OriginalFile file = (OriginalFile) iQuery.findByString("OriginalFile", "name", userDirectory);
        Assert.assertEquals(file.getRepo().getValue(), ScriptRepoHelper.SCRIPT_REPO);
    }
}
