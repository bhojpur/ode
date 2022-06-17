package ode.security.auth;

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

import java.security.Permissions;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import ode.security.SecuritySystem;
import ode.services.messages.LoginAttemptMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Listens for any {@link LoginAttemptMessage}. If there are more than some
 * number of failures, then throttling beings to reduce the number of possible
 * checks. The next successful check resets the count to 0. The state is not
 * stored between server restarts.
 */

public class LoginAttemptListener implements
        ApplicationListener<LoginAttemptMessage> {

    private final static Logger log = LoggerFactory
            .getLogger(LoginAttemptListener.class);

    private final LoadingCache<String, AtomicInteger> counts = CacheBuilder.newBuilder().build(
            new CacheLoader<String, AtomicInteger>() {
                @Override
                public AtomicInteger load(String key) {
                    return new AtomicInteger(0);
                }
            });

    private final int throttleCount;

    private final long throttleTime;

    public LoginAttemptListener(int throttleCount, long throttleTime) {
        this.throttleCount = throttleCount;
        this.throttleTime = throttleTime;
    }

    public void onApplicationEvent(LoginAttemptMessage lam) {

        if (lam.success == null) {
            return; // EARLY EXIT.
        }

        AtomicInteger ai = null;
        try {
            ai = counts.get(lam.user);
        } catch (ExecutionException e) {
            /* cannot occur unless loading thread is interrupted */
        }
        if (lam.success) {
            int previous = ai.getAndSet(0);
            if (previous > 0) {
                log.info(String.format(
                        "Resetting failed login count of %s for %s", previous,
                        lam.user));
            }
        } else {
            int value = ai.incrementAndGet();
            if (value > throttleCount) {
                log.warn(String.format(
                        "%s failed logins for %s. Throttling for %s", value,
                        lam.user, throttleTime));
                if (throttleTime > 0) {
                    try {
                        Thread.sleep(throttleTime); // TODO something nicer
                    } catch (InterruptedException e) {
                        log.debug("Interrupt while throttling for " + lam.user);
                    }
                }
            }
        }
    }

}