/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.tools;

import loci.formats.UpgradeChecker;

/**
 * Utility that checks for a new stable version, and optionally installs
 * a new version of ODE-Formats ueber tools.
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
