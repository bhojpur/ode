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

import java.util.ArrayList;
import java.util.List;

import ode.api.StatefulServiceInterface;
import ode.conditions.ApiUsageException;
import ode.conditions.InternalException;
import ode.conditions.SessionTimeoutException;
import ode.model.meta.Event;
import ode.model.meta.EventLog;
import ode.services.messages.ContextMessage;
import ode.services.util.ReadOnlyStatus;
import ode.system.EventContext;
import ode.tools.hibernate.SessionFactory;
import ode.util.SqlAction;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Session;
import org.springframework.context.ApplicationListener;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

/**
 * method interceptor responsible for login and creation of Events. Calls are
 * made to the {@link BasicSecuritySystem} provided in the
 * {@link EventHandler#EventHandler(SqlAction, BasicSecuritySystem, SessionFactory, TransactionAttributeSource, ReadOnlyStatus)
 * constructor}.
 * 
 * After the method is {@link MethodInterceptor#invoke(MethodInvocation)
 * invoked} various cleanup actions are performed and finally all credentials
 * all {@link BasicSecuritySystem#invalidateEventContext() cleared} from the
 * {@link Thread}.
 */
public class EventHandler implements MethodInterceptor, ApplicationListener<ContextMessage>{

    private static Logger log = LoggerFactory.getLogger(EventHandler.class);

    protected final TransactionAttributeSource txSource;

    protected final BasicSecuritySystem secSys;

    protected final SessionFactory factory;

    protected final SqlAction sql;

    protected final boolean readOnly;

    /**
     * only public constructor, used for dependency injection. Requires an
     * active {@link HibernateTemplate} and {@link BasicSecuritySystem}.
     *
     * @param sql the SQL action
     * @param securitySystem the security system
     * @param factory the Hibernate session factory
     * @param txSource the Spring transaction attribute source
     */
    @Deprecated
    public EventHandler(SqlAction sql,
            BasicSecuritySystem securitySystem, SessionFactory factory,
            TransactionAttributeSource txSource) {
        this(sql, securitySystem, factory, txSource, null);
    }

    public EventHandler(SqlAction sql,
            BasicSecuritySystem securitySystem, SessionFactory factory,
            TransactionAttributeSource txSource, ReadOnlyStatus readOnly) {
        this.secSys = securitySystem;
        this.txSource = txSource;
        this.factory = factory;
        this.sql = sql;
        this.readOnly = readOnly == null ? false : readOnly.isReadOnlyDb();
    }

    /**
     * If a {@link ContextMessage} is received then we need to either add a
     * {@link ode.services.messages.ContextMessage.Push} login to the stack or
     * {@link ode.services.messages.ContextMessage.Pop} remove one.
     */
    @Override
    public void onApplicationEvent(ContextMessage msg) {
       final CurrentDetails cd = secSys.cd;
       final Session session = factory.getSession();
       if (msg instanceof ContextMessage.Pop){
           secSys.disableReadFilter(session); // Disable old name
           cd.logout();
           secSys.enableReadFilter(session); // With old context
       } else if (msg instanceof ContextMessage.Push) {
           // We assume don't close and use the current readOnly setting
           final EventContext curr = cd.getCurrentEventContext();
           final boolean readOnly = curr.isReadOnly();
           final boolean isClose = false;
           // here we try to reproduce what's done in invoke
            // with the addition of having a call context
            // ourselves
            secSys.disableReadFilter(session); // Disable old name
            cd.login(cd.getLast()); // Login with same principal
            cd.setContext(msg.context);
            if (!doLogin(readOnly, isClose)) {
                throw new InternalException("Failed to login on Push: " +
                    msg.context);
            }
            secSys.enableReadFilter(session); // With new context
       }
    }

