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
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import ode.RType;
import ode.ServerError;
import ode.api.IAdminPrx;
import ode.api.IContainerPrx;
import ode.api.IQueryPrx;
import ode.api.IUpdatePrx;
import ode.api.ServiceFactoryPrx;
import ode.model.CommentAnnotation;
import ode.model.CommentAnnotationI;
import ode.model.Dataset;
import ode.model.DatasetAnnotationLinkI;
import ode.model.DatasetImageLink;
import ode.model.DatasetImageLinkI;
import ode.model.Experimenter;
import ode.model.ExperimenterGroup;
import ode.model.ExperimenterGroupI;
import ode.model.ExperimenterI;
import ode.model.Fileset;
import ode.model.Folder;
import ode.model.IObject;
import ode.model.Image;
import ode.model.ImagingEnvironment;
import ode.model.Instrument;
import ode.model.Objective;
import ode.model.ObjectiveSettings;
import ode.model.PermissionsI;
import ode.model.Plate;
import ode.model.PlateAcquisition;
import ode.model.PlateAcquisitionAnnotationLinkI;
import ode.model.PlateAnnotationLinkI;
import ode.model.Project;
import ode.model.ProjectAnnotationLinkI;
import ode.model.ProjectDatasetLink;
import ode.model.ProjectDatasetLinkI;
import ode.model.Screen;
import ode.model.ScreenAnnotationLinkI;
import ode.model.ScreenPlateLink;
import ode.model.ScreenPlateLinkI;
import ode.model.StageLabel;
import ode.sys.EventContext;
import ode.sys.Parameters;
import ode.sys.ParametersI;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.math.IntMath;

import ode.gateway.model.DatasetData;
import ode.gateway.model.ImageData;
import ode.gateway.model.PixelsData;
import ode.gateway.model.PlateAcquisitionData;
import ode.gateway.model.PlateData;
import ode.gateway.model.ProjectData;
import ode.gateway.model.ScreenData;

/**
 * Collections of tests for the <code>IContainer</code> service.
 */
public class PojosServiceTest extends AbstractServerTest {

    /** Reference to class used to create data object. */
    CreatePojosFixture2 fixture;

