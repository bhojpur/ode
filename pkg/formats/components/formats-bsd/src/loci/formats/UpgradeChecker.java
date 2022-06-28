package loci.formats;

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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that allows checking for new versions of ODE-Formats, as well as
 * updating to the latest stable, daily, or trunk version.
 */
public class UpgradeChecker {

  // -- Constants - Bhojpur ODE registry IDs --

  /** Registry ID identifying usage of ODE-Formats in ImageJ. */
  public static final String REGISTRY_IMAGEJ = "ODE.imagej";

  /** Registry ID identifying usage of ODE-Formats as a library in general. */
  public static final String REGISTRY_LIBRARY = "ODE.odeformats";

  // -- Constants --

  /** Version number of the latest stable release. */
  /**
   * @deprecated  As of release 6.6.0
   */
  public static final String STABLE_VERSION = "6.6.0";

  /** Location of the Bhojpur ODE continuous integration server. */
  public static final String CI_SERVER = "http://ci.bhojpur.net";

  /**
   * Location of the JAR artifacts for ODE-Formats' trunk build.
   */
  public static final String TRUNK_BUILD =
    CI_SERVER + "/job/ODEFORMATS-1.0-latest/lastSuccessfulBuild/artifact/artifacts/";

  /**
   * Location of the JAR artifacts for ODE-Formats' daily build.
   */
  public static final String DAILY_BUILD =
    CI_SERVER + "/job/ODEFORMATS-1.0-daily/lastSuccessfulBuild/artifact/artifacts/";

  /**
   * Location of the JAR artifacts for the stable releases.
   */
  public static final String STABLE_BUILD =
    "http://downloads.bhojpur.net/ode-formats/latest/artifacts/";

  /** Name of the ueber tools JAR. */
  public static final String TOOLS = "odeformats_package.jar";

  /**
   * Name of the previous versions' tools JAR.
   *
   * @deprecated Removed in ODE-Formats 6.9.0
   */
  public static final String OLD_TOOLS = "loci_tools.jar";

  /** Name of the Bhojpur ODE tools JAR. */
  public static final String ODE_TOOLS = "ode_tools.jar";

  /** Names of the individual JARs. */
  public static final String[] INDIVIDUAL_JARS = new String[] {
    "formats-api.jar", "formats-bsd.jar", "formats-gpl.jar",
    "jai_imageio.jar", "common.jar", "mdbtools-java.jar", "metakit.jar",
    "ode-xml.jar", "ode-poi.jar"
  };

  /** Location of the ODE registry. */
  public static final String REGISTRY = "http://upgrade.bhojpur.net";

  /** Value of "odeformats.caller" for ODE-Formats utilities. */
  public static final String DEFAULT_CALLER = "ODE-Formats utilities";

  /** Properties that are sent to the ODE registry. */
  private static final String[] REGISTRY_PROPERTIES = new String[] {
    "version", "os.name", "os.version", "os.arch", "java.runtime.version",
    "java.vm.vendor", "odeformats.caller"
  };

  /** System property to set once the upgrade check is performed. */
  private static final String UPGRADE_CHECK_PROPERTY =
    "odeformats_upgrade_check";

  /** System property indicating whether the upgrade check is ever allowed. */
  private static final String UPGRADE_CHECK_ALLOWED_PROPERTY =
    "odeformats_can_do_upgrade_check";

  /** Number of bytes to read from the CI server at a time. */
  private static final int CHUNK_SIZE = 8192;

  private static final Logger LOGGER =
    LoggerFactory.getLogger(UpgradeChecker.class);

  // -- UpgradeChecker API methods --

  /**
   * Return true if an upgrade check has already been performed in this
   * JVM session.
   */
  public boolean alreadyChecked() {
    String checked = System.getProperty(UPGRADE_CHECK_PROPERTY);
    if (checked == null) {
      return false;
    }
    return Boolean.parseBoolean(checked);
  }

  /**
   * Return whether or not we are ever allowed to perform an upgrade check.
   */
  public boolean canDoUpgradeCheck() {
    String checked = System.getProperty(UPGRADE_CHECK_ALLOWED_PROPERTY);
    if (checked == null) {
      return true;
    }
    return Boolean.parseBoolean(checked);
  }

  /**
   * Set whether or not we are ever allowed to perform an upgrade check.
   */
  public void setCanDoUpgradeCheck(boolean canDo) {
    System.setProperty(UPGRADE_CHECK_ALLOWED_PROPERTY, String.valueOf(canDo));
  }

  /**
   * Contact the Bhojpur ODE registry and return true if a new version is available.
   * ODE.registry will identify this as a generic library usage of
   * ODE-Formats (i.e. not associated with a specific client application).
   *
   * @param caller  name of the calling application, e.g. "MATLAB"
   */
  public boolean newVersionAvailable(String caller) {
    return newVersionAvailable(REGISTRY_LIBRARY, caller);
  }

