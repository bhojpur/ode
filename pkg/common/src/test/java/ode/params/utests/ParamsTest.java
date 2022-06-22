package ode.params.utests;

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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ode.parameters.Filter;
import ode.parameters.Options;
import ode.parameters.Parameters;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ParamsTest {

    @Test
    public void test_ParamsWithFilter() throws Exception {
        Parameters p = new Parameters(new Filter().unique());
        Assert.assertTrue(p.isUnique());
    }

    @Test
    public void test_ParamsWithCopy() throws Exception {
        Parameters p = new Parameters();
        p.addBoolean("TEST", Boolean.TRUE);
        Parameters p2 = new Parameters(p);
        Assert.assertTrue(p2.keySet().contains("TEST"));
    }

    @Test
    public void test_toString() throws Exception {
        String display = "";
        Parameters p = new Parameters();
        display = p.toString();
        p.addBoolean("T", Boolean.TRUE);
        display = p.toString();
        p.setFilter(new Filter());
        display = p.toString();
        p.setFilter(new Filter().group(1).page(0, 1));
        display = p.toString();
        Options o = new Options();
        o.acquisitionData = false;
        p.setOptions(o);
        display = p.toString();
        o.orphan = true;
        o.leaves = true;
        display = p.toString();
        p.addLong("id", 1l);
        display = p.toString();
        Map<String, Object> mym = new HashMap<String, Object>();
        mym.put("a", 1);
        p.addMap("mym", mym);
        display = p.toString();
        p.addList("list", Arrays.asList(1L, 2L));
        display = p.toString();
    }
}