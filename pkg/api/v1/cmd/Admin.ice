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

#ifndef ODE_CMD_ADMIN_ICE
#define ODE_CMD_ADMIN_ICE

#include <ode/Collections.ice>
#include <ode/System.ice>
#include <ode/cmd/API.ice>

/*
 * Callback interface allowing to reset password for the given user.
 */

module ode {

    module cmd {

         /**
          * Requests a reset password for the given user.
          * The user must not be an administrator.
          *
          * examples:
          *  - ode.cmd.ResetPasswordRequest(odename, email)
          *      sends new password to the given user
          */
         class ResetPasswordRequest extends Request {
             string odename;
             string email;
         };

         /**
          * Successful response for {@link ResetPasswordRequest}.
          * If no valid user with matching email is found,
          * an {@link ERR} will be returned.
          */
         class ResetPasswordResponse extends OK {
         };

        /**
         * Proposes a change to one or both of the timeToLive
         * and timeToIdle properties of a live session. The session
         * uuid cannot be null. If either other argument is null,
         * it will be ignored. Otherwise, the long value will be
         * interpreted as the the millisecond value which should
         * be set. Non-administrators will not be able to reduce
         * current values. No special response is returned, but
         * an {@link ode.cmd.OK} counts as success.
         **/
        class UpdateSessionTimeoutRequest extends Request {
            string session;
            ode::RLong timeToLive;
            ode::RLong timeToIdle;
        };

        /**
         * Argument-less request that will produce a
         * {@link CurrentSessionsResponse} if no {@link ode.cmd.ERR} occurs.
         */
        class CurrentSessionsRequest extends Request {
        };

        /**
         * Return value from {@link ode.cmd.CurrentSessionsRequest}
         * consisting of two ordered lists of matching length. The sessions
         * field contains a list of the ODE {@link ode.model.Session}
         * objects that are currently active *after* all timeouts have been
         * applied.
         * This is the value that would be returned by
         * {@code ode.api.ISession.getSession} when joined to that session.
         * Similarly, the contexts field contains the value that would be
         * returned by a call to {@code ode.api.IAdmin.getEventContext}.
         * For non-administrators, most values for all sessions other than
         * those belonging to that user will be null.
         */
        class CurrentSessionsResponse extends OK {
            /**
             * {@link ode.model.Session} objects loaded from
             * the database.
             **/
            ode::api::SessionList sessions;

            /**
             * {@link ode.sys.EventContext} objects stored in
             * memory by the server.
             **/
            ode::api::EventContextList contexts;

            /**
             * Other session state which may vary based on
             * usage. This may include "hitCount", "lastAccess",
             * and similar metrics.
             **/
            ode::api::RTypeDictArray data;
        };

    };
};

#endif