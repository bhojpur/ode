/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.codec;

import java.io.IOException;

import loci.common.RandomAccessInputStream;
import loci.formats.FormatException;

/**
 * A codec which just returns the exact data it was given, performing no
 * compression or decompression.
 */
public class PassthroughCodec extends WrappedCodec {
  public PassthroughCodec() {
    super(new ode.codecs.PassthroughCodec());
  }
}
