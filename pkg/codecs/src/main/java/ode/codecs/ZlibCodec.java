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

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.InflaterInputStream;

import loci.common.RandomAccessInputStream;
import ode.codecs.CodecException;

/**
 * This class implements ZLIB decompression.
 */
public class ZlibCodec extends BaseCodec {

  /* @see Codec#compress(byte[], CodecOptions) */
  @Override
  public byte[] compress(byte[] data, CodecOptions options)
    throws CodecException
  {
    if (data == null || data.length == 0)
      throw new IllegalArgumentException("No data to compress");
    Deflater deflater = new Deflater();
    deflater.setInput(data);
    deflater.finish();
    byte[] buf = new byte[8192];
    ByteVector bytes = new ByteVector();
    int r = 0;
    // compress until eof reached
    while ((r = deflater.deflate(buf, 0, buf.length)) > 0) {
      bytes.add(buf, 0, r);
    }
    return bytes.toByteArray();
  }

  /* @see Codec#decompress(RandomAccessInputStream, CodecOptions) */
  @Override
  public byte[] decompress(RandomAccessInputStream in, CodecOptions options)
    throws CodecException, IOException
  {
    InflaterInputStream i = new InflaterInputStream(in);
    ByteVector bytes = new ByteVector();
    byte[] buf = new byte[8192];
    int r = 0;
    // read until eof reached
    try {
      while ((r = i.read(buf, 0, buf.length)) > 0) bytes.add(buf, 0, r);
    }
    catch (EOFException e) { }
    return bytes.toByteArray();
  }

}