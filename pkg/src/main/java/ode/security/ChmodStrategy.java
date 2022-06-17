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

import ode.model.IObject;

/**
 * Strategy for changing the permissions of objects in the database
 * as well as verifying that the permissions for the modified objects
 * are sensible after the change.
 *
 * This interface is designed to be used in an asynchronous situation
 * where as many individual steps as possible are performed rather
 * than performing everything in one go.
 */
public interface ChmodStrategy {

    /**
     * Return all the checks necessary to validate the
     * given object if it were to have its permissions.
     * The Object return value should be assumed opaque,
     * and is primarily intended for passing it back to
     * {@link #check(IObject, Object)}.
     */
    Object[] getChecks(IObject obj, String permissions);

    /**
     * Change the permissions for the given object.
     * This may do nothing if the permissions do not
     * differ from the current settings. In any case,
     * this method is intended to return quickly.
     * Once the change takes place, it will be necessary
     * to run {@link #check(IObject, Object)} to
     * guarantee that no invalid links are present.
     */
    void chmod(IObject obj, String permissions);

    /**
     * Performs one of the checks returned by
     * {@link #getChecks(IObject obj, String permissions)}.
     * These will typically be queries to be performed
     * across all tables.
     */
    void check(IObject obj, Object check);

}