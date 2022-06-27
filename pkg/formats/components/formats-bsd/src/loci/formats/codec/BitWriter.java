/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.codec;

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
