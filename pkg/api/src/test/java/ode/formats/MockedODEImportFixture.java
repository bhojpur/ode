package ode.formats;

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

import ode.formats.importer.ImportConfig;
import ode.formats.importer.ODEWrapper;
import ode.services.server.test.mock.MockFixture;
import ode.system.EventContext;
import ode.system.OdeContext;
import ode.system.ServiceFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jmock.MockObjectTestCase;

/**
 * test fixture for importing files without a GUI. Sample usage:
 * 
 * <pre>
 * ODEMetadataStoreClient client = new ODEMetadataStoreClient(sf);
 * ODEImportFixture fixture = new ODEImportFixture(client);
 * fixture.setUp();
 * fixture.setFile(ResourceUtils.getFile(&quot;classpath:tinyTest.d3d.dv&quot;));
 * fixture.setName(name);
 * fixture.doImport();
 * List&lt;Pixels&gt; p = fixture.getPixels();
 * fixture.tearDown();
 * i.setName(name);
 * i = userSave(i);
 * </pre>
 * 
 * This class is <em>not</em> thread safe.
 */
public class MockedODEImportFixture extends ODEImportFixture {

    Logger log = LoggerFactory.getLogger(MockedODEImportFixture.class);

    ode.client client;

    /**
     * Constructor for use when no server is available, like from server-side
     * tests.
     */
    public MockedODEImportFixture(ServiceFactory sf, String password)
            throws Exception {
        super(mockStore(sf, password), new ODEWrapper(new ImportConfig()));
    }

    public static ODEMetadataStoreClient mockStore(ServiceFactory sf,
            String password) throws Exception {

        System.setProperty("ode.testing", "true");
        OdeContext inner = OdeContext.getManagedServerContext();
        OdeContext outer = new OdeContext(new String[] {
                "classpath:ode/services/messaging.xml", // To share events
                "classpath:ode/formats/fixture.xml",
                "classpath:ode/services/server-servantDefinitions.xml",
                "classpath:ode/services/server-graph-rules.xml",
                "classpath:ode/services/throttling/throttling.xml",
                "classpath:ode/config.xml" }, false);
        outer.setParent(inner);
        outer.refresh();

        EventContext ec = sf.getAdminService().getEventContext();
        String username = ec.getCurrentUserName();
        long groupid = ec.getCurrentGroupId();

        MockFixture fixture = new MockFixture(new MockObjectTestCase() {
        }, outer);
        ode.client client = fixture.newClient();
        // Fixing group permissions from 4.2.0
        client.createSession(username, password).setSecurityContext(
                new ode.model.ExperimenterGroupI(groupid, false));
        ODEMetadataStoreClient store = new ODEMetadataStoreClient();
        store.initialize(client);
        return store;
    }

    @Override
    public void tearDown() {
        try {
            super.tearDown();
        } catch (Exception e) {
            log.error("Error on tearDown in store.logout()", e);
        }
    }
}