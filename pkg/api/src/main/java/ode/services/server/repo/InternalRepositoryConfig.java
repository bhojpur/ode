package ode.services.server.repo;

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

import ode.services.server.fire.Registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Ice.ObjectAdapter;

/**
 * Simple configuration class which is created via repository.xml Spring
 * configuration file after the Bhojpur ODE.repository context is loaded. Used as a
 * factory for creating the InternalRepository, similar to ServerConfiguration.
 */
public class InternalRepositoryConfig {

    private final static Logger log = LoggerFactory
            .getLogger(InternalRepositoryConfig.class);

    private final Ice.InitializationData id;

    private final Ice.Communicator ic;

    private final Ice.ObjectAdapter oa;

    private final Registry reg;

    public InternalRepositoryConfig(String repoDir) throws Exception {

        //
        // Ice Initialization
        //
        id = new Ice.InitializationData();
        id.properties = Ice.Util.createProperties();
        String ICE_CONFIG = System.getProperty("ICE_CONFIG");
        if (ICE_CONFIG != null) {
            id.properties.load(ICE_CONFIG);
        }
        ic = Ice.Util.initialize(id);

        reg = new Registry.Impl(ic);
        oa = ic.createObjectAdapter("RepositoryAdapter");
        oa.activate();

        /*
         * String serverId =
         * ic.getProperties().getProperty("Ice.Admin.ServerId"); Ice.Identity id
         * = Ice.Util.stringToIdentity(serverId);
         */

    }

    public Ice.Communicator getCommunicator() {
        return ic;
    }

    public ObjectAdapter getObjectAdapter() {
        return oa;
    }

    public Registry getRegistry() {
        return reg;
    }

}