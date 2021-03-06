package integration.chgrp;

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

import integration.AbstractServerTest;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ode.specification.XMLMockObjects;
import ode.specification.XMLWriter;
import ode.xml.model.ODE;
import ode.RString;
import ode.api.IContainerPrx;
import ode.api.IMetadataPrx;
import ode.api.IUpdatePrx;
import ode.cmd.Chgrp2;
import ode.cmd.Delete2;
import ode.cmd.OK;
import ode.cmd.Response;
import ode.gateway.util.Requests;
import ode.model.Channel;
import ode.model.Dataset;
import ode.model.DatasetI;
import ode.model.DatasetImageLink;
import ode.model.DatasetImageLinkI;
import ode.model.Detector;
import ode.model.ExperimenterGroup;
import ode.model.ExperimenterGroupI;
import ode.model.Fileset;
import ode.model.FilesetI;
import ode.model.Filter;
import ode.model.FilterSet;
import ode.model.IObject;
import ode.model.Image;
import ode.model.Instrument;
import ode.model.Laser;
import ode.model.LightSource;
import ode.model.LogicalChannel;
import ode.model.OTF;
import ode.model.Objective;
import ode.model.Pixels;
import ode.sys.EventContext;
import ode.sys.Parameters;
import ode.sys.ParametersI;
import ode.util.TempFileManager;
import ode.gateway.model.ChannelAcquisitionData;
import ode.gateway.model.InstrumentData;
import ode.gateway.model.LightSourceData;

/**
 */
public class MultiImageFilesetMoveTest extends AbstractServerTest {

    ExperimenterGroup secondGroup;
    
    private IMetadataPrx iMetadata;

    @BeforeClass
    public void setupSecondGroup() throws Exception {
        EventContext ec = iAdmin.getEventContext();
        secondGroup = newGroupAddUser("rwrw--", ec.userId);
        iAdmin.getEventContext(); // Refresh.
    }

    static class Fixture {
        final List<Dataset> datasets;
        final List<Image> images;
        Fixture(List<Dataset> datasets, List<Image> images) {
            this.datasets = datasets;
            this.images = images;
        }
        DatasetImageLink link(IUpdatePrx iUpdate, int datasetIndex, int imageIndex) throws Exception {
            DatasetImageLink link = new DatasetImageLinkI();
            link.setParent((Dataset) datasets.get(datasetIndex).proxy());
            link.setChild((Image) images.get(imageIndex).proxy());
            link = (DatasetImageLink) iUpdate.saveAndReturnObject(link);
            return link;
        }
    }

    protected List<Image> importMIF(int seriesCount) throws Throwable {
        File fake = TempFileManager.create_path("importMIF",
                String.format("&series=%d.fake", seriesCount));
        List<Pixels> pixels = importFile(createImporter(), fake, null);
        Assert.assertEquals(seriesCount, pixels.size());
        List<Image> images = new ArrayList<Image>();
        for (Pixels pixel : pixels) {
            images.add(pixel.getImage());
        }
        return images;
    }

    /**
     * Creates a list of the given number of {@link Dataset} instances with
     * names of the form "name [1]", "name [2]", etc. and
     * returns them in a list.
     * 
     * @param count
     * @param baseName
     * @return
     * @throws Throwable
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected List<Dataset> createDatasets(int count, String baseName)
            throws Throwable {

        List<IObject> rv = new ArrayList<IObject>();
        for (int i = 0; i < count; i++) {
            Dataset dataset = new DatasetI();
            String suffix = " [" + (i + 1) + "]";
            RString name = ode.rtypes.rstring(baseName + suffix);
            dataset.setName(name);
            rv.add(dataset);
        }
        return (List) iUpdate.saveAndReturnArray(rv);
    }


    protected Fixture createFixture(int datasetCount, int imageCount) throws Throwable {
        List<Dataset> datasets = createDatasets(datasetCount, "MIF");
        List<Image> images = importMIF(imageCount);
        return new Fixture(datasets, images);
    }

    /**
     * Simplest example of the MIF chgrp edge case: a single fileset containing
     * 2 images is split among 2 datasets. Each sibling CANNOT be moved
     * independently of the other.
     */
    @Test(groups = {"fs", "integration"})
    public void testBasicProblem() throws Throwable {
        Fixture f = createFixture(2, 2);
        f.link(iUpdate, 0, 0);
        f.link(iUpdate, 1, 1);

        long img0 = f.images.get(0).getId().getValue();
        long img1 = f.images.get(1).getId().getValue();
        long fs0 = f.images.get(0).getFileset().getId().getValue();
        long fs1 = f.images.get(1).getFileset().getId().getValue();
        Assert.assertEquals(fs0, fs1);

        final Chgrp2 mv = Requests.chgrp().target(f.images.get(0)).toGroup(secondGroup).build();

        Response rsp = doChange(client, factory, mv, false); // Don't pass
        // However, it should still be possible to delete the 2 images
        // and have the fileset cleaned up.
        Delete2 dc = Requests.delete().target("Image").id(img0, img1).build();
        callback(true, client, dc);
        assertDoesNotExist(new FilesetI(fs0, false));
    }

