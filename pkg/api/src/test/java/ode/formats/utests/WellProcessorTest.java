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
import ode.xml.model.primitives.NonNegativeInteger;
import ode.api.ServiceFactoryPrx;
import ode.model.Plate;
import ode.model.Well;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class WellProcessorTest
{
	private ODEMetadataStoreClient store;

	private static final int PLATE_INDEX = 1;

	private static final int WELL_INDEX = 1;

	@BeforeMethod
	protected void setUp() throws Exception
	{
		ServiceFactoryPrx sf = new TestServiceFactory().proxy();
        store = new ODEMetadataStoreClient();
        store.initialize(sf);
        store.setEnumerationProvider(new TestEnumerationProvider());
        store.setInstanceProvider(
			new ServerInstanceProvider(store.getEnumerationProvider()));
        store.setWellColumn(new NonNegativeInteger(0), PLATE_INDEX, WELL_INDEX);
        store.setWellColumn(new NonNegativeInteger(1), PLATE_INDEX, WELL_INDEX + 1);
        store.setWellColumn(new NonNegativeInteger(0), PLATE_INDEX + 1, WELL_INDEX);
        store.setPlateName("setUp Plate", PLATE_INDEX + 1);
	}

	@AfterMethod
	protected void tearDown()
			throws IOException {
		store.logout();
	}

	@Test
	public void testWellExists()
	{
		Assert.assertEquals(store.countCachedContainers(Well.class, null), 3);
		Assert.assertEquals(store.countCachedContainers(Plate.class, null), 1);
		LSID wellLSID1 = new LSID(Well.class, PLATE_INDEX, WELL_INDEX);
		LSID wellLSID2 = new LSID(Well.class, PLATE_INDEX, WELL_INDEX + 1);
		LSID wellLSID3 = new LSID(Well.class, PLATE_INDEX + 1, WELL_INDEX);
		LSID plateLSID1 = new LSID(Plate.class, PLATE_INDEX + 1);
		Well well1 = (Well) store.getSourceObject(wellLSID1);
		Well well2 = (Well) store.getSourceObject(wellLSID2);
		Well well3 = (Well) store.getSourceObject(wellLSID3);
		Plate plate1 = (Plate) store.getSourceObject(plateLSID1);
		Assert.assertNotNull(well1);
		Assert.assertNotNull(well2);
		Assert.assertNotNull(well3);
		Assert.assertNotNull(plate1);
		Assert.assertEquals(well1.getColumn().getValue(), 0);
		Assert.assertEquals(well2.getColumn().getValue(), 1);
		Assert.assertEquals(well3.getColumn().getValue(), 0);
		Assert.assertEquals(plate1.getName().getValue(), "setUp Plate");
	}

	@Test
	public void testWellPostProcess()
	{
		store.postProcess();
		Assert.assertEquals(store.countCachedContainers(Well.class, null), 3);
		Assert.assertEquals(store.countCachedContainers(Plate.class, null), 2);
		LSID plateLSID1 = new LSID(Plate.class, PLATE_INDEX);
		LSID plateLSID2 = new LSID(Plate.class, PLATE_INDEX + 1);
		Plate plate1 = (Plate) store.getSourceObject(plateLSID1);
		Plate plate2 = (Plate) store.getSourceObject(plateLSID2);
		Assert.assertNotNull(plate1);
		Assert.assertNotNull(plate2);
		Assert.assertEquals(plate1.getName().getValue(), "Plate");
		Assert.assertEquals(plate2.getName().getValue(), "setUp Plate");
	}
}