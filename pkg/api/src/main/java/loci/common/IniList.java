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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A data structure containing a parsed list of INI key/value tables.
 */
public class IniList extends ArrayList<IniTable> {

  /**
   * Gets the table names (headers) in the list.
   * @return a List containing the name of each {@link IniTable}
   */
  public List<String> getHeaders() {
    List<String> headers = new ArrayList<String>();
    for (IniTable table : this) {
      String header = table.get(IniTable.HEADER_KEY);
      headers.add(header);
    }
    return headers;
  }

  /**
   * Gets the table with the given name (header).
   *
   * @param tableName the name of the table to look up
   * @return the {@link IniTable} representing the named table,
   *         or null if no table with that name exists
   */
  public IniTable getTable(String tableName) {
    for (IniTable table : this) {
      String header = table.get(IniTable.HEADER_KEY);
      if (tableName.equals(header)) return table;
    }
    return null;
  }

  /**
   * Flattens all of the INI tables into a single HashMap whose keys are
   * of the format "[table name] table key".
   *
   * @return a HashMap containing all key/value pairs in every {@link IniTable}
   *         as described above
   */
  public HashMap<String, String> flattenIntoHashMap() {
    HashMap<String, String> h = new HashMap<String, String>();
    for (IniTable table : this) {
      String tableName = table.get(IniTable.HEADER_KEY);
      for (String key : table.keySet()) {
        if (!key.equals(IniTable.HEADER_KEY)) {
          h.put("[" + tableName + "] " + key, table.get(key));
        }
      }
    }
    return h;
  }

}