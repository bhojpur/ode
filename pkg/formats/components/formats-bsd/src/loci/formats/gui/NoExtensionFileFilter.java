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
 * A file filter that selects files with no extension,
 * for use with a JFileChooser.
 */
public class NoExtensionFileFilter extends FileFilter
  implements java.io.FileFilter, Comparable
{

  // -- FileFilter API methods --

  /** Accepts files with no extension. */
  @Override
  public boolean accept(File f) {
    if (f.isDirectory()) return true;
    return f.getName().lastIndexOf('.') < 0;
  }

  /** Gets the filter's description. */
  @Override
  public String getDescription() { return "Files with no extension"; }

  // -- Object API methods --

  /** Gets a string representation of this file filter. */
  @Override
  public String toString() { return "NoExtensionFileFilter"; }

  // -- Comparable API methods --

  /** Compares two FileFilter objects alphanumerically. */
  @Override
  public int compareTo(Object o) {
    FileFilter filter = (FileFilter) o;
    return getDescription().compareToIgnoreCase(filter.getDescription());
  }

}
