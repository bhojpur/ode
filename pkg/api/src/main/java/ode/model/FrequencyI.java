package ode.model;

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

import static ode.model.units.Conversion.Mul;
import static ode.model.units.Conversion.Add;
import static ode.model.units.Conversion.Int;
import static ode.model.units.Conversion.Pow;
import static ode.model.units.Conversion.Rat;
import static ode.model.units.Conversion.Sym;

import java.math.BigDecimal;

import java.util.Collections;
import java.util.Map;
import java.util.EnumMap;
import java.util.HashMap;

import ode.model.ModelBased;
import ode.model.units.BigResult;
import ode.model.units.Conversion;
import ode.units.unit.Unit;
import ode.util.Filterable;
import ode.util.ModelMapper;
import ode.util.ReverseModelMapper;
import ode.xml.model.enums.EnumerationException;

import ode.model.enums.UnitsFrequency;

/**
 * Bhojpur ODE server wrapper around the {@link ode.model.units.Frequency} class.
 * Like {@link Details} and {@link Permissions}, this object
 * is embedded into other objects and does not have a full life
 * cycle of its own.
 */
public class FrequencyI extends Frequency implements ModelBased {

    private static final long serialVersionUID = 1L;

    private static Map<UnitsFrequency, Conversion> createMapATTOHERTZ() {
        EnumMap<UnitsFrequency, Conversion> c =
            new EnumMap<UnitsFrequency, Conversion>(UnitsFrequency.class);
        c.put(UnitsFrequency.CENTIHERTZ, Mul(Rat(Int(1), Pow(10, 16)), Sym("attohz")));
        c.put(UnitsFrequency.DECAHERTZ, Mul(Rat(Int(1), Pow(10, 19)), Sym("attohz")));
        c.put(UnitsFrequency.DECIHERTZ, Mul(Rat(Int(1), Pow(10, 17)), Sym("attohz")));
        c.put(UnitsFrequency.EXAHERTZ, Mul(Rat(Int(1), Pow(10, 36)), Sym("attohz")));
        c.put(UnitsFrequency.FEMTOHERTZ, Mul(Rat(Int(1), Int(1000)), Sym("attohz")));
        c.put(UnitsFrequency.GIGAHERTZ, Mul(Rat(Int(1), Pow(10, 27)), Sym("attohz")));
        c.put(UnitsFrequency.HECTOHERTZ, Mul(Rat(Int(1), Pow(10, 20)), Sym("attohz")));
        c.put(UnitsFrequency.HERTZ, Mul(Rat(Int(1), Pow(10, 18)), Sym("attohz")));
        c.put(UnitsFrequency.KILOHERTZ, Mul(Rat(Int(1), Pow(10, 21)), Sym("attohz")));
        c.put(UnitsFrequency.MEGAHERTZ, Mul(Rat(Int(1), Pow(10, 24)), Sym("attohz")));
        c.put(UnitsFrequency.MICROHERTZ, Mul(Rat(Int(1), Pow(10, 12)), Sym("attohz")));
        c.put(UnitsFrequency.MILLIHERTZ, Mul(Rat(Int(1), Pow(10, 15)), Sym("attohz")));
        c.put(UnitsFrequency.NANOHERTZ, Mul(Rat(Int(1), Pow(10, 9)), Sym("attohz")));
        c.put(UnitsFrequency.PETAHERTZ, Mul(Rat(Int(1), Pow(10, 33)), Sym("attohz")));
        c.put(UnitsFrequency.PICOHERTZ, Mul(Rat(Int(1), Pow(10, 6)), Sym("attohz")));
        c.put(UnitsFrequency.TERAHERTZ, Mul(Rat(Int(1), Pow(10, 30)), Sym("attohz")));
        c.put(UnitsFrequency.YOCTOHERTZ, Mul(Pow(10, 6), Sym("attohz")));
        c.put(UnitsFrequency.YOTTAHERTZ, Mul(Rat(Int(1), Pow(10, 42)), Sym("attohz")));
        c.put(UnitsFrequency.ZEPTOHERTZ, Mul(Int(1000), Sym("attohz")));
        c.put(UnitsFrequency.ZETTAHERTZ, Mul(Rat(Int(1), Pow(10, 39)), Sym("attohz")));
        return Collections.unmodifiableMap(c);
    }

    private static Map<UnitsFrequency, Conversion> createMapCENTIHERTZ() {
        EnumMap<UnitsFrequency, Conversion> c =
            new EnumMap<UnitsFrequency, Conversion>(UnitsFrequency.class);
        c.put(UnitsFrequency.ATTOHERTZ, Mul(Pow(10, 16), Sym("centihz")));
        c.put(UnitsFrequency.DECAHERTZ, Mul(Rat(Int(1), Int(1000)), Sym("centihz")));
        c.put(UnitsFrequency.DECIHERTZ, Mul(Rat(Int(1), Int(10)), Sym("centihz")));
        c.put(UnitsFrequency.EXAHERTZ, Mul(Rat(Int(1), Pow(10, 20)), Sym("centihz")));
        c.put(UnitsFrequency.FEMTOHERTZ, Mul(Pow(10, 13), Sym("centihz")));
        c.put(UnitsFrequency.GIGAHERTZ, Mul(Rat(Int(1), Pow(10, 11)), Sym("centihz")));
        c.put(UnitsFrequency.HECTOHERTZ, Mul(Rat(Int(1), Pow(10, 4)), Sym("centihz")));
        c.put(UnitsFrequency.HERTZ, Mul(Rat(Int(1), Int(100)), Sym("centihz")));
        c.put(UnitsFrequency.KILOHERTZ, Mul(Rat(Int(1), Pow(10, 5)), Sym("centihz")));
        c.put(UnitsFrequency.MEGAHERTZ, Mul(Rat(Int(1), Pow(10, 8)), Sym("centihz")));
        c.put(UnitsFrequency.MICROHERTZ, Mul(Pow(10, 4), Sym("centihz")));
        c.put(UnitsFrequency.MILLIHERTZ, Mul(Int(10), Sym("centihz")));
        c.put(UnitsFrequency.NANOHERTZ, Mul(Pow(10, 7), Sym("centihz")));
        c.put(UnitsFrequency.PETAHERTZ, Mul(Rat(Int(1), Pow(10, 17)), Sym("centihz")));
        c.put(UnitsFrequency.PICOHERTZ, Mul(Pow(10, 10), Sym("centihz")));
        c.put(UnitsFrequency.TERAHERTZ, Mul(Rat(Int(1), Pow(10, 14)), Sym("centihz")));
        c.put(UnitsFrequency.YOCTOHERTZ, Mul(Pow(10, 22), Sym("centihz")));
        c.put(UnitsFrequency.YOTTAHERTZ, Mul(Rat(Int(1), Pow(10, 26)), Sym("centihz")));
        c.put(UnitsFrequency.ZEPTOHERTZ, Mul(Pow(10, 19), Sym("centihz")));
        c.put(UnitsFrequency.ZETTAHERTZ, Mul(Rat(Int(1), Pow(10, 23)), Sym("centihz")));
        return Collections.unmodifiableMap(c);
    }

    private static Map<UnitsFrequency, Conversion> createMapDECAHERTZ() {
        EnumMap<UnitsFrequency, Conversion> c =
            new EnumMap<UnitsFrequency, Conversion>(UnitsFrequency.class);
        c.put(UnitsFrequency.ATTOHERTZ, Mul(Pow(10, 19), Sym("decahz")));
        c.put(UnitsFrequency.CENTIHERTZ, Mul(Int(1000), Sym("decahz")));
        c.put(UnitsFrequency.DECIHERTZ, Mul(Int(100), Sym("decahz")));
        c.put(UnitsFrequency.EXAHERTZ, Mul(Rat(Int(1), Pow(10, 17)), Sym("decahz")));
        c.put(UnitsFrequency.FEMTOHERTZ, Mul(Pow(10, 16), Sym("decahz")));
        c.put(UnitsFrequency.GIGAHERTZ, Mul(Rat(Int(1), Pow(10, 8)), Sym("decahz")));
        c.put(UnitsFrequency.HECTOHERTZ, Mul(Rat(Int(1), Int(10)), Sym("decahz")));
        c.put(UnitsFrequency.HERTZ, Mul(Int(10), Sym("decahz")));
        c.put(UnitsFrequency.KILOHERTZ, Mul(Rat(Int(1), Int(100)), Sym("decahz")));
        c.put(UnitsFrequency.MEGAHERTZ, Mul(Rat(Int(1), Pow(10, 5)), Sym("decahz")));
        c.put(UnitsFrequency.MICROHERTZ, Mul(Pow(10, 7), Sym("decahz")));
        c.put(UnitsFrequency.MILLIHERTZ, Mul(Pow(10, 4), Sym("decahz")));
        c.put(UnitsFrequency.NANOHERTZ, Mul(Pow(10, 10), Sym("decahz")));
        c.put(UnitsFrequency.PETAHERTZ, Mul(Rat(Int(1), Pow(10, 14)), Sym("decahz")));
        c.put(UnitsFrequency.PICOHERTZ, Mul(Pow(10, 13), Sym("decahz")));
        c.put(UnitsFrequency.TERAHERTZ, Mul(Rat(Int(1), Pow(10, 11)), Sym("decahz")));
        c.put(UnitsFrequency.YOCTOHERTZ, Mul(Pow(10, 25), Sym("decahz")));
        c.put(UnitsFrequency.YOTTAHERTZ, Mul(Rat(Int(1), Pow(10, 23)), Sym("decahz")));
        c.put(UnitsFrequency.ZEPTOHERTZ, Mul(Pow(10, 22), Sym("decahz")));
        c.put(UnitsFrequency.ZETTAHERTZ, Mul(Rat(Int(1), Pow(10, 20)), Sym("decahz")));
        return Collections.unmodifiableMap(c);
    }

