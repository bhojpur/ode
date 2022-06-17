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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import ode.api.IQuery;
import ode.services.eventlogs.AllEntitiesPseudoLogLoader;
import ode.services.eventlogs.AllEventsLogLoader;
import ode.services.eventlogs.EventLogLoader;
import ode.services.eventlogs.PersistentEventLogLoader;
import ode.services.sessions.SessionManager;
import ode.services.util.Executor;
import ode.system.OdeContext;
import ode.system.Principal;
import ode.system.ServiceFactory;
import ode.system.metrics.Metrics;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * Commandline entry-point for various full text actions. Commands include:
 * <ul>
 * <li>full - Index full database</li>
 * <li>events - Index all events</li>
 * </ul>
 */

public class Main {

    private final static Logger log = LoggerFactory.getLogger(Main.class);

    static AtomicBoolean shutdown = new AtomicBoolean(false);
    static String uuid;
    static String[] excludes;
    static OdeContext context;
    static Executor executor;
    static SessionFactory factory;
    static IQuery rawQuery;
    static SessionManager manager;
    static FullTextBridge bridge;
    static PersistentEventLogLoader loader;
    static Metrics metrics;

    // Setup

    public static void init() {

        if (shutdown.get()) {
            return; // EARLY EXIT
        }

        context = OdeContext.getInstance("ode.fulltext");
        try {
            // Now that we're using the fulltext context we need
            // to disable the regular processing, otherwise there
            // are conflicts.
            Scheduler scheduler = context.getBean("scheduler", Scheduler.class);
            scheduler.pauseAll();
        } catch (SchedulerException se) {
            throw new RuntimeException(se);
        }

        SignalHandler handler = new SignalHandler() {
            public void handle(Signal sig) {
                close(sig, null);
            }
        };

        for (String sig : new String[]{"INT","TERM","BREAK"}) {
            try {
                Signal.handle(new Signal(sig), handler);
            } catch (IllegalArgumentException iae) {
                // Ok. BREAK will not exist on non-Windows systems, for example.
            }
        }

        uuid = context.getBean("uuid", String.class);
        executor = (Executor) context.getBean("executor");
        factory = (SessionFactory) context.getBean("sessionFactory");
        rawQuery = (IQuery) context.getBean("internal-ode.api.IQuery");
        manager = (SessionManager) context.getBean("sessionManager");
        bridge = (FullTextBridge) context.getBean("fullTextBridge");
        loader =  (PersistentEventLogLoader) context.getBean("eventLogLoader");
        metrics = (Metrics) context.getBean("metrics");
        String excludesStr = context.getProperty("ode.search.excludes");
        if (excludesStr != null) {
            excludes = excludesStr.split(",");
        } else {
            excludes = new String[]{};
        }
    }

    public static void close(Signal sig, Integer rc) {
        if (!shutdown.get()) {
            if (sig != null) {
                log.info(sig.getName() + ": Shutdown requested.");
            }
            shutdown.set(true);
            OdeContext copy = context;
            context = null;
            copy.close();
            log.info("Done");
            if (sig != null) {
                System.exit(sig.getNumber());
            } else {
                System.exit(rc);
            }
        }
    }

    protected static FullTextThread createFullTextThread(EventLogLoader loader) {
        return createFullTextThread(loader, false);
    }

    protected static FullTextThread createFullTextThread(EventLogLoader loader,
            boolean dryRun) {
        final FullTextIndexer fti = new FullTextIndexer(loader, metrics);
        fti.setApplicationContext(context);
        fti.setDryRun(dryRun);
        final FullTextThread ftt = new FullTextThread(manager, executor, fti,
                bridge);
        return ftt;
    }

    // Public usage

    public static void usage() {
        StringBuilder sb = new StringBuilder();
        sb.append("usage: [-Dlogback.configurationFile=stderr.xml] ");
        sb.append("ode.service.fulltext.Main [help|foreground|dryrun|reset|"
                + "standalone|events|full|");
        sb.append("reindex class1 class2 class3 ...]\n");
        System.out.println(sb.toString());
        System.exit(-2);
    }

