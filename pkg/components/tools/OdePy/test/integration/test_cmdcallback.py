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
Test of the CmdCallbackI object
"""

import threading

from ode.testlib import ITest
import ode
import ode.all
from ode.util.concurrency import get_event

class CmdCallback(ode.callbacks.CmdCallbackI):

    def __init__(self, client, handle):
        self.t_lock = threading.RLock()
        self.t_steps = 0
        self.t_finished = 0
        self.t_event = get_event("CmdCallback")
        super(CmdCallback, self).__init__(client, handle)

    def step(self, complete, total, current=None):
        self.t_lock.acquire()
        try:
            self.t_steps += 1
        finally:
            self.t_lock.release()

    def onFinished(self, rsp, status, current=None):
        self.t_lock.acquire()
        try:
            self.t_event.set()
            self.t_finished += 1
        finally:
            self.t_lock.release()

    def assertSteps(self, expected):
        self.t_lock.acquire()
        try:
            assert expected == self.t_steps
        finally:
            self.t_lock.release()

    def assertFinished(self, expectedSteps=None):
        self.t_lock.acquire()
        try:
            assert self.t_finished != 0
            assert not self.isCancelled()
            assert not self.isFailure()
            rsp = self.getResponse()
            if not rsp:
                assert False, "null response"

            elif isinstance(rsp, ode.cmd.ERR):
                msg = "%s\ncat:%s\nname:%s\nparams:%s\n" % \
                    (rsp, rsp.category, rsp.name, rsp.parameters)
                assert False, msg
        finally:
            self.t_lock.release()

        if expectedSteps is not None:
            self.assertSteps(expectedSteps)

    def assertCancelled(self):
        self.t_lock.acquire()
        try:
            assert self.t_finished != 0
            assert self.isCancelled()
        finally:
            self.t_lock.release()


class TestCmdCallback(ITest):

    def mktestcb(self, req):
        """
        returns a CmdCallback instance for testing
        """
        client = self.new_client(perms="rw----")
        handle = client.getSession().submit(req)
        return CmdCallback(client, handle)

    # Timing
    # =========================================================================

    def timing(self, millis, steps):
        t = ode.cmd.Timing()
        t.millisPerStep = millis
        t.steps = steps
        return self.mktestcb(t)

    def testTimingFinishesOnLatch(self):
        cb = self.timing(25, 4 * 10)  # Runs 1 second
        cb.t_event.wait(1.500)
        assert 1 == cb.t_finished
        cb.assertFinished(10)  # Modulus-10

    def testTimingFinishesOnBlock(self):
        cb = self.timing(25, 4 * 10)  # Runs 1 second
        cb.block(1500)
        cb.assertFinished(10)  # Modulus-10

    def testTimingFinishesOnLoop(self):
        cb = self.timing(25, 4 * 10)  # Runs 1 second
        cb.loop(3, 500)
        cb.assertFinished(10)  # Modulus-10

    # DoAll
    # =========================================================================

    def doAllOfNothing(self):
        return self.mktestcb(ode.cmd.DoAll())

    def doAllTiming(self, count):
        # 6 ms per timing
        timings = [ode.cmd.Timing(3, 2) for x in range(count)]
        return self.mktestcb(ode.cmd.DoAll(timings, None))

    def testDoNothingFinishesOnLatch(self):
        cb = self.doAllOfNothing()
        cb.t_event.wait(5)
        cb.assertCancelled()

    def testDoNothingFinishesOnLoop(self):
        cb = self.doAllOfNothing()
        cb.loop(5, 1000)
        cb.assertCancelled()

    def testDoAllTimingFinishesOnLoop(self):
        cb = self.doAllTiming(5)
        cb.loop(5, 1000)
        cb.assertFinished()
        # For some reason the number of steps is varying between 10 and 15
