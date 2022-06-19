package ode.services.query;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ode.conditions.ApiUsageException;
import ode.parameters.Parameters;

/**
 * query locator which is configured by Spring. A QueryFactory instance is
 * created in the ode/services/services.xml Spring config and is injected with
 * multiple {@link ode.services.query.QuerySource "query sources"}. The lookup
 * proceeds through the available query sources calling
 * {@link ode.services.query.QuerySource#lookup(String, Parameters) querySource.lookup()},
 * and returns the first non-null result. If no result is found, an exception is
 * thrown.
 */
public class QueryFactory {

    private static Logger log = LoggerFactory.getLogger(QueryFactory.class);

    /**
     * sources available for lookups. This array will never be null.
     */
    protected QuerySource[] sources;

    private QueryFactory() {
    }; // We need the sources

    /**
     * main constructor which takes a non-null array of query sources as its
     * only argument. This array is copied, so modifications will not be
     * noticed.
     * 
     * @param querySources
     *            Array of query sources. Not null.
     */
    public QueryFactory(QuerySource... querySources) {
        if (querySources == null || querySources.length == 0) {
            throw new ApiUsageException(
                    "QuerySource[] argument to QueryFactory constructor "
                            + "may not be null or empty.");
        }
        int size = querySources.length;
        this.sources = new QuerySource[size];
        System.arraycopy(querySources, 0, this.sources, 0, size);
    }

    /**
     * 
     * @param <T>
     * @param queryID
     * @param params
     * @return See above.
     */
    public <T> Query<T> lookup(String queryID, Parameters params) {
        Query<T> q = null;

        for (QuerySource source : sources) {
            q = source.lookup(queryID, params);
            if (q != null) {
                break;
            }
        }

        if (q == null) {
            throw new QueryException("No query found for queryID=" + queryID);
        }

        return q;

    }

}
