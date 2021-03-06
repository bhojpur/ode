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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ode.api.IAdminPrx;
import ode.api.IRenderingSettingsPrx;
import ode.cmd.Delete2;
import ode.cmd.DoAll;
import ode.cmd.Request;
import ode.constants.metadata.NSINSIGHTRATING;
import ode.gateway.util.Requests;
import ode.model.AdminPrivilege;
import ode.model.Annotation;
import ode.model.CommentAnnotation;
import ode.model.CommentAnnotationI;
import ode.model.Dataset;
import ode.model.DatasetImageLink;
import ode.model.DatasetImageLinkI;
import ode.model.ExperimenterGroupI;
import ode.model.ExperimenterI;
import ode.model.IObject;
import ode.model.Image;
import ode.model.ImageAnnotationLink;
import ode.model.ImageAnnotationLinkI;
import ode.model.LongAnnotation;
import ode.model.LongAnnotationI;
import ode.model.Permissions;
import ode.model.Pixels;
import ode.model.Plate;
import ode.model.Project;
import ode.model.ProjectDatasetLink;
import ode.model.ProjectDatasetLinkI;
import ode.model.RenderingDef;
import ode.model.Screen;
import ode.model.ScreenPlateLink;
import ode.model.ScreenPlateLinkI;
import ode.model.TagAnnotation;
import ode.model.TagAnnotationI;
import ode.sys.EventContext;
import ode.sys.ParametersI;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

/**
 * Collections of tests for the <code>Delete</code> service related to
 * permissions. Unlike {@link DeleteServiceTest} we are <em>not</em> creating a
 * new user before each method since these methods need multiple users in
 * specific groups.
 */
public class DeleteServicePermissionsTest extends AbstractServerTest {

    /**
     * Since we are creating a new client on each invocation, we should also
     * clean it up. Note: {@link #newUserAndGroup(String)} also closes, but not
     * the very last invocation.
     */
    @AfterMethod
    public void close() throws Exception {
        clean();
    }

