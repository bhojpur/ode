package ode.codecs.gui;

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

import java.awt.image.DataBuffer;
import java.awt.image.DataBufferShort;

/**
 * DataBuffer that stores signed shorts.
 *
 * SignedShortBuffer serves the same purpose as java.awt.image.DataBufferShort;
 * the only difference is that SignedShortBuffer's getType() method
 * returns DataBuffer.TYPE_USHORT.
 * This is a workaround for the fact that java.awt.image.BufferedImage does
 * not support DataBuffers with type DataBuffer.TYPE_SHORT.
 */
public class SignedShortBuffer extends DataBuffer {

  private DataBufferShort helper;

  // -- Constructors --

  public SignedShortBuffer(int size) {
    super(DataBuffer.TYPE_USHORT, size);
    helper = new DataBufferShort(size);
  }

  public SignedShortBuffer(int size, int numbanks) {
    super(DataBuffer.TYPE_USHORT, size, numbanks);
    helper = new DataBufferShort(size, numbanks);
  }

  public SignedShortBuffer(short[] data, int size) {
    super(DataBuffer.TYPE_USHORT, size);
    helper = new DataBufferShort(data, size);
  }

  public SignedShortBuffer(short[] data, int size, int offset) {
    super(DataBuffer.TYPE_USHORT, size, 1, offset);
    helper = new DataBufferShort(data, size, offset);
  }

  public SignedShortBuffer(short[][] data, int size) {
    super(DataBuffer.TYPE_USHORT, size, data.length);
    helper = new DataBufferShort(data, size);
  }

  public SignedShortBuffer(short[][] data, int size, int[] offsets) {
    super(DataBuffer.TYPE_USHORT, size, data.length, offsets);
    helper = new DataBufferShort(data, size, offsets);
  }

  // -- DataBuffer API methods --

  /* @see java.awt.image.DataBufferShort#getData() */
  public short[] getData() {
    return helper.getData();
  }

  /* @see java.awt.image.DataBufferShort#getData(int) */
  public short[] getData(int bank) {
    return helper.getData(bank);
  }

  /* @see java.awt.image.DataBufferShort#getElem(int, int) */
  @Override
  public int getElem(int bank, int i) {
    return helper.getElem(bank, i);
  }

  /* @see java.awt.image.DataBufferShort#setElem(int, int, int) */
  @Override
  public void setElem(int bank, int i, int val) {
    helper.setElem(bank, i, val);
  }

}