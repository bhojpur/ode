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
%}

#ifndef CLASS_${name.upper()}
#define CLASS_${name.upper()}

#include <ode/model/Units.ice>

module ode {

    module model {

      /**
       * Unit of ${name} which is used through the model. This is not
       * an {@link ode.model.IObject} implementation and as such does
       * not have an ID value. Instead, the entire object is embedded
       * into the containing class, so that the value and unit rows
       * can be found on the table itself (e.g. ${dotexample(name)}
       * and ${dotexample(name)}Unit).
       **/
    ["protected"] class ${name}
    {

      /**
       * PositiveFloat value
       */
      double value;

      ode::model::enums::Units${name} unit;

      /**
       * Actual value for this unit-based field. The interpretation of
       * the value is only possible along with the
       * {@link ode.model.enums.Units${name}} enum.
       **/
      double getValue();

      void setValue(double value);

      /**
       * {@link ode.model.enums.Units${name}} instance which is an
       * {@link ode.model.IObject}
       * meaning that its ID is sufficient for identifying equality.
       **/
      ode::model::enums::Units${name} getUnit();

      void setUnit(ode::model::enums::Units${name} unit);

      /**
       * Returns the possibly unicode representation of the "unit"
       * value for display.
       **/
      string getSymbol();

      ${name} copy();

    };
  };
};
#endif