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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ode.api.IPixelsPrx;
import ode.gateway.SecurityContext;
import ode.gateway.exception.DSAccessException;
import ode.gateway.exception.DSOutOfServiceException;
import ode.gateway.facility.BrowseFacility;
import ode.gateway.facility.ROIFacility;
import ode.gateway.model.ROIResult;
import ode.model.Folder;
import ode.model.IObject;
import ode.model.PixelsType;
import ode.model.Roi;
import ode.model.Shape;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ode.gateway.model.FolderData;
import ode.gateway.model.ImageData;
import ode.gateway.model.ROIData;
import ode.gateway.model.RectangleData;
import ode.gateway.model.ShapeData;

public class ROIFacilityTest extends GatewayTest {

    private ImageData img;
    private ROIFacility roifac;
    private BrowseFacility browse;
    private Collection<ROIData> rois;
    
    private ImageData folderImg;
    private FolderData folder;
    private Collection<ROIData> folderRois;

    @Override
    @BeforeClass(alwaysRun = true)
    protected void setUp() throws Exception {
        super.setUp();
        roifac = gw.getFacility(ROIFacility.class);
        browse = gw.getFacility(BrowseFacility.class);
        initData();
    }

    @Test
    public void testSaveROIs() throws DSOutOfServiceException,
            DSAccessException {
        rois = new ArrayList<ROIData>();
        rois.add(createRectangleROI(0, 0, 10, 10));
        rois.add(createRectangleROI(11, 11, 10, 10));
        rois.add(createRectangleROI(12, 12, 10, 10, false));
        rois.add(createRectangleROI(13, 13, 10, 10, true));
        
        Collection<ROIData> saved = roifac.saveROIs(rootCtx, img.getId(), rois);

        Assert.assertEquals(saved.size(), rois.size());
        Assert.assertEquals(getShapes(saved).size(), getShapes(rois).size());
        for (ROIData roi : saved) {
            Assert.assertTrue(roi.getId() >= 0);
        }
        
        // check that ZCT correctly saved:
        List<ShapeData> shapes = getShapes(saved);
        for (ShapeData shape : shapes) {
            if (shape.getROICoordinate().getTimePoint() == -1)
                Assert.assertNull(((Shape) shape.asIObject()).getTheT());
            else
                Assert.assertNotNull(((Shape) shape.asIObject()).getTheT());
            if (shape.getROICoordinate().getZSection() == -1)
                Assert.assertNull(((Shape) shape.asIObject()).getTheZ());
            else
                Assert.assertNotNull(((Shape) shape.asIObject()).getTheZ());
        }
        
        rois = saved;
    } 

    @Test
    public void testSaveImagelessROIs() throws DSOutOfServiceException,
            DSAccessException {
        ROIData r = createRectangleROI(11, 11, 10, 10);
        Collection<ROIData> result = roifac.saveROIs(rootCtx, -1, Collections.singleton(r));

        Assert.assertEquals(result.size(), 1);
        r = result.iterator().next();
        Assert.assertTrue(r.getId() >= 0);
        Assert.assertNull(r.getImage() );
    }

    @Test(dependsOnMethods = { "testSaveROIs" })
    public void testGetROICount() throws DSOutOfServiceException,
            DSAccessException {
        int n = roifac.getROICount(rootCtx, img.getId());
        Assert.assertEquals(rois.size(), n);
    }
    
    @Test(dependsOnMethods = { "testSaveROIs" })
    public void testLoadROIs() throws DSOutOfServiceException,
            DSAccessException {
        List<ROIResult> roiResults = roifac.loadROIs(rootCtx, img.getId());
        List<ROIData> myRois = new ArrayList<ROIData>();
        for (ROIResult r : roiResults) {
            myRois.addAll(r.getROIs());
        }

        Assert.assertEquals(myRois.size(), rois.size());

        compare(myRois, rois);
    }

    @Test
    public void testGetROIFolders() throws DSOutOfServiceException,
            DSAccessException {
        Collection<FolderData> folders = roifac.getROIFolders(rootCtx,
                folderImg.getId());
        Assert.assertEquals(folders.size(), 1);
        Assert.assertEquals(folders.iterator().next().getId(), folder.getId());
    }

    @Test(dependsOnMethods = { "testGetROIFolders" })
    public void testLoadRoisForFolder() throws DSOutOfServiceException,
            DSAccessException {
        Collection<FolderData> folders = roifac.getROIFolders(rootCtx,
                folderImg.getId());
        FolderData folder = folders.iterator().next();

        Collection<ROIResult> roiResults = roifac.loadROIsForFolder(rootCtx,
                folderImg.getId(), folder.getId());

        Collection<ROIData> rois = new ArrayList<ROIData>();
        for (ROIResult r : roiResults)
            for (ROIData rd : r.getROIs())
                rois.add(rd);
        Assert.assertEquals(rois.size(), 2);

        Set<Long> folderRoiIds = new HashSet<Long>();
        for(ROIData d : folderRois)
            folderRoiIds.add(d.getId());
        
        Iterator<ROIData> it = rois.iterator();
        while (it.hasNext()) {
            ROIData r = it.next();
            if (folderRoiIds.contains(r.getId()))
                it.remove();
        }
        Assert.assertTrue(rois.isEmpty());
    }
    
