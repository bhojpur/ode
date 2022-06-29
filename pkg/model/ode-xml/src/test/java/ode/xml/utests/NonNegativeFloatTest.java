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

import static org.testng.AssertJUnit.*;

import ode.xml.model.primitives.NonNegativeFloat;

import org.testng.annotations.Test;

/**
 */
public class NonNegativeFloatTest {

  @Test(expectedExceptions={IllegalArgumentException.class})
  public void testMinusOne() {
    new NonNegativeFloat(-1.0);
  }

  @Test
  public void testZero() {
    new NonNegativeFloat(0.0);
  }

  @Test
  public void testPositiveOne() {
    new NonNegativeFloat(1.0);
  }

  @Test
  public void testEquivalence() {
    NonNegativeFloat a = new NonNegativeFloat(0.1);
    NonNegativeFloat b = new NonNegativeFloat(0.1);
    NonNegativeFloat c = new NonNegativeFloat(2.0);
    Double d = new Double(0.1);
    Double e = new Double(2.0);
    Double f = new Double(3.0);
    assertFalse(a == b);
    assertFalse(a == c);
    assertTrue(a.equals(b));
    assertFalse(a.equals(c));
    assertTrue(a.equals(d));
    assertFalse(a.equals(e));
    assertTrue(c.equals(e));
    assertFalse(a.equals(f));
  }

  @Test
  public void testToString() {
    assertEquals("0.1", new NonNegativeFloat(0.1).toString());
  }
}