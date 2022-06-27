/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.gui;

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
