package ode.services.sessions.events;

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

import java.util.ArrayList;
import java.util.List;

import ode.conditions.SecurityViolation;
import ode.model.IObject;
import ode.services.sessions.SessionManager;
import ode.util.messages.InternalMessage;

/**
 * {@link InternalMessage} published by the {@link SessionManager} when
 * setSecurityContext() is called.
 */
public class ChangeSecurityContextEvent extends InternalMessage {

    final List<String> cancellations = new ArrayList<String>();

    final private String sessionUuid;

    final private IObject previous;

    final private IObject next;

    public ChangeSecurityContextEvent(Object source, String sessionUuid,
            IObject previous, IObject next) {
        super(source);
        this.sessionUuid = sessionUuid;
        this.previous = previous;
        this.next = next;
    }

    private static final long serialVersionUID = 1L;

    public synchronized String getUuid() {
        return sessionUuid;
    }

    public synchronized void cancel(String message) {
        cancellations.add(message);
    }

    public synchronized void throwIfCancelled() {
        if (cancellations.size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("SecurityContext change cancelled:\n");
            for (String str : cancellations) {
                sb.append(str);
                sb.append("\n");
            }
            throw new SecurityViolation(sb.toString());
        }
    }
}