    /**
     * Makes sure that the pixels set is loaded.
     *
     * @param pixels
     *            The pixels to handle.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    private void checkPixels(PixelsData pixels) throws Exception {
        Assert.assertNotNull(pixels);
        Assert.assertNotNull(pixels.getPixelSizeX(null));
        Assert.assertNotNull(pixels.getPixelSizeY(null));
        Assert.assertNotNull(pixels.getPixelSizeZ(null));
        Assert.assertNotNull(pixels.getPixelType());
        Assert.assertNotNull(pixels.getImage());
        Assert.assertNotNull(pixels.getOwner());
        Assert.assertNotNull(pixels.getSizeC());
        Assert.assertNotNull(pixels.getSizeT());
        Assert.assertNotNull(pixels.getSizeZ());
        Assert.assertNotNull(pixels.getSizeY());
        Assert.assertNotNull(pixels.getSizeX());
    }

    /**
     * Initializes the various services.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Override
    @BeforeClass
    protected void setUp() throws Exception {
        super.setUp();
        fixture = CreatePojosFixture2.withNewUser(root);
        fixture.createAllPojos();
    }

    /**
     * Test to load container hierarchy with project specified.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testLoadContainerHierarchyProjectSpecified() throws Exception {
        // first create a project
        Project p = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());

        // link the 2
        ProjectDatasetLink link = new ProjectDatasetLinkI();
        link.setParent(p);
        link.setChild(d);
        iUpdate.saveAndReturnObject(link);
        //
        Parameters param = new ParametersI();
        List<Long> ids = new ArrayList<Long>();
        ids.add(p.getId().getValue());
        List results = factory.getContainerService().loadContainerHierarchy(
                Project.class.getName(), ids, param);
        Assert.assertEquals(results.size(), 1);
        Iterator i = results.iterator();
        ProjectData project;
        Set<DatasetData> datasets;
        Iterator<DatasetData> j;
        DatasetData dataset;
        while (i.hasNext()) {
            project = new ProjectData((Project) i.next());
            Assert.assertEquals(project.getId(), p.getId().getValue());
            datasets = project.getDatasets();
            Assert.assertEquals(datasets.size(), 1);
            j = datasets.iterator();
            while (j.hasNext()) {
                dataset = j.next();
                Assert.assertEquals(dataset.getId(), d.getId().getValue());
            }
        }
    }

    /**
     * Test to load container hierarchy with screen specified.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testLoadContainerHierarchyScreenSpecified() throws Exception {
        // first create a project
        Screen p = (Screen) iUpdate.saveAndReturnObject(mmFactory
                .simpleScreenData().asIObject());
        Plate d = (Plate) iUpdate.saveAndReturnObject(mmFactory
                .simplePlateData().asIObject());

        // link the 2
        ScreenPlateLink link = new ScreenPlateLinkI();
        link.setParent(p);
        link.setChild(d);
        iUpdate.saveAndReturnObject(link);
        //
        Parameters param = new ParametersI();
        List<Long> ids = new ArrayList<Long>();
        ids.add(p.getId().getValue());
        List results = factory.getContainerService().loadContainerHierarchy(
                Screen.class.getName(), ids, param);
        Assert.assertEquals(results.size(), 1);
        Iterator i = results.iterator();
        ScreenData screen;
        Set<PlateData> plates;
        Iterator<PlateData> j;
        PlateData plate;
        while (i.hasNext()) {
            screen = new ScreenData((Screen) i.next());
            Assert.assertEquals(screen.getId(), p.getId().getValue());
            plates = screen.getPlates();
            Assert.assertEquals(plates.size(), 1);
            j = plates.iterator();
            while (j.hasNext()) {
                plate = j.next();
                Assert.assertEquals(plate.getId(), d.getId().getValue());
            }
        }
    }

    /**
     * Test to load container hierarchy with screen specified.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testLoadContainerHierarchyScreenWithPlateAndPlateAcquisitionSpecified()
            throws Exception {
        // first create a project
        Screen p = (Screen) iUpdate.saveAndReturnObject(mmFactory
                .simpleScreenData().asIObject());
        Plate d = (Plate) iUpdate.saveAndReturnObject(mmFactory
                .simplePlateData().asIObject());
        PlateAcquisition pa = (PlateAcquisition) mmFactory
                .simplePlateAcquisitionData().asIObject();
        pa.setPlate(d);
        pa = (PlateAcquisition) iUpdate.saveAndReturnObject(pa);

        // link the 2
        ScreenPlateLink link = new ScreenPlateLinkI();
        link.setParent(p);
        link.setChild(d);
        iUpdate.saveAndReturnObject(link);

        Parameters param = new ParametersI();
        List<Long> ids = new ArrayList<Long>();
        ids.add(p.getId().getValue());
        List results = factory.getContainerService().loadContainerHierarchy(
                Screen.class.getName(), ids, param);
        Assert.assertEquals(results.size(), 1);
        Iterator i = results.iterator();
        ScreenData screen;
        Set<PlateData> plates;
        Iterator<PlateData> j;
        PlateData plate;
        while (i.hasNext()) {
            screen = new ScreenData((Screen) i.next());
            Assert.assertEquals(screen.getId(), p.getId().getValue());
            plates = screen.getPlates();
            Assert.assertEquals(plates.size(), 1);
            j = plates.iterator();
            while (j.hasNext()) {
                plate = j.next();
                Assert.assertEquals(plate.getId(), d.getId().getValue());
            }
        }
    }

    /**
     * Test to load container hierarchy with no project specified, no orphan
     * loaded
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testLoadContainerHierarchyNoProjectSpecified() throws Exception {
        // first create a project
        long self = factory.getAdminService().getEventContext().userId;
        Project p = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());
        Project p2 = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());

        ParametersI param = new ParametersI();
        param.exp(ode.rtypes.rlong(self));
        List results = factory.getContainerService().loadContainerHierarchy(
                Project.class.getName(), new ArrayList(), param);
        Assert.assertFalse(results.isEmpty());
        Iterator i = results.iterator();
        int count = 0;
        IObject object;
        while (i.hasNext()) {
            object = (IObject) i.next();
            if (!(object instanceof Project)) {
                count++;
            }
        }
        Assert.assertEquals(count, 0);
    }

    /**
     * Test to load container hierarchy with no screen specified, no orphan
     * loaded
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testLoadContainerHierarchyNoScreenSpecified() throws Exception {
        // first create a screen
        long self = factory.getAdminService().getEventContext().userId;
        Screen p = (Screen) iUpdate.saveAndReturnObject(mmFactory
                .simpleScreenData().asIObject());
        Screen p2 = (Screen) iUpdate.saveAndReturnObject(mmFactory
                .simpleScreenData().asIObject());

        ParametersI param = new ParametersI();
        param.exp(ode.rtypes.rlong(self));
        List results = factory.getContainerService().loadContainerHierarchy(
                Screen.class.getName(), new ArrayList(), param);
        Assert.assertFalse(results.isEmpty());
        Iterator i = results.iterator();
        int count = 0;
        IObject object;
        while (i.hasNext()) {
            object = (IObject) i.next();
            if (!(object instanceof Screen)) {
                count++;
            }
        }
        Assert.assertEquals(count, 0);
    }

    /**
     * Test to load container hierarchy with project specified, with orphan
     * loaded
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testLoadContainerHierarchyNoProjectSpecifiedWithOrphan()
            throws Exception {
        // first create a project
        long self = factory.getAdminService().getEventContext().userId;
        Project p = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        Project p2 = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());

        ParametersI param = new ParametersI();
        param.exp(ode.rtypes.rlong(self));
        param.orphan();
        List results = factory.getContainerService().loadContainerHierarchy(
                Project.class.getName(), new ArrayList(), param);
        Assert.assertFalse(results.isEmpty());
        Iterator i = results.iterator();
        IObject object;
        int value = 0;
        while (i.hasNext()) {
            object = (IObject) i.next();
            if (object instanceof Dataset) {
                if (object.getId().getValue() == d.getId().getValue()) {
                    value++;
                }
            }
        }
        Assert.assertEquals(value, 1);
    }

    /**
     * Test to load container hierarchy with project specified, with orphan
     * loaded
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testLoadContainerHierarchyNoScreenSpecifiedWithOrphan()
            throws Exception {
        // first create a project
        long self = factory.getAdminService().getEventContext().userId;
        Screen p = (Screen) iUpdate.saveAndReturnObject(mmFactory
                .simpleScreenData().asIObject());
        Plate d = (Plate) iUpdate.saveAndReturnObject(mmFactory
                .simplePlateData().asIObject());

        ParametersI param = new ParametersI();
        param.exp(ode.rtypes.rlong(self));
        param.orphan();
        List results = factory.getContainerService().loadContainerHierarchy(
                Screen.class.getName(), new ArrayList(), param);
        Assert.assertFalse(results.isEmpty());
        Iterator i = results.iterator();
        IObject object;
        int value = 0;
        while (i.hasNext()) {
            object = (IObject) i.next();
            if (object instanceof Plate) {
                if (object.getId().getValue() == d.getId().getValue()) {
                    value++;
                }
            }
        }
        Assert.assertEquals(value, 1);
    }

    /**
     * Test to load container hierarchy with dataset specified and loads the
     * images.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testLoadContainerHierarchyDatasetSpecifiedAndLeaves()
            throws Exception {
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        Image img = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());
        // link the 2
        DatasetImageLink link = new DatasetImageLinkI();
        link.setParent(d);
        link.setChild(img);
        iUpdate.saveAndReturnObject(link);
        ParametersI param = new ParametersI();
        param.leaves();
        List<Long> ids = new ArrayList<Long>();
        ids.add(d.getId().getValue());
        List results = factory.getContainerService().loadContainerHierarchy(
                Dataset.class.getName(), ids, param);
        Assert.assertEquals(results.size(), 1);
        Iterator i = results.iterator();
        DatasetData dataset;
        Set<ImageData> images;
        Iterator<ImageData> j;
        ImageData image;
        while (i.hasNext()) {
            dataset = new DatasetData((Dataset) i.next());
            if (dataset.getId() == d.getId().getValue()) {
                images = dataset.getImages();
                Assert.assertEquals(images.size(), 1);
                j = images.iterator();
                while (j.hasNext()) {
                    image = j.next();
                    Assert.assertNotNull(image.asImage().getDetails().getUpdateEvent());
                    Assert.assertTrue(image.asImage().getDetails().getUpdateEvent()
                            .isLoaded());
                    Assert.assertEquals(image.getId(), img.getId().getValue());
                }
            }
        }
    }

    /**
     * Test to load container hierarchy with dataset specified and loads the
     * images. We then make sure that the default pixels are loaded.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = { "ticket:401", "ticket:221" })
    public void testLoadContainerHierarchyDatasetSpecifiedAndLeavesWithDefaultPixels()
            throws Exception {
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());
        // link the 2
        DatasetImageLink link = new DatasetImageLinkI();
        link.setParent(d);
        link.setChild(img);
        iUpdate.saveAndReturnObject(link);
        ParametersI param = new ParametersI();
        param.leaves();
        List<Long> ids = new ArrayList<Long>();
        ids.add(d.getId().getValue());
        List results = factory.getContainerService().loadContainerHierarchy(
                Dataset.class.getName(), ids, param);
        Assert.assertEquals(results.size(), 1);
        Iterator i = results.iterator();
        DatasetData dataset;
        Set<ImageData> images;
        Iterator<ImageData> j;
        ImageData image;
        PixelsData pixelsData;
        while (i.hasNext()) {
            dataset = new DatasetData((Dataset) i.next());
            if (dataset.getId() == d.getId().getValue()) {
                images = dataset.getImages();
                Assert.assertEquals(images.size(), 1);
                j = images.iterator();
                while (j.hasNext()) {
                    image = j.next();
                    checkPixels(image.getDefaultPixels());
                }
            }
        }
    }

    /**
     * Test to the collection count method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testCollectionCountForDataset() throws Exception {
        Dataset d1 = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        Dataset d2 = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        Image i = (Image) iUpdate.saveAndReturnObject(mmFactory.simpleImage());
        // link the d and i
        DatasetImageLink link = new DatasetImageLinkI();
        link.setParent(d1);
        link.setChild(i);
        iUpdate.saveAndReturnObject(link);
        Parameters p = new ParametersI();
        List<Long> ids = new ArrayList<Long>();
        ids.add(d1.getId().getValue());
        ids.add(d2.getId().getValue());
        Map m = factory.getContainerService().getCollectionCount(
                Dataset.class.getName(),
                DatasetData.IMAGE_LINKS, ids, p);
        Long v = (Long) m.get(d1.getId().getValue());
        Assert.assertEquals(v.longValue(), 1);
        v = (Long) m.get(d2.getId().getValue());
        Assert.assertEquals(v.longValue(), 0);
    }

    /**
     * Tests the retrieval of images filtering by owners.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:318")
    public void testGetImagesByOwner() throws Exception {
        Image i1 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        DatasetImageLink link = new DatasetImageLinkI();
        link.setParent(d);
        link.setChild(i1);
        iUpdate.saveAndReturnObject(link);
        ParametersI param = new ParametersI();
        List<Long> ids = new ArrayList<Long>(1);
        ids.add(d.getId().getValue());
        List<Image> images = factory.getContainerService().getImages(Dataset.class.getName(), ids,
                param);
        Assert.assertFalse(images.isEmpty());
        Iterator<Image> i = images.iterator();
        Image img;
        int count = 0;
        while (i.hasNext()) {
            img = i.next();
            if (img.getId().getValue() == i1.getId().getValue())
                count++;

        }
        Assert.assertEquals(count, 1);
        param = new ParametersI();
        param.exp(ode.rtypes.rlong(fixture.e.getId().getValue()));
        images = factory.getContainerService().getImages(
                Dataset.class.getName(), ids, param);
        Assert.assertEquals(images.size(), 0);
    }

    /**
     * Tests the retrieval of images filtering by owners. Those images will have
     * a pixels set.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:318")
    public void testGetImagesByOwnerWithPixels() throws Exception {
        Image i1 = (Image) iUpdate.saveAndReturnObject(mmFactory.createImage());
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        DatasetImageLink link = new DatasetImageLinkI();
        link.setParent(d);
        link.setChild(i1);
        iUpdate.saveAndReturnObject(link);
        ParametersI param = new ParametersI();
        List<Long> ids = new ArrayList<Long>(1);
        ids.add(d.getId().getValue());
        List<Image> images = factory.getContainerService().getImages(
                Dataset.class.getName(), ids, param);
        Assert.assertFalse(images.isEmpty());
        Iterator<Image> i = images.iterator();
        Image img;
        int count = 0;
        PixelsData pixelsData;
        while (i.hasNext()) {
            img = i.next();
            if (img.getId().getValue() == i1.getId().getValue()) {
                pixelsData = new PixelsData(img.getPixels(0));
                checkPixels(pixelsData);
                count++;
            }
        }
        Assert.assertEquals(count, 1);
        param = new ParametersI();
        param.exp(ode.rtypes.rlong(fixture.e.getId().getValue()));
        images = factory.getContainerService().getImages(Dataset.class.getName(),
                ids, param);
        Assert.assertEquals(images.size(), 0);
    }

    /**
     * Links twice a dataset and an image. Only one link should be inserted.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:318")
    public void testDuplicateDatasetImageLink() throws Exception {
        Image i1 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        DatasetImageLink link = new DatasetImageLinkI();
        link.setParent(d);
        link.setChild(i1);
        iUpdate.saveAndReturnObject(link);
        link = new DatasetImageLinkI();
        link.setParent(d);
        link.setChild(i1);
        try {
            iUpdate.saveAndReturnObject(link);
            Assert.fail("Should not be able to insert twice.");
        } catch (Exception e) {
        }
        String sql = "select link from DatasetImageLink as link where "
                + "link.parent.id = :parentID and link.child.id = :childID";

        ParametersI param = new ParametersI();
        param.map = new HashMap<String, RType>();
        param.map.put("parentID", d.getId());
        param.map.put("childID", i1.getId());
        List l = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(l.size(), 1);
    }

    /**
     * Tests the retrieval of images filtering by groups.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:318")
    public void testGetImagesByGroup() throws Exception {
        // Create 2 groups and add a user
        String uuid1 = UUID.randomUUID().toString();
        ExperimenterGroup g1 = new ExperimenterGroupI();
        g1.setName(ode.rtypes.rstring(uuid1));
        g1.setLdap(ode.rtypes.rbool(false));
        g1.getDetails().setPermissions(new PermissionsI("rw----"));

        String uuid2 = UUID.randomUUID().toString();
        ExperimenterGroup g2 = new ExperimenterGroupI();
        g2.setName(ode.rtypes.rstring(uuid2));
        g2.setLdap(ode.rtypes.rbool(false));
        g2.getDetails().setPermissions(new PermissionsI("rw----"));

        IAdminPrx svc = root.getSession().getAdminService();
        IQueryPrx query = root.getSession().getQueryService();
        long id1 = svc.createGroup(g1);
        long id2 = svc.createGroup(g2);

        ParametersI p = new ParametersI();
        p.addId(id1);

        ExperimenterGroup eg1 = (ExperimenterGroup) query.findByQuery(
                "select distinct g from ExperimenterGroup g where g.id = :id",
                p);
        p = new ParametersI();
        p.addId(id2);

        ExperimenterGroup eg2 = (ExperimenterGroup) query.findByQuery(
                "select distinct g from ExperimenterGroup g where g.id = :id",
                p);
        Experimenter e = new ExperimenterI();
        e.setOdeName(ode.rtypes.rstring(uuid1));
        e.setFirstName(ode.rtypes.rstring("user"));
        e.setLastName(ode.rtypes.rstring("user"));
        e.setLdap(ode.rtypes.rbool(false));

        List<ExperimenterGroup> groups = new ArrayList<ExperimenterGroup>();
        // method tested elsewhere
        groups.add(eg1);
        groups.add(eg2);
        long uid = newUserInGroupWithPassword(e, groups, uuid1);
        svc.setDefaultGroup(svc.getExperimenter(uid), eg1);

        ode.client client = new ode.client();
        ServiceFactoryPrx f = client.createSession(uuid1, uuid1);
        // add an image.
        IUpdatePrx update = f.getUpdateService();
        Dataset d = (Dataset) update.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        long d1 = d.getId().getValue();

        Image image1 = (Image) update.saveAndReturnObject(mmFactory
                .simpleImage());
        // link the 2
        DatasetImageLink link = new DatasetImageLinkI();
        link.setParent(d);
        link.setChild(image1);
        update.saveAndReturnObject(link);

        // Change the security context
        client.getSession().setSecurityContext(
                new ExperimenterGroupI(id2, false));
        // add an image.
        d = (Dataset) update.saveAndReturnObject(mmFactory.simpleDatasetData()
                .asIObject());
        long d2 = d.getId().getValue();
        Image image2 = (Image) f.getUpdateService().saveAndReturnObject(
                mmFactory.simpleImage());
        link = new DatasetImageLinkI();
        link.setParent(d);
        link.setChild(image2);
        f.getUpdateService().saveAndReturnObject(link);
        List<Long> ids = new ArrayList<Long>();
        ids.add(d1);
        ids.add(d2);
        List<Image> images = f.getContainerService().getImages(
                Dataset.class.getName(), ids, p);
        Assert.assertNotNull(images);
        Assert.assertEquals(images.size(), 1);
        Iterator<Image> i = images.iterator();

        // Should only retrieve images from group2
        while (i.hasNext()) {
            Assert.assertEquals(i.next().getId().getValue(), image2.getId().getValue());
        }

        client.getSession().setSecurityContext(
                new ExperimenterGroupI(id1, false));
        images = f.getContainerService().getImages(Dataset.class.getName(),
                ids, p);
        Assert.assertNotNull(images);
        Assert.assertEquals(images.size(), 1);
        i = images.iterator();

        // Should only retrieve images from group2
        while (i.hasNext()) {
            Assert.assertEquals(i.next().getId().getValue(), image1.getId().getValue());
        }
    }

    /**
     * Tests that the pagination works correctly for
     * {@link ome.api.IContainer#getImages(Class, Set, ome.parameters.Parameters)}
     * .
     *
     * @throws Exception
     *             unexpected
     */
    @Test(groups = "ticket:9934")
    public void testGetImagesPaged() throws Exception {
        final int totalNumberOfImages = 12;
        /* create a new dataset containing new images */
        final long datasetId = iUpdate
                .saveAndReturnObject(mmFactory.simpleDatasetData().asIObject())
                .getId().getValue();
        final List<Long> datasetIdList = Collections
                .<Long> singletonList(datasetId);
        final Set<Long> imageIds = new HashSet<Long>(totalNumberOfImages);
        for (int i = 0; i < totalNumberOfImages; i++) {
            final Image image = (Image) iUpdate.saveAndReturnObject(mmFactory
                    .createImage());
            imageIds.add(image.getId().getValue());
            final DatasetImageLink dil = new DatasetImageLinkI();
            dil.setParent((Dataset) iQuery.find(Dataset.class.getName(),
                    datasetId));
            dil.setChild(image);
            iUpdate.saveObject(dil);
        }
        /* check that the resulting image IDs are unique */
        Assert.assertEquals(totalNumberOfImages, imageIds.size(),
                "image IDs should be unique");
        /*
         * try various page sizes, make sure the total results set is as
         * expected
         */
        for (int pageSize = 1; pageSize < totalNumberOfImages + 2; pageSize++) {
            /* note the IDs found by this set of pages */
            final Set<Long> imageIdsPaged = new HashSet<Long>(
                    totalNumberOfImages);
            boolean nextIsEmpty = false;
            int startImageIndex = 0;
            while (true) {
                /* per page */
                final ParametersI parameters = new ParametersI().page(
                        startImageIndex, pageSize);
                final List<Image> pageOfImages = factory.getContainerService().getImages(
                        Dataset.class.getName(), datasetIdList, parameters);
                if (nextIsEmpty) {
                    Assert.assertTrue(pageOfImages.isEmpty(),
                            "expected empty pages after an undersized page");
                } else {
                    nextIsEmpty = pageOfImages.size() < pageSize;
                }
                if (pageOfImages.isEmpty()) {
                    break;
                }
                for (final Image image : pageOfImages) {
                    Assert.assertTrue(
                            imageIdsPaged.add(image.getId().getValue()),
                            "paged query should not return duplicates");
                    Assert.assertTrue(image.getFormat().isLoaded());
                }
                startImageIndex += pageSize;
            }
            /* ensure that exactly the expected image IDs are returned */
            Assert.assertEquals(imageIdsPaged, imageIds,
                    "paged query should cumulatively return same results as when unpaged");
        }
    }

