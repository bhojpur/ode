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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * A wrapper for RandomAccessFile that implements the IRandomAccess interface.
 *
 * @see IRandomAccess
 * @see java.io.RandomAccessFile
 */
public class FileHandle implements IRandomAccess {

  // -- Fields --

  /** The random access file object backing this FileHandle. */
  protected RandomAccessFile raf;

  // -- Constructors --

  /**
   * Creates a random access file stream to read from, and
   * optionally to write to, the file specified by the File argument.
   *
   * @param file a {@link File} representing a file on disk
   * @param mode a valid access mode as defined in {@link RandomAccessFile}
   * @throws FileNotFoundException if the file does not exist
   */
  public FileHandle(File file, String mode) throws FileNotFoundException {
    raf = new RandomAccessFile(file, mode);
  }

  /**
   * Creates a random access file stream to read from, and
   * optionally to write to, a file with the specified name.
   *
   * @param name the path to a file on disk
   * @param mode a valid access mode as defined in {@link RandomAccessFile}
   * @throws FileNotFoundException if the file does not exist
   */
  public FileHandle(String name, String mode) throws FileNotFoundException {
    raf = new RandomAccessFile(name, mode);
  }

  // -- FileHandle API methods --

  /**
   * @return the {@link RandomAccessFile} object backing this FileHandle.
   */
  public RandomAccessFile getRandomAccessFile() { return raf; }

  // -- IRandomAccess API methods --

  /* @see IRandomAccess.close() */
  @Override
  public void close() throws IOException {
    raf.close();
  }

  /* @see IRandomAccess.getFilePointer() */
  @Override
  public long getFilePointer() throws IOException {
    return raf.getFilePointer();
  }

  /* @see IRandomAccess#exists() */
  @Override
  public boolean exists() throws IOException {
    return length() >= 0;
  }

  /* @see IRandomAccess.length() */
  @Override
  public long length() throws IOException {
    return raf.length();
  }

  /* @see IRandomAccess.read(byte[]) */
  @Override
  public int read(byte[] b) throws IOException {
    return raf.read(b);
  }

