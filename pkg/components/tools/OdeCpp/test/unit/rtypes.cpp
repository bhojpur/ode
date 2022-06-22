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

#include <ode/fixture.h>
#include <ode/RTypesI.h>

using namespace ode;
using namespace ode::rtypes;

TEST(RTypeTest, RBool)
{
    RBoolPtr t = rbool(true);
    ASSERT_TRUE(t->getValue());

    RBoolPtr f = rbool(false);
    ASSERT_FALSE(f->getValue());

    ASSERT_TRUE(t == rbool(true));
    ASSERT_TRUE(t != f);
}

template <typename T>
void assertRValues(const T rv1, const T rv2) {
    ASSERT_TRUE(-1 == rv1->compare(rv2));
    ASSERT_TRUE(1 == rv2->compare(rv1));
    ASSERT_FALSE(rv1 == rv2);
    ASSERT_TRUE(rv1 != rv2);
    ASSERT_TRUE(rv1 < rv2);
    ASSERT_TRUE(rv2 > rv1);

    ASSERT_TRUE(rv1->getValue() < rv2->getValue());
    ASSERT_TRUE(rv1->getValue() < rv2->getValue());
    ASSERT_TRUE(rv1->getValue() != rv2->getValue());

    ASSERT_EQ(rv1->compare(rv2), -1);
    ASSERT_EQ(rv2->compare(rv1), 1);
    ASSERT_EQ(rv2->compare(rv2), 0);
}


TEST(RTypesTest, RDouble) {
    RDoublePtr d1 = rdouble(1.1);
    RDoublePtr d2 = rdouble(2.5);

    RTypePtr d3 = rdouble(3.3);
    RFloatPtr rd3 = dynamic_cast<RFloat*>(d3.get());
    if (rd3)
        std::cout << "rd3  " << rd3->getValue() << std::endl;

    assertRValues<RDoublePtr>(d1, d2);
}

TEST(RTypesTest, RFloat) {
    RFloatPtr f1 = rfloat(1.1);
    RFloatPtr f2 = rfloat(2.5);

    assertRValues<RFloatPtr>(f1, f2);
}

TEST(RTypesTest, RInt) {
    RIntPtr i1 = rint(1);
    RIntPtr i2 = rint(2);

    assertRValues<RIntPtr>(i1, i2);
}

TEST(RTypesTest, RLong) {
    RLongPtr l1 = rlong(1);
    RLongPtr l2 = rlong(2);

    assertRValues<RLongPtr>(l1, l2);
}

TEST(RTypesTest, RTime) {
    RTimePtr t1 = rtime(1);
    RTimePtr t2 = rtime(2);

    assertRValues<RTimePtr>(t1, t2);
}

TEST(RTypesTest, RString) {
    RStringPtr s1 = rstring("abc");
    RStringPtr s2 = rstring("def");

    assertRValues<RStringPtr>(s1, s2);
}

TEST(RTypesTest, DISABLED_RArray) {
    RArrayPtr a1 = rarray();
    a1->add(rint(1));

    RArrayPtr a2 = rarray();
    a2->add(rint(2));

    assertRValues<RArrayPtr>(a1, a2);
}

TEST(RTypesTest, DISABLED_RList) {
    RListPtr l1 = rlist();
    l1->add(rint(1));

    RListPtr l2 = rlist();
    l2->add(rint(2));

    assertRValues<RListPtr>(l1, l2);
}

TEST(RTypesTest, DISABLED_RSet) {
    RSetPtr s1 = rset();
    s1->add(rint(1));

    RSetPtr s2 = rset();
    s2->add(rint(2));

    assertRValues<RSetPtr>(s1, s2);
}

TEST(RTypesTest, DISABLED_RMap) {
    RMapPtr m1 = rmap();
    m1->put("a", rint(1));

    RMapPtr m2 = rmap();
    m2->put("b", rint(2));

    assertRValues<RMapPtr>(m1, m2);
}
