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
    Specifically test the parseInputs functionality
    which all scripts might want to use.
"""

from ode.testlib import ITest
import ode
import ode.processor
import ode.scripts
from ode.rtypes import rint

SENDFILE = """
#!/usr/bin/env python3

# Setup to run as an integration test
import os
import sys

import ode.scripts as s
import ode.util.script_utils as su

client = s.client("test_inputs.py",
    s.Int("a"),
    s.String("b", default="c"),
    s.String("d"),
)

for method, inputs in (
        ('arg', client.getInputs(True)),
        ('kw', client.getInputs(unwrap=True)),
        ('util', su.parseInputs(client))
    ):

    # The params object contains all the metadata
    # passed to the constructor above.
    defined = client.params.inputs.keys()
    if set(["a", "b", "d"]) != set(defined):
        raise Exception("Failed!")

    a = inputs["a"]
    if not isinstance(a, (int, long)):
        raise Exception("Failed!")

    b = inputs.get("b")
    if b != "c":
        raise Exception("Failed!")

    d = inputs.get("d")
    if d is not None:
        raise Exception("Failed!")
"""

class TestInputs(ITest):

    def output(self, root, results, which):
        out = results.get(which, None)
        if out:
            rfs = root.sf.createRawFileStore()
            try:
                rfs.setFileId(out.val.id.val)
                text = rfs.read(0, rfs.size())
                if text.strip():
                    print("===", which, "===")
                    print(text)
            finally:
                rfs.close()

    def testInputs(self):
        import logging
        logging.basicConfig(level=10)
        root_client = self.new_client(system=True)
        scripts = root_client.sf.getScriptService()
        id = scripts.uploadScript(
            "/tests/inputs_py/%s.py" % self.uuid(), SENDFILE)
        input = {"a": rint(100)}
        impl = ode.processor.usermode_processor(root_client)
        try:
            process = scripts.runScript(id, input, None)
            cb = ode.scripts.ProcessCallbackI(root_client, process)
            try:
                count = 100
                while cb.block(2000):
                    count -= 1
                    assert count != 0
                rc = process.poll()
                results = process.getResults(0)
                self.output(root_client, results, "stdout")
                self.output(root_client, results, "stderr")
                assert rc is not None
                assert rc.val == 0
            finally:
                cb.close()
        finally:
            impl.cleanup()
