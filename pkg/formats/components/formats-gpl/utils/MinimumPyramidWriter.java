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

import java.util.Arrays;

import loci.common.services.ServiceFactory;
import loci.formats.*;
import loci.formats.ode.ODEPyramidStore;
import loci.formats.services.ODEXMLService;

import ode.xml.model.enums.DimensionOrder;
import ode.xml.model.enums.PixelType;
import ode.xml.model.primitives.PositiveInteger;

/**
 * Demonstrates the minimum amount of metadata
 * necessary to write out an image pyramid.
 */
public class MinimumPyramidWriter {

  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      System.out.println("Please specify an output file name.");
      System.exit(1);
    }
    String id = args[0];

    // create a blank pyramid

    int w = 4096, h = 4096, c = 1;
    int resolutions = 6;
    int pixelType = FormatTools.UINT16;
    int bpp = FormatTools.getBytesPerPixel(pixelType);
    byte[] img = new byte[w * h * c * bpp];

    // fill with random data
    for (int i=0; i<img.length; i++) img[i] = (byte) (256 * Math.random());

    // create metadata object with minimum required metadata fields
    System.out.println("Populating metadata...");

    ServiceFactory factory = new ServiceFactory();
    ODEXMLService service = factory.getInstance(ODEXMLService.class);
    ODEPyramidStore meta = (ODEPyramidStore) service.createODEXMLMetadata();

    MetadataTools.populateMetadata(meta, 0, null, false, "XYZCT",
      FormatTools.getPixelTypeString(pixelType), w, h, 1, c, 1, c);

    for (int i=1; i<resolutions; i++) {
      int scale = (int) Math.pow(2, i);
      meta.setResolutionSizeX(new PositiveInteger(w / scale), 0, i);
      meta.setResolutionSizeY(new PositiveInteger(h / scale), 0, i);
    }

    // write image plane to disk
    System.out.println("Writing image to '" + id + "'...");
    IFormatWriter writer = new ImageWriter();
    writer.setMetadataRetrieve(meta);
    writer.setId(id);
    writer.saveBytes(0, img);
    for (int i=1; i<resolutions; i++) {
      writer.setResolution(i);
      int x = meta.getResolutionSizeX(0, i).getValue();
      int y = meta.getResolutionSizeY(0, i).getValue();
      byte[] downsample = new byte[x * y * bpp * c];
      // don't use random data, so it's obvious that the correct resolution is read
      Arrays.fill(downsample, (byte) i);
      writer.saveBytes(0, downsample);
    }
    writer.close();

    System.out.println("Done.");
  }

}
