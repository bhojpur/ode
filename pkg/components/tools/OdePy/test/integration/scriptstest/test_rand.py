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
import ode
import ode.processor
import ode.scripts
from ode.rtypes import rlong

SENDFILE = """
#<script>
#   <name>
#       rand
#   </name>
#   <description>
#	get rand numbers from matlab instance.
#   </description>
#   <parameters>
#       <variable name="inputParam" type="type" optional="true">
#           <description>
#               This variable can have a name, type and be optional.
#           </description>
#       </variable>
#   </parameters>
#   <return>
#       <variable name="outputParam" type="type">
#           <description>
#           </description>
#       </variable>
#   </return>
#</script>
#!/usr/bin/env python3

import ode, ode.scripts as s
import random

client = s.client(
    "rand.py", "get Random", s.Long("x").inout(), s.Long("y").inout())
client.createSession()
print "Session"
print client.getSession()
keys = client.getInputKeys()
print "Keys found:"
print keys
for key in keys:
    client.setOutput(key, client.getInput(key))

x = client.getInput("x").val
y  = client.getInput("y").val
val = random.randint(x,y);
print val
"""

class TestRand(ITest):

    def testRand(self):
        root_client = self.new_client(system=True)
        scripts = root_client.sf.getScriptService()
        id = scripts.uploadScript(
            "/tests/rand_py/%s.py" % self.uuid(), SENDFILE)
        input = {"x": rlong(3), "y": rlong(3)}
        impl = ode.processor.usermode_processor(root_client)
        try:
            process = scripts.runScript(id, input, None)
            cb = ode.scripts.ProcessCallbackI(root_client, process)
            cb.block(2000)  # ms
            cb.close()
            try:
                output = process.getResults(0)
                assert output["x"].val == 3
            except KeyError:
                print("Key is not in returned dictionary. Is this a fail?")
        finally:
            impl.cleanup()
