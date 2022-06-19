package ode.services.roi.test;

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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ode.api.local.LocalUpdate;
import ode.model.IObject;
import ode.model.annotations.FileAnnotation;
import ode.model.annotations.ImageAnnotationLink;
import ode.model.annotations.XmlAnnotation;
import ode.model.core.Image;
import ode.model.core.OriginalFile;
import ode.services.server.measurements.MeasurementStore;
import ode.services.server.measurements.OdeMeasurementStore;
import ode.ApiUsageException;
import ode.RType;
import ode.grid.Column;
import ode.grid.FileColumn;
import ode.grid.ImageColumn;
import ode.grid.LongColumn;
import ode.grid.RoiColumn;
import ode.grid.StringColumn;
import ode.grid.TablePrx;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.Constraint;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 */
@Test(groups = { "rois", "measurements" })
public class MeasurementStoreUnitTest extends MockObjectTestCase {

    Image image;
    Mock tMock, uMock;
    LocalUpdate update;
    TablePrx table;
    OdeMeasurementStore mea;

    @BeforeMethod
    protected void beforeMethod() throws Exception {
        tMock = mock(TablePrx.class);
        table = (TablePrx) tMock.proxy();
        uMock = mock(LocalUpdate.class);
        update = (LocalUpdate) uMock.proxy();
        image = new Image();
    }

    public void testBasicUsage() throws Exception {

        Column[] cols = new Column[] { new RoiColumn(), new LongColumn() };
        initialize(cols);
        save(new long[] { 1L, 2L, 3L });
        data(cols, 3);

        mea = new MeasurementStore(update, table);
        mea.initialize(new String[] { "roi_id", "value_1" },
                new String[] { "Roi" }, new Class[] { Long.class }, null);
        mea.addRows(new Object[][] {//
                new Object[] { "Roi:1", 1L },//
                        new Object[] { "Roi:2", 0L },//
                        new Object[] { "Roi:3", 1L } //
                });
        mea.addCircle("Roi:1", 5, 5, 1);
        mea.addCircle("Roi:2", 5, 5, 2);
        mea.addCircle("Roi:3", 5, 5, 3);
        mea.save();

    }

    public void testMultipleFileUsage() throws Exception {

        Column[] cols = new Column[] { new FileColumn(), new RoiColumn(),
                new LongColumn() };
        initialize(cols);
        save(new long[] { 1L, 2L, 3L });
        data(cols, 3);

        mea = new MeasurementStore(update, table);
        mea.addObject("Image:1", image);
        mea.initialize(new String[] { "source", "roi_id", "value_1" },
                new String[] { "File", "Roi" }, new Class[] { Long.class },
                null);
        mea.addRows(new Object[][] {//
                new Object[] { "File:1", "Roi:1", 1L },//
                        new Object[] { "File:1", "Roi:2", 0L },//
                        new Object[] { "File:1", "Roi:3", 1L } //
                });
        mea.addCircle("Roi:1", 5, 5, 1);
        mea.addCircle("Roi:2", 5, 5, 2);
        mea.addCircle("Roi:3", 5, 5, 3);
        mea.save();

    }

    public void testImageColumnWithNoImage() throws Exception {

        fail();

        Column[] cols = new Column[] { new FileColumn(), new RoiColumn(),
                new LongColumn() };
        initialize(cols);
        save(new long[] { 1L, 2L, 3L });
        data(cols, 3);

        mea = new MeasurementStore(update, table);
        mea.addObject("Image:1", image);
        mea.initialize(new String[] { "source", "roi_id", "value_1" },
                new String[] { "File", "Roi" }, new Class[] { Long.class },
                null);
        mea.addRows(new Object[][] {//
                new Object[] { "File:1", "Roi:1", 1L },//
                        new Object[] { "File:1", "Roi:2", 0L },//
                        new Object[] { "File:1", "Roi:3", 1L } //
                });
        mea.addCircle("Roi:1", 5, 5, 1);
        mea.addCircle("Roi:2", 5, 5, 2);
        mea.addCircle("Roi:3", 5, 5, 3);
        mea.save();

    }

