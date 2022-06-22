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

import java.util.ArrayList;
import java.util.List;

import integration.AbstractServerTest;
import ode.RType;
import ode.ServerError;
import ode.api.IRenderingSettingsPrx;
import ode.cmd.Delete2;
import ode.cmd.SkipHead;
import ode.gateway.util.Requests;
import ode.model.Dataset;
import ode.model.DatasetImageLink;
import ode.model.DatasetImageLinkI;
import ode.model.Fileset;
import ode.model.FilesetI;
import ode.model.Image;
import ode.sys.Parameters;
import ode.sys.ParametersI;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * Tests for deleting hierarchies and the effects that that should have under
 * double- and multiple-linking.
 */
@Test(groups = { "delete", "integration"})
public class MultiImageFilesetDeleteTest extends AbstractServerTest {

	
	/**
     * Test the delete of datasets hosting images composing a MIF.
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testDeleteDatasetWithSharedImageFromMIF()
    	throws Exception 
    {
    	//first create a project
    	Image i1 = (Image) iUpdate.saveAndReturnObject(
    			mmFactory.simpleImage());
    	Image i2 = (Image) iUpdate.saveAndReturnObject(
    			mmFactory.simpleImage());

    	Fileset fileset = new FilesetI();
    	fileset.setTemplatePrefix(ode.rtypes.rstring("fake"));
    	fileset.addImage(i1);
    	fileset.addImage(i2);
    	fileset = (Fileset) iUpdate.saveAndReturnObject(fileset);
    	Assert.assertEquals(fileset.copyImages().size(), 2);
    	Dataset d1 = (Dataset) iUpdate.saveAndReturnObject(
    			mmFactory.simpleDatasetData().asIObject());
    	Dataset d2 = (Dataset) iUpdate.saveAndReturnObject(
    			mmFactory.simpleDatasetData().asIObject());
    	DatasetImageLink link = new DatasetImageLinkI();
    	link.setChild((Image) i1.proxy());
    	link.setParent((Dataset) d1.proxy());
    	iUpdate.saveAndReturnObject(link);
    	link = new DatasetImageLinkI();
    	link.setChild((Image) i2.proxy());
    	link.setParent((Dataset) d2.proxy());
    	iUpdate.saveAndReturnObject(link);
    	Delete2 dc = Requests.delete().target(d1, d2).build();
    	doChange(dc);
    	assertDoesNotExist(d1);
    	assertDoesNotExist(d2);
    	assertDoesNotExist(fileset);
    	assertDoesNotExist(i1);
    	assertDoesNotExist(i2);
    }

    /* for anchored delete test cases */
    private enum Target {IMAGES_OF_FILESET, RENDERING_SETTINGS_OF_FILESET, RENDERING_SETTINGS_OF_IMAGE, NONSENSE_OF_IMAGE};

    /**
     * Asserts that a given image has some rendering settings.
     * @param imageId the ID of an image
     * @param hasSettings if the image is expected to have some rendering settings
     * @throws ServerError unexpected
     */
    private void assertHasRenderingDef(long imageId, boolean hasSettings) throws ServerError {
        final String query = "SELECT id FROM RenderingDef WHERE pixels.image.id = :id";
        final Parameters params = new ParametersI().addId(imageId);
        final List<List<RType>> results = iQuery.projection(query, params);
        Assert.assertEquals(results.isEmpty(), !hasSettings);
    }

    /**
     * Test an anchored delete operation expressed using {@link SkipHead}.
     * @param target the kind of object to actually delete
     * @param dryRun if the deletion should be a dry run
     * @throws Exception unexpected
     */
    @Test(dataProvider = "anchored")
    public void testAnchoredDelete(Target target, boolean dryRun) throws Exception {

        /* create a fileset with two images, each of which has rendering settings */

        final Image image1 = (Image) iUpdate.saveAndReturnObject(mmFactory.createImage());
        final Image image2 = (Image) iUpdate.saveAndReturnObject(mmFactory.createImage());
        final long image1Id = image1.getId().getValue();
        final long image2Id = image2.getId().getValue();

        Fileset fileset = newFileset();
        fileset.addImage(image1);
        fileset.addImage(image2);
        fileset = (Fileset) iUpdate.saveAndReturnObject(fileset);

        final IRenderingSettingsPrx renderingSettingsService = factory.getRenderingSettingsService();
        renderingSettingsService.setOriginalSettingsInImage(image1Id);
        renderingSettingsService.setOriginalSettingsInImage(image2Id);

        /* delete the images of the fileset, the rendering settings of one of the images, or nonsense */

        final SkipHead skipHead;
        switch (target) {
        case IMAGES_OF_FILESET:
            skipHead = Requests.skipHead().target(fileset).startFrom("Image").dryRun(dryRun).request(Delete2.class).build();
            break;
        case RENDERING_SETTINGS_OF_FILESET:
            skipHead = Requests.skipHead().target(fileset).startFrom("RenderingDef").dryRun(dryRun).request(Delete2.class).build();
            break;
        case RENDERING_SETTINGS_OF_IMAGE:
            skipHead = Requests.skipHead().target(image1).startFrom("RenderingDef").dryRun(dryRun).request(Delete2.class).build();
            break;
        case NONSENSE_OF_IMAGE:
            skipHead = Requests.skipHead().target(image1).startFrom("I like snails").dryRun(dryRun).request(Delete2.class).build();
            break;
        default:
            skipHead = null;
            Assert.fail("unexpected target for delete");
        }

        final boolean isExpectSuccess = target != Target.NONSENSE_OF_IMAGE;

        doChange(client, factory, skipHead, isExpectSuccess);

        /* check the outcome */

        if (target == Target.IMAGES_OF_FILESET && !dryRun) {
            /* actually deleted the fileset via its images */
        } else {

            /* deleted no more than rendering settings, but check exactly what */

            assertExists(fileset);
            assertExists(image1);
            assertExists(image2);

            final boolean isImage1SettingsExist, isImage2SettingsExist;
            if (target == Target.NONSENSE_OF_IMAGE || dryRun) {
                isImage1SettingsExist = true;
                isImage2SettingsExist = true;
            } else {
                isImage1SettingsExist = false;
                isImage2SettingsExist = target == Target.RENDERING_SETTINGS_OF_IMAGE;
            }
            assertHasRenderingDef(image1Id, isImage1SettingsExist);
            assertHasRenderingDef(image2Id, isImage2SettingsExist);

            /* delete the fileset */
            final Delete2 delete = Requests.delete().target(fileset).build();
            doChange(delete);
        }

        /* fileset no longer exists */
        assertDoesNotExist(fileset);
        assertDoesNotExist(image1);
        assertDoesNotExist(image2);
    }

    /**
     * @return a variety of test cases for anchored delete
     */
    @DataProvider(name = "anchored")
    public Object[][] provideAnchoredDeleteCases() {
        int index = 0;
        final int TARGET = index++;
        final int DRY_RUN = index++;

        final boolean[] booleanCases = new boolean[]{false, true};

        final List<Object[]> testCases = new ArrayList<Object[]>();

        for (final Target target : Target.values()) {
            for (final boolean dryRun : booleanCases) {
                final Object[] testCase = new Object[index];
                testCase[TARGET] = target;
                testCase[DRY_RUN] = dryRun;
                // DEBUG: if (target == Target.IMAGES_OF_FILESET && dryRun == true)
                testCases.add(testCase);
            }
        }

        return testCases.toArray(new Object[testCases.size()][]);
    }
}
