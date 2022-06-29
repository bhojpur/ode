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

/**
 * A growable array of bytes.
 */
public class ByteVector {
  private byte[] data;
  private int size;

  public ByteVector() {
    data = new byte[10];
    size = 0;
  }

  public ByteVector(int initialSize) {
    data = new byte[initialSize];
    size = 0;
  }

  public ByteVector(byte[] byteBuffer) {
    data = byteBuffer;
    size = 0;
  }

  public void add(byte x) {
    while (size >= data.length) doubleCapacity();
    data[size++] = x;
  }

  public int size() {
    return size;
  }

  public byte get(int index) {
    return data[index];
  }

  public void add(byte[] array) { add(array, 0, array.length); }

  public void add(byte[] array, int off, int len) {
    while (data.length < size + len) doubleCapacity();
    if (len == 1) data[size] = array[off];
    else if (len < 35) {
      // for loop is faster for small number of elements
      for (int i=0; i<len; i++) data[size + i] = array[off + i];
    }
    else System.arraycopy(array, off, data, size, len);
    size += len;
  }

  void doubleCapacity() {
    byte[] tmp = new byte[data.length*2 + 1];
    System.arraycopy(data, 0, tmp, 0, data.length);
    data = tmp;
  }

  public void clear() {
    size = 0;
  }

  public byte[] toByteArray() {
    byte[] bytes = new byte[size];
    System.arraycopy(data, 0, bytes, 0, size);
    return bytes;
  }

}