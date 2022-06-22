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
   Tests of the ode.client constructors
"""

import os
from ode.testlib import ITest
import ode
import Ice

here = os.path.abspath(os.path.dirname(__file__))

class TestClientConstructors(ITest):

    def setup_method(self, method):
        c = ode.client(pmap=['--Ice.Config='+(os.environ.get("ICE_CONFIG"))])
        try:
            self.host = c.ic.getProperties().getProperty('ode.host')
            self.port = int(c.ic.getProperties().getProperty('ode.port'))
            self.rootpasswd = c.ic.getProperties().getProperty(
                'ode.rootpass')
        finally:
            c.__del__()

    def testHostConstructor(self):
        c = ode.client(host=self.host, port=self.port)
        try:
            c.createSession("root", self.rootpasswd)
            c.closeSession()
            c.createSession("root", self.rootpasswd)
        except:
            c.__del__()

    def testEmptyInitializationDataConstructor(self):
        id = Ice.InitializationData()
        # With no argument id.properties is empty
        id.properties = Ice.createProperties()
        id.properties.setProperty("ode.host", self.host)
        id.properties.setProperty("ode.port", str(self.port))
        id.properties.setProperty("ode.user", "root")
        id.properties.setProperty("ode.pass", self.rootpasswd)
        c = ode.client(id=id)
        try:
            c.createSession()
            c.closeSession()
            c.createSession()
            c.closeSession()
        finally:
            c.__del__()

    def testInitializationDataConstructor(self):
        id = Ice.InitializationData()
        id.properties = Ice.createProperties([])
        id.properties.setProperty("ode.user", "root")
        id.properties.setProperty("ode.pass", self.rootpasswd)
        c = ode.client(id=id)
        try:
            c.createSession()
            c.closeSession()
            c.createSession()
            c.closeSession()
        finally:
            c.__del__()

    def testMainArgsConstructor(self):
        args = ["--ode.host="+self.host,
                "--ode.user=root", "--ode.pass=" + self.rootpasswd]
        c = ode.client(args)
        try:
            c.createSession()
            c.closeSession()
            c.createSession()
            c.closeSession()
        finally:
            c.__del__()

    def testMapConstructor(self):
        p = {}
        p["ode.host"] = self.host
        p["ode.user"] = "root"
        p["ode.pass"] = self.rootpasswd
        c = ode.client(pmap=p)
        try:
            c.createSession()
            c.closeSession()
            c.createSession()
            c.closeSession()
        finally:
            c.__del__()

    def testMainArgsGetsIcePrefix(self):
        args = ["--ode.host="+self.host,
                "--ode.user=root", "--ode.pass=" + self.rootpasswd]
        args.append("--Ice.MessageSizeMax=10")
        c = ode.client(args)
        try:
            c.createSession()
            assert "10" == c.getProperty("Ice.MessageSizeMax")
            c.closeSession()
        finally:
            c.__del__()

    def testMainArgsGetsIceConfig(self):
        cfg = os.path.join(here, "client_ctors.cfg")
        if not os.path.exists(cfg):
            assert False, cfg + " does not exist"
        args = ["--Ice.Config=" + cfg, "--ode.host=unimportant"]
        c = ode.client(args)
        try:
            assert "true" == c.getProperty("in.ice.config")
            # c.createSession()
            # c.closeSession()
        finally:
            c.__del__()

    def testTwoDifferentHosts(self):
        try:
            c1 = ode.client(host="foo")
            c1.createSession()
            c1.closeSession()
        except:
            print("foo failed appropriately")

        c2 = ode.client(host=self.host, port=self.port)
        try:
            user = self.new_user()
            c2.createSession(user.odeName.val, user.odeName.val)
            c2.closeSession()
        finally:
            c2.__del__()

    def testPorts(self):
        c = ode.client("localhost", 1111)
        try:
            assert "1111" == c.ic.getProperties().getProperty("ode.port")
        finally:
            c.__del__()

        c = ode.client("localhost", ["--ode.port=2222"])
        try:
            assert "2222" == c.ic.getProperties().getProperty("ode.port")
        finally:
            c.__del__()
        # c = ode.client("localhost")
        # assert str(ode.constants.GLACIER2PORT) ==\
        #     c.ic.getProperties().getProperty("ode.port")

    def testBlockSize(self):
        c = ode.client("localhost")
        try:
            assert 5000000 == c.getDefaultBlockSize()
        finally:
            c.__del__()
        c = ode.client("localhost", ["--ode.block_size=1000000"])
        try:
            assert 1000000 == c.getDefaultBlockSize()
        finally:
            c.__del__()

    def testPythonCtorRepair(self):
        # c = ode.client(self.host, ode.constants.GLACIER2PORT)
        c = ode.client(self.host, self.port)
        try:
            c.createSession("root", self.rootpasswd)
            c.closeSession()
        finally:
            c.__del__()
