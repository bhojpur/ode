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

import org.hibernate.HibernateException;
import org.hibernate.Session;

import ode.parameters.Parameters;
import ode.tools.lsid.LsidUtils;

/**
 * counts the number of members in a collection. This query is used by the
 * {@link ode.api.IContainer IContainer} interface (possibly among others) to add
 * information to outgoing results.
 */
public class CollectionCountQueryDefinition extends Query {

    static Definitions defs = new Definitions(new IdsQueryParameterDef(),
            new QueryParameterDef("field", String.class, false));

    public CollectionCountQueryDefinition(Parameters parameters) {
        super(defs, parameters);
    }

    @Override
    protected void buildQuery(Session session) throws HibernateException,
            SQLException {
        String s_field = (String) value("field"); // TODO Generics??? if not
        // in arrays!
        String s_target = LsidUtils.parseType(s_field);
        String s_collection = LsidUtils.parseField(s_field);
        String s_query = String.format(
                "select target.id, count(collection) from %s target "
                        + "join target.%s collection "
                        + (check("ids") ? "where target.id in (:ids)" : "")
                        + "group by target.id", s_target, s_collection);

        org.hibernate.Query q = session.createQuery(s_query);
        if (check("ids")) {
            q.setParameterList("ids", (Collection) value("ids"));
        }
        setQuery(q);

    }

    // TODO filters...?
}
