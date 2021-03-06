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
import java.io.IOException;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * A utility class with convenience methods for
 * reading, writing and decoding words.
 */
public final class DataTools {

  // -- Constants --

  // -- Static fields --
  private static final Logger LOGGER = LoggerFactory.getLogger(DataTools.class);

  private static final ThreadLocal<NumberFormat> nf =
      new ThreadLocal<NumberFormat>() {
        @Override protected NumberFormat initialValue() {
            return DecimalFormat.getInstance(Locale.ENGLISH);
        }
    };
  // -- Constructor --

  private DataTools() { }

  /**
   * Reads the contents of the given file into a string.
   *
   * @param id name of the file to read
   *           this can be any name supported by Location,
   *           not necessarily a file on disk
   * @return the complete contents of the specified file
   * @throws IOException if the file cannot be read or is larger than 2GB
   * @see Location#getMappedId(String)
   */
  public static String readFile(String id) throws IOException {
    RandomAccessInputStream in = new RandomAccessInputStream(id);
    long idLen = in.length();
    if (idLen > Integer.MAX_VALUE) {
      throw new IOException("File too large");
    }
    int len = (int) idLen;
    byte[] b = new byte[len];
    in.readFully(b);
    in.close();
    String data = new String(b, Constants.ENCODING);
    b = null;
    return data;
  }

  // -- Word decoding - bytes to primitive types --

  /**
   * Translates up to the first len bytes of a byte array beyond the given
   * offset to a short. If there are fewer than len bytes available,
   * the MSBs are all assumed to be zero (regardless of endianness).
   *
   * @param bytes array of bytes to use for translation
   * @param off offset to the first byte in the array
   * @param len number of bytes to use
   * @param little true if the bytes are provided in little-endian order
   * @return the short value that results from concatenating the specified bytes
   */
  public static short bytesToShort(byte[] bytes, int off, int len,
    boolean little)
  {
    if (bytes.length - off < len) len = bytes.length - off;
    short total = 0;
    for (int i=0, ndx=off; i<len; i++, ndx++) {
      total |= (bytes[ndx] < 0 ? 256 + bytes[ndx] :
        (int) bytes[ndx]) << ((little ? i : len - i - 1) * 8);
    }
    return total;
  }

  /**
   * Translates up to the first 2 bytes of a byte array beyond the given
   * offset to a short. If there are fewer than 2 bytes available
   * the MSBs are all assumed to be zero (regardless of endianness).
   *
   * @param bytes array of bytes to use for translation
   * @param off offset to the first byte in the array
   * @param little true if the bytes are provided in little-endian order
   * @return the short value that results from concatenating the specified bytes
   */
  public static short bytesToShort(byte[] bytes, int off, boolean little) {
    return bytesToShort(bytes, off, 2, little);
  }

  /**
   * Translates up to the first 2 bytes of a byte array to a short.
   * If there are fewer than 2 bytes available, the MSBs are all
   * assumed to be zero (regardless of endianness).
   *
   * @param bytes array of bytes to use for translation
   * @param little true if the bytes are provided in little-endian order
   * @return the short value that results from concatenating the specified bytes
   */
  public static short bytesToShort(byte[] bytes, boolean little) {
    return bytesToShort(bytes, 0, 2, little);
  }

  /**
   * Translates up to the first len bytes of a byte array byond the given
   * offset to a short. If there are fewer than len bytes available,
   * the MSBs are all assumed to be zero (regardless of endianness).
   *
   * @param bytes array of unsigned bytes to use for translation
   * @param off offset to the first byte in the array
   * @param len number of bytes to use
   * @param little true if the bytes are provided in little-endian order
   * @return the short value that results from concatenating the specified bytes
   */
  public static short bytesToShort(short[] bytes, int off, int len,
    boolean little)
  {
    if (bytes.length - off < len) len = bytes.length - off;
    short total = 0;
    for (int i=0, ndx=off; i<len; i++, ndx++) {
      total |= bytes[ndx] << ((little ? i : len - i - 1) * 8);
    }
    return total;
  }

  /**
   * Translates up to the first 2 bytes of a byte array byond the given
   * offset to a short. If there are fewer than 2 bytes available,
   * the MSBs are all assumed to be zero (regardless of endianness).
   *
   * @param bytes array of unsigned bytes to be translated to a short
   * @param off offset into array of bytes; should be non-negative and less than
   *        the length of the array
   * @param little true if the bytes are provided in little-endian order
   * @return the short value that results from concatenating the specified bytes
   */
  public static short bytesToShort(short[] bytes, int off, boolean little) {
    return bytesToShort(bytes, off, 2, little);
  }

  /**
   * Translates up to the first 2 bytes of a byte array to a short.
   * If there are fewer than 2 bytes available, the MSBs are all
   * assumed to be zero (regardless of endianness).
   *
   * @param bytes array of unsigned bytes to be translated to a short
   * @param little true if the bytes are provided in little-endian order
   * @return the short value that results from concatenating the specified bytes
   */
  public static short bytesToShort(short[] bytes, boolean little) {
    return bytesToShort(bytes, 0, 2, little);
  }

  /**
   * Translates up to the first len bytes of a byte array beyond the given
   * offset to an int. If there are fewer than len bytes available,
   * the MSBs are all assumed to be zero (regardless of endianness).
   *
   * @param bytes array of bytes to be translated to a int
   * @param off offset to the first byte in the array
   * @param len number of bytes to use
   * @param little true if the bytes are provided in little-endian order
   * @return the int value that results from concatenating the specified bytes
   */
  public static int bytesToInt(byte[] bytes, int off, int len,
    boolean little)
  {
    if (bytes.length - off < len) len = bytes.length - off;
    int total = 0;
    for (int i=0, ndx=off; i<len; i++, ndx++) {
      total |= (bytes[ndx] < 0 ? 256 + bytes[ndx] :
        (int) bytes[ndx]) << ((little ? i : len - i - 1) * 8);
    }
    return total;
  }

