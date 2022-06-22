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
   Test of the differences in the OdePy mapping.
"""

import pytest
import ode
import Ice

from ode.clients import BaseClient

class test_client(BaseClient):
    def __init__(self):
        pass

    def __del__(self):
        pass

class TestPythonOnly(object):

    def testRepairArguments(self):
        t = test_client()
        ar = ["a", "b"]
        id = Ice.InitializationData()
        pm = {'A': 1}
        ho = "host"
        po = 4064
        no = None
        assert [no == no, no, no, no], t._repair(no, no, no, no, no)
        assert [ar == no, no, no, no], t._repair(ar, no, no, no, no)
        assert [no == id, no, no, no], t._repair(id, no, no, no, no)
        assert [no == no, ho, no, no], t._repair(ho, no, no, no, no)
        assert [no == no, ho, po, no], t._repair(ho, po, no, no, no)
        assert [no == no, no, no, pm], t._repair(pm, no, no, no, no)
        # All mixed up
        assert [ar == id, ho, po, pm], t._repair(id, pm, po, ho, ar)
        # Duplicates
        pytest.raises(ode.ClientError, t._repair, id, id, no, no, no)
        pytest.raises(ode.ClientError, t._repair, ho, ho, no, no, no)
