/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.gui;

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
