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

import java.lang.reflect.Field;

/**
 * A event indicating a cache update.
 */
public class CacheEvent {

  // -- Constants --

  /** Event type indicating a new cache source. */
  public static final int SOURCE_CHANGED = 1;

  /** Event type indicating a new cache strategy. */
  public static final int STRATEGY_CHANGED = 2;

  /** Event type indicating an updated current dimensional position. */
  public static final int POSITION_CHANGED = 3;

  /** Event type indicating new axis priorities. */
  public static final int PRIORITIES_CHANGED = 4;

  /** Event type indicating new planar ordering. */
  public static final int ORDER_CHANGED = 5;

  /** Event type indicating new planar ordering. */
  public static final int RANGE_CHANGED = 6;

  /** Event type indicating an object has been added to the cache. */
  public static final int OBJECT_LOADED = 7;

  /** Event type indicating an object has been removed from the cache. */
  public static final int OBJECT_DROPPED = 8;

  // -- Fields --

  /** Source of the cache update. */
  protected Object source;

  /** Type of cache event. */
  protected int type;

  /** Relevant index to the event, if any. */
  protected int index;

  // -- Constructor --

  /** Constructs a cache event. */
  public CacheEvent(Object source, int type) { this(source, type, -1); }

  /** Constructs a cache event. */
  public CacheEvent(Object source, int type, int index) {
    this.source = source;
    this.type = type;
    this.index = index;
  }

  // -- CacheEvent API methods --

  /** Gets the source of the cache update. */
  public Object getSource() { return source; }

  /** Gets the type of cache update. */
  public int getType() { return type; }

  /**
   * Gets the index relevant to the cache update, if any.
   * This parameter is only set for events POSITION_CHANGED,
   * OBJECT_LOADED and OBJECT_DROPPED.
   */
  public int getIndex() { return index; }

  // -- Object API methods --

  @Override
  public String toString() {
    // scan public fields to determine type name
    String sType = "unknown";
    Field[] fields = getClass().getFields();
    for (int i=0; i<fields.length; i++) {
      try {
        if (fields[i].getInt(null) == type) sType = fields[i].getName();
      }
      catch (IllegalAccessException exc) { }
      catch (IllegalArgumentException exc) { }
    }
    return super.toString() +
      ": source=[" + source + "] type=" + sType + " index=" + index;
  }

}
