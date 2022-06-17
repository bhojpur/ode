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

import java.util.Collection;

import ode.conditions.ApiUsageException;
import ode.parameters.QueryParameter;

/**
 * extension of {@link ode.services.query.QueryParameterDef} which restricts the
 * {@link ode.services.query.QueryParameterDef#type type} to a
 * {@link java.util.Collection}, and specifies the element types of that
 * Collection. Also overrides validation to check that type.
 */
public class CollectionQueryParameterDef extends QueryParameterDef {

    public Class elementType;

    public CollectionQueryParameterDef(String name, boolean optional,
            Class elementType) {
        super(name, Collection.class, optional);
        this.elementType = elementType;

    }

    @Override
    /**
     * adds Collection-element tests after calling super.errorIfInvalid();
     */
    public void errorIfInvalid(QueryParameter parameter) {
        super.errorIfInvalid(parameter);

        if (!optional && ((Collection) parameter.value).size() < 1) {
            throw new ApiUsageException(
                    "Requried collection parameters may not be empty.");
        }

        if (parameter.value != null) {
            for (Object element : (Collection) parameter.value) {

                if (element == null) {
                    throw new ApiUsageException(
                            "Null elements are not allowed "
                                    + "in parameter collections");
                }

                if (!elementType.isAssignableFrom(element.getClass())) {
                    throw new ApiUsageException("Elements of type "
                            + element.getClass().getName()
                            + " are not allowed in collections of type "
                            + elementType.getName());
                }
            }
        }

    }

}
