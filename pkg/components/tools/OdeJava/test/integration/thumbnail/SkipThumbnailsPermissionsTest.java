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

import integration.AbstractServerImportTest;
import ode.formats.importer.ImportConfig;
import ode.ServerError;
import ode.api.RenderingEnginePrx;
import ode.api.ThumbnailStorePrx;
import ode.model.EventI;
import ode.model.EventLogI;
import ode.model.ExperimenterGroup;
import ode.model.ExperimenterGroupI;
import ode.model.Pixels;
import ode.model.StatsInfo;
import ode.sys.EventContext;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tests utilizing single thumbnail loading APIs by users other than the
 * owner of the image. In each test an image is imported into Bhojpur ODE without
 * thumbnails (--skip thumbnails).
 */
@SuppressWarnings("Duplicates")
public class SkipThumbnailsPermissionsTest extends AbstractServerImportTest {

    /* Total wait time will be WAITS * INTERVAL milliseconds */
    /**
     * Maximum number of intervals to wait for pyramid
     **/
    private static final int WAITS = 500;

    /**
     * Wait time in milliseconds
     **/
    private static final long INTERVAL = 1000L;

    /**
     * The collection of files that have to be deleted.
     */
    private List<File> files;

    @BeforeMethod
    protected void startup() throws Throwable {
        files = new ArrayList<>();
        createImporter();
    }

    @AfterMethod
    public void cleanup() {
        for (File file : files) {
            file.delete();
        }
        files.clear();
    }

    /**
     * Test:
     * 1. User1 import an image and skip the thumbnail
     * generation during import.
     * 2. User1 does generate a thumbnail
     * 3. Create a new user: user2 and log as that user
     * 4. User2 create a thumbnail for that image using the {@code getThumbnail}
     *
     * @param permissions
     * @param isAdmin
     * @param isGroupOwner
     * @throws Throwable
     */
    @Test(dataProvider = "permissions")
    public void testGetThumbnail(String permissions, boolean isAdmin,
                                 boolean isGroupOwner) throws Throwable {
        //member in a private group cannot view other user data.
        if (permissions.equalsIgnoreCase("rw----") && !isGroupOwner && !isAdmin) {
            return;
        }
        // Create two users in same group
        EventContext user1 = newUserAndGroup(permissions);
        loginUser(user1);

        ImportConfig config = new ImportConfig();
        config.doThumbnails.set(false); // skip thumbnails

        // Import image without thumbnails
        Pixels pixels = importFile(config);

        // View image as user 1
        ThumbnailStorePrx svc = factory.createThumbnailStore();
        try {
            Utils.setThumbnailStoreToPixels(svc, pixels.getId().getValue());
            Utils.getThumbnail(svc);
        } finally {
            svc.close();
        }

        // Create new user in group and login as that user
        EventContext user2 = newUserInGroup(user1, isGroupOwner);

        // If user is an admin, add them to the system group
        if (isAdmin) {
            ExperimenterGroup systemGroup = new ExperimenterGroupI(roles.systemGroupId, false);
            addUsers(systemGroup, Collections.singletonList(user2.userId), false);
        }
        disconnect();
        // Login as user2
        loginUser(user2);

        // Try to load image
        svc = factory.createThumbnailStore();
        try {
            Utils.setThumbnailStoreToPixels(svc, pixels.getId().getValue());
            Utils.getThumbnail(svc);
        } finally {
            svc.close();
        }
    }

