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
import ode.formats.model.UnitsFactory;
import ode.units.quantity.Power;
import ode.util.LSID;
import ode.xml.model.enums.LaserMedium;
import ode.xml.model.enums.LaserType;
import ode.xml.model.primitives.PercentFraction;
import ode.xml.model.primitives.PositiveInteger;
import ode.api.ServiceFactoryPrx;
import ode.model.Laser;
import ode.model.LightSettings;
import ode.model.Pixels;
import ode.model.PowerI;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class LightSourceSettingsLaserTest
{
	private ODEWrapper wrapper;

	private ODEMetadataStoreClient store;

	private static final int LIGHTSOURCE_INDEX = 0;

	private static final int INSTRUMENT_INDEX = 0;

	private static final int IMAGE_INDEX = 0;

	private static final int CHANNEL_INDEX = 0;

	Power watt(double d) {
	    return UnitsFactory.convertPower(new PowerI(d, UnitsFactory.LightSource_Power));
	}

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

        // First Laser, First LightSourceSettings
		store.setLaserModel("Model", INSTRUMENT_INDEX, LIGHTSOURCE_INDEX);
		store.setLaserID("Laser:0", INSTRUMENT_INDEX, LIGHTSOURCE_INDEX);
		store.setLaserPower(watt(1.0), INSTRUMENT_INDEX, LIGHTSOURCE_INDEX);
		store.setLaserFrequencyMultiplication(
				new PositiveInteger(1), INSTRUMENT_INDEX, LIGHTSOURCE_INDEX);
		store.setChannelLightSourceSettingsID(
				"Laser:0", IMAGE_INDEX, CHANNEL_INDEX);
		store.setChannelLightSourceSettingsAttenuation(
				new PercentFraction(1f), IMAGE_INDEX, CHANNEL_INDEX);

		// Second Laser, Second LightSourceSettings
		store.setLaserModel("Model", INSTRUMENT_INDEX, LIGHTSOURCE_INDEX + 1);
		store.setLaserID("Laser:1", INSTRUMENT_INDEX, LIGHTSOURCE_INDEX + 1);
		store.setLaserPower(watt(1.0), INSTRUMENT_INDEX, LIGHTSOURCE_INDEX + 1);
		store.setLaserFrequencyMultiplication(
				new PositiveInteger(1), INSTRUMENT_INDEX, LIGHTSOURCE_INDEX + 1);
		store.setChannelLightSourceSettingsID(
				"Laser:1", IMAGE_INDEX, CHANNEL_INDEX + 1);
		store.setChannelLightSourceSettingsAttenuation(
			  new PercentFraction(1f), IMAGE_INDEX, CHANNEL_INDEX + 1);

		// Third Laser, Third LightSourceSettings (different orientation)
		store.setLaserLaserMedium(
				LaserMedium.AR, INSTRUMENT_INDEX, LIGHTSOURCE_INDEX + 2);
		store.setLaserType(LaserType.GAS, INSTRUMENT_INDEX, LIGHTSOURCE_INDEX + 2);
		store.setLaserID("Laser:2", INSTRUMENT_INDEX, LIGHTSOURCE_INDEX + 2);
		store.setChannelLightSourceSettingsID(
				"Laser:2", IMAGE_INDEX, CHANNEL_INDEX + 2);
		store.setChannelLightSourceSettingsAttenuation(
				new PercentFraction(1f), IMAGE_INDEX, CHANNEL_INDEX + 2);
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
        Assert.assertEquals(store.countCachedContainers(Laser.class), 3);
        Assert.assertEquals(store.countCachedContainers(null), 7);
	}

	@Test
	public void testLightSourceSettingsCount()
	{
        LSID lsid = new LSID(Pixels.class, IMAGE_INDEX);
        Assert.assertNotNull(store.getSourceObject(lsid));
        Assert.assertEquals(store.countCachedContainers(Laser.class), 3);
        Assert.assertEquals(store.countCachedContainers(null), 7);
	}

	@Test
	public void testReferences()
	{
        for (int i = 0; i < 3; i++)
        {
            LSID imageLsid = new LSID(LightSettings.class, IMAGE_INDEX, i);
            Assert.assertTrue(store.hasReference(imageLsid, new LSID("Laser:" + i)));
        }
        Assert.assertEquals(store.countCachedReferences(null, null), 3);
	}
}