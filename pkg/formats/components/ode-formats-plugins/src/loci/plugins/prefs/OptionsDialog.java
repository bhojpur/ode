package loci.plugins.prefs;

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
 * Base class for options dialogs.
 */
public abstract class OptionsDialog {

  // -- Constants --

  // status values
  public static final int STATUS_OK       = 0;
  public static final int STATUS_CANCELED = 1;
  public static final int STATUS_FINISHED = 2;

  /** Flag indicating whether to invoke workaround for AWT refresh bug. */
  protected static final boolean IS_GLITCHED =
    System.getProperty("os.name").indexOf("Mac OS X") >= 0;

  // -- Fields --

  /** Options list associated with the dialog. */
  protected OptionsList optionsList;

  // -- Constructor --

  /** Creates a new options dialog with the given associated options. */
  public OptionsDialog(OptionsList optionsList) {
    this.optionsList = optionsList;
  }

  // -- OptionsDialog methods --

  /** Displays the options dialog, returning the status of the operation. */
  public abstract int showDialog();

  // -- Helper methods --

  /**
   * Adds a choice to the given dialog object corresponding
   * to the string option identified by the specified key.
   */
  protected void addChoice(GenericDialog gd, String key) {
    gd.addChoice(optionsList.getLabel(key),
      optionsList.getPossible(key), optionsList.getValue(key));
  }

  /**
   * Adds a checkbox to the given dialog object corresponding
   * to the boolean option identified by the specified key.
   */
  protected void addCheckbox(GenericDialog gd, String key) {
    gd.addCheckbox(optionsList.getLabel(key), optionsList.isSet(key));
  }

  /** Blocks the current thread for the specified number of milliseconds. */
  protected void sleep(long ms) {
    try {
      Thread.sleep(ms);
    }
    catch (InterruptedException exc) { }
  }

}
