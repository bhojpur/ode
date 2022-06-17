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

import java.util.Map;

import ode.system.EventContext;
import ode.util.messages.InternalMessage;

public class ContextMessage extends InternalMessage {

    private static final long serialVersionUID = -1L;

    public final Map<String, String> context;

    public ContextMessage(Object source, Map<String, String> context) {
        super(source);
        this.context = context;
    }

    /**
     * Published when an internal service would like to
     * modify the current {@link EventContext}.
     * A {@link ContextMessage.Pop PopContextMessage} should be used once
     * the temporary login is complete.
     */
    public static class Push extends ContextMessage {

        private static final long serialVersionUID = -1L;

        public Push(Object source, Map<String, String> context) {
            super(source, context);
        }

    }

    /**
     * Published when an internal service is finished with
     * the context previously signaled through publishing
     * a {@link Push}.
     */
    public static class Pop extends ContextMessage {

        private static final long serialVersionUID = -1L;

        public Pop(Object source, Map<String, String> context) {
            super(source, context);
        }

    }
}
