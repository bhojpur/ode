package loci.formats.in.LeicaMicrosystemsMetadata;

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

import java.util.ArrayList;
import java.util.List;

import loci.common.DataTools;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.in.MetadataLevel;
import loci.formats.meta.MetadataStore;
import ode.units.UNITS;
import ode.units.quantity.Length;

/*
 * This class helps saving ROI information to a reader's MetadataStore
 */
class ROI {
    // -- Constants --

    public static final int TEXT = 512;
    public static final int SCALE_BAR = 8192;
    public static final int POLYGON = 32;
    public static final int RECTANGLE = 16;
    public static final int LINE = 256;
    public static final int ARROW = 2;
    public static final long METER_MULTIPLY = 1000000;

    // -- Fields --
    public int type;

    public List<Double> x = new ArrayList<Double>();
    public List<Double> y = new ArrayList<Double>();

    // center point of the ROI
    public double transX, transY;

    // transformation parameters
    public double scaleX, scaleY;
    public double rotation;

    public long color;
    public int linewidth;

    public String text;
    public String fontName;
    public String fontSize;
    public String name;

    private boolean normalized = false;

    // -- ROI API methods --

    public void storeROI(MetadataStore store, int series, int roi, int roiIndex, int sizeX, int sizeY, boolean alternateCenter, MetadataLevel level)
    {
      if (level == MetadataLevel.NO_OVERLAYS || level == MetadataLevel.MINIMUM)
      {
        return;
      }

      // keep in mind that vertices are given relative to the center
      // point of the ROI and the transX/transY values are relative to
      // the center point of the image

      String roiID = MetadataTools.createLSID("ROI", roi);
      store.setImageROIRef(roiID, series, roiIndex);
      store.setROIID(roiID, roi);
      store.setLabelID(MetadataTools.createLSID("Shape", roi, 0), roi, 0);
      if (text == null) {
        text = name;
      }
      store.setLabelText(text, roi, 0);
      if (fontSize != null) {
        Double size = DataTools.parseDouble(fontSize);
        if (size != null) {
          Length fontSize = FormatTools.getFontSize(size.intValue());
          if (fontSize != null) {
            store.setLabelFontSize(fontSize, roi, 0);
          }
        }
      }
      Length l = new Length((double) linewidth, UNITS.PIXEL);
      store.setLabelStrokeWidth(l, roi, 0);

      if (!normalized) normalize();

      double cornerX = x.get(0).doubleValue();
      double cornerY = y.get(0).doubleValue();

      store.setLabelX(cornerX, roi, 0);
      store.setLabelY(cornerY, roi, 0);

      int centerX = (sizeX / 2) - 1;
      int centerY = (sizeY / 2) - 1;

      double roiX = centerX + transX;
      double roiY = centerY + transY;

      if (alternateCenter) {
        roiX = transX - 2 * cornerX;
        roiY = transY - 2 * cornerY;
      }

      // TODO : rotation not populated

      String shapeID = MetadataTools.createLSID("Shape", roi, 1);
      switch (type) {
        case POLYGON:
          final StringBuilder points = new StringBuilder();
          for (int i=0; i<x.size(); i++) {
            points.append(x.get(i).doubleValue() * scaleX + roiX);
            points.append(",");
            points.append(y.get(i).doubleValue() * scaleY + roiY);
            if (i < x.size() - 1) points.append(" ");
          }
          store.setPolygonID(shapeID, roi, 1);
          store.setPolygonPoints(points.toString(), roi, 1);

          break;
        case TEXT:
        case RECTANGLE:
          store.setRectangleID(shapeID, roi, 1);
          store.setRectangleX(roiX - Math.abs(cornerX), roi, 1);
          store.setRectangleY(roiY - Math.abs(cornerY), roi, 1);
          double width = 2 * Math.abs(cornerX);
          double height = 2 * Math.abs(cornerY);
          store.setRectangleWidth(width, roi, 1);
          store.setRectangleHeight(height, roi, 1);

          break;
        case SCALE_BAR:
        case ARROW:
        case LINE:
          store.setLineID(shapeID, roi, 1);
          store.setLineX1(roiX + x.get(0), roi, 1);
          store.setLineY1(roiY + y.get(0), roi, 1);
          store.setLineX2(roiX + x.get(1), roi, 1);
          store.setLineY2(roiY + y.get(1), roi, 1);
          break;
      }
    }

    // -- Helper methods --

    /**
     * Vertices and transformation values are not stored in pixel coordinates.
     * We need to convert them from physical coordinates to pixel coordinates
     * so that they can be stored in a MetadataStore.
     */
    private void normalize() {
      if (normalized) return;

      // coordinates are in meters

      transX *= METER_MULTIPLY;
      transY *= METER_MULTIPLY;
      transX *= 1;
      transY *= 1;

      for (int i=0; i<x.size(); i++) {
        double coordinate = x.get(i).doubleValue() * METER_MULTIPLY;
        coordinate *= 1;
        x.set(i, coordinate);
      }

      for (int i=0; i<y.size(); i++) {
        double coordinate = y.get(i).doubleValue() * METER_MULTIPLY;
        coordinate *= 1;
        y.set(i, coordinate);
      }

      normalized = true;
    }
}