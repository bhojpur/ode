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
 * This class implements Targa RLE decompression. Compression is not yet
 * implemented.
 */
public class TargaRLECodec extends BaseCodec {

  /* @see Codec#compress(byte[], CodecOptions) */
  @Override
  public byte[] compress(byte[] data, CodecOptions options)
    throws FormatException
  {
    // TODO: Add compression support.
    throw new UnsupportedCompressionException(
      "Targa RLE compression not currently supported");
  }

  /**
   * The CodecOptions parameter should have the following fields set:
   *  {@link CodecOptions#maxBytes maxBytes}
   *  {@link CodecOptions#bitsPerSample}
   *
   * @see Codec#decompress(RandomAccessInputStream, CodecOptions)
   */
  @Override
  public byte[] decompress(RandomAccessInputStream in, CodecOptions options)
    throws FormatException, IOException
  {
    if (in == null) 
      throw new IllegalArgumentException("No data to decompress.");
    if (options == null) options = CodecOptions.getDefaultOptions();
    long fp = in.getFilePointer();
    ByteArrayOutputStream output = new ByteArrayOutputStream(options.maxBytes);
    int nread = 0;
    BufferedInputStream s = new BufferedInputStream(in, 262144);
    int bpp = options.bitsPerSample / 8;
    while (output.size() < options.maxBytes) {
      byte n = (byte) (s.read() & 0xff);
      nread++;
      if (n >= 0) { // 0 <= n <= 127
        byte[] b = new byte[bpp * (n + 1)];
        s.read(b);
        nread += (bpp * n + 1);
        output.write(b);
        b = null;
      }
      else if (n != -128) { // -127 <= n <= -1
        int len = (n & 0x7f) + 1;
        byte[] inp = new byte[bpp];
        s.read(inp);
        nread += bpp;
        for (int i=0; i<len; i++) output.write(inp);
      }
    }
    in.seek(fp + nread);
    return output.toByteArray();
  }
}
