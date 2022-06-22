package ode.system;

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

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;

import com.google.common.collect.MapMaker;

/**
 * Central configuration for Bhojpur ODE properties from (in order):
 * <ul>
 * <li>Any injected {@link Properties} instances</li>
 * <li>Java {@link System#getProperties()}</li>
 * <li>Any configured property files</li>
 * </ul>
 * 
 * As of Bhojpur ODE, server configurations are not stored in Java's
 * Preferences API but in an IceGrid xml file under etc/grid of the server
 * installation. The properties are set in the config file on node startup, for
 * example in var/master/servers/ODe-0/config/config. When the Java process
 * starts, {@code ode.services.server.Entry} places the values in
 * {#link {@link System#getProperties()}.
 */
public class PreferenceContext extends PropertyPlaceholderConfigurer {

    private final static Logger log = LoggerFactory.getLogger(PreferenceContext.class);

    private final Map<String, Preference> preferences = new MapMaker().makeMap();

    private PropertyPlaceholderHelper helper;

    /**
     * By default, configures this instance for
     * {@link PropertyPlaceholderConfigurer#SYSTEM_PROPERTIES_MODE_OVERRIDE} as
     * well as ignoring unfound resources.
     */
    public PreferenceContext() {
        setSystemPropertiesMode(SYSTEM_PROPERTIES_MODE_OVERRIDE);
        setIgnoreResourceNotFound(true);
        helper = new PropertyPlaceholderHelper(
                PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_PREFIX,
                PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_SUFFIX,
                PropertyPlaceholderConfigurer.DEFAULT_VALUE_SEPARATOR,
                false); // Note, we want the IllegalArgumentThrown for catching.
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {
        super.postProcessBeanFactory(bf);
        // Publish all properties in System.properties
        try {
            log.info("Publishing system properties...");
            Properties properties = mergeProperties();
            Enumeration<?> names = properties.propertyNames();
            while (names.hasMoreElements()) {
                String key = names.nextElement().toString();
                String value = properties.getProperty(key);
                if (System.getProperty(key) == null) {
                    System.setProperty(key, value);
                    log.debug("Set property: {}={}", key, value);
                }
            }
        } catch (IOException ioe) {
            log.error("Error on mergeProperties()", ioe);
            throw new FatalBeanException("Error on mergeProperties()", ioe);
        }
    }
    /**
     * Lookup method for getting access to the {@link #mergeProperties() merged
     * properties} for this instance.
     */
    public String getProperty(String key) {
        try {
            //return parseStringValue("${" + key + "}", mergeProperties(),
            //                            new java.util.HashSet<String>());
            key = "${" + key + "}";
            return helper.replacePlaceholders(key,
                    new PropertyPlaceholderConfigurerResolver(mergeProperties()));
        } catch (IllegalArgumentException iae) {
            return null; // From change of helper in Spring 3.0
        } catch (BeanDefinitionStoreException bdse) {
            return null; // Unknown property. Ok
        } catch (IOException e) {
            log.error("Error on mergeProperties()", e);
            return null;
        } catch (Exception exc) {
            log.error("Other exception on getProperty", exc);
            return null;
        }
    }

    /**
     * preferences are no longer mutable. For that, we will
     * need a python server which can update the XML file.
     */
    public void setProperty(String key, String value) {
        throw new UnsupportedOperationException();
    }

    public void setPreferences(List<Preference> preferences) {
        for (Preference pref : preferences) {
            this.preferences.put(pref.getName(), pref);
        }

    }

    // Defined Preferences
    // =========================================================================

    public String resolveAlias(String key) {

        if (preferences.containsKey(key)) {
            return key;
        }

        for (String current : preferences.keySet()) {
            Preference preference = preferences.get(current);
            if (preference.hasAlias(key)) {
                return current;
            }
        }

        return key;
    }

    public Set<String> getKeySet() {
        return preferences.keySet();
    }

    public boolean checkDatabase(String key) {
        Preference preference = getPreferenceOrDefault(key);

        return preference.isDb();
    }

    public boolean canRead(EventContext ec, String key) {
        Preference preference = getPreferenceOrDefault(key);

        switch (preference.getVisibility()) {
        case all:
            return true;
        case admin:
            return ec.isCurrentUserAdmin();
        case user:
            /* TODO: is a member of the "user" group? */
        case hidden:
        default:
            return false;
        }
    }

    // Helpers
    // =========================================================================

    private Preference getPreferenceOrDefault(String key) {
        Preference preference = preferences.get(key);

        if (preference == null) {
            preference = new Preference();
        }
        return preference;
    }
    
    // Copied from PropertyPlaceholderConfigurer
    private class PropertyPlaceholderConfigurerResolver implements PlaceholderResolver {

        private final Properties props;

        private PropertyPlaceholderConfigurerResolver(Properties props) {
            this.props = props;
        }

        public String resolvePlaceholder(String placeholderName) {
            return PreferenceContext.this.resolvePlaceholder(placeholderName,
                    props, PreferenceContext.SYSTEM_PROPERTIES_MODE_OVERRIDE);
        }
    }

}