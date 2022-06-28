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

from base import add_si
from base import print_conversions

from sympy.physics.units import centi
from sympy.physics.units import deci
from sympy.physics.units import kilo
from sympy.physics.units import mega
from sympy.physics.units import milli

from sympy import symbols as symbols
from sympy import Eq
from sympy import tan

NAME = "PRESSURE"

pa, atm, mmhg, psi= symbols("pa atm mmhg psi")

bar, megabar, kbar, dbar, cbar, mbar = symbols(
    "bar, megabar, kbar, dbar, cbar, mbar")

torr, mtorr = symbols("torr mtorr")

equations = [
    Eq(bar, pa / 100000),
    Eq(megabar, bar / 10**6),
    Eq(kbar, bar / 10**3),
    Eq(dbar, bar * 10),
    Eq(cbar, bar * 100),
    Eq(mbar, bar * 1000),
    Eq(pa, atm * 101325),
    Eq(torr, atm * 760),
    Eq(mtorr, torr / 1000),
    Eq(pa, mmhg * 133.322387415),
    Eq(pa, psi * 6894.75729316836142),  # Approx.
]

units = {
    pa: "PASCAL",
    bar: "BAR",
    megabar: "MEGABAR",
    kbar: "KILOBAR",
    dbar: "DECIBAR",
    cbar: "CENTIBAR",
    mbar: "MILLIBAR",
    atm: "ATMOSPHERE",
    psi: "PSI",
    torr: "TORR",
    mtorr: "MILLITORR",
    mmhg: "MMHG",
}

if __name__ == "__main__":
    import sys
    print_conversions(sys.modules[__name__])