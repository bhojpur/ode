package ode.services.server.util;

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
import java.util.UUID;

import ode.services.server.fire.TopicManager;
import ode.services.util.Executor;
import ode.tools.spring.OnContextRefreshedEventListener;
import ode.util.SqlAction;
import ode.constants.categories.PROCESSORCALLBACK;
import ode.constants.topics.PROCESSORACCEPTS;
import ode.grid.ProcessorCallbackPrx;
import ode.grid.ProcessorCallbackPrxHelper;
import ode.grid.ProcessorPrx;
import ode.grid.ProcessorPrxHelper;
import ode.grid._ProcessorCallbackDisp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.annotation.Transactional;

import Ice.Current;
import Ice.ObjectAdapter;

public class CheckAllJobs extends OnContextRefreshedEventListener {

    private static final Logger log = LoggerFactory.getLogger(CheckAllJobs.class);

    private final Executor ex;

    private final ObjectAdapter oa;

    private final TopicManager tm;

    private final Ice.Identity id;

    private final long waitMs;

    public CheckAllJobs(Executor ex, ObjectAdapter oa, TopicManager tm) {
        this(ex, oa, tm, 10000);
    }

    public CheckAllJobs(Executor ex, ObjectAdapter oa, TopicManager tm,
            long waitMs) {
        this.waitMs = waitMs;
        this.ex = ex;
        this.oa = oa;
        this.tm = tm;
        this.id = new Ice.Identity(UUID.randomUUID().toString(),
                PROCESSORCALLBACK.value);
    }

    @Override
    public void handleContextRefreshedEvent(ContextRefreshedEvent event) {
        run();
    }

    public void run() {
        Callback cb = new Callback();
        Ice.ObjectPrx prx = oa.add(cb, id); // OK ADAPTER USAGE
        ProcessorCallbackPrx cbPrx = ProcessorCallbackPrxHelper.uncheckedCast(prx);
        tm.onApplicationEvent(new TopicManager.TopicMessage(this,
                PROCESSORACCEPTS.value, new ProcessorPrxHelper(),
                "requestRunning", cbPrx));

        new Thread() {
            @Override
            public void run() {
                log.info("Waiting " + waitMs / 1000 + " secs. for callbacks");
                long start = System.currentTimeMillis();
                while (System.currentTimeMillis() < (start + waitMs)) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // ok
                    }
                }
                synchronizeJobs();
            }
        }.start();
    }

    public void synchronizeJobs() {

        final Callback cb = (Callback) oa.find(id);
        final List<Long> ids = new ArrayList<Long>();
        synchronized (cb.openJobs) {
            ids.addAll(cb.openJobs);
        }

        try {
            ex.executeSql(new Executor.SimpleSqlWork(this,
                    "synchronizeJobs") {
                @Transactional(readOnly = false)
                public Object doWork(SqlAction sql) {
                    int count = sql.synchronizeJobs(ids);
                    if (count > 0) {
                        log.warn("Forcibly closed " + count
                                + " abandoned job(s).");
                    }
                    return null;
                }

            });

        } finally {
            oa.remove(id); // OK ADAPTER USAGE
        }
    }

    private class Callback extends _ProcessorCallbackDisp {

        private final List<Long> openJobs = new ArrayList<Long>();

        public void isAccepted(boolean accepted, String sessionUuid,
                String proxyConn, Current __current) {
            log.error("isAccepted should not have been called");
        }

        public void isProxyAccepted(boolean accepted, String sessionUuid,
                ProcessorPrx procProxy, Current __current) {
            log.error("isProxyAccepted should not have been called");
        }

        public void responseRunning(List<Long> jobIds, Current __current) {
            synchronized (openJobs) {
                if (jobIds != null) {
                    log.info("Received " + jobIds.size() + " job(s)");
                    openJobs.addAll(jobIds);
                } else {
                    log.warn("Null jobIds list sent.");
                }
            }
        }

    }

}