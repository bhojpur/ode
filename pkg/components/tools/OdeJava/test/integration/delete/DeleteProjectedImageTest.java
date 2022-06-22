package integration.delete;

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
import java.util.Arrays;
import java.util.List;

import integration.AbstractServerTest;
import ode.SecurityViolation;
import ode.api.IProjectionPrx;
import ode.cmd.Delete2;
import ode.constants.projection.ProjectionType;
import ode.gateway.util.Requests;
import ode.model.Image;
import ode.model.Pixels;
import ode.sys.EventContext;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * Deleted projected image and/or source image.
 */
public class DeleteProjectedImageTest extends AbstractServerTest {

    /** Indicates to delete the source image. */
    private static final int SOURCE_IMAGE = 0;

    /** Indicates to delete the projected image. */
    private static final int PROJECTED_IMAGE = 1;

    /** Indicates to delete the both images. */
    private static final int BOTH_IMAGES = 2;

    /* the IDs of the images left over by the tests */
    private final List<Long> remainingImageIds = new ArrayList<Long>();

    /**
     * Imports the small dv.
     * The image has 5 z-sections, 6 timepoints, 1 channel, signed 16-bit.
     * 
     * @return The id of the pixels set.
     * @throws Exception Thrown if an error occurred.
     */
    private Pixels importImage() throws Exception
    {
        String name = "testDV&pixelType=int16&sizeX=20&sizeY=20&sizeZ=5&sizeT=6&sizeC=1.fake";
        final File srcFile = new File(System.getProperty("java.io.tmpdir"), name);
        srcFile.deleteOnExit();
        srcFile.createNewFile();
        List<Pixels> pixels = null;
        try {
            pixels = importFile(srcFile, "fake");
        } catch (Throwable e) {
            throw new Exception("cannot import image", e);
        }
        return pixels.get(0);
    }

    /** 
     * Creates an image and projects it either by the owner or by another
     * member of the group. The image is then deleted by the owner of the image
     * or by another user.
     *
     * @param src The permissions of the source group.
     * @param memberRole The role of the other group member projecting the
     * image or <code>-1</code> if the owner projects the image.
     * @param deleteMemberRole The role of the member deleting the image
     * image or <code>-1</code> if the owner projects the image.
     * @param action One of the constants defined by this class.
     * @throws Exception Thrown if an error occurred.
     */
    private void deleteImage(String src, int memberRole,
            int deleteMemberRole, int action)
            throws Exception
    {
        deleteImage(true, src, memberRole, deleteMemberRole, action);
    }

    /** 
     * Creates an image and projects it either by the owner or by another
     * member of the group. The image is then deleted by the owner of the image
     * or by another user.
     *
     * @param passes if the delete request's response is expected to be
     * {@link ode.cmd.OK}
     * @param src The permissions of the source group.
     * @param memberRole The role of the other group member projecting the
     * image or <code>-1</code> if the owner projects the image.
     * @param deleteMemberRole The role of the member deleting the image
     * image or <code>-1</code> if the owner projects the image.
     * @param action One of the constants defined by this class.
     * @throws Exception Thrown if an error occurred.
     */
    private void deleteImage(boolean passes, String src, int memberRole,
            int deleteMemberRole, int action)
            throws Exception
    {
        EventContext ctx = newUserAndGroup(src);
        if (memberRole > 0) { //create a second user in the group.
            EventContext ctx2 = newUserInGroup(ctx);
            switch (memberRole) {
            case AbstractServerTest.ADMIN:
                logRootIntoGroup(ctx2);
                break;
            case AbstractServerTest.GROUP_OWNER:
                makeGroupOwner();
            }
        }
        Pixels pixels = importImage();
        long id = pixels.getImage().getId().getValue();
        List<Integer> channels = Arrays.asList(0);
        IProjectionPrx svc = factory.getProjectionService();
        long projectedID = svc.projectPixels(pixels.getId().getValue(), null,
                ProjectionType.MAXIMUMINTENSITY, 0, 1, channels, 1, 0, 1,
                "projectedImage");

        disconnect();
        init(ctx);
        //login is as root
        if (deleteMemberRole == AbstractServerTest.ADMIN)
            logRootIntoGroup(ctx);
        //delete the image(s)
        Delete2 dc;
        switch (action) {
        case SOURCE_IMAGE:
            dc = Requests.delete().target("Image").id(id).build();
            callback(passes, client, dc);
            break;
        case PROJECTED_IMAGE:
            dc = Requests.delete().target("Image").id(projectedID).build();
            callback(passes, client, dc);
            break;
        case BOTH_IMAGES:
            dc = Requests.delete().target("Image").id(id, projectedID).build();
            callback(passes, client, dc);
            break;
        }

        //Check the result
        switch (action) {
        case SOURCE_IMAGE:
            Assert.assertNull(iQuery.find(Image.class.getSimpleName(), id));
            //check that the projected image is still there
            Assert.assertNotNull(iQuery.find(Image.class.getSimpleName(), projectedID));
            remainingImageIds.add(projectedID);
            break;
        case PROJECTED_IMAGE:
            Assert.assertNull(iQuery.find(Image.class.getSimpleName(), projectedID));
           //check that the original image is still there
            Assert.assertNotNull(iQuery.find(Image.class.getSimpleName(), id));
            remainingImageIds.add(id);
            break;
        case BOTH_IMAGES:
            Assert.assertNull(iQuery.find(Image.class.getSimpleName(), projectedID));
            Assert.assertNull(iQuery.find(Image.class.getSimpleName(), id));
        }
    }

