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

import java.io.IOException;
import java.nio.ByteBuffer;

import loci.common.RandomAccessInputStream;
import ode.codecs.CodecException;

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
   * Compresses data from an array into a ByteBuffer.
   * A preexisting destination ByteBuffer may be provided,
   * and a compressor may choose to use (or not) that ByteBuffer
   * if it has enough remaining space.
   * Regardless, the function returns a reference to the ByteBuffer
   * where the compressed data was actually stored.
   * This potentially avoids unnecessary buffer creation and copying,
   * since the ByteBuffer position indicates
   * how much of the buffer is actually used.
   *
   * @param target A preallocated ByteBuffer that may be used for the compressed data.  This may be <code>null</code> to force the compressor to allocate a fresh buffer.
   * @param data The data to be compressed.
   * @param options Options to be used during compression, if appropriate.
   * @return The ByteBuffer holding the compressed data.
   * @throws CodecException If input cannot be processed.
   */
  ByteBuffer compress(ByteBuffer target, byte[] data, CodecOptions options) throws CodecException;

  /**
   * Compresses a block of data.
   *
   * @param data The data to be compressed.
   * @param options Options to be used during compression, if appropriate.
   * @return The compressed data.
   * @throws CodecException If input is not a compressed data block of the
   *   appropriate type.
   */
  byte[] compress(byte[] data, CodecOptions options) throws CodecException;

  /**
   * Compresses a block of data.
   *
   * @param data The data to be compressed.
   * @param options Options to be used during compression, if appropriate.
   * @return The compressed data.
   * @throws CodecException If input is not a compressed data block of the
   *   appropriate type.
   */
  byte[] compress(byte[][] data, CodecOptions options) throws CodecException;

  /**
   * Decompresses a block of data.
   *
   * @param data the data to be decompressed
   * @param options Options to be used during decompression.
   * @return the decompressed data.
   * @throws CodecException If data is not valid.
   */
  byte[] decompress(byte[] data, CodecOptions options) throws CodecException;

  /**
   * Decompresses a block of data.
   *
   * @param data the data to be decompressed
   * @param options Options to be used during decompression.
   * @return the decompressed data.
   * @throws CodecException If data is not valid.
   */
  byte[] decompress(byte[][] data, CodecOptions options) throws CodecException;

  /**
   * Decompresses a block of data.
   *
   * @param data the data to be decompressed.
   * @return The decompressed data.
   * @throws CodecException If data is not valid compressed data for this
   *   decompressor.
   */
  byte[] decompress(byte[] data) throws CodecException;

  /**
   * Decompresses a block of data.
   *
   * @param data The data to be decompressed.
   * @return The decompressed data.
   * @throws CodecException If data is not valid compressed data for this
   *   decompressor.
   */
  byte[] decompress(byte[][] data) throws CodecException;

  /**
   * Decompresses data from the given RandomAccessInputStream.
   *
   * @param in The stream from which to read compressed data.
   * @param options Options to be used during decompression.
   * @return The decompressed data.
   * @throws CodecException If data is not valid compressed data for this
   *   decompressor.
   */
  byte[] decompress(RandomAccessInputStream in, CodecOptions options)
    throws CodecException, IOException;

}