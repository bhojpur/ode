package loci.plugins;

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

import ij.IJ;
import ij.ImagePlus;

import java.io.IOException;

import loci.formats.FormatException;
import loci.plugins.in.DisplayHandler;
import loci.plugins.in.ImagePlusReader;
import loci.plugins.in.ImportProcess;
import loci.plugins.in.ImporterOptions;
import loci.plugins.in.ImporterPrompter;

/**
 * Miscellaneous plugins utility methods.
 */
public final class BF {

  // -- Constructor --

  private BF() { }

  // -- Utility methods --

  public static void debug(String msg) {
    if (IJ.debugMode) IJ.log("ODE-Formats: " + msg);
  }

  public static void status(boolean quiet, String msg) {
    if (quiet) return;
    IJ.showStatus(msg);
  }

  public static void warn(boolean quiet, String msg) {
    if (quiet) return;
    IJ.log("Warning: " + msg);
  }

  public static void progress(boolean quiet, int value, int max) {
    if (quiet) return;
    IJ.showProgress(value, max);
  }

  public static ImagePlus[] openImagePlus(String path)
    throws FormatException, IOException
  {
    ImporterOptions options = new ImporterOptions();
    options.setId(path);
    return openImagePlus(options);
  }

  public static ImagePlus[] openThumbImagePlus(String path)
    throws FormatException, IOException
  {
    ImporterOptions options = new ImporterOptions();
    options.setId(path);
    return openThumbImagePlus(options);
  }

  public static ImagePlus[] openImagePlus(ImporterOptions options)
    throws FormatException, IOException
  {
    ImportProcess process = new ImportProcess(options);
    if (!process.execute()) return null;
    DisplayHandler displayHandler = new DisplayHandler(process);
    if (options != null && options.isShowODEXML()) {
      displayHandler.displayODEXML();
    }
    ImagePlusReader reader = new ImagePlusReader(process);
    ImagePlus[] imps = reader.openImagePlus();
    if (options != null && options.showROIs()) {
      displayHandler.displayROIs(imps);
    }
    if (!options.isVirtual()) {
      process.getReader().close();
    }
    return imps;
  }

  public static ImagePlus[] openThumbImagePlus(ImporterOptions options)
    throws FormatException, IOException
  {
    options.setQuiet(true); // NB: Only needed due to ImporterPrompter.
    options.setWindowless(true); // NB: Only needed due to ImporterPrompter.

    ImportProcess process = new ImportProcess(options);

    new ImporterPrompter(process); // NB: Could eliminate this (see above).

    if (!process.execute()) return null;
    ImagePlusReader reader = new ImagePlusReader(process);
    ImagePlus[] imps = reader.openThumbImagePlus();
    if (!options.isVirtual()) {
      process.getReader().close();
    }
    return imps;
  }

}
