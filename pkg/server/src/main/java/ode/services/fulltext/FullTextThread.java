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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import ode.services.eventlogs.EventLogLoader;
import ode.services.sessions.SessionManager;
import ode.services.util.ExecutionThread;
import ode.services.util.Executor;
import ode.system.Principal;
import ode.system.ServiceFactory;
import ode.util.DetailsFieldBridge;
import ode.util.SqlAction;

/**
 * Library entry-point for indexing. Once the {@link FullTextThread} is properly
 * initialized calling {@link #run()} repeatedly and from multiple
 * {@link Thread threads} should be safe.
 * 
 * For more control, use the {@link EventLogLoader#more()} method to test how
 * often calls to {@link #run} should be made. See {@link Main} for examples.
 * 
 * By default, the indexing will take place as "root".
 */
@Deprecated
public class FullTextThread extends ExecutionThread {

    private final static Logger log = LoggerFactory.getLogger(FullTextThread.class);

    private final static Principal DEFAULT_PRINCIPAL = new Principal("root",
            "system", "FullText");

    private static final Executor.Work<?> PREPARE_INDEXING = new Executor.SimpleWork("FullTextIndexer", "prepare") {
        /**
         * Since this instance is used repeatedly, we need to check for
         * already set SqlAction
         */
        @Override
        public synchronized void setSqlAction(SqlAction sql) {
            if (getSqlAction() == null) {
                super.setSqlAction(sql);
            }
        }

        @Transactional(readOnly = false)
        @Override
        public Object doWork(Session session, ServiceFactory sf) {
            // Re-index entries noted in the _reindexing_required table.
            getSqlAction().refreshEventLogFromUpdatedAnnotations();
            return null;
        }
    };

    final protected boolean waitForLock;
    
    final protected FullTextIndexer indexer;

    final protected FullTextBridge bridge;

    private boolean isactive = true;
    
    private final Lock activeLock = new ReentrantLock(true);

    /**
     * Uses default {@link Principal} for indexing
     */
    public FullTextThread(SessionManager manager, Executor executor,
            FullTextIndexer indexer, FullTextBridge bridge) {
        this(manager, executor, indexer, bridge, DEFAULT_PRINCIPAL);
    }

    /**
     * Uses default {@link Principal} for indexing
     */
    public FullTextThread(SessionManager manager, Executor executor,
            FullTextIndexer indexer, FullTextBridge bridge, boolean waitForLock) {
        this(manager, executor, indexer, bridge, DEFAULT_PRINCIPAL, waitForLock);
    }

    /**
     * Main constructor. No arguments can be null.
     */
    public FullTextThread(SessionManager manager, Executor executor,
            FullTextIndexer indexer, FullTextBridge bridge, Principal principal) {
        super(manager, executor, indexer, principal);
        Assert.notNull(bridge);
        this.indexer = indexer;
        this.bridge = bridge;
        this.waitForLock = false;
    }

    /**
     * Main constructor. No arguments can be null.
     */
    public FullTextThread(SessionManager manager, Executor executor,
            FullTextIndexer indexer, FullTextBridge bridge,
            Principal principal, boolean waitForLock) {
        super(manager, executor, indexer, principal);
        Assert.notNull(bridge);
        this.indexer = indexer;
        this.bridge = bridge;
        this.waitForLock = waitForLock;
    }

    /**
     * Called by Spring on creation. Currently a no-op.
     */
    public void start() {
        log.info("Initializing Full-Text Indexer");
    }

    /**
     * Passes the {@link FullTextIndexer} instance to
     * {@link ode.services.util.Executor.Work#doWork(Session, ServiceFactory)}
     * between calls to {@link DetailsFieldBridge#lock()} and
     * {@link DetailsFieldBridge#unlock()} in order to guarantee that no other
     * {@link org.hibernate.search.bridge.FieldBridge} can edit the property.
     * Therefore, only one indexer using this idiom can run at a time.
     */
    @Override
    public void doRun() {

        activeLock.lock();
        try {
            if (!isactive) {
                log.info("Inactive; skipping");
                return;
            }
        } finally {
            activeLock.unlock();
        }

        final Map<String, String> callContext = new HashMap<String, String>();
        callContext.put("ode.group", "-1");

        final boolean gotLock;
        if (waitForLock) {
            DetailsFieldBridge.lock();
            gotLock = true;
        } else {
            gotLock = DetailsFieldBridge.tryLock();
        }
        if (gotLock) {
            try {
                DetailsFieldBridge.setFieldBridge(this.bridge);
                this.executor.execute(callContext, getPrincipal(), PREPARE_INDEXING);
                this.executor.execute(callContext, getPrincipal(), work);
            } finally {
                DetailsFieldBridge.unlock();
            }
        } else {
            log.info("Currently running; skipping");
        }
    }

    /**
     * Called by Spring on destruction. Waits for the global lock on
     * {@link DetailsFieldBridge} then marks this thread as inactive.
     */
    public void stop() {
        log.info("Shutting down Full-Text Indexer");
        this.indexer.loader.setStop(true);
        boolean acquiredLock = false;
        try {
            acquiredLock = activeLock.tryLock(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.warn("active lock acquisition interrupted.");
        }

        if (!acquiredLock) {
            log.error("Could not acquire active lock "
                    + "for indexer within 60 seconds. Overriding.");
        }

        acquiredLock = DetailsFieldBridge.tryLock();
        if (!acquiredLock) {
            log.error("Could not acquire bridge lock. "
                    + "Waiting 60 seconds and aborting.");
            try {
                Thread.sleep(60 * 1000L);
            } catch (InterruptedException e) {
                log.warn("bridge lock acquisition interrupted.");
            }
        }

        isactive = false;
        if (acquiredLock) {
            DetailsFieldBridge.unlock();
        }
    }

}
