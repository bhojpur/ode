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

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import loci.formats.ImageReader;
import ode.io.bioformats.BfPixelBuffer;
import ode.io.nio.DimensionsOutOfBoundsException;
import ode.io.nio.RomioPixelBuffer;
import ode.ServerError;
import ode.api.RawPixelsStorePrx;
import ode.model.Pixels;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class BfPixelBufferTest extends AbstractServerTest {

    private BfPixelBuffer bf;

    private RawPixelsStorePrx rps;

    private String destFileName;

    private void setUpTestFile(String fileName) throws Throwable,
            NoSuchAlgorithmException {
        File srcFile = new File(System.getProperty("java.io.tmpdir"), fileName);
        srcFile.deleteOnExit();
        srcFile.createNewFile();
        // Import file
        List<Pixels> pixList = importFile(srcFile, "fake");
        log.debug(String.format("Imported: %s, pixid: %d", srcFile, pixList
                .get(0).getId().getValue()));

        // Access the imported pixels via a RawPixelsStore
        rps = factory.createRawPixelsStore();
        rps.setPixelsId(pixList.get(0).getId().getValue(), false);

        // Access the data from file via BfPixelBuffer
        destFileName = srcFile.getCanonicalPath();
        bf = new BfPixelBuffer(destFileName, new ImageReader());
    }

    private void tidyUp() throws Throwable {
        rps.close();
    }

    public void testDV() throws Throwable {
        log.debug(String.format("DV test."));
        String name = "testDV&pixelType=int16&sizeX=20&sizeY=20&sizeZ=5&sizeT=6&sizeC=1.fake";
        setUpTestFile(name);
        testOtherGetters();
        testDimensionGetters();
        testSizeGetters();
        testOffsetGettersZero();
        testOffsetGetters();
        testCheckBounds();
        tidyUp();
    }

    public void testDVpixels() throws Throwable {
        log.debug(String.format("DV pixels test."));
        String name = "testDVpixels&pixelType=int16&sizeX=20&sizeY=20&sizeZ=5&sizeT=6&sizeC=1.fake";
        setUpTestFile(name);
        testgetTimepointDirect();
        testgetStackDirect();
        testgetPlaneDirect();
        testgetPlaneAsHypercube();
        testgetRowDirect();
        testgetColDirect();
        tidyUp();
    }

    public void testJPG() throws Throwable {
        log.debug(String.format("JPG test."));
        String name = "testJPG&pixelType=uint8&sizeX=256&sizeY=256&sizeZ=1&sizeT=1&sizeC=3.fake";
        setUpTestFile(name);
        testOtherGetters();
        testDimensionGetters();
        testSizeGetters();
        testOffsetGettersZero();
        testOffsetGetters();
        testCheckBounds();
        tidyUp();
    }

    public void testJPGpixels() throws Throwable {
        log.debug(String.format("JPG pixels test."));
        String name = "testJPGpixels&pixelType=uint8&sizeX=256&sizeY=256&sizeZ=1&sizeT=1&sizeC=3.fake";
        setUpTestFile(name);
        testgetTimepointDirect();
        testgetStackDirect();
        testgetPlaneDirect();
        testgetPlaneAsHypercube();
        testgetRowDirect();
        testgetColDirect();
        tidyUp();
    }

    public void testBMP() throws Throwable {
        log.debug(String.format("BMP test."));
        String name = "testBMP&pixelType=uint8&sizeX=256&sizeY=256&sizeZ=1&sizeT=1&sizeC=4.fake";
        setUpTestFile(name);
        testOtherGetters();
        testDimensionGetters();
        testSizeGetters();
        testOffsetGettersZero();
        testOffsetGetters();
        testCheckBounds();
        tidyUp();
    }

    public void testBMPpixels() throws Throwable {
        log.debug(String.format("BMP pixels test."));
        String name = "testBMPpixels&pixelType=uint8&sizeX=256&sizeY=256&sizeZ=1&sizeT=1&sizeC=4.fake";
        setUpTestFile(name);
        testgetTimepointDirect();
        testgetStackDirect();
        testgetPlaneDirect();
        testgetPlaneAsHypercube();
        testgetRowDirect();
        testgetColDirect();
        tidyUp();
    }

    private void testgetColDirect() throws IOException,
            DimensionsOutOfBoundsException, ServerError {
        int midX = bf.getSizeX() / 2;
        int midZ = bf.getSizeZ() / 2;
        int midC = bf.getSizeC() / 2;
        int midT = bf.getSizeT() / 2;
        byte[] buff1 = new byte[bf.getColSize()];
        byte[] buff2 = new byte[bf.getColSize()]; // rps has no getColSize
        bf.getColDirect(midX, midZ, midC, midT, buff1);
        buff2 = rps.getCol(midX, midZ, midC, midT);
        Assert.assertEquals(sha1(buff1), sha1(buff2));
    }

    private void testgetRowDirect() throws IOException,
            DimensionsOutOfBoundsException, ServerError {
        int midY = bf.getSizeY() / 2;
        int midZ = bf.getSizeZ() / 2;
        int midC = bf.getSizeC() / 2;
        int midT = bf.getSizeT() / 2;
        byte[] buff1 = new byte[bf.getRowSize()];
        byte[] buff2 = new byte[rps.getRowSize()];
        bf.getRowDirect(midY, midZ, midC, midT, buff1);
        buff2 = rps.getRow(midY, midZ, midC, midT);
        Assert.assertEquals(sha1(buff1), sha1(buff2));
    }

    private void testgetPlaneDirect() throws IOException,
            DimensionsOutOfBoundsException, ServerError {
        int midZ = bf.getSizeZ() / 2;
        int midC = bf.getSizeC() / 2;
        int midT = bf.getSizeT() / 2;
        byte[] buff1 = new byte[bf.getPlaneSize().intValue()];
        byte[] buff2 = new byte[RomioPixelBuffer.safeLongToInteger(rps
                .getPlaneSize())];
        bf.getPlaneDirect(midZ, midC, midT, buff1);
        buff2 = rps.getPlane(midZ, midC, midT);
        Assert.assertEquals(sha1(buff1), sha1(buff2));
    }

    private void testgetPlaneAsHypercube() throws IOException,
            DimensionsOutOfBoundsException, ServerError {
        int sizeX = bf.getSizeX();
        int sizeY = bf.getSizeY();
        int midZ = bf.getSizeZ() / 2;
        int midC = bf.getSizeC() / 2;
        int midT = bf.getSizeT() / 2;
        List<Integer> offset = Arrays.asList(new Integer[] { 0, 0, midZ, midC,
                midT });
        List<Integer> size = Arrays.asList(new Integer[] { sizeX, sizeY, 1, 1,
                1 });
        List<Integer> step = Arrays.asList(new Integer[] { 1, 1, 1, 1, 1 });
        byte[] buff1 = new byte[bf.getPlaneSize().intValue()];
        byte[] buff2 = new byte[bf.getPlaneSize().intValue()];
        bf.getPlaneDirect(midZ, midC, midT, buff1);
        bf.getHypercubeDirect(offset, size, step, buff2);
        Assert.assertEquals(sha1(buff1), sha1(buff2));
    }

    private void testgetStackDirect() throws IOException,
            DimensionsOutOfBoundsException, ServerError {
        int midC = bf.getSizeC() / 2;
        int midT = bf.getSizeT() / 2;
        byte[] buff1 = new byte[bf.getStackSize().intValue()];
        byte[] buff2 = new byte[RomioPixelBuffer.safeLongToInteger(rps
                .getStackSize())];
        bf.getStackDirect(midC, midT, buff1);
        buff2 = rps.getStack(midC, midT);
        Assert.assertEquals(sha1(buff1), sha1(buff2));
    }

    private void testgetTimepointDirect() throws IOException,
            DimensionsOutOfBoundsException, ServerError {
        int midT = bf.getSizeT() / 2;
        byte[] buff1 = new byte[bf.getTimepointSize().intValue()];
        byte[] buff2 = new byte[RomioPixelBuffer.safeLongToInteger(rps
                .getTimepointSize())];
        bf.getTimepointDirect(midT, buff1);
        buff2 = rps.getTimepoint(midT);
        Assert.assertEquals(sha1(buff1), sha1(buff2));
    }

    private void testOtherGetters() {
        Assert.assertEquals(bf.getPath(), destFileName);
    }

    private void testDimensionGetters() throws ServerError {
        Assert.assertEquals(rps.getRowSize() / rps.getByteWidth(), bf.getSizeX());
        Assert.assertEquals(rps.getPlaneSize() / rps.getRowSize(), bf.getSizeY());
        Assert.assertEquals(rps.getStackSize() / rps.getPlaneSize(), bf.getSizeZ());
        Assert.assertEquals(rps.getTimepointSize() / rps.getStackSize(), bf.getSizeC());
        Assert.assertEquals(rps.getTotalSize() / rps.getTimepointSize(), bf.getSizeT());
    }

    private void testSizeGetters() throws ServerError {
        Assert.assertEquals(rps.getRowSize(), bf.getRowSize().intValue());
        Assert.assertEquals(
                rps.getPlaneSize() * rps.getByteWidth() / rps.getRowSize(), bf
                        .getColSize().intValue());
        Assert.assertEquals(rps.getPlaneSize(), bf.getPlaneSize().longValue());
        Assert.assertEquals(rps.getStackSize(), bf.getStackSize().longValue());
        Assert.assertEquals(rps.getTimepointSize(), bf.getTimepointSize().longValue());
        Assert.assertEquals(rps.getTotalSize(), bf.getTotalSize().longValue());
    }

    private void testOffsetGettersZero() throws DimensionsOutOfBoundsException {
        Assert.assertEquals(0, bf.getRowOffset(0, 0, 0, 0).longValue());
        Assert.assertEquals(0, bf.getPlaneOffset(0, 0, 0).longValue());
        Assert.assertEquals(0, bf.getStackOffset(0, 0).longValue());
        Assert.assertEquals(0, bf.getTimepointOffset(0).longValue());
    }

    private void testOffsetGetters() throws DimensionsOutOfBoundsException,
            ServerError {
        int midY = bf.getSizeY() / 2;
        int midZ = bf.getSizeZ() / 2;
        int midC = bf.getSizeC() / 2;
        int midT = bf.getSizeT() / 2;
        Assert.assertEquals((long) rps.getRowOffset(midY, midZ, midC, midT),
                (long) bf.getRowOffset(midY, midZ, midC, midT));
        Assert.assertEquals((long) rps.getPlaneOffset(midZ, midC, midT),
                (long) bf.getPlaneOffset(midZ, midC, midT));
        Assert.assertEquals((long) rps.getStackOffset(midC, midT),
                (long) bf.getStackOffset(midC, midT));
        Assert.assertEquals((long) rps.getTimepointOffset(midT),
                (long) bf.getTimepointOffset(midT));
    }

    private void testCheckBounds() {
        try {
            bf.checkBounds(-1, 0, 0, 0, 0);
            Assert.fail("Failed to throw DimensionsOutOfBoundsException with dimension out of bounds.");
        } catch (DimensionsOutOfBoundsException e) {
        }
        try {
            bf.checkBounds(bf.getSizeX(), 0, 0, 0, 0);
            Assert.fail("Failed to throw DimensionsOutOfBoundsException with dimension out of bounds.");
        } catch (DimensionsOutOfBoundsException e) {
        }
        try {
            bf.checkBounds(0, -1, 0, 0, 0);
            Assert.fail("Failed to throw DimensionsOutOfBoundsException with dimension out of bounds.");
        } catch (DimensionsOutOfBoundsException e) {
        }
        try {
            bf.checkBounds(0, bf.getSizeY(), 0, 0, 0);
            Assert.fail("Failed to throw DimensionsOutOfBoundsException with dimension out of bounds.");
        } catch (DimensionsOutOfBoundsException e) {
        }
        try {
            bf.checkBounds(0, 0, -1, 0, 0);
            Assert.fail("Failed to throw DimensionsOutOfBoundsException with dimension out of bounds.");
        } catch (DimensionsOutOfBoundsException e) {
        }
        try {
            bf.checkBounds(0, 0, bf.getSizeZ(), 0, 0);
            Assert.fail("Failed to throw DimensionsOutOfBoundsException with dimension out of bounds.");
        } catch (DimensionsOutOfBoundsException e) {
        }
        try {
            bf.checkBounds(0, 0, 0, -1, 0);
            Assert.fail("Failed to throw DimensionsOutOfBoundsException with dimension out of bounds.");
        } catch (DimensionsOutOfBoundsException e) {
        }
        try {
            bf.checkBounds(0, 0, 0, bf.getSizeC(), 0);
            Assert.fail("Failed to throw DimensionsOutOfBoundsException with dimension out of bounds.");
        } catch (DimensionsOutOfBoundsException e) {
        }
        try {
            bf.checkBounds(0, 0, 0, 0, -1);
            Assert.fail("Failed to throw DimensionsOutOfBoundsException with dimension out of bounds.");
        } catch (DimensionsOutOfBoundsException e) {
        }
        try {
            bf.checkBounds(0, 0, 0, 0, bf.getSizeT());
            Assert.fail("Failed to throw DimensionsOutOfBoundsException with dimension out of bounds.");
        } catch (DimensionsOutOfBoundsException e) {
        }
    }
}
