package loci.formats.cache;

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
 * A rectangle strategy caches planes extending from the the current
 * dimensional position along each axis and combination of axes.
 * For example, if the current position is Z5-C2-T18, the strategy will
 * preload the next and previous focal planes (Z6-C2-T18 and Z4-C2-T18),
 * the next and previous channels (Z5-C3-T18 and Z5-C1-T18),
 * the next and previous time points (Z5-C2-T19 and Z5-C2-T17), and
 * all combinations thereof (Z6-C3-T18, Z6-C1-T18, Z4-C3-T18, Z4-C1-T18,
 * Z6-C3-T19, etc.).
 * <p>
 * Planes closest to the current position are loaded first, with axes
 * prioritized according to the cache strategy's priority settings.
 * <p>
 * To illustrate the rectangle strategy, here is a diagram showing a case
 * in 2D with 35 dimensional positions (5Z x 7T). For both Z and T, order is
 * centered, range is 2, and priority is normal.
 * The numbers indicate the order planes will be cached, with "0"
 * corresponding to the current dimensional position (Z2-T3).
 * <pre>
 *      T  0  1  2  3  4  5  6
 *    Z /---------------------
 *    0 |    24 20  6 14 22
 *    1 |    18 12  2 10 16
 *    2 |     8  4  0  3  7
 *    3 |    17 11  1  9 15
 *    4 |    23 19  5 13 21
 * </pre>
 */
public class RectangleStrategy extends CacheStrategy {

  // -- Constructor --

  /** Constructs a rectangle strategy. */
  public RectangleStrategy(int[] lengths) { super(lengths); }

  // -- CacheStrategy API methods --

  /* @see CacheStrategy#getPossiblePositions() */
  @Override
  protected int[][] getPossiblePositions() {
    // with sufficient range, any position could be cached
    int[][] p = new int[length()][lengths.length];
    for (int i=0; i<p.length; i++) pos(i, p[i]);
    return p;
  }

}
