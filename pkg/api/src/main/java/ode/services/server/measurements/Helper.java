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

import ode.ServerError;
import ode.ValidationException;
import ode.grid.BoolColumn;
import ode.grid.Column;
import ode.grid.DoubleColumn;
import ode.grid.FileColumn;
import ode.grid.ImageColumn;
import ode.grid.LongColumn;
import ode.grid.RoiColumn;
import ode.grid.StringColumn;
import ode.grid.WellColumn;

/**
 * Parallel type hierarchy to {@link ode.grid.Column} to facilitate (and
 * speed-up) working with columns without adding methods to the slice
 * definitions.
 */
public abstract class Helper {

    /**
     * Factory method to generate Helper instances from the LSID of a given
     * column.
     * 
     * @param lsid The lsid
     * @return See above.
     * @throws ServerError
     */
    public static Helper fromLsid(String lsid) throws ServerError {
        if (lsid.contains("File")) {
            return new FileHelper();
        } else if (lsid.contains("Roi")) {
            return new RoiHelper();
        } else if (lsid.contains("Image")) {
            return new ImageHelper();
        } else if (lsid.contains("Well")) {
            return new WellHelper();
        } else {
            throw new ValidationException(null, null, "Unknown id type: "
                    + lsid);
        }
    }

    /**
     * Factory method to generate Helper instances from the Class of a given
     * column.
     * 
     * @param kls The class.
     * @return See above.
     * @throws ServerError
     */
    public static Helper fromClass(Class kls) throws ServerError {
        if (Long.class.equals(kls)) {
            return new LongHelper();
        } else if (Double.class.equals(kls)) {
            return new DoubleHelper();
        } else if (Boolean.class.equals(kls)) {
            return new BoolHelper();
        } else if (String.class.equals(kls)) {
            return new StringHelper();
        } else {
            throw new ValidationException(null, null, "Unknown column type: "
                    + kls.getName());
        }
    }

    abstract Column newInstance();

    abstract void setSize(Column col, int length);

    abstract void setValue(Column col, int rowIdx, Object object);

    static class FileHelper extends Helper {
        @Override
        Column newInstance() {
            return new FileColumn();
        }

        @Override
        void setSize(Column col, int length) {
            ((FileColumn) col).values = new long[length];
        }

        @Override
        void setValue(Column col, int rowIdx, Object value) {
            ((FileColumn) col).values[rowIdx] = ((Long) value).longValue();
        }
    }
    
    static class RoiHelper extends Helper {
        @Override
        Column newInstance() {
            return new RoiColumn();
        }

        @Override
        void setSize(Column col, int length) {
            ((RoiColumn) col).values = new long[length];
        }

        @Override
        void setValue(Column col, int rowIdx, Object value) {
            ((RoiColumn) col).values[rowIdx] = ((Long) value).longValue();
        }
    }

    static class ImageHelper extends Helper {
        @Override
        Column newInstance() {
            return new ImageColumn();
        }

        @Override
        void setSize(Column col, int length) {
            ((ImageColumn) col).values = new long[length];
        }

        @Override
        void setValue(Column col, int rowIdx, Object value) {
            ((ImageColumn) col).values[rowIdx] = ((Long) value).longValue();
        }

    }

    static class WellHelper extends Helper {
        @Override
        Column newInstance() {
            return new WellColumn();
        }

        @Override
        void setSize(Column col, int length) {
            ((WellColumn) col).values = new long[length];
        }

        @Override
        void setValue(Column col, int rowIdx, Object value) {
            ((WellColumn) col).values[rowIdx] = ((Long) value).longValue();
        }
    }

    static class BoolHelper extends Helper {
        @Override
        Column newInstance() {
            return new BoolColumn();
        }

        @Override
        void setSize(Column col, int length) {
            ((BoolColumn) col).values = new boolean[length];
        }

        @Override
        void setValue(Column col, int rowIdx, Object value) {
            ((BoolColumn) col).values[rowIdx] = ((Boolean) value)
                    .booleanValue();
        }
    }

    static class DoubleHelper extends Helper {
        @Override
        Column newInstance() {
            return new DoubleColumn();
        }

        @Override
        void setSize(Column col, int length) {
            ((DoubleColumn) col).values = new double[length];
        }

        @Override
        void setValue(Column col, int rowIdx, Object value) {
            ((DoubleColumn) col).values[rowIdx] = ((Double) value)
                    .doubleValue();
        }
    }

    static class LongHelper extends Helper {
        @Override
        Column newInstance() {
            return new LongColumn();
        }

        @Override
        void setSize(Column col, int length) {
            ((LongColumn) col).values = new long[length];
        }

        @Override
        void setValue(Column col, int rowIdx, Object value) {
            ((LongColumn) col).values[rowIdx] = ((Long) value).longValue();
        }
    }

    static class StringHelper extends Helper {
        @Override
        Column newInstance() {
            return new StringColumn();
        }

        @Override
        void setSize(Column col, int length) {
            ((StringColumn) col).values = new String[length];
        }

        @Override
        void setValue(Column col, int rowIdx, Object value) {
            ((StringColumn) col).values[rowIdx] = (String) value;
        }
    }
}