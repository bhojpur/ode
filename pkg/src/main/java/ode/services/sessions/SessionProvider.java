package ode.services.sessions;

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

import ode.model.meta.Session;
import ode.system.ServiceFactory;

/**
 * Provides {@link SessionManagerImpl} with wrapper around session storage backends.
 */
public interface SessionProvider {

    Session executeUpdate(ServiceFactory sf, Session session, String uuid, long userId, Long sudoerId);

    /**
     * Loads a session directly, sets its "closed" value and immediately
     * saves it. This method is not called
     * directly from the {@link SessionManager#close(String)} and {@link SessionManager#closeAll()} methods
     * since there are other non-explicit ways for a session to be destroyed, such
     * as a timeout within {@link ode.services.sessions.state.SessionCache} and so this is called from
     * {@link SessionManagerImpl#onApplicationEvent(org.springframework.context.ApplicationEvent)} when a
     * {@link ode.services.messages.DestroySessionMessage} is received.
     */
    void executeCloseSession(String uuid);

    Session executeInternalSession(String uuid, Session session);

    /**
     * Added as an attempt to cure
     */
    long executeNextSessionId();

    /**
     * Retrieves a session by ID.
     * @param id session ID to lookup
     * @param session active Hibernate session
     * @return See above.
     */
    Session findSessionById(long id, org.hibernate.Session session);

    /**
     * Retrieves a session by ID.
     * @param id session ID to lookup
     * @param sf active service factory
     * @return See above.
     */
    Session findSessionById(long id, ServiceFactory sf);

    Long findSessionIdByUuid(String uuid, ServiceFactory sf);

    Long findSessionIdByUuid(String uuid);
}