    @Test(dependsOnMethods = { "testRemoveROIsFromFolder" })
    public void testAddROIsToFolder() throws Exception {
        folder = browse
                .loadFolders(rootCtx, Collections.singletonList(folder.getId()))
                .iterator().next();
        Assert.assertEquals(folder.roiCount(), 0);
        
        roifac.addRoisToFolders(rootCtx, folderImg.getId(), folderRois,
                Collections.singletonList(folder));

        folder = browse
                .loadFolders(rootCtx, Collections.singletonList(folder.getId()))
                .iterator().next();
        Assert.assertEquals(folder.roiCount(), 2);

        List<ROIResult> rrs = roifac.loadROIs(rootCtx, folderImg.getId());
        for (ROIResult rr : rrs) {
            for (ROIData r : rr.getROIs()) {
                Assert.assertEquals(r.getFolders().size(), 1);
            }
        }

    }

    @Test(dependsOnMethods = { "testLoadRoisForFolder"})
    public void testRemoveROIsFromFolder() throws Exception {
        folder = browse
                .loadFolders(rootCtx, Collections.singletonList(folder.getId()))
                .iterator().next();
        Assert.assertEquals(folder.roiCount(), 2);
        
        roifac.removeRoisFromFolders(rootCtx, folderImg.getId(), folderRois,
                Collections.singletonList(folder));

        folder = browse
                .loadFolders(rootCtx, Collections.singletonList(folder.getId()))
                .iterator().next();
        Assert.assertEquals(folder.roiCount(), 0);

        List<ROIResult> rrs = roifac.loadROIs(rootCtx, folderImg.getId());
        for (ROIResult rr : rrs) {
            for (ROIData r : rr.getROIs()) {
                Assert.assertTrue(r.getFolders().isEmpty());
            }
        }
    }
    
    private void initData() throws Exception {
        String name = UUID.randomUUID().toString();
        IPixelsPrx svc = gw.getPixelsService(rootCtx);
        List<IObject> types = gw.getTypesService(rootCtx)
                .allEnumerations(PixelsType.class.getName());
        List<Integer> channels = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++) {
            channels.add(i);
        }
        long imgId = svc.createImage(100, 100, 1, 1, channels,
                (PixelsType) types.get(1), name, "").getValue();

        img = gw.getFacility(BrowseFacility.class).getImage(rootCtx, imgId);
        
        long folderImgId = svc.createImage(100, 100, 1, 1, channels,
                (PixelsType) types.get(1), name, "").getValue();
        folderImg = gw.getFacility(BrowseFacility.class).getImage(rootCtx,
                folderImgId);
        ROIData folderRoi1 = createRectangleROI(5, 5, 10, 10);
        ROIData folderRoi2 = createRectangleROI(10, 10, 10, 10);
        folderRois = new ArrayList<ROIData>(2);
        folderRois.add(folderRoi1);
        folderRois.add(folderRoi2);
        folderRois = roifac.saveROIs(rootCtx, folderImg.getId(), folderRois);
        folder = createRoiFolder(rootCtx, folderRois);
    }
    
    private ROIData createRectangleROI(int x, int y, int w, int h) {
        return createRectangleROI(x, y, w, h, null);
    }

    private ROIData createRectangleROI(int x, int y, int w, int h, Boolean plane) {
        ROIData roiData = new ROIData();
        RectangleData rectangle = new RectangleData(x, y, w, h);
        if (plane != null) {
            if (plane) {
                rectangle.setC(1);
                rectangle.setZ(1);
                rectangle.setT(1);
            }
            else {
                rectangle.setC(-1);
                rectangle.setZ(-1);
                rectangle.setT(-1);
            }
        }
        roiData.addShapeData(rectangle);
        return roiData;
    }
    
    private FolderData createRoiFolder(SecurityContext ctx,
            Collection<ROIData> rois) throws DSOutOfServiceException,
            DSAccessException {
        FolderData folder = new FolderData();
        folder.setName(UUID.randomUUID().toString());
        Folder f = folder.asFolder();
        for (ROIData roi : rois)
            f.linkRoi((Roi) roi.asIObject());
        return (FolderData) datamanagerFacility
                .saveAndReturnObject(ctx, folder);
    }
    
    private void compare(Collection<ROIData> list1, Collection<ROIData> list2) {
        List<ShapeData> shapes1 = getShapes(list1);
        List<ShapeData> shapes2 = getShapes(list2);
        
        Assert.assertEquals(shapes1.size(), shapes2.size());
        
        Iterator<ShapeData> it1 = shapes1.iterator();
        while(it1.hasNext()) {
            ShapeData s1 = it1.next();
            
            Iterator<ShapeData> it2 = shapes2.iterator();
            while(it2.hasNext()) {
                ShapeData s2 = it2.next();
                if(s1.getId() == s2.getId()) {
                    Assert.assertEquals(s1.getROICoordinate(), s2.getROICoordinate());
                    it2.remove();
                    break;
                }
            }
        }
        
        Assert.assertTrue(shapes2.isEmpty());
    }
    
    private List<ShapeData> getShapes(Collection<ROIData> rois) {
        List<ShapeData> res = new ArrayList<ShapeData>();
        for(ROIData r : rois) {
            Iterator<List<ShapeData>> sit = r.getIterator();
            while(sit.hasNext()) 
                res.addAll(sit.next());
        }
        return res;
    }
}
