package ode.model.internal;

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

import ode.conditions.SecurityViolation;
import ode.model.IObject;

/**
 * holds information regarding the graph to which an {@link IObject}
 * belongs.
 * 
 * {@link GraphHolder#hasToken()}, {@link GraphHolder#tokenMatches(Token)},
 * and {@link GraphHolder#setToken(Token, Token)} are final so that subclasses
 * cannot intercept tokens.
 */
public final class GraphHolder {

    private IObject replacement;

    /**
     * a replacement is a <em>managed</em> entity instance which has the same
     * primary key as this instance. Storing this value here allows for several
     * optimizations.
     * 
     * @return entity
     */
    public IObject getReplacement() {
        return replacement;
    }

    /**
     * used mostly by {@code ode.api.IUpdate}. Improper use of this method may
     * cause erratic behavior.
     * 
     * @param replacement
     */

    public void setReplacement(IObject replacement) {
        this.replacement = replacement;
    }

    private Token token;

    /**
     * tests if this {@link GraphHolder} contains a {@link Token} reference.
     */
    public final boolean hasToken() {
        return this.token != null;
    }

    /**
     * check the {@link Token} for the {@link IObject} represented by this
     * {@link GraphHolder}. This can be seen to approximate "ownership" of this
     * Object within the JVM.
     * 
     * @return true only if the two instances are identical.
     */
    public final boolean tokenMatches(Token token) {
        return this.token == token;
    }

    /**
     * set the {@link Token} for this {@link GraphHolder} but only if you posses
     * the current {@link Token}. The first call to
     * {@link #setToken(Token, Token)} will succeed when {@link #token} is null.
     * 
     * @param previousToken
     * @param newToken
     */
    public final void setToken(Token previousToken, Token newToken) {
        if (token == null || previousToken == token) {
            this.token = newToken;
        }

        else {
            throw new SecurityViolation("Tokens do not match.");
        }
    }
}