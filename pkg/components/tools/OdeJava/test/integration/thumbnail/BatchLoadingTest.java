package integration.thumbnail;

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

import com.google.common.collect.ImmutableList;

import integration.AbstractServerTest;
import integration.ModelMockFactory;
import ode.formats.ODEMetadataStoreClient;
import ode.api.ThumbnailStorePrx;
import ode.model.Pixels;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Collections of tests for the <code>ThumbnailStore</code> service.
 */
public class BatchLoadingTest extends AbstractServerTest {

    /**
     * Reference to the importer store.
     */
    private ODEMetadataStoreClient importer;

    /**
     * Set up a new user in a new group and set the local {@code importer} field.
     *
     * @throws Exception unexpected
     */
    @BeforeMethod
    protected void setUpNewUserWithImporter() throws Exception {
        newUserAndGroup("rwr-r-");
        importer = new ODEMetadataStoreClient();
        importer.initialize(factory);
    }

    /**
     * Tests thumbnailService methods: getThumbnailSet(rint, rint, list<long>)
     * and getThumbnailByLongestSideSet(rint, list<long>)
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testGetThumbnailSet() throws Exception {
        ThumbnailStorePrx svc = factory.createThumbnailStore();
        // first import an image already tested see ImporterTest
        String format = ModelMockFactory.FORMATS[0];
        File f = File.createTempFile("testImportGraphicsImages" + format, "."
                + format);
        mmFactory.createImageFile(f, format);
        List<Long> pixelsIds = new ArrayList<Long>();
        int thumbNailCount = 20;
        try {
            for (int i = 0; i < thumbNailCount; i++) {
                List<Pixels> pxls = importFile(importer, f, format);
                pixelsIds.add(pxls.get(0).getId().getValue());
            }
        } catch (Throwable e) {
            throw new Exception("cannot import image", e);
        }
        f.deleteOnExit();

        int sizeX = 48;
        int sizeY = 48;
        Map<Long, byte[]> thmbs = svc.getThumbnailSet(ode.rtypes.rint(sizeX),
                ode.rtypes.rint(sizeY), pixelsIds);
        Map<Long, byte[]> lsThmbs = svc.getThumbnailByLongestSideSet(
                ode.rtypes.rint(sizeX), pixelsIds);
        Iterator<byte[]> it = thmbs.values().iterator();
        int tnCount = 0;
        while (it.hasNext()) {
            Utils.checkSize(it.next(), sizeX, sizeY);
            tnCount++;
        }
        Assert.assertEquals(thumbNailCount, tnCount);

        it = lsThmbs.values().iterator();
        tnCount = 0;
        while (it.hasNext()) {
            Utils.checkSize(it.next(), sizeX, sizeY);
            tnCount++;
        }
        Assert.assertEquals(thumbNailCount, tnCount);
        svc.close();
    }

    /**
     * Test that thumbnails can be retrieved from multiple groups at once.
     *
     * @throws Throwable unexpected
     */
    @Test
    public void testGetThumbnailsMultipleGroups() throws Throwable {
        final byte[] thumbnail;
        final long pixelsIdα, pixelsIdβ;
        ThumbnailStorePrx svc = null;

        /* create a fake image file */
        final File file = File.createTempFile(getClass().getSimpleName(), ".fake");
        file.deleteOnExit();

        try {
            /* import the image as one user in one group and get its thumbnail */
            pixelsIdα = importFile(importer, file, "fake").get(0).getId().getValue();
            svc = factory.createThumbnailStore();
            svc.setPixelsId(pixelsIdα);
            thumbnail = svc.getThumbnailByLongestSide(null);
        } finally {
            if (svc != null) {
                {
                    svc.close();
                    svc = null;
                }
            }
        }

        /* import the image as another user in another group */
        setUpNewUserWithImporter();
        pixelsIdβ = importFile(importer, file, "fake").get(0).getId().getValue();

        final Map<Long, byte[]> thumbnails;

        try {
            /* use all-groups context to fetch both thumbnails at once */
            final List<Long> pixelsIdsαβ = ImmutableList.of(pixelsIdα, pixelsIdβ);
            svc = factory.createThumbnailStore();
            thumbnails = svc.getThumbnailByLongestSideSet(null, pixelsIdsαβ, ALL_GROUPS_CONTEXT);
        } finally {
            if (svc != null) {
                {
                    svc.close();
                    svc = null;
                }
            }
        }

        /* check that the thumbnails are as expected */
        Utils.checkSize(thumbnail, 48, 48);
        Assert.assertEquals(thumbnails.get(pixelsIdα), thumbnail);
        Assert.assertEquals(thumbnails.get(pixelsIdβ), thumbnail);
    }

}
