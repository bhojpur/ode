package loci.formats.gui;

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

import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import loci.formats.FileStitcher;
import loci.formats.IFormatHandler;
import loci.formats.IFormatReader;
import loci.formats.IFormatWriter;
import loci.formats.ImageReader;
import loci.formats.ImageWriter;
import loci.formats.ReaderWrapper;
import loci.formats.WriterWrapper;

/**
 * A utility class for working with graphical user interfaces.
 */
public final class GUITools {

  // -- Fields --

  /** String to use for "all types" combination filter. */
  private static final String ALL_TYPES = "All supported file types";

  // -- Constructor --

  private GUITools() { }

  // -- File chooser --

  /** Constructs a list of file filters for the given file format handler. */
  public static FileFilter[] buildFileFilters(IFormatHandler handler) {
    FileFilter[] filters = null;

    // unwrap reader
    while (true) {
      if (handler instanceof ReaderWrapper) {
        handler = ((ReaderWrapper) handler).getReader();
      }
      else if (handler instanceof FileStitcher) {
        handler = ((FileStitcher) handler).getReader();
      }
      else if (handler instanceof WriterWrapper) {
        handler = ((WriterWrapper) handler).getWriter();
      }
      else break;
    }

    // handle special cases of ImageReader and ImageWriter
    if (handler instanceof ImageReader) {
      ImageReader imageReader = (ImageReader) handler;
      IFormatReader[] readers = imageReader.getReaders();
      Vector filterList = new Vector();
      Vector comboList = new Vector();
      for (int i=0; i<readers.length; i++) {
        filterList.add(new FormatFileFilter(readers[i]));
        // NB: Some readers need to open a file to determine if it is the
        // proper type, when the extension alone is insufficient to
        // distinguish. This behavior is fine for individual filters, but not
        // for ImageReader's combination filter, because it makes the combo
        // filter too slow. So rather than composing the combo filter from
        // FormatFileFilters, we use faster but less accurate
        // ExtensionFileFilters instead.
        String[] suffixes = readers[i].getSuffixes();
        String format = readers[i].getFormat();
        comboList.add(new ExtensionFileFilter(suffixes, format));
      }
      comboList.add(new NoExtensionFileFilter());
      FileFilter combo = makeComboFilter(sortFilters(comboList));
      if (combo != null) filterList.add(combo);

      filters = sortFilters(filterList);
    }
    else if (handler instanceof ImageWriter) {
      IFormatWriter[] writers = ((ImageWriter) handler).getWriters();
      Vector filterList = new Vector();
      for (int i=0; i<writers.length; i++) {
        String[] suffixes = writers[i].getSuffixes();
        String format = writers[i].getFormat();
        filterList.add(new ExtensionFileFilter(suffixes, format));
      }
      filters = sortFilters(filterList);
    }

    // handle default reader and writer cases
    else if (handler instanceof IFormatReader) {
      IFormatReader reader = (IFormatReader) handler;
      filters = new FileFilter[] {new FormatFileFilter(reader)};
    }
    else {
      String[] suffixes = handler.getSuffixes();
      String format = handler.getFormat();
      filters = new FileFilter[] {new ExtensionFileFilter(suffixes, format)};
    }
    return filters;
  }

  /** Constructs a file chooser for the given file format handler. */
  public static JFileChooser buildFileChooser(IFormatHandler handler) {
    return buildFileChooser(handler, true);
  }

  /**
   * Constructs a file chooser for the given file format handler.
   * If preview flag is set, chooser has an preview pane showing
   * a thumbnail and other information for the selected file.
   */
  public static JFileChooser buildFileChooser(IFormatHandler handler,
    boolean preview)
  {
    return buildFileChooser(buildFileFilters(handler), preview);
  }

  /**
   * Builds a file chooser with the given file filters,
   * as well as an "All supported file types" combo filter.
   */
  public static JFileChooser buildFileChooser(final FileFilter[] filters) {
    return buildFileChooser(filters, true);
  }

  /**
   * Builds a file chooser with the given file filters, as well as an "All
   * supported file types" combo filter, if one is not already specified.
   * @param preview If set, chooser has a preview pane showing a
   *   thumbnail and other information for the selected file.
   */
  public static JFileChooser buildFileChooser(final FileFilter[] filters,
    final boolean preview)
  {
    // NB: construct JFileChooser in the AWT worker thread, to avoid deadlocks
    final JFileChooser[] jfc = new JFileChooser[1];
    Runnable r = new Runnable() {
      @Override
      public void run() {
        JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
        FileFilter[] ff = sortFilters(filters);

        FileFilter combo = null;
        if (ff.length > 0 && ff[0] instanceof ComboFileFilter) {
          // check for existing "All supported file types" filter
          ComboFileFilter cff = (ComboFileFilter) ff[0];
          if (ALL_TYPES.equals(cff.getDescription())) combo = cff;
        }
        // make an "All supported file types" filter if we don't have one yet
        if (combo == null) {
          combo = makeComboFilter(ff);
          if (combo != null) fc.addChoosableFileFilter(combo);
        }
        for (int i=0; i<ff.length; i++) fc.addChoosableFileFilter(ff[i]);
        if (combo != null) fc.setFileFilter(combo);
        if (preview) new PreviewPane(fc);
        jfc[0] = fc;
      }
    };
    if (Thread.currentThread().getName().startsWith("AWT-EventQueue")) {
      // current thread is the AWT event queue thread; just execute the code
      r.run();
    }
    else {
      // execute the code with the AWT event thread
      try {
        SwingUtilities.invokeAndWait(r);
      }
      catch (InterruptedException exc) { return null; }
      catch (InvocationTargetException exc) { return null; }
    }
    return jfc[0];
  }

  // -- Helper methods --

  /**
   * Creates an "All supported file types" combo filter
   * encompassing all of the given filters.
   */
  private static FileFilter makeComboFilter(FileFilter[] filters) {
    return filters.length > 1 ? new ComboFileFilter(filters, ALL_TYPES) : null;
  }

  /**
   * Sorts the given list of file filters, keeping the "All supported
   * file types" combo filter (if any) at the front of the list.
   */
  private static FileFilter[] sortFilters(FileFilter[] filters) {
    filters = ComboFileFilter.sortFilters(filters);
    shuffleAllTypesToFront(filters);
    return filters;
  }

  /**
   * Sorts the given list of file filters, keeping the "All supported
   * file types" combo filter (if any) at the front of the list.
   */
  private static FileFilter[] sortFilters(Vector filterList) {
    FileFilter[] filters = ComboFileFilter.sortFilters(filterList);
    shuffleAllTypesToFront(filters);
    return filters;
  }

  /**
   * Looks for an "All supported file types" combo filter
   * and shuffles it to the front of the list.
   */
  private static void shuffleAllTypesToFront(FileFilter[] filters) {
    for (int i=0; i<filters.length; i++) {
      if (filters[i] instanceof ComboFileFilter) {
        if (ALL_TYPES.equals(filters[i].getDescription())) {
          FileFilter f = filters[i];
          for (int j=i; j>=1; j--) filters[j] = filters[j - 1];
          filters[0] = f;
          break;
        }
      }
    }
  }

}
