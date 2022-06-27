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

import ij.plugin.PlugIn;

import java.util.HashSet;

import loci.common.DebugTools;
import loci.plugins.in.Importer;
import loci.plugins.util.LibraryChecker;

/**
 * ImageJ plugin for reading files using the Bhojpur ODE-Formats package.
 * Wraps core logic in {@link loci.plugins.in.Importer}, to avoid
 * direct references to classes in the external ODE-Formats library.
 */
public class LociImporter implements PlugIn {

  // -- Fields --

  /**
   * Flag indicating whether last operation was successful.
   * NB: This field must be updated properly, or the plugin
   * will stop working correctly with HandleExtraFileTypes.
   */
  public boolean success;

  /**
   * Flag indicating whether last operation was canceled.
   * NB: This field must be updated properly, or the plugin
   * will stop working correctly with HandleExtraFileTypes.
   */
  public boolean canceled;

  // -- PlugIn API methods --

  /** Executes the plugin. */
  @Override
  public void run(String arg) {
    DebugTools.enableLogging("INFO");
    canceled = false;
    success = false;
    if (!LibraryChecker.checkJava() || !LibraryChecker.checkImageJ()) return;
    HashSet<String> missing = new HashSet<String>();
    LibraryChecker.checkLibrary(LibraryChecker.Library.BIO_FORMATS, missing);
    LibraryChecker.checkLibrary(LibraryChecker.Library.ODE_JAVA_XML, missing);
    LibraryChecker.checkLibrary(LibraryChecker.Library.FORMS, missing);
    if (!LibraryChecker.checkMissing(missing)) return;
    new Importer(this).run(arg);
  }

}
