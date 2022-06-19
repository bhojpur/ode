package ode.services.util;

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
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ode.conditions.InternalException;
import ode.security.SecuritySystem;
import ode.security.basic.CurrentDetails;
import ode.services.scheduler.ThreadPool;
import ode.system.EventContext;
import ode.system.OdeContext;
import ode.system.Principal;
import ode.system.ServiceFactory;
import ode.tools.hibernate.SessionFactory;
import ode.tools.spring.InternalServiceFactory;
import ode.util.SqlAction;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;

/**
 * Simple execution/work interface which can be used for <em>internal</em> tasks
 * which need to have a full working implementation. The
 * {@link Executor#execute(Principal, Work)} method
 * ensures that {@link SecuritySystem#login(Principal)} is called before the
 * task, that a {@link TransactionCallback} and a {@link HibernateCallback}
 * surround the call, and that subsequently {@link SecuritySystem#logout()} is
 * called.
 */
public interface Executor extends ApplicationContextAware {

    public enum Priority {

        /**
         * Uses a non-limited thread pool.
         */
        SYSTEM,

        /**
         * Uses the limited thread pool configured via ode-server.properties
         * with ode.threads.max_threads, etc.
         */
        USER,

        /**
         * Separate thread pool for long-running tasks that should not prevent
         * users from logging in, etc.
         */
        BACKGROUND;
    }

    /**
     * Provides access to the context for Work-API consumers who need to publish
     * events, etc.
     */
    public OdeContext getContext();

    /**
     * Returns a {@link Principal} representing your current session or null,
     * if none is active.
     */
    public Principal principal();

    /**
     * Call {@link #execute(Map, Principal, Work)} with
     * a null call context.
     */
    <T> T execute(final Principal p, final Work<T> work);

    /**
     * Executes a {@link Work} instance wrapped in two layers of AOP. The first
     * is intended to acquire the proper arguments for
     * {@link Work#doWork(Session, ServiceFactory)} from the
     * {@link OdeContext}, and the second performs all the standard service
     * actions for any normal method call.
     *
     * If the {@link Map} argument is not null, then additionally,
     * setContext will be called in a try/finally block. The first login
     * within this thread will then pick up this delayed context.
     *
     * If the {@link Principal} argument is not null, then additionally, a
     * login/logout sequence will be performed in a try/finally block.
     *
     * {@link Work} implementation must be annotated with {@link Transactional}
     * in order to properly specify isolation, read-only status, etc.
     *
     * @param callContext
     *            Possibly null.
     * @param p
     *            Possibly null.
     * @param work
     *            Not null.
     * @return See above.
     */
    <T> T execute(final Map<String, String> callContext, final Principal p, final Work<T> work);

    /**
     * Call {@link #submit(Map, Callable)} with a null callContext.
     * @param callable
     * @return See above.
     */
    public <T> Future<T> submit(final Callable<T> callable);

    /**
     * Simple submission method which can be used in conjunction with a call to
     * {@link #execute(Principal, Work)} to overcome the no-multiple-login rule.
     * 
     * @param <T>
     * @param callContext Possibly null. See {@link CurrentDetails#setContext(Map)}
     * @param callable Not null. Action to be taken.
     * @return See above.
     */
    public <T> Future<T> submit(final Map<String, String> callContext,
            final Callable<T> callable);

    /**
     * Simple submission method with a {@link Priority}.
     *
     * @param prio Possibly null. See {@link #submit(Priority, Map, Callable)}
     * @param callable Not null. Action to be taken.
     * @return See above.
     */
    public <T> Future<T> submit(final Priority prio,
            final Callable<T> callable);

    /**
     * Like {@link #submit(Map, Callable)} but provide info to which priority
     * queue should be used.
     *
     * @param prio Possibly null. Priority for execution. Default: {@link Priority#USER}
     * @param callContext Possibly null. See {@link CurrentDetails#setContext(Map)}
     * @param callable Not null. Action to be taken.
     * @return See above.
     */
    public <T> Future<T> submit(Priority prio,
            final Map<String, String> callContext,
            final Callable<T> callable);

