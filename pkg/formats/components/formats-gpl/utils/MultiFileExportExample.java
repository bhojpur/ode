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

import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.ImageReader;
import loci.formats.ImageWriter;
import loci.formats.MetadataTools;
import loci.formats.meta.IMetadata;
import loci.formats.services.ODEXMLService;

/**
 * Writes each Z section in a dataset to a separate file.
 */
public class MultiFileExportExample {
  public static void main(String[] args) throws FormatException, IOException {
    if (args.length < 2) {
      System.out.println(
        "Usage: java MultiFileExportExample <infile> <output file extension>");
      System.exit(1);
    }

    ImageReader reader = new ImageReader();
    IMetadata metadata;

    try {
      ServiceFactory factory = new ServiceFactory();
      ODEXMLService service = factory.getInstance(ODEXMLService.class);
      metadata = service.createODEXMLMetadata();
    }
    catch (DependencyException exc) {
      throw new FormatException("Could not create ODE-XML store.", exc);
    }
    catch (ServiceException exc) {
      throw new FormatException("Could not create ODE-XML store.", exc);
    }

    reader.setMetadataStore(metadata);
    reader.setId(args[0]);

    ImageWriter writer = new ImageWriter();
    writer.setMetadataRetrieve(metadata);
    String baseFile = args[0].substring(0, args[0].lastIndexOf("."));
    writer.setId(baseFile + "_s0_z0" + args[1]);

    for (int series=0; series<reader.getSeriesCount(); series++) {
      reader.setSeries(series);
      writer.setSeries(series);

      int planesPerFile = reader.getImageCount() / reader.getSizeZ();
      for (int z=0; z<reader.getSizeZ(); z++) {
        String file = baseFile + "_s" + series + "_z" + z + args[1];
        writer.changeOutputFile(file);
        for (int image=0; image<planesPerFile; image++) {
          int zct[] = FormatTools.getZCTCoords(reader.getDimensionOrder(),
            1, reader.getEffectiveSizeC(), reader.getSizeT(),
            planesPerFile, image);
          int index = FormatTools.getIndex(reader, z, zct[1], zct[2]);
          writer.saveBytes(image, reader.openBytes(index));
        }
      }
    }

    reader.close();
    writer.close();
  }
}
