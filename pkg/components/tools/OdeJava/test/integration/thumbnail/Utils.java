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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import ode.ServerError;
import ode.api.ThumbnailStorePrx;

import org.testng.Assert;

/**
 * Collections of tests for the <code>ThumbnailStore</code> service.
 */
class Utils {

    /** The default width of the thumbnail.*/
    static final int DEFAULT_SIZE_X = 96;

    /** The default height of the thumbnail.*/
    static final int DEFAULT_SIZE_Y = 96;

    /**
     * Returns a byte array for the thumbnail of the default size.
     * It uses the <code>getThumbnail</code> method.
     *
     * @param svc The store to use.
     * @return See above
     * @throws ServerError Thrown if an error occurred during the thumbnail retrieval.
     */
    public static byte[] getThumbnail(ThumbnailStorePrx svc) throws ServerError {

        // Get thumbnail
        byte[] values = svc.getThumbnail(
                ode.rtypes.rint(DEFAULT_SIZE_X),
                ode.rtypes.rint(DEFAULT_SIZE_Y));
        Assert.assertNotNull(values);
        Assert.assertTrue(values.length > 0);

        // Return the bytes
        return values;
    }

    /**
     * Returns a byte array for the thumbnail of the default size.
     * It uses the <code>getThumbnailWithoutDefault</code> method.
     *
     * @param svc The store to use.
     * @return See above
     * @throws ServerError Thrown if an error occurred during the thumbnail retrieval.
     */
    public static byte[] getThumbnailWithoutDefault(ThumbnailStorePrx svc) throws ServerError {

        // Get thumbnail
        byte[] values = svc.getThumbnailWithoutDefault(
                ode.rtypes.rint(DEFAULT_SIZE_X),
                ode.rtypes.rint(DEFAULT_SIZE_Y));
        Assert.assertNotNull(values);

        // Return the bytes
        return values;
    }

    /**
     * Initializes the thumbnail store.
     *
     * @param svc The store to use.
     * @param pixelsId The pixels set to use.
     * @throws ServerError
     */
    public static void setThumbnailStoreToPixels(ThumbnailStorePrx svc, long pixelsId) throws ServerError {
        if (!svc.setPixelsId(pixelsId)) {
            svc.resetDefaults();
            svc.setPixelsId(pixelsId);
        }
    }

    /**
     * Checks if the array corresponding to a given thumbnail corresponds to the
     * specified width and height of the thumbnail.
     *
     * @param values The array representing the thumbnail.
     * @param sizeX The width of the thumbnail.
     * @param sizeY The height of the thumbnail.
     */
    public static void checkSize(byte[] values, int sizeX, int sizeY) {
        Assert.assertNotNull(values);
        Assert.assertTrue(values.length > 0);
        // Check width and height
        try(InputStream in = new ByteArrayInputStream(values)) {
            BufferedImage buf = ImageIO.read(in);
            Assert.assertEquals(sizeX, buf.getWidth());
            Assert.assertEquals(sizeY, buf.getHeight());
        } catch (Exception e) {
            throw new RuntimeException("Cannot convert byte array", e);
        }
    }

}
