/*
 * Top-level reader and writer APIs
 */

package loci.formats;

/**
 * Encompasses basic metadata about a file.
 */
public class FileInfo {

  // -- Fields --

  /** Absolute path to this file. */
  public String filename;

  /** IFormatReader implementation that would be used to read this file. */
  public Class<?> reader;

  /**
   * Whether or not this file can be passed to the appropriate reader's
   * setId(String) method.
   */
  public boolean usedToInitialize;

  // -- Object API methods --

  @Override
  public String toString() {
    return "filename = " + filename + "\nreader = " + reader.getName() +
      "\nused to initialize = " + usedToInitialize;
  }

}