    private static Map<UnitsFrequency, Conversion> createMapDECIHERTZ() {
        EnumMap<UnitsFrequency, Conversion> c =
            new EnumMap<UnitsFrequency, Conversion>(UnitsFrequency.class);
        c.put(UnitsFrequency.ATTOHERTZ, Mul(Pow(10, 17), Sym("decihz")));
        c.put(UnitsFrequency.CENTIHERTZ, Mul(Int(10), Sym("decihz")));
        c.put(UnitsFrequency.DECAHERTZ, Mul(Rat(Int(1), Int(100)), Sym("decihz")));
        c.put(UnitsFrequency.EXAHERTZ, Mul(Rat(Int(1), Pow(10, 19)), Sym("decihz")));
        c.put(UnitsFrequency.FEMTOHERTZ, Mul(Pow(10, 14), Sym("decihz")));
        c.put(UnitsFrequency.GIGAHERTZ, Mul(Rat(Int(1), Pow(10, 10)), Sym("decihz")));
        c.put(UnitsFrequency.HECTOHERTZ, Mul(Rat(Int(1), Int(1000)), Sym("decihz")));
        c.put(UnitsFrequency.HERTZ, Mul(Rat(Int(1), Int(10)), Sym("decihz")));
        c.put(UnitsFrequency.KILOHERTZ, Mul(Rat(Int(1), Pow(10, 4)), Sym("decihz")));
        c.put(UnitsFrequency.MEGAHERTZ, Mul(Rat(Int(1), Pow(10, 7)), Sym("decihz")));
        c.put(UnitsFrequency.MICROHERTZ, Mul(Pow(10, 5), Sym("decihz")));
        c.put(UnitsFrequency.MILLIHERTZ, Mul(Int(100), Sym("decihz")));
        c.put(UnitsFrequency.NANOHERTZ, Mul(Pow(10, 8), Sym("decihz")));
        c.put(UnitsFrequency.PETAHERTZ, Mul(Rat(Int(1), Pow(10, 16)), Sym("decihz")));
        c.put(UnitsFrequency.PICOHERTZ, Mul(Pow(10, 11), Sym("decihz")));
        c.put(UnitsFrequency.TERAHERTZ, Mul(Rat(Int(1), Pow(10, 13)), Sym("decihz")));
        c.put(UnitsFrequency.YOCTOHERTZ, Mul(Pow(10, 23), Sym("decihz")));
        c.put(UnitsFrequency.YOTTAHERTZ, Mul(Rat(Int(1), Pow(10, 25)), Sym("decihz")));
        c.put(UnitsFrequency.ZEPTOHERTZ, Mul(Pow(10, 20), Sym("decihz")));
        c.put(UnitsFrequency.ZETTAHERTZ, Mul(Rat(Int(1), Pow(10, 22)), Sym("decihz")));
        return Collections.unmodifiableMap(c);
    }

    private static Map<UnitsFrequency, Conversion> createMapEXAHERTZ() {
        EnumMap<UnitsFrequency, Conversion> c =
            new EnumMap<UnitsFrequency, Conversion>(UnitsFrequency.class);
        c.put(UnitsFrequency.ATTOHERTZ, Mul(Pow(10, 36), Sym("exahz")));
        c.put(UnitsFrequency.CENTIHERTZ, Mul(Pow(10, 20), Sym("exahz")));
        c.put(UnitsFrequency.DECAHERTZ, Mul(Pow(10, 17), Sym("exahz")));
        c.put(UnitsFrequency.DECIHERTZ, Mul(Pow(10, 19), Sym("exahz")));
        c.put(UnitsFrequency.FEMTOHERTZ, Mul(Pow(10, 33), Sym("exahz")));
        c.put(UnitsFrequency.GIGAHERTZ, Mul(Pow(10, 9), Sym("exahz")));
        c.put(UnitsFrequency.HECTOHERTZ, Mul(Pow(10, 16), Sym("exahz")));
        c.put(UnitsFrequency.HERTZ, Mul(Pow(10, 18), Sym("exahz")));
        c.put(UnitsFrequency.KILOHERTZ, Mul(Pow(10, 15), Sym("exahz")));
        c.put(UnitsFrequency.MEGAHERTZ, Mul(Pow(10, 12), Sym("exahz")));
        c.put(UnitsFrequency.MICROHERTZ, Mul(Pow(10, 24), Sym("exahz")));
        c.put(UnitsFrequency.MILLIHERTZ, Mul(Pow(10, 21), Sym("exahz")));
        c.put(UnitsFrequency.NANOHERTZ, Mul(Pow(10, 27), Sym("exahz")));
        c.put(UnitsFrequency.PETAHERTZ, Mul(Int(1000), Sym("exahz")));
        c.put(UnitsFrequency.PICOHERTZ, Mul(Pow(10, 30), Sym("exahz")));
        c.put(UnitsFrequency.TERAHERTZ, Mul(Pow(10, 6), Sym("exahz")));
        c.put(UnitsFrequency.YOCTOHERTZ, Mul(Pow(10, 42), Sym("exahz")));
        c.put(UnitsFrequency.YOTTAHERTZ, Mul(Rat(Int(1), Pow(10, 6)), Sym("exahz")));
        c.put(UnitsFrequency.ZEPTOHERTZ, Mul(Pow(10, 39), Sym("exahz")));
        c.put(UnitsFrequency.ZETTAHERTZ, Mul(Rat(Int(1), Int(1000)), Sym("exahz")));
        return Collections.unmodifiableMap(c);
    }

    private static Map<UnitsFrequency, Conversion> createMapFEMTOHERTZ() {
        EnumMap<UnitsFrequency, Conversion> c =
            new EnumMap<UnitsFrequency, Conversion>(UnitsFrequency.class);
        c.put(UnitsFrequency.ATTOHERTZ, Mul(Int(1000), Sym("femtohz")));
        c.put(UnitsFrequency.CENTIHERTZ, Mul(Rat(Int(1), Pow(10, 13)), Sym("femtohz")));
        c.put(UnitsFrequency.DECAHERTZ, Mul(Rat(Int(1), Pow(10, 16)), Sym("femtohz")));
        c.put(UnitsFrequency.DECIHERTZ, Mul(Rat(Int(1), Pow(10, 14)), Sym("femtohz")));
        c.put(UnitsFrequency.EXAHERTZ, Mul(Rat(Int(1), Pow(10, 33)), Sym("femtohz")));
        c.put(UnitsFrequency.GIGAHERTZ, Mul(Rat(Int(1), Pow(10, 24)), Sym("femtohz")));
        c.put(UnitsFrequency.HECTOHERTZ, Mul(Rat(Int(1), Pow(10, 17)), Sym("femtohz")));
        c.put(UnitsFrequency.HERTZ, Mul(Rat(Int(1), Pow(10, 15)), Sym("femtohz")));
        c.put(UnitsFrequency.KILOHERTZ, Mul(Rat(Int(1), Pow(10, 18)), Sym("femtohz")));
        c.put(UnitsFrequency.MEGAHERTZ, Mul(Rat(Int(1), Pow(10, 21)), Sym("femtohz")));
        c.put(UnitsFrequency.MICROHERTZ, Mul(Rat(Int(1), Pow(10, 9)), Sym("femtohz")));
        c.put(UnitsFrequency.MILLIHERTZ, Mul(Rat(Int(1), Pow(10, 12)), Sym("femtohz")));
        c.put(UnitsFrequency.NANOHERTZ, Mul(Rat(Int(1), Pow(10, 6)), Sym("femtohz")));
        c.put(UnitsFrequency.PETAHERTZ, Mul(Rat(Int(1), Pow(10, 30)), Sym("femtohz")));
        c.put(UnitsFrequency.PICOHERTZ, Mul(Rat(Int(1), Int(1000)), Sym("femtohz")));
        c.put(UnitsFrequency.TERAHERTZ, Mul(Rat(Int(1), Pow(10, 27)), Sym("femtohz")));
        c.put(UnitsFrequency.YOCTOHERTZ, Mul(Pow(10, 9), Sym("femtohz")));
        c.put(UnitsFrequency.YOTTAHERTZ, Mul(Rat(Int(1), Pow(10, 39)), Sym("femtohz")));
        c.put(UnitsFrequency.ZEPTOHERTZ, Mul(Pow(10, 6), Sym("femtohz")));
        c.put(UnitsFrequency.ZETTAHERTZ, Mul(Rat(Int(1), Pow(10, 36)), Sym("femtohz")));
        return Collections.unmodifiableMap(c);
    }

