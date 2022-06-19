/*
 * Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

#ifndef ODE_API_ICONFIG_ICE
#define ODE_API_ICONFIG_ICE

#include <ode/RTypes.ice>
#include <ode/ServicesF.ice>

module ode {

    module api {

        /**
         * Access to server configuration. These methods provide access to the
         * state and configuration of the server and its components (e.g. the
         * database). However, it should not be assumed that two subsequent
         * calls to a proxy for this service will go to the same server due to
         * clustering.
         *
         * Not all possible server configuration is available through this
         * API. Some values (such as DB connection info, ports, etc.) must
         * naturally be set before this service is accessible.
         *
         * Manages synchronization of the various configuration sources
         * internally. It is therefore important that as far as possible all
         * configuration changes take place via this interface and not, for
         * example, directly via java.util.prefs.Preferences.
         *
         * Also used as the main developer example for developing (stateless)
         * ode.api interfaces. See source code documentation for more.
         */
        ["ami", "amd"] interface IConfig extends ServiceInterface
            {
                /**
                 * Provides the release version. ODE-internal values will be
                 * in the form Major.minor.patch, starting with the value
                 * 4.0.0 for the 4.0 release, Spring 2009.
                 *
                 * Customized values should begin with a alphabetic sequence
                 * followed by a hyphen: ACME-0.0.1 and any build information
                 * should follow the patch number also with a hyphen:
                 * 4.0.0-RC1. These values will be removed by
                 * {@code getVersion}
                 */
                idempotent string getVersion() throws ServerError;

                /**
                 * Retrieves a configuration value from the backend store.
                 * Permissions applied to the configuration value may cause a
                 * {@link ode.SecurityViolation} to be thrown.
                 *
                 * @param key The non-null name of the desired configuration
                 *        value
                 * @return The string value linked to this key, possibly null
                 *         if not set.
                 * @throws ApiUsageException if the key is null or invalid.
                 * @throws SecurityViolation if the value for the key is not
                 *         readable.
                 */
                idempotent string getConfigValue(string key) throws ServerError;

                /**
                 * Retrieves configuration values from the backend store which
                 * match the given regex. Any configuration value which would
                 * throw an exception on being loaded is omitted.
                 *
                 * @param keyRegex The non-null regex of the desired
                 *        configuration values
                 * @return a map from the found keys to the linked values.
                 */
                idempotent ode::api::StringStringMap getConfigValues(string keyRegex) throws ServerError;

                /**
                 * Reads the etc/ode.properties file and returns all the
                 * key/value pairs that are found there. Since this file is
                 * not to be edited its assumed that these values are in the
                 * public domain and so there's no need to protect them.
                 *
                 * @return a map from the found keys to the linked values.
                 */
                idempotent ode::api::StringStringMap getConfigDefaults() throws ServerError;

                /**
                 * Retrieves configuration values like {@code getConfigValues}
                 * but only those with the prefix <i>ode.client</i>.
                 *
                 * @return a map from the found keys to the linked values.
                 */
                idempotent ode::api::StringStringMap getClientConfigValues() throws ServerError;

                /**
                 * Reads the etc/ode.properties file and returns all the
                 * key/value pairs that are found there which match the prefix
                 * <i>ode.client</i>.
                 *
                 * @return a map from the found keys to the linked values.
                 */
                idempotent ode::api::StringStringMap getClientConfigDefaults() throws ServerError;

                /**
                 * Sets a configuration value in the backend store.
                 * Permissions applied to the configuration value may cause a
                 * {@link ode.SecurityViolation} to be thrown. If the value is
                 * null or empty, then the configuration will be removed in
                 * all writable configuration sources. If the configuration is
                 * set in a non-modifiable source (e.g. in a property file on
                 * the classpath), then a subsequent call to
                 * {@code getConfigValue} will return that value.
                 *
                 * @param key The non-null name of the desired configuration
                 *        value
                 * @param value The string value to assign to the given key.
                 * @throws ApiUsageException if the key is null or invalid.
                 * @throws SecurityViolation if the value is not writable.
                 */
                idempotent void setConfigValue(string key, string value) throws ServerError;

                /**
                 * Calls {@code setConfigValue} if and only if the
                 * configuration property is currently equal to the test
                 * argument. If the test is null or empty, then the
                 * configuration property will be set only if missing.
                 *
                 * @param key
                 * @param value
                 * @throws ApiUsageException
                 * @throws SecurityViolation
                 * @see #setConfigValue
                 */
                idempotent bool setConfigValueIfEquals(string key, string value, string test) throws ServerError;

                /**
                 * Provides the UUID for this ODE (database) instance. To
                 * make imports and exports function properly, only one
                 * physical database should be active with a given instance
                 * UUID. All other copies of the database with that UUID are
                 * invalid as soon as one modification is made.
                 *
                 * This value is stored in the configuration table under the
                 * key <i>ode.db.uuid</i>.
                 *
                 * @return String not null.
                 */
                idempotent string getDatabaseUuid() throws ServerError;

                /**
                 * Checks the database for its time using a SELECT statement.
                 *
                 * @return Non-null {@link ode.RTime} representation of the
                 *         database time.
                 * @throws InternalException though any call can throw an
                 *         InternalException it is more likely that this can
                 *         occur while contacting the DB. An exception here
                 *         most likely means (A) a temporary issue with the DB
                 *         or (B) a SQL dialect issue which must be corrected
                 *         by the Bhojpur ODE team.
                 */
                idempotent ode::RTime getDatabaseTime() throws ServerError;

                /**
                 * Checks the current server for its time. This value may be
                 * variant depending on whether the service is clustered or
                 * not.
                 *
                 * @return Non-null {@link ode.RTime} representation of the
                 *         server's own time.
                 */
                idempotent ode::RTime getServerTime() throws ServerError;
            };

    };
};

#endif