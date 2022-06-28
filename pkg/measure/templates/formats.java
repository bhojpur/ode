package ode.formats.model;

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

import ode.units.unit.Unit;
{% for name in sorted(items) %}\
import ode.model.${name};
import ode.model.${name}I;
{% end %}\
{% for name in sorted(items) %}\
import ode.model.enums.Units${name};
{% end %}

/**
 * Utility class to generate and convert unit objects.
 *
 * Be especially careful when using methods which take a string
 * since there are two types of enumerations, CODE-based and
 * SYMBOL-based.
 */
public class UnitsFactory {

{% for name in sorted(items) %}
    //
    // ${name}
    //

    public static ode.xml.model.enums.Units${name} make${name}UnitXML(String unit) {
        return ${name}I.makeXMLUnit(unit);
    }

    public static ode.units.quantity.${name} make${name}XML(double d, String unit) {
        return ${name}I.makeXMLQuantity(d, unit);
    }

    public static ${name} make${name}(double d,
            Unit<ode.units.quantity.${name}> unit) {
        return new ${name}I(d, unit);
    }

    public static ${name} make${name}(double d, Units${name} unit) {
        return new ${name}I(d, unit);
    }

    /**
     * Convert a Bio-Formats {@link ${name}} to a Bhojpur ODE ${name}. A null will be
     * returned if the input is null.
     */
    public static ${name} convert${name}(ode.units.quantity.${name} value) {
        if (value == null)
            return null;
        String internal = xml${name}EnumToODE(value.unit().getSymbol());
        Units${name} ul = Units${name}.valueOf(internal);
        return new ode.model.${name}I(value.value().doubleValue(), ul);
    }

    public static ode.units.quantity.${name} convert${name}(${name} t) {
        return ${name}I.convert(t);
    }

    public static ${name} convert${name}(${name} value, Unit<ode.units.quantity.${name}> ul) {
        return convert${name}XML(value, ul.getSymbol());
    }

    public static ${name} convert${name}XML(${name} value, String xml) {
        String ode = xml${name}EnumToODE(xml);
        return new ${name}I(value, ode);
    }

    public static String xml${name}EnumToODE(Unit<ode.units.quantity.${name}> xml) {
        return ode.model.enums.Units${name}.bySymbol(xml.getSymbol()).toString();
    }

    public static String xml${name}EnumToODE(String xml) {
        return ode.model.enums.Units${name}.bySymbol(xml).toString();
    }

{% end %}

{% for field in fields %}\
    public static Units${field.TYPE} ${field.CLASS}_${field.NAME} = Units${field.TYPE}.valueOf(xml${field.TYPE}EnumToODE(ode.xml.model.${field.CLASS}.get${field.NAME}UnitXsdDefault()));
{% end %}
}