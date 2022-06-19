package ode.services.server.measurements;

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

import java.util.Map;

import ode.model.IObject;
import ode.ServerError;
import ode.ValidationException;
import ode.grid.Column;

/**
 * Wrapper around an array of columns for facilitating instantiation and
 * mutation.
 */
class SmartColumns {

    Map<String, IObject> lsidMap;
    String[] idTypes;
    Column[] cols;
    Helper[] helpers;

    SmartColumns(Map<String, IObject> lsidMap, String[] headers,
            String[] idTypes, Class[] types) throws ServerError {

        if (types == null || headers == null) {
            throw new ode.ApiUsageException(null, null,
                    "Required parameter is null");
        }

        if ((types.length + idTypes.length) != headers.length) {
            throw new ode.ApiUsageException(null, null, String.format(
                    "types.length (%s) != headers.length (%s)", types.length,
                    headers.length));
        }

        this.lsidMap = lsidMap;
        this.idTypes = idTypes;
        cols = new Column[headers.length];
        helpers = new Helper[headers.length];

        for (int i = 0; i < idTypes.length; i++) {
            if (idTypes[i].contains("Roi")) {
                helpers[i] = Helper.fromLsid(idTypes[i]);
                cols[i] = helpers[i].newInstance();
            } else {
                throw new ValidationException(null, null, "Unknown lsid type: "
                        + idTypes[i]);
            }
            cols[i].name = headers[i];
        }

        for (int i = 0; i < types.length; i++) {
            int j = i + idTypes.length;
            helpers[j] = Helper.fromClass(types[i]);
            cols[j] = helpers[j].newInstance();
            cols[j].name = headers[j];
        }

    }

    public void fill(Object[][] data) {

        //
        // Now that we have ids for all of the objects, we can parse the
        // data[][] into columns and pass to the table instance.
        //

        // Initialize the columns
        for (int colIdx = 0; colIdx < cols.length; colIdx++) {
            Helper helper = helpers[colIdx];
            Column col = cols[colIdx];
            helper.setSize(col, data.length);
        }

        for (int rowIdx = 0; rowIdx < data.length; rowIdx++) {
            Object[] row = data[rowIdx];
            // LSIDS
            for (int colIdx = 0; colIdx < idTypes.length; colIdx++) {
                Column col = cols[colIdx];
                Helper helper = helpers[colIdx];
                String lsid = (String) data[rowIdx][colIdx];
                IObject obj = lsidMap.get(lsid);
                Object value = null;
                if (obj != null) {
                    value = obj.getId();
                }
                helper.setValue(col, rowIdx, value);
            }

            // VALUES
            for (int colIdx = idTypes.length; colIdx < cols.length; colIdx++) {
                Column col = cols[colIdx];
                Helper helper = helpers[colIdx];
                helper.setValue(col, rowIdx, data[rowIdx][colIdx]);
            }
        }

    }

    public Column[] asArray() {
        return cols;
    }
}