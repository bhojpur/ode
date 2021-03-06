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
import ij.WindowManager;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.xml.parsers.ParserConfigurationException;

import loci.common.ReflectException;
import loci.common.ReflectedUniverse;
import loci.common.StatusEvent;
import loci.common.StatusListener;
import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.IFormatReader;
import loci.formats.Modulo;
import loci.formats.gui.XMLWindow;
import loci.formats.services.ODEXMLService;
import loci.plugins.BF;
import loci.plugins.util.DataBrowser;
import loci.plugins.util.ROIHandler;
import loci.plugins.util.SearchableWindow;
import loci.plugins.util.WindowTools;

import org.xml.sax.SAXException;

/**
 * Logic for displaying images and metadata onscreen using ImageJ.
 */
public class DisplayHandler implements StatusListener {

  // -- Fields --

  protected ImportProcess process;
  protected ImporterOptions options;
  protected XMLWindow xmlWindow;

  // -- Constructor --

  public DisplayHandler(ImportProcess process) {
    this.process = process;
    options = process.getOptions();
  }

  // -- DisplayHandler API methods --

  /** Displays standard metadata in a table in its own window. */
  public SearchableWindow displayOriginalMetadata() {
    if (!options.isShowMetadata()) return null;

    String name = process.getIdName();
    ImporterMetadata meta = process.getOriginalMetadata();
    String metaString = meta.getMetadataString("\t");
    SearchableWindow metaWindow = new SearchableWindow(
      "Original Metadata - " + name, "Key\tValue", metaString, 400, 400);
    metaWindow.setVisible(true);
    return metaWindow;
  }

  /** Displays ODE-XML metadata in a tree in its own window. */
  public XMLWindow displayODEXML() throws FormatException, IOException {
    if (!options.isShowODEXML()) return null;

    XMLWindow metaWindow = null;
    metaWindow = new XMLWindow("ODE Metadata - " + process.getIdName());
    Exception exc = null;
    try {
      ServiceFactory factory = new ServiceFactory();
      ODEXMLService service = factory.getInstance(ODEXMLService.class);
      metaWindow.setXML(service.getODEXML(process.getODEMetadata()));
      WindowTools.placeWindow(metaWindow);
      metaWindow.setVisible(true);
    }
    catch (DependencyException e) { exc = e; }
    catch (ServiceException e) { exc = e; }
    catch (ParserConfigurationException e) { exc = e; }
    catch (SAXException e) { exc = e; }
    if (exc != null) throw new FormatException(exc);
    xmlWindow = metaWindow; // save reference to ODE-XML window
    return metaWindow;
  }

  /** Displays the given images according to the configured options. */
  public void displayImages(ImagePlus[] imps) {
    if (imps != null) {
      for (ImagePlus imp : imps) displayImage(imp);
    }
  }

  /** Displays the given image according to the configured options. */
  public void displayImage(ImagePlus imp) {
    if (options.isViewNone()) return;
    else if (options.isViewStandard()) displayNormal(imp);
    else if (options.isViewHyperstack()) displayNormal(imp);
    else if (options.isViewBrowser()) displayDataBrowser(imp);
    else if (options.isViewImage5D()) displayImage5D(imp);
    else if (options.isViewView5D()) displayView5D(imp);
    else throw new IllegalStateException("Unknown display mode");
  }

  /**
   * Displays in a normal ImageJ window.
   * ImageJ will show the image as either a standard 2D image window
   * or as a hyperstack (up to 5D, with ZCT sliders) depending on whether
   * imp.setOpenAsHyperStack(true) has been called.
   */
  public void displayNormal(ImagePlus imp) {
    imp.show();
  }

  public void displayDataBrowser(ImagePlus imp) {
    IFormatReader r = process.getReader();

    int[] subC;
    String[] subCTypes;
    Modulo moduloC = r.getModuloC();
    if (moduloC.length() > 1) {
      subC = new int[] {r.getSizeC() / moduloC.length(), moduloC.length()};
      subCTypes = new String[] {moduloC.parentType, moduloC.type};
    } else {
      subC = new int[] {r.getSizeC()};
      subCTypes = new String[] {FormatTools.CHANNEL};
    }

    new DataBrowser(imp, null, subCTypes, subC, xmlWindow);
  }

  public void displayImage5D(ImagePlus imp) {
    WindowManager.setTempCurrentImage(imp);

    IFormatReader r = process.getReader();
    ReflectedUniverse ru = new ReflectedUniverse();
    try {
      ru.exec("import i5d.Image5D");
      ru.setVar("title", imp.getTitle());
      ru.setVar("stack", imp.getStack());
      ru.setVar("sizeC", imp.getNChannels());
      ru.setVar("sizeZ", imp.getNSlices());
      ru.setVar("sizeT", imp.getNFrames());
      ru.exec("i5d = new Image5D(title, stack, sizeC, sizeZ, sizeT)");
      ru.setVar("cal", imp.getCalibration());
      ru.setVar("fi", imp.getOriginalFileInfo());
      ru.exec("i5d.setCalibration(cal)");
      ru.exec("i5d.setFileInfo(fi)");
      //ru.exec("i5d.setDimensions(sizeC, sizeZ, sizeT)");
      ru.exec("i5d.show()");
    }
    catch (ReflectException exc) {
      WindowTools.reportException(exc, options.isQuiet(),
        "Sorry, there was a problem interfacing with Image5D");
    }
  }

  public void displayView5D(ImagePlus imp) {
    WindowManager.setTempCurrentImage(imp);
    //new view5d.View5D_("");
    Exception exc = null;
    try {
      Class<?> c = Class.forName("view5d.View5D_");
      Constructor<?> con = c.getConstructor();
      con.newInstance();
    }
    catch (ClassNotFoundException e) { exc = e; }
    catch (SecurityException e) { exc = e; }
    catch (NoSuchMethodException e) { exc = e; }
    catch (IllegalArgumentException e) { exc = e; }
    catch (InstantiationException e) { exc = e; }
    catch (IllegalAccessException e) { exc = e; }
    catch (InvocationTargetException e) { exc = e; }
    if (exc != null) {
      WindowTools.reportException(exc, options.isQuiet(),
        "Sorry, there was a problem interfacing with View5D");
    }
  }

  public void displayROIs(ImagePlus[] imps) {
    if (!options.showROIs()) return;
    ROIHandler.openROIs(process.getODEMetadata(), imps, options.isODE(), options.getROIsMode());
    
  }
   

  // -- StatusListener methods --

  /** Reports status updates via ImageJ's status bar mechanism. */
  @Override
  public void statusUpdated(StatusEvent e) {
    String msg = e.getStatusMessage();
    if (msg != null) BF.status(options.isQuiet(), msg);
    int value = e.getProgressValue();
    int max = e.getProgressMaximum();
    if (value >= 0 && max >= 0) BF.progress(options.isQuiet(), value, max);
  }

}
