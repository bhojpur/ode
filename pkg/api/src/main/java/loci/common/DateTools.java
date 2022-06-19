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

import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.IllegalInstantException;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A utility class with convenience methods for working with dates.
 */
public final class DateTools {

  // -- Constants --

  /** Timestamp formats. */
  public static final int UNIX = 0;      // January 1, 1970
  public static final int COBOL = 1;     // January 1, 1601
  public static final int MICROSOFT = 2; // December 30, 1899
  public static final int ZVI = 3;
  public static final int ALT_ZVI = 4;

  /** Milliseconds until UNIX epoch. */
  public static final long UNIX_EPOCH = 0;
  public static final long COBOL_EPOCH = 11644473600000L;
  public static final long MICROSOFT_EPOCH = 2209143600000L;
  public static final long ZVI_EPOCH = 2921084975759000L;
  public static final long ALT_ZVI_EPOCH = 2921084284761000L;

  /** ISO 8601 date output formatter with milliseconds. */
  public static final String ISO8601_FORMAT_MS = "yyyy-MM-dd'T'HH:mm:ss.SSS";

  /** ISO 8601 date output formatter without milliseconds. */
  public static final String ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

  /** Human readable timestamp string */
  public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

  /** Human readable timestamp filename string */
  public static final String FILENAME_FORMAT = "yyyy-MM-dd_HH-mm-ss";

  /** ISO 8601 date formatter with milliseconds. */
  private static final DateTimeFormatter ISO8601_FORMATTER_MS =
    DateTimeFormat.forPattern(ISO8601_FORMAT_MS);

  /** ISO 8601 date formatter without milliseconds. */
  private static final DateTimeFormatter ISO8601_FORMATTER =
    DateTimeFormat.forPattern(ISO8601_FORMAT);

  /** Human readable timestamp formatter. */
  private static final DateTimeFormatter TIMESTAMP_FORMATTER =
    DateTimeFormat.forPattern(TIMESTAMP_FORMAT);

  /** Human readable timestamp filename formatter. */
  private static final DateTimeFormatter FILENAME_FORMATTER =
    DateTimeFormat.forPattern(FILENAME_FORMAT);

  /** Logger for this class. */
  private static final Logger LOGGER = LoggerFactory.getLogger(DateTools.class);

  // -- Constructor --

  private DateTools() { }

  // -- Date handling --

  /**
   * Converts from two-word tick representation to milliseconds.
   * Mainly useful in conjunction with COBOL date conversion.
   *
   * @param hi the upper 32 bits of the tick count
   * @param lo the lower 32 bits of the tick count
   * @return the number of milliseconds corresponding to the tick count,
   *         where 1 tick = 100 ns
   */
  public static long getMillisFromTicks(long hi, long lo) {
    long ticks = ((hi << 32) | lo);
    return ticks / 10000; // 100 ns = 0.0001 ms
  }

  /**
   * Converts the given timestamp into an ISO8601 date.
   *
   * @param stamp the format-dependent timestamp in milliseconds
   * @param format the format in which <code>stamp</code> is stored.
   *               This is used to select the epoch value used for normalizing
   *               the timestamp to milliseconds since the UNIX epoch.  Valid
   *               values are #UNIX, #COBOL, #MICROSOFT, #ZVI, and #ALT_ZVI.
   * @return an ISO 8601 formatted timestamp
   * @see #UNIX_EPOCH
   * @see #COBOL_EPOCH
   * @see #MICROSOFT_EPOCH
   * @see #ZVI_EPOCH
   * @see #ALT_ZVI_EPOCH
   */
  public static String convertDate(long stamp, int format) {
    return convertDate(stamp, format, ISO8601_FORMAT);
  }

  /**
   * Converts the given timestamp into a date string with the given format.
   *
   * @param stamp the format-dependent timestamp in milliseconds
   * @param format the format in which <code>stamp</code> is stored.
   *               This is used to select the epoch value used for normalizing
   *               the timestamp to milliseconds since the UNIX epoch.  Valid
   *               values are #UNIX, #COBOL, #MICROSOFT, #ZVI, and #ALT_ZVI.
   * @param outputFormat the pattern used for formatting the timestamp
   * @return a timestamp in the specified output format
   * @see #UNIX_EPOCH
   * @see #COBOL_EPOCH
   * @see #MICROSOFT_EPOCH
   * @see #ZVI_EPOCH
   * @see #ALT_ZVI_EPOCH
   * @see DateTimeFormat
   */
  public static String convertDate(long stamp, int format, String outputFormat)
  {
    return convertDate(stamp, format, outputFormat, false);
  }