    /**
     * Test:
     * 1. User1 import an image and skip the thumbnail
     * generation during import.
     * 2. User1 does generate a thumbnail
     * 3. Create a new user: user2 and log as that user
     * 4. User2 create a thumbnail for that image using the {@code getThumbnailWithoutDefault}
     *
     * @param permissions
     * @param isAdmin
     * @param isGroupOwner
     * @throws Throwable
     */
    @Test(dataProvider = "permissions")
    public void testGetThumbnailWithoutDefault(String permissions, boolean isAdmin, boolean isGroupOwner) throws Throwable {
        //member in a private group cannot view other user data.
        if (permissions.equalsIgnoreCase("rw----") && !isGroupOwner && !isAdmin) {
            return;
        }
        // Create two users in same group
        EventContext user1 = newUserAndGroup(permissions);
        loginUser(user1);

        // Obtain image
        ImportConfig config = new ImportConfig();
        config.doThumbnails.set(false); // skip thumbnails

        // Import image without thumbnails
        Pixels pixels = importFile(config);

        // View image as user 1
        ThumbnailStorePrx svc = factory.createThumbnailStore();
        try {
            Utils.setThumbnailStoreToPixels(svc, pixels.getId().getValue());
            Utils.getThumbnailWithoutDefault(svc);
        } finally {
            svc.close();
        }

        // Create new user in group and login as that user
        addUserAndLogin(user1, isAdmin, isGroupOwner);

        // Try to load image
        svc = factory.createThumbnailStore();
        try {
            Utils.setThumbnailStoreToPixels(svc, pixels.getId().getValue());
            byte[] bytes = Utils.getThumbnailWithoutDefault(svc);
            Assert.assertTrue(bytes.length > 0);
        } finally {
            svc.close();
        }
    }

    /**
     * Test:
     * 1. User1 import an image (pyramid will be created) and skip the thumbnail
     * generation during import.
     * 2. User1 does generate a thumbnail
     * 3. Create a new user: user2 and log as that user
     * 4. User2 create a thumbnail for that image using the <code>getThumbnail</code>
     *
     * @param permissions
     * @param isAdmin
     * @param isGroupOwner
     * @throws Throwable
     */
    @Test(dataProvider = "permissions")
    public void testGetThumbnailLarge(String permissions, boolean isAdmin, boolean isGroupOwner) throws Throwable {
        //member in a private group cannot view other user data.
        if (permissions.equalsIgnoreCase("rw----") && !isGroupOwner && !isAdmin) {
            return;
        }
        // Create two users in same group
        EventContext user1 = newUserAndGroup(permissions);
        loginUser(user1);

        // Obtain image
        ImportConfig config = new ImportConfig();
        config.doThumbnails.set(false); // skip thumbnails

        // Create a fake file. A pyramid will be generated
        Pixels pixels = importLargeFile(config);

        // View image as user 1
        ThumbnailStorePrx svc = factory.createThumbnailStore();
        try {
            Utils.setThumbnailStoreToPixels(svc, pixels.getId().getValue());
            Utils.getThumbnail(svc);
        } finally {
            svc.close();
        }

        // Create new user in group and login as that user
        addUserAndLogin(user1, isAdmin, isGroupOwner);

        // Try to load image
        svc = factory.createThumbnailStore();
        try {
            Utils.setThumbnailStoreToPixels(svc, pixels.getId().getValue());
            byte[] data = Utils.getThumbnail(svc);
            Assert.assertTrue(data.length > 0);
        } finally {
            svc.close();
        }
    }

    /**
     * Test:
     * 1. User1 import an image (pyramid will be created) and skip the thumbnail
     * generation during import.
     * 2. User1 does not generate a thumbnail
     * 3. Create a new user: user2 and log as that user
     * 4. User2 create a thumbnail for that image using the {@code getThumbnail}
     *
     * @param permissions
     * @param isAdmin
     * @param isGroupOwner
     * @throws Throwable
     */
    @Test(dataProvider = "permissions")
    public void testGetThumbnailLargeAsUser2Only(String permissions, boolean isAdmin, boolean isGroupOwner) throws Throwable {
        //member regardless of their role cannot link to Pixels.
        if (permissions.equalsIgnoreCase("rw----")) {
            return;
        }
        //Only admin can reset settings
        if (permissions.equalsIgnoreCase("rwr---") && !isAdmin) {
            return;
        }
        // Create two users in same group
        EventContext user1 = newUserAndGroup(permissions);
        loginUser(user1);

        // Obtain image
        ImportConfig config = new ImportConfig();
        config.doThumbnails.set(false); // skip thumbnails

        // Create a fake file. A pyramid will be generated
        Pixels pixels = importLargeFile(config);

        // Create new user in group and login as that user
        addUserAndLogin(user1, isAdmin, isGroupOwner);

        // Try to load image
        ThumbnailStorePrx svc = factory.createThumbnailStore();
        try {
            Utils.setThumbnailStoreToPixels(svc, pixels.getId().getValue());
            byte[] data = Utils.getThumbnail(svc);
            Assert.assertTrue(data.length > 0);
        } finally {
            svc.close();
        }
    }

