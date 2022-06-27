package loci.plugins.util;

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

import ij.IJ;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Utility methods for verifying that classes
 * are present and versions are correct.
 */
public final class LibraryChecker {

  // -- Constants --

  /** List of possible libraries for which to check. */
  public enum Library {
    BIO_FORMATS,  // ODE-Formats
    ODE_JAVA_XML, // ODE-XML Java
    FORMS         // JGoodies Forms
  }

  /** Minimum version of ImageJ necessary for ODE-Formats plugins. */
  public static final String IMAGEJ_VERSION = "1.43";

  /** Message to be displayed if ImageJ is too old for ODE-Formats plugins. */
  public static final String IMAGEJ_MSG =
    "Sorry, the ODE-Formats plugins require ImageJ v" + IMAGEJ_VERSION + " or later.";

  /** URL for ODE-Formats web page. */
  public static final String URL_BF_SOFTWARE =
    "https://www.bhojpur.net/ode-formats";

  // -- Constructor --

  private LibraryChecker() { }

  // -- Utility methods --

  /** Checks whether the given class is available. */
  public static boolean checkClass(String className) {
    try { Class.forName(className); }
    catch (Throwable t) { return false; }
    return true;
  }

  /** Checks for a required library. */
  public static void checkLibrary(Library library, HashSet<String> missing) {
    switch (library) {
      case BIO_FORMATS:
        checkLibrary("org.slf4j.Logger", "slf4j-api-1.7.2.jar", missing);
        checkLibrary("loci.common.RandomAccessInputStream",
          "common.jar", missing);
        checkLibrary("loci.formats.FormatHandler", "ode-formats.jar", missing);
        checkLibrary("loci.poi.poifs.filesystem.POIFSDocument",
          "ode-poi.jar", missing);
        checkLibrary("mdbtools.libmdb.MdbFile", "mdbtools-java.jar", missing);
        break;
      case ODE_JAVA_XML:
        checkLibrary("ode.xml.model.ODEModelObject", "ode-xml.jar", missing);
        break;
      case FORMS:
        checkLibrary("com.jgoodies.forms.layout.FormLayout",
          "jgoodies-forms-1.7.2.jar", missing);
        break;
    }
  }

  /**
   * Checks whether the given class is available; if not,
   * adds the specified JAR file name to the hash set
   * (presumably to report it missing to the user).
   */
  public static void checkLibrary(String className,
    String jarFile, HashSet<String> missing)
  {
    if (!checkClass(className)) missing.add(jarFile);
  }

  /** Checks for a new enough version of the Java Runtime Environment. */
  public static boolean checkJava() {
    if (!IJ.isJava18()) {
      IJ.error("ODE-Formats Plugins",
        "Sorry, the Bhojpur ODE-Formats plugins require Java 1.8 or later.");
      return false;
    }
    return true;
  }

  /** Checks whether ImageJ is new enough for the ODE-Formats plugins. */
  public static boolean checkImageJ() {
    return checkImageJ(IMAGEJ_VERSION, IMAGEJ_MSG);
  }

  /**
   * Returns true the current ImageJ version is greater than or equal to the
   * specified version. Displays the given warning message if the current
   * version is too old.
   */
  public static boolean checkImageJ(String target, String msg) {
    return checkImageJ(target, msg, "ODE-Formats Plugins");
  }

  /**
   * Returns true the current ImageJ version is greater than or equal to the
   * specified version. Displays the given warning message with the specified
   * title if the current version is too old.
   */
  public static boolean checkImageJ(String target, String msg, String title) {
    boolean success;
    try {
      String current = IJ.getVersion();
      success = current != null && current.compareTo(target) >= 0;
    }
    catch (NoSuchMethodError err) {
      success = false;
    }
    if (!success) IJ.error(title, msg);
    return success;
  }

  /**
   * Reports missing libraries in the given hash set to the user.
   * @return true iff no libraries are missing (the hash set is empty).
   */
  public static boolean checkMissing(HashSet<String> missing) {
    int num = missing.size();
    if (num == 0) return true;
    StringBuffer sb = new StringBuffer();
    sb.append("The following librar");
    sb.append(num == 1 ? "y was" : "ies were");
    sb.append(" not found:");
    Iterator<String> iter = missing.iterator();
    for (int i=0; i<num; i++) sb.append("\n    " + iter.next());
    String them = num == 1 ? "it" : "them";
    sb.append("\nPlease download ");
    sb.append(them);
    sb.append(" from the ODE-Formats website at");
    sb.append("\n    " + URL_BF_SOFTWARE);
    sb.append("\nand place ");
    sb.append(them);
    sb.append(" in the ImageJ plugins folder.");
    IJ.error("ODE-Formats Plugins", sb.toString());
    return false;
  }

}
