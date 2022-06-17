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

import java.util.Collections;

import ode.model.meta.Experimenter;
import ode.services.messages.EventLogMessage;

/**
 * Central {@link PasswordProvider} which uses the "password" table in the
 * central Bhojpur ODE database.
 */

public class JdbcPasswordProvider extends ConfigurablePasswordProvider {

    public JdbcPasswordProvider(PasswordUtil util) {
        super(util);
    }

    public JdbcPasswordProvider(PasswordUtil util, boolean ignoreUnknown) {
        super(util, ignoreUnknown);
    }

    public JdbcPasswordProvider(PasswordUtil util, boolean ignoreUnknown,
            boolean salt) {
        super(util, ignoreUnknown, salt);
    }

    @Override
    public boolean hasPassword(String user) {
        Long id = util.userId(user);
        return id != null;
    }

    /**
     * Retrieves password from the database and calls
     * {@link ConfigurablePasswordProvider#comparePasswords(String, String)}.
     * Uses default logic if user is unknown.
     */
    @Override
    public Boolean checkPassword(String user, String password, boolean readOnly) {
        Long id = util.userId(user);

        // If user doesn't exist, use the default settings for
        // #ignoreUnknown.

        Boolean b = null;
        if (id == null) {
            b = super.checkPassword(user, password, readOnly);
        } else {
            String trusted = util.getUserPasswordHash(id);
            b = comparePasswords(id, trusted, password);
        }
        loginAttempt(user, b);
        return b;
    }

    @Override
    public void changePassword(String user, String password)
            throws PasswordChangeException {
        changePassword(user, password, salt ? PasswordUtil.METHOD.ALL : PasswordUtil.METHOD.LEGACY);
    }

    /**
     * Actually perform the password change in the database and log the event against the user.
     * @param user the name of the user whose password is to be changed
     * @param password the password to prepare for storing in the table
     * @param method how the given password is to be stored
     * @throws PasswordChangeException if the operation failed
     */
    protected void changePassword(String user, String password, PasswordUtil.METHOD method) throws PasswordChangeException {
        final Long id = util.userId(user);
        if (id == null) {
            throw new PasswordChangeException("Couldn't find id: " + user);
        }
        util.changeUserPasswordById(id, password, method);
        final EventLogMessage event =
                new EventLogMessage(this, "PASSWORD", Experimenter.class, Collections.singletonList(id));
        ctx.publishEvent(event);
    }
}