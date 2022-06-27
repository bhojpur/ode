/*
 * Top-level reader and writer APIs
 */

package loci.formats;

/**
 * UnknownFormatException is the exception thrown when the format of a
 * particular data stream or filename cannot be recognized or is unsupported.
 */
public class UnknownFormatException extends FormatException {

  public UnknownFormatException() { super(); }
  public UnknownFormatException(String s) { super(s); }
  public UnknownFormatException(String s, Throwable cause) { super(s, cause); }
  public UnknownFormatException(Throwable cause) { super(cause); }

}
