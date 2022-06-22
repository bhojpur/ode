package ode.api;

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

import java.util.List;
import java.util.Map;
import java.util.Set;

import ode.annotations.Hidden;
import ode.annotations.NotNull;
import ode.conditions.ApiUsageException;
import ode.conditions.RemovedSessionException;
import ode.conditions.SecurityViolation;
import ode.conditions.SessionTimeoutException;
import ode.model.meta.Session;
import ode.system.Principal;

/**
 * <em>Start here</em>: {@link Session} creation service for Bhojpur ODE. Access to
 * all other services is dependent upon a properly created and still active
 * {@link Session}.
 * 
 * The {@link Session session's} {@link Session#getUuid() uuid} can be
 * considered a capability token, or temporary single use password. Simply by
 * possessing it the client has access to all information available to the
 * {@link Session}.
 * 
 * Note: Both the RMI {@link ode.system.ServiceFactory} as well as the Ice
 * {@code ode.api.ServiceFactoryPrx} use {@link ISession} to acquire a
 * {@link Session}. In the RMI case, the {@link ISession} instance is the first
 * remote proxy accessed. In the Ice case, Glacier2 contacts {@link ISession}
 * itself and returns a ServiceFactory remote proxy. From both ServiceFactory
 * instances, it is possible but not necessary to access {@link ISession}.
 */
public interface ISession extends ServiceInterface {

    /**
     * Allows a user to open up another session for him/herself with the given
     * defaults without needing to re-enter password.
     */
    Session createUserSession(long timeToLiveMilliseconds,
            long timeToIdleMillisecond, String defaultGroup);

    /**
     * Allows an admin to create a {@link Session} for the give
     * {@link Principal}
     *  @param principal
     *            Non-null {@link Principal} with the target user's name
     * @param timeToLiveMilliseconds
     *            The time that this {@link Session} has until destruction. This
     *            is useful to override the server default so that an initial
     *            delay before the user is given the token will not be construed
     *            as idle time. A value less than 1 will cause the default max
     */
    Session createSessionWithTimeout(@NotNull Principal principal,
            long timeToLiveMilliseconds);

    /**
     * Allows an admin to create a {@link Session} for the give
     * {@link Principal}
     *  @param principal
     *            Non-null {@link Principal} with the target user's name
     * @param timeToLiveMilliseconds
     *            The time that this {@link Session} has until destruction.
     *            Setting the value to 0 will prevent destruction unless the
     *            session remains idle.
     * @param timeToIdleMilliseconds
 *            The time that this {@link Session} can remain idle before
 *            being destroyed. Setting the value to 0 will prevent idleness
     */
    Session createSessionWithTimeouts(@NotNull Principal principal,
            long timeToLiveMilliseconds, long timeToIdleMilliseconds);

    /**
     * Creates a new session and returns it to the user.
     * 
     * @throws ApiUsageException
     *             if principal is null
     * @throws SecurityViolation
     *             if the password check fails
     */
    Session createSession(@NotNull Principal principal,
            @Hidden String credentials);

    /**
     * Retrieves the session associated with this uuid, updating the last access
     * time as well. Throws a {@link RemovedSessionException} if not present, or
     * a {@link SessionTimeoutException} if expired.
     * 
     * This method can be used as a {@link Session} ping.
     */
    Session getSession(@NotNull String sessionUuid);

    /**
     * Retrieves the current reference count for the given uuid. Has the same
     * semantics as {@link #getSession(String)}.
     */
    int getReferenceCount(@NotNull String sessionUuid);

    /**
     * Closes session and releases all resources. It is preferred that all
     * clients call this method as soon as possible to free memory, but it is
     * possible to not call close, and rejoin a session later.
     * 
     * The current reference count for the session is returned. If the session
     * does not exist, -1. If this call caused the death of the session, then
     * -2.
     */
    int closeSession(@NotNull Session session);

    // Session listings

    /**
     * Returns a list of open sessions for the current user. The list is ordered
     * by session creation time, so that the last item was created last.
     */
    List<Session> getMyOpenSessions();

    /**
     * Like {@link #getMyOpenSessions()} but returns only those sessions
     * with the given agent string.
     */
    List<Session> getMyOpenAgentSessions(String agent);

    /**
     * Like {@link #getMyOpenSessions()} but returns only those sessions
     * started by official Bhojpur ODE clients.
     */
    List<Session> getMyOpenClientSessions();

    // void addNotification(String notification);
    // void removeNotification(String notification);
    // List<String> listNotifications();
    // void defaultNotifications();
    // void clearNotifications();

    // Session joinSessionByName(@NotNull String sessionName); // Here you don't
    // have a
    // void disconnectSession(@NotNull Session session);
    // void pingSession(@NotNull Session session); // Add to ServiceFactoryI

    // Environment contents
    // =========================================================================

    /**
     * Retrieves an entry from the given {@link Session session's} input
     * environment.
     */
    Object getInput(String session, String key);

    /**
     * Retrieves all keys in the {@link Session session's} input environment.
     * 
     * @param session
     * @return a {@link Set} of keys
     */
    Set<String> getInputKeys(String session);

    /**
     * Retrieves all inputs from the given {@link Session session's} input
     * environment.
     */
    Map<String, Object> getInputs(String session);

    /**
     * Places an entry in the given {@link Session session's} input environment.
     * If the value is null, the key will be removed.
     */
    void setInput(String session, String key, Object objection);

    /**
     * Retrieves all keys in the {@link Session sesson's} output environment.
     */
    Set<String> getOutputKeys(String session);

    /**
     * Retrieves an entry from the {@link Session session's} output environment.
     */
    Object getOutput(String session, String key);

    /**
     * Retrieves all outputs from the given {@link Session session's} input
     * environment.
     */
    Map<String, Object> getOutputs(String session);

    /**
     * Places an entry in the given {@link Session session's} output
     * environment. If the value is null, the key will be removed.
     */
    void setOutput(String session, String key, Object objection);
}