package ode.security;

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
import java.util.Set;

import ode.conditions.SecurityViolation;
import ode.model.IObject;
import ode.tools.hibernate.HibernateUtils;

import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostLoadEvent;
import org.hibernate.event.PostLoadEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.hibernate.event.PreDeleteEvent;
import org.hibernate.event.PreDeleteEventListener;
import org.hibernate.event.PreInsertEvent;
import org.hibernate.event.PreInsertEventListener;
import org.hibernate.event.PreLoadEvent;
import org.hibernate.event.PreLoadEventListener;
import org.hibernate.event.PreUpdateEvent;
import org.hibernate.event.PreUpdateEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * responsible for intercepting all pre-INSERT, pre-UPDATE, pre-DELETE, and
 * post-LOAD events to apply access control. For each event, a call is made to
 * the {@link SecuritySystem} to see if the event is allowed, and if not,
 * another call is made to the {@link  SecuritySystem} to throw a
 * {@link SecurityViolation}.
 */
public class ACLEventListener implements
/* BEFORE... */PreDeleteEventListener, PreInsertEventListener,
/* and...... */PreLoadEventListener, PreUpdateEventListener,
/* AFTER.... */PostDeleteEventListener, PostInsertEventListener,
/* TRIGGERS. */PostLoadEventListener, PostUpdateEventListener {

    private static final long serialVersionUID = 3603644089117965153L;

    private static Logger log = LoggerFactory.getLogger(ACLEventListener.class);

    private final ACLVoter aclVoter;

    /**
     * main constructor. controls access to individual db rows..
     */
    public ACLEventListener(ACLVoter aclVoter) {
        this.aclVoter = aclVoter;
    }

    // 
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Acting as all hibernate triggers
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //

    /** unused */
    public void onPostDelete(PostDeleteEvent event) {
    }

    /** unused */
    public void onPostInsert(PostInsertEvent event) {
    }

    /** unused */
    public void onPostUpdate(PostUpdateEvent event) {
    }

    /** unused */
    public void onPreLoad(PreLoadEvent event) {
    }

    /**
     * catches all load events after the fact, and tests the current users
     * permissions to read that object. We have to catch the load after the fact
     * because the permissions information is stored in the db.
     */
    public void onPostLoad(PostLoadEvent event) {
        Object entity = event.getEntity();
        if (entity instanceof IObject) {
            IObject o = (IObject) entity;
            if (!aclVoter.allowLoad(event.getSession(), o.getClass(), o.getDetails(), o.getId())) {
                aclVoter.throwLoadViolation(o);
            }
            Set<String> restrictions = aclVoter.restrictions(o);
            ((IObject) entity).getDetails().getPermissions()
                .addExtendedRestrictions(restrictions);
        }
    }

    public boolean onPreInsert(PreInsertEvent event) {
        Object entity = event.getEntity();
        if (entity instanceof IObject) {
            IObject obj = (IObject) entity;
            if (!aclVoter.allowCreation(obj)) {
                aclVoter.throwCreationViolation(obj);
            }
        }
        return false;
    }

    public boolean onPreUpdate(PreUpdateEvent event) {
        Object entity = event.getEntity();
        Object[] state = event.getOldState();
        String[] names = event.getPersister().getPropertyNames();
        if (entity instanceof IObject) {
            IObject obj = (IObject) entity;

            if (!aclVoter.allowUpdate(obj,
                    HibernateUtils.getDetails(state, names))) {
                aclVoter.throwUpdateViolation(obj);
            }
        }
        return false;
    }

    public boolean onPreDelete(PreDeleteEvent event) {
        Object entity = event.getEntity();
        Object[] state = event.getDeletedState();
        String[] names = event.getPersister().getPropertyNames();
        if (entity instanceof IObject) {
            IObject obj = (IObject) entity;
            if (!aclVoter.allowDelete(obj, HibernateUtils.getDetails(state,
                    names))) {
                aclVoter.throwDeleteViolation(obj);
            }
        }
        return false;
    }

}