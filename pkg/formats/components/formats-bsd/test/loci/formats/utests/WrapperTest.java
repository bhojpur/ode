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
import static org.testng.AssertJUnit.assertNotNull;

import java.io.IOException;
import java.util.List;

import loci.common.Location;
import loci.formats.ChannelFiller;
import loci.formats.ChannelMerger;
import loci.formats.ChannelSeparator;
import loci.formats.CoreMetadata;
import loci.formats.DimensionSwapper;
import loci.formats.FileStitcher;
import loci.formats.FormatException;
import loci.formats.IFormatReader;
import loci.formats.ImageReader;
import loci.formats.Memoizer;
import loci.formats.MinMaxCalculator;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 */
public class WrapperTest {

  private static final String TEST_FILE =
    "test&pixelType=uint8&sizeX=128&sizeY=64&sizeC=2&sizeZ=4&sizeT=5&series=3.fake";

  @DataProvider(name = "wrappers")
  public Object[][] createWrappers() {
    Location.mapId(TEST_FILE, TEST_FILE);
    Object[][] wrappers = new Object[][] {
      {new ChannelFiller()},
      {new ChannelMerger()},
      {new ChannelSeparator()},
      {new DimensionSwapper()},
      {new FileStitcher()},
      {new ImageReader()},
      {new MinMaxCalculator()},
      {new Memoizer()}
    };
    for (int i=0; i<wrappers.length; i++) {
      IFormatReader reader = (IFormatReader) wrappers[i][0];
      try {
        reader.setId(TEST_FILE);
      }
      catch (FormatException e) { e.printStackTrace(); }
      catch (IOException e) { e.printStackTrace(); }
    }
    return wrappers;
  }

  @Test(dataProvider = "wrappers")
  public void testCoreMetadata(IFormatReader reader) {
    assertNotNull(reader.getCurrentFile());
    List<CoreMetadata> coreList = reader.getCoreMetadataList();
    assertEquals(coreList.size(), reader.getSeriesCount());
    for (int i=0; i<reader.getSeriesCount(); i++) {
      CoreMetadata core = coreList.get(i);
      reader.setSeries(i);
      assertEquals(core.sizeX, reader.getSizeX());
      assertEquals(core.sizeY, reader.getSizeY());
      assertEquals(core.sizeZ, reader.getSizeZ());
      assertEquals(core.sizeC, reader.getSizeC());
      assertEquals(core.sizeT, reader.getSizeT());
      assertEquals(core.pixelType, reader.getPixelType());
      assertEquals(core.imageCount, reader.getImageCount());
      assertEquals(core.dimensionOrder, reader.getDimensionOrder());
      assertEquals(core.littleEndian, reader.isLittleEndian());
      assertEquals(core.rgb, reader.isRGB());
      assertEquals(core.interleaved, reader.isInterleaved());
      assertEquals(core.indexed, reader.isIndexed());
    }
  }
}
