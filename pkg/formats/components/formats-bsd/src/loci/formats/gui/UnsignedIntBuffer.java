/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.gui;

import java.awt.image.DataBuffer;

/**
 * DataBuffer that stores unsigned ints.
 */
public class UnsignedIntBuffer extends DataBuffer {

  private int[][] bankData;

  /** Construct a new buffer of unsigned ints using the given int array.  */
  public UnsignedIntBuffer(int[] dataArray, int size) {
    super(DataBuffer.TYPE_INT, size);
    bankData = new int[1][];
    bankData[0] = dataArray;
  }

  /** Construct a new buffer of unsigned ints using the given 2D int array. */
  public UnsignedIntBuffer(int[][] dataArray, int size) {
    super(DataBuffer.TYPE_INT, size);
    bankData = dataArray;
  }

  /* @see java.awt.image.DataBuffer.getData() */
  public int[] getData() {
    return bankData[0];
  }

  /* @see java.awt.image.DataBuffer#getData(int) */
  public int[] getData(int bank) {
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
    int value = bankData[bank][i + getOffsets()[bank]];
    return (int) (value & 0xffffffffL);
  }

  /* @see java.awt.image.DataBuffer#getElemFloat(int) */
  @Override
  public float getElemFloat(int i) {
    return getElemFloat(0, i);
  }

  /* @see java.awt.image.DataBuffer#getElemFloat(int, int) */
  @Override
  public float getElemFloat(int bank, int i) {
    return (float) (getElem(bank, i) & 0xffffffffL);
  }

  /* @see java.awt.image.DataBuffer#getElemDouble(int) */
  @Override
  public double getElemDouble(int i) {
    return getElemDouble(0, i);
  }

  /* @see java.awt.image.DataBuffer#getElemDouble(int, int) */
  @Override
  public double getElemDouble(int bank, int i) {
    return (double) (getElem(bank, i) & 0xffffffffL);
  }

  /* @see java.awt.image.DataBuffer#setElem(int, int) */
  @Override
  public void setElem(int i, int val) {
    setElem(0, i, val);
  }

  /* @see java.awt.image.DataBuffer#setElem(int, int, int) */
  @Override
  public void setElem(int bank, int i, int val) {
    bankData[bank][i + getOffsets()[bank]] = val;
  }

  /* @see java.awt.image.DataBuffer#setElemFloat(int, float) */
  @Override
  public void setElemFloat(int i, float val) {
    setElemFloat(0, i, val);
  }

  /* @see java.awt.image.DataBuffer#setElemFloat(int, int, float) */
  @Override
  public void setElemFloat(int bank, int i, float val) {
    bankData[bank][i + getOffsets()[bank]] = (int) val;
  }

  /* @see java.awt.image.DataBuffer#setElemDouble(int, double) */
  @Override
  public void setElemDouble(int i, double val) {
    setElemDouble(0, i, val);
  }

  /* @see java.awt.image.DataBuffer#setElemDouble(int, int, double) */
  @Override
  public void setElemDouble(int bank, int i, double val) {
    bankData[bank][i + getOffsets()[bank]] = (int) val;
  }

}
