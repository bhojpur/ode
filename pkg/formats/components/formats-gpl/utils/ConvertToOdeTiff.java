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

import loci.common.services.ServiceFactory;
import loci.formats.ImageReader;
import loci.formats.meta.IMetadata;
import loci.formats.services.ODEXMLService;
import loci.formats.out.ODETiffWriter;

/**
 * Converts the given files to ODE-TIFF format.
 */
public class ConvertToOdeTiff {

  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      System.out.println("Usage: java ConvertToOdeTiff file1 file2 ...");
      return;
    }
    ImageReader reader = new ImageReader();
    ODETiffWriter writer = new ODETiffWriter();
    for (int i=0; i<args.length; i++) {
      String id = args[i];
      int dot = id.lastIndexOf(".");
      String outId = (dot >= 0 ? id.substring(0, dot) : id) + ".ode.tif";
      System.out.print("Converting " + id + " to " + outId + " ");

      // record metadata to ODE-XML format
      ServiceFactory factory = new ServiceFactory();
      ODEXMLService service = factory.getInstance(ODEXMLService.class);
      IMetadata odexmlMeta = service.createODEXMLMetadata();
      reader.setMetadataStore(odexmlMeta);
      reader.setId(id);

      // configure ODE-TIFF writer
      writer.setMetadataRetrieve(odexmlMeta);
      writer.setId(outId);
      //writer.setCompression("J2K");

      // write out image planes
      int seriesCount = reader.getSeriesCount();
      for (int s=0; s<seriesCount; s++) {
        reader.setSeries(s);
        writer.setSeries(s);
        int planeCount = reader.getImageCount();
        for (int p=0; p<planeCount; p++) {
          byte[] plane = reader.openBytes(p);
          // write plane to output file
          writer.saveBytes(p, plane);
          System.out.print(".");
        }
      }
      writer.close();
      reader.close();
      System.out.println(" [done]");
    }
  }

}
