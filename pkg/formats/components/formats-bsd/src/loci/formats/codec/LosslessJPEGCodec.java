/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.codec;

import java.io.IOException;
import java.util.Vector;

import loci.common.ByteArrayHandle;
import loci.common.DataTools;
import loci.common.RandomAccessInputStream;
import loci.formats.FormatException;
import loci.formats.UnsupportedCompressionException;

/**
 * Decompresses lossless JPEG images.
 */
public class LosslessJPEGCodec extends WrappedCodec {
  public LosslessJPEGCodec() {
    super(new ode.codecs.LosslessJPEGCodec());
  }
}
