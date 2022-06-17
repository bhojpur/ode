package ode.services.eventlogs;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ode.model.meta.EventLog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread-safe java.util-like Container for storing {@link EventLog} instances
 * for later parsing. This container, however, will not add more than two
 * {@link EventLog logs} with the same (id, eventType, action) tuple.
 * 
 * Further, the container can only either be in the adding state or the removing
 * state. A newly created {@link EventBacklog} is in the adding state. The
 * popping state is entered the first time that {@link #remove()} is called. And
 * the adding state will only be re-entered, once {@link #remove()} has returned
 * null and {@link #logs} is empty.
 * 
 * All calls to {@link #add(EventLog)} while in the popping state will return
 * false.
 */
public class EventBacklog {

    final private static Logger logger = LoggerFactory.getLogger(EventBacklog.class);

    final Map<Long, Map<String, Set<String>>> contained = new HashMap<Long, Map<String, Set<String>>>();

    final List<EventLog> logs = new ArrayList<EventLog>();

    /**
     * Switch between the adding and the removing states.
     */
    protected boolean adding = true;

    /**
     * Adds the given {@link EventLog} instance to the end of a queue for later
     * {@link #remove() popping} if no equivalent {@link EventLog} is present.
     * Equivalence is based on the entityType, entityId, and action fields.
     * Records tracking information to prevent the same {@link EventLog} from
     * being re-added before the last instance is removed.
     */
    public synchronized boolean add(EventLog log) {

        if (!adding) {
            if (logger.isInfoEnabled()) {
                logger.info("Backlog locked:" + log.getEntityType() + ":Id_"
                        + log.getEntityId());
            }
            return false;
        }

        if (log == null || log.getEntityType() == null
                || log.getEntityId() == null || log.getAction() == null) {
            throw new IllegalArgumentException(
                    "EventLog must contain entityType, entityId, and action");
        }

        Map<String, Set<String>> class2action = contained
                .get(log.getEntityId());

        if (class2action == null) {
            class2action = new HashMap<String, Set<String>>();
            contained.put(log.getEntityId(), class2action);
        }

        Set<String> actions = class2action.get(log.getEntityType());
        if (actions == null) {
            actions = new HashSet<String>();
            class2action.put(log.getEntityType(), actions);
        }

        boolean contained = actions.contains(log.getAction());
        if (contained) {
            if (logger.isInfoEnabled()) {
                logger.info("Already in backlog:" + log.getEntityType()
                        + ":Id_" + log.getEntityId());
            }
            return false;
        } else {
            actions.add(log.getAction());
            logs.add(log);
            if (logger.isInfoEnabled()) {
                logger.info("Added to backlog:" + log.getEntityType() + ":Id_"
                        + log.getEntityId());
            }
            return true;
        }
    }

    /**
     * Removes and returns the next {@link EventLog} instance or null if none is
     * present. Also cleans up any tracking information for the given
     * {@link EventLog}.
     * 
     * @return See above.
     */
    public synchronized EventLog remove() {

        if (logs.size() == 0) {
            contained.clear();
            return null; // EARLY EXIT
        }

        adding = false;
        EventLog log = logs.remove(0);

        // None of these can be null as long as the log is still contained
        Map<String, Set<String>> class2action = contained
                .get(log.getEntityId());
        Set<String> actions = class2action.get(log.getEntityType());
        assert actions.remove(log.getAction());
        if (actions.size() == 0) {
            class2action.remove(log.getEntityType());
            if (class2action.size() == 0) {
                contained.remove(log.getEntityId());
            }
        }

        return log;
    }

    /**
     * Flips the {@link EventBacklog} to the "adding" state if it is empty,
     * otherwise to the "removing" state. This is necessary since the indexing
     * happens only at flush time in the
     * {@link ode.services.fulltext.FullTextIndexer}. This
     * restriction means in any one batch only either backlog or new event logs
     * will be processed.
     *
     * @see EventLogLoader#hasNext()
     */
    public synchronized void flipState() {
        adding = (logs.size() == 0);
    }

    /**
     * Check the current state of the {@link EventBacklog}. If in the
     * "removingOnly" state, then any calls to {@link #add(EventLog)} will
     * return false.
     */
    public synchronized boolean removingOnly() {
        return !adding;
    }
}