    private static Map<UnitsFrequency, Conversion> createMapGIGAHERTZ() {
        EnumMap<UnitsFrequency, Conversion> c =
            new EnumMap<UnitsFrequency, Conversion>(UnitsFrequency.class);
        c.put(UnitsFrequency.ATTOHERTZ, Mul(Pow(10, 27), Sym("gigahz")));
        c.put(UnitsFrequency.CENTIHERTZ, Mul(Pow(10, 11), Sym("gigahz")));
        c.put(UnitsFrequency.DECAHERTZ, Mul(Pow(10, 8), Sym("gigahz")));
        c.put(UnitsFrequency.DECIHERTZ, Mul(Pow(10, 10), Sym("gigahz")));
        c.put(UnitsFrequency.EXAHERTZ, Mul(Rat(Int(1), Pow(10, 9)), Sym("gigahz")));
        c.put(UnitsFrequency.FEMTOHERTZ, Mul(Pow(10, 24), Sym("gigahz")));
        c.put(UnitsFrequency.HECTOHERTZ, Mul(Pow(10, 7), Sym("gigahz")));
        c.put(UnitsFrequency.HERTZ, Mul(Pow(10, 9), Sym("gigahz")));
        c.put(UnitsFrequency.KILOHERTZ, Mul(Pow(10, 6), Sym("gigahz")));
        c.put(UnitsFrequency.MEGAHERTZ, Mul(Int(1000), Sym("gigahz")));
        c.put(UnitsFrequency.MICROHERTZ, Mul(Pow(10, 15), Sym("gigahz")));
        c.put(UnitsFrequency.MILLIHERTZ, Mul(Pow(10, 12), Sym("gigahz")));
        c.put(UnitsFrequency.NANOHERTZ, Mul(Pow(10, 18), Sym("gigahz")));
        c.put(UnitsFrequency.PETAHERTZ, Mul(Rat(Int(1), Pow(10, 6)), Sym("gigahz")));
        c.put(UnitsFrequency.PICOHERTZ, Mul(Pow(10, 21), Sym("gigahz")));
        c.put(UnitsFrequency.TERAHERTZ, Mul(Rat(Int(1), Int(1000)), Sym("gigahz")));
        c.put(UnitsFrequency.YOCTOHERTZ, Mul(Pow(10, 33), Sym("gigahz")));
        c.put(UnitsFrequency.YOTTAHERTZ, Mul(Rat(Int(1), Pow(10, 15)), Sym("gigahz")));
        c.put(UnitsFrequency.ZEPTOHERTZ, Mul(Pow(10, 30), Sym("gigahz")));
        c.put(UnitsFrequency.ZETTAHERTZ, Mul(Rat(Int(1), Pow(10, 12)), Sym("gigahz")));
        return Collections.unmodifiableMap(c);
    }

    private static Map<UnitsFrequency, Conversion> createMapHECTOHERTZ() {
        EnumMap<UnitsFrequency, Conversion> c =
            new EnumMap<UnitsFrequency, Conversion>(UnitsFrequency.class);
        c.put(UnitsFrequency.ATTOHERTZ, Mul(Pow(10, 20), Sym("hectohz")));
        c.put(UnitsFrequency.CENTIHERTZ, Mul(Pow(10, 4), Sym("hectohz")));
        c.put(UnitsFrequency.DECAHERTZ, Mul(Int(10), Sym("hectohz")));
        c.put(UnitsFrequency.DECIHERTZ, Mul(Int(1000), Sym("hectohz")));
        c.put(UnitsFrequency.EXAHERTZ, Mul(Rat(Int(1), Pow(10, 16)), Sym("hectohz")));
        c.put(UnitsFrequency.FEMTOHERTZ, Mul(Pow(10, 17), Sym("hectohz")));
        c.put(UnitsFrequency.GIGAHERTZ, Mul(Rat(Int(1), Pow(10, 7)), Sym("hectohz")));
        c.put(UnitsFrequency.HERTZ, Mul(Int(100), Sym("hectohz")));
        c.put(UnitsFrequency.KILOHERTZ, Mul(Rat(Int(1), Int(10)), Sym("hectohz")));
        c.put(UnitsFrequency.MEGAHERTZ, Mul(Rat(Int(1), Pow(10, 4)), Sym("hectohz")));
        c.put(UnitsFrequency.MICROHERTZ, Mul(Pow(10, 8), Sym("hectohz")));
        c.put(UnitsFrequency.MILLIHERTZ, Mul(Pow(10, 5), Sym("hectohz")));
        c.put(UnitsFrequency.NANOHERTZ, Mul(Pow(10, 11), Sym("hectohz")));
        c.put(UnitsFrequency.PETAHERTZ, Mul(Rat(Int(1), Pow(10, 13)), Sym("hectohz")));
        c.put(UnitsFrequency.PICOHERTZ, Mul(Pow(10, 14), Sym("hectohz")));
        c.put(UnitsFrequency.TERAHERTZ, Mul(Rat(Int(1), Pow(10, 10)), Sym("hectohz")));
        c.put(UnitsFrequency.YOCTOHERTZ, Mul(Pow(10, 26), Sym("hectohz")));
        c.put(UnitsFrequency.YOTTAHERTZ, Mul(Rat(Int(1), Pow(10, 22)), Sym("hectohz")));
        c.put(UnitsFrequency.ZEPTOHERTZ, Mul(Pow(10, 23), Sym("hectohz")));
        c.put(UnitsFrequency.ZETTAHERTZ, Mul(Rat(Int(1), Pow(10, 19)), Sym("hectohz")));
        return Collections.unmodifiableMap(c);
    }

    private static Map<UnitsFrequency, Conversion> createMapHERTZ() {
        EnumMap<UnitsFrequency, Conversion> c =
            new EnumMap<UnitsFrequency, Conversion>(UnitsFrequency.class);
        c.put(UnitsFrequency.ATTOHERTZ, Mul(Pow(10, 18), Sym("hz")));
        c.put(UnitsFrequency.CENTIHERTZ, Mul(Int(100), Sym("hz")));
        c.put(UnitsFrequency.DECAHERTZ, Mul(Rat(Int(1), Int(10)), Sym("hz")));
        c.put(UnitsFrequency.DECIHERTZ, Mul(Int(10), Sym("hz")));
        c.put(UnitsFrequency.EXAHERTZ, Mul(Rat(Int(1), Pow(10, 18)), Sym("hz")));
        c.put(UnitsFrequency.FEMTOHERTZ, Mul(Pow(10, 15), Sym("hz")));
        c.put(UnitsFrequency.GIGAHERTZ, Mul(Rat(Int(1), Pow(10, 9)), Sym("hz")));
        c.put(UnitsFrequency.HECTOHERTZ, Mul(Rat(Int(1), Int(100)), Sym("hz")));
        c.put(UnitsFrequency.KILOHERTZ, Mul(Rat(Int(1), Int(1000)), Sym("hz")));
        c.put(UnitsFrequency.MEGAHERTZ, Mul(Rat(Int(1), Pow(10, 6)), Sym("hz")));
        c.put(UnitsFrequency.MICROHERTZ, Mul(Pow(10, 6), Sym("hz")));
        c.put(UnitsFrequency.MILLIHERTZ, Mul(Int(1000), Sym("hz")));
        c.put(UnitsFrequency.NANOHERTZ, Mul(Pow(10, 9), Sym("hz")));
        c.put(UnitsFrequency.PETAHERTZ, Mul(Rat(Int(1), Pow(10, 15)), Sym("hz")));
        c.put(UnitsFrequency.PICOHERTZ, Mul(Pow(10, 12), Sym("hz")));
        c.put(UnitsFrequency.TERAHERTZ, Mul(Rat(Int(1), Pow(10, 12)), Sym("hz")));
        c.put(UnitsFrequency.YOCTOHERTZ, Mul(Pow(10, 24), Sym("hz")));
        c.put(UnitsFrequency.YOTTAHERTZ, Mul(Rat(Int(1), Pow(10, 24)), Sym("hz")));
        c.put(UnitsFrequency.ZEPTOHERTZ, Mul(Pow(10, 21), Sym("hz")));
        c.put(UnitsFrequency.ZETTAHERTZ, Mul(Rat(Int(1), Pow(10, 21)), Sym("hz")));
        return Collections.unmodifiableMap(c);
    }

