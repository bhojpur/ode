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

import static ode.parameters.Parameters.CLASS;
import static ode.parameters.Parameters.IDS;

import java.sql.SQLException;
import java.util.Collection;

import ode.parameters.Parameters;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class PojosFindAnnotationsQueryDefinition extends Query {

    static Definitions defs = new Definitions(new IdsQueryParameterDef(),
            new ClassQueryParameterDef(),
            new QueryParameterDef("annotatorIds", Collection.class, true));

    public PojosFindAnnotationsQueryDefinition(Parameters parameters) {
        super(defs, parameters);
        // TODO set local fields here.
    }

    @Override
    protected void buildQuery(Session session) throws HibernateException,
            SQLException {

        Class k = (Class) value(CLASS);

        Criteria obj = session.createCriteria(k);
        // TODO refactor into CriteriaUtils
        // Not currently loading, since IContainer.findAnnotations, rearranges
        // them to return the annotations, which have no links to their
        // owning objects
        // obj.setFetchMode("details.owner", FetchMode.JOIN);
        // obj.setFetchMode("details.group", FetchMode.JOIN);
        // obj.setFetchMode("details.creationEvent", FetchMode.JOIN);
        // obj.setFetchMode("details.updateEvent", FetchMode.JOIN);
        Collection ids = (Collection) value(IDS);
        if (ids != null && ids.size() > 0)
        	obj.add(Restrictions.in("id", ids));

        // Here we do want the left join, so the consumer will see
        // that there's an empty set
        Criteria links = obj.createCriteria("annotationLinks", "links",
                LEFT_JOIN);
        Criteria ann = links.createCriteria("child", LEFT_JOIN);
        Criteria ann_owner = ann.createAlias("details.owner", "ann_owner",
                LEFT_JOIN);
        Criteria ann_create = ann.createAlias("details.creationEvent",
                "ann_create", LEFT_JOIN);
        Criteria ann_file = ann.createAlias("file",
                "ann_file", LEFT_JOIN);

        obj.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        if (check("annotatorIds")) {
            Collection annotatorIds = (Collection) value("annotatorIds");
            if (annotatorIds != null && annotatorIds.size() > 0) {
                ann.add(Restrictions.in("details.owner.id", annotatorIds));
            }
        }
        setCriteria(obj);
    }
}