    /**
     * Creates a MIF images. Moves the fileset.
     */
    @Test(groups = {"fs", "integration"})
    public void testMoveFilesetAsRoot() throws Throwable {
    	int imageCount = 2;
    	List<Image> images = importMIF(imageCount);

        final Chgrp2 mv = Requests.chgrp().target(images.get(0).getFileset()).toGroup(secondGroup).build();

        Response rsp = doChange(client, factory, mv, true);
        OK err = (OK) rsp;
        Assert.assertNotNull(err);
    }
    
    
    /**
     * Creates a MIF images with acquisition data. Moves the fileSet
     */
    @Test(groups = {"fs", "integration"})
    public void testMoveMIFWithAcquisitionData() throws Throwable {
    	int imageCount = 1;
    	Fileset set = new FilesetI();
    	set.setTemplatePrefix(ode.rtypes.rstring("fake"));
    	List<Long> ids = new ArrayList<Long>();
    	for (int i = 0; i < imageCount; i++) {
    		File f = File.createTempFile(
    		        RandomStringUtils.random(100, false, true),".ode.xml");
    		f.deleteOnExit();
    		XMLMockObjects xml = new XMLMockObjects();
    		XMLWriter writer = new XMLWriter();
    		ODE ode = xml.createImageWithAcquisitionData();
    		writer.writeFile(f, ode, true);
    		List<Pixels> pixels = null;
    		try {
    			pixels = importFile(f, "ode.xml");
    		} catch (Throwable e) {
    			throw new Exception("cannot import image", e);
    		}
    		Pixels p = pixels.get(0);
    		ids.add(p.getImage().getId().getValue());
    	}
    	//Load the image
    	IContainerPrx iContainer = factory.getContainerService();
    	List<Image> images = iContainer.getImages(Image.class.getName(),
    			ids, new Parameters());
    	Iterator<Image> j = images.iterator();
    	while (j.hasNext()) {
    		set.addImage(j.next());
		}
    	set = (Fileset) iUpdate.saveAndReturnObject(set);
    	final Chgrp2 mv = Requests.chgrp().target(set).toGroup(secondGroup).build();
    	Response rsp = doChange(client, factory, mv, true);
    	OK err = (OK) rsp;
    	Assert.assertNotNull(err);

    	// Reconnect in second group to check group.id for all objects in graph
    	long gid2 = secondGroup.getId().getValue();
    	loginUser(new ExperimenterGroupI(secondGroup.getId().getValue(),
    			false));
    	iContainer = factory.getContainerService();
    	images = iContainer.getImages(Image.class.getName(),
    			ids, new Parameters());
    	
    	//for each image
    	Iterator<Image> i = images.iterator();
    	while (i.hasNext()) {
    		Image img = i.next();
    		Assert.assertEquals(img.getDetails().getGroup().getId().getValue(), gid2);
    		Pixels pixels = img.getPrimaryPixels();
        	long pixId = pixels.getId().getValue();
        	//method already tested in PixelsServiceTest
        	//make sure objects are loaded.
        	pixels = factory.getPixelsService().retrievePixDescription(pixId);
        	List<Long> lcIds = new ArrayList<Long>();
        	LogicalChannel lc;
        	Channel channel;
        	for (int i1 = 0; i1 < pixels.getSizeC().getValue(); i1++) {
    			channel = pixels.getChannel(i1);
    			lc = channel.getLogicalChannel();
    			lcIds.add(lc.getId().getValue());
        	}
        	iMetadata = factory.getMetadataService();
        	List<LogicalChannel> channels = iMetadata.loadChannelAcquisitionData(
        			lcIds);
        	Assert.assertEquals(channels.size(), pixels.getSizeC().getValue());
        	LogicalChannel loaded;
        	Iterator<LogicalChannel> lci = channels.iterator();
        	LightSourceData l;
        	while (lci.hasNext()) {
        		loaded = lci.next();
        		Assert.assertNotNull(loaded);
        		Assert.assertEquals(loaded.getDetails().getGroup().getId().getValue(), gid2);
            	ChannelAcquisitionData data = new ChannelAcquisitionData(loaded);
            	Assert.assertEquals(data.getDetector().asIObject().getDetails().getGroup().getId().getValue(), gid2);
//            	Assert.assertEquals(data.getFilterSet().asIObject().getDetails().getGroup().getId().getValue(), gid2);		// getFilterSet() is null
            	l = (LightSourceData) data.getLightSource();
            	Assert.assertEquals(l.asIObject().getDetails().getGroup().getId().getValue(), gid2);
            	Assert.assertEquals(loaded.getDetectorSettings().getDetails().getGroup().getId().getValue(), gid2);
            	Assert.assertEquals(loaded.getLightSourceSettings().getDetails().getGroup().getId().getValue(), gid2);
            	Assert.assertNotNull(loaded.getDetectorSettings().getBinning());	// No Group on Binning
            	Assert.assertEquals(loaded.getDetectorSettings().getDetector().getDetails().getGroup().getId().getValue(), gid2);
            	Assert.assertNotNull(loaded.getDetectorSettings().getDetector().getType());
            	Assert.assertEquals(loaded.getLightPath().getDetails().getGroup().getId().getValue(), gid2);
            	Assert.assertEquals(data.getLightPath().getDichroic().asIObject().getDetails().getGroup().getId().getValue(), gid2);
            	Assert.assertNotNull(data.getContrastMethod());
            	Assert.assertNotNull(data.getIllumination());
            	Assert.assertNotNull(data.getMode());

//            	//OTF support            	
//            	Assert.assertNotNull(loaded.getOtf());		// null
//            	Assert.assertNotNull(loaded.getOtf().getFilterSet());
//            	Assert.assertEquals(loaded.getOtf().getObjective().getDetails().getGroup().getId().getValue(), gid2);
//            	Assert.assertEquals(loaded.getOtf().getFilterSet().getDetails().getGroup().getId().getValue(), gid2);
//            	Assert.assertNotNull(loaded.getOtf().getPixelsType());

    		}
        	
        	
        	Instrument instrument = iMetadata.loadInstrument(
        			img.getInstrument().getId().getValue());
        	InstrumentData data = new InstrumentData(instrument);
        	Assert.assertEquals(data.asIObject().getDetails().getGroup().getId().getValue(), gid2);
    		Assert.assertTrue(CollectionUtils.isNotEmpty(instrument.copyDetector()));
    		Assert.assertTrue(CollectionUtils.isNotEmpty(instrument.copyDichroic()));
    		Assert.assertTrue(CollectionUtils.isNotEmpty(instrument.copyFilter()));
    		Assert.assertTrue(CollectionUtils.isNotEmpty(instrument.copyFilterSet()));
    		Assert.assertEquals(instrument.sizeOfLightSource(), 5);
    		Assert.assertTrue(CollectionUtils.isNotEmpty(instrument.copyObjective()));
//    		Assert.assertTrue(instrument.sizeOfOtf() > 0);
    		
    		Assert.assertEquals(instrument.sizeOfDetector(),
    			data.getDetectors().size());
    		Assert.assertEquals(instrument.sizeOfDichroic(),
    			data.getDichroics().size());
    		Assert.assertEquals(instrument.sizeOfFilter(),
    			data.getFilters().size());
    		Assert.assertEquals(instrument.sizeOfFilterSet(),
    			data.getFilterSets().size());
    		Assert.assertEquals(instrument.sizeOfLightSource(),
    			data.getLightSources().size());
    		Assert.assertEquals(instrument.sizeOfObjective(),
    			data.getObjectives().size());
    		Assert.assertEquals(instrument.sizeOfOtf(),
    			data.getOTF().size());
    		
    		List<Detector> detectors;
        	List<Filter> filters;
        	List<FilterSet> filterSets;
        	List<Objective> objectives;
        	List<LightSource> lights;
        	List<OTF> otfs;
        	Detector detector;
        	Filter filter;
        	FilterSet fs;
        	Objective objective;
        	OTF otf;
        	LightSource light;
        	Laser laser;
        	
        	Iterator j1;
    		detectors = instrument.copyDetector();
    		j1 = detectors.iterator();
    		while (j1.hasNext()) {
    			detector = (Detector) j1.next();
    			Assert.assertEquals(detector.getDetails().getGroup().getId().getValue(), gid2);
			}
    		filters = instrument.copyFilter();
    		j1 = filters.iterator();
    		while (j1.hasNext()) {
				filter = (Filter) j1.next();
				Assert.assertEquals(filter.getTransmittanceRange().getDetails().getGroup().getId().getValue(), gid2);
			}
    		filterSets = instrument.copyFilterSet();
    		j1 = filterSets.iterator();
    		while (j1.hasNext()) {
				fs = (FilterSet) j1.next();
				Assert.assertEquals(fs.getDetails().getGroup().getId().getValue(), gid2);
			}
    		objectives = instrument.copyObjective();
    		j1 = objectives.iterator();
    		while (j1.hasNext()) {
				objective = (Objective) j1.next();
				Assert.assertEquals(objective.getDetails().getGroup().getId().getValue(), gid2);
				Assert.assertNotNull(objective.getCorrection());
				Assert.assertNotNull(objective.getImmersion());
//				Assert.assertEquals(objective.getImmersion().getDetails().getGroup().getId().getValue(), gid2);
			}
//    		otfs = instrument.copyOtf();
//    		j1 = otfs.iterator();
//    		while (j1.hasNext()) {
//				otf = (OTF) j1.next();
//				objective = otf.getObjective();
//				Assert.assertNotNull(otf.getPixelsType());
//				Assert.assertNotNull(otf.getFilterSet());
//				Assert.assertNotNull(objective);
//				Assert.assertNotNull(objective.getCorrection());
//				Assert.assertNotNull(objective.getImmersion());
//			}
    		lights = instrument.copyLightSource();
    		j1 = lights.iterator();
    		while (j1.hasNext()) {
    			light = (LightSource) j1.next();
				if (light instanceof Laser) {
					laser = (Laser) light;
					Assert.assertNotNull(laser.getType());
					Assert.assertNotNull(laser.getLaserMedium());
//					Assert.assertNotNull(laser.getPulse());
				}
			}
    	
    	
    	}
    }
    
