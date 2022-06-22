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

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ode.formats.model.UnitsFactory;
import ode.api.IRenderingSettingsPrx;
import ode.api.IScriptPrx;
import ode.api.RenderingEnginePrx;
import ode.model.Channel;
import ode.model.ChannelBinding;
import ode.model.CodomainMapContext;
import ode.model.Dataset;
import ode.model.DatasetI;
import ode.model.DatasetImageLink;
import ode.model.DatasetImageLinkI;
import ode.model.IObject;
import ode.model.Image;
import ode.model.LengthI;
import ode.model.LogicalChannel;
import ode.model.OriginalFile;
import ode.model.Pixels;
import ode.model.Plate;
import ode.model.PlateAcquisition;
import ode.model.Project;
import ode.model.ProjectDatasetLink;
import ode.model.ProjectDatasetLinkI;
import ode.model.RenderingDef;
import ode.model.Screen;
import ode.model.ScreenPlateLink;
import ode.model.ScreenPlateLinkI;
import ode.model.Well;
import ode.model.WellSample;
import ode.sys.EventContext;
import ode.sys.ParametersI;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Collections of tests for the <code>RenderingSettingsService</code> service.
 */
public class RenderingSettingsServiceTest extends AbstractServerTest {

    /**
     * Create an entire plate, uploading binary data for all the images.
     *
     * After recent changes on the server to check for existing binary data for
     * pixels, many resetDefaults methods tested below began returning null
     * since {@link ode.LockTimeout} exceptions were being thrown server-side.
     * By using ode.client.forEachTile, we can set the necessary data easily.
     *
     * @see ticket:5755
     */
    public Plate createBinaryPlate(int rows, int cols, int fields,
            int acquisitions) throws Exception {
        Plate plate = mmFactory.createPlate(rows, cols, fields, acquisitions,
                true);
        plate = (Plate) iUpdate.saveAndReturnObject(plate);

        for (Well well : plate.copyWells()) {
            for (WellSample ws : well.copyWellSamples()) {
                Image image = createBinaryImage(ws.getImage());
                ws.setImage(image);
            }
        }

        return plate;

    }

