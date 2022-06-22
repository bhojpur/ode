package ode.api;

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

import java.util.Date;
import java.util.Map;
import java.util.prefs.Preferences;

import ode.annotations.NotNull;
import ode.conditions.ApiUsageException;
import ode.conditions.InternalException;
import ode.conditions.SecurityViolation;

/**
 * Access to server configuration. These methods provide access to the state and
 * configuration of the server and its components (e.g. the database). However,
 * it should not be assumed that two subsequent calls to a proxy for this
 * service will go to the same server due to clustering.
 * 
 * Not all possible server configuration is available through this API. Some
 * values (such as DB connection info, ports, etc.) must naturally be set before
 * this service is accessible.
 * 
 * Manages synchronization of the various configuration sources internally. It
 * is therefore important that as far as possible all configuration changes
 * take place via this interface and not, for example, directly via
 * {@link Preferences}.
 * 
 * Also used as the main developer example for developing (stateless) ode.api
 * interfaces. See source code documentation for more.
 */
/*
 * Developer notes: The two annotations below are activated by setting
 * subversion properties on this class file. These values can then be accessed
 * via ode.system.Version
 */
public interface IConfig extends ServiceInterface {

    /**
     * Defines how the ode.version {@link ode.system.Preference} will be parsed
     * into the form: Major.minor.patch for {@link #getVersion()}
     */
    public final static String VERSION_REGEX = "^.*?[-]?(\\d+[.]\\d+[.]\\d+)[-]?.*?$"; 

    /*
     * Developer notes: Simple almost hello-world call. There should be almost
     * nothing that causes this to throw an exception (except perhaps a Java
     * security policy file which disallows "new Date()"). Therefore we don't
     * add a throws clause here. Anything that is thrown will be wrapped in an
     * InternalException
     */
    /**
     * checks the current server for its time. This value may be variant
     * depending on whether the service is clustered or not.
     * 
     * @return Non-null {@link Date} representation of the server's own time.
     */
    Date getServerTime();

    /*
     * Developer notes: This call hits the database through JDBC (not our own
     * Hibernate infrastructure) and therefore it is more likely that an
     * exception can occur. An InternalException will also be thrown (though
     * this may change as more exceptions are created). We mark it here for
     * general consumption; readers of the API will want to know why.
     */
    /**
     * checks the database for its time using a SELECT statement.
     * 
     * @return Non-null {@link Date} representation of the database's time.
     * @throws InternalException
     *             though any call can throw an InternalException it is more
     *             likely that this can occur while contacting the DB. An
     *             exception here most likely means (A) a temporary issue with
     *             the DB or (B) a SQL dialect issue which must be corrected by
     *             the Bhojpur ODE team.
     */
    Date getDatabaseTime() throws InternalException;

    /*
     * Developer notes: The @NotNull annotation on the key parameter will cause
     * all managed method calls on any implementation of this interface to be
     * checked by ode.annotations.ApiConstraintChecker. This is done before any
     * access to the Hibernate session is performed and so balances its own
     * overhead somewhat.
     */
    /**
     * retrieve a configuration value from the backend store. Permissions
     * applied to the configuration value may cause a {@link SecurityViolation}
     * to be thrown.
     *
     * @param key
     *            The non-null name of the desired configuration value
     * @return The {@link String} value linked to this key, possibly null if not
     *         set.
     * @throws ApiUsageException
     *             if the key is null or invalid.
     * @throws SecurityViolation
     *             if the value for the key is not readable.
     */
    String getConfigValue(@NotNull
    String key) throws ApiUsageException, SecurityViolation;

    /**
     * retrieves configuration values from the backend store which match the
     * given regex. Any configuration value which would throw an exception
     * on being loaded is omitted.
     *
     * @param keyRegex
     *            The non-null regex of the desired configuration values
     * @return a {@link Map} from the found keys to the linked values.
     */
    Map<String, String> getConfigValues(@NotNull
    String keyRegex);

    /**
     * reads the etc/ode.properties file and returns all the key/value
     * pairs that are found there. Since this file is not to be edited
     * its assumed that these values are in the public domain and so
     * there's no need to protect them.
     *
     * @return a {@link Map} from the found keys to the linked values.
     */
    Map<String, String> getConfigDefaults();

    /**
     * retrieves configuration values like {@link #getConfigValues(String)}
     * but only those with the prefix "ode.client".
     *
     * @return a {@link Map} from the found keys to the linked values.
     */
    Map<String, String> getClientConfigValues();

    /**
     * reads the etc/ode.properties file and returns all the key/value
     * pairs that are found there which match the prefix "ode.client".
     *
     * @return a {@link Map} from the found keys to the linked values.
     */
    Map<String, String> getClientConfigDefaults();

    /**
     * set a configuration value in the backend store. Permissions applied to
     * the configuration value may cause a {@link SecurityViolation} to be
     * thrown. If the value is null or empty, then the configuration will be
     * removed in all writable configuration sources. If the configuration is
     * set in a non-modifiable source (e.g. in a property file on the classpath),
     * then a subsequent call to getConfigValue() will return that value.
     *
     * @param key
     *            The non-null name of the desired configuration value
     * @param value
     *            The {@link String} value to assign to the given key.
     * @throws ApiUsageException
     *             if the key is null or invalid.
     * @throws SecurityViolation
     *             if the value is not writable.
     */
    void setConfigValue(@NotNull
    String key, String value) throws ApiUsageException, SecurityViolation;

    /**
     * Calls {@link #setConfigValue(String, String)} if and only if the
     * configuration property is currently equal to the test argument. If the
     * test is null or empty, then the configuration property will be set only
     * if missing.
     *
     * @param key
     * @param value
     * @param test
     * @return See above.
     * @throws ApiUsageException
     * @throws SecurityViolation
     * @see #setConfigValue(String, String)
     */
    boolean setConfigValueIfEquals(@NotNull
    String key, String value, String test) throws ApiUsageException, SecurityViolation;

    /**
     * Provides the release version. Bhojpur ODE-internal values will be in the form
     * Major.minor.patch, starting with the value 4.0.0 for the 4.0 release,
     * Spring 2009.
     *
     * Customized values should begin with a alphabetic sequence followed by a
     * hyphen: ACME-0.0.1 and any build information should follow the patch
     * number also with a hyphen: 4.0.0-RC1. These values will be removed by
     * {@link #getVersion()}
     *
     * @return Returns the release version
     * @see #VERSION_REGEX
     */
    String getVersion();

    /**
     * Provides the UUID for this Bhojpur ODE (database) instance. To make imports and
     * exports function properly, only one physical database should be active
     * with a given instance UUID. All other copies of the database with that
     * UUID are invalid as soon as one modification is made.
     *
     * This value is stored in the configuration table under the key
     * "ode.db.uuid"
     *
     * @return String not null.
     */
    String getDatabaseUuid();

}