    /**
     * Test to try to delete an (top) object owned by another user in a private
     * group i.e. RW----.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     *
     *             Group changed from RWRW to RWRA for 4.4 FIXME: is this
     *             incorrectly named?
     */
    public void testDeleteObjectOwnedByOtherRW() throws Exception {

        EventContext user1Ctx = newUserAndGroup("rwra--");
        ode.client user1 = client;

        // Image
        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());
        // Dataset
        Dataset d = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asDataset());

        // Project
        Project p = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asProject());

        // Screen
        Screen s = (Screen) iUpdate.saveAndReturnObject(mmFactory
                .simpleScreenData().asScreen());

        // Plate
        Plate plate = (Plate) iUpdate.saveAndReturnObject(mmFactory
                .simplePlateData().asPlate());

        // other user tries to delete
        disconnect();
        newUserInGroup(user1Ctx);

        List<Request> commands = new ArrayList<Request>();
        commands.add(Requests.delete().target(img).build());
        commands.add(Requests.delete().target(d).build());
        commands.add(Requests.delete().target(p).build());
        commands.add(Requests.delete().target(s).build());
        commands.add(Requests.delete().target(plate).build());

        DoAll all = new DoAll();
        all.requests = commands;
        doChange(client, factory, all, false);

        // Now log the original user back in
        disconnect();
        init(user1);

        assertExists(img);
        assertExists(d);
        assertExists(p);
        assertExists(s);
        assertExists(plate);
    }

    /**
     * Test to try to delete an image owned by another user in a read-write
     * group i.e. RWR---
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testDeleteImageOwnedByOtherRWR() throws Exception {

        // set up collaborative group and one user, "the owner"
        newUserAndGroup("rwr---");

        // create an owner who then creates the image
        Image img = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());

        // create another user and try to delete the image
        newUserInGroup();
        Delete2 dc = Requests.delete().target(img).build();
        callback(false, client, dc);

        // check the image exists as the owner
        assertExists(img);
    }

    /**
     * Test to try to delete an object by the administrator in a private group
     * i.e. RW----
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testDeleteObjectByAdmin() throws Exception {
        // Create the private group
        newUserAndGroup("rw----");

        // Create the data as the user
        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());

        // Log the admin into that users group
        logRootIntoGroup();
        Delete2 dc = Requests.delete().target(img).build();
        callback(true, client, dc);

        assertDoesNotExist(img);
    }

    /**
     * Test to try to delete an object by the owner of a private group i.e.
     * RWRW--
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testDeleteObjectByGroupOwnerRWRW() throws Exception {
        EventContext ownerEc = newUserAndGroup("rwrw--");

        // owner creates the image
        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());

        // group owner deletes it
        disconnect();
        newUserInGroup(ownerEc);
        makeGroupOwner();

        String sql = "select i from Image as i ";
        sql += "where i.id = :id";
        ParametersI param = new ParametersI();
        param.addId(img.getId().getValue());
        List<IObject> images = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(1, images.size());
        img = (Image) images.get(0);

        Permissions perms = img.getDetails().getPermissions();
        Assert.assertTrue(perms.canDelete());

        Delete2 dc = Requests.delete().target(img).build();
        callback(true, client, dc);

        assertDoesNotExist(img);
    }

    /**
     * Test to try to delete an object by the owner of a private group i.e.
     * RW----. The data should not be deleted
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testDeleteObjectByGroupOwnerRW() throws Exception {
        EventContext ownerEc = newUserAndGroup("rw----");

        // owner creates the image
        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());

        // group owner deletes it
        disconnect();
        newUserInGroup(ownerEc);
        makeGroupOwner();

        Delete2 dc = Requests.delete().target(img).build();
        callback(true, client, dc);

        assertDoesNotExist(img); // Deletion permitted in 4.4
    }

    /**
     * Test to try to delete an object by the administrator in a read-only
     * collaborative group i.e. RWR---
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testDeleteObjectByAdminRWR() throws Exception {

        // set up collaborative group
        newUserAndGroup("rwr---");

        // owner creates the image
        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());

        // admin deletes the object.
        logRootIntoGroup();
        Delete2 dc = Requests.delete().target(img).build();
        callback(true, client, dc);
        assertDoesNotExist(img);
    }

    /**
     * Test to delete an image that has another user's comment and rating.
     * @throws Exception unexpected
     */
    @Test(groups = { "ticket:2997" })
    public void testDeleteImageAnnotatedByOtherOrphan() throws Exception {
        // set up collaborative group with an "owner" user
        final EventContext ec = newUserAndGroup("rwra--");

        // owner creates the image
        final Image image = (Image) iUpdate.saveAndReturnObject(mmFactory.simpleImage()).proxy();

        // other user adds comment and rating
        newUserInGroup(ec);

        CommentAnnotation comment = new CommentAnnotationI();
        comment.setTextValue(ode.rtypes.rstring("What a lovely image!"));
        comment = (CommentAnnotation) iUpdate.saveAndReturnObject(comment).proxy();

        LongAnnotation rating = new LongAnnotationI();
        rating.setLongValue(ode.rtypes.rlong(5));
        rating.setNs(ode.rtypes.rstring(NSINSIGHTRATING.value));
        rating = (LongAnnotation) iUpdate.saveAndReturnObject(rating).proxy();

        for (final Annotation annotation : new Annotation[] {comment, rating}) {
            final ImageAnnotationLink link = new ImageAnnotationLinkI();
            link.setParent(image);
            link.setChild(annotation);
            iUpdate.saveAndReturnObject(link);
        }

        // owner deletes image
        loginUser(ec);
        Delete2 dc = Requests.delete().target(image).build();
        callback(true, client, dc);

        // image and annotations are all gone
        assertDoesNotExist(image);
        assertDoesNotExist(comment);
        assertDoesNotExist(rating);
    }

    /**
     * Test to delete an image that has another user's comment and rating.
     * The user has somehow also used the same annotations on their own image.
     * @throws Exception unexpected
     */
    @Test(groups = { "ticket:2997" })
    public void testDeleteImageAnnotatedByOtherMultilinked() throws Exception {
        // set up collaborative group with an "owner" user
        final EventContext ec = newUserAndGroup("rwra--");

        // owner creates the image
        final Image imageOwner = (Image) iUpdate.saveAndReturnObject(mmFactory.simpleImage()).proxy();

        // other user adds comment and rating to that image and their own
        newUserInGroup(ec);

        // other user creates an image
        final Image imageOther = (Image) iUpdate.saveAndReturnObject(mmFactory.simpleImage()).proxy();

        // other user adds same comment and rating to both images
        CommentAnnotation comment = new CommentAnnotationI();
        comment.setTextValue(ode.rtypes.rstring("What a lovely image!"));
        comment = (CommentAnnotation) iUpdate.saveAndReturnObject(comment).proxy();

        LongAnnotation rating = new LongAnnotationI();
        rating.setLongValue(ode.rtypes.rlong(5));
        rating.setNs(ode.rtypes.rstring(NSINSIGHTRATING.value));
        rating = (LongAnnotation) iUpdate.saveAndReturnObject(rating).proxy();

        for (final Image image : new Image[] {imageOwner, imageOther}) {
            for (final Annotation annotation : new Annotation[] {comment, rating}) {
                final ImageAnnotationLink link = new ImageAnnotationLinkI();
                link.setParent(image);
                link.setChild(annotation);
                iUpdate.saveAndReturnObject(link);
            }
        }

        // owner deletes their image
        loginUser(ec);
        long id = imageOwner.getId().getValue();
        Delete2 dc = Requests.delete().target(imageOwner).build();
        callback(true, client, dc);

        // image is gone but other image and annotations remain
        assertDoesNotExist(imageOwner);
        assertExists(imageOther);
        assertExists(comment);
        assertExists(rating);
    }

    /**
     * Test to delete an image tagged collaboratively by another user.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     *
     *             Group changed from RWRW to RWRA for 4.4
     */
    @Test(groups = { "ticket:2881" })
    public void testDeleteTaggedImageTagOwnedByOther() throws Exception {
        // set up collaborative group with an "owner" user
        EventContext ec = newUserAndGroup("rwra--");

        // owner creates the image
        Image img = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());

        // tagger creates tag and tags the image
        newUserInGroup(ec);

        TagAnnotation c = new TagAnnotationI();
        c.setTextValue(ode.rtypes.rstring("tag"));
        c = (TagAnnotation) iUpdate.saveAndReturnObject(c);
        ImageAnnotationLink link = new ImageAnnotationLinkI();
        link.setParent(img);
        link.setChild(new TagAnnotationI(c.getId().getValue(), false));
        link = (ImageAnnotationLink) iUpdate.saveAndReturnObject(link);

        // owner tries to delete image.
        loginUser(ec);
        Delete2 dc = Requests.delete().target(img).build();
        callback(true, client, dc);

        assertDoesNotExist(img);
        assertExists(c);
    }

    /**
     * Test to delete a tag used by another user.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     *
     *             Group changed from RWRW to RWRA for 4.4
     */
    @Test(groups = "ticket:2962")
    public void testDeleteTagUsedByOther() throws Exception {
        // set up collaborative group with an "owner" user
        EventContext ec = newUserAndGroup("rwra--");

        // owner creates the image
        Image img = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());
        ode.client owner = disconnect();

        // tagger creates tag
        EventContext tagger = newUserInGroup(ec);

        TagAnnotation c = new TagAnnotationI();
        c.setTextValue(ode.rtypes.rstring("tag"));
        c = (TagAnnotation) iUpdate.saveAndReturnObject(c);
        disconnect();
        init(ec);
        // Image's owner tags the image.
        ImageAnnotationLink link = new ImageAnnotationLinkI();
        link.setParent(img);
        link.setChild(new TagAnnotationI(c.getId().getValue(), false));
        link = (ImageAnnotationLink) iUpdate.saveAndReturnObject(link);

        // Tag's owner now deletes the tag.
        init(tagger);
        Delete2 dc = Requests.delete().target(c).build();
        callback(false, client, dc);
        assertExists(c);
        assertExists(link);
        assertExists(img);
    }

    /**
     * Test to delete a tag used by another user. The tag is owned by the group
     * owner.
     *
     * On 2010.10.12 meeting, it was decided that this test will be allowed to
     * pass, i.e. the owner will be able to delete, but the clients will show a
     * warning: "You are deleting as an admin/PI".
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:2962")
    public void testDeleteTagOwnedByGroupOwnerUsedByOther() throws Exception {
        // set up collaborative group with an "owner" user
        EventContext ec = newUserAndGroup("rwrw--");

        // owner creates the image
        Image img = (Image) iUpdate.saveAndReturnObject(mmFactory
                .simpleImage());

        disconnect();

        // tagger creates tag
        EventContext tagger = newUserInGroup(ec);
        // make the tagger the group owner.
        makeGroupOwner();

        TagAnnotation c = new TagAnnotationI();
        c.setTextValue(ode.rtypes.rstring("tag"));
        c = (TagAnnotation) iUpdate.saveAndReturnObject(c);
        disconnect();

        init(ec);

        // Image's owner tags the image with another group's owner tag.
        ImageAnnotationLink link = new ImageAnnotationLinkI();
        link.setParent(img);
        link.setChild(new TagAnnotationI(c.getId().getValue(), false));
        link = (ImageAnnotationLink) iUpdate.saveAndReturnObject(link);

        disconnect();
        // Tag's owner now deletes the tag.
        init(tagger);
        Delete2 dc = Requests.delete().target(c).build();
        callback(true, client, dc);

        assertNoneExist(c, link);
        assertExists(img);
    }

    /**
     * Test to delete an image viewed by another user in a RWRW-- group.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:2963")
    public void testDeleteImageViewedByOtherRenderingSettingsOnlyRWRW()
            throws Exception {
        EventContext ownerCtx = newUserAndGroup("rwrw--");
        // owner creates the image
        Image image = (Image) iUpdate.saveAndReturnObject(mmFactory
                .createImage());
        // create rendering settings for that user.
        Pixels pixels = image.getPrimaryPixels();
        long id = pixels.getId().getValue();
        long imageID = image.getId().getValue();
        // Image
        // method already tested
        IRenderingSettingsPrx prx = factory.getRenderingSettingsService();
        prx.setOriginalSettingsInSet(Image.class.getName(),
                Arrays.asList(imageID));
        RenderingDef ownerDef = factory.getPixelsService().retrieveRndSettings(
                id);

        newUserInGroup(ownerCtx);
        prx = factory.getRenderingSettingsService();
        prx.setOriginalSettingsInSet(Image.class.getName(),
                Arrays.asList(imageID));
        RenderingDef otherDef = factory.getPixelsService().retrieveRndSettings(
                id);
        assertAllExist(ownerDef, otherDef);
        disconnect();

        // Delete the image.
        loginUser(ownerCtx);
        Delete2 dc = Requests.delete().target(image).build();
        callback(true, client, dc);
        assertNoneExist(image, ownerDef, otherDef);
    }

    /**
     * Test to delete possible graph P/D in collaborative RWRA-- group. Attempts
     * to delete a graph by a member
     *
     * @throws Exception
     *             Thrown if an error occurred.
     *
     *             Group changed from RWRW to RWRA for 4.4
     */
    @Test(groups = "ticket:3119")
    public void testDeleteProjectDatasetGraphLinkByGroupOwnerRWRA()
            throws Exception {
        EventContext ctx = newUserAndGroup("rwra--");
        Project project = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());
        Dataset dataset = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        // now link the project and dataset.
        ProjectDatasetLink link = new ProjectDatasetLinkI();
        link.setChild((Dataset) dataset.proxy());
        link.setParent((Project) project.proxy());
        iUpdate.saveAndReturnObject(link);
        ode.client user1 = disconnect();

        // new user
        newUserInGroup(ctx);
        makeGroupOwner();
        // Now try to delete the project.
        Delete2 dc = Requests.delete().target(project).build();
        callback(true, client, dc);
        assertDoesNotExist(project);
        assertDoesNotExist(dataset);
    }

    /**
     * Test to delete possible graph P/D in collaborative RWRW-- group. The
     * owner of the dataset creates the link with another user's project.
     * Attempt to delete the dataset. None of the users are owner of the group.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:3119")
    public void testDeleteDatasetGraphLinkDoneByDatasetOwnerRWRW()
            throws Exception {
        EventContext ctx = newUserAndGroup("rwrw--");
        Project project = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());
        ode.client user1 = disconnect();

        // new user
        newUserInGroup(ctx);
        Dataset dataset = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        // now link the project and dataset.
        ProjectDatasetLink link = new ProjectDatasetLinkI();
        link.setChild((Dataset) dataset.proxy());
        link.setParent((Project) project.proxy());
        link = (ProjectDatasetLink) iUpdate.saveAndReturnObject(link);
        // Now try to delete the dataset.
        Delete2 dc = Requests.delete().target(dataset).build();
        callback(true, client, dc);
        assertDoesNotExist(dataset);
        assertExists(project);
        assertDoesNotExist(link);
    }

    /**
     * Test to delete possible graph P/D in collaborative RWRW-- group. The
     * owner of the project creates the link with another user's dataset.
     * Attempt to delete the dataset. None of the users are owner of the group.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:3119")
    public void testDeleteDatasetProjectGraphLinkDoneByProjectOwnerRWRW()
            throws Exception {
        EventContext ctx = newUserAndGroup("rwrw--");
        Project project = (Project) iUpdate.saveAndReturnObject(mmFactory
                .simpleProjectData().asIObject());
        disconnect();

        // new user
        EventContext user2Ctx = newUserInGroup(ctx);
        Dataset dataset = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        disconnect();
        loginUser(ctx);
        // now link the project and dataset.
        ProjectDatasetLink link = new ProjectDatasetLinkI();
        link.setChild((Dataset) dataset.proxy());
        link.setParent((Project) project.proxy());
        link = (ProjectDatasetLink) iUpdate.saveAndReturnObject(link);
        disconnect();
        loginUser(user2Ctx);
        // Now try to delete the dataset.
        Delete2 dc = Requests.delete().target(dataset).build();
        callback(true, client, dc);
        assertDoesNotExist(dataset);
        assertExists(project);
        assertDoesNotExist(link);
    }

    /**
     * Test to delete a dataset in collaborative RWRA-- group. The dataset will
     * contain 2 images, one owned by another user. None of the users are owner
     * of the group.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     *
     *             Group changed from RWRW to RWRA for 4.4
     */
    public void testDeleteDatasetImagesGraphRWRA() throws Exception {
        EventContext ctx = newUserAndGroup("rwra--");
        Dataset dataset = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        Image image1 = (Image) iUpdate.saveAndReturnObject(mmFactory
                .createImage());
        DatasetImageLink link = new DatasetImageLinkI();
        link.setChild((Image) image1.proxy());
        link.setParent((Dataset) dataset.proxy());
        iUpdate.saveAndReturnObject(link);
        disconnect();
        EventContext user2Ctx = newUserInGroup(ctx, true);
        loginUser(user2Ctx);
        // create new user.
        Image image2 = (Image) iUpdate.saveAndReturnObject(mmFactory
                .createImage());
        link = new DatasetImageLinkI();
        link.setChild((Image) image2.proxy());
        link.setParent((Dataset) dataset.proxy());
        iUpdate.saveAndReturnObject(link);
        disconnect();
        // now try to delete the dataset
        loginUser(ctx);
        Delete2 dc = Requests.delete().target(dataset).build();
        callback(true, client, dc);
        assertDoesNotExist(dataset);
        assertDoesNotExist(image1);
        assertExists(image2);
    }

    /**
     * Test to delete an image in collaborative RWRW-- group. The image is
     * linked to another user's dataset. The image was added by the owner of the
     * image. None of the users are owner of the group.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:3119")
    public void testDeleteImageInOtherUserDatasetRWRW() throws Exception {
        EventContext ctx = newUserAndGroup("rwrw--");
        Dataset dataset = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        disconnect();
        EventContext user2Ctx = newUserInGroup(ctx);
        loginUser(user2Ctx);
        // create new user.
        Image image = (Image) iUpdate.saveAndReturnObject(mmFactory
                .createImage());
        DatasetImageLink link = new DatasetImageLinkI();
        link.setChild((Image) image.proxy());
        link.setParent((Dataset) dataset.proxy());
        iUpdate.saveAndReturnObject(link);
        // Now try to delete the image
        Delete2 dc = Requests.delete().target(image).build();
        callback(true, client, dc);

        assertDoesNotExist(image);
        assertExists(dataset);
    }

    /**
     * Test to delete an image in collaborative RWRW-- group. The image is
     * linked to another user's dataset. The image was added by the owner of the
     * dataset. None of the users are owner of the group.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:3119")
    public void testDeleteImageInOtherUserDatasetAddedByDatasetOwnerRWRW()
            throws Exception {
        EventContext ctx = newUserAndGroup("rwrw--");
        Dataset dataset = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        disconnect();
        EventContext user2Ctx = newUserInGroup(ctx);
        loginUser(user2Ctx);
        // create new user.
        Image image = (Image) iUpdate.saveAndReturnObject(mmFactory
                .createImage());
        disconnect();
        loginUser(ctx);
        DatasetImageLink link = new DatasetImageLinkI();
        link.setChild((Image) image.proxy());
        link.setParent((Dataset) dataset.proxy());
        iUpdate.saveAndReturnObject(link);
        disconnect();
        // now try to delete the image
        loginUser(user2Ctx);

        Delete2 dc = Requests.delete().target(image).build();
        callback(true, client, dc);

        assertDoesNotExist(image);
        assertExists(dataset);
    }

    /**
     * Test to delete possible graph Screen/Plate in collaborative RWRA-- group.
     * The graph delete by member None of the users are owner of the group.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     *
     *             Group changed from RWRW to RWRA for 4.4
     */
    @Test(groups = "ticket:3119")
    public void testDeleteScreenPlateGraphLinkRWRA() throws Exception {
        EventContext ctx = newUserAndGroup("rwra--");
        Screen screen = (Screen) iUpdate.saveAndReturnObject(mmFactory
                .simpleScreenData().asIObject());
        Plate plate = (Plate) iUpdate.saveAndReturnObject(mmFactory
                .simplePlateData().asIObject());
        // now link the project and dataset.
        ScreenPlateLink link = new ScreenPlateLinkI();
        link.setChild((Plate) plate.proxy());
        link.setParent((Screen) screen.proxy());
        link = (ScreenPlateLink) iUpdate.saveAndReturnObject(link);
        disconnect();

        // new user
        newUserInGroup(ctx);

        // Now try to delete the screen
        Delete2 dc = Requests.delete().target(screen).build();
        callback(false, client, dc);

        assertExists(screen);
        assertExists(plate);
    }

    /**
     * Test to delete possible graph Screen/Plate in collaborative RWRA-- group.
     * The graph delete by member None of the users are owner of the group.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     *
     *             Group changed from RWRW to RWRA for 4.4
     */
    @Test(groups = "ticket:3119")
    public void testDeleteScreenPlateGraphLinkByGroupOwnerRWRA()
            throws Exception {
        EventContext ctx = newUserAndGroup("rwra--");
        Screen screen = (Screen) iUpdate.saveAndReturnObject(mmFactory
                .simpleScreenData().asIObject());
        Plate plate = (Plate) iUpdate.saveAndReturnObject(mmFactory
                .simplePlateData().asIObject());
        // now link the project and dataset.
        ScreenPlateLink link = new ScreenPlateLinkI();
        link.setChild((Plate) plate.proxy());
        link.setParent((Screen) screen.proxy());
        link = (ScreenPlateLink) iUpdate.saveAndReturnObject(link);
        disconnect();

        // new user
        newUserInGroup(ctx);
        makeGroupOwner();
        // Now try to delete the screen
        Delete2 dc = Requests.delete().target(screen).build();
        callback(true, client, dc);

        assertDoesNotExist(screen);
        assertDoesNotExist(plate);
    }

    /**
     * Test to delete possible graph Screen/Plate in collaborative RWRW-- group.
     * The owner of the plate creates the link with another user's screen.
     * Attempt to delete the plate. None of the users are owner of the group.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:3119")
    public void testDeletePlateGraphLinkDoneByPlateOwnerRWRW() throws Exception {
        EventContext ctx = newUserAndGroup("rwrw--");
        Screen screen = (Screen) iUpdate.saveAndReturnObject(mmFactory
                .simpleScreenData().asIObject());
        disconnect();

        // new user
        newUserInGroup(ctx);

        Plate plate = (Plate) iUpdate.saveAndReturnObject(mmFactory
                .simplePlateData().asIObject());
        ScreenPlateLink link = new ScreenPlateLinkI();
        link.setChild((Plate) plate.proxy());
        link.setParent((Screen) screen.proxy());
        link = (ScreenPlateLink) iUpdate.saveAndReturnObject(link);
        // Now try to delete the plate
        Delete2 dc = Requests.delete().target(plate).build();
        callback(true, client, dc);

        assertDoesNotExist(plate);
        assertExists(screen);
        assertDoesNotExist(link);
    }

    /**
     * Test to delete possible graph Screen/Plate in collaborative RWRW-- group.
     * The owner of the screen creates the link with another user's plate.
     * Attempt to delete the plate. None of the users are owner of the group.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:3119")
    public void testDeletePlateScreenGraphLinkDoneByScreenOwnerRWRW()
            throws Exception {
        EventContext ctx = newUserAndGroup("rwrw--");
        Screen screen = (Screen) iUpdate.saveAndReturnObject(mmFactory
                .simpleScreenData().asIObject());
        disconnect();

        // new user
        EventContext user2Ctx = newUserInGroup(ctx);
        Plate plate = (Plate) iUpdate.saveAndReturnObject(mmFactory
                .simplePlateData().asIObject());
        disconnect();
        loginUser(ctx);
        // now link the project and dataset.
        ScreenPlateLink link = new ScreenPlateLinkI();
        link.setChild((Plate) plate.proxy());
        link.setParent((Screen) screen.proxy());
        link = (ScreenPlateLink) iUpdate.saveAndReturnObject(link);
        disconnect();
        loginUser(user2Ctx);
        // Now try to delete the plate
        Delete2 dc = Requests.delete().target(plate).build();
        callback(true, client, dc);

        assertDoesNotExist(plate);
        assertExists(screen);
        assertDoesNotExist(link);
    }

    /**
     * Test to delete a dataset in collaborative RWRW-- group. The dataset
     * contains one image, the image has added by another user to his/her
     * dataset. None of the users are owner of the group.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    public void testDeleteDatasetWithInOtherUserDatasetRWRW() throws Exception {
        EventContext ctx = newUserAndGroup("rwrw--");
        Dataset dataset = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        Image image = (Image) iUpdate.saveAndReturnObject(mmFactory
                .createImage());
        DatasetImageLink link = new DatasetImageLinkI();
        link.setChild((Image) image.proxy());
        link.setParent((Dataset) dataset.proxy());
        iUpdate.saveAndReturnObject(link);
        disconnect();

        EventContext user2Ctx = newUserInGroup(ctx);
        loginUser(user2Ctx);
        // create new user.
        Dataset dataset2 = (Dataset) iUpdate.saveAndReturnObject(mmFactory
                .simpleDatasetData().asIObject());
        link = new DatasetImageLinkI();
        link.setChild((Image) image.proxy());
        link.setParent((Dataset) dataset2.proxy());
        iUpdate.saveAndReturnObject(link);
        disconnect();
        loginUser(ctx);
        // Now try to delete the dataset
        Delete2 dc = Requests.delete().target(dataset).build();
        callback(true, client, dc);

        assertDoesNotExist(dataset);
        assertExists(image);
        assertExists(dataset2);
    }

    /**
     * Test to try to delete an object by the administrator in a read-annotate
     * collaborative group i.e. RWRA--
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testDeleteObjectByAdminRWRA() throws Exception {

        // set up collaborative group
        newUserAndGroup("rwra--");

        // owner creates the image
        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());

        // admin deletes the object.
        logRootIntoGroup();
        Delete2 dc = Requests.delete().target(img).build();
        callback(true, client, dc);

        assertDoesNotExist(img);
    }

    /**
     * Test to try to delete an object by the administrator in a read-write
     * collaborative group i.e. RWRW--
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testDeleteObjectByAdminRWRW() throws Exception {

        // set up collaborative group
        newUserAndGroup("rwrw--");

        // owner creates the image
        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());

        // admin deletes the object.
        logRootIntoGroup();
        Delete2 dc = Requests.delete().target(img).build();
        callback(true, client, dc);

        assertDoesNotExist(img);
    }

    /**
     * Test to try to delete an object by the owner of a read-annotate i.e.
     * RWRA--
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testDeleteObjectByGroupOwnerRWRA() throws Exception {
        EventContext ownerEc = newUserAndGroup("rwra--");

        // owner creates the image
        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());

        // group owner deletes it
        disconnect();
        newUserInGroup(ownerEc);
        makeGroupOwner();
        String sql = "select i from Image as i ";
        sql += "where i.id = :id";
        ParametersI param = new ParametersI();
        param.addId(img.getId().getValue());
        List<IObject> images = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(1, images.size());
        img = (Image) images.get(0);

        Permissions perms = img.getDetails().getPermissions();
        Assert.assertTrue(perms.canDelete());
        Delete2 dc = Requests.delete().target(img).build();
        callback(true, client, dc);

        // Image should be deleted.
        assertDoesNotExist(img);
    }

    /**
     * Test to try to delete an object by a member of a read-only group i.e.
     * RWR---
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    public void testDeleteObjectByMemberRWR() throws Exception {
        EventContext ownerEc = newUserAndGroup("rwr---");

        // owner creates the image
        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());

        // group owner deletes it
        disconnect();
        newUserInGroup(ownerEc);
        Delete2 dc = Requests.delete().target(img).build();
        callback(false, client, dc);

        assertExists(img);
    }

    /**
     * Test to try to delete an object by a member of a read-annotate group i.e.
     * RWRA--
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    public void testDeleteObjectByMemberRWRA() throws Exception {
        EventContext ownerEc = newUserAndGroup("rwra--");

        // owner creates the image
        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());

        // group owner deletes it
        disconnect();
        newUserInGroup(ownerEc);
        Delete2 dc = Requests.delete().target(img).build();
        callback(false, client, dc);

        assertExists(img);
    }

    /**
     * Test to try to delete an object by a member of a read-write group i.e.
     * RWRW--
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testDeleteObjectByMemberRWRW() throws Exception {
        EventContext ownerEc = newUserAndGroup("rwrw--");

        // owner creates the image
        Image img = (Image) iUpdate
                .saveAndReturnObject(mmFactory.createImage());

        // group owner deletes it
        disconnect();
        newUserInGroup(ownerEc);
        String sql = "select i from Image as i ";
        sql += "where i.id = :id";
        ParametersI param = new ParametersI();
        param.addId(img.getId().getValue());
        List<IObject> images = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(1, images.size());
        img = (Image) images.get(0);

        Permissions perms = img.getDetails().getPermissions();
        Assert.assertTrue(perms.canDelete());

        Delete2 dc = Requests.delete().target(img).build();
        callback(true, client, dc);

        assertDoesNotExist(img);
    }

    /**
     * Test that restricted administrators cannot always delete others' data from the <tt>system</tt> group.
     * @param isRootOwner is the data owner <tt>root</tt>?
     * @param isRootDeleting is the data deleter <tt>root</tt>?
     * @throws Exception unexpected
     */
    @Test(dataProvider = "test cases using two Boolean arguments")
    public void testDeleteSystemImage(boolean isRootOwner, boolean isRootDeleting) throws Exception {
        final boolean isExpectSuccess = isRootDeleting || !isRootOwner;
        final EventContext lightAdmin;
        if (isRootOwner && isRootDeleting) {
            lightAdmin = null;
        } else {
            final IAdminPrx iAdminRoot = root.getSession().getAdminService();
            lightAdmin = newUserInGroup(new ExperimenterGroupI(roles.systemGroupId, false), false);
            iAdminRoot.setAdminPrivileges(new ExperimenterI(lightAdmin.userId, false), Collections.<AdminPrivilege>emptyList());
        }
        if (isRootOwner) {
            logRootIntoGroup(roles.systemGroupId);
        } else {
            loginUser(lightAdmin);
        }
        Image image = (Image) iUpdate.saveAndReturnObject(mmFactory.simpleImage()).proxy();
        if (isRootOwner != isRootDeleting) {
            if (isRootDeleting) {
                logRootIntoGroup(roles.systemGroupId);
            } else {
                loginUser(lightAdmin);
            }
        }
        image = (Image) iQuery.get("Image", image.getId().getValue());
        Assert.assertEquals(image.getDetails().getPermissions().canDelete(), isExpectSuccess);
        doChange(client, factory, Requests.delete().target(image).build(), isExpectSuccess);
    }
}
