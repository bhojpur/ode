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
 * This class implements LZO decompression. Compression is not yet
 * implemented.
 */
public class LZOCodec extends BaseCodec {

  // LZO compression codes
  private static final int LZO_OVERRUN = -6;

  /* @see Codec#compress(byte[], CodecOptions) */
  @Override
  public byte[] compress(byte[] data, CodecOptions options)
    throws CodecException
  {
    // TODO: Add LZO compression support.
    throw new UnsupportedCompressionException(
      "LZO Compression not currently supported");
  }

  /* @see Codec#decompress(RandomAccessInputStream, CodecOptions) */
  @Override
  public byte[] decompress(RandomAccessInputStream in, CodecOptions options)
    throws CodecException, IOException
  {
    if (in == null) 
      throw new IllegalArgumentException("No data to decompress.");
    // Adapted from LZO for Java, available at
    // http://www.oberhumer.com/opensource/lzo/
    ByteVector dst = new ByteVector();
    int t = in.read() & 0xff;
    int mPos;

    if (t > 17) {
      t -= 17;
      // do dst[op++] = src[ip++]; while (--t > 0);
      byte[] b = new byte[t];
      in.read(b);
      dst.add(b);
      t = in.read() & 0xff;
//      if (t < 16) return;
      if(t < 16) {
        return dst.toByteArray();
      }
    }

  loop:
    for (;; t = in.read() & 0xff) {
      if (t < 16) {
        if (t == 0) {
          byte f = in.readByte();
          while (f == 0) {
            t += 255;
            f = in.readByte();
          }
          t += 15 + (f & 0xff);
        }
        t += 3;
        // do dst[op++] = src[ip++]; while (--t > 0);
        byte[] b = new byte[t];
        in.read(b);
        dst.add(b);
        t = in.read() & 0xff;
        if (t < 16) {
          mPos = dst.size() - 0x801 - (t >> 2) - ((in.read() & 0xff) << 2);
          if (mPos < 0) {
            t = LZO_OVERRUN;
            break loop;
          }
          t = 3;
          do {
            dst.add(dst.get(mPos++));
          } while (--t > 0);
//          do dst[op++] = dst[mPos++]; while (--t > 0);
          in.seek(in.getFilePointer() - 2);
          t = in.read() & 3;
          in.skipBytes(1);
          if (t == 0) continue;
//          do dst[op++] = src[ip++]; while (--t > 0);
          b = new byte[t];
          in.read(b);
          dst.add(b);
          t = in.read() & 0xff;
        }
      }
      for (;; t = in.read() & 0xff) {
        if (t >= 64) {
          mPos = dst.size() - 1 - ((t >> 2) & 7) - ((in.read() & 0xff) << 3);
          t = (t >> 5) - 1;
        }
        else if (t >= 32) {
          t &= 31;
          if (t == 0) {
            byte f = in.readByte();
            while (f == 0) {
              t += 255;
              f = in.readByte();
            }
            t += 31 + (f & 0xff);
          }
          mPos = dst.size() - 1 - ((in.read() & 0xff) >> 2);
          mPos -= ((in.read() & 0xff) << 6);
        }
        else if (t >= 16) {
          mPos = dst.size() - ((t & 8) << 11);
          t &= 7;
          if (t == 0) {
            byte f = in.readByte();
            while (f == 0) {
              t += 255;
              f = in.readByte();
            }
            t += 7 + (f & 0xff);
          }
          mPos -= ((in.read() & 0xff) >> 2);
          mPos -= ((in.read() & 0xff) << 6);
          if (mPos == dst.size()) break loop;
          mPos -= 0x4000;
        }
        else {
          mPos = dst.size() - 1 - (t >> 2) - ((in.read() & 0xff) << 2);
          t = 0;
        }

        if (mPos < 0) {
          t = LZO_OVERRUN;
          break loop;
        }

        t += 2;
//        do dst[op++] = dst[mPos++]; while (--t > 0);
        do {
          dst.add(dst.get(mPos++));
        } while (--t > 0);
        in.seek(in.getFilePointer() - 2);
        t = in.read() & 3;
        in.skipBytes(1);
        if (t == 0) break;
//        do dst[op++] = src[ip++]; while (--t > 0);
        byte[] b = new byte[t];
        in.read(b);
        dst.add(b);
        t = 0;
      }
    }
    return dst.toByteArray();
  }
}