    /**
     * Helper method to perform {@link Future#get()} and properly unwrap the
     * exceptions. Any {@link RuntimeException} which was thrown during
     * execution will be rethrown. All other exceptions will be wrapped in an
     * {@link InternalException}.
     */
    public <T> T get(final Future<T> future);

    /**
     * Returns the {@link ExecutorService} assigned to this instance.
     * Used by {@link #submit(Callable)} and {@link #get(Future)}.
     */
    public ExecutorService getService();

    /**
     * Executes a {@link SqlWork} wrapped with a transaction. Since
     * {@link StatelessSession} does not return proxies, there is less concern
     * about returned values, but this method <em>completely</em> overrides
     * Bhojpur ODE security, and should be used <b>very</em> carefully.
     *
     * As with {@link #execute(Principal, Work)} the {@link SqlWork}
     * instance must be properly marked with an {@link Transactional}
     * annotation.
     *
     * @param work
     *            Non-null.
     * @return See above.
     */
    public Object executeSql(final SqlWork work);

    /**
     * Work SPI to perform actions within the server as if they were fully
     * wrapped in our service logic. Note: any results which are coming from
     * Hibernate <em>may <b>not</b></em> be assigned directly to a field, rather
     * must be returned as an {@link Object} so that Hibernate proxies can be
     * properly handled.
     */
    public interface Work<X> {
        /**
         * Work method. Must return all results coming from Hibernate via the
         * {@link Object} return method.
         *
         * @param session
         *            non null.
         * @param sf
         *            non null.
         * @return Any results which will be used by non-wrapped code.
         */
        X doWork(Session session, ServiceFactory sf);
    }

    interface LoggedWork<X> extends Work<X> {
        /**
         * Returns a description of what this work will be doing for logging
         * purposes.
         */
        String description();
    }

    /**
     * In the case of a stateful work order, the bean itself needs to be
     * associated with the sessionHandler and thread rather than the word
     * instance itself. If SessionHandler sees a StatefulWork instance
     * it will ask for the inner "this" to be used.
     */
    public interface StatefulWork {
        Object getThis();
    }

    /**
     * Work SPI to perform actions related to
     * {@link org.hibernate.SessionFactory#openStatelessSession() stateless}
     * sessions. This overrides <em>ALL</em> security in the server and should
     * only be used as a last resort. Currently accept locations are:
     * <ul>
     * <li>In the {@link ode.services.sessions.SessionManager} to boot strap a
     * {@link ode.model.meta.Session session}
     * <li>In the {@link ode.security.basic.EventHandler} to save
     * {@link ode.model.meta.EventLog event logs}
     * </ul>
     * 
     * Before the JTA fixes of 4.0, this interface provided a
     * {@link org.hibernate.StatelessSession}. However, as mentioned in
     * <a href="http://jira.springframework.org/browse/SPR-2495">jira:SPR-2495</a>,
     * that interface is not
     * currently supported in Spring's transaction management.
     */
    public interface SqlWork {

        /**
         * Return a description of what this work will be doing for logging
         * purposes.
         */
        String description();

        Object doWork(SqlAction sql);
    }

    public abstract class Descriptive {

        final protected String description;

        public Descriptive(Object o, String method, Object...params) {
            this(o.getClass().getName(), method, params);
        }

        public Descriptive(String name, String method, Object...params) {
            StringBuilder sb = new StringBuilder();
            sb.append(name);
            sb.append(".");
            sb.append(method);
            sb.append(ServiceHandler.getResultsString(params,
                    new IdentityHashMap<Object, String>()));
            this.description = sb.toString();
        }

        public String description() {
            return description;
        }
    }

    /**
     * Simple adapter which takes a String for {@link #description}
     */
    abstract class SimpleWork<T> extends Descriptive implements LoggedWork<T> {

