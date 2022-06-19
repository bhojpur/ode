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
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import loci.common.DataTools;
import loci.formats.FormatException;
import loci.formats.in.DynamicMetadataOptions;
import loci.formats.in.MetadataLevel;
import ode.formats.Index;
import ode.formats.ODEMetadataStoreClient;
import ode.formats.importer.ImportConfig;
import ode.formats.importer.ODEWrapper;
import ode.formats.model.ServerInstanceProvider;
import ode.util.LSID;
import ode.api.ServiceFactoryPrx;
import ode.metadatastore.IObjectContainer;
import ode.model.Channel;
import ode.model.Detector;
import ode.model.DetectorSettings;
import ode.model.Dichroic;
import ode.model.Filter;
import ode.model.FilterSet;
import ode.model.IObject;
import ode.model.Image;
import ode.model.Instrument;
import ode.model.LightSettings;
import ode.model.LightSource;
import ode.model.LogicalChannel;
import ode.model.OTF;
import ode.model.Objective;
import ode.model.ObjectiveSettings;
import ode.model.Pixels;
import ode.model.PlaneInfo;
import ode.model.Plate;
import ode.model.Well;
import ode.model.WellSample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Generic set of unit tests, that are designed to be initiated on a
 * Bio-Formats "ID" target, which sanity check metadata validity.
 */
@Test(groups={"manual"})
public class MetadataValidatorTest
{
    /** Logger for this class */
    private static final Logger log =
        LoggerFactory.getLogger(MetadataValidatorTest.class);

    /** Our testing Metadata store client */
    private ODEMetadataStoreClient store;

    /** Our testing Metadata store client (minimum metadata). */
    private ODEMetadataStoreClient minimalStore;

    /** The ODE basic wrapper for Bio-Formats readers */
    private ODEWrapper wrapper;

    /** The ODE basic wrapper (initialized with minimum metadata). */
    private ODEWrapper minimalWrapper;

    /** Our current container cache. */
    private Map<LSID, IObjectContainer> containerCache;

    /** Our current reference cache. */
    private Map<LSID, List<LSID>> referenceCache;

    /** Our testing service factory. */
    private ServiceFactoryPrx sf;

    /** Our import configuration. */
    private ImportConfig config;

    @Parameters({ "target" })
    @BeforeClass
    public void setUp(String target) throws Exception
    {
        log.info("METADATA VALIDATOR TARGET: " + target);
        sf = new TestServiceFactory().proxy();
        config = new ImportConfig();
        // Let the user know at what level we're logging
        ch.qos.logback.classic.Logger lociLogger =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("loci");
        ch.qos.logback.classic.Logger odeLogger =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("ode.formats");
        log.info(String.format(
                "Log levels -- Bio-Formats: %s Bhojpur ODE.importer: %s",
                lociLogger.getLevel(), odeLogger.getLevel()));
        store = new ODEMetadataStoreClient();
        store.initialize(sf);
        store.setEnumerationProvider(new TestEnumerationProvider());
        store.setInstanceProvider(
                new ServerInstanceProvider(store.getEnumerationProvider()));
        minimalStore = new ODEMetadataStoreClient();
        minimalStore.initialize(sf);
        minimalStore.setEnumerationProvider(new TestEnumerationProvider());
        minimalStore.setInstanceProvider(
                new ServerInstanceProvider(minimalStore.getEnumerationProvider()));
        wrapper = new ODEWrapper(config);
        wrapper.setMetadataOptions(
                new DynamicMetadataOptions(MetadataLevel.ALL));
        minimalWrapper = new ODEWrapper(config);
        minimalWrapper.setMetadataOptions(
                new DynamicMetadataOptions(MetadataLevel.MINIMUM));
        wrapper.setMetadataStore(store);
        store.setReader(wrapper.getImageReader());
        minimalStore.setReader(minimalWrapper.getImageReader());
    }

    @AfterClass
    public void tearDown() throws IOException
    {
        wrapper.close();
        minimalWrapper.close();
        store.logout();
        minimalStore.logout();
    }

