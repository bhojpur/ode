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

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import ode.util.LSID;
import ode.formats.Index;
import ode.formats.ODEMetadataStoreClient;
import ode.formats.model.ServerInstanceProvider;
import ode.xml.model.primitives.PositiveInteger;
import ode.model.Image;
import ode.model.ObjectiveSettings;
import ode.model.Pixels;
import ode.api.ServiceFactoryPrx;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class IObjectContainerStoreTest
{
	private ODEMetadataStoreClient store;

	private static final int IMAGE_INDEX = 0;

	@BeforeMethod
	protected void setUp() throws Exception
	{
		ServiceFactoryPrx sf = new TestServiceFactory().proxy();
        store = new ODEMetadataStoreClient();
        store.initialize(sf);
        store.setReader(new TestReader());
        store.setEnumerationProvider(new TestEnumerationProvider());
        store.setInstanceProvider(
			new ServerInstanceProvider(store.getEnumerationProvider()));

        // Two objects of the same type
        store.setImageName("Foo1", IMAGE_INDEX);
        store.setImageName("Foo2", IMAGE_INDEX + 1);

        // Objects of a different type
        store.setPixelsSizeX(new PositiveInteger(1), IMAGE_INDEX);
        store.setPixelsSizeX(new PositiveInteger(1), IMAGE_INDEX + 1);

        // Add a reference
        store.setObjectiveSettingsID("Objective:0", IMAGE_INDEX);
	}

	@AfterMethod
	protected void tearDown()
			throws IOException {
		store.logout();
	}

	@Test
	public void testGetCaches()
	{
		Assert.assertNotNull(store.getContainerCache());
		Assert.assertNotNull(store.getReferenceCache());
		Assert.assertNull(store.getReferenceStringCache());
	}

	@Test
	public void testSetReferenceStringCache()
	{
		Map<String, String[]> a = new HashMap<String, String[]>();
		store.setReferenceStringCache(a);
		Assert.assertEquals(a, store.getReferenceStringCache());
	}

	@Test
	public void testGetSourceObject()
	{
	    Assert.assertNotNull(store.getSourceObject(new LSID(Image.class, 0)));
	}

	@Test
	public void testGetSourceObjects()
	{
	    Assert.assertEquals(store.getSourceObjects(Image.class).size(), 2);
	}

	@Test
	public void testGetIObjectContainer()
	{
		LinkedHashMap<Index, Integer> indexes =
			new LinkedHashMap<Index, Integer>();
		indexes.put(Index.IMAGE_INDEX, IMAGE_INDEX + 2);
		store.getIObjectContainer(Image.class, indexes);
		Assert.assertEquals(store.countCachedContainers(Image.class), 3);
	}

	@Test
	public void testCachedContainers()
	{
	    Assert.assertEquals(2, store.countCachedContainers(Image.class), 2);
	    Assert.assertEquals(store.countCachedContainers(Pixels.class), 2);
	    Assert.assertEquals(store.countCachedContainers(
				Pixels.class, IMAGE_INDEX), 1);
	    Assert.assertEquals(store.countCachedContainers(
				Pixels.class, IMAGE_INDEX + 1), 1);
	}

	@Test
	public void testHasReference()
	{
	    Assert.assertTrue(store.hasReference(new LSID(ObjectiveSettings.class,
				                      IMAGE_INDEX), new LSID("Objective:0")));
	}

	@Test
	public void testCount10000CachedContainers()
	{
		for (int i = 0; i < 10000; i++)
		{
			store.setImageName(String.valueOf(i), i);
		}
		long t0 = System.currentTimeMillis();
		store.countCachedContainers(Image.class, null);
		Assert.assertTrue((System.currentTimeMillis() - t0) < 100);
	}

	@Test
	public void testGet10000ContainersByClass()
	{
		for (int i = 0; i < 10000; i++)
		{
			store.setImageName(String.valueOf(i), i);
		}
		long t0 = System.currentTimeMillis();
		store.getIObjectContainers(Image.class);
		Assert.assertTrue((System.currentTimeMillis() - t0) < 100);
	}
}