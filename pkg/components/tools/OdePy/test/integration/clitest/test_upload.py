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

from ode.testlib.cli import CLITest
from ode.cli import NonZeroReturnCode
from ode.plugins.obj import ObjControl
from ode.plugins.upload import UploadControl
from ode.util.temp_files import create_path

class TestUpload(CLITest):

    def setup_method(self, method):
        super(TestUpload, self).setup_method(method)
        self.cli.register("upload", UploadControl, "TEST")
        self.cli.register("obj", ObjControl, "TEST")
        self.args += ["upload"]

    def upload(self, capfd):
        self.cli.invoke(self.args, strict=True)
        return capfd.readouterr()[0]

    def check_file_name(self, originalFile, filename):
        args = self.login_args() + ["obj", "get", originalFile]
        self.cli.invoke(args + ["name"], strict=True)
        name = self.cli.get("tx.state").get_row(0)
        assert filename.name == name

    def testUploadSingleFile(self, capfd):
        f = create_path(suffix=".txt")
        self.args += [str(f)]
        out = self.upload(capfd)
        self.check_file_name(out, f)

    def testUploadMultipleFiles(self, capfd):
        f1 = create_path(suffix=".txt")
        f2 = create_path(suffix=".txt")
        self.args += [str(f1), str(f2)]
        out = self.upload(capfd)
        ids = out.split(":")[1].split(",")
        self.check_file_name("OriginalFile:%s" % ids[0], f1)
        self.check_file_name("OriginalFile:%s" % ids[1], f2)

    def testUploadBadFile(self, capfd):
        f1 = create_path(suffix=".txt")
        f2 = self.uuid() + ""
        self.args += [str(f1), str(f2)]
        with pytest.raises(NonZeroReturnCode):
            self.upload(capfd)