    /**
     * Creates a MIF images and link to a dataset
     */
    @Test(groups = {"fs", "integration"})
    public void testMoveDatasetWithMIF() throws Throwable {
    	int imageCount = 2;
    	List<Image> images = importMIF(imageCount);
    	Dataset dataset = new DatasetI();
        dataset.setName(ode.rtypes.rstring("testMoveDatasetWithMIF"));
        dataset = (Dataset) iUpdate.saveAndReturnObject(dataset);
        Iterator<Image> i = images.iterator();
        List<IObject> links = new ArrayList<IObject>();
        while (i.hasNext()) {
        	DatasetImageLink link = new DatasetImageLinkI();
        	link.setChild((Image) i.next().proxy());
        	link.setParent((Dataset) dataset.proxy());
			links.add(link);
		}
        long filesetID = images.get(0).getFileset().getId().getValue();
        iUpdate.saveAndReturnArray(links);
        final Chgrp2 dc = Requests.chgrp().target(dataset).toGroup(secondGroup).build();

    	doAllChanges(client, factory, true, dc);

    	loginUser(new ExperimenterGroupI(secondGroup.getId().getValue(),
    			false));
    	//load the dataset
    	IContainerPrx iContainer = factory.getContainerService();
    	ParametersI param = new ParametersI();
    	param.leaves();
    	List<IObject> values = iContainer.loadContainerHierarchy(
        		Dataset.class.getName(),
        		Arrays.asList(dataset.getId().getValue()), param);
    	Assert.assertEquals(1, values.size());
    	dataset = (Dataset) values.get(0);
    	Assert.assertEquals(imageCount, dataset.sizeOfImageLinks());
    	List<DatasetImageLink> imageLinks = dataset.copyImageLinks();
    	for (DatasetImageLink link : imageLinks) {
    		Image img = link.getChild();
    		Assert.assertEquals(img.getFileset().getId().getValue(), filesetID);
    	}
    }
}
