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
import loci.formats.*;
import loci.formats.meta.IMetadata;
import loci.formats.services.ODEXMLService;

import ode.xml.model.enums.DimensionOrder;
import ode.xml.model.enums.EnumerationException;
import ode.xml.model.enums.PixelType;
import ode.xml.model.primitives.PositiveInteger;

/**
 * Demonstrates writing multiple RGB image planes to a movie.
 */
public class WriteRGBMovie {

  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      System.out.println("Please specify an output file name.");
      System.exit(1);
    }
    String id = args[0];

    // create 20 blank 512x512 image planes
    System.out.println("Creating random image planes...");
    int w = 511, h = 507, numFrames = 20, numChannels = 3;
    int pixelType = FormatTools.UINT8;
    int bpp = FormatTools.getBytesPerPixel(pixelType);
    int planeSize = h * w * numChannels * bpp;
    byte[][] img = new byte[numFrames][planeSize];

    // fill with random data
    for (int t=0; t<numFrames; t++) {
      for (int i=0; i<img[t].length; i+=numChannels) {
        for (int c=0; c<numChannels; c++) {
          img[t][i + c] = (byte) (256 * Math.random());
        }
      }
    }

    // create metadata object with required metadata fields
    System.out.println("Populating metadata...");
    ServiceFactory factory = new ServiceFactory();
    ODEXMLService service = factory.getInstance(ODEXMLService.class);
    IMetadata meta = service.createODEXMLMetadata();

    MetadataTools.populateMetadata(meta, 0, null, false, "XYZCT",
      FormatTools.getPixelTypeString(pixelType), w, h, 1, numChannels,
      numFrames, numChannels);

    // write image planes to disk
    System.out.print("Writing planes to '" + id + "'");
    IFormatWriter writer = new ImageWriter();
    writer.setMetadataRetrieve(meta);
    writer.setId(id);
    for (int t=0; t<numFrames; t++) {
      System.out.print(".");
      writer.saveBytes(t, img[t]);
    }
    writer.close();

    System.out.println("Done.");
  }

}
