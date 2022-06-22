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

import integration.AbstractServerTest;

import integration.ModelMockFactory;
import ode.formats.ODEMetadataStoreClient;
import ode.ServerError;
import ode.api.IRenderingSettingsPrx;
import ode.api.ThumbnailStorePrx;
import ode.model.Pixels;
import ode.model.RenderingDef;
import ode.sys.EventContext;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;

/**
 * Tests utilizing single thumbnail loading APIs
 */
public class SingleFileTest extends AbstractServerTest {

    /**
     * Reference to the importer store.
     */
    private ODEMetadataStoreClient importer;

    private EventContext owner;
    private File importedFile;
    private Pixels pixels;
    private ThumbnailStorePrx svc;

    @BeforeMethod
    public void setUpNewUserWithImporter() throws Throwable {
        owner = newUserAndGroup("rwr-r-");
        loginUser(owner);

        importer = new ODEMetadataStoreClient();
        importer.initialize(factory);

        String format = ModelMockFactory.FORMATS[0];
        importedFile = createImageFile(format);
        pixels = importFile(importer, importedFile, format).get(0);

        svc = factory.createThumbnailStore();
        Utils.setThumbnailStoreToPixels(svc, pixels.getId().getValue());
    }

    @AfterMethod
    public void cleanup() throws ServerError {
        svc.close();
    }

    /**
     * Test to retrieve the thumbnail for the imported image.
     * Tests thumbnailService method: <code>getThumbnail</code>
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testGetThumbnail() throws Exception {
        Utils.getThumbnail(svc);
    }

    /**
     * Test to retrieve the thumbnail for the imported image.
     * Tests thumbnailService method: <code>getThumbnailByLongestSide</code>
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testGetThumbnailByLongestSide() throws Exception {
        final int sizeX = 48;
        byte[] lsValues = svc.getThumbnailByLongestSide(ode.rtypes
                .rint(sizeX));
        Utils.checkSize(lsValues, sizeX, sizeX);
    }

    /**
     * Test to retrieve the thumbnail for the imported image.
     * Tests thumbnailService method: <code>getThumbnailWithoutDefault</code>
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testGetThumbnailWithoutDefault() throws Exception {
        Utils.getThumbnailWithoutDefault(svc);
    }

    /**
     * Test to retrieve the thumbnails for images. Load the thumbnails, reset
     * the rendering settings then reload the rendering settings again. Tests
     * thumbnailService method: <code>getThumbnail</code>
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testGetThumbnailAfterReset() throws Exception {
        Utils.getThumbnail(svc);

        // Reset rendering settings for pixels object
        resetRenderingSettingsForPixelsObject(pixels);

        // Call getThumbnail
        Utils.getThumbnail(svc);
    }

    /**
     * Test to retrieve the thumbnails for images. Load the thumbnails, reset
     * the rendering settings then reload the rendering settings again. Tests
     * thumbnailService method: <code>getThumbnailWithoutDefault</code>
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testGetThumbnailWithoutDefaultAfterReset() throws Exception {
        Utils.getThumbnailWithoutDefault(svc);

        // Reset rendering settings for pixels object
        resetRenderingSettingsForPixelsObject(pixels);

        Utils.getThumbnailWithoutDefault(svc);
    }

    /**
     * Test to retrieve the thumbnail for the imported image as another user.
     * Tests thumbnailService method: <code>getThumbnail</code>
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testGetThumbnailAsOtherUser() throws Exception {
        // Close current thumbnail store
        svc.close();

        
        // Create new user in group and login as that user
        EventContext newUser = newUserInGroup();
        disconnect();
        loginUser(newUser);

        // Create a new thumbnail store
        svc = factory.createThumbnailStore();

        // Set pixels id to the new thumbnail store
        Utils.setThumbnailStoreToPixels(svc, pixels.getId().getValue());

        // Get the thumbnail
        Utils.getThumbnail(svc);
    }

    /**
     * Test to retrieve the thumbnail for the imported image as another user.
     * Tests thumbnailService method: <code>getThumbnailWithoutDefault</code>
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testGetThumbnailWithoutDefaultAsOtherUser() throws Exception {
        // Close current thumbnail store
        svc.close();

        // Create new user in group and login as that user
        EventContext newUser = newUserInGroup();
        disconnect();
        loginUser(newUser);

        // Create a new thumbnail store
        svc = factory.createThumbnailStore();

        // Set pixels id to the new thumbnail store
        Utils.setThumbnailStoreToPixels(svc, pixels.getId().getValue());

        // Get the thumbnail
        Utils.getThumbnailWithoutDefault(svc);
    }

    /**
     * Resets the settings.
     *
     * @param pixels The pixels set to handle.
     * @throws ServerError
     */
    private void resetRenderingSettingsForPixelsObject(Pixels pixels) throws ServerError {
        // Reset the rendering settings.
        IRenderingSettingsPrx proxy = factory.getRenderingSettingsService();
        RenderingDef settings = proxy.getRenderingSettings(pixels.getId().getValue());
        proxy.resetDefaults(settings, pixels);
    }

    /**
     * Creates an image of the specified format.
     *
     * @param format The format.
     * @return See above
     * @throws Throwable
     */
    private File createImageFile(String format) throws Throwable {
        File f = File.createTempFile("testImportGraphicsImages" + format, "."
                + format);
        mmFactory.createImageFile(f, format);
        f.deleteOnExit();
        return f;
    }

}
