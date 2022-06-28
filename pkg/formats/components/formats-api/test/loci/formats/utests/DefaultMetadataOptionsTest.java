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

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertEquals;

import loci.formats.in.MetadataOptions;
import loci.formats.in.DefaultMetadataOptions;
import loci.formats.in.MetadataLevel;


/**
 * Unit tests for {@link loci.formats.in.DefaultMetadataOptions}.
 */
public class DefaultMetadataOptionsTest {

  private MetadataOptions opt;

  @BeforeMethod
  public void setUp() {
    opt = new DefaultMetadataOptions();
  }

  @Test
  public void testMetadataLevel() {
    assertEquals(opt.getMetadataLevel(), MetadataLevel.ALL);
    for (MetadataLevel level: MetadataLevel.values()) {
      opt.setMetadataLevel(level);
      assertEquals(opt.getMetadataLevel(), level);
      assertEquals(
          (new DefaultMetadataOptions(level)).getMetadataLevel(), level
      );
    }
  }

  @Test
  public void testIsValidate() {
    assertFalse(opt.isValidate());
    opt.setValidate(true);
    assertTrue(opt.isValidate());
    opt.setValidate(false);
    assertFalse(opt.isValidate());
  }

}
