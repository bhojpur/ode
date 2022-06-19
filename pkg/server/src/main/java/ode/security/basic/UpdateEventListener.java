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

// Third-party imports
import ode.model.IObject;
import ode.model.internal.Details;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.event.PreUpdateEvent;
import org.hibernate.event.PreUpdateEventListener;

/**
 * responsible for setting the
 * {@link Details#setUpdateEvent(ode.model.meta.Event) updat event} on all
 * events shortly before being saved.
 */
public class UpdateEventListener implements PreUpdateEventListener {

    public final static String UPDATE_EVENT = "UpdateEvent";

    private static final long serialVersionUID = -7607753637653567889L;

    private static Logger log = LoggerFactory.getLogger(UpdateEventListener.class);

    private final CurrentDetails cd;

    /**
     * main constructor. controls access to individual db rows..
     */
    public UpdateEventListener(CurrentDetails cd) {
        this.cd = cd;
    }

    /**
     * updates the update event field of an {@link IObject} instance.
     * 
     */
    public boolean onPreUpdate(PreUpdateEvent event) {
        Object entity = event.getEntity();
        if (entity instanceof IObject && !cd.isDisabled(UPDATE_EVENT)) {
            int[] dirty = event.getPersister().findDirty(event.getState(),
                    event.getOldState(), event.getEntity(), event.getSession());
            if (dirty == null || dirty.length == 0) {
                // return true; // veto.
            }

            else {
                // otherwise change update event (last modification)
                IObject obj = (IObject) entity;
                obj.getDetails().setUpdateEvent(cd.getEvent());
            }
        }
        return false;
    }

}