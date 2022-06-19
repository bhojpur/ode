package ode.formats.importer;

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
import java.util.HashMap;
import java.util.Map;

import loci.formats.FormatReader;
import ode.formats.ODEMetadataStoreClient;
import ode.model.Dataset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * test fixture for importing files without a GUI.
 *
 * @see ODEMetadataStoreClient
 */
public class ImportFixture
{

    Logger                        log = LoggerFactory.getLogger(ImportFixture.class);

    private ODEMetadataStoreClient store;

    private ODEWrapper       reader;

    private ImportLibrary      library;

    private Map<File,Dataset>  fads = new HashMap<File, Dataset>();

    public ImportFixture(ODEMetadataStoreClient store, ODEWrapper reader)
    {
        this.store = store;
        this.reader = reader;
    }

    public ImportFixture put(File file, Dataset ds)
    {
	if ( file == null || ds == null )
	    // FIXME: Bhojpur ODE transition, ApiUsageException no longer client side.
		throw new RuntimeException("Arguments cannot be null.");

	fads.put(file, ds);
	return this;
    }

    public ImportFixture putAll(Map<File, Dataset> map)
    {
	for (File f : map.keySet()) {
			put(f,map.get(f));
		}
	return this;
    }

    /**
     * checks for the necessary fields and initializes the {@link ImportLibrary}
     *
     * @throws Exception if setup failed
     */
    public void setUp() throws Exception
    {
        this.library = new ImportLibrary(store, reader);
    }

    /**
     * sets {@link ImportLibrary}, {@link ODEMetadataStoreClient}, and file array
     * to null. Also attempts to call {@link FormatReader#close()}.
     */
    public void tearDown()
    {
        this.fads = null;
        this.store = null;
        this.library = null;
        try
        {
            this.reader.close();
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        } finally
        {
            this.reader = null;
        }
    }

    /**
     * Runs import by looping through all files and then calling
     * {@link ImportLibrary#importImage(ImportContainer, int, int, int)}.
     * @throws Throwable reporting a problem with import
     */
    public void doImport() throws Throwable
    {
    	ImportContainer ic;
        for (File file : fads.keySet())
        {
		ic = new ImportContainer(file, fads.get(file),
					null, null, null, null);
		library.importImage(ic, 0, 0, 1);
        /*
		library.importImage(file, 0, 0, 1, file.getAbsolutePath(),
				        null,
				        false,  // To archive?
				        false,  // Create a metadata file?
				        null,
				        fads.get(file));
				        */
        }
    }

}