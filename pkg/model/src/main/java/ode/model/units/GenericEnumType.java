package ode.model.units;

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

import org.hibernate.HibernateException;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

public class GenericEnumType<E extends Enum<E>> implements UserType, ParameterizedType {

    private UNITS units;

    @Override
    public void setParameterValues(Properties parameters) {
        String units = parameters.getProperty("unit");
        this.units = UNITS.valueOf(units);
    }

    public Object assemble(Serializable cached, Object owner)
            throws HibernateException {
        return cached;
    }

    public Object deepCopy(Object obj) throws HibernateException {
        return obj;
    }

    public Serializable disassemble(Object obj) throws HibernateException {
        return (Serializable) obj;
    }

    public boolean equals(Object obj1, Object obj2) throws HibernateException {
        if (obj1 == obj2) {
            return true;
        }
        if (obj1 == null || obj2 == null) {
            return false;
        }
        return obj1.equals(obj2);
    }

    public int hashCode(Object obj) throws HibernateException {
        return obj.hashCode();
    }

    public boolean isMutable() {
        return false;
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
            throws HibernateException, SQLException {
        String value = rs.getString(names[0]);
        if (!rs.wasNull()) {
            return units.fromdbvalue(value);
        }
        return null;
    }

    public void nullSafeSet(PreparedStatement ps, Object obj, int index)
            throws HibernateException, SQLException {
        if (obj == null) {
            // Note: units.sqlType shouldn't be used since this
            // doesn't actually get registered with the PG driver
            // (apparently). Instead we use the official Types.OTHER
            ps.setObject(index, null, Types.OTHER);
        } else {
            ps.setObject(index, units.todbvalue(obj), Types.OTHER);
        }
    }

    public Object replace(Object original, Object target, Object owner)
            throws HibernateException {
        return original;
    }

    @SuppressWarnings("unchecked")
    public Class<E> returnedClass() {
        return (Class<E>) units.enumType;
    }

    public Class<? extends Unit> getQuantityClass() {
        return (Class<? extends Unit>) units.quantityType;
    }

    public int[] sqlTypes() {
        return new int[] { units.sqlType };
    }

}