    @Parameters({ "target" })
    @Test
    public void testMetadataLevelAllSetId(String target) throws Exception
    {
        wrapper.setId(target);
        store.postProcess();
        traceMetadataStoreData(store);
    }

    @Parameters({ "target" })
    @Test
    public void testMetadataLevelMinimumSetId(String target) throws Exception
    {
        minimalWrapper.setId(target);
        minimalStore.postProcess();
        traceMetadataStoreData(minimalStore);
    }

    @Test(dependsOnMethods=
            {"testMetadataLevelAllSetId", "testMetadataLevelMinimumSetId"})
    public void testMetadataLevel()
        throws FormatException, IOException
    {
        Assert.assertEquals(
                minimalWrapper.getMetadataOptions().getMetadataLevel(),
                MetadataLevel.MINIMUM);
        Assert.assertEquals(
                wrapper.getMetadataOptions().getMetadataLevel(),
                MetadataLevel.ALL);
        Assert.assertNotEquals((wrapper.getSeriesMetadata().size()
                          + wrapper.getGlobalMetadata().size()), 0);
    }

    @Test(dependsOnMethods={"testMetadataLevel"})
    public void testMetadataLevelEquivalentDimensions()
    {
        Assert.assertEquals(minimalWrapper.getSeriesCount(), wrapper.getSeriesCount());
        for (int i = 0; i < minimalWrapper.getSeriesCount(); i++)
        {
            wrapper.setSeries(i);
            minimalWrapper.setSeries(i);
            Assert.assertEquals(minimalWrapper.getSizeX(), wrapper.getSizeX());
            Assert.assertEquals(minimalWrapper.getSizeY(), wrapper.getSizeY());
            Assert.assertEquals(minimalWrapper.getSizeZ(), wrapper.getSizeZ());
            Assert.assertEquals(minimalWrapper.getSizeC(), wrapper.getSizeC());
            Assert.assertEquals(minimalWrapper.getSizeT(), wrapper.getSizeT());
            Assert.assertEquals(minimalWrapper.getPixelType(),
                    wrapper.getPixelType());
            Assert.assertEquals(minimalWrapper.isLittleEndian(),
                    wrapper.isLittleEndian());
        }
    }

    @Test(dependsOnMethods={"testMetadataLevel"})
    public void testMetadataLevelEquivalentUsedFiles()
        throws FormatException, IOException
    {
        for (int i = 0; i < minimalWrapper.getSeriesCount(); i++)
        {
            minimalWrapper.setSeries(i);
            wrapper.setSeries(i);

            String[] pixelsOnlyFiles = minimalWrapper.getSeriesUsedFiles();
            String[] allFiles = wrapper.getSeriesUsedFiles();

            Assert.assertEquals(pixelsOnlyFiles.length, allFiles.length);

            Arrays.sort(allFiles);
            Arrays.sort(pixelsOnlyFiles);

            for (int j = 0; j < pixelsOnlyFiles.length; j++)
            {
                Assert.assertEquals(pixelsOnlyFiles[j], allFiles[j]);
            }
        }
    }

    @Test(dependsOnMethods={"testMetadataLevel"})
    public void testMetadataLevelEquivalentPlaneData()
        throws FormatException, IOException
    {
        for (int i = 0; i < minimalWrapper.getSeriesCount(); i++)
        {
            minimalWrapper.setSeries(i);
            wrapper.setSeries(i);
            Assert.assertEquals(minimalWrapper.getImageCount(),
                    wrapper.getImageCount());
            for (int j = 0; j < minimalWrapper.getImageCount(); j++)
            {
                byte[] pixelsOnlyPlane = minimalWrapper.openBytes(j);
                String pixelsOnlySHA1 = sha1(pixelsOnlyPlane);
                byte[] allPlane = wrapper.openBytes(j);
                String allSHA1 = sha1(allPlane);

                if (!pixelsOnlySHA1.equals(allSHA1))
                {
                    Assert.fail(String.format(
                            "MISMATCH: Series:%d Image:%d PixelsOnly%s All:%s",
                            i, j, pixelsOnlySHA1, allSHA1));
                }
            }
        }
    }

