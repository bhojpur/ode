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
 * Primitive type that represents an RGBA color.
 */
public class Color extends PrimitiveType<Integer> {

  public Color(Integer value) {
    super(value);
  }

  public Color(int r, int g, int b, int a) {
    this(r << 24 | g << 16 | b << 8 | a);
  }

  /**
   * Returns an <code>Color</code> object holding the value of
   * the specified string.
   * @param s The string to be parsed.
   * @return See above.
   */
  public static Color valueOf(String s) {
    return new Color(Integer.valueOf(s));
  }

  /**
   * Returns the red component of this color.
   *
   * @return See above.
   */
  public int getRed() {
    return (getValue() >> 24) & 0xff;
  }

  /**
   * Returns the green component of this color.
   *
   * @return See above.
   */
  public int getGreen() {
    return (getValue() >> 16) & 0xff;
  }

  /**
   * Returns the blue component of this color.
   *
   * @return See above.
   */
  public int getBlue() {
    return (getValue() >> 8) & 0xff;
  }

  /**
   * Returns the alpha component of this color.
   *
   * @return See above.
   */
  public int getAlpha() {
    return getValue() & 0xff;
  }

}