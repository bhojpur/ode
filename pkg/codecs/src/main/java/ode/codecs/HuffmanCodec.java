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
import java.util.HashMap;

import loci.common.ByteArrayHandle;
import loci.common.RandomAccessInputStream;
import ode.codecs.CodecException;
import ode.codecs.UnsupportedCompressionException;

/**
 * This class implements Huffman decoding.
 */
public class HuffmanCodec extends BaseCodec {

  // -- Constants --

  private static final int LEAVES_OFFSET = 16;

  // -- Fields --

  private int leafCounter;

  private HashMap<short[], Decoder> cachedDecoders =
    new HashMap<short[], Decoder>();

  // -- Codec API methods --

  /* @see Codec#compress(byte[], CodecOptions) */
  @Override
  public byte[] compress(byte[] data, CodecOptions options)
    throws CodecException
  {
    throw new UnsupportedCompressionException(
      "Huffman encoding not currently supported");
  }

  /**
   * The CodecOptions parameter must be an instance of
   * {@link HuffmanCodecOptions}, and should have the following fields set:
   *   {@link HuffmanCodecOptions#table table}
   *   {@link CodecOptions#bitsPerSample bitsPerSample}
   *   {@link CodecOptions#maxBytes maxBytes}
   *
   * @see Codec#decompress(RandomAccessInputStream, CodecOptions)
   */
  @Override
  public byte[] decompress(RandomAccessInputStream in, CodecOptions options)
    throws CodecException, IOException
  {
    if (in == null) 
      throw new IllegalArgumentException("No data to decompress.");
    if (options == null || !(options instanceof HuffmanCodecOptions)) {
      throw new CodecException("Options must be an instance of " +
        "ode.codecs.HuffmanCodecOptions.");
    }

    HuffmanCodecOptions huffman = (HuffmanCodecOptions) options;

    int nSamples = (huffman.maxBytes * 8) / huffman.bitsPerSample;
    int bytesPerSample = huffman.bitsPerSample / 8;
    if ((huffman.bitsPerSample % 8) != 0) bytesPerSample++;

    BitWriter out = new BitWriter();

    for (int i=0; i<nSamples; i++) {
      int sample = getSample(in, options);
      out.write(sample, bytesPerSample * 8);
    }

    return out.toByteArray();
  }

  // -- HuffmanCodec API methods --

  @Deprecated
  public int getSample(BitBuffer bb, CodecOptions options)
    throws CodecException
  {
    RandomAccessInputStream s = null;
    try {
      try {
        s = new RandomAccessInputStream(new ByteArrayHandle(bb.getByteBuffer()));
        return getSample(s, options);
      }
      finally {
        s.close();
      }
    }
    catch (IOException e) {
      throw new CodecException(e);
    }
  }

  public int getSample(RandomAccessInputStream bb, CodecOptions options)
    throws CodecException
  {
    if (bb == null) {
      throw new IllegalArgumentException("No data to handle.");
    }
    if (options == null || !(options instanceof HuffmanCodecOptions)) {
      throw new CodecException("Options must be an instance of " +
        "ode.codecs.HuffmanCodecOptions.");
    }

    HuffmanCodecOptions huffman = (HuffmanCodecOptions) options;
    Decoder decoder = cachedDecoders.get(huffman.table);
    if (decoder == null) {
      decoder = new Decoder(huffman.table);
      cachedDecoders.put(huffman.table, decoder);
    }

    try {
      int bitCount = decoder.decode(bb);
      if (bitCount == 16) {
        return 0x8000;
      }
      if (bitCount < 0) bitCount = 0;
      int v = bb.readBits(bitCount) & ((int) Math.pow(2, bitCount) - 1);
      if ((v & (1 << (bitCount - 1))) == 0) {
        v -= (1 << bitCount) - 1;
      }
      return v;
    }
    catch (IOException e) {
      throw new CodecException(e);
    }
  }

  // -- Helper class --

  class Decoder {
    public Decoder[] branch = new Decoder[2];
    private int leafValue = -1;

    public Decoder() { }

    public Decoder(short[] source) {
      leafCounter = 0;
      createDecoder(this, source, 0, 0);
    }

    private Decoder createDecoder(short[] source, int start, int level) {
      Decoder dest = new Decoder();
      createDecoder(dest, source, start, level);
      return dest;
    }

    private void createDecoder(Decoder dest, short[] source, int start,
      int level)
    {
      int next = 0;
      int i = 0;
      while (i <= leafCounter && next < LEAVES_OFFSET) {
        i += source[start + next++] & 0xff;
      }

      if (level < next && next <= LEAVES_OFFSET) {
        dest.branch[0] = createDecoder(source, start, level + 1);
        dest.branch[1] = createDecoder(source, start, level + 1);
      }
      else {
        i = start + LEAVES_OFFSET + leafCounter++;
        if (i < source.length) {
          dest.leafValue = source[i] & 0xff;
        }
      }
    }

    public int decode(RandomAccessInputStream bb) throws IOException {
      Decoder d = this;
      while (d.branch[0] != null) {
        int v = bb.readBits(1);
        if (v < 0) break; // eof
        d = d.branch[v];
      }
      return d.leafValue;
    }

  }

}