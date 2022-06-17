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

import ode.model.IObject;
import ode.model.annotations.TagAnnotation;
import ode.services.SearchBean;
import ode.system.ServiceFactory;
import ode.tools.hibernate.QueryBuilder;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

/**
 * Query template used by {@link SearchBean} to store user requests.
 */
public class TagsAndGroups extends SearchAction {

    private static final long serialVersionUID = 1L;

    private final String str;

    private final boolean byTagForGroups;

    public TagsAndGroups(SearchValues values, String query,
            boolean byTagForGroups) {
        super(values);
        this.byTagForGroups = byTagForGroups;
        if (query == null || query.length() < 1) {
            str = null;
        } else {
            this.str = query;
        }
    }

    @Transactional(readOnly = true)
    public Object doWork(Session session, ServiceFactory sf) {

        // Ignore:
        // values.onlyTypes
        // annotatedWith
        // onlyIds

        QueryBuilder qb = new QueryBuilder(128);
        qb.select("target.textValue");
        qb.from("AnnotationAnnotationLink", "link");
        if (byTagForGroups) {
            qb.join("link.parent", "source", false, false);
            qb.join("link.child", "target", false, false);
        } else {
            qb.join("link.parent", "target", false, false);
            qb.join("link.child", "source", false, false);
        }
        qb.where();
        qb.and("source.class = TagAnnotation");
        qb.and("target.class = TagAnnotation");
        if (str != null) {
            qb.and("source.textValue = :str");
            qb.param("str", str);
        }

        ownerOrGroup(TagAnnotation.class, qb, "source.");
        createdOrModified(TagAnnotation.class, qb, "source.");
        if (byTagForGroups) {
            annotatedBy(qb, "source.");
            annotatedBetween(qb, "source.");
        } else {
            annotatedBy(qb, "target.");
            annotatedBetween(qb, "target.");
        }

        Query query = qb.query(session);
        if (timeout != null) {
            query.setTimeout(timeout);
        }

        List<IObject> rv = new ArrayList<IObject>();
        List<String> tags = query.list();
        for (String tag : tags) {
            TagAnnotation ta = new TagAnnotation();
            ta.setTextValue(tag);
            rv.add(ta);
        }
        return rv;
    }
}
