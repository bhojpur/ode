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

import loci.formats.UpgradeChecker;

/**
 * Utility that checks for a new stable version, and optionally installs
 * a new version of Bhojpur ODE-Formats ueber tools.
 */
public class UpgradeCheck {

  public static void main(String[] args) {
    if (args.length > 0 && args[0].equals("-help")) {
      System.out.println("Usage:");
      System.out.println("  java UpgradeCheck [-install X]");
      System.out.println();
      System.out.println("    If no options are specified, an upgrade check");
      System.out.println("    will be performed but no new version will be");
      System.out.println("    downloaded.");
      System.out.println("    With the '-install' option, a version must be");
      System.out.println("    specified; valid values are 'STABLE', 'TRUNK',");
      System.out.println("    and 'DAILY'.  The corresponding build will be");
      System.out.println("    downloaded to the working directory.");
      System.exit(0);
    }

    UpgradeChecker checker = new UpgradeChecker();

    boolean doInstall = args.length > 0 && args[0].equals("-install");

    if (checker.newVersionAvailable(UpgradeChecker.DEFAULT_CALLER)) {
      System.out.println("A newer stable version is available.");
    }
    else {
      System.out.println("A newer stable version is not available.");
    }

    if (doInstall && args.length > 1) {
      String url = "";
      if (args[1].equals("TRUNK")) {
        url = UpgradeChecker.TRUNK_BUILD;
      }
      else if (args[1].equals("DAILY")) {
        url = UpgradeChecker.DAILY_BUILD;
      }
      else if (args[1].equals("STABLE")) {
        url = UpgradeChecker.STABLE_BUILD;
      }
      url += UpgradeChecker.TOOLS;
      checker.install(url, UpgradeChecker.TOOLS);
    }
  }

}
