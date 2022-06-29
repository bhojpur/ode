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

/**
 * A floating point value whose constraints are bound to Java's 32-bit floating
 * point type and a further 0.0f minimum and 1.0f maximum.
 */
public class PercentFraction extends PrimitiveType<Float> implements PrimitiveNumber {

  public PercentFraction(Float value) {
    super(value);
    if (value == null || value.floatValue() < 0.0 || value.floatValue() > 1.0) {
      throw new IllegalArgumentException(
          value + " must be non-null and between 0.0 and 1.0 inclusive.");
    }
  }

  public Number getNumberValue() {
    return value;
  }

  /**
   * Returns an <code>PercentFraction</code> object holding the value of
   * the specified string.
   * @param s The string to be parsed.
   * @return See above.
   */
  public static PercentFraction valueOf(String s) {
    return new PercentFraction(Float.valueOf(s));
  }
}