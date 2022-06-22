package integration.gateway;

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

import integration.ModelMockFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import ode.gateway.SecurityContext;
import ode.gateway.exception.DSAccessException;
import ode.gateway.exception.DSOutOfServiceException;
import ode.model.IObject;
import ode.model.Plate;
import ode.model.Well;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ode.gateway.model.DatasetData;
import ode.gateway.model.ExperimenterData;
import ode.gateway.model.FolderData;
import ode.gateway.model.GroupData;
import ode.gateway.model.ImageData;
import ode.gateway.model.PlateData;
import ode.gateway.model.ProjectData;
import ode.gateway.model.ScreenData;
import ode.gateway.model.WellData;
import ode.gateway.util.PojoMapper;

public class BrowseFacilityTest extends GatewayTest {

    private GroupData group;
    private ExperimenterData user; // has proj, ds and img
    private ExperimenterData user2; // has proj, ds and img
    private ExperimenterData user3; // has no data

    private ImageData img0;
    
    private ProjectData proj;
    private DatasetData ds;
    private ScreenData screen;
    private PlateData plate;
    private ImageData img1;

    private ProjectData proj2;
    private DatasetData ds2;
    private ScreenData screen2;
    private PlateData plate2;
    private ImageData img2;
    
    private FolderData userFolder1;
    private FolderData userFolder2;
    private FolderData userFolder3;
    private FolderData user2Folder;
    
    private PlateData wellsPlate;
    private ArrayList<Long> wellIds;
    
    @Override
    @BeforeClass(alwaysRun = true)
    protected void setUp() throws Exception {
        super.setUp();
        initData();
    }

    @Test
    public void testGetDatasets() throws DSOutOfServiceException, DSAccessException {
        SecurityContext ctx = new SecurityContext(group.getId());

        // get datasets of the group
        Collection<DatasetData> result = browseFacility.getDatasets(ctx);
        Assert.assertEquals(result.size(), 2);

        // get specific dataset
        Collection<Long> ids = new ArrayList<Long>(1);
        ids.add(ds.getId());
        result = browseFacility.getDatasets(ctx, ids);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.iterator().next().getId(), ds.getId());

