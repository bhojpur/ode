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
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

import javax.imageio.ImageIO;

import loci.common.DataTools;
import loci.common.RandomAccessInputStream;
import ode.codecs.CodecException;
import ode.codecs.gui.AWTImageTools;

/**
 * This class implements JPEG compression and decompression.
 */
public class JPEGCodec extends BaseCodec {

  /**
   * The CodecOptions parameter should have the following fields set:
   *  {@link CodecOptions#width width}
   *  {@link CodecOptions#height height}
   *  {@link CodecOptions#channels channels}
   *  {@link CodecOptions#bitsPerSample bitsPerSample}
   *  {@link CodecOptions#interleaved interleaved}
   *  {@link CodecOptions#littleEndian littleEndian}
   *  {@link CodecOptions#signed signed}
   *
   * @see Codec#compress(byte[], CodecOptions)
   */
  @Override
  public byte[] compress(byte[] data, CodecOptions options)
    throws CodecException
  {
    if (data == null || data.length == 0) return data;
    if (options == null) options = CodecOptions.getDefaultOptions();

    if (options.bitsPerSample > 8) {
      throw new CodecException("> 8 bit data cannot be compressed with JPEG.");
    }

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    BufferedImage img = AWTImageTools.makeImage(data, options.width,
      options.height, options.channels, options.interleaved,
      options.bitsPerSample / 8, false, options.littleEndian, options.signed);

    try {
      ImageIO.write(img, "jpeg", out);
    }
    catch (IOException e) {
      throw new CodecException("Could not write JPEG data", e);
    }
    return out.toByteArray();
  }

  /**
   * The CodecOptions parameter should have the following fields set:
   *  {@link CodecOptions#interleaved interleaved}
   *  {@link CodecOptions#littleEndian littleEndian}
   *
   * @see Codec#decompress(RandomAccessInputStream, CodecOptions)
   */
  @Override
  public byte[] decompress(RandomAccessInputStream in, CodecOptions options)
    throws CodecException, IOException
  {
    BufferedImage b;
    long fp = in.getFilePointer();
    try {
      try {
        while (in.read() != (byte) 0xff || in.read() != (byte) 0xd8);
        in.seek(in.getFilePointer() - 2);
      }
      catch (EOFException e) {
        in.seek(fp);
      }

      b = ImageIO.read(new BufferedInputStream(new DataInputStream(in), 81920));
    }
    catch (IOException exc) {
      // probably a lossless JPEG; delegate to LosslessJPEGCodec
      in.seek(fp);
      return new LosslessJPEGCodec().decompress(in, options);
    }

    if (options == null) options = CodecOptions.getDefaultOptions();

    int nPixels = b.getWidth() * b.getHeight();
    WritableRaster r = (WritableRaster) b.getRaster();
    if (!options.ycbcr && r.getDataBuffer() instanceof DataBufferByte &&
      b.getType() == BufferedImage.TYPE_BYTE_GRAY)
    {
      DataBufferByte bb = (DataBufferByte) r.getDataBuffer();

      if (bb.getNumBanks() == 1) {
        byte[] raw = bb.getData();
        if (options.interleaved || bb.getSize() == nPixels) {
          return raw;
        }
      }
    }

    byte[][] buf = AWTImageTools.getPixelBytes(b, options.littleEndian);

    // correct for YCbCr encoding, if necessary
    if (options.ycbcr && buf.length == 3) {
      int nBytes = buf[0].length / (b.getWidth() * b.getHeight());
      int mask = (int) (Math.pow(2, nBytes * 8) - 1);
      for (int i=0; i<buf[0].length; i+=nBytes) {
        double y = DataTools.bytesToInt(buf[0], i, nBytes, options.littleEndian);
        double cb = DataTools.bytesToInt(buf[1], i, nBytes, options.littleEndian);
        double cr = DataTools.bytesToInt(buf[2], i, nBytes, options.littleEndian);

        cb = Math.max(0, cb - 128);
        cr = Math.max(0, cr - 128);

        int red = (int) (y + 1.402 * cr);
        int green = (int) (y - 0.34414 * cb - 0.71414 * cr);
        int blue = (int) (y + 1.772 * cb);

        red = (int) (Math.min(red, mask)) & mask;
        green = (int) (Math.min(green, mask)) & mask;
        blue = (int) (Math.min(blue, mask)) & mask;

        DataTools.unpackBytes(red, buf[0], i, nBytes, options.littleEndian);
        DataTools.unpackBytes(green, buf[1], i, nBytes, options.littleEndian);
        DataTools.unpackBytes(blue, buf[2], i, nBytes, options.littleEndian);
      }
    }

    byte[] rtn = new byte[buf.length * buf[0].length];
    if (buf.length == 1) rtn = buf[0];
    else {
      if (options.interleaved) {
        int next = 0;
        for (int i=0; i<buf[0].length; i++) {
          for (int j=0; j<buf.length; j++) {
            rtn[next++] = buf[j][i];
          }
        }
      }
      else {
        for (int i=0; i<buf.length; i++) {
          System.arraycopy(buf[i], 0, rtn, i*buf[0].length, buf[i].length);
        }
      }
    }
    return rtn;
  }
}