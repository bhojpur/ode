package ode.tools.hibernate;

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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import ode.util.SqlAction;
import ode.util.TableIdGenerator;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.Session;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.impl.SessionFactoryImpl;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

/**
 * Simple source of Thread-aware {@link Session} instances. Wraps a
 * call to {@link SessionFactoryUtils}. Should be safe to call from
 * within any service implementation call or inside of Executor.execute.
 */
public class SessionFactory implements MethodInterceptor {

    private final static Set<String> FORBIDDEN = Collections.unmodifiableSet(
        new HashSet<String>(
            Arrays.asList(
                "createSQLQuery", "getSession", "doWork",
                "connection", "disconnect", "reconnect")));

    static {
        // Check for spelling mistakes
        int found = 0;
        Method[] methods = Session.class.getMethods();
        for (Method m : methods) {
            if (FORBIDDEN.contains(m.getName())) {
                found++;
            }
        }
        if (found < FORBIDDEN.size()) {
            throw new RuntimeException("Method name not found! " + FORBIDDEN);
        }

    }


    private final org.hibernate.SessionFactory factory;

    public SessionFactory(org.hibernate.SessionFactory factory, SqlAction isolatedSqlAction) {
        this.factory = factory;
        for (Object k : this.factory.getAllClassMetadata().keySet()) {
            IdentifierGenerator ig =
                ((SessionFactoryImpl) factory).getIdentifierGenerator((String)k);
            if (ig instanceof TableIdGenerator) {
                ((TableIdGenerator) ig).setSqlAction(isolatedSqlAction);
            }
        }

    }

    /**
     * Returns a session active for the current thread. The returned
     * instance will be wrapped with AOP to prevent certain usage.
     * @return a wrapped active Hibernate session
     */
    public Session getSession() {

        Session unwrapped = SessionFactoryUtils.getSession(factory, false);

        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(new Class[]{
            Session.class,
            org.hibernate.classic.Session.class,
            org.hibernate.event.EventSource.class});
        proxyFactory.setTarget(unwrapped);
        proxyFactory.addAdvice(0, this);
        return (Session) proxyFactory.getProxy();

    }

    /**
     * Wraps all invocations to Session to prevent certain usages.
     * Note: {@link QueryBuilder} may unwrap the session in certain
     * cases.
     */
    public Object invoke(MethodInvocation mi) throws Throwable {

        final String name = mi.getMethod().getName();
        if (FORBIDDEN.contains(name)) {
            throw new ode.conditions.InternalException(String.format(
                "Usage of session.%s is forbidden.", name));
        }
        return mi.proceed();

    }

}