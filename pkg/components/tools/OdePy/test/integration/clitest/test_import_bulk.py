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

plugin = __import__('ode.plugins.import', globals(), locals(),
                    ['ImportControl'], -1)
ImportControl = plugin.ImportControl

from ode.cli import NonZeroReturnCode
from ode.testlib.cli import CLITest
from ode.plugins.sessions import SessionsControl
import sys
import re
import subprocess


class TestImportBulk(CLITest):

    def setup_method(self, method):
        super(TestImportBulk, self).setup_method(method)
        self.cli.register("sessions", SessionsControl, "TEST")
        self.cli.register("import", plugin.ImportControl, "TEST")

# These methods should be refactored up a level

    def do_import(self, capfd, strip_logs=True):
        try:

            # Discard previous out/err
            # left over from previous test.
            capfd.readouterr()

            self.cli.invoke(self.args, strict=True)
            o, e = capfd.readouterr()
            if strip_logs:
                clean_o = ""
                for line in o.splitlines(True):
                    if not (re.search(r'^\d\d:\d\d:\d\d.*', line)
                            or re.search(r'.*\w\.\w.*', line)):
                        clean_o += line
                o = clean_o
        except NonZeroReturnCode:
            o, e = capfd.readouterr()
            print("O" * 40)
            print(o)
            print('E' * 40)
            print(e)
            raise
        return o, e

    def get_object(self, err, obj_type, query=None):
        """Retrieve the created object by parsing the stderr output"""
        pattern = re.compile('^%s:(?P<id>\d+)$' % obj_type)
        for line in reversed(err.split('\n')):
            match = re.match(pattern, line)
            if match:
                break
        obj_id = int(match.group('id'))
        return self.assert_object(obj_type, obj_id, query=query)

    def assert_object(self, obj_type, obj_id, query=None):
        if not query:
            query = self.query
        obj = query.get(obj_type, obj_id,
                        {"ode.group": "-1"})
        assert obj
        assert obj.id.val == obj_id
        return obj

    def get_objects(self, err, obj_type, query=None):
        """Retrieve the created objects by parsing the stderr output"""
        pattern = re.compile('^%s:(?P<idstring>\d+)$' % obj_type)
        objs = []
        for line in reversed(err.split('\n')):
            match = re.match(pattern, line)
            if match:
                ids = match.group('idstring').split(',')
                for obj_id in ids:
                    obj = self.assert_object(obj_type,
                                             int(obj_id), query=query)
                    objs.append(obj)
        return objs

# TESTS

    def testBulk(self, tmpdir, capfd, monkeypatch):
        """Test Bulk import"""

        fakefile = tmpdir.join("test.fake")
        fakefile.write('')

        yml = tmpdir.join("test.yml")
        yml.write("""---
dry_run: "script%s.sh"
path: test.tsv
        """)

        tsv = tmpdir.join("test.tsv")
        tsv.write("test.fake")

        script = tmpdir.join("script1.sh")

        self.args += ["import", "-f", "--bulk", str(yml),
                      "--clientdir", self.ode_dist / "lib" / "client"]

        bin = self.ode_dist / "bin" / "ode"
        monkeypatch.setattr(sys, "argv", [str(bin)])
        out, err = self.do_import(capfd)

        # At this point, script1.sh has been created
        assert script.exists()
        print(script.read())

        # But we need to login and then run the script
        monkeypatch.setenv("ODE_SESSIONDIR", tmpdir)
        host = self.root.getProperty("ode.host")
        port = self.root.getProperty("ode.port")
        self.args = ["sessions", "login"]
        self.args += ["-s", host, "-p", port]
        self.args += ["-u", self.user.odeName.val]
        self.args += ["-w", self.user.odeName.val]
        self.cli.invoke(self.args, strict=True)
        popen = subprocess.Popen(
            ["bash", str(script)],
            cwd=str(tmpdir),
            stdout=subprocess.PIPE)
        out = popen.communicate()[0]
        rcode = popen.poll()
        assert rcode == 0
        assert self.get_object(out, 'Image')
