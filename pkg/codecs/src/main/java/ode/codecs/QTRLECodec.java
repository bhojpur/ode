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

import loci.common.ByteArrayHandle;
import loci.common.RandomAccessInputStream;
import ode.codecs.CodecException;
import ode.codecs.UnsupportedCompressionException;

/**
 * Methods for compressing and decompressing data using QuickTime RLE.
 */
public class QTRLECodec extends BaseCodec {

  /* @see Codec#compress(byte[], CodecOptions) */
  @Override
  public byte[] compress(byte[] data, CodecOptions options)
    throws CodecException
  {
    throw new UnsupportedCompressionException(
      "QTRLE compression not supported.");
  }

  /* @see Codec#decompress(RandomAccessInputStream, CodecOptions) */
  @Override
  public byte[] decompress(RandomAccessInputStream in, CodecOptions options)
    throws CodecException, IOException
  {
    if (in == null) 
      throw new IllegalArgumentException("No data to decompress.");
    byte[] b = new byte[(int) (in.length() - in.getFilePointer())];
    in.read(b);
    return decompress(b, options);
  }

  /**
   * The CodecOptions parameter should have the following fields set:
   *  {@link CodecOptions#width width}
   *  {@link CodecOptions#height height}
   *  {@link CodecOptions#bitsPerSample bitsPerSample}
   *  {@link CodecOptions#previousImage previousImage}
   *
   * @see Codec#decompress(byte[], CodecOptions)
   */
  @Override
  public byte[] decompress(byte[] data, CodecOptions options)
    throws CodecException
  {
    if (options == null) options = CodecOptions.getDefaultOptions();
    if (data == null || data.length == 0)
      throw new IllegalArgumentException("No data to decompress.");
    int numLines = options.height;

    if (data.length < 8) return options.previousImage;

    int bpp = options.bitsPerSample / 8;
    int line = options.width * bpp;

    try {
      ByteArrayHandle s = new ByteArrayHandle(data);
      s.skipBytes(4);

      int header = s.readShort();
      int off = 0;
      int start = 0;

      byte[] output = new byte[options.height * line];

      if ((header & 8) == 8) {
        start = s.readShort();
        s.skipBytes(2);
        numLines = s.readShort();
        s.skipBytes(2);

        if (options.previousImage != null) {
          for (int i=0; i<start; i++) {
            System.arraycopy(options.previousImage, off, output, off, line);
            off += line;
          }
        }

        if (options.previousImage != null) {
          off = line * (start + numLines);
          for (int i=start+numLines; i<options.height; i++) {
            System.arraycopy(options.previousImage, off, output, off, line);
            off += line;
          }
        }
      }
      else throw new CodecException("Unsupported header : " + header);

      // uncompress remaining lines

      int skip = 0; // number of bytes to skip
      byte rle = 0; // RLE code

      int rowPointer = start * line;

      for (int i=0; i<numLines; i++) {
        skip = s.readUnsignedByte();
        if (skip < 0) skip += 256;

        if (options.previousImage != null) {
          try {
            System.arraycopy(options.previousImage, rowPointer, output,
              rowPointer, (skip - 1) * bpp);
          }
          catch (ArrayIndexOutOfBoundsException e) { }
        }

        off = rowPointer + ((skip - 1) * bpp);
        while (true) {
          rle = (byte) (s.readUnsignedByte() & 0xff);

          if (rle == 0) {
            skip = s.readUnsignedByte();

            if (options.previousImage != null) {
              try {
                System.arraycopy(options.previousImage, off, output, off,
                  (skip - 1) * bpp);
              }
              catch (ArrayIndexOutOfBoundsException e) { }
            }

            off += (skip - 1) * bpp;
          }
          else if (rle == -1) {
            if (off < (rowPointer + line) && options.previousImage != null) {
              System.arraycopy(options.previousImage, off, output, off,
                rowPointer + line - off);
            }
            break;
          }
          else if (rle < -1) {
            // unpack next pixel and copy it to output -(rle) times
            for (int j=0; j<(-1*rle); j++) {
              if (off < output.length) {
                System.arraycopy(data, (int) s.getFilePointer(), output,
                  off, bpp);
                off += bpp;
              }
              else break;
            }
            s.skipBytes(bpp);
          }
          else {
            // copy (rle) pixels to output
            int len = rle * bpp;
            if (output.length - off < len) len = output.length - off;
            if (s.length() - s.getFilePointer() < len) {
              len = (int) (s.length() - s.getFilePointer());
            }
            if (len < 0) len = 0;
            if (off > output.length) off = output.length;
            s.read(output, off, len);
            off += len;
          }
          if (s.getFilePointer() >= s.length()) return output;
        }
        rowPointer += line;
      }
      return output;
    }
    catch (IOException e) {
      throw new CodecException(e);
    }
  }

}