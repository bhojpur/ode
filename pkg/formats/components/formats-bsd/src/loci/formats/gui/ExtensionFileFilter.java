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

/**
 * A file filter based on file extensions, for use with a JFileChooser.
 */
public class ExtensionFileFilter extends FileFilter
  implements java.io.FileFilter, Comparable
{

  // -- Fields --

  /** List of valid extensions. */
  private String[] exts;

  /** Description. */
  private String desc;

  // -- Constructors --

  /** Constructs a new filter that accepts the given extension. */
  public ExtensionFileFilter(String extension, String description) {
    this(new String[] {extension}, description);
  }

  /** Constructs a new filter that accepts the given extensions. */
  public ExtensionFileFilter(String[] extensions, String description) {
    exts = new String[extensions.length];
    System.arraycopy(extensions, 0, exts, 0, extensions.length);
    final StringBuilder sb = new StringBuilder(description);
    boolean first = true;
    for (int i=0; i<exts.length; i++) {
      if (exts[i] == null) exts[i] = "";
      if (exts[i].equals("")) continue;
      if (first) {
        sb.append(" (");
        first = false;
      }
      else sb.append(", ");
      sb.append("*.");
      sb.append(exts[i]);
    }
    if (!first) {
      sb.append(")");
    }
    desc = sb.toString();
  }

  // -- ExtensionFileFilter API methods --

  /** Gets the filter's first valid extension. */
  public String getExtension() { return exts[0]; }

  /** Gets the filter's valid extensions. */
  public String[] getExtensions() { return exts; }

  // -- FileFilter API methods --

  /** Accepts files with the proper extensions. */
  @Override
  public boolean accept(File f) {
    if (f.isDirectory()) return true;

    String name = f.getName().toLowerCase();

    for (int i=0; i<exts.length; i++) {
      if (name.endsWith(exts[i].toLowerCase())) return true;
    }

    return false;
  }

  /** Gets the filter's description. */
  @Override
  public String getDescription() { return desc; }

  // -- Object API methods --

  /** Gets a string representation of this file filter. */
  @Override
  public String toString() { return "ExtensionFileFilter: " + desc; }

  // -- Comparable API methods --

  /** Compares two FileFilter objects alphanumerically. */
  @Override
  public int compareTo(Object o) {
    return desc.compareToIgnoreCase(((FileFilter) o).getDescription());
  }

}
