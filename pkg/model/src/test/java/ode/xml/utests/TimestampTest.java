package ode.xml.utests;

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
import org.joda.time.LocalDateTime;

import ode.xml.model.primitives.Timestamp;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 */
public class TimestampTest {

  public static final String TIMESTAMP = "2011-10-20T15:07:14.732Z";

  private final Timestamp a = new Timestamp(TIMESTAMP);

  private void assertYMDHMSS(Instant i) {
    LocalDateTime lt = new LocalDateTime(i);
    Assert.assertEquals(lt.getYear(), 2011);
    Assert.assertEquals(lt.getMonthOfYear(), 10);
    Assert.assertEquals(lt.getDayOfMonth(), 20);
    Assert.assertEquals(lt.getHourOfDay(), 15);
    Assert.assertEquals(lt.getMinuteOfHour(), 7);
    Assert.assertEquals(lt.getSecondOfMinute(), 14);
    Assert.assertEquals(lt.getMillisOfSecond(), 732);
  }

  @Test
  public void testAsInstant()
  {
    Instant i = a.asInstant();
    assertYMDHMSS(i);
  }

  @Test
  public void testAsDateTime()
  {
    DateTime d = a.asDateTime(null);
    assertYMDHMSS(d.toInstant());
  }

  @Test
  public void testInstantConstructor() {
    Timestamp b = new Timestamp(a.asInstant());
    Assert.assertEquals(b, a);
    assertYMDHMSS(b.asInstant());
  }

  @Test
  public void testDateTimeConstructor() {
    Timestamp b = new Timestamp(a.asDateTime(null));
    Assert.assertEquals(b, a);
    assertYMDHMSS(b.asInstant());
  }

  @Test
  public void testString() {
    Timestamp t1 = Timestamp.valueOf("2003-08-26T19:46:38");
    Timestamp t2 = Timestamp.valueOf("2003-08-26T19:46:38.762");
    Timestamp t3 = Timestamp.valueOf("2003-08-26T19:46:38.762Z");
    Timestamp t4 = Timestamp.valueOf("2003-08-26T19:46:38.762+0400");
    Timestamp t5 = Timestamp.valueOf("invalid");
    Timestamp t6 = Timestamp.valueOf("2011-10-20T15:07:14");
    Timestamp t7 = Timestamp.valueOf("2011-10-20T15:07:14Z");
    Timestamp t8 = Timestamp.valueOf("2011-10-20T15:07:14.632Z");

    Assert.assertEquals(t1.toString(), "2003-08-26T19:46:38");
    Assert.assertEquals(t2.toString(), "2003-08-26T19:46:38.762");
    Assert.assertEquals(t3.toString(), "2003-08-26T19:46:38.762");
    Assert.assertEquals(t4.toString(), "2003-08-26T15:46:38.762");
    Assert.assertEquals(t5, null);
    Assert.assertEquals(t6.toString(), "2011-10-20T15:07:14");
    Assert.assertEquals(t7.toString(), "2011-10-20T15:07:14");
    Assert.assertEquals(t8.toString(), "2011-10-20T15:07:14.632");
  }

}