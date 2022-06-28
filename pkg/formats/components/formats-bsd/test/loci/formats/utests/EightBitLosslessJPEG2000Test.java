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
import java.util.ArrayList;

import loci.common.DataTools;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test case which outlines the problems.
 */
public class EightBitLosslessJPEG2000Test {

  private static final Logger LOGGER =
    LoggerFactory.getLogger(EightBitLosslessJPEG2000Test.class);

  private ArrayList<String> files = new ArrayList<String>();
  private byte[][] pixels = new byte[256][1];

  @BeforeMethod
  public void setUp() throws Exception {
    for (byte v=Byte.MIN_VALUE; v<Byte.MAX_VALUE; v++) {
      int index = v + Byte.MAX_VALUE + 1;
      pixels[index][0] = v;

      String file = index + ".jp2";
      File tempFile = File.createTempFile("test", ".jp2");
      tempFile.deleteOnExit();
      Location.mapId(file, tempFile.getAbsolutePath());
      files.add(file);

      IMetadata metadata;

      try {
        ServiceFactory factory = new ServiceFactory();
        ODEXMLService service = factory.getInstance(ODEXMLService.class);
        metadata = service.createODEXMLMetadata();
      }
      catch (DependencyException exc) {
        throw new FormatException("Could not create ODE-XML store.", exc);
      }
      catch (ServiceException exc) {
        throw new FormatException("Could not create ODE-XML store.", exc);
      }

      MetadataTools.populateMetadata(metadata, 0, "foo", false, "XYCZT",
        "uint8", 1, 1, 1, 1, 1, 1);
      IFormatWriter writer = new JPEG2000Writer();
      writer.setMetadataRetrieve(metadata);
      writer.setId(file);
      writer.saveBytes(0, pixels[index]);
      writer.close();
    }
  }

  @Test
  public void testLosslessPixels() throws Exception {
    int failureCount = 0;
    for (int i=0; i<files.size(); i++) {
      ImageReader reader = new ImageReader();
      reader.setId(files.get(i));
      byte[] plane = reader.openBytes(0);
      if (plane[0] != pixels[i][0]) {
        LOGGER.debug("FAILED on {}", pixels[i][0]);
        failureCount++;
      }
      reader.close();
    }
    assertEquals(failureCount, 0);
  }

}
