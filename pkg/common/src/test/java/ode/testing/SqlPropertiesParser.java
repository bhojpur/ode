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

import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * utility to parse a properties file. See "resources/test_queries.properties"
 * for an explanation of the format.
 */
public abstract class SqlPropertiesParser {

    private static Logger log = LoggerFactory.getLogger(SqlPropertiesParser.class);

    static class MyPropertyPlaceholderConfigurer extends
            PropertyPlaceholderConfigurer {
        @Override
        protected void processProperties(
                ConfigurableListableBeanFactory beanFactoryToProcess,
                Properties props) throws BeansException {
        }

        public String doIt(String property) {
            try {
                return parseStringValue(property, this.mergeProperties(),
                        new HashSet());
            } catch (Exception e) {
                throw new RuntimeException(
                        "Error in evaluating embedded properties.", e);
            }
        }
    }

    static MyPropertyPlaceholderConfigurer prc = new MyPropertyPlaceholderConfigurer();

    protected static void load(Properties props, String filename) {
        InputStream is = SqlPropertiesParser.class.getClassLoader()
                .getResourceAsStream(filename);

        try {
            props.load(is);

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse properties file "
                    + filename, e);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (Exception e) {
                    throw new RuntimeException(
                            "Failed to close properties file " + filename, e);
                }
            }
        }

    }

    /* last one wins */
    public static Map parse(String[] filenames) {
        // Get strings from files
        Map result = new HashMap();
        Properties props = new Properties();
        for (int i = 0; i < filenames.length; i++) {
            load(props, filenames[i]);
        }

        // Put them into a map converting embedded ${...}
        prc.setProperties(props);
        prc.postProcessBeanFactory(null);
        for (Enumeration en = props.propertyNames(); en.hasMoreElements();) {
            String name = (String) en.nextElement();
            result.put(name, prc.doIt(props.getProperty(name)));
        }

        // Do extra parsing where necessary
        for (Iterator it = result.keySet().iterator(); it.hasNext();) {
            String name = (String) it.next();
            String value = (String) result.get(name);
            if (!value.startsWith("select")) {
                String[] strValues = value.split("\\s*,\\s*");
                Long[] longValues = new Long[strValues.length];
                for (int i = 0; i < strValues.length; i++) {
                    longValues[i] = Long.valueOf(strValues[i]);
                }
                result.put(name, Arrays.asList(longValues));
            }
        }

        return result;

    }
}