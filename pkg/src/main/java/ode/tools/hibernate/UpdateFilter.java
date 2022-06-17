package ode.tools.hibernate;

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

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.hibernate.Hibernate;

import ode.model.IObject;
import ode.model.internal.Permissions;
import ode.util.ContextFilter;
import ode.util.Filterable;

/**
 * responsible for correlating entity identities during multiple calls to merge.
 * This occurs when {@link Collection collections} or arrays are passed into the
 * {@link ode.logic.UpdateImpl} save methods.
 */
public class UpdateFilter extends ContextFilter {

    /**
     * provides an external hook to unload all files which have already been
     * merged.
     * <p>
     * Merging produces a copy of an entity, so that all old entities should be
     * considered stale. By unloading them, one is forcing the API user to use
     * the {@link ode.model.internal.GraphHolder#getReplacement() replacement}
     * instead.
     * </p>
     * <p>
     * The replacement is set by {@link ode.security.basic.MergeEventListener} and this is the
     * signal that that entity can be unloaded. Usually, this method is invoked
     * by {@link ode.logic.UpdateImpl}
     * </p>
     * 
     * @see ode.security.basic.MergeEventListener
     * @see ode.logic.UpdateImpl
     * @see IObject#unload()
     */
    public void unloadReplacedObjects() {
        for (Object obj : _cache.keySet()) {
            if (hasReplacement(obj)) {
                ((IObject) obj).unload();
            }
        }
    }

    /**
     * overrides {@link ContextFilter#filter(String, Object)} to allow only
     * certain types to enter the Hibernate system
     */
    @Override
    public Object filter(String fieldId, Object o) {

        if (alreadySeen(o)) {
            return returnSeen(o);
        }

        Object result;

        if (o == null) {
            return null;
        } else if (o instanceof Enum) {
            result = o; // 5.1: Enums are unfiltered
        } else if (o instanceof ode.model.internal.Primitive) {
            result = o; // 5.1: units and named values as well
        } else if (o instanceof Filterable) {
            result = filter(fieldId, (Filterable) o);
        } else if (o instanceof Collection) {
            result = filter(fieldId, (Collection) o);
        } else if (o instanceof Map) {
            result = filter(fieldId, (Map) o);
        } else if (o instanceof Number || o instanceof String
                || o instanceof Date || o instanceof Boolean
                || o instanceof Permissions || o instanceof byte[]
                || o instanceof String[] || o instanceof String[][]) {
            result = o;
        } else {
            throw new RuntimeException(
                    "Update Filter cannot allow unknown types to be saved."
                            + o.getClass().getName()
                            + " is not in {IObject,Collection,Map}");
        }
        return result;
    }

    /**
     * overrides {@link ContextFilter#filter(String, Filterable)} to return
     * previously merged or previously checked items.
     */
    @Override
    public Filterable filter(String fieldId, Filterable f) {
        if (alreadySeen(f)) {
            return (Filterable) returnSeen(f);
        }

        return super.filter(fieldId, f);

    }

    /**
     * overrides {@link ContextFilter#filter(String, Collection)} to return
     * previously checked {@link Collection collections}.
     */
    @Override
    public Collection filter(String fieldId, Collection c) {

        if (alreadySeen(c)) {
            return (Collection) returnSeen(c);
        }

        return super.filter(fieldId, c);

    }

    /**
     * Prevents CountPerOwner from being loaded unnecessarily.
     */
    @Override
    public Map filter(String fieldId, Map m) {
        if (fieldId != null && fieldId.endsWith("CountPerOwner")) {
            beforeFilter(fieldId, m);
            afterFilter(fieldId, m);
            return m;
        }
        return super.filter(fieldId, m);
    }

    // Helpers
    // =======================================================

    protected boolean hasReplacement(Object o) {
        if (o instanceof IObject) {
            IObject obj = (IObject) o;
            if (obj.getGraphHolder().getReplacement() != null) {
                return true;
            }
        }
        return false;
    }

    protected boolean alreadySeen(Object o) {
        if (o == null) {
            return false;
        }
        if (!Hibernate.isInitialized(o)) {
            return true;
        }
        return hasReplacement(o) ? true : _cache.containsKey(o);
    }

    protected Object returnSeen(Object o) {
        if (o == null) {
            return null;
        }
        if (!Hibernate.isInitialized(o)) {
            return o;
        }
        if (hasReplacement(o)) {
            IObject obj = (IObject) o;
            IObject replacement = obj.getGraphHolder().getReplacement();
            obj.unload();
            return replacement;
        }
        return o;
    }

}