    public void testImageColumnWithAnImage() throws Exception {

        fail();

        Column[] cols = new Column[] { new FileColumn(), new RoiColumn(),
                new LongColumn() };
        initialize(cols);
        save(new long[] { 1L, 2L, 3L });
        data(cols, 3);

        mea = new MeasurementStore(update, table);
        mea.addObject("Image:1", image);
        mea.initialize(new String[] { "source", "roi_id", "value_1" },
                new String[] { "File", "Roi" }, new Class[] { Long.class },
                null);
        mea.addRows(new Object[][] {//
                new Object[] { "File:1", "Roi:1", 1L },//
                        new Object[] { "File:1", "Roi:2", 0L },//
                        new Object[] { "File:1", "Roi:3", 1L } //
                });
        mea.addCircle("Roi:1", 5, 5, 1);
        mea.addCircle("Roi:2", 5, 5, 2);
        mea.addCircle("Roi:3", 5, 5, 3);
        mea.save();

    }

    @Test(expectedExceptions = ApiUsageException.class)
    public void testSaveMustFollowAddRowsFails() throws Exception {
        fail();
    }

    public void testSaveMustFollowAddRowsPasses() throws Exception {
        fail();
    }

    private Map<XmlAnnotation, List<ImageAnnotationLink>> imagemodel() {

        Image i1 = new Image();
        Image i2 = new Image();

        OriginalFile of1a = new OriginalFile();
        OriginalFile of1b = new OriginalFile();
        OriginalFile of2a = new OriginalFile();
        OriginalFile of2b = new OriginalFile();

        Map<XmlAnnotation, List<ImageAnnotationLink>> rv = new HashMap<XmlAnnotation, List<ImageAnnotationLink>>();
        List<ImageAnnotationLink> linksA = new ArrayList<ImageAnnotationLink>();
        List<ImageAnnotationLink> linksB = new ArrayList<ImageAnnotationLink>();

        FileAnnotation fa1a = new FileAnnotation();
        fa1a.setFile(of1a);
        linksA.add(i1.linkAnnotation(fa1a));

        FileAnnotation fa1b = new FileAnnotation();
        fa1b.setFile(of1b);
        linksB.add(i1.linkAnnotation(fa1b));

        FileAnnotation fa2a = new FileAnnotation();
        fa2a.setFile(of2a);
        linksA.add(i2.linkAnnotation(fa2a));

        FileAnnotation fa2b = new FileAnnotation();
        fa2b.setFile(of2b);
        linksB.add(i2.linkAnnotation(fa2b));

        XmlAnnotation measA = new XmlAnnotation();
        fa1a.linkAnnotation(measA);
        fa2a.linkAnnotation(measA);
        rv.put(measA, linksA);

        XmlAnnotation measB = new XmlAnnotation();
        fa1b.linkAnnotation(measB);
        fa2b.linkAnnotation(measB);
        rv.put(measB, linksB);
        return rv;
    }

    /**
     * During import, the intent is that after all companion files have been
     * properly uploaded and attached to image/well/plate/etc that a query will
     * be done to find them, most likely based on the file format, and that
     */
    public void testRealUsage() throws Exception {

        table.initialize(new Column[] {
                new ImageColumn("image_id", null, null),
                new RoiColumn("roi_id", null, null),
                new LongColumn("colA", null, null),
                new LongColumn("colB", null, null) });

        Map<XmlAnnotation, List<ImageAnnotationLink>> linksPerMeasurement = imagemodel();

        for (XmlAnnotation measurement : linksPerMeasurement.keySet()) {
            List<ImageAnnotationLink> links = linksPerMeasurement
                    .get(measurement);
            for (ImageAnnotationLink link : links) {
                Image image = link.parent();
                OriginalFile file = ((FileAnnotation) link.child()).getFile();
                OdeMeasurementStore store = new MeasurementStore(update,
                        table);
                // HOW TO JOIN ALL THE TABLES TOGETHER
            }
        }

    }

    public void testNoRoisNoneAdded() throws Exception {

        Column[] cols = new Column[] { new StringColumn(), new LongColumn() };
        initialize(cols);
        save(new long[] { 1L, 2L, 3L });
        data(cols, 3);

        mea = new MeasurementStore(update, table);
        mea.initialize(new String[] { "roi_lsid", "value_1" }, new String[] {},
                new Class[] { String.class, Long.class }, null);
        mea.addRows(new Object[][] {//
                new Object[] { "Roi:1", 1L },//
                        new Object[] { "Roi:2", 0L },//
                        new Object[] { "Roi:3", 1L } //
                });
        mea.save();

    }

