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

import loci.formats.in.NativeND2Reader;
import loci.plugins.util.LociPrefs;

/**
 * Custom widgets for configuring Bhojpur ODE-Formats ND2 support.
 */
public class ND2Widgets implements IFormatWidgets, ItemListener {

  // -- Fields --

  private String[] labels;
  private Component[] widgets;

  // -- Constructor --

  public ND2Widgets() {
    boolean nikon = Prefs.get(LociPrefs.PREF_ND2_NIKON, false);

    String legacyLabel = "Nikon";
    JCheckBox legacyBox = new JCheckBox(
      "Use Nikon's ND2 library instead of native ND2 support", nikon);
    legacyBox.addItemListener(this);

    boolean chunkmap = Prefs.get(LociPrefs.PREF_ND2_CHUNKMAP,
      NativeND2Reader.USE_CHUNKMAP_DEFAULT);

    String chunkmapLabel = "Chunkmap";
    JCheckBox chunkmapBox = new JCheckBox(
      "Use chunkmap table to read image offsets", chunkmap);
    chunkmapBox.addItemListener(this);

    labels = new String[] {legacyLabel, chunkmapLabel};
    widgets = new Component[] {legacyBox, chunkmapBox};
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
    if (box.equals(getWidgets()[0])) {
      Prefs.set(LociPrefs.PREF_ND2_NIKON, box.isSelected());
    }
    else if (box.equals(getWidgets()[1])) {
      Prefs.set(LociPrefs.PREF_ND2_CHUNKMAP, box.isSelected());
    }
  }

}
