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

import java.io.IOException;

import loci.common.RandomAccessInputStream;
import ode.codecs.CodecException;
import ode.codecs.UnsupportedCompressionException;

/**
 * Methods for compressing and decompressing data using Microsoft RLE.
 */
public class MSRLECodec extends BaseCodec {

  /* @see Codec#compress(byte[], CodecOptions) */
  @Override
  public byte[] compress(byte[] data, CodecOptions options)
    throws CodecException
  {
    throw new UnsupportedCompressionException(
      "MSRLE compression not supported.");
  }

  /**
   * The CodecOptions parameter should have the following fields set:
   *  {@link CodecOptions#width width}
   *  {@link CodecOptions#height height}
   *  {@link CodecOptions#previousImage previousImage}
   *
   * @see Codec#decompress(RandomAccessInputStream, CodecOptions)
   */
  @Override
  public byte[] decompress(RandomAccessInputStream in, CodecOptions options)
    throws CodecException, IOException
  {
    if (in == null) 
      throw new IllegalArgumentException("No data to decompress.");
    if (options == null) options = CodecOptions.getDefaultOptions();

    int code = 0;
    short extra = 0;
    int stream = 0;

    int pixelPt = 0;
    int rowPt = (options.height - 1) * options.width;
    int frameSize = options.height * options.width;

    if (options.previousImage == null) {
      options.previousImage = new byte[frameSize];
    }

    while (rowPt >= 0 && in.getFilePointer() < in.length() &&
      pixelPt < options.previousImage.length)
    {
      stream = in.read() & 0xff;
      code = stream;

      if (code == 0) {
        stream = in.read() & 0xff;
        if (stream == 0) {
          rowPt -= options.width;
          pixelPt = 0;
        }
        else if (stream == 1) return options.previousImage;
        else if (stream == 2) {
          stream = in.read() & 0xff;
          pixelPt += stream;
          stream = in.read() & 0xff;
          rowPt -= stream * options.width;
        }
        else {
          if ((rowPt + pixelPt + stream > frameSize) || (rowPt < 0)) {
            return options.previousImage;
          }

          code = stream;
          extra = (short) (stream & 0x01);
          if (stream + code + extra > in.length()) return options.previousImage;

          while (code-- > 0) {
            stream = in.read();
            options.previousImage[rowPt + pixelPt] = (byte) stream;
            pixelPt++;
          }
          if (extra != 0) in.skipBytes(1);
        }
      }
      else {
        if ((rowPt + pixelPt + stream > frameSize) || (rowPt < 0)) {
          return options.previousImage;
        }

        stream = in.read();

        while (code-- > 0) {
          options.previousImage[rowPt + pixelPt] = (byte) stream;
          pixelPt++;
        }
      }
    }

    return options.previousImage;
  }

}