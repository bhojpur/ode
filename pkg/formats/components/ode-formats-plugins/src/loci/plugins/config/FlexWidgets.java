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

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import loci.common.services.DependencyException;
import loci.common.services.ServiceFactory;
import loci.formats.services.LuraWaveService;
import loci.formats.services.LuraWaveServiceImpl;

/**
 * Custom widgets for configuring ODE-Formats Flex support.
 */
public class FlexWidgets implements DocumentListener, IFormatWidgets {

  // -- Fields --

  private String[] labels;
  private Component[] widgets;

  private JTextField licenseBox;

  // -- Constructor --

  public FlexWidgets() {
    LuraWaveService service;
    try {
      ServiceFactory factory = new ServiceFactory();
      service = factory.getInstance(LuraWaveService.class);
    }
    catch (DependencyException e) {
      throw new RuntimeException(e);
    }

    // get license code from ImageJ preferences
    String prefCode = Prefs.get(LuraWaveServiceImpl.LICENSE_PROPERTY, null);
    String propCode = service.getLicenseCode();
    String code = "";
    if (prefCode != null) code = prefCode;
    else if (propCode != null) code = null; // hidden code

    String licenseLabel = "LuraWave license code";
    licenseBox = ConfigWindow.makeTextField();
    licenseBox.setText(code == null ? "(Licensed)" : code);
    licenseBox.setEditable(code != null);
    licenseBox.getDocument().addDocumentListener(this);

    labels = new String[] {licenseLabel};
    widgets = new Component[] {licenseBox};
  }

  // -- DocumentListener API methods --

  @Override
  public void changedUpdate(DocumentEvent e) {
    documentUpdate();
  }
  @Override
  public void removeUpdate(DocumentEvent e) {
    documentUpdate();
  }
  @Override
  public void insertUpdate(DocumentEvent e) {
    documentUpdate();
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

  // -- Helper methods --

  private void documentUpdate() {
    String code = licenseBox.getText();
    Prefs.set(LuraWaveServiceImpl.LICENSE_PROPERTY, code);
  }

}
