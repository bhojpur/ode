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

import java.awt.Choice;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import loci.plugins.util.ImageProcessorReader;
import loci.plugins.util.WindowTools;

/**
 * ODE-Formats Importer dimension swapper dialog box.
 */
public class SwapDialog extends ImporterDialog implements ItemListener {

  // -- Fields --

  private Choice zChoice, cChoice, tChoice;

  // -- Constructor --

  /** Creates a dimension swapper dialog for the ODE-Formats Importer. */
  public SwapDialog(ImportProcess process) {
    super(process);
  }

  // -- ImporterDialog methods --

  @Override
  protected boolean needPrompt() {
    return !process.isWindowless() && options.isSwapDimensions();
  }

  @Override
  protected GenericDialog constructDialog() {
    ImageProcessorReader reader = process.getReader();
    int seriesCount = process.getSeriesCount();

    GenericDialog gd = new GenericDialog("Dimension swapping options");

    String[] labels = {"Z", "C", "T"};
    for (int s=0; s<seriesCount; s++) {
      if (!options.isSeriesOn(s)) continue;
      reader.setSeries(s);

      String[] sizes = new String[] {
          String.valueOf(reader.getSizeZ()), String.valueOf(reader.getSizeC()),
          String.valueOf(reader.getSizeT())
        };
      gd.addMessage("Series " + (s + 1) + ":\n");

      for (int i=0; i<labels.length; i++) {
        gd.addChoice(labels[i] + "_" + (s + 1), sizes, sizes[i]);
      }
    }

    List<Choice> choices = WindowTools.getChoices(gd);
    zChoice = choices.get(0);
    cChoice = choices.get(1);
    tChoice = choices.get(2);
    zChoice.addItemListener(this);
    cChoice.addItemListener(this);
    tChoice.addItemListener(this);

    WindowTools.addScrollBars(gd);

    return gd;
  }

  @Override
  protected boolean harvestResults(GenericDialog gd) {
    ImageProcessorReader reader = process.getReader();
    int seriesCount = process.getSeriesCount();

    for (int s=0; s<seriesCount; s++) {
      if (!options.isSeriesOn(s)) continue;
      reader.setSeries(s);
      int z = Integer.parseInt(gd.getNextChoice());
      int c = Integer.parseInt(gd.getNextChoice());
      int t = Integer.parseInt(gd.getNextChoice());

      int originalZ = reader.getSizeZ();
      int originalC = reader.getSizeC();
      int originalT = reader.getSizeT();

      String originalOrder = reader.getDimensionOrder();
      StringBuffer sb = new StringBuffer();
      sb.append("XY");
      for (int i=2; i<originalOrder.length(); i++) {
        int originalValue = 0;
        switch (originalOrder.charAt(i)) {
          case 'Z':
            originalValue = originalZ;
            break;
          case 'C':
            originalValue = originalC;
            break;
          case 'T':
            originalValue = originalT;
            break;
        }
        if (originalValue == z && sb.indexOf("Z") < 0) {
          sb.append("Z");
        }
        else if (originalValue == c && sb.indexOf("C") < 0) {
          sb.append("C");
        }
        else if (originalValue == t && sb.indexOf("T") < 0) {
          sb.append("T");
        }
      }

      options.setInputOrder(s, sb.toString());
    }
    return true;
  }

  // -- ItemListener methods --

  @Override
  public void itemStateChanged(ItemEvent e) {
    final Object src = e.getSource();
    final int zIndex = zChoice.getSelectedIndex();
    final int cIndex = cChoice.getSelectedIndex();
    final int tIndex = tChoice.getSelectedIndex();
    if (src == zChoice) {
      if (zIndex == cIndex) cChoice.select(firstAvailable(zIndex, tIndex));
      else if (zIndex == tIndex) tChoice.select(firstAvailable(zIndex, cIndex));
    }
    else if (src == cChoice) {
      if (cIndex == zIndex) zChoice.select(firstAvailable(cIndex, tIndex));
      else if (cIndex == tIndex) tChoice.select(firstAvailable(zIndex, cIndex));
    }
    else if (src == tChoice) {
      if (tIndex == zIndex) zChoice.select(firstAvailable(cIndex, tIndex));
      else if (tIndex == cIndex) cChoice.select(firstAvailable(zIndex, tIndex));
    }
  }

  // -- Helper methods --

  private int firstAvailable(int... index) {
    final int minValue = 0, maxValue = 2;
    for (int v=minValue; v<=maxValue; v++) {
      boolean taken = false;
      for (int i : index) {
        if (v == i) {
          taken = true;
          break;
        }
      }
      if (!taken) return v;
    }
    return -1;
  }

}
