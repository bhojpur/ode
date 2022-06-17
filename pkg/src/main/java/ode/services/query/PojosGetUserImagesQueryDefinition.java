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

import ode.model.core.Image;
import ode.parameters.Parameters;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class PojosGetUserImagesQueryDefinition extends Query {

    static Definitions defs = new Definitions();

    public PojosGetUserImagesQueryDefinition(Parameters parameters) {
        super(defs, parameters);
    }

    @Override
    protected void buildQuery(Session session) throws HibernateException,
            SQLException {
        // TODO copied from PojosGetImages refactor
        Criteria c = session.createCriteria(Image.class);
        c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        Criteria pix = c.createCriteria("pixels", LEFT_JOIN);
        pix.createCriteria("pixelsType", LEFT_JOIN);
        //pix.createCriteria("pixelsDimensions", LEFT_JOIN);
        // endTODO

        setCriteria(c);
    }

    @Override
    protected void enableFilters(Session session) {
        ownerOrGroupFilters(session, new String[] { Image.OWNER_FILTER },
                new String[] { Image.GROUP_FILTER });
    }

}
// select i from Image i
// #bottomUpHierarchy()
// where
// #imagelist()
// #filters()
// #typeExperimenter()
