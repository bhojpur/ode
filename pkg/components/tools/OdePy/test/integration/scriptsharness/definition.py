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
# THE SOFTWARE..

import sys
import types

import ode
import ode.scripts as sc

client = sc.client("script_1", """
    This is a test script used to test the basic parsing functionality
    and attempts to interaction with the server
    """,
                   sc.Int("myint"),
                   sc.Long("mylong"),
                   sc.Bool("mybool"),
                   sc.String("mystring"),
                   sc.String("myoptional", optional=True)
                   )

assert isinstance(client, types.TupleType)

self = sys.argv[0]
cfg = self.replace("py", "cfg")

real_client = ode.client(["--Ice.Config=%s" % cfg])
parse_only = real_client.getProperty("ode.script.parse")
assert parse_only == "true"
