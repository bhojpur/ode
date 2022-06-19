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

// Java imports

// Third-party libraries

// Application-internal dependencies
import ode.parameters.Parameters;

/**
 * contract for any source of {@link ode.services.query.Query queries}.
 * Instances should be registered with the
 * {@link ode.services.query.QueryFactory} in the Spring configuration.
 * 
 * QuerySources are responsible for mapping the given query ID to a Query
 * instance (possibly dependent on the {@link ode.parameters.Parameters}). The
 * order of sources provided to {@link ode.services.query.QueryFactory} is very
 * important.
 * 
 * QuerySources can use any mechanism available to perform the lookup, e.g. a
 * database backend, flat-files, the set of Hibernate named-queries, or concrete
 * classes (see {@link ode.services.query.ClassQuerySource}).
 */
public abstract class QuerySource {

    /**
     * map the queryID argument to some Query instance (including null). This
     * mapping can be dependent on the provided {@link Parameters}
     * 
     * @param <T>
     *            the generic type of the return Query. This is usually provided
     *            indirectly through the type assignment, e.g. "Query<List> q =
     *            ... "
     * @param queryID
     *            abstract identifier for the sought query.
     * @param parameters
     *            named parameters for lookup and actual bindings.
     * @return A possible null Query for later execution.
     */
    public abstract <T> Query<T> lookup(String queryID, Parameters parameters);
}
