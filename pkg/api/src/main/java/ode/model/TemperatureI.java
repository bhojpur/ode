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

import ode.model.enums.UnitsTemperature;

/**
 * Bhojpur ODE server wrapper around the {@link ode.model.units.Temperature} class.
 * Like {@link Details} and {@link Permissions}, this object
 * is embedded into other objects and does not have a full life
 * cycle of its own.
 */
public class TemperatureI extends Temperature implements ModelBased {

    private static final long serialVersionUID = 1L;

    private static Map<UnitsTemperature, Conversion> createMapCELSIUS() {
        EnumMap<UnitsTemperature, Conversion> c =
            new EnumMap<UnitsTemperature, Conversion>(UnitsTemperature.class);
        c.put(UnitsTemperature.FAHRENHEIT, Add(Mul(Rat(Int(9), Int(5)), Sym("c")), Int(32)));
        c.put(UnitsTemperature.KELVIN, Add(Sym("c"), Rat(Int(5463), Int(20))));
        c.put(UnitsTemperature.RANKINE, Add(Mul(Rat(Int(9), Int(5)), Sym("c")), Rat(Int(49167), Int(100))));
        return Collections.unmodifiableMap(c);
    }

    private static Map<UnitsTemperature, Conversion> createMapFAHRENHEIT() {
        EnumMap<UnitsTemperature, Conversion> c =
            new EnumMap<UnitsTemperature, Conversion>(UnitsTemperature.class);
        c.put(UnitsTemperature.CELSIUS, Add(Mul(Rat(Int(5), Int(9)), Sym("f")), Rat(Int(-160), Int(9))));
        c.put(UnitsTemperature.KELVIN, Add(Mul(Rat(Int(5), Int(9)), Sym("f")), Rat(Int(45967), Int(180))));
        c.put(UnitsTemperature.RANKINE, Add(Sym("f"), Rat(Int(45967), Int(100))));
        return Collections.unmodifiableMap(c);
    }

    private static Map<UnitsTemperature, Conversion> createMapKELVIN() {
        EnumMap<UnitsTemperature, Conversion> c =
            new EnumMap<UnitsTemperature, Conversion>(UnitsTemperature.class);
        c.put(UnitsTemperature.CELSIUS, Add(Sym("k"), Rat(Int(-5463), Int(20))));
        c.put(UnitsTemperature.FAHRENHEIT, Add(Mul(Rat(Int(9), Int(5)), Sym("k")), Rat(Int(-45967), Int(100))));
        c.put(UnitsTemperature.RANKINE, Mul(Rat(Int(9), Int(5)), Sym("k")));
        return Collections.unmodifiableMap(c);
    }

    private static Map<UnitsTemperature, Conversion> createMapRANKINE() {
        EnumMap<UnitsTemperature, Conversion> c =
            new EnumMap<UnitsTemperature, Conversion>(UnitsTemperature.class);
        c.put(UnitsTemperature.CELSIUS, Add(Mul(Rat(Int(5), Int(9)), Sym("r")), Rat(Int(-5463), Int(20))));
        c.put(UnitsTemperature.FAHRENHEIT, Add(Sym("r"), Rat(Int(-45967), Int(100))));
        c.put(UnitsTemperature.KELVIN, Mul(Rat(Int(5), Int(9)), Sym("r")));
        return Collections.unmodifiableMap(c);
    }

    private static final Map<UnitsTemperature, Map<UnitsTemperature, Conversion>> conversions;
    static {

        Map<UnitsTemperature, Map<UnitsTemperature, Conversion>> c
            = new EnumMap<UnitsTemperature, Map<UnitsTemperature, Conversion>>(UnitsTemperature.class);

        c.put(UnitsTemperature.CELSIUS, createMapCELSIUS());
        c.put(UnitsTemperature.FAHRENHEIT, createMapFAHRENHEIT());
        c.put(UnitsTemperature.KELVIN, createMapKELVIN());
        c.put(UnitsTemperature.RANKINE, createMapRANKINE());
        conversions = Collections.unmodifiableMap(c);
    }

    private static final Map<UnitsTemperature, String> SYMBOLS;
    static {
        Map<UnitsTemperature, String> s = new HashMap<UnitsTemperature, String>();
        s.put(UnitsTemperature.CELSIUS, "°C");
        s.put(UnitsTemperature.FAHRENHEIT, "°F");
        s.put(UnitsTemperature.KELVIN, "K");
        s.put(UnitsTemperature.RANKINE, "°R");
        SYMBOLS = s;
    }

    public static String lookupSymbol(UnitsTemperature unit) {
        return SYMBOLS.get(unit);
    }

    public static final Ice.ObjectFactory makeFactory(final ode.client client) {

        return new Ice.ObjectFactory() {

            public Ice.Object create(String arg0) {
                return new TemperatureI();
            }

            public void destroy() {
                // no-op
            }

        };
    };

    //
    // CONVERSIONS
    //

