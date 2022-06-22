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
    Tests the state which is held by MonitorClients
"""

import pytest
import logging
import time

LOGFORMAT = "%(asctime)s %(levelname)-5s [%(name)40s] " \
            "(%(threadName)-10s) %(message)s"
logging.basicConfig(level=0, format=LOGFORMAT)

from ode.util import make_logname

import fsDropBoxMonitorClient as fsDBMC

def nullcb(*args):
    pass

def listcb(l):
    def cb(*args):
        l.append(args)
    return cb

def clearcb(log, state, key):
    def cb(*args):
        assert key == args[0]
        log.info("clearcb called: %s", args[0])
        state.clear(args[0])
    return cb

class TestState(object):

    def setup_method(self, method):
        self.s = fsDBMC.MonitorState()
        self.log = logging.getLogger(make_logname(self))

    def teardown_method(self, method):
        self.s.stop()

    @pytest.mark.broken(ticket="12566")
    def testEmpty(self):
        self.s.update({}, 0, nullcb)

    @pytest.mark.broken(ticket="12566")
    def testSimple(self):
        self.s.update({'file1': ['file1', 'file2']}, 0, nullcb)

    @pytest.mark.broken(ticket="12566")
    def testTimerCalled(self):
        l = []
        self.s.update({'file1': ['file1', 'file2']}, 0, listcb(l))
        time.sleep(0.25)
        assert 1 == len(l)

    @pytest.mark.broken(ticket="12566")
    def testMultipleInsert(self):
        l = []
        m = {
            'file1': ['file1', 'file2'],
            'file2': ['file1', 'file2'],
            'file3': ['file1', 'file2', 'file3']
        }
        self.s.update(m, 0, listcb(l))
        time.sleep(0.25)
        assert 1 == len(l)

    @pytest.mark.broken(ticket="12566")
    def testAddThenReAdd(self):
        l = []
        self.s.update({'file1': ['file1', 'file2']}, 0.1, listcb(l))
        self.s.update({'file1': ['file1', 'file2']}, 0.1, listcb(l))
        time.sleep(0.25)
        assert 1 == len(l)

    @pytest.mark.broken(ticket="12566")
    def testAddThenModify(self):
        l = []
        self.s.update({'file1': ['file1', 'file2']}, 0.1, listcb(l))
        self.s.update({'file1': ['file1', 'file3']}, 0.0, listcb(l))
        time.sleep(0.25)
        assert 1 == len(l)

    @pytest.mark.broken(ticket="12566")
    def testEntryMoved1(self):
        l = []
        self.s.update({'file1': ['file1']}, 0.1, listcb(l))
        assert 1 == self.s.keys()
        self.s.update({'file2': ['file1', 'file2']}, 0.1, listcb(l))
        assert 2 == self.s.keys()
        time.sleep(0.25)
        assert 1 == len(l)

    @pytest.mark.broken(ticket="12566")
    def testEntryMoved2(self):
        self.s.update(
            {'file1': ['file1']}, 0.1, clearcb(self.log, self.s, 'file1'))
        assert 1 == len(self.s.keys())
        assert 1 == self.s.count()
        self.s.update(
            {'file2': ['file1', 'file2']}, 0.1,
            clearcb(self.log, self.s, 'file2'))
        assert 2 == len(self.s.keys())
        assert 1 == self.s.count()
        time.sleep(0.25)
        assert 0 == len(self.s.keys())
        assert 0 == self.s.count()

    @pytest.mark.broken(ticket="12566")
    def testEntryOutOfSyncSubsume(self):
        self.s.update({'file1': ['file1']}, 0.1, nullcb)
        assert 1 == len(self.s.keys())
        self.s.update({'file2': ['file2']}, 0.1, nullcb)
        assert 2 == len(self.s.keys())
        self.s.update({'file2': ['file1', 'file2']}, 0.1, nullcb)
        assert 2 == len(self.s.keys())

    @pytest.mark.broken(ticket="12566")
    def testEntryOutOfSyncSteal(self):
        self.s.update({'file1': ['file1', 'file3']}, 0.1, nullcb)
        assert 2 == len(self.s.keys())
        self.s.update({'file2': ['file2']}, 0.1, nullcb)
        assert 3 == len(self.s.keys())
        self.s.update({'file2': ['file2', 'file3']}, 0.1, nullcb)
        assert 3 == len(self.s.keys())
