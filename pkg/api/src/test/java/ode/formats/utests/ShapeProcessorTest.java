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
import ode.util.LSID;
import ode.api.ServiceFactoryPrx;
import ode.model.Rectangle;
import ode.model.Roi;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class ShapeProcessorTest
{
	private ODEMetadataStoreClient store;

	private static final int ROI_INDEX = 1;

	private static final int SHAPE_INDEX = 1;

	@BeforeMethod
	protected void setUp() throws Exception
	{
		ServiceFactoryPrx sf = new TestServiceFactory().proxy();
        store = new ODEMetadataStoreClient();
        store.initialize(sf);
        store.setEnumerationProvider(new TestEnumerationProvider());
        store.setInstanceProvider(
			new ServerInstanceProvider(store.getEnumerationProvider()));
        store.setROIDescription("Foobar", ROI_INDEX);
        store.setRectangleX(25.0, ROI_INDEX + 1, SHAPE_INDEX);
	}

	@AfterMethod
	protected void tearDown()
			throws IOException {
		store.logout();
	}

	@Test
	public void testShapeExists()
	{
		Assert.assertEquals(store.countCachedContainers(Roi.class, null), 1);
		Assert.assertEquals(store.countCachedContainers(Rectangle.class, null), 1);
		LSID roiLSID1 = new LSID(Roi.class, ROI_INDEX);
		LSID shapeLSID1 = new LSID(Rectangle.class, ROI_INDEX + 1, SHAPE_INDEX);
		Roi roi = (Roi) store.getSourceObject(roiLSID1);
		Rectangle shape = (Rectangle) store.getSourceObject(shapeLSID1);
		Assert.assertNotNull(roi);
		Assert.assertNotNull(shape);
		Assert.assertEquals(roi.getDescription().getValue(), "Foobar");
		Assert.assertEquals(shape.getX().getValue(), 25.0);
	}

	@Test
	public void testShapePostProcess()
	{
		store.postProcess();
		Assert.assertEquals(2, store.countCachedContainers(Roi.class, null));
		Assert.assertEquals(1, store.countCachedContainers(Rectangle.class, null));
		LSID roiLSID1 = new LSID(Roi.class, ROI_INDEX);
		LSID roiLSID2 = new LSID(Roi.class, ROI_INDEX + 1);
		Roi roi1 = (Roi) store.getSourceObject(roiLSID1);
		Roi roi2 = (Roi) store.getSourceObject(roiLSID2);
		Assert.assertNotNull(roi1);
		Assert.assertNotNull(roi2);
		Assert.assertEquals(roi1.getDescription().getValue(), "Foobar");
		Assert.assertNull(roi2.getDescription());
	}
}