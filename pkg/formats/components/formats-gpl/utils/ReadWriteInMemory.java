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

import java.io.*;
import loci.common.ByteArrayHandle;
import loci.common.Location;
import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.*;
import loci.formats.meta.IMetadata;
import loci.formats.services.ODEXMLService;

/**
 * Tests the ODE-Formats I/O logic to and from byte arrays in memory. 
 */
@Deprecated
public class ReadWriteInMemory {

  public static void main(String[] args)
    throws DependencyException, FormatException, IOException, ServiceException
  {
    if (args.length < 1) {
      System.out.println("Please specify a (small) image file.");
      System.exit(1);
    }
    String path = args[0];

    /* file-read-start */
    // read in entire file
    System.out.println("Reading file into memory from disk...");
    File inputFile = new File(path);
    int fileSize = (int) inputFile.length();
    DataInputStream in = new DataInputStream(new FileInputStream(inputFile));
    byte[] inBytes = new byte[fileSize];
    in.readFully(inBytes);
    System.out.println(fileSize + " bytes read.");
    /* file-read-end */

    /* mapping-start */
    // determine input file suffix
    String fileName = inputFile.getName();
    int dot = fileName.lastIndexOf(".");
    String suffix = dot < 0 ? "" : fileName.substring(dot);

    // map input id string to input byte array
    String inId = "inBytes" + suffix;
    Location.mapFile(inId, new ByteArrayHandle(inBytes));
    /* mapping-end */

    // read data from byte array using ImageReader
    System.out.println();
    System.out.println("Reading image data from memory...");

    /* read-start */
    ServiceFactory factory = new ServiceFactory();
    ODEXMLService service = factory.getInstance(ODEXMLService.class);
    IMetadata odeMeta = service.createODEXMLMetadata();

    ImageReader reader = new ImageReader();
    reader.setMetadataStore(odeMeta);
    reader.setId(inId);
    /* read-end */

    int seriesCount = reader.getSeriesCount();
    int imageCount = reader.getImageCount();
    int sizeX = reader.getSizeX();
    int sizeY = reader.getSizeY();
    int sizeZ = reader.getSizeZ();
    int sizeC = reader.getSizeC();
    int sizeT = reader.getSizeT();

    // output some details
    System.out.println("Series count: " + seriesCount);
    System.out.println("First series:");
    System.out.println("\tImage count = " + imageCount);
    System.out.println("\tSizeX = " + sizeX);
    System.out.println("\tSizeY = " + sizeY);
    System.out.println("\tSizeZ = " + sizeZ);
    System.out.println("\tSizeC = " + sizeC);
    System.out.println("\tSizeT = " + sizeT);

    /* out—mapping-start */
    // map output id string to output byte array
    String outId = fileName + ".ode.tif";
    ByteArrayHandle outputFile = new ByteArrayHandle();
    Location.mapFile(outId, outputFile);
    /* out—mapping-end */

    /* write—init-start */
    // write data to byte array using ImageWriter
    System.out.println();
    System.out.print("Writing planes to destination in memory: ");
    ImageWriter writer = new ImageWriter();
    writer.setMetadataRetrieve(odeMeta);
    writer.setId(outId);
    /* write—init-end */

    /* write-start */
    byte[] plane = null;
    for (int i=0; i<imageCount; i++) {
      if (plane == null) {
        // allow reader to allocate a new byte array
        plane = reader.openBytes(i);
      }
      else {
        // reuse previously allocated byte array
        reader.openBytes(i, plane);
      }
      writer.saveBytes(i, plane);
      System.out.print(".");
    }
    reader.close();
    writer.close();
    System.out.println();

    byte[] outBytes = outputFile.getBytes();
    outputFile.close();
    /* write-end */

    /* flush-start */
    // flush output byte array to disk
    System.out.println();
    System.out.println("Flushing image data to disk...");
    File outFile = new File(fileName + ".ode.tif");
    DataOutputStream out = new DataOutputStream(new FileOutputStream(outFile));
    out.write(outBytes);
    out.close();
    /* flush-end */
  }

}
