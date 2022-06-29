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

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import org.testng.annotations.Test;

public class LZWCodecTest {
  LZWCodec codec = new LZWCodec();

  @Test
  public void testCompressShortUniqueSequence() throws Exception {
    byte[] in = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    byte[] expected = { -128, 0, 0, 32, 32, 24, 16, 10, 6, 3, -126, 1, 32, -88, 8 };
    byte[] comp = codec.compress(in, null);
    assertEquals(expected, comp);
  }

  @Test
  public void testCompressShortNonUniqueSequence() throws Exception {
    byte[] in = "This is the first day of the rest of your life".getBytes(Charset.forName("UTF-8"));
    byte[] expected = {
      -128, 21, 13, 6, -109, -104, -126, 8, 32, 58, 26, 12, -94, 3, 49, -92, -28, 115, 58, 8, 12,
      -122, 19, -56, -128, -34, 102, -124, 66, -124, 7, 35, 44, 66, 45, 24, 60, -101, -50, -89, 33,
      1, -80, -46, 102, 50, -64, 64
    };
    byte[] comp = codec.compress(in, null);
    assertEquals(expected, comp);
  }

  @Test
  public void testCompressUncompressParity() throws Exception {
    for (int j = 0; j < 100; j++) {
      byte[] in = new byte[50000];
      new Random().nextBytes(in);
      byte[] comp = codec.compress(in, null);
      CodecOptions opt = new CodecOptions();
      opt.maxBytes = in.length;
      byte[] out = codec.decompress(comp, opt);
      assertEquals(in, out);
    }
  }
}