package integration;

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

import java.util.Date;
import java.util.UUID;

import ode.ApiUsageException;
import ode.ServerError;
import ode.cmd.Delete2;
import ode.gateway.util.Requests;
import ode.grid.Column;
import ode.grid.Data;
import ode.grid.LongColumn;
import ode.grid.StringColumn;
import ode.grid.TablePrx;
import ode.model.OriginalFile;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Collections of tests for the Table handling.
 */
public class TableTest extends AbstractServerTest {

    /** The default size of a buffer. */
    protected int DEFAULT_BUFFER_SIZE = 3;

    /** Identifies the ID column. */
    protected int UID_COLUMN = 0;

    /** Identifies the Long column. */
    protected int LONG_COLUMN = 1;

    /** Identifies the String column. */
    protected int STRING_COLUMN = 2;

    protected long[] ColNumbers = { UID_COLUMN, LONG_COLUMN, STRING_COLUMN };

    /** Reference to the columns. */
    protected Column[] myColumns;

    /** Reference to the table. */
    protected TablePrx myTable = null;

    // ~ Helpers
    // =========================================================================

    /**
     * Creates a number of empty rows of [rows] size for the table
     *
     * @param rows
     *            The number of rows.
     * @return See above.
     */
    private Column[] createColumns(int rows) {
        Column[] newColumns = new Column[3];
        newColumns[UID_COLUMN] = new LongColumn("Uid", "", new long[rows]);
        newColumns[LONG_COLUMN] = new LongColumn("MyLongColumn", "",
                new long[rows]);
        newColumns[STRING_COLUMN] = new StringColumn("MyStringColumn", "", 64,
                new String[rows]);
        return newColumns;
    }

    /**
     * Create/initialize a new myTable.
     *
     * @throws ServerError
     *             Thrown if an error occurred.
     */
    @BeforeMethod
    private String createTable() throws ServerError {
        myColumns = createColumns(1);

        String uniqueTableFile = "TableTest" + UUID.randomUUID().toString();

        // Create new unique table
        long id = factory.sharedResources().repositories().descriptions.get(0).getId().getValue();
        myTable = factory.sharedResources().newTable(id, uniqueTableFile);
        myTable.initialize(myColumns);

        return uniqueTableFile;
    }

    /**
     * Delete myTable.
     *
     * @throws ServerError
     *             Thrown if an error occurred.
     */
    @AfterMethod
    private void deleteTable() throws Exception {
        if (myTable != null) {
            OriginalFile f = myTable.getOriginalFile();
            final Delete2 dc = Requests.delete().target(f).build();
            callback(true, client, dc);
            myTable.close();
            myTable = null;
        }
    }

