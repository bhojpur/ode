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

import loci.common.RandomAccessInputStream;
import loci.common.services.Service;

/**
 * Interface defining methods for interacting with the Bhojpur ODE Metakit library.
 */
public interface MetakitService extends Service {

  /* @see ode.metakit.MetakitReader */
  public void initialize(String file) throws IOException;

  /* @see ode.metakit.MetakitReader */
  public void initialize(RandomAccessInputStream file) throws IOException;

  /* @see ode.metakit.MetakitReader#getTableCount() */
  public int getTableCount();

  /* @see ode.metakit.MetakitReader#getTableNames() */
  public String[] getTableNames();

  /* @see ode.metakit.MetakitReader#getColumnNames(String) */
  public String[] getColumnNames(String table);

  /* @see ode.metakit.MetakitReader#getColumnNames(int) */
  public String[] getColumNames(int table);

  /* @see ode.metakit.MetakitReader#getTableData(String) */
  public Object[][] getTableData(String table);

  /* @see ode.metakit.MetakitReader#getTableData(int) */
  public Object[][] getTableData(int table);

  /* @see ode.metakit.MetakitReader#getRowData(int, String) */
  public Object[] getRowData(int row, String table);

  /* @see ode.metakit.MetakitReader#getRowData(int, int) */
  public Object[] getRowData(int row, int table);

  /* @see ode.metakit.MetakitReader#getRowCount(int) */
  public int getRowCount(int table);

  /* @see ode.metakit.MetakitReader#getRowCount(String) */
  public int getRowCount(String table);

  /* @see ode.metakit.MetakitReader#getColumnTypes(int) */
  public Class[] getColumnTypes(int table);

  /* @see ode.metakit.MetakitReader#getColumnTypes(String) */
  public Class[] getColumnTypes(String table);

  /* @see ode.metakit.MetakitReader#close() */
  public void close();

}
