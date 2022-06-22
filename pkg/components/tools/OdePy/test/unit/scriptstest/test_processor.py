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
   Test of the Process facility independent of Ice.
"""

import os
import sys
import logging
import subprocess

logging.basicConfig(level=logging.DEBUG)

import Ice
import ode.processor
import ode.util
import ode.util.concurrency
from functools import wraps

def pass_through(arg):
    return arg

def make_client(self):
    self.client = None
    self.uuid = "mock_uuid"

def _term(self, *args):
    self.rcode = -9

ode.processor.ProcessI._term = _term
ode.processor.ProcessI.make_client = make_client

class Callback(object):
    def __init__(self):
        self._finished = None
        self._cancelled = None
        self._killed = None

    def ice_getIdentity(self):
        return Ice.Identity("a", "b")

    def ice_oneway(self):
        return self

    def processFinished(self, rc):
        self._finished = rc

    def processCancelled(self, success):
        self._cancelled = success

    def processKilled(self, success):
        self._killed = success


class MockPopen(object):
    def __init__(self, *args, **kwargs):
        self.args = args
        self.kwargs = kwargs
        self.rcode = None
        self.pid = 1

    def poll(self):
        return self.rcode

    def wait(self):
        return self.rcode

    def kill(self, *args):
        self.rcode = -9
        return self.rcode


def with_process(func, Popen=MockPopen):
    """ Decorator for running a test with a Process """
    def handler(*args, **kwargs):
        self = args[0]
        self.process = ode.processor.ProcessI(
            self.ctx, sys.executable, self.props(), self.params(),
            Popen=Popen, callback_cast=pass_through)
        try:
            func(*args, **kwargs)
        finally:
            self.process.cleanup()
    return wraps(func)(handler)


class TestProcess(object):

    def setup_method(self, method):
        self.log = logging.getLogger("TestProcess")
        self.ctx = ode.util.ServerContext(
            server_id='mock', communicator=None,
            stop_event=ode.util.concurrency.get_event())

    def teardown_method(self, method):
        self.log.info("stop_event")
        self.ctx.stop_event.set()

    def props(self):
        p = {
            "ode.user": "sessionId",
            "ode.pass": "sessionId",
            "Ice.Default.Router": "foo"}
        return p

    def params(self):
        params = ode.grid.JobParams()
        params.name = "name"
        params.description = "description"
        params.inputs = {}
        params.outputs = {}
        params.stdoutFormat = "text/plain"
        params.stderrFormat = "text/plain"
        return params

    #
    # Env
    #

    def testEnvironment(self):
        env = ode.util.Environment("PATH")
        env.append("PATH", os.pathsep.join(["bob", "cat"]))
        env.append("PATH", os.path.join(os.getcwd(), "lib"))

    def testEnvironment2(self):
        ode.processor.ProcessI(self.ctx, sys.executable,
                                 self.props(), self.params())

    #
    # MockPopen
    #

    @with_process
    def testMockPopenPoll(self):
        self.process.activate()
        assert None == self.process.poll()
        self.process.popen.rcode = 1
        assert 1 == self.process.poll().val
        # Now wait should return too
        assert 1 == self.process.wait()

    @with_process
    def testMockPopenWait(self):
        self.process.activate()
        assert True == self.process.isActive()
        self.process.popen.rcode = 1
        assert 1 == self.process.wait()
        assert 1 == self.process.poll().val

    @with_process
    def testMockPopenAlreadyDone(self):
        assert not self.process.isActive()
        self.process.activate()
        assert self.process.isActive()
        assert not self.process.isFinished()
        assert not self.process.alreadyDone()
        self.process.deactivate()
        assert not self.process.isActive()
        assert self.process.isFinished()
        assert self.process.alreadyDone()

    @with_process
    def testCallback(self):
        callback = Callback()
        self.process.activate()
        self.process.registerCallback(callback)
        self.process.allcallbacks("processCancelled", True)
        assert callback._cancelled

    #
    # Real calls
    #

    def testPopen(self):
        f = open(str(self.process.script_path), "w")
        f.write("""
print "Hello"
        """)
        f.close()
        self.process.activate()
        assert None != self.process.wait()
        assert None != self.process.poll()
    testPopen = with_process(testPopen, subprocess.Popen)

    def testParameters(self):
        p = self.props()
        p["ode.scripts.parse"] = "1"
        f = open(str(self.process.script_path), "w")
        f.write("""
import ode, ode.scripts s
client = s.client("name","description",s.Long("l"))
        """)
        f.close()
        self.process.activate()
        self.process.wait()
        assert self.process.poll()
    testParameters = with_process(testParameters, subprocess.Popen)

    def testKillProcess(self):
        f = open(str(self.process.script_path), "w")
        f.write("import time\n")
        f.write("time.sleep(100)\n")
        f.close()
        self.process.activate()
        assert not self.process.poll()
        self.process.cleanup()
    testKillProcess = with_process(testKillProcess, subprocess.Popen)
