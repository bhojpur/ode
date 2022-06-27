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

public enum UnitsPressure implements UnitEnum {

    YOTTAPASCAL("YPa"),
    ZETTAPASCAL("ZPa"),
    EXAPASCAL("EPa"),
    PETAPASCAL("PPa"),
    TERAPASCAL("TPa"),
    GIGAPASCAL("GPa"),
    MEGAPASCAL("MPa"),
    KILOPASCAL("kPa"),
    HECTOPASCAL("hPa"),
    DECAPASCAL("daPa"),
    PASCAL("Pa"),
    DECIPASCAL("dPa"),
    CENTIPASCAL("cPa"),
    MILLIPASCAL("mPa"),
    MICROPASCAL("µPa"),
    NANOPASCAL("nPa"),
    PICOPASCAL("pPa"),
    FEMTOPASCAL("fPa"),
    ATTOPASCAL("aPa"),
    ZEPTOPASCAL("zPa"),
    YOCTOPASCAL("yPa"),
    BAR("bar"),
    MEGABAR("Mbar"),
    KILOBAR("kbar"),
    DECIBAR("dbar"),
    CENTIBAR("cbar"),
    MILLIBAR("mbar"),
    ATMOSPHERE("atm"),
    PSI("psi"),
    TORR("Torr"),
    MILLITORR("mTorr"),
    MMHG("mm Hg");

    private static final Map<String, UnitsPressure> bySymbol
        = new HashMap<String, UnitsPressure>();

    static {
        for (UnitsPressure t : UnitsPressure.values()) {
            bySymbol.put(t.symbol, t);
        }
    }

    protected String symbol;

    private UnitsPressure(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static UnitsPressure bySymbol(String symbol) {
        return bySymbol.get(symbol);
    }

};
