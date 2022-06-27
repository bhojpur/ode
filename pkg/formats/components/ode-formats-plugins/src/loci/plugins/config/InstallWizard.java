package loci.plugins.config;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import loci.common.Constants;

/**
 * A wizard for walking users through installation of third party
 * libraries and plugins used by the Bhojpur ODE-Formats plugins.
 */
public class InstallWizard extends JFrame
{

  // -- Fields --

  // -- Constructor --

  public InstallWizard() {
    setTitle("ODE-Formats Plugins Library Installer");
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    /*
    1. get JAR file associated with a particular FQ Java class;
      if present, make sure JAR file name matches expected value.
        if so:
          a. get datestamp;
          b. query server for newer version
          if newer version:
            i. ask user if they want to upgrade
          else:
            i. tell user they have latest version
        if not:
          a. tell user they have a non-standard installation
             and we can't upgrade automatically
      if not present, ask user if they want to install the lib
    */

    // capabilities:

    // Check for ImageJ 1.43 or newer
    // download and install latest ij.jar
    // don't launch ImageJ updater plugin because it might not exist

    // check whether plugins are running from a ueber jar or as separate JARs

    // check for conflicting JARs -- i.e., duplicate classes

    // Win32: download and install QuickTime
    // Download http://www.apple.com/quicktime/download/
    // Find line with qtimewin, extract URL
    // Download the URL
    // Find line with QuickTimeInstaller.exe, extract URL
    // Download the URL
    // Execute program
    // Wait for process completion before continuing

    // Linux: download and install Image I/O Tools native codecs
    // Download http://download.java.net/media/jai-imageio/builds/release/1.1/jai_imageio-1_1-lib-linux-i586-jre.bin
    // Chmod 755 and execute?

    // Win32: download and install Image I/O Tools native codecs
    // Download http://download.java.net/media/jai-imageio/builds/release/1.1/jai_imageio-1_1-lib-windows-i586-jre.exe
    // Execute program
    // Wait for process completion before continuing

    // File: jre/bin/clib_jiio.dll
    // Find jre/bin folder by checking ImageJ.cfg file?
    // test with data/dicom/john/E724_S007_A0024.dcm

    // Win32: download and install Nikon ND2 plugin
    // (gray out option to use Nikon ND2 if plugin is not available)
    // Download http://rsb.info.nih.gov/ij/plugins/download/jars/ImageJND2ReaderPlugin.zip
    // Extract .msi file and execute it
    // Wait for process completion before continuing

    // Option to download Image5D
    // Download http://rsb.info.nih.gov/ij/plugins/download/jars/Image_5D.jar
    // Place in ImageJ plugins folder

    // Option to download View5D
    // Download http://wwwuser.gwdg.de/~rheintz/Nanoimaging/View5D/View5D_.jar
    // Place in ImageJ plugins folder

    // Check for upgrades to existing JAR libraries, and if available,
    // download and update them

    // Individual readers throw new MissingLibraryException
    // (extends FormatException) when required third party library is missing
    // This can allow importer plugin to prompt user to run LociInstaller to
    // install the missing library.
  }

  public static void checkLatest(String urlPath, String localPath)
    throws IOException
  {
    URL url = new URL(urlPath);
    URLConnection conn = url.openConnection();
    long latest = conn.getLastModified();
    File file = new File(localPath);
    long installed = file.lastModified();
    if (installed < latest) {
      // TODO: prompt user to upgrade
    }
  }

  public static String download(URLConnection conn) throws IOException {
    StringBuffer sb = new StringBuffer();
    try (InputStreamReader in =
          new InputStreamReader(conn.getInputStream(), Constants.ENCODING)) {
      char[] buf = new char[65536];

      while (true) {
        int r = in.read(buf);
        if (r <= 0) break;
        sb.append(buf, 0, r);
      }
    }
    return sb.toString();
  }

  public static void download(URLConnection conn, File dest)
    throws IOException
  {
    try (InputStream in = conn.getInputStream();
          FileOutputStream out = new FileOutputStream(dest)) {
      byte[] buf = new byte[65536];
      while (true) {
        int r = in.read(buf);
        if (r <= 0) break;
        out.write(buf, 0, r);
      }
    }
  }

}
