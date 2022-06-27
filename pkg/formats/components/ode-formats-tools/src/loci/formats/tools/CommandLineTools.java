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

import loci.common.DataTools;
import loci.formats.FormatTools;
import loci.formats.UpgradeChecker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An utility class for various methods used in command-line tools
 */
public final class CommandLineTools {

  // -- Constants --

  private static final Logger LOGGER = LoggerFactory.getLogger(CommandLineTools.class);
  public static final String VERSION = "-version";

  public static final String NO_UPGRADE_CHECK = "-no-upgrade";

  public static void printVersion() {
    System.out.println("Version: " + FormatTools.VERSION);
    System.out.println("Build date: " + FormatTools.DATE);
    System.out.println("VCS revision: " + FormatTools.VCS_REVISION);
  }

  public static void runUpgradeCheck(String[] args) {
    if (DataTools.indexOf(args, NO_UPGRADE_CHECK) != -1) {
      LOGGER.debug("Skipping upgrade check");
      return;
    }
    UpgradeChecker checker = new UpgradeChecker();
    boolean canUpgrade =
      checker.newVersionAvailable(UpgradeChecker.DEFAULT_CALLER);
    if (canUpgrade) {
      LOGGER.info("*** A new stable version is available. ***");
      LOGGER.info("*** Install the new version using:     ***");
      LOGGER.info("***   'upgradechecker -install'        ***");
    }
  }
}
