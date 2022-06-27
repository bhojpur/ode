/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.codec;

/**
 * A class for reading arbitrary numbers of bits from a byte array.
 *
 * @deprecated Use loci.common.RandomAccessInputStream instead
 */
public class BitBuffer {

  private ode.codecs.BitBuffer buffer;

  /** Default constructor. */
  public BitBuffer(byte[] byteBuffer) {
    buffer = new ode.codecs.BitBuffer(byteBuffer);
  }

  /** Return the backing byte array. */
  public byte[] getByteBuffer() {
    return this.buffer.getByteBuffer();
  }

  /**
   * Skips a number of bits in the BitBuffer.
   *
   * @param bits Number of bits to skip
   */
  public void skipBits(long bits) {
    this.buffer.skipBits(bits);
  }

  /**
   * Returns an int value representing the value of the bits read from
   * the byte array, from the current position. Bits are extracted from the
   * "left side" or high side of the byte.<p>
   * The current position is modified by this call.<p>
   * Bits are pushed into the int from the right, endianness is not
   * considered by the method on its own. So, if 5 bits were read from the
   * buffer "10101", the int would be the integer representation of
   * 000...0010101 on the target machine. <p>
   * In general, this also means the result will be positive unless a full
   * 32 bits are read. <p>
   * Requesting more than 32 bits is allowed, but only up to 32 bits worth of
   * data will be returned (the last 32 bits read). <p>
   *
   * @param bitsToRead the number of bits to read from the bit buffer
   * @return the value of the bits read
   */
  public int getBits(int bitsToRead) {
    return this.buffer.getBits(bitsToRead);
  }

  /**
   * Checks if the current position is on a byte boundary, that is the next
   * bit in the byte array is the first bit in a byte.
   *
   * @return true if bit is on byte boundary, false otherwise.
   */
  public boolean isBitOnByteBoundary() {
    return this.buffer.isBitOnByteBoundary();
  }

  ode.codecs.BitBuffer getWrapped() {
    return this.buffer;
  }

  /**
   * Testing method.
   * @param args Ignored.
   */
  public static void main(String[] args) {
    ode.codecs.BitBuffer.main(args);
  }
}
