package ode.tools.spring;

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

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * {@link ApplicationListener} which only listens for
 * {@link ContextRefreshedEvent} <em>and</em> only responds to the first one.
 */
public abstract class OnContextRefreshedEventListener implements
        ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private/* final */ApplicationContext ctx;

    private final boolean handleOthers;

    private final int limit;

    private final AtomicInteger count = new AtomicInteger(0);

    public OnContextRefreshedEventListener() {
        this(false, 1);
    }

    public OnContextRefreshedEventListener(boolean handleOthers, int limit) {
        this.handleOthers = handleOthers;
        this.limit = limit;
    }

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.ctx = applicationContext;
    }

    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext() != ctx && !handleOthers) {
            if (log.isDebugEnabled()) {
                log.debug("Ignoring other application context refresh: " + ctx);
            }
            return;
        }

        int current = count.incrementAndGet();
        if (current > limit) {
            if (log.isDebugEnabled()) {
                log.debug("Ignoring refresh beyond limit: " + current);
            }
            return;
        }
        handleContextRefreshedEvent(event);
    }

    public abstract void handleContextRefreshedEvent(ContextRefreshedEvent event);

}