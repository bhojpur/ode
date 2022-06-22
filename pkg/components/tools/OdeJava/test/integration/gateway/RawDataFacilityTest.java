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
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import ode.api.IPixelsPrx;
import ode.api.RawPixelsStorePrx;
import ode.gateway.exception.DSAccessException;
import ode.gateway.exception.DSOutOfServiceException;
import ode.gateway.exception.DataSourceException;
import ode.gateway.rnd.Plane2D;
import ode.model.IObject;
import ode.model.PixelsType;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ode.gateway.model.DatasetData;
import ode.gateway.model.ImageData;
import ode.gateway.model.ProjectData;

public class RawDataFacilityTest extends GatewayTest {

    private long imgId;
    private byte[] rawData;
    
    @Override
    @BeforeClass(alwaysRun = true)
    protected void setUp() throws Exception {
        super.setUp();
        initData();
    }

    @Test
    public void testGetPlane() throws DataSourceException, DSOutOfServiceException, DSAccessException {
        ImageData img = browseFacility.getImage(rootCtx, imgId);
        Plane2D plane = rawdataFacility.getPlane(rootCtx, img.getDefaultPixels(), 0, 0, 0);
        byte[] planeData = new byte[100*100];
        for(int i=0; i<10000; i++)
            planeData[i] = plane.getRawValue(i);
        
        Assert.assertEquals(planeData, rawData);
    }
    
    @Test
    public void testGetPixelValues() throws DataSourceException,
            DSOutOfServiceException, DSAccessException {
        ImageData img = browseFacility.getImage(rootCtx, imgId);
        Plane2D plane = rawdataFacility.getPlane(rootCtx,
                img.getDefaultPixels(), 0, 0, 0);
        double[][] pixelData = new double[100][100];
        double[][] expPixelData = new double[100][100];
        for (int i = 0; i < 10000; i++) {
            int x = i % 100;
            int y = i / 100;
            pixelData[x][y] = plane.getPixelValue(x, y);
            expPixelData[x][y] = (double) Byte.toUnsignedInt(plane
                    .getRawValue(y * 100 + x));
        }

        Assert.assertEquals(pixelData, expPixelData);
        Assert.assertEquals(plane.getPixelValues(), expPixelData);
    }
    
    @Test
    public void testGetTile() throws DataSourceException, DSOutOfServiceException, DSAccessException {
        ImageData img = browseFacility.getImage(rootCtx, imgId);
        int x = 0, y=0, w=img.getDefaultPixels().getSizeX(), h=1;
        
        // get the first pixel row of the image as "tile"
        Plane2D plane = rawdataFacility.getTile(rootCtx, img.getDefaultPixels(), 0, 0, 0, x, y, w, h);
        byte[] planeData = new byte[w];
        for(int i=0; i<w; i++)
            planeData[i] = plane.getRawValue(i);
        
        byte[] rawDataPart = new byte[w];
        System.arraycopy(rawData, 0, rawDataPart, 0, w);
        
        Assert.assertEquals(planeData, rawDataPart);
    }
    
    @Test
    public void testGetHistogram() throws DataSourceException,
            DSOutOfServiceException, DSAccessException {
        ImageData img = browseFacility.getImage(rootCtx, imgId);

        int[] exp = new int[256];
        for (byte b : rawData) {
            int bin = ((int) b) & 0xFF;
            exp[bin]++;
        }

        Map<Integer, int[]> histo = rawdataFacility.getHistogram(rootCtx,
                img.getDefaultPixels(), new int[] { 0 }, 0, 0);
        int[] data = histo.entrySet().iterator().next().getValue();
        Assert.assertEquals(data.length, 256);

        for (int i = 0; i < 256; i++) {
            Assert.assertEquals(data[i], exp[i]);
        }
    }
    
    private void initData() throws Exception {
        ProjectData p = createProject(rootCtx);
        DatasetData d = createDataset(rootCtx, p);

        String name = UUID.randomUUID().toString();
        IPixelsPrx svc = gw.getPixelsService(rootCtx);
        List<IObject> types = gw.getTypesService(rootCtx)
                .allEnumerations(PixelsType.class.getName());
        PixelsType type = (PixelsType) types.get(2); // unit8
        List<Integer> channels = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++) {
            channels.add(i);
        }
        imgId = svc.createImage(100, 100, 1, 1, channels,
                type, name, "").getValue();

        List<Long> ids = new ArrayList<Long>(1);
        ids.add(imgId);
        ImageData img = browseFacility.getImages(rootCtx, ids).iterator()
                .next();

        List<ImageData> l = new ArrayList<ImageData>(1);
        l.add(img);
        datamanagerFacility.addImagesToDataset(rootCtx, l, d);

        ids.clear();
        ids.add(d.getId());
        d = browseFacility.getDatasets(rootCtx, ids).iterator().next();

        RawPixelsStorePrx store = gw.createPixelsStore(rootCtx);
        store.setPixelsId(img.getDefaultPixels().getId(), false);
        Random rand = new Random();
        rawData = new byte[100 * 100];
        for (int i = 0; i < rawData.length; i++) {
            int r = rand.nextInt(256);
            rawData[i] = (byte) r;
        }
        store.setPlane(rawData, 0, 0, 0);
        gw.closeService(rootCtx, store);
    }
}
