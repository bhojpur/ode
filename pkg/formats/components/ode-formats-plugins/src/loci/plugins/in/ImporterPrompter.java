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
import loci.common.StatusEvent;
import loci.common.StatusListener;
import loci.plugins.BF;
import loci.plugins.prefs.OptionsDialog;

/**
 * Helper class for presenting the user with dialog boxes
 * for configuring importer options.
 *
 * If running as a macro, gets parameter values from macro options;
 * if not, get parameter values from user input from dialog boxes.
 *
 * Which dialogs are shown depends on a variety of factors, including the
 * current configuration (i.e., which options are enabled), whether quiet or
 * windowless mode is set, and whether the method is being called from within
 * a macro.
 */
public class ImporterPrompter implements StatusListener {

  // -- Fields --

  private ImportProcess process;

  // -- Constructor --

  public ImporterPrompter(ImportProcess process) {
    this.process = process;
    process.addStatusListener(this);
  }

  // -- StatusListener methods --

  @Override
  public void statusUpdated(StatusEvent e) {
    final String message = e.getStatusMessage();
    final int value = e.getProgressValue();
    //final int max = e.getProgressMaximum();
    final ImportStep step = ImportStep.getStep(value);

    BF.status(!process.getOptions().isQuiet(), message);

    switch (step) {
      case READER:
        if (!promptUpgrade()) { process.cancel(); break; }
        if (!promptLocation()) { process.cancel(); break; }
        if (!promptId()) { process.cancel(); break; }
        break;
      case FILE:
        if (!promptMain()) process.cancel();
        break;
      case STACK:
        ImporterOptions options = process.getOptions();
        if (options != null && options.doMustGroup() && options.isGroupFiles()) {
          IJ.showMessage("ODE-Formats",
    				 "File Stitching Options are not available for files of this format.\n"
    				 + "Files will be grouped according to image format specifications.\n");
        }
        else if (!promptFilePattern()) process.cancel();
        break;
      case SERIES:
        if (!promptSeries()) process.cancel();
        break;
      case DIM_ORDER:
        if (!promptSwap()) process.cancel();
        break;
      case RANGE:
        if (!promptRange()) process.cancel();
        break;
      case CROP:
        if (!promptCrop()) process.cancel();
        break;
      case COLORS:
        if (!promptColors()) process.cancel();
        break;
      case METADATA:
        break;
      case COMPLETE:
        if (!promptMemory()) process.cancel();
        break;
      default:
    }
  }

  // -- Helper methods - dialog prompts --

  private boolean promptUpgrade() {
    UpgradeDialog dialog = new UpgradeDialog(process);
    return dialog.showDialog() == OptionsDialog.STATUS_OK;
  }

  private boolean promptLocation() {
    LocationDialog dialog = new LocationDialog(process);
    return dialog.showDialog() == OptionsDialog.STATUS_OK;
  }

  private boolean promptId() {
    IdDialog dialog = new IdDialog(process);
    return dialog.showDialog() == OptionsDialog.STATUS_OK;
  }

  private boolean promptMain() {
    MainDialog dialog = new MainDialog(process);
    return dialog.showDialog() == OptionsDialog.STATUS_OK;
  }

  /** Prompts for the file pattern, if necessary. May override id value. */
  private boolean promptFilePattern() {
    FilePatternDialog dialog = new FilePatternDialog(process);
    return dialog.showDialog() == OptionsDialog.STATUS_OK;
  }

  /** Prompts for which series to import, if necessary. */
  private boolean promptSeries() {
    SeriesDialog dialog = new SeriesDialog(process);
    return dialog.showDialog() == OptionsDialog.STATUS_OK;
  }

  /** Prompts for dimension swapping parameters, if necessary. */
  private boolean promptSwap() {
    SwapDialog dialog = new SwapDialog(process);
    return dialog.showDialog() == OptionsDialog.STATUS_OK;
  }

  /** Prompts for the range of planes to import, if necessary. */
  private boolean promptRange() {
    RangeDialog dialog = new RangeDialog(process);
    return dialog.showDialog() == OptionsDialog.STATUS_OK;
  }

  /** Prompts for cropping details, if necessary. */
  private boolean promptCrop() {
    CropDialog dialog = new CropDialog(process);
    return dialog.showDialog() == OptionsDialog.STATUS_OK;
  }

  /** Prompts for color details, if necessary. */
  private boolean promptColors() {
    ColorDialog dialog = new ColorDialog(process);
    return dialog.showDialog() == OptionsDialog.STATUS_OK;
  }

  /** Prompts for confirmation of memory usage, if necessary. */
  private boolean promptMemory() {
    MemoryDialog dialog = new MemoryDialog(process);
    return dialog.showDialog() == OptionsDialog.STATUS_OK;
  }

}
