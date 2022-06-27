/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.gui;

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