    /**
     * Tests to set the default rendering settings for a set.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testResetDefaultInSetForPixels() throws Exception {
        Image image = createBinaryImage();
        Pixels pixels = image.getPrimaryPixels();
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        prx.setOriginalSettingsInSet(Pixels.class.getName(),
                Arrays.asList(pixels.getId().getValue()));
        // Pixels first
        List<Long> ids = new ArrayList<Long>();
        ids.add(pixels.getId().getValue());
        List<Long> v = prx.resetDefaultsInSet(Pixels.class.getName(), ids);
        Assert.assertNotNull(v);
        // check if we have settings now.
        ParametersI param = new ParametersI();
        param.addLong("pid", pixels.getId().getValue());
        String sql = "select rdef from RenderingDef as rdef "
                + "where rdef.pixels.id = :pid";
        List<IObject> values = iQuery.findAllByQuery(sql, param);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 1);

        // Image
        ids = new ArrayList<Long>();
        ids.add(image.getId().getValue());
        v = prx.resetDefaultsInSet(Image.class.getName(), ids);
        Assert.assertNotNull(v);
        values = iQuery.findAllByQuery(sql, param);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 1);
    }

    /**
     * Tests to set the default rendering settings for a set.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testResetDefaultInSetForImage() throws Exception {
        Image image = createBinaryImage();
        Pixels pixels = image.getPrimaryPixels();
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        prx.setOriginalSettingsInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));
        // Image
        List<Long> ids = new ArrayList<Long>();
        ids.add(image.getId().getValue());
        List<Long> v = prx.resetDefaultsInSet(Image.class.getName(), ids);
        Assert.assertNotNull(v);
        Assert.assertEquals(v.size(), 1);
        ParametersI param = new ParametersI();
        param.addLong("pid", pixels.getId().getValue());
        String sql = "select rdef from RenderingDef as rdef "
                + "where rdef.pixels.id = :pid";
        List<IObject> values = iQuery.findAllByQuery(sql, param);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 1);
    }

    /**
     * Tests to set the default rendering settings for a set.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testResetDefaultInSetForDataset() throws Exception {
        Image image = createBinaryImage();
        Pixels pixels = image.getPrimaryPixels();

        // create a dataset
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        DatasetImageLink l = new DatasetImageLinkI();
        l.setChild(image);
        l.setParent(d);
        iUpdate.saveAndReturnObject(l);

        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        prx.setOriginalSettingsInSet(Dataset.class.getName(),
                Arrays.asList(d.getId().getValue()));

        // Dataset
        List<Long> v = prx.resetDefaultsInSet(Dataset.class.getName(),
                Arrays.asList(d.getId().getValue()));
        Assert.assertNotNull(v);
        Assert.assertEquals(v.size(), 1);
        ParametersI param = new ParametersI();
        param.addLong("pid", pixels.getId().getValue());
        String sql = "select rdef from RenderingDef as rdef "
                + "where rdef.pixels.id = :pid";
        List<IObject> values = iQuery.findAllByQuery(sql, param);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 1);
    }

    /**
     * Tests to set the default rendering settings for a project.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testResetDefaultInSetForProject() throws Exception {
        // create a project.
        Project p = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());
        // create a dataset

        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());

        Image image = createBinaryImage();
        Pixels pixels = image.getPrimaryPixels();

        ProjectDatasetLink link = new ProjectDatasetLinkI();
        link.link(p, d);
        link = (ProjectDatasetLink) iUpdate.saveAndReturnObject(link);

        DatasetImageLink l = new DatasetImageLinkI();
        l.link(new DatasetI(d.getId().getValue(), false), image);
        l = (DatasetImageLink) iUpdate.saveAndReturnObject(l);

        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        prx.setOriginalSettingsInSet(Project.class.getName(),
                Arrays.asList(p.getId().getValue()));

        List<Long> ids = new ArrayList<Long>();
        ids.add(p.getId().getValue());
        List<Long> v = prx.resetDefaultsInSet(Project.class.getName(), ids);
        ParametersI param;
        String sql;
        param = new ParametersI();
        ids.clear();
        ids.add(p.getId().getValue());
        param.addIds(ids);
        sql = "select pix from Pixels as pix " + "join fetch pix.image as i "
                + "join fetch pix.pixelsType "
                + "join fetch pix.channels as c "
                + "join fetch c.logicalChannel "
                + "join i.datasetLinks as dil " + "join dil.parent as d "
                + "left outer join d.projectLinks as pdl "
                + "left outer join pdl.parent as p " + "where p.id in (:ids)";
        Assert.assertEquals(iQuery.findAllByQuery(sql, param).size(), 1);

        Assert.assertNotNull(v);
        Assert.assertEquals(v.size(), 1);
        param = new ParametersI();
        param.addLong("pid", pixels.getId().getValue());
        sql = "select rdef from RenderingDef as rdef "
                + "where rdef.pixels.id = :pid";
        List<IObject> values = iQuery.findAllByQuery(sql, param);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 1);
    }

    /**
     * Tests to set the default rendering settings for a screen.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testResetDefaultInSetForScreen() throws Exception {
        Screen screen = (Screen) iUpdate.saveAndReturnObject(mmFactory
                .simpleScreenData().asIObject());
        Plate p = createBinaryPlate(1, 1, 1, 0);
        p = (Plate) iUpdate.saveAndReturnObject(p);

        ScreenPlateLink link = new ScreenPlateLinkI();
        link.setChild(p);
        link.setParent(screen);
        iUpdate.saveAndReturnObject(link);

        // load the well
        List<Well> results = loadWells(p.getId().getValue(), true);
        Well well = results.get(0);
        Image image = well.getWellSample(0).getImage();
        Pixels pixels = image.getPrimaryPixels();
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        prx.setOriginalSettingsInSet(Screen.class.getName(),
                Arrays.asList(screen.getId().getValue()));
        // Image
        List<Long> ids = new ArrayList<Long>();
        ids.add(screen.getId().getValue());
        List<Long> v = prx.resetDefaultsInSet(Screen.class.getName(), ids);
        Assert.assertNotNull(v);
        Assert.assertEquals(v.size(), 1);
        ParametersI param = new ParametersI();
        param.addLong("pid", pixels.getId().getValue());
        String sql = "select rdef from RenderingDef as rdef "
                + "where rdef.pixels.id = :pid";
        List<IObject> values = iQuery.findAllByQuery(sql, param);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 1);
    }

    /**
     * Tests to set the default rendering settings for a empty dataset. Tests
     * the <code>resetDefaultsInSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testResetDefaultInSetForEmptyDataset() throws Exception {
        // create a dataset
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());

        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        // Dataset
        prx.setOriginalSettingsInSet(Dataset.class.getName(),
                Arrays.asList(d.getId().getValue()));
        List<Long> ids = new ArrayList<Long>();
        ids.add(d.getId().getValue());
        List<Long> v = prx.resetDefaultsInSet(Dataset.class.getName(), ids);
        Assert.assertNotNull(v);
        Assert.assertTrue(v.isEmpty());
    }

    /**
     * Tests to apply the rendering settings to a collection of images. Tests
     * the <code>ApplySettingsToSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testApplySettingsToSetForImage() throws Exception {
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        Image image = createBinaryImage();
        Pixels pixels = image.getPrimaryPixels();
        long id = pixels.getId().getValue();
        // Image
        prx.setOriginalSettingsInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));

        // method already tested
        RenderingDef def = factory.getPixelsService().retrieveRndSettings(id);
        // Create a second image.
        Image image2 = createBinaryImage();
        Map<Boolean, List<Long>> m = prx
                .applySettingsToSet(id, Image.class.getName(),
                        Arrays.asList(image2.getId().getValue()));
        Assert.assertNotNull(m);
        List<Long> success = (List<Long>) m.get(Boolean.valueOf(true));
        List<Long> failure = (List<Long>) m.get(Boolean.valueOf(false));
        Assert.assertNotNull(success);
        Assert.assertNotNull(failure);
        Assert.assertEquals(success.size(), 1);
        Assert.assertTrue(failure.isEmpty());
        id = success.get(0); // image id.
        Assert.assertEquals(id, image2.getId().getValue());
        RenderingDef def2 = factory.getPixelsService().retrieveRndSettings(
                image2.getPrimaryPixels().getId().getValue());
        compareRenderingDef(def, def2);
    }

    /**
     * Tests to apply the rendering settings to a collection of images within a
     * dataset. Tests the <code>ApplySettingsToSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testApplySettingsToSetForDataset() throws Exception {
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        Image image = createBinaryImage();
        Pixels pixels = image.getPrimaryPixels();
        long id = pixels.getId().getValue();
        // Image
        // method already tested
        prx.setOriginalSettingsInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));

        // method already tested
        RenderingDef def = factory.getPixelsService().retrieveRndSettings(id);
        // Create a second image.
        Image image2 = createBinaryImage();
        // Create a dataset

        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        DatasetImageLink l = new DatasetImageLinkI();
        l.setChild(image2);
        l.setParent(d);
        iUpdate.saveAndReturnObject(l);

        Map<Boolean, List<Long>> m = prx.applySettingsToSet(id,
                Dataset.class.getName(), Arrays.asList(d.getId().getValue()));
        Assert.assertNotNull(m);
        List<Long> success = (List<Long>) m.get(Boolean.valueOf(true));
        List<Long> failure = (List<Long>) m.get(Boolean.valueOf(false));
        Assert.assertNotNull(success);
        Assert.assertNotNull(failure);
        Assert.assertEquals(success.size(), 1);
        Assert.assertTrue(failure.isEmpty());
        id = success.get(0); // image id.
        Assert.assertEquals(id, image2.getId().getValue());
        RenderingDef def2 = factory.getPixelsService().retrieveRndSettings(
                image2.getPrimaryPixels().getId().getValue());
        compareRenderingDef(def, def2);
    }

    /**
     * Tests to apply the rendering settings to an empty dataset. Tests the
     * <code>ApplySettingsToSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testApplySettingsToSetForEmptyDataset() throws Exception {
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        Image image = createBinaryImage();
        Pixels pixels = image.getPrimaryPixels();
        long id = pixels.getId().getValue();
        // Image
        prx.setOriginalSettingsInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));

        // create a dataset
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());

        // Dataset
        Map<Boolean, List<Long>> m = prx.applySettingsToSet(id,
                Dataset.class.getName(), Arrays.asList(d.getId().getValue()));
        Assert.assertNotNull(m);
    }

    /**
     * Tests to apply the rendering settings to a collection of images contained
     * in a project. Tests the <code>ApplySettingsToSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testApplySettingsToSetForProject() throws Exception {
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        Image image = createBinaryImage();
        Pixels pixels = image.getPrimaryPixels();
        long id = pixels.getId().getValue();
        // Image
        // method already tested
        prx.setOriginalSettingsInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));
        // method already tested
        RenderingDef def = factory.getPixelsService().retrieveRndSettings(id);
        // Create a second image.
        Image image2 = createBinaryImage();
        // Create a dataset
        // Link image and dataset
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());

        Project project = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());

        ProjectDatasetLink pLink = new ProjectDatasetLinkI();
        pLink.link(project, d);
        iUpdate.saveAndReturnObject(pLink);

        DatasetImageLink link = new DatasetImageLinkI();
        link.link(new DatasetI(d.getId().getValue(), false), image2);
        iUpdate.saveAndReturnObject(link);

        Map<Boolean, List<Long>> m = prx.applySettingsToSet(id,
                Project.class.getName(),
                Arrays.asList(project.getId().getValue()));
        Assert.assertNotNull(m);
        List<Long> success = (List<Long>) m.get(Boolean.valueOf(true));
        List<Long> failure = (List<Long>) m.get(Boolean.valueOf(false));
        Assert.assertNotNull(success);
        Assert.assertNotNull(failure);
        Assert.assertEquals(success.size(), 1);
        Assert.assertTrue(failure.isEmpty());
        id = success.get(0); // image id.
        Assert.assertEquals(id, image2.getId().getValue());
        RenderingDef def2 = factory.getPixelsService().retrieveRndSettings(
                image2.getPrimaryPixels().getId().getValue());
        compareRenderingDef(def, def2);
    }

    /**
     * Tests to apply the rendering settings to a plate. Tests the
     * <code>ApplySettingsToSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testApplySettingsToSetForPlate() throws Exception {
        Plate p = createBinaryPlate(1, 1, 1, 0);
        p = (Plate) iUpdate.saveAndReturnObject(p);
        // load the well
        List<Well> results = loadWells(p.getId().getValue(), true);
        Well well = results.get(0);

        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        Image image = well.getWellSample(0).getImage();
        Pixels pixels = image.getPrimaryPixels();
        long id = pixels.getId().getValue();
        // Image
        // method already tested
        prx.setOriginalSettingsInSet(Plate.class.getName(),
                Arrays.asList(p.getId().getValue()));

        // method already tested
        RenderingDef def = factory.getPixelsService().retrieveRndSettings(id);

        // Create a second plate
        p = createBinaryPlate(1, 1, 1, 0);
        p = (Plate) iUpdate.saveAndReturnObject(p);
        results = loadWells(p.getId().getValue(), true);
        well = (Well) results.get(0);
        Image image2 = well.getWellSample(0).getImage();
        Map<Boolean, List<Long>> m = prx.applySettingsToSet(id,
                Plate.class.getName(), Arrays.asList(p.getId().getValue()));
        Assert.assertNotNull(m);
        List<Long> success = (List<Long>) m.get(Boolean.valueOf(true));
        List<Long> failure = (List<Long>) m.get(Boolean.valueOf(false));
        Assert.assertNotNull(success);
        Assert.assertNotNull(failure);
        Assert.assertEquals(success.size(), 1);
        Assert.assertTrue(failure.isEmpty());
        id = success.get(0); // image id.
        Assert.assertEquals(id, image2.getId().getValue());
        RenderingDef def2 = factory.getPixelsService().retrieveRndSettings(
                image2.getPrimaryPixels().getId().getValue());
        compareRenderingDef(def, def2);
    }

    /**
     * Tests to apply the rendering settings to a plate acquisition. Tests the
     * <code>ApplySettingsToSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testApplySettingsToSetForPlateAcquisition() throws Exception {
        Plate p = createBinaryPlate(1, 1, 1, 1);
        p = (Plate) iUpdate.saveAndReturnObject(p);
        // load the well
        List<Well> results = loadWells(p.getId().getValue(), true);
        Well well = results.get(0);

        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        WellSample ws = well.getWellSample(0);
        Image image = ws.getImage();
        Pixels pixels = image.getPrimaryPixels();
        long id = pixels.getId().getValue();
        // Image
        // method already tested
        prx.setOriginalSettingsInSet(PlateAcquisition.class.getName(),
                Arrays.asList(ws.getPlateAcquisition().getId().getValue()));
        // method already tested
        RenderingDef def = factory.getPixelsService().retrieveRndSettings(id);

        // Create a second plate
        p = createBinaryPlate(1, 1, 1, 1);
        p = (Plate) iUpdate.saveAndReturnObject(p);
        results = loadWells(p.getId().getValue(), true);
        well = (Well) results.get(0);
        ws = well.getWellSample(0);
        Image image2 = ws.getImage();
        Map<Boolean, List<Long>> m = prx.applySettingsToSet(id,
                PlateAcquisition.class.getName(),
                Arrays.asList(ws.getPlateAcquisition().getId().getValue()));
        Assert.assertNotNull(m);
        List<Long> success = (List<Long>) m.get(Boolean.valueOf(true));
        List<Long> failure = (List<Long>) m.get(Boolean.valueOf(false));
        Assert.assertNotNull(success);
        Assert.assertNotNull(failure);
        Assert.assertEquals(success.size(), 1);
        Assert.assertTrue(failure.isEmpty());
        id = success.get(0); // image id.
        Assert.assertEquals(id, image2.getId().getValue());
        RenderingDef def2 = factory.getPixelsService().retrieveRndSettings(
                image2.getPrimaryPixels().getId().getValue());
        compareRenderingDef(def, def2);
    }

    /**
     * Tests to apply the rendering settings to a screen. Tests the
     * <code>ApplySettingsToSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testApplySettingsToSetForScreen() throws Exception {
        Screen screen = (Screen) iUpdate.saveAndReturnObject(mmFactory
                .simpleScreenData().asIObject());
        Plate p = createBinaryPlate(1, 1, 1, 0);
        p = (Plate) iUpdate.saveAndReturnObject(p);

        ScreenPlateLink link = new ScreenPlateLinkI();
        link.setChild(p);
        link.setParent(screen);
        link = (ScreenPlateLink) iUpdate.saveAndReturnObject(link);
        screen = link.getParent();
        // load the well
        List<Well> results = loadWells(p.getId().getValue(), true);
        Well well = results.get(0);

        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        Image image = well.getWellSample(0).getImage();
        Pixels pixels = image.getPrimaryPixels();
        long id = pixels.getId().getValue();
        // Image

        prx.setOriginalSettingsInSet(Plate.class.getName(),
                Arrays.asList(p.getId().getValue()));

        // method already tested
        RenderingDef def = factory.getPixelsService().retrieveRndSettings(id);

        // Create a second plate
        p = createBinaryPlate(1, 1, 1, 0);
        p = (Plate) iUpdate.saveAndReturnObject(p);

        link = new ScreenPlateLinkI();
        link.setChild(p);
        link.setParent(screen);
        link = (ScreenPlateLink) iUpdate.saveAndReturnObject(link);

        results = loadWells(p.getId().getValue(), true);
        well = results.get(0);
        Image image2 = well.getWellSample(0).getImage();
        Map<Boolean, List<Long>> m = prx.applySettingsToSet(id,
                Screen.class.getName(),
                Arrays.asList(screen.getId().getValue()));
        Assert.assertNotNull(m);
        List<Long> success = (List<Long>) m.get(Boolean.valueOf(true));
        List<Long> failure = (List<Long>) m.get(Boolean.valueOf(false));
        Assert.assertNotNull(success);
        Assert.assertNotNull(failure);
        Assert.assertEquals(success.size(), 1);
        Assert.assertTrue(failure.isEmpty());
        id = success.get(0); // image id.
        Assert.assertEquals(id, image2.getId().getValue());
        RenderingDef def2 = factory.getPixelsService().retrieveRndSettings(
                image2.getPrimaryPixels().getId().getValue());
        compareRenderingDef(def, def2);

    }

    /**
     * Tests to reset the default rendering settings to a plate. Tests the
     * <code>ResetDefaultInSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testResetDefaultInSetForPlate() throws Exception {
        Plate p = createBinaryPlate(1, 1, 1, 0);
        p = (Plate) iUpdate.saveAndReturnObject(p);
        // load the well
        List<Well> results = loadWells(p.getId().getValue(), true);
        Well well = results.get(0);
        Image image = well.getWellSample(0).getImage();
        Pixels pixels = image.getPrimaryPixels();
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        prx.setOriginalSettingsInSet(Plate.class.getName(),
                Arrays.asList(p.getId().getValue()));
        // Image
        List<Long> ids = new ArrayList<Long>();
        ids.add(p.getId().getValue());
        List<Long> v = prx.resetDefaultsInSet(Plate.class.getName(), ids);
        Assert.assertNotNull(v);
        Assert.assertEquals(v.size(), 1);
        ParametersI param = new ParametersI();
        param.addLong("pid", pixels.getId().getValue());
        String sql = "select rdef from RenderingDef as rdef "
                + "where rdef.pixels.id = :pid";
        List<IObject> values = iQuery.findAllByQuery(sql, param);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 1);
    }

    /**
     * Tests to reset the default rendering settings to a plate acquisition.
     * Tests the <code>ResetDefaultInSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testResetDefaultInSetForPlateAcquisition() throws Exception {
        Plate p = createBinaryPlate(1, 1, 1, 1);
        p = (Plate) iUpdate.saveAndReturnObject(p);
        // load the well
        List<Well> results = loadWells(p.getId().getValue(), true);
        Well well = results.get(0);
        WellSample ws = well.getWellSample(0);
        Image image = ws.getImage();
        Pixels pixels = image.getPrimaryPixels();
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        prx.setOriginalSettingsInSet(PlateAcquisition.class.getName(),
                Arrays.asList(ws.getPlateAcquisition().getId().getValue()));
        // Image
        List<Long> ids = new ArrayList<Long>();
        ids.add(ws.getPlateAcquisition().getId().getValue());
        List<Long> v = prx.resetDefaultsInSet(PlateAcquisition.class.getName(),
                ids);
        Assert.assertNotNull(v);
        Assert.assertEquals(v.size(), 1);
        ParametersI param = new ParametersI();
        param.addLong("pid", pixels.getId().getValue());
        String sql = "select rdef from RenderingDef as rdef "
                + "where rdef.pixels.id = :pid";
        List<IObject> values = iQuery.findAllByQuery(sql, param);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 1);
    }

    /**
     * Tests to apply the rendering settings to a collection of images. Tests
     * the <code>ResetMinMaxForSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testResetMinMaxForSetForImage() throws Exception {
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        Image image = createBinaryImage();
        Pixels pixels = image.getPrimaryPixels();
        long id = pixels.getId().getValue();
        // Image
        // method already tested
        prx.setOriginalSettingsInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));

        // method already tested
        RenderingDef def = factory.getPixelsService().retrieveRndSettings(id);

        // Modified the settings.
        ChannelBinding channel;
        List<Point> list = new ArrayList<Point>();

        Point p;
        List<IObject> toUpdate = new ArrayList<IObject>();
        for (int i = 0; i < pixels.getSizeC().getValue(); i++) {
            channel = def.getChannelBinding(0);
            p = new Point();
            p.setLocation(channel.getInputStart().getValue(), channel
                    .getInputEnd().getValue());
            list.add(p);
            channel.setInputStart(ode.rtypes.rdouble(1));
            channel.setInputEnd(ode.rtypes.rdouble(2));
            toUpdate.add(channel);
        }
        iUpdate.saveAndReturnArray(toUpdate);
        List<Long> m = prx.resetMinMaxInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));
        Assert.assertNotNull(m);
        Assert.assertEquals(m.size(), 1);
        def = factory.getPixelsService().retrieveRndSettings(id);
        for (int i = 0; i < pixels.getSizeC().getValue(); i++) {
            channel = def.getChannelBinding(i);
            p = list.get(i);
            Assert.assertEquals(channel.getInputStart().getValue(), p.getX());
            Assert.assertEquals(channel.getInputEnd().getValue(), p.getY());
        }
    }

    /**
     * Tests to apply the rendering settings to a collection of images. Tests
     * the <code>ResetMinMaxForSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testResetMinMaxForSetForDataset() throws Exception {
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        Image image = createBinaryImage();
        Pixels pixels = image.getPrimaryPixels();
        long id = pixels.getId().getValue();
        // Image
        // method already tested
        prx.setOriginalSettingsInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));

        // method already tested
        RenderingDef def = factory.getPixelsService().retrieveRndSettings(id);

        // Modified the settings.
        ChannelBinding channel;
        List<Point> list = new ArrayList<Point>();

        Point p;
        List<IObject> toUpdate = new ArrayList<IObject>();
        for (int i = 0; i < pixels.getSizeC().getValue(); i++) {
            channel = def.getChannelBinding(0);
            p = new Point();
            p.setLocation(channel.getInputStart().getValue(), channel
                    .getInputEnd().getValue());
            list.add(p);
            channel.setInputStart(ode.rtypes.rdouble(1));
            channel.setInputEnd(ode.rtypes.rdouble(2));
            toUpdate.add(channel);
        }
        iUpdate.saveAndReturnArray(toUpdate);
        // Link image and dataset
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        DatasetImageLink link = new DatasetImageLinkI();
        link.setChild(image);
        link.setParent(d);
        iUpdate.saveAndReturnObject(link);
        List<Long> m = prx.resetMinMaxInSet(Dataset.class.getName(),
                Arrays.asList(d.getId().getValue()));
        Assert.assertNotNull(m);
        Assert.assertEquals(m.size(), 1);
        def = factory.getPixelsService().retrieveRndSettings(id);
        for (int i = 0; i < pixels.getSizeC().getValue(); i++) {
            channel = def.getChannelBinding(i);
            p = list.get(i);
            Assert.assertEquals(channel.getInputStart().getValue(), p.getX());
            Assert.assertEquals(channel.getInputEnd().getValue(), p.getY());
        }
    }

    /**
     * Tests to apply the rendering settings to a collection of images. Tests
     * the <code>ResetMinMaxForSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testResetMinMaxForSetForEmptyDataset() throws Exception {
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        prx.setOriginalSettingsInSet(Dataset.class.getName(),
                Arrays.asList(d.getId().getValue()));
        List<Long> m = prx.resetMinMaxInSet(Dataset.class.getName(),
                Arrays.asList(d.getId().getValue()));
        Assert.assertTrue(m.isEmpty());
    }

    /**
     * Tests to apply the rendering settings to a collection of images. Tests
     * the <code>ResetMinMaxForSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:5755")
    public void testResetMinMaxForSetForProject() throws Exception {
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        Image image = createBinaryImage();
        Pixels pixels = image.getPrimaryPixels();
        long id = pixels.getId().getValue();
        // Image
        // method already tested
        prx.setOriginalSettingsInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));
        // method already tested
        RenderingDef def = factory.getPixelsService().retrieveRndSettings(id);
        Assert.assertNotNull(def);

        // Modified the settings.
        ChannelBinding channel;
        List<Point> list = new ArrayList<Point>();

        Point p;
        List<IObject> toUpdate = new ArrayList<IObject>();
        for (int i = 0; i < pixels.getSizeC().getValue(); i++) {
            channel = def.getChannelBinding(0);
            p = new Point();
            p.setLocation(channel.getInputStart().getValue(), channel
                    .getInputEnd().getValue());
            list.add(p);
            channel.setInputStart(ode.rtypes.rdouble(1));
            channel.setInputEnd(ode.rtypes.rdouble(2));
            toUpdate.add(channel);
        }
        iUpdate.saveAndReturnArray(toUpdate);
        // Link image and dataset
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());

        Project project = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());

        ProjectDatasetLink pLink = new ProjectDatasetLinkI();
        pLink.link(project, d);
        iUpdate.saveAndReturnObject(pLink);

        DatasetImageLink link = new DatasetImageLinkI();
        link.link(new DatasetI(d.getId().getValue(), false), image);
        iUpdate.saveAndReturnObject(link);

        List<Long> m = prx.resetMinMaxInSet(Project.class.getName(),
                Arrays.asList(project.getId().getValue()));
        Assert.assertNotNull(m);
        Assert.assertEquals(m.size(), 1);
        def = factory.getPixelsService().retrieveRndSettings(id);
        for (int i = 0; i < pixels.getSizeC().getValue(); i++) {
            channel = def.getChannelBinding(i);
            p = list.get(i);
            Assert.assertEquals(channel.getInputStart().getValue(), p.getX());
            Assert.assertEquals(channel.getInputEnd().getValue(), p.getY());
        }

    }

    /**
     * Tests to apply the rendering settings to a plate. Tests the
     * <code>ResetMinMaxForSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testResetMinMaxForSetForPlate() throws Exception {
        Plate plate = createBinaryPlate(1, 1, 1, 0);
        plate = (Plate) iUpdate.saveAndReturnObject(plate);
        // load the well
        List<Well> results = loadWells(plate.getId().getValue(), true);
        Well well = results.get(0);

        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        Image image = well.getWellSample(0).getImage();
        Pixels pixels = image.getPrimaryPixels();
        long id = pixels.getId().getValue();
        // Image
        prx.setOriginalSettingsInSet(Plate.class.getName(),
                Arrays.asList(plate.getId().getValue()));

        // method already tested
        RenderingDef def = factory.getPixelsService().retrieveRndSettings(id);
        // Modified the settings.
        ChannelBinding channel;
        List<Point> list = new ArrayList<Point>();

        Point p;
        List<IObject> toUpdate = new ArrayList<IObject>();
        for (int i = 0; i < pixels.getSizeC().getValue(); i++) {
            channel = def.getChannelBinding(0);
            p = new Point();
            p.setLocation(channel.getInputStart().getValue(), channel
                    .getInputEnd().getValue());
            list.add(p);
            channel.setInputStart(ode.rtypes.rdouble(1));
            channel.setInputEnd(ode.rtypes.rdouble(2));
            toUpdate.add(channel);
        }
        iUpdate.saveAndReturnArray(toUpdate);

        List<Long> m = prx.resetMinMaxInSet(Plate.class.getName(),
                Arrays.asList(plate.getId().getValue()));
        Assert.assertNotNull(m);
        Assert.assertEquals(m.size(), 1);
        def = factory.getPixelsService().retrieveRndSettings(id);
        for (int i = 0; i < pixels.getSizeC().getValue(); i++) {
            channel = def.getChannelBinding(i);
            p = list.get(i);
            Assert.assertEquals(channel.getInputStart().getValue(), p.getX());
            Assert.assertEquals(channel.getInputEnd().getValue(), p.getY());
        }
    }

    /**
     * Tests to apply the rendering settings to a plate. Tests the
     * <code>ResetMinMaxForSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testResetMinMaxForSetForPlateAcquisition() throws Exception {
        Plate plate = createBinaryPlate(1, 1, 1, 1);
        plate = (Plate) iUpdate.saveAndReturnObject(plate);
        // load the well
        List<Well> results = loadWells(plate.getId().getValue(), true);
        Well well = results.get(0);

        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        WellSample ws = well.getWellSample(0);
        Image image = ws.getImage();
        Pixels pixels = image.getPrimaryPixels();
        long id = pixels.getId().getValue();
        // Image
        // method already tested
        prx.setOriginalSettingsInSet(PlateAcquisition.class.getName(),
                Arrays.asList(ws.getPlateAcquisition().getId().getValue()));
        // method already tested
        RenderingDef def = factory.getPixelsService().retrieveRndSettings(id);
        // Modified the settings.
        ChannelBinding channel;
        List<Point> list = new ArrayList<Point>();

        Point p;
        List<IObject> toUpdate = new ArrayList<IObject>();
        for (int i = 0; i < pixels.getSizeC().getValue(); i++) {
            channel = def.getChannelBinding(0);
            p = new Point();
            p.setLocation(channel.getInputStart().getValue(), channel
                    .getInputEnd().getValue());
            list.add(p);
            channel.setInputStart(ode.rtypes.rdouble(1));
            channel.setInputEnd(ode.rtypes.rdouble(2));
            toUpdate.add(channel);
        }
        iUpdate.saveAndReturnArray(toUpdate);

        List<Long> m = prx.resetMinMaxInSet(PlateAcquisition.class.getName(),
                Arrays.asList(ws.getPlateAcquisition().getId().getValue()));
        Assert.assertNotNull(m);
        Assert.assertEquals(m.size(), 1);
        def = factory.getPixelsService().retrieveRndSettings(id);
        for (int i = 0; i < pixels.getSizeC().getValue(); i++) {
            channel = def.getChannelBinding(i);
            p = list.get(i);
            Assert.assertEquals(channel.getInputStart().getValue(), p.getX());
            Assert.assertEquals(channel.getInputEnd().getValue(), p.getY());
        }
    }

    /**
     * Tests to apply reset the min/max values for a screen.s Tests the
     * <code>ResetMinMaxForSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:5755")
    public void testResetMinMaxForSetForScreen() throws Exception {
        Screen screen = (Screen) iUpdate.saveAndReturnObject(mmFactory
                .simpleScreenData().asIObject());
        Plate plate = createBinaryPlate(1, 1, 1, 0);
        plate = (Plate) iUpdate.saveAndReturnObject(plate);

        ScreenPlateLink link = new ScreenPlateLinkI();
        link.setChild(plate);
        link.setParent(screen);
        link = (ScreenPlateLink) iUpdate.saveAndReturnObject(link);
        screen = link.getParent();
        // load the well
        List<Well> results = loadWells(plate.getId().getValue(), true);
        Well well = results.get(0);

        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        Image image = well.getWellSample(0).getImage();
        Pixels pixels = image.getPrimaryPixels();
        long id = pixels.getId().getValue();

        prx.setOriginalSettingsInSet(Plate.class.getName(),
                Arrays.asList(plate.getId().getValue()));
        // method already tested
        RenderingDef def = factory.getPixelsService().retrieveRndSettings(id);
        // Modified the settings.
        ChannelBinding channel;
        List<Point> list = new ArrayList<Point>();

        Point p;
        List<IObject> toUpdate = new ArrayList<IObject>();
        for (int i = 0; i < pixels.getSizeC().getValue(); i++) {
            channel = def.getChannelBinding(0);
            p = new Point();
            p.setLocation(channel.getInputStart().getValue(), channel
                    .getInputEnd().getValue());
            list.add(p);
            channel.setInputStart(ode.rtypes.rdouble(1));
            channel.setInputEnd(ode.rtypes.rdouble(2));
            toUpdate.add(channel);
        }
        iUpdate.saveAndReturnArray(toUpdate);

        List<Long> m = prx.resetMinMaxInSet(Plate.class.getName(),
                Arrays.asList(plate.getId().getValue()));
        Assert.assertNotNull(m);
        Assert.assertEquals(m.size(), 1);
        def = factory.getPixelsService().retrieveRndSettings(id);
        for (int i = 0; i < pixels.getSizeC().getValue(); i++) {
            channel = def.getChannelBinding(i);
            p = list.get(i);
            Assert.assertEquals(channel.getInputStart().getValue(), p.getX());
            Assert.assertEquals(channel.getInputEnd().getValue(), p.getY());
        }
    }

    /**
     * Tests to apply the rendering settings to a collection of images. Tests
     * the <code>ApplySettingsToSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testSetOriginalSettingsInSet() throws Exception {
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        Image image = createBinaryImage();
        Pixels pixels = image.getPrimaryPixels();
        long id = pixels.getId().getValue();
        // Image
        // method already tested
        prx.setOriginalSettingsInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));

        // method already tested
        RenderingDef def = factory.getPixelsService().retrieveRndSettings(id);
        // Create a second image.
        Image image2 = createBinaryImage();
        Map<Boolean, List<Long>> m = prx
                .applySettingsToSet(id, Image.class.getName(),
                        Arrays.asList(image2.getId().getValue()));
        Assert.assertNotNull(m);
        List<Long> success = (List<Long>) m.get(Boolean.valueOf(true));
        List<Long> failure = (List<Long>) m.get(Boolean.valueOf(false));
        Assert.assertNotNull(success);
        Assert.assertNotNull(failure);
        Assert.assertEquals(success.size(), 1);
        Assert.assertTrue(failure.isEmpty());
        id = success.get(0); // image id.
        Assert.assertEquals(id, image2.getId().getValue());
        RenderingDef def2 = factory.getPixelsService().retrieveRndSettings(
                image2.getPrimaryPixels().getId().getValue());
        compareRenderingDef(def, def2);
    }

    /**
     * Tests to apply the rendering settings to a collection of images. Tests
     * the <code>ApplySettingsToSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testResetDefaultByOwner() throws Exception {
        EventContext ctx = newUserAndGroup("rwra--");
        Image image = createBinaryImage();
        Pixels pixels = image.getPrimaryPixels();
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        prx.setOriginalSettingsInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));

        disconnect();
        EventContext ctx2 = newUserInGroup(ctx);
        prx = factory.getRenderingSettingsService();
        prx.setOriginalSettingsInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));

        // Image
        List<Long> v = prx.resetDefaultsByOwnerInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));
        Assert.assertNotNull(v);
        Assert.assertEquals(v.size(), 1);
        ParametersI param = new ParametersI();
        param.addLong("pid", pixels.getId().getValue());
        param.addLong("oid", ctx2.userId);
        String sql = "select rdef from RenderingDef as rdef "
                + "where rdef.pixels.id = :pid and rdef.details.owner.id = :oid";
        List<IObject> values = iQuery.findAllByQuery(sql, param);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 1);
    }

    /**
     * Tests to apply the rendering settings to a collection of images. Tests
     * the <code>ApplySettingsToSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testResetDefaultByOwnerNoRndSettingsTarget() throws Exception {
        EventContext ctx = newUserAndGroup("rwra--");
        Image image = createBinaryImage();
        Pixels pixels = image.getPrimaryPixels();
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        prx.setOriginalSettingsInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));

        disconnect();
        EventContext ctx2 = newUserInGroup(ctx);
        prx = factory.getRenderingSettingsService();

        // in that case create rendering settings for the target.
        // Image
        List<Long> v = prx.resetDefaultsByOwnerInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));
        Assert.assertNotNull(v);
        Assert.assertEquals(v.size(), 1);
        ParametersI param = new ParametersI();
        param.addLong("pid", pixels.getId().getValue());
        param.addLong("oid", ctx2.userId);
        String sql = "select rdef from RenderingDef as rdef "
                + "where rdef.pixels.id = :pid and rdef.details.owner.id = :oid";
        List<IObject> values = iQuery.findAllByQuery(sql, param);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 1);
    }

    /**
     * Tests to apply the rendering settings to a collection of images. Tests
     * the <code>ApplySettingsToSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testResetDefaultByOwnerNoRndSettingsSource() throws Exception {
        EventContext ctx = newUserAndGroup("rwra--");
        Image image = createBinaryImage();
        Pixels pixels = image.getPrimaryPixels();
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();

        disconnect();
        EventContext ctx2 = newUserInGroup(ctx);
        prx = factory.getRenderingSettingsService();
        prx.setOriginalSettingsInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));
        // Image
        List<Long> v = prx.resetDefaultsByOwnerInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));
        Assert.assertNotNull(v);
        Assert.assertTrue(v.isEmpty());
    }

    /**
     * Tests to apply the rendering settings to a collection of images. Tests
     * the <code>ResetMinMaxForSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testResetMinMaxForSetForImageNoSettings() throws Exception {
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        Image image = createBinaryImage();
        // Image
        // method already tested
        List<Long> m = prx.resetMinMaxInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));
        Assert.assertNotNull(m);
        Assert.assertEquals(m.size(), 1);
    }

    /**
     * Tests to apply the rendering settings to a collection of images. Tests
     * the <code>ResetMinMaxForSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testResetMinMaxForSetForNonValidImage() throws Exception {
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        Image image = mmFactory.simpleImage();
        image = (Image) iUpdate.saveAndReturnObject(image);
        // Image
        // method already tested
        List<Long> m = prx.resetMinMaxInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));
        Assert.assertNotNull(m);
        Assert.assertTrue(m.isEmpty());
    }

    /**
     * Tests to apply the rendering settings to a collection of images. Tests
     * the <code>ApplySettingsToSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testResetDefaultByOwnerNonValidImage() throws Exception {
        EventContext ctx = newUserAndGroup("rwra--");
        Image image = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());
        // Image
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        List<Long> v = prx.resetDefaultsByOwnerInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));
        Assert.assertNotNull(v);
        Assert.assertTrue(v.isEmpty());
    }

    /**
     * Tests to apply the rendering settings to a collection of images. Tests
     * the <code>ApplySettingsToSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testSetOriginalSettingsInSetNonValidImage() throws Exception {
        EventContext ctx = newUserAndGroup("rwra--");
        Image image = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());
        // Image
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        List<Long> v = prx.setOriginalSettingsInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));
        Assert.assertNotNull(v);
        Assert.assertTrue(v.isEmpty());
    }

    /**
     * Tests to apply the rendering settings to a collection of images. Tests
     * the <code>ApplySettingsToSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testResetDefaultInSetNonValidImage() throws Exception {
        EventContext ctx = newUserAndGroup("rwra--");
        Image image = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());
        // Image
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        List<Long> v = prx.resetDefaultsInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));
        Assert.assertNotNull(v);
        Assert.assertTrue(v.isEmpty());
    }

    /**
     * Tests to apply the rendering settings to an image w/o stats info Tests
     * the <code>ResetMinMaxForSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testResetMinMaxForSetForImageNoStatsInfo() throws Exception {
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        Image image = createBinaryImage();
        // Delete the stats info object.
        Pixels pixels = image.getPixels(0);
        // load the channel.
        Channel channel;
        String sql = "select c from Channel as c where c.pixels.id = :iid";
        ParametersI param = new ParametersI();
        param.addLong("iid", pixels.getId().getValue());
        List<IObject> channels = iQuery.findAllByQuery(sql, param);
        Iterator<IObject> i = channels.iterator();
        while (i.hasNext()) {
            channel = (Channel) i.next();
            channel.setStatsInfo(null);
            iUpdate.saveAndReturnObject(channel);
        }

        // Make sure the channels not have stats info.
        channels = iQuery.findAllByQuery(sql, param);
        i = channels.iterator();
        while (i.hasNext()) {
            channel = (Channel) i.next();
            Assert.assertNull(channel.getStatsInfo());
        }
        // Image
        // method already tested
        List<Long> m = prx.resetMinMaxInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));
        Assert.assertNotNull(m);
        Assert.assertEquals(m.size(), 1);
    }

    /**
     * Tests to apply the rendering settings of an image with Lut and
     * codomain transformation
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testApplySettingsWithLutAndCodomain() throws Exception {
        IScriptPrx svc = factory.getScriptService();
        List<OriginalFile> luts = svc.getScriptsByMimetype(
                ScriptServiceTest.LUT_MIMETYPE);
        Assert.assertNotNull(luts);
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        Image image = createBinaryImage();
        Pixels pixels = image.getPrimaryPixels();
        long id = pixels.getId().getValue();
        // Image
        prx.setOriginalSettingsInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));
        RenderingDef def1 = factory.getPixelsService().retrieveRndSettings(id);
        RenderingEnginePrx re = factory.createRenderingEngine();
        re.lookupPixels(id);
        if (!(re.lookupRenderingDef(id))) {
            re.resetDefaultSettings(true);
            re.lookupRenderingDef(id);
        }
        re.load();
        List<ChannelBinding> channels = def1.copyWaveRendering();

        for (int k = 0; k < channels.size(); k++) {
            ode.romio.ReverseIntensityMapContext ctx = new ode.romio.ReverseIntensityMapContext();
            re.addCodomainMapToChannel(ctx, k);
            re.setChannelLookupTable(k, luts.get(0).getName().getValue());
        }
        re.saveCurrentSettings();
        // method already tested
        re.close();
        def1 = factory.getPixelsService().retrieveRndSettings(id);
        // Create a second image.
        Image image2 = createBinaryImage();
        Map<Boolean, List<Long>> m = prx
                .applySettingsToSet(id, Image.class.getName(),
                        Arrays.asList(image2.getId().getValue()));
        Assert.assertNotNull(m);
        List<Long> success = (List<Long>) m.get(Boolean.valueOf(true));
        List<Long> failure = (List<Long>) m.get(Boolean.valueOf(false));
        Assert.assertNotNull(success);
        Assert.assertNotNull(failure);
        Assert.assertEquals(success.size(), 1);
        Assert.assertTrue(failure.isEmpty());
        id = success.get(0); // image id.
        Assert.assertEquals(id, image2.getId().getValue());
        RenderingDef def2 = factory.getPixelsService().retrieveRndSettings(
                image2.getPrimaryPixels().getId().getValue());
        compareRenderingDef(def1, def2);
        //Check if we have lut
        List<ChannelBinding> channels1 = def1.copyWaveRendering();
        List<ChannelBinding> channels2 = def2.copyWaveRendering();
        ChannelBinding c1, c2;
        int index = 0;
        Iterator<ChannelBinding> i = channels1.iterator();
        while (i.hasNext()) {
            c1 = i.next();
            c2 = channels2.get(index);
            //Check lut
            Assert.assertEquals(c1.getLookupTable().getValue(),
                    c2.getLookupTable().getValue());
            List<CodomainMapContext> ctxList1 = c1.copySpatialDomainEnhancement();
            List<CodomainMapContext> ctxList2 = c2.copySpatialDomainEnhancement();
            Assert.assertEquals(ctxList1.size(), ctxList2.size());
            Assert.assertTrue(ctxList1.size() > 0);
            Iterator<CodomainMapContext> j = ctxList1.iterator();
            int k = 0;
            CodomainMapContext ctx1, ctx2;
            while (j.hasNext()) {
                ctx1 = j.next();
                ctx2 = ctxList2.get(k);
                Assert.assertEquals(ctx1.getClass(), ctx2.getClass());
                Assert.assertNotEquals(ctx1.getId().getValue(), ctx2.getId().getValue());
            }
        }
    }

    /**
     * Tests to apply the rendering settings of an image
     * codomain transformation and remove it.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testApplySettingsWithCodomainAndRemove() throws Exception {
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        Image image = createBinaryImage();
        Pixels pixels = image.getPrimaryPixels();
        long id = pixels.getId().getValue();
        // Image
        prx.setOriginalSettingsInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));
        RenderingDef def1 = factory.getPixelsService().retrieveRndSettings(id);
        RenderingEnginePrx re = factory.createRenderingEngine();
        re.lookupPixels(id);
        if (!(re.lookupRenderingDef(id))) {
            re.resetDefaultSettings(true);
            re.lookupRenderingDef(id);
        }
        re.load();
        List<ChannelBinding> channels = def1.copyWaveRendering();

        for (int k = 0; k < channels.size(); k++) {
            ode.romio.ReverseIntensityMapContext ctx = new ode.romio.ReverseIntensityMapContext();
            re.addCodomainMapToChannel(ctx, k);
        }
        re.saveCurrentSettings();
        // method already tested
        re.close();
        def1 = factory.getPixelsService().retrieveRndSettings(id);
        // Create a second image.
        Image image2 = createBinaryImage();
        Map<Boolean, List<Long>> m = prx
                .applySettingsToSet(id, Image.class.getName(),
                        Arrays.asList(image2.getId().getValue()));
        Assert.assertNotNull(m);
        List<Long> success = (List<Long>) m.get(Boolean.valueOf(true));
        List<Long> failure = (List<Long>) m.get(Boolean.valueOf(false));
        Assert.assertNotNull(success);
        Assert.assertNotNull(failure);
        Assert.assertEquals(success.size(), 1);
        Assert.assertTrue(failure.isEmpty());
        Assert.assertEquals(success.get(0).longValue(), image2.getId().getValue());
        RenderingDef def2 = factory.getPixelsService().retrieveRndSettings(
                image2.getPrimaryPixels().getId().getValue());
        compareRenderingDef(def1, def2);
        List<ChannelBinding> channels1 = def1.copyWaveRendering();
        List<ChannelBinding> channels2 = def2.copyWaveRendering();
        ChannelBinding c1, c2;
        int index = 0;
        Iterator<ChannelBinding> i = channels1.iterator();
        while (i.hasNext()) {
            c1 = i.next();
            c2 = channels2.get(index);
            List<CodomainMapContext> ctxList1 = c1.copySpatialDomainEnhancement();
            List<CodomainMapContext> ctxList2 = c2.copySpatialDomainEnhancement();
            Assert.assertEquals(ctxList1.size(), ctxList2.size());
            Assert.assertTrue(ctxList1.size() > 0);
            Iterator<CodomainMapContext> j = ctxList1.iterator();
            int k = 0;
            CodomainMapContext ctx1, ctx2;
            while (j.hasNext()) {
                ctx1 = j.next();
                ctx2 = ctxList2.get(k);
                Assert.assertEquals(ctx1.getClass(), ctx2.getClass());
                Assert.assertNotEquals(ctx1.getId().getValue(), ctx2.getId().getValue());
            }
        }
        //remove the codomain from image 1
        id = pixels.getId().getValue();
        re = factory.createRenderingEngine();
        re.lookupPixels(id);
        if (!(re.lookupRenderingDef(id))) {
            re.resetDefaultSettings(true);
            re.lookupRenderingDef(id);
        }
        re.load();
        def1 = factory.getPixelsService().retrieveRndSettings(id);
        channels = def1.copyWaveRendering();
        for (int k = 0; k < channels.size(); k++) {
            ode.romio.ReverseIntensityMapContext ctx = new ode.romio.ReverseIntensityMapContext();
            re.removeCodomainMapFromChannel(ctx, k);
        }
        re.saveCurrentSettings();
        // method already tested
        re.close();
        def1 = factory.getPixelsService().retrieveRndSettings(id);
        m = prx.applySettingsToSet(id, Image.class.getName(),
                        Arrays.asList(image2.getId().getValue()));
        Assert.assertNotNull(m);
        success = (List<Long>) m.get(Boolean.valueOf(true));
        failure = (List<Long>) m.get(Boolean.valueOf(false));
        Assert.assertNotNull(success);
        Assert.assertNotNull(failure);
        Assert.assertEquals(success.size(), 1);
        Assert.assertTrue(failure.isEmpty());
        Assert.assertEquals(success.get(0).longValue(), image2.getId().getValue());
        def2 = factory.getPixelsService().retrieveRndSettings(
                image2.getPrimaryPixels().getId().getValue());
        compareRenderingDef(def1, def2);
        channels1 = def1.copyWaveRendering();
        channels2 = def2.copyWaveRendering();
        index = 0;
        i = channels1.iterator();
        while (i.hasNext()) {
            c1 = i.next();
            c2 = channels2.get(index);
            List<CodomainMapContext> ctxList1 = c1.copySpatialDomainEnhancement();
            List<CodomainMapContext> ctxList2 = c2.copySpatialDomainEnhancement();
            Assert.assertEquals(ctxList1.size(), ctxList2.size());
            Assert.assertEquals(ctxList1.size(), 0);
        }
    }

    /**
     * Tests to apply the rendering settings to a collection of images. Tests
     * the <code>ApplySettingsToSet</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testResetDefaultByOwnerInSetCodomain() throws Exception {
        EventContext ctx = newUserAndGroup("rwra--");
        Image image = createBinaryImage();
        Pixels pixels = image.getPrimaryPixels();
        long id = pixels.getId().getValue();
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        prx.setOriginalSettingsInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));
        RenderingDef defOwner = factory.getPixelsService().retrieveRndSettings(id);
        List<ChannelBinding> channels = defOwner.copyWaveRendering();
        ChannelBinding cb;
        List<CodomainMapContext> l;
        for (int k = 0; k < channels.size(); k++) {
            cb = channels.get(k);
            l = cb.copySpatialDomainEnhancement();
            Assert.assertEquals(l.size(), 0);
        }
        disconnect();
        EventContext ctx2 = newUserInGroup(ctx);
        prx = factory.getRenderingSettingsService();
        prx.setOriginalSettingsInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));
        
        // Image
        RenderingDef def1 = factory.getPixelsService().retrieveRndSettings(id);
        RenderingEnginePrx re = factory.createRenderingEngine();
        re.lookupPixels(id);
        if (!(re.lookupRenderingDef(id))) {
            re.resetDefaultSettings(true);
            re.lookupRenderingDef(id);
        }
        re.load();
        channels = def1.copyWaveRendering();

        for (int k = 0; k < channels.size(); k++) {
            ode.romio.ReverseIntensityMapContext reverse = new ode.romio.ReverseIntensityMapContext();
            re.addCodomainMapToChannel(reverse, k);
        }
        re.saveCurrentSettings();
        // method already tested
        re.close();
        def1 = factory.getPixelsService().retrieveRndSettings(id);
        channels = def1.copyWaveRendering();
        for (int k = 0; k < channels.size(); k++) {
            cb = channels.get(k);
            l = cb.copySpatialDomainEnhancement();
            Assert.assertEquals(l.size(), 1);
        }
        List<Long> v = prx.resetDefaultsByOwnerInSet(Image.class.getName(),
                Arrays.asList(image.getId().getValue()));
        Assert.assertNotNull(v);
        Assert.assertEquals(v.size(), 1);
        def1 = factory.getPixelsService().retrieveRndSettings(id);
        channels = def1.copyWaveRendering();
        for (int k = 0; k < channels.size(); k++) {
            cb = channels.get(k);
            l = cb.copySpatialDomainEnhancement();
            Assert.assertEquals(l.size(), 0);
        }
    }

    /**
     * Tests to the default color assigned.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testDefaultColor() throws Exception {
        Image image = createBinaryImage(128, 128, 1, 1, 6);
        Pixels pixels = image.getPrimaryPixels();
        long id = pixels.getId().getValue();
        //Do not set emission wavelength
        pixels = factory.getPixelsService().retrievePixDescription(id);
        List<IObject> toUpdate = new ArrayList<IObject>();
        for (int i = 0; i < pixels.getSizeC().getValue(); i++) {
            LogicalChannel lc = pixels.getChannel(i).getLogicalChannel();
            lc.unloadChannels();
            lc.setEmissionWave(null);
            toUpdate.add(lc);
        }
        factory.getUpdateService().saveAndReturnArray(toUpdate);

        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        prx.setOriginalSettingsInSet(Pixels.class.getName(),
                Arrays.asList(id));
        RenderingDef def = factory.getPixelsService().retrieveRndSettings(id);
        List<ChannelBinding> channels = def.copyWaveRendering();
        for (int i = 0; i < channels.size(); i++) {
            ChannelBinding cb = channels.get(i);
            int r = cb.getRed().getValue();
            int g = cb.getGreen().getValue();
            int b = cb.getBlue().getValue();
            int a = cb.getAlpha().getValue();
            Assert.assertEquals(a, 255);
            int v = i%3;
            switch (v) {
                case 0:
                    //red
                    Assert.assertEquals(r, 255);
                    Assert.assertEquals(g, 0);
                    Assert.assertEquals(b, 0);
                    break;
                case 1:
                    //green
                    Assert.assertEquals(r, 0);
                    Assert.assertEquals(g, 255);
                    Assert.assertEquals(b, 0);
                    break;
                case 2:
                    //blue
                    Assert.assertEquals(r, 0);
                    Assert.assertEquals(g, 0);
                    Assert.assertEquals(b, 255);
            }
        }
    }

    /**
     * Tests that the color assigned to the channel matches the range
     * of the emission wavelength.
     * First channel should be blue.
     * Second channel should be red.
     * Third channel should be green.
     *
     * By default the emission wavelength is set to 200.1. This will be mapped
     * to blue.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testEmissionWavelengthRange() throws Exception {
        Image image = createBinaryImage(128, 128, 1, 1, 3);
        Pixels pixels = image.getPrimaryPixels();
        long id = pixels.getId().getValue();
        //Do not set emission wavelength
        pixels = factory.getPixelsService().retrievePixDescription(id);
        List<IObject> toUpdate = new ArrayList<IObject>();
        for (int i = 1; i < pixels.getSizeC().getValue(); i++) {
            LogicalChannel lc = pixels.getChannel(i).getLogicalChannel();
            lc.unloadChannels();
            if (i == 1) {
                //should be mapped to red
                lc.setEmissionWave(new LengthI(600.1, UnitsFactory.Channel_EmissionWavelength));
            } else {
                //should be mapped to green
                lc.setEmissionWave(new LengthI(510.1, UnitsFactory.Channel_EmissionWavelength));
            }
            
            toUpdate.add(lc);
        }
        factory.getUpdateService().saveAndReturnArray(toUpdate);

        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        prx.setOriginalSettingsInSet(Pixels.class.getName(),
                Arrays.asList(id));
        RenderingDef def = factory.getPixelsService().retrieveRndSettings(id);
        List<ChannelBinding> channels = def.copyWaveRendering();
        for (int i = 0; i < channels.size(); i++) {
            ChannelBinding cb = channels.get(i);
            int r = cb.getRed().getValue();
            int g = cb.getGreen().getValue();
            int b = cb.getBlue().getValue();
            int a = cb.getAlpha().getValue();
            Assert.assertEquals(a, 255);
            int v = i%3;
            switch (v) {
                case 0:
                    //should be mapped to blue
                    Assert.assertEquals(r, 0);
                    Assert.assertEquals(g, 0);
                    Assert.assertEquals(b, 255);
                    break;
                case 1:
                    //should be mapped to red
                    Assert.assertEquals(r, 255);
                    Assert.assertEquals(g, 0);
                    Assert.assertEquals(b, 0);
                    break;
                case 2:
                    //should be mapped to green
                    Assert.assertEquals(r, 0);
                    Assert.assertEquals(g, 255);
                    Assert.assertEquals(b, 0);
            }
        }
    }

    /**
     * Tests to the default color assigned.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testColorSetAtImport() throws Exception {
        //This creates an image with a wavelength that will set the color to
        //blue
        Image image = createBinaryImage(128, 128, 1, 1, 1);
        Pixels pixels = image.getPrimaryPixels();
        long id = pixels.getId().getValue();
        pixels = factory.getPixelsService().retrievePixDescription(id);
        Channel channel = pixels.getChannel(0);
        //set the color as red
        channel.setRed(ode.rtypes.rint(255));
        channel.setGreen(ode.rtypes.rint(0));
        channel.setBlue(ode.rtypes.rint(0));
        channel.setAlpha(ode.rtypes.rint(255));
        factory.getUpdateService().saveAndReturnObject(channel);
        //now set the rendering settings.
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        prx.setOriginalSettingsInSet(Pixels.class.getName(),
                Arrays.asList(id));
        RenderingDef def = factory.getPixelsService().retrieveRndSettings(id);
        List<ChannelBinding> channels = def.copyWaveRendering();
        ChannelBinding cb = channels.get(0);
        //color should be red
        int r = cb.getRed().getValue();
        int g = cb.getGreen().getValue();
        int b = cb.getBlue().getValue();
        int a = cb.getAlpha().getValue();
        Assert.assertEquals(a, 255);
        Assert.assertEquals(r, 255);
        Assert.assertEquals(g, 0);
        Assert.assertEquals(b, 0);
    }

    /**
     * Tests to the default color assigned.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testColorSetAtImportReset() throws Exception {
        //This creates an image with a wavelength that will set the color to
        //blue
        Image image = createBinaryImage(128, 128, 1, 1, 1);
        Pixels pixels = image.getPrimaryPixels();
        long id = pixels.getId().getValue();
        pixels = factory.getPixelsService().retrievePixDescription(id);
        Channel channel = pixels.getChannel(0);
        //set the color as red
        channel.setRed(ode.rtypes.rint(255));
        channel.setGreen(ode.rtypes.rint(0));
        channel.setBlue(ode.rtypes.rint(0));
        channel.setAlpha(ode.rtypes.rint(255));
        factory.getUpdateService().saveAndReturnObject(channel);
        //now set the rendering settings.
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        prx.setOriginalSettingsInSet(Pixels.class.getName(),
                Arrays.asList(id));
        RenderingDef def = factory.getPixelsService().retrieveRndSettings(id);
        List<ChannelBinding> channels = def.copyWaveRendering();
        ChannelBinding cb = channels.get(0);
        cb.setGreen(ode.rtypes.rint(255));
        cb.unloadCollections();
        factory.getUpdateService().saveAndReturnObject(cb);
        def = factory.getPixelsService().retrieveRndSettings(id);
        channels = def.copyWaveRendering();
        cb = channels.get(0);
        int r = cb.getRed().getValue();
        int g = cb.getGreen().getValue();
        int b = cb.getBlue().getValue();
        int a = cb.getAlpha().getValue();
        Assert.assertEquals(a, 255);
        Assert.assertEquals(r, 255);
        Assert.assertEquals(g, 255);
        Assert.assertEquals(b, 0);
        prx.resetDefaultsForPixels(id);
        //should be back to red
        def = factory.getPixelsService().retrieveRndSettings(id);
        channels = def.copyWaveRendering();
        cb = channels.get(0);
        r = cb.getRed().getValue();
        g = cb.getGreen().getValue();
        b = cb.getBlue().getValue();
        a = cb.getAlpha().getValue();
        Assert.assertEquals(a, 255);
        Assert.assertEquals(r, 255);
        Assert.assertEquals(g, 0);
        Assert.assertEquals(b, 0);
    }
}
