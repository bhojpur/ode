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

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * Factory for creating counter objects. Passed to other Spring beans to prevent
 * constant context lookups.
 */
public class CounterFactory implements ApplicationEventPublisherAware {

    protected ApplicationEventPublisher publisher;

    protected int objectsReadHardLimit = Integer.MAX_VALUE;

    protected int objectsWrittenHardLimit = Integer.MAX_VALUE;

    protected int methodHardLimit = Integer.MAX_VALUE;

    public void setApplicationEventPublisher(
            ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public void setObjectsReadHardLimit(int objectsReadHardLimit) {
        this.objectsReadHardLimit = objectsReadHardLimit;
    }

    public void setObjectsWrittenHardLimit(int objectsWrittenHardLimit) {
        this.objectsWrittenHardLimit = objectsWrittenHardLimit;
    }

    public void setMethodHardLimit(int methodHardLimit) {
        this.methodHardLimit = methodHardLimit;
    }

    public SessionStats createStats() {
        ObjectsReadCounter read = new ObjectsReadCounter(objectsReadHardLimit);
        read.setApplicationEventPublisher(publisher);
        ObjectsWrittenCounter written = new ObjectsWrittenCounter(
                objectsWrittenHardLimit);
        written.setApplicationEventPublisher(publisher);
        MethodCounter methods = new MethodCounter(methodHardLimit);
        methods.setApplicationEventPublisher(publisher);
        return new SimpleSessionStats(read, written, methods);
    }

}
