package loci.formats.tools;

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

import loci.common.DebugTools;
import loci.common.Location;
import loci.formats.FormatHandler;
import loci.formats.ResourceNamer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ImageFaker is a wrapper class for invoking methods in {@link FakeImage}.
 */
public class ImageFaker {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ImageFaker.class);

  private String targetDirectoryPath;

  private int plates = 1;

  private int plateAcquisitions = 1;

  private int rows = 1;

  private int columns = 1;

  private int fields = 1;

  public boolean parseArgs(String[] args) {
    if (args == null || args.length == 0) {
      return false;
    }
    for (int i = 0; i < args.length; i++) {
      if (args[i].charAt(0) == '-') {
        if (args[i].equals("-plates")) {
          plates = Integer.parseInt(args[++i]);
        } else if (args[i].equals("-runs")) {
          plateAcquisitions = Integer.parseInt(args[++i]);
        } else if (args[i].equals("-rows")) {
          rows = Integer.parseInt(args[++i]);
        } else if (args[i].equals("-columns")) {
          columns = Integer.parseInt(args[++i]);
        } else if (args[i].equals("-fields")) {
          fields = Integer.parseInt(args[++i]);
        } else if (args[i].equals("-debug")) {
          DebugTools.setRootLevel("DEBUG");
        }
      } else {
        if (targetDirectoryPath == null) {
          targetDirectoryPath = args[i];
        } else {
          LOGGER.error("Found unknown argument: {}; exiting.", args[i]);
          return false;
        }
      }
    }
    return true;
  }

  public void printUsage() {
    String[] s = { "To generate a fake file / dir structure, run:",
        "  mkfake path [-plates] [-runs] [-rows] [-columns] ",
        "    [-fields] [-debug]", "",
        "        path: the top-level directory for the SPW structure",
        "     -plates: number of plates (default: 1)",
        "       -runs: number of plate runs (acquisitions) (default: 1)",
        "       -rows: number of rows in a plate (default: 1)",
        "    -columns: number of columns in a plate (default: 1)",
        "     -fields: number of fields in a plate (default: 1)",
        "      -debug: turn on debugging output", "" };
    for (int i = 0; i < s.length; i++) {
      LOGGER.info(s[i]);
    }
  }

  public boolean fakeScreen(String[] args) {
    DebugTools.enableLogging("INFO");

    boolean validArgs = parseArgs(args);

    if (!validArgs || targetDirectoryPath == null) {
      printUsage();
      return false;
    }

    // make sure that we don't end up with just a ".fake" directory
    if (new Location(targetDirectoryPath).exists()) {
      Location p = new Location(targetDirectoryPath, "screen.fake");
      int index = 1;
      while (p.exists()) {
        p = new Location(targetDirectoryPath, "screen" + index + ".fake");
        index++;
      }

      targetDirectoryPath = p.getAbsolutePath();
    }

    Location directoryRoot;
    if (!FormatHandler.checkSuffix(targetDirectoryPath,
            ResourceNamer.FAKE_EXT)) {
      directoryRoot = new Location(targetDirectoryPath + ResourceNamer.DOT
        + ResourceNamer.FAKE_EXT);
    } else {
      directoryRoot = new Location(targetDirectoryPath);
    }

    FakeImage fake = new FakeImage(directoryRoot);
    fake.generateScreen(plates, plateAcquisitions, rows, columns, fields);

    return true;
  }

  public static void main(String[] args) throws Exception {
    if (!new ImageFaker().fakeScreen(args)) {
      System.exit(1);
    }
  }

}
