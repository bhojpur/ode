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
import java.util.Iterator;
import java.util.List;

import ode.formats.ODEMetadataStoreClient;
import ode.formats.importer.ImportConfig;
import ode.formats.importer.ODEWrapper;
import ode.formats.model.ServerInstanceProvider;
import ode.formats.model.ChannelData;
import ode.xml.model.enums.*;
import ode.xml.model.primitives.*;
import ode.api.ServiceFactoryPrx;
import ode.model.Filament;
import ode.model.Filter;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the creation of channel objects.
 */
public class ChannelDataTest
{

	/** Reference to the wrapper. */
	private ODEWrapper wrapper;

	/** Reference to the store. */
	private ODEMetadataStoreClient store;

	/** Identifies the index of the filter set. */
	private static final int FILTER_SET_INDEX = 0;

	/** Identifies the index of the filter. */
	private static final int FILTER_INDEX = 0;

	/** Identifies the index of the light source. */
	private static final int LIGHTSOURCE_INDEX = 0;

	/** Identifies the index of the instrument. */
	private static final int INSTRUMENT_INDEX = 0;

	/** Identifies the index of the image. */
	private static final int IMAGE_INDEX = 0;

	/** Identifies the index of the channel. */
	private static final int CHANNEL_INDEX = 0;

	/** Identifies the index of the emission filter. */
    private static final int EM_FILTER_INDEX = 0;

    /** Identifies the index of the excitation filter. */
    private static final int EX_FILTER_INDEX = 0;

    /**
     * Initializes the components and populates the store.
     */
	@BeforeMethod
	protected void setUp()
		throws Exception
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
		store.setFilamentID(
				"Filament:0", INSTRUMENT_INDEX, LIGHTSOURCE_INDEX);
		store.setFilamentManufacturer("0", INSTRUMENT_INDEX,
				LIGHTSOURCE_INDEX);
		store.setFilamentType(FilamentType.OTHER, INSTRUMENT_INDEX,
				LIGHTSOURCE_INDEX);
		store.setChannelLightSourceSettingsID(
				"Filament:0", IMAGE_INDEX, CHANNEL_INDEX);
		store.setChannelLightSourceSettingsAttenuation(
				new PercentFraction(1.0f), IMAGE_INDEX, CHANNEL_INDEX);

		// Second Filament, Second LightSourceSettings
		store.setFilamentID(
				"Filament:1", INSTRUMENT_INDEX, LIGHTSOURCE_INDEX + 1);
		store.setFilamentManufacturer("1", INSTRUMENT_INDEX,
				LIGHTSOURCE_INDEX + 1);
		store.setFilamentType(FilamentType.OTHER, INSTRUMENT_INDEX,
				LIGHTSOURCE_INDEX + 1);
		store.setChannelLightSourceSettingsID(
				"Filament:1", IMAGE_INDEX, CHANNEL_INDEX + 1);
		store.setChannelLightSourceSettingsAttenuation(
				new PercentFraction(1.0f), IMAGE_INDEX, CHANNEL_INDEX + 1);

		// FilterSet
		store.setFilterSetID("FilterSet:0", INSTRUMENT_INDEX,
				FILTER_SET_INDEX);
		store.setFilterSetLotNumber("0", INSTRUMENT_INDEX,
				FILTER_SET_INDEX);
		store.setFilterSetID("FilterSet:1", INSTRUMENT_INDEX,
				FILTER_SET_INDEX + 1);
		store.setFilterSetLotNumber("1", INSTRUMENT_INDEX,
				FILTER_SET_INDEX + 1);

		// FilterSet linkages
		store.setChannelFilterSetRef("FilterSet:0", IMAGE_INDEX,
				CHANNEL_INDEX);
		store.setChannelFilterSetRef("FilterSet:1", IMAGE_INDEX,
				CHANNEL_INDEX + 1);

		// Filters
		store.setFilterID("Filter:0", INSTRUMENT_INDEX, FILTER_INDEX);
		store.setFilterLotNumber("0", INSTRUMENT_INDEX, FILTER_INDEX);
		store.setFilterID("Filter:1", INSTRUMENT_INDEX, FILTER_INDEX + 1);
		store.setFilterLotNumber("1", INSTRUMENT_INDEX, FILTER_INDEX + 1);
		store.setFilterID("Filter:2", INSTRUMENT_INDEX, FILTER_INDEX + 2);
		store.setFilterLotNumber("2", INSTRUMENT_INDEX, FILTER_INDEX + 2);
		store.setFilterID("Filter:3", INSTRUMENT_INDEX, FILTER_INDEX + 3);
		store.setFilterLotNumber("3", INSTRUMENT_INDEX, FILTER_INDEX + 3);
		store.setFilterID("Filter:4", INSTRUMENT_INDEX, FILTER_INDEX + 4);
		store.setFilterLotNumber("4", INSTRUMENT_INDEX, FILTER_INDEX + 4);
		store.setFilterID("Filter:5", INSTRUMENT_INDEX, FILTER_INDEX + 5);
		store.setFilterLotNumber("5", INSTRUMENT_INDEX, FILTER_INDEX + 5);
		store.setFilterID("Filter:6", INSTRUMENT_INDEX, FILTER_INDEX + 6);
		store.setFilterLotNumber("6", INSTRUMENT_INDEX, FILTER_INDEX + 6);
		store.setFilterID("Filter:7", INSTRUMENT_INDEX, FILTER_INDEX + 7);
		store.setFilterLotNumber("7", INSTRUMENT_INDEX, FILTER_INDEX + 7);

