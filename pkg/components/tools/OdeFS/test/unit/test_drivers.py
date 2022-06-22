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
   Utility classes for generating file-system-like events
   for testing.
"""

import ode.grid.monitors as monitors
import uuid

from path import path
from ode.util.temp_files import create_path
from drivers import CallbackEvent, InfoEvent, DirInfoEvent
from drivers import MockMonitor, Driver, Simulator

class TestDrivers(object):

    """
    Simple test to test the testing functionality (driver)
    """

    def setup_method(self, method):
        self.client = MockMonitor()
        self.driver = Driver(self.client)

    def teardown_method(self, method):
        self.client.stop()
        MockMonitor.static_stop()

    def assertEventCount(self, count):
        assert count == len(self.client.events)

    def testCallback(self):
        l = []
        self.driver.add(CallbackEvent(1, lambda: l.append(True)))
        self.driver.run()
        assert 1 == len(l)
        self.assertEventCount(0)  # Events don't get passed on callbacks

    def testInfo(self):
        self.driver.add(InfoEvent(1, monitors.EventInfo()))
        self.driver.run()
        self.assertEventCount(1)


class TestSimulator(object):

    """
    Simple test to test the testing functionality (simulator)
    """

    def setup_method(self, method):
        self.uuid = str(uuid.uuid4())
        self.dir = create_path(folder=True) / self.uuid
        self.dir.makedirs()
        self.sim = Simulator(self.dir)
        self.driver = Driver(self.sim)

    def teardown_method(self, method):
        assert 0 == len(self.driver.errors)

    def assertErrors(self, count=1):
        assert count == len(self.driver.errors)
        for i in range(count):
            self.driver.errors.pop()

    def testRelativeTest(self):
        assert (self.dir / "foo").parpath(self.dir)
        assert (self.dir / "foo" / "bar" / "baz").parpath(self.dir)
        # Not relative
        assert not (path("/")).parpath(self.dir)
        assert not (path("/root")).parpath(self.dir)
        assert not (path(".")).parpath(self.dir)

    def testBad(self):
        self.driver.add(InfoEvent(1, monitors.EventInfo(
            "foo", monitors.EventType.Create)))
        self.driver.run()
        self.assertErrors()

    def testSimpleCreate(self):
        self.driver.add(InfoEvent(1, monitors.EventInfo(
            self.dir / "foo", monitors.EventType.Create)))
        self.driver.run()

    def testBadCreate(self):
        self.driver.add(InfoEvent(1, monitors.EventInfo(
            self.dir / "foo", monitors.EventType.Create)))
        self.driver.add(InfoEvent(1, monitors.EventInfo(
            self.dir / "foo", monitors.EventType.Create)))
        self.driver.run()
        self.assertErrors()

    def testBadModify(self):
        self.driver.add(InfoEvent(1, monitors.EventInfo(
            self.dir / "foo", monitors.EventType.Modify)))
        self.driver.run()
        self.assertErrors()

    def testSimpleModify(self):
        self.driver.add(InfoEvent(1, monitors.EventInfo(
            self.dir / "foo", monitors.EventType.Create)))
        self.driver.add(InfoEvent(1, monitors.EventInfo(
            self.dir / "foo", monitors.EventType.Modify)))
        self.driver.run()

    def testBadDelete(self):
        self.driver.add(InfoEvent(1, monitors.EventInfo(
            self.dir / "foo", monitors.EventType.Delete)))
        self.driver.run()
        self.assertErrors()

    def testSimpleDelete(self):
        self.driver.add(InfoEvent(1, monitors.EventInfo(
            self.dir / "foo", monitors.EventType.Create)))
        self.driver.add(InfoEvent(1, monitors.EventInfo(
            self.dir / "foo", monitors.EventType.Delete)))
        self.driver.run()

    def testSimpleDeleteWithModify(self):
        self.driver.add(InfoEvent(1, monitors.EventInfo(
            self.dir / "foo", monitors.EventType.Create)))
        self.driver.add(InfoEvent(1, monitors.EventInfo(
            self.dir / "foo", monitors.EventType.Modify)))
        self.driver.add(InfoEvent(1, monitors.EventInfo(
            self.dir / "foo", monitors.EventType.Delete)))
        self.driver.run()

    def testDirectoryMethodsInfo(self):
        self.driver.add(InfoEvent(1, monitors.EventInfo(
            self.dir / "foo", monitors.EventType.Create)))
        self.driver.add(InfoEvent(1, monitors.EventInfo(
            self.dir / "foo", monitors.EventType.Modify)))
        self.driver.add(InfoEvent(1, monitors.EventInfo(
            self.dir / "foo", monitors.EventType.Delete)))
        self.driver.run()

    def testDirectoryMethodsDirInfo(self):
        self.driver.add(DirInfoEvent(1, monitors.EventInfo(
            self.dir / "dir", monitors.EventType.Create)))
        self.driver.add(DirInfoEvent(1, monitors.EventInfo(
            self.dir / "dir", monitors.EventType.Modify)))
        self.driver.add(DirInfoEvent(1, monitors.EventInfo(
            self.dir / "dir", monitors.EventType.Delete)))
        self.driver.run()

    def testDirectoryDoesntExistOnModify(self):
        self.driver.add(DirInfoEvent(1, monitors.EventInfo(
            self.dir / "dir", monitors.EventType.Modify)))
        self.driver.run()
        self.assertErrors()

    def testDirectoryDoesntExistOnDelete(self):
        self.driver.add(DirInfoEvent(1, monitors.EventInfo(
            self.dir / "dir", monitors.EventType.Delete)))
        self.driver.run()
        self.assertErrors()
