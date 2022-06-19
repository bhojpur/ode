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

import java.util.concurrent.atomic.AtomicBoolean;

import ode.logic.HardWiredInterceptor;
import ode.system.Principal;
import ode.system.ServiceFactory;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AopContextInitializer extends HardWiredInterceptor {

    private final static Logger log = LoggerFactory.getLogger(AopContextInitializer.class);

    final ServiceFactory sf;
    
    final Principal pr;

    /**
     * Whether or not the current session was created via password-based (or
     * similar) login, or whether a session id was used to login (i.e. it's
     * "reused")
     */
    final AtomicBoolean reusedSession;

    public AopContextInitializer(ServiceFactory sf, Principal p, AtomicBoolean reusedSession) {
        this.sf = sf;
        this.pr = p;
        this.reusedSession = reusedSession;
    }
    
    public Object invoke(MethodInvocation mi) throws Throwable {
        HardWiredInterceptor.initializeUserAttributes(mi, sf, pr, reusedSession);
        return mi.proceed();
    }

}