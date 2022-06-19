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

import java.io.EOFException;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * A wrapper for a byte array that implements the IRandomAccess interface.
 *
 * @see IRandomAccess
 */
public class ByteArrayHandle extends AbstractNIOHandle {

  // -- Constants --

  /** Initial length of a new file. */
  protected static final int INITIAL_LENGTH = 1000000;

  // -- Fields --

  /** Backing ByteBuffer. */
  protected ByteBuffer buffer;

  // -- Constructors --

  /**
   * Creates a random access byte stream to read from, and
   * write to, the bytes specified by the byte[] argument.
   *
   * @param bytes byte array to work with
   */
  public ByteArrayHandle(byte[] bytes) {
    buffer = ByteBuffer.wrap(bytes);
  }

  /**
   *
   * Creates a random access byte stream to read from, and
   * write to, the supplied ByteBuffer.
   *
   * @param bytes ByteBuffer used for reading and writing
   */
  public ByteArrayHandle(ByteBuffer bytes) {
    buffer = bytes;
  }

  /**
   * Creates a random access byte stream to read from, and write to.
   * @param capacity Number of bytes to initially allocate.
   */
  public ByteArrayHandle(int capacity) {
    buffer = ByteBuffer.allocate(capacity);
    buffer.limit(capacity);
  }

  /** Creates a random access byte stream to write to a byte array. */
  public ByteArrayHandle() {
    buffer = ByteBuffer.allocate(INITIAL_LENGTH);
    buffer.limit(0);
  }

  // -- ByteArrayHandle API methods --

  /**
   * Gets a byte array representing the current state of this ByteArrayHandle.
   *
   * @return a byte array representation of the backing ByteBuffer
   */
  public byte[] getBytes() {
    return buffer.array();
  }

  /**
   * Gets the byte buffer backing this handle. <b>NOTE:</b> This is the
   * backing buffer. Any modifications to this buffer including position,
   * length and capacity will affect subsequent calls upon its source handle.
   * @return Backing buffer of this handle.
   */
  public ByteBuffer getByteBuffer() {
    return buffer;
  }

  // -- AbstractNIOHandle API methods --

  /* @see AbstractNIOHandle.setLength(long) */
  @Override
  public void setLength(long length) throws IOException {
    if (length > buffer.capacity()) {
      long fp = getFilePointer();
      ByteBuffer tmp = ByteBuffer.allocate((int) (length * 2));
      ByteOrder order = buffer == null ? null : getOrder();
      seek(0);
      buffer = tmp.put(buffer);
      if (order != null) setOrder(order);
      seek(fp);
    }
    buffer.limit((int) length);
  }

  // -- IRandomAccess API methods --

  /* @see IRandomAccess.close() */
  @Override
  public void close() {
  }

  /* @see IRandomAccess.getFilePointer() */
  @Override
  public long getFilePointer() {
    return buffer.position();
  }

  /* @see IRandomAccess.length() */
  @Override
  public long length() {
    return buffer.limit();
  }

  /* @see IRandomAccess.read(byte[]) */
  @Override
  public int read(byte[] b) throws IOException {
    return read(b, 0, b.length);
  }

