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
import loci.plugins.util.ImageProcessorReader;
import loci.plugins.util.WindowTools;

/**
 * Bhojpur ODE-Formats Importer range chooser dialog box.
 */
public class RangeDialog extends ImporterDialog {

  // -- Constructor --

  /**
   * Creates a range chooser dialog for the ODE-Formats Importer.
   *
   * @param process the {@link ImportProcess} to use for extracting
   * details of each series.
   */
  public RangeDialog(ImportProcess process) {
    super(process);
  }

  // -- ImporterDialog methods --

  @Override
  protected boolean needPrompt() {
    if (process.isWindowless() || !options.isSpecifyRanges()) return false;

    ImageProcessorReader r = process.getReader();
    int seriesCount = process.getSeriesCount();
    int currentSeries = r.getSeries();
    for (int s=0; s<seriesCount; s++) {
      r.setSeries(s);
      if (options.isSeriesOn(s) && r.getImageCount() > 1) {
        r.setSeries(currentSeries);
        return true;
      }
    }
    r.setSeries(currentSeries);
    return false;
  }

  @Override
  protected GenericDialog constructDialog() {
    ImageProcessorReader r = process.getReader();
    int seriesCount = process.getSeriesCount();

    // construct dialog
    GenericDialog gd = new GenericDialog("ODE-Formats Range Options");
    for (int s=0; s<seriesCount; s++) {
      if (!options.isSeriesOn(s)) continue;
      r.setSeries(s);
      gd.addMessage(process.getSeriesLabel(s).replaceAll("_", " "));
      String suffix = seriesCount > 1 ? "_" + (s + 1) : "";
      //if (r.isOrderCertain()) {
      if (r.getEffectiveSizeC() > 1) {
        gd.addNumericField("C_Begin" + suffix, process.getCBegin(s) + 1, 0);
        gd.addNumericField("C_End" + suffix, process.getCEnd(s) + 1, 0);
        gd.addNumericField("C_Step" + suffix, process.getCStep(s), 0);
      }
      if (r.getSizeZ() > 1) {
        gd.addNumericField("Z_Begin" + suffix, process.getZBegin(s) + 1, 0);
        gd.addNumericField("Z_End" + suffix, process.getZEnd(s) + 1, 0);
        gd.addNumericField("Z_Step" + suffix, process.getZStep(s), 0);
      }
      if (r.getSizeT() > 1) {
        gd.addNumericField("T_Begin" + suffix, process.getTBegin(s) + 1, 0);
        gd.addNumericField("T_End" + suffix, process.getTEnd(s) + 1, 0);
        gd.addNumericField("T_Step" + suffix, process.getTStep(s), 0);
      }
      //}
      //else {
      //  gd.addNumericField("Begin" + suffix, process.getCBegin(s) + 1, 0);
      //  gd.addNumericField("End" + suffix, process.getCEnd(s) + 1, 0);
      //  gd.addNumericField("Step" + suffix, process.getCStep(s), 0);
      //}
    }
    WindowTools.addScrollBars(gd);

    return gd;
  }

  @Override
  protected boolean harvestResults(GenericDialog gd) {
    ImageProcessorReader r = process.getReader();
    int seriesCount = process.getSeriesCount();

    for (int s=0; s<seriesCount; s++) {
      if (!options.isSeriesOn(s)) continue;
      r.setSeries(s);
      int sizeC = r.getEffectiveSizeC();
      int sizeZ = r.getSizeZ();
      int sizeT = r.getSizeT();
      boolean certain = r.isOrderCertain();

      int cBegin = process.getCBegin(s);
      int cEnd = process.getCEnd(s);
      int cStep = process.getCStep(s);
      int zBegin = process.getZBegin(s);
      int zEnd = process.getZEnd(s);
      int zStep = process.getZStep(s);
      int tBegin = process.getTBegin(s);
      int tEnd = process.getTEnd(s);
      int tStep = process.getTStep(s);

      //if (certain) {
      if (r.getEffectiveSizeC() > 1) {
        cBegin = (int) gd.getNextNumber() - 1;
        cEnd = (int) gd.getNextNumber() - 1;
        cStep = (int) gd.getNextNumber();
      }
      if (r.getSizeZ() > 1) {
        zBegin = (int) gd.getNextNumber() - 1;
        zEnd = (int) gd.getNextNumber() - 1;
        zStep = (int) gd.getNextNumber();
      }
      if (r.getSizeT() > 1) {
        tBegin = (int) gd.getNextNumber() - 1;
        tEnd = (int) gd.getNextNumber() - 1;
        tStep = (int) gd.getNextNumber();
      }
      //}
      //else {
      //  cBegin = (int) gd.getNextNumber() - 1;
      //  cEnd = (int) gd.getNextNumber() - 1;
      //  cStep = (int) gd.getNextNumber();
      //}
      int maxC = certain ? sizeC : r.getImageCount();
      if (cBegin < 0) cBegin = 0;
      if (cBegin >= maxC) cBegin = maxC - 1;
      if (cEnd < cBegin) cEnd = cBegin;
      if (cEnd >= maxC) cEnd = maxC - 1;
      if (cStep < 1) cStep = 1;
      if (zBegin < 0) zBegin = 0;
      if (zBegin >= sizeZ) zBegin = sizeZ - 1;
      if (zEnd < zBegin) zEnd = zBegin;
      if (zEnd >= sizeZ) zEnd = sizeZ - 1;
      if (zStep < 1) zStep = 1;
      if (tBegin < 0) tBegin = 0;
      if (tBegin >= sizeT) tBegin = sizeT - 1;
      if (tEnd < tBegin) tEnd = tBegin;
      if (tEnd >= sizeT) tEnd = sizeT - 1;
      if (tStep < 1) tStep = 1;

      options.setCBegin(s, cBegin);
      options.setCEnd(s, cEnd);
      options.setCStep(s, cStep);
      options.setZBegin(s, zBegin);
      options.setZEnd(s, zEnd);
      options.setZStep(s, zStep);
      options.setTBegin(s, tBegin);
      options.setTEnd(s, tEnd);
      options.setTStep(s, tStep);
    }

    return true;
  }

}
