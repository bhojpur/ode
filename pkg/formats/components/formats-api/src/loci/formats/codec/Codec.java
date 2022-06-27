/*
 * Top-level reader and writer APIs
 */

package loci.formats.codec;

import java.io.IOException;

import loci.common.RandomAccessInputStream;
import loci.formats.FormatException;

/**
 * This class is an interface for any kind of compression or decompression.
 * Data is presented to the compressor in a 1D or 2D byte array,
 * with (optionally, depending on the compressor) pixel dimensions and
 * an Object containing any other options the compressor may need.
 *
 * If an argument is not appropriate for the compressor type, it is expected
 * to completely ignore the argument. i.e.: Passing a compressor that does not
 * require pixel dimensions null for the dimensions must not cause the
 * compressor to throw a NullPointerException.
 *
 * Classes implementing the Codec interface are expected to either
 * implement both compression methods or neither. (The same is expected for
 * decompression).
 */
public interface Codec {

  /**
   * Compresses a block of data.
   *
   * @param data The data to be compressed.
   * @param options Options to be used during compression, if appropriate.
   * @return The compressed data.
   * @throws FormatException If input is not a compressed data block of the
   *   appropriate type.
   */
  byte[] compress(byte[] data, CodecOptions options) throws FormatException;

  /**
   * Compresses a block of data.
   *
   * @param data The data to be compressed.
   * @param options Options to be used during compression, if appropriate.
   * @return The compressed data.
   * @throws FormatException If input is not a compressed data block of the
   *   appropriate type.
   */
  byte[] compress(byte[][] data, CodecOptions options) throws FormatException;

  /**
   * Decompresses a block of data.
   *
   * @param data the data to be decompressed
   * @param options Options to be used during decompression.
   * @return the decompressed data.
   * @throws FormatException If data is not valid.
   */
  byte[] decompress(byte[] data, CodecOptions options) throws FormatException;

  /**
   * Decompresses a block of data.
   *
   * @param data the data to be decompressed
   * @param options Options to be used during decompression.
   * @return the decompressed data.
   * @throws FormatException If data is not valid.
   */
  byte[] decompress(byte[][] data, CodecOptions options) throws FormatException;

  /**
   * Decompresses a block of data.
   *
   * @param data the data to be decompressed.
   * @return The decompressed data.
   * @throws FormatException If data is not valid compressed data for this
   *   decompressor.
   */
  byte[] decompress(byte[] data) throws FormatException;

  /**
   * Decompresses a block of data.
   *
   * @param data The data to be decompressed.
   * @return The decompressed data.
   * @throws FormatException If data is not valid compressed data for this
   *   decompressor.
   */
  byte[] decompress(byte[][] data) throws FormatException;

  /**
   * Decompresses data from the given RandomAccessInputStream.
   *
   * @param in The stream from which to read compressed data.
   * @param options Options to be used during decompression.
   * @return The decompressed data.
   * @throws FormatException If data is not valid compressed data for this
   *   decompressor.
   */
  byte[] decompress(RandomAccessInputStream in, CodecOptions options)
    throws FormatException, IOException;

}
