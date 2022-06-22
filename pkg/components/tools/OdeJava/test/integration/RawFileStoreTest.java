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

import java.util.Iterator;
import java.util.List;

import ode.ServerError;
import ode.api.IScriptPrx;
import ode.api.RawFileStorePrx;
import ode.model.OriginalFile;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Collections of tests for the <code>RawFileStore</code> service.
 */
public class RawFileStoreTest extends AbstractServerTest {

    /**
     * Tests the upload of a file. This tests uses the <code>write</code>
     * method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testUploadFile() throws Exception {
        RawFileStorePrx svc = factory.createRawFileStore();
        // create an original file
        OriginalFile f = mmFactory.createOriginalFile();
        f = (OriginalFile) iUpdate.saveAndReturnObject(f);
        svc.setFileId(f.getId().getValue());
        byte[] data = new byte[] { 1 };
        svc.write(data, 0, data.length);
        OriginalFile ff = svc.save(); // save
        Assert.assertEquals(f.getId().getValue(), ff.getId().getValue());
        svc.close();
    }

    /**
     * Tests the download of a file. This tests uses the <code>read</code>
     * method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     * @see #testUploadFile()
     */
    @Test
    public void testDownloadFile() throws Exception {
        // first write the file. See
        RawFileStorePrx svc = factory.createRawFileStore();
        // create an original file
        OriginalFile f = mmFactory.createOriginalFile();
        f = (OriginalFile) iUpdate.saveAndReturnObject(f);
        svc.setFileId(f.getId().getValue());
        byte[] data = new byte[] { 1 };
        svc.write(data, 0, data.length);
        f = svc.save(); // save

        int size = (int) f.getSize().getValue();
        byte[] values = svc.read(0, size);
        Assert.assertNotNull(values);
        Assert.assertEquals(data.length, values.length);
        for (int i = 0; i < values.length; i++) {
            Assert.assertEquals(data[i], values[i]);
        }
        svc.close();
    }

    /**
     * Tests the download of the scripts. This tests uses the <code>read</code>
     * method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     * @see #testUploadFile()
     */
    @Test
    public void testDownloadScript() throws Exception {
        IScriptPrx svc = factory.getScriptService();
        List<OriginalFile> scripts = svc.getScripts();
        Iterator<OriginalFile> i = scripts.iterator();
        OriginalFile f;
        RawFileStorePrx store;
        byte[] values;
        int size;
        Assert.assertFalse(scripts.isEmpty());
        while (i.hasNext()) {
            f = i.next();
            store = factory.createRawFileStore();
            store.setFileId(f.getId().getValue());
            size = (int) f.getSize().getValue();
            values = store.read(0, size);
            Assert.assertNotNull(values);
            Assert.assertNotEquals(values.length, 0);
            store.close();
        }
    }

    /**
     * Test that a sensible exception is thrown when a bad file ID is set.
     * @throws Exception for bad file ID
     */
    @Test(expectedExceptions = ServerError.class)
    public void testBadFileId() throws Exception {
        newUserAndGroup("rw----");
        final RawFileStorePrx rfs = factory.createRawFileStore();
        try {
            rfs.setFileId(-1);
            Assert.fail("should not be able to open file with bad ID");
        } finally {
            rfs.close();
        }
    }
}
