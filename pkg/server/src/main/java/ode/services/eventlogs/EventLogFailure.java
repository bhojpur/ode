package ode.services.eventlogs;

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

import ode.model.meta.EventLog;
import ode.util.messages.InternalMessage;

/**
 * Signifies that the processing of a {@link EventLog} has failed.
 * Previously, this was being handled by a try/catch
 * block within the processor (e.g. FullTextIndexer)
 * but in order to allow each {@link EventLogLoader} to handle the error
 * differently, this message was created.
 */
public class EventLogFailure extends InternalMessage {

    private static final long serialVersionUID = -6829538576297712696L;

    public final EventLogLoader loader;

    public final EventLog log;

    public final Throwable throwable;

    public EventLogFailure(EventLogLoader loader, EventLog log, Throwable t) {
        super(loader);
        this.loader = loader;
        this.log = log;
        this.throwable = t;
    }

    public boolean wasSource(EventLogLoader loader) {
        return loader == this.loader;
    }
}
