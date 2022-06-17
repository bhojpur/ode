package ode.services.util;

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

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ode.security.SecuritySystem;
import ode.system.EventContext;

/**
 * Sets timeouts for queries according to event context.
 */
public class TimeoutSetter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeoutSetter.class);

    private final SecuritySystem securitySystem;

    private final int timeout, timeoutAdmin;  /* in seconds */

    /**
     * Construct the timeout setter.
     * @param securitySystem the security system
     * @param timeout the timeout to set for regular users, in seconds
     * @param timeoutAdmin the timeout to set for administrative users, in seconds
     */
    public TimeoutSetter(SecuritySystem securitySystem, int timeout, int timeoutAdmin) {
        if (timeout < 1 || timeoutAdmin < 1) {
            throw new IllegalArgumentException("query timeouts must be strictly positive");
        }
        this.securitySystem = securitySystem;
        this.timeout = timeout;
        this.timeoutAdmin = timeoutAdmin;
        if (timeout == timeoutAdmin) {
            LOGGER.info("Query timeout set to {}s for all users.", timeout);
        } else {
            LOGGER.info("Query timeout set to {}s and for administrators to {}s.", timeout, timeoutAdmin);
        }
    }

    /**
     * Set the timeout on the given query.
     * @param query a query consuming a timeout setting
     */
    public void setTimeout(Consumer<Integer> query) {
        final EventContext ec = securitySystem.getEventContext();
        final int selectedTimeout = ec.isCurrentUserAdmin() ? timeoutAdmin : timeout;
        query.accept(selectedTimeout);
        final Long userId = ec.getCurrentUserId();
        if (userId == null) {
            LOGGER.debug("Set timeout for unknown user's query to {}s.", selectedTimeout);
        } else {
            LOGGER.debug("Set timeout for user {}'s query to {}s.", userId, selectedTimeout);
        }
    }
}