package ode.parameters;

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

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import ode.conditions.ApiUsageException;

/**
 * arbitrary query parameter used by {@code ode.api.IQuery}.
 */
public class QueryParameter implements Serializable {

    final public Class type;

    final public String name;

    final public Object value;

    public QueryParameter(String name, Class type, Object value) {

        if (name == null) {
            throw new ApiUsageException("Expecting a value for name.");
        }

        if (type == null) {
            throw new ApiUsageException("Expecting a value for type.");
        }

        if (value == null || type.isAssignableFrom(value.getClass())) {
            this.name = name;
            this.type = type;
            this.value = value;
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append("Value object should be of type: ");
            sb.append(type.getName());
            sb.append(" and not: ");
            sb.append(value.getClass().getName());
            throw new ApiUsageException(sb.toString());
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(64);
        sb.append("QP{");
        sb.append("name=");
        sb.append(name);
        sb.append(",type=");
        sb.append(type.getName());
        sb.append(",value=");
        sb.append(value);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof QueryParameter)) {
            return false;
        }

        QueryParameter qp = (QueryParameter) obj;

        if (this == qp) {
            return true;
        }

        if (!this.name.equals(qp.name)) {
            return false;
        }
        if (!this.type.equals(qp.type)) {
            return false;
        }

        return this.value == null ? qp.value == null : this.value
                .equals(qp.value);

    }

    @Override
    public int hashCode() {
        int result = 11;
        result = 17 * result + name.hashCode();
        result = 19 * result + type.hashCode();
        result = 23 * result + (value == null ? 0 : value.hashCode());
        return result;
    }

    // ~ Serialization
    // =========================================================================
    private static final long serialVersionUID = 112229651549133492L;

    private void readObject(ObjectInputStream s) throws IOException,
            ClassNotFoundException {
        s.defaultReadObject();

        if (type == null) {
            throw new InvalidObjectException(
                    "QueryParameter type cannot be null.");
        }

        if (value == null) {
            throw new InvalidObjectException(
                    "QueryParameter value cannot be null.");
        }

        if (!type.isAssignableFrom(value.getClass())) {
            throw new InvalidObjectException(
                    "QueryParameter value must be of type type.");
        }
    }

}