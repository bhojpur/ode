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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import ode.conditions.SecurityViolation;
import ode.model.IObject;

/**
 * Simple base class for {@link Policy} implementations which always returns
 * true for {@link #isRestricted(IObject)} and always fails on
 * {@link #checkRestriction(IObject)}.
 */
public abstract class BasePolicy implements Policy {

    final protected Set<Class<IObject>> types;

    public BasePolicy(Set<Class<IObject>> types) {
        if (types == null) {
            this.types = Collections.emptySet();
        } else {
            this.types = Collections.unmodifiableSet(
                    new HashSet<Class<IObject>>(types));
        }
    }
    public abstract String getName();

    @Override
    public Set<Class<IObject>> getTypes() {
        return types;
    }

    @Override
    public boolean isRestricted(IObject obj) {
        return true;
    }

    @Override
    public void checkRestriction(IObject obj) {
        throw new SecurityViolation(getName()+ ":: disallowed.");
    }

}