  /**
   * Contact the Bhojpur ODE registry and return true if a new version is available.
   *
   * @param registryID how the application identifies itself to ODE.registry
   *                  @see #REGISTRY_IMAGEJ
   *                  @see #REGISTRY_LIBRARY
   * @param caller  name of the calling application, e.g. "MATLAB"
   */
  public boolean newVersionAvailable(String registryID, String caller) {
    if (!canDoUpgradeCheck()) {
      return false;
    }

    // build the registry query

    System.setProperty("odeformats.caller", caller);
    final StringBuilder query = new StringBuilder(REGISTRY);
    for (int i=0; i<REGISTRY_PROPERTIES.length; i++) {
      if (i == 0) {
        query.append("?");
      }
      else {
        query.append(";");
      }

      query.append(REGISTRY_PROPERTIES[i]);
      query.append("=");

      if (i == 0) {
        query.append(FormatTools.VERSION);
      }
      else {
        try {
          query.append(URLEncoder.encode(
            System.getProperty(REGISTRY_PROPERTIES[i]), "UTF-8"));
        }
        catch (UnsupportedEncodingException e) {
          LOGGER.warn("Failed to append query argument: " +
            REGISTRY_PROPERTIES[i], e);
        }
      }
    }

    System.setProperty(UPGRADE_CHECK_PROPERTY, "true");

    try {
      // connect to the registry

      URLConnection conn = new URL(query.toString()).openConnection();
      conn.setConnectTimeout(5000);
      conn.setUseCaches(false);
      conn.addRequestProperty("User-Agent", registryID);
      conn.connect();

      // retrieve the string from the registry
      InputStream in = conn.getInputStream();
      final StringBuilder sb = new StringBuilder();
      while (true) {
        int data = in.read();
        if (data == -1) {
          break;
        }
        sb.append((char) data);
      }
      in.close();

      // check if the string is not empty (upgrade available)

      String result = sb.toString();
      if (sb.length() == 0) {
        LOGGER.debug("No update needed");
        return false;
      } else {
        LOGGER.debug("UPGRADE AVAILABLE:" + result);
        return true;
      }
    }
    catch (UnknownHostException e) {
      LOGGER.warn("Failed to reach the update site");
    }
    catch (IOException e) {
      LOGGER.warn("Failed to compare version numbers", e);
    }
    return false;
  }

  /**
   * Download and install all of the individual JAR files into the given
   * directory.
   *
   * @param urlDir the location from which to download the JAR files
   * @param downloadDir the directory into which to save the JAR files
   * @return true if installation was successfull
   *
   * @see #install(String, String)
   */
  public boolean installIndividualJars(String urlDir, String downloadDir) {
    boolean overallSuccess = true;
    for (String jar : INDIVIDUAL_JARS) {
      boolean success = install(urlDir + File.separator + jar,
        downloadDir + File.separator + jar);
      if (overallSuccess) {
        overallSuccess = success;
      }
    }
    return overallSuccess;
  }

  /**
   * Download and install a JAR file from the given URL.
   *
   * @param urlPath the location from which to download the JAR
   * @param downloadPath the location in which to write the JAR;
   *                     if this location already exists, it will be overwritten
   * @return true if installation was successful
   */
  public boolean install(String urlPath, String downloadPath) {
    // if an old version exists, then remove it

    File jar = new File(downloadPath + ".tmp");
    if (jar.exists()) {
      LOGGER.debug("Removing {}", jar.getAbsolutePath());
      if (!jar.delete()) {
        LOGGER.warn("Failed to delete '{}'", jar.getAbsolutePath());
        return false;
      }
    }

    // download new version

    try {
      LOGGER.debug("Attempting to download {}", urlPath);
      URL url = new URL(urlPath);
      URLConnection urlConn = url.openConnection();
      int total = urlConn.getContentLength();
      byte[] buf = new byte[total];

      LOGGER.debug("File length: {} bytes", total);

      DataInputStream in = new DataInputStream(
        new BufferedInputStream(urlConn.getInputStream()));
      int off = 0;
      while (off < total) {
        int len = CHUNK_SIZE;
        if (off + len > total) {
          len = total - off;
        }
        int r = in.read(buf, off, len);
        if (r <= 0) {
          LOGGER.warn("Truncated JAR file");
          return false;
        }
        off += r;
      }

      in.close();

      // write the downloaded JAR to a file on disk
      LOGGER.debug("Writing downloaded bytes to {}", jar.getAbsolutePath());
      FileOutputStream out = new FileOutputStream(jar);
      out.write(buf);
      out.close();

      // remove the old bundle jar if the new bundle jar was downloaded

      File downloadFile = new File(downloadPath);
      File oldFile = new File(downloadFile.getParent(), OLD_TOOLS);
      if (oldFile.exists() && downloadFile.getName().equals(TOOLS)) {
        LOGGER.debug("Deleting {}", oldFile.getAbsolutePath());
        oldFile.delete();
      }

      LOGGER.debug("Renaming {} to {}", jar.getAbsolutePath(), downloadPath);
      boolean success = jar.renameTo(downloadFile);
      if (!success) {
        LOGGER.warn("Failed to rename '{}' to '{}'", jar.getAbsolutePath(),
          downloadPath);
      }
      return success;
    }
    catch (IOException e) {
      LOGGER.warn("Failed to download from " + urlPath, e);
    }
    return false;
  }

}
