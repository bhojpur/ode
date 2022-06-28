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
 * Interface for cache strategies. A cache strategy identifies which
 * objects should be loaded into the cache, in which order. Unlike
 * {@link ICacheSource}, it works with multidimensional (N-D) position arrays
 * rather than rasterized (1-D) indices. The two are made equivalent via a
 * mapping between the two, invoked within {@link Cache} as needed.
 */
public interface ICacheStrategy extends CacheReporter {

  // -- Constants --

  int MIN_PRIORITY = -10;
  int LOW_PRIORITY = -5;
  int NORMAL_PRIORITY = 0;
  int HIGH_PRIORITY = 5;
  int MAX_PRIORITY = 10;

  int CENTERED_ORDER = 0;
  int FORWARD_ORDER = 1;
  int BACKWARD_ORDER = -1;

  // -- ICacheStrategy API methods --

  /**
   * Gets the indices of the objects to cache,
   * surrounding the object at the given position.
   */
  int[][] getLoadList(int[] pos) throws CacheException;

  /** Retrieves the priority for caching each axis. */
  int[] getPriorities();

  /** Sets the priority for caching the given axis. */
  void setPriority(int priority, int axis);

  /**
   * Retrieves the order in which objects should be loaded along each axis.
   * @return An array whose constituents are each one of:<ul>
   *   <li>CENTERED_ORDER</li>
   *   <li>FORWARD_ORDER</li>
   *   <li>BACKWARD_ORDER</li>
   */
  int[] getOrder();

  /**
   * Sets the order in which objects should be loaded along each axis.
   * @param order One of:<ul>
   *   <li>CENTERED_ORDER</li>
   *   <li>FORWARD_ORDER</li>
   *   <li>BACKWARD_ORDER</li>
   * @param axis The axis for which to set the order.
   */
  void setOrder(int order, int axis);

  /** Retrieves the number of objects to cache along each axis. */
  int[] getRange();

  /** Sets the number of objects to cache along the given axis. */
  void setRange(int num, int axis);

  /** Gets the lengths of all the axes. */
  int[] getLengths();

}