    /**
     * Test:
     * 1. User1 import an image (pyramid will be created) and skip the thumbnail
     * generation during import.
     * 2. User1 generates a thumbnail
     * 3. Create a new user: user2 and log as that user
     * 4. User2 create a thumbnail for that image using the {@code getThumbnailWithoutDefault}
     *
     * @param permissions
     * @param isAdmin
     * @param isGroupOwner
     * @throws Throwable
     */
    @Test(dataProvider = "permissions")
    public void testGetThumbnailWithoutDefaultLarge(String permissions, boolean isAdmin, boolean isGroupOwner) throws Throwable {
        //member in a private group cannot view other user data.
        if (permissions.equalsIgnoreCase("rw----") && !isGroupOwner && !isAdmin) {
            return;
        }
        // Create two users in same group
        EventContext user1 = newUserAndGroup(permissions);
        loginUser(user1);

        // Obtain image
        ImportConfig config = new ImportConfig();
        config.doThumbnails.set(false); // skip thumbnails

        // Create a fake file. A pyramid will be generated
        Pixels pixels = importLargeFile(config);

        // View image as user 1
        ThumbnailStorePrx svc = factory.createThumbnailStore();
        try {
            Utils.setThumbnailStoreToPixels(svc, pixels.getId().getValue());
            Utils.getThumbnailWithoutDefault(svc);
        } finally {
            svc.close();
        }

        // Create new user in group and login as that user
        addUserAndLogin(user1, isAdmin, isGroupOwner);

        // Try to load image
        svc = factory.createThumbnailStore();
        try {
            Utils.setThumbnailStoreToPixels(svc, pixels.getId().getValue());
            byte[] data = Utils.getThumbnailWithoutDefault(svc);
            Assert.assertTrue(data.length > 0);
        } finally {
            svc.close();
        }
    }

    /**
     * Test:
     * 1. User1 import an image (pyramid will be created) and skip the thumbnail
     * generation during import.
     * 2. User1 does not generate a thumbnail
     * 3. Create a new user: user2 and log as that user
     * 4. User2 create a thumbnail for that image using the {@code getThumbnailWithoutDefault}
     *
     * @param permissions
     * @param isAdmin
     * @param isGroupOwner
     * @throws Throwable
     */
    @Test(dataProvider = "permissions")
    public void testGetThumbnailWithoutDefaultLargeAsUser2Only(String permissions, boolean isAdmin, boolean isGroupOwner) throws Throwable {
        //member regardless of their role cannot link to Pixels.
        if (permissions.equalsIgnoreCase("rw----")) {
            return;
        }
        //Only admin can reset settings
        if (permissions.equalsIgnoreCase("rwr---") && !isAdmin) {
            return;
        }
        // Create two users in same group
        EventContext user1 = newUserAndGroup(permissions);
        loginUser(user1);

        // Obtain image
        ImportConfig config = new ImportConfig();
        config.doThumbnails.set(false); // skip thumbnails

        // Create a fake file. A pyramid will be generated
        Pixels pixels = importLargeFile(config);

        // Create new user in group and login as that user
        addUserAndLogin(user1, isAdmin, isGroupOwner);

        // Try to load image
        ThumbnailStorePrx svc = factory.createThumbnailStore();
        try {
            Utils.setThumbnailStoreToPixels(svc, pixels.getId().getValue());
            byte[] data = Utils.getThumbnailWithoutDefault(svc);
            Assert.assertTrue(data.length > 0);
        } finally {
            svc.close();
        }
    }

