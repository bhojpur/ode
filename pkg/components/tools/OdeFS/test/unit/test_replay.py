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
   Replays all the log records under records/ to see if they cause exceptions
"""

import pytest
import logging

from drivers import MockMonitor, Replay, with_driver

from path import path

logging.basicConfig(level=logging.WARN)

class TestReplay(object):

    def teardown_method(self, method):
        MockMonitor.static_stop()

    @with_driver
    def XestOutOfSyncFind(self):
        """
        This was used to print out a record so that we could detect
        what the groups of used files were.
        """
        source = path(".") / "test" / "records" / "outofsync.txt"
        l = len(self.dir)

        class MyReplay(Replay):

            def fileset(self, timestamp, data):
                f = Replay.fileset(self, timestamp, data)
                print("=" * 80)
                for k, v in f.items():
                    print(k[l:])
                    for i in v:
                        print("\t", i[l:])
                return f
        MyReplay(self.dir, source, None).run()
        self.driver.run()

    @pytest.mark.broken(ticket="12566")
    @with_driver
    def testOutOfSync(self):
        source = path(".") / "test" / "records" / "outofsync.txt"
        Replay(self.dir, source, self.driver).run()
        self.driver.run()
