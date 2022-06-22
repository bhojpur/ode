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
   Integration test focused on the ode.api.IConfig interface.
"""

import ode
import pytest
from ode.testlib import ITest

class TestConfig(ITest):

    EXPECTS = (
        (".*", ("ode.router.insecure",)),
        ("^ode.*", ("ode.router.insecure",)),
        (".*router.*", ("ode.router.insecure",)),
        ("ode.db", ("ode.db.authority",)),
    )

    @pytest.mark.parametrize("data", EXPECTS)
    def testValuesRegex(self, data):
        regex, contains = data
        cfg = self.sf.getConfigService()
        rv = cfg.getConfigValues(regex)
        for c in contains:
            assert c in rv

    def testDefaults(self):
        cfg = self.sf.getConfigService()
        with pytest.raises(ode.SecurityViolation):
            cfg.getConfigDefaults()

    def testRootDefaults(self):
        cfg = self.root.sf.getConfigService()
        defs = cfg.getConfigDefaults()
        for x in (
            "ode.version",
            "ode.db.name",
        ):
            assert x in defs

    def testClientDefaults(self):
        cfg = self.sf.getConfigService()
        defs = cfg.getClientConfigDefaults()
        assert "ode.client.ui.menu.dropdown.colleagues.label" in defs

    def testClientValues(self):
        # Not sure what's in this so just calling
        cfg = self.sf.getConfigService()
        defs = cfg.getClientConfigValues()
        assert defs is not None
