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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ode.model.meta.EventLog;
import ode.util.messages.InternalMessage;

import com.google.common.collect.Multimap;

/**
 * Published with the final collection of {@link EventLog} instances which
 * <em>will</em> be saved.
 */
public class EventLogsMessage extends InternalMessage {

    private static final long serialVersionUID = 7132548299119420025L;

    final Multimap<String, EventLog> logs;

    public EventLogsMessage(Object source, Multimap<String, EventLog> logs) {
        super(source);
        this.logs = logs;
    }

    public Collection<EventLog> matches(String klass, String action) {
        List<EventLog> rv = new ArrayList<EventLog>();
        for (EventLog el : logs.get(klass)) {
            if (el.getAction().equals(action)) {
                rv.add(el);
            }
        }
        return rv;
    }
}
