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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.util.ArrayList;
import java.util.List;

import loci.plugins.util.ImageProcessorReader;
import loci.plugins.util.WindowTools;

/**
 * Bhojpur ODE-Formats Importer custom color chooser dialog box.
 *
 * Heavily adapted from {@link ij.gui.ColorChooser}.
 * ColorChooser is not used because there is no way to change the slider
 * labels&mdash;this means that we can't record macros in which custom colors
 * are chosen for multiple channels.

 */
public class ColorDialog extends ImporterDialog {

  // -- Constants --

  private static final Dimension SWATCH_SIZE = new Dimension(100, 50);

  // -- Constructor --

  public ColorDialog(ImportProcess process) {
    super(process);
  }

  // -- ImporterDialog methods --

  @Override
  protected boolean needPrompt() {
    return !process.isWindowless() && options.isColorModeCustom();
  }

  @Override
  protected GenericDialog constructDialog() {
    GenericDialog gd = new GenericDialog("ODE-Formats Custom Colorization");

    // CTR FIXME - avoid problem with MAX_SLIDERS in GenericDialog
    final ImageProcessorReader reader = process.getReader();
    final List<Panel> swatches = new ArrayList<Panel>();
    for (int s=0; s<process.getSeriesCount(); s++) {
      if (!options.isSeriesOn(s)) continue;
      reader.setSeries(s);
      for (int c=0; c<reader.getSizeC(); c++) {
        Color color = options.getCustomColor(s, c);
        if (color == null) color = options.getDefaultCustomColor(c);
        gd.addSlider(makeLabel("Red:", s, c), 0, 255, color.getRed());
        gd.addSlider(makeLabel("Green:", s, c), 0, 255, color.getGreen());
        gd.addSlider(makeLabel("Blue:", s, c), 0, 255, color.getBlue());

        Panel swatch = createSwatch(color);
        gd.addPanel(swatch, GridBagConstraints.CENTER, new Insets(5, 0, 5, 0));
        swatches.add(swatch);
      }
    }

    // change swatch colors when sliders move
    List<TextField> colorFields = getNumericFields(gd);
    attachListeners(colorFields, swatches);

    WindowTools.addScrollBars(gd);

    return gd;
  }

  @Override
  protected boolean harvestResults(GenericDialog gd) {
    final ImageProcessorReader reader = process.getReader();
    for (int s=0; s<process.getSeriesCount(); s++) {
      if (!options.isSeriesOn(s)) continue;
      reader.setSeries(s);
      for (int c=0; c<reader.getSizeC(); c++) {
        int red = (int) gd.getNextNumber();
        int green = (int) gd.getNextNumber();
        int blue = (int) gd.getNextNumber();
        Color color = new Color(red, green, blue);
        options.setCustomColor(s, c, color);
      }
    }
    return true;
  }

  // -- Helper methods --

  private String makeLabel(String baseLabel, int s, int c) {
    return "Series_" + s + "_Channel_" + c + "_" + baseLabel;
  }

  private Panel createSwatch(Color color) {
    Panel swatch = new Panel();
    swatch.setPreferredSize(SWATCH_SIZE);
    swatch.setMinimumSize(SWATCH_SIZE);
    swatch.setMaximumSize(SWATCH_SIZE);
    swatch.setBackground(color);
    return swatch;
  }

  private void attachListeners(List<TextField> colors, List<Panel> swatches) {
    int colorIndex = 0, swatchIndex = 0;
    while (colorIndex < colors.size()) {
      final TextField redField = colors.get(colorIndex++);
      final TextField greenField = colors.get(colorIndex++);
      final TextField blueField = colors.get(colorIndex++);
      final Panel swatch = swatches.get(swatchIndex++);
      TextListener textListener = new TextListener() {
        @Override
        public void textValueChanged(TextEvent e) {
          int red = getColorValue(redField);
          int green = getColorValue(greenField);
          int blue = getColorValue(blueField);
          swatch.setBackground(new Color(red, green, blue));
          swatch.repaint();
        }
      };
      redField.addTextListener(textListener);
      greenField.addTextListener(textListener);
      blueField.addTextListener(textListener);
    }
  }

  private int getColorValue(TextField colorField) {
    int color = 0;
    try {
      color = Integer.parseInt(colorField.getText());
    }
    catch (NumberFormatException exc) { }
    if (color < 0) color = 0;
    if (color > 255) color = 255;
    return color;
  }

  @SuppressWarnings("unchecked")
  private List<TextField> getNumericFields(GenericDialog gd) {
    return gd.getNumericFields();
  }

}