  /**
   * Translates up to the first 4 bytes of a byte array beyond the given
   * offset to an int. If there are fewer than 4 bytes available,
   * the MSBs are all assumed to be zero (regardless of endianness).
   *
   * @param bytes array of bytes to be translated to a int
   * @param off offset to the first byte in the array
   * @param little true if the bytes are provided in little-endian order
   * @return the int value that results from concatenating the specified bytes
   */
  public static int bytesToInt(byte[] bytes, int off, boolean little) {
    return bytesToInt(bytes, off, 4, little);
  }

  /**
   * Translates up to the first 4 bytes of a byte array to an int.
   * If there are fewer than 4 bytes available, the MSBs are all
   * assumed to be zero (regardless of endianness).
   *
   *
   * @param bytes array of bytes to be translated to a int
   * @param little true if the bytes are provided in little-endian order
   * @return the int value that results from concatenating the specified bytes
   */
  public static int bytesToInt(byte[] bytes, boolean little) {
    return bytesToInt(bytes, 0, 4, little);
  }

  /**
   * Translates up to the first len bytes of a byte array beyond the given
   * offset to an int. If there are fewer than len bytes available,
   * the MSBs are all assumed to be zero (regardless of endianness).
   *
   * @param bytes array of unsigned bytes to be translated to a int
   * @param off offset to the first byte in the array
   * @param len number of bytes to use
   * @param little true if the bytes are provided in little-endian order
   * @return the int value that results from concatenating the specified bytes
   */
  public static int bytesToInt(short[] bytes, int off, int len,
    boolean little)
  {
    if (bytes.length - off < len) len = bytes.length - off;
    int total = 0;
    for (int i=0, ndx=off; i<len; i++, ndx++) {
      total |= bytes[ndx] << ((little ? i : len - i - 1) * 8);
    }
    return total;
  }

  /**
   * Translates up to the first 4 bytes of a byte array beyond the given
   * offset to an int. If there are fewer than 4 bytes available,
   * the MSBs are all assumed to be zero (regardless of endianness).
   *
   * @param bytes array of unsigned bytes to be translated to a int
   * @param off offset to the first byte in the array
   * @param little true if the bytes are provided in little-endian order
   * @return the int value that results from concatenating the specified bytes
   */
  public static int bytesToInt(short[] bytes, int off, boolean little) {
    return bytesToInt(bytes, off, 4, little);
  }

  /**
   * Translates up to the first 4 bytes of a byte array to an int.
   * If there are fewer than 4 bytes available, the MSBs are all
   * assumed to be zero (regardless of endianness).
   *
   * @param bytes array of unsigned bytes to be translated to a int
   * @param little true if the bytes are provided in little-endian order
   * @return the int value that results from concatenating the specified bytes
   */
  public static int bytesToInt(short[] bytes, boolean little) {
    return bytesToInt(bytes, 0, 4, little);
  }

  /**
   * Translates up to the first len bytes of a byte array beyond the given
   * offset to a float. If there are fewer than len bytes available,
   * the MSBs are all assumed to be zero (regardless of endianness).
   *
   * @param bytes array of bytes to be translated to a float
   * @param off offset to the first byte in the array
   * @param len number of bytes to use
   * @param little true if the bytes are provided in little-endian order
   * @return the float value that results from concatenating the specified bytes
   */
  public static float bytesToFloat(byte[] bytes, int off, int len,
    boolean little)
  {
    return Float.intBitsToFloat(bytesToInt(bytes, off, len, little));
  }

  /**
   * Translates up to the first 4 bytes of a byte array beyond a given
   * offset to a float. If there are fewer than 4 bytes available,
   * the MSBs are all assumed to be zero (regardless of endianness).
   *
   * @param bytes array of bytes to be translated to a float
   * @param off offset to the first byte in the array
   * @param little true if the bytes are provided in little-endian order
   * @return the float value that results from concatenating the specified bytes
   */
  public static float bytesToFloat(byte[] bytes, int off, boolean little) {
    return bytesToFloat(bytes, off, 4, little);
  }

  /**
   * Translates up to the first 4 bytes of a byte array to a float.
   * If there are fewer than 4 bytes available, the MSBs are all
   * assumed to be zero (regardless of endianness).
   *
   * @param bytes array of bytes to be translated to a float
   * @param little true if the bytes are provided in little-endian order
   * @return the float value that results from concatenating the specified bytes
   */
  public static float bytesToFloat(byte[] bytes, boolean little) {
    return bytesToFloat(bytes, 0, 4, little);
  }

  /**
   * Translates up to the first len bytes of a byte array beyond a given
   * offset to a float. If there are fewer than len bytes available,
   * the MSBs are all assumed to be zero (regardless of endianness).
   *
   * @param bytes array of unsigned bytes to be translated to a float
   * @param off offset to the first byte in the array
   * @param len number of bytes to use
   * @param little true if the bytes are provided in little-endian order
   * @return the float value that results from concatenating the specified bytes
   */
  public static float bytesToFloat(short[] bytes, int off, int len,
    boolean little)
  {
    return Float.intBitsToFloat(bytesToInt(bytes, off, len, little));
  }

