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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import ode.units.unit.Unit;
import ode.xml.model.enums.EnumerationException;

import ode.model.enums.UnitsElectricPotential;
import ode.util.Filter;
import ode.util.Filterable;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

/**
 * class storing both a ElectricPotential and a unit for that ElectricPotential
 * (e.g. m, in, ly, etc.) encapsulated in a {@link UnitsElectricPotential} instance. As
 * also described in the remoting definition (.ice) for ElectricPotential, this is an
 * embedded class meaning that the columns here do not appear in their own
 * table but exist directly on the containing object. Like Details and
 * Permissions, instances do not contain long identifiers and cannot be
 * persisted on their own.
 */
@Embeddable
public class ElectricPotential implements Serializable, Filterable, ode.model.units.Unit {

    private static final long serialVersionUID = 1L;

    public final static String VALUE = "ode.model.units.ElectricPotential_value";

    public final static String UNIT = "ode.model.units.ElectricPotential_unit";

    public static ode.xml.model.enums.UnitsElectricPotential makeElectricPotentialUnitXML(String unit) {
        try {
            return ode.xml.model.enums.UnitsElectricPotential
                    .fromString((String) unit);
        } catch (EnumerationException e) {
            throw new RuntimeException("Bad ElectricPotential unit: " + unit, e);
        }
    }

    public static ode.units.quantity.ElectricPotential makeElectricPotentialXML(double d, String unit) {
        ode.units.unit.Unit<ode.units.quantity.ElectricPotential> units =
                ode.xml.model.enums.handlers.UnitsElectricPotentialEnumHandler
                        .getBaseUnit(makeElectricPotentialUnitXML(unit));
        return new ode.units.quantity.ElectricPotential(d, units);
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
    public static ode.units.quantity.ElectricPotential convertElectricPotential(ElectricPotential t) {
        if (t == null) {
            return null;
        }

        Double v = t.getValue();
        String u = t.getUnit().getSymbol();
        ode.xml.model.enums.UnitsElectricPotential units = makeElectricPotentialUnitXML(u);
        ode.units.unit.Unit<ode.units.quantity.ElectricPotential> units2 =
                ode.xml.model.enums.handlers.UnitsElectricPotentialEnumHandler
                        .getBaseUnit(units);

        return new ode.units.quantity.ElectricPotential(v, units2);
    }

    public static ElectricPotential convertElectricPotential(ElectricPotential value, Unit<ode.units.quantity.ElectricPotential> ul) {
        return convertElectricPotential(value, ul.getSymbol());
    }

    public static ElectricPotential convertElectricPotential(ElectricPotential value, String target) {
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
    public ElectricPotential() {
        // no-op
    }

    public ElectricPotential(double d, String u) {
        this.value = d;
        this.unit = UnitsElectricPotential.valueOf(u);
    }

    public ElectricPotential(double d, UnitsElectricPotential u) {
        this.value = d;
        this.unit = u;
    }

    public ElectricPotential(double d,
            Unit<ode.units.quantity.ElectricPotential> unit) {
        this(d, UnitsElectricPotential.bySymbol(unit.getSymbol()));
    }

    public ElectricPotential(ode.units.quantity.ElectricPotential value) {
        this(value.value().doubleValue(),
            UnitsElectricPotential.bySymbol(value.unit().getSymbol()));
    }

    // ~ Fields
    // =========================================================================

    /**
     * positive float representation of the ElectricPotential represented by this
     * field.
     */
    private double value;

    /**
     * representation of the units which should be considering when
     * producing a representation of the {@link #value} field.
     */
    private UnitsElectricPotential unit = null;

    // ~ Property accessors : used primarily by Hibernate
    // =========================================================================

    /**
     * value of this unit-field. It will be persisted to a column with the same
     * name as the containing field. For example, detectorSettings.getVolatage()
     * which is of type {@link ElectricPotential} will be stored in a column "detectorSettingsvolatage".
     **/
    @Column(name = "value", nullable = false)
    public double getValue() {
        return this.value;
    }

    /**
     * Many-to-one field ode.model.units.ElectricPotential.unit (ode.model.enums.UnitsElectricPotential).
     * These values are stored in a column suffixed by "Unit". Whereas {@link #value}
     * for physicalSizeX will be stored as "detectorSettings.volatage", the unit enum
     * will be stored as "detectorSettings.volatageUnit".
     */
    @javax.persistence.Column(name="unit", nullable=false,
        unique=false, insertable=true, updatable=true)
    @Type(type="ode.model.units.GenericEnumType",
          parameters=@Parameter(name="unit", value="ELECTRICPOTENTIAL"))
    public UnitsElectricPotential getUnit() {
        return this.unit;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setUnit(UnitsElectricPotential unit) {
        this.unit = unit;
    }

    @Override
    public boolean acceptFilter(Filter filter) {
        this.unit = (UnitsElectricPotential) filter.filter(UNIT, unit);
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
        return "ElectricPotential(" + value + " " + unit + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ElectricPotential other = (ElectricPotential) obj;
        if (unit != other.unit)
            return false;
        if (Double.doubleToLongBits(value) != Double
                .doubleToLongBits(other.value))
            return false;
        return true;
    }

}
