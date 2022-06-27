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
import loci.formats.IFormatReader;
import loci.formats.ImageReader;
import loci.formats.meta.IMetadata;
import loci.formats.services.ODEXMLService;

/**
 * Uses ODE-Formats to extract lens numerical aperture
 * in a format-independent manner from a dataset.
 */
public class PrintLensNA {

  public static void main(String[] args)
    throws DependencyException, FormatException, IOException, ServiceException
  {
    // parse command line arguments
    if (args.length < 1) {
      System.err.println("Usage: java PrintLensNA imageFile");
      System.exit(1);
    }
    String id = args[0];

    // configure reader
    IFormatReader reader = new ImageReader();
    ServiceFactory factory = new ServiceFactory();
    ODEXMLService service = factory.getInstance(ODEXMLService.class);
    IMetadata meta = service.createODEXMLMetadata();
    reader.setMetadataStore(meta);
    System.out.println("Initializing file: " + id);
    reader.setId(id); // parse metadata

    // output metadata values
    int instrumentCount = meta.getInstrumentCount();
    System.out.println("There are " + instrumentCount +
      " instrument(s) associated with this file");
    for (int i=0; i<instrumentCount; i++) {
      int objectiveCount = meta.getObjectiveCount(i);
      System.out.println();
      System.out.println("Instrument #" + i +
        " [" + meta.getInstrumentID(i) + "]: " +
        objectiveCount + " objective(s) found");
      for (int o=0; o<objectiveCount; o++) {
        Double lensNA = meta.getObjectiveLensNA(i, o);
        System.out.println("\tObjective #" + o +
          " [" + meta.getObjectiveID(i, o) + "]: LensNA=" + lensNA);
      }
    }
  }

}
