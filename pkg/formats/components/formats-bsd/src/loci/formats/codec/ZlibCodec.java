/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.codec;

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.InflaterInputStream;

import loci.common.RandomAccessInputStream;
import loci.formats.FormatException;

/**
 * This class implements ZLIB decompression.
 */
public class ZlibCodec extends WrappedCodec {
  public ZlibCodec() {
    super(new ode.codecs.ZlibCodec());
  }
}
