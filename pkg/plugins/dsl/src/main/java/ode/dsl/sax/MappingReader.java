package ode.dsl.sax;

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

import ode.dsl.Property;
import ode.dsl.SemanticType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * reads semantic-type-xml and produces a Set of SemanticType objects. Most
 * logic is passed off to the {@link SemanticType ST} and
 * {@link Property Property} classes.
 */
public class MappingReader {

    /**
     * handler which collects all types and properties from the input file
     */
    private DSLHandler handler;

    /**
     * SAXparser which does the actualy processing
     */
    private SAXParser parser;

    public MappingReader(String profile, Properties databaseTypes) {
        handler = new DSLHandler(profile, databaseTypes);
        init();
    }

    private void init() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            parser = factory.newSAXParser();
        } catch (Exception e) {
            throw new RuntimeException("Error setting up MappingReader :\n"
                    + e.getMessage(), e);
        }
    }

    /**
     * parses file and returns types
     */
    public Map<String, SemanticType> parse(File file) {
        try {
            URL xmlFile = file.toURI().toURL();
            parser.parse(xmlFile.getPath(), handler);
            return handler.getTypes();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error determining file's path:" + file
                    + " :\n" + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing " + file + " :\n"
                    + e.getMessage(), e);
        }
    }

    public static class DSLHandler extends DefaultHandler {

        final static Logger log = LoggerFactory.getLogger(DSLHandler.class);

        // Turns output on/off
        public boolean verbose = false;

        // Indention for output
        private String depth = "";

        private SemanticType type;

        private Property property;

        private final String profile;

        // For handling
        private final Map<String, SemanticType> types = new HashMap<>();

        private Properties databaseTypes;

        public DSLHandler(String profile, Properties databaseTypes)
        {
            this.profile = profile;
            this.databaseTypes = databaseTypes;
        }

        /**
         * dispatches to output (printing) and handling (object-creation) routines
         */
        @Override
        public void startElement(String arg0, String arg1, String element,
                                 Attributes attrs) throws SAXException {
            if (verbose) {
                outputStart(element, attrs);
            }
            handleEntry(element, attrs);
            super.startElement(arg0, arg1, element, attrs);
        }

        /**
         * dispatches to output (printing) and handling (object-creation) routines
         */
        @Override
        public void endElement(String arg0, String arg1, String element)
                throws SAXException {
            super.endElement(arg0, arg1, element);
            handleExit(element);
            if (verbose) {
                outputStop(element);
            }
        }

        public Map<String, SemanticType> getTypes() {
            return types;
        }

        /**
         * creates a new type or property based on element name
         */
        private void handleEntry(String element, Attributes attrs) {
            if (Property.FIELDS.contains(element)) {

                if (null != property) {
                    throw new IllegalStateException("Trying to enter property "
                            + element + " from within property" + property);
                }

                if (null == type) {
                    throw new IllegalStateException("Trying to create property "
                            + element + " without a type!");
                }

                property = Property.makeNew(element, type, attrs2props(attrs), databaseTypes);

            } else if ("properties".equals(element)) {
                // ok. these usually contains lots of properties
            } else if (SemanticType.TYPES.contains(element)) {
                if (null != type) {
                    throw new IllegalStateException("Trying to enter type "
                            + element + " from within type " + type);
                }

                type = SemanticType.makeNew(profile, element, attrs2props(attrs), databaseTypes);

            } else if ("types".equals(element)) {
                // also ok.
            } else {
                log.debug("Deprecated: In the future elements of type " + element
                        + " will be considered an error.");
            }

        }

        /**
         * checks to see that after type creation, the model is in a valid state
         */
        private void handleExit(String element) {

            if (Property.FIELDS.contains(element)) {
                if (null == property) {
                    throw new IllegalStateException(
                            "Exiting non-extant property!\n" + "Element:" + element
                                    + "\nType:" + type + "\nProperty:" + property);
                }

                if (null == type) {
                    throw new IllegalStateException("Inside of non-extant type!!\n"
                            + "Element:" + element + "\nType:" + type);
                }

                property.validate();
                type.getProperties().add(property);
                property = null;

            } else if (SemanticType.TYPES.contains(element)) {
                if (null == type) {
                    throw new IllegalStateException("Exiting non-extent type!\n"
                            + "Element:" + element + "\nType:" + type);
                }

                type.validate();
                types.put(type.getId(), type);
                type = null;
            }
        }

        /**
         * simple outputting routine with indention
         */
        private void outputStart(String element, Attributes attrs) {
            if (log.isDebugEnabled()) {
                log.debug(depth + element + "(");
            }
            for (int i = 0; i < attrs.getLength(); i++) {
                String attr = attrs.getQName(i);
                String value = attrs.getValue(i);
                if (log.isDebugEnabled()) {
                    log.debug(" " + attr + "=\"" + value + "\" ");
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("): ");
            }
            depth += "  ";
        }

        /**
         * reduces indention for output
         */
        private void outputStop(String element) {
            depth = depth.substring(2);
        }

        /**
         * converts xml attributes to java.util.Properties
         */
        private Properties attrs2props(Attributes attrs) {
            Properties p = new Properties();
            for (int i = 0; i < attrs.getLength(); i++) {
                String key = attrs.getQName(i);
                String value = attrs.getValue(i);
                p.put(key, value);
            }
            return p;
        }

    }

}

