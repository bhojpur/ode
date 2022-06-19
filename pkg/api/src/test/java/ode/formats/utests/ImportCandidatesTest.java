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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ode.formats.ODEMetadataStoreClient;
import ode.formats.importer.IObservable;
import ode.formats.importer.IObserver;
import ode.formats.importer.ImportCandidates;
import ode.formats.importer.ImportConfig;
import ode.formats.importer.ImportContainer;
import ode.formats.importer.ImportEvent;
import ode.formats.importer.ImportFixture;
import ode.formats.importer.ImportLibrary;
import ode.formats.importer.ODEWrapper;
import ode.formats.importer.ImportCandidates.SCANNING;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * candidate processing tests.
 *
 * @see ImportFixture
 * @see ImportReader
 * @see ImportConfig
 * @see ImportLibrary
 * @see ImportCandidates
 * @see ODEWrapper
 * @see ODEMetadataStoreClient
 * @see IObserver
 */
public class ImportCandidatesTest {

    final List<SCANNING> scannings = new ArrayList<SCANNING>();
    Logger log = LoggerFactory.getLogger(ImportCandidatesTest.class);
    ImportCandidates c;

    ImportConfig config = new ImportConfig();
    ODEWrapper w = new ODEWrapper(config);
    IObserver o = new IObserver() {
        public void update(IObservable importLibrary, ImportEvent event) {
            if (event instanceof ImportCandidates.SCANNING) {
                ImportCandidates.SCANNING s = (ImportCandidates.SCANNING) event;
                scannings.add(s);
            }
        }
    };

    static class Canceler implements IObserver {
        public int count = 0;
        public void update(IObservable importLibrary, ImportEvent event) {
            if (event instanceof ImportCandidates.SCANNING) {
                ImportCandidates.SCANNING s = (ImportCandidates.SCANNING) event;
                count++;
                s.cancel();
            }
        }
    };

    private void basic(IObserver obs) {
        URL url = getClass().getResource(getClass().getSimpleName() + ".class");
        String thisClass = url.getFile();
        File file = new File(thisClass);
        file = file.getParentFile().getParentFile().getParentFile();
        c = new ImportCandidates(w, new String[] { file.getAbsolutePath() },
                obs);
    }

    private ImportContainer container(String...usedFiles) {
        File file = new File("a");
        String reader = "";
        Boolean isSPW = false;
        ode.model.IObject target = null;
        return new ImportContainer(file, target,
                null, reader, usedFiles, isSPW);

    }

    @Test
    public void testTwoPasses() throws Exception {
        basic(o);
        // Nothing valid. assertTrue(c.size() > 0);
        Assert.assertTrue(scannings.size() > 0);
    }

    @Test
    public void testCancelFunctions() throws Exception {
        Canceler cancel = new Canceler();
        basic(cancel);
        Assert.assertEquals(c.size(), 0);
        Assert.assertEquals(cancel.count, 1);
        Assert.assertTrue(c.wasCancelled());
    }

    @Test
    public void testOrderedReturns() {
        c = new ImportCandidates(w, new String[]{"a","b"}, o) {
            @Override
            protected ImportContainer singleFile(File file, ImportConfig config) {
                return container(file.getName());
            }
        };
    }

}