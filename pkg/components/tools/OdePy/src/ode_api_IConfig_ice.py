#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.

from sys import version_info as _version_info_
import Ice, IcePy
import ode_RTypes_ice
import ode_ServicesF_ice

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module Glacier2
_M_Glacier2 = Ice.openModule('Glacier2')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')

# Included module ode.api
_M_ode.api = Ice.openModule('ode.api')

# Included module ode.grid
_M_ode.grid = Ice.openModule('ode.grid')

# Start of module ode
__name__ = 'ode'

# Start of module ode.api
__name__ = 'ode.api'

if 'IConfig' not in _M_ode.api.__dict__:
    _M_ode.api.IConfig = Ice.createTempClass()
    class IConfig(_M_ode.api.ServiceInterface):
        """
        Access to server configuration. These methods provide access to the
        state and configuration of the server and its components (e.g. the
        database). However, it should not be assumed that two subsequent
        calls to a proxy for this service will go to the same server due to
        clustering.
        Not all possible server configuration is available through this
        API. Some values (such as DB connection info, ports, etc.) must
        naturally be set before this service is accessible.
        Manages synchronization of the various configuration sources
        internally. It is therefore important that as far as possible all
        configuration changes take place via this interface and not, for
        example, directly via java.util.prefs.Preferences.
        Also used as the main developer example for developing (stateless)
        ode.api interfaces. See source code documentation for more.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.IConfig:
                raise RuntimeError('ode.api.IConfig is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::IConfig', '::ode::api::ServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::IConfig'

        def ice_staticId():
            return '::ode::api::IConfig'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion_async(self, _cb, current=None):
            """
            Provides the release version. ODE-internal values will be
            in the form Major.minor.patch, starting with the value
            4.0.0 for the 4.0 release, Spring 2009.
            Customized values should begin with a alphabetic sequence
            followed by a hyphen: ACME-0.0.1 and any build information
            should follow the patch number also with a hyphen:
            4.0.0-RC1. These values will be removed by
            {@code getVersion}
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getConfigValue_async(self, _cb, key, current=None):
            """
            Retrieves a configuration value from the backend store.
            Permissions applied to the configuration value may cause a
            ode.SecurityViolation to be thrown.
            Arguments:
            _cb -- The asynchronous callback object.
            key -- The non-null name of the desired configuration value
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if the key is null or invalid.
            SecurityViolation -- if the value for the key is not readable.
            """
            pass

        def getConfigValues_async(self, _cb, keyRegex, current=None):
            """
            Retrieves configuration values from the backend store which
            match the given regex. Any configuration value which would
            throw an exception on being loaded is omitted.
            Arguments:
            _cb -- The asynchronous callback object.
            keyRegex -- The non-null regex of the desired configuration values
            current -- The Current object for the invocation.
            """
            pass

        def getConfigDefaults_async(self, _cb, current=None):
            """
            Reads the etc/ode.properties file and returns all the
            key/value pairs that are found there. Since this file is
            not to be edited its assumed that these values are in the
            public domain and so there's no need to protect them.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getClientConfigValues_async(self, _cb, current=None):
            """
            Retrieves configuration values like {@code getConfigValues}
            but only those with the prefix ode.client.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getClientConfigDefaults_async(self, _cb, current=None):
            """
            Reads the etc/ode.properties file and returns all the
            key/value pairs that are found there which match the prefix
            ode.client.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def setConfigValue_async(self, _cb, key, value, current=None):
            """
            Sets a configuration value in the backend store.
            Permissions applied to the configuration value may cause a
            ode.SecurityViolation to be thrown. If the value is
            null or empty, then the configuration will be removed in
            all writable configuration sources. If the configuration is
            set in a non-modifiable source (e.g. in a property file on
            the classpath), then a subsequent call to
            {@code getConfigValue} will return that value.
            Arguments:
            _cb -- The asynchronous callback object.
            key -- The non-null name of the desired configuration value
            value -- The string value to assign to the given key.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if the key is null or invalid.
            SecurityViolation -- if the value is not writable.
            """
            pass

        def setConfigValueIfEquals_async(self, _cb, key, value, test, current=None):
            """
            Calls {@code setConfigValue} if and only if the
            configuration property is currently equal to the test
            argument. If the test is null or empty, then the
            configuration property will be set only if missing.
            Arguments:
            _cb -- The asynchronous callback object.
            key -- 
            value -- 
            test -- 
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- 
            SecurityViolation -- 
            """
            pass

        def getDatabaseUuid_async(self, _cb, current=None):
            """
            Provides the UUID for this ODE (database) instance. To
            make imports and exports function properly, only one
            physical database should be active with a given instance
            UUID. All other copies of the database with that UUID are
            invalid as soon as one modification is made.
            This value is stored in the configuration table under the
            key ode.db.uuid.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getDatabaseTime_async(self, _cb, current=None):
            """
            Checks the database for its time using a SELECT statement.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            Throws:
            InternalException -- though any call can throw an InternalException it is more likely that this can occur while contacting the DB. An exception here most likely means (A) a temporary issue with the DB or (B) a SQL dialect issue which must be corrected by the Bhojpur ODE team.
            """
            pass

        def getServerTime_async(self, _cb, current=None):
            """
            Checks the current server for its time. This value may be
            variant depending on whether the service is clustered or
            not.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_IConfig)

        __repr__ = __str__

    _M_ode.api.IConfigPrx = Ice.createTempClass()
    class IConfigPrx(_M_ode.api.ServiceInterfacePrx):

        """
        Provides the release version. ODE-internal values will be
        in the form Major.minor.patch, starting with the value
        4.0.0 for the 4.0 release, Spring 2009.
        Customized values should begin with a alphabetic sequence
        followed by a hyphen: ACME-0.0.1 and any build information
        should follow the patch number also with a hyphen:
        4.0.0-RC1. These values will be removed by
        {@code getVersion}
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getVersion(self, _ctx=None):
            return _M_ode.api.IConfig._op_getVersion.invoke(self, ((), _ctx))

        """
        Provides the release version. ODE-internal values will be
        in the form Major.minor.patch, starting with the value
        4.0.0 for the 4.0 release, Spring 2009.
        Customized values should begin with a alphabetic sequence
        followed by a hyphen: ACME-0.0.1 and any build information
        should follow the patch number also with a hyphen:
        4.0.0-RC1. These values will be removed by
        {@code getVersion}
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IConfig._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Provides the release version. ODE-internal values will be
        in the form Major.minor.patch, starting with the value
        4.0.0 for the 4.0 release, Spring 2009.
        Customized values should begin with a alphabetic sequence
        followed by a hyphen: ACME-0.0.1 and any build information
        should follow the patch number also with a hyphen:
        4.0.0-RC1. These values will be removed by
        {@code getVersion}
        Arguments:
        """
        def end_getVersion(self, _r):
            return _M_ode.api.IConfig._op_getVersion.end(self, _r)

        """
        Retrieves a configuration value from the backend store.
        Permissions applied to the configuration value may cause a
        ode.SecurityViolation to be thrown.
        Arguments:
        key -- The non-null name of the desired configuration value
        _ctx -- The request context for the invocation.
        Returns: The string value linked to this key, possibly null if not set.
        Throws:
        ApiUsageException -- if the key is null or invalid.
        SecurityViolation -- if the value for the key is not readable.
        """
        def getConfigValue(self, key, _ctx=None):
            return _M_ode.api.IConfig._op_getConfigValue.invoke(self, ((key, ), _ctx))

        """
        Retrieves a configuration value from the backend store.
        Permissions applied to the configuration value may cause a
        ode.SecurityViolation to be thrown.
        Arguments:
        key -- The non-null name of the desired configuration value
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getConfigValue(self, key, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IConfig._op_getConfigValue.begin(self, ((key, ), _response, _ex, _sent, _ctx))

        """
        Retrieves a configuration value from the backend store.
        Permissions applied to the configuration value may cause a
        ode.SecurityViolation to be thrown.
        Arguments:
        key -- The non-null name of the desired configuration value
        Returns: The string value linked to this key, possibly null if not set.
        Throws:
        ApiUsageException -- if the key is null or invalid.
        SecurityViolation -- if the value for the key is not readable.
        """
        def end_getConfigValue(self, _r):
            return _M_ode.api.IConfig._op_getConfigValue.end(self, _r)

        """
        Retrieves configuration values from the backend store which
        match the given regex. Any configuration value which would
        throw an exception on being loaded is omitted.
        Arguments:
        keyRegex -- The non-null regex of the desired configuration values
        _ctx -- The request context for the invocation.
        Returns: a map from the found keys to the linked values.
        """
        def getConfigValues(self, keyRegex, _ctx=None):
            return _M_ode.api.IConfig._op_getConfigValues.invoke(self, ((keyRegex, ), _ctx))

        """
        Retrieves configuration values from the backend store which
        match the given regex. Any configuration value which would
        throw an exception on being loaded is omitted.
        Arguments:
        keyRegex -- The non-null regex of the desired configuration values
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getConfigValues(self, keyRegex, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IConfig._op_getConfigValues.begin(self, ((keyRegex, ), _response, _ex, _sent, _ctx))

        """
        Retrieves configuration values from the backend store which
        match the given regex. Any configuration value which would
        throw an exception on being loaded is omitted.
        Arguments:
        keyRegex -- The non-null regex of the desired configuration values
        Returns: a map from the found keys to the linked values.
        """
        def end_getConfigValues(self, _r):
            return _M_ode.api.IConfig._op_getConfigValues.end(self, _r)

        """
        Reads the etc/ode.properties file and returns all the
        key/value pairs that are found there. Since this file is
        not to be edited its assumed that these values are in the
        public domain and so there's no need to protect them.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: a map from the found keys to the linked values.
        """
        def getConfigDefaults(self, _ctx=None):
            return _M_ode.api.IConfig._op_getConfigDefaults.invoke(self, ((), _ctx))

        """
        Reads the etc/ode.properties file and returns all the
        key/value pairs that are found there. Since this file is
        not to be edited its assumed that these values are in the
        public domain and so there's no need to protect them.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getConfigDefaults(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IConfig._op_getConfigDefaults.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Reads the etc/ode.properties file and returns all the
        key/value pairs that are found there. Since this file is
        not to be edited its assumed that these values are in the
        public domain and so there's no need to protect them.
        Arguments:
        Returns: a map from the found keys to the linked values.
        """
        def end_getConfigDefaults(self, _r):
            return _M_ode.api.IConfig._op_getConfigDefaults.end(self, _r)

        """
        Retrieves configuration values like {@code getConfigValues}
        but only those with the prefix ode.client.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: a map from the found keys to the linked values.
        """
        def getClientConfigValues(self, _ctx=None):
            return _M_ode.api.IConfig._op_getClientConfigValues.invoke(self, ((), _ctx))

        """
        Retrieves configuration values like {@code getConfigValues}
        but only those with the prefix ode.client.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getClientConfigValues(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IConfig._op_getClientConfigValues.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Retrieves configuration values like {@code getConfigValues}
        but only those with the prefix ode.client.
        Arguments:
        Returns: a map from the found keys to the linked values.
        """
        def end_getClientConfigValues(self, _r):
            return _M_ode.api.IConfig._op_getClientConfigValues.end(self, _r)

        """
        Reads the etc/ode.properties file and returns all the
        key/value pairs that are found there which match the prefix
        ode.client.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: a map from the found keys to the linked values.
        """
        def getClientConfigDefaults(self, _ctx=None):
            return _M_ode.api.IConfig._op_getClientConfigDefaults.invoke(self, ((), _ctx))

        """
        Reads the etc/ode.properties file and returns all the
        key/value pairs that are found there which match the prefix
        ode.client.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getClientConfigDefaults(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IConfig._op_getClientConfigDefaults.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Reads the etc/ode.properties file and returns all the
        key/value pairs that are found there which match the prefix
        ode.client.
        Arguments:
        Returns: a map from the found keys to the linked values.
        """
        def end_getClientConfigDefaults(self, _r):
            return _M_ode.api.IConfig._op_getClientConfigDefaults.end(self, _r)

        """
        Sets a configuration value in the backend store.
        Permissions applied to the configuration value may cause a
        ode.SecurityViolation to be thrown. If the value is
        null or empty, then the configuration will be removed in
        all writable configuration sources. If the configuration is
        set in a non-modifiable source (e.g. in a property file on
        the classpath), then a subsequent call to
        {@code getConfigValue} will return that value.
        Arguments:
        key -- The non-null name of the desired configuration value
        value -- The string value to assign to the given key.
        _ctx -- The request context for the invocation.
        Throws:
        ApiUsageException -- if the key is null or invalid.
        SecurityViolation -- if the value is not writable.
        """
        def setConfigValue(self, key, value, _ctx=None):
            return _M_ode.api.IConfig._op_setConfigValue.invoke(self, ((key, value), _ctx))

        """
        Sets a configuration value in the backend store.
        Permissions applied to the configuration value may cause a
        ode.SecurityViolation to be thrown. If the value is
        null or empty, then the configuration will be removed in
        all writable configuration sources. If the configuration is
        set in a non-modifiable source (e.g. in a property file on
        the classpath), then a subsequent call to
        {@code getConfigValue} will return that value.
        Arguments:
        key -- The non-null name of the desired configuration value
        value -- The string value to assign to the given key.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setConfigValue(self, key, value, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IConfig._op_setConfigValue.begin(self, ((key, value), _response, _ex, _sent, _ctx))

        """
        Sets a configuration value in the backend store.
        Permissions applied to the configuration value may cause a
        ode.SecurityViolation to be thrown. If the value is
        null or empty, then the configuration will be removed in
        all writable configuration sources. If the configuration is
        set in a non-modifiable source (e.g. in a property file on
        the classpath), then a subsequent call to
        {@code getConfigValue} will return that value.
        Arguments:
        key -- The non-null name of the desired configuration value
        value -- The string value to assign to the given key.
        Throws:
        ApiUsageException -- if the key is null or invalid.
        SecurityViolation -- if the value is not writable.
        """
        def end_setConfigValue(self, _r):
            return _M_ode.api.IConfig._op_setConfigValue.end(self, _r)

        """
        Calls {@code setConfigValue} if and only if the
        configuration property is currently equal to the test
        argument. If the test is null or empty, then the
        configuration property will be set only if missing.
        Arguments:
        key -- 
        value -- 
        test -- 
        _ctx -- The request context for the invocation.
        Throws:
        ApiUsageException -- 
        SecurityViolation -- 
        """
        def setConfigValueIfEquals(self, key, value, test, _ctx=None):
            return _M_ode.api.IConfig._op_setConfigValueIfEquals.invoke(self, ((key, value, test), _ctx))

        """
        Calls {@code setConfigValue} if and only if the
        configuration property is currently equal to the test
        argument. If the test is null or empty, then the
        configuration property will be set only if missing.
        Arguments:
        key -- 
        value -- 
        test -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setConfigValueIfEquals(self, key, value, test, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IConfig._op_setConfigValueIfEquals.begin(self, ((key, value, test), _response, _ex, _sent, _ctx))

        """
        Calls {@code setConfigValue} if and only if the
        configuration property is currently equal to the test
        argument. If the test is null or empty, then the
        configuration property will be set only if missing.
        Arguments:
        key -- 
        value -- 
        test -- 
        Throws:
        ApiUsageException -- 
        SecurityViolation -- 
        """
        def end_setConfigValueIfEquals(self, _r):
            return _M_ode.api.IConfig._op_setConfigValueIfEquals.end(self, _r)

        """
        Provides the UUID for this ODE (database) instance. To
        make imports and exports function properly, only one
        physical database should be active with a given instance
        UUID. All other copies of the database with that UUID are
        invalid as soon as one modification is made.
        This value is stored in the configuration table under the
        key ode.db.uuid.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: String not null.
        """
        def getDatabaseUuid(self, _ctx=None):
            return _M_ode.api.IConfig._op_getDatabaseUuid.invoke(self, ((), _ctx))

        """
        Provides the UUID for this ODE (database) instance. To
        make imports and exports function properly, only one
        physical database should be active with a given instance
        UUID. All other copies of the database with that UUID are
        invalid as soon as one modification is made.
        This value is stored in the configuration table under the
        key ode.db.uuid.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getDatabaseUuid(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IConfig._op_getDatabaseUuid.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Provides the UUID for this ODE (database) instance. To
        make imports and exports function properly, only one
        physical database should be active with a given instance
        UUID. All other copies of the database with that UUID are
        invalid as soon as one modification is made.
        This value is stored in the configuration table under the
        key ode.db.uuid.
        Arguments:
        Returns: String not null.
        """
        def end_getDatabaseUuid(self, _r):
            return _M_ode.api.IConfig._op_getDatabaseUuid.end(self, _r)

        """
        Checks the database for its time using a SELECT statement.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: Non-null ode.RTime representation of the database time.
        Throws:
        InternalException -- though any call can throw an InternalException it is more likely that this can occur while contacting the DB. An exception here most likely means (A) a temporary issue with the DB or (B) a SQL dialect issue which must be corrected by the Bhojpur ODE team.
        """
        def getDatabaseTime(self, _ctx=None):
            return _M_ode.api.IConfig._op_getDatabaseTime.invoke(self, ((), _ctx))

        """
        Checks the database for its time using a SELECT statement.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getDatabaseTime(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IConfig._op_getDatabaseTime.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Checks the database for its time using a SELECT statement.
        Arguments:
        Returns: Non-null ode.RTime representation of the database time.
        Throws:
        InternalException -- though any call can throw an InternalException it is more likely that this can occur while contacting the DB. An exception here most likely means (A) a temporary issue with the DB or (B) a SQL dialect issue which must be corrected by the Bhojpur ODE team.
        """
        def end_getDatabaseTime(self, _r):
            return _M_ode.api.IConfig._op_getDatabaseTime.end(self, _r)

        """
        Checks the current server for its time. This value may be
        variant depending on whether the service is clustered or
        not.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: Non-null ode.RTime representation of the server's own time.
        """
        def getServerTime(self, _ctx=None):
            return _M_ode.api.IConfig._op_getServerTime.invoke(self, ((), _ctx))

        """
        Checks the current server for its time. This value may be
        variant depending on whether the service is clustered or
        not.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getServerTime(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IConfig._op_getServerTime.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Checks the current server for its time. This value may be
        variant depending on whether the service is clustered or
        not.
        Arguments:
        Returns: Non-null ode.RTime representation of the server's own time.
        """
        def end_getServerTime(self, _r):
            return _M_ode.api.IConfig._op_getServerTime.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.IConfigPrx.ice_checkedCast(proxy, '::ode::api::IConfig', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.IConfigPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::IConfig'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_IConfigPrx = IcePy.defineProxy('::ode::api::IConfig', IConfigPrx)

    _M_ode.api._t_IConfig = IcePy.defineClass('::ode::api::IConfig', IConfig, -1, (), True, False, None, (_M_ode.api._t_ServiceInterface,), ())
    IConfig._ice_type = _M_ode.api._t_IConfig

    IConfig._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_string, False, 0), (_M_ode._t_ServerError,))
    IConfig._op_getConfigValue = IcePy.Operation('getConfigValue', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0),), (), ((), IcePy._t_string, False, 0), (_M_ode._t_ServerError,))
    IConfig._op_getConfigValues = IcePy.Operation('getConfigValues', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode.api._t_StringStringMap, False, 0), (_M_ode._t_ServerError,))
    IConfig._op_getConfigDefaults = IcePy.Operation('getConfigDefaults', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.api._t_StringStringMap, False, 0), (_M_ode._t_ServerError,))
    IConfig._op_getClientConfigValues = IcePy.Operation('getClientConfigValues', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.api._t_StringStringMap, False, 0), (_M_ode._t_ServerError,))
    IConfig._op_getClientConfigDefaults = IcePy.Operation('getClientConfigDefaults', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.api._t_StringStringMap, False, 0), (_M_ode._t_ServerError,))
    IConfig._op_setConfigValue = IcePy.Operation('setConfigValue', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0)), (), None, (_M_ode._t_ServerError,))
    IConfig._op_setConfigValueIfEquals = IcePy.Operation('setConfigValueIfEquals', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0)), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    IConfig._op_getDatabaseUuid = IcePy.Operation('getDatabaseUuid', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_string, False, 0), (_M_ode._t_ServerError,))
    IConfig._op_getDatabaseTime = IcePy.Operation('getDatabaseTime', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode._t_RTime, False, 0), (_M_ode._t_ServerError,))
    IConfig._op_getServerTime = IcePy.Operation('getServerTime', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode._t_RTime, False, 0), (_M_ode._t_ServerError,))

    _M_ode.api.IConfig = IConfig
    del IConfig

    _M_ode.api.IConfigPrx = IConfigPrx
    del IConfigPrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