    /**
     * Invocation interceptor for preparing this {@link Thread} for execution
     * and subsequently resetting it.
     * 
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(MethodInvocation arg0) throws Throwable {
        boolean readOnly = checkReadOnly(arg0);
        boolean stateful = StatefulServiceInterface.class.isAssignableFrom(arg0
                .getThis().getClass());
        boolean isClose = stateful && "close".equals(arg0.getMethod().getName());

        if (!readOnly && this.readOnly) {
            throw new ApiUsageException("This instance is read-only");
        }

        final Session session = factory.getSession();

        if (!readOnly) {
            sql.deferConstraints();
        }
        if (!doLogin(readOnly, isClose)) {
            return null;
        }

        boolean failure = false;
        Object retVal = null;
        try {
            secSys.enableReadFilter(session);
            retVal = arg0.proceed();
            saveLogs(readOnly, session);
            secSys.cd.loadPermissions(session);
            return retVal;
        } catch (Throwable ex) {
            failure = true;
            throw ex;
        } finally {
            try {

                // on failure, we want to make sure that no one attempts
                // any further changes.
                if (failure) {
                    // TODO we should probably do some forced clean up here.
                }

                // stateful services should NOT be flushed, because that's part
                // of the state that should hang around.
                else if (stateful) {
                    // we don't want to do anything, really.
                }

                // read-only sessions should not have anything changed.
                else if (readOnly) {
                    if (session.isDirty()) {
                        if (log.isDebugEnabled()) {
                            log.debug("Clearing dirty session.");
                        }
                        session.clear();
                    }
                }

                // stateless services, don't keep their sesssions about.
                else {
                    session.flush();
                    if (session.isDirty()) {
                        throw new InternalException(
                                "Session is dirty. Cannot properly "
                                        + "reset security system. Must rollback.\n Session="
                                        + session);
                    }
                    secSys.disableReadFilter(session);
                    session.clear();
                }

            } finally {
                secSys.disableReadFilter(session);
                secSys.invalidateEventContext();
            }
        }

    }

    public boolean doLogin(boolean readOnly, boolean isClose) {

        try {
            secSys.loadEventContext(readOnly, isClose);
        } catch (SessionTimeoutException ste) {
            // If this is a CloseOnNoSessionContext then we skip all handling
            // since almost any action by the close() method will try to load
            // the context and will fail. This assumes that EventHandler is
            // the most inner handler. If this changes, then this logic may
            // need to be pushed down further.
            if (ste.sessionContext instanceof
                BasicSecurityWiring.CloseOnNoSessionContext) {
                log.debug("CloseOnNoSessionContext. Skipping");
                return false;
            }
            throw ste;
        }

        // now the user can be considered to be logged in.
        EventContext ec = secSys.getEventContext();
        if (!readOnly) {
            sql.prepareSession(
                    ec.getCurrentEventId(),
                    ec.getCurrentUserId(),
                    ec.getCurrentGroupId());
        }
        if (log.isInfoEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append(" Auth:\tuser=");
            sb.append(ec.getCurrentUserId());
            sb.append(",group=");
            sb.append(ec.getCurrentGroupId());
            sb.append(",event=");
            sb.append(ec.getCurrentEventId());
            sb.append("(");
            sb.append(ec.getCurrentEventType());
            sb.append("),sess=");
            sb.append(ec.getCurrentSessionUuid());
            Long shareId = ec.getCurrentShareId();
            if (shareId != null) {
                sb.append(",share=");
                sb.append(shareId);
            }
            log.info(sb.toString());
        }
        return true;
    }

    /**
     * checks method (and as a fallback the class) for the Spring
     * {@link Transactional} annotation.
     * 
     * @param mi
     *            Non-null method invocation.
     * @return true if the {@link Transactional} annotation lists this method as
     *         read-only, or if no annotation is found.
     */
    boolean checkReadOnly(MethodInvocation mi) {
        TransactionAttribute ta = txSource.getTransactionAttribute(mi
                .getMethod(), mi.getThis().getClass());
        return ta == null ? true : ta.isReadOnly();

    }

    /**
     * Calling clearLogs posits that these EventLogs were successfully saved,
     * and so this method may raise an event signalling such. This could
     * eventually be reworked to be fully within the security system.
     */
    void saveLogs(boolean readOnly, Session session) {

        // Grabbing a copy to prevent ConcurrentModificationEx
        final List<EventLog> logs = new ArrayList<EventLog>(secSys.getLogs());
        secSys.clearLogs();

        if (logs == null || logs.size() == 0) {
            return; // EARLY EXIT
        }

        if (readOnly) {
            // If we reach here, we have logs when we shouldn't.
            StringBuilder sb = new StringBuilder();
            sb.append("EventLogs in readOnly transaction:\n");
            for (EventLog eventLog : logs) {
                sb.append(eventLog.getAction());
                sb.append(" ");
                sb.append(eventLog);
                sb.append(eventLog.getEntityType());
                sb.append(" ");
                sb.append(eventLog.getEntityId());
                sb.append("\b");
            }
            throw new InternalException(sb.toString());
        }

        try {
            long lastValue = sql.nextValue("seq_eventlog", logs.size());
            long id = lastValue - logs.size() + 1;
            List<Object[]> batchData = new ArrayList<Object[]>();
            for (EventLog l : logs) {
                Event e = l.getEvent();
                if (e.getId() == null) {
                    throw new RuntimeException("Transient event");
                }
                batchData
                        .add(new Object[] { id++, -35L, l.getEntityId(),
                                l.getEntityType(), l.getAction(),
                                l.getEvent().getId() });
            }

            sql.insertLogs(batchData);

        } catch (Exception ex) {
            log.error("Error saving event logs: " + logs, ex);
        }

        if (secSys.getLogs().size() > 0) {
            throw new InternalException("More logs present after saveLogs()");
        }

    }
}