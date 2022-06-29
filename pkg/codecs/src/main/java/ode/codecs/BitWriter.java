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

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class for writing arbitrary numbers of bits to a byte array.
 *
 * @deprecated Use loci.common.RandomAccessOutputStream instead
 */
public class BitWriter {

  // -- Constants --

  private static final Logger LOGGER = LoggerFactory.getLogger(BitWriter.class);

  // -- Fields --

  /** Buffer storing all bits written thus far. */
  private byte[] buf;

  /** Byte index into the buffer. */
  private int index;

  /** Bit index into current byte of the buffer. */
  private int bit;

  // -- Constructors --

  /** Constructs a new bit writer. */
  public BitWriter() { this(10); }

  /** Constructs a new bit writer with the given initial buffer size. */
  public BitWriter(int size) { buf = new byte[size]; }

  // -- BitWriter API methods --

  /** Writes the given value using the given number of bits. */
  public void write(int value, int numBits) {
    if (numBits <= 0) return;
    byte[] bits = new byte[numBits];
    for (int i=0; i<numBits; i++) {
      bits[i] = (byte) (value & 0x0001);
      value >>= 1;
    }
    for (int i=numBits-1; i>=0; i--) {
      int b = bits[i] << (7 - bit);
      buf[index] |= b;
      bit++;
      if (bit > 7) {
        bit = 0;
        index++;
        if (index >= buf.length) {
          // buffer is full; increase the size
          byte[] newBuf = new byte[buf.length * 2];
          System.arraycopy(buf, 0, newBuf, 0, buf.length);
          buf = newBuf;
        }
      }
    }
  }

  /**
   * Writes the bits represented by a bit string to the buffer.
   *
   * @throws IllegalArgumentException If any characters other than
   *   '0' and '1' appear in the string.
   */
  public void write(String bitString) {
    if (bitString == null) 
      throw new IllegalArgumentException("The string cannot be null.");
    for (int i = 0; i < bitString.length(); i++) {
      if ('1' == bitString.charAt(i)) {
        int b = 1 << (7 - bit);
        buf[index] |= b;
      }
      else if ('0' != bitString.charAt(i)) {
        throw new IllegalArgumentException(bitString.charAt(i) +
          "found at character " + i +
          "; 0 or 1 expected. Write only partially completed.");
      }
      bit++;
      if (bit > 7) {
        bit = 0;
        index++;
        if (index >= buf.length) {
          // buffer is full; increase the size
          byte[] newBuf = new byte[buf.length * 2];
          System.arraycopy(buf, 0, newBuf, 0, buf.length);
          buf = newBuf;
        }
      }
    }
  }

  /** Gets an array containing all bits written thus far. */
  public byte[] toByteArray() {
    int size = index;
    if (bit > 0) size++;
    byte[] b = new byte[size];
    System.arraycopy(buf, 0, b, 0, size);
    return b;
  }

  // -- Main method --

  /** Tests the BitWriter class. */
  public static void main(String[] args) {
    int max = 50000;
    // randomize values
    LOGGER.info("Generating random list of {} values", max);
    int[] values = new int[max];
    int[] bits = new int[max];
    double log2 = Math.log(2);
    for (int i=0; i<values.length; i++) {
      values[i] = (int) (50000 * Math.random()) + 1;
      int minBits = (int) Math.ceil(Math.log(values[i] + 1) / log2);
      bits[i] = (int) (10 * Math.random()) + minBits;
    }

    // write values out
    LOGGER.info("Writing values to byte array");
    BitWriter out = new BitWriter();
    for (int i=0; i<values.length; i++) out.write(values[i], bits[i]);

    // read values back in
    LOGGER.info("Reading values from byte array");
    BitBuffer bb = new BitBuffer(out.toByteArray());
    for (int i=0; i<values.length; i++) {
      int value = bb.getBits(bits[i]);
      if (value != values[i]) {
        LOGGER.info("Value #{} does not match (got {}; expected {}; {} bits)",
          new Object[] {i, value, values[i], bits[i]});
      }
    }

    // Testing string functionality
    Random r = new Random();
    LOGGER.info("Generating 5000 random bits for String test");
    final StringBuilder sb = new StringBuilder(5000);
    for (int i = 0; i < 5000; i++) {
      sb.append(r.nextInt(2));
    }
    out = new BitWriter();
    LOGGER.info("Writing values to byte array");
    out.write(sb.toString());
    LOGGER.info("Reading values from byte array");
    bb = new BitBuffer(out.toByteArray());
    for (int i = 0; i < 5000; i++) {
      int value = bb.getBits(1);
      int expected = (sb.charAt(i) == '1') ? 1 : 0;
      if (value != expected) {
        LOGGER.info("Bit #{} does not match (got {}; expected {}.",
          new Object[] {i, value, expected});
      }
    }
  }

}