    /**
     * Delete the images left over from the tests.
     * @throws Exception unexpected
     */
    @AfterClass
    public void cleanUpRemainingImages() throws Exception {
        doChange(root, root.getSession(), Requests.delete().target("Image").id(remainingImageIds).build(), true);
        remainingImageIds.clear();
    }

    /**
     * Test the delete of the image that has been projected in a
     * RW---- group. The source image is deleted.
     * The projection is done by the owner of the data and deleted by the owner.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testSourceImageByOwnerdeleteByOwnerRW() throws Exception {
        deleteImage("rw----", -1, -1, SOURCE_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RW---- group. The projected image is deleted.
     * The projection is done by the owner of the data and deleted by the owner.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testProjectedImageByOwnerdeleteByOwnerRW() throws Exception {
        deleteImage("rw----",  -1, -1, PROJECTED_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RW---- group. Both images are deleted.
     * The projection is done by the owner of the data and deleted by the owner.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testBothImagesByOwnerdeleteByOwnerRW() throws Exception {
        deleteImage("rw----", -1, -1, BOTH_IMAGES);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWR--- group. The source image is deleted.
     * The projection is done by the owner of the data and deleted by the owner.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testSourceImageByOwnerdeleteByOwnerRWR() throws Exception {
        deleteImage("rwr---", -1, -1, SOURCE_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWR--- group. The projected image is deleted.
     * The projection is done by the owner of the data and deleted by the owner.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testProjectedImageByOwnerdeleteByOwnerRWR() throws Exception {
        deleteImage("rwr---", -1, -1, PROJECTED_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWR---- group. Both images are deleted.
     * The projection is done by the owner of the data and deleted by the owner.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testBothImagesByOwnerdeleteByOwnerRWR() throws Exception {
        deleteImage("rwr---", -1, -1, BOTH_IMAGES);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWRA-- group. The source image is deleted.
     * The projection is done by the owner of the data and deleted by the owner.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testSourceImageByOwnerdeleteByOwnerRWRA() throws Exception {
        deleteImage("rwra--", -1, -1, SOURCE_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWRA-- group. The projected image is deleted.
     * The projection is done by the owner of the data and deleted by the owner.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testProjectedImageByOwnerdeleteByOwnerRWRA() throws Exception {
        deleteImage("rwra--", -1, -1, PROJECTED_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWRA-- group. Both images are deleted.
     * The projection is done by the owner of the data and deleted by the owner.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testBothImagesByOwnerdeleteByOwnerRWRA() throws Exception {
        deleteImage("rwra--", -1, -1, BOTH_IMAGES);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWRW-- group. The source image is deleted.
     * The projection is done by the owner of the data and deleted by the owner.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testSourceImageByOwnerdeleteByOwnerRWRW() throws Exception {
        deleteImage("rwrw--", -1, -1, SOURCE_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWRW-- group. The projected image is deleted.
     * The projection is done by the owner of the data and deleted by the owner.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testProjectedImageByOwnerdeleteByOwnerRWRW() throws Exception {
        deleteImage("rwrw--", -1, -1, PROJECTED_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWRW-- group. Both images are deleted.
     * The projection is done by the owner of the data and deleted by the owner.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testBothImagesByOwnerdeleteByOwnerRWRW() throws Exception {
        deleteImage("rwrw--", -1, -1, BOTH_IMAGES);
    }

    //Project by another member delete by Admin.
    /**
     * Test the delete of the image that has been projected in a
     * RW---- group. The source image is deleted.
     * The projection is done by a member and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testSourceImageByMemberdeleteByAdminRW() throws Exception {
        deleteImage("rw----", AbstractServerTest.MEMBER,
                AbstractServerTest.ADMIN, SOURCE_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RW---- group. The projected image is deleted.
     * The projection is done by a member and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testProjectedImageByMemberdeleteByAdminRW() throws Exception {
        deleteImage("rw----", AbstractServerTest.MEMBER,
                AbstractServerTest.ADMIN, PROJECTED_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RW---- group. Both images are deleted.
     * The projection is done by a member and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testBothImagesByMemberdeleteByAdminRW() throws Exception {
        deleteImage("rw----", AbstractServerTest.MEMBER,
                AbstractServerTest.ADMIN, BOTH_IMAGES);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWR--- group. The source image is deleted.
     * The projection is done by a member and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testSourceImageByMemberdeleteByAdminRWR() throws Exception {
        deleteImage("rwr---", AbstractServerTest.MEMBER,
                AbstractServerTest.ADMIN, SOURCE_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWR--- group. The projected image is deleted.
     * The projection is done by a member and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testProjectedImageByMemberdeleteByAdminRWR() throws Exception {
        deleteImage("rwr---", AbstractServerTest.MEMBER,
                AbstractServerTest.ADMIN, PROJECTED_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWR--- group. Both images are deleted.
     * The projection is done by a member and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testBothImagesByMemberdeleteByAdminRWR() throws Exception {
        deleteImage("rwr---", AbstractServerTest.MEMBER,
                AbstractServerTest.ADMIN, BOTH_IMAGES);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWRA-- group. The source image is deleted.
     * The projection is done by a member and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testSourceImageByMemberdeleteByAdminRWRA() throws Exception {
        deleteImage("rwra--", AbstractServerTest.MEMBER,
                AbstractServerTest.ADMIN, SOURCE_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWRA--  group. The projected image is deleted.
     * The projection is done by a member and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testProjectedImageByMemberdeleteByAdminRWRA() throws Exception {
        deleteImage("rwra--", AbstractServerTest.MEMBER,
                AbstractServerTest.ADMIN, PROJECTED_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWRA-- group. Both images are deleted.
     * The projection is done by a member and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testBothImagesByMemberdeleteByAdminRWRA() throws Exception {
        deleteImage("rwra--", AbstractServerTest.MEMBER,
                AbstractServerTest.ADMIN, BOTH_IMAGES);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWRW-- group. The source image is deleted.
     * The projection is done by a member and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testSourceImageByMemberdeleteByAdminRWRW() throws Exception {
        deleteImage("rwrw--", AbstractServerTest.MEMBER,
                AbstractServerTest.ADMIN, SOURCE_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWRW-- group. The projected image is deleted.
     * The projection is done by a member and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testProjectedImageByMemberdeleteByAdminRWRW() throws Exception {
        deleteImage("rwrw--", AbstractServerTest.MEMBER,
                AbstractServerTest.ADMIN, PROJECTED_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWRW-- group. Both images are deleted.
     * The projection is done by the owner of the data and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testBothImagesByMemberdeleteByAdminRWRW() throws Exception {
        deleteImage("rwrw--", AbstractServerTest.MEMBER,
                AbstractServerTest.ADMIN, BOTH_IMAGES);
    }

    //delete by admin
    /**
     * Test the delete of the image that has been projected in a
     * RW---- group. The source image is deleted.
     * The projection is done by the owner of the data and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testSourceImageByOwnerdeleteByAdminRW() throws Exception {
        deleteImage("rw----", -1, AbstractServerTest.ADMIN, SOURCE_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RW---- group. The projected image is deleted.
     * The projection is done by the owner of the data and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testProjectedImageByOwnerdeleteByAdminRW() throws Exception {
        deleteImage("rw----", -1, AbstractServerTest.ADMIN, PROJECTED_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RW---- group. Both images are deleted.
     * The projection is done by the owner of the data and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testBothImagesByOwnerdeleteByAdminRW() throws Exception {
        deleteImage("rw----", -1, AbstractServerTest.ADMIN, BOTH_IMAGES);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWR--- group. The source image is deleted.
     * The projection is done by the owner of the data and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testSourceImageByOwnerdeleteByAdminRWR() throws Exception {
        deleteImage("rwr---", -1, AbstractServerTest.ADMIN, SOURCE_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWR--- group. The projected image is deleted.
     * The projection is done by the owner of the data and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testProjectedImageByOwnerdeleteByAdminRWR() throws Exception {
        deleteImage("rwr---", -1, AbstractServerTest.ADMIN, PROJECTED_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWR--- group. Both images are deleted.
     * The projection is done by the owner of the data and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testBothImagesByOwnerdeleteByAdminRWR() throws Exception {
        deleteImage("rwr---", -1, AbstractServerTest.ADMIN, BOTH_IMAGES);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWRA-- group. The source image is deleted.
     * The projection is done by the owner of the data and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testSourceImageByOwnerdeleteByAdminRWRA() throws Exception {
        deleteImage("rwra--", -1, AbstractServerTest.ADMIN, SOURCE_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWRA-- group. The projected image is deleted.
     * The projection is done by the owner of the data and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testProjectedImageByOwnerdeleteByAdminRWRA() throws Exception {
        deleteImage("rwra--", -1, AbstractServerTest.ADMIN, PROJECTED_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWRA-- group. Both images are deleted.
     * The projection is done by the owner of the data and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testBothImagesByOwnerdeleteByAdminRWRA() throws Exception {
        deleteImage("rwra--", -1, AbstractServerTest.ADMIN, BOTH_IMAGES);
    }


    /**
     * Test the delete of the image that has been projected in a
     * RWRW-- group. The source image is deleted.
     * The projection is done by the owner of the data and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testSourceImageByOwnerdeleteByAdminRWRW() throws Exception {
        deleteImage("rwrw--", -1, AbstractServerTest.ADMIN, SOURCE_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWRW-- group. The projected image is deleted.
     * The projection is done by the owner of the data and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testProjectedImageByOwnerdeleteByAdminRWRW() throws Exception {
        deleteImage("rwrw--", -1, AbstractServerTest.ADMIN, PROJECTED_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWRW-- group. Both images are deleted.
     * The projection is done by the owner of the data and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testBothImagesByOwnerdeleteByAdminRWRW() throws Exception {
        deleteImage("rwrw--", -1, AbstractServerTest.ADMIN, BOTH_IMAGES);
    }

    //Projected by another member delete by data owner.
    /**
     * Test the delete of the image that has been projected in a
     * RW---- group. The source image is deleted.
     * The projection is done by a member and deleted by the owner.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test(expectedExceptions = SecurityViolation.class)
    public void testSourceImageByMemberdeleteByOwnerRW() throws Exception {
        deleteImage(false, "rw----", AbstractServerTest.MEMBER,
                -1, SOURCE_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWR--- group. The source image is deleted.
     * The projection is done by a member and deleted by the owner.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test(groups = "broken")
    public void testSourceImageByMemberdeleteByOwnerRWR() throws Exception {
        deleteImage("rwr---", AbstractServerTest.MEMBER,
                -1, SOURCE_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWRA-- group. The source image is deleted.
     * The projection is done by a member and deleted by the admin.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test(groups = "broken")
    public void testSourceImageByMemberdeleteByOwnerRWRA() throws Exception {
        deleteImage("rwra--", AbstractServerTest.MEMBER,
                -1, SOURCE_IMAGE);
    }

    /**
     * Test the delete of the image that has been projected in a
     * RWRW-- group. The source image is deleted.
     * The projection is done by a member and deleted by the owner.
     *
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testSourceImageByMemberdeleteByOwnerRWRW() throws Exception {
        deleteImage("rwrw--", AbstractServerTest.MEMBER,
                -1, SOURCE_IMAGE);
    }

}
