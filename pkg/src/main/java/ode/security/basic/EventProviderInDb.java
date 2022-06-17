package ode.security.basic;

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

import ode.api.IUpdate;
import ode.model.meta.Event;
import ode.security.EventProvider;
import ode.services.util.ReadOnlyStatus;
import ode.system.ServiceFactory;

/**
 * Provider for {@link Event} objects which is responsible for persisting and
 * populating such entities using Hibernate in accordance with the currently
 * available {@link IUpdate} implementation.
 */
public class EventProviderInDb
    implements EventProvider, ReadOnlyStatus.IsAware {

    private ServiceFactory sf;

    /**
     * Main public constructor for this {@link EventProvider} implementation.
     * @param sf the service factory
     */
    public EventProviderInDb(ServiceFactory sf) {
        this.sf = sf;
    }

    /**
     * Persists a given {@link Event}.
     * @param event the event to persist
     * @return updated event
     */
    public Event updateEvent(Event event) {
        IUpdate update = sf.getUpdateService();
        return update.saveAndReturnObject(event);
    }

    @Override
    public boolean isReadOnly(ReadOnlyStatus readOnly) {
        return readOnly.isReadOnlyDb();
    }
}