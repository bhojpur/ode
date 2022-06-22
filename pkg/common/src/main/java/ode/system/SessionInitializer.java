package ode.system;

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

import ode.api.ISession;
import ode.model.meta.Session;

/**
 * Manages the creation of a single {@link Session} created via the injected
 * {@link ISession} service. This is used for by the client-side
 * {@link ServiceFactory}. Each instance synchronizes on an internal mutex
 * during every call to {@link #getSession()} and
 * {@link #setSession(Session)}
 */
public class SessionInitializer {

    protected Object mutex = new Object();

    /** Principal given by the user */
    protected Principal principal;

    protected String credentials;

    protected ode.model.meta.Session session;

    protected ISession sessions;

    public void setSessionService(ISession service) {
        this.sessions = service;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }

    public void setCredentials(String securityCredentials) {
        this.credentials = securityCredentials;
    }

    public boolean hasSession() {
        synchronized (mutex) {
            return session != null;
        }
    }

    public ode.model.meta.Session getSession() {
        synchronized (mutex) {
            if (session == null) {
                session = sessions.createSession(principal, credentials);
            }
        }
        return this.session;
    }

    public void setSession(Session s) {
        synchronized (mutex) {
            this.session = s;
        }
    }

    public Principal createPrincipal() {
        getSession();
        Principal sessionPrincipal = new Principal(this.session.getUuid(),
                this.principal.getGroup(), this.principal.getEventType());
        return sessionPrincipal;
    }

}
