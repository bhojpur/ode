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
Conversion utilities for changing between units.
"""

from __future__ import division

from builtins import str
from past.utils import old_div
from builtins import object

class Conversion(object):
    """
    Base-functor like object which can be used for preparing complex
    equations for converting from one unit to another. Primarily these
    classes and static methods are used via code-generation. Sympy-generated
    strings are placed directly into code. If the proper imports are in place,
    then a top-level Conversion (usually of type Add or
    Mul is returned from the evaluation).
    """

    def __init__(self, *conversions):
        self.conversions = conversions

    def __call__(self, original):
        raise NotImplemented()

    def join(self, sym):
        sb = sym.join([str(x) for x in self.conversions])
        return "(%s)" % sb

class Add(Conversion):
    """
    Sums all the Conversion instances which
    are passed in to the constructor.
    """

    def __call__(self, original):
        rv = 0.0
        for c in self.conversions:
            rv += c(original)
        return rv

    def __str__(self):
        return self.join(" + ")

class Int(Conversion):
    """
    Simple representation of a possibly
    very large integer.
    """

    def __init__(self, i):
        if isinstance(i, int):
            self.i = i
        else:
            self.i = float(i)  # Handles big strings

    def __call__(self, original):
        return self.i

    def __str__(self):
        return str(self.i)

class Mul(Conversion):
    """
    Multiplies all the Conversion instances which
    are passed in to the constructor.
    """

    def __call__(self, original):
        rv = 1.0
        for c in self.conversions:
            rv *= c(original)
        return rv

    def __str__(self):
        return self.join(" * ")

class Pow(Conversion):
    """
    Raises the first argument (base) to
    the power of the second (exponent).
    """

    def __init__(self, base, exp):
        self.base = base
        self.exp = exp

    def __call__(self, original):
        return self.base ** self.exp

    def __str__(self):
        return "(%s ** %s)" % (self.base, self.exp)

class Rat(Conversion):
    """
    Divides the first argument (numerator)
    by the second (denominator).
    """

    def __init__(self, n, d):
        self.n = n
        self.d = d

    def unwrap(self, x, original):
        if isinstance(x, (int, float, str)):
            return float(x)
        else:
            return x(original)

    def __call__(self, original):
        n = self.unwrap(self.n, original)
        d = self.unwrap(self.d, original)
        return old_div(float(n), d)

    def __str__(self):
        return "(%s / %s)" % (self.n, self.d)

class Sym(Conversion):
    """
    Represents the variable of the source
    unit and simply returns the original
    value passed to it.
    """

    def __init__(self, s):
        self.s = s

    def __call__(self, original):
        return float(original)

    def __str__(self):
        return "x"