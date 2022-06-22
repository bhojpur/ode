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
   Test of the scripts plugin
"""

from ode.testlib.cli import CLITest
from ode.plugins.script import ScriptControl
from ode.util.temp_files import create_path

scriptText = """
import ode, ode.scripts as s
from ode.rtypes import *

client = s.client("testFullSession", "simple ping script", \
s.Long("a").inout(), s.String("b").inout())
client.setOutput("a", rlong(0))
client.setOutput("b", rstring("c"))
client.closeSession()
"""

class TestScript(CLITest):

    def setup_method(self, method):
        super(TestScript, self).setup_method(method)
        self.cli.register("script", ScriptControl, "TEST")
        self.args += ["script"]

    def testList(self):
        self.args += ["list"]
        self.cli.invoke(self.args, strict=True)  # Throws NonZeroReturnCode

    def testDemo(self):
        self.args += ["demo"]
        self.cli.invoke(self.args, strict=True)

    def testFullSession(self):
        p = create_path(suffix=".py")
        p.write_text(scriptText)
        # Sets current script
        self.cli.invoke(self.args + ["upload", str(p)], strict=True)
        self.cli.invoke(self.args + ["list", "user"], strict=True)

    # Replace subcommand
    # ========================================================================
    def testReplace(self):
        p = create_path(suffix=".py")
        p.write_text(scriptText)

        # test replace with user script (not official)
        # Sets current script
        self.cli.invoke(self.args + ["upload", str(p)], strict=True)
        newId = self.cli.get("script.file.id")
        self.cli.invoke(self.args + ["list", "user"], strict=True)
        replaceArgs = self.args + ["replace", str(newId), str(p)]
        self.cli.invoke(replaceArgs, strict=True)

    def testReplaceOfficial(self):
        p = create_path(suffix=".py")
        p.write_text(scriptText)

        # test replace with official script
        self.args = self.root_login_args() + ["script"]
        uploadArgs = self.args + ["upload", str(p), "--official"]
        self.cli.invoke(uploadArgs, strict=True)  # Sets current script
        newId = self.cli.get("script.file.id")
        self.cli.invoke(self.args + ["list"], strict=True)
        replaceArgs = self.args + ["replace", str(newId), str(p)]
        self.cli.invoke(replaceArgs, strict=True)
