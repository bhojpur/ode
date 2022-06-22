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

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import ode.model.Image;
import ode.model.Pixels;
import ode.model.StatsInfo;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Collection of tests to import "big" images and check min/max values.
 *
 *         These tests use a simple single plane image.
 *
 *         These tests depend on PNG being imported using fs-lite and having a
 *         pyramid file created. If that changes then it will be necessary to
 *         change the import.
 */
public class PyramidMinMaxTest extends AbstractServerTest {

    /** The format tested here. */
    private static final String FORMAT = "png";

    /* Total wait time will be WAITS * INTERVAL milliseconds */
    /** Maximum number of intervals to wait for pyramid **/
    private static final int WAITS = 100;

    /** Wait time in milliseconds **/
    private static final long INTERVAL = 100L;

    /** The collection of files that have to be deleted. */
    private List<File> files;

    /**
     * Overridden to initialize the list.
     *
     * @see AbstractServerTest#setUp()
     */
    @Override
    @BeforeClass
    protected void setUp() throws Exception {
        super.setUp();
        files = new ArrayList<File>();
    }

    /**
     * Overridden to delete the files.
     *
     * @see AbstractServerTest#tearDown()
     */
    @Override
    @AfterClass
    public void tearDown() throws Exception {
        Iterator<File> i = files.iterator();
        while (i.hasNext()) {
            i.next().delete();
        }
        files.clear();
    }

    /**
     * Import a <code>PNG</code> which generates a pyramid files with all
     * zeroes.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testForMinMaxAllZero() throws Exception {
        // Create a file, the default is all zeroes
        BufferedImage bi = new BufferedImage(ModelMockFactory.WIDTH,
                ModelMockFactory.HEIGHT, BufferedImage.TYPE_INT_RGB);
        File f = createImageFileWithBufferedImage(bi, FORMAT);
        files.add(f);
        Pixels p = importAndWaitForPyramid(f, FORMAT);
        assertMinMaxOnAllChannels(p, 0.0, 0.0);
    }

    /**
     * Import a <code>PNG</code> which generates a pyramid files with all
     * FFFFFF.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testForMinMaxAll255() throws Exception {
        // Create a png file, with all RGB values FFFFFF
        BufferedImage bi = new BufferedImage(ModelMockFactory.WIDTH,
                ModelMockFactory.HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < ModelMockFactory.WIDTH; x++) {
            for (int y = 0; y < ModelMockFactory.HEIGHT; y++) {
                bi.setRGB(x, y, Integer.valueOf("FFFFFF", 16));
            }
        }
        File f = createImageFileWithBufferedImage(bi, FORMAT);
        files.add(f);
        Pixels p = importAndWaitForPyramid(f, FORMAT);
        assertMinMaxOnAllChannels(p, 255.0, 255.0);
    }

    /**
     * Import a <code>PNG</code> which generates a pyramid files with some 0 and
     * some FFFFFF.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testForMinMaxHalfZeroAndHalf255() throws Exception {
        // Create a png file, with half RGB values FFFFFF
        BufferedImage bi = new BufferedImage(ModelMockFactory.WIDTH,
                ModelMockFactory.HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < ModelMockFactory.WIDTH; x += 2) {
            for (int y = 0; y < ModelMockFactory.HEIGHT; y++) {
                bi.setRGB(x, y, Integer.valueOf("FFFFFF", 16));
            }
        }
        File f = createImageFileWithBufferedImage(bi, FORMAT);
        files.add(f);
        Pixels p = importAndWaitForPyramid(f, FORMAT);
        assertMinMaxOnAllChannels(p, 0.0, 255.0);
    }

    /**
     * Import a <code>PNG</code> which generates a pyramid files with most 0 and
     * one FFFFFF.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testForMinMaxAllZeroWithFirst255() throws Exception {
        // Create a png file, with first RGB value FFFFFF
        BufferedImage bi = new BufferedImage(ModelMockFactory.WIDTH,
                ModelMockFactory.HEIGHT, BufferedImage.TYPE_INT_RGB);
        bi.setRGB(0, 0, Integer.valueOf("FFFFFF", 16));
        File f = createImageFileWithBufferedImage(bi, FORMAT);
        files.add(f);
        Pixels p = importAndWaitForPyramid(f, FORMAT);
        assertMinMaxOnAllChannels(p, 0.0, 255.0);
    }

    /**
     * Import a <code>PNG</code> which generates a pyramid files with most 0 and
     * one FFFFFF.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testForMinMaxAllZeroWithLast255() throws Exception {
        // Create a png file, with last RGB value FFFFFF
        BufferedImage bi = new BufferedImage(ModelMockFactory.WIDTH,
                ModelMockFactory.HEIGHT, BufferedImage.TYPE_INT_RGB);
        bi.setRGB(ModelMockFactory.WIDTH - 1, ModelMockFactory.HEIGHT - 1,
                Integer.valueOf("FFFFFF", 16));
        File f = createImageFileWithBufferedImage(bi, FORMAT);
        files.add(f);
        Pixels p = importAndWaitForPyramid(f, FORMAT);
        assertMinMaxOnAllChannels(p, 0.0, 255.0);
    }

    /**
     * Check the min and max on all three channels
     */
    private void assertMinMaxOnAllChannels(Pixels p, double min, double max) {
        for (int c = 0; c < 3; c++) {
            assert (p.getChannel(c).getStatsInfo().getGlobalMin().getValue() == min);
            assert (p.getChannel(c).getStatsInfo().getGlobalMax().getValue() == max);
        }
    }

