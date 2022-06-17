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

import java.util.List;

import ode.api.local.LocalAdmin;
import ode.api.local.LocalQuery;
import ode.model.IObject;
import ode.model.containers.Dataset;
import ode.model.core.Image;
import ode.parameters.Parameters;
import ode.security.AdminAction;

/**
 * {@link AdminAction} which queries all {@link Dataset datasets} and
 * retrieve {@link Image images} linked to the datasets.
 * @see ode.api.IDelete
 */
public class QueryConstraints implements AdminAction {

    public final static String dsAllQuery = "select ds from Dataset ds join ds.imageLinks il "
            + " join il.child as img where img.id = :id";
    public final static String dsNotOwnQuery = "select ds from Dataset ds join ds.imageLinks il "
            + " join il.child as img where img.id = :id "
            + " and ds.details.owner.id != :owner"; // TODO what about links?

    final LocalAdmin iAdmin;
    final LocalQuery iQuery;
    final long id;
    final boolean force;
    final UnloadedCollector rv;
    final Parameters p;

    public QueryConstraints(LocalAdmin iAdmin, LocalQuery iQuery, long id,
            boolean force) {
        this.iAdmin = iAdmin;
        this.iQuery = iQuery;
        this.id = id;
        this.force = force;
        p = new Parameters().addId(id);
        rv = new UnloadedCollector(iQuery, iAdmin, false);
    }

    public void runAsAdmin() {

        String dsQuery;
        if (force) {
            dsQuery = dsNotOwnQuery;
            p.addLong("owner", iAdmin.getEventContext().getCurrentUserId());
        } else {
            dsQuery = dsAllQuery;
        }
        rv.addAll(iQuery.findAllByQuery(dsQuery, p));

        // TODO What about categories of other users?

    }

    public List<IObject> getResults() {
        return rv.list;
    }

}
