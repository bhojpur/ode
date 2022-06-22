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

import integration.AbstractServerTest;
import integration.DeleteServiceTest;

import ode.cmd.Delete2;
import ode.gateway.util.Requests;
import ode.model.Image;
import ode.model.Pixels;
import ode.sys.ParametersI;

import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * Collections of tests for the <code>Delete</code> service. This carries on
 * from {@link DeleteServiceTest}
 */
@Test(groups = "ticket:2615")
public class RelatedToTest extends AbstractServerTest {

    @Test(groups = { "ticket:1228", "ticket:2776" })
    public void testDeleteWithProjectionRemovesRelatedTo() throws Exception {

        newUserAndGroup("rw----");
        Image i1 = (Image) iUpdate.saveAndReturnObject(mmFactory.createImage());
        Pixels p1 = i1.getPixels(0);
        Image i2 = (Image) iUpdate.saveAndReturnObject(mmFactory.createImage());
        Pixels p2 = i2.getPixels(0);

        p2.setRelatedTo(p1);
        p2 = (Pixels) iUpdate.saveAndReturnObject(p2);
        Assert.assertEquals(p1.getId(), p2.getRelatedTo().getId());

        final Delete2 dc = Requests.delete().target(i1).build();
        callback(true, client, dc);

        assertDoesNotExist(i1);
        assertDoesNotExist(p1);
        assertExists(i2);
        assertExists(p2);

    }

    /**
     * Test to control if the related pixels set is to <code>null</code> when
     * deleted.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:2776")
    public void testPixelsRelatedTo() throws Exception {
        Image img1 = (Image) iUpdate.saveAndReturnObject(mmFactory
                .createImage());
        Image img2 = (Image) iUpdate.saveAndReturnObject(mmFactory
                .createImage());
        Pixels pixels1 = img1.getPrimaryPixels();
        Pixels pixels2 = img2.getPrimaryPixels();
        pixels1.setRelatedTo(pixels2);
        pixels1 = (Pixels) iUpdate.saveAndReturnObject(pixels1);
        Pixels pixels = pixels1.getRelatedTo();
        Assert.assertNotNull(pixels);
        Assert.assertEquals(pixels.getId().getValue(), pixels2.getId().getValue());

        final Delete2 dc = Requests.delete().target(img2).build();
        callback(true, client, dc);

        String sql = "select i from Image i where i.id = :id";
        ParametersI param = new ParametersI();
        param.addId(img2.getId().getValue());
        Assert.assertNull(iQuery.findByQuery(sql, param));
        sql = "select i from Pixels i where i.id = :id";
        param = new ParametersI();
        param.addId(pixels2.getId().getValue());
        Assert.assertNull(iQuery.findByQuery(sql, param));

        sql = "select i from Pixels i where i.id = :id";
        param = new ParametersI();
        param.addId(pixels1.getId().getValue());
        pixels1 = (Pixels) iQuery.findByQuery(sql, param);
        Assert.assertNull(pixels1.getRelatedTo());
    }

    /**
     * Test to control if the related pixels set is to <code>null</code> when
     * deleted.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(groups = "ticket:2776")
    public void testPixelsRelatedToUsingDeleteImage() throws Exception {
        Image img1 = (Image) iUpdate.saveAndReturnObject(mmFactory
                .createImage());
        Image img2 = (Image) iUpdate.saveAndReturnObject(mmFactory
                .createImage());
        Pixels pixels1 = img1.getPrimaryPixels();
        Pixels pixels2 = img2.getPrimaryPixels();
        pixels1.setRelatedTo(pixels2);
        pixels1 = (Pixels) iUpdate.saveAndReturnObject(pixels1);
        Pixels pixels = pixels1.getRelatedTo();
        Assert.assertNotNull(pixels);
        Assert.assertEquals(pixels.getId().getValue(), pixels2.getId().getValue());

        final Delete2 dc = Requests.delete().target(img2).build();
        callback(true, client, dc);

        String sql = "select i from Image i where i.id = :id";
        ParametersI param = new ParametersI();
        param.addId(img2.getId().getValue());
        Assert.assertNull(iQuery.findByQuery(sql, param));
        sql = "select i from Pixels i where i.id = :id";
        param = new ParametersI();
        param.addId(pixels2.getId().getValue());
        Assert.assertNull(iQuery.findByQuery(sql, param));

        sql = "select i from Pixels i where i.id = :id";
        param = new ParametersI();
        param.addId(pixels1.getId().getValue());
        pixels1 = (Pixels) iQuery.findByQuery(sql, param);
        Assert.assertNull(pixels1.getRelatedTo());
    }

}
