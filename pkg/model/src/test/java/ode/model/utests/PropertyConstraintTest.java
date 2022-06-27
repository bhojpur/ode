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

import org.testng.annotations.Test;

import ode.model.acquisition.Laser;
import ode.model.acquisition.LightSettings;
import ode.model.acquisition.TransmittanceRange;
import ode.model.core.LogicalChannel;
import ode.model.core.Pixels;
import ode.model.enums.UnitsLength;
import ode.model.units.Length;

/**
 * Test that client-side code enforces the special cases of column domain constraints enumerated in <tt>psql-footer.vm</tt>.
 */
public class PropertyConstraintTest {

    /* Laser.wavelength is a positive float */

    @Test
    public void testPositiveLaserWavelength() {
        new Laser().setWavelength(new Length(1, UnitsLength.CENTIMETER));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testZeroLaserWavelength() {
        new Laser().setWavelength(new Length(0, UnitsLength.CENTIMETER));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNegativeLaserWavelength() {
        new Laser().setWavelength(new Length(-1, UnitsLength.CENTIMETER));
    }

    /* LightSettings.wavelength is a positive float */

    @Test
    public void testPositiveLightSettingsWavelength() {
        new LightSettings().setWavelength(new Length(1, UnitsLength.CENTIMETER));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testZeroLightSettingsWavelength() {
        new LightSettings().setWavelength(new Length(0, UnitsLength.CENTIMETER));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNegativeLightSettingsWavelength() {
        new LightSettings().setWavelength(new Length(-1, UnitsLength.CENTIMETER));
    }

    /* LogicalChannel.emissionWave is a positive float */

    @Test
    public void testPositiveLogicalChannelEmissionWave() {
        new LogicalChannel().setEmissionWave(new Length(1, UnitsLength.CENTIMETER));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testZeroLogicalChannelEmissionWave() {
        new LogicalChannel().setEmissionWave(new Length(0, UnitsLength.CENTIMETER));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNegativeLogicalChannelEmissionWave() {
        new LogicalChannel().setEmissionWave(new Length(-1, UnitsLength.CENTIMETER));
    }

    /* LogicalChannel.excitationWave is a positive float */

    @Test
    public void testPositiveLogicalChannelExcitationWave() {
        new LogicalChannel().setExcitationWave(new Length(1, UnitsLength.CENTIMETER));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testZeroLogicalChannelExcitationWave() {
        new LogicalChannel().setExcitationWave(new Length(0, UnitsLength.CENTIMETER));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNegativeLogicalChannelExcitationWave() {
        new LogicalChannel().setExcitationWave(new Length(-1, UnitsLength.CENTIMETER));
    }

    /* Pixels.physicalSizeX is a positive float */

    @Test
    public void testPositivePixelsPhysicalSizeX() {
        new Pixels().setPhysicalSizeX(new Length(1, UnitsLength.CENTIMETER));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testZeroPixelsPhysicalSizeX() {
        new Pixels().setPhysicalSizeX(new Length(0, UnitsLength.CENTIMETER));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNegativePixelsPhysicalSizeX() {
        new Pixels().setPhysicalSizeX(new Length(-1, UnitsLength.CENTIMETER));
    }

    /* Pixels.physicalSizeY is a positive float */

    @Test
    public void testPositivePixelsPhysicalSizeY() {
        new Pixels().setPhysicalSizeY(new Length(1, UnitsLength.CENTIMETER));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testZeroPixelsPhysicalSizeY() {
        new Pixels().setPhysicalSizeY(new Length(0, UnitsLength.CENTIMETER));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNegativePixelsPhysicalSizeY() {
        new Pixels().setPhysicalSizeY(new Length(-1, UnitsLength.CENTIMETER));
    }

    /* Pixels.physicalSizeZ is a positive float */

    @Test
    public void testPositivePixelsPhysicalSizeZ() {
        new Pixels().setPhysicalSizeZ(new Length(1, UnitsLength.CENTIMETER));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testZeroPixelsPhysicalSizeZ() {
        new Pixels().setPhysicalSizeZ(new Length(0, UnitsLength.CENTIMETER));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNegativePixelsPhysicalSizeZ() {
        new Pixels().setPhysicalSizeZ(new Length(-1, UnitsLength.CENTIMETER));
    }

    /* TransmittanceRange.cutIn is a positive float */

    @Test
    public void testPositiveTransmittanceRangeCutIn() {
        new TransmittanceRange().setCutIn(new Length(1, UnitsLength.CENTIMETER));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testZeroTransmittanceRangeCutIn() {
        new TransmittanceRange().setCutIn(new Length(0, UnitsLength.CENTIMETER));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNegativeTransmittanceRangeCutIn() {
        new TransmittanceRange().setCutIn(new Length(-1, UnitsLength.CENTIMETER));
    }

    /* TransmittanceRange.cutOut is a positive float */

    @Test
    public void testPositiveTransmittanceRangeCutOut() {
        new TransmittanceRange().setCutOut(new Length(1, UnitsLength.CENTIMETER));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testZeroTransmittanceRangeCutOut() {
        new TransmittanceRange().setCutOut(new Length(0, UnitsLength.CENTIMETER));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNegativeTransmittanceRangeCutOut() {
        new TransmittanceRange().setCutOut(new Length(-1, UnitsLength.CENTIMETER));
    }

    /* TransmittanceRange.cutInTolerance is a nonnegative float */

    @Test
    public void testPositiveTransmittanceRangeCutInTolerance() {
        new TransmittanceRange().setCutInTolerance(new Length(1, UnitsLength.CENTIMETER));
    }

    @Test
    public void testZeroTransmittanceRangeCutInTolerance() {
        new TransmittanceRange().setCutInTolerance(new Length(0, UnitsLength.CENTIMETER));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNegativeTransmittanceRangeCutInTolerance() {
        new TransmittanceRange().setCutInTolerance(new Length(-1, UnitsLength.CENTIMETER));
    }

    /* TransmittanceRange.cutOutTolerance is a nonnegative float */

    @Test
    public void testPositiveTransmittanceRangeCutOutTolerance() {
        new TransmittanceRange().setCutOutTolerance(new Length(1, UnitsLength.CENTIMETER));
    }

    @Test
    public void testZeroTransmittanceRangeCutOutTolerance() {
        new TransmittanceRange().setCutOutTolerance(new Length(0, UnitsLength.CENTIMETER));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNegativeTransmittanceRangeCutOutTolerance() {
        new TransmittanceRange().setCutOutTolerance(new Length(-1, UnitsLength.CENTIMETER));
    }
}