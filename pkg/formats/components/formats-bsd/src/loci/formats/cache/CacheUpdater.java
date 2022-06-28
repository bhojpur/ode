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