    /**
     * Test scenario outlined on:
     * https://trello.com/c/itoDPkxB/24-read-only-settings-and-thumbnails-generation
     * <p>
     * 1. User 1 import image and skip thumbnail generation (don't view it)
     * 2. User 2 view the image (create rendering settings)
     * 3. User 1 view the image and change the rendering settings
     * 4. User 2 view load their thumbnail and compare to user 1's thumbnail
     *
     * @throws Throwable
     */
    @Test(dataProvider = "permissions")
    public void testGetThumbnailWithRenderingSettingsChange(String permissions, boolean isAdmin,
                                                            boolean isGroupOwner) throws Throwable {
        //member regardless of their role cannot link to Pixels.
        if (permissions.equalsIgnoreCase("rw----")) {
            return;
        }

        EventContext user1 = newUserAndGroup(permissions);
        loginUser(user1);

        ImportConfig config = new ImportConfig();
        config.doThumbnails.set(false); // skip thumbnails

        // Create a fake file. A pyramid will be generated
        Pixels pixels = importLargeFile(config);
        final long pixelsId = pixels.getId().getValue();
        RenderingEnginePrx re = factory.createRenderingEngine();
        try {
            re.lookupPixels(pixelsId);
            if (!re.lookupRenderingDef(pixelsId)) {
                re.resetDefaultSettings(true);
                re.lookupRenderingDef(pixelsId);
            }
        } finally {
            re.close();
        }

        disconnect();
        // Create new user in group and login as that user
        EventContext user2 = newUserInGroup(user1, isGroupOwner);

        // If user is an admin, add them to the system group
        if (isAdmin) {
            ExperimenterGroup systemGroup = new ExperimenterGroupI(roles.systemGroupId, false);
            addUsers(systemGroup, Collections.singletonList(user2.userId), false);
        }

        // Login as user2
        loginUser(user2);

        // Generate rendering settings for user 2
        re = factory.createRenderingEngine();
        try {
            re.lookupPixels(pixelsId);
            if (!re.lookupRenderingDef(pixelsId)) {
                re.resetDefaultSettings(true);
                re.lookupRenderingDef(pixelsId);
            }
        } finally {
            re.close();
        }

        // Load thumbnail as user 2 to create thumbnail on disk
        ThumbnailStorePrx svc = factory.createThumbnailStore();
        byte[] user2Thumbnail = null;
        try {
            Utils.setThumbnailStoreToPixels(svc, pixelsId);
            user2Thumbnail = Utils.getThumbnailWithoutDefault(svc);
            Assert.assertNotNull(user2Thumbnail);
            Utils.checkSize(user2Thumbnail, Utils.DEFAULT_SIZE_X,
                    Utils.DEFAULT_SIZE_Y);
        } finally {
            svc.close();
        }

        // Switch to user 1
        disconnect();
        loginUser(user1);

        // Load and change to trigger rendering settings and thumbnail creation for user 1
        svc = factory.createThumbnailStore();
        Utils.setThumbnailStoreToPixels(svc, pixels.getId().getValue());

        // Get rendering settings for pixels object as user 1
        re = factory.createRenderingEngine();
        try {
            re.lookupPixels(pixelsId);
            if (!re.lookupRenderingDef(pixelsId)) {
                re.resetDefaultSettings(true);
                re.lookupRenderingDef(pixelsId);
            }
            re.load();
            re.setActive(0, false);
            re.saveCurrentSettings();
        } finally {
            re.close();
        }

        

        // Get thumbnail for user 1
        byte[] user1Thumbnail = null;
        try {
            user1Thumbnail = Utils.getThumbnailWithoutDefault(svc);
            Assert.assertNotNull(user1Thumbnail);
            Utils.checkSize(user1Thumbnail, Utils.DEFAULT_SIZE_X, Utils.DEFAULT_SIZE_Y);
        } finally {
            svc.close();
        }

        // Check that the thumbnails are different
        Assert.assertFalse(Arrays.equals(user1Thumbnail, user2Thumbnail));
    }

