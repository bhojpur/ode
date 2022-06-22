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
import java.util.Iterator;
import java.util.List;

import ode.cmd.Chgrp2;
import ode.gateway.util.Requests;
import ode.model.Dataset;
import ode.model.DatasetI;
import ode.model.DatasetImageLink;
import ode.model.DatasetImageLinkI;
import ode.model.ExperimenterGroup;
import ode.model.IObject;
import ode.model.Image;
import ode.sys.EventContext;
import ode.sys.ParametersI;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the move of data objects containing others members data.
 */
public class HierarchyMoveCombinedDataTest extends AbstractServerTest {

    /**
     * Moves a dataset containing an image owned by another user and an image
     * owned.
     *
     * @param source
     *            The permissions of the source group.
     * @param target
     *            The permissions of the destination group.
     * @param sourceRole
     *            The user's role in the source group.
     * @param targetRole
     *            The user's role in the target group.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    private void moveDatasetAndImage(String source, String target,
            int sourceRole, int targetLevel) throws Exception {
        // Step 1
        // Create a new group
        EventContext ctx = newUserAndGroup(source);
        // Create an image.
        Image img1 = (Image) iUpdate.saveAndReturnObject(mmFactory
                .createImage());
        long user1 = img1.getDetails().getOwner().getId().getValue();

        // Step 2
        // create a new user and add it to the group
        ctx = newUserInGroup(ctx);
        switch (sourceRole) {
            case GROUP_OWNER:
                makeGroupOwner();
                break;
            case ADMIN:
                logRootIntoGroup(ctx);
        }

        loginUser(ctx);
        // Create a dataset
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        // link the dataset and the image
        DatasetImageLink link = new DatasetImageLinkI();
        link.setChild(img1);
        link.setParent(new DatasetI(d.getId().getValue(), false));
        iUpdate.saveAndReturnObject(link);

        Image img2 = (Image) iUpdate.saveAndReturnObject(mmFactory
                .createImage());
        link = new DatasetImageLinkI();
        link.setChild(img2);
        link.setParent(new DatasetI(d.getId().getValue(), false));
        iUpdate.saveAndReturnObject(link);

        long user2 = d.getDetails().getOwner().getId().getValue();
        Assert.assertNotEquals(user1, user2);
        
        // Step 3
        // Create a new group, the user is now a member of the new group.
        ExperimenterGroup g = newGroupAddUser(target, ctx.userId);
        loginUser(g);

        // Step 4
        // reconnect to the source group.
        switch (sourceRole) {
            case MEMBER:
            case GROUP_OWNER:
            default:
                loginUser(ctx);
                break;
            case ADMIN:
                logRootIntoGroup(ctx.groupId);
        }
        // Create commands to move and create the link in target
        final Chgrp2 dc = Requests.chgrp().target(d).toGroup(g).build();
        callback(true, client, dc);

        // Check if the dataset has been removed.
        ParametersI param = new ParametersI();
        param.addId(d.getId().getValue());
        String sql = "select i from Dataset as i where i.id = :id";
        Assert.assertNull(iQuery.findByQuery(sql, param));

        List<Long> ids = new ArrayList<Long>();
        ids.add(img1.getId().getValue());
        ids.add(img2.getId().getValue());

        param = new ParametersI();
        param.addIds(ids);
        sql = "select i from Image as i where i.id in (:ids)";
        List<IObject> results = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(0, results.size());

        // Step 5
        // log into source group to perform the move
        switch (sourceRole) {
            case MEMBER:
            case GROUP_OWNER:
            default:
                loginUser(g);
                break;
            case ADMIN:
                logRootIntoGroup(g.getId().getValue());
        }
        param = new ParametersI();
        param.addId(d.getId().getValue());
        sql = "select i from Dataset as i where i.id = :id";

        // Check if the dataset is in the target group.
        Assert.assertNotNull(iQuery.findByQuery(sql, param));

        // Check
        param = new ParametersI();
        param.addIds(ids);
        sql = "select i from Image as i where i.id in (:ids)";
        results = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(ids.size(), results.size());
        Iterator<IObject> i = results.iterator();
        int count = 0;
        while (i.hasNext()) {
            if (ids.contains(i.next().getId().getValue()))
                count++;
        }
        Assert.assertEquals(ids.size(), count);
        disconnect();
    }

    /**
     * Test to move by the group's owner a dataset containing one image owned by
     * another group's member from a <code>RWRA</code> group to
     * <code>RWRA</code> group.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testMoveDatasetByGroupOwnerRWRAtoRWRA() throws Exception {
        moveDatasetAndImage("rwra--", "rwra--", GROUP_OWNER, MEMBER);
    }

    /**
     * Test to move by the group's owner a dataset containing one image owned by
     * another group's member from a <code>RWR</code> group to <code>RWR</code>
     * group.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testMoveDatasetByGroupOwnerRWRtoRWR() throws Exception {
        moveDatasetAndImage("rwr---", "rwr---", GROUP_OWNER, MEMBER);
    }

    /**
     * Test to move by the group's owner a dataset containing one image owned by
     * another group's member from a <code>RWR</code> group to <code>RWR</code>
     * group.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testMoveDatasetByGroupOwnerRWRWtoRWRW() throws Exception {
        moveDatasetAndImage("rwrw--", "rwrw--", GROUP_OWNER, MEMBER);
    }

    /**
     * Test to move by an admin a dataset containing one image owned by another
     * group's member from a <code>RWRA</code> group to <code>RWRA</code> group.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testMoveDatasetByAdminRWRWtoRWRW() throws Exception {
        moveDatasetAndImage("rwrw--", "rwrw--", ADMIN, MEMBER);
    }

    /**
     * Test to move by the group's owner a dataset containing one image owned by
     * another group's member from a <code>RWR</code> group to <code>RWR</code>
     * group.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testMoveDatasetByMemberRWRWtoRWRW() throws Exception {
        moveDatasetAndImage("rwrw--", "rwrw--", MEMBER, MEMBER);
    }

}
