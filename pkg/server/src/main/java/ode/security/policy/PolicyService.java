package ode.security.policy;

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

import java.util.Set;

import ode.conditions.SecurityViolation;
import ode.model.IObject;

/**
 * Internal service containing a number of configured {@link Policy} instances.
 * Each {@link Policy} is stored under a unique name, for which there may be
 * several other {@link Policy} instances. Consumers can either check whether
 * such a policy restriction is active via
 * {@link #isRestricted(String, IObject)} or let an exception be thrown by the
 * {@link Policy} itself via {@link #checkRestriction(String, IObject)}.
 * Further, the list of currently active restrictions can be provided in bulk to
 * clients via {@link #listActiveRestrictions(IObject)} so that restricted
 * operations need not be called only to have an exception thrown.
 */
public interface PolicyService {

    /**
     * Ask each configured {@link Policy} instance with the given name argument
     * if it considers the restriction active for the given {@link IObject}
     * argument. If any are active, return true.
     *
     * @param name
     *            non-null identifier of a class of {@link Policy} instances.
     * @param obj
     *            non-null "context" for this check.
     * @return true if any {@link Policy} returns true from
     *         {@link Policy#isRestricted(IObject)}.
     */
    boolean isRestricted(String name, IObject obj);

    /**
     * Give each configured {@link Policy} instance the chance to throw a
     * {@link SecurityViolation} from its
     * {@link Policy#checkRestriction(IObject)} method.
     *
     * @param name
     *            non-null identifier of a class of {@link Policy} instances.
     * @param obj
     *            non-null "context" for this check.
     */
    void checkRestriction(String name, IObject obj) throws SecurityViolation;

    /**
     * Return all configured identifier strings as would be passed as the first
     * argument to {@link #isRestricted(String, IObject)} or
     * {@link #checkRestriction(String, IObject)}.
     */
    Set<String> listAllRestrictions();

    /**
     * Return all identifier strings as would be passed as the first argument to
     * {@link #isRestricted(String, IObject)} or
     * {@link #checkRestriction(String, IObject)} <em>which</em> considers
     * itself active for the given argument.
     *
     * @param obj
     *            non-null context passed to each {@link Policy} instance.
     * @return a possibly empty string set of identifiers which should be
     *         returned to clients via
     *         {@link ode.model.internal.Permissions#copyExtendedRestrictions()}
     *         .
     */
    Set<String> listActiveRestrictions(IObject obj);

}