    /**
     * Test scenario outlined on:
     * https://trello.com/c/itoDPkxB/24-read-only-settings-and-thumbnails-generation
     * <p>
     * 1. User 1 import image and skip thumbnail generation (don't view it)
     * 2. User 2 view the image (create rendering settings)
     * 3. User 1 view the image and change the rendering settings
     * 4. User 2 view load their thumbnail and compare to user 1's thumbnail
     *
     * @throws Throwable
     */
    @Test(dataProvider = "permissions")
    public void testGetThumbnailWithRenderingSettingsChangeSmallImage(String permissions, boolean isAdmin,
                                                            boolean isGroupOwner) throws Throwable {
        //member regardless of their role cannot link to Pixels.
        if (permissions.equalsIgnoreCase("rw----")) {
            return;
        }

        EventContext user1 = newUserAndGroup(permissions);
        loginUser(user1);

        ImportConfig config = new ImportConfig();
        config.doThumbnails.set(false); // skip thumbnails

        // Create a fake file. A pyramid will be generated
        Pixels pixels = importLargeFile(config);
        final long pixelsId = pixels.getId().getValue();
        RenderingEnginePrx re = factory.createRenderingEngine();
        try {
            re.lookupPixels(pixelsId);
            if (!re.lookupRenderingDef(pixelsId)) {
                re.resetDefaultSettings(true);
                re.lookupRenderingDef(pixelsId);
            }
        } finally {
            re.close();
        }
        disconnect();
        // Create new user in group and login as that user
        EventContext user2 = newUserInGroup(user1, isGroupOwner);

        // If user is an admin, add them to the system group
        if (isAdmin) {
            ExperimenterGroup systemGroup = new ExperimenterGroupI(roles.systemGroupId, false);
            addUsers(systemGroup, Collections.singletonList(user2.userId), false);
        }

        // Login as user2
        loginUser(user2);

        // Generate rendering settings for user 2
        re = factory.createRenderingEngine();
        try {
            re.lookupPixels(pixelsId);
            if (!re.lookupRenderingDef(pixelsId)) {
                re.resetDefaultSettings(true);
                re.lookupRenderingDef(pixelsId);
            }
        } finally {
            re.close();
        }

        // Load thumbnail as user 2 to create thumbnail on disk
        ThumbnailStorePrx svc = factory.createThumbnailStore();
        byte[] user2Thumbnail = null;
        try {
            Utils.setThumbnailStoreToPixels(svc, pixelsId);
            user2Thumbnail = Utils.getThumbnailWithoutDefault(svc);
            Assert.assertNotNull(user2Thumbnail);
            Utils.checkSize(user2Thumbnail, Utils.DEFAULT_SIZE_X,
                    Utils.DEFAULT_SIZE_Y); 
        } finally {
            svc.close();
        }

        // Switch to user 1
        disconnect();
        loginUser(user1);

        // Load and change to trigger rendering settings and thumbnail creation for user 1
        svc = factory.createThumbnailStore();
        try {
            Utils.setThumbnailStoreToPixels(svc, pixels.getId().getValue());
        } finally {
            svc.close();
        }
        

        // Get rendering settings for pixels object as user 1
        re = factory.createRenderingEngine();
        try {
            re.lookupPixels(pixelsId);
            if (!re.lookupRenderingDef(pixelsId)) {
                re.resetDefaultSettings(true);
                re.lookupRenderingDef(pixelsId);
            }
            re.load();
            re.setActive(0, false);
            re.saveCurrentSettings();
        } finally {
            re.close();
        }

        // Get thumbnail for user 1
        svc = factory.createThumbnailStore();
        byte[] user1Thumbnail = null;
        try {
            Utils.setThumbnailStoreToPixels(svc, pixels.getId().getValue());
            user1Thumbnail = Utils.getThumbnailWithoutDefault(svc);
            Assert.assertNotNull(user1Thumbnail);
            Utils.checkSize(user1Thumbnail, Utils.DEFAULT_SIZE_X,
                    Utils.DEFAULT_SIZE_Y);
        } finally {
            svc.close();
        }

        // Check that the thumbnails are different
        Assert.assertFalse(Arrays.equals(user1Thumbnail, user2Thumbnail));
    }

