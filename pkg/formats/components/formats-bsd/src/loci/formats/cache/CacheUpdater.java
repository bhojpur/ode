/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread responsible for updating the cache
 * (loading and dropping planes) in the background.
 */
public class CacheUpdater extends Thread {

  // -- Constants --

  private static final Logger LOGGER =
    LoggerFactory.getLogger(CacheUpdater.class);

  // -- Fields --

  private Cache cache;
  private boolean quit;

  // -- Constructors --

  public CacheUpdater(Cache cache) {
    super("ODE-Formats-Cache-Updater");
    setPriority(Thread.MIN_PRIORITY);
    this.cache = cache;
    quit = false;
  }

  // -- CacheUpdater API methods --

  public void quit() {
    quit = true;
    // NB: Must wait for thread to die; ODE-Formats is not thread-safe, so
    // it would be bad for more than one CacheUpdater thread to try to use the
    // same IFormatReader at the same time.
    try {
      join();
    }
    catch (InterruptedException exc) {
      LOGGER.info("Thread interrupted", exc);
    }
  }

  // -- Thread API methods --

  @Override
  public void run() {
    int length = 0;
    try {
      length = cache.getStrategy().getLoadList(cache.getCurrentPos()).length;
      for (int i=0; i<length; i++) {
        if (quit) break;
        cache.recache(i);
      }
    }
    catch (CacheException e) {
      LOGGER.info("", e);
    }
  }

}
