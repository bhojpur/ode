/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.cache;

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