    /**
     * Tests the finding of projects filtering by owners.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testFindContainerHierarchiesProjectAsRootFilterByOwner()
            throws Exception {
        long id = fixture.e.getId().getValue();
        ParametersI param = new ParametersI();
        param.leaves();
        param.exp(ode.rtypes.rlong(id));

        Image i = (Image) iUpdate.saveAndReturnObject(mmFactory.simpleImage());
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        Project p = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());
        // link dataset and image
        DatasetImageLink link = new DatasetImageLinkI();
        link.setParent(d);
        link.setChild(i);
        iUpdate.saveAndReturnObject(link);
        // link project and dataset
        ProjectDatasetLink l = new ProjectDatasetLinkI();
        l.setParent(p);
        l.setChild(d);
        iUpdate.saveAndReturnObject(l);

        List<Long> ids = new ArrayList<Long>(1);
        ids.add(i.getId().getValue());
        // Should have one project.
        List results = factory.getContainerService().findContainerHierarchies(
                Project.class.getName(), ids, param);
        Assert.assertEquals(results.size(), 1);
        Image pp = (Image) results.get(0);
        Assert.assertEquals(pp.getId().getValue(), i.getId().getValue());
    }

    /**
     * Tests the retrieval of a projects filtering by owners.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:318")
    public void testLoadContainerHierarchyFilterByOwner() throws Exception {
        long id = fixture.e.getId().getValue();
        Project p = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());
        ParametersI param = new ParametersI();
        param.exp(ode.rtypes.rlong(id));

        List<Long> ids = fixture.getProjectIds();
        List results = factory.getContainerService().loadContainerHierarchy(
                Project.class.getName(), new ArrayList<Long>(), param);
        Iterator i = results.iterator();
        IObject object;
        int value = 0;
        while (i.hasNext()) {
            object = (IObject) i.next();
            if (p.getId().getValue() == object.getId().getValue())
                value++;
        }
        Assert.assertEquals(value, 0);
    }

    /**
     * Tests the retrieval of a projects filtering by groups.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:318")
    public void testLoadContainerHierarchyFilterByGroup() throws Exception {
        long id = fixture.g.getId().getValue();
        Project p = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());
        ParametersI param = new ParametersI();
        param.grp(ode.rtypes.rlong(id));

        List<Long> ids = fixture.getProjectIds();
        List results = factory.getContainerService().loadContainerHierarchy(
                Project.class.getName(), new ArrayList<Long>(), param);
        Iterator i = results.iterator();
        IObject object;
        int value = 0;
        while (i.hasNext()) {
            object = (IObject) i.next();
            if (p.getId().getValue() == object.getId().getValue())
                value++;
        }
        Assert.assertEquals(value, 0);
    }

    /**
     * Tests the retrieval of images created during a given period.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:318")
    public void testGetImagesByOptions() throws Exception {
        GregorianCalendar gc = new GregorianCalendar();
        gc = new GregorianCalendar(gc.get(Calendar.YEAR),
                gc.get(Calendar.MONTH), gc.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        long startTime = gc.getTime().getTime();
        ParametersI po = new ParametersI();
        po.leaves();
        po.startTime(ode.rtypes.rtime(startTime - 1));
        Image i = mmFactory.simpleImage();
        i.setAcquisitionDate(ode.rtypes.rtime(startTime));
        i = (Image) iUpdate.saveAndReturnObject(i);

        List result = factory.getContainerService().getImagesByOptions(po);
        Assert.assertFalse(result.isEmpty());
        Iterator j = result.iterator();
        int count = 0;
        IObject object;
        Image img;
        int value = 0;
        while (j.hasNext()) {
            object = (IObject) j.next();
            if (object instanceof Image) {
                img = (Image) object;
                if (img.getId().getValue() == i.getId().getValue())
                    value++;
                Assert.assertTrue(img.getFormat().isLoaded());
                count++;
            }
        }
        Assert.assertEquals(result.size(), count);
        Assert.assertEquals(value, 1);
        //
        gc = new GregorianCalendar(gc.get(Calendar.YEAR),
                gc.get(Calendar.MONTH), gc.get(Calendar.DAY_OF_MONTH), 23, 59,
                59);
        startTime = gc.getTime().getTime();
        po = new ParametersI();
        po.leaves();
        po.startTime(ode.rtypes.rtime(startTime));
        result = factory.getContainerService().getImagesByOptions(po);
        Assert.assertEquals(result.size(), 0);
    }

    /**
     * Test to load container hierarchy with dataset as root and no leaves flag
     * turned on.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:907")
    public void testLoadContainerHierarchyDatasetLeavesNotLoaded()
            throws Exception {
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        Image img = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());
        // link the 2
        DatasetImageLink link = new DatasetImageLinkI();
        link.setParent(d);
        link.setChild(img);
        iUpdate.saveAndReturnObject(link);
        ParametersI param = new ParametersI();
        param.noLeaves();
        List<Long> ids = new ArrayList<Long>();
        List results = factory.getContainerService().loadContainerHierarchy(
                Dataset.class.getName(), ids, param);
        Assert.assertFalse(results.isEmpty());
        Iterator i = results.iterator();
        DatasetData dataset;
        Set<ImageData> images;
        Iterator<ImageData> j;
        ImageData image;
        while (i.hasNext()) {
            dataset = new DatasetData((Dataset) i.next());
            if (dataset.getId() == d.getId().getValue()) {
                images = dataset.getImages();
                Assert.assertNull(images);
            }
        }

        // now check if the image is correctly loaded
        param = new ParametersI();
        param.leaves();
        results = factory.getContainerService().loadContainerHierarchy(
                Dataset.class.getName(),
                ids, param);
        Assert.assertFalse(results.isEmpty());
        i = results.iterator();
        while (i.hasNext()) {
            dataset = new DatasetData((Dataset) i.next());
            if (dataset.getId() == d.getId().getValue()) {
                images = dataset.getImages();
                Assert.assertFalse(images.isEmpty());
            }
        }
    }

    /**
     * Test to load an image with its acquisition data. This method invoked the
     * <code>getImages</code>.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testLoadImageWithAcquisitionData() throws Exception {
        // First create an image
        Image image = mmFactory.createImage();
        image = (Image) iUpdate.saveAndReturnObject(image);
        // create an instrument
        Instrument instrument = (Instrument) iUpdate
                .saveAndReturnObject(mmFactory
                        .createInstrument(ModelMockFactory.LASER));
        ParametersI param = new ParametersI();
        param.addLong("iid", instrument.getId().getValue());
        String sql = "select d from Objective as d where d.instrument.id = :iid";
        Objective objective = (Objective) iQuery.findByQuery(sql, param);
        // create so settings.
        ObjectiveSettings settings = (ObjectiveSettings) iUpdate
                .saveAndReturnObject(mmFactory
                        .createObjectiveSettings(objective));
        Assert.assertNotNull(settings);
        image.setObjectiveSettings(settings);
        StageLabel label = (StageLabel) iUpdate.saveAndReturnObject(mmFactory
                .createStageLabel());
        image.setStageLabel(label);
        ImagingEnvironment env = (ImagingEnvironment) iUpdate
                .saveAndReturnObject(mmFactory.createImageEnvironment());
        image.setImagingEnvironment(env);
        iUpdate.saveAndReturnObject(image);
        ParametersI po = new ParametersI();
        po.acquisitionData();
        List<Long> ids = new ArrayList<Long>(1);
        ids.add(image.getId().getValue());
        List results = factory.getContainerService().getImages(Image.class.getName(), ids, param);
        Assert.assertNotNull(results);
        Assert.assertEquals(1, results.size());
        // Check if acquisition data are loaded.
        Image test = (Image) results.get(0);
        Assert.assertNotNull(test);
        Assert.assertEquals(test.getId().getValue(), image.getId().getValue());
        Assert.assertNotNull(test.getObjectiveSettings());
        Assert.assertNotNull(test.getImagingEnvironment());
        Assert.assertNotNull(test.getStageLabel());
        Assert.assertEquals(test.getObjectiveSettings().getId().getValue(), settings
                .getId().getValue());
        Assert.assertEquals(test.getImagingEnvironment().getId().getValue(), env
                .getId().getValue());
        Assert.assertEquals(test.getStageLabel().getId().getValue(), label.getId()
                .getValue());
    }

    /**
     * Test to load container hierarchy and make sure the annotations are
     * counted.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testLoadContainerHierarchyProjectDatasetWithAnnotations()
            throws Exception {
        // first create a project
        Project p = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        CommentAnnotation comment = new CommentAnnotationI();
        comment.setTextValue(ode.rtypes.rstring("comment Project"));
        comment = (CommentAnnotation) iUpdate.saveAndReturnObject(comment);
        // attach comment to Project
        ProjectAnnotationLinkI pal = new ProjectAnnotationLinkI();
        pal.setParent((Project) p.proxy());
        pal.setChild(comment);
        iUpdate.saveAndReturnObject(pal);
        comment = new CommentAnnotationI();
        comment.setTextValue(ode.rtypes.rstring("comment Dataset"));
        comment = (CommentAnnotation) iUpdate.saveAndReturnObject(comment);
        // attach comment to Project
        DatasetAnnotationLinkI dal = new DatasetAnnotationLinkI();
        dal.setParent((Dataset) d.proxy());
        dal.setChild(comment);
        iUpdate.saveAndReturnObject(dal);

        // link the 2
        ProjectDatasetLink link = new ProjectDatasetLinkI();
        link.setParent((Project) p.proxy());
        link.setChild((Dataset) d.proxy());
        iUpdate.saveAndReturnObject(link);
        Parameters param = new ParametersI();
        List<Long> ids = new ArrayList<Long>();
        ids.add(p.getId().getValue());
        List results = factory.getContainerService().loadContainerHierarchy(
                Project.class.getName(), ids, param);
        Assert.assertEquals(results.size(), 1);
        Iterator i = results.iterator();
        ProjectData project;
        Set<DatasetData> datasets;
        Iterator<DatasetData> j;
        DatasetData dataset;
        Map<Long, Long> count;
        Entry entry;
        Iterator k;
        while (i.hasNext()) {
            // use ode.gateway.model
            project = new ProjectData((Project) i.next());
            count = project.getAnnotationsCounts();
            Assert.assertEquals(1, count.size());
            datasets = project.getDatasets();
            k = count.entrySet().iterator();
            while (k.hasNext()) {
                entry = (Entry) k.next();
                Assert.assertEquals(((Long) entry.getValue()).longValue(), 1);

            }
            // Assert.assertTrue(count.containsKey(ctx.userId));
            // one annotation to project.
            // Assert.assertEquals(((Long) count.get(ctx.userId)).longValue(), 1);
            j = datasets.iterator();
            while (j.hasNext()) {
                dataset = j.next();
                count = dataset.getAnnotationsCounts();
                Assert.assertEquals(count.size(), 1);
                k = count.entrySet().iterator();
                while (k.hasNext()) {
                    entry = (Entry) k.next();
                    Assert.assertEquals(((Long) entry.getValue()).longValue(), 1);

                }
            }
        }
    }

    /**
     * Test to load container hierarchy and make sure the annotations are
     * counted.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testLoadContainerHierarchyScreenPlatePlateAcquisitionWithAnnotations()
            throws Exception {
        // first create a Screen/Plate/PlateAcquisition
        Screen s = (Screen) iUpdate.saveAndReturnObject(mmFactory
                .simpleScreenData().asIObject());
        Plate p = (Plate) iUpdate.saveAndReturnObject(mmFactory
                .simplePlateData().asIObject());
        PlateAcquisition a = (PlateAcquisition) mmFactory
                .simplePlateAcquisitionData().asIObject();
        a.setPlate(p);
        a = (PlateAcquisition) iUpdate.saveAndReturnObject(a);

        // Now create and attach comments to each
        CommentAnnotation comment = new CommentAnnotationI();
        comment.setTextValue(ode.rtypes.rstring("comment Screen"));
        comment = (CommentAnnotation) iUpdate.saveAndReturnObject(comment);
        // attach comment to Screen
        ScreenAnnotationLinkI sal = new ScreenAnnotationLinkI();
        sal.setParent((Screen) s.proxy());
        sal.setChild(comment);
        iUpdate.saveAndReturnObject(sal);

        comment = new CommentAnnotationI();
        comment.setTextValue(ode.rtypes.rstring("comment Plate"));
        comment = (CommentAnnotation) iUpdate.saveAndReturnObject(comment);
        // attach comment to Plate
        PlateAnnotationLinkI pal = new PlateAnnotationLinkI();
        pal.setParent((Plate) p.proxy());
        pal.setChild(comment);
        iUpdate.saveAndReturnObject(pal);

        comment = new CommentAnnotationI();
        comment.setTextValue(ode.rtypes.rstring("comment PlateAcquisition"));
        comment = (CommentAnnotation) iUpdate.saveAndReturnObject(comment);
        // attach comment to Plate
        PlateAcquisitionAnnotationLinkI aal = new PlateAcquisitionAnnotationLinkI();
        aal.setParent((PlateAcquisition) a.proxy());
        aal.setChild(comment);
        iUpdate.saveAndReturnObject(aal);

        // link the Screen and Plate
        ScreenPlateLink splink = new ScreenPlateLinkI();
        splink.setParent((Screen) s.proxy());
        splink.setChild((Plate) p.proxy());
        iUpdate.saveAndReturnObject(splink);

        Parameters param = new ParametersI();
        List<Long> ids = new ArrayList<Long>();
        ids.add(s.getId().getValue());
        List results = factory.getContainerService().loadContainerHierarchy(
                Screen.class.getName(), ids, param);
        Assert.assertEquals(results.size(), 1);
        Iterator i = results.iterator();
        ScreenData screen;
        Set<PlateData> plates;
        Set<PlateAcquisitionData> plateAcquisitions;
        Iterator<PlateData> j;
        Iterator<PlateAcquisitionData> l;
        PlateData plate;
        PlateAcquisitionData plateAcquisition;
        Map<Long, Long> count;
        Entry entry;
        Iterator k;
        while (i.hasNext()) {
            // use ode.gateway.model
            screen = new ScreenData((Screen) i.next());
            count = screen.getAnnotationsCounts();
            Assert.assertEquals(1, count.size());
            k = count.entrySet().iterator();
            while (k.hasNext()) {
                entry = (Entry) k.next();
                Assert.assertEquals(((Long) entry.getValue()).longValue(), 1);

            }
            plates = screen.getPlates();
            j = plates.iterator();
            while (j.hasNext()) {
                plate = j.next();
                count = plate.getAnnotationsCounts();
                Assert.assertEquals(1, count.size());
                k = count.entrySet().iterator();
                while (k.hasNext()) {
                    entry = (Entry) k.next();
                    Assert.assertEquals(((Long) entry.getValue()).longValue(), 1);

                }
                plateAcquisitions = plate.getPlateAcquisitions();
                l = plateAcquisitions.iterator();
                while (l.hasNext()) {
                    plateAcquisition = l.next();
                    count = plateAcquisition.getAnnotationsCounts();
                    Assert.assertEquals(1, count.size());
                    k = count.entrySet().iterator();
                    while (k.hasNext()) {
                        entry = (Entry) k.next();
                        Assert.assertEquals(((Long) entry.getValue()).longValue(), 1);
                    }
                }
            }
        }
    }

    /**
     * Test to find the P/D the specified images are in.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testFindContainerHierarchyFromProject() throws Exception {
        Project p = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        Image i = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        // link the project and the dataset
        ProjectDatasetLink l = new ProjectDatasetLinkI();
        l.setParent((Project) p.proxy());
        l.setChild((Dataset) d.proxy());
        iUpdate.saveAndReturnObject(l);

        DatasetImageLink link = new DatasetImageLinkI();
        link.setParent((Dataset) d.proxy());
        link.setChild((Image) i.proxy());
        iUpdate.saveAndReturnObject(link);

        ParametersI param = new ParametersI();
        List<Long> ids = new ArrayList<Long>();
        ids.add(i.getId().getValue());
        List results = factory.getContainerService().findContainerHierarchies(
                Project.class.getName(), ids, param);
        Assert.assertEquals(results.size(), 1);
        Project found = (Project) results.get(0);
        Assert.assertEquals(found.getId().getValue(), p.getId().getValue());
        ProjectData project = new ProjectData(found);
        Set<DatasetData> datasets = project.getDatasets();
        Assert.assertEquals(datasets.size(), 1);
        Iterator<DatasetData> j = datasets.iterator();
        DatasetData dataset;
        Set<ImageData> images;
        Iterator<ImageData> k;
        while (j.hasNext()) {
            dataset = j.next();
            Assert.assertEquals(dataset.getId(), d.getId().getValue());
            images = dataset.getImages();
            k = images.iterator();
            while (k.hasNext()) {
                Assert.assertEquals(k.next().getId(), i.getId().getValue());
            }
        }
    }

    /**
     * Test to find the P/D the specified images are in.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testFindContainerHierarchyFromDataset() throws Exception {
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        Image i = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        // link the project and the dataset
        DatasetImageLink link = new DatasetImageLinkI();
        link.setParent((Dataset) d.proxy());
        link.setChild((Image) i.proxy());
        iUpdate.saveAndReturnObject(link);

        ParametersI param = new ParametersI();
        List<Long> ids = new ArrayList<Long>();
        ids.add(i.getId().getValue());
        List results = factory.getContainerService().findContainerHierarchies(
                Project.class.getName(), ids, param);
        Assert.assertEquals(results.size(), 1);
        Dataset found = (Dataset) results.get(0);
        Assert.assertEquals(found.getId().getValue(), d.getId().getValue());

        DatasetData dataset = new DatasetData(found);
        Set<ImageData> images = dataset.getImages();
        Iterator<ImageData> k = images.iterator();
        while (k.hasNext()) {
            Assert.assertEquals(k.next().getId(), i.getId().getValue());
        }
    }

    /**
     * Test to find the P/D the specified images are in.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testFindContainerHierarchyFromImage() throws Exception {
        Image i = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());

        ParametersI param = new ParametersI();
        List<Long> ids = new ArrayList<Long>();
        ids.add(i.getId().getValue());
        List results = factory.getContainerService().findContainerHierarchies(
                Project.class.getName(), ids, param);
        Assert.assertEquals(results.size(), 1);
        Image found = (Image) results.get(0);
        Assert.assertEquals(found.getId().getValue(), i.getId().getValue());
    }

    /**
     * Test to find the P/D the specified images are in.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testFindContainerHierarchyWrongType() throws Exception {
        Image i = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());

        ParametersI param = new ParametersI();
        List<Long> ids = new ArrayList<Long>();
        ids.add(i.getId().getValue());
        try {
            factory.getContainerService().findContainerHierarchies(Dataset.class.getName(), ids,
                    param);
            Assert.fail("Only Project type is supported.");
        } catch (Exception e) {
        }
    }

    /**
     * Test to load container hierarchy with no plate specified loaded
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testLoadContainerHierarchyNoPlateSpecified() throws Exception {
        // first create a screen
        long self = factory.getAdminService().getEventContext().userId;
        Plate p = (Plate) iUpdate.saveAndReturnObject(mmFactory
                .simplePlateData().asIObject());
        Plate p2 = (Plate) iUpdate.saveAndReturnObject(mmFactory
                .simplePlateData().asIObject());

        ParametersI param = new ParametersI();
        param.exp(ode.rtypes.rlong(self));
        List results = factory.getContainerService().loadContainerHierarchy(
                Plate.class.getName(), new ArrayList(), param);
        Assert.assertFalse(results.isEmpty());
        Iterator i = results.iterator();
        int count = 0;
        IObject object;
        while (i.hasNext()) {
            object = (IObject) i.next();
            if (!(object instanceof Plate)) {
                count++;
            }
        }
        Assert.assertEquals(count, 0);
    }

    /**
     * Test to load container hierarchy with plate specified.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testLoadContainerHierarchyPlateSpecified() throws Exception {
        // first create a project
        Plate p = (Plate) iUpdate.saveAndReturnObject(mmFactory
                .simplePlateData().asIObject());

        Parameters param = new ParametersI();
        List<Long> ids = new ArrayList<Long>();
        ids.add(p.getId().getValue());
        List results = factory.getContainerService().loadContainerHierarchy(Plate.class.getName(),
                ids, param);
        Assert.assertEquals(results.size(), 1);
        Iterator i = results.iterator();
        PlateData plate;
        while (i.hasNext()) {
            plate = new PlateData((Plate) i.next());
            Assert.assertEquals(plate.getId(), p.getId().getValue());
        }
    }

    /**
     * Test the <code>getImagesBySplitFilesets</code> Image as root.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testGetImagesBySplitFilesetsImageAsRoot() throws Exception {
        // first create a project
        Image i1 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Image i2 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Fileset fileset = newFileset();
        fileset.addImage(i1);
        fileset.addImage(i2);
        fileset = (Fileset) iUpdate.saveAndReturnObject(fileset);
        // Check that 2 images are linked
        Assert.assertEquals(fileset.copyImages().size(), 2);

        Parameters param = new ParametersI();
        Map<String, List<Long>> map = new HashMap<String, List<Long>>(1);
        map.put(Image.class.getName(), Arrays.asList(i1.getId().getValue()));
        Map<Long, Map<Boolean, List<Long>>> results = factory.getContainerService()
                .getImagesBySplitFilesets(map, param);
        Assert.assertEquals(results.size(), 1);
        Entry<Long, Map<Boolean, List<Long>>> e;
        Entry<Boolean, List<Long>> entry;
        Iterator<Entry<Boolean, List<Long>>> j;
        Iterator<Entry<Long, Map<Boolean, List<Long>>>> i = results.entrySet()
                .iterator();
        while (i.hasNext()) {
            e = i.next();
            Assert.assertEquals(e.getKey().longValue(), fileset.getId().getValue());
            j = e.getValue().entrySet().iterator();
            while (i.hasNext()) {
                entry = j.next();
                Assert.assertEquals(entry.getValue().size(), 1);
                if (entry.getKey().booleanValue()) {
                    Assert.assertTrue(entry.getValue().contains(i1.getId().getValue()));
                } else
                    Assert.assertTrue(entry.getValue().contains(i2.getId().getValue()));
            }
        }
    }

    /**
     * Test the <code>getImagesBySplitFilesets</code> Image as root.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testGetImagesBySplitFilesetsImageAsRootAll() throws Exception {
        // first create a project
        Image i1 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Image i2 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Fileset fileset = newFileset();
        fileset.addImage(i1);
        fileset.addImage(i2);
        fileset = (Fileset) iUpdate.saveAndReturnObject(fileset);
        // Check that 2 images are linked
        Assert.assertEquals(fileset.copyImages().size(), 2);

        Parameters param = new ParametersI();
        List<Long> ids = new ArrayList<Long>();
        ids.add(i1.getId().getValue());
        ids.add(i2.getId().getValue());
        Map<String, List<Long>> map = new HashMap<String, List<Long>>(1);
        map.put(Image.class.getName(), ids);
        Map<Long, Map<Boolean, List<Long>>> results = factory.getContainerService()
                .getImagesBySplitFilesets(map, param);
        Assert.assertEquals(results.size(), 0);
    }

    /**
     * Test the <code>getImagesBySplitFilesets</code> Dataset as root.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testGetImagesBySplitFilesetsDatasetAsRoot() throws Exception {
        // first create a project
        Image i1 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Image i2 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Fileset fileset = newFileset();
        fileset.addImage(i1);
        fileset.addImage(i2);
        fileset = (Fileset) iUpdate.saveAndReturnObject(fileset);
        Assert.assertEquals(fileset.copyImages().size(), 2);

        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        // link the 2
        DatasetImageLink link = new DatasetImageLinkI();
        link.setParent(d);
        link.setChild((Image) i1.proxy());
        iUpdate.saveAndReturnObject(link);

        Parameters param = new ParametersI();
        Map<String, List<Long>> map = new HashMap<String, List<Long>>(1);
        map.put(Dataset.class.getName(), Arrays.asList(d.getId().getValue()));
        Map<Long, Map<Boolean, List<Long>>> results = factory.getContainerService()
                .getImagesBySplitFilesets(map, param);
        Assert.assertEquals(results.size(), 1);
        Entry<Long, Map<Boolean, List<Long>>> e;
        Entry<Boolean, List<Long>> entry;
        Iterator<Entry<Boolean, List<Long>>> j;
        Iterator<Entry<Long, Map<Boolean, List<Long>>>> i = results.entrySet()
                .iterator();
        while (i.hasNext()) {
            e = i.next();
            Assert.assertEquals(e.getKey().longValue(), fileset.getId().getValue());
            j = e.getValue().entrySet().iterator();
            while (i.hasNext()) {
                entry = j.next();
                Assert.assertEquals(entry.getValue().size(), 1);
                if (entry.getKey().booleanValue()) {
                    Assert.assertTrue(entry.getValue().contains(i1.getId().getValue()));
                } else
                    Assert.assertTrue(entry.getValue().contains(i2.getId().getValue()));
            }
        }
    }

    /**
     * Test the <code>getImagesBySplitFilesets</code> Project as root.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testGetImagesBySplitFilesetsProjectAsRoot() throws Exception {
        // first create a project
        Image i1 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Image i2 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Fileset fileset = newFileset();
        fileset.addImage(i1);
        fileset.addImage(i2);
        fileset = (Fileset) iUpdate.saveAndReturnObject(fileset);
        Assert.assertEquals(fileset.copyImages().size(), 2);

        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        // link the 2
        DatasetImageLink link = new DatasetImageLinkI();
        link.setParent(d);
        link.setChild((Image) i1.proxy());
        iUpdate.saveAndReturnObject(link);

        Project p = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());

        ProjectDatasetLink lp = new ProjectDatasetLinkI();
        lp.setParent(p);
        lp.setChild((Dataset) d.proxy());

        iUpdate.saveAndReturnObject(lp);

        Parameters param = new ParametersI();
        Map<String, List<Long>> map = new HashMap<String, List<Long>>(1);
        map.put(Project.class.getName(), Arrays.asList(p.getId().getValue()));
        Map<Long, Map<Boolean, List<Long>>> results = factory.getContainerService()
                .getImagesBySplitFilesets(map, param);
        Assert.assertEquals(results.size(), 1);
        Entry<Long, Map<Boolean, List<Long>>> e;
        Entry<Boolean, List<Long>> entry;
        Iterator<Entry<Boolean, List<Long>>> j;
        Iterator<Entry<Long, Map<Boolean, List<Long>>>> i = results.entrySet()
                .iterator();
        while (i.hasNext()) {
            e = i.next();
            Assert.assertEquals(e.getKey().longValue(), fileset.getId().getValue());
            j = e.getValue().entrySet().iterator();
            while (i.hasNext()) {
                entry = j.next();
                Assert.assertEquals(entry.getValue().size(), 1);
                if (entry.getKey().booleanValue()) {
                    Assert.assertTrue(entry.getValue().contains(i1.getId().getValue()));
                } else
                    Assert.assertTrue(entry.getValue().contains(i2.getId().getValue()));
            }
        }
    }

    /**
     * Test the <code>getImagesBySplitFilesets</code> Dataset as root. The
     * fileset is split between 2 datasets. Both datasets are specified.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testGetImagesBySplitFilesetsMixedDatasetAsRoot()
            throws Exception {
        // first create a project
        Image i1 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Image i2 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Fileset fileset = newFileset();
        fileset.addImage(i1);
        fileset.addImage(i2);
        fileset = (Fileset) iUpdate.saveAndReturnObject(fileset);
        Assert.assertEquals(fileset.copyImages().size(), 2);

        Dataset d1 = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        // link the 2
        DatasetImageLink link = new DatasetImageLinkI();
        link.setParent(d1);
        link.setChild((Image) i1.proxy());
        iUpdate.saveAndReturnObject(link);

        Dataset d2 = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        // link the 2
        link = new DatasetImageLinkI();
        link.setParent(d2);
        link.setChild((Image) i2.proxy());
        iUpdate.saveAndReturnObject(link);

        Parameters param = new ParametersI();
        List<Long> ids = new ArrayList<Long>();
        ids.add(d1.getId().getValue());
        ids.add(d2.getId().getValue());
        Map<String, List<Long>> map = new HashMap<String, List<Long>>(1);
        map.put(Dataset.class.getName(), ids);
        Map<Long, Map<Boolean, List<Long>>> results = factory.getContainerService()
                .getImagesBySplitFilesets(map, param);
        Assert.assertEquals(results.size(), 0);
    }

    /**
     * Test the <code>getImagesBySplitFilesets</code> Project as root. The
     * fileset is split between 2 datasets contained into 2 projects. Both
     * projects are specified.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testGetImagesBySplitFilesetsMixedProjectAsRoot()
            throws Exception {
        // first create a project
        Image i1 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Image i2 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Fileset fileset = newFileset();
        fileset.addImage(i1);
        fileset.addImage(i2);
        fileset = (Fileset) iUpdate.saveAndReturnObject(fileset);
        Assert.assertEquals(fileset.copyImages().size(), 2);

        Dataset d1 = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());

        Dataset d2 = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());

        // link the 2
        DatasetImageLink link = new DatasetImageLinkI();
        link.setParent(d1);
        link.setChild((Image) i1.proxy());
        iUpdate.saveAndReturnObject(link);

        link = new DatasetImageLinkI();
        link.setParent(d2);
        link.setChild((Image) i2.proxy());
        iUpdate.saveAndReturnObject(link);

        Project p1 = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());

        ProjectDatasetLink lp = new ProjectDatasetLinkI();
        lp.setParent(p1);
        lp.setChild((Dataset) d1.proxy());

        iUpdate.saveAndReturnObject(lp);

        Project p2 = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());

        lp = new ProjectDatasetLinkI();
        lp.setParent(p2);
        lp.setChild((Dataset) d2.proxy());

        iUpdate.saveAndReturnObject(lp);

        Parameters param = new ParametersI();
        List<Long> ids = new ArrayList<Long>();
        ids.add(p1.getId().getValue());
        ids.add(p2.getId().getValue());
        Map<String, List<Long>> map = new HashMap<String, List<Long>>(1);
        map.put(Project.class.getName(), ids);
        Map<Long, Map<Boolean, List<Long>>> results = factory.getContainerService()
                .getImagesBySplitFilesets(map, param);
        Assert.assertEquals(results.size(), 0);
    }

    /**
     * Test the <code>getImagesBySplitFilesets</code> with image/dataset and
     * project as root.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testGetImagesBySplitFilesetsMixedRoot() throws Exception {
        // first create a project
        Image i1 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Image i2 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Image i3 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());

        Fileset fileset = newFileset();
        fileset.addImage(i1);
        fileset.addImage(i2);
        fileset.addImage(i3);
        fileset = (Fileset) iUpdate.saveAndReturnObject(fileset);
        Assert.assertEquals(fileset.copyImages().size(), 3);

        Dataset d1 = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());

        Dataset d2 = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());

        // link the 2
        DatasetImageLink link = new DatasetImageLinkI();
        link.setParent(d1);
        link.setChild((Image) i1.proxy());
        iUpdate.saveAndReturnObject(link);

        link = new DatasetImageLinkI();
        link.setParent(d2);
        link.setChild((Image) i2.proxy());
        iUpdate.saveAndReturnObject(link);

        Project p1 = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());

        ProjectDatasetLink lp = new ProjectDatasetLinkI();
        lp.setParent(p1);
        lp.setChild((Dataset) d1.proxy());

        iUpdate.saveAndReturnObject(lp);

        Parameters param = new ParametersI();
        Map<String, List<Long>> map = new HashMap<String, List<Long>>();
        map.put(Project.class.getName(), Arrays.asList(p1.getId().getValue()));
        map.put(Dataset.class.getName(), Arrays.asList(d2.getId().getValue()));
        map.put(Image.class.getName(), Arrays.asList(i3.getId().getValue()));
        Map<Long, Map<Boolean, List<Long>>> results = factory.getContainerService()
                .getImagesBySplitFilesets(map, param);
        Assert.assertEquals(results.size(), 0);
    }

    /**
     * Test the <code>getImagesBySplitFilesets</code> with dataset and project
     * as root. One image missing.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testGetImagesBySplitFilesetsMixedRootMissingImage()
            throws Exception {
        // first create a project
        Image i1 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Image i2 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Image i3 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());

        Fileset fileset = newFileset();
        fileset.addImage(i1);
        fileset.addImage(i2);
        fileset.addImage(i3);
        fileset = (Fileset) iUpdate.saveAndReturnObject(fileset);
        Assert.assertEquals(fileset.copyImages().size(), 3);

        Dataset d1 = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());

        Dataset d2 = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());

        // link the 2
        DatasetImageLink link = new DatasetImageLinkI();
        link.setParent(d1);
        link.setChild((Image) i1.proxy());
        iUpdate.saveAndReturnObject(link);

        link = new DatasetImageLinkI();
        link.setParent(d2);
        link.setChild((Image) i2.proxy());
        iUpdate.saveAndReturnObject(link);

        Project p1 = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());

        ProjectDatasetLink lp = new ProjectDatasetLinkI();
        lp.setParent(p1);
        lp.setChild((Dataset) d1.proxy());

        iUpdate.saveAndReturnObject(lp);

        Parameters param = new ParametersI();
        Map<String, List<Long>> map = new HashMap<String, List<Long>>();
        map.put(Project.class.getName(), Arrays.asList(p1.getId().getValue()));
        map.put(Dataset.class.getName(), Arrays.asList(d2.getId().getValue()));
        Map<Long, Map<Boolean, List<Long>>> results = factory.getContainerService()
                .getImagesBySplitFilesets(map, param);
        Assert.assertEquals(results.size(), 1);
        Entry<Long, Map<Boolean, List<Long>>> e;
        Entry<Boolean, List<Long>> entry;
        Iterator<Entry<Boolean, List<Long>>> j;
        Iterator<Entry<Long, Map<Boolean, List<Long>>>> i = results.entrySet()
                .iterator();
        while (i.hasNext()) {
            e = i.next();
            Assert.assertEquals(e.getKey().longValue(), fileset.getId().getValue());
            j = e.getValue().entrySet().iterator();
            List<Long> l;
            while (i.hasNext()) {
                entry = j.next();
                Assert.assertEquals(entry.getValue().size(), 1);
                l = entry.getValue();
                if (entry.getKey().booleanValue()) {
                    Assert.assertEquals(l.size(), 2);
                    Assert.assertTrue(l.contains(i1.getId().getValue()));
                    Assert.assertTrue(l.contains(i2.getId().getValue()));
                } else {
                    Assert.assertEquals(l.size(), 1);
                    Assert.assertTrue(l.contains(i3.getId().getValue()));
                }
            }
        }
    }

    /**
     * Test the <code>getImagesBySplitFilesets</code> Dataset as root. One image
     * is not part of a file set.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testGetImagesBySplitFilesetsDatasetAsRootWithNonFSdata()
            throws Exception {
        // first create a project
        Image i1 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Image i2 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Image i3 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());

        Fileset fileset = newFileset();
        fileset.addImage(i1);
        fileset.addImage(i2);
        fileset = (Fileset) iUpdate.saveAndReturnObject(fileset);
        Assert.assertEquals(fileset.copyImages().size(), 2);

        Dataset d1 = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        // link the 2
        DatasetImageLink link = new DatasetImageLinkI();
        link.setParent(d1);
        link.setChild((Image) i1.proxy());
        iUpdate.saveAndReturnObject(link);

        link = new DatasetImageLinkI();
        link.setParent((Dataset) d1.proxy());
        link.setChild((Image) i2.proxy());
        iUpdate.saveAndReturnObject(link);

        link = new DatasetImageLinkI();
        link.setParent((Dataset) d1.proxy());
        link.setChild((Image) i3.proxy());
        iUpdate.saveAndReturnObject(link);

        Parameters param = new ParametersI();
        List<Long> ids = new ArrayList<Long>();
        ids.add(d1.getId().getValue());
        Map<String, List<Long>> map = new HashMap<String, List<Long>>(1);
        map.put(Dataset.class.getName(), ids);
        Map<Long, Map<Boolean, List<Long>>> results = factory.getContainerService()
                .getImagesBySplitFilesets(map, param);
        Assert.assertEquals(results.size(), 0);
    }

    /**
     * Test the <code>getImagesBySplitFilesets</code> Dataset as root. One image
     * is not part of a file set.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testGetImagesBySplitFilesetsImageAsRootWithNonFSdata()
            throws Exception {
        // first create a project
        Image i1 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Image i2 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Image i3 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());

        Fileset fileset = newFileset();
        fileset.addImage(i1);
        fileset.addImage(i2);
        fileset = (Fileset) iUpdate.saveAndReturnObject(fileset);
        Assert.assertEquals(fileset.copyImages().size(), 2);
        Parameters param = new ParametersI();
        Map<String, List<Long>> map = new HashMap<String, List<Long>>(1);
        map.put(Image.class.getName(), Arrays.asList(i3.getId().getValue()));
        Map<Long, Map<Boolean, List<Long>>> results = factory.getContainerService()
                .getImagesBySplitFilesets(map, param);
        Assert.assertEquals(results.size(), 0);
    }

    /**
     * Test the <code>getImagesBySplitFilesets</code> Image as root. One image
     * is not part of a file set.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testGetImagesBySplitFilesetsImageAsRootMixFSNonFSdata()
            throws Exception {
        // first create a project
        Image i1 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Image i2 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());
        Image i3 = (Image) iUpdate
                .saveAndReturnObject(mmFactory.simpleImage());

        Fileset fileset = newFileset();
        fileset.addImage(i1);
        fileset.addImage(i2);
        fileset = (Fileset) iUpdate.saveAndReturnObject(fileset);
        Assert.assertEquals(fileset.copyImages().size(), 2);
        Parameters param = new ParametersI();
        Map<String, List<Long>> map = new HashMap<String, List<Long>>(1);
        List<Long> ids = new ArrayList<Long>();
        ids.add(i1.getId().getValue());
        ids.add(i3.getId().getValue());
        map.put(Image.class.getName(), ids);
        Map<Long, Map<Boolean, List<Long>>> results = factory.getContainerService()
                .getImagesBySplitFilesets(map, param);
        Assert.assertEquals(results.size(), 1);
        Entry<Long, Map<Boolean, List<Long>>> e;
        Entry<Boolean, List<Long>> entry;
        Iterator<Entry<Boolean, List<Long>>> j;
        Iterator<Entry<Long, Map<Boolean, List<Long>>>> i = results.entrySet()
                .iterator();
        while (i.hasNext()) {
            e = i.next();
            Assert.assertEquals(e.getKey().longValue(), fileset.getId().getValue());
            j = e.getValue().entrySet().iterator();
            List<Long> l;
            while (i.hasNext()) {
                entry = j.next();
                Assert.assertEquals(entry.getValue().size(), 1);
                l = entry.getValue();
                Assert.assertEquals(l.size(), 1);
                if (entry.getKey().booleanValue()) {
                    Assert.assertTrue(l.contains(i1.getId().getValue()));
                } else {
                    Assert.assertTrue(l.contains(i2.getId().getValue()));
                }
            }
        }
    }

    /**
     * Test that split filesets are accurately detected when images are referenced via folders.
     * @throws ServerError unexpected
     */
    @Test
    public void testGetImagesBySplitFilesetsViaFolders() throws ServerError {
        final List<Image> images = new ArrayList<Image>();
        final List<Folder> folders = new ArrayList<Folder>();

        /* create four images */

        for (int i = 0; i < 4; i++) {
            images.add((Image) iUpdate.saveAndReturnObject(mmFactory.simpleImage()));
        }

        /* make a fileset from each pair of images */

        for (int i = 0; i < images.size();) {
            final Fileset fileset = newFileset();
            fileset.addImage((Image) images.get(i++));
            fileset.addImage((Image) images.get(i++));
            iUpdate.saveObject(fileset);
        }

        /* make 2^n folders for each of the n images; the bits of the folder index correspond to specific images */

        for (int f = IntMath.checkedPow(2, images.size()); f > 0; f--) {
            if (Integer.bitCount(f) > 1) {
                while (folders.size() <= f) {
                    folders.add(null);
                }
                folders.set(f, saveAndReturnFolder(mmFactory.simpleFolder()));
            }
        }

        /* folders whose index has two bits set get two images added directly, one for each bit */

        for (int f = 0; f < folders.size(); f++) {
            if (Integer.bitCount(f) == 2) {
                folders.set(f, returnFolder(folders.get(f)));
                for (int i = 0, b = 1; i < images.size(); i++, b <<= 1) {
                    if ((f & b) == b) {
                        folders.get(f).linkImage((Image) images.get(i).proxy());
                        folders.set(f, saveAndReturnFolder(folders.get(f)));
                    }
                }
            }
        }

        /* folders whose index has more than two bits set get folders and images added */

        for (int f = 0; f < folders.size(); f++) {
            if (Integer.bitCount(f) > 2) {
                int needed = f;
                /* add image folders to cover multiple images still needing to be covered */
                for (int j = 0; Integer.bitCount(needed) > 1 && j < folders.size(); j++) {
                    if (Integer.bitCount(j) == 2 && (j & needed) != 0 && (~f & j) == 0 &&
                            folders.get(j).getParentFolder() == null) {
                        folders.get(f).addChildFolders((Folder) folders.get(j));
                        needed &= ~j;
                    }
                }
                /* add images if folders did not suffice */
                while (needed != 0) {
                    final int i = Integer.numberOfTrailingZeros(needed);
                    folders.get(f).linkImage((Image) images.get(i).proxy());
                    needed &= ~(1 << i);
                }
                folders.set(f, saveAndReturnFolder(folders.get(f)));
            }
        }

        /* start tests */

        final IContainerPrx iContainer = factory.getContainerService();
        final Map<String, List<Long>> args = new HashMap<String, List<Long>>();
        final Parameters params = new Parameters();

        /* check for split filesets within folders that cover multiple images */

        for (int f = 0; f < folders.size(); f++) {
            if (Integer.bitCount(f) > 1) {

                /* calculate the expected splits */

                final Set<Long> expectedIncluded = new HashSet<Long>();
                final Set<Long> expectedExcluded = new HashSet<Long>();
                for (int i = 0; i < images.size(); i += 2) {
                    int b1 = 1 << i;
                    int b2 = b1 << 1;
                    if ((f & b1) != 0) {
                        if ((f & b2) == 0) {
                            expectedIncluded.add(images.get(i).getId().getValue());
                            expectedExcluded.add(images.get(i+1).getId().getValue());
                        }
                    } else {
                        if ((f & b2) != 0) {
                            expectedExcluded.add(images.get(i).getId().getValue());
                            expectedIncluded.add(images.get(i+1).getId().getValue());
                        }
                    }
                }

                /* determine the reported splits */

                args.put("Folder", Collections.singletonList(folders.get(f).getId().getValue()));
                final Set<Long> actualIncluded = new HashSet<Long>();
                final Set<Long> actualExcluded = new HashSet<Long>();
                for (final Map<Boolean, List<Long>> filesetImages : iContainer.getImagesBySplitFilesets(args, params).values()) {
                    actualIncluded.addAll(filesetImages.get(true));
                    actualExcluded.addAll(filesetImages.get(false));
                }

                /* ensure that the two agree */

                Assert.assertEquals(actualIncluded, expectedIncluded);
                Assert.assertEquals(actualExcluded, expectedExcluded);
            }
        }
    }