    private static Map<UnitsFrequency, Conversion> createMapKILOHERTZ() {
        EnumMap<UnitsFrequency, Conversion> c =
            new EnumMap<UnitsFrequency, Conversion>(UnitsFrequency.class);
        c.put(UnitsFrequency.ATTOHERTZ, Mul(Pow(10, 21), Sym("kilohz")));
        c.put(UnitsFrequency.CENTIHERTZ, Mul(Pow(10, 5), Sym("kilohz")));
        c.put(UnitsFrequency.DECAHERTZ, Mul(Int(100), Sym("kilohz")));
        c.put(UnitsFrequency.DECIHERTZ, Mul(Pow(10, 4), Sym("kilohz")));
        c.put(UnitsFrequency.EXAHERTZ, Mul(Rat(Int(1), Pow(10, 15)), Sym("kilohz")));
        c.put(UnitsFrequency.FEMTOHERTZ, Mul(Pow(10, 18), Sym("kilohz")));
        c.put(UnitsFrequency.GIGAHERTZ, Mul(Rat(Int(1), Pow(10, 6)), Sym("kilohz")));
        c.put(UnitsFrequency.HECTOHERTZ, Mul(Int(10), Sym("kilohz")));
        c.put(UnitsFrequency.HERTZ, Mul(Int(1000), Sym("kilohz")));
        c.put(UnitsFrequency.MEGAHERTZ, Mul(Rat(Int(1), Int(1000)), Sym("kilohz")));
        c.put(UnitsFrequency.MICROHERTZ, Mul(Pow(10, 9), Sym("kilohz")));
        c.put(UnitsFrequency.MILLIHERTZ, Mul(Pow(10, 6), Sym("kilohz")));
        c.put(UnitsFrequency.NANOHERTZ, Mul(Pow(10, 12), Sym("kilohz")));
        c.put(UnitsFrequency.PETAHERTZ, Mul(Rat(Int(1), Pow(10, 12)), Sym("kilohz")));
        c.put(UnitsFrequency.PICOHERTZ, Mul(Pow(10, 15), Sym("kilohz")));
        c.put(UnitsFrequency.TERAHERTZ, Mul(Rat(Int(1), Pow(10, 9)), Sym("kilohz")));
        c.put(UnitsFrequency.YOCTOHERTZ, Mul(Pow(10, 27), Sym("kilohz")));
        c.put(UnitsFrequency.YOTTAHERTZ, Mul(Rat(Int(1), Pow(10, 21)), Sym("kilohz")));
        c.put(UnitsFrequency.ZEPTOHERTZ, Mul(Pow(10, 24), Sym("kilohz")));
        c.put(UnitsFrequency.ZETTAHERTZ, Mul(Rat(Int(1), Pow(10, 18)), Sym("kilohz")));
        return Collections.unmodifiableMap(c);
    }

    private static Map<UnitsFrequency, Conversion> createMapMEGAHERTZ() {
        EnumMap<UnitsFrequency, Conversion> c =
            new EnumMap<UnitsFrequency, Conversion>(UnitsFrequency.class);
        c.put(UnitsFrequency.ATTOHERTZ, Mul(Pow(10, 24), Sym("megahz")));
        c.put(UnitsFrequency.CENTIHERTZ, Mul(Pow(10, 8), Sym("megahz")));
        c.put(UnitsFrequency.DECAHERTZ, Mul(Pow(10, 5), Sym("megahz")));
        c.put(UnitsFrequency.DECIHERTZ, Mul(Pow(10, 7), Sym("megahz")));
        c.put(UnitsFrequency.EXAHERTZ, Mul(Rat(Int(1), Pow(10, 12)), Sym("megahz")));
        c.put(UnitsFrequency.FEMTOHERTZ, Mul(Pow(10, 21), Sym("megahz")));
        c.put(UnitsFrequency.GIGAHERTZ, Mul(Rat(Int(1), Int(1000)), Sym("megahz")));
        c.put(UnitsFrequency.HECTOHERTZ, Mul(Pow(10, 4), Sym("megahz")));
        c.put(UnitsFrequency.HERTZ, Mul(Pow(10, 6), Sym("megahz")));
        c.put(UnitsFrequency.KILOHERTZ, Mul(Int(1000), Sym("megahz")));
        c.put(UnitsFrequency.MICROHERTZ, Mul(Pow(10, 12), Sym("megahz")));
        c.put(UnitsFrequency.MILLIHERTZ, Mul(Pow(10, 9), Sym("megahz")));
        c.put(UnitsFrequency.NANOHERTZ, Mul(Pow(10, 15), Sym("megahz")));
        c.put(UnitsFrequency.PETAHERTZ, Mul(Rat(Int(1), Pow(10, 9)), Sym("megahz")));
        c.put(UnitsFrequency.PICOHERTZ, Mul(Pow(10, 18), Sym("megahz")));
        c.put(UnitsFrequency.TERAHERTZ, Mul(Rat(Int(1), Pow(10, 6)), Sym("megahz")));
        c.put(UnitsFrequency.YOCTOHERTZ, Mul(Pow(10, 30), Sym("megahz")));
        c.put(UnitsFrequency.YOTTAHERTZ, Mul(Rat(Int(1), Pow(10, 18)), Sym("megahz")));
        c.put(UnitsFrequency.ZEPTOHERTZ, Mul(Pow(10, 27), Sym("megahz")));
        c.put(UnitsFrequency.ZETTAHERTZ, Mul(Rat(Int(1), Pow(10, 15)), Sym("megahz")));
        return Collections.unmodifiableMap(c);
    }

    private static Map<UnitsFrequency, Conversion> createMapMICROHERTZ() {
        EnumMap<UnitsFrequency, Conversion> c =
            new EnumMap<UnitsFrequency, Conversion>(UnitsFrequency.class);
        c.put(UnitsFrequency.ATTOHERTZ, Mul(Pow(10, 12), Sym("microhz")));
        c.put(UnitsFrequency.CENTIHERTZ, Mul(Rat(Int(1), Pow(10, 4)), Sym("microhz")));
        c.put(UnitsFrequency.DECAHERTZ, Mul(Rat(Int(1), Pow(10, 7)), Sym("microhz")));
        c.put(UnitsFrequency.DECIHERTZ, Mul(Rat(Int(1), Pow(10, 5)), Sym("microhz")));
        c.put(UnitsFrequency.EXAHERTZ, Mul(Rat(Int(1), Pow(10, 24)), Sym("microhz")));
        c.put(UnitsFrequency.FEMTOHERTZ, Mul(Pow(10, 9), Sym("microhz")));
        c.put(UnitsFrequency.GIGAHERTZ, Mul(Rat(Int(1), Pow(10, 15)), Sym("microhz")));
        c.put(UnitsFrequency.HECTOHERTZ, Mul(Rat(Int(1), Pow(10, 8)), Sym("microhz")));
        c.put(UnitsFrequency.HERTZ, Mul(Rat(Int(1), Pow(10, 6)), Sym("microhz")));
        c.put(UnitsFrequency.KILOHERTZ, Mul(Rat(Int(1), Pow(10, 9)), Sym("microhz")));
        c.put(UnitsFrequency.MEGAHERTZ, Mul(Rat(Int(1), Pow(10, 12)), Sym("microhz")));
        c.put(UnitsFrequency.MILLIHERTZ, Mul(Rat(Int(1), Int(1000)), Sym("microhz")));
        c.put(UnitsFrequency.NANOHERTZ, Mul(Int(1000), Sym("microhz")));
        c.put(UnitsFrequency.PETAHERTZ, Mul(Rat(Int(1), Pow(10, 21)), Sym("microhz")));
        c.put(UnitsFrequency.PICOHERTZ, Mul(Pow(10, 6), Sym("microhz")));
        c.put(UnitsFrequency.TERAHERTZ, Mul(Rat(Int(1), Pow(10, 18)), Sym("microhz")));
        c.put(UnitsFrequency.YOCTOHERTZ, Mul(Pow(10, 18), Sym("microhz")));
        c.put(UnitsFrequency.YOTTAHERTZ, Mul(Rat(Int(1), Pow(10, 30)), Sym("microhz")));
        c.put(UnitsFrequency.ZEPTOHERTZ, Mul(Pow(10, 15), Sym("microhz")));
        c.put(UnitsFrequency.ZETTAHERTZ, Mul(Rat(Int(1), Pow(10, 27)), Sym("microhz")));
        return Collections.unmodifiableMap(c);
    }

