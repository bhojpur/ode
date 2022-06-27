/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.codec;

/**
 * Implements encoding (compress) and decoding (decompress) methods
 * for Base64. This code was adapted from the Jakarta Commons Codec source,
 * http://jakarta.apache.org/commons
 */
public class Base64Codec extends WrappedCodec {
  public Base64Codec() {
    super(new ode.codecs.Base64Codec());
  }
}
