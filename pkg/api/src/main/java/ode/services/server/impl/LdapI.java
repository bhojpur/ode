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
import java.util.List;

import ode.api.ILdap;
import ode.services.server.util.ServerExecutor;
import ode.ServerError;
import ode.api.AMD_ILdap_createUser;
import ode.api.AMD_ILdap_discover;
import ode.api.AMD_ILdap_discoverGroups;
import ode.api.AMD_ILdap_findDN;
import ode.api.AMD_ILdap_findExperimenter;
import ode.api.AMD_ILdap_findGroup;
import ode.api.AMD_ILdap_findGroupDN;
import ode.api.AMD_ILdap_getSetting;
import ode.api.AMD_ILdap_searchAll;
import ode.api.AMD_ILdap_searchByAttribute;
import ode.api.AMD_ILdap_searchByAttributes;
import ode.api.AMD_ILdap_searchByDN;
import ode.api.AMD_ILdap_searchDnInGroups;
import ode.api.AMD_ILdap_setDN;
import ode.api._ILdapOperations;
import Ice.Current;

/**
 * Implementation of the ILdap service.
 * 
 * @see ode.api.ILdap
 */
public class LdapI extends AbstractAmdServant implements _ILdapOperations {

    public LdapI(ILdap service, ServerExecutor be) {
        super(service, be);
    }

    // Interface methods
    // =========================================================================

    /*
     * public void checkAttributes_async(AMD_ILdap_checkAttributes __cb, String
     * dn, List<String> attrs, Current __current) throws ServerError {
     * 
     * callInvokerOnRawArgs(__cb, __current, dn, attrs); }
     */

    public void searchAll_async(AMD_ILdap_searchAll __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void searchDnInGroups_async(AMD_ILdap_searchDnInGroups __cb,
            String attribute, String value, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, attribute, value);
    }

    public void searchByAttribute_async(AMD_ILdap_searchByAttribute __cb,
            String dn, String attribute, String value, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, dn, attribute, value);
    }

    public void searchByAttributes_async(AMD_ILdap_searchByAttributes __cb,
            String dn, List<String> attributes, List<String> values,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, dn, attributes, values);
    }

    public void searchByDN_async(AMD_ILdap_searchByDN __cb, String userdn,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, userdn);
    }

    public void findDN_async(AMD_ILdap_findDN __cb, String username,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, username);
    }

    public void findGroupDN_async(AMD_ILdap_findGroupDN __cb, String groupname,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, groupname);
    }

    public void findExperimenter_async(AMD_ILdap_findExperimenter __cb,
            String username, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, username);
    }

    public void findGroup_async(AMD_ILdap_findGroup __cb, String groupname,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, groupname);
    }

    public void setDN_async(AMD_ILdap_setDN __cb, ode.RLong experimenterID,
            String dn, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, experimenterID, dn);
    }

    public void getSetting_async(AMD_ILdap_getSetting __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void createUser_async(AMD_ILdap_createUser __cb,
            String username, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, username);
    }

    public void discover_async(AMD_ILdap_discover __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void discoverGroups_async(AMD_ILdap_discoverGroups __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

}