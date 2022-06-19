package ode.formats.utests;

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

import ode.formats.ODEMetadataStoreClient;
import ode.formats.model.ServerInstanceProvider;
import ode.units.UNITS;
import ode.units.quantity.Time;
import ode.util.LSID;
import ode.xml.model.primitives.NonNegativeInteger;
import ode.api.ServiceFactoryPrx;
import ode.model.PlaneInfo;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class PlaneInfoProcessorTest
{
    private ODEMetadataStoreClient store;

    private static final int IMAGE_INDEX = 1;

    private static final int PLANE_INFO_INDEX = 1;

    @BeforeMethod
    protected void setUp() throws Exception
    {
        Time onesec = new Time(1, UNITS.SECOND);
        ServiceFactoryPrx sf = new TestServiceFactory().proxy();
        store = new ODEMetadataStoreClient();
        store.initialize(sf);
        store.setEnumerationProvider(new TestEnumerationProvider());
        store.setInstanceProvider(
                new ServerInstanceProvider(store.getEnumerationProvider()));
        store.setPlaneTheC(new NonNegativeInteger(0), IMAGE_INDEX, PLANE_INFO_INDEX);
        store.setPlaneTheZ(new NonNegativeInteger(0), IMAGE_INDEX, PLANE_INFO_INDEX);
        store.setPlaneTheT(new NonNegativeInteger(0), IMAGE_INDEX, PLANE_INFO_INDEX);
        store.setPlaneTheC(new NonNegativeInteger(1), IMAGE_INDEX, PLANE_INFO_INDEX + 1);
        store.setPlaneTheZ(new NonNegativeInteger(1), IMAGE_INDEX, PLANE_INFO_INDEX + 1);
        store.setPlaneTheT(new NonNegativeInteger(1), IMAGE_INDEX, PLANE_INFO_INDEX + 1);
        store.setPlaneTheC(new NonNegativeInteger(2), IMAGE_INDEX, PLANE_INFO_INDEX + 2);
        store.setPlaneTheZ(new NonNegativeInteger(2), IMAGE_INDEX, PLANE_INFO_INDEX + 2);
        store.setPlaneTheT(new NonNegativeInteger(2), IMAGE_INDEX, PLANE_INFO_INDEX + 2);
        store.setPlaneDeltaT(onesec, IMAGE_INDEX, PLANE_INFO_INDEX +2);
    }

    @AfterMethod
    protected void tearDown()
            throws IOException {
        store.logout();
    }

    @Test
    public void testPlaneInfoExists()
    {
        Assert.assertEquals(store.countCachedContainers(PlaneInfo.class, null), 3);
        LSID planeInfoLSID1 = new LSID(PlaneInfo.class, IMAGE_INDEX, PLANE_INFO_INDEX);
        LSID planeInfoLSID2 = new LSID(PlaneInfo.class, IMAGE_INDEX, PLANE_INFO_INDEX + 1);
        LSID planeInfoLSID3 = new LSID(PlaneInfo.class, IMAGE_INDEX, PLANE_INFO_INDEX + 2);
        PlaneInfo pi1 = (PlaneInfo) store.getSourceObject(planeInfoLSID1);
        PlaneInfo pi2 = (PlaneInfo) store.getSourceObject(planeInfoLSID2);
        PlaneInfo pi3 = (PlaneInfo) store.getSourceObject(planeInfoLSID3);
        Assert.assertNotNull(pi1);
        Assert.assertNotNull(pi2);
        Assert.assertNotNull(pi3);
        Assert.assertEquals(pi1.getTheC().getValue(), 0);
        Assert.assertEquals(pi1.getTheZ().getValue(), 0);
        Assert.assertEquals(pi1.getTheT().getValue(), 0);
        Assert.assertEquals(pi2.getTheC().getValue(), 1);
        Assert.assertEquals(pi2.getTheZ().getValue(), 1);
        Assert.assertEquals(pi2.getTheT().getValue(), 1);
        Assert.assertEquals(pi3.getTheC().getValue(), 2);
        Assert.assertEquals(pi3.getTheZ().getValue(), 2);
        Assert.assertEquals(pi3.getTheT().getValue(), 2);
        Assert.assertEquals(pi3.getDeltaT().getValue(), 1.0);
    }

    @Test
    public void testPlaneInfoCleanup()
    {
        store.postProcess();
        Assert.assertEquals(1, store.countCachedContainers(PlaneInfo.class, null));
        LSID planeInfoLSID = new LSID(PlaneInfo.class, IMAGE_INDEX, PLANE_INFO_INDEX + 2);
        PlaneInfo pi = (PlaneInfo) store.getSourceObject(planeInfoLSID);
        Assert.assertNotNull(pi);
        Assert.assertEquals(pi.getTheC().getValue(), 2);
        Assert.assertEquals(pi.getTheZ().getValue(), 2);
        Assert.assertEquals(pi.getTheT().getValue(), 2);
        Assert.assertEquals(pi.getDeltaT().getValue(), 1.0);
    }
}