#!/usr/bin/env python
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
   Function for setting the working directory for a
   Bhojpur ODE installation on Windows, since relative paths
   are not supported.
"""

from __future__ import print_function

from builtins import str
import sys
from extras.path import path
import fileinput

dummy = object()

def win_set_path(new_name=dummy, old_name=r"c:\ode_dist", dir=path(".")):
    """
    Parses the Windows cfg and xml files and
    replaces the default "c:\ode_dist" with the
    given value.
    """

    cfg = dir / "etc" / "Windows.cfg"
    xml = dir / "etc" / "grid" / "windefault.xml"

    if new_name == dummy:
        new_name = dir.abspath()
    if new_name is None or old_name is None:
        raise Exception("Arguments cannot be None")

    if new_name.find(" ") >= 0:
        raise Exception("Contains whitespace: '%s'" % new_name)

    new_name = path(new_name).abspath()
    old_name = path(old_name).abspath()

    print("Converting from %s to %s" % (old_name, new_name))

    new_name2 = new_name.replace("\\", "\\\\")
    old_name2 = old_name.replace("\\", "\\\\")

    count = 0
    for line in fileinput.input([str(cfg), str(xml)], inplace=1):
        if line.find(old_name) >= 0:
            count += 1
            print(line.replace(old_name, new_name), end=' ')
        elif line.find(old_name2) >= 0:
            count += 1
            print(line.replace(old_name2, new_name2), end=' ')
        else:
            print(line, end=' ')

    fileinput.close()
    print("Changes made: %s" % count)
    return count

if __name__ == "__main__":
    try:
        if "-h" in sys.argv or "--help" in sys.argv:
            pass
        elif len(sys.argv) == 1:
            win_set_path()
            sys.exit(0)
        elif len(sys.argv) == 2:
            win_set_path(new_name=sys.argv[1])
            sys.exit(0)
        elif len(sys.argv) == 3:
            win_set_path(old_name=sys.argv[1], new_name=sys.argv[2])
            sys.exit(0)
    except Exception as e:
        print("Failed to set path: ", e)
        sys.exit(1)

    print("""Usage: %s [oldname] newname

Replaces the [oldname] entries in the Windows configuration files
with [newname]. By default, [oldname] is set to "c:\ode_dist"
        """ % sys.argv[0])
    sys.exit(2)