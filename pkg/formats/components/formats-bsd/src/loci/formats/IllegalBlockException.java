/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats;


/**
 * Exception thrown when {@link FilePatternBlock} encounters an
 * invalid pattern.
 */
public class IllegalBlockException extends IllegalArgumentException {

  public IllegalBlockException() {}

  public IllegalBlockException(String message) {
    super(message);
  }

  public IllegalBlockException(String message, Throwable cause) {
    super(message, cause);
  }

  public IllegalBlockException(Throwable cause) {
    super(cause);
  }

}