	/**
	 * Checks if the creation date is loaded for all container {@link IObject}s
	 * 
	 * @throws Exception
	 *             Thrown if an error occurred.
	 */
	@Test
	public void testContainerCreationDateLoaded() throws Exception {
		Project p = (Project) iUpdate.saveAndReturnObject(mmFactory
				.simpleProjectData().asIObject());
		List<Long> ids = new ArrayList<Long>();
		ids.add(p.getId().getValue());
		p = (Project) factory.getContainerService()
				.loadContainerHierarchy(Project.class.getName(), ids, null)
				.iterator().next();

		Assert.assertTrue(p.getDetails().getCreationEvent().isLoaded());

		Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
				.simpleDatasetData().asIObject());
		ids = new ArrayList<Long>();
		ids.add(d.getId().getValue());
		d = (Dataset) factory.getContainerService()
				.loadContainerHierarchy(Dataset.class.getName(), ids, null)
				.iterator().next();

		Assert.assertTrue(d.getDetails().getCreationEvent().isLoaded());

		Screen s = (Screen) iUpdate.saveAndReturnObject(mmFactory
				.simpleScreenData().asIObject());
		ids = new ArrayList<Long>();
		ids.add(s.getId().getValue());
		s = (Screen) factory.getContainerService()
				.loadContainerHierarchy(Screen.class.getName(), ids, null)
				.iterator().next();

