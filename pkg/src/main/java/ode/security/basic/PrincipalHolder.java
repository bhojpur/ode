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

import ode.security.SecuritySystem;
import ode.system.Principal;

/**
 * Stack of active {@link Principal} instances. As a user logs in, an empty
 * context is created which must later be primed by the {@link SecuritySystem}
 * in order to be operational.
 * 
 * @see BasicSecuritySystem
 */
public interface PrincipalHolder {

    /**
     * Get the number of active principal contexts.
     * @return the number of active principals
     */
    public int size();

    /**
     * Get the last, i.e. currently active, principal.
     * @return the current principal
     */
    public Principal getLast();

    /**
     * Add a new principal context to the stack.
     * @param principal the principal to add
     */
    public void login(Principal principal);

    /**
     * Allow logging in directly with an event context.
     * @param bec the event context to use
     */
    public void login(BasicEventContext bec);

    /**
     * Pop the last created principal context and return the number of active
     * contexts remaining.
     * @return the number of active principals after the logout
     */
    public int logout();
}