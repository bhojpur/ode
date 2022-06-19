package ode.services.server.test.utests;

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

import static ode.rtypes.rdouble;
import static ode.rtypes.rstring;

import static ode.formats.model.UnitsFactory.makePower;

import java.util.HashMap;
import java.util.Map;

import ode.model.containers.Dataset;
import ode.model.containers.Project;
import ode.model.core.Image;
import ode.model.core.Pixels;
import ode.model.meta.Experimenter;
import ode.model.meta.ExperimenterGroup;
import ode.units.UNITS;
import ode.model.ArcI;
import ode.model.ChannelI;
import ode.model.DatasetI;
import ode.model.DatasetImageLink;
import ode.model.EventI;
import ode.model.ExperimenterGroupI;
import ode.model.ExperimenterI;
import ode.model.ImageI;
import ode.model.PixelsI;
import ode.model.ProjectI;
import ode.util.IceMapper;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ModelTest {

    @Test(groups = "ticket:636")
    public void testInheritanceInConcreteClasses() throws Exception {
        ArcI arcI = new ArcI();
        // arcI.unload();
        arcI.setPower(makePower(1.0f, UNITS.WATT));
    }

    @Test
    public void testMapper() throws Exception {

        Experimenter e = new Experimenter();
        e.setOmeName("hi");
        e.setLdap(false);
        e.linkExperimenterGroup(new ExperimenterGroup("foo", false));

        IceMapper mapper = new IceMapper();
        ExperimenterI ei = (ExperimenterI) mapper.map(e);
        Assert.assertEquals(new Integer(1), new Integer(ei
                .sizeOfGroupExperimenterMap()));

    }

    @Test
    public void testCopyObject() throws Exception {
        Experimenter e = new Experimenter();
        e.setOmeName("hi");
        e.setLdap(false);
        e.linkExperimenterGroup(new ExperimenterGroup("foo", false));
        ExperimenterI ei = new ExperimenterI();
        ei.copyObject(e, new IceMapper());
        // This may not hold without being called from the top level mapper
        // method
        // assertEquals(new Integer(1), new
        // Integer(ei.sizeOfGroupExperimenterMap()));

        Pixels p = new Pixels();
        Image i = new Image();
        p.setImage(i);
        p.getDetails().setOwner(e);
        new PixelsI().copyObject(p, new IceMapper());

    }

    @Test
    public void testFillObject() throws Exception {
        ExperimenterI ei = new ExperimenterI();
        ei.setOmeName(rstring("name"));
        ei.linkExperimenterGroup(new ExperimenterGroupI());
        Experimenter e = (Experimenter) ei.fillObject(new IceMapper());
        Assert.assertEquals(new Integer(1),
                new Integer(e.sizeOfGroupExperimenterMap()));

        PixelsI p = new PixelsI();
        ImageI i = new ImageI();
        p.setImage(i);
        p.getDetails().setOwner(ei);
        p.fillObject(new IceMapper());
    }

    @Test
    public void testCounts() throws Exception {
        Map<Long, Long> counts = new HashMap<Long, Long>();
        counts.put(1L, 1L);
        class CExperimenter extends Experimenter {
            CExperimenter(Map<Long, Long> counts) {
                setAnnotationLinksCountPerOwner(counts);
            }
        }

        Experimenter e = new CExperimenter(counts);
        ExperimenterI ei = new ExperimenterI();
        ei.copyObject(e, new IceMapper());
        Map<Long, Long> countsi = ei.getAnnotationLinksCountPerOwner();
        Assert.assertEquals(new Long(1L), countsi.get(1L));
    }

    @Test
    public void testLoadedness1() throws Exception {
        ExperimenterGroup g = new ExperimenterGroup();
        Experimenter e = new Experimenter();
        Project p = new Project();
        p.getDetails().setOwner(e);
        e.linkExperimenterGroup(g);
        Assert.assertEquals(1, e.sizeOfGroupExperimenterMap());

        IceMapper mapper = new IceMapper();
        ProjectI pi = (ProjectI) mapper.handleOutput(Project.class, p);
        ExperimenterI ei = (ExperimenterI) pi.getDetails().getOwner();
        Assert.assertEquals(1, e.sizeOfGroupExperimenterMap());

    }

    @Test
    public void testLoadedness2() throws Exception {
        ExperimenterGroup g = new ExperimenterGroup();
        Experimenter e = new Experimenter();
        e.linkExperimenterGroup(g);

        Project p = new Project();
        p.getDetails().setOwner(e);
        p.getDetails().setGroup(g);

        Dataset d = new Dataset();
        d.getDetails().setOwner(e);
        d.getDetails().setGroup(g);

        p.linkDataset(d); // Adding an extra object

        Assert.assertEquals(1, e.sizeOfGroupExperimenterMap());

        IceMapper mapper = new IceMapper();
        ProjectI pi = (ProjectI) mapper.handleOutput(Project.class, p);
        ExperimenterI ei = (ExperimenterI) pi.getDetails().getOwner();
        ExperimenterGroupI gi = (ExperimenterGroupI) pi.getDetails().getGroup();
        Assert.assertEquals(1, ei.sizeOfGroupExperimenterMap());
        Assert.assertEquals(1, gi.sizeOfGroupExperimenterMap());

    }

    @Test
    public void testLoadedness3() throws Exception {
        Experimenter e = new Experimenter();
        e.putAt(Experimenter.GROUPEXPERIMENTERMAP, null);

        Project p = new Project();
        p.getDetails().setOwner(e);

        Assert.assertEquals(-1, e.sizeOfGroupExperimenterMap());

        IceMapper mapper = new IceMapper();
        ProjectI pi = (ProjectI) mapper.handleOutput(Project.class, p);
        ExperimenterI ei = (ExperimenterI) pi.getDetails().getOwner();
        Assert.assertEquals(-1, ei.sizeOfGroupExperimenterMap());

    }

    @Test
    public void testRemoval() throws Exception {
        ode.model.Image i = new ImageI();
        Assert.assertEquals(0, i.sizeOfDatasetLinks());
        DatasetImageLink link = i.linkDataset(new DatasetI());
        Assert.assertEquals(1, i.sizeOfDatasetLinks());
        i.removeDatasetImageLink(link);
        Assert.assertEquals(0, i.sizeOfDatasetLinks());
        
        link = i.linkDataset(new DatasetI());
        Assert.assertEquals(1, i.sizeOfDatasetLinks());
        i.removeDatasetImageLinkFromBoth(link, true);
        Assert.assertEquals(0, i.sizeOfDatasetLinks());
        
        ode.model.Dataset d = new DatasetI();
        i.linkDataset(d);
        Assert.assertEquals(1, i.sizeOfDatasetLinks());
        i.unlinkDataset(d);
        Assert.assertEquals(0, i.sizeOfDatasetLinks());
    }
    
    @Test
    public void testReloading() throws Exception {
        
        // This is our "real" graph
        ode.model.Image i = new ImageI(1L, true);
        i.getDetails().setUpdateEvent(new EventI(1L, false));
        ode.model.Dataset d = new DatasetI(1L, true);
        d.getDetails().setUpdateEvent(new EventI(1L, false));
        i.linkDataset(d);
        Assert.assertEquals(1, i.sizeOfDatasetLinks());
        i.unloadDatasetLinks();
        Assert.assertEquals(-1, i.sizeOfDatasetLinks());
        
        // We shouldn't be able to reload from just any image
        ode.model.Image badId = new ImageI(666L, true);
        badId.getDetails().setUpdateEvent(new EventI(1L, false));
        ode.model.Image badUp = new ImageI(1L, true);
        badUp.getDetails().setUpdateEvent(new EventI(0L, false));
        
        try {
            i.reloadDatasetLinks(badId);
            Assert.fail();
        } catch (ode.ClientError ce) {
            // good
        }
        
        try {
            i.reloadDatasetLinks(badUp);
            Assert.fail();
        } catch (ode.ClientError ce) {
            // good
        }
        
        // From this we want to reload
        ode.model.Image i2 = new ImageI(1L, true);
        i2.getDetails().setUpdateEvent(new EventI(1L, false));
        ode.model.Dataset d2 = new DatasetI(1L, true);
        i2.linkDataset(d2);
        Assert.assertEquals(1, i2.sizeOfDatasetLinks());
        i.reloadDatasetLinks(i2);
        Assert.assertEquals(1, i.sizeOfDatasetLinks());
        Assert.assertEquals(-1, i2.sizeOfDatasetLinks());
        
    }

    @Test(groups = "ticket:2547")
    public void testOrderedCollectionsTicket2547() {
        PixelsI pixels = new PixelsI();
        ChannelI channel0 = new ChannelI();
        ChannelI channel1 = new ChannelI();
        pixels.addChannel(channel0);
        Assert.assertEquals(1, pixels.sizeOfChannels());
        ChannelI old = (ChannelI) pixels.setChannel(0, channel1);
        Assert.assertEquals(old, channel0);
        Assert.assertEquals(1, pixels.sizeOfChannels());
    }
}