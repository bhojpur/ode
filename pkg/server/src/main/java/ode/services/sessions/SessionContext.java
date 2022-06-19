package ode.services.sessions;

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

import java.util.List;

import ode.system.EventContext;
import ode.model.meta.Session;
import ode.services.sessions.state.SessionCache;
import ode.services.sessions.stats.SessionStats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extends {@link EventContext} to hold a {@link Session}. This is used by the
 * {@link SessionManager} to store information in the {@link SessionCache}.
 */
public interface SessionContext extends EventContext {

    Session getSession();

    List<String> getUserRoles();

    // Reference counting

    /**
     * The Share id is the only mutable bit in the {@link SessionContext}.
     */
    void setShareId(Long shareId);

    /**
     * Return a {@link SessionStats} implementation for this session.
     */
    SessionStats stats();

    /**
     * Returns the {@link Count} instance held by this context. This may be
     * shared with other contexts, so that in critical phases as when the context
     * is being copied, the reference count will be kept in sync.
     */
    Count count();

    /**
     * Synchronized counter which can be passed between {@link SessionContext}
     * instances as they are recreated.
     */
    public class Count {
        private final Logger log = LoggerFactory.getLogger(Count.class);
        private final Object[] refLock = new Object[0];
        private int ref;
        private String uuid;

        public Count(String uuid) {
            this.uuid = uuid;
        }

        /**
         * Return the current number of references which this session is aware of.
         */
        public int get() {
            synchronized (refLock) {
                return ref;
            }
        }

        /**
         * Increment the current {@link #ref reference count} and return the
         * new value atomically.
         */
        public int increment() {
            synchronized (refLock) {
                if (ref < 0) {
                    ref = 1;
                } else {
                    // This should never happen, but just in case
                    // some loop is incrementing indefinitely.
                    if (ref < Integer.MAX_VALUE) {
                        ref = ref + 1;
                        if (log.isDebugEnabled()) {
                            log.debug("+Reference count: " + uuid + "=" + ref);
                        }
                    } else {
                        log.warn("Reference count == MAX_VALUE");
                    }
                }
                return ref;
            }
        }

        /**
         * Decrement the current {@link #ref reference count} and return the
         * new value atomically.
         */
        public int decrement() {
            synchronized (refLock) {
                if (ref < 1) {
                    ref = 0;
                } else {
                    ref = ref - 1;
                    log.info("-Reference count: " + uuid + "=" + ref);
                }
                return ref;
            }
        }
    }
}
