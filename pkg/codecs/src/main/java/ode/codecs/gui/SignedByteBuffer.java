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

/**
 * DataBuffer that stores signed bytes.
 */
public class SignedByteBuffer extends DataBuffer {

  private byte[][] bankData;

  /** Construct a new buffer of signed bytes using the given byte array.  */
  public SignedByteBuffer(byte[] dataArray, int size) {
    super(DataBuffer.TYPE_BYTE, size);
    bankData = new byte[1][];
    bankData[0] = dataArray;
  }

  /** Construct a new buffer of signed bytes using the given 2D byte array. */
  public SignedByteBuffer(byte[][] dataArray, int size) {
    super(DataBuffer.TYPE_BYTE, size);
    bankData = dataArray;
  }

  /* @see java.awt.image.DataBuffer.getData() */
  public byte[] getData() {
    return bankData[0];
  }

  /* @see java.awt.image.DataBuffer#getData(int) */
  public byte[] getData(int bank) {
    return bankData[bank];
  }

  /* @see java.awt.image.DataBuffer#getElem(int) */
  @Override
  public int getElem(int i) {
    return getElem(0, i);
  }

  /* @see java.awt.image.DataBuffer#getElem(int, int) */
  @Override
  public int getElem(int bank, int i) {
    return (int) bankData[bank][i + getOffsets()[bank]];
  }

  /* @see java.awt.image.DataBuffer#setElem(int, int) */
  @Override
  public void setElem(int i, int val) {
    setElem(0, i, val);
  }

  /* @see java.awt.image.DataBuffer#setElem(int, int, int) */
  @Override
  public void setElem(int bank, int i, int val) {
    bankData[bank][i + getOffsets()[bank]] = (byte) val;
  }

}