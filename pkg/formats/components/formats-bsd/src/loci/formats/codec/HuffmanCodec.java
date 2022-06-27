/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.codec;

import java.io.IOException;
import java.util.HashMap;

import loci.common.ByteArrayHandle;
import loci.common.RandomAccessInputStream;
import loci.formats.FormatException;
import loci.formats.UnsupportedCompressionException;

/**
 * This class implements Huffman decoding.
 */
public class HuffmanCodec extends WrappedCodec {
  public HuffmanCodec() {
    super(new ode.codecs.HuffmanCodec());
  }

  @Deprecated
  public int getSample(BitBuffer bb, CodecOptions options)
    throws FormatException
  {
    try {
      return ((ode.codecs.HuffmanCodec) this.codec).getSample(bb.getWrapped(), getOptions(options));
    }
    catch(ode.codecs.CodecException e) {
      throw WrappedCodec.unwrapCodecException(e);
    }
  }

  public int getSample(RandomAccessInputStream bb, CodecOptions options)
    throws FormatException
  {
    try {
      return ((ode.codecs.HuffmanCodec) this.codec).getSample(bb, getOptions(options));
    }
    catch(ode.codecs.CodecException e) {
      throw WrappedCodec.unwrapCodecException(e);
    }
  }
}
