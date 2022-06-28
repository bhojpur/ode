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

import loci.formats.UpgradeChecker;
import loci.plugins.BF;
import loci.plugins.Updater;

/**
 * Bhojpur ODE-Formats Importer upgrade checker dialog box.
 */
public class UpgradeDialog extends ImporterDialog {

  // -- Static fields --

  /** Whether an upgrade check has already been performed this session. */
  private static boolean checkPerformed = false;

  // -- Constructor --

  /** Creates an upgrade checker dialog for the ODE-Formats Importer. */
  public UpgradeDialog(ImportProcess process) {
    super(process);
  }

  // -- ImporterDialog methods --

  @Override
  protected boolean needPrompt() {
    return !options.isQuiet() && !process.isWindowless();
  }

  @Override
  protected GenericDialog constructDialog() { return null; }

  /**
   * Asks user whether ODE-Formats should automatically check for upgrades,
   * and if so, checks for an upgrade and prompts user to install it.
   *
   * @return status of operation
   */
  @Override
  protected boolean displayDialog(GenericDialog gd) {
    if (checkPerformed) return true;

    if (!options.isQuiet() && options.isFirstTime()) {
      // present user with one-time dialog box
      gd = new GenericDialog("ODE-Formats Upgrade Checker");
      gd.addMessage("One-time notice: The ODE-Formats plugins for ImageJ can " +
        "automatically check for upgrades\neach time they are run. If you " +
        "wish to disable this feature, uncheck the box below.\nYou can " +
        "toggle this behavior later in the ODE-Formats Plugins Configuration's " +
        "\"Upgrade\" tab.");
      addCheckbox(gd, ImporterOptions.KEY_UPGRADE_CHECK);
      gd.showDialog();
      if (gd.wasCanceled()) return false;

      // save choice
      final boolean checkForUpgrades = gd.getNextBoolean();
      options.setUpgradeCheck(checkForUpgrades);
      if (!checkForUpgrades) return true;
    }

    if (options.doUpgradeCheck()) {
      UpgradeChecker checker = new UpgradeChecker();
      checkPerformed = true;
      BF.status(false, "Checking for new stable version...");
      // check for Fiji here instead of earlier in the method so that we
      // still have a chance of keeping ODE.registry up to date
      if (checker.newVersionAvailable("ImageJ") && !Updater.isFiji()) {
        boolean doUpgrade = IJ.showMessageWithCancel("",
          "A new stable version of ODE-Formats is available.\n" +
          "Click 'OK' to upgrade now, or 'Cancel' to skip for now.");
        if (doUpgrade) {
          Updater.install(UpgradeChecker.STABLE_BUILD + UpgradeChecker.TOOLS);
        }
      }
    }

    return true;
  }

  @Override
  protected boolean harvestResults(GenericDialog gd) { return true; }

}
