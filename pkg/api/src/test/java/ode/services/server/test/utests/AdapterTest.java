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

import static ode.rtypes.rarray;
import static ode.rtypes.rbool;
import static ode.rtypes.rclass;
import static ode.rtypes.rdouble;
import static ode.rtypes.rfloat;
import static ode.rtypes.rint;
import static ode.rtypes.rlist;
import static ode.rtypes.rlong;
import static ode.rtypes.robject;
import static ode.rtypes.rset;
import static ode.rtypes.rstring;
import static ode.rtypes.rtime;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.Assert;
import ode.model.annotations.AnnotationAnnotationLink;
import ode.model.annotations.CommentAnnotation;
import ode.model.containers.Dataset;
import ode.model.containers.Project;
import ode.model.containers.ProjectDatasetLink;
import ode.model.core.Image;
import ode.model.core.Pixels;
import ode.model.display.CodomainMapContext;
import ode.model.display.ChannelBinding;
import ode.model.display.ReverseIntensityContext;
import ode.model.meta.Event;
import ode.parameters.Parameters;
import ode.RArray;
import ode.RBool;
import ode.RClass;
import ode.RDouble;
import ode.RFloat;
import ode.RInt;
import ode.RList;
import ode.RLong;
import ode.RObject;
import ode.RSet;
import ode.RString;
import ode.RTime;
import ode.RType;
import ode.model.AnnotationAnnotationLinkI;
import ode.model.CommentAnnotationI;
import ode.model.DatasetI;
import ode.model.EventI;
import ode.model.IObject;
import ode.model.ImageI;
import ode.model.PlaneSlicingContextI;
import ode.model.ProjectDatasetLinkI;
import ode.model.ProjectI;
import ode.model.ReverseIntensityContextI;
import ode.sys.ParametersI;
import ode.util.IceMapper;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AdapterTest {

    Project p;

    Dataset d;

    Image i;

    Pixels pix;

    @BeforeMethod
    protected void setUp() throws Exception {
        p = new Project();
        d = new Dataset();
        i = new Image();
        pix = new Pixels();
    }

    @Test
    public void test_simple() throws Exception {
        IceMapper mapper = new IceMapper();
        ProjectI p_remote = (ProjectI) mapper.map(p);
        Project p_test = (Project) mapper.reverse(p_remote);
    }

    @Test
    public void test_with_values() throws Exception {
        p.setName("test");
        IceMapper mapper = new IceMapper();
        ProjectI p_remote = (ProjectI) mapper.map(p);
        Project p_test = (Project) mapper.reverse(p_remote);
        Assert.assertTrue("test".equals(p_remote.getName()));
        Assert.assertTrue("test".equals(p_test.getName()));
    }

    @Test
    public void test_with_collections() throws Exception {
        p.linkDataset(d);
        IceMapper mapper = new IceMapper();
        ProjectI p_remote = (ProjectI) mapper.map(p);
        Project p_test = (Project) mapper.reverse(p_remote);
        Assert.assertTrue(p_remote.copyDatasetLinks().size() == 1);
        Assert.assertTrue(p_test.sizeOfDatasetLinks() == 1);
    }

    @Test
    public void test_complex() throws Exception {
        p.linkDataset(d);
        d.linkImage(i);
        i.addPixels(pix);
        IceMapper mapper = new IceMapper();
        ProjectI p_remote = (ProjectI) mapper.map(p);
        Project p_test = (Project) mapper.reverse(p_remote);
        Assert.assertTrue(p_remote.copyDatasetLinks().size() == 1);
        Assert.assertTrue(p_test.sizeOfDatasetLinks() == 1);
        ProjectDatasetLinkI pdl_remote = (ProjectDatasetLinkI) p_remote
                .copyDatasetLinks().get(0);
        ProjectDatasetLink pdl_test = (ProjectDatasetLink) mapper
                .reverse(pdl_remote);
        Assert.assertTrue(pdl_remote.getParent() == p_remote);
        Assert.assertTrue(pdl_test.parent() != p.collectDatasetLinks(null).get(0));

        ode.model.Dataset d_remote = pdl_remote.getChild();
        Assert.assertTrue(d_remote.sizeOfImageLinks() == 1);
        ode.model.DatasetImageLink dil_remote = d_remote.copyImageLinks()
                .get(0);
        Assert.assertTrue(dil_remote.getParent() == d_remote);
        ode.model.Image i_remote = dil_remote.getChild();
        Assert.assertTrue(i_remote.sizeOfPixels() == 1);
        ode.model.Pixels pix_remote = i_remote.copyPixels().get(0);
        Assert.assertTrue(pix_remote.getImage() == i_remote);
    }

    @Test
    public void testInheritance() throws Exception {

        IceMapper mapper = new IceMapper();

        ChannelBinding def = new ChannelBinding();
        CodomainMapContext cmc = new ReverseIntensityContext();
        cmc.setChannelBinding(def);

        ReverseIntensityContextI cmc_remote = (ReverseIntensityContextI) mapper
                .map(cmc);
        CodomainMapContext cmc_test = (CodomainMapContext) mapper
                .reverse(cmc_remote);
    }

    @Test
    public void testUnloadedCollectionIsMappedUnloaded() throws Exception {

        IceMapper mapper = new IceMapper();

        Project p = new Project();
        p.putAt(Project.DATASETLINKS, null);
        Assert.assertTrue(p.sizeOfDatasetLinks() < 0);

        ProjectI p_remote = (ProjectI) mapper.map(p);
        Assert.assertTrue(p_remote.sizeOfDatasetLinks() > 0);
    }

    @Test
    public void testUnloadedCollectionisReversedUnloaded() throws Exception {

        IceMapper mapper = new IceMapper();

        ProjectI p_remote = new ProjectI();
        p_remote.unloadDatasetLinks();

        Project p = (Project) mapper.reverse(p_remote);

        Assert.assertTrue(p.sizeOfDatasetLinks() < 0);

    }

    @Test
    public void testUnloadedObjectisMappedUnloaded() throws Exception {

        IceMapper mapper = new IceMapper();

        CommentAnnotation pa = new CommentAnnotation();
        pa.addAnnotationAnnotationLink(new AnnotationAnnotationLink(1L, false));

        CommentAnnotationI pa_remote = (CommentAnnotationI) mapper.map(pa);
        Assert.assertFalse(pa_remote.copyAnnotationLinks().get(0).isLoaded());

    }

    @Test
    public void testUnloadedObjectIsReversedUnloaded() throws Exception {

        IceMapper mapper = new IceMapper();

        CommentAnnotationI pa_remote = new CommentAnnotationI();
        AnnotationAnnotationLinkI p_remote = new AnnotationAnnotationLinkI();
        p_remote.unload();
        pa_remote.addAnnotationAnnotationLink(p_remote);

        CommentAnnotation pa = (CommentAnnotation) mapper.reverse(pa_remote);
        Assert.assertFalse(pa.iterateAnnotationLinks().next().isLoaded());
    }

    @Test(groups = "ticket:684")
    public void testNoDuplicateObjectsWithListsInsteadOfSets() throws Exception {
        p.linkDataset(d);
        IceMapper mapper = new IceMapper();
        ProjectI p_remote = (ProjectI) mapper.map(p);
        DatasetI d_remote = (DatasetI) mapper.map(d);

        long p_sz = p_remote.sizeOfDatasetLinks();
        long d_sz = d_remote.sizeOfProjectLinks();

        Assert.assertTrue(d_sz == 1L, d_sz + "!=1");
        Assert.assertTrue(p_sz == 1L, p_sz + "!=1");
    }

    @Test
    public void testUnloadedCollectionsRemainUnloaded() throws Exception {
        p.putAt(Project.DATASETLINKS, null);
        Assert.assertTrue(p.sizeOfDatasetLinks() < 0);
        IceMapper mapper = new IceMapper();
        ProjectI p_remote = (ProjectI) mapper.map(p);
        Assert.assertFalse(p_remote.sizeOfDatasetLinks() > 0);

        // reverse
        p_remote = new ProjectI();
        p_remote.unloadDatasetLinks();
        Assert.assertFalse(p_remote.sizeOfDatasetLinks() > 0);
        mapper = new IceMapper();
        p = (Project) mapper.reverse(p_remote);
        Assert.assertTrue(p.sizeOfDatasetLinks() < 0);

    }

    @Test
    public void testParameterMapAndPojoOptions() throws Exception {
        Parameters po = new Parameters();
        po.leaves();
        po.exp(1L);

        RList rl = rlist(
                Arrays.<RType> asList(rstring("a"), rstring("b")));

        ParametersI p = new ParametersI();
        p.leaves();
        p.exp(rlong(1L));
        Map<String, RType> map = new HashMap<String, RType>();
        map.put("c", rbool(true));
        map.put("d", rlong(1L));

        IceMapper mapper = new IceMapper();
        Map reversed = mapper.reverse(map);
        Long l = (Long) reversed.get("d");
        Assert.assertEquals(l, po.getExperimenter());
        Boolean b = (Boolean) reversed.get("c");
        Assert.assertEquals(b, Boolean.valueOf(po.isLeaves()));
    }

    @Test
    public void testRTypes() throws Exception {
        IceMapper mapper = new IceMapper();
        // Nulls
        Assert.assertNull(mapper.fromRType((RString) null));
        Assert.assertNull(mapper.fromRType((RBool) null));
        Assert.assertNull(mapper.fromRType((RInt) null));
        Assert.assertNull(mapper.fromRType((RLong) null));
        Assert.assertNull(mapper.fromRType((RDouble) null));
        Assert.assertNull(mapper.fromRType((RClass) null));
        Assert.assertNull(mapper.fromRType((RFloat) null));
        Assert.assertNull(mapper.fromRType((RObject) null));
        Assert.assertNull(mapper.convert((RTime) null));
        Assert.assertNull(mapper.fromRType((RList) null));
        Assert.assertNull(mapper.fromRType((RSet) null));
        //
        Assert.assertEquals("a", mapper.fromRType(rstring("a")));
        Assert.assertEquals(1L, mapper.fromRType(rlong(1L)));
        Assert.assertEquals(1, mapper.fromRType(rint(1)));
        Assert.assertEquals(1.0, mapper.fromRType(rdouble(1.0)));
        Assert.assertEquals(1.0f, mapper.fromRType(rfloat(1.0f)));
        Assert.assertEquals(true, mapper.fromRType(rbool(true)));
        Assert.assertEquals(Image.class, mapper.fromRType(rclass("Image")));
        IObject obj = new ImageI(1L, false);
        Image img = (Image) mapper.fromRType(robject(obj));
        Assert.assertEquals(img.getId(), Long.valueOf(obj.getId().getValue()));
        RTime time = rtime(1L);
        Timestamp ts = mapper.convert(time);
        Assert.assertEquals(ts.getTime(), time.getValue());
        RArray jarr = rarray(rstring("A"));
        String[] strings = (String[]) mapper.fromRType(jarr);
        Assert.assertTrue(strings[0].equals("A"));
        RList jlist = rlist(Arrays.<RType> asList(rstring("L")));
        List stringList = (List) mapper.fromRType(jlist);
        Assert.assertTrue(stringList.contains("L"));
        RSet jset = rset(new HashSet<RType>(Arrays
                .<RType> asList(rstring("S"))));
        Set stringSet = (Set) mapper.fromRType(jset);
        Assert.assertTrue(stringSet.contains("S"));
    }

    @Test
    public void testMapsAreProperlyDispatched() throws Exception {
        IceMapper mapper = new IceMapper();
        Map m = new HashMap();
        m.put("a", rstring("a"));
        Map reversed = mapper.reverse(m);
        Assert.assertTrue(reversed.get("a").equals("a"));
    }

    @Test(groups = "ticket:737")
    public void testEventsAndTimes() throws Exception {
        Event e = new Event();
        Timestamp t = new Timestamp(System.currentTimeMillis());
        e.setTime(t);
        IceMapper mapper = new IceMapper();
        EventI ei = (EventI) mapper.map(e);
        Assert.assertNotNull(ei.getTime());
    }
}