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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract IRandomAccess implementation for reading from InputStreams and
 * writing to OutputStreams.
 *
 * @see IRandomAccess
 */
public abstract class StreamHandle implements IRandomAccess {

  // TODO: Decide how to handle S3Handle and other reader settings
  public static class Settings {
    public String get(String key) {
      return System.getenv(key);
    }

    public String getRemoteCacheRootDir() {
      return get("BF_REMOTE_CACHE_ROOTDIR");
    }
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(StreamHandle.class);

  // -- Fields --

  /** Name of the open stream. */
  protected String file;

  /** InputStream to be used for reading. */
  protected DataInputStream stream;

  /** OutputStream to be used for writing. */
  protected DataOutputStream outStream;

  /** Length of the stream. */
  protected long length;

  /** Current position within the stream. */
  protected long fp;

  /** Marked position within the stream. */
  protected long mark;

  /** Byte ordering of this stream. */
  protected ByteOrder order;

  // -- Constructor --

  /**
   * Construct a new StreamHandle.
   * The file pointer will be set to 0, and the byte ordering
   * will be big-endian.
   */
  public StreamHandle() {
    fp = 0;
    order = ByteOrder.BIG_ENDIAN;
  }

  // -- IRandomAccess API methods --

  /* @see IRandomAccess#close() */
  @Override
  public void close() throws IOException {
    LOGGER.trace("closing");
    length = fp = mark = 0;
    if (stream != null) stream.close();
    if (outStream != null) outStream.close();
    stream = null;
    outStream = null;
    file = null;
  }

  /* @see IRandomAccess#getFilePointer() */
  @Override
  public long getFilePointer() throws IOException {
    return fp;
  }

  /* @see IRandomAccess#exists() */
  @Override
  public boolean exists() throws IOException {
    return length >= 0;
  }

  /* @see IRandomAccess#length() */
  @Override
  public long length() throws IOException {
    // Too verbose
    // LOGGER.trace("{}", length);
    return length;
  }

  /* @see IRandomAccess#read(byte[]) */
  @Override
  public int read(byte[] b) throws IOException {
    LOGGER.trace("0 {}", b.length);
    return read(b, 0, b.length);
  }

  /* @see IRandomAccess#read(byte[], int, int) */
  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    LOGGER.trace("{} {}", off, len);
    int n = stream.read(b, off, len);
    if (n >= 0) fp += n;
    else n = 0;
    markManager();
    while (n < len && fp < length()) {
      int s = stream.read(b, off + n, len - n);
      fp += s;
      n += s;
    }
    return n == -1 ? 0 : n;
  }

  /* @see IRandomAccess#read(ByteBuffer) */
  @Override
  public int read(ByteBuffer buffer) throws IOException {
    return read(buffer, 0, buffer.capacity());
  }

  /* @see IRandomAccess#read(ByteBuffer, int, int) */
  @Override
  public int read(ByteBuffer buffer, int off, int len) throws IOException {
    LOGGER.trace("{} {}", off, len);
    if (buffer.hasArray()) {
      return read(buffer.array(), off, len);
    }

    byte[] b = new byte[len];
    int n = read(b);
    buffer.put(b, off, len);
    return n;
  }

  /* @see IRandomAccess#seek(long) */
  @Override
  public void seek(long pos) throws IOException {
    LOGGER.trace("{}", pos);
    long diff = pos - fp;
    fp = pos;

    if (diff < 0) {
      // resetStream sets the fp to 0
      resetStream();
      diff = pos;
      fp = pos;
    }
    int skipped = stream.skipBytes((int) diff);
    while (skipped < diff) {
      int n = stream.skipBytes((int) (diff - skipped));
      if (n == 0) break;
      skipped += n;
    }
    markManager();
  }

  /* @see IRandomAccess.write(ByteBuffer) */
  @Override
  public void write(ByteBuffer buf) throws IOException {
    LOGGER.trace("0 {}", buf.capacity());
    write(buf, 0, buf.capacity());
  }

  /* @see IRandomAccess.write(ByteBuffer, int, int) */
  @Override
  public void write(ByteBuffer buf, int off, int len) throws IOException {
    LOGGER.trace("{} {}", off, len);
    buf.position(off);
    if (buf.hasArray()) {
      write(buf.array(), off, len);
    }
    else {
      byte[] b = new byte[len];
      buf.get(b);
      write(b);
    }
  }

