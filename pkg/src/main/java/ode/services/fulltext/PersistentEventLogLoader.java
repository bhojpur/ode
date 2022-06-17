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

import ode.conditions.InternalException;
import ode.model.IEnum;
import ode.model.meta.EventLog;
import ode.services.eventlogs.EventLogFailure;
import ode.services.eventlogs.EventLogLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;

/**
 * {@link EventLogLoader} implementation which keeps tracks of the last
 * {@link EventLog} instance, and always provides the next unindexed instance.
 * Resetting that saved value would restart indexing.
 */
@Deprecated
public class PersistentEventLogLoader extends ode.services.eventlogs.PersistentEventLogLoader {

    private final static Logger log = LoggerFactory
            .getLogger(PersistentEventLogLoader.class);

    /**
     * Called when the configuration database does not contain a valid
     * current_id. Used to index all the data which does not have an EventLog.
     */
    @Override
    public void initialize() {
        for (Class<IEnum> cls : types.getEnumerationTypes()) {
            for (IEnum e : queryService.findAll(cls, null)) {
                addEventLog(cls, e.getId());
            }
        }
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof EventLogFailure) {
            EventLogFailure failure = (EventLogFailure) event;
            if (failure.wasSource(this)) {
                String msg = "FullTextIndexer stuck! "
                    + "Failed to index EventLog: " + failure.log;
                log.error(msg, failure.throwable);
                rollback(failure.log);
                throw new InternalException(msg);
            }
        } else {
            super.onApplicationEvent(event);
        }
    }

}
