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
import java.util.ArrayList;

import loci.common.RandomAccessInputStream;
import ode.codecs.CodecException;
import ode.codecs.UnsupportedCompressionException;

/**
 * Methods for compressing and decompressing QuickTime Motion JPEG-B data.
 */
public class MJPBCodec extends BaseCodec {

  // -- Constants --

  private static final byte[] HEADER = new byte[] {
    (byte) 0xff, (byte) 0xd8, 0, 16, 0x4a, 0x46, 0x49, 0x46, 0,
    1, 1, 0, 0x48, 0x48, 0, 0
  };

  // -- Codec API methods --

  /* @see Codec#compress(byte[], CodecOptions) */
  @Override
  public byte[] compress(byte[] data, CodecOptions options)
    throws CodecException
  {
    throw new UnsupportedCompressionException(
      "Motion JPEG-B compression not supported.");
  }

  /**
   * The CodecOptions parameter must be an instance of {@link MJPBCodecOptions},
   * and should have the following fields set:
   *  {@link MJPBCodecOptions#interlaced interlaced}
   *  {@link CodecOptions#width width}
   *  {@link CodecOptions#height height}
   *  {@link CodecOptions#bitsPerSample bitsPerSample}
   *  {@link CodecOptions#littleEndian littleEndian}
   *  {@link CodecOptions#interleaved interleaved}
   *
   * @see Codec#decompress(RandomAccessInputStream, CodecOptions)
   */
  @Override
  public byte[] decompress(RandomAccessInputStream in, CodecOptions options)
    throws CodecException, IOException
  {
    if (options == null) options = CodecOptions.getDefaultOptions();
    if (!(options instanceof MJPBCodecOptions)) {
      throw new CodecException("Options must be an instance of " +
        "ode.codecs.MJPBCodecOptions");
    }

    byte[] raw = null;
    byte[] raw2 = null;
    ArrayList<Table> huffmanTables = new ArrayList<Table>();

    long fp = in.getFilePointer();

    try {
      in.skipBytes(4);

      byte[] lumDcBits = null, lumAcBits = null, lumDc = null, lumAc = null;
      byte[] quant = null;
      byte[] quant2 = null;

      String s1 = in.readString(4);
      in.skipBytes(12);
      String s2 = in.readString(4);
      in.seek(in.getFilePointer() - 4);
      if (s1.equals("mjpg") || s2.equals("mjpg")) {
        int extra = 16;
        if (s1.startsWith("m")) {
          extra = 0;
          in.seek(fp + 4);
        }
        in.skipBytes(12);

        int offset = in.readInt() + extra;
        int quantOffset = in.readInt() + extra;
        int huffmanOffset = in.readInt() + extra;
        int sof = in.readInt() + extra;
        int sos = in.readInt() + extra;
        int sod = in.readInt() + extra;

        if (quantOffset != 0 && quantOffset + fp < in.length()) {
          in.seek(fp + quantOffset);
          int length = in.readShort();
          int index = in.read();
          quant = new byte[64];
          in.read(quant);
          if (length + fp + quantOffset > in.getFilePointer()) {
            index = in.read();
            quant2 = new byte[64];
            in.read(quant2);
          }
        }
        else {
          quant = new byte[] {
            7, 5, 5, 6, 5, 5, 7, 6, 6, 6, 8, 7, 7, 8, 10, 17, 11, 10, 9, 9, 10,
            20, 15, 15, 12, 17, 24, 21, 25, 25, 23, 21, 23, 23, 26, 29, 37, 32,
            26, 28, 35, 28, 23, 23, 33, 44, 33, 35, 39, 40, 42, 42, 42, 25, 31,
            46, 49, 45, 41, 49, 37, 41, 42, 40
          };
        }

        if (huffmanOffset != 0 && huffmanOffset + fp < in.length()) {
          in.seek(fp + huffmanOffset);
          int length = in.readShort();
          while (in.getFilePointer() < fp + huffmanOffset + length) {
            huffmanTables.add(readTable(in));
          }
        }

        in.seek(fp + sof + 7);

        int channels = in.read() & 0xff;

        int[] sampling = new int[channels];
        for (int i=0; i<channels; i++) {
          in.skipBytes(1);
          sampling[i] = in.read();
          in.skipBytes(1);
        }

        in.seek(fp + sos + 3);
        int[] tables = new int[channels];
        for (int i=0; i<channels; i++) {
          in.skipBytes(1);
          tables[i] = in.read();
        }

        in.seek(fp + sod);
        int numBytes = (int) (offset - in.getFilePointer());
        if (offset == 0) numBytes = (int) (in.length() - in.getFilePointer());
        raw = new byte[numBytes];
        in.read(raw);

        if (offset != 0) {
          in.seek(fp + offset + 36);
          int n = in.readInt();
          in.skipBytes(n);
          in.seek(in.getFilePointer() - 40);

          numBytes = (int) (in.length() - in.getFilePointer());
          raw2 = new byte[numBytes];
          in.read(raw2);
        }
      }

      // insert zero after each byte equal to 0xff
      ByteVector b = new ByteVector();
      for (int i=0; i<raw.length; i++) {
        b.add((byte) raw[i]);
        if (raw[i] == (byte) 0xff) {
          b.add((byte) 0);
        }
      }

      if (raw2 == null) raw2 = new byte[0];
      ByteVector b2 = new ByteVector();
      for (int i=0; i<raw2.length; i++) {
        b2.add((byte) raw2[i]);
        if (raw2[i] == (byte) 0xff) {
          b2.add((byte) 0);
        }
      }

      // assemble fake JPEG plane

      ByteVector v = new ByteVector(1000);
      v.add(HEADER);

      v.add(new byte[] {(byte) 0xff, (byte) 0xdb});

      int length = 4 + quant.length*2;
      v.add((byte) ((length >>> 8) & 0xff));
      v.add((byte) (length & 0xff));
      v.add((byte) 0);
      v.add(quant);

      v.add((byte) 1);
      v.add(quant2 == null ? quant : quant2);

      v.add(new byte[] {(byte) 0xff, (byte) 0xc4});
      length = 2;
      for (Table t : huffmanTables) {
        length += t.content.length;
        length++;
      }
      v.add((byte) ((length >>> 8) & 0xff));
      v.add((byte) (length & 0xff));

      for (Table t : huffmanTables) {
        v.add(t.position);
        v.add(t.content);
      }

      v.add((byte) 0xff);
      v.add((byte) 0xc0);

      length = (options.bitsPerSample >= 40) ? 11 : 17;
      v.add((byte) ((length >>> 8) & 0xff));
      v.add((byte) (length & 0xff));

      int fieldHeight = options.height;
      if (((MJPBCodecOptions) options).interlaced) fieldHeight /= 2;
      if (options.height % 2 == 1) fieldHeight++;

      int c = options.bitsPerSample == 24 ? 3 :
        (options.bitsPerSample == 32 ? 4 : 1);

      v.add(options.bitsPerSample >= 40 ? (byte) (options.bitsPerSample - 32) :
        (byte) (options.bitsPerSample / c));
      v.add((byte) ((fieldHeight >>> 8) & 0xff));
      v.add((byte) (fieldHeight & 0xff));
      v.add((byte) ((options.width >>> 8) & 0xff));
      v.add((byte) (options.width & 0xff));
      v.add((options.bitsPerSample >= 40) ? (byte) 1 : (byte) 3);

      v.add((byte) 1);
      v.add((byte) 33);
      v.add((byte) 0);

      if (options.bitsPerSample < 40) {
        v.add((byte) 2);
        v.add((byte) 17);
        v.add((byte) 1);
        v.add((byte) 3);
        v.add((byte) 17);
        v.add((byte) 1);
      }

      v.add((byte) 0xff);
      v.add((byte) 0xda);

      length = (options.bitsPerSample >= 40) ? 8 : 12;
      v.add((byte) ((length >>> 8) & 0xff));
      v.add((byte) (length & 0xff));

      v.add((options.bitsPerSample >= 40) ? (byte) 1 : (byte) 3);
      v.add((byte) 1);
      v.add((byte) 0);

      if (options.bitsPerSample < 40) {
        v.add((byte) 2);
        v.add((byte) 0x11);
        v.add((byte) 3);
        v.add((byte) 0x11);
      }

      v.add((byte) 0);
      v.add((byte) 0x3f);
      v.add((byte) 0);

      if (((MJPBCodecOptions) options).interlaced) {
        ByteVector v2 = new ByteVector(v.size());
        v2.add(v.toByteArray());

        v.add(b.toByteArray());
        v.add((byte) 0xff);
        v.add((byte) 0xd9);
        v2.add(b2.toByteArray());
        v2.add((byte) 0xff);
        v2.add((byte) 0xd9);

        JPEGCodec jpeg = new JPEGCodec();
        byte[] top = jpeg.decompress(v.toByteArray(), options);
        byte[] bottom = jpeg.decompress(v2.toByteArray(), options);

        int bpp = options.bitsPerSample < 40 ? options.bitsPerSample / 8 :
          (options.bitsPerSample - 32) / 8;
        int ch = options.bitsPerSample < 40 ? 3 : 1;
        byte[] result = new byte[options.width * options.height * bpp * ch];

        int topNdx = 0;
        int bottomNdx = 0;

        int row = options.width * bpp;

        for (int yy=0; yy<options.height; yy++) {
          if (yy % 2 == 0 && (topNdx + 1) * row <= top.length) {
            System.arraycopy(top, topNdx*row, result, yy*row, row);
            topNdx++;
          }
          else {
            if ((bottomNdx + 1) * row <= bottom.length) {
              System.arraycopy(bottom, bottomNdx*row, result, yy*row, row);
            }
            bottomNdx++;
          }
        }

        return result;
      }
      else {
        v.add(b.toByteArray());
        v.add((byte) 0xff);
        v.add((byte) 0xd9);
        return new JPEGCodec().decompress(v.toByteArray(), options);
      }
    }
    catch (IOException e) {
      throw new CodecException(e);
    }
  }

  private Table readTable(RandomAccessInputStream s) throws IOException {
    Table t = new Table();
    t.position = s.readByte();

    byte[] bits = new byte[16];
    s.read(bits);
    int sum = 0;
    for (byte bit : bits) {
      sum += (bit & 0xff);
    }
    t.content = new byte[bits.length + sum];
    System.arraycopy(bits, 0, t.content, 0, bits.length);
    s.read(t.content, bits.length, t.content.length - bits.length);
    return t;
  }

  class Table {
    public byte position;
    public byte[] content;
  }

}