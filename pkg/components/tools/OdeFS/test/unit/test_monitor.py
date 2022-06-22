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
   Unit tests for the directory timeout logic.
"""

import pytest
import logging
import time

import ode
import ode.all
import ode.grid.monitors as monitors

from drivers import DirInfoEvent, MockMonitor, with_driver

logging.basicConfig(level=logging.INFO)

class TestMonitor(object):

    def teardown_method(self, method):
        MockMonitor.static_stop()

    def testBadId(self):
        with pytest.raises(ode.ApiUsageException):
            MockMonitor().fsEventHappened('foo', [])

    def testBadFileId(self):
        # Could cause infinite loop
        with pytest.raises(ode.ApiUsageException):
            MockMonitor().fsEventHappened('', [monitors.EventInfo()])

    def testEmptyAdd(self):
        MockMonitor().fsEventHappened('', [])  # Does nothing.

    @pytest.mark.broken(ticket="12566")
    @with_driver
    def testBasicAdd(self):
        self.driver.add(DirInfoEvent(1, monitors.EventInfo(
            self.dir / "root" / "dirtimeout",
            monitors.EventType.Create)))
        self.driver.run()

    @pytest.mark.broken(ticket="12566")
    @with_driver
    def testWithSingleImport(self):
        f = self.dir / "root" / "file"
        self.client.files = {str(f): [str(f)]}
        self.driver.add(DirInfoEvent(0, monitors.EventInfo(
            self.dir / "root" / "file", monitors.EventType.Create)))
        self.driver.run()
        time.sleep(0.25)

    @pytest.mark.broken(ticket="12566")
    @with_driver
    def testWithMultiImport(self):
        f1 = str(self.dir / "root" / "file1")
        f2 = str(self.dir / "root" / "file2")
        f3 = str(self.dir / "root" / "file3")
        f4 = str(self.dir / "root" / "file4")
        self.client.files = {f1: [f1, f2, f3, f4]}
        self.client.setDirImportWait(1)
        self.driver.add(DirInfoEvent(0.0, monitors.EventInfo(
            f1, monitors.EventType.Create)))
        self.driver.add(DirInfoEvent(100, monitors.EventInfo(
            f2, monitors.EventType.Create)))
        self.driver.add(DirInfoEvent(200, monitors.EventInfo(
            f3, monitors.EventType.Create)))
        self.driver.add(DirInfoEvent(300, monitors.EventInfo(
            f4, monitors.EventType.Create)))
        time.sleep(1)
        self.driver.run()

    @pytest.mark.broken(ticket="12566")
    @with_driver
    def testDirectoryInDirectory(self):
        self.driver.add(DirInfoEvent(1, monitors.EventInfo(
            self.dir / "root" / "dir", monitors.EventType.Create)))
        self.driver.add(DirInfoEvent(1, monitors.EventInfo(
            self.dir / "root" / "dir" / "dir", monitors.EventType.Create)))
        self.driver.run()
