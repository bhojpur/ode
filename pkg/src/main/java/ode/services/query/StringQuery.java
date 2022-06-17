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

import java.sql.SQLException;
import java.util.Collection;

import ode.conditions.ApiUsageException;
import ode.parameters.Parameters;
import ode.util.SqlAction;

import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * simple HQL query. Parameters are added as named parameters ({@link org.hibernate.Query#setParameter(java.lang.String, java.lang.Object)}.
 * Parameters with a value of type {@link Collection} are added as
 * {@link org.hibernate.Query#setParameterList(java.lang.String, java.util.Collection)}
 * 
 * No parsing is done until execution time.
 */
public class StringQuery extends Query {

    /**
     * parameter name for the definition of the HQL string.
     */
    public final static String STRING = "::string::";

    static Definitions defs = new Definitions(new QueryParameterDef(STRING,
            String.class, false));

    private final SqlAction sql;

    /**
     * Default constructor, used primarily for testing.
     * Leaves {@link SqlAction} field null preventing
     * query rewriting.
     */
    public StringQuery(Parameters parameters) {
        this(null, parameters);
    }

    public StringQuery(SqlAction sql, Parameters parameters) {
        super(defs, parameters);
        this.sql = sql;
    }

    @Override
    protected void buildQuery(Session session) throws HibernateException,
            SQLException {

        String queryString = (String) value(STRING);
        if (sql != null) {
            for (String key : params.keySet()) {
                if (STRING.equals(key)) {
                    continue; // Skip ::string: since its what it is.
                }
                queryString = sql.rewriteHql(queryString, key, value(key));
            }
        }

        org.hibernate.Query query;
        try {
            query = session.createQuery(queryString);
        } catch (Exception e) {
            // Caused by a query parser error in Hibernate.
            throw new QueryException("Illegal query:" + value(STRING) + "\n"
                    + e.getMessage());
        }
        String[] nParams = query.getNamedParameters();
        for (int i = 0; i < nParams.length; i++) {
            String p = nParams[i];
            Object v = value(p);
            if (v == null) {
                throw new ApiUsageException("Null parameters not allowed: " + p);
            }
            if (Collection.class.isAssignableFrom(v.getClass())) {
                query.setParameterList(p, (Collection) v);
            } else {
                query.setParameter(p, v);
            }
        }

        setQuery(query);
    }
}
