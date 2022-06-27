/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.cache;

/**
 * A listener for cache updates.
 */
public interface CacheListener {

  /** Called when cache is updated. */
  void cacheUpdated(CacheEvent e);

}
