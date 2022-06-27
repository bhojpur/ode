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

public enum UnitsTime implements UnitEnum {

    YOTTASECOND("Ys"),
    ZETTASECOND("Zs"),
    EXASECOND("Es"),
    PETASECOND("Ps"),
    TERASECOND("Ts"),
    GIGASECOND("Gs"),
    MEGASECOND("Ms"),
    KILOSECOND("ks"),
    HECTOSECOND("hs"),
    DECASECOND("das"),
    SECOND("s"),
    DECISECOND("ds"),
    CENTISECOND("cs"),
    MILLISECOND("ms"),
    MICROSECOND("µs"),
    NANOSECOND("ns"),
    PICOSECOND("ps"),
    FEMTOSECOND("fs"),
    ATTOSECOND("as"),
    ZEPTOSECOND("zs"),
    YOCTOSECOND("ys"),
    MINUTE("min"),
    HOUR("h"),
    DAY("d");

    private static final Map<String, UnitsTime> bySymbol
        = new HashMap<String, UnitsTime>();

    static {
        for (UnitsTime t : UnitsTime.values()) {
            bySymbol.put(t.symbol, t);
        }
    }

    protected String symbol;

    private UnitsTime(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static UnitsTime bySymbol(String symbol) {
        return bySymbol.get(symbol);
    }

};
