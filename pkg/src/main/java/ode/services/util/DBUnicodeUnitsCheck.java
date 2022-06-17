package ode.services.util;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import ode.conditions.InternalException;
import ode.system.PreferenceContext;
import ode.util.SqlAction;

/**
 * Checks that the database contains correctly encoded enumerations for units of measure.
 *
 */
public class DBUnicodeUnitsCheck extends BaseDBCheck {

    public final static Logger LOGGER = LoggerFactory.getLogger(DBUnicodeUnitsCheck.class);

    protected DBUnicodeUnitsCheck(Executor executor, PreferenceContext preferences, ReadOnlyStatus readOnly) {
        super(executor, preferences, readOnly);
    }

    @Override
    protected void doCheck() {
        final boolean hasUnicodeUnits;
        try {
            hasUnicodeUnits = (Boolean) executor.executeSql(new Executor.SimpleSqlWork(this, "DBUnicodeUnitsCheck") {
                @Transactional(readOnly = true)
                public Boolean doWork(SqlAction sql) {
                    return sql.hasUnicodeUnits();
                }
            });
        } catch (Exception e) {
            final String message = "Error while checking the encoding of units of measure.";
            LOGGER.error(message, e);
            throw new InternalException(message);
        }
        if (hasUnicodeUnits) {
            LOGGER.info("Database has the correctly encoded units of measure.");
        } else {
            final String message = "Database does not contain correctly encoded units of measure.";
            LOGGER.error(message);
            throw new InternalException(message);
        }
    }
}
