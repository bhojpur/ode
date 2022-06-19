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

#ifndef CLASS_UNITS
#define CLASS_UNITS

module ode {

    module model {

        module enums {

            enum UnitsElectricPotential {
                YOTTAVOLT,
                ZETTAVOLT,
                EXAVOLT,
                PETAVOLT,
                TERAVOLT,
                GIGAVOLT,
                MEGAVOLT,
                KILOVOLT,
                HECTOVOLT,
                DECAVOLT,
                VOLT,
                DECIVOLT,
                CENTIVOLT,
                MILLIVOLT,
                MICROVOLT,
                NANOVOLT,
                PICOVOLT,
                FEMTOVOLT,
                ATTOVOLT,
                ZEPTOVOLT,
                YOCTOVOLT
            };

            enum UnitsFrequency {
                YOTTAHERTZ,
                ZETTAHERTZ,
                EXAHERTZ,
                PETAHERTZ,
                TERAHERTZ,
                GIGAHERTZ,
                MEGAHERTZ,
                KILOHERTZ,
                HECTOHERTZ,
                DECAHERTZ,
                HERTZ,
                DECIHERTZ,
                CENTIHERTZ,
                MILLIHERTZ,
                MICROHERTZ,
                NANOHERTZ,
                PICOHERTZ,
                FEMTOHERTZ,
                ATTOHERTZ,
                ZEPTOHERTZ,
                YOCTOHERTZ
            };

            enum UnitsLength {
                YOTTAMETER,
                ZETTAMETER,
                EXAMETER,
                PETAMETER,
                TERAMETER,
                GIGAMETER,
                MEGAMETER,
                KILOMETER,
                HECTOMETER,
                DECAMETER,
                METER,
                DECIMETER,
                CENTIMETER,
                MILLIMETER,
                MICROMETER,
                NANOMETER,
                PICOMETER,
                FEMTOMETER,
                ATTOMETER,
                ZEPTOMETER,
                YOCTOMETER,
                ANGSTROM,
                ASTRONOMICALUNIT,
                LIGHTYEAR,
                PARSEC,
                THOU,
                LINE,
                INCH,
                FOOT,
                YARD,
                MILE,
                POINT,
                PIXEL,
                REFERENCEFRAME
            };

            enum UnitsPower {
                YOTTAWATT,
                ZETTAWATT,
                EXAWATT,
                PETAWATT,
                TERAWATT,
                GIGAWATT,
                MEGAWATT,
                KILOWATT,
                HECTOWATT,
                DECAWATT,
                WATT,
                DECIWATT,
                CENTIWATT,
                MILLIWATT,
                MICROWATT,
                NANOWATT,
                PICOWATT,
                FEMTOWATT,
                ATTOWATT,
                ZEPTOWATT,
                YOCTOWATT
            };

            enum UnitsPressure {
                YOTTAPASCAL,
                ZETTAPASCAL,
                EXAPASCAL,
                PETAPASCAL,
                TERAPASCAL,
                GIGAPASCAL,
                MEGAPASCAL,
                KILOPASCAL,
                HECTOPASCAL,
                DECAPASCAL,
                PASCAL,
                DECIPASCAL,
                CENTIPASCAL,
                MILLIPASCAL,
                MICROPASCAL,
                NANOPASCAL,
                PICOPASCAL,
                FEMTOPASCAL,
                ATTOPASCAL,
                ZEPTOPASCAL,
                YOCTOPASCAL,
                BAR,
                MEGABAR,
                KILOBAR,
                DECIBAR,
                CENTIBAR,
                MILLIBAR,
                ATMOSPHERE,
                PSI,
                TORR,
                MILLITORR,
                MMHG
            };

            enum UnitsTemperature {
                KELVIN,
                CELSIUS,
                FAHRENHEIT,
                RANKINE
            };

            enum UnitsTime {
                YOTTASECOND,
                ZETTASECOND,
                EXASECOND,
                PETASECOND,
                TERASECOND,
                GIGASECOND,
                MEGASECOND,
                KILOSECOND,
                HECTOSECOND,
                DECASECOND,
                SECOND,
                DECISECOND,
                CENTISECOND,
                MILLISECOND,
                MICROSECOND,
                NANOSECOND,
                PICOSECOND,
                FEMTOSECOND,
                ATTOSECOND,
                ZEPTOSECOND,
                YOCTOSECOND,
                MINUTE,
                HOUR,
                DAY
            };

        };

    };
};
#endif
