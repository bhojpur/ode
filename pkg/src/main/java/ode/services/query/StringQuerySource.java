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
import ode.parameters.Parameters;
import ode.util.SqlAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * interprets the query id as an HQL query. In this implementation, no parsing
 * is done at lookup or creation-time, but an implementation which does so is
 * conceivable. The id itself is added to the list of parameters with the name
 * {@link ode.services.query.StringQuery#STRING}
 * 
 * This query source should be placed at the end of the array of query sources
 * provided to {@link ode.services.query.QueryFactory} because it will always
 * return a {@link ode.services.query.Query} regardless of the id. An exception
 * will be thrown at execution time if the HQL is invalid.
 */
public class StringQuerySource extends QuerySource {

    private static Logger log = LoggerFactory.getLogger(StringQuerySource.class);

    private final SqlAction sql;

    /**
     * Default constructor, used primarily for testing.
     * Passes as null {@link SqlAction} to created {@link StringQuery}
     * instances.
     */
    public StringQuerySource() {
        this(null);
    }

    public StringQuerySource(SqlAction sql) {
        this.sql = sql;
    }

    @Override
    public Query lookup(String queryID, Parameters parameters) {
        Parameters p = new Parameters(parameters);
        p.addString(StringQuery.STRING, queryID);
        return new StringQuery(sql, p);
    }

}