    private static Map<UnitsFrequency, Conversion> createMapMILLIHERTZ() {
        EnumMap<UnitsFrequency, Conversion> c =
            new EnumMap<UnitsFrequency, Conversion>(UnitsFrequency.class);
        c.put(UnitsFrequency.ATTOHERTZ, Mul(Pow(10, 15), Sym("millihz")));
        c.put(UnitsFrequency.CENTIHERTZ, Mul(Rat(Int(1), Int(10)), Sym("millihz")));
        c.put(UnitsFrequency.DECAHERTZ, Mul(Rat(Int(1), Pow(10, 4)), Sym("millihz")));
        c.put(UnitsFrequency.DECIHERTZ, Mul(Rat(Int(1), Int(100)), Sym("millihz")));
        c.put(UnitsFrequency.EXAHERTZ, Mul(Rat(Int(1), Pow(10, 21)), Sym("millihz")));
        c.put(UnitsFrequency.FEMTOHERTZ, Mul(Pow(10, 12), Sym("millihz")));
        c.put(UnitsFrequency.GIGAHERTZ, Mul(Rat(Int(1), Pow(10, 12)), Sym("millihz")));
        c.put(UnitsFrequency.HECTOHERTZ, Mul(Rat(Int(1), Pow(10, 5)), Sym("millihz")));
        c.put(UnitsFrequency.HERTZ, Mul(Rat(Int(1), Int(1000)), Sym("millihz")));
        c.put(UnitsFrequency.KILOHERTZ, Mul(Rat(Int(1), Pow(10, 6)), Sym("millihz")));
        c.put(UnitsFrequency.MEGAHERTZ, Mul(Rat(Int(1), Pow(10, 9)), Sym("millihz")));
        c.put(UnitsFrequency.MICROHERTZ, Mul(Int(1000), Sym("millihz")));
        c.put(UnitsFrequency.NANOHERTZ, Mul(Pow(10, 6), Sym("millihz")));
        c.put(UnitsFrequency.PETAHERTZ, Mul(Rat(Int(1), Pow(10, 18)), Sym("millihz")));
        c.put(UnitsFrequency.PICOHERTZ, Mul(Pow(10, 9), Sym("millihz")));
        c.put(UnitsFrequency.TERAHERTZ, Mul(Rat(Int(1), Pow(10, 15)), Sym("millihz")));
        c.put(UnitsFrequency.YOCTOHERTZ, Mul(Pow(10, 21), Sym("millihz")));
        c.put(UnitsFrequency.YOTTAHERTZ, Mul(Rat(Int(1), Pow(10, 27)), Sym("millihz")));
        c.put(UnitsFrequency.ZEPTOHERTZ, Mul(Pow(10, 18), Sym("millihz")));
        c.put(UnitsFrequency.ZETTAHERTZ, Mul(Rat(Int(1), Pow(10, 24)), Sym("millihz")));
        return Collections.unmodifiableMap(c);
    }

    private static Map<UnitsFrequency, Conversion> createMapNANOHERTZ() {
        EnumMap<UnitsFrequency, Conversion> c =
            new EnumMap<UnitsFrequency, Conversion>(UnitsFrequency.class);
        c.put(UnitsFrequency.ATTOHERTZ, Mul(Pow(10, 9), Sym("nanohz")));
        c.put(UnitsFrequency.CENTIHERTZ, Mul(Rat(Int(1), Pow(10, 7)), Sym("nanohz")));
        c.put(UnitsFrequency.DECAHERTZ, Mul(Rat(Int(1), Pow(10, 10)), Sym("nanohz")));
        c.put(UnitsFrequency.DECIHERTZ, Mul(Rat(Int(1), Pow(10, 8)), Sym("nanohz")));
        c.put(UnitsFrequency.EXAHERTZ, Mul(Rat(Int(1), Pow(10, 27)), Sym("nanohz")));
        c.put(UnitsFrequency.FEMTOHERTZ, Mul(Pow(10, 6), Sym("nanohz")));
        c.put(UnitsFrequency.GIGAHERTZ, Mul(Rat(Int(1), Pow(10, 18)), Sym("nanohz")));
        c.put(UnitsFrequency.HECTOHERTZ, Mul(Rat(Int(1), Pow(10, 11)), Sym("nanohz")));
        c.put(UnitsFrequency.HERTZ, Mul(Rat(Int(1), Pow(10, 9)), Sym("nanohz")));
        c.put(UnitsFrequency.KILOHERTZ, Mul(Rat(Int(1), Pow(10, 12)), Sym("nanohz")));
        c.put(UnitsFrequency.MEGAHERTZ, Mul(Rat(Int(1), Pow(10, 15)), Sym("nanohz")));
        c.put(UnitsFrequency.MICROHERTZ, Mul(Rat(Int(1), Int(1000)), Sym("nanohz")));
        c.put(UnitsFrequency.MILLIHERTZ, Mul(Rat(Int(1), Pow(10, 6)), Sym("nanohz")));
        c.put(UnitsFrequency.PETAHERTZ, Mul(Rat(Int(1), Pow(10, 24)), Sym("nanohz")));
        c.put(UnitsFrequency.PICOHERTZ, Mul(Int(1000), Sym("nanohz")));
        c.put(UnitsFrequency.TERAHERTZ, Mul(Rat(Int(1), Pow(10, 21)), Sym("nanohz")));
        c.put(UnitsFrequency.YOCTOHERTZ, Mul(Pow(10, 15), Sym("nanohz")));
        c.put(UnitsFrequency.YOTTAHERTZ, Mul(Rat(Int(1), Pow(10, 33)), Sym("nanohz")));
        c.put(UnitsFrequency.ZEPTOHERTZ, Mul(Pow(10, 12), Sym("nanohz")));
        c.put(UnitsFrequency.ZETTAHERTZ, Mul(Rat(Int(1), Pow(10, 30)), Sym("nanohz")));
        return Collections.unmodifiableMap(c);
    }

    private static Map<UnitsFrequency, Conversion> createMapPETAHERTZ() {
        EnumMap<UnitsFrequency, Conversion> c =
            new EnumMap<UnitsFrequency, Conversion>(UnitsFrequency.class);
        c.put(UnitsFrequency.ATTOHERTZ, Mul(Pow(10, 33), Sym("petahz")));
        c.put(UnitsFrequency.CENTIHERTZ, Mul(Pow(10, 17), Sym("petahz")));
        c.put(UnitsFrequency.DECAHERTZ, Mul(Pow(10, 14), Sym("petahz")));
        c.put(UnitsFrequency.DECIHERTZ, Mul(Pow(10, 16), Sym("petahz")));
        c.put(UnitsFrequency.EXAHERTZ, Mul(Rat(Int(1), Int(1000)), Sym("petahz")));
        c.put(UnitsFrequency.FEMTOHERTZ, Mul(Pow(10, 30), Sym("petahz")));
        c.put(UnitsFrequency.GIGAHERTZ, Mul(Pow(10, 6), Sym("petahz")));
        c.put(UnitsFrequency.HECTOHERTZ, Mul(Pow(10, 13), Sym("petahz")));
        c.put(UnitsFrequency.HERTZ, Mul(Pow(10, 15), Sym("petahz")));
        c.put(UnitsFrequency.KILOHERTZ, Mul(Pow(10, 12), Sym("petahz")));
        c.put(UnitsFrequency.MEGAHERTZ, Mul(Pow(10, 9), Sym("petahz")));
        c.put(UnitsFrequency.MICROHERTZ, Mul(Pow(10, 21), Sym("petahz")));
        c.put(UnitsFrequency.MILLIHERTZ, Mul(Pow(10, 18), Sym("petahz")));
        c.put(UnitsFrequency.NANOHERTZ, Mul(Pow(10, 24), Sym("petahz")));
        c.put(UnitsFrequency.PICOHERTZ, Mul(Pow(10, 27), Sym("petahz")));
        c.put(UnitsFrequency.TERAHERTZ, Mul(Int(1000), Sym("petahz")));
        c.put(UnitsFrequency.YOCTOHERTZ, Mul(Pow(10, 39), Sym("petahz")));
        c.put(UnitsFrequency.YOTTAHERTZ, Mul(Rat(Int(1), Pow(10, 9)), Sym("petahz")));
        c.put(UnitsFrequency.ZEPTOHERTZ, Mul(Pow(10, 36), Sym("petahz")));
        c.put(UnitsFrequency.ZETTAHERTZ, Mul(Rat(Int(1), Pow(10, 6)), Sym("petahz")));
        return Collections.unmodifiableMap(c);
    }

    private static Map<UnitsFrequency, Conversion> createMapPICOHERTZ() {
        EnumMap<UnitsFrequency, Conversion> c =
            new EnumMap<UnitsFrequency, Conversion>(UnitsFrequency.class);
        c.put(UnitsFrequency.ATTOHERTZ, Mul(Pow(10, 6), Sym("picohz")));
        c.put(UnitsFrequency.CENTIHERTZ, Mul(Rat(Int(1), Pow(10, 10)), Sym("picohz")));
        c.put(UnitsFrequency.DECAHERTZ, Mul(Rat(Int(1), Pow(10, 13)), Sym("picohz")));
        c.put(UnitsFrequency.DECIHERTZ, Mul(Rat(Int(1), Pow(10, 11)), Sym("picohz")));
        c.put(UnitsFrequency.EXAHERTZ, Mul(Rat(Int(1), Pow(10, 30)), Sym("picohz")));
        c.put(UnitsFrequency.FEMTOHERTZ, Mul(Int(1000), Sym("picohz")));
        c.put(UnitsFrequency.GIGAHERTZ, Mul(Rat(Int(1), Pow(10, 21)), Sym("picohz")));
        c.put(UnitsFrequency.HECTOHERTZ, Mul(Rat(Int(1), Pow(10, 14)), Sym("picohz")));
        c.put(UnitsFrequency.HERTZ, Mul(Rat(Int(1), Pow(10, 12)), Sym("picohz")));
        c.put(UnitsFrequency.KILOHERTZ, Mul(Rat(Int(1), Pow(10, 15)), Sym("picohz")));
        c.put(UnitsFrequency.MEGAHERTZ, Mul(Rat(Int(1), Pow(10, 18)), Sym("picohz")));
        c.put(UnitsFrequency.MICROHERTZ, Mul(Rat(Int(1), Pow(10, 6)), Sym("picohz")));
        c.put(UnitsFrequency.MILLIHERTZ, Mul(Rat(Int(1), Pow(10, 9)), Sym("picohz")));
        c.put(UnitsFrequency.NANOHERTZ, Mul(Rat(Int(1), Int(1000)), Sym("picohz")));
        c.put(UnitsFrequency.PETAHERTZ, Mul(Rat(Int(1), Pow(10, 27)), Sym("picohz")));
        c.put(UnitsFrequency.TERAHERTZ, Mul(Rat(Int(1), Pow(10, 24)), Sym("picohz")));
        c.put(UnitsFrequency.YOCTOHERTZ, Mul(Pow(10, 12), Sym("picohz")));
        c.put(UnitsFrequency.YOTTAHERTZ, Mul(Rat(Int(1), Pow(10, 36)), Sym("picohz")));
        c.put(UnitsFrequency.ZEPTOHERTZ, Mul(Pow(10, 9), Sym("picohz")));
        c.put(UnitsFrequency.ZETTAHERTZ, Mul(Rat(Int(1), Pow(10, 33)), Sym("picohz")));
        return Collections.unmodifiableMap(c);
    }

