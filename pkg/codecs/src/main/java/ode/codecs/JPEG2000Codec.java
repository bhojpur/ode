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

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import loci.common.ByteArrayHandle;
import loci.common.DataTools;
import loci.common.RandomAccessInputStream;
import loci.common.RandomAccessOutputStream;
import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import ode.codecs.CodecException;
import ode.codecs.MissingLibraryException;
import ode.codecs.gui.AWTImageTools;
import ode.codecs.gui.UnsignedIntBuffer;
import ode.codecs.services.JAIIIOService;
import ode.codecs.services.JAIIIOServiceImpl;

/**
 * This class implements JPEG 2000 compression and decompression.
 *
 * <dl>
 * </dl>
 */
public class JPEG2000Codec extends BaseCodec {

  // -- Fields --

  private JAIIIOService service;

  // -- Codec API methods --

  /**
   * The CodecOptions parameter should have the following fields set:
   *  {@link CodecOptions#width width}
   *  {@link CodecOptions#height height}
   *  {@link CodecOptions#bitsPerSample bitsPerSample}
   *  {@link CodecOptions#channels channels}
   *  {@link CodecOptions#interleaved interleaved}
   *  {@link CodecOptions#littleEndian littleEndian}
   *  {@link CodecOptions#lossless lossless}
   *
   * @see Codec#compress(byte[], CodecOptions)
   */
  @Override
  public byte[] compress(byte[] data, CodecOptions options)
    throws CodecException
  {
    if (data == null || data.length == 0) return data;
    initialize();

    JPEG2000CodecOptions j2kOptions;
    if (options instanceof JPEG2000CodecOptions) {
      j2kOptions = (JPEG2000CodecOptions) options;
    }
    else {
      j2kOptions = (JPEG2000CodecOptions)
        JPEG2000CodecOptions.getDefaultOptions(options);
    }

    ByteArrayHandle handle = new ByteArrayHandle();
    RandomAccessOutputStream out = new RandomAccessOutputStream(handle);
    BufferedImage img = null;

    int next = 0;

    // NB: Construct BufferedImages manually, rather than using
    // AWTImageTools.makeImage. The AWTImageTools.makeImage methods construct
    // images that are not properly handled by the JPEG2000 writer.
    // Specifically, 8-bit multi-channel images are constructed with type
    // DataBuffer.TYPE_INT (so a single int is used to store all of the
    // channels for a specific pixel).

    int plane = j2kOptions.width * j2kOptions.height;

    if (j2kOptions.bitsPerSample == 8) {
      byte[][] b = new byte[j2kOptions.channels][plane];
      if (j2kOptions.interleaved) {
        for (int q=0; q<plane; q++) {
          for (int c=0; c<j2kOptions.channels; c++) {
            if (next < data.length) {
              b[c][q] = data[next++];
            }
            else {
              break;
            }
          }
        }
      }
      else {
        for (int c=0; c<j2kOptions.channels; c++) {
          System.arraycopy(data, c * plane, b[c], 0, plane);
        }
      }
      DataBuffer buffer = new DataBufferByte(b, plane);
      img = AWTImageTools.constructImage(b.length, DataBuffer.TYPE_BYTE,
        j2kOptions.width, j2kOptions.height, false, true, buffer,
        j2kOptions.colorModel);
    }
    else if (j2kOptions.bitsPerSample == 16) {
      short[][] s = new short[j2kOptions.channels][plane];
      if (j2kOptions.interleaved) {
        for (int q=0; q<plane; q++) {
          for (int c=0; c<j2kOptions.channels; c++) {
            s[c][q] = DataTools.bytesToShort(data, next, 2,
              j2kOptions.littleEndian);
            next += 2;
          }
        }
      }
      else {
        for (int c=0; c<j2kOptions.channels; c++) {
          for (int q=0; q<plane; q++) {
            s[c][q] = DataTools.bytesToShort(data, next, 2,
              j2kOptions.littleEndian);
            next += 2;
          }
        }
      }
      DataBuffer buffer = new DataBufferUShort(s, plane);
      img = AWTImageTools.constructImage(s.length, DataBuffer.TYPE_USHORT,
        j2kOptions.width, j2kOptions.height, false, true, buffer,
        j2kOptions.colorModel);
    }
    else if (j2kOptions.bitsPerSample == 32) {
      int[][] s = new int[j2kOptions.channels][plane];
      if (j2kOptions.interleaved) {
        for (int q=0; q<plane; q++) {
          for (int c=0; c<j2kOptions.channels; c++) {
            s[c][q] = DataTools.bytesToInt(data, next, 4,
              j2kOptions.littleEndian);
            next += 4;
          }
        }
      }
      else {
        for (int c=0; c<j2kOptions.channels; c++) {
          for (int q=0; q<plane; q++) {
            s[c][q] = DataTools.bytesToInt(data, next, 4,
              j2kOptions.littleEndian);
            next += 4;
          }
        }
      }

      DataBuffer buffer = new UnsignedIntBuffer(s, plane);
      img = AWTImageTools.constructImage(s.length, DataBuffer.TYPE_INT,
        j2kOptions.width, j2kOptions.height, false, true, buffer,
        j2kOptions.colorModel);
    }

    try {
      service.writeImage(out, img, j2kOptions);
    }
    catch (IOException e) {
      throw new CodecException("Could not compress JPEG-2000 data.", e);
    }
    catch (ServiceException e) {
      throw new CodecException("Could not compress JPEG-2000 data.", e);
    }
    finally {
      try {
        out.close();
      }
      catch (IOException e) {
        throw new CodecException("Failed to close RandomAccessOutputStream.", e);
      }
    }

    try {
      RandomAccessInputStream is = new RandomAccessInputStream(handle);
      try {
        is.seek(0);
        if (!j2kOptions.writeBox) {
          while (is.getFilePointer() < is.length()) {
            // checking both 0xff4f and 0xff51 prevents this from
            // stopping at an escaped 0xff4f (i.e. 0x00ff4f) sequence
            // in the box
            while ((is.readShort() & 0xffff) != 0xff4f) {
              is.seek(is.getFilePointer() - 1);
            }
            if ((is.readShort() & 0xffff) == 0xff51) {
              break;
            }
            is.seek(is.getFilePointer() - 2);
          }
          is.seek(is.getFilePointer() - 4);
        }
        byte[] buf = new byte[(int) (is.length() - is.getFilePointer())];
        is.readFully(buf);
        return buf;
      }
      finally {
        is.close();
      }
    }
    catch (IOException e) {
    }
    return null;
  }