    public void testNoRoisButAdded() throws Exception {

        Column[] cols = new Column[] { new StringColumn(), new LongColumn() };
        initialize(cols);
        save(new long[] { 1L, 2L, 3L });
        data(cols, 3);

        mea = new MeasurementStore(update, table);
        mea.initialize(new String[] { "roi_lsid", "value_1" }, new String[] {},
                new Class[] { String.class, Long.class }, null);
        mea.addRows(new Object[][] {//
                new Object[] { "Roi:1", 1L },//
                        new Object[] { "Roi:2", 0L },//
                        new Object[] { "Roi:3", 1L } //
                });
        mea.addCircle("Roi:1", 5, 5, 1);
        mea.addCircle("Roi:2", 5, 5, 2);
        mea.addCircle("Roi:3", 5, 5, 3);
        mea.save();

    }

    public void testMetadata() throws Exception {

        Map<String, Object> metadata = new HashMap<String, Object>();
        metadata.put("ode.RString", "this is a string");
        metadata.put("ode.RLong", Long.valueOf(1));
        metadata.put("ode.RInt", Integer.valueOf(1));
        metadata.put("ode.RBool", Boolean.TRUE);
        metadata.put("ode.RDouble", Double.valueOf(0));
        metadata.put("ode.RFloat", Float.valueOf(0));

        Column[] cols = new Column[] { new StringColumn(), new LongColumn() };
        initialize(cols);
        metadataCK(metadata);
        save(new long[] { 1L, 2L, 3L });
        data(cols, 3);

        mea = new MeasurementStore(update, table);
        mea.initialize(new String[] { "roi_lsid", "value_1" }, new String[] {},
                new Class[] { String.class, Long.class }, null);
        mea.addRows(new Object[][] {//
                new Object[] { "Roi:1", 1L },//
                        new Object[] { "Roi:2", 0L },//
                        new Object[] { "Roi:3", 1L } //
                });
        mea.addCircle("Roi:1", 5, 5, 1);
        mea.addCircle("Roi:2", 5, 5, 2);
        mea.addCircle("Roi:3", 5, 5, 3);
        mea.save();

    }

    // Helpers
    // =========================================================================

    boolean colmatch(Column[] testcols, Column[] foundcols, Integer size) {

        boolean failed = false;
        failed |= (testcols.length != foundcols.length);
        for (int i = 0; i < testcols.length; i++) {
            failed |= (!(testcols[i].getClass().equals(foundcols[i].getClass())));
        }
        try {
            for (int i = 0; i < foundcols.length; i++) {
                Column col = foundcols[i];
                Field f = col.getClass().getField("values");
                Object o = f.get(col);
                if (size == null) {
                    failed |= (o != null);
                } else {
                    failed |= (size.intValue() != Array.getLength(o));
                }
            }
        } catch (Exception e) {
            failed = true;
        }
        return !failed;
    }

    void initialize(final Column[] testcols) {
        tMock.expects(once()).method("initialize").with(new Constraint() {
            public boolean eval(Object arg0) {
                Column[] foundcols = (Column[]) arg0;
                return colmatch(testcols, foundcols, null);
            }

            public StringBuffer describeTo(StringBuffer arg0) {
                arg0.append("proper columns");
                return arg0;
            }
        });
    }

    void metadataCK(Map<String, Object> metadata) {
        tMock.expects(once()).method("setAllMetadata").with(new Constraint() {
            public boolean eval(Object arg0) {
                Map<String, RType> dict = (Map<String, RType>) arg0;
                boolean maps = true;
                for (Map.Entry<String, RType> entry : dict.entrySet()) {
                    String k = entry.getKey();
                    try {
                        maps &= Class.forName(k).isAssignableFrom(
                                entry.getValue().getClass());
                    } catch (ClassNotFoundException e) {
                        maps = false;
                    }
                }
                return maps;
            }

            public StringBuffer describeTo(StringBuffer arg0) {
                arg0.append("properly parsed metadata");
                return arg0;
            }
        });
    }

    void save(long[] arr) {
        uMock.expects(once()).method("saveAndReturnIds").with(new Constraint() {
            public boolean eval(Object arg0) {
                IObject[] rois = (IObject[]) arg0;
                return rois.length == 3;
            }

            public StringBuffer describeTo(StringBuffer arg0) {
                arg0.append("saves all");
                return arg0;
            }
        }).will(returnValue(arr));
    }

    void data(final Column[] testcols, final int size) {
        tMock.expects(once()).method("addData").with(new Constraint() {
            public boolean eval(Object arg0) {
                Column[] foundcols = (Column[]) arg0;
                return colmatch(testcols, foundcols, size);
            }

            public StringBuffer describeTo(StringBuffer arg0) {
                arg0.append("proper data");
                return arg0;
            }
        });
    }
}