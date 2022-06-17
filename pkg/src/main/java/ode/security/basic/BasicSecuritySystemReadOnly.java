package ode.security.basic;

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

import java.util.Iterator;
import java.util.List;

import ode.model.annotations.Annotation;
import ode.model.annotations.CommentAnnotation;
import ode.security.ACLVoter;
import ode.security.EventProvider;
import ode.security.SecurityFilter;
import ode.security.SystemTypes;
import ode.security.policy.PolicyService;
import ode.services.sessions.SessionManager;
import ode.services.sessions.SessionManagerImpl;
import ode.services.sessions.SessionProvider;
import ode.system.Roles;
import ode.system.ServiceFactory;

/**
 * Provides a group context check that does not rely on SQL to bypass interception by Hibernate.
 * This read-only variant of the service queries group sudo annotations from the session provider instead of the database.
 */
public class BasicSecuritySystemReadOnly extends BasicSecuritySystem {

    public BasicSecuritySystemReadOnly(OdeInterceptor interceptor, SystemTypes sysTypes, CurrentDetails cd,
            SessionManager sessionManager, SessionProvider sessionProvider, EventProvider eventProvider, Roles roles,
            ServiceFactory sf, TokenHolder tokenHolder, List<SecurityFilter> filters, PolicyService policyService,
            ACLVoter aclVoter) {
        super(interceptor, sysTypes, cd, sessionManager, sessionProvider, eventProvider, roles, sf, tokenHolder, filters,
                policyService, aclVoter);
    }

    @Override
    protected boolean isGroupContextPermitted(long sessionId, long groupId) {
        final ode.model.meta.Session session = sessionProvider.findSessionById(sessionId, sf);
        final Iterator<Annotation> sessionAnnotations = session.linkedAnnotationIterator();
        while (sessionAnnotations.hasNext()) {
            final Annotation sessionAnnotation = sessionAnnotations.next();
            if (sessionAnnotation instanceof CommentAnnotation &&
                    SessionManagerImpl.GROUP_SUDO_NS.equals(sessionAnnotation.getNs()) &&
                    roles.isRootUser(sessionAnnotation.getDetails().getOwner()) &&
                    !isGroupContextPermitted(groupId, ((CommentAnnotation) sessionAnnotation).getTextValue())) {
                return false;
            }
        }
        return true;
    }
}