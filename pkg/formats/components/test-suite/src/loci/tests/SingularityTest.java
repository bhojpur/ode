package loci.tests;

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

import java.io.IOException;

import loci.formats.FormatException;
import loci.formats.ImageReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class for testing the accuracy of
 * {@link loci.formats.IFormatReader#isSingleFile(String)}.
 */
public class SingularityTest {

  private static final Logger LOGGER =
    LoggerFactory.getLogger(SingularityTest.class);

  private static ImageReader reader = new ImageReader();

  public static void main(String[] args) throws FormatException, IOException {

    if (args.length < 1) {
      LOGGER.info("Usage: java.loci.tests.SingularityTest /path/to/input-file");
      System.exit(1);
    }

    LOGGER.info("Testing {}", args[0]);

    ImageReader reader = new ImageReader();
    boolean isSingleFile = reader.isSingleFile(args[0]);

    reader.setId(args[0]);
    String[] usedFiles = reader.getUsedFiles();

    if (isSingleFile && usedFiles.length > 1) {
      LOGGER.info("  Used files list contains more than one file, " +
        "but isSingleFile(String) returned true.");
      LOGGER.info("FAILURE");
    }
    else if (!isSingleFile && usedFiles.length == 1) {
      LOGGER.info("  Used files list only contains one file, " +
        "but isSingleFile(String) returned false.");
      LOGGER.info("FAILURE");
    }
    else LOGGER.info("SUCCESS");
  }

}
