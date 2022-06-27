/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.codec;

import java.io.IOException;

import loci.common.RandomAccessInputStream;
import loci.formats.FormatException;
import loci.formats.UnsupportedCompressionException;

/**
 * Methods for compressing and decompressing QuickTime Motion JPEG-B data.
 */
public class MJPBCodec extends WrappedCodec {
  public MJPBCodec() {
    super(new ode.codecs.MJPBCodec());
  }
}
