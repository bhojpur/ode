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

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.fail;

import java.io.IOException;
import java.security.MessageDigest;

import loci.common.DataTools;
import loci.formats.FormatException;
import loci.formats.in.DeltavisionReader;
import loci.formats.in.MetadataLevel;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DeltavisionMetadataConfigurableTest {

  private static final String ID = "/Users/shashi.rai/testimages/tinyTest.d3d.dv";

  private static final String ALL_ONLY_KEY = "Image Type";

  private static final String ALL_ONLY_VALUE = "normal";

  private DeltavisionReader pixelsOnly;

  private DeltavisionReader all;

  @BeforeClass
  public void setUp() {
    pixelsOnly = new DeltavisionReader();
    pixelsOnly.getMetadataOptions().setMetadataLevel(MetadataLevel.MINIMUM);
    all = new DeltavisionReader();
    all.getMetadataOptions().setMetadataLevel(MetadataLevel.ALL);
  }

  @Test
  public void testSetId() throws FormatException, IOException {
    long t0 = System.currentTimeMillis();
    pixelsOnly.setId(ID);
    assertEquals(MetadataLevel.MINIMUM,
                 pixelsOnly.getMetadataOptions().getMetadataLevel());
    assertNull(pixelsOnly.getSeriesMetadata().get(ALL_ONLY_KEY));
    long t1 = System.currentTimeMillis();
    all.setId(ID);
    assertEquals(MetadataLevel.ALL,
                 all.getMetadataOptions().getMetadataLevel());
    assertEquals(ALL_ONLY_VALUE, all.getGlobalMetadata().get(ALL_ONLY_KEY));
    long t2 = System.currentTimeMillis();
    System.err.println(String.format("Pixels only: %d -- All: %d",
        t1 - t0, t2 - t1));
  }

  @Test(dependsOnMethods={"testSetId"})
  public void testDimensions() {
    assertEquals(all.getSeriesCount(), pixelsOnly.getSeriesCount());
    assertEquals(all.getSizeX(), pixelsOnly.getSizeX());
    assertEquals(all.getSizeY(), pixelsOnly.getSizeY());
    assertEquals(all.getSizeZ(), pixelsOnly.getSizeZ());
    assertEquals(all.getSizeC(), pixelsOnly.getSizeC());
    assertEquals(all.getSizeT(), pixelsOnly.getSizeT());
    assertEquals(all.getPixelType(), pixelsOnly.getPixelType());
  }

  @Test(dependsOnMethods={"testSetId"})
  public void testPlaneData() throws FormatException, IOException {
    for (int i = 0; i < pixelsOnly.getSeriesCount(); i++) {
      pixelsOnly.setSeries(i);
      all.setSeries(i);
      assertEquals(all.getImageCount(), pixelsOnly.getImageCount());
      for (int j = 0; j < pixelsOnly.getImageCount(); j++) {
        byte[] pixelsOnlyPlane = pixelsOnly.openBytes(j);
        String sha1PixelsOnlyPlane = sha1(pixelsOnlyPlane);
        byte[] allPlane = all.openBytes(j);
        String sha1AllPlane = sha1(allPlane);
        if (!sha1PixelsOnlyPlane.equals(sha1AllPlane)) {
          fail(String.format(
              "MISMATCH: Series:%d Image:%d PixelsOnly:%s All:%s",
              i, j, sha1PixelsOnlyPlane, sha1AllPlane));
        }
      }
    }
  }

  private String sha1(byte[] buf) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-1"); 
      return DataTools.bytesToHex(md.digest(buf));
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
