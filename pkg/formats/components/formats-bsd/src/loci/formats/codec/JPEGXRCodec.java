package loci.formats.codec;

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
import loci.common.services.DependencyException;
import loci.common.services.ServiceFactory;
import loci.formats.FormatException;
import loci.formats.MissingLibraryException;
import loci.formats.UnsupportedCompressionException;
import loci.formats.services.JPEGXRService;

/**
 */
public class JPEGXRCodec extends BaseCodec {

  // -- Fields --

  private JPEGXRService service;

  // -- Codec API methods --

  /* @see Codec#compress(byte[], CodecOptions) */
  @Override
  public byte[] compress(byte[] data, CodecOptions options)
    throws FormatException
  {
    throw new UnsupportedCompressionException(
      "JPEG-XR compression not supported");
  }

  /* @see Codec#decompress(RandomAccessInputStream, CodecOptions) */
  @Override
  public byte[] decompress(RandomAccessInputStream in, CodecOptions options)
    throws FormatException, IOException
  {
    byte[] buf = new byte[(int) in.length()];
    in.read(buf);
    return decompress(buf, options);
  }

  /**
   * The CodecOptions parameter should have the following fields set:
   *  {@link CodecOptions#maxBytes maxBytes}
   *  {@link CodecOptions#interleaved interleaved}
   *  {@link CodecOptions#bitsPerSample bitsPerSample}
   *  {@link CodecOptions#littleEndian littleEndian}
   *  {@link CodecOptions#width width}
   *  {@link CodecOptions#height height}
   *
   * @see Codec#decompress(byte[], CodecOptions)
   */
  @Override
  public byte[] decompress(byte[] buf, CodecOptions options)
    throws FormatException
  {
    initialize();

    int bpp = options.bitsPerSample / 8;
    int pixels = options.width * options.height;

    byte[] uncompressed = service.decompress(buf);
    int channels = uncompressed.length / (pixels * bpp);

    if (channels == 1 || options.interleaved) {
      return uncompressed;
    }

    byte[] deinterleaved = new byte[uncompressed.length];

    for (int p=0; p<pixels; p++) {
      for (int c=0; c<channels; c++) {
        for (int b=0; b<bpp; b++) {
          int bb = options.littleEndian ? b : bpp - b - 1;
          int src = bpp * (p * channels + c) + b;
          int dest = bpp * (c * pixels + p) + bb;
          deinterleaved[dest] = uncompressed[src];
        }
      }
    }

    return deinterleaved;
  }

  // -- Helper methods --

  /**
   * Initializes the JPEG-XR dependency service. This is called at the
   * beginning of the {@link #decompress} method to avoid having the
   * constructor's method definition contain a checked exception.
   *
   * @throws FormatException If there is an error initializing JPEG-XR
   * services.
   */
  private void initialize() throws FormatException {
    if (service != null) return;
    try {
      ServiceFactory factory = new ServiceFactory();
      service = factory.getInstance(JPEGXRService.class);
    }
    catch (DependencyException e) {
      throw new MissingLibraryException("JPEG-XR library not available", e);
    }
  }

}
