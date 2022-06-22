package ode.util.messages;

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

import java.util.EventObject;

import ode.system.OdeContext;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ApplicationEventMulticaster;

/**
 * Message which can be published via
 * {@link OdeContext#publishEvent(ApplicationEvent)} or
 * {@link OdeContext#publishMessage(InternalMessage)}. It is currently
 * assumed that the Spring-configured {@link ApplicationEventMulticaster} will
 * publish the {@link InternalMessage} in the current {@link Thread}. If a
 * subclass can properly handle the threaded case, it should set
 * {@link #threadSafe} to true.
 * 
 * Since the the {@link OdeContext#publishEvent(ApplicationEvent)} does not
 * allow for an exception, consumers of {@link InternalMessage} subclasses can
 * throw a {@link MessageException} which will properly handled by the
 * {@link OdeContext#publishMessage(InternalMessage)} method. (Users of
 * {@link OdeContext#publishEvent(ApplicationEvent)} will have to manually
 * unwrap the {@link MessageException}.
 * 
 * 
 * Note: this class may or may not be useful for the client-side, but it must be
 * in the common/ package for use by {@link ode.system.OdeContext}
 * 
 * @see MessageException
 * @see OdeContext
 */
public abstract class InternalMessage extends ApplicationEvent {

    protected boolean threadSafe = false;

    /**
     * Sole constructor which takes the "source" of this {@link EventObject}.
     * 
     * @param source
     * @see EventObject#EventObject(Object)
     */
    public InternalMessage(Object source) {
        super(source);
    }

    /**
     * Returns true if this message can safely be passed to another
     * {@link Thread}. The default {@link ApplicationEventMulticaster} executes
     * in the same {@link Thread}.
     */
    public boolean isThreadSafe() {
        return threadSafe;
    }

}