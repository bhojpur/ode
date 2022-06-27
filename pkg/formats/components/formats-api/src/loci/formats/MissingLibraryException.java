/*
 * Top-level reader and writer APIs
 */

package loci.formats;

/**
 * MissingLibraryException is the exception thrown when a particular data
 * stream or filename requires a library that is not present on the system.
 */
public class MissingLibraryException extends FormatException {

  public MissingLibraryException() { super(); }
  public MissingLibraryException(String s) { super(s); }
  public MissingLibraryException(String s, Throwable cause) { super(s, cause); }
  public MissingLibraryException(Throwable cause) { super(cause); }

}
