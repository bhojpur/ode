package ode.services.throttling;

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

import ode.conditions.OverUsageException;
import ode.services.messages.stats.AbstractStatsMessage;
import ode.services.messages.stats.ObjectsReadStatsMessage;
import ode.services.messages.stats.ObjectsWrittenStatsMessage;
import ode.system.OdeContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;

/**
 * Throttling implementation which uses the calling server {@link Thread} for
 * execution. This mimics the behavior of the pre-AMD server
 */
public abstract class AbstractThrottlingStrategy implements ApplicationContextAware, ThrottlingStrategy {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected /*final*/ OdeContext ctx;

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.ctx = (OdeContext) applicationContext;
    }

    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ObjectsReadStatsMessage) {
            ObjectsReadStatsMessage read = (ObjectsReadStatsMessage) event;
            handle(read, read.getObjectsRead() + " objects read.");
        } else if (event instanceof ObjectsWrittenStatsMessage) {
            ObjectsWrittenStatsMessage written = (ObjectsWrittenStatsMessage) event;
            handle(written, written.getObjectsWritten() + " objects written.");
        }
    }

    private void handle(AbstractStatsMessage event, String msg) {
        if (event.isHard()) {
            throw new OverUsageException(String.format(
                    "Aborting execution: Reason = \"%s\"", msg));
        } else {
            log.info("Blocking for 5 seconds: " + msg);
            // Allow one second to pass before continuing
            while (System.currentTimeMillis() < event.getTimestamp() + 5000L) {
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    // ok
                }
            }
        }
    }

}