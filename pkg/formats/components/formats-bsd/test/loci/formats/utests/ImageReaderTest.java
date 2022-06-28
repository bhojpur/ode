package loci.formats.utests;

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

import loci.formats.ImageReader;
import loci.formats.in.MetadataOptions;
import loci.formats.in.DynamicMetadataOptions;
import loci.formats.in.MetadataLevel;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


public class ImageReaderTest {

  public static final String KEY = "test.option";
  public static final String VALUE = "foo";

  @DataProvider(name = "levels")
  public Object[][] createLevels() {
    return new Object[][] {
      {MetadataLevel.MINIMUM},
      {MetadataLevel.NO_OVERLAYS},
      {MetadataLevel.ALL}
    };
  }

  @Test
  public void testOptionsExplicit() throws Exception {
    DynamicMetadataOptions opt = new DynamicMetadataOptions();
    opt.set(KEY, VALUE);
    ImageReader reader = new ImageReader();
    reader.setMetadataOptions(opt);
    reader.setId("test.fake");
    MetadataOptions rOpt = reader.getReader().getMetadataOptions();
    assertTrue(rOpt instanceof DynamicMetadataOptions);
    String v = ((DynamicMetadataOptions) rOpt).get(KEY);
    assertNotNull(v);
    assertEquals(v, VALUE);
    reader.close();
  }

  @Test(dataProvider = "levels")
  public void testOptionsImplicit(MetadataLevel level) throws Exception {
    ImageReader reader = new ImageReader();
    reader.getMetadataOptions().setMetadataLevel(level);
    reader.setId("test.fake");
    MetadataLevel rLevel =
      reader.getReader().getMetadataOptions().getMetadataLevel();
    assertEquals(rLevel, level);
    reader.close();
  }

}
