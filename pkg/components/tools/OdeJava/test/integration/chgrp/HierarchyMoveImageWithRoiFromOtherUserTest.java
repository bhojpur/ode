package integration.chgrp;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ode.ServerError;
import ode.cmd.Chgrp2;
import ode.gateway.util.Requests;
import ode.model.ExperimenterGroup;
import ode.model.IObject;
import ode.model.Image;
import ode.model.Rectangle;
import ode.model.RectangleI;
import ode.model.Roi;
import ode.model.RoiI;
import ode.model.Shape;
import ode.sys.EventContext;
import ode.sys.ParametersI;

import org.testng.Assert;
import org.testng.annotations.Test;

public class HierarchyMoveImageWithRoiFromOtherUserTest extends
        AbstractServerTest {

    /**
     * Performs the changing of group for an image with an ROI owned by the same
     * user
     *
     * @param sourceGroupPermissions
     * @param targetGroupPermissions
     * @throws Exception
     */
    private void moveImageBetweenPermissionGroups(
            String sourceGroupPermissions, String targetGroupPermissions,
            boolean roiOwnerInTargetGroup) throws Exception {

        // image owner
        EventContext imageOwnerContext = newUserAndGroup(
                sourceGroupPermissions, false);

        Image image = createSimpleImage();
        long originalImageId = image.getId().getValue();

        // change to another user in source group
        EventContext roiUserContext = newUserInGroup(imageOwnerContext);

        // get the image from the roi user's perspective
        Image roiUserImage = getImageWithId(image.getId().getValue());

        // create the roi as the roi user
        Roi serverROI = createSimpleRoiFor(roiUserImage);
        long originalRoiId = serverROI.getId().getValue();

        List<Long> shapeIds = new ArrayList<Long>();

        for (int i = 0; i < serverROI.sizeOfShapes(); i++) {
            Shape shape = serverROI.getShape(i);
            shapeIds.add(shape.getId().getValue());
        }
        // switch back to the original user
        loginUser(imageOwnerContext);
        // create the target group
        ExperimenterGroup targetGroup = newGroupAddUser(targetGroupPermissions,
                imageOwnerContext.userId);
        iAdmin.getEventContext();

        if (roiOwnerInTargetGroup) {
            addUserToGroup(roiUserContext.userId, targetGroup);
        }
        iAdmin.getEventContext();
        // Perform the move operation as original user
        final Chgrp2 dc = Requests.chgrp().target(image).toGroup(targetGroup).build();
        callback(true, client, dc);

        // check the roi has been moved to target group
        Roi originalRoi = getRoiWithId(originalRoiId);
        Assert.assertNull(originalRoi);

        // check the shapes have moved to target group
        List<IObject> orginalShapes = getShapesWithIds(shapeIds);
        Assert.assertEquals(0, orginalShapes.size());

        // Move the user into the target group!
        loginUser(targetGroup);

        EventContext targetGroupContext = iAdmin.getEventContext();

        Assert.assertFalse(imageOwnerContext.groupId == targetGroupContext.groupId);
        Assert.assertEquals(imageOwnerContext.userId, targetGroupContext.userId);

        // check that the image has moved
        Image movedImage = getImageWithId(originalImageId);
        Assert.assertNotNull(movedImage);

        // Check that the ROI has moved
        Roi movedRoi = getRoiWithId(originalRoiId);
        Assert.assertNotNull(movedRoi);

        List<IObject> movedShapes = getShapesWithIds(shapeIds);
        Assert.assertEquals(shapeIds.size(), movedShapes.size());

        // check who the owner of roi is
        long movedRoiOwnerId = movedRoi.getDetails().getOwner().getId()
                .getValue();
        Assert.assertEquals(roiUserContext.userId, movedRoiOwnerId);
    }

    /**
     * Test moving data as the data owner from a read-write to a read-write
     * group
     *
     * @throws Exception
     */
    @Test
    public void moveImageRWRWtoRWRW() throws Exception {
        moveImageBetweenPermissionGroups("rwrw--", "rwrw--", true);
    }

    /**
     * Test moving data as the data owner from a read-write to a read-write
     * group where the Roi User is not in the target group
     *
     * @throws Exception
     */
    @Test
    public void moveImageRWRWtoRWRW_RoiUserNotInTargetGroup() throws Exception {
        moveImageBetweenPermissionGroups("rwrw--", "rwrw--", false);
    }

    /**
     * Test moving data as the data owner from a read-annotate to a read-only
     * group
     *
     * @throws Exception
     */
    @Test
    public void moveImageRWRAtoRWR() throws Exception {
        moveImageBetweenPermissionGroups("rwra--", "rwr---", true);
    }

    /**
     * Test moving data as the data owner from a read-annotate to a read-only
     * group where Roi user is not in the target group group
     *
     * @throws Exception
     */
    @Test
    public void moveImageRWRAtoRWR_RoiUserNotInTargetGroup() throws Exception {
        moveImageBetweenPermissionGroups("rwra--", "rwr---", false);
    }

    /**
     * Test moving data as the data owner from a read-annotate to a
     * read-annotate group
     *
     * @throws Exception
     */
    @Test
    public void moveImageRWRAtoRWRA() throws Exception {
        moveImageBetweenPermissionGroups("rwra--", "rwra--", true);
    }

    /**
     * Test moving data as the data owner from a read-annotate to a
     * read-annotate group where Roi user is not in the target group group
     *
     * @throws Exception
     */
    @Test
    public void moveImageRWRAtoRWRA_RoiUserNotInTargetGroup() throws Exception {
        moveImageBetweenPermissionGroups("rwra--", "rwra--", false);
    }

    /**
     * Test moving data as the data owner from a read-annotate to a read-write
     * group
     *
     * @throws Exception
     */
    @Test
    public void moveImageRWRAtoRWRW() throws Exception {
        moveImageBetweenPermissionGroups("rwra--", "rwrw--", true);
    }

    /**
     * Test moving data as the data owner from a read-annotate to a read-write
     * group where Roi user is not in the target group group
     *
     * @throws Exception
     */
    @Test
    public void moveImageRWRAtoRWRW_RoiUserNotInTargetGroup() throws Exception {
        moveImageBetweenPermissionGroups("rwra--", "rwrw--", false);
    }

    /**
     * Test moving data as the data owner from a read-write to a read-only group
     *
     * @throws Exception
     */
    @Test
    public void moveImageRWRWtoRWR() throws Exception {
        moveImageBetweenPermissionGroups("rwrw--", "rwr---", true);
    }

    /**
     * Test moving data as the data owner from a read-write to a read-only group
     * where Roi user is not in the target group group
     *
     * @throws Exception
     */
    @Test
    public void moveImageRWRWtoRWR_RoiUserNotInTargetGroup() throws Exception {
        moveImageBetweenPermissionGroups("rwrw--", "rwr---", false);
    }

    /**
     * Test moving data as the data owner from a read-write to a read-annotate
     * group
     *
     * @throws Exception
     */
    @Test
    public void moveImageRWRWtoRWRA() throws Exception {
        moveImageBetweenPermissionGroups("rwrw--", "rwra--", true);
    }

    /**
     * Test moving data as the data owner from a read-write to a read-annotate
     * group where Roi user is not in the target group group
     *
     * @throws Exception
     */
    @Test
    public void moveImageRWRWtoRWRA_RoiUserNotInTargetGroup() throws Exception {
        moveImageBetweenPermissionGroups("rwrw--", "rwra--", false);
    }

    /**
     * Adds the user to the group, no context switching
     *
     * @param userId
     * @param targetGroup
     * @throws Exception
     */
    private void addUserToGroup(long userId, ExperimenterGroup targetGroup)
            throws Exception {
        addUsers(targetGroup, Arrays.asList(userId), true);
    }

    /**
     * Queries the server for the Image with the id provided under the current
     * user/group security context
     *
     * @param imageId
     * @return
     * @throws ServerError
     */
    private Image getImageWithId(long imageId) throws ServerError {
        ParametersI queryParameters = new ParametersI();
        queryParameters.addId(imageId);
        String queryForImage = "select d from Image as d where d.id = :id";
        return (Image) iQuery.findByQuery(queryForImage, queryParameters);
    }

    /**
     * Queries the server for the ROI with the id provided under the current
     * user/group security context
     *
     * @param roiId
     * @return
     * @throws ServerError
     */
    private Roi getRoiWithId(long roiId) throws ServerError {
        ParametersI queryParameters = new ParametersI();
        queryParameters.addId(roiId);
        String queryForROI = "select d from Roi as d where d.id = :id";
        return (Roi) iQuery.findByQuery(queryForROI, queryParameters);
    }

    /**
     * Queries the server for all the shapes with matching ids under the current
     * user/group security context
     *
     * @param shapeIds
     * @return
     * @throws ServerError
     */
    private List<IObject> getShapesWithIds(List<Long> shapeIds)
            throws ServerError {
        ParametersI queryParameters = new ParametersI();
        queryParameters.addIds(shapeIds);
        String queryForShapes = "select d from Shape as d where d.id in (:ids)";
        return iQuery.findAllByQuery(queryForShapes, queryParameters);
    }

    /**
     * Creates and returns a server created ROI on an image under the current
     * user/group security context
     *
     * @param image
     * @return
     * @throws ServerError
     */
    private Roi createSimpleRoiFor(Image image) throws ServerError {
        Roi roi = new RoiI();
        roi.setImage(image);

        for (int i = 0; i < 3; i++) {
            Rectangle rect = new RectangleI();
            rect.setX(ode.rtypes.rdouble(10));
            rect.setY(ode.rtypes.rdouble(20));
            rect.setWidth(ode.rtypes.rdouble(40));
            rect.setHeight(ode.rtypes.rdouble(80));
            rect.setTheZ(ode.rtypes.rint(i));
            rect.setTheT(ode.rtypes.rint(0));
            roi.addShape(rect);
        }

        return (RoiI) iUpdate.saveAndReturnObject(roi);
    }

    /**
     * Creates and returns an image on the server under the current user/group
     * security context
     *
     * @return
     * @throws ServerError
     */
    private Image createSimpleImage() throws ServerError {
        Image simpleImage = mmFactory.simpleImage();
        return (Image) iUpdate.saveAndReturnObject(simpleImage);
    }
}
