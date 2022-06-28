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

{% python
    def example(name):
        if name == "ElectricPotential":
            return ("detectorSettings", "volatage")
        elif name == "Frequency":
            return ("detectorSettings", "readOutRate")
        elif name == "Length":
            return ("pixels", "physicalSizeX")
        elif name == "Power":
            return ("lightSource", "power")
        elif name == "Pressure":
            return ("imagingEnvironment", "pressure")
        elif name == "Temperature":
            return ("imagingEnvironment", "temperature")
        elif name == "Time":
            return ("planeInfo", "exposureTime")
        else:
            raise Exception("Unknown: %s" % name)
    
    def dotexample(name):
        return ".".join(example(name))
    
    def getexample(name):
        t, f = example(name)
        return "%s.get%s()" % (t, f.capitalize())
    %}
    package ode.model.units;
    
    import java.io.Serializable;
    
    import javax.persistence.Column;
    import javax.persistence.Embeddable;
    
    import ode.units.unit.Unit;
    import ode.xml.model.enums.EnumerationException;
    
    import ode.model.enums.Units${name};
    import ode.util.Filter;
    import ode.util.Filterable;
    
    import org.hibernate.annotations.Parameter;
    import org.hibernate.annotations.Type;
    
    /**
     * class storing both a ${name} and a unit for that ${name}
     * (e.g. m, in, ly, etc.) encapsulated in a {@link Units${name}} instance. As
     * also described in the remoting definition (.ice) for ${name}, this is an
     * embedded class meaning that the columns here do not appear in their own
     * table but exist directly on the containing object. Like Details and
     * Permissions, instances do not contain long identifiers and cannot be
     * persisted on their own.
     */
    @Embeddable
    public class ${name} implements Serializable, Filterable, ode.model.units.Unit {
    
        private static final long serialVersionUID = 1L;
    
        public final static String VALUE = "ode.model.units.${name}_value";
    
        public final static String UNIT = "ode.model.units.${name}_unit";
    
        public static ode.xml.model.enums.Units${name} make${name}UnitXML(String unit) {
            try {
                return ode.xml.model.enums.Units${name}
                        .fromString((String) unit);
            } catch (EnumerationException e) {
                throw new RuntimeException("Bad ${name} unit: " + unit, e);
            }
        }
    
        public static ode.units.quantity.${name} make${name}XML(double d, String unit) {
            ode.units.unit.Unit<ode.units.quantity.${name}> units =
                    ode.xml.model.enums.handlers.Units${name}EnumHandler
                            .getBaseUnit(make${name}UnitXML(unit));
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
        public static ode.units.quantity.${name} convert${name}(${name} t) {
            if (t == null) {
                return null;
            }
    
            Double v = t.getValue();
            String u = t.getUnit().getSymbol();
            ode.xml.model.enums.Units${name} units = make${name}UnitXML(u);
            ode.units.unit.Unit<ode.units.quantity.${name}> units2 =
                    ode.xml.model.enums.handlers.Units${name}EnumHandler
                            .getBaseUnit(units);
    
            return new ode.units.quantity.${name}(v, units2);
        }
    
        public static ${name} convert${name}(${name} value, Unit<ode.units.quantity.${name}> ul) {
            return convert${name}(value, ul.getSymbol());
        }
    
        public static ${name} convert${name}(${name} value, String target) {
            String source = value.getUnit().getSymbol();
            if (target.equals(source)) {
                return value;
            }
            throw new RuntimeException(String.format(
                    "%f %s cannot be converted to %s",
                    value.getValue(), value.getUnit().getSymbol(), source));
        }
    
        // ~ Constructors
        // =========================================================================
    
        /**
         * no-arg constructor to keep Hibernate happy.
         */
        @Deprecated
        public ${name}() {
            // no-op
        }
    
        public ${name}(double d, String u) {
            this.value = d;
            this.unit = Units${name}.valueOf(u);
        }
    
        public ${name}(double d, Units${name} u) {
            this.value = d;
            this.unit = u;
        }
    
        public ${name}(double d,
                Unit<ode.units.quantity.${name}> unit) {
            this(d, Units${name}.bySymbol(unit.getSymbol()));
        }
    
        public ${name}(ode.units.quantity.${name} value) {
            this(value.value().doubleValue(),
                Units${name}.bySymbol(value.unit().getSymbol()));
        }
    
        // ~ Fields
        // =========================================================================
    
        /**
         * positive float representation of the ${name} represented by this
         * field.
         */
        private double value;
    
        /**
         * representation of the units which should be considering when
         * producing a representation of the {@link #value} field.
         */
        private Units${name} unit = null;
    
        // ~ Property accessors : used primarily by Hibernate
        // =========================================================================
    
        /**
         * value of this unit-field. It will be persisted to a column with the same
         * name as the containing field. For example, ${getexample(name)}
         * which is of type {@link ${name}} will be stored in a column "${example(name)}".
         **/
        @Column(name = "value", nullable = false)
        public double getValue() {
            return this.value;
        }
    
        /**
         * Many-to-one field ode.model.units.${name}.unit (ode.model.enums.Units${name}).
         * These values are stored in a column suffixed by "Unit". Whereas {@link #value}
         * for physicalSizeX will be stored as "${dotexample(name)}", the unit enum
         * will be stored as "${dotexample(name)}Unit".
         */
        @javax.persistence.Column(name="unit", nullable=false,
            unique=false, insertable=true, updatable=true)
        @Type(type="ode.model.units.GenericEnumType",
              parameters=@Parameter(name="unit", value="${name.upper()}"))
        public Units${name} getUnit() {
            return this.unit;
        }
    
        public void setValue(double value) {
            this.value = value;
        }
    
        public void setUnit(Units${name} unit) {
            this.unit = unit;
        }
    
        @Override
        public boolean acceptFilter(Filter filter) {
            this.unit = (Units${name}) filter.filter(UNIT, unit);
            this.value = (Double) filter.filter(VALUE,  value);
            return true;
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