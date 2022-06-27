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

import static org.testng.AssertJUnit.*;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import loci.common.Constants;
import loci.formats.FormatException;
import loci.formats.IFormatReader;
import loci.formats.ImageReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checks that no file handles are open after closing a reader.
 * This will not work on Windows, as it depends upon the 'lsof' command.
 */
public class FileHandleTest {
  private static final Logger LOGGER =
    LoggerFactory.getLogger(FileHandleTest.class);

  private String id;

  private IFormatReader reader;

  @Parameters({"id"})
  @BeforeClass
  public void init(String id) {
    this.id = id;
  }

  @Test
  public void testHandleCount() throws FormatException, IOException {
    ArrayList<String> initialHandles = TestTools.getHandles();
    reader = new ImageReader();
    reader.setId(id);

    ArrayList<String> intermediateHandles = TestTools.getHandles();
    reader.close();
    ArrayList<String> finalHandles = TestTools.getHandles();

    int intermediateHandleCount = intermediateHandles.size();

    for (int i=0; i<initialHandles.size(); i++) {
      String s = initialHandles.get(i);
      initialHandles.remove(s);
      finalHandles.remove(s);
      intermediateHandles.remove(s);
      i--;
    }

    for (int i=0; i<finalHandles.size(); i++) {
      String s = finalHandles.get(i);
      if (s.endsWith("libnio.so") || s.endsWith("resources.jar") ||
        s.startsWith("/usr/lib") || s.startsWith("/opt/") ||
        s.startsWith("/usr/share/locale") || s.startsWith("/lib") ||
        s.indexOf("turbojpeg") > 0 || s.indexOf("/jre/") > 0 ||
        s.indexOf("nativedata") > 0 || s.indexOf("jhdf") > 0)
      {
        finalHandles.remove(s);
        i--;
      }
      else {
        LOGGER.warn(s);
      }
    }

    assertEquals(finalHandles.size(), initialHandles.size());
    assertTrue(intermediateHandles.size() >= initialHandles.size());
    assertTrue(intermediateHandleCount < 1024);
  }

}