  /* @see IRandomAccess.getOrder() */
  @Override
  public ByteOrder getOrder() {
    return order;
  }

  /* @see IRandomAccess.setOrder(ByteOrder) */
  @Override
  public void setOrder(ByteOrder order) {
    this.order = order;
  }

  // -- DataInput API methods --

  /* @see java.io.DataInput#readBoolean() */
  @Override
  public boolean readBoolean() throws IOException {
    fp++;
    return stream.readBoolean();
  }

  /* @see java.io.DataInput#readByte() */
  @Override
  public byte readByte() throws IOException {
    fp++;
    return stream.readByte();
  }

  /* @see java.io.DataInput#readChar() */
  @Override
  public char readChar() throws IOException {
    fp++;
    return stream.readChar();
  }

  /* @see java.io.DataInput#readDouble() */
  @Override
  public double readDouble() throws IOException {
    fp += 8;
    double v = stream.readDouble();
    return order.equals(ByteOrder.LITTLE_ENDIAN) ? DataTools.swap(v) : v;
  }

  /* @see java.io.DataInput#readFloat() */
  @Override
  public float readFloat() throws IOException {
    fp += 4;
    float v = stream.readFloat();
    return order.equals(ByteOrder.LITTLE_ENDIAN) ? DataTools.swap(v) : v;
  }

  /* @see java.io.DataInput#readFully(byte[]) */
  @Override
  public void readFully(byte[] b) throws IOException {
    stream.readFully(b);
    fp += b.length;
  }

  /* @see java.io.DataInput#readFully(byte[], int, int) */
  @Override
  public void readFully(byte[] b, int off, int len) throws IOException {
    stream.readFully(b, off, len);
    fp += len;
  }

  /* @see java.io.DataInput#readInt() */
  @Override
  public int readInt() throws IOException {
    fp += 4;
    int v = stream.readInt();
    return order.equals(ByteOrder.LITTLE_ENDIAN) ? DataTools.swap(v) : v;
  }

  /* @see java.io.DataInput#readLine() */
  @Override
  public String readLine() throws IOException {
    throw new IOException("Unimplemented");
  }

  /* @see java.io.DataInput#readLong() */
  @Override
  public long readLong() throws IOException {
    fp += 8;
    long v = stream.readLong();
    return order.equals(ByteOrder.LITTLE_ENDIAN) ? DataTools.swap(v) : v;
  }

  /* @see java.io.DataInput#readShort() */
  @Override
  public short readShort() throws IOException {
    fp += 2;
    short v = stream.readShort();
    return order.equals(ByteOrder.LITTLE_ENDIAN) ? DataTools.swap(v) : v;
  }

  /* @see java.io.DataInput#readUnsignedByte() */
  @Override
  public int readUnsignedByte() throws IOException {
    fp++;
    return stream.readUnsignedByte();
  }

  /* @see java.io.DataInput#readUnsignedShort() */
  @Override
  public int readUnsignedShort() throws IOException {
    return readShort() & 0xffff;
  }

  /* @see java.io.DataInput#readUTF() */
  @Override
  public String readUTF() throws IOException {
    String s = stream.readUTF();
    fp += s.length();
    return s;
  }

  /* @see java.io.DataInput#skipBytes(int) */
  @Override
  public int skipBytes(int n) throws IOException {
    return (int) skipBytes((long) n);
  }

  /* @see #skipBytes(int) */
  @Override
  public long skipBytes(long n) throws IOException {
    long skipped = 0;
    try {
      for (long i=0; i<n; i++) {
        if (readUnsignedByte() != -1) skipped++;
        markManager();
      }
    }
    catch (EOFException e) { }
    return skipped;
  }

  // -- DataOutput API methods --

  /* @see java.io.DataOutput#write(byte[]) */
  @Override
  public void write(byte[] b) throws IOException {
    if (outStream == null) {
      throw new HandleException("This stream is read-only.");
    }
    outStream.write(b);
  }

