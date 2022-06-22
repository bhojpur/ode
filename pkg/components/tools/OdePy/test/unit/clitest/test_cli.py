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
   Test of the ode cli base class including line parsing.t
"""

import pytest

from ode.cli import CLI, NonZeroReturnCode
from ode.plugins.basics import LoadControl

class TestCli(object):

    def testMultipleLoad(self):
        """
        In DropBox, the loading of multiple CLIs seems to
        lead to the wrong context being assigned to some
        controls.

        See #4749
        """
        import random
        from threading import Thread, Event

        event = Event()

        class T(Thread):
            def run(self, *args):
                pause = random.random()
                event.wait(pause)
                self.cli = CLI()
                self.cli.loadplugins()
                self.con = self.cli.controls["admin"]
                self.cmp = self.con.ctx

        threads = [T() for x in range(20)]
        for t in threads:
            t.start()
        event.set()
        for t in threads:
            t.join()

        assert len(threads) == len(set([t.cli for t in threads]))
        assert len(threads) == len(set([t.con for t in threads]))
        assert len(threads) == len(set([t.cmp for t in threads]))

    def testLoad(self, tmpdir):
        tmpfile = tmpdir.join('test')
        tmpfile.write("foo")
        self.cli = CLI()
        self.cli.register("load", LoadControl, "help")

        with pytest.raises(NonZeroReturnCode):
            self.cli.invoke("load %s" % tmpfile, strict=True)

        self.cli.invoke("load -k %s" % tmpfile, strict=True)
        self.cli.invoke("load --keep-going %s" % tmpfile, strict=True)
