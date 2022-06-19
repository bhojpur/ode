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
import java.io.DataInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.channels.Channel;

/**
 * NIOInputStream provides methods for "intelligent" reading of files
 * and byte arrays.
 *
 */
public class NIOInputStream extends InputStream implements DataInput {

  // -- Constants --

  /**
   * Block size to use when searching through the stream.
   * This value should not exceed MAX_OVERHEAD!
   */
  protected static final int DEFAULT_BLOCK_SIZE = 256 * 1024; // 256 KB

  /** Maximum number of bytes to search when searching through the stream. */
  protected static final int MAX_SEARCH_SIZE = 512 * 1024 * 1024; // 512 MB

  // -- Fields --

  protected IRandomAccess raf;

  /** The file name. */
  protected String filename;

  /** The file. */
  protected File file;

  /** The file channel backed by the random access file. */
  protected Channel channel;

  /** Endianness of the stream. */
  protected boolean isLittleEndian;

  // -- Constructors --

  /**
   * Constructs an NIOInputStream around the given file.
   *
   * @param filename the path to a readable file on disk
   * @throws IOException if the path is invalid or unreadable
   */
  public NIOInputStream(String filename) throws IOException {
    this.filename = filename;
    file = new File(filename);
    raf = new FileHandle(file, "r");
  }

  /**
   * Constructs a random access stream around the given handle.
   *
   * @param handle the {@link IRandomAccess} to wrap in a stream
   */
  public NIOInputStream(IRandomAccess handle) {
    raf = handle;
  }

  /**
   * Constructs a random access stream around the given byte array.
   *
   * @param array the byte array to wrap in a stream
   */
  public NIOInputStream(byte[] array) {
    this(new ByteArrayHandle(array));
  }

  // -- NIOInputStream API methods --

  /**
   * @return the underlying InputStream.
   */
  public DataInputStream getInputStream() {
    return null;
  }

  /**
   * Sets the number of bytes by which to extend the stream.  This only applies
   * to InputStream API methods.
   *
   * @param extend the number of bytes by which to extend the stream
   */
  public void setExtend(int extend) {
  }

  /**
   * Seeks to the given offset within the stream.
   *
   * @param pos the offset to which to seek
   * @throws IOException if the seek is not successful
   */
  public void seek(long pos) throws IOException {
    raf.seek(pos);
  }

  /** Alias for readByte(). */
  @Override
  public int read() throws IOException {
    return raf.readUnsignedByte();
  }

  /**
   * Gets the number of bytes in the file.
   *
   * @return the length of the stream in bytes
   * @throws IOException if the length cannot be retrieved
   */
  public long length() throws IOException {
    return raf.length();
  }

