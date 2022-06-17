package ode.services.fulltext;

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

import ode.model.IAnnotated;
import ode.model.IGlobal;
import ode.model.IMutable;
import ode.model.IObject;
import ode.model.meta.EventLog;
import ode.services.eventlogs.EventLogFailure;
import ode.services.eventlogs.EventLogLoader;
import ode.services.eventlogs.PersistentEventLogLoader;
import ode.services.util.Executor.SimpleWork;
import ode.system.OdeContext;
import ode.system.ServiceFactory;
import ode.system.metrics.Histogram;
import ode.system.metrics.Metrics;
import ode.system.metrics.NullMetrics;
import ode.system.metrics.Timer;
import ode.tools.hibernate.QueryBuilder;
import ode.util.SqlAction;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Simple action which can be done in an asynchronous thread in order to index
 * Hibernate entities. Attempts to index each {@link EventLog} passed from the
 * {@link EventLogLoader} multiple times on failure. Eventually
 */
@Deprecated
public class FullTextIndexer extends SimpleWork implements ApplicationContextAware {

    private final static Logger log = LoggerFactory.getLogger(FullTextIndexer.class);

    /**
     * Default number of loops to wait if no external value is set.
     */
    public final static int DEFAULT_REPORTING_LOOPS = 100;

    abstract class Action {
        Class type;
        long id;
        IObject obj;

        abstract void go(FullTextSession session);

        abstract void log(Logger log);
    }

    class Purge extends Action {
        Purge(Class type, long id) {
            this.type = type;
            this.id = id;
        }

        @Override
        void go(FullTextSession session) {
            session.purge(type, id);
            session.flushToIndexes();
        }

