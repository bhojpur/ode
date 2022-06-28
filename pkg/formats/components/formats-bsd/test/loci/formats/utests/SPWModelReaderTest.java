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
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SPWModelReaderTest {

  private SPWModelMock mock;
  
  private SPWModelMock mockWithNoLightSources;

  private File temporaryFile;

  private File temporaryFileWithNoLightSources;
  
  private IFormatReader reader;

  private IFormatReader readerWithNoLightSources;

  private IMetadata metadata;

  private IMetadata metadataWithNoLightSources;

  @BeforeClass
  public void setUp() throws Exception {
    mock = new SPWModelMock(true);
    mockWithNoLightSources = new SPWModelMock(false);
    temporaryFile = File.createTempFile(this.getClass().getName(), ".ode");
    temporaryFileWithNoLightSources = 
      File.createTempFile(this.getClass().getName(), ".ode");
    writeMockToFile(mock, temporaryFile, true);
    writeMockToFile(mockWithNoLightSources, temporaryFileWithNoLightSources,
                    true);
  }

  /**
   * Writes a model mock to a file as XML.
   * @param mock Mock to build a DOM tree of and serialize to XML.
   * @param file File to write serialized XML to.
   * @param withBinData Whether or not to do BinData post processing.
   * @throws Exception If there is an error writing the XML to the file.
   */
  public static void writeMockToFile(ModelMock mock, File file,
  boolean withBinData) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder parser = factory.newDocumentBuilder();
    Document document = parser.newDocument();
    // Produce a valid ODE DOM element hierarchy
    Element root = mock.getRoot().asXMLElement(document);
    SPWModelMock.postProcess(root, document, withBinData);
    // Write the ODE DOM to the requested file
    OutputStream stream = new FileOutputStream(file);
    stream.write(SPWModelMock.asString(document).getBytes());
  }

  @AfterClass
  public void tearDown() throws Exception {
    temporaryFile.delete();
    temporaryFileWithNoLightSources.delete();
  }

  @Test
  public void testSetId() throws Exception {
    reader = new MinMaxCalculator(new ChannelSeparator(
        new ChannelFiller(new ImageReader())));
    metadata = new ODEXMLMetadataImpl();
    reader.setMetadataStore(metadata);
    reader.setId(temporaryFile.getAbsolutePath());
  }

  @Test
  public void testSetIdWithNoLightSources() throws Exception {
    readerWithNoLightSources = new MinMaxCalculator(new ChannelSeparator(
        new ChannelFiller(new ImageReader())));
    metadataWithNoLightSources = new ODEXMLMetadataImpl();
    readerWithNoLightSources.setMetadataStore(metadataWithNoLightSources);
    readerWithNoLightSources.setId(
      temporaryFileWithNoLightSources.getAbsolutePath());
  }

  @Test(dependsOnMethods={"testSetId"})
  public void testSeriesCount() {
    assertEquals(384, reader.getSeriesCount());
  }

  @Test(dependsOnMethods={"testSetId"})
  public void testCanReadEveryPlane() throws Exception {
    assertTrue(canReadEveryPlane(reader));
  }

  @Test(dependsOnMethods={"testSetIdWithNoLightSources"})
  public void testCanReadEveryPlaneWithNoLightSources() throws Exception {
    assertTrue(canReadEveryPlane(readerWithNoLightSources));
  }

  /**
   * Checks to see if every plane of an initialized reader can be read.
   * @param reader Reader to read all planes from.
   * @return <code>true</code> if all planes can be read, <code>false</code>
   * otherwise.
   * @throws Exception If there is an error reading data.
   */
  public static boolean canReadEveryPlane(IFormatReader reader)
  throws Exception {
    int sizeX = reader.getSizeX();
    int sizeY = reader.getSizeY();
    int pixelType = reader.getPixelType();
    int bytesPerPixel = getBytesPerPixel(pixelType);
    byte[] buf = new byte[sizeX * sizeY * bytesPerPixel];
    for (int i = 0; i < reader.getSeriesCount(); i++)
    {
      reader.setSeries(i);
      for (int j = 0; j < reader.getImageCount(); j++)
      {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(
                    "Required SHA-1 message digest algorithm unavailable.");
        }
        buf = reader.openBytes(j, buf);
        try {
          md.update(buf);
        } catch (Exception e) {
          // This better not happen. :)
          throw new RuntimeException(e);
        }
      }
    }
    return true;
  }

  @Test(dependsOnMethods={"testSetId"})
  public void testHasLightSources() {
    assertEquals(1, metadata.getInstrumentCount());
    assertEquals(5, metadata.getLightSourceCount(0));
  }

  @Test(dependsOnMethods={"testSetIdWithNoLightSources"})
  public void testHasNoLightSources() {
    assertEquals(1, metadataWithNoLightSources.getInstrumentCount());
    assertEquals(0, metadataWithNoLightSources.getLightSourceCount(0));
  }

  /**
   * Retrieves how many bytes per pixel the current plane or section has.
   * @return the number of bytes per pixel.
   */
  public static int getBytesPerPixel(int type) {
    switch(type) {
    case 0:
    case 1:
      return 1;  // INT8 or UINT8
    case 2:
    case 3:
      return 2;  // INT16 or UINT16
    case 4:
    case 5:
    case 6:
      return 4;  // INT32, UINT32 or FLOAT
    case 7:
      return 8;  // DOUBLE
    }
    throw new RuntimeException("Unknown type with id: '" + type + "'");
  }

}
