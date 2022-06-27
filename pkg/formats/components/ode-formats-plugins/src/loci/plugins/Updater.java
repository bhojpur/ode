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
import ij.ImageJ;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;

import java.io.File;

import loci.formats.UpgradeChecker;

/**
 * A plugin for updating the ODE-Formats plugins.
 */
public class Updater implements PlugIn {

  // -- Constants --

  private static final String TRUNK = "Trunk build";
  private static final String DAILY = "Daily build";
  private static final String STABLE =
    "Stable build (" + UpgradeChecker.STABLE_VERSION + ")";

  // -- Fields --

  /** Flag indicating whether last operation was canceled. */
  public boolean canceled;

  /** Path to odeformats_package.jar uber-jar. */
  private String urlPath;

  // -- PlugIn API methods --

  @Override
  public void run(String arg) {
    if (isFiji()) {
      IJ.showMessage("Please use 'Help > Update...' to update.");
      return;
    }

    GenericDialog upgradeDialog = new GenericDialog("Update ODE-Formats Plugins");
    String[] options = new String[] {TRUNK, DAILY, STABLE};
    upgradeDialog.addChoice("Release", options, options[0]);
    upgradeDialog.showDialog();

    if (upgradeDialog.wasCanceled()) {
      canceled = true;
      return;
    }

    String release = upgradeDialog.getNextChoice();

    if (release.equals(TRUNK)) {
      urlPath = UpgradeChecker.TRUNK_BUILD;
    }
    else if (release.equals(DAILY)) {
      urlPath = UpgradeChecker.DAILY_BUILD;
    }
    else if (release.equals(STABLE)) {
      urlPath = UpgradeChecker.STABLE_BUILD;
    }
    urlPath += UpgradeChecker.TOOLS;
    install(urlPath);
  }

  /**
   * Returns true if the current ImageJ instance is actually a Fiji instance.
   */
  public static boolean isFiji() {
    ImageJ ij = IJ.getInstance();
    if (ij != null) {
      String title = ij.getTitle();
      if (title != null) {
        return title.indexOf("Fiji") >= 0;
      }
    }
    return false;
  }

  /**
   * Install the tools bundle which can be retrieved from the specified path.
   */
  public static void install(String urlPath) {
    String pluginsDirectory = IJ.getDirectory("plugins");
    String jarPath = pluginsDirectory;
    if (!isFiji()) {
      jarPath += UpgradeChecker.TOOLS;
    }

    BF.status(false, "Downloading...");
    boolean success = false;
    if (isFiji()) {
      return;
    }
    else {
      success = new UpgradeChecker().install(urlPath, jarPath);
    }

    BF.status(false, "");
    if (!success) {
      IJ.showMessage("An error occurred while downloading the ODE-Formats plugins");
    }
    else {
      IJ.showMessage("The ODE-Formats plugins have been downloaded.\n" +
        "Please restart ImageJ to complete the upgrade process.");
    }
  }

  // -- Helper methods --

  private static String find(String dir, String filename) {
    File dirFile = new File(dir);
    String[] list = dirFile.list();
    for (String f : list) {
      File nextFile = new File(dirFile, f);
      if (nextFile.isDirectory()) {
        String result = find(nextFile.getAbsolutePath(), filename);
        if (result != null) {
          return result;
        }
      }
      else {
        int dot = filename.indexOf('.');
        if (f.startsWith(filename.substring(0, dot)) &&
          f.endsWith(filename.substring(dot)))
        {
          return nextFile.getAbsolutePath();
        }
      }
    }
    return null;
  }

}