  /**
   * Converts the given timestamp into a date string with the given format.
   *
   * If correctTimeZoneForGMT is set, then the timestamp will be interpreted
   * as being relative to GMT and not the local time zone.
   *
   * @param stamp the format-dependent timestamp in milliseconds
   * @param format the format in which <code>stamp</code> is stored.
   *               This is used to select the epoch value used for normalizing
   *               the timestamp to milliseconds since the UNIX epoch.  Valid
   *               values are #UNIX, #COBOL, #MICROSOFT, #ZVI, and #ALT_ZVI.
   * @param outputFormat the pattern used for formatting the timestamp
   * @param correctTimeZoneForGMT true if the timestamp is relative to GMT
   * @return a timestamp in the specified output format
   * @see #UNIX_EPOCH
   * @see #COBOL_EPOCH
   * @see #MICROSOFT_EPOCH
   * @see #ZVI_EPOCH
   * @see #ALT_ZVI_EPOCH
   * @see DateTimeFormat
   */
  public static String convertDate(long stamp, int format, String outputFormat,
    boolean correctTimeZoneForGMT)
  {
    // see http://www.merlyn.demon.co.uk/critdate.htm for more information on
    // dates than you will ever need (or want)

    long ms = stamp;

    switch (format) {
      case UNIX:
        ms -= UNIX_EPOCH;
        break;
      case COBOL:
        ms -= COBOL_EPOCH;
        break;
      case MICROSOFT:
        ms -= MICROSOFT_EPOCH;
        break;
      case ZVI:
        ms -= ZVI_EPOCH;
        break;
      case ALT_ZVI:
        ms -= ALT_ZVI_EPOCH;
        break;
    }

    final DateTimeFormatter fmt = DateTimeFormat.forPattern(outputFormat);

    try {
      if (correctTimeZoneForGMT) {
        DateTimeZone tz = DateTimeZone.getDefault();
        ms = tz.convertLocalToUTC(ms, false);
      }
    }
    catch (ArithmeticException e) {}
    catch (IllegalInstantException e) {}

    DateTime d = new DateTime(ms, DateTimeZone.UTC);

    return fmt.print(d);
  }

  /**
   * Parse the given date as a Joda instant
   *
   * @param date      The date to parse as a Joda timestamp
   * @param format    The date format to parse the string date
   * @param separator The separator for milliseconds
   * @return the Joda Instant object representing the timestamp
   * @see Instant
   */
  protected static Instant parseDate(String date, String format, String separator) {

      long ms = 0L;
      int msSeparator = 0;
      String newDate = date.trim();

      if (separator != null) {
        msSeparator = date.lastIndexOf(separator);
      }
      if (msSeparator > 0) {
        newDate = date.substring(0, msSeparator);
        String msString = date.substring(msSeparator + 1);
        // Handle formats ending with another pattern, e.g. "SSS aa" or "SSSZ"
        int postmsSeparator = 0;
        for (int pos=0; pos<msString.length(); pos++) {
          if (!Character.isDigit(msString.charAt(pos))) {
            postmsSeparator = pos;
            break;
          }
        }
        if (postmsSeparator > 0) {
          try {
            ms = Long.parseLong(msString.substring(0, postmsSeparator));
          }
          catch (NumberFormatException e) {
            LOGGER.debug("Failed to parse milliseconds from '{}'", ms);
          }
          newDate = newDate + msString.substring(postmsSeparator);
        }
        else {
          try {
            ms = Long.parseLong(msString);
          }
          catch (NumberFormatException e) {
            LOGGER.debug("Failed to parse milliseconds from '{}'", ms);
          }
        }
      }

      final DateTimeFormatter parser =
        DateTimeFormat.forPattern(format).withZone(DateTimeZone.UTC);
      Instant timestamp = null;
      try {
        timestamp = Instant.parse(newDate, parser);
        timestamp = timestamp.plus(ms);
      }
      catch (IllegalArgumentException e) {
        LOGGER.debug("Invalid timestamp '{}' for current locale", date);
        // if the date couldn't be parsed with the current locale,
        // try to parse with an English-language locale
        // this is helpful for dates that use a three-letter month
        DateTimeFormatter usParser = parser.withLocale(Locale.US);
        try {
          timestamp = Instant.parse(newDate, usParser);
          timestamp = timestamp.plus(ms);
        }
        catch (IllegalArgumentException|UnsupportedOperationException exc) {
          LOGGER.debug("Could not parse timestamp '{}' with EN_US locale",
            date, exc);
        }

      }
      catch (UnsupportedOperationException e) {
        LOGGER.debug("Error parsing timestamp '{}'", date, e);
      }
      return timestamp;
  }

  /**
   * Formats the given date as an ISO 8601 date.
   * Delegates to {@link #formatDate(String, String, boolean)}, with the
   * {@code lenient} flag set to false.
   *
   * @param date   The date to format as ISO 8601
   * @param format The date format to parse the string date
   * @return an ISO 8601 formatted timestamp
   */
  public static String formatDate(String date, String format) {
    return formatDate(date, format, false);
  }

