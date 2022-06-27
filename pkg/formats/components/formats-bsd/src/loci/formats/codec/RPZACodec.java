/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.codec;

import java.io.IOException;

import loci.common.RandomAccessInputStream;
import loci.formats.FormatException;
import loci.formats.UnsupportedCompressionException;

/**
 * Implements encoding and decoding methods for Apple RPZA.  This code was
 * adapted from the RPZA codec for ffmpeg - see http://ffmpeg.mplayerhq.hu
 */
public class RPZACodec extends WrappedCodec {
  public RPZACodec() {
    super(new ode.codecs.RPZACodec());
  }
}
