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
   Test of the export plugin
"""

import os
from path import path
from ode.cli import CLI, NonZeroReturnCode
from ode.plugins.export import ExportControl
from ode.util.temp_files import create_path

odeDir = path(os.getcwd()) / "build"

class MockCLI(CLI):

    def conn(self, args):
        return MockClient()


class MockClient(object):

    def getSession(self, *args):
        return MockSession()


class MockSession(object):

    def createExporter(self):
        return MockExporter()


class MockExporter(object):

    def all(self, *args, **kwargs):
        pass

    def __getattr__(self, key):
        if key == "generateTiff":
            return self.generateTiff
        return self.all

    def generateTiff(self, *args):
        return 1


class TestExport(object):

    def setup_method(self, method):
        self.cli = MockCLI()
        self.cli.register("x", ExportControl, "TEST")
        self.p = create_path()
        self.p.remove()

    def invoke(self, string):
        self.cli.invoke(string, strict=True)

    def testSimpleExport(self):
        self.invoke("x -f %s Image:3" % self.p)

    def testStdOutExport(self):
        """
        "-f -" should export to stdout. See ticket:7106
        """
        self.invoke("x -f - Image:3")

    def testNoStdOutExportForDatasets(self):
        try:
            self.invoke("x -f - --iterate Dataset:3")
            assert False, "ZeroReturnCode??"
        except NonZeroReturnCode:
            pass
