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
import java.util.LinkedHashMap;

import ode.formats.Index;
import ode.formats.ODEMetadataStoreClient;
import ode.formats.model.ServerInstanceProvider;
import ode.formats.model.UnitsFactory;
import ode.units.UNITS;
import ode.units.quantity.Frequency;
import ode.units.quantity.Length;
import ode.xml.model.enums.LaserMedium;
import ode.xml.model.enums.LaserType;
import ode.xml.model.enums.Pulse;
import ode.api.ServiceFactoryPrx;
import ode.metadatastore.IObjectContainer;
import ode.model.Laser;
import ode.model.LengthI;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LaserTest
{

	private ODEMetadataStoreClient store;

	private static final int LIGHTSOURCE_INDEX = 0;

	private static final int INSTRUMENT_INDEX = 0;
	
	private static Length makeWave(double d) {
	    return UnitsFactory.convertLength(new LengthI(d, UnitsFactory.Channel_EmissionWavelength));
	}

	private static Frequency hz(double d) {
	    return UnitsFactory.convertFrequency(
	            UnitsFactory.makeFrequency(d, UNITS.HERTZ));
	}

	@BeforeMethod
	protected void setUp() throws Exception
	{
		ServiceFactoryPrx sf = new TestServiceFactory().proxy();
        store = new ODEMetadataStoreClient();
        store.initialize(sf);
        store.setEnumerationProvider(new TestEnumerationProvider());
        store.setInstanceProvider(
			new ServerInstanceProvider(store.getEnumerationProvider()));
	}

	@AfterMethod
	protected void tearDown()
			throws IOException {
		store.logout();
	}

	@Test
	public void testNewLaserAllAttributes()
	{
		int i = LIGHTSOURCE_INDEX + 10;
		store.setLaserID("Laser:100", INSTRUMENT_INDEX, i);
		store.setLaserWavelength(makeWave(100.1), INSTRUMENT_INDEX, i);
		store.setLaserType(LaserType.METALVAPOR, INSTRUMENT_INDEX, i);
		store.setLaserLaserMedium(LaserMedium.EMINUS, INSTRUMENT_INDEX, i);
		store.setLaserPockelCell(true, INSTRUMENT_INDEX, i);
		store.setLaserPulse(Pulse.REPETITIVE, INSTRUMENT_INDEX, i);
		store.setLaserRepetitionRate(hz(2.0), INSTRUMENT_INDEX, i);
		store.setLaserTuneable(true, INSTRUMENT_INDEX, i);
	}

	@Test
	public void testNewLaserIdFirst()
	{
	    int i = LIGHTSOURCE_INDEX + 10;
	    store.setLaserID("LightSource:100", INSTRUMENT_INDEX, i);
      store.setLaserType(LaserType.METALVAPOR, INSTRUMENT_INDEX, i);
      LinkedHashMap<Index, Integer> indexes =
          new LinkedHashMap<Index, Integer>();
      indexes.put(Index.INSTRUMENT_INDEX, INSTRUMENT_INDEX);
      indexes.put(Index.LIGHT_SOURCE_INDEX, i);
      IObjectContainer laserContainer =
          store.getIObjectContainer(Laser.class, indexes);
      Assert.assertEquals(laserContainer.LSID, "LightSource:100");
	}

	@Test
    public void testNewLaserConcreteAttributeFirst()
    {
        int i = LIGHTSOURCE_INDEX + 10;
        store.setLaserType(LaserType.METALVAPOR, INSTRUMENT_INDEX, i);
        store.setLaserID("LightSource:100", INSTRUMENT_INDEX, i);
        LinkedHashMap<Index, Integer> indexes =
            new LinkedHashMap<Index, Integer>();
        indexes.put(Index.INSTRUMENT_INDEX, INSTRUMENT_INDEX);
        indexes.put(Index.LIGHT_SOURCE_INDEX, i);
        IObjectContainer laserContainer =
            store.getIObjectContainer(Laser.class, indexes);
        Assert.assertEquals(laserContainer.LSID, "LightSource:100");
    }

	@Test
    public void testNewLaserSuperclassAttributeLast()
    {
        int i = LIGHTSOURCE_INDEX + 10;
        store.setLaserID("LightSource:100", INSTRUMENT_INDEX, i);
        store.setLaserType(LaserType.METALVAPOR, INSTRUMENT_INDEX, i);
        store.setLaserModel("Bar", INSTRUMENT_INDEX, i);
        LinkedHashMap<Index, Integer> indexes =
            new LinkedHashMap<Index, Integer>();
        indexes.put(Index.INSTRUMENT_INDEX, INSTRUMENT_INDEX);
        indexes.put(Index.LIGHT_SOURCE_INDEX, i);
        IObjectContainer laserContainer =
            store.getIObjectContainer(Laser.class, indexes);
        Assert.assertEquals(laserContainer.LSID, "LightSource:100");
    }
}