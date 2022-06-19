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

import ode.conditions.InternalException;
import ode.system.PreferenceContext;
import ode.util.SqlAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hook run by the context. This hook tests the database version against the
 * software version on {@link #start()}.
 */
public class DBPatchCheck {

    public final static Logger log = LoggerFactory.getLogger(DBPatchCheck.class);

    final SqlAction sql;
    final PreferenceContext prefs;

    public DBPatchCheck(SqlAction sql, PreferenceContext prefs) {
        this.sql = sql;
        this.prefs = prefs;
    }

    private final static String line = "***************************************************************************************\n";
    private final static String see = "See https://docs.bhojpur.net/latest/ode/sysadmins/server-upgrade.html\n";
    private final static String no_table = mk("Error connecting to database table dbpatch. You may need to bootstrap.\n");
    private final static String wrong_version = mk("DB version (%s) does not match the configured value (%s). Please apply a db upgrade.\n");

    private static String mk(String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(line);
        sb.append(msg);
        sb.append(see);
        sb.append(line);
        return sb.toString();
    }

    /**
     */
    public void start() throws Exception {

        final String[] results = new String[3];
        try {
            results[0] = sql.dbVersion();
            results[1] = prefs.getProperty("ode.db.version");
            results[2] = prefs.getProperty("ode.db.patch");
        } catch (Exception e) {
            log.error(no_table, e); // slf4j migration: fatal() to error()
            InternalException ie = new InternalException(no_table);
            throw ie;
        }

        String patch = results[0];
        String version = results[1];
        String dbpatch = results[2];
        String ode = version + "__" + dbpatch;
        if (patch == null || !patch.equals(ode)) {
            String str = String.format(wrong_version, patch, ode);
            log.error(str); // slf4j migration: fatal() to error()
            InternalException ie = new InternalException(str);
            throw ie;
        }

        log.info(String.format("Verified database patch: %s", patch));
    }

}
