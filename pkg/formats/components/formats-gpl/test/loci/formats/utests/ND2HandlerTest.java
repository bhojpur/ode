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

import java.util.List;
import java.util.ArrayList;

import loci.formats.CoreMetadata;
import loci.formats.CoreMetadataList;
import loci.formats.in.ND2Handler;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit tests for {@link ND2Handler}.
 */
public class ND2HandlerTest {

  private CoreMetadataList coreList;
  private ND2Handler handler;

  @BeforeMethod
  public void setUp() {
    coreList = new CoreMetadataList();
    coreList.add(new CoreMetadata());
    handler = new ND2Handler(coreList, 1);
  }

  @DataProvider(name = "pixelsSizeKey")
  public Object[][] createPixelsSizeKey() {
    return new Object[][] {
      {"dZStep", ".1", .1},
      {"- Step .1 ", "", .1},
      {"- Step .1", "", .1},
      {"- Step ,1 ", "", .1},
      {"- Step", "", 0.0},
      {"- Step ", "", 0.0},
      {"- Step d", "", 0.0},
    };
  }
  
  @Test(dataProvider="pixelsSizeKey")
  public void testParsePixelsSizeZ(String key, String value, double pixelSizeZ)
  {
    handler.parseKeyAndValue(key, value, "");
    assertEquals(handler.getPixelSizeZ(), pixelSizeZ);
  }
}
