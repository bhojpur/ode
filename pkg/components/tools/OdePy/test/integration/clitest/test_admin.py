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

import pytest
import os

from ode.testlib.cli import CLITest
from ode.testlib.cli import RootCLITest
import ode.plugins.admin
from ode.cli import NonZeroReturnCode
from path import path
from ode.util.upgrade_check import UpgradeCheck

ODEDIR = os.getenv('ODEDIR', None)

def createUpgradeCheckClass(version):
    class MockUpgradeCheck(UpgradeCheck):
        def __init__(self, agent, url=None):
            if url:
                super(MockUpgradeCheck, self).__init__(
                    agent, url, version=version)
            else:
                super(MockUpgradeCheck, self).__init__(agent, version=version)

    return MockUpgradeCheck


class TestAdmin(RootCLITest):

    def setup_method(self, method):
        super(TestAdmin, self).setup_method(method)
        self.cli.register("admin", ode.plugins.admin.AdminControl, "TEST")
        # Bhojpur ODE needs the etc/grid directory
        self.cli.dir = (path(__file__).dirname()
                        / ".." / ".." / ".." / ".." / ".." / ".." / "dist")
        self.args += ["admin"]

    def go(self):
        self.cli.invoke(self.args, strict=True)

    def test_checkupgrade0(self, monkeypatch):
        monkeypatch.setattr(ode.plugins.prefs, "UpgradeCheck",
                            createUpgradeCheckClass("999999999.0.0"))
        self.args.append("checkupgrade")
        self.go()

    def test_checkupgrade1(self, monkeypatch):
        monkeypatch.setattr(ode.plugins.prefs, "UpgradeCheck",
                            createUpgradeCheckClass("0.0.0"))
        self.args.append("checkupgrade")
        with pytest.raises(NonZeroReturnCode) as exc:
            self.go()
        assert exc.value.rv == 1

    def test_log(self):
        import uuid
        test = str(uuid.uuid4())
        self.args += ["log"]
        self.args += ["ScriptRepo"]
        self.args += [test]
        self.cli.invoke(self.args, strict=True)

        log_file = ODEDIR + "/var/log/ODE-0.log"
        import fileinput
        found = False
        for line in fileinput.input(log_file):
            if line.__contains__(test):
                found = True
                break
        fileinput.close()
        assert found


class TestAdminRestrictedAdmin(CLITest):

    # make the user in this test a member of system group
    DEFAULT_SYSTEM = True
    # make the new member of system group to a Restricted
    # Admin with no privileges
    DEFAULT_PRIVILEGES = ()

    def setup_method(self, method):
        super(TestAdminRestrictedAdmin, self).setup_method(method)
        self.cli.register("admin", ode.plugins.admin.AdminControl, "TEST")
        # Bhojpur ODE needs the etc/grid directory
        self.cli.dir = (path(__file__).dirname()
                        / ".." / ".." / ".." / ".." / ".." / ".." / "dist")
        self.args += ["admin"]

    def test_log(self):
        import uuid
        test = str(uuid.uuid4())
        self.args += ["log"]
        self.args += ["ScriptRepo"]
        self.args += [test]
        self.cli.invoke(self.args, strict=True)

        log_file = ODEDIR + "/var/log/ODE-0.log"
        import fileinput
        found = False
        for line in fileinput.input(log_file):
            if line.__contains__(test):
                found = True
                break
        fileinput.close()
        assert found

    def test_checkupgrade0(self, monkeypatch):
        monkeypatch.setattr(ode.plugins.prefs, "UpgradeCheck",
                            createUpgradeCheckClass("999999999.0.0"))
        self.args.append("checkupgrade")
        self.cli.invoke(self.args, strict=True)

    def test_checkupgrade1(self, monkeypatch):
        monkeypatch.setattr(ode.plugins.prefs, "UpgradeCheck",
                            createUpgradeCheckClass("0.0.0"))
        self.args.append("checkupgrade")
        with pytest.raises(NonZeroReturnCode) as exc:
            self.cli.invoke(self.args, strict=True)
        assert exc.value.rv == 1
