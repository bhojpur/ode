package ode.model;

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

import java.io.Serializable;
import java.util.Set;

import ode.conditions.ApiUsageException;
import ode.model.internal.Details;
import ode.model.internal.GraphHolder;
import ode.util.Filterable;
import ode.util.Validation;

/**
 * central model interface. All entities that the backend can persist to the DB
 * implement this interface. (Note: value objects like
 * {@link ode.model.internal.Details} get saved to the DB, but only embedded in
 * other entites.
 */
public interface IObject extends Filterable, Serializable {

    /**
     * primary key of this object. Before the session is flushed, this value
     * <em>may be</em> null.
     * 
     * @return Long primary key. May be null.
     */
    public Long getId();

    /**
     * usually unneeded. Ids are managed by the backend.
     * 
     * @param id
     *            Long value for this id.
     */
    public void setId(Long id);

    // ~ Security
    // =========================================================================
    /**
     * Value (i.e. not entity) which is available on all rows in the database.
     * Low-level "details" such as security, ownership, auditing are managed
     * here.
     * 
     * When setting values on {@link Details}, it is important to realize that
     * most of the values are managed by the backend and may be replaced. For
     * example, a user does not have permission to change the owner of an
     * object, not even when owned by that user.
     * 
     * To replace all of the values from an existing {@link Details} instance,
     * use {@link Details#copy(Details)} or {@link Details#shallowCopy(Details)}
     */
    public Details getDetails();

    // ~ Lifecycle
    // =========================================================================
    /**
     * transient field (not stored in the DB) which specifies whether this
     * object has been loaded from the DB or is only a wrapper around the ID.
     */
    public boolean isLoaded();

    /**
     * set the loaded field to false, and set all non-ID fields to null.
     * Subsequent calls to all accessors other than getId/setId will throw an
     * ApiUsageException
     * 
     * @throws ApiUsageException
     */
    public void unload() throws ApiUsageException;

    // ~ Validation
    // =========================================================================
    /**
     * calls the class-specific validator for this instance and returns the
     * value from {@link Validation#isValid()}
     */
    public boolean isValid();

    /**
     * calls the class-specific validator for this instance and returns the
     * {@link Validation} object.
     * 
     * @return Validation collecting parameter.
     */
    public Validation validate();

    // ~ For dynamic/generic programming
    // =========================================================================
    /**
     * retrieves a value from this instance. Values for <code>field</code>
     * which match a field of this instance will be delegated to the accessors.
     * Otherwise, values will be retrieved from a lazy-loaded map filled by
     * calls to {@link #putAt(String, Object)}
     */
    public Object retrieve(String field);

    /**
     * stores a value in this instance. Values for <code>field</code> which
     * match a field of this instance will be delegated to the accessors.
     * Otherwise, values will be stored in a lazy-loaded map.
     * 
     * @param field
     *            Field name
     * @param value
     *            Any object to be stored.
     */
    public void putAt(String field, Object value);

    /** returns a Set of field names that belong to this class */
    public Set fields();

    // ~ Graph information
    // =========================================================================
    /**
     * retrieves the {@link GraphHolder} for this entity. If the GraphHolder has
     * not been actively set, a new one will be instatiated.
     * 
     * @return Non-null GraphHolder
     */
    public GraphHolder getGraphHolder();
}