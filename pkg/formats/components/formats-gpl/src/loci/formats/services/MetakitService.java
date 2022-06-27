/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.services;

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
