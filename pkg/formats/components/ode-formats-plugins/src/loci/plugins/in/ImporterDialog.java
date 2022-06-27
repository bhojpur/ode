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

import ij.gui.GenericDialog;
import loci.plugins.BF;
import loci.plugins.prefs.OptionsDialog;

/**
 * Abstract superclass of importer dialogs.
 */
public abstract class ImporterDialog extends OptionsDialog {

  // -- Fields --

  protected ImportProcess process;
  protected ImporterOptions options;

  // -- Constructor --

  public ImporterDialog(ImportProcess process) {
    super(process.getOptions());
    this.process = process;
    this.options = process.getOptions();
  }

  // -- ImporterDialog methods --

  protected abstract boolean needPrompt();

  protected abstract GenericDialog constructDialog();

  /** Displays the dialog, or grabs values from macro options. */
  protected boolean displayDialog(GenericDialog gd) {
    gd.showDialog();
    return !gd.wasCanceled();
  }

  protected abstract boolean harvestResults(GenericDialog gd);

  // -- OptionsDialog methods --

  @Override
  public int showDialog() {
    // verify whether prompt is necessary
    if (!needPrompt()) {
      BF.debug(getClass().getName() + ": skip");
      return STATUS_OK;
    }
    BF.debug(getClass().getName() + ": prompt");

    GenericDialog gd = constructDialog();
    if (!displayDialog(gd)) return STATUS_CANCELED;
    if (!harvestResults(gd)) return STATUS_CANCELED;

    return STATUS_OK;
  }

}
