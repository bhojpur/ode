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

#include <ode/model/${name}I.h>
#include <ode/ClientErrors.h>

::Ice::Object* IceInternal::upCast(::ode::model::${name}I* t) { return t; }

using namespace ode::conversions;

typedef ode::model::enums::Units${name} Units${name};

namespace ode {

    namespace model {

{% for cfrom in sorted(equations) %}\
        static std::map<Units${name}, ConversionPtr> createMap${cfrom}() {
            std::map<Units${name}, ConversionPtr> c;
{% for cto, equation in sorted(equations.get(cfrom, {}).items()) %}\
{% if cfrom != cto %}\
            c[enums::${cto}] = ${equation};
{% end %}\
{% end %}\
            return c;
        }

{% end %}\
        static std::map<Units${name},
            std::map<Units${name}, ConversionPtr> > makeConversions() {
            std::map<Units${name}, std::map<Units${name}, ConversionPtr> > c;
{% for cfrom in sorted(equations) %}\
            c[enums::${cfrom}] = createMap${cfrom}();
{% end %}\
            return c;
        }

        static std::map<Units${name}, std::string> makeSymbols(){
            std::map<Units${name}, std::string> s;
{% for x in sorted(items) %}\
            s[enums::${x.CODE}] = "${x.SYMBOL}";
{% end %}\
            return s;
        }

        std::map<Units${name},
            std::map<Units${name}, ConversionPtr> > ${name}I::CONVERSIONS = makeConversions();

        std::map<Units${name}, std::string> ${name}I::SYMBOLS = makeSymbols();

        ${name}I::~${name}I() {}

        ${name}I::${name}I() : ${name}() {
        }

        ${name}I::${name}I(const double& value, const Units${name}& unit) : ${name}() {
            setValue(value);
            setUnit(unit);
        }

        ${name}I::${name}I(const ${name}Ptr& value, const Units${name}& target) : ${name}() {
            double orig = value->getValue();
            Units${name} source = value->getUnit();
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

        Ice::Double ${name}I::getValue(const Ice::Current& /* current */) {
            return value;
        }

        void ${name}I::setValue(Ice::Double _value, const Ice::Current& /* current */) {
            value = _value;
        }

        Units${name} ${name}I::getUnit(const Ice::Current& /* current */) {
            return unit;
        }

        void ${name}I::setUnit(Units${name} _unit, const Ice::Current& /* current */) {
            unit = _unit;
        }

        std::string ${name}I::getSymbol(const Ice::Current& /* current */) {
            return SYMBOLS[unit];
        }

        ${name}Ptr ${name}I::copy(const Ice::Current& /* current */) {
            ${name}Ptr copy = new ${name}I();
            copy->setValue(getValue());
            copy->setUnit(getUnit());
            return copy;
        }
    }
}