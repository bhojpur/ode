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

// Java imports

// Third-party imports
import ode.model.IObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;

/**
 * responsible for responding to all Hibernate Events. Delegates tasks to
 * various components. It is assumed that graphs coming to the Hibernate methods
 * which produces these events have already been processed by the
 * {@link ode.tools.hibernate.UpdateFilter}
 */
public class EventLogListener implements PostUpdateEventListener,
        PostDeleteEventListener, PostInsertEventListener {

    private static final long serialVersionUID = 3245068515908082533L;

    private static Logger log = LoggerFactory.getLogger(EventLogListener.class);

    protected final CurrentDetails cd;

    /**
     * main constructor.
     */
    public EventLogListener(CurrentDetails cd) {
        this.cd = cd;
    }

    // 
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Acting as all hibernate triggers
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //

    public void onPostDelete(PostDeleteEvent event) {
        add("DELETE", event.getEntity());
    }

    public void onPostInsert(PostInsertEvent event) {
        add("INSERT", event.getEntity());
    }

    public void onPostUpdate(PostUpdateEvent event) {
        add("UPDATE", event.getEntity());
    }

    // ~ Helpers
    // =========================================================================

    void add(String action, Object entity) {
        if (entity instanceof IObject) {
            Class klass = entity.getClass();
            Long id = ((IObject) entity).getId();
            cd.addLog(action, klass, id);
        }
    }

}