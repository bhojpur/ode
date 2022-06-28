package spec.schema.samples;

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

import ode.units.quantity.Frequency;
import ode.units.quantity.Length;
import ode.units.UNITS;
import loci.formats.FormatTools;

import ode.xml.model.enums.ArcType;
import ode.xml.model.enums.Binning;
import ode.xml.model.enums.Correction;
import ode.xml.model.enums.DetectorType;
import ode.xml.model.enums.DimensionOrder;
import ode.xml.model.enums.FilterType;
import ode.xml.model.enums.Immersion;
import ode.xml.model.enums.LaserMedium;
import ode.xml.model.enums.LaserType;
import ode.xml.model.enums.MicroscopeType;
import ode.xml.model.enums.PixelType;
import ode.xml.model.primitives.Color;
import ode.xml.model.primitives.NonNegativeLong;
import ode.xml.model.primitives.PercentFraction;
import ode.xml.model.primitives.PositiveInteger;

/**
 * This class represents the sample file 2011-06/6x4y1z1t1c1b-swatch-instrument.ode
 */
public class Instrument2011_06
{
    /**
     * This inner class holds all the string used by this file and the file location.
     * It is imported into the associated test class
     */
    public static class ref {
        public static final String FILE_LOCATION = "/spec/schema/samples/2011-06/6x4y1z1t1c8b-swatch-instrument.ode";
        public static final MicroscopeType Instrument0MicroscopeType = MicroscopeType.INVERTED;
        public static final String Instrument0MicroscopeManufacturer ="ODE Instruments";
        public static final String Instrument0MicroscopeModel = "Lab Mk3";
        public static final String Instrument0MicroscopeSerialNumber ="L3-1234";
        public static final Double Instrument0LightSource0Power = Double.valueOf("200");
        public static final String Instrument0LightSource0Manufacturer = "ODE Lights Ltd";
        public static final String Instrument0LightSource0Model = "Ruby60";
        public static final String Instrument0LightSource0SerialNumber = "A654321";
        public static final LaserType Instrument0LightSource0LaserType = LaserType.SOLIDSTATE;
        public static final LaserMedium Instrument0LightSource0LaserLaserMedium = LaserMedium.RUBY;
        public static final Double Instrument0LightSource1Power = Double.valueOf("300");
        public static final String Instrument0LightSource1Manufacturer = "ODE Lights Inc";
        public static final String Instrument0LightSource1Model = "Arc60";
        public static final String Instrument0LightSource1SerialNumber = "A123456";
        public static final ArcType Instrument0LightSource1ArcType = ArcType.XE;
        public static final DetectorType Instrument0DetectorType = DetectorType.CCD;
        public static final String Instrument0Detector0Manufacturer = "ODE Detectors";
        public static final String Instrument0Detector0Model = "Standard CCD Mk2";
        public static final Correction Instrument0Objective0Correction = Correction.UV;
        public static final Immersion Instrument0Objective0Immersion = Immersion.AIR;
        public static final Double Instrument0Objective0LensNA = Double.valueOf("1.2");
        public static final Double Instrument0Objective0NominalMagnification = Double.valueOf("60.0");
        public static final Double Instrument0Objective0CalibratedMagnification = Double.valueOf("60.12");
        public static final Length Instrument0Objective0WorkingDistance = FormatTools.createLength(Double.valueOf("20"), UNITS.MICRODETER);
        public static final String Instrument0Objective0Manufacturer = "ODE Objectives";
        public static final String Instrument0Objective0Model = "60xUV-Air";
        public static final String Instrument0FilterSet0Manufacturer = "ODE Filters";
        public static final String Instrument0FilterSet0Model = "Standard Mk3";
        public static final String Instrument0FilterSet0LotNumber = "Lot174-A";
        public static final String Instrument0Filter0Manufacturer = "ODE Filters Inc";
        public static final String Instrument0Filter0Model = "Model1";
        public static final String Instrument0Filter0FilterWheel = "Disc A";
        public static final Length Instrument0Filter0TransmittanceRangeCutIn = new Length(350.0, UNITS.NANOMETER);
        public static final Length Instrument0Filter0TransmittanceRangeCutOut = new Length(450.0, UNITS.NANOMETER);
        public static final Length Instrument0Filter0TransmittanceRangeCutInTolerance = new Length(10.0, UNITS.NANOMETER);
        public static final Length Instrument0Filter0TransmittanceRangeCutOutTolerance = new Length(20.0, UNITS.NANOMETER);
        public static final PercentFraction Instrument0Filter0TransmittanceRangeTransmittance = PercentFraction.valueOf("0.3");
        public static final String Instrument0Filter1Manufacturer = "ODE Filters Ltd";
        public static final String Instrument0Filter1Model = "Deluxe Mk4";
        public static final FilterType Instrument0Filter1Type = FilterType.BANDPASS;
        public static final Length Instrument0Filter1TransmittanceRangeCutIn = new Length(560.0, UNITS.NANOMETER);
        public static final Length Instrument0Filter1TransmittanceRangeCutOut = new Length(630.0, UNITS.NANOMETER);
        public static final Length Instrument0Filter1TransmittanceRangeCutInTolerance = new Length(25.0, UNITS.NANOMETER);
        public static final Length Instrument0Filter1TransmittanceRangeCutOutTolerance = new Length(30.0, UNITS.NANOMETER);
        public static final PercentFraction Instrument0Filter1TransmittanceRangeTransmittance = PercentFraction.valueOf("0.8");
        public static final String Instrument0Filter2Manufacturer = "ODE Filters Asc";
        public static final String Instrument0Filter2Model = "Deluxe Mk5";
        public static final FilterType Instrument0Filter2Type = FilterType.BANDPASS;
        public static final Length Instrument0Filter2TransmittanceRangeCutIn = new Length(562.0, UNITS.NANOMETER);
        public static final Length Instrument0Filter2TransmittanceRangeCutOut = new Length(633.0, UNITS.NANOMETER);
        public static final Length Instrument0Filter2TransmittanceRangeCutInTolerance = new Length(11.0, UNITS.NANOMETER);
        public static final Length Instrument0Filter2TransmittanceRangeCutOutTolerance = new Length(23.0, UNITS.NANOMETER);
        public static final PercentFraction Instrument0Filter2TransmittanceRangeTransmittance = PercentFraction.valueOf("0.5");
        public static final String Instrument0Filter3Manufacturer = "ODE Filters.Com";
        public static final String Instrument0Filter3Model = "Deluxe Mk6";
        public static final FilterType Instrument0Filter3Type = FilterType.BANDPASS;
        public static final Length Instrument0Filter3TransmittanceRangeCutIn = new Length(463.0, UNITS.NANOMETER);
        public static final Length Instrument0Filter3TransmittanceRangeCutOut = new Length(535.0, UNITS.NANOMETER);
        public static final Length Instrument0Filter3TransmittanceRangeCutInTolerance = new Length(21.0, UNITS.NANOMETER);
        public static final Length Instrument0Filter3TransmittanceRangeCutOutTolerance = new Length(34.0, UNITS.NANOMETER);
        public static final PercentFraction Instrument0Filter3TransmittanceRangeTransmittance = PercentFraction.valueOf("0.7");
        public static final String Instrument0Dichroic0Model = "Standard Mk3" ;
        public static final String Instrument0Dichroic0Manufacturer = "ODE Instruments";
        public static final String Image0Name = "6x6x1x8-swatch.tif";
        public static final String Image0AcquiredDate = "2010-02-23T12:51:30";
        public static final String Image0Description = "This image has instrument data";
        public static final DimensionOrder Image0Pixels0_0DimensionOrder = DimensionOrder.XYCZT;
        public static final Length Image0Pixels0_0PhysicalSizeX = FormatTools.createLength(Double.valueOf("10000.0"), UNITS.MICROMETER);
        public static final Length Image0Pixels0_0PhysicalSizeY = FormatTools.createLength(Double.valueOf("10000.0"), UNITS.MICROMETER);
        public static final PixelType Image0Pixels0_0Type = PixelType.UINT8;
        public static final PositiveInteger Image0Pixels0_0SizeC = PositiveInteger.valueOf("1");
        public static final PositiveInteger Image0Pixels0_0SizeT = PositiveInteger.valueOf("1");
        public static final PositiveInteger Image0Pixels0_0SizeX = PositiveInteger.valueOf("6");
        public static final PositiveInteger Image0Pixels0_0SizeY = PositiveInteger.valueOf("4");
        public static final PositiveInteger Image0Pixels0_0SizeZ = PositiveInteger.valueOf("1");
        public static final Color Image0Pixels0_0Channel0Color = Color.valueOf("-2147483648");
        public static final NonNegativeLong Image0Pixels0_0Bindata0Length = NonNegativeLong.valueOf("32");
        public static final Boolean Image0Pixels0_0Bindata0BigEndian = Boolean.FALSE;
        public static final PercentFraction Image0LightSourceSettings0Attenuation = PercentFraction.valueOf("0.8");
        public static final Length Image0LightSourceSettings0Wavelength = FormatTools.createLength(Double.valueOf("510"), UNITS.NANOMETER);
        public static final Binning Image0DetectorSettings0Binning = Binning.TWOBYTWO;
        public static final Double Image0DetectorSettings0Gain = Double.valueOf("1.2");
        public static final Double Image0DetectorSettings0Offset = Double.valueOf("0.7");
        public static final Frequency Image0DetectorSettings0ReadOutRate = new Frequency(3200.0, UNITS.MEGAHERTZ);
        public static final Double Image0DetectorSettings0Voltage = Double.valueOf("120");
    }
}
