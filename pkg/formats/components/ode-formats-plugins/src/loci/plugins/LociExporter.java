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

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

import java.util.HashSet;

import loci.common.DebugTools;
import loci.plugins.out.Exporter;
import loci.plugins.util.LibraryChecker;

/**
 * ImageJ plugin for writing files using the Bhojpur ODE-Formats package.
 * Wraps core logic in {@link loci.plugins.out.Exporter}, to avoid
 * direct references to classes in the external Bhojpur ODE-Formats library.
 */
public class LociExporter implements PlugInFilter {

  // -- Fields --

  /** Argument passed to setup method. */
  public String arg;

  private Exporter exporter;

  // -- PlugInFilter API methods --

  /** Sets up the writer. */
  @Override
  public int setup(String arg, ImagePlus imp) {
    this.arg = arg;
    exporter = new Exporter(this, imp);
    return DOES_ALL + NO_CHANGES;
  }

  /** Executes the plugin. */
  @Override
  public void run(ImageProcessor ip) {
    DebugTools.enableLogging("INFO");
    if (!LibraryChecker.checkJava() || !LibraryChecker.checkImageJ()) return;
    HashSet<String> missing = new HashSet<String>();
    LibraryChecker.checkLibrary(LibraryChecker.Library.BIO_FORMATS, missing);
    LibraryChecker.checkLibrary(LibraryChecker.Library.ODE_JAVA_XML, missing);
    if (!LibraryChecker.checkMissing(missing)) return;
    if (exporter != null) exporter.run();
  }

}
