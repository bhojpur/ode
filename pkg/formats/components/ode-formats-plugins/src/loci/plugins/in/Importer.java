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

import ij.ImageJ;
import ij.ImagePlus;
import ij.Macro;

import java.io.IOException;

import loci.formats.FormatException;
import loci.plugins.BF;
import loci.plugins.LociImporter;
import loci.plugins.util.WindowTools;

/**
 * Core logic for the ODE-Formats Importer ImageJ plugin.
 */
public class Importer {

  // -- Fields --

  /**
   * A handle to the plugin wrapper, for toggling
   * the canceled and success flags.
   */
  private LociImporter plugin;

  // -- Constructor --

  public Importer(LociImporter plugin) {
    this.plugin = plugin;
  }

  // -- Importer API methods --

  /** Executes the plugin. */
  public void run(String arg) {
    ImporterOptions options = null;
    try {
      BF.debug("parse core options");
      options = parseOptions(arg);
      if (plugin.canceled) return;

      ImportProcess process = new ImportProcess(options);

      BF.debug("display option dialogs");
      showDialogs(process);
      if (plugin.canceled) return;

      BF.debug("display metadata");
      DisplayHandler displayHandler = new DisplayHandler(process);
      displayHandler.displayOriginalMetadata();
      displayHandler.displayODEXML();

      BF.debug("read pixel data");
      ImagePlusReader reader = new ImagePlusReader(process);
      ImagePlus[] imps = readPixels(reader, options, displayHandler);

      BF.debug("display pixels");
      displayHandler.displayImages(imps);

      BF.debug("display ROIs");
      displayHandler.displayROIs(imps);

      BF.debug("finish");
      finish(process);
    }
    catch (FormatException exc) {
      boolean quiet = options == null ? false : options.isQuiet();
      WindowTools.reportException(exc, quiet,
        "Sorry, there was a problem during import.");
    }
    catch (IOException exc) {
      boolean quiet = options == null ? false : options.isQuiet();
      WindowTools.reportException(exc, quiet,
        "Sorry, there was an I/O problem during import.");
    }
  }

  /** Parses core options. */
  public ImporterOptions parseOptions(String arg) throws IOException {
    ImporterOptions options = new ImporterOptions();
    if (Macro.getOptions() == null) {
      options.loadOptions();
    }
    options.parseArg(arg);
    options.checkObsoleteOptions();
    return options;
  }

  public void showDialogs(ImportProcess process)
    throws FormatException, IOException
  {
    // TODO: Do not use the ImporterPrompter in batch mode. Unfortunately,
    // without avoiding usage of ImporterPrompter in batch mode, the
    // ODE-Formats Importer plugin cannot work in headless mode.
    //
    // The problem is that invoking the ImporterPrompter activates crucial
    // macro-related functionality (via GenericDialogs). We cannot currently
    // eliminate the use of ImporterPrompter in batch/headless mode, as we do
    // not do our own harvesting of importer options from the macro argument.
    //
    // Further, we need to be sure all the Dialog classes are not performing
    // any "side-effect" logic on the ImportProcess and/or ImporterOptions
    // before we can make this change.

    // attach dialog prompter to process
    new ImporterPrompter(process);

    // execute the preparation process
    process.execute();
    if (process.wasCanceled()) plugin.canceled = true;
  }

  public ImagePlus[] readPixels(ImagePlusReader reader, ImporterOptions options,
    DisplayHandler displayHandler) throws FormatException, IOException
  {
    if (options.isViewNone()) return null;
    if (!options.isQuiet()) reader.addStatusListener(displayHandler);
    ImagePlus[] imps = reader.openImagePlus();
    return imps;
  }

  public void finish(ImportProcess process) throws IOException {
    if (!process.getOptions().isVirtual()) process.getReader().close();
    plugin.success = true;
  }

  // -- Main method --

  /** Main method, for testing. */
  public static void main(String[] args) {
    new ImageJ(null);
    StringBuffer sb = new StringBuffer();
    for (int i=0; i<args.length; i++) {
      if (i > 0) sb.append(" ");
      sb.append(args[i]);
    }
    new LociImporter().run(sb.toString());
  }

}
