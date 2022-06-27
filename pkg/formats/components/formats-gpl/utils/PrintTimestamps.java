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

import ode.xml.model.primitives.NonNegativeInteger;
import ode.units.quantity.Time;
import ode.units.UNITS;

/**
 * Uses ODE-Formats to extract timestamp information
 * in a format-independent manner from a dataset.
 */
public class PrintTimestamps {

  /** Outputs dimensional information. */
  public static void printDimensions(IFormatReader reader) {
    // output dimensional information
    int sizeX = reader.getSizeX();
    int sizeY = reader.getSizeY();
    int sizeZ = reader.getSizeZ();
    int sizeC = reader.getSizeC();
    int sizeT = reader.getSizeT();
    int imageCount = reader.getImageCount();
    System.out.println();
    System.out.println("Image dimensions:");
    System.out.println("\tWidth = " + sizeX);
    System.out.println("\tHeight = " + sizeY);
    System.out.println("\tFocal planes = " + sizeZ);
    System.out.println("\tChannels = " + sizeC);
    System.out.println("\tTimepoints = " + sizeT);
    System.out.println("\tTotal planes = " + imageCount);
  }

  /** Outputs global timing details. */
  public static void printGlobalTiming(IMetadata meta, int series) {
    String imageName = meta.getImageName(series);
    String creationDate = null;
    if (meta.getImageAcquisitionDate(series) != null) {
      creationDate = meta.getImageAcquisitionDate(series).getValue();
    }
    Time timeInc = meta.getPixelsTimeIncrement(series);
    System.out.println();
    System.out.println("Global timing information:");
    System.out.println("\tImage name = " + imageName);
    System.out.println("\tCreation date = " + creationDate);
    if (creationDate != null) {
      System.out.println("\tCreation time (in ms since epoch) = " +
        DateTools.getTime(creationDate, DateTools.ISO8601_FORMAT));
    }
    System.out.println("\tTime increment (in seconds) = " + timeInc.value(UNITS.SECOND).doubleValue());
  }

  /** Outputs timing details per timepoint. */
  public static void printTimingPerTimepoint(IMetadata meta, int series) {
    System.out.println();
    System.out.println(
      "Timing information per timepoint (from beginning of experiment):");
    int planeCount = meta.getPlaneCount(series);
    for (int i = 0; i < planeCount; i++) {
      Time deltaT = meta.getPlaneDeltaT(series, i);
      if (deltaT == null) continue;
      // convert plane ZCT coordinates into image plane index
      int z = meta.getPlaneTheZ(series, i).getValue().intValue();
      int c = meta.getPlaneTheC(series, i).getValue().intValue();
      int t = meta.getPlaneTheT(series, i).getValue().intValue();
      if (z == 0 && c == 0) {
        System.out.println("\tTimepoint #" + t + " = " + deltaT.value(UNITS.SECOND).doubleValue() + " s");
      }
    }
  }

  /**
   * Outputs timing details per plane.
   *
   * This information may seem redundant or unnecessary, but it is possible
   * that two image planes recorded at the same timepoint actually have
   * slightly different timestamps. Thus, ODE allows for recording a separate
   * timestamp for every individual image plane.
   */
  public static void printTimingPerPlane(IMetadata meta, int series) {
    System.out.println();
    System.out.println(
      "Timing information per plane (from beginning of experiment):");
    int planeCount = meta.getPlaneCount(series);
    for (int i = 0; i < planeCount; i++) {
      Time deltaT = meta.getPlaneDeltaT(series, i);
      if (deltaT == null) continue;
      // convert plane ZCT coordinates into image plane index
      int z = meta.getPlaneTheZ(series, i).getValue().intValue();
      int c = meta.getPlaneTheC(series, i).getValue().intValue();
      int t = meta.getPlaneTheT(series, i).getValue().intValue();
      System.out.println("\tZ " + z + ", C " + c + ", T " + t + " = " +
        deltaT.value(UNITS.SECOND).doubleValue() + " s");
    }
  }

  public static void main(String[] args) throws Exception {
    // parse command line arguments
    if (args.length < 1) {
      System.err.println("Usage: java PrintTimestamps imageFile [seriesNo]");
      System.exit(1);
    }
    String id = args[0];
    int series = args.length > 1 ? Integer.parseInt(args[1]) : 0;

    // enable debugging
    //FormatReader.debug = true;

    // create ODE-XML metadata store of the latest schema version
    ServiceFactory factory = new ServiceFactory();
    ODEXMLService service = factory.getInstance(ODEXMLService.class);
    IMetadata meta = service.createODEXMLMetadata();
    // or if you want a specific schema version, you can use:
    //IMetadata meta = service.createODEXMLMetadata(null, "2009-02");
    //meta.createRoot();

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

    printDimensions(reader);
    printGlobalTiming(meta, series);
    printTimingPerTimepoint(meta, series);
    printTimingPerPlane(meta, series);
  }

}
