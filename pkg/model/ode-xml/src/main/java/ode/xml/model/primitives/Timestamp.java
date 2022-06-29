package ode.xml.model.primitives;

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

import org.joda.time.Instant;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Primitive type that represents an ISO 8601 timestamp.
 */
public class Timestamp extends PrimitiveType<String> {

  /** ISO 8601 date output format with milliseconds. */
  public static final String ISO8601_FORMAT_MS = "yyyy-MM-dd'T'HH:mm:ss.SSS";

  /** ISO 8601 date output format without milliseconds. */
  public static final String ISO8601_FORMAT_S = "yyyy-MM-dd'T'HH:mm:ss";

  /** ISO 8601 date input formatter. */
  public static final DateTimeFormatter ISO8601_PARSER = ISODateTimeFormat.dateTimeParser().withZone(DateTimeZone.UTC);

  /** ISO 8601 date output formatter with milliseconds. */
  public static final DateTimeFormatter ISO8601_FORMATTER_MS = DateTimeFormat.forPattern(ISO8601_FORMAT_MS);

  /** ISO 8601 date output formatter without milliseconds. */
  public static final DateTimeFormatter ISO8601_FORMATTER_S = DateTimeFormat.forPattern(ISO8601_FORMAT_S);

  /** Logger for this class. */
  private static final Logger LOGGER =
      LoggerFactory.getLogger(Timestamp.class);

  final Instant timestamp;

  public Timestamp(final String value) throws IllegalArgumentException, UnsupportedOperationException {
    this.timestamp = Instant.parse(value, ISO8601_PARSER);
    this.value = toString();
  }

  public Timestamp(final Instant instant) {
    this.timestamp = new Instant(instant);
    this.value = toString();
  }

  public Timestamp(final DateTime datetime) {
    this.timestamp = datetime.toInstant();
    this.value = toString();
  }

  /**
   * Returns a <code>Timestamp</code> object holding the value of
   * the specified string, or null if parsing failed.
   * @param value The string to be parsed.
   * @return See above.
   */
  public static Timestamp valueOf(String value) {
    if (value == null)
      return null;
    Timestamp t = null;
    try {
      t = new Timestamp(value);
    }
    catch (IllegalArgumentException e) {
        LOGGER.debug("Invalid timestamp '{}'", value);
    }
    catch (UnsupportedOperationException e) {
        LOGGER.debug("Error parsing timestamp '{}'", value, e);
    }
    return t;
  }

  /**
   * Returns the timestamp as a Joda {@link org.joda.time.DateTime} type.
   * @return See above.
   */
  public Instant asInstant() {
    return timestamp;
  }

  /**
   * Returns the timestamp as a Joda {@link org.joda.time.DateTime} type.
   * @param zone the DateTime instance uses the specified timezone, or the default zone if null.
   * @return See above.
   */
  public DateTime asDateTime(DateTimeZone zone) {
      return new DateTime(timestamp, zone);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    if (timestamp == null)
        return "";
    if ((timestamp.getMillis() % 1000) != 0)
        return ISO8601_FORMATTER_MS.print(timestamp);
    else
        return ISO8601_FORMATTER_S.print(timestamp);
  }

}