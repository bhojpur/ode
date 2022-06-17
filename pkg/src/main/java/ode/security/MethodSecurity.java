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

import java.lang.reflect.Method;

import ode.annotations.RolesAllowed;
import ode.conditions.SecurityViolation;
import ode.system.Principal;

/**
 * Interface which allows security interceptors to check if a method should be
 * executable for a given user. This determination is most likely based on
 * {@link RolesAllowed} annotations and replaces the security provided by an
 * application server.
 */
public interface MethodSecurity {

    /**
     * Indicates whether or not method security is active. If not, then no
     * further checks should be made, and implementations are free to throw
     * exceptions if they are not properly initialized. Clients of this
     * interface can assume that method-level security has been configured
     * elsewhere.
     *
     * @return true if the other methods of this interface can and should be
     *         called.
     */
    boolean isActive();

    /**
     * Throws a {@link SecurityViolation} exception if the given
     * {@link Principal} does not have the proper permissions to execute the
     * given method. If {@link #isActive()} returns false, this method may also
     * throw any {@link RuntimeException} to specify that it is not in an active
     * state.
     *
     * @param obj     {@link Object} on which this method will be called.
     * @param method  {@link Method} to be called.
     * @param principal {@link Principal} for which permissions will be checked.
     * @param hasPassword flag if the user's session has been authenticated directly
     *  and not via a one-time session id or similar.
     * @throws SecurityViolation if the given pr
     */
    void checkMethod(Object obj, Method method, Principal principal, boolean hasPassword)
            throws SecurityViolation;

}