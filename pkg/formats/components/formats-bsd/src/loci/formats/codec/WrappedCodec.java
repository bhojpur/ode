/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.codec;

import java.io.IOException;

import loci.common.RandomAccessInputStream;
import loci.formats.FormatException;

/**
 * Generic wrapper for codecs from ode-codecs.  Individual wrapper
 * codec classes should extend this class and pass the class to wrap
 * to the constructor.
 */
class WrappedCodec extends BaseCodec {

  // Wrapped alphabet and codes
  protected ode.codecs.Codec codec = null;

  /**
   * Construct with a codec implementation to wrap.
   * @param codec the codec to wrap.
   */
  protected WrappedCodec(ode.codecs.Codec codec) {
    this.codec = codec;
  }

  /* @see Codec#compress(byte[], CodecOptions) */
  @Override
  public byte[] compress(byte[] data, CodecOptions options) throws FormatException
  {
    try {
      return codec.compress(data, getOptions(options));
    }
    catch (ode.codecs.CodecException e) {
      throw unwrapCodecException(e);
    }
  }

  /* @see Codec#compress(byte[][], CodecOptions) */
  @Override
  public byte[] compress(byte[][] data, CodecOptions options) throws FormatException
  {
    try {
      return codec.compress(data, getOptions(options));
    }
    catch (ode.codecs.CodecException e) {
      throw unwrapCodecException(e);
    }
  }

  /* @see Codec#decompress(byte[], CodecOptions) */
  public byte[] decompress(byte[] data, CodecOptions options) throws FormatException
  {
    try {
      return codec.decompress(data, getOptions(options));
    }
    catch (ode.codecs.CodecException e) {
      throw unwrapCodecException(e);
    }
  }

  /* @see Codec#decompress(byte[][], CodecOptions) */
  public byte[] decompress(byte[][] data, CodecOptions options) throws FormatException
  {
    try {
      return codec.decompress(data, getOptions(options));
    }
    catch (ode.codecs.CodecException e) {
      throw unwrapCodecException(e);
    }
  }

  /* @see Codec#decompress(byte[]) */
  public byte[] decompress(byte[] data) throws FormatException
  {
    try {
      return codec.decompress(data);
    }
    catch (ode.codecs.CodecException e) {
      throw unwrapCodecException(e);
    }
  }

  /* @see Codec#decompress(byte[][]) */
  public byte[] decompress(byte[][] data) throws FormatException
  {
    try {
      return codec.decompress(data);
    }
    catch (ode.codecs.CodecException e) {
      throw unwrapCodecException(e);
    }
  }

  /* @see Codec#decompress(RandomAccessInputStream, CodecOptions) */
  @Override
  public byte[] decompress(RandomAccessInputStream in, CodecOptions options)
    throws FormatException, IOException
  {
    try {
      return codec.decompress(in, getOptions(options));
    }
    catch (ode.codecs.CodecException e) {
      throw unwrapCodecException(e);
    }
  }

  private static void copyOptions(CodecOptions src, ode.codecs.CodecOptions dest)
    throws FormatException
  {
    dest.width = src.width;
    dest.height = src.height;
    dest.channels = src.channels;
    dest.bitsPerSample = src.bitsPerSample;
    dest.littleEndian = src.littleEndian;
    dest.interleaved = src.interleaved;
    dest.signed = src.signed;
    dest.maxBytes = src.maxBytes;
    dest.previousImage = src.previousImage;
    dest.lossless = src.lossless;
    dest.colorModel = src.colorModel;
    dest.quality = src.quality;
    dest.tileWidth = src.tileWidth;
    dest.tileHeight = src.tileHeight;
    dest.tileGridXOffset = src.tileGridXOffset;
    dest.tileGridYOffset = src.tileGridYOffset;
    dest.ycbcr = src.ycbcr;
  }

  protected static ode.codecs.CodecOptions getOptions(CodecOptions options)
    throws FormatException
  {
    if(options == null) {
      return null;
    }

    ode.codecs.CodecOptions newOptions = null;

    Class c = options.getClass();

    if(c.equals(HuffmanCodecOptions.class)) {
      newOptions = new ode.codecs.HuffmanCodecOptions();
      copyOptions(options, newOptions);
      ((ode.codecs.HuffmanCodecOptions) newOptions).table = ((HuffmanCodecOptions) options).table;
    }
    else if(c.equals(JPEG2000CodecOptions.class)) {
      newOptions = new ode.codecs.JPEG2000CodecOptions();
      copyOptions(options, newOptions);
      ((ode.codecs.JPEG2000CodecOptions) newOptions).codeBlockSize = ((JPEG2000CodecOptions) options).codeBlockSize;
      ((ode.codecs.JPEG2000CodecOptions) newOptions).numDecompositionLevels = ((JPEG2000CodecOptions) options).numDecompositionLevels;
      ((ode.codecs.JPEG2000CodecOptions) newOptions).resolution = ((JPEG2000CodecOptions) options).resolution;
      ((ode.codecs.JPEG2000CodecOptions) newOptions).writeBox = ((JPEG2000CodecOptions) options).writeBox;
    }
    else if(c.equals(MJPBCodecOptions.class)) {
      newOptions = new ode.codecs.MJPBCodecOptions();
      copyOptions(options, newOptions);
      ((ode.codecs.MJPBCodecOptions) newOptions).interlaced = ((MJPBCodecOptions) options).interlaced;
    }
    else if(c.equals(CodecOptions.class)) {
      newOptions = new ode.codecs.CodecOptions();
      copyOptions(options, newOptions);
    }
    else {
      throw new FormatException("Unwrapped codec: " + c.getName());
    }

    return newOptions;
  }

  static FormatException unwrapCodecException(ode.codecs.CodecException e)
  {
    FormatException fe;
    if(e.getMessage() != null) {
      fe = new FormatException(e.getMessage());
    }
    else {
      fe = new FormatException();
    }

    if(e.getCause() != null) {
      fe.initCause(e.getCause());
    }

    fe.setStackTrace(e.getStackTrace());

    return fe;
  }
}
