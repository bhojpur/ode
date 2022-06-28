package loci.formats.tiff;

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
 * A rational number (numerator over denominator).
 */
public class TiffRational extends Number implements Comparable<TiffRational> {

  // -- Fields --

  /** Components of the rational's fractional representation. */
  protected long numer, denom;

  // -- Constructor --

  /** Constructs a rational number. */
  public TiffRational(long numer, long denom) {
    this.numer = numer;
    this.denom = denom;
  }

  // -- TiffRational API methods --

  /** Gets this number's numerator. */
  public long getNumerator() { return numer; }

  /** Gets this number's denominator. */
  public long getDenominator() { return denom; }

  /** Reduces this rational's fraction to lowest terms. */
  public void reduce() {
    long sqrt1 = (long) Math.sqrt(numer);
    long sqrt2 = (long) Math.sqrt(denom);
    long gcdMax = sqrt1 < sqrt2 ? sqrt1 : sqrt2;
    // search for greatest common divisor
    for (long i=gcdMax; i>=2; i--) {
      if (numer % i == 0 && denom % i == 0) {
        numer /= i;
        denom /= i;
        reduce();
        break;
      }
    }
  }

  // -- Number API methods --

  /** Returns the value of the specified number as a byte. */
  @Override
  public byte byteValue() { return (byte) longValue(); }

  /** Returns the value of the specified number as a double. */
  @Override
  public double doubleValue() {
    return denom == 0 ? Double.MAX_VALUE : ((double) numer / (double) denom);
  }

  /** Returns the value of the specified number as a float. */
  @Override
  public float floatValue() { return (float) doubleValue(); }

  /** Returns the value of the specified number as an int. */
  @Override
  public int intValue() { return (int) longValue(); }

  /** Returns the value of the specified number as a long. */
  @Override
  public long longValue() {
    return denom == 0 ? Long.MAX_VALUE : (numer / denom);
  }

  /** Returns the value of the specified number as a short. */
  @Override
  public short shortValue() { return (short) longValue(); }

  // -- Object API methods --

  /** Indicates whether some other object is "equal to" this one. */
  @Override
  public boolean equals(Object o) {
    return o != null && o instanceof TiffRational &&
      compareTo((TiffRational) o) == 0;
  }

  /** Reasonable hash value for use with hashtables. */
  @Override
  public int hashCode() { return (int) (numer - denom); }

  /** Returns a string representation of the object. */
  @Override
  public String toString() { return numer + "/" + denom; }

  // -- Comparable API methods --

  /**
   * Compares this object with the specified object for order.
   * Returns a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the specified object.
   */
  @Override
  public int compareTo(TiffRational q) {
    long diff = (numer * q.denom - q.numer * denom);
    if (diff > Integer.MAX_VALUE) diff = Integer.MAX_VALUE;
    else if (diff < Integer.MIN_VALUE) diff = Integer.MIN_VALUE;
    return (int) diff;
  }

}
