package org.libjpegturbo.turbojpeg;

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
 * Fractional scaling factor
 */
public class TJScalingFactor {

  public TJScalingFactor(int num, int denom) throws Exception {
    if(num < 1 || denom < 1)
      throw new Exception("Numerator and denominator must be >= 1");
    this.num = num;
    this.denom = denom;
  }

  /**
   * Returns numerator
   * @return numerator
   */
  public int getNum() {
    return num;
  }

  /**
   * Returns denominator
   * @return denominator
   */
  public int getDenom() {
    return denom;
  }

  /**
   * Returns the scaled value of <code>dimension</code>.  This function
   * performs the integer equivalent of
   * <code>ceil(dimension * scalingFactor)</code>.
   * @return the scaled value of <code>dimension</code>
   */
  public int getScaled(int dimension) {
    return (dimension * num + denom - 1) / denom;
  }

  /**
   * Returns true or false, depending on whether this instance and
   * <code>other</code> have the same numerator and denominator.
   * @return true or false, depending on whether this instance and
   * <code>other</code> have the same numerator and denominator
   */
  public boolean equals(TJScalingFactor other) {
    return (this.num == other.num && this.denom == other.denom);
  }

  /**
   * Returns true or false, depending on whether this instance is equal to
   * 1/1.
   * @return true or false, depending on whether this instance is equal to
   * 1/1
   */
  public boolean isOne() {
    return (num == 1 && denom == 1);
  }

  /**
   * Numerator
   */
  private int num = 1;

  /**
   * Denominator
   */
  private int denom = 1;
};
