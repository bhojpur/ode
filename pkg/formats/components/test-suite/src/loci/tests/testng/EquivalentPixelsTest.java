package loci.tests.testng;

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

import static org.testng.AssertJUnit.*;

import loci.formats.FormatTools;
import loci.formats.IFormatReader;
import loci.formats.ImageReader;
import loci.formats.ReaderWrapper;
import nl.javadude.assumeng.Assumption;
import nl.javadude.assumeng.AssumptionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Compare each pixel in two files to ensure that they are equivalent.
 */
@Listeners(value = AssumptionListener.class)
public class EquivalentPixelsTest
{
  private static final Logger LOGGER =
      LoggerFactory.getLogger(EquivalentPixelsTest.class);

  private String src;
  private String check;

  private IFormatReader srcReader;
  private IFormatReader checkReader;

  @Parameters({"src", "check"})
  @BeforeClass
  public void init(String src, String check) throws Exception {
    this.src = src;
    this.check = check;
  }

  @AfterClass
  public void tearDown() throws Exception {
    srcReader.close();
    checkReader.close();
  }

  @Test
  public void setId() throws Exception {
    srcReader = new ImageReader();
    checkReader = new ImageReader();

    srcReader.setId(src);
    checkReader.setId(check);

    assertEquals(srcReader.getSeriesCount(), checkReader.getSeriesCount());
    for (int s=0; s<srcReader.getSeriesCount(); s++) {
      srcReader.setSeries(s);
      checkReader.setSeries(s);

      assertEquals(srcReader.getSizeX(), checkReader.getSizeX());
      assertEquals(srcReader.getSizeY(), checkReader.getSizeY());
      assertEquals(srcReader.getSizeC(), checkReader.getSizeC());
      assertEquals(srcReader.getSizeZ(), checkReader.getSizeZ());
      assertEquals(srcReader.getSizeT(), checkReader.getSizeT());
      assertEquals(srcReader.getPixelType(), checkReader.getPixelType());
      assertEquals(srcReader.isRGB(), checkReader.isRGB());
      assertEquals(srcReader.getDimensionOrder(), checkReader.getDimensionOrder());
      // it's OK if the endian setting is different, so don't check that
    }
  }

  @Test(dependsOnMethods={"setId"})
  public void testEquivalent() throws Exception {
    for (int s=0; s<srcReader.getSeriesCount(); s++) {
      srcReader.setSeries(s);
      checkReader.setSeries(s);

      int optimalTileWidth = srcReader.getOptimalTileWidth();
      int optimalTileHeight = srcReader.getOptimalTileHeight();
      int bpp = FormatTools.getBytesPerPixel(srcReader.getPixelType());
      boolean flipEndian = bpp > 1 &&
        (srcReader.isLittleEndian() != checkReader.isLittleEndian());

      for (int image=0; image<srcReader.getImageCount(); image++) {
        LOGGER.info("Reading from series {} image {}", s, image);
        int tilesWide =
          (int) Math.ceil((double) srcReader.getSizeX() / optimalTileWidth);
        int tilesHigh =
          (int) Math.ceil((double) srcReader.getSizeY() / optimalTileHeight);
        int x, y = 0;
        for (int tileX = 0; tileX < tilesWide; tileX++) {
          for (int tileY = 0; tileY < tilesHigh; tileY++) {
            x = tileX * optimalTileWidth;
            y = tileY * optimalTileHeight;

            int actualTileWidth =
              (int) Math.min(optimalTileWidth, srcReader.getSizeX() - x);
            int actualTileHeight =
              (int) Math.min(optimalTileHeight, srcReader.getSizeY() - y);

            LOGGER.info("Reading tile at {}x{}", x, y);
            byte[] srcTile = srcReader.openBytes(image, x, y, actualTileWidth, actualTileHeight);
            byte[] checkTile = checkReader.openBytes(image, x, y, actualTileWidth, actualTileHeight);

            if (flipEndian) {
              for (int p=0; p<checkTile.length; p+=bpp) {
                for (int b=0; b<bpp/2; b++) {
                  byte tmp = checkTile[p + b];
                  checkTile[p + b] = checkTile[p + (bpp - b - 1)];
                  checkTile[p + (bpp - b - 1)] = tmp;
                }
              }
            }
            String srcMD5 = TestTools.md5(srcTile);
            String checkMD5 = TestTools.md5(checkTile);
            assertEquals(srcMD5, checkMD5);
          }
        }
      }
    }
  }

}
