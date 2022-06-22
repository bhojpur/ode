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

#include <ode/model/TemperatureI.h>
#include <ode/ClientErrors.h>

::Ice::Object* IceInternal::upCast(::ode::model::TemperatureI* t) { return t; }

using namespace ode::conversions;

typedef ode::model::enums::UnitsTemperature UnitsTemperature;

namespace ode {

    namespace model {

        static std::map<UnitsTemperature, ConversionPtr> createMapCELSIUS() {
            std::map<UnitsTemperature, ConversionPtr> c;
            c[enums::FAHRENHEIT] = Add(Mul(Rat(Int(9), Int(5)), Sym("c")), Int(32));
            c[enums::KELVIN] = Add(Sym("c"), Rat(Int(5463), Int(20)));
            c[enums::RANKINE] = Add(Mul(Rat(Int(9), Int(5)), Sym("c")), Rat(Int(49167), Int(100)));
            return c;
        }

        static std::map<UnitsTemperature, ConversionPtr> createMapFAHRENHEIT() {
            std::map<UnitsTemperature, ConversionPtr> c;
            c[enums::CELSIUS] = Add(Mul(Rat(Int(5), Int(9)), Sym("f")), Rat(Int(-160), Int(9)));
            c[enums::KELVIN] = Add(Mul(Rat(Int(5), Int(9)), Sym("f")), Rat(Int(45967), Int(180)));
            c[enums::RANKINE] = Add(Sym("f"), Rat(Int(45967), Int(100)));
            return c;
        }

        static std::map<UnitsTemperature, ConversionPtr> createMapKELVIN() {
            std::map<UnitsTemperature, ConversionPtr> c;
            c[enums::CELSIUS] = Add(Sym("k"), Rat(Int(-5463), Int(20)));
            c[enums::FAHRENHEIT] = Add(Mul(Rat(Int(9), Int(5)), Sym("k")), Rat(Int(-45967), Int(100)));
            c[enums::RANKINE] = Mul(Rat(Int(9), Int(5)), Sym("k"));
            return c;
        }

        static std::map<UnitsTemperature, ConversionPtr> createMapRANKINE() {
            std::map<UnitsTemperature, ConversionPtr> c;
            c[enums::CELSIUS] = Add(Mul(Rat(Int(5), Int(9)), Sym("r")), Rat(Int(-5463), Int(20)));
            c[enums::FAHRENHEIT] = Add(Sym("r"), Rat(Int(-45967), Int(100)));
            c[enums::KELVIN] = Mul(Rat(Int(5), Int(9)), Sym("r"));
            return c;
        }

        static std::map<UnitsTemperature,
            std::map<UnitsTemperature, ConversionPtr> > makeConversions() {
            std::map<UnitsTemperature, std::map<UnitsTemperature, ConversionPtr> > c;
            c[enums::CELSIUS] = createMapCELSIUS();
            c[enums::FAHRENHEIT] = createMapFAHRENHEIT();
            c[enums::KELVIN] = createMapKELVIN();
            c[enums::RANKINE] = createMapRANKINE();
            return c;
        }

        static std::map<UnitsTemperature, std::string> makeSymbols(){
            std::map<UnitsTemperature, std::string> s;
            s[enums::CELSIUS] = "°C";
            s[enums::FAHRENHEIT] = "°F";
            s[enums::KELVIN] = "K";
            s[enums::RANKINE] = "°R";
            return s;
        }

        std::map<UnitsTemperature,
            std::map<UnitsTemperature, ConversionPtr> > TemperatureI::CONVERSIONS = makeConversions();

        std::map<UnitsTemperature, std::string> TemperatureI::SYMBOLS = makeSymbols();

        TemperatureI::~TemperatureI() {}

        TemperatureI::TemperatureI() : Temperature() {
        }

        TemperatureI::TemperatureI(const double& value, const UnitsTemperature& unit) : Temperature() {
            setValue(value);
            setUnit(unit);
        }

        TemperatureI::TemperatureI(const TemperaturePtr& value, const UnitsTemperature& target) : Temperature() {
            double orig = value->getValue();
            UnitsTemperature source = value->getUnit();
            if (target == source) {
                // No conversion needed
                setValue(orig);
                setUnit(target);
            } else {
                ConversionPtr conversion = CONVERSIONS[source][target];
                if (!conversion) {
                    std::stringstream ss;
                    ss << orig << " " << source;
                    ss << "cannot be converted to " << target;
                    throw ode::ClientError(__FILE__, __LINE__, ss.str().c_str());
                }
                double converted = conversion->convert(orig);
                setValue(converted);
                setUnit(target);
            }
        }

        Ice::Double TemperatureI::getValue(const Ice::Current& /* current */) {
            return value;
        }

        void TemperatureI::setValue(Ice::Double _value, const Ice::Current& /* current */) {
            value = _value;
        }

        UnitsTemperature TemperatureI::getUnit(const Ice::Current& /* current */) {
            return unit;
        }

        void TemperatureI::setUnit(UnitsTemperature _unit, const Ice::Current& /* current */) {
            unit = _unit;
        }

        std::string TemperatureI::getSymbol(const Ice::Current& /* current */) {
            return SYMBOLS[unit];
        }

        TemperaturePtr TemperatureI::copy(const Ice::Current& /* current */) {
            TemperaturePtr copy = new TemperatureI();
            copy->setValue(getValue());
            copy->setUnit(getUnit());
            return copy;
        }
    }
}