        /**
         * Member field set by the {@link Executor} instance before
         * invoking {@link #doWork(Session, ServiceFactory)}. This
         * was introduced to prevent strange contortions trying to
         * get access to JDBC directly since the methods on Session
         * are no longer usable. It was introduced as a setter-injection
         * to prevent wide-scale changes to the code-base. It could
         * equally have been added to the interface method as an argument.
         */
        private /*final*/ SqlAction sql;

        public SimpleWork(Object o, String method, Object...params) {
            super(o, method, params);
        }

        public synchronized void setSqlAction(SqlAction sql) {
            if (this.sql != null) {
                throw new InternalException("Can only set SqlAction once!");
            }
            this.sql = sql;
        }

        public SqlAction getSqlAction() {
            return sql;
        }
    }

    /**
     * Simple adapter which takes a String for {@link #description}
     */
    public abstract class SimpleSqlWork extends Descriptive implements SqlWork {

        public SimpleSqlWork(Object o, String method, Object...params) {
            super(o, method, params);
        }

    }

    public class Impl implements Executor {

        private final static Logger log = LoggerFactory.getLogger(Executor.class);

        protected OdeContext context;
        protected InternalServiceFactory isf;
        final protected List<Advice> advices = new ArrayList<Advice>();
        final protected CurrentDetails principalHolder;
        final protected String[] proxyNames;
        final protected SessionFactory factory;
        final protected SqlAction sqlAction;
        final protected ThreadPool service;
        final protected ExecutorService systemService;

        public Impl(CurrentDetails principalHolder, SessionFactory factory,
                SqlAction sqlAction, String[] proxyNames) {
            this(principalHolder, factory, sqlAction, proxyNames,
                    new ThreadPool());
        }

        public Impl(CurrentDetails principalHolder, SessionFactory factory,
                SqlAction sqlAction, String[] proxyNames,
                ThreadPool service) {
            this.sqlAction = sqlAction;
            this.factory = factory;
            this.principalHolder = principalHolder;
            this.proxyNames = proxyNames;
            this.service = service;
            // Allowed to create more threads.
            this.systemService = Executors.newCachedThreadPool();
        }

        public void setApplicationContext(ApplicationContext applicationContext)
                throws BeansException {
            this.context = (OdeContext) applicationContext;
            this.isf = new InternalServiceFactory(this.context);
            for (String name : proxyNames) {
                advices.add((Advice) this.context.getBean(name));
            }
        }

        public OdeContext getContext() {
            return this.context;
        }

        public Principal principal() {
            if (principalHolder.size() == 0) {
                return null;
            } else {
                EventContext ec = principalHolder.getCurrentEventContext();
                String session = ec.getCurrentSessionUuid();
                return new Principal(session);
            }
        }

        /**
         * Call {@link #execute(Map, Principal, Work)}
         * with a null call context.
         */
        public <T> T execute(final Principal p, final Work<T> work) {
            return execute(null, p, work);
        }

        /**
         * Executes a {@link Work} instance wrapped in two layers of AOP. The
         * first is intended to acquire the proper arguments for
         * {@link Work#doWork(Session, ServiceFactory)} for
         * the {@link OdeContext}, and the second performs all the standard
         * service actions for any normal method call.
         *
         * If the {@link Principal} argument is not null, then additionally, a
         * login/logout sequence will be performed in a try/finally block.
         *  @param callContext Possibly null key-value map.
         * @param p
         * @param work
         */
        public <T> T execute(final Map<String, String> callContext,
                             final Principal p, final Work<T> work) {

            if (work instanceof SimpleWork) {
                ((SimpleWork) work).setSqlAction(sqlAction);
            }

            Interceptor i = new Interceptor(factory);
            ProxyFactory factory = new ProxyFactory();
            factory.setTarget(work);
            factory.setInterfaces(Work.class);

            for (Advice advice : advices) {
                factory.addAdvice(advice);
            }
            factory.addAdvice(i);

            Work wrapper = (Work) factory.getProxy();

            // First we guarantee that this will cause one and only
            // login to take place.
            if (p == null && principalHolder.size() == 0) {
                throw new IllegalStateException("Must provide principal");
            } else if (p != null && principalHolder.size() > 0) {
                throw new IllegalStateException(
                        "Already logged in. Use Executor.submit() and .get().");
            }

            // Don't need to worry about the login stack below since
            // already checked.
            if (p != null) {
                this.principalHolder.login(p);
            }
            if (callContext != null) {
                this.principalHolder.setContext(callContext);
            }

            try {
                // Arguments will be replaced after hibernate is in effect
                return (T) wrapper.doWork(null, isf);
            } finally {
                if (callContext != null) {
                    this.principalHolder.setContext(null);
                }
                if (p != null) {
                    int left = this.principalHolder.logout();
                    if (left > 0) {
                        log.warn("Logins left: " + left);
                        for (int j = 0; j < left; j++) {
                            this.principalHolder.logout();
	                    }
                    }
                }
            }
        }

