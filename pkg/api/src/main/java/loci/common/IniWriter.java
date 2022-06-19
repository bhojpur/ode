package loci.common;

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

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple writer for INI configuration files.
 */
public class IniWriter {

  /** Logger for this class. */
  private static final Logger LOGGER = LoggerFactory.getLogger(IniWriter.class);

  // -- IniWriter API methods --

  /**
   * Saves the given IniList to the given file.
   * If the given file already exists, then the IniList will be appended.
   *
   * @param ini the {@link IniList} to be written
   * @param path the path to a writable file on disk
   * @throws IOException if there is an error during writing
   */
  public void saveINI(IniList ini, String path) throws IOException {
    saveINI(ini, path, true);
  }

  /**
   * Saves the given IniList to the given file.
   *
   * @param ini the {@link IniList} to be written
   * @param path the path to a writable file on disk
   * @param append true if the INI data should be appended to
   *               the end of the file if it already exists
   * @param sorted true if the INI keys should be sorted before writing
   * @throws IOException if there is an error during writing
   */
  public void saveINI(IniList ini, String path, boolean append, boolean sorted)
    throws IOException
  {
    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
      new FileOutputStream(path, append), Constants.ENCODING));

    for (IniTable table : ini) {

      String header = table.get(IniTable.HEADER_KEY);
      out.write("[" + header + "]\n");
      Set<String> keys;
      if (sorted) {
        Map<String, String> treeMap = new TreeMap<String, String>(table);
        keys = treeMap.keySet();
      }
      else {
        keys = table.keySet();
      }

      for (String key : keys) {
        out.write(key + " = " + table.get(key) + "\n");
      }
      out.write("\n");
    }

    out.close();
  }

  /**
   * Saves the given IniList to the given file.
   *
   * @param ini the {@link IniList} to be written
   * @param path the path to a writable file on disk
   * @param append true if the INI data should be appended to
   *               the end of the file if it already exists
   * @throws IOException if there is an error during writing
   */
  public void saveINI(IniList ini, String path, boolean append)
    throws IOException
  {
    saveINI(ini, path, append, false);
  }

}