    public static ode.xml.model.enums.UnitsTemperature makeXMLUnit(String unit) {
        try {
            return ode.xml.model.enums.UnitsTemperature
                    .fromString((String) unit);
        } catch (EnumerationException e) {
            throw new RuntimeException("Bad Temperature unit: " + unit, e);
        }
    }

    public static ode.units.quantity.Temperature makeXMLQuantity(double d, String unit) {
        ode.units.unit.Unit<ode.units.quantity.Temperature> units =
                ode.xml.model.enums.handlers.UnitsTemperatureEnumHandler
                        .getBaseUnit(makeXMLUnit(unit));
        return new ode.units.quantity.Temperature(d, units);
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
   public static ode.units.quantity.Temperature convert(Temperature t) {
       if (t == null) {
           return null;
       }

       Double v = t.getValue();
       // Use the code/symbol-mapping in the ode.model.enums files
       // to convert to the specification value.
       String u = ode.model.enums.UnitsTemperature.valueOf(
               t.getUnit().toString()).getSymbol();
       ode.xml.model.enums.UnitsTemperature units = makeXMLUnit(u);
       ode.units.unit.Unit<ode.units.quantity.Temperature> units2 =
               ode.xml.model.enums.handlers.UnitsTemperatureEnumHandler
                       .getBaseUnit(units);

       return new ode.units.quantity.Temperature(v, units2);
   }


    //
    // REGULAR ICE CLASS
    //

    public final static Ice.ObjectFactory Factory = makeFactory(null);

    public TemperatureI() {
        super();
    }

    public TemperatureI(double d, UnitsTemperature unit) {
        super();
        this.setUnit(unit);
        this.setValue(d);
    }

    public TemperatureI(double d,
            Unit<ode.units.quantity.Temperature> unit) {
        this(d, ode.model.enums.UnitsTemperature.bySymbol(unit.getSymbol()));
    }

   /**
    * Copy constructor that converts the given {@link ode.model.Temperature}
    * based on the given ode-xml enum
    */
   public TemperatureI(Temperature value, Unit<ode.units.quantity.Temperature> ul) throws BigResult {
       this(value,
            ode.model.enums.UnitsTemperature.bySymbol(ul.getSymbol()).toString());
   }

   /**
    * Copy constructor that converts the given {@link ode.model.Temperature}
    * based on the given ode.model enum
    */
   public TemperatureI(double d, ode.model.enums.UnitsTemperature ul) {
        this(d, UnitsTemperature.valueOf(ul.toString()));
    }

   /**
    * Copy constructor that converts the given {@link ode.model.Temperature}
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
    public TemperatureI(Temperature value, String target) throws BigResult {

       final UnitsTemperature sourceUnit = value.getUnit();
       final UnitsTemperature targetUnit = UnitsTemperature.valueOf(target);
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
    public TemperatureI(Temperature value, UnitsTemperature target) throws BigResult {
        this(value, target.toString());
    }

    /**
     * Convert a Bio-Formats {@link Length} to a Bhojpur ODE Length.
     */
    public TemperatureI(ode.units.quantity.Temperature value) {
        ode.model.enums.UnitsTemperature internal =
            ode.model.enums.UnitsTemperature.bySymbol(value.unit().getSymbol());
        UnitsTemperature ul = UnitsTemperature.valueOf(internal.toString());
        setValue(value.value().doubleValue());
        setUnit(ul);
    }

    public double getValue(Ice.Current current) {
        return this.value;
    }

    public void setValue(double value , Ice.Current current) {
        this.value = value;
    }

    public UnitsTemperature getUnit(Ice.Current current) {
        return this.unit;
    }

    public void setUnit(UnitsTemperature unit, Ice.Current current) {
        this.unit = unit;
    }

    public String getSymbol(Ice.Current current) {
        return SYMBOLS.get(this.unit);
    }

    public Temperature copy(Ice.Current ignore) {
        TemperatureI copy = new TemperatureI();
        copy.setValue(getValue());
        copy.setUnit(getUnit());
        return copy;
    }

    @Override
    public void copyObject(Filterable model, ModelMapper mapper) {
        if (model instanceof ode.model.units.Temperature) {
            ode.model.units.Temperature t = (ode.model.units.Temperature) model;
            this.value = t.getValue();
            this.unit = UnitsTemperature.valueOf(t.getUnit().toString());
        } else {
            throw new IllegalArgumentException(
              "Temperature cannot copy from " +
              (model==null ? "null" : model.getClass().getName()));
        }
    }

    @Override
    public Filterable fillObject(ReverseModelMapper mapper) {
        ode.model.enums.UnitsTemperature ut = ode.model.enums.UnitsTemperature.valueOf(getUnit().toString());
        ode.model.units.Temperature t = new ode.model.units.Temperature(getValue(), ut);
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
        return "Temperature(" + value + " " + unit + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Temperature other = (Temperature) obj;
        if (unit != other.unit)
            return false;
        if (Double.doubleToLongBits(value) != Double
                .doubleToLongBits(other.value))
            return false;
        return true;
    }

}
