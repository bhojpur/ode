package loci.plugins.util;

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

import loci.formats.ClassList;
import loci.formats.FormatTools;
import loci.formats.IFormatReader;
import loci.formats.ImageReader;
import loci.formats.in.CellSensReader;
import loci.formats.in.DynamicMetadataOptions;
import loci.formats.in.LIFReader;
import loci.formats.in.MetadataOptions;
import loci.formats.in.NativeND2Reader;
import loci.formats.in.ND2Reader;
import loci.formats.in.PictReader;
import loci.formats.in.QTReader;
import loci.formats.in.SDTReader;
import loci.formats.in.TiffDelegateReader;
import loci.formats.in.ZeissCZIReader;

/**
 * Utility methods for ImageJ preferences for ODE-Formats plugins.
 */
public final class LociPrefs {

  // -- Constants --

  public static final String PREF_READER_ENABLED = "odeformats.enabled";
  public static final String PREF_READER_WINDOWLESS = "odeformats.windowless";

  public static final String PREF_ND2_NIKON = "odeformats.nd2.nikon";
  public static final String PREF_PICT_QTJAVA = "odeformats.pict.qtjava";
  public static final String PREF_QT_QTJAVA = "odeformats.qt.qtjava";
  public static final String PREF_SDT_INTENSITY = "odeformats.sdt.intensity";
  public static final String PREF_TIFF_IMAGEIO = "odeformats.tiff.imageio";
  public static final String PREF_CZI_AUTOSTITCH =
    "odeformats.zeissczi.allow.autostitch";
  public static final String PREF_CZI_ATTACHMENT =
    "odeformats.zeissczi.include.attachments";
  public static final String PREF_ND2_CHUNKMAP =
    "odeformats.nativend2.chunkmap";
  public static final String PREF_LEICA_LIF_PHYSICAL_SIZE =
    "odeformats.leicalif.physicalsize.compatibility";
  public static final String PREF_SLICE_LABEL_PATTERN = "odeformats.sliceLabelPattern";
  public static final String PREF_SLICE_LABEL_BASE_INDEX = "odeformats.sliceLabelBaseIndex";
  public static final String PREF_CELLSENS_FAIL =
    "odeformats.cellsens.fail_on_missing_ets";

  // -- Constructor --

  private LociPrefs() { }

  // -- Utility methods --

  /**
   * Creates an image reader according to the current configuration settings,
   * including which format readers are currently enabled, as well as
   * format-specific configuration settings.
   */
  public static ImageReader makeImageReader() {
    ClassList<IFormatReader> defaultClasses =
      ImageReader.getDefaultReaderClasses();
    Class<? extends IFormatReader>[] c = defaultClasses.getClasses();

    // include only enabled classes
    ClassList<IFormatReader> enabledClasses =
      new ClassList<IFormatReader>(IFormatReader.class);
    for (int i=0; i<c.length; i++) {
      boolean on = LociPrefs.isReaderEnabled(c[i]);
      if (on) enabledClasses.addClass(c[i]);
    }
    ImageReader reader = new ImageReader(enabledClasses);

    MetadataOptions options = reader.getMetadataOptions();
    if (options instanceof DynamicMetadataOptions) {
      ((DynamicMetadataOptions) options).setBoolean(
        ZeissCZIReader.ALLOW_AUTOSTITCHING_KEY, allowCZIAutostitch());
      ((DynamicMetadataOptions) options).setBoolean(
        ZeissCZIReader.INCLUDE_ATTACHMENTS_KEY, includeCZIAttachments());
      ((DynamicMetadataOptions) options).setBoolean(
        NativeND2Reader.USE_CHUNKMAP_KEY, useND2Chunkmap());
      ((DynamicMetadataOptions) options).setBoolean(
        LIFReader.OLD_PHYSICAL_SIZE_KEY, isLeicaLIFPhysicalSizeBackwardsCompatible());
      ((DynamicMetadataOptions) options).setBoolean(
        CellSensReader.FAIL_ON_MISSING_KEY, isCellsensFailOnMissing());
      reader.setMetadataOptions(options);
    }

    // toggle reader-specific options
    boolean nd2Nikon = LociPrefs.isND2Nikon();
    boolean pictQTJava = LociPrefs.isPictQTJava();
    boolean qtQTJava = LociPrefs.isQTQTJava();
    boolean sdtIntensity = LociPrefs.isSDTIntensity();
    boolean tiffImageIO = LociPrefs.isTiffImageIO();
    IFormatReader[] r = reader.getReaders();
    for (int i=0; i<r.length; i++) {
      if (r[i] instanceof ND2Reader) {
        ND2Reader nd2 = (ND2Reader) r[i];
        nd2.setLegacy(nd2Nikon);
      }
      else if (r[i] instanceof PictReader) {
        PictReader pict = (PictReader) r[i];
        pict.setLegacy(pictQTJava);
      }
      else if (r[i] instanceof QTReader) {
        QTReader qt = (QTReader) r[i];
        qt.setLegacy(qtQTJava);
      }
      else if (r[i] instanceof SDTReader) {
        SDTReader sdt = (SDTReader) r[i];
        sdt.setIntensity(sdtIntensity);
      }
      else if (r[i] instanceof TiffDelegateReader) {
        TiffDelegateReader tiff = (TiffDelegateReader) r[i];
        tiff.setLegacy(tiffImageIO);
      }
    }

    return reader;
  }

