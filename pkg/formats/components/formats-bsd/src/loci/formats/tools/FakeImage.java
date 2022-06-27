/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import loci.common.Location;
import loci.formats.ResourceNamer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generates fake image structures. Methods defensively check
 * caller-supplied arguments and throw relevant exceptions where needed.
 */
public class FakeImage {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(FakeImage.class);

  private Location directoryRoot;

  private ResourceNamer resourceNamer;

  public FakeImage(Location directoryRoot) {
    this.directoryRoot = directoryRoot;
  }

  public static void isValidRange(int arg, int min, int max) {
    if (arg < min || arg > max) {
      throw new IllegalArgumentException("Method argument value outside "
          + "valid range.");
    }
  }

  /**
   * Creates a fake SPW file/directory structure. Maximum supported size is a
   * 384-well plate with multiple runs and fields. All arguments indicating
   * plate or well element count must be at least <code>1</code> and cannot be
   * <code>null</code>. The structure appears on the file system as: <br/>
   *
   * <pre>
   * foo.fake/
   * └── Plate000/
   *     └── Run000/
   *         └── WellA000/
   *             └── Field000.fake
   *                 ...
   *             ...
   *             WellA254/
   *             WellB000/
   *             ...
   *             WellAA000/
   *             ...
   *         Run001/
   *         ...
   *     Plate001/
   *     ...
   * </pre>
   *
   * @param plates
   *          Number of plates in a screen (max 255).
   * @param plateAcquisitions
   *          Number of plate acquisitions (runs) in a plate (max 255).
   * @param rows
   *          Number of rows in a plate (max 255).
   * @param columns
   *          Number of columns in a plate (max 255).
   * @param fields
   *          Number of fields for a plate acquisition (max 255).
   * @throws IllegalArgumentException
   *           when any of the arguments fail validation.
   * @throws NullPointerException
   *           when null specified as argument value.
   * @return {@link Location} Instance representing the top-level directory
   *           of the SPW structure.
   */
  public Location generateScreen(int plates, int plateAcquisitions, int rows,
      int columns, int fields) {
    isValidRange(plates, 1, 255);
    isValidRange(plateAcquisitions, 1, 255);
    isValidRange(rows, 1, 255);
    isValidRange(columns, 1, 255);
    isValidRange(fields, 1, 255);

    List<Location> paths = new ArrayList<Location>();
    this.resourceNamer = new ResourceNamer(rows);

    long start = System.currentTimeMillis();
    for (int i = 0; i < plates; ++i) {
      Location plateLocation = resourceNamer.getLocationFromResourceName(
          directoryRoot, ResourceNamer.PLATE, i, File.separator);
      for (int j = 0; j < plateAcquisitions; ++j) {
        Location plateAcquisitionLocation = resourceNamer.
            getLocationFromResourceName(plateLocation, ResourceNamer.RUN, j,
                File.separator);
        resourceNamer.restartAlphabet();
        for (int k = 0; k < rows; ++k) {
          for (int l = 0; l < columns; ++l) {
            Location wellLocation = resourceNamer.getLocationFromResourceName(
                plateAcquisitionLocation, ResourceNamer.WELL +
                resourceNamer.getLetter(), l, File.separator);
            paths.add(wellLocation);
          }
          resourceNamer.nextLetter();
        }
      }
    }

    for (Location path : paths) {
      if (path.mkdirs()) {
        for (int i = 0; i < fields; ++i) {
          Location fieldLocation = resourceNamer.
              getLocationFromResourceName(path, ResourceNamer.FIELD, i,
                  ResourceNamer.FAKE_EXT);
          try {
            fieldLocation.createNewFile();
            LOGGER.debug("Created: " + fieldLocation.getCanonicalPath());
          } catch (IOException ioe) {
            throw new RuntimeException(ioe);
          }
        }
      }
    }

    long end = System.currentTimeMillis();
    LOGGER.debug(String.format("Fake SPW structure generation took %s ms.", end
        - start));

    return directoryRoot;
  }

}
