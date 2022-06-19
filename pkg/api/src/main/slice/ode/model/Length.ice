/*
 * Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

#ifndef CLASS_LENGTH
#define CLASS_LENGTH

#include <ode/model/Units.ice>

module ode {

    module model {

      /**
       * Unit of Length which is used through the model. This is not
       * an {@link ode.model.IObject} implementation and as such does
       * not have an ID value. Instead, the entire object is embedded
       * into the containing class, so that the value and unit rows
       * can be found on the table itself (e.g. pixels.physicalSizeX
       * and pixels.physicalSizeXUnit).
       **/
    ["protected"] class Length
    {

      /**
       * PositiveFloat value
       **/
      double value;

      ode::model::enums::UnitsLength unit;

      /**
       * Actual value for this unit-based field. The interpretation of
       * the value is only possible along with the
       * {@link ode.model.enums.UnitsLength} enum.
       **/
      double getValue();

      void setValue(double value);

      /**
       * {@link ode.model.enums.UnitsLength} instance which is an
       * {@link ode.model.IObject}
       * meaning that its ID is sufficient for identifying equality.
       **/
      ode::model::enums::UnitsLength getUnit();

      void setUnit(ode::model::enums::UnitsLength unit);

      /**
       * Returns the possibly unicode representation of the ""unit""
       * value for display.
       **/
      string getSymbol();

      Length copy();

    };
  };
};
#endif
