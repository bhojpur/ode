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

/**
 * A class for representing a rectangular region.
 * This class is very similar to {@link java.awt.Rectangle};
 * it mainly exists to avoid problems with AWT, JNI and headless operation.
 */
public class Region {

  // -- Fields --

  public int x;
  public int y;
  public int width;
  public int height;

  // -- Constructors --

  public Region() {
  }

  public Region(int x, int y, int w, int h) {
    this.x = x;
    this.y = y;
    this.width = w;
    this.height = h;
  }

  // -- Region API --

  /**
   * @param r the region to check for intersection
   * @return true if this region intersects the given region
   * @see java.awt.Rectangle#intersects(java.awt.Rectangle)
   */
  public boolean intersects(Region r) {
    int tw = this.width;
    int th = this.height;
    int rw = r.width;
    int rh = r.height;
    if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
      return false;
    }
    int tx = this.x;
    int ty = this.y;
    int rx = r.x;
    int ry = r.y;
    rw += rx;
    rh += ry;
    tw += tx;
    th += ty;
    boolean rtn = ((rw < rx || rw > tx) && (rh < ry || rh > ty) &&
      (tw < tx || tw > rx) && (th < ty || th > ry));
    return rtn;
  }

  /**
   * Returns a Region representing the intersection of this Region with the
   * given Region.  If the two Regions do not intersect, the result is an
   * empty Region.
   *
   * @param r the region for which to calculate an intersection (or overlap)
   * @return a Region representing the intersection (overlap) of the two Regions.
   *         If the two Regions have no common area, then the width and/or height
   *         of the returned Region will be 0.  null is never returned.
   */
  public Region intersection(Region r) {
    int x = Math.max(this.x, r.x);
    int y = Math.max(this.y, r.y);
    int w = Math.min(this.x + this.width, r.x + r.width) - x;
    int h = Math.min(this.y + this.height, r.y + r.height) - y;

    if (w < 0) w = 0;
    if (h < 0) h = 0;

    return new Region(x, y, w, h);
  }

  /**
   * Returns true if the point specified by the given X and Y coordinates
   * is contained within this region.
   *
   * @param xc the integer X coordinate of a point
   * @param yc the integer Y coordinate of a point
   * @return true if this Region encloses the given point
   */
  public boolean containsPoint(int xc, int yc) {
    return intersects(new Region(xc, yc, 1, 1));
  }

  @Override
  public String toString() {
    return "x=" + x + ", y=" + y + ", w=" + width + ", h=" + height;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Region)) return false;

    Region that = (Region) o;
    return this.x == that.x && this.y == that.y && this.width == that.width &&
      this.height == that.height;
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

}