    /**
     * Sets the group permissions and the user's ones.
     *
     * @return
     */
    @SuppressWarnings("Duplicates")
    @DataProvider(name = "permissions")
    public Object[][] providePermissions() {
        int index = 0;
        final int PERMISSION = index++;
        final int IS_ADMIN = index++;
        final int IS_GROUP_OWNER = index++;

        boolean[] booleanCases = new boolean[]{false, true};
        String[] permsCases = new String[]{"rw----", "rwr---", "rwra--", "rwrw--"};
        List<Object[]> testCases = new ArrayList<>();

        for (boolean isAdmin : booleanCases) {
            for (boolean isGroupOwner : booleanCases) {
                for (String groupPerms : permsCases) {
                    Object[] testCase = new Object[index];
                    testCase[PERMISSION] = groupPerms;
                    testCase[IS_ADMIN] = isAdmin;
                    testCase[IS_GROUP_OWNER] = isGroupOwner;
                    testCases.add(testCase);
                }
            }
        }
        return testCases.toArray(new Object[testCases.size()][]);
    }

    /**
     * Creates a 512x512 fake image.
     *
     * @param config ode import config
     * @return
     * @throws Throwable
     */
    private Pixels importFile(ImportConfig config) throws Throwable {
        File file = createImageFile("fake");
        return importFile(config, file, "fake").get(0);
    }

    /**
     * Creates a large fake image. A pyramid will be generated.
     *
     * @param config ode import config
     * @return
     * @throws Throwable
     */
    private Pixels importLargeFile(ImportConfig config) throws Throwable {
        File f = File.createTempFile("bigImageFake&sizeX=3500&sizeY=3500&little=false", ".fake");
        f.deleteOnExit();
        return importAndWaitForPyramid(config, f, "fake");
    }

    /**
     * Creates a 512x512 fake image.
     *
     * @param extension
     * @return
     * @throws Throwable
     */
    private File createImageFile(String extension) throws Throwable {
        File f = File.createTempFile("imageFake", "."+ extension);
        f.deleteOnExit();
        return f;
    }

    private void triggerPyramidGeneration(long pixelsId) throws ServerError {
        // This strange out of place event log is required for triggering
        // generation of pyramids for imported file.
        EventLogI el = new EventLogI();
        el.setAction(ode.rtypes.rstring("PIXELDATA"));
        el.setEntityId(ode.rtypes.rlong(pixelsId));
        el.setEntityType(ode.rtypes.rstring(ode.model.core.Pixels.class.getName()));
        el.setEvent(new EventI(0, false));

        // Need to use root session to save eventlog otherwise you get a
        // security violation
        root.getSession().getUpdateService().saveObject(el);
    }

    /**
     * Import an image file of the given format then wait for a pyramid file to
     * be generated by checking if stats exists.
     *
     * @param config ode import config
     * @param file file to import
     * @param format file format / extension
     * @return pixels object
     * @throws Exception
     */
    private Pixels importAndWaitForPyramid(ImportConfig config, File file, String format)
            throws Exception {
        Pixels pixels = null;
        try {
            pixels = importFile(config, file, format).get(0);
        } catch (Throwable e) {
            Assert.fail("Cannot import image file: " + file.getAbsolutePath()
                    + " Reason: " + e.toString());
        }
        triggerPyramidGeneration(pixels.getId().getValue());

        // Wait for a pyramid to be built (stats will be not null)
        Pixels p = factory.getPixelsService().retrievePixDescription(pixels.getId().getValue());
        StatsInfo stats = p.getChannel(0).getStatsInfo();
        int waits = 0;
        Assert.assertEquals(stats, null);
        while (stats == null && waits < WAITS) {
            Thread.sleep(INTERVAL);
            waits++;
            factory.createRawPixelsStore();
            p = factory.getPixelsService().retrievePixDescription(
                    pixels.getId().getValue());
            stats = p.getChannel(0).getStatsInfo();
        }
        if (stats == null) {
            Assert.fail("No pyramid after " + WAITS + " seconds");
        }
        return p;
    }
}
