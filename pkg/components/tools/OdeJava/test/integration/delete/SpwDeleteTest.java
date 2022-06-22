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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ode.specification.XMLMockObjects;
import ode.specification.XMLWriter;
import ode.xml.model.ODE;
import ode.cmd.Delete2;
import ode.cmd.graphs.ChildOption;
import ode.gateway.util.Requests;
import ode.model.Experiment;
import ode.model.Pixels;
import ode.model.Plate;
import ode.model.Screen;
import ode.model.WellSample;

import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * Tests for deleting screen/plate/wells
 */
@Test(groups = "ticket:2615")
public class SpwDeleteTest extends AbstractServerTest {

    @Test(groups = {"ticket12601", "ticket:3102", "ticket:11540"})
    public void testScreen() throws Exception {

        newUserAndGroup("rw----");

        List<Pixels> pixels = createScreen();

        Experiment exp = null;
        Screen screen = null;
        List<Plate> plates = new ArrayList<Plate>();
        List<WellSample> samples = new ArrayList<WellSample>();

        for (Pixels p : pixels) {

            Experiment e = getExperiment(p);
            if (exp == null) {
                exp = e;
            } else {
                Assert.assertEquals(exp.getId().getValue(), e.getId().getValue());
            }

            WellSample ws = getWellSample(p);
            Plate plate = ws.getWell().getPlate();
            Screen s = plate.copyScreenLinks().get(0).getParent();
            if (screen == null) {
                screen = s;
            } else {
                Assert.assertEquals(screen.getId().getValue(), s.getId().getValue());
            }
        }

        // In order to avoid ode.LockTimeout
        // see XMLMockObjects.createScreen()
        scalingFactor *= 1*2*2*2*2;

        final Delete2 dc = Requests.delete().target(screen).build();
        callback(true, client, dc);

        assertDoesNotExist(screen);
        assertNoneExist(plates.toArray(new Plate[0]));
        assertNoneExist(samples.toArray(new WellSample[0]));
        assertNoneExist(pixels.toArray(new Pixels[0]));

    }

    /**
     * Tests to delete a screen and keep the plate.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    public void testScreenKeepPlates() throws Exception {

        newUserAndGroup("rw----");

        List<Pixels> pixels = createScreen();

        Pixels p = pixels.get(0);
        WellSample ws = getWellSample(p);
        Plate plate = ws.getWell().getPlate();
        Screen screen = plate.copyScreenLinks().get(0).getParent();

        final ChildOption option = Requests.option().excludeType("Plate").build();
        final Delete2 dc = Requests.delete().target(screen).option(option).build();
        callback(true, client, dc);

        assertDoesNotExist(screen);
        assertExists(plate);

    }

    @Test(groups = {"ticket12601", "ticket:3890"})
    public void testImportMultiplePlates() throws Exception {
        create(new Creator() {
            public ODE create(XMLMockObjects xml) {
                return xml.createPopulatedScreen(2, 2, 2, 2, 2);
            }
        });
    }

    //
    // Helpers
    //

    interface Creator {
        ODE create(XMLMockObjects xml);
    }

    private List<Pixels> createScreen() throws IOException, Exception {
        return create(new Creator() {
            public ODE create(XMLMockObjects xml) {
                return xml.createPopulatedScreen();
            }
        });
    }

    private List<Pixels> create(Creator creator) throws Exception {

        File f = File.createTempFile("testImportPlate", ".ode.xml");
        f.deleteOnExit();
        XMLMockObjects xml = new XMLMockObjects();
        XMLWriter writer = new XMLWriter();
        ODE ode = creator.create(xml);
        writer.writeFile(f, ode, true);
        List<Pixels> pixels = null;
        try {
            pixels = importFile(f, "ode.xml");
        } catch (Throwable e) {
            throw new Exception("cannot import the plate", e);
        }
        return pixels;
    }

}
