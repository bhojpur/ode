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

import pytest
from ode.cli import CLI
from ode.plugins.hql import HqlControl, BLACKLISTED_KEYS, WHITELISTED_VALUES

class TestHql(object):

    def setup_method(self, method):
        self.cli = CLI()
        self.cli.register("hql", HqlControl, "TEST")
        self.args = ["hql"]

    def testHelp(self):
        self.args += ["-h"]
        self.cli.invoke(self.args, strict=True)

    @pytest.mark.parametrize("key", BLACKLISTED_KEYS)
    def testFilterBlacklist(self, key):
        output = self.cli.controls["hql"].filter({key: 1})
        assert output == {}

    @pytest.mark.parametrize("key", ["rois", "groupExperimenterMap"])
    def testFilterLoaded(self, key):
        output = self.cli.controls["hql"].filter({"_" + key + "Loaded": 1})
        assert output == {}

    @pytest.mark.parametrize(
        ("value", "outcome"),
        [("owner=None;group=None", {}),
         ("owner=1", {"details": "owner=1"})])
    def testFilterDetails(self, value, outcome):
        output = self.cli.controls["hql"].filter({"_details": value})
        assert output == outcome

    @pytest.mark.parametrize("multi_value", [[0, 1]])
    def testFilterMultiValue(self, multi_value):
        output = self.cli.controls["hql"].filter({'key': multi_value})
        assert output == {}

    @pytest.mark.parametrize("empty_value", [None, [], {}])
    def testFilterEmptyValue(self, empty_value):
        output = self.cli.controls["hql"].filter({'key': empty_value})
        assert output == {}

    @pytest.mark.parametrize("value", WHITELISTED_VALUES)
    def testFilterWhitelist(self, value):
        output = self.cli.controls["hql"].filter({'key': value})
        assert output == {'key': value}

    def testFilterStrip(self):
        output = self.cli.controls["hql"].filter({'_key': 1})
        assert output == {'key': 1}