    /**
     * Create an image file from a BufferedImage of the given format.
     */
    private File createImageFileWithBufferedImage(BufferedImage bi,
            String format) throws Exception {
        File f = File.createTempFile("testImage", "." + format);
        Iterator writers = ImageIO.getImageWritersByFormatName(format);
        ImageWriter writer = (ImageWriter) writers.next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(f);
        writer.setOutput(ios);
        writer.write(bi);
        ios.close();
        return f;
    }

    /**
     * Import an image file of the given format then wait for a pyramid file to
     * be generated by checking if stats exists.
     */
    private Pixels importAndWaitForPyramid(File f, String format)
            throws Exception {
        List<Pixels> pixels = null;
        try {
            pixels = importFile(f, FORMAT);
        } catch (Throwable e) {
            Assert.fail("Cannot import image file: " + f.getAbsolutePath()
                    + " Reason: " + e.toString());
        }
        // Wait for a pyramid to be built (stats will be not null)
        Pixels p = factory.getPixelsService().retrievePixDescription(
                pixels.get(0).getId().getValue());
        StatsInfo stats = p.getChannel(0).getStatsInfo();
        int waits = 0;
        while (stats == null && waits < WAITS) {
            Thread.sleep(INTERVAL);
            waits++;
            p = factory.getPixelsService().retrievePixDescription(
                    pixels.get(0).getId().getValue());
            stats = p.getChannel(0).getStatsInfo();
        }
        if (stats == null) {
            Assert.fail("No pyramid after " + WAITS * INTERVAL / 1000.0 + " seconds");
        }
        return p;
    }

    /**
     * Test the creation of tiles using RPSTileLoop.
     * @throws Exception
     */
    @Test
    public void testRPSTileloop() throws Exception {
        int sizeX = 256;
        int sizeY = 256;
        int sizeZ = 2;
        int sizeT = 3;
        int sizeC = 4;
        Image image = mmFactory.createImage(sizeX, sizeY, sizeZ, sizeT,
                sizeC, ModelMockFactory.UINT16);
        image = (Image) iUpdate.saveAndReturnObject(image);
        Pixels pixels = image.getPrimaryPixels();
     // first write to the image
        ode.util.RPSTileLoop loop = new ode.util.RPSTileLoop(
                client.getSession(), pixels);
        loop.forEachTile(sizeX, sizeY, new ode.util.TileLoopIteration() {
            public void run(ode.util.TileData data, int z, int c, int t,
                    int x, int y, int tileWidth, int tileHeight, int tileCount) {
                data.setTile(new byte[tileWidth * tileHeight * 8], z, c, t, x,
                        y, tileWidth, tileHeight);
            }
        });
        // This block will change the updateEvent on the pixels
        // therefore we're going to reload the pixels.

        image.setPixels(0, loop.getPixels());
    }
}
