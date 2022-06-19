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
import java.lang.reflect.Constructor;
// Third-party libraries
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// Application-internal dependencies
import ode.model.IObject;
import ode.parameters.Parameters;

/**
 * creates a query based on the id string by interpreting it as a Class. The
 * class can either be a {@link ode.services.query.Query} implementation or an
 * {@link ode.model.IObject} implementation.
 * 
 * <p>
 * If it is an {@link ode.model.IObject} implementation, the
 * {@link ode.parameters.QueryParameter} instances passed in through
 * {@link Parameters} are interpreted as being field names whose
 * {@link ode.parameters.QueryParameter#value values} should equals the value in
 * the database.
 * </p>
 * 
 * <p>
 * If it is an {@link ode.services.query.Query} implementation, then it is
 * instantiated by passing the {@link ode.parameters.Parameters} into the
 * constructor.
 * </p>
 * 
 * @see ode.services.query.IObjectClassQuery
 */
public class ClassQuerySource extends QuerySource {

    private static Logger log = LoggerFactory.getLogger(ClassQuerySource.class);

    @Override
    public Query lookup(String queryID, Parameters parameters) {
        Query q = null;
        Class klass = null;
        try {
            klass = Class.forName(queryID);
        } catch (ClassNotFoundException e) {
            // Not an issue.
        }

        // return null immediately
        if (klass == null) {
            return null;
        }

        // it's a query
        else if (Query.class.isAssignableFrom(klass)) {
            try {
                Constructor c = klass.getConstructor(Parameters.class);
                q = (Query) c.newInstance(parameters);
            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    log.debug("Query could not be instanced.", e.getCause());
                }
                throw new RuntimeException("Error while trying to instantiate:"
                        + queryID, e);
            }
            return q;
        }

        // it's an IObject
        else if (IObject.class.isAssignableFrom(klass)) {
            Parameters p = new Parameters(parameters);
            p.addClass(klass);
            return new IObjectClassQuery(p);
        }

        else {
            return null;
        }
    }

}
