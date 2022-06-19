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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.BeanCreationException;

import ode.model.meta.Event;
import ode.security.EventProvider;
import ode.services.util.ReadOnlyStatus;

/**
 * An event provider that offers a unified view of multiple underlying event providers.
 * @param <P> event providers that adjust according to read-only status
 */
public class EventProviderWrapper<P extends EventProvider & ReadOnlyStatus.IsAware> implements EventProvider {

    private final List<P> write;

    /**
     * Construct a new event provider.
     * @param readOnly the read-only status
     * @param providers the event providers to wrap: the earlier providers are tried first and at least one provider must support
     * write operations according to {@link ode.services.util.ReadOnlyStatus.IsAware#isReadOnly(ReadOnlyStatus)}
     */
    public EventProviderWrapper(ReadOnlyStatus readOnly, List<P> providers) {
        write = new ArrayList<P>(providers.size());
        for (final P provider : providers) {
            if (!provider.isReadOnly(readOnly)) {
                write.add(provider);
            }
        }
        if (write.isEmpty()) {
            throw new BeanCreationException("must be given a read-write event provider");
        }
    }

    @Override
    public Event updateEvent(Event event) {
        return write.get(0).updateEvent(event);
    }
}