  /**
   * @return the current (absolute) file pointer.
   */
  public long getFilePointer() {
    try {
      return raf.getFilePointer();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /** Closes the streams. */
  @Override
  public void close() throws IOException {
  }

  /**
   * Sets the endianness of the stream.
   *
   * @param isLittleEndian true if the stream order is little-endian
   */
  public void order(boolean isLittleEndian) {
    this.isLittleEndian = isLittleEndian;
  }

  /**
   * @return the endianness of the stream.
   */
  public boolean isLittleEndian() {
    return isLittleEndian;
  }

  /**
   * Reads a string ending with one of the characters in the given string.
   *
   * @param lastChars string containing possible final characters for the returned string
   * @return the smallest string that ends in one of the characters in <code>lastChars</code>
   * @throws IOException If the maximum length (512 MB) is exceeded.
   * @see #findString(String...)
   */
  public String readString(String lastChars) throws IOException {
    if (lastChars.length() == 1) return findString(lastChars);

    String[] terminators = new String[lastChars.length()];
    for (int i=0; i<terminators.length; i++) {
      terminators[i] = lastChars.substring(i, i + 1);
    }
    return findString(terminators);
  }

  /**
   * Reads a string ending with one of the given terminating substrings.
   *
   * @param terminators The strings for which to search.
   *
   * @return The string from the initial position through the end of the
   *   terminating sequence, or through the end of the stream if no
   *   terminating sequence is found.
   * @throws IOException If the maximum search length (512 MB) is exceeded.
   */
  public String findString(String... terminators) throws IOException {
    return findString(true, DEFAULT_BLOCK_SIZE, terminators);
  }

  /**
   * Reads or skips a string ending with
   * one of the given terminating substrings.
   *
   * @param saveString Whether to collect the string from the current file
   *   pointer to the terminating bytes, and return it. If false, returns null.
   * @param terminators The strings for which to search.
   *
   * @throws IOException If saveString flag is set
   *   and the maximum search length (512 MB) is exceeded.
   *
   * @return The string from the initial position through the end of the
   *   terminating sequence, or through the end of the stream if no
   *   terminating sequence is found, or null if saveString flag is unset.
   * @throws IOException If saveString flag is set
   *   and the maximum search length (512 MB) is exceeded.
   */
  public String findString(boolean saveString, String... terminators)
    throws IOException
  {
    return findString(saveString, DEFAULT_BLOCK_SIZE, terminators);
  }

  /**
   * Reads a string ending with one of the given terminating
   * substrings, using the specified block size for buffering.
   *
   * @param blockSize The block size to use when reading bytes in chunks.
   * @param terminators The strings for which to search.
   *
   * @return The string from the initial position through the end of the
   *   terminating sequence, or through the end of the stream if no
   *   terminating sequence is found.
   * @throws IOException If the maximum search length (512 MB) is exceeded.
   */
  public String findString(int blockSize, String... terminators)
    throws IOException
  {
    return findString(true, blockSize, terminators);
  }

  /**
   * Reads or skips a string ending with one of the given terminating
   * substrings, using the specified block size for buffering.
   *
   * @param saveString Whether to collect the string from the current file
   *   pointer to the terminating bytes, and return it. If false, returns null.
   * @param blockSize The block size to use when reading bytes in chunks.
   * @param terminators The strings for which to search.
   *
   * @throws IOException If saveString flag is set
   *   and the maximum search length (512 MB) is exceeded.
   *
   * @return The string from the initial position through the end of the
   *   terminating sequence, or through the end of the stream if no
   *   terminating sequence is found, or null if saveString flag is unset.
   */
  public String findString(boolean saveString, int blockSize,
    String... terminators) throws IOException
  {
    StringBuilder out = new StringBuilder();
    long startPos = getFilePointer();
    long bytesDropped = 0;
    long inputLen = length();
    long maxLen = inputLen - startPos;
    boolean tooLong = saveString && maxLen > MAX_SEARCH_SIZE;
    if (tooLong) maxLen = MAX_SEARCH_SIZE;
    boolean match = false;
    int maxTermLen = 0;
    for (String term : terminators) {
      int len = term.length();
      if (len > maxTermLen) maxTermLen = len;
    }

    InputStreamReader in = new InputStreamReader(this, Constants.ENCODING);
    char[] buf = new char[blockSize];
    long loc = 0;
    while (loc < maxLen) {
      long pos = startPos + loc;

      // if we're not saving the string, drop any old, unnecessary output
      if (!saveString) {
        int outLen = out.length();
        if (outLen >= maxTermLen) {
          int dropIndex = outLen - maxTermLen + 1;
          String last = out.substring(dropIndex, outLen);
          out.setLength(0);
          out.append(last);
          bytesDropped += dropIndex;
        }
      }

      // read block from stream
      int num = blockSize;
      if (pos + blockSize > inputLen) num = (int) (inputLen - pos);
      int r = in.read(buf, 0, num);
      if (r <= 0) throw new IOException("Cannot read from stream: " + r);

      // append block to output
      out.append(buf, 0, r);

      // check output, returning smallest possible string
      int min = Integer.MAX_VALUE, tagLen = 0;
      for (int t=0; t<terminators.length; t++) {
        int len = terminators[t].length();
        int start = (int) (loc - bytesDropped - len);
        int value = out.indexOf(terminators[t], start < 0 ? 0 : start);
        if (value >= 0 && value < min) {
          match = true;
          min = value;
          tagLen = len;
        }
      }

      if (match) {
        // reset stream to proper location
        seek(startPos + bytesDropped + min + tagLen);

        // trim output string
        if (saveString) {
          out.setLength(min + tagLen);
          return out.toString();
        }
        return null;
      }

      loc += r;
    }

    // no match
    if (tooLong) throw new IOException("Maximum search length reached.");
    return null;
  }

  // -- DataInput API methods --

  /** Read an input byte and return true if the byte is nonzero. */
  @Override
  public boolean readBoolean() throws IOException {
    return raf.readBoolean();
  }

  /** Read one byte and return it. */
  @Override
  public byte readByte() throws IOException {
    return raf.readByte();
  }

  /** Read an input char. */
  @Override
  public char readChar() throws IOException {
    return raf.readChar();
  }

  /** Read eight bytes and return a double value. */
  @Override
  public double readDouble() throws IOException {
    return raf.readDouble();
  }

  /** Read four bytes and return a float value. */
  @Override
  public float readFloat() throws IOException {
    return raf.readFloat();
  }

  /** Read four input bytes and return an int value. */
  @Override
  public int readInt() throws IOException {
    return raf.readInt();
  }

  /** Read the next line of text from the input stream. */
  @Override
  public String readLine() throws IOException {
    return findString("\n");
  }

  /**
   * Read a string of arbitrary length, terminated by a null char.
   *
   * @return the smallest string such that the last character is a null byte
   * @throws IOException if a null-terminated string is not found
   * @see #findString(String...)
   */
  public String readCString() throws IOException {
    return findString("\0");
  }

  /**
   * Read a string of length n.
   *
   * @param n the number of bytes to read
   * @return a string representing the next <code>n</code> bytes in the stream
   * @throws IOException if there is an error during reading
   */
  public String readString(int n) throws IOException {
    byte[] b = new byte[n];
    readFully(b);
    return new String(b, Constants.ENCODING);
  }

  /** Read eight input bytes and return a long value. */
  @Override
  public long readLong() throws IOException {
    return raf.readLong();
  }

  /** Read two input bytes and return a short value. */
  @Override
  public short readShort() throws IOException {
    return raf.readShort();
  }

  /** Read an input byte and zero extend it appropriately. */
  @Override
  public int readUnsignedByte() throws IOException {
    return raf.readUnsignedByte();
  }

  /** Read two bytes and return an int in the range 0 through 65535. */
  @Override
  public int readUnsignedShort() throws IOException {
    return raf.readUnsignedShort();
  }

  /** Read a string that has been encoded using a modified UTF-8 format. */
  @Override
  public String readUTF() throws IOException {
    return null;
  }

  /** Skip n bytes within the stream. */
  @Override
  public int skipBytes(int n) throws IOException {
    return raf.skipBytes(n);
  }

  /** Read bytes from the stream into the given array. */
  @Override
  public int read(byte[] array) throws IOException {
    return read(array, 0, array.length);
  }

  /**
   * Read n bytes from the stream into the given array at the specified offset.
   */
  @Override
  public int read(byte[] array, int offset, int n) throws IOException {
    return raf.read(array, offset, n);
  }

  /** Read bytes from the stream into the given array. */
  @Override
  public void readFully(byte[] array) throws IOException {
    readFully(array, 0, array.length);
  }

  /**
   * Read n bytes from the stream into the given array at the specified offset.
   */
  @Override
  public void readFully(byte[] array, int offset, int n) throws IOException {
    raf.readFully(array, offset, n);
  }

  // -- InputStream API methods --

  @Override
  public int available() throws IOException {
    return 0;
  }

  @Override
  public void mark(int readLimit) {
  }

  @Override
  public boolean markSupported() {
    return false;
  }

  @Override
  public void reset() throws IOException {
    raf.seek(0);
  }

}