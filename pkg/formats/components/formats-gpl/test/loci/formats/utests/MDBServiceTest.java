/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.utests;

import static org.testng.AssertJUnit.assertEquals;

import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import loci.common.services.DependencyException;
import loci.common.services.ServiceFactory;
import loci.formats.services.MDBService;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MDBServiceTest {

  private static final String[] COLUMNS = new String[] {
    "ID", "text column", "number column", "date column", "currency column",
    "boolean column", "OLE column", "memo column"
  };

  private static final String[][] ROWS = new String[][] {
    {"1", "row 1, column 1", "1", "3/14/1999 0:0:0", "6.6600", "1", null, "foo"},
    {"2", "row 2, column 1", "2", "8/1/2008 0:0:0", "0.3700", "0", null, "bar"},
    {"3", "row 3, column 1", "3", "9/21/2001 0:0:0", "1000000.0000", "1", null, "baz"},
  };

  private static final String TEST_FILE = "test.mdb";

  private MDBService service;

  @BeforeMethod
  public void setUp() throws DependencyException, IOException {
    ServiceFactory sf = new ServiceFactory();
    service = sf.getInstance(MDBService.class);
    URL file = this.getClass().getResource(TEST_FILE);
    service.initialize(file.getPath());
  }

  @Test
  public void testData() throws IOException {
    Vector<Vector<String[]>> data = service.parseDatabase();

    assertEquals(1, data.size());

    Vector<String[]> table = data.get(0);
    assertEquals(4, table.size());

    String[] columnNames = table.get(0);
    assertEquals(COLUMNS.length + 1, columnNames.length);
    assertEquals("test table", columnNames[0]);

    for (int i=1; i<columnNames.length; i++) {
      assertEquals(columnNames[i], COLUMNS[i - 1]);
    }

    for (int i=1; i<table.size(); i++) {
      String[] row = table.get(i);
      for (int col=0; col<row.length; col++) {
        assertEquals(ROWS[i - 1][col], row[col]);
      }
    }
  }

}
