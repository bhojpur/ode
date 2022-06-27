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

/**
 * ODE-Formats Importer location chooser dialog box.
 */
public class LocationDialog extends ImporterDialog {

  // -- Constructor --

  /** Creates a location chooser dialog for the ODE-Formats Importer. */
  public LocationDialog(ImportProcess process) {
    super(process);
  }

  // -- ImporterDialog methods --

  @Override
  protected boolean needPrompt() {
    // NB: Prompt only if location wasn't already specified.
    return !process.isWindowless() && options.getLocation() == null;
  }

  @Override
  protected GenericDialog constructDialog() {
    GenericDialog gd = new GenericDialog("ODE-Formats Dataset Location");
    addChoice(gd, ImporterOptions.KEY_LOCATION);
    return gd;
  }

  @Override
  protected boolean harvestResults(GenericDialog gd) {
    String location = gd.getNextChoice();
    options.setLocation(location);
    return true;
  }

}