  /* @see IRandomAccess.read(byte[], int, int) */
  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    return raf.read(b, off, len);
  }

  /* @see IRandomAccess.read(ByteBuffer) */
  @Override
  public int read(ByteBuffer buffer) throws IOException {
    return read(buffer, 0, buffer.capacity());
  }

  /* @see IRandomAccess.read(ByteBuffer, int, int) */
  @Override
  public int read(ByteBuffer buffer, int off, int len) throws IOException {
    byte[] b = new byte[len];
    int n = read(b);
    buffer.put(b, off, len);
    return n;
  }

  /* @see IRandomAccess.seek(long) */
  @Override
  public void seek(long pos) throws IOException {
    raf.seek(pos);
  }

  /* @see IRandomAccess.write(ByteBuffer) */
  @Override
  public void write(ByteBuffer buf) throws IOException {
    write(buf, 0, buf.capacity());
  }

  /* @see IRandomAccess.write(ByteBuffer, int, int) */
  @Override
  public void write(ByteBuffer buf, int off, int len) throws IOException {
    // TODO
  }

  // -- DataInput API methods --

  /* @see java.io.DataInput.readBoolean() */
  @Override
  public boolean readBoolean() throws IOException {
    return raf.readBoolean();
  }

  /* @see java.io.DataInput.readByte() */
  @Override
  public byte readByte() throws IOException {
    return raf.readByte();
  }

  /* @see java.io.DataInput.readChar() */
  @Override
  public char readChar() throws IOException {
    return raf.readChar();
  }

  /* @see java.io.DataInput.readDouble() */
  @Override
  public double readDouble() throws IOException {
    return raf.readDouble();
  }

  /* @see java.io.DataInput.readFloat() */
  @Override
  public float readFloat() throws IOException {
    return raf.readFloat();
  }

  /* @see java.io.DataInput.readFully(byte[]) */
  @Override
  public void readFully(byte[] b) throws IOException {
    raf.readFully(b);
  }

  /* @see java.io.DataInput.readFully(byte[], int, int) */
  @Override
  public void readFully(byte[] b, int off, int len) throws IOException {
    raf.readFully(b, off, len);
  }

  /* @see java.io.DataInput.readInt() */
  @Override
  public int readInt() throws IOException {
    return raf.readInt();
  }

  /* @see java.io.DataInput.readLine() */
  @Override
  public String readLine() throws IOException {
    return raf.readLine();
  }

  /* @see java.io.DataInput.readLong() */
  @Override
  public long readLong() throws IOException {
    return raf.readLong();
  }

  /* @see java.io.DataInput.readShort() */
  @Override
  public short readShort() throws IOException {
    return raf.readShort();
  }

  /* @see java.io.DataInput.readUnsignedByte() */
  @Override
  public int readUnsignedByte() throws IOException {
    return raf.readUnsignedByte();
  }

  /* @see java.io.DataInput.readUnsignedShort() */
  @Override
  public int readUnsignedShort() throws IOException {
    return raf.readUnsignedShort();
  }

  /* @see java.io.DataInput.readUTF() */
  @Override
  public String readUTF() throws IOException {
    return raf.readUTF();
  }

  /* @see java.io.DataInput.skipBytes(int) */
  @Override
  public int skipBytes(int n) throws IOException {
    return raf.skipBytes(n);
  }

  /* @see #skipBytes(int) */
  @Override
  public long skipBytes(long n) throws IOException {
    if (n < 1) {
      return 0;
    }
    final long currentPosition = getFilePointer();
    n = Math.min(n, length() - currentPosition);
    if (n <= Integer.MAX_VALUE) {
      /* use standard library if possible */
      return skipBytes((int) n);
    }
    seek(currentPosition + n);
    return n;
  }

  // -- DataOutput API metthods --

  /* @see java.io.DataOutput.write(byte[]) */
  @Override
  public void write(byte[] b) throws IOException {
    raf.write(b);
  }

  /* @see java.io.DataOutput.write(byte[], int, int) */
  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    raf.write(b, off, len);
  }

  /* @see java.io.DataOutput.write(int b) */
  @Override
  public void write(int b) throws IOException {
    raf.write(b);
  }

  /* @see java.io.DataOutput.writeBoolean(boolean) */
  @Override
  public void writeBoolean(boolean v) throws IOException {
    raf.writeBoolean(v);
  }

  /* @see java.io.DataOutput.writeByte(int) */
  @Override
  public void writeByte(int v) throws IOException {
    raf.writeByte(v);
  }

  /* @see java.io.DataOutput.writeBytes(String) */
  @Override
  public void writeBytes(String s) throws IOException {
    raf.writeBytes(s);
  }

  /* @see java.io.DataOutput.writeChar(int) */
  @Override
  public void writeChar(int v) throws IOException {
    raf.writeChar(v);
  }

  /* @see java.io.DataOutput.writeChars(String) */
  @Override
  public void writeChars(String s) throws IOException {
    raf.writeChars(s);
  }

  /* @see java.io.DataOutput.writeDouble(double) */
  @Override
  public void writeDouble(double v) throws IOException {
    raf.writeDouble(v);
  }

  /* @see java.io.DataOutput.writeFloat(float) */
  @Override
  public void writeFloat(float v) throws IOException {
    raf.writeFloat(v);
  }

  /* @see java.io.DataOutput.writeInt(int) */
  @Override
  public void writeInt(int v) throws IOException {
    raf.writeInt(v);
  }

  /* @see java.io.DataOutput.writeLong(long) */
  @Override
  public void writeLong(long v) throws IOException {
    raf.writeLong(v);
  }

  /* @see java.io.DataOutput.writeShort(int) */
  @Override
  public void writeShort(int v) throws IOException {
    raf.writeShort(v);
  }

  /* @see java.io.DataOutput.writeUTF(String)  */
  @Override
  public void writeUTF(String str) throws IOException {
    raf.writeUTF(str);
  }

  @Override
  public ByteOrder getOrder() {
    return null;
  }

  @Override
  public void setOrder(ByteOrder order) {
  }

}