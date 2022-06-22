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
   Test of the ode.scripts comparison functionality
"""

import ode
from ode.scripts import Long, List, validate_inputs
from ode.rtypes import rmap, rint, rlong, rstring, rlist, rset, unwrap

class TestPrototypes(object):

    def testRSetRInt(self):
        params = ode.grid.JobParams()
        param = ode.grid.Param()
        param.prototype = rset(rint(0))
        params.inputs = {"a": param}

        input = rset(rint(1))
        inputs = {"a": input}
        assert "" == validate_inputs(params, inputs)

    # Nested lists
    def testRListRInt(self):
        params = ode.grid.JobParams()
        param = ode.grid.Param()
        param.prototype = rlist(rint(0))
        params.inputs = {"a": param}

        input = rlist(rint(1))
        inputs = {"a": input}
        assert "" == validate_inputs(params, inputs)

    def testRListRList(self):
        params = ode.grid.JobParams()
        param = ode.grid.Param()
        param.prototype = rlist(rlist())
        params.inputs = {"a": param}

        input = rlist(rlist(rint(1), rstring("a")))
        inputs = {"a": input}
        assert "" == validate_inputs(params, inputs)

    def testRListRListRString(self):
        params = ode.grid.JobParams()
        param = ode.grid.Param()
        param.prototype = rlist(rlist(rstring("")))
        params.inputs = {"a": param}

        input = rlist(rlist(rstring("a")))
        inputs = {"a": input}
        assert "" == validate_inputs(params, inputs)

        input.val[0].val.insert(0, rint(1))
        assert not "" == validate_inputs(params, inputs)

    # Nested maps
    def testRMapRInt(self):
        params = ode.grid.JobParams()
        param = ode.grid.Param()
        param.prototype = rmap({"b": rint(0)})
        params.inputs = {"a": param}

        input = rmap({"b": rint(1)})
        inputs = {"a": input}
        assert "" == validate_inputs(params, inputs)

    def testRMapRMap(self):
        params = ode.grid.JobParams()
        param = ode.grid.Param()
        param.prototype = rmap({"b": rmap({})})
        params.inputs = {"a": param}

        input = rmap({"b": rmap({"l": rlong(0)})})
        inputs = {"a": input}
        assert "" == validate_inputs(params, inputs)

    def testRMapRMapRInt(self):
        params = ode.grid.JobParams()
        param = ode.grid.Param()
        param.prototype = rmap({"b": rmap({"c": rint(0)})})
        params.inputs = {"a": param}

        input = rmap({"b": rmap({"c": rint(1)})})
        inputs = {"a": input}
        assert "" == validate_inputs(params, inputs)

    # Other
    def testAllParametersChecked(self):
        params = ode.grid.JobParams()
        param = ode.grid.Param()
        param.prototype = rlist(rstring(""))
        params.inputs = {"a": param}

        input = rlist(rstring("foo"), rint(1))
        inputs = {"a": input}
        assert not "" == validate_inputs(params, inputs)

    # Bugs
    def testTicket2323Min(self):
        params = ode.grid.JobParams()
        # Copied from integration/scripts.py:testUploadOfficialScripts
        param = Long('longParam', True, description='theDesc', min=long(1),
                     max=long(10), values=[rlong(5)])
        assert 1 == param.min.getValue(), \
            "Min value not correct:" + str(param.min)
        assert 10 == param.max.getValue(), \
            "Max value not correct:" + str(param.max)
        assert 5 == param.values.getValue()[0].getValue(), \
            "First option value not correct:" + str(param.values)

        params.inputs = {"a": param}
        inputs = {"a": rlong(5)}
        errors = validate_inputs(params, inputs)
        assert "" == errors, errors

    def testTicket2323List(self):
        param = List('listParam', True, description='theDesc',
                     values=[rlong(5)])
        assert [5] == unwrap(param.values), str(param.values)
