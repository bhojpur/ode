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

import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

import loci.common.RandomAccessInputStream;
import loci.formats.FormatException;

/**
 * This is an optimized LZW codec for use with TIFF files.
 * Most of the code is inlined, and specifics of TIFF usage of LZW
 * (known size of decompressor output; possible lengths of LZW codes; specified
 * values for <code>CLEAR</code> and <code>END_OF_INFORMATION</code> codes)
 * are taken in account.
 * <p>
 * Estimating the worst-case size of compressor output:
 * <ul>
 * <li> The worst case means that there is no compression at all, and every
 *      input byte generates code to output.
 * <li> This means that the LZW table will be full (and reset) after reading
 *      each portion of 4096-256-2-1=3837 bytes of input
 *      (first 256 codes are preallocated, 2 codes are used for CLEAR and
 *      END_OF_IFORMATION, 1 code is lost due to original bug in TIFF library
 *      that now is a feature).
 * <li> Each full portion of 3837 byte will produce in output:
 * <ul>
 * <li> 9 bits for CLEAR code;
 * <li> 9*253 + 10*512 + 11*1024 + 12*2048 = 43237 bits for character codes.
 * </ul>
 * <li> Let n=3837, m=(number of bytes in the last incomplete portion),
 *      N=(number of bytes in compressed complete portion with CLEAR code),
 *      M=(number of bytes in compressed last incomplete portion).
 *      We have inequalities:
 * <ul>
 * <li> N <= 1.41 * n
 * <li> M <= 1.41 * m
 * <li> The last incomplete portion should also include CLEAR and
 *      END_OF_INFORMATION codes; they occupy less than 3 bytes.
 * </ul>
 * Thus, we can claim than the number of bytes in compressed output never
 * exceeds 1.41*(number of input bytes)+3.
 * <p>
 */
public class LZWCodec extends WrappedCodec {
  public LZWCodec() {
    super(new ode.codecs.LZWCodec());
  }
}
