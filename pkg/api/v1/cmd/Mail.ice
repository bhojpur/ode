/*
 * Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

#ifndef ODE_CMD_MAIL_ICE
#define ODE_CMD_MAIL_ICE

#include <ode/Collections.ice>
#include <ode/System.ice>
#include <ode/cmd/API.ice>

/*
 * Callback interface allowing to send email using JavaMailSender, supporting
 * MIME messages through preparation callbacks.
 */

module ode {

    module cmd {

         /**
         * Requests an email to be send to all users of the Bhojpur ODE
         * determines inactive users, an active members of given groups
         * and/or specific users.
         *
         * examples:
         *  - ode.cmd.SendEmailRequest(subject, body, everyone=True)
         *      sends message to everyone who has email set
         *      and is an active user
         *  - ode.cmd.SendEmailRequest(subject, body, everyone=True, inactive=True)
         *      sends message to everyone who has email set,
         *      even inactive users
         *  - ode.cmd.SendEmailRequest(subject, body, groupIds=\[...],
         *      userIds=\[...] )
         *      sends email to active members of given groups and selected users
         *  - extra=\[...] allows to set extra email address if not in DB
         */
         class SendEmailRequest extends Request {
             string subject;
             string body;
             bool html;
             ode::sys::LongList userIds;
             ode::sys::LongList groupIds;
             ode::api::StringSet extra;
             bool inactive;
             bool everyone;
         };

         /**
         * Successful response for {@code SendEmailRequest}. Contains
         * a list of invalid users that has no email address set.
         * If no recipients or invalid users found, an {@code ERR} will be
         * returned.
         *
         * - invalidusers is a list of userIds that email didn't pass criteria
         *                  such as was empty or less then 5 characters
         * - invalidemails is a list of email addresses that send email failed
         * - total is a total number of email in the pull to be sent.
         * - success is a number of emails that were sent successfully.
         */
         class SendEmailResponse extends Response {
             long total;
             long success;
             ode::api::LongList invalidusers;
             ode::api::StringSet invalidemails;
         };

    };
};

#endif