package loci.common;

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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Interface for random access into structures (e.g., files or arrays).
 */
public interface IRandomAccess extends DataInput, DataOutput {
  /**
   * Closes this random access stream and releases
   * any system resources associated with the stream.
   *
   * @throws IOException if the underlying stream(s) could not be closed
   */
  void close() throws IOException;

  /**
   * Returns the current offset in this stream.
   *
   * @return the current byte offset within the file; expected to be
   *         non-negative and less than the value of #length()
   * @throws IOException if the offset cannot be retrieved
   */
  long getFilePointer() throws IOException;

  /**
   * Returns whether this refers to a valid object
   *
   * @return true if this refers to a valid object
   * @throws IOException if unable to determine whether the object is valid
   */
  boolean exists() throws IOException;

  /**
   * Returns the length of this stream.
   *
   * @return the length in bytes of the stream
   * @throws IOException if the length cannot be retrieved
   */
  long length() throws IOException;

  /**
   * Returns the current order (endianness) of the stream.
   * @return See above.
   */
  ByteOrder getOrder();

  /**
   * Sets the byte order (endianness) of the stream.
   * @param order Order to set.
   */
  void setOrder(ByteOrder order);

  /**
   * Reads up to b.length bytes of data
   * from this stream into an array of bytes.
   *
   * @param b the array to fill from this stream
   * @return the total number of bytes read into the buffer.
   * @throws IOException if reading is not possible
   */
  int read(byte[] b) throws IOException;

  /**
   * Reads up to len bytes of data from this stream into an array of bytes.
   *
   * @param b the array to fill from this stream
   * @param off the offset in <code>b</code> from which to start filling;
   *        expected to be non-negative and no greater than
   *        <code>b.length - len</code>
   * @param len the number of bytes to read; expected to be positive and
   *        no greater than <code>b.length - offset</code>
   * @return the total number of bytes read into the buffer.
   * @throws IOException if reading is not possible
   */
  int read(byte[] b, int off, int len) throws IOException;

  /**
   * Reads up to buffer.capacity() bytes of data
   * from this stream into a ByteBuffer.
   *
   * @param buffer the ByteBuffer to fill from this stream
   * @return the total number of bytes read into the buffer.
   * @throws IOException if reading is not possible
   */
  int read(ByteBuffer buffer) throws IOException;

  /**
   * Reads up to len bytes of data from this stream into a ByteBuffer.
   *
   * @param buffer the ByteBuffer to fill from this stream
   * @param offset the offset in <code>b</code> from which to start filling;
   *        expected to be non-negative and no greater than
   *        <code>buffer.capacity() - len</code>
   * @param len the number of bytes to read; expected to be positive and
   *        no greater than <code>buffer.capacity() - offset</code>
   * @return the total number of bytes read into the buffer.
   * @throws IOException if reading is not possible
   */
  int read(ByteBuffer buffer, int offset, int len) throws IOException;

  /**
   * Sets the stream pointer offset, measured from the beginning
   * of this stream, at which the next read or write occurs.
   *
   * @param pos new byte offset (pointer) in the current stream.
   *        Unless otherwise noted, may be larger or smaller than the
   *        current pointer, but must be non-negative and less than the
   *        value of #length()
   * @throws IOException if <code>pos</code> is invalid or the seek fails
   * @see #getFilePointer()
   */
  void seek(long pos) throws IOException;

  /**
   * A {@code long} variant of {@link #skipBytes(int)}.
   * @param n the number of bytes to skip
   * @return the number of bytes skipped
   * @throws IOException if the operation failed
   */
  long skipBytes(long n) throws IOException;

  /**
   * Writes up to buffer.capacity() bytes of data from the given
   * ByteBuffer to this stream.
   *
   * @param buf the ByteBuffer containing bytes to write to this stream
   * @throws IOException if writing is not possible
   */
  void write(ByteBuffer buf) throws IOException;

  /**
   * Writes up to len bytes of data from the given ByteBuffer to this
   * stream.
   *
   * @param buf the ByteBuffer containing bytes to write to this stream
   * @param off the offset in <code>b</code> from which to start writing;
   *        expected to be non-negative and no greater than
   *        <code>buf.capacity() - len</code>
   * @param len the number of bytes to write; expected to be positive and
   *        no greater than <code>buf.capacity() - offset</code>
   * @throws IOException if writing is not possible
   */
  void write(ByteBuffer buf, int off, int len) throws IOException;
}