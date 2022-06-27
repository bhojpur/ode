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

import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.FormatException;
import loci.formats.ImageReader;
import loci.formats.ImageWriter;
import loci.formats.MetadataTools;
import loci.formats.meta.IMetadata;
import loci.formats.services.ODEXMLService;

/**
 */
public class TiledExportExample {
  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.out.println("Usage: java TiledExportExample <infile> <outfile>");
      System.exit(1);
    }

    ImageReader reader = new ImageReader();
    ImageWriter writer = new ImageWriter();
    IMetadata meta;

    try {
      ServiceFactory factory = new ServiceFactory();
      ODEXMLService service = factory.getInstance(ODEXMLService.class);
      meta = service.createODEXMLMetadata();
    }
    catch (DependencyException exc) {
      throw new FormatException("Could not create ODE-XML store.", exc);
    }
    catch (ServiceException exc) {
      throw new FormatException("Could not create ODE-XML store.", exc);
    }

    reader.setMetadataStore(meta);

    reader.setId(args[0]);
    writer.setMetadataRetrieve(meta);
    writer.setId(args[1]);

    for (int series=0; series<reader.getSeriesCount(); series++) {
      reader.setSeries(series);
      writer.setSeries(series);

      for (int image=0; image<reader.getImageCount(); image++) {
        for (int row=0; row<2; row++) {
          for (int col=0; col<2; col++) {
            int w = reader.getSizeX() / 2;
            int h = reader.getSizeY() / 2;
            int x = col * w;
            int y = row * h;
            /* debug */ System.out.println("[" + x + ", " + y + ", " + w + ", " + h + "]");
            byte[] buf = reader.openBytes(image, x, y, w, h);
            writer.saveBytes(image, buf, x, y, w, h);
          }
        }
      }
    }

    reader.close();
    writer.close();
  }
}
