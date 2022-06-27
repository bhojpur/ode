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

import loci.plugins.util.LociPrefs;

/**
 * Custom widgets for configuring ODE-Formats Zeiss CZI support.
 *
 */
public class ZeissCZIWidgets implements IFormatWidgets, ItemListener {

  // -- Fields --

  private String[] labels;
  private Component[] widgets;

  // -- Constructor --

  public ZeissCZIWidgets() {
    boolean attachmentImages = Prefs.get(LociPrefs.PREF_CZI_ATTACHMENT, true);
    boolean autostitch = Prefs.get(LociPrefs.PREF_CZI_AUTOSTITCH, true);

    String attachmentLabel = "Attachment";
    String autostitchLabel = "Autostitch";

    JCheckBox attachmentBox = new JCheckBox(
      "Include attachment images", attachmentImages);
    attachmentBox.addItemListener(this);

    JCheckBox autostitchBox = new JCheckBox(
      "Automatically stitch tiled images", autostitch);
    autostitchBox.addItemListener(this);

    labels = new String[] {attachmentLabel, autostitchLabel};
    widgets = new Component[] {attachmentBox, autostitchBox};
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
      Prefs.set(LociPrefs.PREF_CZI_ATTACHMENT, box.isSelected());
    }
    else if (box.equals(widgets[1])) {
      Prefs.set(LociPrefs.PREF_CZI_AUTOSTITCH, box.isSelected());
    }
  }

}
