package ode.services.roi.test;

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

import static ode.rtypes.rint;
import static ode.rtypes.rstring;
import static ode.rtypes.rtime;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import ode.api.AMD_IRoi_getShapeStats;
import ode.api.ShapeStats;
import ode.model.Image;
import ode.model.ImageI;
import ode.model.LogicalChannel;
import ode.model.Pixels;
import ode.model.Rectangle;
import ode.model.Roi;
import ode.model.Shape;
import ode.sys.ParametersI;

import org.testng.annotations.Test;

/**
 *
 */
@Test(groups = { "integration", "rois" })
public class ShapeStatsTest extends AbstractRoiITest {

    Image i;

    @Test
    public void testStatsOfRectangle() throws Exception {
        Pixels p = makeAndLoadPixels();
        Image i = new ImageI();
        i.addPixels(p);
        i.setName(rstring("statsOfRect"));
        Rectangle r = geomTool.rect(0, 0, 10, 10);
        Roi roi = createRoi(i, "statsOfRect", r);
        ShapeStats stats = assertStats(roi.getPrimaryShape());
    }

    @Test
    public void testStatsWithImplicitChannels() throws Exception {

        Pixels p = makeAndLoadPixels();
        LogicalChannel[] lcs = collectLogicalChannels(p);

        Image i = new ImageI();
        i.addPixels(p);
        i.setName(rstring("statsOfRectImplicitChannels"));
        Rectangle r = geomTool.rect(0, 0, 10, 10);
        Roi roi = createRoi(i, "statsOfRect", r);

        ShapeStats stats = assertStats(roi.getPrimaryShape(), lcs);
    }

    @Test
    public void testStatsWithExplicitChannels() throws Exception {

        Pixels p = makeAndLoadPixels();
        LogicalChannel[] lcs = collectLogicalChannels(p);

        Image i = new ImageI();
        i.addPixels(p);
        i.setName(rstring("statsOfRectExplicitChannels"));
        Rectangle r = geomTool.rect(0, 0, 10, 10);
        // Now add one channel
        LogicalChannel lc = (LogicalChannel) assertFindByQuery(
                "select lc from LogicalChannel lc where id = :id",
                new ParametersI().addId(lcs[0].getId().getValue())).get(0);
        r.setTheC(rint(0)); // Link the same channel

        Roi roi = createRoi(i, "statsOfRect", r);

        ShapeStats stats = assertStats(roi.getPrimaryShape(), lcs[0]);
    }

    protected ShapeStats assertStats(final Shape shape,
            final LogicalChannel... lcs) throws Exception {
        final RV rv = new RV();
        user_roisvc.getShapeStats_async(new AMD_IRoi_getShapeStats() {

            public void ice_exception(Exception ex) {
                rv.ex = ex;
            }

            public void ice_response(ShapeStats __ret) {
                rv.rv = __ret;
            }
        }, shape.getId().getValue(), null);

        rv.assertPassed();
        ShapeStats stats = (ShapeStats) rv.rv;
        assertNotNull(stats);
        assertEquals(shape.getId().getValue(), stats.shapeId);
        assertNotNull(stats.min);
        assertNotNull(stats.max);
        assertNotNull(stats.sum);
        assertNotNull(stats.mean);
        assertNotNull(stats.stdDev);
        assertNotNull(stats.pointsCount);
        assertNotNull(stats.channelIds);
        if (lcs.length > 0) {
            Set<Long> chosenIds = new HashSet<Long>();
            Set<Long> foundIds = new HashSet<Long>();
            for (LogicalChannel lc : lcs) {
                chosenIds.add(lc.getId().getValue());
            }
            for (long id : stats.channelIds) {
                foundIds.add(id);
            }
            assertTrue(chosenIds + " v. " + foundIds, chosenIds
                    .equals(foundIds));
        }
        return stats;
    }

    private Pixels makeAndLoadPixels() throws Exception, FileNotFoundException {
        long pix = makePixels();
        Pixels p = (Pixels) assertFindByQuery(
                "select p from Pixels p join fetch p.channels ch "
                        + "join fetch ch.logicalChannel lc where p.id = " + pix,
                null).get(0);
        return p;
    }

    private LogicalChannel[] collectLogicalChannels(Pixels p) {
        LogicalChannel[] lcs = new LogicalChannel[p.sizeOfChannels()];
        for (int j = 0; j < lcs.length; j++) {
            lcs[j] = p.getChannel(j).getLogicalChannel();
        }
        return lcs;
    }
}