		// Filter linkages
		store.setFilterSetEmissionFilterRef("Filter:0", INSTRUMENT_INDEX,
				FILTER_SET_INDEX, FILTER_INDEX);
		store.setFilterSetExcitationFilterRef("Filter:1", INSTRUMENT_INDEX,
				FILTER_SET_INDEX, FILTER_INDEX);
		store.setFilterSetEmissionFilterRef("Filter:6", INSTRUMENT_INDEX,
				FILTER_SET_INDEX + 1, FILTER_INDEX);
		store.setFilterSetExcitationFilterRef("Filter:7", INSTRUMENT_INDEX,
				FILTER_SET_INDEX + 1, FILTER_INDEX);
		store.setLightPathEmissionFilterRef(
				"Filter:2", IMAGE_INDEX, CHANNEL_INDEX, EM_FILTER_INDEX);
		store.setLightPathExcitationFilterRef(
				"Filter:3", IMAGE_INDEX, CHANNEL_INDEX, EX_FILTER_INDEX);
		store.setLightPathEmissionFilterRef("Filter:4",
        IMAGE_INDEX, CHANNEL_INDEX + 1, EM_FILTER_INDEX + 1);
		store.setLightPathExcitationFilterRef("Filter:5",
        IMAGE_INDEX, CHANNEL_INDEX + 1, EX_FILTER_INDEX + 1);
	}

	@AfterMethod
	protected void tearDown()
            throws IOException {
        wrapper.close();
		store.logout();
	}

	/** Tests the creation of the first channel. */
	@Test
	public void testChannelDataChannelOne()
	{
		ChannelData data = ChannelData.fromObjectContainerStore(
				store, IMAGE_INDEX, CHANNEL_INDEX);
		Assert.assertNotNull(data);
		Assert.assertNotNull(data.getChannel());
		Assert.assertNotNull(data.getLogicalChannel());
		Assert.assertNotNull(data.getFilterSet());
		Assert.assertEquals(data.getFilterSet().getLotNumber().getValue(), "0");
		Assert.assertNotNull(data.getFilterSetEmissionFilter());
		Assert.assertEquals(data.getFilterSetEmissionFilter().getLotNumber().getValue(),
		        "0");
		Assert.assertNotNull(data.getFilterSetExcitationFilter());
		Assert.assertEquals(
				data.getFilterSetExcitationFilter().getLotNumber().getValue(), "1");
		List<Filter> filters = data.getLightPathEmissionFilters();
		Assert.assertNotNull(filters);
		Iterator<Filter> i = filters.iterator();
		Assert.assertEquals(filters.size(), 1);
		Filter f;
		while (i.hasNext()) {
			f = i.next();
			Assert.assertEquals(f.getLotNumber().getValue(), "2");
		}
		filters = data.getLightPathExcitationFilters();
		Assert.assertNotNull(filters);
		i = filters.iterator();

		Assert.assertEquals(filters.size(), 1);

		while (i.hasNext()) {
			f = i.next();
			Assert.assertEquals(f.getLotNumber().getValue(), "3");
		}
		Assert.assertNotNull(data.getLightSource());
		Assert.assertTrue(data.getLightSource() instanceof Filament);
		Assert.assertEquals(
				data.getLightSource().getManufacturer().getValue(), "0");
		Assert.assertNotNull(data.getLightSourceSettings());
		Assert.assertEquals(
				data.getLightSourceSettings().getAttenuation().getValue(), 1.0);
	}

	/** Tests the creation of the second channel. */
	@Test
	public void testChannelDataChannelTwo()
	{
		ChannelData data = ChannelData.fromObjectContainerStore(
				store, IMAGE_INDEX, CHANNEL_INDEX + 1);
		Assert.assertNotNull(data);
		Assert.assertNotNull(data.getChannel());
		Assert.assertNotNull(data.getLogicalChannel());
		Assert.assertNotNull(data.getFilterSet());
		Assert.assertEquals(data.getFilterSet().getLotNumber().getValue(), "1");
		Assert.assertNotNull(data.getFilterSetEmissionFilter());
		Assert.assertEquals(
				data.getFilterSetEmissionFilter().getLotNumber().getValue(), "6");
		Assert.assertNotNull(data.getFilterSetExcitationFilter());
		Assert.assertEquals(
				data.getFilterSetExcitationFilter().getLotNumber().getValue(), "7");
		List<Filter> filters = data.getLightPathEmissionFilters();
		Assert.assertNotNull(filters);
		Iterator<Filter> i = filters.iterator();

		Assert.assertEquals(filters.size(), 1);

		Filter f;
		while (i.hasNext()) {
			f = i.next();
			Assert.assertEquals(f.getLotNumber().getValue(), "4");
		}
		filters = data.getLightPathExcitationFilters();
		Assert.assertNotNull(filters);
		i = filters.iterator();

		Assert.assertEquals(filters.size(), 1);

		while (i.hasNext()) {
			f = i.next();
			Assert.assertEquals(f.getLotNumber().getValue(), "5");
		}

		Assert.assertNotNull(data.getLightSource());
		Assert.assertTrue(data.getLightSource() instanceof Filament);
		Assert.assertEquals(
				data.getLightSource().getManufacturer().getValue(), "1");
		Assert.assertNotNull(data.getLightSourceSettings());
		Assert.assertEquals(
				data.getLightSourceSettings().getAttenuation().getValue(), 1.0);
	}

}