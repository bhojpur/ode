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
import static ode.rtypes.rdouble;
import static ode.rtypes.rfloat;
import static ode.rtypes.rint;
import static ode.rtypes.rinternal;
import static ode.rtypes.rlist;
import static ode.rtypes.rlong;
import static ode.rtypes.rmap;
import static ode.rtypes.robject;
import static ode.rtypes.rset;
import static ode.rtypes.rstring;
import static ode.rtypes.rtime;
import static ode.rtypes.rtype;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ode.ClientError;
import ode.RList;
import ode.RLong;
import ode.RMap;
import ode.RSet;
import ode.RString;
import ode.RType;
import ode.grid.JobParams;
import ode.model.ImageI;

import org.testng.annotations.Test;

public class RTypesTest{

    @Test
    public void testConversionMethod() {
        assertNull(rtype(null));
	assertEquals(rlong(1), rtype(rlong(1))); // Returns self
        assertEquals(rbool(true), rtype(Boolean.valueOf(true)));
        assertEquals(rdouble(0), rtype(Double.valueOf(0)));
        assertEquals(rfloat(0), rtype(Float.valueOf(0)));
        assertEquals(rlong(0), rtype(Long.valueOf(0)));
        assertEquals(rint(0), rtype(Integer.valueOf(0)));
        assertEquals(rstring("string"), rtype("string"));
        long time = System.currentTimeMillis();
        assertEquals(rtime(time), rtype(new Timestamp(time)));
        rtype(new ImageI());
        rtype(new JobParams());
        rtype(new HashSet(Arrays.asList(rlong(1))));
        rtype(Arrays.asList(rlong(2)));
        rtype(new HashMap());
        try {
            rtype(new RType[] {});
            fail("Shouldn't be able to handle this yet");
        } catch (ClientError ce) {
            // ok
        }
    }
    
