package loci.tests.testng;

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
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.fail;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;

import loci.common.DataTools;
import loci.common.services.ServiceFactory;
import loci.formats.FormatException;
import loci.formats.ImageReader;
import loci.formats.MetadataTools;
import loci.formats.in.DefaultMetadataOptions;
import loci.formats.in.MetadataLevel;
import loci.formats.meta.IMetadata;
import loci.formats.services.ODEXMLService;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.SkipException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static loci.tests.testng.TestTools.getProperty;

/**
 */
public class MetadataConfigurableTest {

  private static final Logger LOGGER =
    LoggerFactory.getLogger(MetadataConfigurableTest.class);
  private static final String FILENAME_PROPERTY = "testng.filename";
  private static final String SKIP_MESSAGE = "No image file specified";

  private ImageReader pixelsOnly;
  private ImageReader all;
  private ImageReader noOverlays;
  private String id;

  @BeforeClass
  public void setUp() {
    pixelsOnly = new ImageReader();
    pixelsOnly.setMetadataOptions(
      new DefaultMetadataOptions(MetadataLevel.MINIMUM));
    all = new ImageReader();
    all.setMetadataOptions(new DefaultMetadataOptions(MetadataLevel.ALL));
    noOverlays = new ImageReader();
    noOverlays.setMetadataOptions(
      new DefaultMetadataOptions(MetadataLevel.NO_OVERLAYS));
    id = getProperty(FILENAME_PROPERTY);
    if (null == id) {
      LOGGER.error(SKIP_MESSAGE);
      throw new SkipException(SKIP_MESSAGE);
    }
  }

  @Test
  public void testSetId() throws FormatException, IOException {
    long t0 = System.currentTimeMillis();
    pixelsOnly.setId(id);
    assertEquals(MetadataLevel.MINIMUM,
      pixelsOnly.getMetadataOptions().getMetadataLevel());

    long t1 = System.currentTimeMillis();
    all.setId(id);
    assertEquals(MetadataLevel.ALL,
      all.getMetadataOptions().getMetadataLevel());
    assertFalse(0 ==
      all.getSeriesMetadata().size() + all.getGlobalMetadata().size());

    long t2 = System.currentTimeMillis();
    System.err.println(String.format("Pixels only: %d -- All: %d",
      t1 - t0, t2 - t1));

    IMetadata metadata = null;
    try {
      ServiceFactory factory = new ServiceFactory();
      ODEXMLService service = factory.getInstance(ODEXMLService.class);
      metadata = service.createODEXMLMetadata();
      noOverlays.setMetadataStore(metadata);
    } catch (Exception e) {
      throw new FormatException("Cannot initialize ODEXML metadata store");
    }
  
    noOverlays.setId(id);
    assertEquals(MetadataLevel.NO_OVERLAYS,
      noOverlays.getMetadataOptions().getMetadataLevel());
    assertEquals(metadata.getROICount(), 0);
  }

  @Test(dependsOnMethods={"testSetId"})
  public void testDimensions() {
    assertEquals(all.getSeriesCount(), pixelsOnly.getSeriesCount());
    assertEquals(all.getSeriesCount(), noOverlays.getSeriesCount());
    for (int i=0; i<pixelsOnly.getSeriesCount(); i++) {
      all.setSeries(i);
      pixelsOnly.setSeries(i);
      noOverlays.setSeries(i);

      assertEquals(all.getSizeX(), pixelsOnly.getSizeX());
      assertEquals(all.getSizeY(), pixelsOnly.getSizeY());
      assertEquals(all.getSizeZ(), pixelsOnly.getSizeZ());
      assertEquals(all.getSizeC(), pixelsOnly.getSizeC());
      assertEquals(all.getSizeT(), pixelsOnly.getSizeT());
      assertEquals(all.getPixelType(), pixelsOnly.getPixelType());
      assertEquals(all.isLittleEndian(), pixelsOnly.isLittleEndian());
      assertEquals(all.isIndexed(), pixelsOnly.isIndexed());

      assertEquals(all.getSizeX(), noOverlays.getSizeX());
      assertEquals(all.getSizeY(), noOverlays.getSizeY());
      assertEquals(all.getSizeZ(), noOverlays.getSizeZ());
      assertEquals(all.getSizeC(), noOverlays.getSizeC());
      assertEquals(all.getSizeT(), noOverlays.getSizeT());
      assertEquals(all.getPixelType(), noOverlays.getPixelType());
      assertEquals(all.isLittleEndian(), noOverlays.isLittleEndian());
      assertEquals(all.isIndexed(), noOverlays.isIndexed());
    }
  }

  @Test(dependsOnMethods={"testSetId"})
  public void testPlaneData() throws FormatException, IOException {
    for (int i=0; i<pixelsOnly.getSeriesCount(); i++) {
      pixelsOnly.setSeries(i);
      all.setSeries(i);
      noOverlays.setSeries(i);
      assertEquals(all.getImageCount(), pixelsOnly.getImageCount());
      assertEquals(all.getImageCount(), noOverlays.getImageCount());
      for (int j=0; j<pixelsOnly.getImageCount(); j++) {
        byte[] pixelsOnlyPlane = pixelsOnly.openBytes(j);
        String pixelsOnlySHA1 = sha1(pixelsOnlyPlane);
        byte[] allPlane = all.openBytes(j);
        String allSHA1 = sha1(allPlane);
        byte[] noOverlaysPlane = noOverlays.openBytes(j);
        String noOverlaysSHA1 = sha1(noOverlaysPlane);

        if (!pixelsOnlySHA1.equals(allSHA1)) {
          fail(String.format("MISMATCH: Series:%d Image:%d PixelsOnly%s All:%s",
            i, j, pixelsOnlySHA1, allSHA1));
        }
        if (!noOverlaysSHA1.equals(allSHA1)) {
          fail(String.format("MISMATCH: Series:%d Image:%d PixelsOnly%s All:%s",
            i, j, noOverlaysSHA1, allSHA1));
        }
      }
    }
  }

  @Test(dependsOnMethods={"testSetId"})
  public void testUsedFiles() throws FormatException, IOException {
    for (int i=0; i<pixelsOnly.getSeriesCount(); i++) {
      pixelsOnly.setSeries(i);
      all.setSeries(i);
      noOverlays.setSeries(i);

      String[] pixelsOnlyFiles = pixelsOnly.getSeriesUsedFiles();
      String[] allFiles = all.getSeriesUsedFiles();
      String[] noOverlaysFiles = noOverlays.getSeriesUsedFiles();

      assertEquals(allFiles.length, pixelsOnlyFiles.length);
      assertEquals(allFiles.length, noOverlaysFiles.length);

      Arrays.sort(allFiles);
      Arrays.sort(pixelsOnlyFiles);
      Arrays.sort(noOverlaysFiles);

      for (int j=0; j<pixelsOnlyFiles.length; j++) {
        assertEquals(allFiles[j], pixelsOnlyFiles[j]);
        assertEquals(allFiles[j], noOverlaysFiles[j]);
      }
    }
  }

  // -- Utility methods --

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