  /**
   * Translates up to the first 4 bytes of a byte array beyond a given
   * offset to a float. If there are fewer than 4 bytes available,
   * the MSBs are all assumed to be zero (regardless of endianness).
   *
   * @param bytes array of unsigned bytes to be translated to a float
   * @param off offset to the first byte in the array
   * @param little true if the bytes are provided in little-endian order
   * @return the float value that results from concatenating the specified bytes
   */
  public static float bytesToFloat(short[] bytes, int off, boolean little) {
    return bytesToFloat(bytes, off, 4, little);
  }

  /**
   * Translates up to the first 4 bytes of a byte array to a float.
   * If there are fewer than 4 bytes available, the MSBs are all
   * assumed to be zero (regardless of endianness).
   *
   * @param bytes array of unsigned bytes to be translated to a float
   * @param little true if the bytes are provided in little-endian order
   * @return the float value that results from concatenating the specified bytes
   */
  public static float bytesToFloat(short[] bytes, boolean little) {
    return bytesToFloat(bytes, 0, 4, little);
  }

  /**
   * Translates up to the first len bytes of a byte array beyond the given
   * offset to a long. If there are fewer than len bytes available,
   * the MSBs are all assumed to be zero (regardless of endianness).
   *
   * @param bytes array of bytes to be translated to a long
   * @param off offset to the first byte in the array
   * @param len number of bytes to use
   * @param little true if the bytes are provided in little-endian order
   * @return the long value that results from concatenating the specified bytes
   */
  public static long bytesToLong(byte[] bytes, int off, int len,
    boolean little)
  {
    if (bytes.length - off < len) len = bytes.length - off;
    long total = 0;
    for (int i=0, ndx=off; i<len; i++, ndx++) {
      total |= (bytes[ndx] < 0 ? 256L + bytes[ndx] :
        (long) bytes[ndx]) << ((little ? i : len - i - 1) * 8);
    }
    return total;
  }

  /**
   * Translates up to the first 8 bytes of a byte array beyond the given
   * offset to a long. If there are fewer than 8 bytes available,
   * the MSBs are all assumed to be zero (regardless of endianness).
   *
   * @param bytes array of bytes to be translated to a long
   * @param off offset to the first byte in the array
   * @param little true if the bytes are provided in little-endian order
   * @return the long value that results from concatenating the specified bytes
   */
  public static long bytesToLong(byte[] bytes, int off, boolean little) {
    return bytesToLong(bytes, off, 8, little);
  }

  /**
   * Translates up to the first 8 bytes of a byte array to a long.
   * If there are fewer than 8 bytes available, the MSBs are all
   * assumed to be zero (regardless of endianness).
   *
   * @param bytes array of bytes to be translated to a long
   * @param little true if the bytes are provided in little-endian order
   * @return the long value that results from concatenating the specified bytes
   */
  public static long bytesToLong(byte[] bytes, boolean little) {
    return bytesToLong(bytes, 0, 8, little);
  }

  /**
   * Translates up to the first len bytes of a byte array beyond the given
   * offset to a long. If there are fewer than len bytes available,
   * the MSBs are all assumed to be zero (regardless of endianness).
   *
   * @param bytes array of unsigned bytes to be translated to a long
   * @param off offset to the first byte in the array
   * @param len number of bytes to use
   * @param little true if the bytes are provided in little-endian order
   * @return the long value that results from concatenating the specified bytes
   */
  public static long bytesToLong(short[] bytes, int off, int len,
    boolean little)
  {
    if (bytes.length - off < len) len = bytes.length - off;
    long total = 0;
    for (int i=0, ndx=off; i<len; i++, ndx++) {
      total |= ((long) bytes[ndx]) << ((little ? i : len - i - 1) * 8);
    }
    return total;
  }

  /**
   * Translates up to the first 8 bytes of a byte array beyond the given
   * offset to a long. If there are fewer than 8 bytes available,
   * the MSBs are all assumed to be zero (regardless of endianness).
   *
   * @param bytes array of unsigned bytes to be translated to a long
   * @param off offset to the first byte in the array
   * @param little true if the bytes are provided in little-endian order
   * @return the long value that results from concatenating the specified bytes
   */
  public static long bytesToLong(short[] bytes, int off, boolean little) {
    return bytesToLong(bytes, off, 8, little);
  }

  /**
   * Translates up to the first 8 bytes of a byte array to a long.
   * If there are fewer than 8 bytes available, the MSBs are all
   * assumed to be zero (regardless of endianness).
   *
   * @param bytes array of unsigned bytes to be translated to a long
   * @param little true if the bytes are provided in little-endian order
   * @return the long value that results from concatenating the specified bytes
   */
  public static long bytesToLong(short[] bytes, boolean little) {
    return bytesToLong(bytes, 0, 8, little);
  }

  /**
   * Translates up to the first len bytes of a byte array beyond the given
   * offset to a double. If there are fewer than len bytes available,
   * the MSBs are all assumed to be zero (regardless of endianness).
   *
   * @param bytes array of bytes to be translated to a double
   * @param off offset to the first byte in the array
   * @param len number of bytes to use
   * @param little true if the bytes are provided in little-endian order
   * @return the double value that results from concatenating the specified bytes
   */
  public static double bytesToDouble(byte[] bytes, int off, int len,
    boolean little)
  {
    return Double.longBitsToDouble(bytesToLong(bytes, off, len, little));
  }

