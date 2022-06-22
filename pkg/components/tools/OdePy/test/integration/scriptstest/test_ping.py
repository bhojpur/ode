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
   Integration test testing distributed processing via
   ServiceFactoryI.acquireProcessor().
"""

from ode.testlib import ITest
import pytest
import os

import ode
import ode.clients
import ode.model
import ode.api
import uuid

from ode.util.temp_files import create_path, remove_path
from ode.rtypes import rlong, rstring, rmap
from ode.scripts import wait

PINGFILE = """
#!/usr/bin/env python3

import os
import ode, ode.scripts as s
import uuid

#
# Unique name so that IScript does not reject us
# based on duplicate file names.
#
uuid = str(uuid.uuid4())
print "I am the script named %s" % uuid

#
# Creation
#
client = s.client(uuid, "simple ping script",
    s.Long("a", optional=True).inout(), s.String("b", optional=True).inout())
print "Session", client.getSession()

#
# Various diagnostics
#
import sys
from pprint import pprint
print "PATH:"
pprint(sys.path)

print "CONFIG"
f = open("config","r")
print "".join(f.readlines())
f.close()

from ode.rtypes import *

import Ice
ic = Ice.initialize(["--Ice.Plugin.IceSSL=IceSSL:createIceSSL"])
print ic.getProperties().getPropertiesForPrefix("Ice")
print ic.getProperties().getPropertiesForPrefix("ode")

#
# Echo'ing input to output
#
keys = client.getInputKeys()
print "Keys found:"
print keys
for key in keys:
    client.setOutput(key, client.getInput(key))

#
# Env
#
print "This was my environment:"
for k,v in os.environ.items():
    print "%s => %s" %(k,v)

sys.stderr.write("Oh, and this is stderr.");

"""

class CallbackI(ode.grid.ProcessCallback):

    def __init__(self):
        self.finish = []
        self.cancel = []
        self.kill = []

    def processFinished(self, rv, current=True):
        self.finish.append(rv)

    def processCancelled(self, rv, current=True):
        self.cancel.append(rv)

    def processKilled(self, rv, current=True):
        self.kill.append(rv)


class TestPing(ITest):

    """
    Tests which use the trivial script defined by PINGFILE to
    test the scripts API.
    """

    #
    # Helper methods
    #

    def _getProcessor(self):
        scripts = self.root.getSession().getScriptService()
        id = scripts.uploadOfficialScript(
            "/tests/ping_py/%s.py" % self.uuid(), PINGFILE)
        j = ode.model.ScriptJobI()
        j.linkOriginalFile(ode.model.OriginalFileI(rlong(id), False))
        p = self.client.sf.sharedResources().acquireProcessor(j, 100)
        return p

    def _checkstd(self, output, which):
        rfile = output.val[which]
        ofile = rfile.val
        assert ofile

        tmppath = create_path("pingtest")
        try:
            self.client.download(ofile, str(tmppath))
            assert os.path.getsize(str(tmppath))
            return tmppath.text()
        finally:
            remove_path(tmppath)

    def assertIO(self, output):
        stdout = self._checkstd(output, "stdout")
        stderr = self._checkstd(output, "stderr")
        return stdout, stderr

    def assertSuccess(self, processor, process):
        wait(self.client, process)
        rc = process.poll()
        output = processor.getResults(process)
        stdout, stderr = self.assertIO(output)
        if rc is None or rc.val != 0:
            assert False, "STDOUT:\n%s\nSTDERR:\n%s\n" % (stdout, stderr)
        return output

    #
    # Test methods
    #

    def testPingViaISCript(self):
        p = self._getProcessor()
        input = rmap({})
        input.val["a"] = rlong(2)
        input.val["b"] = rstring("d")
        process = p.execute(input)
        output = self.assertSuccess(p, process)
        assert output.val["a"].val == 2

    def testPingParametersViaISCript(self):
        p = self._getProcessor()
        params = p.params()
        assert params
        assert params.inputs["a"]
        assert params.inputs["b"]
        assert params.outputs["a"]
        assert params.outputs["b"]

    def testPingStdout(self):
        p = self._getProcessor()
        params = p.params()
        assert params.stdoutFormat

        process = p.execute(rmap({}))
        self.assertSuccess(p, process)

    @pytest.mark.broken(ticket="11494")
    def testProcessCallback(self):

        callback = CallbackI()

        id = self.client.getCommunicator().stringToIdentity(str(uuid.uuid4()))
        cb = self.client.getAdapter().add(callback, id)
        cb = ode.grid.ProcessCallbackPrx.uncheckedCast(cb)
        p = self._getProcessor()
        params = p.params()
        assert params.stdoutFormat

        process = p.execute(rmap({}))
        process.registerCallback(cb)
        self.assertSuccess(p, process)

        assert len(callback.finish) > 0

    def testProcessShutdown(self):
        p = self._getProcessor()
        process = p.execute(rmap({}))
        process.shutdown()

        p.getResults(process)

        # Line above was: output = p.getResults(process)
        # Probably doesn't have IO since killed
        # self.assertIO(output)

    def testProcessShutdownOneway(self):
        p = self._getProcessor()
        process = p.execute(rmap({}))
        oneway = ode.grid.ProcessPrx.uncheckedCast(process.ice_oneway())
        oneway.shutdown()
        # Depending on what's faster this may or may not throw
        try:
            p.getResults(process)
            assert process.poll()
            p.getResults(process)
        except ode.ServerError:
            pass

        # Line above was: output = p.getResults(process)
        # Probably doesn't have IO since killed
        # self.assertIO(output)

    def testProcessorGetResultsBeforeFinished(self):
        p = self._getProcessor()
        process = p.execute(None)
        with pytest.raises(ode.ServerError):
            p.getResults(process)
        self.assertSuccess(p, process)

    #
    # Execution-less tests
    #

    def testProcessorExpires(self):
        p = self._getProcessor()
        assert p.expires() > 0

    def testProcessorGetJob(self):
        p = self._getProcessor()
        assert p.getJob()

    def testProcessorStop(self):
        p = self._getProcessor()
        p.execute(rmap({}))
        p.stop()

    def testProcessorDetach(self):
        p = self._getProcessor()
        p.execute(rmap({}))
        p.setDetach(True)
        p.stop()
