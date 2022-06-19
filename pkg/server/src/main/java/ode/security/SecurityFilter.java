package ode.security;

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

import org.hibernate.Session;

import ode.model.internal.Details;
import ode.model.internal.Permissions;
import ode.model.internal.Permissions.Right;
import ode.security.basic.OdeInterceptor;
import ode.system.EventContext;

/**
 * Base filter interface ...
 */
public interface SecurityFilter {

    static public final String is_share = "is_share";

    static public final String is_adminorpi = "is_adminorpi";

    static public final String is_nonprivate = "is_nonprivate";

    static public final String current_user = "current_user";

    /**
     * Name of this security filter. By default this will likely return
     * the simple class name for the instance. This value will be used
     * to activate the filter on the Hibernate session.
     */
    public String getName();

    /**
     * Return a mapping of the hibernate types for each of the parameters
     * that the condition takes.
     */
    public Map<String, String> getParameterTypes();

    /**
     * Return the string to be used as the condition.
     */
    public String getDefaultCondition();

    /**
     * tests that the {@link Details} argument passes the security test that
     * this filter defines. The two must be kept in sync. This will be used
     * mostly by the
     * {@link OdeInterceptor#onLoad(Object, java.io.Serializable, Object[], String[], org.hibernate.type.Type[])}
     * method.
     *
     * @param d
     *            Details instance. If null (or if its {@link Permissions} are
     *            null all {@link Right rights} will be assumed.
     * @return true if the object to which this
     */
    public boolean passesFilter(Session session, Details d, EventContext c);

    /**
     * Enables this filter with the settings from this filter. The intent is
     * that after this call, no Hibernate queries will return any objects that
     * would fail a call to
     * {@link #passesFilter(Session, Details, EventContext)}.
     *
     * @param sess Non-null.
     * @param ec Non-null.
     */
    public void enable(Session sess, EventContext ec);

    /**
     * Reverts the call to {@link #enable(Session, EventContext)}.
     */
    public void disable(Session sess);

}