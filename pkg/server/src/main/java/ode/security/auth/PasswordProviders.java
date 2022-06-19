package ode.security.auth;

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

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.util.Assert;

/**
 * Composite class which delegates to each of the configured providers in turn.
 * The first instance which is responible for a user name wins.
 */
public class PasswordProviders implements PasswordProvider {

    final private PasswordProvider[] providers;

    private AtomicBoolean ignoreCaseLookup;

    public PasswordProviders(PasswordProvider... providers) {
        this(new AtomicBoolean(false), providers);
    }

    public PasswordProviders(AtomicBoolean ignoreCaseLookup,
            PasswordProvider... providers) {
        Assert.notNull(providers);
        int l = providers.length;
        this.providers = new PasswordProvider[l];
        System.arraycopy(providers, 0, this.providers, 0, l);
        this.ignoreCaseLookup = ignoreCaseLookup;
    }

    public boolean hasPassword(String user) {
        user = ignoreCaseLookup.get() ? user.toLowerCase() : user;
        for (PasswordProvider provider : providers) {
            boolean hasPassword = provider.hasPassword(user);
            if (hasPassword) {
                return true;
            }
        }
        return false;
    }

    public Boolean checkPassword(String user, String password, boolean readOnly) {
        user = ignoreCaseLookup.get() ? user.toLowerCase() : user;
        for (PasswordProvider provider : providers) {
            Boolean rv = provider.checkPassword(user, password, readOnly);
            if (rv != null) {
                return rv;
            }
        }
        return null;
    }

    public void changePassword(String user, String password)
            throws PasswordChangeException {
        user = ignoreCaseLookup.get() ? user.toLowerCase() : user;
        for (PasswordProvider provider : providers) {
            boolean hasPassword = provider.hasPassword(user);
            if (hasPassword) {
                provider.changePassword(user, password);
                return;
            }
        }
        throw new PasswordChangeException("No provider found: " + user);
    }

    public void changeDistinguisedName(String user, String dn)
            throws PasswordChangeException {

        for (PasswordProvider provider : providers) {
            boolean hasPassword = provider.hasPassword(user);
            if (hasPassword) {
                provider.changePassword(user, dn);
                return;
            }
        }
        throw new PasswordChangeException("No provider found:" + user);
    }
}