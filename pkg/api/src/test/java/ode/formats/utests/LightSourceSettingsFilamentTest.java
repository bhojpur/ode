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
import ode.formats.importer.ImportConfig;
import ode.formats.importer.ODEWrapper;
import ode.formats.model.ServerInstanceProvider;
import ode.util.LSID;
import ode.xml.model.enums.FilamentType;
import ode.xml.model.primitives.PercentFraction;
import ode.xml.model.primitives.PositiveInteger;
import ode.api.ServiceFactoryPrx;
import ode.model.Filament;
import ode.model.LightSettings;
import ode.model.Pixels;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class LightSourceSettingsFilamentTest
{
	private ODEWrapper wrapper;

	private ODEMetadataStoreClient store;

	private static final int LIGHTSOURCE_INDEX = 0;

	private static final int INSTRUMENT_INDEX = 0;

	private static final int IMAGE_INDEX = 0;

	private static final int CHANNEL_INDEX = 0;

	@BeforeMethod
	protected void setUp() throws Exception
	{
		ServiceFactoryPrx sf = new TestServiceFactory().proxy();
        wrapper = new ODEWrapper(new ImportConfig());
        store = new ODEMetadataStoreClient();
        store.initialize(sf);
        store.setEnumerationProvider(new TestEnumerationProvider());
        store.setInstanceProvider(
			new ServerInstanceProvider(store.getEnumerationProvider()));
        wrapper.setMetadataStore(store);

        // Need to populate at least one pixels field.
        store.setPixelsSizeX(new PositiveInteger(1), IMAGE_INDEX);

        // First Filament, First LightSourceSettings
		store.setFilamentModel("Model", INSTRUMENT_INDEX, LIGHTSOURCE_INDEX);
		store.setFilamentID("Filament:0", INSTRUMENT_INDEX, LIGHTSOURCE_INDEX);
		store.setFilamentType(
        FilamentType.OTHER, INSTRUMENT_INDEX, LIGHTSOURCE_INDEX);
		store.setChannelLightSourceSettingsID(
				"Filament:0", IMAGE_INDEX, CHANNEL_INDEX);
		store.setChannelLightSourceSettingsAttenuation(
				new PercentFraction(1f), IMAGE_INDEX, CHANNEL_INDEX);

		// Second Filament, Second LightSourceSettings
		store.setFilamentModel("Model", INSTRUMENT_INDEX, LIGHTSOURCE_INDEX + 1);
		store.setFilamentID("Filament:1", INSTRUMENT_INDEX, LIGHTSOURCE_INDEX + 1);
		store.setFilamentType(
        FilamentType.OTHER, INSTRUMENT_INDEX, LIGHTSOURCE_INDEX + 1);
		store.setChannelLightSourceSettingsID(
				"Filament:1", IMAGE_INDEX, CHANNEL_INDEX + 1);
		store.setChannelLightSourceSettingsAttenuation(
				new PercentFraction(1f), IMAGE_INDEX, CHANNEL_INDEX + 1);
	}

	@AfterMethod
	protected void tearDown()
			throws IOException {
		wrapper.close();
		store.logout();
	}

	@Test
	public void testLightSourceCount()
	{
        LSID lsid = new LSID(Pixels.class, IMAGE_INDEX);
        Assert.assertNotNull(store.getSourceObject(lsid));
        Assert.assertEquals(store.countCachedContainers(Filament.class), 2);
        Assert.assertEquals(store.countCachedContainers(null), 5);
	}

	@Test
	public void testLightSourceSettingsCount()
	{
        LSID lsid = new LSID(Pixels.class, IMAGE_INDEX);
        Assert.assertNotNull(store.getSourceObject(lsid));
        Assert.assertEquals(store.countCachedContainers(Filament.class), 2);
        Assert.assertEquals(store.countCachedContainers(null), 5);
	}

	@Test
	public void testReferences()
	{
        for (int i = 0; i < 2; i++)
        {
            LSID imageLsid = new LSID(LightSettings.class, IMAGE_INDEX, i);
            Assert.assertTrue(store.hasReference(imageLsid, new LSID("Filament:" + i)));
        }
        Assert.assertEquals(store.countCachedReferences(null, null), 2);
	}
}