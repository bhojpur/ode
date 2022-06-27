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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Factory;

/**
 * Factory for scanning a directory structure and generating instances of
 * {@link FormatWriterTest} based on the image files found.
 */
public class FormatWriterTestFactory {

  // -- Constants --

  private static final Logger LOGGER =
    LoggerFactory.getLogger(FormatWriterTestFactory.class);

  // -- TestNG factory methods --

  @Factory
  public Object[] createInstances() {
    List files = new ArrayList();

    // parse explicit filename, if any
    final String nameProp = "testng.filename";
    String filename = System.getProperty(nameProp);
    if (filename != null && filename.equals("${" + nameProp + "}")) {
      filename = null;
    }
    if (filename != null && !new File(filename).exists()) {
      LOGGER.error("Invalid filename: {}", filename);
      return new Object[0];
    }

    String baseDir = null;
    if (filename == null) {
      // parse base directory
      final String baseDirProp = "testng.directory";
      baseDir = System.getProperty(baseDirProp);
      if (!new File(baseDir).isDirectory()) {
        if (baseDir == null || baseDir.equals("${" + baseDirProp + "}")) {
          LOGGER.error("No base directory specified.");
        }
        else LOGGER.error("Invalid base directory: {}", baseDir);
        LOGGER.error("Please specify a directory containing files to test:");
        LOGGER.error("   ant -D{}=\"/path/to/data\" test-all", baseDirProp);
        return new Object[0];
      }
      FormatWriterTest.configTree = new ConfigurationTree(baseDir);

      LOGGER.info("testng.directory = {}", baseDir);
    }

    // parse multiplier
    final String multProp = "testng.multiplier";
    String mult = System.getProperty(multProp);
    float multiplier = 1;
    if (mult != null && !mult.equals("${" + multProp + "}")) {
      try {
        multiplier = Float.parseFloat(mult);
      }
      catch (NumberFormatException exc) {
        LOGGER.warn("Invalid multiplier: {}", mult);
      }
    }
    LOGGER.info("testng.multiplier = {}", multiplier);

    final String toplevelConfigProp = "testng.toplevel-config";
    String toplevelConfig = System.getProperty(toplevelConfigProp);

    // detect maximum heap size
    long maxMemory = Runtime.getRuntime().maxMemory() >> 20;
    LOGGER.info("Maximum heap size = {} MB", maxMemory);

    // display local information
    LOGGER.info("user.language = {}", System.getProperty("user.language"));
    LOGGER.info("user.country = {}", System.getProperty("user.country"));

    if (filename == null) {
      // scan for files
      System.out.println("Scanning for files...");
      long start = System.currentTimeMillis();
      TestTools.getFiles(baseDir, files, FormatWriterTest.configTree,
        toplevelConfig);
      long end = System.currentTimeMillis();
      double time = (end - start) / 1000.0;
      LOGGER.info(TestTools.DIVIDER);
      LOGGER.info("Total files: {}", files.size());
      long avg = end - start;
      if (files.size() > 0) avg /= files.size();
      LOGGER.info("Scan time: {} s ({} ms/file)", time, avg);
      LOGGER.info(TestTools.DIVIDER);
    }
    else {
      files.add(filename);
    }

    // create test class instances
    System.out.println("Building list of tests...");
    Object[] tests = new Object[files.size()];
    for (int i=0; i<tests.length; i++) {
      String id = (String) files.get(i);
      tests[i] = new FormatWriterTest(id);
    }
    if (tests.length == 1) System.out.println("Ready to test " + files.get(0));
    else System.out.println("Ready to test " + tests.length + " files");

    return tests;
  }

}
