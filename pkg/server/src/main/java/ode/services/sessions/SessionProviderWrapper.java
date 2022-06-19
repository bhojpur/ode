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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.BeanCreationException;

import ode.model.meta.Session;
import ode.services.util.ReadOnlyStatus;
import ode.system.ServiceFactory;

/**
 * A session provider that offers a unified view of multiple underlying session providers.
 * @param <P> session providers that adjust according to read-only status
 */
public class SessionProviderWrapper<P extends SessionProvider & ReadOnlyStatus.IsAware> implements SessionProvider {

    private final List<P> read, write;

    /**
     * Construct a new Session provider.
     * @param readOnly the read-only status
     * @param providers the Session providers to wrap: the earlier providers are tried first and at least one provider must support
     * write operations according to {@link ode.services.util.ReadOnlyStatus.IsAware#isReadOnly(ReadOnlyStatus)}
     */
    public SessionProviderWrapper(ReadOnlyStatus readOnly, List<P> providers) {
        read = providers;
        write = new ArrayList<P>(read.size());
        for (final P provider : read) {
            if (!provider.isReadOnly(readOnly)) {
                write.add(provider);
            }
        }
        if (write.isEmpty()) {
            throw new BeanCreationException("must be given a read-write session provider");
        }
    }

    @Override
    public Session executeUpdate(ServiceFactory sf, Session session, String uuid, long userId, Long sudoerId) {
        /* working through all readers because we want a failure exception if the session exists as read-only */
        for (final P provider : read) {
            if (provider.findSessionIdByUuid(uuid, sf) != null) {
                return provider.executeUpdate(sf, session, uuid, userId, sudoerId);
            }
        }
        /* creating a new session */
        return write.get(0).executeUpdate(sf, session, uuid, userId, sudoerId);
    }

    @Override
    public void executeCloseSession(String uuid) {
        /* working through all readers because we want a failure exception if the session exists as read-only */
        for (final P provider : read) {
            if (provider.findSessionIdByUuid(uuid) != null) {
                provider.executeCloseSession(uuid);
            }
        }
    }

    @Override
    public Session executeInternalSession(String uuid, Session session) {
        return write.get(0).executeInternalSession(uuid, session);
    }

    @Override
    public long executeNextSessionId() {
        return write.get(0).executeNextSessionId();
    }

    @Override
    public Session findSessionById(long id, org.hibernate.Session hibernateSession) {
        for (final P provider : read) {
            final Session session = provider.findSessionById(id, hibernateSession);
            if (session != null) {
                return session;
            }
        }
        return null;
    }

    @Override
    public Session findSessionById(long id, ServiceFactory sf) {
        for (final P provider : read) {
            final Session session = provider.findSessionById(id, sf);
            if (session != null) {
                return session;
            }
        }
        return null;
    }

    @Override
    public Long findSessionIdByUuid(String uuid, ServiceFactory sf) {
        for (final P provider : read) {
            final Long sessionId = provider.findSessionIdByUuid(uuid, sf);
            if (sessionId != null) {
                return sessionId;
            }
        }
        return null;
    }

    @Override
    public Long findSessionIdByUuid(String uuid) {
        for (final P provider : read) {
            final Long sessionId = provider.findSessionIdByUuid(uuid);
            if (sessionId != null) {
                return sessionId;
            }
        }
        return null;
    }
}
