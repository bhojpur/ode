/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.cache;

/**
 * Interface for components capable of reporting cache updates.
 */
public interface CacheReporter {

  /** Adds a listener for status update events. */
  void addCacheListener(CacheListener l);

  /** Removes a listener for status update events. */
  void removeCacheListener(CacheListener l);

  /** Gets a list of all registered status update listeners. */
  CacheListener[] getCacheListeners();

}
