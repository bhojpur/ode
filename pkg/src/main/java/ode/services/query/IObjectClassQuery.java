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

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import ode.parameters.Parameters;
import ode.parameters.QueryParameter;

/**
 * simple query subclass which uses the {@link ode.parameters.Parameters#CLASS}
 * parameter value to create a {@link org.hibernate.Criteria} and then adds
 * {@link org.hibernate.criterion.Expression} instances based on all other
 * parameter names.
 * <p>
 * For example:
 * </p>
 * <code>
 *   Parameters p = new Parameters().addClass( Image.class )
 *   .addString( "name", "LT-3059");
 * </code>
 * <p>
 * produces a query of the form "select i from Image i where name = 'LT-3059'"
 * </p>
 */
public class IObjectClassQuery extends Query {

    static String CLASS = Parameters.CLASS;

    static Definitions defs = new Definitions(new QueryParameterDef(CLASS,
            Class.class, false));

    public IObjectClassQuery(Parameters parameters) {
        super(defs, parameters);
    }

    @Override
    protected void buildQuery(Session session) throws HibernateException,
            SQLException {
        Criteria c = session.createCriteria((Class) value(CLASS));
        for (QueryParameter qp : params.queryParameters()) {
            if (!qp.name.equals(CLASS)) {
                c.add(Restrictions.eq(qp.name, qp.value)); // TODO checks for
                                                            // type.
            }
        }
        setCriteria(c);
    }
}
