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

import java.util.Map;

import ode.model.units.UNITS;

import org.hibernate.Hibernate;
import org.hibernate.dialect.function.SQLFunctionTemplate;

/**
 * select i from Image i where i.id in (:ids)
 * becomes
 * select i from Image i where i.id in (select id from temp_ids());
 */
public class PostgresqlDialect extends org.hibernate.dialect.PostgreSQLDialect {

    public PostgresqlDialect() {
        super();
        registerFunction("temp_ids_cursor",
                new SQLFunctionTemplate(Hibernate.LONG, "select id from table(temp_ids_cursor(?1))"));

        for (Map.Entry<String, Integer> entry : UNITS.listSqlTypes().entrySet()) {
            registerColumnType(entry.getValue(), entry.getKey());
        }

    }

}