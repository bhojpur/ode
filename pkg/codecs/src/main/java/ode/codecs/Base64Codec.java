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

/**
 * Implements encoding (compress) and decoding (decompress) methods
 * for Base64.  This code was adapted from the Jakarta Commons Codec source,
 * http://jakarta.apache.org/commons
 */
public class Base64Codec extends BaseCodec {

  // Base64 alphabet and codes

  private static final byte PAD = (byte) '=';

  private static byte[] base64Alphabet = new byte[255];
  private static byte[] lookupBase64Alphabet = new byte[255];

  static {
    for (int i=0; i<255; i++) {
      base64Alphabet[i] = (byte) -1;
    }
    for (int i = 'Z'; i >= 'A'; i--) {
      base64Alphabet[i] = (byte) (i - 'A');
      lookupBase64Alphabet[i - 'A'] = (byte) i;
    }
    for (int i = 'z'; i >= 'a'; i--) {
      base64Alphabet[i] = (byte) (i - 'a' + 26);
      lookupBase64Alphabet[i - 'a' + 26] = (byte) i;
    }
    for (int i = '9'; i >= '0'; i--) {
      base64Alphabet[i] = (byte) (i - '0' + 52);
      lookupBase64Alphabet[i - '0' + 52] = (byte) i;
    }

    base64Alphabet['+'] = 62;
    base64Alphabet['/'] = 63;

    lookupBase64Alphabet[62] = (byte) '+';
    lookupBase64Alphabet[63] = (byte) '/';
  }

  /* @see Codec#compress(byte[], CodecOptions) */
  @Override
  public byte[] compress(byte[] input, CodecOptions options)
    throws CodecException
  {
    if (input == null || input.length == 0) return null;
    int dataBits = input.length * 8;
    int fewerThan24 = dataBits % 24;
    int numTriples = dataBits / 24;
    ByteVector encoded = new ByteVector();

    byte k, l, b1, b2, b3;

    int dataIndex = 0;

    for (int i=0; i<numTriples; i++) {
      dataIndex = i * 3;
      b1 = input[dataIndex];
      b2 = input[dataIndex + 1];
      b3 = input[dataIndex + 2];

      l = (byte) (b2 & 0x0f);
      k = (byte) (b1 & 0x03);

      byte v1 = ((b1 & -128) == 0) ? (byte) (b1 >> 2) :
        (byte) ((b1) >> 2 ^ 0xc0);
      byte v2 = ((b2 & -128) == 0) ? (byte) (b2 >> 4) :
        (byte) ((b2) >> 4 ^ 0xf0);
      byte v3 = ((b3 & -128) == 0) ? (byte) (b3 >> 6) :
        (byte) ((b3) >> 6 ^ 0xfc);

      encoded.add(lookupBase64Alphabet[v1]);
      encoded.add(lookupBase64Alphabet[v2 | (k << 4)]);
      encoded.add(lookupBase64Alphabet[(l << 2) | v3]);
      encoded.add(lookupBase64Alphabet[b3 & 0x3f]);
    }

    dataIndex = numTriples * 3;

    if (fewerThan24 == 8) {
      b1 = input[dataIndex];
      k = (byte) (b1 & 0x03);
      byte v = ((b1 & -128) == 0) ? (byte) (b1 >> 2) :
        (byte) ((b1) >> 2 ^ 0xc0);
      encoded.add(lookupBase64Alphabet[v]);
      encoded.add(lookupBase64Alphabet[k << 4]);
      encoded.add(PAD);
      encoded.add(PAD);
    }
    else if (fewerThan24 == 16) {
      b1 = input[dataIndex];
      b2 = input[dataIndex + 1];
      l = (byte) (b2 & 0x0f);
      k = (byte) (b1 & 0x03);

      byte v1 = ((b1 & -128) == 0) ? (byte) (b1 >> 2) :
        (byte) ((b1) >> 2 ^ 0xc0);
      byte v2 = ((b2 & -128) == 0) ? (byte) (b2 >> 4) :
        (byte) ((b2) >> 4 ^ 0xf0);

      encoded.add(lookupBase64Alphabet[v1]);
      encoded.add(lookupBase64Alphabet[v2 | (k << 4)]);
      encoded.add(lookupBase64Alphabet[l << 2]);
      encoded.add(PAD);
    }

    return encoded.toByteArray();
  }

  /* @see Codec#decompress(RandomAccessInputStream, CodecOptions) */
  @Override
  public byte[] decompress(RandomAccessInputStream in, CodecOptions options)
    throws CodecException, IOException
  {
    if (in == null)
      throw new IllegalArgumentException("No data to decompress.");
    if (in.length() == 0) return new byte[0];

    byte b3 = 0, b4 = 0, marker0 = 0, marker1 = 0;

    ByteVector decodedData = new ByteVector();

    byte[] block = new byte[8192];
    int nRead = in.read(block);
    int p = 0;
    byte b1 = base64Alphabet[block[p++]];
    byte b2 = base64Alphabet[block[p++]];

    while (b1 != -1 && b2 != -1 &&
      (in.getFilePointer() - nRead + p < in.length()))
    {
      marker0 = block[p++];
      marker1 = block[p++];

      if (p == block.length) {
        nRead = in.read(block);
        p = 0;
      }

      decodedData.add((byte) (b1 << 2 | b2 >> 4));
      if (p >= nRead && in.getFilePointer() >= in.length()) break;
      if (marker0 != PAD && marker1 != PAD) {
        b3 = base64Alphabet[marker0];
        b4 = base64Alphabet[marker1];

        decodedData.add((byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf)));
        decodedData.add((byte) (b3 << 6 | b4));
      }
      else if (marker0 == PAD) {
        decodedData.add((byte) 0);
        decodedData.add((byte) 0);
      }
      else if (marker1 == PAD) {
        b3 = base64Alphabet[marker0];

        decodedData.add((byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf)));
        decodedData.add((byte) 0);
      }
      b1 = base64Alphabet[block[p++]];
      b2 = base64Alphabet[block[p++]];
    }
    return decodedData.toByteArray();
  }

}