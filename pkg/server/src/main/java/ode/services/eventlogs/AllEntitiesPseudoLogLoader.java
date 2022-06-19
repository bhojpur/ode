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
import java.util.List;
import java.util.Set;

import ode.model.IObject;
import ode.model.meta.EventLog;
import ode.parameters.Parameters;

/**
 * {@link EventLogLoader} which iterates through each object model type (in no
 * particular order) and returns each object from lowest to highest id.
 */

public class AllEntitiesPseudoLogLoader<T extends IObject> extends
        EventLogLoader {

    List<String> classes;
    String current = null;
    long last = -1;

    public void setClasses(Set<String> classes) {
        this.classes = new ArrayList<String>(classes);
    }

    @Override
    protected EventLog query() {
        if (current == null) {
            if (more() == 0) {
                return null;
            }
            current = classes.remove(0);
        }

        final String query = String.format(
                "select obj from %s obj where obj.id > %d order by id asc",
                current, last);
        final IObject obj = queryService.findByQuery(query,
                new Parameters().page(0, 1));

        if (obj != null) {
            last = obj.getId();
            // Here we pass the string to prevent $$ CGLIB style issues
            return wrap(current, obj);
        } else {
            // If no object, then reset and recurse.
            current = null;
            last = -1;
            return query();
        }
    }

    /**
     * Returns the number of classes which are still unprocessed as a proxy for
     * how much is left to process.
     */
    @Override
    public long more() {
        return classes.size() + (current == null ? 0 : 1);
    }

    protected EventLog wrap(String cls, IObject obj) {
        EventLog el = new EventLog();
        el.setEntityType(cls);
        el.setEntityId(obj.getId());
        el.setAction("UPDATE");
        return el;
    }
}
