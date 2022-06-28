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
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;

import loci.formats.ChannelFiller;
import loci.formats.ChannelSeparator;
import loci.formats.IFormatReader;
import loci.formats.ImageReader;
import loci.formats.MinMaxCalculator;
import loci.formats.meta.IMetadata;
import loci.formats.ode.ODEXMLMetadataImpl;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class BaseModelReaderTest {

  private BaseModelMock mock;
  
  private File temporaryFile;

  private IFormatReader reader;

  private IMetadata metadata;

  @BeforeClass
  public void setUp() throws Exception {
    mock = new BaseModelMock();
    temporaryFile = File.createTempFile(this.getClass().getName(), ".ode");
    SPWModelReaderTest.writeMockToFile(mock, temporaryFile, true);
  }

  @AfterClass
  public void tearDown() throws Exception {
    temporaryFile.delete();
  }

  @Test
  public void testSetId() throws Exception {
    reader = new MinMaxCalculator(new ChannelSeparator(
        new ChannelFiller(new ImageReader())));
    metadata = new ODEXMLMetadataImpl();
    reader.setMetadataStore(metadata);
    reader.setId(temporaryFile.getAbsolutePath());
  }

  @Test(dependsOnMethods={"testSetId"})
  public void testSeriesCount() {
    assertEquals(1, reader.getSeriesCount());
  }

  @Test(dependsOnMethods={"testSetId"})
  public void testCanReadEveryPlane() throws Exception {
    assertTrue(SPWModelReaderTest.canReadEveryPlane(reader));
  }

}