  /* @see java.io.DataOutput#write(byte[], int, int) */
  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    if (outStream == null) {
      throw new HandleException("This stream is read-only.");
    }
    outStream.write(b, off, len);
  }

  /* @see java.io.DataOutput#write(int) */
  @Override
  public void write(int b) throws IOException {
    if (outStream == null) {
      throw new HandleException("This stream is read-only.");
    }
    if (order.equals(ByteOrder.LITTLE_ENDIAN)) b = DataTools.swap(b);
    outStream.write(b);
  }

  /* @see java.io.DataOutput#writeBoolean(boolean) */
  @Override
  public void writeBoolean(boolean v) throws IOException {
    if (outStream == null) {
      throw new HandleException("This stream is read-only.");
    }
    outStream.writeBoolean(v);
  }

  /* @see java.io.DataOutput#writeByte(int) */
  @Override
  public void writeByte(int v) throws IOException {
    if (outStream == null) {
      throw new HandleException("This stream is read-only.");
    }
    if (order.equals(ByteOrder.LITTLE_ENDIAN)) v = DataTools.swap(v);
    outStream.writeByte(v);
  }

  /* @see java.io.DataOutput#writeBytes(String) */
  @Override
  public void writeBytes(String s) throws IOException {
    if (outStream == null) {
      throw new HandleException("This stream is read-only.");
    }
    outStream.writeBytes(s);
  }

  /* @see java.io.DataOutput#writeChar(int) */
  @Override
  public void writeChar(int v) throws IOException {
    if (outStream == null) {
      throw new HandleException("This stream is read-only.");
    }
    if (order.equals(ByteOrder.LITTLE_ENDIAN)) v = DataTools.swap(v);
    outStream.writeChar(v);
  }

  /* @see java.io.DataOutput#writeChars(String) */
  @Override
  public void writeChars(String s) throws IOException {
    if (outStream == null) {
      throw new HandleException("This stream is read-only.");
    }
    outStream.writeChars(s);
  }

  /* @see java.io.DataOutput#writeDouble(double) */
  @Override
  public void writeDouble(double v) throws IOException {
    if (outStream == null) {
      throw new HandleException("This stream is read-only.");
    }
    if (order.equals(ByteOrder.LITTLE_ENDIAN)) v = DataTools.swap(v);
    outStream.writeDouble(v);
  }

  /* @see java.io.DataOutput#writeFloat(float) */
  @Override
  public void writeFloat(float v) throws IOException {
    if (outStream == null) {
      throw new HandleException("This stream is read-only.");
    }
    if (order.equals(ByteOrder.LITTLE_ENDIAN)) v = DataTools.swap(v);
    outStream.writeFloat(v);
  }

  /* @see java.io.DataOutput#writeInt(int) */
  @Override
  public void writeInt(int v) throws IOException {
    if (outStream == null) {
      throw new HandleException("This stream is read-only.");
    }
    if (order.equals(ByteOrder.LITTLE_ENDIAN)) v = DataTools.swap(v);
    outStream.writeInt(v);
  }

  /* @see java.io.DataOutput#writeLong(long) */
  @Override
  public void writeLong(long v) throws IOException {
    if (outStream == null) {
      throw new HandleException("This stream is read-only.");
    }
    if (order.equals(ByteOrder.LITTLE_ENDIAN)) v = DataTools.swap(v);
    outStream.writeLong(v);
  }

  /* @see java.io.DataOutput#writeShort(int) */
  @Override
  public void writeShort(int v) throws IOException {
    if (outStream == null) {
      throw new HandleException("This stream is read-only.");
    }
    if (order.equals(ByteOrder.LITTLE_ENDIAN)) v = DataTools.swap(v);
    outStream.writeShort(v);
  }

  /* @see java.io.DataOutput#writeUTF(String) */
  @Override
  public void writeUTF(String str) throws IOException {
    if (outStream == null) {
      throw new HandleException("This stream is read-only.");
    }
    outStream.writeUTF(str);
  }

  // -- Helper methods --

  /**
   * Close and reopen the stream; the stream pointer and mark should be
   * reset to 0.  This method is called if we need to seek backwards within
   * the stream.
   *
   * @throws IOException if the stream cannot be reset
   */
  protected abstract void resetStream() throws IOException;

  /** Reset the marked position, if necessary. */
  private void markManager() {
    if (fp >= mark + RandomAccessInputStream.MAX_OVERHEAD - 1) {
      mark = fp;
      stream.mark(RandomAccessInputStream.MAX_OVERHEAD);
    }
  }

}