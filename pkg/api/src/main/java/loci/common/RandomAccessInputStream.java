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

import java.io.Closeable;
import java.io.DataInput;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Top-level class for reading from various data sources.
 */
public class RandomAccessInputStream extends InputStream implements DataInput, Closeable, KryoSerializable {

  // -- Constants --

  /** Maximum size of the buffer used by the DataInputStream. */
  protected static final int MAX_OVERHEAD = 1048576;

  /** Logger for this class. */
  private static final Logger LOGGER =
    LoggerFactory.getLogger(RandomAccessInputStream.class);

  /**
   * Block size to use when searching through the stream.
   */
  protected static final int DEFAULT_BLOCK_SIZE = 256 * 1024; // 256 KB

  /** Maximum number of bytes to search when searching through the stream. */
  protected static final int MAX_SEARCH_SIZE = 512 * 1024 * 1024; // 512 MB

  /** Various bitmasks for the 0000xxxx side of a byte. */
  private static final int[] BACK_MASK = {
    0x00, // 00000000
    0x01, // 00000001
    0x03, // 00000011
    0x07, // 00000111
    0x0F, // 00001111
    0x1F, // 00011111
    0x3F, // 00111111
    0x7F  // 01111111
  };

  /** Various bitmasks for the xxxx0000 side of a byte. */
  private static final int[] FRONT_MASK = {
    0x0000, // 00000000
    0x0080, // 10000000
    0x00C0, // 11000000
    0x00E0, // 11100000
    0x00F0, // 11110000
    0x00F8, // 11111000
    0x00FC, // 11111100
    0x00FE  // 11111110
  };

  // -- Fields --

  protected IRandomAccess raf;

  /** The file name. */
  protected String file;

  protected long length = -1;

  protected long markedPos = -1;

  protected String encoding = Constants.ENCODING;

  private int currentBit;

  // -- Constructors --

  /**
   * Constructs a hybrid RandomAccessFile/DataInputStream
   * around the given file.
   *
   * @param file a name that can be passed to {@link Location#getHandle(String)}
   * @throws IOException if the name is invalid
   */
  public RandomAccessInputStream(String file) throws IOException {
    this(Location.getHandle(file), file);
  }

  /**
   * Constructs a hybrid RandomAccessFile/DataInputStream
   * around the given file.
   *
   * @param file a name that can be passed to {@link Location#getHandle(String)}
   * @param bufferSize the size of the caching buffer in bytes
   * @throws IOException if the name is invalid
   */
  public RandomAccessInputStream(String file, int bufferSize) throws IOException
  {
    this(Location.getHandle(file, false, true, bufferSize), file);
  }

  /**
   * Constructs a random access stream around the given handle.
   *
   * @param handle the {@link IRandomAccess} to be wrapped
   * @throws IOException if the handle is invalid
   */
  public RandomAccessInputStream(IRandomAccess handle) throws IOException {
    this(handle, null);
  }

  /**
   * Constructs a random access stream around the given handle,
   * and with the associated file path.
   *
   * @param handle the {@link IRandomAccess} to be wrapped
   * @param file the name associated with the handle. Can be null.
   * @throws IOException if the handle is invalid
   */
  public RandomAccessInputStream(IRandomAccess handle, String file)
    throws IOException
  {
    if (LOGGER.isTraceEnabled()) {
      LOGGER.trace("RandomAccessInputStream {} OPEN", hashCode());
    }
    raf = handle;
    raf.setOrder(ByteOrder.BIG_ENDIAN);
    this.file = file;
    seek(0);
    length = -1;
  }

  /**
   * Constructs a random access stream around the given byte array.
   *
   * @param array the byte array to be wrapped via {@link ByteArrayHandle}
   * @throws IOException if the {@link ByteArrayHandle} cannot be created
   */
  public RandomAccessInputStream(byte[] array) throws IOException {
    this(new ByteArrayHandle(array));
  }

  // -- RandomAccessInputStream API methods --

  /**
   * Sets the native encoding of the stream.
   *
   * @param encoding the name of a standard charset to use when
   *                 working with strings
   * @see loci.common.Constants#ENCODING
   * @see java.nio.charset.Charset
   */
  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  /**
   * Seeks to the given offset within the stream.
   *
   * @param pos the new byte offset
   * @throws IOException if the seek fails
   */
  public void seek(long pos) throws IOException {
    raf.seek(pos);
  }

  /**
   * @return the number of bytes in the file.
   * @throws IOException if the length cannot be retrieved
   */
  public long length() throws IOException {
    return length < 0 ? raf.length() : length;
  }

  /**
   * Sets the length of the stream.
   * The new length must be less than the real length of the stream.
   * This allows us to work with a truncated view of a file, without modifying
   * the file itself.
   *
   * Passing in a negative value will reset the length to the stream's real length.
   *
   * @param newLength the new stream length as defined above
   * @throws IOException if the original stream length cannot be retrieved
   */
  public void setLength(long newLength) throws IOException {
    if (newLength < length()) {
      this.length = newLength;
    }
  }

