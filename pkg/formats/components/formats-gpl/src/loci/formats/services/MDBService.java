/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.services;

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