    /**
     * Retrieve table's OriginalFile.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testGetOriginalFile() throws Exception {
        myTable.getOriginalFile();
    }

    /**
     * Retrieve table header.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testGetHeaders() throws Exception {
        myTable.getHeaders();
    }

    /**
     * Add two rows of data to the table.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testAddData() throws Exception {
        Column[] newRow = createColumns(2);

        LongColumn uids = (LongColumn) newRow[UID_COLUMN];
        LongColumn myLongs = (LongColumn) newRow[LONG_COLUMN];
        StringColumn myStrings = (StringColumn) newRow[STRING_COLUMN];

        uids.values[0] = 0;
        myLongs.values[0] = 0;
        myStrings.values[0] = "zero";
        uids.values[1] = 1;
        myLongs.values[1] = 1;
        myStrings.values[1] = "one";

        myTable.addData(newRow);
    }

    /**
     * Retrieves the number of rows.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testGetNumberOfRows() throws Exception {
        Assert.assertEquals(myTable.getNumberOfRows(), 0);

        Column[] newRow = createColumns(1);

        LongColumn uids = (LongColumn) newRow[UID_COLUMN];
        LongColumn myLongs = (LongColumn) newRow[LONG_COLUMN];
        StringColumn myStrings = (StringColumn) newRow[STRING_COLUMN];

        uids.values[0] = 0;
        myLongs.values[0] = 0;
        myStrings.values[0] = "none";

        myTable.addData(newRow);

        Assert.assertEquals(myTable.getNumberOfRows(), 1);
    }

    /**
     * Tests the <code>WhereList</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testGetWhereListEmptyTable() throws Exception {
        long[] ids = myTable.getWhereList("(Uid==" + 0 + ")", null, 0,
                myTable.getNumberOfRows(), 1);

        Assert.assertEquals(ids.length, 0);
    }

    /**
     * Tests the <code>WhereList</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testGetWhereListManyRows() throws Exception {
        Column[] newRow = createColumns(3);

        LongColumn uids = (LongColumn) newRow[UID_COLUMN];
        LongColumn myLongs = (LongColumn) newRow[LONG_COLUMN];
        StringColumn myStrings = (StringColumn) newRow[STRING_COLUMN];

        uids.values[0] = 0;
        myLongs.values[0] = 0;
        myStrings.values[0] = "zero";
        uids.values[1] = 1;
        myLongs.values[1] = 1;
        myStrings.values[1] = "one";
        uids.values[2] = 2;
        myLongs.values[2] = 2;
        myStrings.values[2] = "two";

        myTable.addData(newRow);

        long[] ids = myTable.getWhereList("(Uid==" + 1 + ")", null, 0,
                myTable.getNumberOfRows(), 1);

        // getWhereList should have returned one row
        Assert.assertEquals(ids.length, 1);

        // Retrieve data again
        Data myData = myTable.read(ColNumbers, 0L,
                myTable.getNumberOfRows());

        myStrings = (StringColumn) myData.columns[STRING_COLUMN];
        myLongs = (LongColumn) myData.columns[LONG_COLUMN];

        // Row's time string and value should be the same
        Assert.assertEquals((myLongs.values[(int) ids[0]]), 1);
        Assert.assertEquals(myStrings.values[(int) ids[0]], "one");
    }

    /**
     * Tests <code>readCoordinates()</code> with zero rows in table. This throws
     * an exception because there's no need to try to read zero data.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(expectedExceptions = ApiUsageException.class)
    public void testReadCoordinates0Rows() throws Exception {
        myTable.readCoordinates(null);
    }

    /**
     * Tests <code>readCoordinates()</code> with one row in table.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testReadCoordinates1Rows() throws Exception {
        Column[] newRow = createColumns(1);

        LongColumn uids = (LongColumn) newRow[UID_COLUMN];
        LongColumn myLongs = (LongColumn) newRow[LONG_COLUMN];
        StringColumn myStrings = (StringColumn) newRow[STRING_COLUMN];

        uids.values[0] = 0;
        myLongs.values[0] = 0;
        myStrings.values[0] = "zero";

        myTable.addData(newRow);
        myTable.readCoordinates(new long[] { 0L });
    }

    /**
     * Tests <code>readCoordinates()</code> with two row in table
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testReadCoordinates2Rows() throws Exception {
        Column[] newRow = createColumns(2);

        LongColumn uids = (LongColumn) newRow[UID_COLUMN];
        LongColumn myLongs = (LongColumn) newRow[LONG_COLUMN];
        StringColumn myStrings = (StringColumn) newRow[STRING_COLUMN];

        uids.values[0] = 0;
        myLongs.values[0] = 0;
        myStrings.values[0] = "zero";
        uids.values[1] = 1;
        myLongs.values[1] = 1;
        myStrings.values[1] = "one";

        myTable.addData(newRow);
        myTable.readCoordinates(new long[] { 0L, 1L });
    }

    /**
     * Tests <code>read()</code> with no rows in table.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testRead0Rows() throws Exception {
        myTable.read(ColNumbers, 0L, myTable.getNumberOfRows());
    }

    /**
     * Tests <code>read</code> method with one row in table.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testRead1Rows() throws Exception {
        Column[] newRow = createColumns(1);

        LongColumn uids = (LongColumn) newRow[UID_COLUMN];
        LongColumn myLongs = (LongColumn) newRow[LONG_COLUMN];
        StringColumn myStrings = (StringColumn) newRow[STRING_COLUMN];

        uids.values[0] = 0;
        myLongs.values[0] = 0;
        myStrings.values[0] = "none";

        myTable.addData(newRow);
        myTable.read(ColNumbers, 0L, myTable.getNumberOfRows());
    }

    /**
     * Test <code>read</code> method with two rows in table
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testRead2Rows() throws Exception {
        Column[] newRow = createColumns(2);

        LongColumn uids = (LongColumn) newRow[UID_COLUMN];
        LongColumn myLongs = (LongColumn) newRow[LONG_COLUMN];
        StringColumn myStrings = (StringColumn) newRow[STRING_COLUMN];

        uids.values[0] = 0;
        myLongs.values[0] = 0;
        myStrings.values[0] = "zero";
        uids.values[1] = 1;
        myLongs.values[1] = 1;
        myStrings.values[1] = "one";

        myTable.addData(newRow);
        myTable.read(ColNumbers, 0L, myTable.getNumberOfRows());
    }

    /**
     * Tests <code>slice</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(expectedExceptions = ApiUsageException.class)
    public void testSlice0Rows() throws Exception {
        myTable.slice(null, null);
    }

    /**
     * Tests <code>slice</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testSlice1Rows() throws Exception {
        Column[] newRow = createColumns(1);

        LongColumn uids = (LongColumn) newRow[UID_COLUMN];
        LongColumn myLongs = (LongColumn) newRow[LONG_COLUMN];
        StringColumn myStrings = (StringColumn) newRow[STRING_COLUMN];

        uids.values[0] = 0;
        myLongs.values[0] = 0;
        myStrings.values[0] = "zero";

        myTable.addData(newRow);
        myTable.slice(ColNumbers, new long[] { 0L });
    }

    /**
     * Tests <code>slice</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testSlice2Rows() throws Exception {
        Column[] newRow = createColumns(2);

        LongColumn uids = (LongColumn) newRow[UID_COLUMN];
        LongColumn myLongs = (LongColumn) newRow[LONG_COLUMN];
        StringColumn myStrings = (StringColumn) newRow[STRING_COLUMN];

        uids.values[0] = 0;
        myLongs.values[0] = 0;
        myStrings.values[0] = "zero";
        uids.values[1] = 1;
        myLongs.values[1] = 1;
        myStrings.values[1] = "one";

        myTable.addData(newRow);

        myTable.slice(ColNumbers, new long[] { 0L, 1L });
    }

    /**
     * Add then update a table row, assert its validity
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testUpdateTableWith1Rows() throws Exception {
        // Add a new row to table
        Column[] newRow = createColumns(1);

        LongColumn uids = (LongColumn) newRow[UID_COLUMN];
        LongColumn myLongs = (LongColumn) newRow[LONG_COLUMN];
        StringColumn myStrings = (StringColumn) newRow[STRING_COLUMN];

        Long oldTime = new Date().getTime();

        uids.values[0] = 1;
        myLongs.values[0] = oldTime;
        myStrings.values[0] = oldTime.toString();

        myTable.addData(newRow);

        // Retrieve the table data
        Data myData = myTable.read(ColNumbers, 0L,
                myTable.getNumberOfRows());

        // Find the specific row we added
        long[] ids = myTable.getWhereList("(Uid==" + 1 + ")", null, 0,
                myTable.getNumberOfRows(), 1);

        // getWhereList should have returned one row
        Assert.assertEquals(ids.length, 1);

        // Update the row with new data
        Long newTime = new Date().getTime();

        ((LongColumn) myData.columns[LONG_COLUMN]).values[(int) ids[0]] = newTime;
        ((StringColumn) myData.columns[STRING_COLUMN]).values[(int) ids[0]] = newTime
                .toString();

        myTable.update(myData);

        // Retrieve data again
        myData = myTable.read(ColNumbers, 0L, myTable.getNumberOfRows());

        myStrings = (StringColumn) myData.columns[STRING_COLUMN];
        myLongs = (LongColumn) myData.columns[LONG_COLUMN];

        // Row's time string and value should be the same
        Assert.assertEquals(
                myStrings.values[(int) ids[0]], newTime.toString());
        Assert.assertEquals(myLongs.values[(int) ids[0]], newTime.longValue());
    }

    /**
     * Add then update a table row, assert its validity.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testUpdateTableWith2Rows() throws Exception {
     // Add a new row to table
        Column[] newRow = createColumns(2);

        LongColumn uids = (LongColumn) newRow[UID_COLUMN];
        LongColumn myLongs = (LongColumn) newRow[LONG_COLUMN];
        StringColumn myStrings = (StringColumn) newRow[STRING_COLUMN];

        Long oldTime = new Date().getTime();

        uids.values[0] = 1;
        myLongs.values[0] = oldTime;
        myStrings.values[0] = oldTime.toString();

        uids.values[1] = 2;
        myLongs.values[1] = oldTime;
        myStrings.values[1] = oldTime.toString();

        myTable.addData(newRow);

        // Retrieve the table data
        Data myData = myTable.read(ColNumbers, 0L,
                myTable.getNumberOfRows());

        // Find the specific row we added
        long[] ids = myTable.getWhereList("(Uid==" + 1 + ")", null, 0,
                myTable.getNumberOfRows(), 1);

        // getWhereList should have returned one row
        Assert.assertEquals(ids.length, 1);

        // Update the row with new data
        Long newTime = new Date().getTime();

        ((LongColumn) myData.columns[LONG_COLUMN]).values[(int) ids[0]] = newTime;
        ((StringColumn) myData.columns[STRING_COLUMN]).values[(int) ids[0]] = newTime
                .toString();

        myTable.update(myData);

        // Retrieve data again
        myData = myTable.read(ColNumbers, 0L, myTable.getNumberOfRows());

        myStrings = (StringColumn) myData.columns[STRING_COLUMN];
        myLongs = (LongColumn) myData.columns[LONG_COLUMN];

        // Row's time string and value should be the same
        Assert.assertEquals(
                myStrings.values[(int) ids[0]], newTime.toString());
        Assert.assertEquals(myLongs.values[(int) ids[0]], newTime.longValue());
    }

}