		Assert.assertTrue(s.getDetails().getCreationEvent().isLoaded());

		Plate pl = (Plate) iUpdate.saveAndReturnObject(mmFactory
				.simplePlateData().asIObject());
		ids = new ArrayList<Long>();
		ids.add(pl.getId().getValue());
		pl = (Plate) factory.getContainerService()
				.loadContainerHierarchy(Plate.class.getName(), ids, null)
				.iterator().next();

		Assert.assertTrue(pl.getDetails().getCreationEvent().isLoaded());
	}
	

    /**
     * Test to load container hierarchy with project containing an dataset
     * owned by another member of the group.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testLoadContainerHierarchyProjectWithOtherMembersDataset()
            throws Exception {
        // first create a project
        String perms = "rwrw--";
        EventContext ctx = newUserAndGroup(perms, true);

        Project p = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());

        EventContext dataOwner = newUserInGroup();
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        Dataset dNotOrphaned = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        Assert.assertEquals(dataOwner.userId,
                dNotOrphaned.getDetails().getOwner().getId().getValue());
        //link the dataset to another user's project.
        ProjectDatasetLink link = new ProjectDatasetLinkI();
        link.setParent(p);
        link.setChild(dNotOrphaned);
        iUpdate.saveAndReturnObject(link);

        //create a project
        Project p1 = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());
        ParametersI param = new ParametersI();
        param.exp(ode.rtypes.rlong(dataOwner.userId));
        param.orphan();
        List results = factory.getContainerService().loadContainerHierarchy(
                Project.class.getName(), new ArrayList(), param);
        Assert.assertEquals(2, results.size());
        Iterator i = results.iterator();
        IObject object;
        int value = 0;
        boolean orphaned = false;
        while (i.hasNext()) {
            object = (IObject) i.next();
            if (object instanceof Dataset) {
                if (object.getId().getValue() == d.getId().getValue()) {
                    value++;
                } else if (object.getId().getValue() == dNotOrphaned.getId().getValue()) {
                    orphaned = true;
                }
            } else if (object instanceof Project) {
                if (object.getId().getValue() == p1.getId().getValue()) {
                    value++;
                }
            }
        }
        Assert.assertEquals(2, value);
        Assert.assertEquals(orphaned, false);
    }

    /**
     * Test to load container hierarchy with screen containing a plate
     * owned by another member of the group.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testLoadContainerHierarchyScreenWithOtherMembersPlate()
            throws Exception {
        // first create a Screen
        String perms = "rwrw--";
        EventContext ctx = newUserAndGroup(perms, true);
        Screen p = (Screen) iUpdate.saveAndReturnObject(mmFactory
                .simpleScreenData().asIObject());

        EventContext dataOwner = newUserInGroup();
        Plate d = (Plate) iUpdate.saveAndReturnObject(mmFactory
                .simplePlateData().asIObject());
        Plate dNotOrphaned = (Plate) iUpdate.saveAndReturnObject(mmFactory
                .simplePlateData().asIObject());
        Assert.assertEquals(dataOwner.userId,
                dNotOrphaned.getDetails().getOwner().getId().getValue());
        ScreenPlateLink link = new ScreenPlateLinkI();
        link.setParent(p);
        link.setChild(dNotOrphaned);
        iUpdate.saveAndReturnObject(link);
        //link the plate to another user's screen.
        Screen p1 = (Screen) iUpdate.saveAndReturnObject(mmFactory
                .simpleScreenData().asIObject());
        ParametersI param = new ParametersI();
        param.exp(ode.rtypes.rlong(dataOwner.userId));
        param.orphan();
        List results = factory.getContainerService().loadContainerHierarchy(
                Screen.class.getName(), new ArrayList(), param);
        Assert.assertEquals(2, results.size());
        Iterator i = results.iterator();
        IObject object;
        int value = 0;
        boolean orphaned = false;
        while (i.hasNext()) {
            object = (IObject) i.next();
            if (object instanceof Plate) {
                if (object.getId().getValue() == d.getId().getValue()) {
                    value++;
                } else if (object.getId().getValue() == dNotOrphaned.getId().getValue()) {
                    orphaned = true;
                }
            } else if (object instanceof Screen) {
                if (object.getId().getValue() == p1.getId().getValue()) {
                    value++;
                }
            }
        }
        Assert.assertEquals(2, value);
        Assert.assertEquals(orphaned, false);
    }
}
