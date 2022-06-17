package ode.api.local;

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

import org.springframework.orm.hibernate3.HibernateCallback;

import ode.services.query.Query;

/**
 * Provides local (internal) extensions for querying
 */
public interface LocalQuery extends ode.api.IQuery {

    /**
     * creates and returns a convenience Dao which provides generics despite the
     * &lt; Java5 requirement on {@link ode.api.IQuery}.
     * 
     * @param <T>
     * @return
     */
    // TODO <T extends IObject> Dao<T> getDao();
    /**
     * Executes a {@link HibernateCallback}
     * 
     * @param callback
     *            An implementation of the HibernateCallback interface.
     * 
     * @see org.springframework.orm.hibernate3.HibernateTemplate
     * @see org.springframework.orm.hibernate3.HibernateCallback
     */
    <T> T execute(HibernateCallback callback);

    /**
     * Executes a locally defined Query.
     * 
     * @param query
     *            A subclass of the {@link Query} interface.
     * @return result of the query See document for the query for the return
     *         type.
     */
    <T> T execute(Query<T> query);

    /**
     * Tests if an object is currently contained in the session.
     * 
     * @param object
     */
    boolean contains(Object object);

    /**
     * Removes an object graph from the session. This allows for non-permanent,
     * mutable calls on the graph.
     * 
     * @param object
     */
    void evict(Object object);

	void clear();

    /**
     * Uses the Hibernate static method <code>initialize</code> to prepare an
     * object for shipping over the wire.
     * 
     * It is better to do this in your queries.
     * 
     * @param object
     * @see org.hibernate.Hibernate
     */
    void initialize(Object object);

    /**
     * Checks if a type has been mapped in Hibernate.
     * 
     * @param type
     *            String representation of a full-qualified Hibernate-mapped
     *            type.
     * @return yes or no.
     */
    boolean checkType(String type);

    /**
     * Checks if a property is defined on a mapped Hibernate type.
     * 
     * @param type
     *            String representation of a full-qualified Hibernate-mapped
     *            type.
     * @param property
     *            Property as defined in Hibernate NOT the public final static
     *            Strings on our IObject classes.
     * @return yes or no.
     */
    boolean checkProperty(String type, String property);
}