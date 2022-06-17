package ode.services.sessions.stats;

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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import ode.util.messages.InternalMessage;

/**
 * Counter object which increments an internal long by some integer value,
 * and according to some strategy publishes an {@link InternalMessage} subclass.
 */
public abstract class LongCounter implements ApplicationEventPublisherAware {

    private final Logger log = LoggerFactory.getLogger(getClass());
    
    private ApplicationEventPublisher publisher;
    
    private int interval = 0;
    
    private long last = 0;
    
    private final Object mutex = new Object();
    
    protected long count = 0;
    
    public LongCounter(int interval) {
        this.interval = interval;
    }

    public void setApplicationEventPublisher(
            ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public void increment(int incr) {
        synchronized (mutex) {
            count = count + incr;
            if (count >= (last+interval)) {
                last = count;
                InternalMessage message = message();
                try {
                    log.info("Publishing "+ message);
                    publisher.publishEvent(message);
                } catch (Throwable t) {
                    log.error(message + " produced an error: "+t);
                }
            }
        }
    }
 
    /**
     * 
     * @return The message.
     */
    protected abstract InternalMessage message();

}
