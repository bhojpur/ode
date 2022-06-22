#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.

"""
Simple tests of the new conversions used for units.
"""

from pytest import assertAlmostEqual
from ode.conversions import Add
from ode.conversions import Int
from ode.conversions import Mul
from ode.conversions import Pow
from ode.conversions import Rat
from ode.conversions import Sym

class TestConversions(object):

    def assertEquals(self, a, b, places=6):
        assertAlmostEqual(a, b, places=places)

    def testSimpleAdd(self):
        add = Add(Rat(1, 2), Rat(1, 2))
        assert "(1 + 2)", str(add)
        whole = add(-1)  # -1 is ignored
        self.assertEquals(1.0, whole)

    def testSimpleMul(self):
        mul = Mul(Int(1000000), Sym("megas"))
        assert "(1000000 * x)" == str(mul)
        seconds = mul(5.0)
        self.assertEquals(5000000.0, seconds)

    def testSimpleInt(self):
        i = Int(123)
        assert "123" == str(i)
        x = i(-1)  # -1 is ignored
        self.assertEquals(123.0, x)

    def testBigInt(self):
        big = "123456789012345678901234567890"
        big = big * 5
        i = Mul(Int(big), Int(big))
        x = i(-1)  # -1 is ignored
        target = float(big) * float(big)
        self.assertEquals(target, x)

    def testSimplePow(self):
        p = Pow(3, 2)
        assert "(3 ** 2)" == str(p)
        x = p(-1)  # -1 is ignored
        self.assertEquals(9.0, x)

    def testSimpleRat(self):
        r = Rat(1, 3)
        assert "(1 / 3)" == str(r)
        x = r(-1)  # -1 is ignored
        self.assertEquals(0.333333, x)

    def testDelayedRat(self):
        r = Rat(Int(1), Int(3))
        x = r(-1)  # -1 is ignored
        self.assertEquals(0.333333, x)

    def testSimpleSym(self):
        s = Sym("s")
        assert "x" == str(s)
        x = s(5.0)
        self.assertEquals(5.0, x)

    def testFahrenheitCelsius(self):
        ftoc = Add(Mul(Rat(5, 9), Sym("f")), Rat(-160, 9))
        assert "(((5 / 9) * x) + (-160 / 9))" == str(ftoc)
        self.assertEquals(0.0, ftoc(32.0))
        self.assertEquals(100.0, ftoc(212.0))
        self.assertEquals(-40.0, ftoc(-40.0))
