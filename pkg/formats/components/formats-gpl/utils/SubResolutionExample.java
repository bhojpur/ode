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

import loci.formats.FormatException;
import loci.formats.IFormatReader;
import loci.formats.ImageReader;

/**
 * Demonstration of the sub-resolution API.
 */
public class SubResolutionExample {

  public static void main(String[] args) throws FormatException, IOException {
    // parse command line arguments
    if (args.length < 1) {
      System.err.println("Usage: java SubResolutionExample imageFile");
      System.exit(1);
    }
    String id = args[0];

    // configure reader
    IFormatReader reader = new ImageReader();
    reader.setFlattenedResolutions(false);
    System.out.println("Initializing file: " + id);
    reader.setId(id); // parse metadata

    int seriesCount = reader.getSeriesCount();

    System.out.println("  Series count = " + seriesCount);

    for (int series=0; series<seriesCount; series++) {
      reader.setSeries(series);
      int resolutionCount = reader.getResolutionCount();

      System.out.println("    Resolution count for series #" + series +
        " = " + resolutionCount);

      for (int r=0; r<resolutionCount; r++) {
        reader.setResolution(r);
        System.out.println("      Resolution #" + r + " dimensions = " +
          reader.getSizeX() + " x " + reader.getSizeY());
      }
    }

    reader.close();
  }

}