  /**
   * Translates up to the first 8 bytes of a byte array beyond the given
   * offset to a double. If there are fewer than 8 bytes available,
   * the MSBs are all assumed to be zero (regardless of endianness).
   *
   * @param bytes array of bytes to be translated to a double
   * @param off offset to the first byte in the array
   * @param little true if the bytes are provided in little-endian order
   * @return the double value that results from concatenating the specified bytes
   */
  public static double bytesToDouble(byte[] bytes, int off,
    boolean little)
  {
    return bytesToDouble(bytes, off, 8, little);
  }

  /**
   * Translates up to the first 8 bytes of a byte array to a double.
   * If there are fewer than 8 bytes available, the MSBs are all
   * assumed to be zero (regardless of endianness).
   *
   * @param bytes array of bytes to be translated to a double
   * @param little true if the bytes are provided in little-endian order
   * @return the double value that results from concatenating the specified bytes
   */
  public static double bytesToDouble(byte[] bytes, boolean little) {
    return bytesToDouble(bytes, 0, 8, little);
  }

  /**
   * Translates up to the first len bytes of a byte array beyond the given
   * offset to a double. If there are fewer than len bytes available,
   * the MSBs are all assumed to be zero (regardless of endianness).
   *
   * @param bytes array of unsigned bytes to be translated to a double
   * @param off offset to the first byte in the array
   * @param len number of bytes to use
   * @param little true if the bytes are provided in little-endian order
   * @return the double value that results from concatenating the specified bytes
   */
  public static double bytesToDouble(short[] bytes, int off, int len,
    boolean little)
  {
    return Double.longBitsToDouble(bytesToLong(bytes, off, len, little));
  }

  /**
   * Translates up to the first 8 bytes of a byte array beyond the given
   * offset to a double. If there are fewer than 8 bytes available,
   * the MSBs are all assumed to be zero (regardless of endianness).
   *
   * @param bytes array of unsigned bytes to be translated to a double
   * @param off offset to the first byte in the array
   * @param little true if the bytes are provided in little-endian order
   * @return the double value that results from concatenating the specified bytes
   */
  public static double bytesToDouble(short[] bytes, int off,
    boolean little)
  {
    return bytesToDouble(bytes, off, 8, little);
  }

  /**
   * Translates up to the first 8 bytes of a byte array to a double.
   * If there are fewer than 8 bytes available, the MSBs are all
   * assumed to be zero (regardless of endianness).
   *
   * @param bytes array of unsigned bytes to be translated to a double
   * @param little true if the bytes are provided in little-endian order
   * @return the double value that results from concatenating the specified bytes
   */
  public static double bytesToDouble(short[] bytes, boolean little) {
    return bytesToDouble(bytes, 0, 8, little);
  }

  /**
   * Translates the given byte array into a String of hexadecimal digits.
   *
   * @param b the array of bytes to parse
   * @return a string of length <code>2 * b.length</code> representing the
   *         hexadecimal digits of each byte in <code>b</code> concatenated
   */
  public static String bytesToHex(byte[] b) {
    StringBuffer sb = new StringBuffer();
    for (int i=0; i<b.length; i++) {
      String a = Integer.toHexString(b[i] & 0xff);
      if (a.length() == 1) sb.append("0");
      sb.append(a);
    }
    return sb.toString();
  }

  /**
   * Parses the input string into a short
   *
   * @param value a String representation of a short value
   * @return {@code null} if the string could not be parsed
   */
  public static Short parseShort(String value) {
    try {
      return Short.valueOf(value);
    } catch (NumberFormatException e) {
      LOGGER.debug("Could not parse integer value", e);
    }
    return null;
  }

  /**
   * Parses the input string into a byte
   *
   * @param value a String representation of a byte value
   * @return {@code null} if the string could not be parsed
   */
  public static Byte parseByte(String value) {
    try {
      return Byte.valueOf(value);
    } catch (NumberFormatException e) {
      LOGGER.debug("Could not parse integer value", e);
    }
    return null;
  }

  /**
   * Parses the input string into an integer
   *
   * @param value a String representation of an int value
   * @return {@code null} if the string could not be parsed
   */
  public static Integer parseInteger(String value) {
    try {
      return Integer.valueOf(value);
    } catch (NumberFormatException e) {
      LOGGER.debug("Could not parse integer value", e);
    }
    return null;
  }

  /**
   * Parses the input string into a long
   *
   * @param value a String representation of a long value
   * @return {@code null} if the string could not be parsed
   */
  public static Long parseLong(String value) {
    try {
      return Long.valueOf(value);
    } catch (NumberFormatException e) {
      LOGGER.debug("Could not parse integer value", e);
    }
    return null;
  }

  /**
   * Parses the input string into a float accounting for the locale decimal
   * separator
   * @param value a String representation of a float value
   * @return {@code null} if the string could not be parsed
   */
  public static Float parseFloat(String value) {
    if (value == null) return null;
    try {
      // upper-case before parsing to ensure that scientific notation
      // is handled correctly
      String toParse = value.replaceAll(",", ".").toUpperCase();
      return nf.get().parse(toParse).floatValue();
    } catch (ParseException e) {
      LOGGER.debug("Could not parse float value", e);
    }
    return null;
  }

  /**
   * Parses the input string into a double accounting for the locale decimal
   * separator
   * @param value a String representation of a double value
   * @return {@code null} if the string could not be parsed
   */
  public static Double parseDouble(String value) {
    if (value == null) return null;
    try {
      // upper-case before parsing to ensure that scientific notation
      // is handled correctly
      String toParse = value.replaceAll(",", ".").toUpperCase();
      return nf.get().parse(toParse).doubleValue();
    } catch (ParseException e) {
      LOGGER.debug("Could not parse double value", e);
    }
    return null;
  }

