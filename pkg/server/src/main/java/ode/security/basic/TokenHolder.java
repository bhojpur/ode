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

import ode.model.IObject;
import ode.model.internal.GraphHolder;
import ode.model.internal.Token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages a special token (a unique object) which can be inserted into
 * {@link IObject} instances for special almost-administrative handling.
 * 
 * Identifies loose "ownership" of certain objects.
 * 
 * @see IObject#getGraphHolder()
 * @see GraphHolder#hasToken()
 */
public class TokenHolder {

    private static Logger log = LoggerFactory.getLogger(TokenHolder.class);

    private final Token token = new Token();

    public void setToken(GraphHolder gh) {
        gh.setToken(token, token);
    }

    public void clearToken(GraphHolder gh) {
        gh.setToken(token, null);
    }

    public boolean hasPrivilegedToken(IObject obj) {

        if (obj == null) {
            return false;
        }

        GraphHolder gh = obj.getGraphHolder();

        // most objects will not have a token
        if (gh.hasToken()) {
            // check if truly secure.
            if (gh.tokenMatches(token)) {
                return true;
            }
        }
        return false;
    }

    /**
     * copy a token from one {@link IObject} to another. This is currently
     * insecure and should take a third token implying the rights to copy.
     * Should only be called by {@link MergeEventListener}
     */
    public void copyToken(IObject source, IObject copy) {
        if (source == null || copy == null || source == copy) {
            return;
        }

        GraphHolder gh1 = source.getGraphHolder();
        GraphHolder gh2 = copy.getGraphHolder();

        // try our token first
        if (gh1.tokenMatches(token)) {
            gh2.setToken(token, token);
        }

    }
}