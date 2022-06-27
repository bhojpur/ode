/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.codec;

import java.io.IOException;

import loci.common.ByteArrayHandle;
import loci.common.RandomAccessInputStream;
import loci.formats.FormatException;
import loci.formats.UnsupportedCompressionException;

/**
 * Methods for compressing and decompressing data using QuickTime RLE.
 */
public class QTRLECodec extends WrappedCodec {
  public QTRLECodec() {
    super(new ode.codecs.QTRLECodec());
  }
}