  /**
   * Normalizes the decimal separator for the user's locale.
   * @deprecated Use {@link #parseDouble(String)} instead
   *
   * @param value the String representation of a double value,
   *        which may contain invalid characters that prevent parsing
   * @return see above
   */
  @Deprecated
  public static String sanitizeDouble(String value) {
    value = value.replaceAll("[^0-9,\\.]", "");
    char separator = new DecimalFormatSymbols().getDecimalSeparator();
    char usedSeparator = separator == '.' ? ',' : '.';
    value = value.replace(usedSeparator, separator);
    // upper-case before parsing to ensure that scientific notation
    // is handled correctly
    value = value.toUpperCase();
    try {
      Double.parseDouble(value);
    }
    catch (Exception e) {
      value = value.replace(separator, usedSeparator);
    }
    return value;
  }

  // -- Word decoding - primitive types to bytes --

  /**
   * Translates the short value into an array of two bytes.
   *
   * @param value the short to be split into bytes
   * @param little true if the returned bytes should be in little-endian order
   * @return byte array with length 2
   */
  public static byte[] shortToBytes(short value, boolean little) {
    byte[] v = new byte[2];
    unpackBytes(value, v, 0, 2, little);
    return v;
  }

  /**
   * Translates the int value into an array of four bytes.
   *
   * @param value the int to be split into bytes
   * @param little true if the returned bytes should be in little-endian order
   * @return byte array with length 4
   */
  public static byte[] intToBytes(int value, boolean little) {
    byte[] v = new byte[4];
    unpackBytes(value, v, 0, 4, little);
    return v;
  }

  /**
   * Translates the float value into an array of four bytes.
   *
   * @param value the float to be split into bytes
   * @param little true if the returned bytes should be in little-endian order
   * @return byte array with length 4
   */
  public static byte[] floatToBytes(float value, boolean little) {
    byte[] v = new byte[4];
    unpackBytes(Float.floatToIntBits(value), v, 0, 4, little);
    return v;
  }

  /**
   * Translates the long value into an array of eight bytes.
   *
   * @param value the long to be split into bytes
   * @param little true if the returned bytes should be in little-endian order
   * @return byte array with length 8
   */
  public static byte[] longToBytes(long value, boolean little) {
    byte[] v = new byte[8];
    unpackBytes(value, v, 0, 8, little);
    return v;
  }

  /**
   * Translates the double value into an array of eight bytes.
   *
   * @param value the double to be split into bytes
   * @param little true if the returned bytes should be in little-endian order
   * @return byte array with length 8
   */
  public static byte[] doubleToBytes(double value, boolean little) {
    byte[] v = new byte[8];
    unpackBytes(Double.doubleToLongBits(value), v, 0, 8, little);
    return v;
  }

  /**
   * Translates an array of short values into an array of byte values.
   *
   * @param values the shorts to be split into bytes
   * @param little true if the returned bytes should be in little-endian order
   * @return byte array with length <code>2 * values.length</code>
   */
  public static byte[] shortsToBytes(short[] values, boolean little) {
    byte[] v = new byte[values.length * 2];
    for (int i=0; i<values.length; i++) {
      unpackBytes(values[i], v, i * 2, 2, little);
    }
    return v;
  }

  /**
   * Translates an array of int values into an array of byte values.
   *
   * @param values the ints to be split into bytes
   * @param little true if the returned bytes should be in little-endian order
   * @return byte array with length <code>4 * values.length</code>
   */
  public static byte[] intsToBytes(int[] values, boolean little) {
    byte[] v = new byte[values.length * 4];
    for (int i=0; i<values.length; i++) {
      unpackBytes(values[i], v, i * 4, 4, little);
    }
    return v;
  }

  /**
   * Translates an array of float values into an array of byte values.
   *
   * @param values the floats to be split into bytes
   * @param little true if the returned bytes should be in little-endian order
   * @return byte array with length <code>4 * values.length</code>
   */
  public static byte[] floatsToBytes(float[] values, boolean little) {
    byte[] v = new byte[values.length * 4];
    for (int i=0; i<values.length; i++) {
      unpackBytes(Float.floatToIntBits(values[i]), v, i * 4, 4, little);
    }
    return v;
  }

  /**
   * Translates an array of long values into an array of byte values.
   *
   * @param values the longs to be split into bytes
   * @param little true if the returned bytes should be in little-endian order
   * @return byte array with length <code>8 * values.length</code>
   */
  public static byte[] longsToBytes(long[] values, boolean little) {
    byte[] v = new byte[values.length * 8];
    for (int i=0; i<values.length; i++) {
      unpackBytes(values[i], v, i * 8, 8, little);
    }
    return v;
  }

  /**
   * Translates an array of double values into an array of byte values.
   *
   * @param values the doubles to be split into bytes
   * @param little true if the returned bytes should be in little-endian order
   * @return byte array with length <code>8 * values.length</code>
   */
  public static byte[] doublesToBytes(double[] values, boolean little) {
    byte[] v = new byte[values.length * 8];
    for (int i=0; i<values.length; i++) {
      unpackBytes(Double.doubleToLongBits(values[i]), v, i * 8, 8, little);
    }
    return v;
  }

