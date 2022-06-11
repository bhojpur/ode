/*
 * Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

#ifndef ODE_TABLES_ICE
#define ODE_TABLES_ICE

#include <ode/ModelF.ice>
#include <ode/RTypes.ice>
#include <ode/System.ice>
#include <ode/Collections.ice>
#include <ode/Repositories.ice>
#include <ode/ServerErrors.ice>

/*
 * The Tables API is intended to provide a storage mechanism
 * for tabular data.
 */
module ode {

    /*
     * Forward declaration
     */
    module api {
        interface ServiceFactory;
    };

    module grid {

    //
    // User-consumable types dealing with
    // measurements/results (""tables"").
    // ========================================================================
    //

        /**
         * Base type for dealing working with tabular data. For efficiency,
         * data is grouped by type, i.e. column. These value objects are passed
         * through the {@code Table} interface.
         **/
        class Column {

            string name;
            string description;

        };

        class FileColumn extends Column {
            ode::api::LongArray values;
        };

        class ImageColumn extends Column {
            ode::api::LongArray values;
        };

        class DatasetColumn extends Column {
            ode::api::LongArray values;
        };

        class RoiColumn extends Column {
            ode::api::LongArray values;
        };

        class WellColumn extends Column {
            ode::api::LongArray values;
        };

        class PlateColumn extends Column {
            ode::api::LongArray values;
        };

        class BoolColumn extends Column {
            ode::api::BoolArray values;
        };

        class DoubleColumn extends Column {
            ode::api::DoubleArray values;
        };

        class LongColumn extends Column {
            ode::api::LongArray values;
        };

        class StringColumn extends Column {
            long size;
            ode::api::StringArray values;
        };

        class FloatArrayColumn extends Column {
            long size;
            ode::api::FloatArrayArray values;
        };

        class DoubleArrayColumn extends Column {
            long size;
            ode::api::DoubleArrayArray values;
        };

        class LongArrayColumn extends Column {
            long size;
            ode::api::LongArrayArray values;
        };

        //
        // Inline ROIs.
        //

        /**
         * Column requiring special handling.
         **/
        class MaskColumn extends Column {
            ode::api::LongArray imageId;
            ode::api::IntegerArray theZ;
            ode::api::IntegerArray theT;
            ode::api::DoubleArray x;
            ode::api::DoubleArray y;
            ode::api::DoubleArray w;
            ode::api::DoubleArray h;
            ode::api::ByteArrayArray bytes;
        };

        sequence<Column> ColumnArray;

        class Data {

            long lastModification;
            ode::api::LongArray rowNumbers;
            ColumnArray columns;

        };

        ["ami"] interface Table {


            //
            // Reading ======================================================
            //

            idempotent
            ode::model::OriginalFile
                getOriginalFile()
                throws ode::ServerError;

            /**
             * Returns empty columns.
             **/
            idempotent
            ColumnArray
                getHeaders()
                throws ode::ServerError;

            idempotent
            long
                getNumberOfRows()
                throws ode::ServerError;

            /**
             * http://www.pytables.org/docs/manual/apb.html
             *
             * Leave all three of start, stop, step to 0 to disable.
             *
             * TODO:Test effect of returning a billion rows matching getWhereList()
             *
             **/
            idempotent
            ode::api::LongArray
                getWhereList(string condition, ode::RTypeDict variables, long start, long stop, long step)
                throws ode::ServerError;

            /**
             * Read the given rows of data.
             *
             * @param rowNumbers must contain at least one element or an
             * {@link ode.ApiUsageException} will be thrown.
             **/
            idempotent
            Data
                readCoordinates(ode::api::LongArray rowNumbers)
                throws ode::ServerError;

            /**
             * http://www.pytables.org/docs/manual/ch04.html#Table.read
             **/
            idempotent
            Data
                read(ode::api::LongArray colNumbers, long start, long stop)
                throws ode::ServerError;

            /**
             * Simple slice method which will return only the given columns
             * and rows in the order supplied.
             *
             * If colNumbers or rowNumbers is empty (or None), then all values
             * will be returned.
             *
             * <h4>Python examples:</h4>
             * <pre>
             * data = table.slice(None, None)
             * assert len(data.rowNumbers) == table.getNumberOfRows()
             *
             * data = table.slice(None, \[3,2,1])
             * assert data.rowNumbers == \[3,2,1]
             * </pre>
             **/
            idempotent
            Data
                slice(ode::api::LongArray colNumbers, ode::api::LongArray rowNumbers)
                throws ode::ServerError;

            //
            // Writing ========================================================
            //

            void
                addData(ColumnArray cols)
                throws ode::ServerError;

            /**
             * Allows the user to modify a Data instance passed back
             * from a query method and have the values modified. It
             * is critical that the {@code Data.lastModification} and the
             * {@code Data.rowNumbers} fields are properly set. An exception
             * will be thrown if the data has since been modified.
             **/
            void update(Data modifiedData)
                throws ode::ServerError;

            //
            // Metadata =======================================================
            //

            idempotent
            ode::RTypeDict
                getAllMetadata()
                throws ode::ServerError;

            idempotent
            ode::RType
                getMetadata(string key)
                throws ode::ServerError;

            idempotent
            void
                setAllMetadata(ode::RTypeDict dict)
                throws ode::ServerError;

            idempotent
            void
                setMetadata(string key, ode::RType value)
                throws ode::ServerError;

            //
            // Life-cycle =====================================================
            //

            /**
             * Initializes the structure based on
             **/
            void
                initialize(ColumnArray cols)
                throws ode::ServerError;

            /**
             * Adds a column and returns the position index of the new column.
             **/
            int
                addColumn(Column col)
                throws ode::ServerError;

            /**
             **/
            void
                delete()
                throws ode::ServerError;

            /**
             **/
            void
                close()
                throws ode::ServerError;

        };


    //
    // Interfaces and types running the backend.
    // Used by ODE.server to manage the public
    // ode.api types.
    // ========================================================================
    //

        ["ami"] interface Tables {

            /**
             * Returns the Repository which this Tables service is watching.
             **/
            idempotent
             ode::grid::Repository*
                getRepository()
                throws ode::ServerError;

            /**
             * Returns the Table service for the given ""ODE.tables"" file.
             * This service will open the file locally to access the data.
             * After any modification, the file will be saved locally and
             * the server asked to update the database record. This is done
             * via services in the {@link ode.api.ServiceFactory}.
             */
            idempotent
            Table*
                getTable(ode::model::OriginalFile file, ode::api::ServiceFactory* sf)
                throws ode::ServerError;

        };

    };

};

#endif