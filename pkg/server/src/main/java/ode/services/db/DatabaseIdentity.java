package ode.services.db;

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

import ode.util.SqlAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the unique identity of this database, consisting of the
 * ode.db.authority and ode.db.uuid properties. Used primarily to fulfill
 * the LSID contract of globally unique identifiers. On database initialization:
 * <p>
 * 
 * <pre>
 *   bin/ode db script
 *   psql my_database &lt; script
 * </pre>
 * 
 * </p>
 * a UUID is added to the "configuration" table with the key "ode.db.uuid".
 * This value will be used in all objects exported from this database, so that
 * they can be cleanly re-imported.
 * 
 * This implies that it is <em>not safe</em> to copy a database and use it
 * actively while the original database is still running. Only use database
 * copies (or "dumps") as a backup in case of catastrophic failure.
 * 
 * A default authority of "export.bhojpur.net" is used to simplify
 * initial configuration, but you are welcome to use a domain belonging to you
 * as the authority. If you choose to do so, you will need to use the same
 * authority on any host which you may happen to migrate your database to.
 */
public class DatabaseIdentity {

    private static String uuid(SqlAction sql) {
        return sql.dbUuid();
    }

    private final static Logger log = LoggerFactory.getLogger(DatabaseIdentity.class);

    private final String authority;

    private final String uuid;

    private final String format;

    public DatabaseIdentity(String authority, SqlAction sql) {
        this(authority, uuid(sql));
    }

    public DatabaseIdentity(String authority, String uuid) {
        this.authority = authority;
        this.uuid = uuid;
        this.format = String.format("urn:lsid:%s:%%s:%s_%%s%%s", authority, uuid);
        log.info("Using LSID format: " + format);
    }

    public String getAuthority() {
        return authority;
    }

    public String getUuid() {
        return uuid;
    }

    public boolean valid(String lsid) {
        return false;
    }

    public boolean own(String lsid) {
        return false;
    }

    public String lsid(Class k, long id) {
        return String.format(format, parse(k), id, "");
    }

    public String lsid(Class k, long id, long version) {
        return String.format(format, parse(k), id, ":" + version);
    }

    public String lsid(String ns, String id) {
        return String.format(format, ns, id, "");
    }

    public String lsid(String ns, String id, String version) {
        return String.format(format, ns, id, version);
    }

    // Helpers
    // =========================================================================

    private String parse(Class k) {
        String name = k.getSimpleName();
        int last = name.length() - 1;
        if (name.substring(last).equals("I")) {
            return name.substring(0, last);
        } else {
            return name;
        }
    }
}
