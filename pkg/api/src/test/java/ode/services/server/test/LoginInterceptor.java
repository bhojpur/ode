package ode.services.server.test;

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

import ode.security.basic.PrincipalHolder;
import ode.system.Principal;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * With {@link ManagedContextFixture} this test class was copied from
 * components/server/ode/server/itests, due to lack of support in the ant
 * build for sharing testing infrastructure (Nov2008)
 * @DEV.TODO Reunite with server code.
 */
public class LoginInterceptor implements MethodInterceptor {

    final PrincipalHolder holder;
    public Principal p;

    LoginInterceptor(PrincipalHolder holder) {
        this.holder = holder;
    }

    public Object invoke(MethodInvocation arg0) throws Throwable {
        int still;
        still = holder.size();
        if (still != 0) {
            throw new RuntimeException(still + " remaining on login!");
        }
        holder.login(p);
        try {
            return arg0.proceed();
        } finally {
            still = holder.logout();
            if (still != 0) {
                throw new RuntimeException(still + " remaining on logout!");
            }
        }
    }

}