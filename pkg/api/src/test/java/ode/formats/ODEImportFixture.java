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

// Java imports
import java.io.File;
import java.util.List;

import loci.formats.FormatReader;
import ode.formats.importer.IObservable;
import ode.formats.importer.ImportCandidates;
import ode.formats.importer.ImportConfig;
import ode.formats.importer.ImportEvent;
import ode.formats.importer.ImportLibrary;
import ode.formats.importer.ODEWrapper;
import ode.formats.importer.util.ErrorHandler; // Was previously cli for sending debug text
import ode.formats.importer.util.ErrorHandler.EXCEPTION_EVENT;
import ode.model.Pixels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class ODEImportFixture {

    Logger log = LoggerFactory.getLogger(ODEImportFixture.class);

    protected ODEMetadataStoreClient store;

    private ODEWrapper reader;

    private ImportLibrary library;

    private File file;

    private List<Pixels> pixels;

    private EXCEPTION_EVENT exception = null;

    @SuppressWarnings("unused")
	private String name;

    public ODEImportFixture(ODEMetadataStoreClient store,
            ODEWrapper reader) {
        this.store = store;
        this.reader = reader;
    }

    /**
     * checks for the necessary fields and initializes the {@link ImportLibrary}
     *
     * @throws Exception if the import library could not be instantiated
     */
    public void setUp() throws Exception {
        this.library = new ImportLibrary(store, reader);
    }

    /**
     * sets {@link ImportLibrary}, {@link ODEMetadataStore}, and file array to
     * null. Also attempts to call {@link FormatReader#close()}.
     */
    public void tearDown() {
        if (this.store != null) {
            this.store.logout();
            this.store = null;
        }

        this.library = null;

        try {
            if (this.reader != null) {
                this.reader.close();
                this.reader = null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            this.reader = null;
        }
    }

    /**
     * Provides one complete import cycle.
     * @param f the file to import
     * @param name the name (ignored)
     * @return the {@link Pixels} instance(s) created by the import
     * @throws Exception if the import failed
     */
    public List<Pixels> fullImport(File f, String name) throws Exception {
        this.setUp();
        try {
            this.setFile(f);
            this.setName(name);
            this.doImport();
            return this.getPixels();
        } finally {
            this.tearDown();
        }
    }

    /**
     * Runs import by looping through all files and then calling
     * {@link ImportLibrary#importCandidates(ImportConfig, ImportCandidates)}.
     * @throws Exception if import failed in a way that is not handled by an {@link EXCEPTION_EVENT}
     */
    public void doImport() throws Exception {
        String fileName = file.getAbsolutePath();
        ImportConfig config = new ImportConfig();
        ErrorHandler handler = new ErrorHandler(config) {
            @Override
            public void onUpdate(IObservable importLibrary, ImportEvent event) {
                // super.onUpdate(importLibrary, event);
                // Was previously cli.ErrorHandler, which sends feedback.
                if (event instanceof ImportEvent.IMPORT_DONE) {
                    pixels = ((ImportEvent.IMPORT_DONE) event).pixels;
                } else if (event instanceof EXCEPTION_EVENT) {
                    exception = (EXCEPTION_EVENT) event;
                }
            }
        };
        ImportCandidates candidates = new ImportCandidates(reader, new String[]{fileName}, handler);
        library.addObserver(handler);
        library.importCandidates(config, candidates);
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Accessor for the created pixels. Should be called before the next call to
     * {@link #doImport()}
     * @return the {@link Pixels} instance(s) created by the import
     * @throws Exception from an {@link EXCEPTION_EVENT} if the import failed
     */
    public List<Pixels> getPixels() throws Exception {
        if (exception != null) {
            throw exception.exception;
        }
        return this.pixels;
    }

}