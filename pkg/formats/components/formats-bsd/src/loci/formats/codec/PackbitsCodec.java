/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.codec;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import loci.common.RandomAccessInputStream;
import loci.formats.FormatException;
import loci.formats.UnsupportedCompressionException;

/**
 * This class implements packbits decompression. Compression is not yet
 * implemented.
 */
public class PackbitsCodec extends WrappedCodec {
  public PackbitsCodec() {
    super(new ode.codecs.PackbitsCodec());
  }
}
