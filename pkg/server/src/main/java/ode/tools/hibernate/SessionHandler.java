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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import javax.sql.DataSource;

import ode.api.StatefulServiceInterface;
import ode.conditions.ApiUsageException;
import ode.conditions.InternalException;
import ode.services.messages.RegisterServiceCleanupMessage;
import ode.services.util.Executor;
import ode.system.OdeContext;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate3.HibernateInterceptor;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * holder for Hibernate sessions in stateful servics. A count of calls is kept.
 */
class SessionStatus {

    int calls = 0;

    Session session;

    SessionStatus(Session session) {
        if (null == session) {
            throw new IllegalArgumentException("No null sessions.");
        }

        this.session = session;
    }

}

/**
 * interceptor which delegates to
 * {@link org.springframework.orm.hibernate3.HibernateInterceptor} for stateless
 * services but which keeps a {@link java.util.WeakHashMap} of sessions keyed by
 * the stateful service reference.
 */
public class SessionHandler implements MethodInterceptor,
        ApplicationContextAware {

    /**
     * used by the SessionHandler to test for the end of the stateful service's
     * life. Using reflection so we get a bit more type safety.
     */
    private final Method close;

    private final static Logger log = LoggerFactory.getLogger(SessionHandler.class);

    /**
     * Access to this collection should only be performed by the protected
     * methods on this class in order to guarantee the semantics in
     * {@link #getThis(MethodInvocation)}.
     */
    private final Map<Object, SessionStatus> __sessions = Collections
            .synchronizedMap(new WeakHashMap<Object, SessionStatus>());

    private OdeContext ctx;

    private final SessionFactory factory;

    private final static SessionHolder DUMMY = new EmptySessionHolder();

    final private static String CTOR_MSG = "Both arguments to the SessionHandler"
            + " constructor should be not null.";

    /**
     * Constructor taking a {@link SessionFactory}.
     * A new {@link HibernateInterceptor} will be created.
     *
     * @param factory
     *            Not null.
     */
    public SessionHandler(SessionFactory factory) {
        if (factory == null) {
            throw new ApiUsageException(CTOR_MSG);
        }

        this.factory = factory;

        try {
            close = StatefulServiceInterface.class.getMethod("close");
        } catch (Exception e) {
            throw new InternalException(
                    "Can't get StatefulServiceInterface.close method.");
        }

    }

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.ctx = (OdeContext) applicationContext;
    }

    //
    // LOOKUP METHODS
    //
    protected Object getThis(MethodInvocation invocation) {
        Object obj = invocation.getThis();
        if (obj instanceof Executor.StatefulWork) {
            obj = ((Executor.StatefulWork) obj).getThis();
        }
        return obj;
    }

    protected void putStatus(MethodInvocation invocation, SessionStatus status) {
        __sessions.put(getThis(invocation), status);
    }

    private SessionStatus getStatus(MethodInvocation invocation) {
        return __sessions.get(getThis(invocation));
    }

    protected SessionStatus removeStatus(final MethodInvocation invocation) {
        return __sessions.remove(getThis(invocation));
    }

    //
    // THREAD METHODS
    //

    public void cleanThread() {
        if (TransactionSynchronizationManager.hasResource(factory)) {
            SessionHolder holder = (SessionHolder) TransactionSynchronizationManager
                    .getResource(factory);
            if (holder == null) {
                throw new IllegalStateException("Can't be null.");
            } else if (holder == DUMMY) {
                TransactionSynchronizationManager.unbindResource(factory);
            } else {
                throw new IllegalStateException("Thread corrupted.");
            }
        }
    }

    /**
     * delegates to {@link HibernateInterceptor} or manages sessions internally,
     * based on the type of service.
     */
    public Object invoke(final MethodInvocation invocation) throws Throwable {

        Object obj = getThis(invocation);

        // Stateless; normal semantics.
        if (!StatefulServiceInterface.class.isAssignableFrom(
                obj.getClass())) {
            throw new InternalException(
                    "Stateless service configured as stateful.");
        }

        // Stateful; let's get to work.
        debug("Performing action in stateful session.");
        return doStateful(invocation);
    }

    private Object doStateful(final MethodInvocation invocation)
            throws Throwable {
        Object result = null;
        SessionStatus status = null;
        try {
            // Need to open even if "closing" because the service may need
            // to perform cleanup in its close() method.
            status = newOrRestoredSession(invocation);
            status.session.setFlushMode(FlushMode.COMMIT);
            // changing MANUAL to COMMIT the appserver
            // won't allow us to commit here anyway, and setting to COMMIT
            // prevents Spring from automatically re-writing the flushMode
            // as AUTO
            result = invocation.proceed();
            return result;
        } finally {
            // TODO do we need to check for disconnected or closed session here?
            // The newOrRestoredSession method does not attempt to close the
            // session before throwing the dirty session exception. We must do
            // it here.
            try {
                if (isCloseSession(invocation)) {
                    ctx.publishMessage(new RegisterServiceCleanupMessage(this,
                            invocation.getThis()) {
                        @Override
                        public void close() {
                            SessionStatus status = removeStatus(invocation);
                            status.session.disconnect();
                            status.session.close();
                        }

                    });
                } else {
                    if (status != null) {
                        // Guarantee that no one has changed the FlushMode
                        status.session.setFlushMode(FlushMode.MANUAL);
                        status.session.disconnect();
                        status.calls--;
                    }
                }
            } catch (Exception e) {

                log.error("Error while closing/disconnecting session.", e);

            } finally {

                try {
                    resetThreadSession();
                } catch (Exception e) {
                    log.error("Could not cleanup thread session.", e);
                    throw e;
                }

            }

        }
    }

    private SessionStatus newOrRestoredSession(MethodInvocation invocation)
            throws HibernateException {

        SessionStatus status = getStatus(invocation);
        Session previousSession = nullOrSessionBoundToThread();

        // a session is currently running.
        // something has gone wrong (e.g. with cleanup) abort!
        if (previousSession != null) {
            String msg = "Dirty Hibernate Session " + previousSession
                    + " found in Thread " + Thread.currentThread();

            // If it is closeSession, then this will be handled by
            // the finally{} block of doStateful
            if (!isCloseSession(invocation)) {
                previousSession.close();
            }
            throw new InternalException(msg);
        }

        // we may or may not be in a session, but if we haven't yet bound
        // it to This, then we need to.
        else if (status == null || !status.session.isOpen()) {
            Session currentSession = acquireAndBindSession();
            status = new SessionStatus(currentSession);
            putStatus(invocation, status);
        }

        // the session bound to This is already currently being called. abort!
        else if (status.calls > 1) {
            throw new InternalException(
                    "Hibernate session is not re-entrant.\n"
                            + "Either you have two threads operating on the same "
                            + "stateful object (don't do this)\n or you have a "
                            + "recursive call (recurse on the unwrapped object). ");
        }

        // all is fine.
        else {
            debug("Binding and reconnecting session.");
            // TODO doesn't make sense to check, because hibernate always
            // says "yes" if it has a connectionProvider
            // if (status.session.isConnected())
            // {
            // throw new InternalException("Session already connected!");
            // }
            bindSession(status.session);
            // Connection connection =
            // DataSourceUtils.getConnection(dataSource);
            // status.session.reconnect(connection);
        }

        // It's ready to be used. Increment.
        status.calls++;
        return status;

    }

    // ~ SESSIONS
    // =========================================================================

    private boolean isCloseSession(MethodInvocation invocation) {
        return close.getName().equals(invocation.getMethod().getName());
    }

    private Session acquireAndBindSession() throws HibernateException {
        debug("Opening and binding session.");
        Session session = factory.openSession();
        bindSession(session);
        return session;
    }

    private void bindSession(Session session) {
        debug("Binding session to thread.");
        SessionHolder sessionHolder = new SessionHolder(session);
        sessionHolder.setTransaction(sessionHolder.getSession()
                .beginTransaction()); // FIXME TODO
        // If we reach this point, it's ok to bind the new SessionHolder,
        // however the DUMMY EmptySessionHolder may be present so unbind
        // just in case.
        if (TransactionSynchronizationManager.hasResource(factory)) {
            TransactionSynchronizationManager.unbindResource(factory);
        }
        TransactionSynchronizationManager.bindResource(factory, sessionHolder);
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            throw new InternalException("Synchronization not active for "
                    + "TransactionSynchronizationManager");
        }
    }

    private Session nullOrSessionBoundToThread() {
        SessionHolder holder = null;
        if (TransactionSynchronizationManager.hasResource(factory)) {
            holder = (SessionHolder) TransactionSynchronizationManager
                    .getResource(factory);
            // A bit tricky. Works in coordinate with resetThreadSession
            // since the DUMMY would be replaced anyway.
            if (holder != null && holder.isEmpty()) {
                holder = null;
            }
        }
        return holder == null ? null : holder.getSession();
    }

    private boolean isSessionBoundToThread() {
        return nullOrSessionBoundToThread() != null;
    }

    private void resetThreadSession() {
        if (isSessionBoundToThread()) {
            debug("Session bound to thread. Resetting.");
            TransactionSynchronizationManager.unbindResource(factory);
            TransactionSynchronizationManager.bindResource(factory, DUMMY);
        } else {
            debug("Session not bound to thread. No need to reset.");
        }
    }

    private void debug(String message) {
        if (log.isDebugEnabled()) {
            log.debug(message);
        }
    }

}

class EmptySessionHolder extends SessionHolder {
    public EmptySessionHolder() {
        super((Session) Proxy.newProxyInstance(Session.class.getClassLoader(),
                new Class[] { Session.class }, new InvocationHandler() {
                    public Object invoke(Object proxy, Method method,
                            Object[] args) throws Throwable {
                        String name = method.getName();
                        if (name.equals("toString")) {
                            return "NULL SESSION PROXY";
                        }

                        else if (name.equals("hashCode")) {
                            return 0;
                        } else if (name.equals("equals")) {
                            return args[0] == null ? false : proxy == args[0];
                        } else {
                            throw new RuntimeException("No methods allowed");
                        }
                    }
                }));
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}