package ode.services.search;

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

import ode.api.IQuery;
import ode.model.IObject;
import ode.parameters.Parameters;
import ode.parameters.QueryParameter;
import ode.system.ServiceFactory;

import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

/**
 * Delegate to {@link IQuery#findAllByQuery(String, Parameters)}. Uses the
 * {@link SearchAction#chainedList} list to fill the named parameter ":IDLIST"
 * if present.
 */
public class HqlQuery extends SearchAction {

    private static final long serialVersionUID = 1L;

    private final String query;
    private final Parameters params;

    public HqlQuery(SearchValues values, String query, Parameters p) {
        super(values);
        if (query == null || query.length() < 1) {
            throw new IllegalArgumentException("Query string must be non-empty");
        }
        this.query = query;
        this.params = p;
    }

    @Transactional(readOnly = true)
    public Object doWork(Session session, ServiceFactory sf) {

        Parameters _p = this.params;

        // If contained then we need to handle it.
        if (this.query.contains("IDLIST")) {

            QueryParameter qp;

            // Initialize.
            if (_p == null) {
                _p = new Parameters();
                qp = null;
            } else {
                qp = _p.get("IDLIST");
            }

            if (qp != null) {
                // User set something specifically. Move along.
            } else {

                List<Long> ids = new ArrayList<Long>();

                if (this.chainedList == null || this.chainedList.size() == 0) {
                    // No results, but Hibernate cannot handle
                    // empty lists so we set this to a non-existant
                    // id.
                    ids.add(-1L);
                } else {
                    for (IObject obj : chainedList) {
                        if (obj != null) {
                            ids.add(obj.getId());
                        }
                    }
                }
                _p.addList("IDLIST", ids);

            }
        }
        return sf.getQueryService().findAllByQuery(query, _p);
    }
}