  /**
   * @return the current (absolute) file pointer.
   * @throws IOException if the current pointer cannot be retrieved
   */
  public long getFilePointer() throws IOException {
    return raf.getFilePointer();
  }

  /** Closes the streams. */
  @Override
  public void close() throws IOException {
    if (LOGGER.isTraceEnabled()) {
      LOGGER.trace("RandomAccessInputStream {} CLOSE", hashCode());
    }
    if (Location.getMappedFile(file) != null) return;
    if (raf != null) raf.close();
    raf = null;
    markedPos = -1;
  }

  /**
   * Sets the endianness of the stream.
   *
   * @param little true if the stream ordering should be little-endian
   */
  public void order(boolean little) {
    if (raf != null) {
      raf.setOrder(little ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN);
    }
  }

  /**
   * Gets the endianness of the stream.
   *
   * @return true if the stream ordering is little-endian
   */
  public boolean isLittleEndian() {
    return raf.getOrder() == ByteOrder.LITTLE_ENDIAN;
  }

  /**
   * Reads a string ending with one of the characters in the given string.
   *
   * @param lastChars each character is a possible terminator
   * @return The string from the initial position through the end of the
   *   terminating sequence, or through the end of the stream if no
   *   terminating sequence is found.
   * @throws IOException If the maximum search length (512 MB) is exceeded.
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

    InputStreamReader in = new InputStreamReader(this, encoding);
    char[] buf = new char[blockSize];
    long loc = 0;
    while (loc < maxLen && getFilePointer() < length() - 1) {
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
      int r = in.read(buf, 0, blockSize);
      if (r <= 0) throw new IOException("Cannot read from stream: " + r);

      // append block to output
      out.append(buf, 0, r);

      // check output, returning smallest possible string
      int min = Integer.MAX_VALUE, tagLen = 0;
      for (String t : terminators) {
        int len = t.length();
        int start = (int) (loc - bytesDropped - len);
        int value = out.indexOf(t, start < 0 ? 0 : start);
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
    return saveString ? out.toString() : null;
  }

  /**
   * Skips a number of bits in the BitBuffer.
   *
   * @param bits Number of bits to skip
   * @throws IllegalArgumentException if bits is negative
   * @throws IOException if an error occurs while skipping
   */
  public void skipBits(long bits) throws IOException {
    if (bits < 0) {
      throw new IllegalArgumentException("Bits to skip cannot be negative");
    }

    bits += currentBit;
    final long bytesToSkip = bits / 8;
    currentBit = (int) (bits % 8);
    if (bytesToSkip > 0) {
      skipBytes(bytesToSkip);
    }
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
   * @throws IllegalArgumentException if bits is negative
   * @throws IOException if an error occurs while skipping
   */
  public int readBits(int bitsToRead) throws IOException {
    if (bitsToRead < 0) {
      throw new IllegalArgumentException("Bits to read cannot be negative");
    }

    if (bitsToRead == 0) {
      return 0;
    }
    int toStore = 0;
    while (bitsToRead != 0 && getFilePointer() < length()) {
      if (currentBit < 0 || currentBit > 7) {
        throw new IllegalArgumentException("byte=" + getFilePointer() +
          ", bit=" + currentBit);
      }

      int bitsLeft = 8 - currentBit;
      if (bitsToRead >= bitsLeft) {
        toStore <<= bitsLeft;
        bitsToRead -= bitsLeft;
        int cb = readByte();
        if (currentBit == 0) {
          // we can read in a whole byte, so we'll do that.
          toStore += cb & 0xff;
        }
        else {
          // otherwise, only read the appropriate number of bits off the back
          // side of the byte, in order to "finish" the current byte in the
          // buffer.
          toStore += cb & BACK_MASK[bitsLeft];
          currentBit = 0;
        }
      }
      else {
        // We will be able to finish using the current byte.
        // read the appropriate number of bits off the front side of the byte,
        // then push them into the int.
        toStore = toStore << bitsToRead;
        int cb = readByte() & 0xff;
        seek(getFilePointer() - 1);
        toStore += (cb & (0x00FF - FRONT_MASK[currentBit])) >>
          (bitsLeft - bitsToRead);
        currentBit += bitsToRead;
        bitsToRead = 0;
      }
    }
    return toStore;
  }

  /**
   * Checks if the current position is on a byte boundary, that is the next
   * bit in the byte array is the first bit in a byte.
   *
   * @return true if bit is on byte boundary, false otherwise.
   */
  public boolean isBitOnByteBoundary() {
    return currentBit % 8 == 0;
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

  /**
   * Read four input bytes and return an unsigned value.
   *
   * @return the next 4 bytes in the stream as a long
   * @throws IOException if there is an error during reading
   */
  public long readUnsignedInt() throws IOException {
    return readInt() & 0xffffffffL;
  }

  /** Read the next line of text from the input stream. */
  @Override
  public String readLine() throws IOException {
    String line = findString("\n");
    return line.length() == 0 ? null : line;
  }

  /**
   * Read a string of arbitrary length, terminated by a null char.
   *
   * @return the shortest null-terminated string from the current pointer
   * @throws IOException if there is an error during reading
   */
  public String readCString() throws IOException {
    String line = findString("\0");
    return line.length() == 0 ? null : line;
  }

  /**
   * Reads a byte array of the given length byte by byte. Returns a string
   * using the set encoding.
   *
   * @param n The length of the array.
   * @return See above
   * @throws IOException Thrown if an error occurred while reading the data.
   * @see #setEncoding(String)
   */
  public String readByteToString(int n) throws IOException {
    n = (int) Math.min(available(), n);
    byte[] bytes = new byte[n];
    readFully(bytes);
    StringBuffer newString = new StringBuffer();
    for (byte b : bytes) {
      int v = b & 0xff;
      if (v > 0x7f) {
          newString.append(Character.toChars(v));
      } else {
          newString.append((char) b);
      }
    }
    String s = newString.toString();
    return new String(s.getBytes(encoding), encoding);
  }

  /**
   * Read a string of up to length n.
   *
   * @param n the number of bytes to read
   * @return a string representing the read bytes, using the default encoding
   * @throws IOException if an error occurred during reading
   * @see #setEncoding(String)
   */
  public String readString(int n) throws IOException {
    int avail = available();
    if (n > avail) n = avail;
    byte[] b = new byte[n];
    readFully(b);
    return new String(b, encoding);
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
    return raf.readUTF();
  }

  /** Skip n bytes within the stream. */
  @Override
  public int skipBytes(int n) throws IOException {
    return raf.skipBytes(n);
  }

  /** Skip n bytes within the stream. */
  public long skipBytes(long n) throws IOException {
    return raf.skipBytes(n);
  }

  /** Read bytes from the stream into the given array. */
  @Override
  public int read(byte[] array) throws IOException {
    int rtn = raf.read(array);
    if (rtn == 0 && raf.getFilePointer() >= raf.length() - 1) rtn = -1;
    return rtn;
  }

  /**
   * Read n bytes from the stream into the given array at the specified offset.
   */
  @Override
  public int read(byte[] array, int offset, int n) throws IOException {
    int rtn = raf.read(array, offset, n);
    if (rtn == 0 && raf.getFilePointer() >= raf.length() - 1) rtn = -1;
    return rtn;
  }

  /**
   * Read bytes from the stream into the given buffer.
   *
   * @param buf the {@link ByteBuffer} to fill. <code>buf.capacity()</code>
   *            determines the number of bytes to read
   * @return the number of bytes read
   * @throws IOException if an error occurred during reading
   */
  public int read(ByteBuffer buf) throws IOException {
    return raf.read(buf);
  }

  /**
   * Read n bytes from the stream into the given buffer at the specified offset.
   *
   * @param buf the {@link ByteBuffer} to fill
   * @param offset the offset to the first byte in the buffer
   * @param n the number of bytes to read
   * @return the number of bytes actually read
   * @throws IOException if an error occurred during reading
   */
  public int read(ByteBuffer buf, int offset, int n) throws IOException {
    return raf.read(buf, offset, n);
  }

  /** Read bytes from the stream into the given array. */
  @Override
  public void readFully(byte[] array) throws IOException {
    raf.readFully(array);
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
  public int read() throws IOException {
    int b = readByte();
    if (b == -1 && (getFilePointer() >= length())) return 0;
    return b;
  }

  @Override
  public int available() throws IOException {
    long remain = length() - getFilePointer();
    if (remain > Integer.MAX_VALUE) remain = Integer.MAX_VALUE;
    return (int) remain;
  }

  @Override
  public void mark(int readLimit) {
    try {
      markedPos = getFilePointer();
    }
    catch (IOException exc) {
      LOGGER.warn("Cannot set mark", exc);
    }
  }

  @Override
  public boolean markSupported() {
    return true;
  }

  @Override
  public void reset() throws IOException {
    if (markedPos < 0) throw new IOException("No mark set");
    seek(markedPos);
  }

  // -- Externalizable API methods --

  @Override
  public void read(Kryo kryo, Input in) {
    raf = (IRandomAccess) kryo.readClassAndObject(in);
    file = kryo.readObjectOrNull(in, String.class);
    if (file != null) {
      try {
        raf = Location.getHandle(file);
      }
      catch (IOException e) {
        LOGGER.warn("Failed to reopen file", e);
      }
    }
    length = kryo.readObject(in, Long.class);
    markedPos = kryo.readObject(in, Long.class);
    encoding = kryo.readObject(in, String.class);
  }

  @Override
  public void write(Kryo kryo, Output out) {
    kryo.writeClassAndObject(out, raf);
    kryo.writeObjectOrNull(out, file, String.class);
    kryo.writeObject(out, length);
    kryo.writeObject(out, markedPos);
    kryo.writeObject(out, encoding);
  }

}