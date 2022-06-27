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

import ode.model.core.Image;
import ode.util.LSID;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class LSIDParseTest
{
    public void testParseJavaClass()
    {
    	LSID a = new LSID("ode.model.core.Image:0");
    	Assert.assertEquals(a.parseJavaClass(), Image.class);
    }

    public void testParseNoIndex()
    {
    	LSID a = new LSID("ode.model.core.Image");
		Assert.assertEquals(0, a.parseIndexes().length);
    }
    
    public void testParseSingleIndex()
    {
    	LSID a = new LSID("ode.model.core.Image:0");
    	int[] indexes = a.parseIndexes();
		Assert.assertEquals(1, indexes.length);
		Assert.assertEquals(0, indexes[0]);
    }

    public void testParseDoubleIndex()
    {
    	LSID a = new LSID("ode.model.core.Image:0:0");
    	int[] indexes = a.parseIndexes();
		Assert.assertEquals(2, indexes.length);
		Assert.assertEquals(0, indexes[0]);
		Assert.assertEquals(0, indexes[1]);
    }
    
    public void testParseBigSingleIndex()
    {
    	LSID a = new LSID("ode.model.core.Image:667667");
    	int[] indexes = a.parseIndexes();
		Assert.assertEquals(1, indexes.length);
		Assert.assertEquals(667667, indexes[0]);
    }
    
    public void testParseBigDoubleIndex()
    {
    	LSID a = new LSID("ode.model.core.Image:667667:766766");
    	int[] indexes = a.parseIndexes();
		Assert.assertEquals(2, indexes.length);
		Assert.assertEquals(667667, indexes[0]);
		Assert.assertEquals(766766, indexes[1]);
    }
}