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
import java.util.List;
import java.util.Vector;

import loci.common.Location;
import loci.common.services.AbstractService;
import mdbtools.jdbc2.File;
import mdbtools.libmdb.Catalog;
import mdbtools.libmdb.Constants;
import mdbtools.libmdb.Data;
import mdbtools.libmdb.Holder;
import mdbtools.libmdb.MdbCatalogEntry;
import mdbtools.libmdb.MdbColumn;
import mdbtools.libmdb.MdbHandle;
import mdbtools.libmdb.MdbTableDef;
import mdbtools.libmdb.Table;
import mdbtools.libmdb.file;
import mdbtools.libmdb.mem;

/**
 * Implementation of {@link MDBService} for parsing MDB database files.
 */
public class MDBServiceImpl extends AbstractService implements MDBService {

  // -- Fields --

  private MdbHandle mdb;
  private Vector<Holder> boundValues;

  // -- MDBService API methods --

  /**
   * Default constructor.
   */
  public MDBServiceImpl() {
    // One check from each package
    checkClassDependency(mdbtools.jdbc2.File.class);
    checkClassDependency(mdbtools.libmdb.Catalog.class);
  }

  /* @see MDBService#initialize(String) */
  @Override
  public void initialize(String filename) throws IOException {
    boundValues = new Vector<Holder>();
    mem.mdb_init();
    File dbfile = new File(Location.getMappedId(filename));
    mdb = file.mdb_open(dbfile);

    Catalog.mdb_read_catalog(mdb, Constants.MDB_TABLE);
  }

  /* @see MDBService#parseDatabase() */
  @Override
  public Vector<Vector<String[]>> parseDatabase() throws IOException {
    List catalog = mdb.catalog;

    Vector<Vector<String[]>> rtn = new Vector<Vector<String[]>>();

    int previousColumnCount = 0;

    for (Object entry : catalog) {
      int type = ((MdbCatalogEntry) entry).object_type;
      String name = ((MdbCatalogEntry) entry).object_name;

      if (type == Constants.MDB_TABLE && !name.startsWith("MSys")) {
        Vector<String[]> tableData = new Vector<String[]>();
        MdbTableDef table = Table.mdb_read_table((MdbCatalogEntry) entry);
        Table.mdb_read_columns(table);

        int numCols = table.num_cols;
        for (int i=0; i<numCols; i++) {
          Holder h = new Holder();
          Data.mdb_bind_column(table, i + 1, h);
          boundValues.add(h);
        }

        String[] columnNames = new String[numCols + 1];
        columnNames[0] = name;
        for (int i=0; i<numCols; i++) {
          columnNames[i + 1] = ((MdbColumn) table.columns.get(i)).name;
        }
        tableData.add(columnNames);

        while (fetchRow(table)) {
          String[] row = new String[numCols];
          for (int i=0; i<numCols; i++) {
            Holder h = boundValues.get(i + previousColumnCount);
            row[i] = h.s;
          }
          tableData.add(row);
        }

        previousColumnCount += numCols;
        rtn.add(tableData);
      }
    }
    return rtn;
  }

  @Override
  public Vector<String[]> parseTable(String name) throws IOException {
    List catalog = mdb.catalog;
    Vector<Holder> binder = new Vector<Holder>();

    for (Object entry : catalog) {
      int type = ((MdbCatalogEntry) entry).object_type;
      String tableName = ((MdbCatalogEntry) entry).object_name;

      if (type == Constants.MDB_TABLE && tableName.equals(name)) {
        Vector<String[]> rtn = new Vector<String[]>();

        MdbTableDef table = Table.mdb_read_table((MdbCatalogEntry) entry);
        Table.mdb_read_columns(table);

        int numCols = table.num_cols;
        for (int i=0; i<numCols; i++) {
          Holder h = new Holder();
          Data.mdb_bind_column(table, i + 1, h);
          binder.add(h);
        }

        String[] columnNames = new String[numCols];
        for (int i=0; i<numCols; i++) {
          columnNames[i] = ((MdbColumn) table.columns.get(i)).name;
        }
        rtn.add(columnNames);

        while (fetchRow(table)) {
          String[] row = new String[numCols];
          for (int i=0; i<numCols; i++) {
            Holder h = binder.get(i);
            row[i] = h.s;
          }
          rtn.add(row);
        }

        return rtn;

      }
    }

    return null;
  }

  /* @see MDBService#close() */
  @Override
  public void close() {
    mdb.close();
    mdb = null;
    boundValues = null;
  }

  /**
   * Fetches the next row from the table, ignoring potential parsing exceptions.
   * @param table Table to fetch the next available row from.
   * @return <code>true</code> if there are further rows to fetch.
   * <code>false</code> if there are no further rows to fetch or an exception
   * is thrown while parsing the row.
   */
  private boolean fetchRow(MdbTableDef table) {
    try {
      return Data.mdb_fetch_row(table);
    } catch (Exception e) {
      return false;
    }
  }
}
