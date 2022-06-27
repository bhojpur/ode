/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.services;

import java.io.IOException;

import loci.common.RandomAccessInputStream;

import ode.metakit.MetakitException;
import ode.metakit.MetakitReader;

/**
 * Implementation of MetakitService for interacting with the
 * ODE Metakit library.
 */
public class MetakitServiceImpl implements MetakitService {

  // -- Fields --

  private MetakitReader reader;

  // -- MetakitService API methods --

  /* @see loci.formats.services.MetakitException */
  @Override
  public void initialize(String file) throws IOException {
    try {
      reader = new MetakitReader(file);
    }
    catch (MetakitException e) { }
  }

  /* @see loci.formats.services.MetakitService */
  @Override
  public void initialize(RandomAccessInputStream file) {
    try {
      reader = new MetakitReader(file);
    }
    catch (MetakitException e) { }
  }

  /* @see loci.formats.services.MetakitService#getTableCount() */
  @Override
  public int getTableCount() {
    return reader.getTableCount();
  }

  /* @see loci.formats.services.MetakitService#getTableNames() */
  @Override
  public String[] getTableNames() {
    return reader.getTableNames();
  }

  /* @see loci.formats.services.MetakitService#getColumnNames(String) */
  @Override
  public String[] getColumnNames(String table) {
    return reader.getColumnNames(table);
  }

  /* @see loci.formats.services.MetakitService#getColumnNames(int) */
  @Override
  public String[] getColumNames(int table) {
    return reader.getColumnNames(table);
  }

  /* @see loci.formats.services.MetakitService#getTableData(String) */
  @Override
  public Object[][] getTableData(String table) {
    return reader.getTableData(table);
  }

  /* @see loci.formats.services.MetakitService#getTableData(int) */
  @Override
  public Object[][] getTableData(int table) {
    return reader.getTableData(table);
  }

  /* @see loci.formats.services.MetakitService#getRowData(int, String) */
  @Override
  public Object[] getRowData(int row, String table) {
    return reader.getRowData(row, table);
  }

  /* @see loci.formats.services.MetakitService#getRowData(int, int) */
  @Override
  public Object[] getRowData(int row, int table) {
    return reader.getRowData(row, table);
  }

  /* @see loci.formats.services.MetakitService#getRowCount(int) */
  @Override
  public int getRowCount(int table) {
    return reader.getRowCount(table);
  }

  /* @see loci.formats.services.MetakitService#getRowCount(String) */
  @Override
  public int getRowCount(String table) {
    return reader.getRowCount(table);
  }

  /* @see loci.formats.services.MetakitService#getColumnTypes(int) */
  @Override
  public Class[] getColumnTypes(int table) {
    return reader.getColumnTypes(table);
  }

  /* @see loci.formats.services.MetakitService#getColumnTypes(String) */
  @Override
  public Class[] getColumnTypes(String table) {
    return reader.getColumnTypes(table);
  }

  /* @see loci.formats.services.MetakitService#close() */
  @Override
  public void close() {
    reader.close();
  }

}
