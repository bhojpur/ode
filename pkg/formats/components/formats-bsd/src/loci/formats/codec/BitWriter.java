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

/**
 * A class for writing arbitrary numbers of bits to a byte array.
 *
 * @deprecated Use loci.common.RandomAccessOutputStream instead
 */
public class BitWriter {
  private ode.codecs.BitWriter writer;

  // -- Constructors --

  /** Constructs a new bit writer. */
  public BitWriter() {
    writer = new ode.codecs.BitWriter();
  }

  /** Constructs a new bit writer with the given initial buffer size. */
  public BitWriter(int size) {
    writer = new ode.codecs.BitWriter(size);
  }

  // -- BitWriter API methods --

  /** Writes the given value using the given number of bits. */
  public void write(int value, int numBits) {
    this.writer.write(value, numBits);
  }

  /**
   * Writes the bits represented by a bit string to the buffer.
   *
   * @throws IllegalArgumentException If any characters other than
   *   '0' and '1' appear in the string.
   */
  public void write(String bitString) {
    this.writer.write(bitString);
  }

  /** Gets an array containing all bits written thus far. */
  public byte[] toByteArray() {
    return this.writer.toByteArray();
  }

  ode.codecs.BitWriter getWrapped() {
    return this.writer;
  }

  // -- Main method --

  /** Tests the BitWriter class. */
  public static void main(String[] args) {
    ode.codecs.BitWriter.main(args);
  }

}