        @Override
        void log(Logger log) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("Purged: %s:Id_%d", type, id));
            }
        }
    }

    class Index extends Action {

        Index(IObject obj) {
            this.obj = obj;
        }

        @Override
        void go(FullTextSession session) {
            session.index(obj);
            session.flushToIndexes();
        }

        @Override
        void log(Logger log) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("Indexed: %s", obj));
            }
        }
    }

    final protected EventLogLoader loader;

    final protected ParserSession parserSession;

    final protected Timer batchTimer;

    final protected Histogram completeSlow, completeFast;

    protected int reps = 5;

    protected long batch;

    /**
     * Frequency with which the percentage done should be calculated.
     */
    protected int reportingLoops = DEFAULT_REPORTING_LOOPS;

    protected boolean dryRun = false;

    protected OdeContext context = null;

    /**
     * Spring injector. Sets the number of indexing runs will be made if there
     * is a substantial backlog.
     */
    public void setRepetitions(int reps) {
        this.reps = reps;
    }

    public void setReportingLoops(int loops) {
        this.reportingLoops = loops;
    }

    public void setDryRun(boolean dryRun) {
        this.dryRun = dryRun;
    }

    public void setApplicationContext(ApplicationContext ctx) {
        this.context = (OdeContext) ctx;
    }

    public FullTextIndexer(EventLogLoader ll) {
        this(ll, new NullMetrics());
    }

    public FullTextIndexer(EventLogLoader ll, Metrics metrics) {
        super("FullTextIndexer", "index");
        this.loader = ll;
        this.parserSession = new ParserSession();
        this.batchTimer =
                metrics.timer(this, "batch");
        this.completeSlow=
                metrics.histogram(this, "percentCompleteSlow");
        this.completeFast =
                metrics.histogram(this, "percentCompleteFast");
    }

    /**
     * Since these instances are used repeatedly, we need to check for
     * already set SqlAction
     */
    @Override
    public synchronized void setSqlAction(SqlAction sql) {
        if (getSqlAction() == null) {
            super.setSqlAction(sql);
        }
    }

    /**
     * Runs {@link #doIndexing(FullTextSession)} within a Lucene transaction.
     * {@link #doIndexing(FullTextSession)} will also be called
     */
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE)
    public Object doWork(Session session, ServiceFactory sf) {
        int count = 1;
        int perbatch = 0;
        long start = System.currentTimeMillis();
        Timer.Context timer = null;
        do {
            batch++;
            timer = batchTimer.time();
            try {
                    // The following is non-portable and can later be refactored
                    // for a more general solution.
                    getSqlAction().deferConstraints();

                    // s.execute("set statement_timeout=10000");
                    // The Postgresql Driver does not currently support the
                    // "timeout" value on @Transactional and so if a query timeout
                    // is required, then this must be set.

                    FullTextSession fullTextSession = Search
                            .getFullTextSession(session);
                    fullTextSession.setFlushMode(FlushMode.MANUAL);
                    fullTextSession.setCacheMode(CacheMode.IGNORE);
                    perbatch = doIndexingWithWorldRead(sf, fullTextSession);
            } finally {
                timer.stop();
                count++;
            }
        } while (doMore(count));

        if (perbatch == 0) {
            log.debug("No objects indexed");
        } else {
            final long elapsed = (System.currentTimeMillis() - start);
            if (loader instanceof PersistentEventLogLoader) {
                long currId = ((PersistentEventLogLoader) loader).getCurrentId();
                long lastId = loader.lastEventLog().getId();
                String which = "~";
                double perc = 0.0f;
                if (batchTimer.getCount() % reportingLoops == 0) {
                    which = "";
                    perc = getSqlAction().getEventLogPercent(
                        ((PersistentEventLogLoader) loader).getKey());
                    completeSlow.update((int) perc);
                } else {
                    perc = 100.0 * ((float) currId) / ((float) lastId);
                    completeFast.update((int) perc);
                }

                log.info(String.format("INDEXED %4s objects in batch#%-6s " +
                    "[%7d ms.]  %s%2d%% done (%d of %d)",
                    perbatch, batch, elapsed,
                    which, ((int) perc), currId, lastId));
            } else {
                log.info(String.format("INDEXED %4s objects in batch#%-6s " +
                    "[%7d ms.]", perbatch, batch, elapsed));
            }
        }

        return null;
    }

    private int doIndexingWithWorldRead(ServiceFactory sf, FullTextSession session) {
        int rc = doIndexing(session);
        return rc;
    }

    public int doIndexing(FullTextSession session) {

        int count = 0;

        for (EventLog eventLog : loader) {

            if (dryRun) {
                continue;
            }

            if (eventLog != null) {
                handleEventLog(session, eventLog);
                count++;
            }
            session.flush();
            parserSession.closeParsedFiles();

        }
        return count;
    }

    protected void handleEventLog(FullTextSession session, EventLog eventLog) {
        String act = eventLog.getAction();
        Class type = asClassOrNull(eventLog.getEntityType());
        if (type != null) {
            long id = eventLog.getEntityId();

            Action action = null;
            if ("DELETE".equals(act)) {
                action = new Purge(type, id);
            } else if ("REINDEX".equals(act) || "UPDATE".equals(act) || "INSERT".equals(act)) {
                IObject obj = get(session, type, id);
                if (obj == null) {
                    // This object was deleted before the indexer caught up with
                    // the INSERT/UDPDATE log. Though this isn't a problem itself,
                    // this does mean that the indexer is likely going too slow.
                    log.debug(String.format("Null returned! Purging "
                            + "since cannot index %s:Id_%s for %s", type
                            .getName(), id, eventLog));
                    action = new Purge(type, id);
                } else {
                    action = new Index(obj);
                }
            } else {
                // Likely CHGRP-VALIDATION, PIXELDATA or similar.
                if (log.isDebugEnabled()) {
                    log.debug("Unknown action type: " + act);
                }
            }

            if (action != null) {
                try {
                    action.go(session);
                } catch (Exception e) {
                    try {
                        this.context.publishMessage(new EventLogFailure(loader, eventLog, e));
                    } catch (RuntimeException re) {
                        throw re;
                    } catch (Throwable e1) {
                        throw new RuntimeException(e1);
                    }
                }
                action.log(log);
            }
        }
    }

    /**
     * Default implementation suggests doing more if fewer than {@link #reps}
     * runs have been made and if there are still more than
     * {@link EventLogLoader#batchSize} x 100 backlog entries.
     *
     * This is based on the assumption that indexing runs roughly 120 times an
     * hour, so if there are more than an hours worth of batches, do extra work
     * to catch up.
     */
    public boolean doMore(int count) {
        if (count < this.reps && loader.more() > loader.getBatchSize() * 100) {
            log.info(String
                    .format("Suggesting round %s of "
                            + "indexing to reduce backlog of %s:", count,
                            loader.more()));
            return true;
        }
        return false;
    }

    protected Class asClassOrNull(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            log.warn("Unknown entity type found in database: " + str);
            return null;
        }
    }

    protected IObject get(Session session, Class type, long id) {
        QueryBuilder qb = new QueryBuilder();
        qb.select("this").from(type.getName(), "this");
        if (IAnnotated.class.isAssignableFrom(type)) {
            qb.join("this.annotationLinks", "l1", true, true);
            qb.join("l1.child", "a1", true, true);
            qb.join("a1.annotationLinks", "l2", true, true);
            qb.join("l2.child", "a2", true, true);
        }
        if (!IGlobal.class.isAssignableFrom(type)) {
            if (IMutable.class.isAssignableFrom(type)) {
                qb.join("this.details.updateEvent", "update", false, true);
            }
            qb.join("this.details.creationEvent", "create", false, true);
            qb.join("this.details.owner", "owner", false, true);
            qb.join("this.details.group", "group", false, true);
        }
        qb.where().and("this.id = :id");
        qb.param("id", id);

        return (IObject) qb.query(session).uniqueResult();
    }
}
