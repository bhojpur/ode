/*
 * Top-level reader and writer APIs
 */

package loci.formats;

/**
 * UnsupportedCompressionException is the exception thrown when the compression
 * scheme used in a particular file or stream is unsupported.
 */
public class UnsupportedCompressionException extends FormatException {

  public UnsupportedCompressionException() { super(); }
  public UnsupportedCompressionException(String s) { super(s); }
  public UnsupportedCompressionException(String s, Throwable cause) {
    super(s, cause);
  }
  public UnsupportedCompressionException(Throwable cause) { super(cause); }

}
