package loci.plugins.config;

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

import ij.Prefs;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

import loci.formats.in.LIFReader;

import loci.plugins.util.LociPrefs;

/**
 * Custom widgets for configuring ODE-Formats Leica LIF support.
 *
 */
public class LIFWidgets implements IFormatWidgets, ItemListener {

  // -- Fields --

  private String[] labels;
  private Component[] widgets;

  // -- Constructor --

  public LIFWidgets() {
    boolean physicalSizeBackwardsCompatibility =
      Prefs.get(LociPrefs.PREF_LEICA_LIF_PHYSICAL_SIZE, LIFReader.OLD_PHYSICAL_SIZE_DEFAULT);

    String physicalSizeLabel = "Physical size";
    JCheckBox physicalSizeBox = new JCheckBox(
      "Ensure physical pixel sizes are compatible with versions <= 5.3.2", physicalSizeBackwardsCompatibility);
    physicalSizeBox.addItemListener(this);

    labels = new String[] {physicalSizeLabel};
    widgets = new Component[] {physicalSizeBox};
  }

  // -- IFormatWidgets API methods --

  @Override
  public String[] getLabels() {
    return labels;
  }

  @Override
  public Component[] getWidgets() {
    return widgets;
  }

  // -- ItemListener API methods --

  @Override
  public void itemStateChanged(ItemEvent e) {
    JCheckBox box = (JCheckBox) e.getSource();
    if (box.equals(widgets[0])) {
      Prefs.set(LociPrefs.PREF_LEICA_LIF_PHYSICAL_SIZE, box.isSelected());
    }
  }

}
