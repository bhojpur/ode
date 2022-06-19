package ode.services.delete;

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
import java.util.List;
import java.util.Map;

import ode.api.local.LocalAdmin;
import ode.api.local.LocalQuery;
import ode.model.IObject;
import ode.util.CBlock;
import ode.util.Utils;

import org.hibernate.Session;

/**
 * {@link CBlock} implementation which counts the number of locking instances
 * there are for a single {@link IObject} while walking a graph.
 * @see ode.api.IDelete
 */
class UnloadedCollector implements CBlock {

    final protected boolean count;
    final protected LocalQuery query;
    final protected LocalAdmin admin;
    final List<IObject> list = new ArrayList<IObject>();
    final Map<String, Map<Long, Map<String, Long>>> map = new HashMap<String, Map<Long, Map<String, Long>>>();

    public UnloadedCollector(LocalQuery query, LocalAdmin admin, boolean count) {
        this.query = query;
        this.admin = admin;
        this.count = count;
    }

    public void addAll(List<IObject> list) {
        for (IObject object : list) {
            call(object);
        }
    }

    public Object call(IObject object) {

        if (object == null) {
            return null;
        }

        IObject copy = (IObject) Utils.trueInstance(object.getClass());
        copy.setId(object.getId());
        copy.unload();
        list.add(copy);
        if (count) {
            count(object); /* PERFORMANCE HIT */
        }
        return null;
    }

    /**
     * Counts via {@link LocalAdmin#getLockingIds(Session, IObject)} all the
     * items which entities which link to the given object.
     *
     * @param object
     */
    @SuppressWarnings("unchecked")
    void count(final IObject object) {

        Map<Long, Map<String, Long>> id_class_id = map.get(object.getClass()
                .getName());

        if (id_class_id == null) {
            id_class_id = new HashMap<Long, Map<String, Long>>();
            map.put(object.getClass().getName(), id_class_id);
        }

        if (!id_class_id.containsKey(object.getId())) {
            id_class_id.put(object.getId(), admin.getLockingIds(
                    (Class<IObject>) object.getClass(), object.getId(), null));
        }

    }
}
