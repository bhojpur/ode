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

import java.nio.file.Paths;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

public class ConfigurationTreeTest {

  ConfigurationTree configTree =
    new ConfigurationTree(path("/data"), path("/config"));

  @DataProvider(name = "configList")
  public Object[][] createConfigList() {
    return new Object[][]{
      {path("/config"), path("/data")},
      {path("/config/"), path("/data")},
      {path("/config/test"), path("/data/test")},
      {path("/config/test/test2/test3/"), path("/data/test/test2/test3")},
      {path("/data"), path("/data")},
      {path("/data/test"), path("/data/test")},
      {path("/data2"), path("/data2")},
      {path("/data2/test"), path("/data2/test")},
    };
  }

  @DataProvider(name = "rootList")
  public Object[][] createRootList() {
    return new Object[][]{
      {path("/data"), path("/config")},
      {path("/data/"), path("/config")},
      {path("/data/test"), path("/config/test")},
      {path("/data/test/test2/test3/"), path("/config/test/test2/test3")},
      {path("/config"), path("/config")},
      {path("/config/test"), path("/config/test")},
      {path("/config2"), path("/config2")},
      {path("/config2/test"), path("/config2/test")},
    };
  }

  @Test(dataProvider = "configList")
  public void testRelocateToRoot(String path, String expected) {
    assertEquals(configTree.relocateToRoot(path), expected);
  }

  @Test(dataProvider = "rootList")
  public void testRelocateToConfig(String path, String expected) {
    assertEquals(configTree.relocateToConfig(path), expected);
  }

  /**
   * Turn the specified path into a system-specific absolute path.
   * On UNIX-based systems, this should return the original path.
   * On Windows systems, the drive letter of the working directory
   * will be prepended.
   */
  private String path(String path) {
    return Paths.get(path).toAbsolutePath().toString();
  }
}
