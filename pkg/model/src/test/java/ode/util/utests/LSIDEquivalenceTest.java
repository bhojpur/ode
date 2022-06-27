package ode.util.utests;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ode.model.acquisition.DetectorSettings;
import ode.model.acquisition.FilterSet;
import ode.model.acquisition.ObjectiveSettings;
import ode.model.core.Image;
import ode.model.core.LogicalChannel;
import ode.model.screen.Plate;
import ode.util.LSID;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class LSIDEquivalenceTest
{
	public void testStringHashMapContainsKey()
	{
		Map<LSID, List<LSID>> map = new HashMap<LSID, List<LSID>>();
		LSID lsid1 = new LSID("Image:0"); 
		map.put(lsid1, new ArrayList<LSID>());
		LSID lsid2 = new LSID("Image:1");
		map.put(lsid2, new ArrayList<LSID>());
		Assert.assertEquals(2, map.size());
        Assert.assertTrue(map.containsKey(lsid1));
        Assert.assertTrue(map.containsKey(lsid2));
	}
	
	public void testLSIDHashMapContainsKey()
	{
		Map<LSID, String> map = new HashMap<LSID, String>();
		LSID lsid1 = new LSID(Image.class, 0); 
		map.put(lsid1, "Test");
		LSID lsid2 = new LSID(Image.class, 1);
		map.put(lsid2, "Test");
        Assert.assertEquals(2, map.size());
        Assert.assertTrue(map.containsKey(lsid1));
        Assert.assertTrue(map.containsKey(lsid2));
	}

	public void testHashMapContainsMultipleKeys()
	{
		Map<LSID, List<LSID>> map = new HashMap<LSID, List<LSID>>();
		LSID lsid1 = new LSID(FilterSet.class, 0, 0);
		map.put(lsid1, new ArrayList<LSID>());
		LSID lsid2 = new LSID(FilterSet.class, 0, 1);
		map.put(lsid2, new ArrayList<LSID>());
		LSID lsid3 = new LSID(FilterSet.class, 0, 2);
		map.put(lsid3, new ArrayList<LSID>());
		LSID lsid4 = new LSID(FilterSet.class, 0, 3);
		map.put(lsid4, new ArrayList<LSID>());
		LSID lsid5 = new LSID(DetectorSettings.class, 0, 0);
		map.put(lsid5, new ArrayList<LSID>());
		LSID lsid6 = new LSID(LogicalChannel.class, 0, 0);
		map.put(lsid6, new ArrayList<LSID>());
		LSID lsid7 = new LSID(Image.class, 0);
		map.put(lsid7, new ArrayList<LSID>());
		LSID lsid8 = new LSID(ObjectiveSettings.class, 0);
		map.put(lsid8, new ArrayList<LSID>());

        Assert.assertEquals(8, map.size());
        Assert.assertEquals(8, map.entrySet().size());
        Assert.assertTrue(map.containsKey(lsid1));
        Assert.assertTrue(map.containsKey(lsid2));
        Assert.assertTrue(map.containsKey(lsid3));
        Assert.assertTrue(map.containsKey(lsid4));
        Assert.assertTrue(map.containsKey(lsid5));
        Assert.assertTrue(map.containsKey(lsid6));
        Assert.assertTrue(map.containsKey(lsid7));
        Assert.assertTrue(map.containsKey(lsid8));
	}
	
    public void testStringVsStringNumeric()
    {
        LSID a = new LSID("Image:0");
        LSID b = new LSID("Image:0");
        Assert.assertEquals(a, b);
    }
    
    public void testStringVsStringAlpha()
    {
        LSID a = new LSID("Image:ABC");
        LSID b = new LSID("Image:ABC");
        Assert. assertEquals(a, b);
    }
    
    public void testStringVsStringMixed()
    {
        LSID a = new LSID("Image:ABC-1");
        LSID b = new LSID("Image:ABC-1");
        Assert.assertEquals(a, b);
    }
    
    public void testLSIDVsString()
    {
        LSID a = new LSID("ode.model.core.Image:0");
        LSID b = new LSID(Image.class, 0);
        Assert.assertEquals(a, b);
    }
    
    public void testStringVsLSID()
    {
        LSID a = new LSID(Image.class, 0);
        LSID b = new LSID("ode.model.core.Image:0");
        Assert.assertEquals(a, b);
    }
    
    public void testPlateStringVsLSID()
    {
        LSID a = new LSID(Plate.class, 0);
        LSID b = new LSID("ode.model.screen.Plate:0");
        Assert.assertEquals(a, b);
    }
    
    public void testLSIDVsStringWithConstructor()
    {
        LSID a = new LSID("ode.model.core.Image:0", true);
        LSID b = new LSID(Image.class, 0);
        Assert.assertEquals(a, b);
        Assert.assertEquals(a.getJavaClass(), b.getJavaClass());
        Assert.assertEquals(a.getIndexes()[0], b.getIndexes()[0]);
    }
    
    public void testStringVsLSIDWithConstructor()
    {
        LSID a = new LSID(Image.class, 0);
        LSID b = new LSID("ode.model.core.Image:0", true);
        Assert.assertEquals(a, b);
        Assert.assertEquals(a.getJavaClass(), b.getJavaClass());
        Assert.assertEquals(a.getIndexes()[0], b.getIndexes()[0]);
    }
    
    public void testPlateStringVsLSIDWithConstructor()
    {
        LSID a = new LSID(Plate.class, 0);
        LSID b = new LSID("ode.model.screen.Plate:0", true);
        Assert.assertEquals(a, b);
        Assert.assertEquals(a.getJavaClass(), b.getJavaClass());
        Assert.assertEquals(a.getIndexes()[0], b.getIndexes()[0]);
    }

    public void testBigListsWithSet()
    {
        Set<LSID> set = new HashSet<LSID>();
        for (int i = 0; i < 100000; i++)
        {
            set.add(new LSID(Plate.class, i));
        }
    }

    public void testBigListsWithSetAndList()
    {
        Set<LSID> found = new HashSet<LSID>();
        List<LSID> list = new ArrayList<LSID>();
        for (int i = 0; i < 100000; i++)
        {
            LSID lsid = new LSID(Plate.class, i);
            if (!found.contains(lsid)) {
                found.add(lsid);
                list.add(lsid);
            }
        }
    }

    public void testBigListsWithLinkedHashSet()
    {
        LinkedHashSet<LSID> set = new LinkedHashSet<LSID>();
        for (int i = 0; i < 100000; i++)
        {
            LSID lsid = new LSID(Plate.class, i);
            if (!set.contains(lsid)) {
                set.add(lsid);
            }
        }
    }
}