    public static void main(String[] args) throws Throwable {

        int rc = 0;
        try {
            if (args == null || args.length == 0) {
                usage();
            } else if ("reset".equals(args[0])) {
                reset(args);
            } else if ("dryrun".equals(args[0])) {
                foreground(true, args);
            } else if ("foreground".equals(args[0])) {
                foreground(false, args);
            } else if ("standalone".equals(args[0])) {
                standalone(args);
            } else if ("events".equals(args[0])) {
                indexAllEvents();
            } else if ("full".equals(args[0])) {
                indexFullDb();
            } else if ("reindex".equals(args[0])) {
                if (args.length < 2) {
                    usage(); // EARLY EXIT
                }
                Set<String> set = new HashSet<String>();
                for (int i = 1; i < args.length; i++) {
                    set.add(args[i]);
                }
                indexByClass(set);
            } else {
                usage();
            }
        } catch (Throwable t) {
            rc = 1;
            t.printStackTrace();
        } finally {
            close(null, rc);
        }
    }

    public static void indexFullDb() {
        init();
        final AllEntitiesPseudoLogLoader loader = new AllEntitiesPseudoLogLoader();
        loader.setQueryService(rawQuery);
        loader.setExcludes(excludes);
        loader.setClasses(factory.getAllClassMetadata().keySet());
        final FullTextThread ftt = createFullTextThread(loader);
        while (loader.more() > 0) {
            ftt.run();
        }
    }

    public static void indexByClass(Set<String> set) {
        init();
        final AllEntitiesPseudoLogLoader loader = new AllEntitiesPseudoLogLoader();
        loader.setQueryService(rawQuery);
        loader.setClasses(set);
        final FullTextThread ftt = createFullTextThread(loader);
        while (loader.more() > 0) {
            ftt.run();
        }
    }

    public static void indexAllEvents() {
        init();
        final AllEventsLogLoader loader = new AllEventsLogLoader();
        loader.setExcludes(excludes);
        loader.setQueryService(rawQuery);
        final FullTextThread ftt = createFullTextThread(loader);

        while (loader.more() > 0) {
            ftt.run();
        }
    }

    /**
     * Can be used to reset the value that the {@link PersistentEventLogLoader}
     * would read if started now.
     */
    public static void reset(String[] args) {
        init();
        long oldValue = -1;
        long newValue = 0;
        if (args == null || args.length != 2) {
            System.out.println("Using 0 as reset target");
        } else {
            newValue = Long.valueOf(args[1]);
        }

        oldValue = loader.getCurrentId();
        loader.setCurrentId(newValue);
        System.out.println("=================================================");
        System.out.println(String.format("Value reset to %s. Was %s",
                newValue, oldValue));
        System.out.println("=================================================");
    }

    /**
     * Uses a {@link PersistentEventLogLoader} and cycles through all
     * the remaining logs. Reset can be called first for a complete
     * re-indexing.
     */
    public static void foreground(boolean dryrun, String[] args) {
        init();
        final FullTextThread ftt = createFullTextThread(loader, dryrun);

        long loops = 0;
        long current = current(loader);
        while (true) {
            // Quartz usually would wait 3 seconds here.
            loops++;
            ftt.run();
            long newCurrent = current(loader);
            if (newCurrent == current) {
                break;
            } else {
                current = newCurrent;
            }
        }
        System.out.println("=================================================");
        System.out.println(String.format(
                "Done in %s loops. Now at: %s", loops, current));
        System.out.println("=================================================");
    }

    /**
     * Starts up and simply waits until told by the grid to disconnect.
     */
    public static void standalone(String[] args) {
        Ice.Communicator ic = Ice.Util.initialize(args);
        Ice.ObjectAdapter oa = ic.createObjectAdapter("IndexerAdapter");
        oa.activate();
        String cron = ic.getProperties().getProperty("ode.search.cron");
        if (cron == null || cron.length() == 0) {
            System.out.println("Using default cron value.");
        } else {
            System.setProperty("ode.search.cron",cron);
        }
        try {
            init(); // Starts cron
        } finally {
            ic.waitForShutdown();
        }
    }

    private static long current(final PersistentEventLogLoader loader) {
        Principal p = new Principal(uuid);
        return (Long) executor.execute(p, new Executor.SimpleWork(loader, "more"){
            @Override
            @Transactional(readOnly=false)
            public Object doWork(Session session, ServiceFactory sf) {
                return loader.getCurrentId();
            }
        });
    }
}
