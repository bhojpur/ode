/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.codec;

import java.io.IOException;

import loci.common.RandomAccessInputStream;
import loci.formats.FormatException;
import loci.formats.UnsupportedCompressionException;

/**
 * This class implements LZO decompression. Compression is not yet
 * implemented.
 */
public class LZOCodec extends WrappedCodec {
  public LZOCodec() {
    super(new ode.codecs.LZOCodec());
  }
}
