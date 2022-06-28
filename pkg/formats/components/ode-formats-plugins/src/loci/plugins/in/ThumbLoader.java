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

import java.awt.Dialog;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import loci.common.DebugTools;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.IFormatReader;
import loci.formats.gui.AWTImageTools;
import loci.formats.gui.BufferedImageReader;
import loci.plugins.BF;

/**
 * Loads thumbnails for Bhojpur ODE-Formats Importer
 * series chooser in a separate thread.
 */
public class ThumbLoader implements Runnable {

  // -- Fields --

  private BufferedImageReader ir;
  private Panel[] p;
  private Dialog dialog;
  private boolean stop;
  private Thread loader;

  // -- Constructor --

  /**
   * Constructs a new thumbnail loader.
   * @param ir the reader to use for obtaining the thumbnails
   * @param p the panels upon which to populate the results
   * @param dialog the dialog containing the panels
   */
  public ThumbLoader(IFormatReader ir, Panel[] p, Dialog dialog) {
    this.ir = BufferedImageReader.makeBufferedImageReader(ir);
    this.p = p;
    this.dialog = dialog;
    loader = new Thread(this, "OdeFormats-ThumbLoader");
    loader.start();
  }

  // -- ThumbLoader methods --

  /** Instructs the thumbnail loader to stop loading thumbnails. */
  public void stop() {
    if (loader == null) return;
    stop = true;
    BF.status(false, "Canceling thumbnail generation");
    try {
      loader.join();
      loader = null;
    }
    catch (InterruptedException exc) {
      exc.printStackTrace();
    }
    BF.status(false, "");
  }

  // -- Runnable methods --

  /** Does the work of loading the thumbnails. */
  @Override
  public void run() {
    BF.status(false, "Gathering series information");
    int seriesCount = ir.getSeriesCount();

    // find image plane for each series and sort by size
    SeriesInfo[] info = new SeriesInfo[seriesCount];
    for (int i=0; i<seriesCount; i++) {
      if (stop) return;
      ir.setSeries(i);
      info[i] = new SeriesInfo(i, ir.getSizeX() * ir.getSizeY());
    }
    if (stop) return;
    Arrays.sort(info);

    // open each thumbnail, fastest/smallest first
    for (int i=0; i<seriesCount; i++) {
      if (stop) return;
      final int ii = info[i].index;
      loadThumb(ir, ii, p[ii], false);
      if (dialog != null) dialog.validate();
    }
  }

  // -- Helper methods --

  public static void loadThumb(BufferedImageReader thumbReader,
    int series, Panel panel, boolean quiet)
  {
    BF.status(quiet, "Reading thumbnail for series #" + (series + 1));
    // open middle image thumbnail in smallest pyramid resolution
    List<CoreMetadata> core = thumbReader.getCoreMetadataList();
    int index = series;
    for (int i=0; i<=index; i++) {
      if (core.get(i).resolutionCount > 1 &&
        i + core.get(i).resolutionCount > index)
      {
        index = i + core.get(i).resolutionCount - 1;
        break;
      }
    }
    thumbReader.setSeries(index);

    int z = thumbReader.getSizeZ() / 2;
    int t = thumbReader.getSizeT() / 2;
    int ndx = thumbReader.getIndex(z, 0, t);
    Exception exc = null;
    try {
      BufferedImage thumb = thumbReader.openThumbImage(ndx);
      // autoscaling floating point thumbnails typically results in a black image
      if (!FormatTools.isFloatingPoint(thumbReader.getPixelType())) {
        thumb = AWTImageTools.autoscale(thumb);
      }
      ImageIcon icon = new ImageIcon(thumb);
      panel.removeAll();
      panel.add(new JLabel(icon));
    }
    catch (FormatException e) { exc = e; }
    catch (IOException e) { exc = e; }
    if (exc != null) {
      BF.warn(quiet, "Error loading thumbnail for series #" + (series + 1));
      BF.debug(DebugTools.getStackTrace(exc));
    }
  }

  // -- Helper classes --

  /** Helper class for sorting series by image plane size. */
  public class SeriesInfo implements Comparable<SeriesInfo> {

    private int index, size;

    public SeriesInfo(int index, int size) {
      this.index = index;
      this.size = size;
    }

    @Override
    public int compareTo(SeriesInfo info) {
      return size - info.size;
    }

  }

}
