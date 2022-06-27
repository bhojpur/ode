package ode.util;

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

import java.util.Set;

import ode.conditions.ApiUsageException;
import ode.model.IObject;
import ode.model.internal.Details;
import ode.model.internal.GraphHolder;

import org.hibernate.Hibernate;

/**
 * wrapper class which can be used in HQL queries to properly load
 * the full context for a permissions object. Rather than writing a
 * query of the form:
 *
 * <code>
 * select d.details.permissions from Dataset d
 * </code>
 *
 * which returns a {@link ode.model.internal.Permissions} object with none of the extended
 * restrictions (canRead, canAnnotate, etc) properly loaded, use:
 *
 * <code>
 * select new ode.util.PermDetails(d) from Dataset d
 * </code>
 *
 * The return value for each will be the same.
 */
@SuppressWarnings("serial")
public class PermDetails implements IObject {

    private final IObject context;

    public PermDetails(IObject context) {
        Hibernate.initialize(context);
        this.context = context;
    }

    /**
     * In order to properly test the permissions for this object, it must be
     * possible to get the internal context. This is for use by the security
     * system only.
     */
    public IObject getInternalContext() {
        return this.context;
    }

    //
    // DELEGATE METHODS
    //

    public Long getId() {
        return context.getId();
    }

    public void setId(Long id) {
        context.setId(id);
    }

    public Details getDetails() {
        return context.getDetails();
    }

    public boolean isLoaded() {
        return context.isLoaded();
    }

    public void unload() throws ApiUsageException {
        context.unload();
    }

    public boolean isValid() {
        return context.isValid();
    }

    public Validation validate() {
        return context.validate();
    }

    public Object retrieve(String field) {
        return context.retrieve(field);
    }

    public void putAt(String field, Object value) {
        context.putAt(field, value);
    }

    public Set<?> fields() {
        return context.fields();
    }

    public GraphHolder getGraphHolder() {
        return context.getGraphHolder();
    }

    @Override
    public boolean acceptFilter(Filter filter) {
        return context.acceptFilter(filter);
    }

}