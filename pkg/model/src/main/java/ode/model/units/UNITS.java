package ode.model.units;

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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import ode.model.enums.*;

public enum UNITS {

    ELECTRICPOTENTIAL(
            ElectricPotential.class,
            UnitsElectricPotential.class,
            3001),
    FREQUENCY(
            Frequency.class,
            UnitsFrequency.class,
            3002),
    LENGTH(
            Length.class,
            UnitsLength.class,
            3003),
    PRESSURE(
            Pressure.class,
            UnitsPressure.class,
            3004),
    POWER(
            Power.class,
            UnitsPower.class,
            3005),
    TEMPERATURE(
            Temperature.class,
            UnitsTemperature.class,
            3006),
    TIME(
            Time.class,
            UnitsTime.class,
            3007);

    Class<? extends Unit> quantityType;
    Class<? extends Enum<?>> enumType;
    int sqlType;
    Enum<? extends Enum<?>>[] values;
    Map<String, Enum<? extends Enum<?>>> enumMap;
    Map<Enum<? extends Enum<?>>, String> valueMap;

    UNITS(Class<? extends Unit> quantityType, Class<? extends Enum<?>> enumType, int sqlType) {
        this.quantityType = quantityType;
        this.enumType = enumType;
        this.sqlType = sqlType;
        this.values = enumType.getEnumConstants();
        enumMap = new HashMap<String, Enum<? extends Enum<?>>>();
        valueMap = new HashMap<Enum<? extends Enum<?>>, String>();
        try {
            Method m = this.enumType.getMethod("getSymbol");
            for (Enum<? extends Enum<?>> e : values) {
                String symbol = (String) m.invoke(e);
                enumMap.put(symbol, e);
                valueMap.put(e, symbol);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse units", e);
        }
    }

    public static Map<String, Integer> listSqlTypes() {
        try {
            Map<String, Integer> rv = new HashMap<String, Integer>();
            for (UNITS u : values()) {
                rv.put(u.enumType.getSimpleName(), u.sqlType);
            }
            return rv;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load sql types", e);
        }
    }

    /**
     * Map from the CODE-based enums which are used in Java, Ice, etc.
     * to the SYMBOL-based enum present in the DB which contain invalid
     * characters for most languages.
     *
     * @param obj can't be null
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String todbvalue(Object obj) {
        Enum e = Enum.valueOf((Class)enumType, obj.toString());
        return valueMap.get(e);
    }

    /**
     * Perform the reverse lookup from {@link #todbvalue(Object)} converting
     * the DB's enums which contain invalid characters to the upper-cased
     * CODE-based enums used elsewhere.
     */
    @SuppressWarnings("rawtypes")
    public Enum fromdbvalue(String obj) {
        return enumMap.get(obj);
    }

}