    private static Map<UnitsFrequency, Conversion> createMapTERAHERTZ() {
        EnumMap<UnitsFrequency, Conversion> c =
            new EnumMap<UnitsFrequency, Conversion>(UnitsFrequency.class);
        c.put(UnitsFrequency.ATTOHERTZ, Mul(Pow(10, 30), Sym("terahz")));
        c.put(UnitsFrequency.CENTIHERTZ, Mul(Pow(10, 14), Sym("terahz")));
        c.put(UnitsFrequency.DECAHERTZ, Mul(Pow(10, 11), Sym("terahz")));
        c.put(UnitsFrequency.DECIHERTZ, Mul(Pow(10, 13), Sym("terahz")));
        c.put(UnitsFrequency.EXAHERTZ, Mul(Rat(Int(1), Pow(10, 6)), Sym("terahz")));
        c.put(UnitsFrequency.FEMTOHERTZ, Mul(Pow(10, 27), Sym("terahz")));
        c.put(UnitsFrequency.GIGAHERTZ, Mul(Int(1000), Sym("terahz")));
        c.put(UnitsFrequency.HECTOHERTZ, Mul(Pow(10, 10), Sym("terahz")));
        c.put(UnitsFrequency.HERTZ, Mul(Pow(10, 12), Sym("terahz")));
        c.put(UnitsFrequency.KILOHERTZ, Mul(Pow(10, 9), Sym("terahz")));
        c.put(UnitsFrequency.MEGAHERTZ, Mul(Pow(10, 6), Sym("terahz")));
        c.put(UnitsFrequency.MICROHERTZ, Mul(Pow(10, 18), Sym("terahz")));
        c.put(UnitsFrequency.MILLIHERTZ, Mul(Pow(10, 15), Sym("terahz")));
        c.put(UnitsFrequency.NANOHERTZ, Mul(Pow(10, 21), Sym("terahz")));
        c.put(UnitsFrequency.PETAHERTZ, Mul(Rat(Int(1), Int(1000)), Sym("terahz")));
        c.put(UnitsFrequency.PICOHERTZ, Mul(Pow(10, 24), Sym("terahz")));
        c.put(UnitsFrequency.YOCTOHERTZ, Mul(Pow(10, 36), Sym("terahz")));
        c.put(UnitsFrequency.YOTTAHERTZ, Mul(Rat(Int(1), Pow(10, 12)), Sym("terahz")));
        c.put(UnitsFrequency.ZEPTOHERTZ, Mul(Pow(10, 33), Sym("terahz")));
        c.put(UnitsFrequency.ZETTAHERTZ, Mul(Rat(Int(1), Pow(10, 9)), Sym("terahz")));
        return Collections.unmodifiableMap(c);
    }

    private static Map<UnitsFrequency, Conversion> createMapYOCTOHERTZ() {
        EnumMap<UnitsFrequency, Conversion> c =
            new EnumMap<UnitsFrequency, Conversion>(UnitsFrequency.class);
        c.put(UnitsFrequency.ATTOHERTZ, Mul(Rat(Int(1), Pow(10, 6)), Sym("yoctohz")));
        c.put(UnitsFrequency.CENTIHERTZ, Mul(Rat(Int(1), Pow(10, 22)), Sym("yoctohz")));
        c.put(UnitsFrequency.DECAHERTZ, Mul(Rat(Int(1), Pow(10, 25)), Sym("yoctohz")));
        c.put(UnitsFrequency.DECIHERTZ, Mul(Rat(Int(1), Pow(10, 23)), Sym("yoctohz")));
        c.put(UnitsFrequency.EXAHERTZ, Mul(Rat(Int(1), Pow(10, 42)), Sym("yoctohz")));
        c.put(UnitsFrequency.FEMTOHERTZ, Mul(Rat(Int(1), Pow(10, 9)), Sym("yoctohz")));
        c.put(UnitsFrequency.GIGAHERTZ, Mul(Rat(Int(1), Pow(10, 33)), Sym("yoctohz")));
        c.put(UnitsFrequency.HECTOHERTZ, Mul(Rat(Int(1), Pow(10, 26)), Sym("yoctohz")));
        c.put(UnitsFrequency.HERTZ, Mul(Rat(Int(1), Pow(10, 24)), Sym("yoctohz")));
        c.put(UnitsFrequency.KILOHERTZ, Mul(Rat(Int(1), Pow(10, 27)), Sym("yoctohz")));
        c.put(UnitsFrequency.MEGAHERTZ, Mul(Rat(Int(1), Pow(10, 30)), Sym("yoctohz")));
        c.put(UnitsFrequency.MICROHERTZ, Mul(Rat(Int(1), Pow(10, 18)), Sym("yoctohz")));
        c.put(UnitsFrequency.MILLIHERTZ, Mul(Rat(Int(1), Pow(10, 21)), Sym("yoctohz")));
        c.put(UnitsFrequency.NANOHERTZ, Mul(Rat(Int(1), Pow(10, 15)), Sym("yoctohz")));
        c.put(UnitsFrequency.PETAHERTZ, Mul(Rat(Int(1), Pow(10, 39)), Sym("yoctohz")));
        c.put(UnitsFrequency.PICOHERTZ, Mul(Rat(Int(1), Pow(10, 12)), Sym("yoctohz")));
        c.put(UnitsFrequency.TERAHERTZ, Mul(Rat(Int(1), Pow(10, 36)), Sym("yoctohz")));
        c.put(UnitsFrequency.YOTTAHERTZ, Mul(Rat(Int(1), Pow(10, 48)), Sym("yoctohz")));
        c.put(UnitsFrequency.ZEPTOHERTZ, Mul(Rat(Int(1), Int(1000)), Sym("yoctohz")));
        c.put(UnitsFrequency.ZETTAHERTZ, Mul(Rat(Int(1), Pow(10, 45)), Sym("yoctohz")));
        return Collections.unmodifiableMap(c);
    }

    private static Map<UnitsFrequency, Conversion> createMapYOTTAHERTZ() {
        EnumMap<UnitsFrequency, Conversion> c =
            new EnumMap<UnitsFrequency, Conversion>(UnitsFrequency.class);
        c.put(UnitsFrequency.ATTOHERTZ, Mul(Pow(10, 42), Sym("yottahz")));
        c.put(UnitsFrequency.CENTIHERTZ, Mul(Pow(10, 26), Sym("yottahz")));
        c.put(UnitsFrequency.DECAHERTZ, Mul(Pow(10, 23), Sym("yottahz")));
        c.put(UnitsFrequency.DECIHERTZ, Mul(Pow(10, 25), Sym("yottahz")));
        c.put(UnitsFrequency.EXAHERTZ, Mul(Pow(10, 6), Sym("yottahz")));
        c.put(UnitsFrequency.FEMTOHERTZ, Mul(Pow(10, 39), Sym("yottahz")));
        c.put(UnitsFrequency.GIGAHERTZ, Mul(Pow(10, 15), Sym("yottahz")));
        c.put(UnitsFrequency.HECTOHERTZ, Mul(Pow(10, 22), Sym("yottahz")));
        c.put(UnitsFrequency.HERTZ, Mul(Pow(10, 24), Sym("yottahz")));
        c.put(UnitsFrequency.KILOHERTZ, Mul(Pow(10, 21), Sym("yottahz")));
        c.put(UnitsFrequency.MEGAHERTZ, Mul(Pow(10, 18), Sym("yottahz")));
        c.put(UnitsFrequency.MICROHERTZ, Mul(Pow(10, 30), Sym("yottahz")));
        c.put(UnitsFrequency.MILLIHERTZ, Mul(Pow(10, 27), Sym("yottahz")));
        c.put(UnitsFrequency.NANOHERTZ, Mul(Pow(10, 33), Sym("yottahz")));
        c.put(UnitsFrequency.PETAHERTZ, Mul(Pow(10, 9), Sym("yottahz")));
        c.put(UnitsFrequency.PICOHERTZ, Mul(Pow(10, 36), Sym("yottahz")));
        c.put(UnitsFrequency.TERAHERTZ, Mul(Pow(10, 12), Sym("yottahz")));
        c.put(UnitsFrequency.YOCTOHERTZ, Mul(Pow(10, 48), Sym("yottahz")));
        c.put(UnitsFrequency.ZEPTOHERTZ, Mul(Pow(10, 45), Sym("yottahz")));
        c.put(UnitsFrequency.ZETTAHERTZ, Mul(Int(1000), Sym("yottahz")));
        return Collections.unmodifiableMap(c);
    }

