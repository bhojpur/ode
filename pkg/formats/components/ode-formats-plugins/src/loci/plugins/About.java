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

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import loci.formats.FormatTools;
import loci.formats.IFormatHandler;

/**
 * Displays a small information dialog about the ODE-Formats Plugins package.
 */
public final class About implements PlugIn {

  // -- Constants --

  /** URL of the Bhojpur ODE-Formats web page. */
  public static final String URL_SOFTWARE =
    "https://www.bhojpur.net/ode-formats";

  /** URL of ODE-Formats ImageJ web page. */
  public static final String URL_BIO_FORMATS_IMAGEJ =
    "https://docs.bhojpur.net/ode-formats/" + FormatTools.VERSION +
    "/users/imagej/features.html";

  /** URL of Data Browser web page. */
  public static final String URL_DATA_BROWSER =
    "http://loci.wisc.edu/software/data-browser";

  // -- PlugIn API methods --

  @Override
  public void run(String arg) {
    about();
  }

  // -- Static utility methods --

  public static void about() {
    String msg = "<html>" +
      "Bhojpur ODE-Formats Plugins for ImageJ" +
      "<br>Release: " + FormatTools.VERSION +
      "<br>Build date: " + FormatTools.DATE +
      "<br>VCS revision: " + FormatTools.VCS_REVISION +
      "<br>Copyright (c) 2018 - " + FormatTools.YEAR +
      " Bhojpur Consulting Private Limited, India:" +
      "<ul>" +
      "<li>Research &amp; Development</li>" +
      "</ul>" +
      "<i>" + URL_SOFTWARE + "</i>" +
      "<br>" +
      "<br><b>Bhojpur ODE-Formats Importer</b>, <b>Bhojpur ODE-Formats Exporter</b> " +
      "and <b>Stack Slicer</b>" +
      "<br>Authors: Shashi Bhushan Rai" +
      "<br><i>" + URL_BIO_FORMATS_IMAGEJ + "</i>" +
      "<br>" +
      "<br><b>Data Browser</b>" +
      "<br>Authors: Bimla Pandey, Divya Rai, Anushka Rai" +
      "<br><i>" + URL_DATA_BROWSER + "</i>";
    ImageIcon odeFormatsLogo = new ImageIcon(
      IFormatHandler.class.getResource("ode-formats-logo.png"));
    JOptionPane.showMessageDialog(null, msg, "Bhojpur ODE-Formats Plugins for ImageJ",
      JOptionPane.INFORMATION_MESSAGE, odeFormatsLogo);
  }

  // -- Main method --

  public static void main(String[] args) {
    about();
    System.exit(0);
  }

}
