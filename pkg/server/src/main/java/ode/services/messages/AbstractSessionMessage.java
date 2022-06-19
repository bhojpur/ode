package ode.services.messages;

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

import org.springframework.context.ApplicationListener;

import ode.util.messages.InternalMessage;

/**
 * {@link InternalMessage} implementations which carry relate to some
 * {@link ode.model.meta.Session} bound event.
 * These messages are <em>not</em> thread-safe
 * and so will be called within the same {@link Thread} as the publisher. This
 * means {@link ApplicationListener listeners} have a chance to throw an
 * exception and cancel the related event.
 * 
 * @see ode.services.sessions.SessionManager
 */
public abstract class AbstractSessionMessage extends InternalMessage {

    String id;

    public AbstractSessionMessage(Object source, String sessionId) {
        super(source);
        this.id = sessionId;
    }

    public String getSessionId() {
        return this.id;
    }

    @Override
    public final boolean isThreadSafe() {
        return false;
    }

}