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
import java.io.IOException;
import java.util.ArrayList;

import loci.common.DataTools;

/**
 * Recursively searchs a directory for files that have not been configured for
 * testing.
 */
public class UnconfiguredFiles {

  private ArrayList<String> unconfigured = new ArrayList<String>();

  public void buildUnconfiguredList(File root) throws IOException {
    if (!root.isDirectory()) return;
    String[] list = root.list();
    File configFile = new File(root, ".odeformats");
    String configData = null;
    if (configFile.exists()) {
      configData = DataTools.readFile(configFile.getAbsolutePath());
    }

    for (String file : list) {
      File child = new File(root, file).getAbsoluteFile();
      if (file.startsWith(".")) continue;
      else if (child.isDirectory()) buildUnconfiguredList(child);
      else if (!configFile.exists() ||
        configData.indexOf("\"" + child.getName() + "\"") < 0)
      {
        unconfigured.add(child.getAbsolutePath());
      }
    }
  }

  public void printList() {
    if (unconfigured.size() > 0) {
      System.out.println("Unconfigured files:");
      for (String file : unconfigured) {
        System.out.println("  " + file);
      }
    }
    else System.out.println("All files have been configured!");
  }

  public static void main(String[] args) throws IOException {
    UnconfiguredFiles f = new UnconfiguredFiles();
    f.buildUnconfiguredList(new File(args[0]));
    f.printList();
  }

}
