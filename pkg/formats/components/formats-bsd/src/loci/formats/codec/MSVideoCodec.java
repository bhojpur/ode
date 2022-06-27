/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.codec;

import java.io.IOException;

import loci.common.RandomAccessInputStream;
import loci.formats.FormatException;
import loci.formats.UnsupportedCompressionException;

/**
 * Methods for compressing and decompressing data using Microsoft Video 1.
 *
 * See http://wiki.multimedia.cx/index.php?title=Microsoft_Video_1 for an
 * excellent description of MSV1.
 */
public class MSVideoCodec extends WrappedCodec {
  public MSVideoCodec() {
    super(new ode.codecs.MSVideoCodec());
  }
}
