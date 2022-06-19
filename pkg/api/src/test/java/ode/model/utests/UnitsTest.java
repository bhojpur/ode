package ode.model.utests;

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

import ode.model.units.BigResult;
import ode.units.UNITS;
import ode.model.LengthI;
import ode.model.enums.UnitsLength;

import org.testng.annotations.Test;

public class UnitsTest {

    /**
     * Creates a new length instance of the default units.
     *
     * @param d The value.
     * @return See above.
     */
    protected ode.model.Length mm(double d) {
        return mm(d, UnitsLength.MILLIMETER);
    }

    /**
     * Creates a new length instance of the specified units.
     *
     * @param d The value.
     * @param units The units.
     * @return See above.
     */
    protected ode.model.Length mm(double d, UnitsLength units) {
        ode.model.Length l = new ode.model.LengthI();
        l.setUnit(units);
        l.setValue(d);
        return l;
    }

    @Test
    public void testLengthConversion() {
        LengthI.convert(mm(1));
    }

    @Test
    public void testLengthMapping() throws BigResult {
        new LengthI(mm(1), UNITS.MEGAMETER);
    }

    @Test
    public void testLengthNoOpMapping() throws BigResult {
        new LengthI(mm(1), UNITS.MEGAMETER);
    }

    @Test
    public void testLengthMappingFromCentimeterToMicrometer() throws BigResult {
        new LengthI(mm(1, UnitsLength.CENTIMETER), UnitsLength.MICROMETER);
    }

    @Test
    public void testLengthMappingFromMeterToMicrometer() throws BigResult {
        new LengthI(mm(1, UnitsLength.METER), UnitsLength.MICROMETER);
    }

    @Test
    public void testLengthMappingFromMicrometerToMicrometer() throws BigResult {
        new LengthI(mm(1, UnitsLength.MICROMETER), UnitsLength.MICROMETER);
    }

    @Test
    public void testLengthMappingFromMeterToCentimeter() throws BigResult {
        new LengthI(mm(1, UnitsLength.METER), UnitsLength.CENTIMETER);
    }

    @Test
    public void testLengthMappingFromCentimeterToMeter() throws BigResult {
        new LengthI(mm(1, UnitsLength.CENTIMETER), UnitsLength.METER);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testLengthMappingFromPixelToMicrometer() throws BigResult {
        new LengthI(mm(1, UnitsLength.PIXEL), UnitsLength.MICROMETER);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testLengthMappingFromReferenceFrameToMicrometer() throws BigResult {
        new LengthI(mm(1, UnitsLength.REFERENCEFRAME), UnitsLength.MICROMETER);
    }
}