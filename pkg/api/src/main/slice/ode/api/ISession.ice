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

#ifndef ODE_API_ISESSION_ICE
#define ODE_API_ISESSION_ICE

#include <ode/ModelF.ice>
#include <ode/ServicesF.ice>
#include <ode/System.ice>
#include <ode/Collections.ice>

module ode {

    module api {

        /**
         * {@link ode.model.Session} creation service for Bhojpur ODE. Access to
         * all other services is dependent upon a properly created and still
         * active {@link ode.model.Session}.
         *
         * The session uuid ({@code ode.model.Session.getUuid}) can be
         * considered a capability token, or temporary single use password.
         * Simply by possessing it the client has access to all information
         * available to the {@link ode.model.Session}.
         *
         * Note: Both the RMI <code>ode.system.ServiceFactory</code> as well
         * as the Ice {@link ode.api.ServiceFactory} use
         * {@link ode.api.ISession} to acquire a
         * {@link ode.model.Session}. In the Ice case, Glacier2
         * contacts {@link ode.api.ISession} itself and returns a
         * ServiceFactory remote proxy. From both ServiceFactory
         * instances, it is possible but not necessary to access
         * {@link ode.api.ISession}.
         */
        ["ami", "amd"] interface ISession extends ServiceInterface
            {
                /**
                 * Creates a new session and returns it to the user.
                 *
                 * @throws ApiUsageException if principal is null
                 * @throws SecurityViolation if the password check fails
                 */
                ode::model::Session createSession(ode::sys::Principal p, string credentials)
                throws ServerError, Glacier2::CannotCreateSessionException;

                /**
                 * Allows a user to open up another session for him/herself with the given
                 * defaults without needing to re-enter password.
                 */
                ode::model::Session createUserSession(long timeToLiveMilliseconds, long timeToIdleMilliseconds, string defaultGroup)
                throws ServerError, Glacier2::CannotCreateSessionException;

                //
                // System users
                //

                /**
                 * Allows an admin to create a {@link ode.model.meta.Session} for the give
                 * {@link ode.sys.Principal}.
                 *
                 * @param principal
                 *            Non-null {@link ode.sys.Principal} with the
                 *            target user's name
                 * @param timeToLiveMilliseconds
                 *            The time that this {@link ode.model.meta.Session}
                 *            has until destruction. This is useful to
                 *            override the server default so that an initial
                 *            delay before the user is given the token will
                 *            not be construed as idle time. A value less than
                 *            1 will cause the default max timeToLive to be
                 *            used; but timeToIdle will be disabled.
                 */
                ode::model::Session createSessionWithTimeout(ode::sys::Principal principal, long timeToLiveMilliseconds)
                throws ServerError, Glacier2::CannotCreateSessionException;

                /**
                 * Allows an admin to create a {@link ode.model.meta.Session} for
                 * the given {@link ode.sys.Principal}.
                 *
                 * @param principal
                 *            Non-null {@link ode.sys.Principal} with the
                 *            target user's name
                 * @param timeToLiveMilliseconds
                 *            The time that this {@link ode.model.meta.Session}
                 *            has until destruction. Setting the value to 0
                 *            will prevent destruction unless the session
                 *            remains idle.
                 * @param timeToIdleMilliseconds
                 *            The time that this {@link ode.model.meta.Session}
                 *            can remain idle before being destroyed. Setting
                 *            the value to 0 will prevent idleness based
                 *            destruction.
                 */
                ode::model::Session createSessionWithTimeouts(ode::sys::Principal principal, long timeToLiveMilliseconds, long timeToIdleMilliseconds)
                throws ServerError, Glacier2::CannotCreateSessionException;

                /**
                 * Retrieves the session associated with this uuid, updating
                 * the last access time as well. Throws a
                 * {@link ode.RemovedSessionException} if not present, or
                 * a {@link ode.SessionTimeoutException} if expired.
                 *
                 * This method can be used as a {@link ode.model.meta.Session} ping.
                 */
                idempotent ode::model::Session getSession(string sessionUuid) throws ServerError;

                /**
                 * Retrieves the current reference count for the given uuid.
                 * Has the same semantics as {@code getSession}.
                 */
                idempotent int getReferenceCount(string sessionUuid) throws ServerError;

                /**
                 * Closes session and releases all resources. It is preferred
                 * that all clients call this method as soon as possible to
                 * free memory, but it is possible to not call close, and
                 * rejoin a session later.
                 *
                 * The current reference count for the session is returned. If
                 * the session does not exist, -1. If this call caused the
                 * death of the session, then -2.
                 */
                int closeSession(ode::model::Session sess) throws ServerError;

                // Listing
                /**
                 * Returns a list of open sessions for the current user. The
                 * list is ordered by session creation time, so that the last
                 * item was created last.
                 */
                idempotent SessionList getMyOpenSessions() throws ServerError;

                /**
                 * Like {@code getMyOpenSessions} but returns only those
                 * sessions with the given agent string.
                 */
                idempotent SessionList getMyOpenAgentSessions(string agent) throws ServerError;

                /**
                 * Like {@code getMyOpenSessions} but returns only those
                 * sessions started by official ODE clients.
                 */
                idempotent SessionList getMyOpenClientSessions() throws ServerError;

                // Environment
                /**
                 * Retrieves an entry from the given
                 * {@link ode.model.Session} input environment.
                 */
                idempotent ode::RType getInput(string sess, string key) throws ServerError;

                /**
                 * Retrieves an entry from the {@link ode.model.Session}
                 * output environment.
                 */
                idempotent ode::RType getOutput(string sess, string key) throws ServerError;

                /**
                 * Places an entry in the given {@link ode.model.Session}
                 * input environment.
                 * If the value is null, the key will be removed.
                 */
                idempotent void setInput(string sess, string key, ode::RType value) throws ServerError;

                /**
                 * Places an entry in the given {@link ode.model.Session}
                 * output environment. If the value is null, the key will be
                 * removed.
                 */
                idempotent void setOutput(string sess, string key, ode::RType value) throws ServerError;

                /**
                 * Retrieves all keys in the {@link ode.model.Session} input
                 * environment.
                 *
                 * @return a {@link java.util.Set} of keys
                 */
                idempotent StringSet getInputKeys(string sess) throws ServerError;

                /**
                 * Retrieves all keys in the {@link ode.model.Session}
                 * output environment.
                 */
                idempotent StringSet getOutputKeys(string sess) throws ServerError;

                /**
                 * Retrieves all inputs from the given
                 * {@link ode.model.Session} input environment.
                 */
                idempotent ode::RTypeDict getInputs(string sess) throws ServerError;

                /**
                 * Retrieves all outputs from the given
                 * {@link ode.model.Session} input environment.
                 */
                idempotent ode::RTypeDict getOutputs(string sess) throws ServerError;
            };

    };
};

#endif