        public <T> Future<T> submit(final Callable<T> callable) {
            return submit(null, null, callable);
        }

        public <T> Future<T> submit(final Map<String, String> callContext,
                final Callable<T> callable) {
            return submit(null, callContext, callable);
        }

        public <T> Future<T> submit(final Priority prio,
                final Callable<T> callable) {
            return submit(prio, null, callable);
        }

        public <T> Future<T> submit(final Priority prio,
                final Map<String, String> callContext,
                final Callable<T> callable) {

            Callable<T> wrapper = callable;
            if (callContext != null) {
                wrapper = new Callable<T>() {
                    public T call() throws Exception {
                        principalHolder.setContext(callContext);
                        try {
                            return callable.call();
                        } finally {
                            principalHolder.setContext(null);
                        }
                    }
                };
            }

            if (prio == null || prio == Priority.USER) {
                return service.submit(wrapper);
            } else if (prio == Priority.BACKGROUND) {
                return service.background(wrapper);
            } else if (prio == Priority.SYSTEM) {
                return systemService.submit(wrapper);
            } else {
                throw new InternalException("Unknown priority: " + prio);
            }
        }

        public <T> T get(final Future<T> future) {
            try {
                return future.get();
            } catch (InterruptedException e1) {
                throw new InternalException("Future.get interrupted:"
                        + e1.getMessage());
            } catch (ExecutionException e1) {
                if (e1.getCause() instanceof RuntimeException) {
                    throw (RuntimeException) e1.getCause();
                } else {
                    throw new InternalException(
                            "Caught exception thrown by Future.get:"
                                    + e1.getMessage());
                }
            }
        }

        public ExecutorService getService() {
            return service;
        }

        /**
         * Executes a {@link SqlWork} in transaction.
         * 
         * @param work
         *            Non-null.
         * @return See above.
         */
        public Object executeSql(final SqlWork work) {

            if (principalHolder.size() > 0) {
                throw new IllegalStateException(
                        "Currently logged in. \n"
                                + "JDBC will then take part in transaction directly. \n"
                                + "Please have the proper JDBC or data source injected.");
            }

            ProxyFactory factory = new ProxyFactory();
            factory.setTarget(work);
            factory.setInterfaces(SqlWork.class);
            factory.addAdvice(advices.get(2)); // TX FIXME
            SqlWork wrapper = (SqlWork) factory.getProxy();
            return wrapper.doWork(this.sqlAction);
        }

        /**
         * Interceptor class which properly lookups and injects the session
         * objects in the
         * {@link Work#doWork(Session, ServiceFactory)}
         * method.
         */
        static class Interceptor implements MethodInterceptor {
            private final SessionFactory factory;

            public Interceptor(SessionFactory sf) {
                this.factory = sf;
            }

            public Object invoke(final MethodInvocation mi) throws Throwable {
                final Object[] args = mi.getArguments();
                args[0] = factory.getSession();
                return mi.proceed();
            }
        }

    }
}
