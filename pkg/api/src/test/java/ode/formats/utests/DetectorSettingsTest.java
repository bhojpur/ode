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
import ode.xml.model.primitives.PositiveInteger;
import ode.api.ServiceFactoryPrx;
import ode.model.Detector;
import ode.model.DetectorSettings;
import ode.model.Image;
import ode.model.Instrument;
import ode.model.Pixels;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class DetectorSettingsTest
{
	private ODEWrapper wrapper;

	private ODEMetadataStoreClient store;

	private static final int DETECTOR_INDEX = 0;

	private static final int INSTRUMENT_INDEX = 0;

	private static final int IMAGE_INDEX = 0;

	private static final int CHANNEL_INDEX = 0;

	private static final String DETECTOR_MODEL = "Model";

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

        // Need to populate at least one pixels field of each Image.
        store.setPixelsSizeX(new PositiveInteger(1), IMAGE_INDEX);
        store.setPixelsSizeX(new PositiveInteger(1), IMAGE_INDEX + 1);
        store.setPixelsSizeX(new PositiveInteger(1), IMAGE_INDEX + 2);

        // Add some metadata to the Detector to ensure that it is not lost.
        store.setDetectorModel(
			DETECTOR_MODEL, INSTRUMENT_INDEX, DETECTOR_INDEX);

        // Set the LSID on our Objective and link to all three images. Also
        // link the Instrument to all three images.
        store.setDetectorID("Detector:0", INSTRUMENT_INDEX, DETECTOR_INDEX);
        store.setInstrumentID("Instrument:0", INSTRUMENT_INDEX);
        store.setImageInstrumentRef("Instrument:0", IMAGE_INDEX);
        store.setImageInstrumentRef("Instrument:0", IMAGE_INDEX + 1);
        store.setImageInstrumentRef("Instrument:0", IMAGE_INDEX + 2);
        store.setDetectorSettingsID(
			"Detector:0", IMAGE_INDEX, CHANNEL_INDEX);
        store.setDetectorSettingsID(
			"Detector:0", IMAGE_INDEX + 1, CHANNEL_INDEX);
        store.setDetectorSettingsID(
			"Detector:0", IMAGE_INDEX + 2, CHANNEL_INDEX);
	}

	@AfterMethod
	protected void tearDown()
			throws IOException {
		wrapper.close();
		store.logout();
	}

	@Test
	public void testImageDetectorExists()
	{
	    for (int i = 0; i < 3; i++)
	    {
	        LSID lsid = new LSID(Pixels.class, i);
	        Assert.assertNotNull(store.getSourceObject(lsid));
	    }
	    Assert.assertNotNull(store.getSourceObject(new LSID(Instrument.class, 0)));
	    Assert.assertNotNull(store.getSourceObject(new LSID(Detector.class, 0, 0)));
	}

	@Test
	public void testDetectorModelPreserved()
	{
	    Detector detector = store.getDetector(INSTRUMENT_INDEX,
	                                          DETECTOR_INDEX);
	    Assert.assertEquals(DETECTOR_MODEL, detector.getModel().getValue());
	}

	@Test
	public void testContainerCount()
	{
	    Assert.assertEquals(store.countCachedContainers(Detector.class), 1);
	    Assert.assertEquals(store.countCachedContainers(Instrument.class), 1);
	    Assert.assertEquals(store.countCachedContainers(Pixels.class), 3);
	    Assert.assertEquals(store.countCachedContainers(null), 5);
	}

	@Test
	public void testReferences()
	{
	    for (int i = 0; i < 3; i++)
	    {
	        LSID imageLsid = new LSID(Image.class, i);
	        LSID dsLsid = new LSID(DetectorSettings.class, i,
	                               CHANNEL_INDEX);
	        Assert.assertTrue(store.hasReference(dsLsid, new LSID("Detector:0")));
	        Assert.assertTrue(store.hasReference(imageLsid, new LSID("Instrument:0")));
	    }
	    Assert.assertEquals(store.countCachedReferences(null, null), 6);
	}
}