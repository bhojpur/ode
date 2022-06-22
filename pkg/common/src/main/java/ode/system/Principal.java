package ode.system;

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

import java.io.Serializable;

import ode.model.enums.EventType;
import ode.model.internal.Permissions;
import ode.model.meta.ExperimenterGroup;

/**
 * implementation of {@link java.security.Principal}. Specialized for Bhojpur ODE to
 * carry a {@link ExperimenterGroup group}, an {@link EventType event type} and
 * a {@link Permissions umask}.
 *
 * @see EventType
 * @see ExperimenterGroup
 * @see Permissions
 */
public class Principal implements java.security.Principal, Serializable {

    private static final long serialVersionUID = 3761954018296933086L;

    protected String name;

    protected String group;

    protected String type;

    private final PreferenceContext preferences = new PreferenceContext();

    /**
     * Creates a Principal with null group and event type. These must be taken
     * from the session.
     *
     * @param name
     */
    public Principal(String name) {
        this(name, null, null);
    }

    public Principal(String name, String group, String eventType) {
        if (Boolean.parseBoolean(preferences
                .getProperty("ode.security.ignore_case"))) {
            name = name.toLowerCase();
        }
        this.name = name;
        this.group = group;
        this.type = eventType;
    }

    // IMMUTABLE

    public String getName() {
        return this.name;
    }

    public String getGroup() {
        return this.group;
    }

    public String getEventType() {
        return this.type;
    }

    /**
     * returns only the name of the instance because that is the expected
     * behavior of {@link java.security.Principal} implementations
     *
     * @return value of {@link #name}
     */
    @Override
    public String toString() {
        return this.name;
    }

}