  /* @see IRandomAccess.read(byte[], int, int) */
  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    if (getFilePointer() + len > length()) {
      len = (int) (length() - getFilePointer());
    }
    buffer.get(b, off, len);
    return len;
  }

  /* @see IRandomAccess.read(ByteBuffer) */
  @Override
  public int read(ByteBuffer buf) throws IOException {
    return read(buf, 0, buf.capacity());
  }

  /* @see IRandomAccess.read(ByteBuffer, int, int) */
  @Override
  public int read(ByteBuffer buf, int off, int len) throws IOException {
    if (buf.hasArray()) {
      buffer.get(buf.array(), off, len);
      return len;
    }

    byte[] b = new byte[len];
    read(b);
    buf.put(b, 0, len);
    return len;
  }

  /* @see IRandomAccess.seek(long) */
  @Override
  public void seek(long pos) throws IOException {
    if (pos > length()) {
      setLength(pos);
    }
    buffer.position((int) pos);
  }

  /* @see IRandomAccess.getOrder() */
  @Override
  public ByteOrder getOrder() {
    return buffer.order();
  }

  /* @see IRandomAccess.setOrder(ByteOrder) */
  @Override
  public void setOrder(ByteOrder order) {
    buffer.order(order);
  }

  // -- DataInput API methods --

  /* @see java.io.DataInput.readBoolean() */
  @Override
  public boolean readBoolean() throws IOException {
    return readByte() != 0;
  }

  /* @see java.io.DataInput.readByte() */
  @Override
  public byte readByte() throws IOException {
    if (getFilePointer() + 1 > length()) {
      throw new EOFException(EOF_ERROR_MSG);
    }
    try {
      return buffer.get();
    }
    catch (BufferUnderflowException e) {
      EOFException eof = new EOFException();
      eof.initCause(e);
      throw eof;
    }
  }

  /* @see java.io.DataInput.readChar() */
  @Override
  public char readChar() throws IOException {
    if (getFilePointer() + 2 > length()) {
      throw new EOFException(EOF_ERROR_MSG);
    }
    try {
      return buffer.getChar();
    }
    catch (BufferUnderflowException e) {
      EOFException eof = new EOFException();
      eof.initCause(e);
      throw eof;
    }
  }

  /* @see java.io.DataInput.readDouble() */
  @Override
  public double readDouble() throws IOException {
    if (getFilePointer() + 8 > length()) {
      throw new EOFException(EOF_ERROR_MSG);
    }
    try {
      return buffer.getDouble();
    }
    catch (BufferUnderflowException e) {
      EOFException eof = new EOFException();
      eof.initCause(e);
      throw eof;
    }
  }

  /* @see java.io.DataInput.readFloat() */
  @Override
  public float readFloat() throws IOException {
    if (getFilePointer() + 4 > length()) {
      throw new EOFException(EOF_ERROR_MSG);
    }
    try {
      return buffer.getFloat();
    }
    catch (BufferUnderflowException e) {
      EOFException eof = new EOFException();
      eof.initCause(e);
      throw eof;
    }
  }

  /* @see java.io.DataInput.readFully(byte[]) */
  @Override
  public void readFully(byte[] b) throws IOException {
    readFully(b, 0, b.length);
  }

  /* @see java.io.DataInput.readFully(byte[], int, int) */
  @Override
  public void readFully(byte[] b, int off, int len) throws IOException {
    if (getFilePointer() + len > length()) {
      throw new EOFException(EOF_ERROR_MSG);
    }
    try {
      buffer.get(b, off, len);
    }
    catch (BufferUnderflowException e) {
      EOFException eof = new EOFException();
      eof.initCause(e);
      throw eof;
    }
  }

  /* @see java.io.DataInput.readInt() */
  @Override
  public int readInt() throws IOException {
    if (getFilePointer() + 4 > length()) {
      throw new EOFException(EOF_ERROR_MSG);
    }
    try {
      return buffer.getInt();
    }
    catch (BufferUnderflowException e) {
      EOFException eof = new EOFException();
      eof.initCause(e);
      throw eof;
    }
  }

  /* @see java.io.DataInput.readLine() */
  @Override
  public String readLine() throws IOException {
    throw new IOException("Unimplemented");
  }

  /* @see java.io.DataInput.readLong() */
  @Override
  public long readLong() throws IOException {
    if (getFilePointer() + 8 > length()) {
      throw new EOFException(EOF_ERROR_MSG);
    }
    try {
      return buffer.getLong();
    }
    catch (BufferUnderflowException e) {
      EOFException eof = new EOFException();
      eof.initCause(e);
      throw eof;
    }
  }

  /* @see java.io.DataInput.readShort() */
  @Override
  public short readShort() throws IOException {
    if (getFilePointer() + 2 > length()) {
      throw new EOFException(EOF_ERROR_MSG);
    }
    try {
      return buffer.getShort();
    }
    catch (BufferUnderflowException e) {
      EOFException eof = new EOFException();
      eof.initCause(e);
      throw eof;
    }
  }

  /* @see java.io.DataInput.readUnsignedByte() */
  @Override
  public int readUnsignedByte() throws IOException {
    return readByte() & 0xff;
  }

  /* @see java.io.DataInput.readUnsignedShort() */
  @Override
  public int readUnsignedShort() throws IOException {
    return readShort() & 0xffff;
  }

  /* @see java.io.DataInput.readUTF() */
  @Override
  public String readUTF() throws IOException {
    int length = readUnsignedShort();
    byte[] b = new byte[length];
    read(b);
    return new String(b, Constants.ENCODING);
  }

  /* @see java.io.DataInput.skipBytes(int) */
  @Override
  public int skipBytes(int n) throws IOException {
    return (int) skipBytes((long) n);
  }

  /* @see #skipBytes(int) */
  @Override
  public long skipBytes(long n) throws IOException {
    final long currentPosition = getFilePointer();
    n = Math.min(n, length() - currentPosition);
    if (n <= 0) {
      return 0;
    }
    seek(currentPosition + n);
    return n;
  }

  // -- DataOutput API methods --

  /* @see java.io.DataOutput.write(byte[]) */
  @Override
  public void write(byte[] b) throws IOException {
    write(b, 0, b.length);
  }

  /* @see java.io.DataOutput.write(byte[], int, int) */
  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    validateLength(len);
    buffer.put(b, off, len);
  }

  /* @see IRandomAccess.write(ByteBuffer) */
  @Override
  public void write(ByteBuffer buf) throws IOException {
    write(buf, 0, buf.capacity());
  }

  /* @see IRandomAccess.write(ByteBuffer, int, int) */
  @Override
  public void write(ByteBuffer buf, int off, int len) throws IOException {
    validateLength(len);
    buf.position(off);
    buf.limit(off + len);
    buffer.put(buf);
  }

  /* @see java.io.DataOutput.write(int b) */
  @Override
  public void write(int b) throws IOException {
    validateLength(1);
    buffer.put((byte) b);
  }

  /* @see java.io.DataOutput.writeBoolean(boolean) */
  @Override
  public void writeBoolean(boolean v) throws IOException {
    write(v ? 1 : 0);
  }

  /* @see java.io.DataOutput.writeByte(int) */
  @Override
  public void writeByte(int v) throws IOException {
    write(v);
  }

  /* @see java.io.DataOutput.writeBytes(String) */
  @Override
  public void writeBytes(String s) throws IOException {
    write(s.getBytes(Constants.ENCODING));
  }

  /* @see java.io.DataOutput.writeChar(int) */
  @Override
  public void writeChar(int v) throws IOException {
    validateLength(2);
    buffer.putChar((char) v);
  }

  /* @see java.io.DataOutput.writeChars(String) */
  @Override
  public void writeChars(String s) throws IOException {
    int len = 2 * s.length();
    validateLength(len);
    char[] c = s.toCharArray();
    for (int i=0; i<c.length; i++) {
      writeChar(c[i]);
    }
  }

  /* @see java.io.DataOutput.writeDouble(double) */
  @Override
  public void writeDouble(double v) throws IOException {
    validateLength(8);
    buffer.putDouble(v);
  }

  /* @see java.io.DataOutput.writeFloat(float) */
  @Override
  public void writeFloat(float v) throws IOException {
    validateLength(4);
    buffer.putFloat(v);
  }

  /* @see java.io.DataOutput.writeInt(int) */
  @Override
  public void writeInt(int v) throws IOException {
    validateLength(4);
    buffer.putInt(v);
  }

  /* @see java.io.DataOutput.writeLong(long) */
  @Override
  public void writeLong(long v) throws IOException {
    validateLength(8);
    buffer.putLong(v);
  }

  /* @see java.io.DataOutput.writeShort(int) */
  @Override
  public void writeShort(int v) throws IOException {
    validateLength(2);
    buffer.putShort((short) v);
  }

  /* @see java.io.DataOutput.writeUTF(String)  */
  @Override
  public void writeUTF(String str) throws IOException {
    byte[] b = str.getBytes(Constants.ENCODING);
    writeShort(b.length);
    write(b);
  }

}