    private static Map<UnitsFrequency, Conversion> createMapZEPTOHERTZ() {
        EnumMap<UnitsFrequency, Conversion> c =
            new EnumMap<UnitsFrequency, Conversion>(UnitsFrequency.class);
        c.put(UnitsFrequency.ATTOHERTZ, Mul(Rat(Int(1), Int(1000)), Sym("zeptohz")));
        c.put(UnitsFrequency.CENTIHERTZ, Mul(Rat(Int(1), Pow(10, 19)), Sym("zeptohz")));
        c.put(UnitsFrequency.DECAHERTZ, Mul(Rat(Int(1), Pow(10, 22)), Sym("zeptohz")));
        c.put(UnitsFrequency.DECIHERTZ, Mul(Rat(Int(1), Pow(10, 20)), Sym("zeptohz")));
        c.put(UnitsFrequency.EXAHERTZ, Mul(Rat(Int(1), Pow(10, 39)), Sym("zeptohz")));
        c.put(UnitsFrequency.FEMTOHERTZ, Mul(Rat(Int(1), Pow(10, 6)), Sym("zeptohz")));
        c.put(UnitsFrequency.GIGAHERTZ, Mul(Rat(Int(1), Pow(10, 30)), Sym("zeptohz")));
        c.put(UnitsFrequency.HECTOHERTZ, Mul(Rat(Int(1), Pow(10, 23)), Sym("zeptohz")));
        c.put(UnitsFrequency.HERTZ, Mul(Rat(Int(1), Pow(10, 21)), Sym("zeptohz")));
        c.put(UnitsFrequency.KILOHERTZ, Mul(Rat(Int(1), Pow(10, 24)), Sym("zeptohz")));
        c.put(UnitsFrequency.MEGAHERTZ, Mul(Rat(Int(1), Pow(10, 27)), Sym("zeptohz")));
        c.put(UnitsFrequency.MICROHERTZ, Mul(Rat(Int(1), Pow(10, 15)), Sym("zeptohz")));
        c.put(UnitsFrequency.MILLIHERTZ, Mul(Rat(Int(1), Pow(10, 18)), Sym("zeptohz")));
        c.put(UnitsFrequency.NANOHERTZ, Mul(Rat(Int(1), Pow(10, 12)), Sym("zeptohz")));
        c.put(UnitsFrequency.PETAHERTZ, Mul(Rat(Int(1), Pow(10, 36)), Sym("zeptohz")));
        c.put(UnitsFrequency.PICOHERTZ, Mul(Rat(Int(1), Pow(10, 9)), Sym("zeptohz")));
        c.put(UnitsFrequency.TERAHERTZ, Mul(Rat(Int(1), Pow(10, 33)), Sym("zeptohz")));
        c.put(UnitsFrequency.YOCTOHERTZ, Mul(Int(1000), Sym("zeptohz")));
        c.put(UnitsFrequency.YOTTAHERTZ, Mul(Rat(Int(1), Pow(10, 45)), Sym("zeptohz")));
        c.put(UnitsFrequency.ZETTAHERTZ, Mul(Rat(Int(1), Pow(10, 42)), Sym("zeptohz")));
        return Collections.unmodifiableMap(c);
    }

    private static Map<UnitsFrequency, Conversion> createMapZETTAHERTZ() {
        EnumMap<UnitsFrequency, Conversion> c =
            new EnumMap<UnitsFrequency, Conversion>(UnitsFrequency.class);
        c.put(UnitsFrequency.ATTOHERTZ, Mul(Pow(10, 39), Sym("zettahz")));
        c.put(UnitsFrequency.CENTIHERTZ, Mul(Pow(10, 23), Sym("zettahz")));
        c.put(UnitsFrequency.DECAHERTZ, Mul(Pow(10, 20), Sym("zettahz")));
        c.put(UnitsFrequency.DECIHERTZ, Mul(Pow(10, 22), Sym("zettahz")));
        c.put(UnitsFrequency.EXAHERTZ, Mul(Int(1000), Sym("zettahz")));
        c.put(UnitsFrequency.FEMTOHERTZ, Mul(Pow(10, 36), Sym("zettahz")));
        c.put(UnitsFrequency.GIGAHERTZ, Mul(Pow(10, 12), Sym("zettahz")));
        c.put(UnitsFrequency.HECTOHERTZ, Mul(Pow(10, 19), Sym("zettahz")));
        c.put(UnitsFrequency.HERTZ, Mul(Pow(10, 21), Sym("zettahz")));
        c.put(UnitsFrequency.KILOHERTZ, Mul(Pow(10, 18), Sym("zettahz")));
        c.put(UnitsFrequency.MEGAHERTZ, Mul(Pow(10, 15), Sym("zettahz")));
        c.put(UnitsFrequency.MICROHERTZ, Mul(Pow(10, 27), Sym("zettahz")));
        c.put(UnitsFrequency.MILLIHERTZ, Mul(Pow(10, 24), Sym("zettahz")));
        c.put(UnitsFrequency.NANOHERTZ, Mul(Pow(10, 30), Sym("zettahz")));
        c.put(UnitsFrequency.PETAHERTZ, Mul(Pow(10, 6), Sym("zettahz")));
        c.put(UnitsFrequency.PICOHERTZ, Mul(Pow(10, 33), Sym("zettahz")));
        c.put(UnitsFrequency.TERAHERTZ, Mul(Pow(10, 9), Sym("zettahz")));
        c.put(UnitsFrequency.YOCTOHERTZ, Mul(Pow(10, 45), Sym("zettahz")));
        c.put(UnitsFrequency.YOTTAHERTZ, Mul(Rat(Int(1), Int(1000)), Sym("zettahz")));
        c.put(UnitsFrequency.ZEPTOHERTZ, Mul(Pow(10, 42), Sym("zettahz")));
        return Collections.unmodifiableMap(c);
    }

    private static final Map<UnitsFrequency, Map<UnitsFrequency, Conversion>> conversions;
    static {

        Map<UnitsFrequency, Map<UnitsFrequency, Conversion>> c
            = new EnumMap<UnitsFrequency, Map<UnitsFrequency, Conversion>>(UnitsFrequency.class);

        c.put(UnitsFrequency.ATTOHERTZ, createMapATTOHERTZ());
        c.put(UnitsFrequency.CENTIHERTZ, createMapCENTIHERTZ());
        c.put(UnitsFrequency.DECAHERTZ, createMapDECAHERTZ());
        c.put(UnitsFrequency.DECIHERTZ, createMapDECIHERTZ());
        c.put(UnitsFrequency.EXAHERTZ, createMapEXAHERTZ());
        c.put(UnitsFrequency.FEMTOHERTZ, createMapFEMTOHERTZ());
        c.put(UnitsFrequency.GIGAHERTZ, createMapGIGAHERTZ());
        c.put(UnitsFrequency.HECTOHERTZ, createMapHECTOHERTZ());
        c.put(UnitsFrequency.HERTZ, createMapHERTZ());
        c.put(UnitsFrequency.KILOHERTZ, createMapKILOHERTZ());
        c.put(UnitsFrequency.MEGAHERTZ, createMapMEGAHERTZ());
        c.put(UnitsFrequency.MICROHERTZ, createMapMICROHERTZ());
        c.put(UnitsFrequency.MILLIHERTZ, createMapMILLIHERTZ());
        c.put(UnitsFrequency.NANOHERTZ, createMapNANOHERTZ());
        c.put(UnitsFrequency.PETAHERTZ, createMapPETAHERTZ());
        c.put(UnitsFrequency.PICOHERTZ, createMapPICOHERTZ());
        c.put(UnitsFrequency.TERAHERTZ, createMapTERAHERTZ());
        c.put(UnitsFrequency.YOCTOHERTZ, createMapYOCTOHERTZ());
        c.put(UnitsFrequency.YOTTAHERTZ, createMapYOTTAHERTZ());
        c.put(UnitsFrequency.ZEPTOHERTZ, createMapZEPTOHERTZ());
        c.put(UnitsFrequency.ZETTAHERTZ, createMapZETTAHERTZ());
        conversions = Collections.unmodifiableMap(c);
    }

