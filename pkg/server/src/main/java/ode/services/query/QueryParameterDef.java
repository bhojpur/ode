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
import ode.parameters.Parameters;
import ode.parameters.QueryParameter;

/**
 * definition of a slot into which a {@link ode.parameters.QueryParameter} must
 * fit. These are typically defined statically in
 * {@link ode.services.query.Query} subclasses and collected into
 * {@link ode.services.query.Definitions} which get passed to the super
 * {@link ode.services.query.Query#Query(Definitions, Parameters) Query constructor.}
 */
public class QueryParameterDef {

    /**
     * name of this parameter. Will be compared to all
     * {@link ode.parameters.QueryParameter query parameters} with an equal
     * {@link QueryParameter#name name}.
     */
    final public String name;

    /**
     * type of this parameter. Will restrict what values can be assigned to
     * {@link QueryParameter#value}
     */
    final public Class type;

    /**
     * whether or not this {@link QueryParameter} can be omitted or its
     * {@link QueryParameter#value value} null.
     */
    final public boolean optional;

    /**
     * main constructor. Provides all three fields, none of which can be null.
     */
    public QueryParameterDef(String name, Class type, boolean optional) {
        if (name == null) {
            throw new ApiUsageException("Name cannot be null.");
        }

        if (type == null) {
            throw new ApiUsageException("Type cannot be null.");
        }

        this.name = name;
        this.type = type;
        this.optional = optional;
    }

    /**
     * validation method called by {@link Query#checkParameters()}. Subclasses
     * should be <em>very</em> careful to call super.errorIfInvalid.
     * 
     * @param parameter
     *            Parameter with a matching name to be validated.
     */
    public void errorIfInvalid(QueryParameter parameter) {
        // If paramter is null, skip the rest
        if (parameter == null) {
            if (!this.optional) {
                throw new ApiUsageException(
                        "Non-optional parameter cannot be null.");
            }

            // If the names don't match, there is a problem.
        } else if (parameter.name == null || !parameter.name.equals(this.name)) {
            throw new ApiUsageException(String.format(
                    "Parameter name does not match: %s != %s ", this.name,
                    parameter.name));

            // If parameter.type is null, skip the rest.
        } else if (parameter.type == null) {
            if (!this.optional) {
                throw new ApiUsageException(
                        "Non-optional parameter type cannot be null.");
            }

            // If value is null, skip the rest
        } else if (parameter.value == null) {
            if (!this.optional) {
                throw new ApiUsageException("Non-optional parameter "
                        + this.name + " may not be null.");
            }

        } else {
            // Fields are non-null, check them.
            if (!this.type.isAssignableFrom(parameter.type)) {
                throw new ApiUsageException(String.format(
                        " Type of parameter %s doesn't match: %s != %s", name,
                        this.type, parameter.type));
            }

            if (!this.optional && Collection.class.isAssignableFrom(this.type)
                    && ((Collection) parameter.value).size() < 1) {
                throw new ApiUsageException(
                        "Non-optional collections may not be empty.");
            }

        }

    }

}

// ~ Simple short-cuts
// =========================================================================

class AlgorithmQueryParameterDef extends QueryParameterDef {
    public AlgorithmQueryParameterDef() {
        super(Parameters.ALGORITHM, String.class, false);
    }
}

class ClassQueryParameterDef extends QueryParameterDef {
    public ClassQueryParameterDef() {
        super(Parameters.CLASS, Class.class, false);
    }
}

class IdsQueryParameterDef extends CollectionQueryParameterDef {
    public IdsQueryParameterDef() {
        super(Parameters.IDS, false, Long.class);
    }
}
