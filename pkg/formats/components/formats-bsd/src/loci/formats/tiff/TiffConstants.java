/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.tiff;

/**
 * Generally useful TIFF-related constants.
 */
public final class TiffConstants {

  // -- Constants --

  /** The number of bytes in each IFD entry. */
  public static final int BYTES_PER_ENTRY = 12;

  /** The number of bytes in each IFD entry of a BigTIFF file. */
  public static final int BIG_TIFF_BYTES_PER_ENTRY = 20;

  // TIFF header constants
  public static final int MAGIC_NUMBER = 42;
  public static final int BIG_TIFF_MAGIC_NUMBER = 43;
  public static final int LITTLE = 0x49;
  public static final int BIG = 0x4d;

  // -- Constructor --

  private TiffConstants() { }

}
