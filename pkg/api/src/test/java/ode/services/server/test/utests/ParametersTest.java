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

import static ode.rtypes.rbool;
import static ode.rtypes.rint;
import static ode.rtypes.rlong;
import static ode.rtypes.rstring;
import static ode.rtypes.rtime;

import java.util.Arrays;

import ode.RList;
import ode.sys.ParametersI;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ParametersTest {

    ParametersI p;

    @BeforeMethod
    public void setup() {
        p = new ParametersI();
    }

    //
    // Copied from PojoOptionsTest
    //

    @Test
    public void testBasics() throws Exception {
        p.exp(rlong(1));
        p.grp(rlong(1));
        p.endTime(rtime(1));
    }

    @Test
    public void testDefaults() throws Exception {
        // Removed to prevent confusion. assertFalse(p.isLeaves());
        Assert.assertFalse(p.isGroup());
        Assert.assertFalse(p.isExperimenter());
        Assert.assertFalse(p.isEndTime());
        Assert.assertFalse(p.isStartTime());
        Assert.assertFalse(p.isPagination());
    }

    @Test
    public void testExperimenter() throws Exception {
        p.exp(rlong(1));
        Assert.assertTrue(p.isExperimenter());
        Assert.assertEquals(p.getExperimenter().getValue(), 1L);
        p.allExps();
        Assert.assertFalse(p.isExperimenter());
    }

    @Test
    public void testGroup() throws Exception {
        p.grp(rlong(1));
        Assert.assertTrue(p.isGroup());
        Assert.assertEquals(p.getGroup().getValue(), 1L);
        p.allGrps();
        Assert.assertFalse(p.isGroup());
    }

    //
    // Parameters.theFilter.limit, offset
    //

    @Test
    public void testFilter() throws Exception {
        p.noPage();
        Assert.assertNull(p.theFilter);
        p.page(2, 3);
        Assert.assertEquals(rint(2), p.theFilter.offset);
        Assert.assertEquals(rint(3), p.theFilter.limit);
        p.noPage();
        Assert.assertNull(p.theFilter.offset);
        Assert.assertNull(p.getOffset());
        Assert.assertNull(p.theFilter.limit);
        Assert.assertNull(p.getLimit());
    }

    //
    // Parameters.theFilter.ownerId, groupId
    //

    @Test
    public void testOwnerId() throws Exception {
        Assert.assertNull(p.theFilter);
        p.exp(rlong(1));
        Assert.assertNotNull(p.theFilter);
        Assert.assertNotNull(p.theFilter.ownerId);
        Assert.assertEquals(rlong(1), p.getExperimenter());
        Assert.assertNull(p.allExps().getExperimenter());
        Assert.assertNotNull(p.theFilter);
    }

    @Test
    public void testGroupId() throws Exception {
        Assert.assertNull(p.theFilter);
        p.grp(rlong(1));
        Assert.assertNotNull(p.theFilter);
        Assert.assertNotNull(p.theFilter.groupId);
        Assert.assertEquals(rlong(1), p.getGroup());
        Assert.assertNull(p.allGrps().getGroup());
        Assert.assertNotNull(p.theFilter);
    }

    //
    // Parameters.theFilter.startTime, endTime
    //

    @Test
    public void testTimes() throws Exception {
        Assert.assertNull(p.theFilter);
        p.startTime(rtime(0));
        Assert.assertNotNull(p.theFilter);
        Assert.assertNotNull(p.theFilter.startTime);
        p.endTime(rtime(1));
        Assert.assertNotNull(p.theFilter.endTime);
        p.allTimes();
        Assert.assertNotNull(p.theFilter);
        Assert.assertNull(p.theFilter.startTime);
        Assert.assertNull(p.theFilter.endTime);
    }

    //
    // Parameters.theOptions
    //

    @Test
    public void testOptionsAcquisitionData() throws Exception {
        Assert.assertNull(p.getAcquisitionData());
        Assert.assertEquals(rbool(true), p.acquisitionData().getAcquisitionData());
        Assert.assertEquals(rbool(false), p.noAcquisitionData().getAcquisitionData());
        Assert.assertNotNull(p.getAcquisitionData());
    }

    @Test
    public void testOptionsOrphan() throws Exception {
        Assert.assertNull(p.getOrphan());
        Assert.assertEquals(rbool(true), p.orphan().getOrphan());
        Assert.assertEquals(rbool(false), p.noOrphan().getOrphan());
        Assert.assertNotNull(p.getOrphan());
    }

    @Test
    public void testOptionsUnique() throws Exception {
        Assert.assertNull(p.getLeaves());
        Assert.assertEquals(rbool(true), p.leaves().getLeaves());
        Assert.assertEquals(rbool(false), p.noLeaves().getLeaves());
        Assert.assertNotNull(p.getLeaves());
    }

    //
    // Parameters.map
    //

    @Test
    public void testAddBasicString() throws Exception {
        p.add("string", rstring("a"));
        Assert.assertEquals(rstring("a"), p.map.get("string"));
    }

    @Test
    public void testAddBasicInt() throws Exception {
        p.add("int", rint(1));
        Assert.assertEquals(rint(1), p.map.get("int"));
    }

    @Test
    public void testAddIdRaw() throws Exception {
        p.addId(1);
        Assert.assertEquals(rlong(1), p.map.get("id"));
    }

    @Test
    public void testAddIdRType() throws Exception {
        p.addId(rlong(1));
        Assert.assertEquals(rlong(1), p.map.get("id"));
    }

    @Test
    public void testAddLongRaw() throws Exception {
        p.addLong("long", 1L);
        Assert.assertEquals(rlong(1), p.map.get("long"));
    }

    @Test
    public void testAddLongRType() throws Exception {
        p.addLong("long", rlong(1L));
        Assert.assertEquals(rlong(1), p.map.get("long"));
    }

    @Test
    public void testAddIds() throws Exception {
        p.addIds(Arrays.asList(1L, 2L));
        RList list = (RList) p.map.get("ids");
        Assert.assertTrue(list.getValue().contains(rlong(1)));
        Assert.assertTrue(list.getValue().contains(rlong(2)));
    }

    @Test
    public void testAddLongs() throws Exception {
        p.addLongs("longs", Arrays.asList(1L, 2L));
        RList list = (RList) p.map.get("longs");
        Assert.assertTrue(list.getValue().contains(rlong(1)));
        Assert.assertTrue(list.getValue().contains(rlong(2)));
    }

}