package ode.util;

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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Container class for storing resources which should be cleaned up on close and
 * periodically checked.
 *
 * Note: this class uses java.util.logging (JUL) rather than commons-logging
 * since it may be used on the client-side. Any use server-side will have logs
 * forwarded to log4j via slf4j.
 */
public class Resources {

    /**
     * Interface to be implemented by any object which wants to be managed by
     * the {@link Resources} class.
     */
    public interface Entry {
        /**
         * Called during each cycle. If it returns false or throws an exception,
         * {@link #cleanup()} will be called on the instance, and it will be
         * removed.
         */
        boolean check();

        /**
         * Gives an {@link Entry} a chance to cleanup resources before it is
         * removed from checking.
         */
        void cleanup();
    }

    private static Logger log = Logger.getLogger(Resources.class.getName());

    private final int sleeptime;

    private final ScheduledFuture<?> future;

    private final ScheduledExecutorService service;

    private final List<Entry> stuff = new CopyOnWriteArrayList<Entry>();

    /**
     * As {@link Resources#Resources(int)} but specifies a 60 second sleep
     * period between task execution.
     */
    public Resources() {
        this(60);
    }

    /**
     * As {@link Resources#Resources(int, ScheduledExecutorService)} but uses a
     * {@link Executors#newSingleThreadExecutor()}.
     */
    public Resources(int sleeptimeSeconds) {
        this(sleeptimeSeconds, Executors.newSingleThreadScheduledExecutor());
    }

    public Resources(int sleeptimeSeconds, ScheduledExecutorService service) {
        this.sleeptime = sleeptimeSeconds;
        this.service = service;
        log.finest("Starting");
        this.future = this.service.scheduleAtFixedRate(task(), 1, sleeptime, TimeUnit.SECONDS);
    }

    private Runnable task() {
        return new Runnable() {
            public void run() {
                log.finest("Running checks...");
                for (Entry entry : stuff) {
                    log.finest("Checking " + entry);
                    boolean success = true;
                    try {
                        success = entry.check();
                    } catch (Exception e) {
                        log.warning("Exception thrown by entry: "
                                + e.getMessage());
                        success = false;
                    }
                    if (!success) {
                        remove(entry);
                    }
                }
                log.finest("Finished checks.");
            }

        };
    }

    public void add(Entry entry) {
        if (entry == null) {
            log.warning("Entry null");
            return;
        }
        log.finest("Adding object " + entry);
        stuff.add(entry);
    }

    public int size() {
        return stuff.size();
    }

    public void cleanup() {

        log.finest("Cleaning called");

        for (Entry entry : stuff) {
            remove(entry);
        }

        log.finest("Stopping");
        // Cancel thread; allows current task to finish
        future.cancel(false);
        log.finest("Stopped");
    }

    public void remove(Entry entry) {
        if (entry == null) {
            log.warning("Entry null");
            return;
        }
        log.finest("Cleaning " + entry);
        try {
            entry.cleanup();
        } catch (Exception e) {
            log.warning("Cleaning entry threw an exception" + e);
        }
        log.finest("Removing " + entry);
        stuff.remove(entry);
    }

}