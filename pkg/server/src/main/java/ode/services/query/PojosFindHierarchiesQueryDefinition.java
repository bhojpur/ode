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

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import ode.model.containers.Dataset;
import ode.model.containers.Project;
import ode.model.core.Image;
import ode.parameters.Parameters;
import static ode.parameters.Parameters.*;

/**
 * walks up the hierarchy tree starting at {@link ode.model.core.Image} nodes
 * while fetching various information.
 * @see ode.api.IContainer#findContainerHierarchies(Class, java.util.Set, Parameters)
 */

public class PojosFindHierarchiesQueryDefinition extends
        AbstractClassIdsOptionsQuery {

    public PojosFindHierarchiesQueryDefinition(Parameters p) {
        super(p);
    }

    @Override
    protected void buildQuery(Session session) throws HibernateException,
            SQLException {
        Criteria c = session.createCriteria(Image.class);
        c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        c.add(Restrictions.in("id", (Collection) value(IDS)));
        Hierarchy.fetchParents(c, (Class) value(CLASS), Integer.MAX_VALUE);
        setCriteria(c);
    }

    @Override
    protected void enableFilters(Session session) {
        ownerOrGroupFilters(session,
                // TODO this needs to be moved to Hierarchy.
                // TODO these are also not all needed. Need to simplify.
                new String[] { Project.OWNER_FILTER,
                        Project.OWNER_FILTER_DATASETLINKS,
                        Dataset.OWNER_FILTER, Dataset.OWNER_FILTER_IMAGELINKS,
                        Dataset.OWNER_FILTER_PROJECTLINKS,
                        Image.OWNER_FILTER_DATASETLINKS,
                        }, new String[] {
                        Project.GROUP_FILTER,
                        Project.GROUP_FILTER_DATASETLINKS,
                        Dataset.GROUP_FILTER, Dataset.GROUP_FILTER_IMAGELINKS,
                        Dataset.GROUP_FILTER_PROJECTLINKS,
                        Image.GROUP_FILTER_DATASETLINKS
                        });
    }

}
// select i from Image i
// #bottomUpHierarchy()
// where
// #imagelist()
// #filters()
// #typeExperimenter()
