package ode.services.mail;

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

import static org.apache.commons.collections.CollectionUtils.isEmpty;

import java.util.Collections;
import java.util.Set;

import ode.services.messages.LoginAttemptMessage;

import org.springframework.context.ApplicationListener;

/**
 * On an {@link LoginAttemptMessage} potentially send an email to administrators
 * or the user herself to alert of possible security issues.
 *
 * @since 5.1.0
 */
public class FailedLoginMailSender extends MailSender implements
        ApplicationListener<LoginAttemptMessage> {

    private boolean contactUser;

    private boolean contactSystem;

    private boolean onAllUsers;

    private Set<String> onSpecificUsers;

    public void setContactUser(boolean contactUser) {
        this.contactUser = contactUser;
    }

    public void setContactSystem(boolean contactSystem) {
        this.contactSystem = contactSystem;
    }

    public void setOnAllUsers(boolean onAllUsers) {
        this.onAllUsers = onAllUsers;
    }

    public void setOnSpecificUsers(Set<String> onSpecificUsers) {
        this.onSpecificUsers = onSpecificUsers;
    }

    //
    // Main method
    //

    @Override
    public void onApplicationEvent(LoginAttemptMessage lam) {

        if (!isEnabled()) {
            return;
        }

        if (lam.success == null || lam.success) {
            return;
        }

        final String subj = String.format("Login failed for '%s'", lam.user);
        final boolean hasSpecific = (!isEmpty(onSpecificUsers) &&
                onSpecificUsers.contains(lam.user));

        if (onAllUsers || hasSpecific) {
            if (contactSystem) {
                sendBlind(getAllSystemUsers(false), subj);
            }
            if (contactUser) {
                sendBlind(Collections.singleton(getUserEmail(lam.user)),
                        subj);
            }
        }
    }

}
