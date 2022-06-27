/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.utests;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import java.io.IOException;

import loci.common.Location;
import loci.formats.ChannelFiller;
import loci.formats.ChannelMerger;
import loci.formats.ChannelSeparator;
import loci.formats.CoreMetadata;
import loci.formats.DimensionSwapper;
import loci.formats.FileStitcher;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.IFormatReader;
import loci.formats.ImageReader;
import loci.formats.Memoizer;
import loci.formats.MinMaxCalculator;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 */
public class ReaderEqualityTest {

  private static final String TEST_FILE_A =
    "A&pixelType=uint8&sizeX=128&sizeY=64&sizeC=2&sizeZ=4&sizeT=5&series=3.fake";
  private static final String TEST_FILE_B =
    "B&pixelType=uint8&sizeX=128&sizeY=64&sizeC=2&sizeZ=4&sizeT=5&series=2.fake";

  @DataProvider(name = "equalWrappers")
  public Object[][] createEqualWrappers() {
    Location.mapId(TEST_FILE_A, TEST_FILE_A);
    Location.mapId(TEST_FILE_B, TEST_FILE_B);

    Object[][] wrappers = new Object[][] {
      {new ImageReader(), new ImageReader()},
      {new ImageReader(), new ImageReader()},
      {new MinMaxCalculator(new ChannelSeparator(new ChannelFiller())),
       new MinMaxCalculator(new ChannelSeparator(new ChannelFiller()))},
      {new DimensionSwapper(new ChannelMerger()),
       new DimensionSwapper(new ChannelMerger())}
    };
    for (int i=1; i<wrappers.length; i++) {
      IFormatReader readerA = (IFormatReader) wrappers[i][0];
      IFormatReader readerB = (IFormatReader) wrappers[i][1];
      try {
        readerA.setId(TEST_FILE_A);
        readerB.setId(TEST_FILE_B);
      }
      catch (FormatException e) { e.printStackTrace(); }
      catch (IOException e) { e.printStackTrace(); }
    }
    return wrappers;
  }

  @DataProvider(name = "unequalWrappers")
  public Object[][] createUnequalWrappers() {
    Object[][] wrappers = new Object[][] {
      {new ImageReader(), new ImageReader()},
      {new ImageReader(), new ImageReader()},
      {new MinMaxCalculator(new ChannelSeparator(new ChannelFiller())),
       new MinMaxCalculator(new ChannelSeparator())},
      {new DimensionSwapper(new ChannelMerger()),
       new DimensionSwapper(new ChannelMerger())}
    };

    for (int i=0; i<wrappers.length; i++) {
      IFormatReader readerA = (IFormatReader) wrappers[i][0];
      IFormatReader readerB = (IFormatReader) wrappers[i][1];

      try {
        if (i != 1) {
          readerA.setId(TEST_FILE_A);
        }
        else {
          readerA.setId(TEST_FILE_B);
        }
        if (i > 1) {
          readerB.setId(TEST_FILE_B);
        }
        else if (i == 1) {
          readerB.setId(TEST_FILE_A);
        }
      }
      catch (FormatException e) { e.printStackTrace(); }
      catch (IOException e) { e.printStackTrace(); }
    }
    return wrappers;
  }

  @Test(dataProvider = "equalWrappers")
  public void testEquality(IFormatReader[] readers) {
    if (readers.length == 2) {
      assertTrue(FormatTools.equalReaders(readers[0], readers[1]));
    }
  }

  @Test(dataProvider = "unequalWrappers")
  public void testInequality(IFormatReader[] readers) {
    if (readers.length == 2) {
      assertFalse(FormatTools.equalReaders(readers[0], readers[1]));
    }
  }
}