        // get specific dataset for user
        result = browseFacility.getDatasets(ctx, user.getId(), ids);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.iterator().next().getId(), ds.getId());

        // Get datasets for user2
        result = browseFacility.getDatasets(ctx, user2.getId());
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.iterator().next().getId(), ds2.getId());

        // Get datasets for user3 - has none
        result = browseFacility.getDatasets(ctx, user3.getId());
        Assert.assertEquals(result.size(), 0);
    }

    @Test
    public void testGetProjects() throws DSOutOfServiceException, DSAccessException {
        SecurityContext ctx = new SecurityContext(group.getId());

        // get projects of the group
        Collection<ProjectData> result = browseFacility.getProjects(ctx);
        Assert.assertEquals(result.size(), 2);

        // check that we do *not* load the whole tree by default
        for(ProjectData p : result) {
            Set<DatasetData> datasets = p.getDatasets();
            for(DatasetData ds : datasets) {
                Assert.assertNull(ds.getImages(), "Images should not have been loaded at this point!");
            }
        }
        
        // get specific project
        Collection<Long> ids = new ArrayList<Long>(1);
        ids.add(proj.getId());
        result = browseFacility.getProjects(ctx, ids);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.iterator().next().getId(), proj.getId());

        // get specific project for user
        result = browseFacility.getProjects(ctx, user.getId(), ids);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.iterator().next().getId(), proj.getId());

        // Get projects for user2
        result = browseFacility.getProjects(ctx, user2.getId());
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.iterator().next().getId(), proj2.getId());

        // Get projects for user3 - has none
        result = browseFacility.getProjects(ctx, user3.getId());
        Assert.assertEquals(result.size(), 0);
    }

    @Test
    public void testGetScreens() throws DSOutOfServiceException, DSAccessException {
        SecurityContext ctx = new SecurityContext(group.getId());

        // get screens of the group
        Collection<ScreenData> result = browseFacility.getScreens(ctx);
        Assert.assertEquals(result.size(), 2);

        // get specific screen
        Collection<Long> ids = new ArrayList<Long>(1);
        ids.add(screen.getId());
        result = browseFacility.getScreens(ctx, ids);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.iterator().next().getId(), screen.getId());

        // get specific screen for user
        result = browseFacility.getScreens(ctx, user.getId(), ids);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.iterator().next().getId(), screen.getId());

        // Get screens for user2
        result = browseFacility.getScreens(ctx, user2.getId());
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.iterator().next().getId(), screen2.getId());

        // Get screens for user3 - has none
        result = browseFacility.getScreens(ctx, user3.getId());
        Assert.assertEquals(result.size(), 0);
    }

    @Test
    public void testGetPlates() throws DSOutOfServiceException, DSAccessException {
        SecurityContext ctx = new SecurityContext(group.getId());

        // get plates of the group
        Collection<PlateData> result = browseFacility.getPlates(ctx);
        Assert.assertEquals(result.size(), 2);

        // get specific plate
        Collection<Long> ids = new ArrayList<Long>(1);
        ids.add(plate.getId());
        result = browseFacility.getPlates(ctx, ids);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.iterator().next().getId(), plate.getId());

        // get specific plate for user
        result = browseFacility.getPlates(ctx, user.getId(), ids);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.iterator().next().getId(), plate.getId());

        // Get plates for user2
        result = browseFacility.getPlates(ctx, user2.getId());
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.iterator().next().getId(), plate2.getId());

        // Get plates for user3 - has none
        result = browseFacility.getPlates(ctx, user3.getId());
        Assert.assertEquals(result.size(), 0);
    }

    @Test
    public void testGetOrphanedImages() throws DSOutOfServiceException, DSAccessException {
        SecurityContext ctx = new SecurityContext(group.getId());

        // get orphaned images for user
        Collection<ImageData> result = browseFacility.getOrphanedImages(ctx, user.getId());
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.iterator().next().getId(), img0.getId());
    }
    
    @Test
    public void testGetImages() throws DSOutOfServiceException, DSAccessException {
        SecurityContext ctx = new SecurityContext(group.getId());

        // get images of the root user
        Collection<ImageData> result = browseFacility.getUserImages(ctx);
        Assert.assertEquals(result.size(), 3);
        
        // get specific image
        Collection<Long> ids = new ArrayList<Long>(1);
        ids.add(img1.getId());
        result = browseFacility.getImages(ctx, ids);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.iterator().next().getId(), img1.getId());

        // get specific image for user
        result = browseFacility.getImages(ctx, user.getId(), ids);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.iterator().next().getId(), img1.getId());

        // Get images for user2
        ids.clear();
        ids.add(this.img2.getId());
        result = browseFacility.getImages(ctx, user2.getId(), ids);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.iterator().next().getId(), img2.getId());
    }

    @Test
    public void testFindIObject() throws DSOutOfServiceException,
            DSAccessException {
        SecurityContext ctx = new SecurityContext(group.getId());

        // find iobject from IObject
        IObject obj = browseFacility.findIObject(ctx, ds.asIObject());
        Assert.assertEquals(obj.getId().getValue(), ds.getId());

        // find iobject by classname string and id
        obj = browseFacility.findIObject(ctx,
                PojoMapper.getModelType(ProjectData.class).getName(),
                proj.getId());
        Assert.assertEquals(obj.getId().getValue(), proj.getId());

        // find iobject by classname string and id, across all groups
        obj = browseFacility.findIObject(rootCtx,
                PojoMapper.getModelType(ImageData.class).getName(),
                img1.getId(), true);
        Assert.assertEquals(obj.getId().getValue(), img1.getId());
    }

    @Test
    public void testFindObject() throws DSOutOfServiceException,
            DSAccessException {
        SecurityContext ctx = new SecurityContext(group.getId());
        
        // find object by pojo name string and id
        ImageData i = (ImageData) browseFacility.findObject(ctx,
                "ImageData", img0.getId());
        Assert.assertEquals(i.getId(), img0.getId());

        // find object by pojo name string and id across groups
        i = (ImageData) browseFacility.findObject(rootCtx, "ImageData",
                img1.getId(), true);
        Assert.assertEquals(i.getId(), img1.getId());

        // find object by pojo class and id
        i = browseFacility
                .findObject(ctx, ImageData.class, img0.getId());
        Assert.assertEquals(i.getId(), img0.getId());

        // find object by pojo class and id across groups
        i = browseFacility.findObject(rootCtx, ImageData.class,
                img1.getId(), true);
        Assert.assertEquals(i.getId(), img1.getId());
    }
    
    @Test
    public void testFindNonPersistedObject() throws DSOutOfServiceException,
            DSAccessException {
        DatasetData nonPersistedDS = new DatasetData();
        IObject tmp = browseFacility.findIObject(rootCtx,
                nonPersistedDS.asIObject());
        Assert.assertNull(tmp);
    }

    @Test
    public void testGetFolders() throws DSOutOfServiceException,
            DSAccessException {
        SecurityContext ctx = new SecurityContext(group.getId());
        Collection<FolderData> tmp = browseFacility.getFolders(ctx);
        Assert.assertEquals(tmp.size(), 4);
        Iterator<FolderData> it = tmp.iterator();
        while (it.hasNext()) {
            FolderData f = it.next();
            if (f.getId() == userFolder1.getId()
                    || f.getId() == userFolder2.getId()
                    || f.getId() == userFolder3.getId()
                    || f.getId() == user2Folder.getId())
                it.remove();
        }
        Assert.assertTrue(tmp.isEmpty());
    }
    
    @Test
    public void testGetFoldersById() throws DSOutOfServiceException,
            DSAccessException {
        SecurityContext ctx = new SecurityContext(group.getId());

        Collection<Long> ids = new ArrayList<Long>(2);
        ids.add(userFolder1.getId());
        ids.add(userFolder2.getId());

        Collection<FolderData> tmp = browseFacility.getFolders(ctx, ids);
        Assert.assertEquals(2, tmp.size());
        Iterator<FolderData> it = tmp.iterator();
        while (it.hasNext()) {
            FolderData f = it.next();
            if (f.getId() == userFolder1.getId()
                    || f.getId() == userFolder2.getId())
                it.remove();
        }
        Assert.assertTrue(tmp.isEmpty());
    }
    
    @Test
    public void testGetFoldersByUserId() throws DSOutOfServiceException,
            DSAccessException {
        SecurityContext ctx = new SecurityContext(user2.getGroupId());
        Collection<FolderData> tmp = browseFacility.getFolders(ctx,
                user2.getId());
        Assert.assertEquals(1, tmp.size());
        FolderData f = tmp.iterator().next();
        Assert.assertEquals(user2Folder.getId(), f.getId());
    }
    
    @Test
    public void testGetLookupTables() throws DSOutOfServiceException, DSAccessException {
        Collection<String> luts = browseFacility.getLookupTables(rootCtx);
        Assert.assertFalse(luts.isEmpty());
     }
    
    @Test
    public void testGetWells() throws DSOutOfServiceException, DSAccessException {
        Collection<WellData> wells = browseFacility.getWells(rootCtx, wellIds);
        Assert.assertEquals(wells.size(), wellIds.size());
        ArrayList<Long> loadedIds = new ArrayList<Long>();
        for(WellData w : wells) {
            Assert.assertEquals(w.getWellSamples().size(), 2);
            loadedIds.add(w.getId());
        }
        Collections.sort(loadedIds);
        Assert.assertEquals(loadedIds, wellIds);
     }

     @Test
     public void testGetByName() throws Exception {
         ProjectData p = createProject(rootCtx);
         String aDatasetName = createDataset(rootCtx, p).getName();
         String aProjectName = p.getName();

         ScreenData s = createScreen(rootCtx);
         String aPlateName = createPlate(rootCtx, s).getName();
         String aScreenName = s.getName();
         String anImageName = UUID.randomUUID().toString();
         ImageData i = createImage(rootCtx, null);
         i.setName(anImageName);
         datamanagerFacility.saveAndReturnObject(rootCtx, i);

         Collection res = browseFacility.getScreens(rootCtx, aScreenName);
         Assert.assertEquals(res.size(), 1);
         Assert.assertEquals(((ScreenData)res.iterator().next()).getName(), aScreenName);

         res = browseFacility.getPlates(rootCtx, aPlateName);
         Assert.assertEquals(res.size(), 1);
         Assert.assertEquals(((PlateData)res.iterator().next()).getName(), aPlateName);

         res = browseFacility.getProjects(rootCtx, aProjectName);
         Assert.assertEquals(res.size(), 1);
         Assert.assertEquals(((ProjectData)res.iterator().next()).getName(), aProjectName);

         res = browseFacility.getDatasets(rootCtx, aDatasetName);
         Assert.assertEquals(res.size(), 1);
         Assert.assertEquals(((DatasetData)res.iterator().next()).getName(), aDatasetName);

         res = browseFacility.getImages(rootCtx, anImageName);
         Assert.assertEquals(res.size(), 1);
         Assert.assertEquals(((ImageData)res.iterator().next()).getName(), anImageName);
     }
    
    private void initData() throws Exception {
        this.group = createGroup();
        this.user = createExperimenter(group);
        this.user2 = createExperimenter(group);
        this.user3 = createExperimenter(group);

        SecurityContext ctx = new SecurityContext(group.getId());
        ctx.setExperimenter(user);
        ctx.sudo();

        this.img0 = createImage(ctx, null);
        
        this.proj = createProject(ctx);
        this.ds = createDataset(ctx, proj);
        this.screen = createScreen(ctx);
        this.plate = createPlate(ctx, screen);

        this.img1 = createImage(ctx, ds);
        this.userFolder1 = createFolder(ctx);
        this.userFolder2 = createFolder(ctx);
        this.userFolder3 = createFolder(ctx);

        ctx = new SecurityContext(group.getId());
        ctx.setExperimenter(user2);
        ctx.sudo();

        this.proj2 = createProject(ctx);
        this.ds2 = createDataset(ctx, proj2);
        this.screen2 = createScreen(ctx);
        this.plate2 = createPlate(ctx, screen2);
        this.img2 = createImage(ctx, ds2);
        
        this.user2Folder = createFolder(ctx);
        
        ModelMockFactory mf = new ModelMockFactory(gw.getTypesService(rootCtx));
        this.wellsPlate = new PlateData(mf.createPlate(3, 3, 2, 1, false));
        this.wellsPlate = (PlateData) datamanagerFacility.saveAndReturnObject(rootCtx, wellsPlate);
        
        this.wellIds = new ArrayList<Long>();
        Plate p = this.wellsPlate.asPlate();
        for (Well w : p.copyWells()) {
            this.wellIds.add(w.getId().getValue());
        }
        Collections.sort(this.wellIds);
    }

}