    private static final Map<UnitsFrequency, String> SYMBOLS;
    static {
        Map<UnitsFrequency, String> s = new HashMap<UnitsFrequency, String>();
        s.put(UnitsFrequency.ATTOHERTZ, "aHz");
        s.put(UnitsFrequency.CENTIHERTZ, "cHz");
        s.put(UnitsFrequency.DECAHERTZ, "daHz");
        s.put(UnitsFrequency.DECIHERTZ, "dHz");
        s.put(UnitsFrequency.EXAHERTZ, "EHz");
        s.put(UnitsFrequency.FEMTOHERTZ, "fHz");
        s.put(UnitsFrequency.GIGAHERTZ, "GHz");
        s.put(UnitsFrequency.HECTOHERTZ, "hHz");
        s.put(UnitsFrequency.HERTZ, "Hz");
        s.put(UnitsFrequency.KILOHERTZ, "kHz");
        s.put(UnitsFrequency.MEGAHERTZ, "MHz");
        s.put(UnitsFrequency.MICROHERTZ, "µHz");
        s.put(UnitsFrequency.MILLIHERTZ, "mHz");
        s.put(UnitsFrequency.NANOHERTZ, "nHz");
        s.put(UnitsFrequency.PETAHERTZ, "PHz");
        s.put(UnitsFrequency.PICOHERTZ, "pHz");
        s.put(UnitsFrequency.TERAHERTZ, "THz");
        s.put(UnitsFrequency.YOCTOHERTZ, "yHz");
        s.put(UnitsFrequency.YOTTAHERTZ, "YHz");
        s.put(UnitsFrequency.ZEPTOHERTZ, "zHz");
        s.put(UnitsFrequency.ZETTAHERTZ, "ZHz");
        SYMBOLS = s;
    }

    public static String lookupSymbol(UnitsFrequency unit) {
        return SYMBOLS.get(unit);
    }

    public static final Ice.ObjectFactory makeFactory(final ode.client client) {

        return new Ice.ObjectFactory() {

            public Ice.Object create(String arg0) {
                return new FrequencyI();
            }

            public void destroy() {
                // no-op
            }

        };
    };

    //
    // CONVERSIONS
    //

    public static ode.xml.model.enums.UnitsFrequency makeXMLUnit(String unit) {
        try {
            return ode.xml.model.enums.UnitsFrequency
                    .fromString((String) unit);
        } catch (EnumerationException e) {
            throw new RuntimeException("Bad Frequency unit: " + unit, e);
        }
    }

    public static ode.units.quantity.Frequency makeXMLQuantity(double d, String unit) {
        ode.units.unit.Unit<ode.units.quantity.Frequency> units =
                ode.xml.model.enums.handlers.UnitsFrequencyEnumHandler
                        .getBaseUnit(makeXMLUnit(unit));
        return new ode.units.quantity.Frequency(d, units);
    }

   /**
    * FIXME: this should likely take a default so that locations which don't
    * want an exception can have
    *
    * log.warn("Using new PositiveFloat(1.0)!", e); return new
    * PositiveFloat(1.0);
    *
    * or similar.
    */
   public static ode.units.quantity.Frequency convert(Frequency t) {
       if (t == null) {
           return null;
       }

       Double v = t.getValue();
       // Use the code/symbol-mapping in the ode.model.enums files
       // to convert to the specification value.
       String u = ode.model.enums.UnitsFrequency.valueOf(
               t.getUnit().toString()).getSymbol();
       ode.xml.model.enums.UnitsFrequency units = makeXMLUnit(u);
       ode.units.unit.Unit<ode.units.quantity.Frequency> units2 =
               ode.xml.model.enums.handlers.UnitsFrequencyEnumHandler
                       .getBaseUnit(units);

       return new ode.units.quantity.Frequency(v, units2);
   }


    //
    // REGULAR ICE CLASS
    //

    public final static Ice.ObjectFactory Factory = makeFactory(null);

    public FrequencyI() {
        super();
    }

    public FrequencyI(double d, UnitsFrequency unit) {
        super();
        this.setUnit(unit);
        this.setValue(d);
    }

    public FrequencyI(double d,
            Unit<ode.units.quantity.Frequency> unit) {
        this(d, ode.model.enums.UnitsFrequency.bySymbol(unit.getSymbol()));
    }

   /**
    * Copy constructor that converts the given {@link ode.model.Frequency}
    * based on the given ode-xml enum
    */
   public FrequencyI(Frequency value, Unit<ode.units.quantity.Frequency> ul) throws BigResult {
       this(value,
            ode.model.enums.UnitsFrequency.bySymbol(ul.getSymbol()).toString());
   }

   /**
    * Copy constructor that converts the given {@link ode.model.Frequency}
    * based on the given ode.model enum
    */
   public FrequencyI(double d, ode.model.enums.UnitsFrequency ul) {
        this(d, UnitsFrequency.valueOf(ul.toString()));
    }

   /**
    * Copy constructor that converts the given {@link ode.model.Frequency}
    * based on the given enum string.
    *
    * If either the source or the target unit is null or if no conversion
    * is possible between the two types (e.g. PIXELS), an
    * {@link IllegalArgumentException} will be thrown. If the conversion
    * results in an overflow, a {@link BigResult} will be thrown, unless
    * the input value was already Infinite or NaN, in which case that will
    * be the return value.
    *
    * @param target String representation of the CODE enum
    * @throws IllegalArgumentException if the source or target unit
    *         is null or an unconvertible type (e.g. PIXELS)
    * @throws BigResult if the conversion leads to an infinite or NaN result
    */
    public FrequencyI(Frequency value, String target) throws BigResult {

       final UnitsFrequency sourceUnit = value.getUnit();
       final UnitsFrequency targetUnit = UnitsFrequency.valueOf(target);
       if (sourceUnit == null || targetUnit == null) {
           throw new IllegalArgumentException(String.format(
                       "conversion impossible from %s to %s",
                       sourceUnit, targetUnit));
       }

       final String source = value.getUnit().toString();
       if (target.equals(source)) {
           setValue(value.getValue());
           setUnit(value.getUnit());
       } else {
            final Conversion conversion = conversions.get(sourceUnit).get(targetUnit);
            if (conversion == null) {
                throw new IllegalArgumentException(String.format(
                    "%f %s cannot be converted to %s",
                        value.getValue(), value.getUnit(), target));
            }
            double orig = value.getValue();
            double converted = orig;
            if (!Double.isFinite(orig)) {
                // Infinite or NaN
                // Do nothing. Use orig
            } else {
                BigDecimal big = conversion.convert(orig);
                converted = big.doubleValue();
                if (!Double.isFinite(converted)) {
                    throw new BigResult(big,
                            "Failed to convert " + source + ":" + target);
                }
            }
            setValue(converted);
            setUnit(targetUnit);
       }
    }

   /**
    * Copy constructor that converts between units if possible.
    *
    * @param target unit that is desired. non-null.
    */
    public FrequencyI(Frequency value, UnitsFrequency target) throws BigResult {
        this(value, target.toString());
    }

    /**
     * Convert a Bio-Formats {@link Length} to a Bhojpur ODE Length.
     */
    public FrequencyI(ode.units.quantity.Frequency value) {
        ode.model.enums.UnitsFrequency internal =
            ode.model.enums.UnitsFrequency.bySymbol(value.unit().getSymbol());
        UnitsFrequency ul = UnitsFrequency.valueOf(internal.toString());
        setValue(value.value().doubleValue());
        setUnit(ul);
    }

    public double getValue(Ice.Current current) {
        return this.value;
    }

    public void setValue(double value , Ice.Current current) {
        this.value = value;
    }

    public UnitsFrequency getUnit(Ice.Current current) {
        return this.unit;
    }

    public void setUnit(UnitsFrequency unit, Ice.Current current) {
        this.unit = unit;
    }

    public String getSymbol(Ice.Current current) {
        return SYMBOLS.get(this.unit);
    }

    public Frequency copy(Ice.Current ignore) {
        FrequencyI copy = new FrequencyI();
        copy.setValue(getValue());
        copy.setUnit(getUnit());
        return copy;
    }

    @Override
    public void copyObject(Filterable model, ModelMapper mapper) {
        if (model instanceof ode.model.units.Frequency) {
            ode.model.units.Frequency t = (ode.model.units.Frequency) model;
            this.value = t.getValue();
            this.unit = UnitsFrequency.valueOf(t.getUnit().toString());
        } else {
            throw new IllegalArgumentException(
              "Frequency cannot copy from " +
              (model==null ? "null" : model.getClass().getName()));
        }
    }

    @Override
    public Filterable fillObject(ReverseModelMapper mapper) {
        ode.model.enums.UnitsFrequency ut = ode.model.enums.UnitsFrequency.valueOf(getUnit().toString());
        ode.model.units.Frequency t = new ode.model.units.Frequency(getValue(), ut);
        return t;
    }

    // ~ Java overrides
    // =========================================================================

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((unit == null) ? 0 : unit.hashCode());
        long temp;
        temp = Double.doubleToLongBits(value);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Frequency(" + value + " " + unit + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Frequency other = (Frequency) obj;
        if (unit != other.unit)
            return false;
        if (Double.doubleToLongBits(value) != Double
                .doubleToLongBits(other.value))
            return false;
        return true;
    }

}
