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

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * Definition of a server configuration variable ("preference") along with its
 * mutability, visibility, aliases and other important information. These
 * {@link Preference preferences} are defined in ode/config.xml along with the
 * {@link PreferenceContext}, and the default values are defined in the
 * etc/*.properties files which get stored in the final jars.
 * 
 * For any configuration which does not have an explicit mapping, the default
 * will be as if "new Preference()" is called. See the individual fields below
 * for more information.
 */
public class Preference implements BeanNameAware {

    public enum Visibility {
        /* Warning: "user" currently treated like "hidden". */
        hidden, all, admin, user;
    }

    /**
     * Whether or not an administrator can change this property remotely. By
     * default this value is true, since otherwise creating new configuration
     * values would be impossible. Therefore, it is necessary to explicitly
     * specify all configuration keys which should be immutable.
     */
    private boolean mutable = true;

    /**
     * Whether or not a configuration value can be found in the database. Some
     * values inherently make no sense to store in the db, like the db
     * connection information. All other properties should be storable there,
     * and so {@link #db} is true by default.
     */
    private boolean db = true;

    /**
     * Whether or not a configuration value can be found in the system
     * preferences. True by default.
     */
    private boolean prefs = true;

    /**
     * For whom this preference is visible. By default, admin.
     */
    private Visibility visibility = Visibility.admin;

    /**
     * For backwards compatibility, the key strings which were use in
     * Bhojpur ODE have been aliased to the new key strings. These may
     * eventually be removed.
     */
    private String[] aliases = new String[0];

    /**
     * To simplify configuration, the Spring bean id/name becomes the key string
     * for this {@link Preference}.
     */
    private String beanName = "unknown";

    /**
     * By default, configures this instance for
     * {@link PropertyPlaceholderConfigurer#SYSTEM_PROPERTIES_MODE_OVERRIDE} as
     * well as ignoring unfound resources.
     */
    public Preference() {

    }

    public Preference(String beanName, boolean mutable, Visibility visibility,
            String[] aliases) {
        setBeanName(beanName);
        setMutable(mutable);
        setVisibility(visibility);
        setAliases(aliases);
    }

    public String getName() {
        return this.beanName;
    }

    /**
     * Setter injector
     */
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    /**
     * Setter injector
     */
    public void setMutable(boolean mutable) {
        this.mutable = mutable;
    }

    public Visibility getVisibility() {
        return this.visibility;
    }

    /**
     * Setter injector
     */
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public boolean hasAlias(String key) {
        if (aliases != null && key != null) {
            for (int i = 0; i < aliases.length; i++) {
                if (key.equals(aliases[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Setter injector
     */
    public void setAliases(String[] aliases) {
        if (aliases == null) {
            this.aliases = null;
        } else {

            this.aliases = new String[aliases.length];
            System.arraycopy(aliases, 0, this.aliases, 0, aliases.length);
        }
    }

    public boolean isDb() {
        return this.db;
    }

    /**
     * Setter injector
     */
    public void setDb(boolean db) {
        this.db = db;
    }
    
    public boolean isPrefs() {
        return this.prefs;
    }

    public void setPrefs(boolean prefs) {
        this.prefs = prefs;
    }
}