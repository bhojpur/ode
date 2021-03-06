package ode.testing;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

/**
 * abstract data container for testing. Sub-classes can set whatever values it
 * would like in <code>init()</code>. After the ODEData instance is inserted
 * into the test class by Spring, it SHOULD not be changed, but this is a matter
 * of opionon. Setting the same <code>seed</code> value for two independent
 * Data instances is also assumed to create identical values.
 */
public class ODEData {

    final static String emptyColl = "Collections may not be empty.\n"
            + "You are currently trying to run a test on a Bhojpur ODE database\n"
            + "that does not appear to have the needed data.\n"
            + "\n"
            + "There must be at least one:\n"
            + "project,dataset,image,experimenter,classification,category,category group,image annotation and dataset annotation\n"
            + "\n"
            + "Testing results would be unpredictable without test data.\n"
            + "Please fill your database and retry.";

    private static Logger log = LoggerFactory.getLogger(ODEData.class);

    boolean initialized = false;

    DataSource ds;

    Map properties;

    Map values = new HashMap();

    long seed;

    Random rnd;

    String[] files = new String[] {"test_data.properties"};

    public void setDataSource(DataSource dataSource) {
        this.ds = dataSource;
    }

    public ODEData() {
        init();
    }

    public ODEData(String[] files) {
        this.files = files;
        init();
    }

    void init() {
        properties = SqlPropertiesParser.parse(files);
        seed = System.currentTimeMillis();
        rnd = new Random(seed);
    }

    /* allows for storing arbitrary objects in data */
    public void put(String propertyKey, Object value) {
        toCache(propertyKey, value);
    }

    public List get(String propertyKey) {

        if (inCache(propertyKey)) {
            return (List) fromCache(propertyKey);
        }

        Object obj = properties.get(propertyKey);
        if (obj == null) {
            return null;
        } else if (obj instanceof List) {
            toCache(propertyKey, obj);
            return (List) obj;
        } else if (obj instanceof String) {
            String sql = (String) obj;
            List result = runSql(sql);
            toCache(propertyKey, result);
            return result;
        } else {
            throw new RuntimeException("Error in properties. Not expecting "
                    + obj == null ? null : obj.getClass().getName());
        }
    }

    List getRandomNumber(List l, Number number) {

        if (number == null) {
            return null;
        }

        if (l == null || l.size() == 0) {
            log.warn(emptyColl);
            return null;
        }

        List ordered = new ArrayList(l);
        List result = new ArrayList();

        while (ordered.size() > 0 && result.size() < number.longValue()) {
            int choice = randomChoice(ordered.size());
            result.add(ordered.remove(choice));
        }

        return result;

    }

    public List getMax(String propertyKey, int maximum) {
        List l = get(propertyKey);
        return getRandomNumber(l, new Integer(maximum));

    }

    public List getPercent(String propertyKey, double percent) {
        List l = get(propertyKey);
        return getRandomNumber(l, new Double(l.size() * percent));

    }

    public Object getRandom(String propertyKey) {
        List l = get(propertyKey);
        List result = getRandomNumber(l, new Integer(1));

        if (result == null || result.size() < 1) {
            return null;
        }

        return result.get(0);
    }

    public Object getFirst(String propertyKey) {
        List l = get(propertyKey);

        if (l == null || l.size() == 0) {
            log.warn(emptyColl);
            return null;
        }

        return l.get(0);
    }

    boolean inCache(String key) {
        return values.containsKey(key);
    }

    void toCache(String key, Object value) {
        values.put(key, value);
    }

    Object fromCache(String key) {
        return values.get(key);
    }

    /**
     * returns a list of results from the sql statement. if there is more than
     * one column in the result set, a map from column name to Object is
     * returned, else the Object itself.
     * 
     * @param sql
     * @return
     */
    List runSql(String sql) {
        JdbcTemplate jt = new JdbcTemplate(ds);
        SqlRowSet rows = jt.queryForRowSet(sql);
        List result = new ArrayList();
        while (rows.next()) {
            SqlRowSetMetaData meta = rows.getMetaData();
            int count = meta.getColumnCount();
            if (count > 1) {
                Map cols = new HashMap();
                String[] names = meta.getColumnNames();
                for (int i = 0; i < names.length; i++) {
                    cols.put(names[i], rows.getObject(names[i]));
                }
                result.add(cols);
            } else {
                result.add(rows.getObject(1));
            }
        }
        log.debug("SQL:" + sql + "\n\nResult:" + result);
        return result;
    }

    public int randomChoice(int size) {
        double value = (size - 1) * rnd.nextDouble();
        return (new Double(value)).intValue();
    }

}