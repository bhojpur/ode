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
import java.util.Arrays;
import java.util.List;

import ode.model.meta.EventLog;
import ode.services.eventlogs.EventLogLoader;

/**
 * {@link EventLogLoader} implementation which keeps tracks of the last
 * {@link EventLog} instance, and always provides the next unindexed instance.
 * Resetting that saved value would restart indexing.
 */
public class PersistentEventLogLoader extends ode.services.eventlogs.PersistentEventLogLoader {

    protected final String repo;

    /**
     * The lowest entity id from a single dataPerUser set.
     */
    protected long lowestEntityId = -1;

    protected final int numThreads;

    protected List<long[]> dataPerUser = null;

    public PersistentEventLogLoader(String repo, int numThreads) {
        this.repo = repo;
        this.numThreads = numThreads;
    }

    @Override
    public void initialize() {
        // no-op
    }
    
    /**
     * Uses data from the {@link #dataPerUser} "queue" to allow new requests to
     * be processed even if one user adds a large number of PIXELDATA events.
     * Only the lowest event log id will be saved as the {@link #getCurrentId()}
     * meaning that some event logs will be processed multiple times. The call
     * to create the pyramid must properly ignore existing pyramids.
     */
    @Override
    protected EventLog query() {

        if (available()) {
            return pop();
        } else {
            final long current_id = getCurrentId();
            if (log.isDebugEnabled()) {
                log.debug(String.format(
                        "Locating next PIXELSDATA EventLog repo:%s > id:%d",
                        repo, current_id));
            }

            // Taking a multiple of the numThreads that takes a few as possible
            // from the second row.
            dataPerUser = new ArrayList<long[]>();
            List<long[]> tmp = sql.nextPixelsDataLogForRepo(repo, current_id, numThreads);

            while (tmp.size() > 0) {
                long[] data = tmp.remove(0);
                if (dataPerUser.size() < numThreads) {
                    dataPerUser.add(data); // If we haven't covered the threads, take it
                    log.debug("Data: " + Arrays.toString(data));
                } else if (data[3] == 1) {
                    dataPerUser.add(data); // If this is the first per user, take it.
                    log.debug("Data: " + Arrays.toString(data));
                } else {
                    log.debug("Skip: " + Arrays.toString(data));
                }
            }

            if (available()) {
                return pop();
            }
        }

        return null;
    }

    protected boolean available() {
        return dataPerUser != null && dataPerUser.size() > 0;
    }

    protected EventLog pop() {

        if (!available()) {
            throw new IllegalStateException();
        }

        long[] data = dataPerUser.remove(0);
        final long experimenter = data[0];
        final long eventLog = data[1];
        final long pixels = data[2];
        final long row = data[3];

        if (log.isDebugEnabled()) {
            log.debug(String.format("Handling pixels id:%d for user id:%d",
                    pixels, experimenter));
        }

        // Store the lowest of the entity ids.
        if (lowestEntityId < 0) {
            lowestEntityId = eventLog;
        } else {
            if (lowestEntityId > eventLog) {
                lowestEntityId = eventLog;
            }
        }

        // If we are finished, then save this as current id
        if (!available()) {
            dataPerUser = null;
            try {
                setCurrentId(lowestEntityId);
            } finally {
                lowestEntityId = -1;
            }
        }

        return queryService.get(EventLog.class, eventLog);

    }

}