  /**
   * Translates nBytes of the given long and places the result in the
   * given byte array.
   *
   * @param value the long to be split into bytes
   * @param buf the byte array in which to store the unpacked bytes
   * @param ndx the offset to the first byte in the array
   * @param nBytes the number of unpacked bytes
   * @param little true if the unpacked bytes should be in little-endian order
   * @throws IllegalArgumentException
   *   if the specified indices fall outside the buffer
   */
  public static void unpackBytes(long value, byte[] buf, int ndx,
    int nBytes, boolean little)
  {
    if (buf.length < ndx + nBytes) {
      throw new IllegalArgumentException("Invalid indices: buf.length=" +
        buf.length + ", ndx=" + ndx + ", nBytes=" + nBytes);
    }
    if (little) {
      for (int i=0; i<nBytes; i++) {
        buf[ndx + i] = (byte) ((value >> (8 * i)) & 0xff);
      }
    }
    else {
      for (int i=0; i<nBytes; i++) {
        buf[ndx + i] = (byte) ((value >> (8 * (nBytes - i - 1))) & 0xff);
      }
    }
  }

  /**
   * Convert a byte array to the appropriate 1D primitive type array.
   *
   * @param b Byte array to convert.
   * @param bpp Denotes the number of bytes in the returned primitive type
   *   (e.g. if bpp == 2, we should return an array of type short).
   * @param fp If set and bpp == 4 or bpp == 8, then return floats or doubles.
   * @param little Whether byte array is in little-endian order.
   * @return an array of primitives
   */
  public static Object makeDataArray(byte[] b,
    int bpp, boolean fp, boolean little)
  {
    if (bpp == 1) {
      return b;
    }
    else if (bpp == 2) {
      short[] s = new short[b.length / 2];
      for (int i=0; i<s.length; i++) {
        s[i] = bytesToShort(b, i * 2, 2, little);
      }
      return s;
    }
    else if (bpp == 4 && fp) {
      float[] f = new float[b.length / 4];
      for (int i=0; i<f.length; i++) {
        f[i] = bytesToFloat(b, i * 4, 4, little);
      }
      return f;
    }
    else if (bpp == 4) {
      int[] i = new int[b.length / 4];
      for (int j=0; j<i.length; j++) {
        i[j] = bytesToInt(b, j * 4, 4, little);
      }
      return i;
    }
    else if (bpp == 8 && fp) {
      double[] d = new double[b.length / 8];
      for (int i=0; i<d.length; i++) {
        d[i] = bytesToDouble(b, i * 8, 8, little);
      }
      return d;
    }
    else if (bpp == 8) {
      long[] l = new long[b.length / 8];
      for (int i=0; i<l.length; i++) {
        l[i] = bytesToLong(b, i * 8, 8, little);
      }
      return l;
    }
    return null;
  }

  /**
   * Convert a byte array to the appropriate 2D primitive type array.
   *
   * @param b Byte array to convert.
   * @param bpp Denotes the number of bytes in the returned primitive type
   *   (e.g. if bpp == 2, we should return an array of type short).
   * @param fp If set and bpp == 4 or bpp == 8, then return floats or doubles.
   * @param little Whether byte array is in little-endian order.
   * @param height The height of the output primitive array (2nd dim length).
   *
   * @return a 2D primitive array of appropriate type,
   *   dimensioned [height][b.length / (bpp * height)]
   *
   * @throws IllegalArgumentException if input byte array does not divide
   *   evenly into height pieces
   */
  public static Object makeDataArray2D(byte[] b,
    int bpp, boolean fp, boolean little, int height)
  {
    if (b.length % (bpp * height) != 0) {

    }
    final int width = b.length / (bpp * height);
    if (bpp == 1) {
      byte[][] b2 = new byte[height][width];
      for (int y=0; y<height; y++) {
        int index = width * y;
        System.arraycopy(b, index, b2[y], 0, width);
      }
      return b2;
    }
    else if (bpp == 2) {
      short[][] s = new short[height][width];
      for (int y=0; y<height; y++) {
        for (int x=0; x<width; x++) {
          int index = 2 * (width * y + x);
          s[y][x] = bytesToShort(b, index, 2, little);
        }
      }
      return s;
    }
    else if (bpp == 4 && fp) {
      float[][] f = new float[height][width];
      for (int y=0; y<height; y++) {
        for (int x=0; x<width; x++) {
          int index = 4 * (width * y + x);
          f[y][x] = bytesToFloat(b, index, 4, little);
        }
      }
      return f;
    }
    else if (bpp == 4) {
      int[][] i = new int[height][width];
      for (int y=0; y<height; y++) {
        for (int x=0; x<width; x++) {
          int index = 4 * (width * y + x);
          i[y][x] = bytesToInt(b, index, 4, little);
        }
      }
      return i;
    }
    else if (bpp == 8 && fp) {
      double[][] d = new double[height][width];
      for (int y=0; y<height; y++) {
        for (int x=0; x<width; x++) {
          int index = 8 * (width * y + x);
          d[y][x] = bytesToDouble(b, index, 8, little);
        }
      }
      return d;
    }
    else if (bpp == 8) {
      long[][] l = new long[height][width];
      for (int y=0; y<height; y++) {
        for (int x=0; x<width; x++) {
          int index = 8 * (width * y + x);
          l[y][x] = bytesToLong(b, index, 8, little);
        }
      }
      return l;
    }
    return null;
  }

  // -- Byte swapping --

  /**
   * Reverse the order of bytes in the given short.
   *
   * @param x short value to byte swap
   * @return short value resulting from byte swapping <code>x</code>
   */
  public static short swap(short x) {
    return (short) ((x << 8) | ((x >> 8) & 0xFF));
  }
  /**
   * Reverse the order of bytes in the given char.
   *
   * @param x char value to byte swap
   * @return char value resulting from byte swapping <code>x</code>
   */
  public static char swap(char x) {
    return (char) ((x << 8) | ((x >> 8) & 0xFF));
  }

