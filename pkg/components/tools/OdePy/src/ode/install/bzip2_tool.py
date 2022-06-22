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
   Function for enabling/disabling the bzip2.dll which
   comes with PyTables.
"""

from __future__ import print_function

import os
import sys

def bzip2_tool(disable=False):
    """
    Renames the bzip2.dll library which comes with PyTables.
    """

    import tables
    f = tables.__file__
    p = os.path.dirname(f)
    p = os.path.abspath(p)
    b = os.path.join(p, "bzip2.dll")
    d = os.path.join(p, "bzip2_DISABLED.dll")
    if disable:
        _swap(b, d)
    else:
        _swap(d, b)

def _swap(f, t):
    if not os.path.exists(f):
        print("%s doesn't exist" % f)
        sys.exit(0)
    os.rename(f, t)

if __name__ == "__main__":
    try:
        if len(sys.argv) == 2:
            which = sys.argv[1]
            if which == "disable":
                which = True
            elif which == "enable":
                which = False
            else:
                print("Unknown command: ", which)
                sys.exit(2)
            bzip2_tool(disable=which)
            sys.exit(0)
    except Exception as e:
        print("bzip2_tool failed: ", e)
        sys.exit(1)

    print("Usage: %s disable|enable" % sys.argv[0])
    sys.exit(2)