    @Test
    public void testObjectCreationEqualsAndHash() {

        // RBool
        ode.RBool true1 = rbool(true);
        ode.RBool true2 = rbool(true);
        ode.RBool false1 = rbool(false);
        ode.RBool false2 = rbool(false);
        assertTrue(true1 == true2);
        assertTrue(false1 == false2);
        assertTrue(true1.getValue());
        assertTrue(!false1.getValue());
        assertTrue(true1.equals(true2));
        assertTrue(!true1.equals(false1));

        // RDouble
        ode.RDouble double_zero1 = rdouble(0.0);
        ode.RDouble double_zero2 = rdouble(0.0);
        ode.RDouble double_notzero1 = rdouble(1.1);
        ode.RDouble double_notzero1b = rdouble(1.1);
        ode.RDouble double_notzero2 = rdouble(2.2);
        assertTrue(double_zero1.getValue() == 0.0);
        assertTrue(double_notzero1.getValue() == 1.1);
        assertTrue(double_zero1.equals(double_zero2));
        assertTrue(!double_zero1.equals(double_notzero1));
        assertTrue(double_notzero1.equals(double_notzero1b));
        assertTrue(!double_notzero1.equals(double_notzero2));

        // RFloat
        ode.RFloat float_zero1 = rfloat(0.0f);
        ode.RFloat float_zero2 = rfloat(0.0f);
        ode.RFloat float_notzero1 = rfloat(1.1f);
        ode.RFloat float_notzero1b = rfloat(1.1f);
        ode.RFloat float_notzero2 = rfloat(2.2f);
        assertTrue(float_zero1.getValue() == 0.0);
        assertTrue(float_notzero1.getValue() == 1.1f);
        assertTrue(float_zero1.equals(float_zero2));
        assertTrue(!float_zero1.equals(float_notzero1));
        assertTrue(float_notzero1.equals(float_notzero1b));
        assertTrue(!float_notzero1.equals(float_notzero2));

        // RInt
        ode.RInt int_zero1 = rint(0);
        ode.RInt int_zero2 = rint(0);
        ode.RInt int_notzero1 = rint(1);
        ode.RInt int_notzero1b = rint(1);
        ode.RInt int_notzero2 = rint(2);
        assertTrue(int_zero1.getValue() == 0);
        assertTrue(int_notzero1.getValue() == 1);
        assertTrue(int_zero1.equals(int_zero2));
        assertTrue(!int_zero1.equals(int_notzero1));
        assertTrue(int_notzero1.equals(int_notzero1b));
        assertTrue(!int_notzero1.equals(int_notzero2));

        // RLong
        ode.RLong long_zero1 = rlong(0);
        ode.RLong long_zero2 = rlong(0);
        ode.RLong long_notzero1 = rlong(1);
        ode.RLong long_notzero1b = rlong(1);
        ode.RLong long_notzero2 = rlong(2);
        assertTrue(long_zero1.getValue() == 0);
        assertTrue(long_notzero1.getValue() == 1);
        assertTrue(long_zero1.equals(long_zero2));
        assertTrue(!long_zero1.equals(long_notzero1));
        assertTrue(long_notzero1.equals(long_notzero1b));
        assertTrue(!long_notzero1.equals(long_notzero2));

        // RTime
        ode.RTime time_zero1 = rtime(0);
        ode.RTime time_zero2 = rtime(0);
        ode.RTime time_notzero1 = rtime(1);
        ode.RTime time_notzero1b = rtime(1);
        ode.RTime time_notzero2 = rtime(2);
        assertTrue(time_zero1.getValue() == 0);
        assertTrue(time_notzero1.getValue() == 1);
        assertTrue(time_zero1.equals(time_zero2));
        assertTrue(!time_zero1.equals(time_notzero1));
        assertTrue(time_notzero1.equals(time_notzero1b));
        assertTrue(!time_notzero1.equals(time_notzero2));

        // RInternal
        ode.RInternal internal_null1 = rinternal(null);
        ode.RInternal internal_null2 = rinternal(null);
        ode.RInternal internal_notnull1 = rinternal(new ode.grid.JobParams());
        ode.RInternal internal_notnull2 = rinternal(new ode.grid.JobParams());
        assertTrue(internal_null1 == internal_null2);
        assertTrue(internal_null1.equals(internal_null2));
        assertTrue(!internal_null1.equals(internal_notnull2));
        assertTrue(internal_notnull1.equals(internal_notnull1));
        assertTrue(!internal_notnull1.equals(internal_notnull2));

        // RObject
        ode.RObject object_null1 = robject(null);
        ode.RObject object_null2 = robject(null);
        ode.RObject object_notnull1 = robject(new ode.model.ImageI());
        ode.RObject object_notnull2 = robject(new ode.model.ImageI());
        assertTrue(object_null1 == object_null2);
        assertTrue(object_null1.equals(object_null2));
        assertTrue(!object_null1.equals(object_notnull2));
        assertTrue(object_notnull1.equals(object_notnull1));
        assertTrue(!object_notnull1.equals(object_notnull2));

        // RString
        ode.RString string_null1 = rstring(null);
        ode.RString string_null2 = rstring(null);
        ode.RString string_notnull1 = rstring("str1");
        ode.RString string_notnull1b = rstring("str1");
        ode.RString string_notnull2 = rstring("str2");
        assertTrue(string_null1 == string_null2);
        assertTrue(string_null1.equals(string_null2));
        assertTrue(!string_null1.equals(string_notnull2));
        assertTrue(string_notnull1.equals(string_notnull1));
        assertTrue(!string_notnull1.equals(string_notnull2));
        assertTrue(string_notnull1.equals(string_notnull1b));

    }

    List<RType> ids = new ArrayList<RType>();
    {
        ids.add(rlong(1));
    }

    @Test
    public void testArrayCreationEqualsHash() {

        ode.RArray array_notnull1 = rarray(ids);
        ode.RArray array_notnull2 = rarray(ids);
        // Equals based on content
        assertTrue(!array_notnull1.equals(array_notnull2));
        // But content is copied!
        assertTrue(array_notnull1.getValue() != array_notnull2.getValue());

        ode.RArray array_null1 = rarray();
        ode.RArray array_null2 = rarray((RType[]) null);
        ode.RArray array_null3 = rarray((Collection<RType>) null);

        // All different since the contents are mutable.
        assertTrue(!array_null1.equals(array_notnull1));
        assertTrue(!array_null1.equals(array_null2));
        assertTrue(!array_null1.equals(array_null3));

    }

