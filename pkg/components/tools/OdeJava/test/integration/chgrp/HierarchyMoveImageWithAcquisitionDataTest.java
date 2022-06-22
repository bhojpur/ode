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

import java.io.File;
import java.util.List;

import ode.specification.XMLMockObjects;
import ode.specification.XMLWriter;
import ode.xml.model.ODE;
import ode.ServerError;
import ode.cmd.Chgrp2;
import ode.gateway.util.Requests;
import ode.model.ExperimenterGroup;
import ode.model.Image;
import ode.model.Instrument;
import ode.model.Laser;
import ode.model.LightSource;
import ode.model.Objective;
import ode.model.Pixels;
import ode.sys.ParametersI;

import org.apache.commons.lang.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

public class HierarchyMoveImageWithAcquisitionDataTest extends
        AbstractServerTest {

    /**
     * Overridden to delete the files.
     *
     * @see AbstractServerTest#tearDown()
     */
    @Override
    @AfterClass
    public void tearDown() throws Exception {
    }

    private Pixels createImageWithAcquisitionData() throws Exception {
        File f = File.createTempFile(RandomStringUtils.random(100, false, true),
                ".ode.xml");
        f.deleteOnExit();
        XMLMockObjects xml = new XMLMockObjects();
        XMLWriter writer = new XMLWriter();
        ODE ode = xml.createImageWithAcquisitionData();
        writer.writeFile(f, ode, true);

        try {
            List<Pixels> pixels = importFile(f, "ode.xml");
            return pixels.get(0);
        } catch (Throwable e) {
            throw new Exception("cannot import image", e);
        }
    }

    /**
     * Helper method to load the Image.
     *
     * @param p
     * @return
     * @throws ServerError
     */
    protected Image getImageWithId(long imageId) throws ServerError {
        ParametersI queryParameters = new ParametersI();
        queryParameters.addId(imageId);
        String queryForImage = "select img from Image as img where img.id = :id";
        return (Image) iQuery.findByQuery(queryForImage, queryParameters);
    }

    /**
     * Test moving data as the data owner from a private to a private group
     *
     * @throws Exception unexpected
     */
    @Test
    public void moveImageRWtoRW() throws Exception {
        moveImageBetweenPermissionGroups("rw----", "rw----");
    }

    /**
     * Performs the changing of group for an image with an ROI owned by the same
     * user
     *
     * @param sourceGroupPermissions
     * @param targetGroupPermissions
     * @throws Exception
     */
    private void moveImageBetweenPermissionGroups(
            String sourceGroupPermissions, String targetGroupPermissions)
            throws Exception {

        XMLMockObjects mockObjects = new XMLMockObjects();

        long userId = iAdmin.getEventContext().userId;

        ExperimenterGroup sourceGroup = createGroupWithMember(userId,
                sourceGroupPermissions);

        ExperimenterGroup targetGroup = createGroupWithMember(userId,
                targetGroupPermissions);

        // force a refresh of the user's group membership
        iAdmin.getEventContext();

        Pixels pixels = createImageWithAcquisitionData();
        Image sourceImage = pixels.getImage();

        Image savedImage = (Image) iUpdate.saveAndReturnObject(sourceImage);
        long originalImageId = savedImage.getId().getValue();
        final long originalObjectiveId = savedImage.getObjectiveSettings().getObjective().getId().getValue();

        // make sure we are in the source group
        loginUser(sourceGroup);

        // Perform the move operation.
        final Chgrp2 dc = Requests.chgrp().target(savedImage).toGroup(targetGroup).build();
        callback(true, client, dc);

        // check if the image has been moved.
        Image returnedSourceImage = getImageWithId(originalImageId);
        Assert.assertNull(returnedSourceImage);

        // Move the user into the target group!
        loginUser(targetGroup);

        // get the acquisition data

        // check it's correct
        ode.xml.model.Laser xmlLaser = (ode.xml.model.Laser) mockObjects
                .createLightSource(ode.xml.model.Laser.class.getName(), 0);

        Image returnedTargetImage = getImageWithId(originalImageId);
        Assert.assertNotNull(returnedTargetImage);

        long instrumentId = returnedTargetImage.getInstrument().getId()
                .getValue();

        Instrument instrument = factory.getMetadataService().loadInstrument(
                instrumentId);

        List<LightSource> lights = instrument.copyLightSource();

        for (LightSource lightSource : lights) {
            if (lightSource instanceof Laser)
                validateLaser((Laser) lightSource, xmlLaser);
        }

        // check that the objective and settings moved
        final Objective returnedObjective = (Objective) iQuery.findByQuery(
                "SELECT i.objectiveSettings.objective FROM Image i WHERE i.id = :id",
                new ParametersI().addId(originalImageId));
        Assert.assertEquals(returnedObjective.getId().getValue(), originalObjectiveId);
    }

    /**
     * Creates a new group for the user with the permissions detailed
     *
     * @param userId
     * @param permissions
     * @return
     * @throws Exception
     */
    private ExperimenterGroup createGroupWithMember(long userId,
            String permissions) throws Exception {
        return newGroupAddUser(permissions, userId);
    }

    /**
     * Validates if the inserted object corresponds to the XML object.
     *
     * @param laser
     *            The laser to check.
     * @param xml
     *            The XML version.
     */
    private void validateLaser(Laser laser, ode.xml.model.Laser xml) {
        Assert.assertEquals(laser.getManufacturer().getValue(), xml.getManufacturer());
        Assert.assertEquals(laser.getModel().getValue(), xml.getModel());
        Assert.assertEquals(laser.getSerialNumber().getValue(), xml.getSerialNumber());
        Assert.assertEquals(laser.getLotNumber().getValue(), xml.getLotNumber());
        Assert.assertEquals(laser.getPower().getValue(), xml.getPower().value());
        Assert.assertEquals(laser.getType().getValue().getValue(),
                XMLMockObjects.LASER_TYPE.getValue());
    }
}
