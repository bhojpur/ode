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

import loci.common.Region;
import loci.formats.IFormatReader;
import loci.plugins.util.WindowTools;

/**
 * Bhojpur ODE-Formats Importer crop options dialog box.
 */
public class CropDialog extends ImporterDialog {

  // -- Constructor --

  /** Creates a crop options dialog for the ODE-Formats Importer. */
  public CropDialog(ImportProcess process) {
    super(process);
  }

  // -- ImporterDialog methods --

  @Override
  protected boolean needPrompt() {
    return !process.isWindowless() && options.doCrop();
  }

  @Override
  protected GenericDialog constructDialog() {
    final int seriesCount = process.getSeriesCount();
    final IFormatReader r = process.getReader();

    // construct dialog
    final GenericDialog gd = new GenericDialog("ODE-Formats Crop Options");
    for (int s=0; s<seriesCount; s++) {
      if (!options.isSeriesOn(s)) continue;
      r.setSeries(s);

      Region region = process.getCropRegion(s);

      gd.addMessage(process.getSeriesLabel(s).replaceAll("_", " "));
      gd.addNumericField("X_Coordinate_" + (s + 1), region.x, 0);
      gd.addNumericField("Y_Coordinate_" + (s + 1), region.y, 0);
      gd.addNumericField("Width_" + (s + 1), region.width, 0);
      gd.addNumericField("Height_" + (s + 1), region.height, 0);
    }
    WindowTools.addScrollBars(gd);

    return gd;
  }

  @Override
  protected boolean harvestResults(GenericDialog gd) {
    final int seriesCount = process.getSeriesCount();
    final IFormatReader r = process.getReader();

    for (int s=0; s<seriesCount; s++) {
      if (!options.isSeriesOn(s)) continue;
      r.setSeries(s);

      Region region = new Region();
      region.x = (int) gd.getNextNumber();
      region.y = (int) gd.getNextNumber();
      region.width = (int) gd.getNextNumber();
      region.height = (int) gd.getNextNumber();

      if (region.x < 0) region.x = 0;
      if (region.y < 0) region.y = 0;
      if (region.x >= r.getSizeX()) region.x = r.getSizeX() - region.width - 1;
      if (region.y >= r.getSizeY()) region.y = r.getSizeY() - region.height - 1;
      if (region.width < 1) region.width = 1;
      if (region.height < 1) region.height = 1;
      if (region.width + region.x > r.getSizeX()) {
        region.width = r.getSizeX() - region.x;
      }
      if (region.height + region.y > r.getSizeY()) {
        region.height = r.getSizeY() - region.y;
      }

      options.setCropRegion(s, region);
    }

    return true;
  }

}
