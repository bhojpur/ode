package ode.codecs;

// Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import loci.common.RandomAccessInputStream;
import ode.codecs.CodecException;
import ode.codecs.UnsupportedCompressionException;

/**
 * This class implements packbits decompression. Compression is not yet
 * implemented.
 */
public class PackbitsCodec extends BaseCodec {

  /* @see Codec#compress(byte[], CodecOptions) */
  @Override
  public byte[] compress(byte[] data, CodecOptions options)
    throws CodecException
  {
    // TODO: Add compression support.
    throw new UnsupportedCompressionException(
      "Packbits Compression not currently supported");
  }

  /**
   * The CodecOptions parameter should have the following fields set:
   *  {@link CodecOptions#maxBytes maxBytes}
   *
   * @see Codec#decompress(RandomAccessInputStream, CodecOptions)
   */
  @Override
  public byte[] decompress(RandomAccessInputStream in, CodecOptions options)
    throws CodecException, IOException
  {
    if (options == null) options = CodecOptions.getDefaultOptions();
    if (in == null) 
      throw new IllegalArgumentException("No data to decompress.");
    long fp = in.getFilePointer();
    // Adapted from the TIFF 6.0 specification, page 42.
    ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
    int nread = 0;
    BufferedInputStream s = new BufferedInputStream(in, 262144);
    while (output.size() < options.maxBytes) {
      byte n = (byte) (s.read() & 0xff);
      nread++;
      if (n >= 0) { // 0 <= n <= 127
        byte[] b = new byte[n + 1];
        s.read(b);
        nread += n + 1;
        output.write(b);
        b = null;
      }
      else if (n != -128) { // -127 <= n <= -1
        int len = -n + 1;
        byte inp = (byte) (s.read() & 0xff);
        nread++;
        for (int i=0; i<len; i++) output.write(inp);
      }
    }
    if (fp + nread < in.length()) in.seek(fp + nread);
    return output.toByteArray();
  }
}