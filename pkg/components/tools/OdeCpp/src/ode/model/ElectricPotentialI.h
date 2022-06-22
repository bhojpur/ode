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

#ifndef ODE_MODEL_ELECTRICPOTENTIALI_H
#define ODE_MODEL_ELECTRICPOTENTIALI_H

#include <ode/IcePortPush.h>
#include <ode/IceNoWarnPush.h>
#include <ode/model/ElectricPotential.h>
#include <ode/model/Units.h>
#include <ode/IceNoWarnPop.h>

#include <ode/conversions.h>

#ifndef ODE_CLIENT
#   ifdef ODE_CLIENT_EXPORTS
#       define ODE_CLIENT ICE_DECLSPEC_EXPORT
#   else
#       define ODE_CLIENT ICE_DECLSPEC_IMPORT
#   endif
#endif

namespace ode {
  namespace model {
    class ElectricPotentialI;
  }
}

namespace IceInternal {
  ODE_CLIENT ::Ice::Object* upCast(::ode::model::ElectricPotentialI*);
}

namespace ode {
  namespace model {

    typedef IceInternal::Handle<ElectricPotentialI> ElectricPotentialIPtr;

    class ODE_CLIENT ElectricPotentialI : virtual public ElectricPotential {

    protected:
        virtual ~ElectricPotentialI(); // protected as outlined in Ice docs.
        static std::map<enums::UnitsElectricPotential,
            std::map<enums::UnitsElectricPotential,
                ode::conversions::ConversionPtr> > CONVERSIONS;
        static std::map<enums::UnitsElectricPotential, std::string> SYMBOLS;

    public:

        static std::string lookupSymbol(enums::UnitsElectricPotential unit) {
            return SYMBOLS[unit];
        }

        ElectricPotentialI();

        ElectricPotentialI(const double& value, const enums::UnitsElectricPotential& unit);

        // Conversion constructor
        ElectricPotentialI(const ElectricPotentialPtr& value, const enums::UnitsElectricPotential& target);

        virtual Ice::Double getValue(
                const Ice::Current& current = Ice::Current());

        virtual void setValue(
                Ice::Double value,
                const Ice::Current& current = Ice::Current());

        virtual enums::UnitsElectricPotential getUnit(
                const Ice::Current& current = Ice::Current());

        virtual void setUnit(
                enums::UnitsElectricPotential unit,
                const Ice::Current& current = Ice::Current());

        virtual std::string getSymbol(
                const Ice::Current& current = Ice::Current());

        virtual ElectricPotentialPtr copy(
                const Ice::Current& = Ice::Current());

    };
  }
}

#include <ode/IcePortPop.h>

#endif // ODE_MODEL_ELECTRICPOTENTIALI_H

