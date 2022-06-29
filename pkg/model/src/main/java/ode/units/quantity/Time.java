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
 * Time quantity.
 */
public class Time extends Quantity implements Comparable<Time>
{
  /** Seed for hashCode. */
  private static final int SEED1 = 78;
  /** Seed for hashCode. */
  private static final int SEED2 = 89;
  /** Value of this quantity. */
  Number value;
  /** Unit type of this quantity. */
  Unit<ode.units.quantity.Time> unit;
  /** Cached value for hashCode. */
  private int hashCodeValue;

  /**
   * Create an Time.
   *
   * @param inValue the value.
   * @param inUnit the unit type.
   */
  public Time(Number inValue,
    Unit<ode.units.quantity.Time> inUnit)
  {
    if (inValue == null)
    {
      throw new NullPointerException("Time: Time cannot be constructed with a null value.");
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
  public Number value(Unit<ode.units.quantity.Time> inUnit)
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
    Time otherTime = (Time)other;
    if (unit.equals(otherTime.unit))
    {
      // Times use same unit so compare value
      return value.equals(otherTime.value);
    } else {
      if (unit.isConvertible(otherTime.unit))
      {
        // Times use different compatible units so convert value then compare
        return (unit.convertValue(value, otherTime.unit)).equals(otherTime.value);
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
  public int compareTo(Time other)
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
  public Unit<ode.units.quantity.Time> unit()
  {
    return unit;
  }
}