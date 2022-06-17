package ode.services.pixeldata;

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
import java.util.NoSuchElementException;

import ode.api.IQuery;
import ode.conditions.LockTimeout;
import ode.io.nio.PixelsService;
import ode.model.core.Channel;
import ode.model.core.Pixels;
import ode.model.meta.Event;
import ode.model.meta.EventLog;
import ode.model.stats.StatsInfo;
import ode.parameters.Parameters;
import ode.services.eventlogs.EventLogLoader;
import ode.services.util.Executor.SimpleWork;
import ode.system.ServiceFactory;
import ode.util.SqlAction;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Simple action which can be done in an asynchronous thread in order to process
 * PIXELDATA event logs.
 */
public class PixelDataHandler extends SimpleWork {

    private final static Logger log = LoggerFactory.getLogger(PixelDataHandler.class);

    final protected EventLogLoader loader;

    final protected PixelsService pixelsService;

    protected int reps = 5;

    /**
     * Spring injector. Sets the number of indexing runs will be made if there
     * is a substantial backlog.
     */
    public void setRepetitions(int reps) {
        this.reps = reps;
        ;
    }

    public PixelDataHandler(EventLogLoader ll, PixelsService pixelsService) {
        super("PixelDataHandler", "process");
        this.loader = ll;
        this.pixelsService = pixelsService;
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
     * Loads {@link #reps} {@link EventLog} instances and returns them.
     * This is the first phase used by the {@link PixelDataThread}. A later
     * phase will invoke {@link #handleEventLog(EventLog, Session, ServiceFactory)}
     * with the returned instance.
     */
    @Transactional(readOnly = false)
    public Object doWork(Session session, ServiceFactory sf) {
        List<EventLog> logs = new ArrayList<EventLog>();
        while (logs.size() < this.reps) {
            try {
                logs.add(loader.next());
            } catch (NoSuchElementException nsee) {
                if (!loader.hasNext()) {
                    break;
                };
            }
        }

        // Preload
        for (EventLog el : logs) {
            EventLog live = (EventLog) session.get(EventLog.class, el.getId());
            Event evt = live.getEvent();
            el.setEvent(evt);
        }

        return logs;
    }

    /**
     * Handles only single elements from the {@link PersistentEventLogLoader}
     * in order to keep transactions short and safe.
     */
    public void handleEventLog(EventLog eventLog, Session session, ServiceFactory sf) {
        final long start = System.currentTimeMillis();
        final boolean handled = process(eventLog.getEntityId(), sf, session);
        final String msg = String.format("EventLog:%s(entityId=%s) [%s ms.]",
                eventLog.getId(), eventLog.getEntityId(),
                (System.currentTimeMillis() - start));

        if (handled) {
            log.info("HANDLED "+ msg);
        } else {
            log.debug("SKIPPED "+ msg);
        }
    }

    /**
     * Here we assume that our log loader will only return
     * us the proper types, since we are using the specific
     * type defined in this package.
     *
     * @param id
     * @param sf
     * @param s
     * @return See above.
     */
    public boolean process(Long id, ServiceFactory sf, Session s) {

        final Pixels pixels = getPixels(id, sf);

        if (pixels == null) {
            log.error("No valid pixels found with id=" + id);
            return false;
        }

        try
        {
            StatsInfo[] statsInfo = pixelsService.makePyramid(pixels);
            if(statsInfo == null) {
                // Either exists or failed to be created, but that's
                // the PixelsService's business. It should throw an exception
                // if it wants us to more concretely handle any issues.
                log.debug("No min/max values for pixels " + id);
                return false;
            }

            for(int c=0;c<statsInfo.length;c++) {
                final StatsInfo si = statsInfo[c];
                final Channel ch = pixels.getChannel(c);
                long siId = getSqlAction().setStatsInfo(ch, si);
                log.info(String.format("Added StatsInfo:%s for %s - C:%s Max:%s Min:%s",
                        siId, ch, c, si.getGlobalMax(), si.getGlobalMin()));
            }
        } catch (LockTimeout lt) {
            log.warn("Pixels:" + id + " -- " + lt.getMessage());
            return false;
        } catch (Exception t) {
            log.error("Failed to handle pixels " + id, t);
            return false;
        }

        return true;
    }

    protected Pixels getPixels(Long id, ServiceFactory sf)
    {
        final IQuery iQuery = sf.getQueryService();
        final Pixels pixels = iQuery.findByQuery(
                "select p from Pixels as p " +
                "left outer join fetch p.channels ch " + // For statsinfo
                "join fetch p.pixelsType where p.id = :id ",
                new Parameters().addId(id));
        return pixels;
    }

}
