package integration;

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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import loci.formats.in.FakeReader;

import ode.formats.ODEMetadataStoreClient;
import ode.formats.importer.ImportConfig;
import ode.formats.importer.ImportContainer;
import ode.formats.importer.ImportLibrary;
import ode.formats.importer.ImportLibrary.ImportCallback;
import ode.formats.importer.ODEWrapper;
import ode.formats.importer.util.ProportionalTimeEstimatorImpl;
import ode.formats.importer.util.TimeEstimator;
import ode.util.checksum.ChecksumProviderFactory;
import ode.util.checksum.ChecksumProviderFactoryImpl;

import ode.cmd.HandlePrx;
import ode.grid.ImportLocation;
import ode.grid.ImportProcessPrx;
import ode.grid.ImportRequest;
import ode.model.IObject;

import org.testng.Assert;

public class AbstractServerImportTest extends AbstractServerTest {

    /**
     * Import the given files. Like {@link #importFileset(List, int)} but with
     * all the srcPaths to be uploaded.
     *
     * @param srcPaths
     *            the source paths
     * @return the resulting import location
     * @throws Exception
     *             unexpected
     */
    protected ImportLocation importFileset(List<String> srcPaths) throws Exception {
        return importFileset(srcPaths, srcPaths.size(), null);
    }

    /**
     * Import the given files.
     *
     * @param srcPaths
     *            the source paths
     * @param numberToUpload
     *            how many of the source paths to actually upload
     * @param targetObject
     *            object (Dataset or Screen) to import the Fileset into
     * @return the resulting import location
     * @throws Exception
     *             unexpected
     */
    protected ImportLocation importFileset(List<String> srcPaths, int numberToUpload, IObject targetObject) throws Exception {

        // Setup that should be easier, most likely a single ctor on IL
        ODEMetadataStoreClient client = new ODEMetadataStoreClient();
        client.initialize(this.client);
        ODEWrapper wrapper = new ODEWrapper(new ImportConfig());
        ImportLibrary lib = new ImportLibrary(client, wrapper);

        // This should also be simplified.
        ImportContainer container = new ImportContainer(new File(
                srcPaths.get(0)), targetObject /* target */, null /* user pixels */,
                FakeReader.class.getName(), srcPaths.toArray(new String[srcPaths.size()]),
                false /* isspw */);

        // Now actually use the library.
        ImportProcessPrx proc = lib.createImport(container);

        // The following is largely a copy of ImportLibrary.importImage
        final String[] srcFiles = container.getUsedFiles();
        final List<String> checksums = new ArrayList<String>();
        final byte[] buf = new byte[client.getDefaultBlockSize()];
        final ChecksumProviderFactory cpf = new ChecksumProviderFactoryImpl();
        final TimeEstimator estimator = new ProportionalTimeEstimatorImpl(
                container.getUsedFilesTotalSize());

        for (int i = 0; i < numberToUpload; i++) {
            checksums.add(lib.uploadFile(proc, srcFiles, i, cpf, estimator,
                    buf));
        }

        // At this point the import is running, check handle for number of
        // steps.
        final HandlePrx handle = proc.verifyUpload(checksums);
        final ImportRequest req = (ImportRequest) handle.getRequest();
        final ImportCallback cb = lib.createCallback(proc, handle, container);
        cb.loop(60 * 60, 1000); // Wait 1 hr per step.
        Assert.assertNotNull(cb.getImportResponse());
        return req.location;
    }
}
