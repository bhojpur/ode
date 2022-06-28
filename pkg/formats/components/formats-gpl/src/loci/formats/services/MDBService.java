package loci.formats.services;

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
import java.util.Vector;

import loci.common.services.Service;

/**
 * Interface defining methods for parsing MDB database files.
 */
public interface MDBService extends Service {

  /**
   * Prepare the given .mdb file for reading.  This method must be called
   * before {@link #parseDatabase()}.
   *
   * @throws IOException if a problem occurs when opening the file
   */
  public void initialize(String filename) throws IOException;

  /**
   * Read all tables from a pre-initialized .mdb files.  Each Vector<String[]>
   * in the outer Vector represents a table; each String[] in a table represents
   * a row, and each element of the String[] represents the value of a
   * particular column within the row.
   *
   * The first row in each table contains the names for each column.
   * The first entry in the column name row is the name of the table.
   *
   * {@link #initialize(String)} must be called before calling parseDatabase().
   *
   * @throws IOException if there is a problem reading the table data
   */
  public Vector<Vector<String[]>> parseDatabase() throws IOException;

  /**
   * Read named table from a pre-initialized .mdb files.
   * Each String[] in a table represents a row, and each element of the String[]
   * represents the value of a particular column within the row.
   *
   * The first row in the table contains the names for each column.
   *
   * {@link #initialize(String)} must be called before calling parseTable.
   *
   * @param name table name
   * @return table data or null if the named table does not exist
   * @throws IOException if there is a problem reading the table data
   */
  public default Vector<String[]> parseTable(String name) throws IOException {
    return null;
  }

  /** Close the currently initialized file. */
  public void close();

}
