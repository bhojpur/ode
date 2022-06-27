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

import static org.testng.AssertJUnit.*;

import java.io.IOException;

import loci.formats.FormatException;
import loci.formats.IFormatReader;
import loci.formats.ImageReader;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Demonstration of the sub-resolution API.
 */
public class SubResolutionTest {

  private String id;
  private IFormatReader reader;

  @Parameters({"id"})
  @BeforeClass
  public void init(String id) throws FormatException, IOException {
    this.id = id;

    reader = new ImageReader();
    reader.setFlattenedResolutions(false);
    reader.setId(id);
  }

  @Test
  public void testSubResolutionCount() {
    int seriesCount = reader.getSeriesCount();

    assertTrue(seriesCount > 0);

    for (int series=0; series<seriesCount; series++) {
      reader.setSeries(series);

      int resolutionCount = reader.getResolutionCount();
      assertTrue(resolutionCount > 0);

      for (int resolution=0; resolution<resolutionCount; resolution++) {
        reader.setResolution(resolution);
        assertTrue(reader.getSizeX() > 0);
        assertTrue(reader.getSizeY() > 0);
      }
    }
  }

  @AfterClass
  public void cleanup() throws IOException {
    reader.close();
  }

}
