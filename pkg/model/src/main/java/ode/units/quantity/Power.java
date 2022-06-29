package ode.units.quantity;

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

/**
 * Power quantity.
 */
public class Power extends Quantity implements Comparable<Power>
{
  /** Seed for hashCode. */
  private static final int SEED1 = 56;
  /** Seed for hashCode. */
  private static final int SEED2 = 67;
  /** Value of this quantity. */
  Number value;
  /** Unit type of this quantity. */
  Unit<ode.units.quantity.Power> unit;
  /** Cached value for hashCode. */
  private int hashCodeValue;

  /**
   * Create a Power.
   *
   * @param inValue the value.
   * @param inUnit the unit type.
   */
  public Power(Number inValue,
    Unit<ode.units.quantity.Power> inUnit)
  {
    if (inValue == null)
    {
      throw new NullPointerException("Power: Power cannot be constructed with a null value.");
    }
    value = inValue;
    unit = inUnit;
    hashCodeValue = SEED1;
    hashCodeValue = SEED2 * hashCodeValue + Float.floatToIntBits(value.floatValue());
    hashCodeValue = SEED2 * hashCodeValue + unit.getSymbol().hashCode();
  }

  @Override
  public Number value()
  {
    return value;
  }

  /**
   * Perform a unit conversion.
   *
   * @param inUnit the unit to convert to.
   * @return the current quantity value converted to the specified
   * unit, or null if the conversion is not possible.
   */
  public Number value(Unit<ode.units.quantity.Power> inUnit)
  {
    if (unit.equals(inUnit))
    {
      return value;
    }
    if (unit.isConvertible(inUnit))
    {
      return unit.convertValue(value, inUnit);
    }
    return null;
  }

  /**
   * Check quantities for equality.
   *
   * Unit conversion will be performed when required to convert into
   * the unit system of this quantity in order to perform the
   * comparison.
   *
   * Note that floating point comparison is dangerous.  Do not use
   * this method.
   *
   * @return true if equal, false if not equal.
   */
  @Override
  public boolean equals(Object other)
  {
    if (other == null)
    {
      return false;
    }
    if (this.getClass() != other.getClass())
    {
      return false;
    }
    Power otherPower = (Power)other;
    if (unit.equals(otherPower.unit))
    {
      // Powers use same unit so compare value
      return value.equals(otherPower.value);
    } else {
      if (unit.isConvertible(otherPower.unit))
      {
        // Powers use different compatible units so convert value then compare
        return (unit.convertValue(value, otherPower.unit)).equals(otherPower.value);
      }
    }
    return false;
  }

  /**
   * Check quantities for equality.
   *
   * Unit conversion will be performed when required to convert into
   * the unit system of this quantity in order to perform the
   * comparison.
   *
   * Note that floating point comparison is dangerous.  Do not use
   * this method.
   *
   * @return true if equal, false if not equal.
   */
  @Override
  public int compareTo(Power other)
  {
    if (this == other) {
      return 0;
    }
    return Double.compare(value.doubleValue(), other.value(unit).doubleValue());
  }

  @Override
  public int hashCode()
  {
    return hashCodeValue;
  }
  @Override
  public String toString()
  {
    StringBuilder result = new StringBuilder();
    result.append(this.getClass().getName());
    result.append(": ");
    result.append("value[");
    result.append(value);
    result.append("], unit[");
    result.append(unit.getSymbol());
    result.append("] stored as ");
    result.append(value.getClass().getName());
    return result.toString();
  }

  @Override
  public Unit<ode.units.quantity.Power> unit()
  {
    return unit;
  }
}