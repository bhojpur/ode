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

import java.io.IOException;

import loci.common.Location;
import loci.formats.DimensionSwapper;
import loci.formats.FormatException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Verifies the input and output manipulations of DimensionSwapper are
 * functioning as intended, specifically with respect to updating 
 * dimension sizes and orders.
 */
public class DimensionSwapperTest {
  
  private static final int SIZE_C = 2;
  private static final int SIZE_T = 5;
  private static final int SIZE_Z = 4;
  private static final String NEW_ORDER = "XYCTZ";
  private static final String OUTPUT_ORDER = "XYZCT";
  private static final String TEST_FILE =
      "test&pixelType=uint8&sizeX=128&sizeY=64&sizeC="+SIZE_C+"&sizeZ="+SIZE_Z+"&sizeT="+SIZE_T+"&series=3.fake";
  
  @DataProvider(name = "swapper")
  public Object[][] createDimSwapper() {
    Location.mapId(TEST_FILE, TEST_FILE);
    
    DimensionSwapper swapper = new DimensionSwapper();
    
    try {
      swapper.setId(TEST_FILE);
    } catch (FormatException e) {  e.printStackTrace(); }
    catch (IOException e) { e.printStackTrace(); }
    
    swapper.setOutputOrder(OUTPUT_ORDER);
    
    return new Object[][]{{swapper}};
  }
  
  /**
   * Tests the results of setting the output order.
   */
  @Test(dataProvider="swapper")
  public void testOutputOrdering(DimensionSwapper swapper) {
    // set output order
    swapper.setOutputOrder(NEW_ORDER);
    
    // output order should be updated
    assertEquals(swapper.getDimensionOrder().equals(NEW_ORDER), true);
    
    // dimension sizes should be unchanged
    assertEquals(swapper.getSizeZ(), SIZE_Z);
    assertEquals(swapper.getSizeC(), SIZE_C);
    assertEquals(swapper.getSizeT(), SIZE_T);
  }
  
  /**
   * Tests the results of setting the input order.
   */
  @Test(dataProvider="swapper")
  public void testInputOrdering(DimensionSwapper swapper) {
    // set input (storage) order
    swapper.swapDimensions(NEW_ORDER);
    
    // output order should be unchanged
    assertEquals(swapper.getDimensionOrder().equals(OUTPUT_ORDER), true);
    
    // dimension sizes should be updated
    assertEquals(swapper.getSizeZ(), SIZE_T);
    assertEquals(swapper.getSizeC(), SIZE_Z);
    assertEquals(swapper.getSizeT(), SIZE_C);
    
  }
  
  /**
   * Tests the interactions of setting output and input orders.
   */
  @Test(dataProvider="swapper")
  public void testInputOutputOrdering(DimensionSwapper swapper) {
    
    swapper.setOutputOrder(NEW_ORDER);
    swapper.swapDimensions(NEW_ORDER);
    
    // output order should be updated
    assertEquals(swapper.getDimensionOrder().equals(NEW_ORDER), true);

    // dimension sizes should be updated
    assertEquals(swapper.getSizeZ(), SIZE_T);
    assertEquals(swapper.getSizeC(), SIZE_Z);
    assertEquals(swapper.getSizeT(), SIZE_C);
  }

}