  /**
   * Gets whether windowless mode should be used when
   * opening this reader's currently initialized dataset.
   */
  public static boolean isWindowless(IFormatReader r) {
    return getPref(PREF_READER_WINDOWLESS, r.getClass(), false);
  }

  public static boolean isReaderEnabled(Class<? extends IFormatReader> c) {
    return getPref(PREF_READER_ENABLED, c, true);
  }

  public static boolean isND2Nikon() {
    return Prefs.get(PREF_ND2_NIKON, false);
  }

  public static boolean isPictQTJava() {
    return Prefs.get(PREF_PICT_QTJAVA, false);
  }

  public static boolean isQTQTJava() {
    return Prefs.get(PREF_QT_QTJAVA, false);
  }

  public static boolean isSDTIntensity() {
    return Prefs.get(PREF_SDT_INTENSITY, false);
  }

  public static boolean isTiffImageIO() {
    return Prefs.get(PREF_TIFF_IMAGEIO, false);
  }

  public static boolean allowCZIAutostitch() {
    return Prefs.get(PREF_CZI_AUTOSTITCH,
                     ZeissCZIReader.ALLOW_AUTOSTITCHING_DEFAULT);
  }

  public static boolean includeCZIAttachments() {
    return Prefs.get(PREF_CZI_ATTACHMENT,
                     ZeissCZIReader.INCLUDE_ATTACHMENTS_DEFAULT);
  }

  public static boolean useND2Chunkmap() {
    return Prefs.get(PREF_ND2_CHUNKMAP, NativeND2Reader.USE_CHUNKMAP_DEFAULT);
  }

  public static boolean isLeicaLIFPhysicalSizeBackwardsCompatible() {
    return Prefs.get(PREF_LEICA_LIF_PHYSICAL_SIZE,
      LIFReader.OLD_PHYSICAL_SIZE_DEFAULT);
  }

  public static String getSliceLabelPattern() {
    return Prefs.get(PREF_SLICE_LABEL_PATTERN, "%c%z%t- %n");
  }
  
  public static int getSliceLabelBaseIndex() {
    return Prefs.getInt(PREF_SLICE_LABEL_BASE_INDEX, 1);
  }
  
  public static boolean isCellsensFailOnMissing() {
    return Prefs.get(PREF_CELLSENS_FAIL, CellSensReader.FAIL_ON_MISSING_DEFAULT);
  }

  // -- Helper methods --

  private static boolean getPref(String pref,
    Class<? extends IFormatReader> c, boolean defaultValue)
  {
    String n = c.getName();
    String readerName = n.substring(n.lastIndexOf(".") + 1, n.length() - 6);
    String key = pref + "." + readerName;
    return Prefs.get(key, defaultValue);
  }

}
