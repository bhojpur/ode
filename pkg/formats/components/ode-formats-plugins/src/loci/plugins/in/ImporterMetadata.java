package loci.plugins.in;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;

import loci.formats.FormatTools;
import loci.formats.IFormatReader;
import loci.formats.MetadataTools;

/**
 * Helper class for storing original metadata key/value pairs.
 */
public class ImporterMetadata extends HashMap<String, Object> {

  // -- Constructor --

  public ImporterMetadata(IFormatReader r, ImportProcess process,
    boolean usePrefix)
  {
    // merge global metadata
    putAll(r.getGlobalMetadata());

    // merge location path
    put("Location", process.getCurrentFile());

    final ImporterOptions options = process.getOptions();
    final int oldSeries = r.getSeries();
    final int seriesCount = r.getSeriesCount();
    final int digits = digits(seriesCount);
    for (int i=0; i<seriesCount; i++) {
      if (!options.isSeriesOn(i)) continue;
      r.setSeries(i);

      // build prefix from image name and/or series number
      String s = "";
      if (usePrefix) {
        s = process.getODEMetadata().getImageName(i);
        if ((s == null || s.trim().length() == 0) && seriesCount > 1) {
          StringBuffer sb = new StringBuffer();
          sb.append("Series ");
          int zeroes = digits - digits(i + 1);
          for (int j=0; j<zeroes; j++) sb.append(0);
          sb.append(i + 1);
          sb.append(" ");
          s = sb.toString();
        }
        else s += ' ';
      }

      // merge series metadata
      Hashtable<String, Object> seriesMeta = r.getSeriesMetadata();
      MetadataTools.merge(seriesMeta, this, s);

      // merge core values
      final String pad = " "; // puts core values first when alphabetizing
      put(pad + s + "SizeX", new Integer(r.getSizeX()));
      put(pad + s + "SizeY", new Integer(r.getSizeY()));
      put(pad + s + "SizeZ", new Integer(r.getSizeZ()));
      put(pad + s + "SizeT", new Integer(r.getSizeT()));
      put(pad + s + "SizeC", new Integer(r.getSizeC()));
      put(pad + s + "IsRGB", new Boolean(r.isRGB()));
      put(pad + s + "PixelType",
        FormatTools.getPixelTypeString(r.getPixelType()));
      put(pad + s + "LittleEndian", new Boolean(r.isLittleEndian()));
      put(pad + s + "DimensionOrder", r.getDimensionOrder());
      put(pad + s + "IsInterleaved", new Boolean(r.isInterleaved()));
      put(pad + s + "BitsPerPixel", new Integer(r.getBitsPerPixel()));

      String seriesName = process.getODEMetadata().getImageName(i);
      put(pad + "Series " + i + " Name", seriesName);
    }
    r.setSeries(oldSeries);
  }

  /** Returns a string with each key/value pair on its own line. */
  public String getMetadataString(String separator) {
    ArrayList<String> keys = new ArrayList<String>(keySet());
    Collections.sort(keys);

    StringBuffer sb = new StringBuffer();
    for (String key : keys) {
      sb.append(key);
      sb.append(separator);
      sb.append(get(key));
      sb.append("\n");
    }
    return sb.toString();
  }

  // -- Object API methods --

  @Override
  public String toString() {
    return getMetadataString(" = ");
  }

  // -- Helper methods --

  /** Computes the given value's number of digits. */
  private static int digits(int value) {
    int digits = 0;
    while (value > 0) {
      value /= 10;
      digits++;
    }
    return digits;
  }

}
