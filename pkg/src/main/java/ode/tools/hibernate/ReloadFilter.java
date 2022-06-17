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

import java.util.HashMap;
import java.util.Map;

import ode.conditions.ApiUsageException;
import ode.model.IEnum;
import ode.model.IObject;
import ode.util.Filterable;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 * {@link UpdateFilter} subclass specialized for use with the
 * {@link Session#save(Object)} (as opposed to {@link Session#merge(Object)})
 * method. This is primarily of use during import when all objects are either
 * unloaded, enums, or newly created instances.
 */
public class ReloadFilter extends UpdateFilter {

    private final Session session;

    /**
     * Cache of all the enumerations which have been looked up via the session
     * in {@link #filter(String, Object)}.
     */
    private final Map<Class, Map<String, IEnum>> enumsMap = new HashMap<Class, Map<String, IEnum>>();

    public ReloadFilter(Session s) {
        this.session = s;
    }

    public Filterable filter(String fieldId, Filterable f) {

        // If the session already has this object load then we can move on.
        if (session.contains(f)) {
            return f;
        }

        if (f instanceof IObject) {

            // For the moment we are only worrying with IObjects.
            // The logic for handling Details, and other Filterable objects
            // should be the default.
            IObject o = (IObject) f;

            // If the object is unloaded, then we load it.
            if (!o.isLoaded()) {
                return (IObject) session.get(o.getClass(), o.getId());
            }

            // If the object is an enum, then we try to match based on value.
            if (f instanceof IEnum) {

                IEnum e = (IEnum) f;
                String val = e.getValue();

                // First, heck if in map.
                Map<String, IEnum> enums = enumsMap.get(f.getClass());
                if (enums != null) {
                    IEnum enu = enums.get(val);
                    if (enu != null) {
                        return enu;
                    }
                }

                // We haven't seen this enum yet. Try to load it, and if found
                // put it in enumsMap
                Query q = session.createQuery(String.format(
                        "select e from %s e where e.value = :val", f.getClass()
                                .getName()));
                q.setString("val", val);
                IEnum existing = (IEnum) q.uniqueResult();
                if (existing != null) {
                    enums = enumsMap.get(f.getClass());
                    if (enums == null) {
                        enums = new HashMap<String, IEnum>();
                        enumsMap.put(f.getClass(), enums);
                    }
                    enums.put(val, existing);
                    return existing;
                }
            }

            // Finally, if we've reached here, then this object is not an enum
            // and not unloaded. If it has an id, that means a user *might* be
            // trying to make an UPDATE, but these operations assume INSERT
            // only.
            if (o.getId() != null) {
                throw new ApiUsageException(
                        "INSERTs only! Pass only new objects, enums, or "
                                + "unloaded objects to this method.");
            }

        }

        return super.filter(fieldId, f);

    }
}