  /**
   * Reverse the order of bytes in the given int.
   *
   * @param x int value to byte swap
   * @return int value resulting from byte swapping <code>x</code>
   */
  public static int swap(int x) {
    return (swap((short) x) << 16) | (swap((short) (x >> 16)) & 0xFFFF);
  }

  /**
   * Reverse the order of bytes in the given long.
   *
   * @param x long value to byte swap
   * @return long value resulting from byte swapping <code>x</code>
   */
  public static long swap(long x) {
    return ((long) swap((int) x) << 32) | (swap((int) (x >> 32)) & 0xFFFFFFFFL);
  }

  /**
   * Reverse the order of bytes in the given float.
   *
   * @param x float value to byte swap
   * @return float value resulting from byte swapping <code>x</code>
   */
  public static float swap(float x) {
    return Float.intBitsToFloat(swap(Float.floatToIntBits(x)));
  }

  /**
   * Reverse the order of bytes in the given double.
   *
   * @param x double value to byte swap
   * @return double value resulting from byte swapping <code>x</code>
   */
  public static double swap(double x) {
    return Double.longBitsToDouble(swap(Double.doubleToLongBits(x)));
  }

  // -- Strings --

  /**
   * Remove null bytes from a string.
   *
   * @param toStrip String from which to remove null bytes
   * @return a String copy of <code>toStrip</code> with all
   *         null (0) bytes removed
   */
  public static String stripString(String toStrip) {
    StringBuffer s = new StringBuffer();
    for (int i=0; i<toStrip.length(); i++) {
      if (toStrip.charAt(i) != 0) {
        s.append(toStrip.charAt(i));
      }
    }
    return s.toString().trim();
  }

  /**
   * Check if two filenames have the same prefix.
   * The order in which the filenames are supplied does not matter.
   *
   * Examples:
   *  s1 = /tmp/path.txt
   *  s2 = /home/anonymous/path.png
   *  returns true
   *
   *  s1 = /tmp/path1.txt
   *  s2 = /tmp/path246.txt
   *  returns false
   *
   *  s1 = /tmp/path.txt
   *  s2 = /tmp/path246.txt
   *  returns true
   *
   * @param s1 first String filename to compare
   * @param s2 second String filename to compare
   * @return true if the relative filename without suffix for
   *         either input filename is a substring of the other
   *         input filenames' relative filename without suffix
   */
  public static boolean samePrefix(String s1, String s2) {
    if (s1 == null || s2 == null) return false;
    int n1 = s1.indexOf('.');
    int n2 = s2.indexOf('.');
    if ((n1 == -1) || (n2 == -1)) return false;

    int slash1 = s1.lastIndexOf(File.pathSeparator);
    int slash2 = s2.lastIndexOf(File.pathSeparator);

    String sub1 = s1.substring(slash1 + 1, n1);
    String sub2 = s2.substring(slash2 + 1, n2);
    return sub1.equals(sub2) || sub1.startsWith(sub2) || sub2.startsWith(sub1);
  }

  /**
   * Remove unprintable characters from the given string.
   *
   * @param s String from which to remove unprintable characters
   * @return a String copy of <code>s</code> with tabs, newlines,
   *         and control characters removed
   * @see Character#isISOControl(char)
   */
  public static String sanitize(String s) {
    if (s == null) return null;
    StringBuffer buf = new StringBuffer(s);
    for (int i=0; i<buf.length(); i++) {
      char c = buf.charAt(i);
      if (c != '\t' && c != '\n' && Character.isISOControl(c)) {
        buf = buf.deleteCharAt(i--);
      }
    }
    return buf.toString();
  }

  // -- Normalization --

  /**
   * Normalize the given float array so that the minimum value maps to 0.0
   * and the maximum value maps to 1.0.
   *
   * @param data array of <code>float</code> values to normalize
   * @return array of <code>float</code> values in the range
   *         <code>[0.0, 1.0]</code>
   */
  public static float[] normalizeFloats(float[] data) {
    float[] rtn = new float[data.length];

    // determine the finite min and max values
    float min = Float.MAX_VALUE;
    float max = Float.MIN_VALUE;
    for (int i=0; i<data.length; i++) {
      if (data[i] == Float.POSITIVE_INFINITY ||
        data[i] == Float.NEGATIVE_INFINITY)
      {
        continue;
      }
      if (data[i] < min) min = data[i];
      if (data[i] > max) max = data[i];
    }

    // normalize infinity values
    for (int i=0; i<data.length; i++) {
      if (data[i] == Float.POSITIVE_INFINITY) data[i] = max;
      else if (data[i] == Float.NEGATIVE_INFINITY) data[i] = min;
    }

    // now normalize; min => 0.0, max => 1.0
    float range = max - min;
    for (int i=0; i<rtn.length; i++) {
      rtn[i] = (data[i] - min) / range;
    }
    return rtn;
  }

  /**
   * Normalize the given double array so that the minimum value maps to 0.0
   * and the maximum value maps to 1.0.
   *
   * @param data array of <code>double</code> values to normalize
   * @return array of <code>double</code> values in the range
   *         <code>[0.0, 1.0]</code>
   */
  public static double[] normalizeDoubles(double[] data) {
    double[] rtn = new double[data.length];

    // determine the finite min and max values
    double min = Double.MAX_VALUE;
    double max = Double.MIN_VALUE;
    for (int i=0; i<data.length; i++) {
      if (data[i] == Double.POSITIVE_INFINITY ||
        data[i] == Double.NEGATIVE_INFINITY)
      {
        continue;
      }
      if (data[i] < min) min = data[i];
      if (data[i] > max) max = data[i];
    }

    // normalize infinity values
    for (int i=0; i<data.length; i++) {
      if (data[i] == Double.POSITIVE_INFINITY) data[i] = max;
      else if (data[i] == Double.NEGATIVE_INFINITY) data[i] = min;
    }

    // now normalize; min => 0.0, max => 1.0
    double range = max - min;
    for (int i=0; i<rtn.length; i++) {
      rtn[i] = (data[i] - min) / range;
    }
    return rtn;
  }

