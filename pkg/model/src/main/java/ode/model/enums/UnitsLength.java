package ode.model.enums;

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

import ode.model.units.UnitEnum;

import java.util.HashMap;
import java.util.Map;

public enum UnitsLength implements UnitEnum {

    YOTTAMETER("Ym"),
    ZETTAMETER("Zm"),
    EXAMETER("Em"),
    PETAMETER("Pm"),
    TERAMETER("Tm"),
    GIGAMETER("Gm"),
    MEGAMETER("Mm"),
    KILOMETER("km"),
    HECTOMETER("hm"),
    DECAMETER("dam"),
    METER("m"),
    DECIMETER("dm"),
    CENTIMETER("cm"),
    MILLIMETER("mm"),
    MICROMETER("µm"),
    NANOMETER("nm"),
    PICOMETER("pm"),
    FEMTOMETER("fm"),
    ATTOMETER("am"),
    ZEPTOMETER("zm"),
    YOCTOMETER("ym"),
    ANGSTROM("Å"),
    ASTRONOMICALUNIT("ua"),
    LIGHTYEAR("ly"),
    PARSEC("pc"),
    THOU("thou"),
    LINE("li"),
    INCH("in"),
    FOOT("ft"),
    YARD("yd"),
    MILE("mi"),
    POINT("pt"),
    PIXEL("pixel"),
    REFERENCEFRAME("reference frame");

    private static final Map<String, UnitsLength> bySymbol
        = new HashMap<String, UnitsLength>();

    static {
        for (UnitsLength t : UnitsLength.values()) {
            bySymbol.put(t.symbol, t);
        }
    }

    protected String symbol;

    private UnitsLength(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static UnitsLength bySymbol(String symbol) {
        return bySymbol.get(symbol);
    }

};