    @Test(dependsOnMethods={"testMetadataLevel"})
    public void testEquivalentBlockRetrievalPlaneData()
        throws FormatException, IOException
    {
        String fileName = wrapper.getCurrentFile();
        int sizeX = wrapper.getSizeX();
        int sizeY = wrapper.getSizeY();
        int sizeZ = wrapper.getSizeZ();
        int sizeC = wrapper.getSizeC();
        int sizeT = wrapper.getSizeT();
        int bytesPerPixel = wrapper.getBitsPerPixel() / 8;
        int planeSize = sizeX * sizeY * bytesPerPixel;
        byte[] planar = new byte[planeSize];
        byte[] block = new byte[planeSize];
        int planeNumber = 0;
        String planarDigest;
        String blockDigest;
        for (int t = 0; t < sizeT; t++)
        {
            for (int c = 0; c < sizeC; c++)
            {
                for (int z = 0; z < sizeZ; z++)
                {
                    planeNumber = wrapper.getIndex(z, c, t);
                    wrapper.openPlane2D(fileName, planeNumber,
                                        planar);
                    planarDigest = sha1(planar);
                    wrapper.openPlane2D(fileName, planeNumber, block, 0, 0,
                                        sizeX, sizeY);
                    blockDigest = sha1(block);
                    Assert.assertEquals(blockDigest, planarDigest);
                }
            }
        }
    }

    /**
     * Dumps <code>TRACE</code> data for a given metadata store.
     * @param store The store to dump <code>TRACE</code> data for.
     */
    private void traceMetadataStoreData(ODEMetadataStoreClient store)
    {
        containerCache = store.getContainerCache();
        referenceCache = store.getReferenceCache();
        log.trace("Starting container cache...");
        for (LSID key : containerCache.keySet())
        {
            String s = String.format("%s == %s,%s",
                    key, containerCache.get(key).sourceObject,
                    containerCache.get(key).LSID);
            log.trace(s);
        }
        log.trace("Starting reference cache...");
        for (LSID key : referenceCache.keySet())
        {
            for (LSID value : referenceCache.get(key))
            {
                String s = String.format("%s == %s", key, value);
                log.trace(s);
            }
        }
        log.trace("containerCache contains " + containerCache.size()
                + " entries.");
        log.trace("referenceCache contains "
                + store.countCachedReferences(null, null)
                + " entries.");
        List<IObjectContainer> imageContainers =
            store.getIObjectContainers(Image.class);
        for (IObjectContainer imageContainer : imageContainers)
        {
            Image image = (Image) imageContainer.sourceObject;
            log.trace(String.format(
                    "Image indexes:%s name:%s", imageContainer.indexes,
                    image.getName().getValue()));
        }
    }

