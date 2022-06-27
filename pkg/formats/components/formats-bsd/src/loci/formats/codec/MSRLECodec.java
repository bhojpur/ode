/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.codec;

import java.io.IOException;

import loci.common.RandomAccessInputStream;
import loci.formats.FormatException;
import loci.formats.UnsupportedCompressionException;

/**
 * Methods for compressing and decompressing data using Microsoft RLE.
 */
public class MSRLECodec extends WrappedCodec {
  public MSRLECodec() {
    super(new ode.codecs.MSRLECodec());
  }
}
