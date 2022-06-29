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
import java.io.ByteArrayInputStream;
import java.io.IOException;

import loci.common.DataTools;
import loci.common.RandomAccessInputStream;
import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import ode.codecs.CodecException;
import ode.codecs.MissingLibraryException;
import ode.codecs.UnsupportedCompressionException;
import ode.codecs.services.LuraWaveService;
import ode.codecs.services.LuraWaveServiceImpl;

/**
 * This class provides LuraWave decompression, using LuraWave's Java decoding
 * library. Compression is not supported. Decompression requires a LuraWave
 * license code, specified in the lurawave.license system property (e.g.,
 * <code>-Dlurawave.license=XXXX</code> on the command line).
 */
public class LuraWaveCodec extends BaseCodec {

  // -- Fields --

  private LuraWaveService service;

  // -- Codec API methods --

  /* @see Codec#compress(byte[], CodecOptions) */
  @Override
  public byte[] compress(byte[] data, CodecOptions options)
    throws CodecException
  {
    throw new UnsupportedCompressionException(
      "LuraWave compression not supported");
  }

  /* @see Codec#decompress(RandomAccessInputStream, CodecOptions) */
  @Override
  public byte[] decompress(RandomAccessInputStream in, CodecOptions options)
    throws CodecException, IOException
  {
    byte[] buf = new byte[(int) in.length()];
    in.read(buf);
    return decompress(buf, options);
  }

  /**
   * The CodecOptions parameter should have the following fields set:
   *  {@link CodecOptions#maxBytes maxBytes}
   *
   * @see Codec#decompress(byte[], CodecOptions)
   */
  @Override
  public byte[] decompress(byte[] buf, CodecOptions options)
    throws CodecException
  {
    initialize();

    BufferedInputStream stream =
      new BufferedInputStream(new ByteArrayInputStream(buf), 4096);
    try {
      service.initialize(stream);
    }
    catch (DependencyException e) {
      throw new CodecException(LuraWaveServiceImpl.NO_LICENSE_MSG, e);
    }
    catch (ServiceException e) {
      throw new CodecException(LuraWaveServiceImpl.INVALID_LICENSE_MSG, e);
    }
    catch (IOException e) {
      throw new CodecException(e);
    }

    int w = service.getWidth();
    int h = service.getHeight();

    int nbits = 8 * (options.maxBytes / (w * h));

    if (nbits == 8) {
      byte[] image8 = new byte[w * h];
      try {
        service.decodeToMemoryGray8(image8, -1, 1024, 0);
      }
      catch (ServiceException e) {
        throw new CodecException(LuraWaveServiceImpl.INVALID_LICENSE_MSG, e);
      }
      return image8;
    }
    else if (nbits == 16) {
      short[] image16 = new short[w * h];
      try {
        service.decodeToMemoryGray16(image16, 0, -1, 1024, 0, 1, w, 0, 0, w, h);
      }
      catch (ServiceException e) {
        throw new CodecException(LuraWaveServiceImpl.INVALID_LICENSE_MSG, e);
      }

      byte[] output = new byte[w * h * 2];
      for (int i=0; i<image16.length; i++) {
        DataTools.unpackBytes(image16[i], output, i * 2, 2, true);
      }
      return output;
    }

    throw new CodecException("Unsupported bits per pixel: " + nbits);
  }

  // -- Helper methods --

  /**
   * Initializes the LuraWave dependency service. This is called at the
   * beginning of the {@link #decompress} method to avoid having the
   * constructor's method definition contain a checked exception.
   *
   * @throws CodecException If there is an error initializing LuraWave
   * services.
   */
  private void initialize() throws CodecException {
    if (service != null) return;
    try {
      ServiceFactory factory = new ServiceFactory();
      service = factory.getInstance(LuraWaveService.class);
    }
    catch (DependencyException e) {
      throw new MissingLibraryException(LuraWaveServiceImpl.NO_LURAWAVE_MSG, e);
    }
  }

}