    /**
     * Calculates a SHA-1 digest on a byte array.
     * @param buf Byte array to calculate a SHA-1 digest for.
     * @return Hex string of the SHA-1 digest for <code>buf</code>.
     */
    private String sha1(byte[] buf)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            return DataTools.bytesToHex(md.digest(buf));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Examines the container cache and returns whether or not an LSID is
     * present.
     * @param klass Instance class of the source object container.
     * @param lsid LSID to compare against.
     * @return <code>true</code> if the object exists in the container cache,
     * and <code>false</code> otherwise.
     */
    private boolean authoritativeLSIDExists(Class<? extends IObject> klass,
                                            LSID lsid)
    {
        List<IObjectContainer> containers =
            store.getIObjectContainers(klass);
        for (IObjectContainer container : containers)
        {
            LSID containerLSID = new LSID(container.LSID);
            if (containerLSID.equals(lsid))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Examines the container cache and returns whether or not an LSID is
     * present.
     * @param klasses Instance classes of potential source object containers.
     * @param lsid LSID to compare against.
     * @return <code>true</code> if an object exists in the container cache,
     * for one of <code>klasses</code> and <code>false</code> otherwise.
     */
    private boolean authoritativeLSIDExists(List<Class<? extends IObject>> klasses,
                                            LSID lsid)
    {
        for (Class<? extends IObject> klass : klasses)
        {
            List<IObjectContainer> containers =
                store.getIObjectContainers(klass);

            for (IObjectContainer container : containers)
            {
                LSID containerLSID = new LSID(container.LSID);
                if (containerLSID.equals(lsid))
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Test(dependsOnMethods={"testMetadataLevel"})
    public void testCreationDateIsReasonable()
    {
        List<IObjectContainer> containers =
            store.getIObjectContainers(Image.class);
        for (IObjectContainer container : containers)
        {
            Image image = (Image) container.sourceObject;
            Assert.assertNotNull(image.getAcquisitionDate());
            Date acquisitionDate =
                new Date(image.getAcquisitionDate().getValue());
            Date now = new Date(System.currentTimeMillis());
            Date january1st1995 = new GregorianCalendar(1995, 1, 1).getTime();
            if (acquisitionDate.after(now))
            {
                Assert.fail(String.format("%s after %s", acquisitionDate, now));
            }
            if (acquisitionDate.before(january1st1995))
            {
                Assert.fail(String.format("%s before %s", acquisitionDate,
                                   january1st1995));
            }
        }
    }

    @Test(dependsOnMethods={"testMetadataLevel"})
    public void testPlaneInfoZCT()
    {
        List<IObjectContainer> containers =
            store.getIObjectContainers(PlaneInfo.class);
        for (IObjectContainer container : containers)
        {
            PlaneInfo planeInfo = (PlaneInfo) container.sourceObject;
            Assert.assertNotNull(planeInfo.getTheZ(), "theZ is null");
            Assert.assertNotNull(planeInfo.getTheC(), "theC is null");
            Assert.assertNotNull(planeInfo.getTheT(), "theT is null");
        }
    }

    @Test(dependsOnMethods={"testMetadataLevel"})
    public void testChannelCount()
    {
        List<IObjectContainer> containers =
            store.getIObjectContainers(Pixels.class);
        for (IObjectContainer container : containers)
        {
            Pixels pixels = (Pixels) container.sourceObject;
            Assert.assertNotNull(pixels.getSizeC());
            int sizeC = pixels.getSizeC().getValue();
            Integer imageIndex =
                    container.indexes.get(Index.IMAGE_INDEX.getValue());
            int count = store.countCachedContainers(Channel.class, imageIndex);
            String e = String.format(
                    "Pixels sizeC %d != channel object count %d",
                    sizeC, count);
            for (int c = 0; c < sizeC; c++)
            {
                count = store.countCachedContainers(
                        Channel.class, imageIndex, c);
                e = String.format(
                        "Missing channel object; imageIndex=%d " +
                        "channelIndex=%d", imageIndex, c);
                Assert.assertEquals(count, 1, e);
            }
        }
    }

    @Test(dependsOnMethods={"testMetadataLevel"})
    public void testLogicalChannelCount()
    {
        List<IObjectContainer> containers =
            store.getIObjectContainers(Pixels.class);
        for (IObjectContainer container : containers)
        {
            Pixels pixels = (Pixels) container.sourceObject;
            Assert.assertNotNull(pixels.getSizeC());
            int sizeC = pixels.getSizeC().getValue();
            Integer imageIndex =
                    container.indexes.get(Index.IMAGE_INDEX.getValue());
            int count = store.countCachedContainers(LogicalChannel.class,
                                                    imageIndex);
            String e = String.format(
                    "Pixels sizeC %d != logical channel object count %d",
                    sizeC, count);
            Assert.assertEquals(count, sizeC, e);
            for (int c = 0; c < sizeC; c++)
            {
                count = store.countCachedContainers(
                    LogicalChannel.class, imageIndex, c);
                e = String.format(
                        "Missing logical channel object; imageIndex=%d " +
                        "channelIndex=%d", imageIndex, c);
                Assert.assertEquals(count, 1, e);
            }
        }
    }

    @Test(dependsOnMethods={"testMetadataLevel"})
    public void testPlatesExist()
    {
        List<IObjectContainer> containers =
            store.getIObjectContainers(WellSample.class);
        for (IObjectContainer container : containers)
        {
            Map<String, Integer> indexes = container.indexes;
            Integer plateIndex = indexes.get(Index.PLATE_INDEX.getValue());
            String e = String.format(
                    "Plate %d not found in container cache", plateIndex);
            Assert.assertTrue(
                    store.countCachedContainers(Plate.class, plateIndex) > 0, e);
        }
    }

    @Test(dependsOnMethods={"testMetadataLevel"})
    public void testWellsExist()
    {
        List<IObjectContainer> containers =
            store.getIObjectContainers(WellSample.class);
        for (IObjectContainer container : containers)
        {
            Map<String, Integer> indexes = container.indexes;
            Integer plateIndex = indexes.get(Index.PLATE_INDEX.getValue());
            Integer wellIndex = indexes.get(Index.WELL_INDEX.getValue());
            String e = String.format(
                    "Well %d not found in container cache", wellIndex);
            int count = store.countCachedContainers(Well.class, plateIndex,
                    wellIndex);
            Assert.assertEquals(count, 1, e);
        }
    }

    @Test(dependsOnMethods={"testMetadataLevel"})
    public void testAuthoritativeLSIDsAreUnique()
    {
        Set<String> authoritativeLSIDs = new HashSet<String>();
        Set<IObjectContainer> containers =
            new HashSet<IObjectContainer>(containerCache.values());
        for (IObjectContainer container : containers)
        {
            if (!authoritativeLSIDs.add(container.LSID))
            {
                String e = String.format(
                        "LSID from %s,%s not unique.",
                        container.sourceObject, container.LSID);
                Assert.fail(e);
            }
        }
    }

    @Test(dependsOnMethods={"testMetadataLevel"})
    public void testDetectorSettingsIndexes()
    {
        Class<? extends IObject> klass = DetectorSettings.class;
        List<IObjectContainer> containers =
            store.getIObjectContainers(klass);
        for (IObjectContainer container : containers)
        {
            Map<String, Integer> indexes = container.indexes;
            Integer imageIndex = indexes.get(Index.IMAGE_INDEX.getValue());
            Integer channelIndex = indexes.get(Index.CHANNEL_INDEX.getValue());
            int imageCount = store.countCachedContainers(Image.class, null);
            String e = String.format("imageIndex %d >= imageCount %d",
                    imageIndex, imageCount);
            Assert.assertFalse(imageIndex >= imageCount, e);
            int logicalChannelCount = store.countCachedContainers(
                    LogicalChannel.class, imageIndex);
            e = String.format(
                    "channelIndex %d >= logicalChannelCount %d",
                    channelIndex, logicalChannelCount);
            Assert.assertFalse(channelIndex >= logicalChannelCount, e);
        }
    }

    @Test(dependsOnMethods={"testMetadataLevel"})
    public void testDetectorSettingsDetectorRef()
    {
        Class<? extends IObject> klass = DetectorSettings.class;
        List<IObjectContainer> containers =
            store.getIObjectContainers(klass);
        referenceCache = store.getReferenceCache();
        for (IObjectContainer container : containers)
        {
            LSID lsid = new LSID(container.LSID);
            String e = String.format(
                    "%s %s not found in reference cache",
                    klass, lsid);
            Assert.assertTrue(referenceCache.containsKey(lsid), e);
            List<LSID> references = referenceCache.get(lsid);
            Assert.assertTrue(references.size() > 0);
            for (LSID referenceLSID : references)
            {
                Assert.assertNotNull(referenceLSID);
                klass = Detector.class;
                e = String.format(
                        "%s with LSID %s not found in container cache",
                        klass, referenceLSID);
                Assert.assertTrue(authoritativeLSIDExists(klass, referenceLSID), e);
            }
        }
    }

    @Test(dependsOnMethods={"testMetadataLevel"})
    public void testObjectiveSettingsIndexes()
    {
        Class<? extends IObject> klass = ObjectiveSettings.class;
        List<IObjectContainer> containers =
            store.getIObjectContainers(klass);
        for (IObjectContainer container : containers)
        {
            Map<String, Integer> indexes = container.indexes;
            Integer imageIndex = indexes.get(Index.IMAGE_INDEX.getValue());
            int imageCount = store.countCachedContainers(Image.class, null);
            String e = String.format("imageIndex %d >= imageCount %d",
                    imageIndex, imageCount);
            Assert.assertFalse(imageIndex >= imageCount, e);
        }
    }

    @Test(dependsOnMethods={"testMetadataLevel"})
    public void testObjectiveSettingsObjectiveRef()
    {
        Class<? extends IObject> klass = ObjectiveSettings.class;
        List<IObjectContainer> containers =
            store.getIObjectContainers(klass);
        referenceCache = store.getReferenceCache();
        for (IObjectContainer container : containers)
        {
            LSID lsid = new LSID(container.LSID);
            String e = String.format(
                    "%s %s not found in reference cache", klass, lsid);
            Assert.assertTrue(referenceCache.containsKey(lsid), e);
            List<LSID> references = referenceCache.get(lsid);
            Assert.assertTrue(references.size() > 0);
            for (LSID referenceLSID : references)
            {
                Assert.assertNotNull(referenceLSID);
                klass = Objective.class;
                e = String.format(
                        "%s with LSID %s not found in container cache",
                        klass, referenceLSID);
                Assert.assertTrue(authoritativeLSIDExists(klass, referenceLSID), e);
            }
        }
    }

    @Test(dependsOnMethods={"testMetadataLevel"})
    public void testLightSourceSettingsIndexes()
    {
        Class<? extends IObject> klass = LightSettings.class;
        List<IObjectContainer> containers =
            store.getIObjectContainers(klass);
        for (IObjectContainer container : containers)
        {
            Map<String, Integer> indexes = container.indexes;
            Integer imageIndex = indexes.get(Index.IMAGE_INDEX.getValue());
            Integer channelIndex = indexes.get(Index.CHANNEL_INDEX.getValue());
            int imageCount = store.countCachedContainers(Image.class, null);
            String e = String.format("imageIndex %d >= imageCount %d",
                    imageIndex, imageCount);
            Assert.assertFalse(imageIndex >= imageCount, e);
            int logicalChannelCount = store.countCachedContainers(
                    LogicalChannel.class, imageIndex);
            e = String.format(
                    "channelIndex %d >= logicalChannelCount %d",
                    channelIndex, logicalChannelCount);
            Assert.assertFalse(channelIndex >= logicalChannelCount, e);
        }
    }

    @Test(dependsOnMethods={"testMetadataLevel"})
    public void testLightSourceSettingsLightSourceRef()
    {
        Class<? extends IObject> klass = LightSettings.class;
        List<IObjectContainer> containers =
            store.getIObjectContainers(klass);
        referenceCache = store.getReferenceCache();
        for (IObjectContainer container : containers)
        {
            LSID lsid = new LSID(container.LSID);
            String e = String.format(
                    "%s %s not found in reference cache", klass, container.LSID);
            Assert.assertTrue(referenceCache.containsKey(lsid), e);
            List<LSID> references = referenceCache.get(lsid);
            Assert.assertTrue(references.size() > 0);
            for (LSID referenceLSID : references)
            {
                Assert.assertNotNull(referenceLSID);
                klass = LightSource.class;
                e = String.format(
                        "%s with LSID %s not found in container cache",
                        klass, referenceLSID);
                Assert.assertTrue(authoritativeLSIDExists(klass, referenceLSID), e);
            }
        }
    }

    @Test(dependsOnMethods={"testMetadataLevel"})
    public void testLogicalChannelRefs()
    {
        Class<? extends IObject> klass = LogicalChannel.class;
        List<IObjectContainer> containers =
            store.getIObjectContainers(klass);
        referenceCache = store.getReferenceCache();
        for (IObjectContainer container : containers)
        {
            LSID lsid = new LSID(container.LSID);
            if (!referenceCache.containsKey(lsid))
            {
                continue;
            }
            List<LSID> references = referenceCache.get(lsid);
            Assert.assertTrue(references.size() > 0);
            for (LSID referenceLSID : references)
            {
                String asString = referenceLSID.toString();
                if (asString.endsWith(ODEMetadataStoreClient.ODE_EMISSION_FILTER_SUFFIX)
                    || asString.endsWith(ODEMetadataStoreClient.ODE_EXCITATION_FILTER_SUFFIX))
                {
                    int index = asString.lastIndexOf(':');
                    referenceLSID = new LSID(asString.substring(0, index));
                }
                Assert.assertNotNull(referenceLSID);
                List<Class<? extends IObject>> klasses =
                    new ArrayList<Class<? extends IObject>>();
                klasses.add(Filter.class);
                klasses.add(FilterSet.class);
                String e = String.format(
                        "LSID %s not found in container cache", referenceLSID);
                Assert.assertTrue(authoritativeLSIDExists(klasses, referenceLSID), e);
            }
        }
    }

    @Test(dependsOnMethods={"testMetadataLevel"})
    public void testInstrumentImageRef()
    {
        Class<? extends IObject> klass = Instrument.class;
        List<IObjectContainer> containers =
            store.getIObjectContainers(klass);
        referenceCache = store.getReferenceCache();
        for (IObjectContainer container : containers)
        {
            LSID lsid = new LSID(container.LSID);
            for (LSID target : referenceCache.keySet())
            {
                for (LSID reference : referenceCache.get(target))
                {
                    if (reference.equals(lsid))
                    {
                        return;
                    }
                }
            }
            Assert.fail(String.format(
                    "%s %s not referenced by any image.", klass, lsid));
        }
    }

    @Test(dependsOnMethods={"testMetadataLevel"})
    public void testFilterSetRefs()
    {
        Class<? extends IObject> klass = FilterSet.class;
        List<IObjectContainer> containers =
            store.getIObjectContainers(klass);
        referenceCache = store.getReferenceCache();
        for (IObjectContainer container : containers)
        {
            LSID lsid = new LSID(container.LSID);
            if (!referenceCache.containsKey(lsid))
            {
                continue;
            }
            List<LSID> references = referenceCache.get(lsid);
            Assert.assertTrue(references.size() > 0);
            for (LSID referenceLSID : references)
            {
                Assert.assertNotNull(referenceLSID);
                List<Class<? extends IObject>> klasses =
                    new ArrayList<Class<? extends IObject>>();
                klasses.add(Filter.class);
                klasses.add(Dichroic.class);
                String e = String.format(
                        "LSID %s not found in container cache", referenceLSID);
                Assert. assertTrue(authoritativeLSIDExists(klasses, referenceLSID), e);
            }
        }
    }

    @Test(dependsOnMethods={"testMetadataLevel"})
    public void testOTFIsReferenced()
    {
        Class<? extends IObject> klass = OTF.class;
        List<IObjectContainer> containers =
            store.getIObjectContainers(klass);
        referenceCache = store.getReferenceCache();
        for (IObjectContainer container : containers)
        {
            LSID lsid = new LSID(container.LSID);
            for (LSID target : referenceCache.keySet())
            {
                for (LSID reference : referenceCache.get(target))
                {
                    if (reference.equals(lsid))
                    {
                        return;
                    }
                }
            }
            Assert.fail(String.format(
                    "%s %s not referenced by any object.", klass, lsid));
        }
    }
}