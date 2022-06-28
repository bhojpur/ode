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

from argparse import ArgumentParser

from base import print_conversions
from base import print_python

import os

import angle
import elec
import freq
import length
import power
import pressure
import temp
import _time

ALL = (
        angle, elec, freq, length,
        pressure, power, temp, _time
      )

LOOKUP = dict()
for e in ALL:
    LOOKUP[e.NAME] = e

if __name__ == "__main__":
    parser = ArgumentParser()
    parser.add_argument("--load",
                        action="store_true")
    parser.add_argument("--type")
    parser.add_argument("--python",
                        action="store_true")
    parser.add_argument("--plain",
                        action="store_true")
    ns = parser.parse_args()
    which = None
    if ns.type:
        which = (LOOKUP[ns.type],)
    else:
        which = ALL

    if ns.load:
        dir = os.path.dirname(__file__)
        efile = os.path.join(dir, "equations.py")
        execfile(efile)
        for x in which:
            print(x.NAME)
            sources = EQUATIONS[x.NAME]
            for source, targets in sorted(sources.items()):
                for target, eqn in sorted(targets.items()):
                    print(source, target, eqn)
    else:
        if ns.python:
            print_python(*which)
        elif ns.plain:
            print_conversions(*which)