/*
 * Top-level reader and writer APIs
 */

package loci.formats;

/**
 * FormatException is the exception thrown when something
 * goes wrong performing a file format operation.
 */
public class FormatException extends Exception {

  public FormatException() { super(); }
  public FormatException(String s) { super(s); }
  public FormatException(String s, Throwable cause) { super(s, cause); }
  public FormatException(Throwable cause) { super(cause); }

}

