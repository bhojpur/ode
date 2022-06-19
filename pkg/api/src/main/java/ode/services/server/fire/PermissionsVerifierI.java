package ode.services.server.fire;

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

import ode.api.IQuery;
import ode.conditions.SecurityViolation;
import ode.conditions.SessionException;
import ode.model.meta.Experimenter;
import ode.services.sessions.SessionManager;
import ode.services.sessions.SessionProvider;
import ode.services.util.Executor;
import ode.system.Principal;
import ode.system.ServiceFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import Glacier2._PermissionsVerifierDisp;
import Ice.Current;
import Ice.StringHolder;

public class PermissionsVerifierI extends _PermissionsVerifierDisp {

    private final static Logger log = LoggerFactory
            .getLogger(PermissionsVerifierI.class);

    private final Ring ring;

    private final SessionManager manager;

    private final SessionProvider sessionProvider;

    private final Executor ex;

    private final Principal p;

    public PermissionsVerifierI(Ring ring, SessionManager manager, SessionProvider sessionProvider, Executor ex, String uuid) {
        this.ring = ring;
        this.manager = manager;
        this.sessionProvider = sessionProvider;
        this.ex = ex;
        this.p = new Principal(uuid);
    }

    public boolean checkPermissions(final String userId, final String password,
            final StringHolder reason, final Current __current) {

        try {
            // At the very first, check if the userId or password is
            // actually a session id, if so we enforce that the userId
            // and the password are identical, becauser we have no other
            // method of signalling to the SessionManagerI instance that
            // the user has not provided a real password
            //
            // The addition of the userId check was added from an odd
            // case in which root added a username equal to the current
            // session. This prevents any LDAP lookup etc. from happening,
            // but by "giving" the user a session id (passed via the username)
            // s/he **will** be able to login **AS ROOT**!
            //

            Object session = null;

            // Local checks are faster.
            try {
                session = manager.find(password);
            } catch (SessionException e) {
                // pass
            }

            if (session == null) {
                try {
                    session = manager.find(userId); //
                } catch (SessionException e) {
                    // pass
                }
            }

            // If that doesn't work, make sure that the cluster doesn't know
            // something this instance doesn't. (Default of nullRedirector
            // returns null immediately)
            if (session == null) {
                if (ring.checkPassword(userId)) {
                    session = "ring.checkPassword(userId)";
                }
            }


            /*
             * ring.checkPassword calls SqlAction.activeSession
             * which logs the user id.
             *
            if (session == null) {
                if (ring.checkPassword(password)) {
                    session = "ring.checkPassword(password)";
                }
            }
            */


            // If any of the above blocks returned a valid value
            // then the password and/or userId matches an active
            // session. As long as userId == password, we return
            // true. Otherwise we return false, otherwise it
            // would be possible to circumvent the @HasPassword
            // restrictions.
            if (session != null) {
                if (userId.equals(password)) {
                    return true;
                } else {
                    log.warn("username and password don't match: " + userId);
                    reason.value = "username and password must be equal; use joinSession";
                    return false;
                }
            }

            // First check locally. Since we typically use redirects in the
            // cluster, it's most likely that our password will be in memory
            // in this instance.
            if (manager.executePasswordCheck(userId, password)) {
                return true;
            } else {
                final List<String> data = new ArrayList<String>();
                ex.execute(p, new Executor.SimpleWork<Void>("failedPassword", userId) {
                    @Transactional(readOnly = true)
                    public Void doWork(Session session, ServiceFactory sf) {
                        final Long sessionId = sessionProvider.findSessionIdByUuid(userId, sf);
                        final ode.model.meta.Session s =
                                sessionId == null ? null : sessionProvider.findSessionById(sessionId, sf);
                        IQuery q = sf.getQueryService();
                        Experimenter e = null;
                        if (s != null) {
                            e = s.getOwner();
                            if (!e.isLoaded()) {
                                e = q.get(Experimenter.class, e.getId());
                            }
                            data.add(String.format("user=%s", e.getOmeName()));
                        } else {
                            e = q.findByString(Experimenter.class,
                                    "odeName", userId);
                            if (e != null) {
                                data.add(String.format("id=%s", e.getId()));
                            }
                        }

                        if (s != null) {
                            data.add(String.format("created=%s", s.getStarted()));
                            data.add(String.format("closed=%s", s.getClosed()));
                        }

                        return null;
                    }
                });

                reason.value = String.format("Password check failed for '%s': %s",
                        userId, data);

                return false;
            }

        } catch (SecurityViolation sv) {
            reason.value = sv.getMessage();
            return false;
        } catch (Throwable t) {
            reason.value = "Internal error. Please contact your administrator:\n"
                    + t.getMessage();
            log.error("Exception thrown while checking password for:" + userId,
                    t);
            return false;
        }
    }

}