    @Test
    public void testListCreationEqualsHash() {

        ode.RList list_notnull1 = rlist(ids);
        ode.RList list_notnull2 = rlist(ids);
        // Equals based on content
        assertTrue(!list_notnull1.equals(list_notnull2));
        // But content is copied!
        assertTrue(list_notnull1.getValue() != list_notnull2.getValue());

        ode.RList list_null1 = rlist();
        ode.RList list_null2 = rlist((RType[]) null);
        ode.RList list_null3 = rlist((Collection<RType>) null);

        // All different since the contents are mutable.
        assertTrue(!list_null1.equals(list_notnull1));
        assertTrue(!list_null1.equals(list_null2));
        assertTrue(!list_null1.equals(list_null3));

    }

    @Test
    public void testSetCreationEqualsHash() {

        ode.RSet set_notnull1 = rset(ids);
        ode.RSet set_notnull2 = rset(ids);
        // Equals based on content
        assertTrue(!set_notnull1.equals(set_notnull2));
        // But content is copied!
        assertTrue(set_notnull1.getValue() != set_notnull2.getValue());

        ode.RSet set_null1 = rset();
        ode.RSet set_null2 = rset((RType[]) null);
        ode.RSet set_null3 = rset((Collection<RType>) null);

        // All different since the contents are mutable.
        assertTrue(!set_null1.equals(set_notnull1));
        assertTrue(!set_null1.equals(set_null2));
        assertTrue(!set_null1.equals(set_null3));

    }

    @Test
    public void testMapCreationEqualsHash() {

        RLong id = rlong(1L);
        ode.RMap map_notnull1 = rmap("ids", id);
        ode.RMap map_notnull2 = rmap("ids", id);
        // Equals based on content
        assertTrue(map_notnull1.equals(map_notnull2)); // TODO different with
                                                        // maps
        // But content is copied!
        assertTrue(map_notnull1.getValue() != map_notnull2.getValue());

        ode.RMap map_null1 = rmap();
        ode.RMap map_null2 = rmap((Map<String, RType>) null);

        // All different since the contents are mutable.
        assertTrue(!map_null1.equals(map_notnull1));
        assertTrue(map_null1.equals(map_null2)); // TODO Different with maps

    }

    @Test
    public void testWrapRListString() {
        List<String> input = Arrays.asList("A", "B");
        RList rlist = (RList) ode.rtypes.wrap(input);
        assertEquals(2, rlist.getValue().size());
        assertEquals("A", ((RString) rlist.getValue().get(0)).getValue());
        assertEquals("B", ((RString) rlist.getValue().get(1)).getValue());
    }
 
    @SuppressWarnings("unchecked")
    @Test
    public void testWrapUnwrapMap() {
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("int", new Integer(0));
        input.put("float", new Float(0.0f));
        RMap output = (RMap) ode.rtypes.wrap(input);
        Map<String, Object> test = (Map<String, Object>) ode.rtypes.unwrap(output);
        assertEquals(new Integer(0), test.get("int"));
        assertEquals(new Float(0.0f), test.get("float"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMoreComplexeWrapping() {
        // Create a bunch of wacky structures
        Map<String, List<Map<String, Set<Object>>>> wow
            = new HashMap<String, List<Map<String, Set<Object>>>>();
        List<Map<String, Set<Object>>> list = new ArrayList<Map<String, Set<Object>>>();
        Map<String, Set<Object>> map1 = new HashMap<String, Set<Object>>();
        Set<Object> set = new HashSet<Object>();

        // Now put everything in everything
        set.add(new Integer(3));
        set.add(new Double(0.0d));
        set.add(new Timestamp(0l));
        set.add(new Long(0L));
        set.add(new String("string"));
        /*
        Due to the key semantics of hash sets, placing these in the set
        leads to stackoverflow semantics. A IdentityHashSet is needed.
        set.add(map1);
        set.add(list);
        set.add(wow);
        */
        set.add(new Object[]{ list, map1, set});
        
        map1.put("set", set);
        map1.put("set2", set);

        list.add(map1);
        wow.put("list", list);

        RMap mapOut = (RMap) ode.rtypes.wrap(wow);
        RList listOut = (RList) mapOut.getValue().get("list");
        RMap map1Out = (RMap) listOut.getValue().get(0);
        RSet setOut = (RSet) map1Out.getValue().get("set");
        set = (Set<Object>) ode.rtypes.unwrap(setOut);

        wow = (Map<String, List<Map<String, Set<Object>>>>) ode.rtypes.unwrap(mapOut);
        list = wow.get("list");
        map1 = list.get(0);
        set = map1.get("set");
        assertTrue(set.contains(new Integer(3)));
    }
}