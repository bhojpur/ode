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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ode.api.local.LocalUpdate;
import ode.model.IObject;
import ode.model.core.Image;
import ode.model.roi.Ellipse;
import ode.model.roi.Roi;
import ode.ApiUsageException;
import ode.RType;
import ode.ServerError;
import ode.grid.TablePrx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parser API implementation for storing result measurements from SPW companion
 * files as ROIs and ODE.tables.
 */
public class MeasurementStore implements OdeMeasurementStore {

    // Static

    private final static Logger log = LoggerFactory.getLogger(MeasurementStore.class);

    // Final

    private final Map<String, IObject> lsidMap = new HashMap<String, IObject>();

    private final List<Long> roiIds = new ArrayList<Long>();

    private final LocalUpdate update;

    private final TablePrx table;

    // Mutable

    private Map<String, Object> metadata;

    private String[] headers;

    private String[] idTypes;

    private Class[] types;

    private SmartColumns cols;

    private Object[][] rows;

    private boolean initialized = false;

    public MeasurementStore(LocalUpdate update, TablePrx table)
            throws ServerError {
        this.table = table;
        this.update = update;
    }

    // LOCI API
    // =========================================================================

    public void initialize(String[] headers, String[] idTypes, Class[] types,
            Map<String, Object> metadata) throws ServerError {

        if (initialized) {
            throw new ode.ApiUsageException(null, null,
                    "Already initialized.");
        }

        this.metadata = metadata;
        this.headers = headers;
        this.idTypes = idTypes;
        this.types = types;

        cols = new SmartColumns(lsidMap, headers, idTypes, types);
        table.initialize(cols.asArray());
        Map<String, RType> dict = new HashMap<String, RType>();
        if (metadata != null) {
            for (Map.Entry<String, Object> entry : metadata.entrySet()) {
                dict.put(entry.getKey(), ode.rtypes.rtype(entry.getValue()));
            }
            table.setAllMetadata(dict);
        }
        initialized = true;

    }

    public void addObject(String lsid, IObject obj) {
        if (lsid != null) {
            if (obj == null) {
                lsidMap.remove(lsid);
            } else {
                lsidMap.put(lsid, obj);
            }
        }
    }

    public void addObjects(Map<String, IObject> map) {
        if (map != null) {
            lsidMap.putAll(map);
        }
    }

    public void addRows(Object[][] rows) throws ServerError {
        if (rows != null) {
            throw new ApiUsageException(null, null,
                    "Call save before adding more data");
        }
        this.rows = rows;
    }

    public void addCircle(String roiLsid, double x, double y, double r)
            throws ServerError {
        Roi roi = new Roi();
        Ellipse ellipse = new Ellipse();
        ellipse.setX(x);
        ellipse.setY(y);
        ellipse.setRadiusX(r);
        ellipse.setRadiusY(r);
        roi.addShape(ellipse);
        appendRoi(roiLsid, roi);
    }

    private void appendRoi(String lsid, Roi roi) throws ServerError {
        Image image = null;
        IObject obj = lsidMap.get(lsid);
        if (obj instanceof Image) {
            image = (Image) obj;
        }
        if (image == null || image.getId() == null) {
            throw new ApiUsageException(null, null,
                    "No image set; cannot create Roi");
        }
        roi.setImage(image);
        lsidMap.put(lsid, roi);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ode.services.server.measurements.OdeMeasurementStore#save()
     */
    public void save() throws Exception {

        if (!initialized) {
            throw new ApiUsageException(null, null, "Not initialized");
        }

        //
        // First save the rois, so that if it fails, no changes will be
        // made to the table
        //
        List<IObject> unsaved = new ArrayList<IObject>();
        for (Map.Entry<String, IObject> entry : lsidMap.entrySet()) {
            unsaved.add(entry.getValue());
        }
        if (unsaved.size() > 0) {
            IObject[] objs = unsaved.toArray(new IObject[unsaved.size()]);
            List<Long> ids = update.saveAndReturnIds(objs);
            for (int i = 0; i < ids.size(); i++) {
                unsaved.get(i).setId(ids.get(i));
                unsaved.get(i).unload();
                roiIds.add(ids.get(i));
            }
        }

        // Now fill the columns with data, and send to the table instance
        cols.fill(rows);
        table.addData(cols.asArray());
        rows = null;

    }

    // Bhojpur ODE API
    // =========================================================================

    public List<Long> getRoiIds() {
        return roiIds;
    }

    public TablePrx getTable() {
        return table;
    }

}