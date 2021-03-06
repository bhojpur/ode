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

import java.io.Serializable;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.enhanced.OptimizerFactory;
import org.hibernate.id.enhanced.TableGenerator;
import org.hibernate.type.Type;

/**
 * Bhojpur ODE specific id generation strategy. Combines both {@link TableGenerator}
 * and {@link OptimizerFactory} into a single class because of
 * weirdness in their implementation. Instead, uses our own ode_nextval(?,?)
 * method to keep the Hibernate sequence values ({@link #hiValue}) in sync with
 * the database values.
 */
public class TableIdGenerator extends TableGenerator {

    private final static Logger log = LoggerFactory.getLogger(TableIdGenerator.class);

    long value;

    long hiValue = -1;

    private SqlAction sql = null;

    @Override
    public void configure(Type type, Properties params, Dialect dialect)
            throws MappingException {
        super.configure(type, params, dialect);
    }

    public void setSqlAction(SqlAction sql) {
        this.sql = sql;
    }

    public synchronized Serializable generate(final SessionImplementor session,
            Object obj) {

        if (hiValue < 0 || value >= hiValue) {
            hiValue = sql.nextValue(getSegmentValue(), getIncrementSize());
            if (log.isDebugEnabled()) {
                log.debug("Loaded new hiValue " + hiValue + " for "
                        + getSegmentValue());
            }
            value = hiValue - getIncrementSize();
        }
        value++;
        return value;
    }

}