  // -- Array handling --

  /**
   * Allocates a 1-dimensional byte array matching the product of the given
   * sizes.
   * 
   * @param sizes list of sizes from which to allocate the array
   * @return a byte array of the appropriate size
   * @throws IllegalArgumentException if the total size exceeds 2GB, which is
   *           the maximum size of an array in Java; or if any size argument is
   *           zero or negative
   */
  public static byte[] allocate(int... sizes) throws IllegalArgumentException {
    if (sizes == null) return null;
    if (sizes.length == 0) return new byte[0];
    int total = safeMultiply32(sizes);
    return new byte[total];
  }

  /**
   * Checks that the product of the given sizes does not exceed the 32-bit
   * integer limit (i.e., {@link Integer#MAX_VALUE}).
   * 
   * @param sizes list of sizes from which to compute the product
   * @return the product of the given sizes
   * @throws IllegalArgumentException if the total size exceeds 2GiB, which is
   *           the maximum size of an int in Java; or if any size argument is
   *           zero or negative
   */
  public static int safeMultiply32(int... sizes)
    throws IllegalArgumentException
  {
    if (sizes.length == 0) return 0;
    long total = 1;
    for (int size : sizes) {
      if (size < 1) {
        throw new IllegalArgumentException("Invalid array size: " +
          sizeAsProduct(sizes));
      }
      total *= size;
      if (total > Integer.MAX_VALUE) {
        throw new IllegalArgumentException("Array size too large: " +
          sizeAsProduct(sizes));
      }
    }
    // NB: The downcast to int is safe here, due to the checks above.
    return (int) total;
  }

  /**
   * Checks that the product of the given sizes does not exceed the 64-bit
   * integer limit (i.e., {@link Long#MAX_VALUE}).
   * 
   * @param sizes list of sizes from which to compute the product
   * @return the product of the given sizes
   * @throws IllegalArgumentException if the total size exceeds 8EiB, which is
   *           the maximum size of a long in Java; or if any size argument is
   *           zero or negative
   */
  public static long safeMultiply64(long... sizes)
    throws IllegalArgumentException
  {
    if (sizes.length == 0) return 0;
    long total = 1;
    for (long size : sizes) {
      if (size < 1) {
        throw new IllegalArgumentException("Invalid array size: " +
          sizeAsProduct(sizes));
      }
      if (willOverflow(total, size)) {
        throw new IllegalArgumentException("Array size too large: " +
          sizeAsProduct(sizes));
      }
      total *= size;
    }
    return total;
  }

  /**
   * Returns true if the given value is contained in the given array.
   *
   * @param array an array of ints to search
   * @param value the int for which to search
   * @return true if <code>array</code> contains at least one occurence of
   *         <code>value</code>
   * @see #indexOf(int[], int)
   */
  public static boolean containsValue(int[] array, int value) {
    return indexOf(array, value) != -1;
  }

  /**
   * Returns the index of the first occurrence of the given value in the given
   * array. If the value is not in the array, returns -1.
   *
   * @param array an array of ints to search
   * @param value the int for which to search
   * @return the index of the first occurence of <code>value</code> in
   *         <code>array</code>, or -1 if <code>value</code> is not found
   */
  public static int indexOf(int[] array, int value) {
    for (int i=0; i<array.length; i++) {
      if (array[i] == value) return i;
    }
    return -1;
  }

  /**
   * Returns the index of the first occurrence of the given value in the given
   * Object array. If the value is not in the array, returns -1.
   *
   * @param array an array of Objects to search
   * @param value the Object for which to search
   * @return the index of the first occurence of <code>value</code> in
   *         <code>array</code>, or -1 if <code>value</code> is not found
   */
  public static int indexOf(Object[] array, Object value) {
    for (int i=0; i<array.length; i++) {
      if (value == null) {
        if (array[i] == null) return i;
      }
      else if (value.equals(array[i])) return i;
    }
    return -1;
  }

  // -- Signed data conversion --

  public static byte[] makeSigned(byte[] b) {
    for (int i=0; i<b.length; i++) {
      b[i] = (byte) (b[i] + 128);
    }
    return b;
  }

  public static short[] makeSigned(short[] s) {
    for (int i=0; i<s.length; i++) {
      s[i] = (short) (s[i] + 32768);
    }
    return s;
  }

  public static int[] makeSigned(int[] i) {
    for (int j=0; j<i.length; j++) {
      i[j] = (int) (i[j] + 2147483648L);
    }
    return i;
  }

  // -- Helper methods --

  private static String sizeAsProduct(int... sizes) {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (int size : sizes) {
      if (first) first = false;
      else sb.append(" x ");
      sb.append(size);
    }
    return sb.toString();
  }

  private static String sizeAsProduct(long... sizes) {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (long size : sizes) {
      if (first) first = false;
      else sb.append(" x ");
      sb.append(size);
    }
    return sb.toString();
  }

  private static boolean willOverflow(long v1, long v2) {
    return Long.MAX_VALUE / v1 < v2;
  }

}