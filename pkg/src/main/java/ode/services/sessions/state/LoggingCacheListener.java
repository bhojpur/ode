package ode.services.sessions.state;

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

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic {@link CacheEventListener} which does nothing other than log.
 */
class LoggingCacheListener implements CacheEventListener {

    private final static Logger log = LoggerFactory
            .getLogger(LoggingCacheListener.class);

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void dispose() {
        log.debug("Disposing cache event listener.");
    }

    public void notifyElementEvicted(Ehcache arg0, Element arg1) {
        if (log.isDebugEnabled()) {
            log.debug("Evicting element: " + idString(arg1));
        }
    }

    public void notifyElementExpired(Ehcache arg0, Element arg1) {
        if (log.isDebugEnabled()) {
            log.debug("Expiring servant: " + idString(arg1));
        }
    }

    public void notifyElementPut(Ehcache arg0, Element arg1)
            throws CacheException {
        if (log.isDebugEnabled()) {
            log.debug("Putting element: " + idString(arg1));
        }
    }

    public void notifyElementRemoved(Ehcache arg0, Element arg1)
            throws CacheException {
        if (log.isDebugEnabled()) {
            log.debug("Removing element: " + idString(arg1));
        }
    }

    public void notifyElementUpdated(Ehcache arg0, Element arg1)
            throws CacheException {
        if (log.isDebugEnabled()) {
            log.debug("Updating element: " + idString(arg1));
        }
    }

    public void notifyRemoveAll(Ehcache arg0) {
        log.debug("Removing all elements from servant cache.");
    }

    protected String idString(Element elt) {
        Object key = elt.getObjectKey();
        return key.toString();
    }

}
