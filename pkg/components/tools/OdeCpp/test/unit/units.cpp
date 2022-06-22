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

#include <ode/model/LengthI.h>
#include <ode/model/PowerI.h>
#include <ode/model/TemperatureI.h>
#include <ode/fixture.h>
#include <string>

using namespace ode::model;
using namespace ode::model::enums;

TEST(UnitsTest, testPowerConversion)
{
    PowerPtr p1 = new PowerI(100.1, CENTIWATT);
    PowerPtr p2 = new PowerI(p1, WATT);
    ASSERT_NEAR(1.001, p2->getValue(), 1e-5);
}

TEST(UnitsTest, testLengthSymbol)
{
    LengthPtr l = new LengthI(100.1, MICROMETER);
    ASSERT_EQ("µm", l->getSymbol());
}

TEST(UnitsTest, testBoiling)
{
    TemperaturePtr f = new TemperatureI(212, FAHRENHEIT);
    TemperaturePtr c = new TemperatureI(f, CELSIUS);

    ASSERT_NEAR(100.0, c->getValue(), 1e-5);
}

TEST(UnitsTest, testTemperatureConversion)
{
    TemperaturePtr f = new TemperatureI(32, FAHRENHEIT);
    TemperaturePtr c = new TemperatureI(f, CELSIUS);
    TemperaturePtr k = new TemperatureI(c, KELVIN);

    ASSERT_NEAR(0, c->getValue(), 1e-5);
    ASSERT_NEAR(273.15, k->getValue(), 1e-5);
}
