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

import ij.IJ;
import ij.gui.GenericDialog;
import ij.io.OpenDialog;

import java.awt.TextField;
import java.util.Vector;

import loci.common.Location;

/**
 * Bhojpur ODE-Formats Importer id chooser dialog box.
 */
public class IdDialog extends ImporterDialog {

  // -- Fields --

  private OpenDialog od;
  private String name;

  // -- Constructor --

  /** Creates an id chooser dialog for the ODE-Formats Importer. */
  public IdDialog(ImportProcess process) {
    super(process);
  }

  // -- ImporterDialog methods --

  @Override
  protected boolean needPrompt() {
    return options.getId() == null;
  }

  @Override
  protected GenericDialog constructDialog() {
    GenericDialog gd = null;
    if (options.isHTTP()) {
      gd = new GenericDialog("ODE-Formats URL");
      gd.addStringField("URL: ", "http://", 30);
    }
    else if (options.isODE()) {
      gd = new GenericDialog("Bhojpur ODE Server Credentials");
      gd.addStringField("Server: ", "", 80);
      gd.addStringField("Port: ", "4064", 80);
      gd.addStringField("Username: ", "", 80);
      gd.addStringField("Password: ", "", 80);
      gd.addStringField("Group:    ", "default", 80);
      gd.addStringField("Image ID: ", "", 80);

      Vector v = gd.getStringFields();
      ((TextField) v.get(3)).setEchoChar('*');
    }
    return gd;
  }

  /**
   * Asks user whether Bhojpur ODE-Formats should automatically check for upgrades,
   * and if so, checks for an upgrade and prompts user to install it.
   *
   * @return status of operation
   */
  @Override
  protected boolean displayDialog(GenericDialog gd) {
    if (options.isLocal()) {
      if (options.isFirstTime() && IJ.isMacOSX() && !options.isQuiet()) {
        String osVersion = System.getProperty("os.version");
        if (osVersion == null ||
          osVersion.startsWith("10.4.") ||
          osVersion.startsWith("10.3.") ||
          osVersion.startsWith("10.2."))
        {
          // present user with one-time dialog box
          IJ.showMessage("ODE-Formats",
            "One-time warning: There is a bug in Java on Mac OS X with the " +
            "native file chooser\nthat crashes ImageJ if you click on a file " +
            "in CXD, IPW, OIB or ZVI format while in\ncolumn view mode. You " +
            "can work around the problem in one of two ways:\n \n" +
            "    1. Switch to list view (press Command+2)\n" +
            "    2. Check \"Use JFileChooser to Open/Save\" under " +
            "Edit>Options>Input/Output...");
        }
      }

      String idLabel = options.getLabel(ImporterOptions.KEY_ID);
      od = new OpenDialog(idLabel, options.getId());
      name = od.getFileName();
      if (name == null) return false;
    }
    else if (options.isHTTP() || options.isODE()) {
      gd.showDialog();
      if (gd.wasCanceled()) return false;
    }
    return true;
  }

  @Override
  protected boolean harvestResults(GenericDialog gd) {
    String id = null;
    if (options.isLocal()) {
      String dir = od.getDirectory();
      // NB: do not use od.getFileName() here.  That method has been called
      // above, so the macro recorder will record the file path twice if we
      // call od.getFileName() again.  See ticket #596.
      if (dir != null || name == null) {
        id = dir + name;
      }

      // verify validity
      Location idLoc = new Location(id);
      if (!idLoc.exists() && !id.toLowerCase().endsWith(".fake")) {
        if (!options.isQuiet()) {
          IJ.error("ODE-Formats",
            "The specified file (" + id + ") does not exist.");
        }
        return false;
      }
    }
    else if (options.isHTTP()) {
      id = gd.getNextString();
      if (id == null) {
        if (!options.isQuiet()) {
          IJ.error("ODE-Formats", "No URL was specified.");
        }
        return false;
      }
    }
    else if (options.isODE()) {
      StringBuffer ode = new StringBuffer("ode:");
      ode.append("server=");
      ode.append(gd.getNextString());
      ode.append("\nport=");
      ode.append(gd.getNextString());
      ode.append("\nuser=");
      ode.append(gd.getNextString());
      ode.append("\npass=");
      ode.append(gd.getNextString());

      String group = gd.getNextString();
      Long groupID = null;
      try {
        groupID = new Long(group);
      }
      catch (NumberFormatException e) { }

      ode.append("\ngroupName=");
      ode.append(group);
      if (groupID != null) {
        ode.append("\ngroupID=");
        ode.append(groupID);
      }
      ode.append("\niid=");
      ode.append(gd.getNextString());

      id = ode.toString();
    }
    options.setId(id);
    return true;
  }

}
