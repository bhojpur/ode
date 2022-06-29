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

import ode.model.enums.Units${name};

/**
 * Bhojpur ODE core wrapper around the {@link ode.model.units.${name}} class.
 * Like {@link Details} and {@link Permissions}, this object
 * is embedded into other objects and does not have a full life
 * cycle of its own.m
 */
public class ${name}I extends ${name} implements ModelBased {

    private static final long serialVersionUID = 1L;

{% for cfrom in sorted(equations) %}\
    private static Map<Units${name}, Conversion> createMap${cfrom}() {
        EnumMap<Units${name}, Conversion> c =
            new EnumMap<Units${name}, Conversion>(Units${name}.class);
{% for cto, equation in sorted(equations.get(cfrom, {}).items()) %}\
{% if cfrom != cto %}\
        c.put(Units${name}.${cto}, ${equation});
{% end %}\
{% end %}\
        return Collections.unmodifiableMap(c);
    }

{% end %}\
    private static final Map<Units${name}, Map<Units${name}, Conversion>> conversions;
    static {

        Map<Units${name}, Map<Units${name}, Conversion>> c
            = new EnumMap<Units${name}, Map<Units${name}, Conversion>>(Units${name}.class);

{% for cfrom in sorted(equations) %}\
        c.put(Units${name}.${cfrom}, createMap${cfrom}());
{% end %}\
        conversions = Collections.unmodifiableMap(c);
    }

    private static final Map<Units${name}, String> SYMBOLS;
    static {
        Map<Units${name}, String> s = new HashMap<Units${name}, String>();
{% for x in sorted(items) %}\
        s.put(Units${name}.${x.CODE}, "${x.SYMBOL}");
{% end %}\
        SYMBOLS = s;
    }

    public static String lookupSymbol(Units${name} unit) {
        return SYMBOLS.get(unit);
    }

    public static final Ice.ObjectFactory makeFactory(final ode.client client) {

        return new Ice.ObjectFactory() {

            public Ice.Object create(String arg0) {
                return new ${name}I();
            }

            public void destroy() {
                // no-op
            }

        };
    };

    //
    // CONVERSIONS
    //

    public static ode.xml.model.enums.Units${name} makeXMLUnit(String unit) {
        try {
            return ode.xml.model.enums.Units${name}
                    .fromString((String) unit);
        } catch (EnumerationException e) {
            throw new RuntimeException("Bad ${name} unit: " + unit, e);
        }
    }

    public static ode.units.quantity.${name} makeXMLQuantity(double d, String unit) {
        ode.units.unit.Unit<ode.units.quantity.${name}> units =
                ode.xml.model.enums.handlers.Units${name}EnumHandler
                        .getBaseUnit(makeXMLUnit(unit));
        return new ode.units.quantity.${name}(d, units);
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
   public static ode.units.quantity.${name} convert(${name} t) {
       if (t == null) {
           return null;
       }

       Double v = t.getValue();
       // Use the code/symbol-mapping in the ode.model.enums files
       // to convert to the specification value.
       String u = ode.model.enums.Units${name}.valueOf(
               t.getUnit().toString()).getSymbol();
       ode.xml.model.enums.Units${name} units = makeXMLUnit(u);
       ode.units.unit.Unit<ode.units.quantity.${name}> units2 =
               ode.xml.model.enums.handlers.Units${name}EnumHandler
                       .getBaseUnit(units);

       return new ode.units.quantity.${name}(v, units2);
   }


    //
    // REGULAR ICE CLASS
    //

    public final static Ice.ObjectFactory Factory = makeFactory(null);

    public ${name}I() {
        super();
    }

    public ${name}I(double d, Units${name} unit) {
        super();
        this.setUnit(unit);
        this.setValue(d);
    }

    public ${name}I(double d,
            Unit<ode.units.quantity.${name}> unit) {
        this(d, ode.model.enums.Units${name}.bySymbol(unit.getSymbol()));
    }

   /**
    * Copy constructor that converts the given {@link ode.model.${name}}
    * based on the given ode-xml enum
    */
   public ${name}I(${name} value, Unit<ode.units.quantity.${name}> ul) throws BigResult {
       this(value,
            ode.model.enums.Units${name}.bySymbol(ul.getSymbol()).toString());
   }

   /**
    * Copy constructor that converts the given {@link ode.model.${name}}
    * based on the given ode.model enum
    */
   public ${name}I(double d, ode.model.enums.Units${name} ul) {
        this(d, Units${name}.valueOf(ul.toString()));
    }

   /**
    * Copy constructor that converts the given {@link ode.model.${name}}
    * based on the given enum string.
    *
    * @param target String representation of the CODE enum
    */
    public ${name}I(${name} value, String target) throws BigResult {
       String source = value.getUnit().toString();
       if (target.equals(source)) {
           setValue(value.getValue());
           setUnit(value.getUnit());
        } else {
            Units${name} targetUnit = Units${name}.valueOf(target);
            Conversion conversion = conversions.get(value.getUnit()).get(targetUnit);
            if (conversion == null) {
                throw new RuntimeException(String.format(
                    "%f %s cannot be converted to %s",
                        value.getValue(), value.getUnit(), target));
            }
            double orig = value.getValue();
            BigDecimal big = conversion.convert(orig);
            double converted = big.doubleValue();
            if (Double.isInfinite(converted)) {
                throw new BigResult(big,
                        "Failed to convert " + source + ":" + target);
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
    public ${name}I(${name} value, Units${name} target) throws BigResult {
        this(value, target.toString());
    }

    /**
     * Convert a Bhojpur ODE-Formats {@link Length} to a Bhojpur ODE Length.
     */
    public ${name}I(ode.units.quantity.${name} value) {
        ode.model.enums.Units${name} internal =
            ode.model.enums.Units${name}.bySymbol(value.unit().getSymbol());
        Units${name} ul = Units${name}.valueOf(internal.toString());
        setValue(value.value().doubleValue());
        setUnit(ul);
    }

    public double getValue(Ice.Current current) {
        return this.value;
    }

    public void setValue(double value , Ice.Current current) {
        this.value = value;
    }

    public Units${name} getUnit(Ice.Current current) {
        return this.unit;
    }

    public void setUnit(Units${name} unit, Ice.Current current) {
        this.unit = unit;
    }

    public String getSymbol(Ice.Current current) {
        return SYMBOLS.get(this.unit);
    }

    public ${name} copy(Ice.Current ignore) {
        ${name}I copy = new ${name}I();
        copy.setValue(getValue());
        copy.setUnit(getUnit());
        return copy;
    }

    @Override
    public void copyObject(Filterable model, ModelMapper mapper) {
        if (model instanceof ode.model.units.${name}) {
            ode.model.units.${name} t = (ode.model.units.${name}) model;
            this.value = t.getValue();
            this.unit = Units${name}.valueOf(t.getUnit().toString());
        } else {
            throw new IllegalArgumentException(
              "${name} cannot copy from " +
              (model==null ? "null" : model.getClass().getName()));
        }
    }

    @Override
    public Filterable fillObject(ReverseModelMapper mapper) {
        ode.model.enums.Units${name} ut = ode.model.enums.Units${name}.valueOf(getUnit().toString());
        ode.model.units.${name} t = new ode.model.units.${name}(getValue(), ut);
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
        return "${name}(" + value + " " + unit + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ${name} other = (${name}) obj;
        if (unit != other.unit)
            return false;
        if (Double.doubleToLongBits(value) != Double
                .doubleToLongBits(other.value))
            return false;
        return true;
    }

}