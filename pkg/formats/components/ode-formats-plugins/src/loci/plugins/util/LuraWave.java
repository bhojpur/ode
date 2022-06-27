package loci.plugins.util;

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

import ij.Prefs;
import ij.gui.GenericDialog;
import loci.formats.FormatException;
import loci.formats.services.LuraWaveServiceImpl;

/**
 * Utility methods for dealing with proprietary LuraWave licensing.
 */
public final class LuraWave {

  // -- Constants --

  public static final int MAX_TRIES = 5;
  public static final String TOO_MANY_TRIES =
    "Too many LuraWave license code attempts; giving up.";

  // -- Constructor --

  private LuraWave() { }

  // -- Utility methods --

  /** Reads LuraWave license code from ImageJ preferences, if available. */
  public static String initLicenseCode() {
    String code = Prefs.get(LuraWaveServiceImpl.LICENSE_PROPERTY, null);
    if (code != null && code.length() >= 6) {
      System.setProperty(LuraWaveServiceImpl.LICENSE_PROPERTY, code);
    }
    return code;
  }

  /**
   * Returns true if the given exception was cause
   * by a missing or invalid LuraWave license code.
   */
  public static boolean isLicenseCodeException(FormatException exc) {
    String msg = exc == null ? null : exc.getMessage();
    return msg != null && (msg.equals(LuraWaveServiceImpl.NO_LICENSE_MSG) ||
      msg.startsWith(LuraWaveServiceImpl.INVALID_LICENSE_MSG));
  }

  /**
   * Prompts the user to enter their LuraWave
   * license code in an ImageJ dialog window.
   */
  public static String promptLicenseCode(String code, boolean first) {
    GenericDialog gd = new GenericDialog("LuraWave License Code");
    if (!first) gd.addMessage("Invalid license code; try again.");
    gd.addStringField("LuraWave_License Code: ", code, 16);
    gd.showDialog();
    if (gd.wasCanceled()) return null;
    code = gd.getNextString();
    if (code != null) Prefs.set(LuraWaveServiceImpl.LICENSE_PROPERTY, code);
    return code;
  }

}