  /**
   * The CodecOptions parameter should have the following fields set:
   * {@link CodecOptions#interleaved interleaved}
   * {@link CodecOptions#littleEndian littleEndian}
   *
   * @see Codec#decompress(RandomAccessInputStream, CodecOptions)
   */
  @Override
  public byte[] decompress(RandomAccessInputStream in, CodecOptions options)
    throws CodecException, IOException
  {
    if (in == null) {
      throw new IllegalArgumentException("No data to decompress.");
    }
    if (options == null || !(options instanceof JPEG2000CodecOptions)) {
      options = JPEG2000CodecOptions.getDefaultOptions(options);
    }

    byte[] buf = null;
    long fp = in.getFilePointer();
    if (options.maxBytes == 0) {
      buf = new byte[(int) (in.length() - fp)];
    }
    else {
      buf = new byte[(int) (options.maxBytes - fp)];
    }
    in.read(buf);
    return decompress(buf, options);
  }

  /**
   * The CodecOptions parameter should have the following fields set:
   * {@link CodecOptions#interleaved interleaved}
   * {@link CodecOptions#littleEndian littleEndian}
   *
   * @see Codec#decompress(byte[], CodecOptions)
   */
  @Override
  public byte[] decompress(byte[] buf, CodecOptions options)
    throws CodecException
  {
    initialize();

    if (options == null || !(options instanceof JPEG2000CodecOptions)) {
      options = JPEG2000CodecOptions.getDefaultOptions(options);
    }
    else {
      options = new JPEG2000CodecOptions(options);
    }

    byte[][] single = null;
    WritableRaster b = null;
    int bpp = options.bitsPerSample / 8;

    try {
      ByteArrayInputStream bis = new ByteArrayInputStream(buf);
      b = (WritableRaster) service.readRaster(
          bis, (JPEG2000CodecOptions) options);
      single = AWTImageTools.getPixelBytes(b, options.littleEndian);
      bpp = single[0].length / (b.getWidth() * b.getHeight());

      bis.close();
      b = null;
    }
    catch (IOException e) {
      throw new CodecException("Could not decompress JPEG2000 image. Please " +
        "make sure that jai_imageio.jar is installed.", e);
    }
    catch (ServiceException e) {
      throw new CodecException("Could not decompress JPEG2000 image. Please " +
        "make sure that jai_imageio.jar is installed.", e);
    }

    if (single.length == 1) return single[0];
    byte[] rtn = new byte[single.length * single[0].length];
    if (options.interleaved) {
      int next = 0;
      for (int i=0; i<single[0].length/bpp; i++) {
        for (int j=0; j<single.length; j++) {
          for (int bb=0; bb<bpp; bb++) {
            rtn[next++] = single[j][i * bpp + bb];
          }
        }
      }
    }
    else {
      for (int i=0; i<single.length; i++) {
        System.arraycopy(single[i], 0, rtn, i * single[0].length,
          single[i].length);
      }
    }
    single = null;

    return rtn;
  }

  // -- Helper methods --

  /**
   * Initializes the JAI ImageIO dependency service. This is called at the
   * beginning of the {@link #compress} and {@link #decompress} methods to
   * avoid having the constructor's method definition contain a checked
   * exception.
   *
   * @throws CodecException If there is an error initializing JAI ImageIO
   *   services.
   */
  private void initialize() throws CodecException {
    if (service != null) return;
    try {
      ServiceFactory factory = new ServiceFactory();
      service = factory.getInstance(JAIIIOService.class);
    }
    catch (DependencyException de) {
      throw new MissingLibraryException(JAIIIOServiceImpl.NO_J2K_MSG, de);
    }
  }

}