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

import static org.testng.AssertJUnit.*;

import java.io.File;
import java.io.IOException;

import loci.common.Location;
import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.FormatException;
import loci.formats.IFormatWriter;
import loci.formats.ImageReader;
import loci.formats.MetadataTools;
import loci.formats.meta.IMetadata;
import loci.formats.out.JPEG2000Writer;
import loci.formats.services.ODEXMLService;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test case which outlines the problems.
 */
public class LosslessJPEG2000Test {

  private static final byte[] PIXELS_8 = new byte[] {119};
  private static final byte[] PIXELS_16 = new byte[] {0, 119};

  private static final String FILE_8 = "8.jp2";
  private static final String FILE_16 = "16.jp2";

  @BeforeMethod
  public void setUp() throws Exception {
    File temp8 = File.createTempFile("test", ".jp2");
    File temp16 = File.createTempFile("test", ".jp2");
    temp8.deleteOnExit();
    temp16.deleteOnExit();
    Location.mapId(FILE_8, temp8.getAbsolutePath());
    Location.mapId(FILE_16, temp16.getAbsolutePath());

    IMetadata metadata8;

    try {
      ServiceFactory factory = new ServiceFactory();
      ODEXMLService service = factory.getInstance(ODEXMLService.class);
      metadata8 = service.createODEXMLMetadata();
    }
    catch (DependencyException exc) {
      throw new FormatException("Could not create ODE-XML store.", exc);
    }
    catch (ServiceException exc) {
      throw new FormatException("Could not create ODE-XML store.", exc);
    }

    MetadataTools.populateMetadata(metadata8, 0, "foo", false, "XYCZT",
      "uint8", 1, 1, 1, 1, 1, 1);
    IFormatWriter writer8 = new JPEG2000Writer();
    writer8.setMetadataRetrieve(metadata8);
    writer8.setId(FILE_8);
    writer8.saveBytes(0, PIXELS_8);
    writer8.close();

    IMetadata metadata16;

    try {
      ServiceFactory factory = new ServiceFactory();
      ODEXMLService service = factory.getInstance(ODEXMLService.class);
      metadata16 = service.createODEXMLMetadata();
    }
    catch (DependencyException exc) {
      throw new FormatException("Could not create ODE-XML store.", exc);
    }
    catch (ServiceException exc) {
      throw new FormatException("Could not create ODE-XML store.", exc);
    }

    MetadataTools.populateMetadata(metadata16, 0, "foo", false, "XYCZT",
      "uint16", 1, 1, 1, 1, 1, 1);
    IFormatWriter writer16 = new JPEG2000Writer();
    writer16.setMetadataRetrieve(metadata16);
    writer16.setId(FILE_16);
    writer16.saveBytes(0, PIXELS_16);
    writer16.close();
  }

  @Test
  public void testEquivalentPixels8Bit() throws Exception {
    ImageReader reader = new ImageReader();
    reader.setId(FILE_8);
    byte[] plane = reader.openBytes(0);
    assertEquals(plane.length, PIXELS_8.length);
    for (int q=0; q<plane.length; q++) {
      assertEquals(plane[q], PIXELS_8[q]);
    }
    reader.close();
  }

  @Test
  public void testEquivalentPixels16Bit() throws Exception {
    ImageReader reader = new ImageReader();
    reader.setId(FILE_16);
    byte[] plane = reader.openBytes(0);
    assertEquals(plane.length, PIXELS_16.length);
    for (int q=0; q<plane.length; q++) {
      assertEquals(plane[q], PIXELS_16[q]);
    }
    reader.close();
  }

}
