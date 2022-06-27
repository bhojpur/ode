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

import ij.ImagePlus;
import ij.measure.Calibration;

import java.util.Arrays;

import loci.formats.FormatTools;
import loci.formats.meta.IMetadata;
import ode.xml.model.primitives.NonNegativeInteger;
import ode.xml.model.primitives.PositiveInteger;

import ode.units.quantity.Time;
import ode.units.quantity.Length;
import ode.units.UNITS;

/**
 * Logic for calibrating images.
 */
public class Calibrator {

  // -- Fields --

  private final ImportProcess process;

  // -- Constructor --

  public Calibrator(ImportProcess process) {
    this.process = process;
  }

  // -- Calibrator methods --

  /** Applies spatial calibrations to an image stack. */
  public void applyCalibration(ImagePlus imp) {
    final IMetadata meta = process.getODEMetadata();
    final int series = (Integer) imp.getProperty(ImagePlusReader.PROP_SERIES);

    double xcal = Double.NaN, ycal = Double.NaN;
    double zcal = Double.NaN, tcal = Double.NaN;

    Length xd = meta.getPixelsPhysicalSizeX(series);
    if (xd != null && xd.unit().isConvertible(UNITS.MICROMETER))
        xcal = xd.value(UNITS.MICROMETER).doubleValue();
    Length yd = meta.getPixelsPhysicalSizeY(series);
    if (yd != null && yd.unit().isConvertible(UNITS.MICROMETER))
        ycal = yd.value(UNITS.MICROMETER).doubleValue();
    Length zd = meta.getPixelsPhysicalSizeZ(series);
    if (zd != null && zd.unit().isConvertible(UNITS.MICROMETER))
        zcal = zd.value(UNITS.MICROMETER).doubleValue();
    Time td = meta.getPixelsTimeIncrement(series);
    if (td != null) tcal = td.value(UNITS.SECOND).doubleValue();

    boolean xcalPresent = !Double.isNaN(xcal);
    boolean ycalPresent = !Double.isNaN(ycal);
    boolean zcalPresent = !Double.isNaN(zcal);
    boolean tcalPresent = !Double.isNaN(tcal) && tcal != 0;

    // HACK: If the physical width or height are missing,
    // assume that the width and height are equal.
    if (xcalPresent && !ycalPresent) ycal = xcal;
    else if (ycalPresent && !xcalPresent) xcal = ycal;

    // HACK: If the time increment is missing,
    // average any variable time interval values.
    if (!tcalPresent) tcal = computeVariableTimeInterval(meta, series);

    xcalPresent = !Double.isNaN(xcal);
    ycalPresent = !Double.isNaN(ycal);
    zcalPresent = !Double.isNaN(zcal);
    tcalPresent = !Double.isNaN(tcal);
    final boolean hasSpatial = xcalPresent || ycalPresent || zcalPresent;
    final boolean hasCalibration = hasSpatial || ycalPresent;

    if (hasCalibration) {
      // set calibration only if at least one value is present
      Calibration cal = new Calibration();
      if (hasSpatial) cal.setUnit("micron");
      if (xcalPresent) cal.pixelWidth = xcal == 0 ? 1 : xcal;
      if (ycalPresent) cal.pixelHeight = ycal == 0 ? 1 : ycal;
      if (zcalPresent) cal.pixelDepth = zcal == 0 ? 1 : zcal;
      if (tcalPresent) cal.frameInterval = tcal == 0 ? 1 : tcal;
      imp.setCalibration(cal);
    }

    String type = meta.getPixelsType(series).toString();
    int pixelType = FormatTools.pixelTypeFromString(type);

    // NB: INT32 is represented with FloatProcessor, so no need to calibrate.
    boolean signed = pixelType == FormatTools.INT8 ||
      pixelType == FormatTools.INT16; // || pixelType == FormatTools.INT32;

    // set calibration function, so that both signed and unsigned pixel
    // values are shown
    if (signed) {
      int bitsPerPixel = FormatTools.getBytesPerPixel(pixelType) * 8;
      double min = -1 * Math.pow(2, bitsPerPixel - 1);
      imp.getLocalCalibration().setFunction(Calibration.STRAIGHT_LINE,
        new double[] {min, 1.0}, "gray value");
    }
  }

  private double computeVariableTimeInterval(IMetadata meta, int series) {
    // collect variable time interval values
    final PositiveInteger sizeT = meta.getPixelsSizeT(series);
    final int tSize = sizeT == null ? 1 : sizeT.getValue();
    final int planeCount = meta.getPlaneCount(series);
    final Time[] deltas = new Time[tSize];
    Arrays.fill(deltas, new Time(Double.NaN, UNITS.SECOND));
    for (int p=0; p<planeCount; p++) {
      final NonNegativeInteger theZ = meta.getPlaneTheZ(series, p);
      final NonNegativeInteger theC = meta.getPlaneTheC(series, p);
      final NonNegativeInteger theT = meta.getPlaneTheT(series, p);
      if (theZ == null || theC == null || theT == null) continue;
      if (theZ.getValue() != 0 || theC.getValue() != 0) continue;
      // store delta T value at appropriate index
      final int t = theT.getValue();
      if (t >= tSize) continue;
      final Time deltaT = meta.getPlaneDeltaT(series, p);
      if (deltaT == null) continue;
      deltas[t] = deltaT;
    }
    // average delta T differences
    double tiTotal = 0;
    int tiCount = 0;
    for (int t=1; t<tSize; t++) {
      double delta1 = deltas[t - 1].value(UNITS.SECOND).doubleValue();;
      double delta2 = deltas[t].value(UNITS.SECOND).doubleValue();;
      if (Double.isNaN(delta1) || Double.isNaN(delta2)) continue;
      tiTotal += delta2 - delta1;
      tiCount++;
    }
    if (tiCount == 0) return Double.NaN;

    // DeltaT is stored in seconds, and the expected units are seconds
    return (float) (tiTotal / tiCount);
  }

}
