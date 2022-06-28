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
 * A growable array of bytes.
 */
public class ByteVector {
  private ode.codecs.ByteVector vector;

  public ByteVector() {
    vector = new ode.codecs.ByteVector();
  }

  public ByteVector(int initialSize) {
    vector = new ode.codecs.ByteVector(initialSize);
  }

  public ByteVector(byte[] byteBuffer) {
    vector = new ode.codecs.ByteVector(byteBuffer);
  }

  public void add(byte x) {
    this.vector.add(x);
  }

  public int size() {
    return this.vector.size();
  }

  public byte get(int index) {
    return this.vector.get(index);
  }

  public void add(byte[] array) {
    this.vector.add(array);
  }

  public void add(byte[] array, int off, int len) {
    this.vector.add(array, off, len);
  }

  void doubleCapacity() {
  }

  public void clear() {
    this.vector.clear();
  }

  public byte[] toByteArray() {
    return this.vector.toByteArray();
  }

  ode.codecs.ByteVector getWrapped() {
    return this.vector;
  }

}
