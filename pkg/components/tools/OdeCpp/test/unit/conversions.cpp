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

#include <ode/conversions.h>
#include <ode/fixture.h>
#include <string>

using namespace ode::conversions;

TEST(ConversionsTest, testSimplAdd)
{
    ConversionPtr add = Add(Rat(1, 2), Rat(1,2));
    double whole = add->convert(-1); // -1 is ignored
    ASSERT_NEAR(1.0, whole, 0.0001);
}

TEST(ConversionsTest, testSimpleMul)
{
    ConversionPtr mul = Mul(Int(1000000), Sym("megas"));
    double seconds = mul->convert(5.0);
    ASSERT_NEAR(5000000.0, seconds, 0.0001);
}

TEST(ConversionsTest, testSimpleInt)
{
    ConversionPtr i = Int(123);
    double x = i->convert(-1); // -1 is ignored
    ASSERT_NEAR(123.0, x, 0.0001);
}

TEST(ConversionsTest, testBigInt)
{
    std::string big = "123456789012345678901234567891234567890";
    big = big + big + big + big + big;
    ConversionPtr i = Mul(Int(big), Int(big));
    i->convert(-1);
    // TODO: This should throw an exception for "no conversion possible"
    // ASSERT_NEAR(Double.POSITIVE_INFINITY, rv);
}

TEST(ConversionsTest, testSimplePow)
{
    ConversionPtr p = Pow(3, 2);
    double x = p->convert(-1); // -1 is ignored
    ASSERT_NEAR(9.0, x, 0.0001);
}

TEST(ConversionsTest, testSimpleRat)
{
    ConversionPtr r = Rat(1, 3);
    double x = r->convert(-1); // -1 is ignored
    ASSERT_NEAR(0.33333333, x, 0.0001);
}

TEST(ConversionsTest, testDelayedRat)
{
    ConversionPtr r = Rat(Int(1), Int(3));
    double x = r->convert(-1); // -1 is ignored
    ASSERT_NEAR(0.33333333, x, 0.0001);
}

TEST(ConversionsTest, testSimpleSym)
{
    ConversionPtr sym = Sym("x");
    double x = sym->convert(5.0);
    ASSERT_NEAR(5.0, x, 0.0001);
}

TEST(ConversionsTest, testFahrenheit)
{
    ConversionPtr ftoc = Add(Mul(Rat(5, 9), Sym("f")), Rat(-160, 9));
    ASSERT_NEAR(0.0, ftoc->convert(32.0), 0.0001);
    ASSERT_NEAR(100.0, ftoc->convert(212.0), 0.0001);
    ASSERT_NEAR(-40.0, ftoc->convert(-40.0), 0.0001);
}
