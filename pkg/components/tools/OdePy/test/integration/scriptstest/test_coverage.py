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
   Integration test demonstrating various script creation methods
"""

from ode.testlib import ITest
import pytest

import ode

class TestCoverage(ITest):

    @classmethod
    def setup_class(cls):
        """
        getScripts returns official scripts,
        several of which are shipped with Bhojpur ODE.
        """
        super(TestCoverage, cls).setup_class()
        cls.rs = cls.root.sf.getScriptService()
        cls.us = cls.client.sf.getScriptService()
        assert len(cls.rs.getScripts()) > 0
        assert len(cls.us.getScripts()) > 0
        assert len(cls.us.getUserScripts([])) == 0  # New user. No scripts

    def testGetScriptWithDetails(self):
        scriptList = self.us.getScripts()
        script = scriptList[0]
        scriptMap = self.us.getScriptWithDetails(script.id.val)

        assert len(scriptMap) == 1

    def testUploadAndScript(self):
        scriptID = self.us.uploadScript(
            "/ODE/Foo.py",
            """if True:
            import ode
            import ode.grid as OG
            import ode.rtypes as OR
            import ode.scripts as OS
            client = OS.client("testUploadScript")
            print "done"
            """)
        return scriptID

    def testUserCantUploadOfficalScript(self):
        with pytest.raises(ode.SecurityViolation):
            self.us.uploadOfficialScript(
                "/%s/fails.py" % self.uuid(),
                """if True:
                import ode
                """)
