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
import ode.tools.hibernate.QueryBuilder;

import org.hibernate.HibernateException;
import org.hibernate.Session;

public class PojosGetImagesByOptionsQueryDefinition extends Query {

    static Definitions defs = new Definitions();

    public PojosGetImagesByOptionsQueryDefinition(Parameters parameters) {
        super(defs, parameters);
    }

    @Override
    protected void buildQuery(Session session) throws HibernateException,
            SQLException {

        // TODO copied from PojosGetImagesQueryDefinition. Should be merged.
        QueryBuilder qb = new QueryBuilder();
        qb.select("img");
        qb.from("Image", "img");
        qb.join("img.details.creationEvent", "ce", true, true);
        qb.join("img.details.updateEvent", "ue", true, true);
        qb.join("img.pixels", "pix", true, true);
        qb.join("img.format", "format", true, true);
        qb.join("pix.pixelsType", "pt", true, true);
        qb.join("img.annotationLinksCountPerOwner", "i_c_ann", true, true);
        // qb.join("img.datasetLinksCountPerOwner", "i_c_ds", true, true);

        if (params.isAcquisitionData()) {
	        qb.join("img.stageLabel", "position", true, true);
	        qb.join("img.imagingEnvironment", "condition", true, true);
	        qb.join("img.objectiveSettings", "os", true, true);
	        qb.join("os.medium", "me", true, true);
	        qb.join("os.objective", "objective", true, true);
	        qb.join("objective.immersion", "im", true, true);
	        qb.join("objective.correction", "co", true, true);
        }
        
        qb.where();

        // if PojoOptions sets START_TIME and/or END_TIME
        if (params.getStartTime() != null) {
            qb.and("img.details.creationEvent.time > :starttime");
            qb.param("starttime", params.getStartTime());
        }
        if (params.getEndTime() != null) {
            qb.and("img.details.creationEvent.time < :endtime");
            qb.param("endtime", params.getEndTime());
        }

        setQuery(qb.query(session));
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
