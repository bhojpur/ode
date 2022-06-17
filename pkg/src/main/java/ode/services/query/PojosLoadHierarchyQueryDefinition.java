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

import ode.conditions.ApiUsageException;
import ode.model.containers.Dataset;
import ode.model.containers.Project;
import ode.model.screen.Plate;
import ode.model.screen.Screen;
import ode.parameters.Parameters;

import org.hibernate.HibernateException;
import org.hibernate.Session;

public class PojosLoadHierarchyQueryDefinition extends Query {

    static Definitions defs = new Definitions(new CollectionQueryParameterDef(
            Parameters.IDS, true, Long.class),
            new ClassQueryParameterDef());

    public PojosLoadHierarchyQueryDefinition(Parameters parameters) {
        super(defs, parameters);
    }

    @Override
    protected void buildQuery(Session session) throws HibernateException,
            SQLException {

        Class klass = (Class) value(CLASS);

        StringBuilder sb = new StringBuilder();
        if (Project.class.isAssignableFrom(klass)) {
            sb.append("select this from Project this ");
            sb.append("left outer join fetch this.details.creationEvent ");
            sb.append("left outer join fetch this.datasetLinks pdl ");
            sb.append("left outer join fetch pdl.child ds ");
            if (params.isLeaves()) {
                sb.append("left outer join fetch ds.imageLinks dil ");
                sb.append("left outer join fetch dil.child img ");
            }
            sb.append("left outer join fetch "
                    + "this.annotationLinksCountPerOwner this_a_c ");
        } else if (Dataset.class.isAssignableFrom(klass)) {
            sb.append("select this from Dataset this ");
            sb.append("left outer join fetch this.details.creationEvent ");
            if (params.isLeaves()) {
                sb.append("left outer join fetch this.imageLinks dil ");
                sb.append("left outer join fetch dil.child img ");
            }
            sb.append("left outer join fetch "
                    + "this.annotationLinksCountPerOwner this_a_c ");
            sb.append("left outer join fetch "
                    + "this.imageLinksCountPerOwner this_i_c ");
        } else if (Screen.class.isAssignableFrom(klass)) {
        	sb.append("select this from Screen this ");
        	sb.append("left outer join fetch this.details.creationEvent ");
            sb.append("left outer join fetch this.plateLinks pdl ");
            sb.append("left outer join fetch pdl.child ds ");
            sb.append("left outer join fetch ds.details.creationEvent ");
            sb.append("left outer join fetch ds.details.updateEvent ");
            sb.append("left outer join fetch ds.plateAcquisitions sa ");
            sb.append("left outer join fetch "
                    + "this.annotationLinksCountPerOwner this_a_c ");
            sb.append("left outer join fetch sa.annotationLinksCountPerOwner sa_a_c ");
        } else if (Plate.class.isAssignableFrom(klass)) {
        	sb.append("select this from Plate this ");
        	sb.append("left outer join fetch this.details.creationEvent ");
        	sb.append("left outer join fetch this.plateAcquisitions sa ");
            sb.append("left outer join fetch "
                    + "this.annotationLinksCountPerOwner this_a_c ");
            sb.append("left outer join fetch sa.annotationLinksCountPerOwner sa_a_c ");
        } else {
            throw new ApiUsageException("Unknown container class: "
                    + klass.getName());
        }

        if (params.isLeaves()) {
            if (Screen.class.isAssignableFrom(klass) || Plate.class.isAssignableFrom(klass)) {
                sb.append("left outer join fetch sa.wellSample ws ");
                sb.append("left outer join fetch ws.image img ");
            }
            sb.append("left outer join fetch img.details.creationEvent as cre ");
            sb.append("left outer join fetch img.details.updateEvent as evt ");
            sb.append("left outer join fetch img.pixels as pix ");
            sb.append("left outer join fetch img.format as format ");
            sb.append("left outer join fetch pix.pixelsType as pt ");
            if (params.isAcquisitionData()) {
	            sb.append("left outer join fetch img.stageLabel as position ");
	            sb.append("left outer join fetch img.imagingEnvironment" +
	            		" as condition ");
	            sb.append("left outer join fetch img.objectiveSettings as os ");
	            sb.append("left outer join fetch os.medium as me ");
	            sb.append("left outer join fetch os.objective as objective ");
	            sb.append("left outer join fetch objective.immersion as im ");
	            sb.append("left outer join fetch objective.correction as co ");
            }
        }

        // optional ids
        Collection ids = (Collection) value(IDS);
        if (ids != null && ids.size() > 0) {
            sb.append("where this.id in (:ids)");
        }

        org.hibernate.Query q = session.createQuery(sb.toString());
        if (ids != null && ids.size() > 0) {
            q.setParameterList("ids", ids);
        }
        setQuery(q);
    }

    @Override
    protected void enableFilters(Session session) {
        ownerOrGroupFilters(session,
        // TODO this needs to be moved to Hierarchy.
                new String[] { 
        				Project.OWNER_FILTER, Dataset.OWNER_FILTER, 
        				Screen.OWNER_FILTER, Plate.OWNER_FILTER }, 
                new String[] { 
        				Project.GROUP_FILTER, Dataset.GROUP_FILTER, 
        		        Screen.GROUP_FILTER, Plate.GROUP_FILTER });
    }

}
