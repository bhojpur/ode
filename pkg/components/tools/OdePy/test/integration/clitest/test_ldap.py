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

from ode.testlib.cli import CLITest
from ode.cli import NonZeroReturnCode
from ode.plugins.ldap import LdapControl

import pytest

subcommands = ["active", "discover", "create", "getdn", "setdn"]
# 'list' command is available to non-admins


class TestLDAP(CLITest):

    def setup_method(self, method):
        super(TestLDAP, self).setup_method(method)
        self.cli.register("ldap", LdapControl, "TEST")
        self.args += ["ldap"]

    @pytest.mark.parametrize('subcommand', subcommands)
    def testAdminOnly(self, subcommand, capsys):
        """Test ldap active subcommand"""

        self.args += [subcommand]
        if subcommand in ["create"]:
            self.args += [self.uuid()]
        elif subcommand in ["setdn"]:
            self.args += ["--user-name", self.uuid(), "true"]
        elif subcommand in ["getdn"]:
            self.args += ["--user-name", self.uuid()]

        with pytest.raises(NonZeroReturnCode):
            self.cli.invoke(self.args, strict=True)
        out, err = capsys.readouterr()
        assert err.endswith("SecurityViolation: Admins only!\n")
