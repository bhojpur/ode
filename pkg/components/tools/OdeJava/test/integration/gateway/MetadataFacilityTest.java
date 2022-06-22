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
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import ode.model.units.BigResult;
import ode.api.IPixelsPrx;
import ode.gateway.exception.DSAccessException;
import ode.gateway.exception.DSOutOfServiceException;
import ode.gateway.facility.BrowseFacility;
import ode.gateway.facility.DataManagerFacility;
import ode.gateway.facility.MetadataFacility;
import ode.model.IObject;
import ode.model.ImagingEnvironment;
import ode.model.ImagingEnvironmentI;
import ode.model.PixelsType;
import ode.model.Temperature;
import ode.model.TemperatureI;
import ode.model.enums.UnitsTemperature;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ode.gateway.model.AnnotationData;
import ode.gateway.model.ChannelData;
import ode.gateway.model.DataObject;
import ode.gateway.model.ImageAcquisitionData;
import ode.gateway.model.ImageData;
import ode.gateway.model.ProjectData;
import ode.gateway.model.TagAnnotationData;
import ode.gateway.model.TextualAnnotationData;

public class MetadataFacilityTest extends GatewayTest {

    private ImageData img;
    private Temperature temp;
    private TagAnnotationData tag;
    private TextualAnnotationData comment;
    
    @Override
    @BeforeClass(alwaysRun = true)
    protected void setUp() throws Exception {
        super.setUp();
        initData();
    }

    @Test
    public void testGetImageAcquisitionData() throws ExecutionException,
            BigResult, DSOutOfServiceException, DSAccessException {
        MetadataFacility mdf = gw.getFacility(MetadataFacility.class);
        ImageAcquisitionData d = mdf.getImageAcquisitionData(rootCtx,
                img.getId());
        Assert.assertEquals(d.getTemperature(UnitsTemperature.CELSIUS), temp);
    }

    @Test
    public void testgetChannelData() throws ExecutionException,
            DSOutOfServiceException, DSAccessException {
        MetadataFacility mdf = gw.getFacility(MetadataFacility.class);
        List<ChannelData> channels = mdf.getChannelData(rootCtx, img.getId());
        Assert.assertEquals(channels.size(), 3);
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

        temp = new TemperatureI(20, UnitsTemperature.CELSIUS);
        ImagingEnvironment env = new ImagingEnvironmentI();
        env.setTemperature(temp);
        img.asImage().setImagingEnvironment(env);

        img = (ImageData) gw.getFacility(DataManagerFacility.class).saveAndReturnObject(rootCtx,
                img);
        
        DataManagerFacility dm = gw.getFacility(DataManagerFacility.class);
        tag = dm.attachAnnotation(rootCtx, new TagAnnotationData("tag1", "test"), img);
        comment = dm.attachAnnotation(rootCtx, new TextualAnnotationData("bla bla"), img);
    }
    
    @Test
    public void testGetAnnotations() throws ExecutionException,
            DSOutOfServiceException, DSAccessException {
        MetadataFacility mdf = gw.getFacility(MetadataFacility.class);
        List<AnnotationData> annos = mdf.getAnnotations(rootCtx, img);
        Assert.assertEquals(annos.size(), 2);
        int found = 0;
        for (AnnotationData anno : annos) {
            if (anno instanceof TagAnnotationData
                    && anno.getId() == tag.getId())
                found++;
            if (anno instanceof TextualAnnotationData
                    && anno.getId() == comment.getId())
                found++;
        }
        Assert.assertEquals(found, 2);
    }
    
    @Test
    public void testGetSpecificAnnotations() throws ExecutionException,
            DSOutOfServiceException, DSAccessException {
        final MetadataFacility mdf = gw.getFacility(MetadataFacility.class);
        final List<DataObject> objs = new ArrayList<DataObject>();
        objs.add(img);

        final List<Class<? extends AnnotationData>> types = new ArrayList<Class<? extends AnnotationData>>();
        types.add(TagAnnotationData.class);

        Map<DataObject, List<AnnotationData>> annoMap = mdf.getAnnotations(
                rootCtx, objs, types, null);
        Assert.assertEquals(1, annoMap.size());

        List<AnnotationData> annos = annoMap.get(img);
        Assert.assertEquals(annos.size(), 1);
        Assert.assertEquals(tag.getId(), annos.get(0).getId());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetSpecificAnnotationsException()
            throws ExecutionException, DSOutOfServiceException,
            DSAccessException {
        final MetadataFacility mdf = gw.getFacility(MetadataFacility.class);

        // Test if exception is thrown when types are mixed.
        final List<DataObject> objs = new ArrayList<DataObject>();
        objs.add(img);
        objs.add(new ProjectData());

        final List<Class<? extends AnnotationData>> types = new ArrayList<Class<? extends AnnotationData>>();
        types.add(TagAnnotationData.class);

        mdf.getAnnotations(rootCtx, objs, types, null);
    }
}
