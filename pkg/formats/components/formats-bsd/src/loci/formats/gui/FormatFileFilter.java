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

import java.io.File;

import javax.swing.filechooser.FileFilter;

import loci.formats.IFormatReader;

/**
 * A file filter for a biological file format, for use with a JFileChooser.
 */
public class FormatFileFilter extends FileFilter
  implements java.io.FileFilter, Comparable
{

  // -- Fields --

  /** Associated file format reader. */
  private IFormatReader reader;

  /** Whether it is ok to open a file to determine its type. */
  private boolean allowOpen;

  /** Description. */
  private String desc;

  // -- Constructors --

  /** Constructs a new filter that accepts files of the given reader's type. */
  public FormatFileFilter(IFormatReader reader) {
    this(reader, true);
  }

  /**
   * Constructs a new filter that accepts files of the given reader's type,
   * allowing the reader to open files only if the allowOpen flag is set.
   * @param reader The reader to use for verifying a file's type.
   * @param allowOpen Whether it is ok to open a file to determine its type.
   */
  public FormatFileFilter(IFormatReader reader, boolean allowOpen) {
    this.reader = reader;
    this.allowOpen = allowOpen;
    final StringBuilder sb = new StringBuilder(reader.getFormat());
    String[] exts = reader.getSuffixes();
    boolean first = true;
    for (int i=0; i<exts.length; i++) {
      if (exts[i] == null || exts[i].equals("")) continue;
      if (first) {
        sb.append(" (");
        first = false;
      }
      else sb.append(", ");
      sb.append("*.");
      sb.append(exts[i]);
    }
    sb.append(")");
    desc = sb.toString();
  }

  // -- FileFilter API methods --

  /** Accepts files in accordance with the file format reader. */
  @Override
  public boolean accept(File f) {
    if (f.isDirectory()) return true;
    return reader.isThisType(f.getPath(), allowOpen);
  }

  /** Returns the filter's reader. */
  public IFormatReader getReader() { return reader; }

  /** Gets the filter's description. */
  @Override
  public String getDescription() { return desc; }

  // -- Object API methods --

  /** Gets a string representation of this file filter. */
  @Override
  public String toString() { return "FormatFileFilter: " + desc; }

  // -- Comparable API methods --

  /** Compares two FileFilter objects alphanumerically. */
  @Override
  public int compareTo(Object o) {
    return desc.compareTo(((FileFilter) o).getDescription());
  }

}
