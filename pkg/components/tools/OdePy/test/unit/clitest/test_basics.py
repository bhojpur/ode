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

cli = CLI()
cli.loadplugins()
commands = cli.controls.keys()
topics = cli.topics.keys()

class TestBasics(object):

    def testHelp(self):
        self.args = ["help", "-h"]
        cli.invoke(self.args, strict=True)

    @pytest.mark.parametrize('recursive', [None, "--recursive"])
    def testHelpAll(self, recursive):
        self.args = ["help", "--all"]
        if recursive:
            self.args.append(recursive)
        cli.invoke(self.args, strict=True)

    @pytest.mark.parametrize('recursive', [None, "--recursive"])
    @pytest.mark.parametrize('command', commands)
    def testHelpCommand(self, command, recursive):
        self.args = ["help", command]
        if recursive:
            self.args.append(recursive)
        cli.invoke(self.args, strict=True)

    @pytest.mark.parametrize('topic', topics)
    def testHelpTopic(self, topic):
        self.args = ["help", topic, "-h"]
        cli.invoke(self.args, strict=True)

    def testHelpList(self):
        self.args = ["help", "list"]
        cli.invoke(self.args, strict=True)

    def testQuit(object):
        cli.invoke(["quit"], strict=True)

    def testVersion(object):
        cli.invoke(["version"], strict=True)
