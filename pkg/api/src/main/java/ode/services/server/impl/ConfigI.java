package ode.services.server.impl;

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

// Java imports
import ode.api.IConfig;
import ode.services.server.util.ServerExecutor;
import ode.ServerError;
import ode.api.AMD_IConfig_getClientConfigDefaults;
import ode.api.AMD_IConfig_getClientConfigValues;
import ode.api.AMD_IConfig_getConfigDefaults;
import ode.api.AMD_IConfig_getConfigValue;
import ode.api.AMD_IConfig_getConfigValues;
import ode.api.AMD_IConfig_getDatabaseTime;
import ode.api.AMD_IConfig_getDatabaseUuid;
import ode.api.AMD_IConfig_getServerTime;
import ode.api.AMD_IConfig_getVersion;
import ode.api.AMD_IConfig_setConfigValue;
import ode.api.AMD_IConfig_setConfigValueIfEquals;
import ode.api._IConfigOperations;
import Ice.Current;

/**
 * Implementation of the IConfig service.
 * 
 * @see ode.api.IConfig
 */
public class ConfigI extends AbstractAmdServant implements _IConfigOperations {

    public ConfigI(IConfig service, ServerExecutor be) {
        super(service, be);
    }

    // Interface methods
    // =========================================================================

    public void getConfigValue_async(AMD_IConfig_getConfigValue __cb,
            String key, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, key);
    }

    public void getConfigValues_async(AMD_IConfig_getConfigValues __cb,
            String keyRegex, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, keyRegex);
    }

    public void getConfigDefaults_async(AMD_IConfig_getConfigDefaults __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void getClientConfigValues_async(AMD_IConfig_getClientConfigValues __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void getClientConfigDefaults_async(AMD_IConfig_getClientConfigDefaults __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void getDatabaseTime_async(AMD_IConfig_getDatabaseTime __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }
    
    public void getDatabaseUuid_async(AMD_IConfig_getDatabaseUuid __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void getServerTime_async(AMD_IConfig_getServerTime __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void getVersion_async(AMD_IConfig_getVersion __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void setConfigValue_async(AMD_IConfig_setConfigValue __cb,
            String key, String value, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, key, value);
    }

    public void setConfigValueIfEquals_async(
            AMD_IConfig_setConfigValueIfEquals __cb, String key, String value,
            String test, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, key, value, test);
    }

}