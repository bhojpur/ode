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

import loci.common.DateTools;
import loci.common.services.ServiceFactory;
import loci.formats.FormatReader;
import loci.formats.IFormatReader;
import loci.formats.ImageReader;
import loci.formats.meta.IMetadata;
import loci.formats.services.ODEXMLService;

import ode.units.quantity.Length;
import ode.units.quantity.Time;
import ode.units.UNITS;

/**
 * Uses ODE-Formats to extract some basic standardized
 * (format-independent) metadata.
 */
public class GetPhysicalMetadata {

  /** Outputs dimensional information. */
  public static void printPixelDimensions(IFormatReader reader) {
    // output dimensional information
    int sizeX = reader.getSizeX();
    int sizeY = reader.getSizeY();
    int sizeZ = reader.getSizeZ();
    int sizeC = reader.getSizeC();
    int sizeT = reader.getSizeT();
    int imageCount = reader.getImageCount();
    System.out.println();
    System.out.println("Pixel dimensions:");
    System.out.println("\tWidth = " + sizeX);
    System.out.println("\tHeight = " + sizeY);
    System.out.println("\tFocal planes = " + sizeZ);
    System.out.println("\tChannels = " + sizeC);
    System.out.println("\tTimepoints = " + sizeT);
    System.out.println("\tTotal planes = " + imageCount);
  }

  /** Outputs global timing details. */
  public static void printPhysicalDimensions(IMetadata meta, int series) {
    Length physicalSizeX = meta.getPixelsPhysicalSizeX(series);
    Length physicalSizeY = meta.getPixelsPhysicalSizeY(series);
    Length physicalSizeZ = meta.getPixelsPhysicalSizeZ(series);
    Time timeIncrement = meta.getPixelsTimeIncrement(series);
    System.out.println();
    System.out.println("Physical dimensions:");
    System.out.println("\tX spacing = " +
      physicalSizeX.value() + " " + physicalSizeX.unit().getSymbol());
    System.out.println("\tY spacing = " +
      physicalSizeY.value() + " " + physicalSizeY.unit().getSymbol());
    System.out.println("\tZ spacing = " +
      physicalSizeZ.value() + " " + physicalSizeZ.unit().getSymbol());
    System.out.println("\tTime increment = " + timeIncrement.value(UNITS.SECOND).doubleValue() + " seconds");
  }

  public static void main(String[] args) throws Exception {
    // parse command line arguments
    if (args.length < 1) {
      System.err.println("Usage: java GetMetadata imageFile [seriesNo]");
      System.exit(1);
    }
    String id = args[0];
    int series = args.length > 1 ? Integer.parseInt(args[1]) : 0;

    // create ODE-XML metadata store
    ServiceFactory factory = new ServiceFactory();
    ODEXMLService service = factory.getInstance(ODEXMLService.class);
    IMetadata meta = service.createODEXMLMetadata();

    // create format reader
    IFormatReader reader = new ImageReader();
    reader.setMetadataStore(meta);

    // initialize file
    System.out.println("Initializing " + id);
    reader.setId(id);

    int seriesCount = reader.getSeriesCount();
    if (series < seriesCount) reader.setSeries(series);
    series = reader.getSeries();
    System.out.println("\tImage series = " + series + " of " + seriesCount);

    printPixelDimensions(reader);
    printPhysicalDimensions(meta, series);
  }

}
