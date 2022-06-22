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

import ode.system.OdeContext;

/**
 * Message which wraps a {@link Throwable} instance since
 * {@link OdeContext#publishEvent(org.springframework.context.ApplicationEvent)}
 * cannot throw a checked exception.
 */
public class MessageException extends RuntimeException {

    private static final long serialVersionUID = 5563094828794438400L;

    protected final Throwable t;

    /**
     * Create an instance. The passed in {@link Throwable} is available via
     * {@link #getException()}.
     * 
     * @param msg
     * @param throwable
     */
    public MessageException(String msg, Throwable throwable) {
        super(msg, throwable);
        t = throwable;
    }

    /**
     * Get the exception which this instance wraps.
     * @return the {@link Throwable exception}
     */
    public Throwable getException() {
        return t;
    }

}