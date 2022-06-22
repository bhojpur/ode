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
Test of the ode/plugins/tx.py module
"""

import pytest
from ode.api import IQueryPrx
from ode.api import IUpdatePrx
from ode.api import ServiceFactoryPrx
from ode.cli import CLI
from ode.clients import BaseClient
from ode.model import ProjectI
from ode.plugins.obj import NewObjectTxAction
from ode.plugins.obj import TxCmd
from ode.plugins.obj import ObjControl
from ode.plugins.obj import TxState
from extras.mox import IgnoreArg
from extras.mox import Mox

class MockCLI(CLI):

    def conn(self, *args, **kwargs):
        return self.get_client()

    def close(self, *args, **kwargs):
        pass

    def out(self, out):
        if hasattr(self, "_out"):
            self._out.append(out)
        else:
            self._out = [out]

class TxBase(object):

    def setup_method(self, method):
        self.mox = Mox()
        self.client = self.mox.CreateMock(BaseClient)
        self.sf = self.mox.CreateMock(ServiceFactoryPrx)
        self.query = self.mox.CreateMock(IQueryPrx)
        self.update = self.mox.CreateMock(IUpdatePrx)
        self.client.sf = self.sf
        self.cli = MockCLI()
        self.cli.set_client(self.client)
        self.cli.set("tx.state", TxState(self.cli))

    def teardown_method(self, method):
        self.mox.UnsetStubs()
        self.mox.VerifyAll()

    def queries(self, obj):
        self.sf.getQueryService().AndReturn(self.query)
        self.query.get(IgnoreArg(), IgnoreArg(), IgnoreArg()).AndReturn(obj)

    def saves(self, obj):
        self.sf.getUpdateService().AndReturn(self.update)
        self.update.saveAndReturnObject(IgnoreArg()).AndReturn(obj)


class TestNewObjectTxAction(TxBase):

    def test_unknown_class(self):
        self.saves(ProjectI(1, False))
        self.mox.ReplayAll()
        state = TxState(self.cli)
        cmd = TxCmd(state, arg_list=["new", "Project", "name=foo"])
        action = NewObjectTxAction(state, cmd)
        action.go(self.cli, None)


class TestObjControl(TxBase):

    def setup_method(self, method):
        super(TestObjControl, self).setup_method(method)
        self.cli.register("obj", ObjControl, "TEST")
        self.args = ["obj"]

    def test_simple_new_usage(self):
        self.saves(ProjectI(1, False))
        self.mox.ReplayAll()
        self.cli.invoke("obj new Project name=foo", strict=True)
        assert self.cli._out == ["Project:1"]

    def test_simple_update_usage(self):
        self.queries(ProjectI(1, True))
        self.saves(ProjectI(1, False))
        self.mox.ReplayAll()
        self.cli.invoke(("obj update Project:1 name=bar "
                        "description=loooong"), strict=True)
        assert self.cli._out == ["Project:1"]

    def testHelp(self):
        self.args += ["-h"]
        self.cli.invoke(self.args, strict=True)

    @pytest.mark.parametrize('subcommand', ObjControl().get_subcommands())
    def testSubcommandHelp(self, subcommand):
        self.args += [subcommand, "-h"]
        self.cli.invoke(self.args, strict=True)
