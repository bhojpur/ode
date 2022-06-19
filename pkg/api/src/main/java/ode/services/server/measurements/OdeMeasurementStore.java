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

import java.util.List;
import java.util.Map;

import ode.model.IObject;
import ode.grid.TablePrx;

/**
 * Sub-interface of {@link MeasurementStore} with extended life-cycle methods
 * for handling exceptions during saving the measurements to multiple stores.
 */
public interface OdeMeasurementStore {

    // Original API: should be moved to loci.*

    /**
     * 
     */
    public abstract void initialize(String[] headers, String[] idTypes,
            Class[] types, Map<String, Object> metadata) throws Exception;

    /**
     * Adds an array of rows (Object[]) to the store. These rows might be
     * indexed by an LSID which correlates to an added ROI. For each call to
     * {@link #addRows(Object[][])} a call to {@link #save()} must be made. This
     * allows a single measurement store to be filled with the values from
     * multiple measurement files. To differentiate between the various files,
     * include a File lsid column during the initialization phase.
     * 
     * @param rows
     * @throws Exception
     */
    public abstract void addRows(Object[][] rows) throws Exception;

    public abstract void addCircle(String roiLsid, double x, double y, double r)
            throws Exception;

    public abstract void save() throws Exception;

    // Bhojpur ODE specific API

    public abstract void addObject(String lsid, IObject object);

    public abstract void addObjects(Map<String, IObject> objects);

    /**
     * Returns the ids of all Roi instances created during the save method. If
     * Roi creation failed or if {@link #save()} has not been called, this will
     * return null.
     */
    public abstract List<Long> getRoiIds();

    /**
     * Returns the Table proxy which is in use by this service.
     * @return See above.
     */
    public abstract TablePrx getTable();

}