  /**
   * Formats the given date as an ISO 8601 date.
   * Delegates to {@link #formatDate(String, String, boolean, String)} with the
   * {@code lenient} flag set to false.
   *
   * @param date      The date to format as ISO 8601
   * @param format    The date format to parse the string date
   * @param separator The separator for milliseconds
   * @return an ISO 8601 formatted timestamp
   */
  public static String formatDate(String date, String format, String separator) {
    return formatDate(date, format, false, separator);
  }

  /**
   * Formats the given date as an ISO 8601 date.
   *
   * @param date    The date to format as ISO 8601.
   * @param format  The date format to parse the string date
   * @param lenient Whether or not to leniently parse the date.
   * @return an ISO 8601 formatted timestamp
   */
  public static String formatDate(String date, String format, boolean lenient) {
   return formatDate(date, format, false, null);
  }

  /**
   * Formats the given date as an ISO 8601 date.
   *
   * @param date      The date to format as ISO 8601
   * @param format    The date format to parse the string date
   * @param lenient   Whether or not to leniently parse the date.
   * @param separator The separator for milliseconds
   * @return an ISO 8601 formatted timestamp
   */
  public static String formatDate(String date, String format, boolean lenient, String separator) {
    if (date == null) return null;

    Instant timestamp = parseDate(date, format, separator);

    if (timestamp == null) {
      return null;
    }

    final DateTimeFormatter isoformat;
    if ((timestamp.getMillis() % 1000) != 0) {
      isoformat = ISO8601_FORMATTER_MS;
    }
    else {
      isoformat = ISO8601_FORMATTER;
    }

    return isoformat.print(timestamp);
  }

  /**
   * Formats the given date as an ISO 8601 date.
   *
   * Delegates to {@link #formatDate(String, String[], boolean, String)} with
   * {@code lenient} set to false and {@code separator} set to null.
   *
   * @param date    The date to format as ISO 8601
   * @param formats The date possible formats to parse the string date
   * @return an ISO 8601 formatted timestamp
   */
  public static String formatDate(String date, String[] formats) {
    return formatDate(date, formats, false, null);
  }

  /**
   * Formats the given date as an ISO 8601 date.
   *
   * Delegates to {@link #formatDate(String, String[], boolean, String)} with
   * {@code separator} set to null.
   *
   * @param date    The date to format as ISO 8601.
   * @param formats The date possible formats to parse the string date
   * @param lenient Whether or not to leniently parse the date.
   * @return an ISO 8601 formatted timestamp
   */
  public static String formatDate(String date, String[] formats,
    boolean lenient)
  {
    return formatDate(date, formats, lenient, null);
  }

  /**
   * Formats the given date as an ISO 8601 date.
   * Delegates to {@link #formatDate(String, String[], boolean, String)} with
   * {@code lenient} set to false.
   *
   * @param date    The date to format as ISO 8601
   * @param formats The date possible formats to parse the string date
   * @param separator  The separator for milliseconds
   * @return an ISO 8601 formatted timestamp
   */
  public static String formatDate(String date, String[] formats, String separator) {
    return formatDate(date, formats, false, separator);
  }

  /**
   * Formats the given date as an ISO 8601 date.
   *
   * @param date       The date to format as ISO 8601.
   * @param formats    The date possible formats to parse the string date
   * @param lenient    Whether or not to leniently parse the date.
   * @param separator  The separator for milliseconds
   * @return an ISO 8601 formatted timestamp
   */
  public static String formatDate(String date, String[] formats,
    boolean lenient, String separator)
  {
    for (int i=0; i<formats.length; i++) {
      String result = formatDate(date, formats[i], lenient, separator);
      if (result != null) return result;
    }
    return null;
  }

  /**
   * Converts a string date in the given format to a long timestamp
   * (in Unix format: milliseconds since January 1, 1970).
   *
   * @param date   The date to convert
   * @param format The date format to parse the string date
   * @return       The date in milliseconds
   */
  public static long getTime(String date, String format) {
    return getTime(date, format, null);
  }

  /**
   * Converts a string date in the given format to a long timestamp
   * (in Unix format: milliseconds since January 1, 1970) with special
   * milliseconds handling.
   *
   * @param date       The date to convert
   * @param format     The date format to parse the string date
   * @param separator  The separator for milliseconds
   * @return           The date in milliseconds
   */
  public static long getTime(String date, String format, String separator) {
    Instant timestamp = parseDate(date, format, separator);
    if (timestamp == null) {
      return -1;
    }
    else {
      return timestamp.getMillis();
    }
  }

  /**
   * @return a timestamp for the current timezone in a
   * human-readable locale-independent format ("YYYY-MM-DD HH:MM:SS")
   */
  public static String getTimestamp() {
    return TIMESTAMP_FORMATTER.print(new DateTime());
  }

  /**
   * @return a timestamp for the current timezone in a format suitable
   * for a filename in a locale-independent format
   * ("YYYY-MM-DD_HH-MM-SS")
   */
  public static String getFileTimestamp() {
    return FILENAME_FORMATTER.print(new DateTime());
  }

}