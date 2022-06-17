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

import java.util.List;

import ode.model.IObject;
import ode.util.messages.InternalMessage;

/**
 * Published only when an event log should be saved at the end of a transaction.
 * Most {@link ode.model.meta.EventLog} instances are created directly in the database and so
 * a listener cannot expect to know the full state of the system just from these.
 */
public class EventLogMessage extends InternalMessage {

    private static final long serialVersionUID = 7132548299119420025L;

    public final String action;

    public final Class<? extends IObject> entityType;

    public final List<Long> entityIds;

    public EventLogMessage(Object source, String action, Class<? extends IObject> entityType,
            List<Long> entityIds) {
        super(source);
        this.action = action;
        this.entityType = entityType;
        this.entityIds = entityIds;
    }

}