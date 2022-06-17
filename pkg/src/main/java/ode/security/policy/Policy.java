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
 * Strategy to permit the flexible restriction of certain actions
 * throughout Bhojpur ODE. Code which intends to allow such checks should
 * create a new {@link Policy} class with a unique {@link #getName() name}
 * and add checks within that code. For example:
 *
 * <pre>
 *    public class MyPolicy implements Policy {
 *
 *        public final static string NAME = "MyPolicy";
 *
 *        public String getName() {
 *            return NAME;
 *        }
 *    }
 *
 *    public void someImportantMethod() {
 *        IObject objBeingAccessed = ...;
 *        policyService.checkRestriction(MyPolicy.NAME, objBeingAccessed);
 *        // Here an exception may be thrown
 *    }
 * </pre>
 */
public interface Policy {

    /**
     * Unique name for a class of restrictions that this {@link Policy}
     * will enforce. This string will be sent to clients via
     * {@link ode.model.internal.Permissions#copyExtendedRestrictions()} in
     * order to prevent exceptions, and server-code will pass the same name
     * to the check method to potentially have an exception thrown.
     */
    String getName();

    /**
     * Each {@link Policy} should tell the {@link PolicyService} which types
     * of {@link IObject} instances it cares about. Only those which are of
     * interest to <em>some</em> {@link Policy} need be considered.
     */
    Set<Class<IObject>> getTypes();

    /**
     * Checks whether or not this instance would throw a
     * {@link SecurityViolation} if the same instance were passed to
     * {@link #checkRestriction(IObject)}. This is likely determined by first
     * testing the type of the {@link IObject} and then that the
     * current user context has access to the given context.
     *
     * @param obj
     *            a non-null {@link IObject} instance.
     *
     * @return true if this {@link Policy} decides that a restriction should be
     *         placed on the passed context.
     */
    boolean isRestricted(IObject obj);

    /**
     * Like {@link #isRestricted(IObject)} but throws an appropriate
     * {@link SecurityViolation} subclass if the restriction is active.
     */
    void checkRestriction(IObject obj) throws SecurityViolation;

}