package ode.util.utests;

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