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

import org.hibernate.dialect.Dialect;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.DiscriminatorType;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

/**
 * A Hibernate type for string-valued properties where only privileged readers see non-{@code null} values.
 */
/* TODO: With Hibernate 4.3 could try AttributeConverter and @Convert. */
@SuppressWarnings("serial")
public abstract class PrivilegedStringType extends AbstractSingleColumnStandardBasicType<String>
    implements DiscriminatorType<String> {

    /** String type for which users read {@code null} except for full administrators. */
    public static class FilteredFullAdmin extends PrivilegedStringType {
        public FilteredFullAdmin() {
            super(VarcharTypeDescriptor.INSTANCE, PrivilegedStringTypeDescriptor.FilteredFullAdmin.INSTANCE);
        }
    };

    /** String type for which users read a dummy value except for full administrators. */
    public static class FilteredFullAdminHidden extends PrivilegedStringType {
        public FilteredFullAdminHidden() {
            super(VarcharTypeDescriptor.INSTANCE, PrivilegedStringTypeDescriptor.FilteredFullAdminHidden.INSTANCE);
        }
    };

    /** String type for which users read a dummy UUID except for full administrators. */
    public static class FilteredFullAdminUUID extends PrivilegedStringType {
        public FilteredFullAdminUUID() {
            super(VarcharTypeDescriptor.INSTANCE, PrivilegedStringTypeDescriptor.FilteredFullAdminUUID.INSTANCE);
        }
    };

    /** String type for which users read {@code null} except for (potentially) related users. */
    public static class FilteredRelatedUser extends PrivilegedStringType {
        public FilteredRelatedUser() {
            super(VarcharTypeDescriptor.INSTANCE, PrivilegedStringTypeDescriptor.FilteredRelatedUser.INSTANCE);
        }
    };

    /** String type for which users read a dummy value except for (potentially) related users. */
    public static class FilteredRelatedUserHidden extends PrivilegedStringType {
        public FilteredRelatedUserHidden() {
            super(VarcharTypeDescriptor.INSTANCE, PrivilegedStringTypeDescriptor.FilteredRelatedUserHidden.INSTANCE);
        }
    };

    /**
     * @param sqlInstance the SQL type of the privileged strings
     * @param javaInstance the Java type of the privileged strings
     */
    public PrivilegedStringType(VarcharTypeDescriptor sqlInstance, PrivilegedStringTypeDescriptor javaInstance) {
        super(sqlInstance, javaInstance);
    }

    /* Adapted from org.hibernate.type.StringType. */

    @Override
    public String getName() {
        return "securestring";
    }

    @Override
    public String objectToSQLString(String value, Dialect dialect) throws Exception {
        return '\'' + value + '\'';
    }

    @Override
    public String stringToObject(String xml) throws Exception {
        return xml;
    }

    @Override
    public String toString(String value) {
        return value;
    }
}