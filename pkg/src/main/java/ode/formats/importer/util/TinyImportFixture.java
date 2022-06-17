package ode.formats.importer.util;

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
import java.io.FileNotFoundException;
import java.util.UUID;

import ode.model.containers.Dataset;
import ode.model.core.Pixels;
import ode.system.ServiceFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

/**
 * test fixture which uses a hard-coded file ("tinyTest.d3d.dv") from the
 * classpath, and adds them to a new UUID-named dataset.
 */
@Deprecated
public class TinyImportFixture
{

    /** Hard-coded filename of the image to be imported */
    public final static String FILENAME = "tinyTest.d3d.dv";

    Logger log = LoggerFactory.getLogger(TinyImportFixture.class);

    private Dataset d;

    private ServiceFactory sf;

    public TinyImportFixture(ServiceFactory services) throws Exception
    {
        this.sf = services;
    }

    /**
     * Creates a dataset and locates the test image file.
     * @throws FileNotFoundException if the test image file could not be found
     */
    public void setUp() throws FileNotFoundException
    {
        d = new Dataset();
        d.setName(UUID.randomUUID().toString());
        d = sf.getUpdateService().saveAndReturnObject(d);

        File tinyTest = ResourceUtils.getFile("classpath:"+FILENAME);
    }

    public void doImport() {}
    public void tearDown() {}

    /** provides access to the created {@link Dataset} instance.
     * @return the dataset
     */
    public Dataset getDataset()
    {
        return d;
    }

    public Pixels getPixels()
    {
        return sf.getQueryService().findByQuery("select p from Dataset d " +
            "join d.imageLinks dil " +
            "join dil.child img " +
            "join img.pixels p where d.id = "+d.getId(), null);
    }
}