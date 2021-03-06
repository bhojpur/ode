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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.tree.DefaultMutableTreeNode;

import loci.common.Constants;
import loci.common.IniList;
import loci.common.IniParser;
import loci.common.IniTable;
import loci.common.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stores configuration data about files in a directory structure.
 * This class is not designed to be thread safe, so should be synchronized
 * externally for proper thread safety.
 */
public class ConfigurationTree {

  // -- Constants --

  private static final Logger LOGGER =
    LoggerFactory.getLogger(ConfigurationTree.class);

  // -- Fields --

  /** Directory on the file system associated with the tree root. */
  private String rootDir;

  /** Base directory on the file system associated with the configuration
   * files.
   */
  private String configDir;

  /** Base directory on the file system associated with the memo files.
   */
  private String cacheDir;

  /**
   * Root of tree structure containing configuration data.
   * Each node's user object is a hashtable of key/value pairs.
   */
  private DefaultMutableTreeNode root;

  // -- Constructor --

  /**
   *  Constructs a new configuration tree rooted at the given directory in the
   *  file system.
   *
   *  @param rootDir a string specifying the root directory
   *
   */
  public ConfigurationTree(String rootDir) {
    this(rootDir, null);
  }

  /**
   *  Constructs a new configuration tree rooted at the given directory in the
   *  file system with a custom configuration directory.
   *
   *  @param rootDir a string specifying the root directory
   *  @param configDir a string specifying the base configuration directory
   *
   */
  public ConfigurationTree(String rootDir, String configDir) {
    this(rootDir, configDir, null);
  }

  /**
   *  Constructs a new configuration tree rooted at the given directory in the
   *  file system with a custom configuration directory anc cache directory.
   *
   *  @param rootDir a string specifying the root directory
   *  @param configDir a string specifying the base configuration directory
   *  @param cacheDir a string specifying the cache  directory
   */
  public ConfigurationTree(String rootDir, String configDir, String cacheDir) {
    if (rootDir == null) {
      throw new IllegalArgumentException("rootDir cannot be null.");
    }
    this.rootDir = new File(rootDir).getAbsolutePath();
    if (configDir != null) {
        this.configDir = new File(configDir).getAbsolutePath();
    }
    if (cacheDir != null) {
      this.cacheDir = new File(cacheDir).getAbsolutePath();
    }
    root = new DefaultMutableTreeNode();
  }

  // -- ConfigurationTree API methods --

  /**
   *  Returns the toot directory holding the data
   */
  public String getRootDirectory() {
    return this.rootDir;
  }

  /**
   *  Returns the base directory holding the configuration files
   */
  public String getConfigDirectory() {
    return this.configDir;
  }

  /**
   *  Returns the base directory holding the configuration files
   */
  public String getCacheDirectory() {
    return this.cacheDir;
  }

  /**
   *  Relocate a path from an base directory into a target directory
   */
  public String relocate(String path, String oldRoot, String newRoot) {
    if (!path.startsWith(oldRoot)) {
      return path;
    }
    String subPath = path.substring((int) Math.min(
      oldRoot.length() + 1, path.length()));
    if (subPath.length() == 0) {
      return newRoot;
    }
    else {
      return new Location(newRoot, subPath).getAbsolutePath();
    }
  }

  /**
   *  Relocate a path under the root directory to the configuration directory
   */
  public String relocateToConfig(String path) {
    return relocate(path, this.rootDir, this.configDir);
  }

  /**
   *  Relocate a path under the configuration directory to the root directory
   */
  public String relocateToRoot(String path) {
    return relocate(path, this.configDir, this.rootDir);
  }

  /** Retrieves the Configuration object corresponding to the given file. */
  public Configuration get(String id) throws IOException {
    DefaultMutableTreeNode pos = findNode(id, false, null);
    if (pos == null) return null;
    Hashtable table = (Hashtable) pos.getUserObject();
    return (Configuration) table.get("configuration");
  }

  public void parseConfigFile(String configFile) throws IOException {
    File file = new File(configFile);
    if (file.isDirectory()) {
      return;
    }
    String parent = file.getParent();
    if (configDir != null) {
      parent = relocateToRoot(parent);
    }

    configFile = file.getAbsolutePath();
    String dir = file.getParentFile().getAbsolutePath();

    IniParser parser = new IniParser();
    parser.setCommentDelimiter(null);
    FileInputStream stream = new FileInputStream(configFile);
    IniList iniList = parser.parseINI(new BufferedReader(
      new InputStreamReader(stream, Constants.ENCODING)));
    for (IniTable table : iniList) {
      String id = table.get(IniTable.HEADER_KEY);
      id = id.substring(0, id.lastIndexOf(" "));

      id = new File(parent, id).getAbsolutePath();

      DefaultMutableTreeNode node = findNode(id, true, configFile);
      if (node == null) {
        LOGGER.warn("config file '{}' has invalid filename '{}'",
          configFile, id);
        continue;
      }
    }
  }

  // -- Helper methods --

  /** Gets the tree node associated with the given file. */
  private DefaultMutableTreeNode findNode(String id, boolean create, String configFile) {
    String baseID = id;
    if (!id.startsWith(rootDir)) return null;
    id = id.substring(rootDir.length());
    StringTokenizer st = new StringTokenizer(id, "\\/");
    DefaultMutableTreeNode node = root;
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      Enumeration en = node.children();
      DefaultMutableTreeNode next = null;
      while (en.hasMoreElements()) {
        DefaultMutableTreeNode child =
          (DefaultMutableTreeNode) en.nextElement();
        Hashtable table = (Hashtable) child.getUserObject();
        if (token.equals(table.get("name"))) {
          next = child;
          break;
        }
      }
      if (next == null) {
        // create node, if applicable
        if (!create) return null;
        next = new DefaultMutableTreeNode();
        Hashtable table = new Hashtable();
        table.put("name", token);
        try {
          table.put("configuration", new Configuration(baseID, configFile));
        }
        catch (IOException e) { }
        next.setUserObject(table);
        node.add(next);
      }
      node = next;
    }
    return node;
  }

}
