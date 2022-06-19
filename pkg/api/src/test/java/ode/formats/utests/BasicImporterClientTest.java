package ode.formats.utests;

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

import ode.formats.ODEMetadataStoreClient;
import ode.formats.importer.ImportCandidates;
import ode.formats.importer.ImportConfig;
import ode.formats.importer.ImportFixture;
import ode.formats.importer.ImportLibrary;
import ode.formats.importer.ODEWrapper;
import ode.formats.importer.cli.ErrorHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * Simple tests which show the basic way to configure a client.
 *
 * @see ImportFixture
 * @see ImportReader
 * @see ImportConfig
 * @see ImportLibrary
 * @see ImportCandidates
 * @see ODEWrapper
 * @see ODEMetadataStoreClient
 */
@Test(groups={"manual"})
public class BasicImporterClientTest {

    Logger log = LoggerFactory.getLogger(BasicImporterClientTest.class);

    @Test
    public void testSimpleClientWthErrorHandling() throws Exception {

        final ImportConfig config = new ImportConfig();
        final ErrorHandler handler = new ErrorHandler(config);

        ODEMetadataStoreClient client = null;
        try {
            client = config.createStore();
            ODEWrapper reader = new ODEWrapper(config);
            ImportCandidates candidates = new ImportCandidates(reader, new String[] {}, handler);
            ImportLibrary library = new ImportLibrary(client, reader);

            // importCandidates never throws exception
            library.importCandidates(config, candidates);

            // so to handle exceptions, we register an observer.
            library.addObserver(new ErrorHandler(config));
            library.importCandidates(config, candidates);

        } finally {
            if (client != null) {
                client.logout();
            }
            config.saveAll();
        }

    }

}