package ode.security.basic;

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

import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ode.util.SqlAction;

/**
 * Periodically clean up old entries from the <tt>_current_admin_privileges</tt> database table.
 * Relies on {@link org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean#setConcurrent(boolean)}
 * having disabled concurrency via {@code false}.
 */
public class LightAdminPrivilegesCleanup implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(LightAdminPrivilegesCleanup.class);

    private final SqlAction sqlAction;
    private final long halfDelayMs;

    private long latestRunEnded = Long.MIN_VALUE;
    private Collection<Long> transactionIds = Collections.emptyList();

    /**
     * Start a new scheduled repeating task for cleaning up the <tt>_current_admin_privileges</tt> database table.
     * @param sqlAction the SQL action to use for executing the cleanup JDBC
     * @param delay the interval to wait in between cleanups, in seconds
     */
    public LightAdminPrivilegesCleanup(SqlAction sqlAction, int delay) {
        this.sqlAction = sqlAction;
        /* require delay of at least half the interval */
        halfDelayMs = 500L * delay;
    }

    @Override
    public void run() {
        if (latestRunEnded + halfDelayMs < System.currentTimeMillis()) {
            LOGGER.debug("running periodic cleanup of _current_admin_privileges table");
        } else {
            /* simple emulation of ThreadPoolTaskScheduler.scheduleWithFixedDelay */
            LOGGER.debug("skipping periodic cleanup of _current_admin_privileges table");
            return;
        }
        sqlAction.deleteOldAdminPrivileges(transactionIds);
        transactionIds = sqlAction.findOldAdminPrivileges();
        latestRunEnded = System.currentTimeMillis();
    }
}