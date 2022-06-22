package ode.api;

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

import ode.model.meta.Event;
import ode.system.EventContext;

/**
 * Bhojpur ODE API Interface with stateful semantics.
 *
 * As of 1.0, each stateful service is responsible for providing its
 * own passivation/activation logic in the similarly named methods.
 */
public interface StatefulServiceInterface extends ServiceInterface {

    
    /**
     * Perform whatever passivation is possible or throw an exception.
     * A good passivation method will free up as much memory as
     * possible, most likely by storing it to disk. A call to
     * passivate should be safe even if the service is already
     * passivated.
     */
    void passivate();
    
    /**
     * Completely restore this service for active use from whatever
     * passivation it has implemented. A call to activate should be
     * safe even if the service is already activated.
     */
    void activate();

    /**
     * signals the end of the service lifecycle. Resources such as Sessions can
     * be released. All further calls will throw an exception.
     */
    void close();

    /**
     * Returns the current {@link EventContext} for this instance. This is
     * useful for later identifying changes made by this {@link Event}.
     */
    EventContext getCurrentEventContext();
}