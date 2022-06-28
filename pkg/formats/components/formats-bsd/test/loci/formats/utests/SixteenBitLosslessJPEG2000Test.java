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

import java.util.ArrayList;

import loci.common.ByteArrayHandle;
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
public class SixteenBitLosslessJPEG2000Test {

  private static final Logger LOGGER =
    LoggerFactory.getLogger(SixteenBitLosslessJPEG2000Test.class);

  private final int increment;

  public SixteenBitLosslessJPEG2000Test() {
    this(1024);
  }

  public SixteenBitLosslessJPEG2000Test(int increment) {
      this.increment = increment;
  }

  @Test
  public void testLosslessPixels() throws Exception {
    int failureCount = 0;
    for (int v=Short.MIN_VALUE; v<Short.MAX_VALUE; v+=increment) {
      int index = v + Short.MAX_VALUE + 1;
      byte[] pixels = DataTools.shortToBytes((short) v, false);

      String file = index + ".jp2";
      ByteArrayHandle tmpFile = new ByteArrayHandle(1);
      Location.mapFile(file, tmpFile);

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
      writer16.setId(file);
      writer16.saveBytes(0, pixels);
      writer16.close();

      byte[] buf = tmpFile.getBytes();
      byte[] realData = new byte[(int) tmpFile.length()];
      System.arraycopy(buf, 0, realData, 0, realData.length);
      tmpFile.close();
      tmpFile = new ByteArrayHandle(realData);
      Location.mapFile(file, tmpFile);

      ImageReader reader = new ImageReader();
      reader.setId(file);
      byte[] plane = reader.openBytes(0);
      for (int q=0; q<plane.length; q++) {
        if (plane[q] != pixels[q]) {
          LOGGER.debug("FAILED on {}",
            DataTools.bytesToShort(pixels, false));
          failureCount++;
          break;
        }
      }
      reader.close();
      tmpFile.close();

      Location.mapFile(file, null);
    }
    assertEquals(failureCount, 0);
  }

}
