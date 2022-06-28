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

::Ice::Object* IceInternal::upCast(::ode::model::${name}I* t) { return t; }

namespace ode {

    namespace model {

        ${name}I::~${name}I() {}

        ${name}I::${name}I() : ${name}() {
        }

        Ice::Double ${name}I::getValue(const Ice::Current& /* current */) {
            return value;
        }

        void ${name}I::setValue(Ice::Double _value, const Ice::Current& /* current */) {
            value = _value;
        }

        ode::model::enums::Units${name} ${name}I::getUnit(const Ice::Current& /* current */) {
            return unit;
        }

        void ${name}I::setUnit(ode::model::enums::Units${name} _unit, const Ice::Current& /* current */) {
            unit = _unit;
        }

        ${name}Ptr ${name}I::copy(const Ice::Current& /* current */) {
            ${name}Ptr copy = new ${name}I();
            copy->setValue(getValue());
            copy->setUnit(getUnit());
            return copy;
        }
    }
}